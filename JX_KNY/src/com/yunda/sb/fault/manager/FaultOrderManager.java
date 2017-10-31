package com.yunda.sb.fault.manager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.fault.entity.FaultOrder;
import com.yunda.sb.fault.entity.FaultOrderBean;
import com.yunda.sb.fault.entity.FaultStatisM;
import com.yunda.sb.pointcheck.manager.EquipmentShutdownManager;


/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：FaultOrder业务类，数据表：E_FAULT_ORDER
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "faultOrderManager")
public class FaultOrderManager extends JXBaseManager<FaultOrder, FaultOrder> {

//	/** SystemMessage管理器，数据表：T_SYSTEM_MESSAGE */
//	@Resource
//	private SystemMessageManager systemMessageManager;

	/** 附件管理 */
	@Resource
	private AttachmentManager attachmentManager;

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** EquipmentShutdownManager，数据表：SBJX_EQUIPMENT_SHUTDOWN */
	@Resource
	private EquipmentShutdownManager equipmentShutdownManager;

	/**
	 * <li>说明：实体保存前的字段唯一性验证
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 故障提票实体对象
	 * @return 验证消息
	 */
	@Override
	public String[] validateUpdate(FaultOrder t) {
		if (null == t.getFaultOrderNo()) {
			return super.validateUpdate(t);
		}
		String hql = "From FaultOrder Where recordStatus = 0 And faultOrderNo = ?";
		FaultOrder entity = (FaultOrder) this.daoUtils.findSingle(hql, t.getFaultOrderNo());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { "提票单编号：" + t.getFaultOrderNo() + "已经存在，请重新输入！" };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：重写保存方法，在新增故障提票时，如果页面未传提票人，则取当前系统操作用户
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月20日
	 * <li>修改人：何涛
	 * <li>修改内容：2016年9月20
	 * <li>修改日期：增加更新验证，如果提票单已经被派工，则不允许修改
	 * @param t 故障提票实体对象
	 * @return 验证消息
	 */
	@Override
	public void saveOrUpdate(FaultOrder t) throws NoSuchFieldException {
		// 如果提票单已经被派工，则不允许修改
		if (!StringUtil.isNullOrBlank(t.getIdx())) {
			String hql = "From FaultOrder Where recordStatus = 0 And idx = ? And state <> ?";
			Object o = this.daoUtils.findSingle(hql, t.getIdx(), FaultOrder.STATE_XJ);
			if (null != o) {
				throw new BusinessException(String.format("当前操作的故障提票已被派工，不能再次编辑，请刷新后重试！", t.getFaultOrderNo()));
			}
		}
		// 如果提票单号字段为空，则默认设置一个唯一编码
		if (StringUtil.isNullOrBlank(t.getFaultOrderNo())) {
			t.setFaultOrderNo(this.createFaultOrderNo(null));
		}
		// 如果状态字段为空，则默认该操作未新增
		if (null == t.getState()) {
			t.setState(FaultOrder.STATE_XJ);
		}
		// 如果是新增，且提票人字段为空，则默认设置提票人为当前系统操作人
		if (FaultOrder.STATE_XJ.equals(t.getState()) && null == t.getSubmitEmpId()) {
			t.setSubmitEmpId(SystemContext.getOmEmployee().getEmpid());
			t.setSubmitEmp(SystemContext.getOmEmployee().getEmpname());
		}
		super.saveOrUpdate(t);
		// 故障提票创建时，推送故障提票调度派工信息
		this.pushMessage2Ddpg(t);
		// 故障提票创建时，向设备使用车间推送故障提票信息
		this.pushMessage2UseWorkshop(t);
	}

	/**
	 * <li>说明：自动生成提票单编号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月17日
	 * <li>修改人：何涛
	 * <li>修改内容：2016-06-20
	 * <li>修改日期：修改算法，确保生成的提票单号是一个未被使用的编码
	 * @param faultOrderNo 提票单号
	 * @return 提票单编号
	 */
	public String createFaultOrderNo(String faultOrderNo) {
		if (StringUtil.isNullOrBlank(faultOrderNo)) {
			Date date = Calendar.getInstance().getTime();
			String now = DateUtil.yyyy_MM_dd.format(date);
			StringBuilder sb = new StringBuilder("SELECT COUNT(*) From FaultOrder Where recordStatus = 0");
			sb.append(" And createTime > to_date('"+ now +"', 'yyyy-mm-dd')");
			int count = this.daoUtils.getCount(sb.toString());
			faultOrderNo = String.valueOf(Long.parseLong(now.replace("-", "")) * 10000 + 1 + count);
		} else {
			faultOrderNo = Long.parseLong(faultOrderNo) + 1 + "";
		}
		// 验证提票单号是否已经存在
		String hql = "From FaultOrder Where recordStatus = 0 And faultOrderNo = ?";
		FaultOrder entity = (FaultOrder) this.daoUtils.findSingle(hql, faultOrderNo);
		if (null == entity) {
			return faultOrderNo;
		}
		return createFaultOrderNo(faultOrderNo);
	}

	/**
	 * <li>说明：根据设备编码获取设备信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月17日
	 * <li>修改人：何涛
	 * <li>修改内容：增加参数-设备编码的空值验证
	 * <li>修改日期：2016年8月29日
	 * @param equipmentCode 设备编码
	 * @return 设备主要信息
	 */
	public EquipmentPrimaryInfo getEquipmentByCode(String equipmentCode) {
		if (StringUtil.isNullOrBlank(equipmentCode)) {
			throw new BusinessException("查询设备编号不能为空！");
		}
		String hql = "From EquipmentPrimaryInfo Where recordStatus = 0 And equipmentCode Like '%" + equipmentCode + "%'";
		int count = this.daoUtils.getCount(hql);
		if (count != 1) {
			throw new BusinessException("未查询到编号为：" + equipmentCode + "的设备信息！状态码：" + count);
		}
		return (EquipmentPrimaryInfo) this.daoUtils.findSingle(hql);
	}

	/**
	 * <li>说明：保存故障提票调度派工信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 故障提票idx主键数组
	 * @param 施修班组json对象{repairTeamId: "384", repairTeam: "交接组"} 
	 * @throws NoSuchFieldException 
	 */
	public void saveRepairTeam(String[] ids, FaultOrder t) throws NoSuchFieldException {
		FaultOrder entity = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			if (FaultOrder.STATE_CLZ.equals(entity.getState())) {
				continue;
			}
			entity.setState(FaultOrder.STATE_YPG); // 设置记录状态为：已派工
			entity.setRepairTeam(t.getRepairTeam());
			entity.setRepairTeamId(t.getRepairTeamId());
			entity.setAssistRepairTeam(t.getAssistRepairTeam());
			entity.setAssistRepairTeamId(t.getAssistRepairTeamId());
			// 调度派工日期
			entity.setDispatchDateDD(Calendar.getInstance().getTime());

			super.saveOrUpdate(entity);
			// 调度派工后，推送故障提票工长派工消息
			pushMessage2Gzpg(entity);
		}
	}

