package com.yunda.sb.pointcheck.manager;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.pointcheck.entity.PointCheck;
import com.yunda.sb.pointcheck.entity.PointCheckCatalog;
import com.yunda.sb.pointcheck.entity.PointCheckCatalogBean;
import com.yunda.sb.pointcheck.entity.PointCheckScope;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheckCatalog管理器，数据表：SBJX_POINT_CHECK_CATALOG
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "pointCheckCatalogManager")
public class PointCheckCatalogManager extends JXBaseManager<PointCheckCatalog, PointCheckCatalog> {

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** PointCheckScope业务类，数据表：SBJX_POINT_CHECK_SCOPE */
	@Resource
	private PointCheckScopeManager pointCheckScopeManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yunda.frame.core.BaseManager#validateUpdate(java.lang.Object)
	 */
	@Override
	public String[] validateUpdate(PointCheckCatalog t) {
		// 验证是否已经维护了设备点检范围
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
		if (null == epi) {
			return new String[] { String.format("设备主要信息数据异常，idx：%s", t.getEquipmentIdx()) };
		}
		// Modified by hetao on 2017-02-24
		// 针对同类型不同设备会存在不同点检范围的情况，优先使用设备编号查询点检范围，未找到结果时再根据设备类别编码进行查询
		List<PointCheckScope> pcsList = this.pointCheckScopeManager.getModelsByClassCode(epi.getEquipmentCode(), epi.getClassCode());
		if (null == pcsList || pcsList.isEmpty()) {
			return new String[] { String.format("%s（%s）未维护点检范围，请维护点检范围后重试！", epi.getEquipmentName(), epi.getEquipmentCode()) };
		}
		// 验证数据唯一性
		String hql = "From PointCheckCatalog Where equipmentIdx = ? And recordStatus = 0";
		PointCheckCatalog entity = (PointCheckCatalog) this.daoUtils.findSingle(hql, t.getEquipmentIdx());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { "不能添加重复的数据！" };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：查询，查询设备点检目录
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年10月25日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体
	 * @return 分页集合
	 */
	public Page<PointCheckCatalogBean> queryPageList(SearchEntity<PointCheckCatalogBean> searchEntity) {
		PointCheckCatalogBean entity = searchEntity.getEntity();

		String sql = SqlMapUtil.getSql(String.format("check%cpoint_check_catalog:queryPageList", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		// 查询条件 - 设备编码
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" And A.equipment_code like '%").append(entity.getEquipmentCode()).append("%'");
		}
		if (!StringUtil.isNullOrBlank(entity.getClassName())) {
			sb.append(" And A.class_name like '%").append(entity.getClassName()).append("%'");
		}
		if (!StringUtil.isNullOrBlank(entity.getEquipmentName())) {
			sb.append(" And A.equipment_name like '%").append(entity.getEquipmentName()).append("%'");
		}
		sql = sb.toString();
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
		Page<PointCheckCatalogBean> page = super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, PointCheckCatalogBean.class);
		// 查询设备“最近点检日期”
		List<PointCheckCatalogBean> list = page.getList();
		for (PointCheckCatalogBean t : list) {
			String hql = "From PointCheck Where recordStatus = 0 And equipmentIdx = ? And state = ? Order By checkDate Desc";
			PointCheck pc = (PointCheck) this.daoUtils.findSingle(hql, t.getEquipmentIdx(), PointCheck.STATE_YCL);
			if (null != pc) {
				t.setLatestCheckDate(pc.getCheckDate());
			}
		}
		return page;
	}

	/**
	 * <li>说明：设备点检统计-日度
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体
	 * @return 分页集合
	 */
	public Page<Sbdjtj> statisticsByDaily(SearchEntity<Sbdjtj> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("check%cpoint_check_catalog:statisticsByDaily", File.separatorChar));
		Sbdjtj entity = searchEntity.getEntity();
		if (null != entity.getCheckDate()) {
			sql = sql.replace("2017-03-24", DateUtil.yyyy_MM_dd.format(entity.getCheckDate()));
		}
		StringBuilder sb = new StringBuilder(sql);

