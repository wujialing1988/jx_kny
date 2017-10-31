package com.yunda.sb.pointcheck.manager;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.pointcheck.entity.CalendarEventBean;
import com.yunda.sb.pointcheck.entity.PointCheck;
import com.yunda.sb.pointcheck.entity.PointCheckBean;
import com.yunda.sb.pointcheck.entity.PointCheckContent;
import com.yunda.sb.pointcheck.entity.PointCheckOmitBean;
import com.yunda.sb.pointcheck.entity.PointCheckStatistic;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheck管理器，数据表：SBJX_POINT_CHECK
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "pointCheckManager")
public class PointCheckManager extends JXBaseManager<PointCheck, PointCheck> {

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** PointCheckContent管理器, 数据表： SBJX_POINT_CHECK_CONTENT */
	@Resource
	private PointCheckContentManager pointCheckContentManager;

	/** PointCheckCatalog管理器，数据表：SBJX_POINT_CHECK_CATALOG */
	@Resource
	private PointCheckCatalogManager pointCheckCatalogManager;
	/** PointCheckOmit管理器，数据表：SBJX_POINT_CHECK_OMIT */
	
    @Resource
    private PointCheckOmitManager pointCheckOmitManager;

	/**
	 * <li>说明：启动设备
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param idx 点检单idx主键
	 * @throws NoSuchFieldException
	 */
	public void startUpEquipment(String idx) throws NoSuchFieldException {
		Date now = Calendar.getInstance().getTime();
		PointCheck entity = this.getModelById(idx);
		if (null == entity) {
			throw new BusinessException("数据异常，未查询到设备点检任务单！");
		}
		entity.setCheckTime(now);
		entity.setEquipmentState(PointCheck.EQUIPMENT_STATE_QD);
		this.saveOrUpdate(entity);
	}

	/**
	 * <li>说明：停止（暂停）设备
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：何涛
	 * <li>修改内容：2016年8月24日
	 * <li>修改日期：设备停止时，增加对手动输入的运转台时的时间计算
	 * @param idx 设备idx主键
	 * @return 设备点检单实例对象
	 * @throws NoSuchFieldException
	 */
	public PointCheck endUpEquipment(String idx) throws NoSuchFieldException {
		PointCheck entity = this.getModelById(idx);
		// 非第一次扫描直接返回实例对象，显示给用户已处理的点检单
		if (PointCheck.EQUIPMENT_STATE_TZ.equals(entity.getEquipmentState())) {
			return entity;
		}
		Date now = Calendar.getInstance().getTime();
		// 上一次点检开始（暂停）的时间
		long start = entity.getCheckTime().getTime();
		// 这一次点检结束（暂停）的时间
		long end = now.getTime();

		BigDecimal divide = BigDecimal.valueOf(end - start).divide(BigDecimal.valueOf(1000 * 60 * 60), 2, RoundingMode.HALF_UP);
		// 计算设备的运转时间（pad监测时间）
		Float calRunningTime = entity.getCalRunningTime();
		if (null == calRunningTime || 0 >= calRunningTime.floatValue()) {
			entity.setCalRunningTime(divide.floatValue());
		} else {
			entity.setCalRunningTime(divide.floatValue() + calRunningTime.floatValue());
		}
		// 计算设备的运转时间（手动输入时间）
		Float runningTime = entity.getRunningTime();
		if (null == runningTime || 0 >= runningTime.floatValue()) {
			entity.setRunningTime(divide.floatValue());
		} else {
			entity.setRunningTime(divide.floatValue() + runningTime.floatValue());
		}
		// 点击停止按钮更改设备状态为停止
		entity.setEquipmentState(PointCheck.EQUIPMENT_STATE_TZ);
		this.saveOrUpdate(entity);
		return entity;
	}

	/**
	 * <li>说明：处理点检单
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentCode 设备编码
	 * @return 设备点检单实例对象
	 * @throws NoSuchFieldException
	 */
	public PointCheck startUp(String equipmentCode) throws NoSuchFieldException {
		// 根据设备编码查询设备信息
		EquipmentPrimaryInfo equipment = this.equipmentPrimaryInfoManager.getModelByEquipmentCode(equipmentCode);
		if (null == equipment) {
			throw new BusinessException("未查询到编码为：" + equipmentCode + "的固资设备信息！");
		}

		// 获取指定设备下还未处理的设备点检任务单
		PointCheck pointCheck = this.getUndoPointCheck(equipment.getIdx());
		if (null == pointCheck) {
			// 获取今天的设备点检任务单
			pointCheck = getCurrentPointCheck(equipment.getIdx());
		}
		if (null != pointCheck) {
			pointCheck.setEquipmentInfo(equipment);
			// 根据当前点检单主键查询点检内容
			List<PointCheckContent> pointCheckContentList = this.pointCheckContentManager.getModelsByPointCheckIdx(pointCheck.getIdx());
			pointCheck.setCheckContentList(pointCheckContentList);
			return pointCheck;
		}

		// 生成（初始化）点检任务单
		return this.insert(equipment);
	}