	/**
	 * <li>说明：保存故障提票工长派工信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 故障提票idx主键数组
	 * @param 施修班组json对象{repairEmpId: "384", repairEmp: "易文良"} 
	 * @throws NoSuchFieldException 
	 */
	public void saveRepairEmp(String[] ids, FaultOrder t) throws NoSuchFieldException {
		List<FaultOrder> entityList = new ArrayList<FaultOrder>(ids.length);
		FaultOrder entity = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			if (FaultOrder.STATE_YCL.equals(entity.getState())) {
				continue;
			}
			entity.setState(FaultOrder.STATE_CLZ); // 设置记录状态为：处理中
			entity.setRepairEmp(t.getRepairEmp());
			entity.setRepairEmpId(t.getRepairEmpId());
			// 工长派工日期
			entity.setDispatchDateGZ(Calendar.getInstance().getTime());
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：故障提票处理
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月20日
	 * <li>修改人：何涛
	 * <li>修改内容：2016年12月5日
	 * <li>修改日期：故障处理增加“故障恢复时间”
	 * @param idx 故障提票idx主键
	 * @param 施修班组json对象{repairContent: "处理完成", causeAnalysis: "阻塞", assistRepairEmps: "张三,李四"} 
	 * @throws NoSuchFieldException 
	 */
	public void updateFinished(String idx, FaultOrder t, String[] filePathArray) throws NoSuchFieldException {
		FaultOrder entity = this.getModelById(idx);

		if (FaultOrder.STATE_YCL.equals(entity.getState())) {
			return;
		}

		entity.setState(FaultOrder.STATE_YCL); // 设置记录状态为：处理中
		entity.setRepairContent(t.getRepairContent()); // 实际修理内容
		entity.setCauseAnalysis(t.getCauseAnalysis()); // 原因分析
		entity.setAssistRepairEmps(t.getAssistRepairEmps()); // 辅修人员
		// 如果修理人字段为空，则默认设置修理人为当前系统操作人
		if (null == t.getRepairEmpId()) {
			entity.setRepairEmpId(SystemContext.getOmEmployee().getEmpid().toString());
			entity.setRepairEmp(SystemContext.getOmEmployee().getEmpname());
		}

		// Modified by hetao on 2016-12-05 增加“故障恢复时间”存储
		if (null == t.getFaultRecoverTime()) {
			entity.setFaultRecoverTime(Calendar.getInstance().getTime());
		} else {
			entity.setFaultRecoverTime(t.getFaultRecoverTime());
		}
		super.saveOrUpdate(entity);

		// 设备故障提票处理完成后自动记录设备停机时间
		this.equipmentShutdownManager.insertByFaultOrder(entity);

		if (null == filePathArray || 0 >= filePathArray.length) {
			return;
		}
		// 保存附件信息
		this.attachmentManager.insert(t.getIdx(), "e_fault_order", filePathArray);
	}

	/**
	* <li>说明：管理员通过web端对故障处理结果进行修改
	* <li>创建人：卢轶
	* <li>创建日期：2016年12月8日
	* <li>修改人：
	* <li>修改内容：
	* <li>修改日期：
	* @param idx 故障提票idx主键
	* @param 施修班组json对象{repairContent: "处理完成", causeAnalysis: "阻塞", assistRepairEmps: "张三,李四"} 
	* @throws NoSuchFieldException 
	*/
	public void updateFinishedWeb(String idx, FaultOrder t) throws NoSuchFieldException {
		FaultOrder entity = this.getModelById(idx);
		String beforeSaveState = entity.getState();
		// 设备故障提票处理完成后自动记录设备停机时间
		if (!FaultOrder.STATE_YCL.equals(beforeSaveState)) {
			entity.setState(FaultOrder.STATE_YCL); // 设置记录状态为：已处理
		}
		entity.setRepairContent(t.getRepairContent()); // 实际修理内容
		entity.setCauseAnalysis(t.getCauseAnalysis()); // 原因分析
		entity.setAssistRepairEmps(t.getAssistRepairEmps()); // 辅修人员
		if (null != t.getRepairEmpId()) {
			entity.setRepairEmpId(t.getRepairEmpId());
		} // 修理人ID 不能为空
		if (null != t.getRepairEmp()) {
			entity.setRepairEmp(t.getRepairEmp());
		} // 修理人姓名 不能为空
		entity.setAssistRepairTeamId(t.getAssistRepairTeamId());// 辅修班组ID
		entity.setAssistRepairTeam(t.getAssistRepairTeam()); // 辅修班组
		if (null != t.getRepairTeamId()) {
			entity.setRepairTeamId(t.getRepairTeamId());
		} // 主修班组ID 不能为空
		if (null != t.getRepairTeamId()) {
			entity.setRepairTeam(t.getRepairTeam());
		} // 主修班组 不能为空
			// 修改故障恢复时间
		if (null == t.getFaultRecoverTime()) {
			entity.setFaultRecoverTime(Calendar.getInstance().getTime());
		} else {
			entity.setFaultRecoverTime(t.getFaultRecoverTime());
		}
		super.saveOrUpdate(entity);
		if (!FaultOrder.STATE_YCL.equals(beforeSaveState)) {
			this.equipmentShutdownManager.insertByFaultOrder(entity); // 故障处理完成后自动记录设备停机时间
		}
	}

	/**
	 * <li>说明：设备使用人确认故障提票处理
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：兰佳妮
	 * <li>修改内容：将单个处理改为批量处理
	 * <li>修改日期：2016年10月12日
	 * @param entity 故障提票实体对象
	 * @throws NoSuchFieldException 
	 */
	public void confirm(String[] ids) throws NoSuchFieldException {
		OmEmployee userData = SystemContext.getOmEmployee();
		for (Serializable idx : ids) {
			FaultOrder entity = this.getModelById(idx);
			if (null != entity.getUseWorkerId()) {
				throw new BusinessException(String.format("故障提票单号：%s已被【%s】确认，请刷新后重试！", entity.getFaultOrderNo(), entity.getUseWorker()));
			}
			entity.setUseWorker(userData.getEmpname());
			entity.setUseWorkerId(userData.getEmpid());
			super.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：设备故障提票单退回
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月4日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 故障提票idx主键数组
	 * @param backReason 退回原因
	 * @throws NoSuchFieldException
	 */
	public void updateBacked(String[] ids, String backReason) throws NoSuchFieldException {
		List<FaultOrder> entityList = new ArrayList<FaultOrder>(ids.length);
		FaultOrder entity = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			entity.setBackReason(backReason);
			entity.setState(FaultOrder.STATE_TH);
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：手持pda提交故障提票信息，一并保存附件-故障位置照片信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月11日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 故障提票
	 * @param filePathArray 故障提票附件，保存到服务器磁盘的全路径数组：如：["F:\EquipmentUpload\e_fault_order\2016\8\20160810150140320.jpg"]
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate2(FaultOrder t, String[] filePathArray) throws NoSuchFieldException {
		this.saveOrUpdate(t);
		if (null == filePathArray || 0 >= filePathArray.length) {
			return;
		}
		// 保存附件信息
		this.attachmentManager.insert(t.getIdx(), "e_fault_order", filePathArray);
	}

	/**
	 * <li>说明：获取工长派工代办项记录数
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 工长派工代办项记录数
	 */
	public int getGzpgCount() {
		String hql = "From FaultOrder Where recordStatus = 0 And state = ? And repairTeamId = ?";
		return this.daoUtils.getCount(hql, FaultOrder.STATE_YPG, SystemContext.getOmEmployee().getOrgid());
	}

	/**
	 * <li>说明：获取调度派工代办项记录数
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 调度派工代办项记录数
	 */
	public int getDdpgCount() {
		String hql = "From FaultOrder Where recordStatus = 0 And state = ?";
		return this.daoUtils.getCount(hql, FaultOrder.STATE_XJ);
	}

	/**
	 * <li>说明：获取故障处理待办项记录数
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年3月9日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 故障处理待办项记录数
	 */
	public int getGzclCount() {
		String hql = "From FaultOrder where recordStatus = 0 And state = ? and concat(',', repairEmpId, ',') like " + "'%," + SystemContext.getOmEmployee().getEmpid() + ",%'";
		return this.daoUtils.getCount(hql, FaultOrder.STATE_CLZ);
	}

	/**
	 * <li>说明：获取故障提票待办项记录数
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年3月24日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 故障提票待办项记录数
	 */
	public int getGztpCount() {
		String hql = "From FaultOrder where recordStatus = 0 And state = ? And submitEmpId = ?";
		return this.daoUtils.getCount(hql, FaultOrder.STATE_XJ, SystemContext.getOmEmployee().getEmpid());
	}

	/**
	 * <li>说明：故障提票创建时，推送故障提票调度派工信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 故障提票实体对象
	 * @throws NoSuchFieldException
	 */
	private void pushMessage2Ddpg(FaultOrder t) throws NoSuchFieldException {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
		if (null == epi) {
			return;
		}
//		SystemMessage msg = new SystemMessage().title(Menu.提票调度派工.toString()).content(String.format("%s提报了设备故障，请及时进行处理：", t.getSubmitEmp())).businessIdx(t.getIdx())
//				.equipmentIdx(t.getEquipmentIdx()).equipmentName(t.getEquipmentName()).equipmentCode(t.getEquipmentCode()).authVerifyMode(SystemMessage.AUTH_VERIFY_MODE_MENUSEQ);
//		msg.setUsePlace(epi.getUsePlace()); // 设备设置地点
//		msg.setFaultPlace(t.getFaultPlace()); // 故障部位及意见
//		msg.setFaultPhenomenon(t.getFaultPhenomenon()); // 故障现象
//		this.systemMessageManager.saveAndPushMessage(msg);
	}

	/**
	 * <li>说明：故障提票创建时，向设备使用车间推送故障提票信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 故障提票实体对象
	 * @throws NoSuchFieldException 
	 */
	private void pushMessage2UseWorkshop(FaultOrder t) throws NoSuchFieldException {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
		if (null == epi) {
			return;
		}
//		SystemMessage msg = new SystemMessage().title("设备故障发生").content(String.format("%s提报了设备故障，请注意使用以下设备：", t.getSubmitEmp())).businessIdx(t.getIdx())
//				.equipmentIdx(t.getEquipmentIdx()).equipmentName(t.getEquipmentName()).equipmentCode(t.getEquipmentCode()).receiveOrgIds(epi.getUseWorkshopId())
//				.authVerifyMode(SystemMessage.AUTH_VERIFY_MODE_ORGID);
//		msg.setUsePlace(epi.getUsePlace()); // 设备设置地点
//		msg.setFaultPlace(t.getFaultPlace()); // 故障部位及意见
//		msg.setFaultPhenomenon(t.getFaultPhenomenon()); // 故障现象
//		this.systemMessageManager.saveAndPushMessage(msg);
	}

	/**
	 * <li>说明：调度派工后，推送故障提票工长派工消息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 故障提票实体对象
	 * @throws NoSuchFieldException
	 */
	private void pushMessage2Gzpg(FaultOrder t) throws NoSuchFieldException {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
		if (null == epi) {
			return;
		}
//		SystemMessage msg = new SystemMessage().title(Menu.提票工长派工.toString()).content(String.format("%s提报了设备故障，请及时进行处理！", t.getSubmitEmp())).businessIdx(t.getIdx())
//				.equipmentIdx(t.getEquipmentIdx()).equipmentName(t.getEquipmentName()).equipmentCode(t.getEquipmentCode()).receiveOrgIds(t.getRepairTeamId().toString())
//				.authVerifyMode(SystemMessage.AUTH_VERIFY_MODE_MENUSEQ + SystemMessage.AUTH_VERIFY_MODE_ORGID);
//		msg.setUsePlace(epi.getUsePlace()); // 设备设置地点
//		msg.setFaultPlace(t.getFaultPlace()); // 故障部位及意见
//		msg.setFaultPhenomenon(t.getFaultPhenomenon()); // 故障现象
//		this.systemMessageManager.saveAndPushMessage(msg);
	}

	/**
	 * <li>说明：分页查询，查询需要使用人确认的故障提票 - pda
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年10月12日
	 * <li>修改人：何涛
	 * <li>修改内容：2016年10月25日
	 * <li>修改日期：修改查询语句拼接时的对象引用错误
	 * @param searchEntity查询条件实体
	 * @return 故障提票对象集合
	 */
	public Page<FaultOrder> queryPageList2User(SearchEntity<FaultOrder> searchEntity) {
		String hql = "From FaultOrder Where recordStatus = 0 And useWorkerId Is Null And state = '" + FaultOrder.STATE_YCL + "'";
		StringBuilder sb = new StringBuilder(hql);

		// 查询条件 - 设备编码或者名称
		FaultOrder entity = searchEntity.getEntity();
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" And (").append(" equipmentCode Like '%").append(entity.getEquipmentCode()).append("%'").append(" Or").append(" equipmentName Like '%")
					.append(entity.getEquipmentCode()).append("%'").append(" )");
		}

		// Modified by hetao on 2017-02-27 处理排序
		this.processOrdersInSql(searchEntity, sb);
		// Modified by hetao on 2016-10-25 修改查询语句拼接时的对象引用错误
		String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
		return this.findPageList(totalHql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit());
	}

	/**
	 * <li>说明：分页查询，统计设备在一段时间内发生故障的次数
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月21日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param  searchEntity查询条件实体
	 * @return 故障提票统计对象集合 
	 */
	public Page<FaultOrderBean> queryPageStatistics(SearchEntity<FaultOrderBean> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("fault%cfault_order:queryPageStatistics", File.separatorChar));

		FaultOrderBean entity = searchEntity.getEntity();

		// 查询条件 - 开始时间
		if (null != entity.getStartDate()) {
			sql = sql.replace("1988-02-10", DateUtil.yyyy_MM_dd.format(entity.getStartDate()));
		}
		// 查询条件 - 结束时间
		if (null != entity.getEndDate()) {
			sql = sql.replace("2099-01-01", DateUtil.yyyy_MM_dd.format(entity.getEndDate()));
		}

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT FROM (" + sql.toString() + ") T";
		return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, FaultOrderBean.class);
	}

	/**
	 * <li>说明：分页查询，按月度统计设备故障发生次数
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月25日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param  searchEntity查询条件实体
	 * @return 故障提票统计对象集合 
	 */
	public Page<FaultStatisM> queryPageStatisticsByMonth(SearchEntity<FaultStatisM> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("fault%cfault_order:queryPageStatisticsByMonth", File.separatorChar));

		FaultStatisM entity = searchEntity.getEntity();

		// 查询条件 - 故障发生年份
		if (!StringUtil.isNullOrBlank(entity.getYear())) {
			sql = sql.replace("2016", entity.getYear());
		}

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT FROM (" + sql.toString() + ") T";
		return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, FaultStatisM.class);
	}

}
