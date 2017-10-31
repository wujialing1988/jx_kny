package com.yunda.sb.base.common.manager;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.sb.base.common.entity.SystemMessageReceiver;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: SystemMessageReceiver管理器，数据表：T_SYSTEM_MESSAGE_RECEIVER
 * <li>创建人：何涛
 * <li>创建日期：2016年9月19日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service(value = "systemMessageReceiverManger")
public class SystemMessageReceiverManger extends JXBaseManager<SystemMessageReceiver, SystemMessageReceiver> {

	/**
	 * <li>说明：系统操作者确认接收消息后，记录消息的接收人、班组等信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param systemMessageIdx 系统消息idx主键
	 * @param receiveState 接收状态，0：忽略，1：确认（查看）
	 * @throws NoSuchFieldException 
	 */
	public void receive(String systemMessageIdx, int receiveState) throws NoSuchFieldException {
		if (StringUtil.isNullOrBlank(systemMessageIdx)) {
			throw new NullPointerException("数据异常，消息主键为空！");
		}
		OmEmployee userData = SystemContext.getOmEmployee();
		long empid = userData.getEmpid();
		SystemMessageReceiver entity = getModel(empid, systemMessageIdx);
		if (null != entity) {
			if (SystemMessageReceiver.RECEIVE_STATE_QR == receiveState && SystemMessageReceiver.RECEIVE_STATE_QR != entity.getReceiveState().intValue()) {
				entity.setReceiveState(receiveState);
				this.saveOrUpdate(entity);
			}
			return;
		}
		entity = new SystemMessageReceiver()
			.systemMessageIdx(systemMessageIdx)
			.orgId(userData.getOrgid())
			.orgName(userData.getOrgname())
			.empId(userData.getEmpid())
			.empName(userData.getEmpname())
			.receiveState(receiveState);
		this.saveOrUpdate(entity);
	}
	
	/**
	 * <li>说明：获取消息接收记录对象实体
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param empId 人员id
	 * @param systemMessageIdx 系统消息idx主键
	 * @return 消息接收记录对象实体
	 */
	private SystemMessageReceiver getModel(Long empId, String systemMessageIdx) {
		String hql = "From SystemMessageReceiver Where recordStatus = 0 And empId = ? And systemMessageIdx = ?"; 
		return (SystemMessageReceiver) this.daoUtils.findSingle(hql, empId, systemMessageIdx);
	}

}