	/**
	 * <li>说明：提交点检单
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 设备点检单实例对象
	 * @throws NoSuchFieldException 
	 */
	public void updateFinish(PointCheck t) throws NoSuchFieldException {
		// 验证设备的所有点检内容是否都已完成，验证不通过则不能进行提交
		if (!validateUpdateFinish(t)) {
			return;
		}
		PointCheck entity = this.getModelById(t.getIdx());
		// 如果设备是启动状态则计算时间
		if (PointCheck.EQUIPMENT_STATE_QD.equals(entity.getEquipmentState())) {
			Date now = Calendar.getInstance().getTime();
			Float runningTime = entity.getRunningTime();
			// 上一次点检开始（暂停）的时间
			long start = entity.getCheckTime().getTime();
			// 提交检点的时间
			long end = now.getTime();
			BigDecimal divide = BigDecimal.valueOf(end - start).divide(BigDecimal.valueOf(1000 * 60 * 60), 2, RoundingMode.HALF_UP);
			if (null == runningTime || 0 >= runningTime.floatValue()) {
				entity.setCalRunningTime(divide.floatValue());
			} else {
				entity.setCalRunningTime(divide.floatValue() + runningTime.floatValue());
			}
		}
		// 保存用户手动输入的设备运行时间
		entity.setRunningTime(t.getRunningTime());
		// 设置设备状态为停止
		entity.setEquipmentState(PointCheck.EQUIPMENT_STATE_TZ);
		// 设置点检单状态为已处理
		entity.setState(PointCheck.STATE_YCL);
		this.saveOrUpdate(entity);
		
		//Modified by hetao on 2017-05-18 对于当日扫描了设备进行了点检任务单生成，但是在当日未进行处理，而是第二天进行了点检任务的处理，这种情况需删除昨日的漏检记录
	    this.pointCheckOmitManager.deleteOmit(t.getEquipmentIdx(), t.getCheckDate());
	}

	/**
	 * <li>说明：根据设备主键和今日日期查询点检单
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备主键
	 * @return 设备点检单实例对象
	 */
	private PointCheck getCurrentPointCheck(String equipmentIdx) {
		String hql = "From PointCheck Where recordStatus = 0 And equipmentIdx = ? And checkDate = ?";
		return (PointCheck) this.daoUtils.findSingle(hql, equipmentIdx, Calendar.getInstance().getTime());
	}

	/**
	 * <li>说明：获取指定设备下还未处理的设备点检任务单
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备idx主键
	 * @return 设备点检任务单
	 */
	private PointCheck getUndoPointCheck(String equipmentIdx) {
		String hql = "From PointCheck Where recordStatus = 0 And equipmentIdx = ? And state = ?";
		return (PointCheck) this.daoUtils.findSingle(hql, equipmentIdx, PointCheck.STATE_WCL);
	}

	/**
	 * <li>说明：生成（初始化）点检任务单
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentInfo 设备实例对象
	 * @return 设备点检单实例对象
	 * @throws NoSuchFieldException
	 */
	private PointCheck insert(EquipmentPrimaryInfo equipmentInfo) throws NoSuchFieldException {
		OmEmployee userData = SystemContext.getOmEmployee();
		PointCheck pointCheck = new PointCheck();
		// 取当前系统操作员为点检人
		pointCheck.setCheckEmp(userData.getEmpname());
		pointCheck.setCheckEmpId(userData.getEmpid() + "");
		pointCheck.setEquipmentInfo(equipmentInfo);
		pointCheck.setEquipmentIdx(equipmentInfo.getIdx());
		pointCheck.setCheckDate(Calendar.getInstance().getTime());
		pointCheck.setState(PointCheck.STATE_WCL);
		pointCheck.setEquipmentState(PointCheck.EQUIPMENT_STATE_TZ);
		// 保存设备点检任务单
		this.saveOrUpdate(pointCheck);

		List<PointCheckContent> checkContentList = this.pointCheckContentManager.insert(equipmentInfo, pointCheck);
		// 设置设备点检内容 
		pointCheck.setCheckContentList(checkContentList);

		// Added by hetao on 2016-11-15 生成点检任务单后，自动将该设备添加到设备点检目录
		this.pointCheckCatalogManager.insertByPointCheck(pointCheck.getEquipmentIdx());
		return pointCheck;
	}

	/**
	 * <li>说明：验证设备的所有点检内容是否都已完成
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 设备点检单实例对象
	 * @return true：设备的所有点检内容是否都已完成，其他：抛出未点检内容异常
	 */
	private boolean validateUpdateFinish(PointCheck t) {
		List<PointCheckContent> list = this.pointCheckContentManager.getModelsByPointCheckIdx(t.getIdx());
		for (PointCheckContent content : list) {
			if (StringUtil.isNullOrBlank(content.getTechnologyStateFlag())) {
				throw new BusinessException(content.getCheckContent() + "还未进行点检！");
			}
		}
		return true;
	}

