package com.yunda.zb.schedule.manager;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.zb.schedule.entity.ScheduleProposeAndApprove;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：调度命令单操作业务类
 * <li>创建人：黄杨
 * <li>创建日期：2017-6-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "scheduleProposeAndApproveManager")
public class ScheduleProposeAndApproveManager extends JXBaseManager<ScheduleProposeAndApprove, ScheduleProposeAndApprove> {

	/**
	 * <li>说明： 重写保存，增加申请人，申请时间，命令单状态保存，默认状态为待审批
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-6-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@param t ScheduleProposeAndApprove实体
	 */
	@Override
	public void saveOrUpdate(ScheduleProposeAndApprove t) throws BusinessException, NoSuchFieldException {
		// 如果是待审批，则是审批流程,更改状态为已审批，否则为命令单申请
		if (ScheduleProposeAndApprove.STATUS_DSP.equals(t.getStatus())) {
				t.setStatus(ScheduleProposeAndApprove.STATUS_YSP);
		}else {
				OmEmployee omEmployee = SystemContext.getOmEmployee();
				// 申请人ID
				t.setProposerId(omEmployee.getEmpid().toString());
				// 申请人名称
				t.setProposerName(omEmployee.getEmpname());
				// 申请时间
				t.setProposeTime(new Date());
				t.setStatus(ScheduleProposeAndApprove.STATUS_XJ);
		}
		super.saveOrUpdate(t);
	}

	/**
	 * <li>说明： 撤销命令单
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-6-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@param ids 调度命令单主键数组
	 * @throws Exception 
	 */
	public void rovokeApplication(String[] ids) throws Exception {
		String idxs = StringUtil.join(ids);
		String idxsStr = CommonUtil.buildInSqlStr(idxs);
		String sql = "UPDATE SCHEDULE_PROPOSE_AND_APPROVE SET STATUS = '" + ScheduleProposeAndApprove.STATUS_XJ + "'" + " WHERE idx IN " + idxsStr + "";
		this.daoUtils.executeSql(sql);
	}

	/**
	 * <li>说明： 审批
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-6-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@param idxs 调度命令单主键数组
	 * @throws Exception 
	 */
	public void approveApplication(String[] ids) throws Exception {
		String idxs = StringUtil.join(ids);
		String idxsStr = CommonUtil.buildInSqlStr(idxs);
		String sql = "UPDATE SCHEDULE_PROPOSE_AND_APPROVE SET STATUS = '" + ScheduleProposeAndApprove.STATUS_YSP + "'" + " WHERE idx IN " + idxsStr + "";
		this.daoUtils.executeSql(sql);
	}

	/**
	* <li>说明： 发送申请
	* <li>创建人： 黄杨
	* <li>创建日期：2017-6-27
	* <li>修改人：
	* <li>修改内容：
	* <li>修改日期：
	*@param idxs 调度命令单主键数组
	 * @throws Exception 
	*/
	public void sendApplication(String[] ids) throws Exception {
		String idxs = StringUtil.join(ids);
		String idxsStr = CommonUtil.buildInSqlStr(idxs);
		String sql = "UPDATE SCHEDULE_PROPOSE_AND_APPROVE SET STATUS = '" + ScheduleProposeAndApprove.STATUS_DSP + "'" + " WHERE idx IN " + idxsStr + "";
		this.daoUtils.executeSql(sql);
	}

}