		// 执行查询
		@SuppressWarnings("unchecked")
		List<Sbdjtj> list = this.daoUtils.executeSqlQueryEntity(sb.toString(), Sbdjtj.class);
		return new Page<PointCheckCatalogManager.Sbdjtj>(list);
	}

	/**
	 * <li>说明：保存点检目录明细，添加已选择的设备到点检目录中
	 * <li>创建人：卢轶
	 * <li>创建日期：2016年11月11日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param equipmentIds 设备信息idx主键数组
	 * @throws NoSuchFieldException
	 */
	public void save(String[] equipmentIds) throws NoSuchFieldException {
		if (null == equipmentIds || 0 >= equipmentIds.length) {
			return;
		}
		for (String equipmentIdx : equipmentIds) {
			this.insert(equipmentIdx);
		}
	}

	/**
	 * <li>说明：插入设备到设备点检目录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备信息idx主键
	 * @throws NoSuchFieldException
	 */
	public void insert(String equipmentIdx) throws NoSuchFieldException {
		PointCheckCatalog entity = new PointCheckCatalog(equipmentIdx);
		// 验证该设备是否已经存在于设备点检目录
		String[] errMsg = this.validateUpdate(entity);
		if (null != errMsg && 0 < errMsg.length) {
			throw new BusinessException(errMsg[0]);
		}
		this.saveOrUpdate(entity);
	}

	/**
	 * <li>说明：通过设备点检任务单初始化设备点检目录数据
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws NoSuchFieldException
	 */
	public void initDataByPointCheck() throws NoSuchFieldException {
		String hql = "Select Distinct equipmentIdx From PointCheck Where recordStatus = 0";
		List checkList = this.daoUtils.find(hql);
		if (null == checkList || checkList.isEmpty()) {
			return;
		}
		for (Object o : checkList) {
			try {
				this.insert(o.toString());
			} catch (BusinessException e) {
				continue;
			}
		}
	}

	/**
	 * <li>说明： 生成点检任务单后，自动将该设备添加到设备点检目录
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备idx主键
	 * @throws NoSuchFieldException
	 */
	public void insertByPointCheck(String equipmentIdx) throws NoSuchFieldException {
		String hql = "From PointCheckCatalog Where equipmentIdx = ?";
		Object o = this.daoUtils.findSingle(hql, equipmentIdx);
		if (null != o) {
			return;
		}
		PointCheckCatalog entity = new PointCheckCatalog(equipmentIdx);
		this.saveOrUpdate(entity);
	}

	/**
	 * <li>标题: 设备管理信息系统
	 * <li>说明: 设备点检统计-日度
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月24日
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * @author 信息系统事业部设备管理系统项目组
	 * @version 3.0.1
	 */
	@Entity
	public static final class Sbdjtj {
		/** 主键 */
		@Id
		private String idx;

		/** 设备名称 */
		@Column(name = "equipment_name")
		private String equipmentName;

		/** 设备编码 */
		@Column(name = "equipment_code")
		private String equipmentCode;

		/** 使用人 */
		@Column(name = "USE_PERSON")
		private String usePerson;

		/** 点检人名称 */
		@Column(name = "CHECK_EMP")
		private String checkEmp;

		/** 点检日期 */
		@Column(name = "CHECK_DATE")
		@Temporal(TemporalType.DATE)
		private Date checkDate;

		/** 设备运转时间，单位：小时，运转时间 = 点检结束时间 - 点检开始时间 */
		@Column(name = "RUNNING_TIME")
		private Float runningTime;

		/** 计算设备运转时间 */
		@Column(name = "cal_running_time")
		private Float calRunningTime;

		/** 处理状态 */
		@Column(name = "state")
		private String state;

		public String getIdx() {
			return idx;
		}

		public void setIdx(String idx) {
			this.idx = idx;
		}

		public String getEquipmentName() {
			return equipmentName;
		}

		public void setEquipmentName(String equipmentName) {
			this.equipmentName = equipmentName;
		}

		public String getEquipmentCode() {
			return equipmentCode;
		}

		public void setEquipmentCode(String equipmentCode) {
			this.equipmentCode = equipmentCode;
		}

		public String getUsePerson() {
			return usePerson;
		}

		public void setUsePerson(String usePerson) {
			this.usePerson = usePerson;
		}

		public String getCheckEmp() {
			return checkEmp;
		}

		public void setCheckEmp(String checkEmp) {
			this.checkEmp = checkEmp;
		}

		public Date getCheckDate() {
			return checkDate;
		}

		public void setCheckDate(Date checkDate) {
			this.checkDate = checkDate;
		}

		public Float getRunningTime() {
			return runningTime;
		}

		public void setRunningTime(Float runningTime) {
			this.runningTime = runningTime;
		}

		public Float getCalRunningTime() {
			return calRunningTime;
		}

		public void setCalRunningTime(Float calRunningTime) {
			this.calRunningTime = calRunningTime;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

	}

}