	/**
	 * <li>说明：设备点检统计分页查询，按月统计每台设备点检发生次数
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月11日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体对象
	 * @return 设备点检统计分页集合
	 */
	public Page<PointCheckStatistic> queryPage2Statistic(SearchEntity<PointCheckStatistic> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("check%cpoint_check:queryPage2statistic", File.separatorChar));

		PointCheckStatistic entity = searchEntity.getEntity();

		// 查询条件 - 年月
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, entity.getYear());
		calendar.set(Calendar.MONTH, entity.getMonth() - 1);
		calendar.getTime();
		sql = sql.replaceAll("2016-11", new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
		StringBuilder sb = new StringBuilder(sql);

		// 查询条件 - 设备名称或编码
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND (");
			sb.append(" EQUIPMENT_CODE Like '%").append(entity.getEquipmentCode()).append("%'");
			sb.append(" OR");
			sb.append(" EQUIPMENT_NAME Like '%").append(entity.getEquipmentCode()).append("%'");
			sb.append(" )");
		}
		sb.append(" ORDER BY CHECK_COUNT DESC");

		sql = sb.toString();
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
		return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, PointCheckStatistic.class);
	}

	/**
	 * <li>说明：设备点检任务单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2016年12月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体对象
	 * @return 设备点检任务单分页集合
	 */
	public Page<PointCheckBean> queryPageList(SearchEntity<PointCheckBean> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("check%cpoint_check:queryPageList", File.separatorChar));

		PointCheckBean entity = searchEntity.getEntity();

		// 查询条件 - 年月
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, entity.getYear());
		calendar.set(Calendar.MONTH, entity.getMonth() - 1);
		calendar.getTime();
		sql = sql.replace("2016-11", new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
		StringBuilder sb = new StringBuilder(sql);

		// 查询条件 - 设备名称或编码
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND (");
			sb.append(" EQUIPMENT_CODE Like '%").append(entity.getEquipmentCode()).append("%'");
			sb.append(" OR");
			sb.append(" EQUIPMENT_NAME Like '%").append(entity.getEquipmentCode()).append("%'");
			sb.append(" )");
		}

		// 查询条件 - 处理状态
		if (!StringUtil.isNullOrBlank(entity.getState())) {
			sb.append("AND T.STATE IN('").append(entity.getState().replace(",", "','")).append("')");
		}

		sb.append(" ORDER BY T.CHECK_TIME DESC");

		sql = sb.toString();
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
		return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, PointCheckBean.class);
	}

	/**
	 * <li>说明：一键处理设备点检任务单
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月1日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param idx 设备点检任务单idx主键
	 * @param runningTime 设备运转时间，单位：小时
	 * @throws NoSuchFieldException 
	 */
	public void updateFinishedByOneKey(String idx, Float runningTime) throws NoSuchFieldException {
		PointCheck entity = this.getModelById(idx);
		// 保存用户手动输入的设备运行时间
		if (null != runningTime) {
			entity.setRunningTime(runningTime);
		}
		// 设置设备状态为停止
		entity.setEquipmentState(PointCheck.EQUIPMENT_STATE_TZ);
		// 设置点检单状态为已处理
		entity.setState(PointCheck.STATE_YCL);
		this.saveOrUpdate(entity);

		// 设置所有未处理的点检内容为：良好
		String hql = "From PointCheckContent Where recordStatus = 0 And pointCheckIdx = ? And (technologyStateFlag Is Null Or CHAR_LENGTH(technologyStateFlag) <= 0)";
		@SuppressWarnings("unchecked")
		List<PointCheckContent> list = this.daoUtils.find(hql, idx);
		for (PointCheckContent content : list) {
			content.setTechnologyStateFlag(PointCheckContent.STATE_FLAG_LH);
		}
		this.pointCheckContentManager.saveOrUpdate(list);
	}

	/**
	 * <li>说明：分页查询，统计设备在当月内漏检次数
	 * <li>创建人：何东
	 * <li>创建日期：2017年1月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param  searchEntity查询条件实体
	 * @return 点检漏检统计查询实体集合 
	 */
	public Page<PointCheckOmitBean> queryOmitCount(SearchEntity<PointCheckOmitBean> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("check%cpoint_check_omit:queryOmitCount", File.separatorChar));
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT FROM (" + sql.toString() + ") T";
		return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, PointCheckOmitBean.class);
	}

	/**
	* <li>说明： 设备点检统计，以日历方式显示点检情况
	* <li>创建人： 黄杨
	* <li>创建日期：2017-5-18
	* <li>修改人：
	* <li>修改内容：
	* <li>修改日期：
	*@param equipmentCode 设备编号
	*@param queryDate 查询年月，如：2017-04
	*@return 设备漏检日期集合
	*/
	@SuppressWarnings("unchecked")
	public List<CalendarEventBean> queryToStatistic(String equipmentCode, String queryDate) {
		String sql = SqlMapUtil.getSql(String.format("check%cpoint_check:queryToStatistic", File.separatorChar));
		sql = sql.replace("4650120003", equipmentCode).replace("2017-05", queryDate);
		return this.daoUtils.executeSqlQueryEntity(sql, CalendarEventBean.class);

	}
}
