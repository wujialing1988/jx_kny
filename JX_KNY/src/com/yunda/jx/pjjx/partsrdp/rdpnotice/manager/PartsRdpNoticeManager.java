package com.yunda.jx.pjjx.partsrdp.rdpnotice.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpWorkerManager;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.entity.PartsRdpNotice;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpNotice业务类,提票单
 * <li>创建人：何涛
 * <li>创建日期：2014-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpNoticeManager")
public class PartsRdpNoticeManager extends JXBaseManager<PartsRdpNotice, PartsRdpNotice>{
	
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** PartsRdpWorker业务类,作业人员 */
	@Resource
	private PartsRdpWorkerManager partsRdpWorkerManager;
	
	/** CodeRuleConfig业务类,业务编码规则配置 */
	@Resource
	private CodeRuleConfigManager codeRuleConfigManager;
	
	public static final String PJJX_QC_NOTICE_NO = "PJJX_QC_NOTICE_NO";
	
	/**
	 * <li>说明：验证提票单编号的唯一性
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人：何涛
	 * <li>修改日期：2016-04-11
	 * <li>修改内容：优化代码
	 * 
	 * @param t 工单实体对象
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 * @return String[]
	 */
	@Override
	public String[] validateUpdate(PartsRdpNotice t) {
		// 如果前段没有设置编号值，则在后台设置
		String noticeNo = t.getNoticeNo();
		if (null == noticeNo || noticeNo.trim().length() <= 0) {
			noticeNo = codeRuleConfigManager.makeConfigRule(PJJX_QC_NOTICE_NO);
			t.setNoticeNo(noticeNo);
		}
        String hql = "From PartsRdpNotice Where recordStatus = 0 And rdpIDX = ? And noticeNo = ?";
        PartsRdpNotice entity = (PartsRdpNotice) this.daoUtils.findSingle(hql, new Object[]{ t.getRdpIDX(), t.getNoticeNo()});
        if (null != entity && entity.getIdx().equals(t.getIdx())) {
            return new String[]{"提票单编号：" + t.getNoticeNo() + "已经存在，不能重复添加！"};
        }
		return null;
	}
	
	/**
	 * <li>说明：批量撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 工单实体对象idx主键数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void giveUpBatchJob(String[] ids) throws BusinessException, NoSuchFieldException {
		List<PartsRdpNotice> entityList = new ArrayList<PartsRdpNotice>(ids.length);
		PartsRdpNotice entity = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DLQ);		// 状态
			entity.setSolution(null);									// 处理结果描述
			entity.setHandleEmpID(null);								// 领活人
			entity.setHandleEmpName(null);								// 领活人名称
			entity.setWorkEmpID(null);									// 领活人
			entity.setWorkEmpName(null);								// 领活人名称
			entity.setWorkStartTime(null);								// 作业开始时间
			entity.setWorkEndTime(null);								// 作业开始时间
			
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：批量销活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 工单实体对象idx主键数组
	 * @param tempEntity 包含业务处理信息的临时实体对象
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void finishBatchJob(String[] ids, PartsRdpNotice tempEntity) throws BusinessException, NoSuchFieldException {
		PartsRdpNotice entity;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			this.finish(entity, tempEntity);
		}
	}
	
	/**
	 * <li>说明：销活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tempEntity 包含业务处理信息的临时实体对象
	 * @return PartsRdpNotice
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public PartsRdpNotice finishJob(PartsRdpNotice tempEntity) throws BusinessException, NoSuchFieldException {
		PartsRdpNotice entity = this.getModelById(tempEntity.getIdx());
		// 销活
		this.finish(entity, tempEntity);
		return entity;
	}
	
	/**
	 * <li>说明：销活功能的统一接口，其他对外暴露的【销活】功能均必须调用该方法
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 被销活工单的实例对象
	 * @param tempEntity 包含销活信息的封装的临时对象
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	private void finish(PartsRdpNotice entity, PartsRdpNotice tempEntity) throws BusinessException, NoSuchFieldException {
		// 检验“开工时间”是否填写,如果没有填写则提示
		if (null != tempEntity.getWorkStartTime()) {
			entity.setWorkStartTime(tempEntity.getWorkStartTime());
		}
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getNoticeNo()).append(Constants.BRACKET_L).append(entity.getNoticeDesc()).append(Constants.BRACKET_R);
		if (null == entity.getWorkStartTime()) {
			throw new NullPointerException(sb.append("开工时间未设置！").toString());
		}
		// 检验“完工时间”是否填写，如果没有填写则设置为系统当前时间
		if (null != tempEntity.getWorkEndTime()) {
			entity.setWorkEndTime(tempEntity.getWorkEndTime());
		}
		if (null == entity.getWorkEndTime()) {
			entity.setWorkEndTime(new Date());
		}
		entity.setWorkEmpID(this.partsRdpWorkerManager.formatWorkerID(tempEntity.getWorkEmpID()));		// 作业人员
		entity.setWorkEmpName(this.partsRdpWorkerManager.formatWorkerName(tempEntity.getWorkEmpName()));// 作业人员名称
		entity.setSolution(tempEntity.getSolution());								// 处理方案描述
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_XJ);						// 设置状态为“修竣”
		
		this.saveOrUpdate(entity);
	}
	
	/**
	 * <li>说明：根据“作业主键”获取【提票工单】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpNotice> getModels(String rdpIDX) {
		String hql = "From PartsRdpNotice Where recordStatus = 0 And rdpIDX = ?";
		return this.daoUtils.find(hql,  new Object[]{rdpIDX});
	}
	
	/**
	 * <li>说明：批量领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 工单实体对象idx主键数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void startUpBatchJob(String[] ids) throws BusinessException, NoSuchFieldException {
		for (String idx : ids) {
			// 领活
			this.startUpJob(idx);
		}
	}
	
	/**
	 * <li>说明：领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 工单实体对象idx主键
	 * @return PartsRdpNotice
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public PartsRdpNotice startUpJob(String idx) throws BusinessException, NoSuchFieldException {
		// 获取当前登录用户的信息
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		PartsRdpNotice entity = this.getModelById(idx);
		entity.setHandleEmpID(omEmployee.getEmpid());				// 领活人
		entity.setHandleEmpName(omEmployee.getEmpname());			// 领活人名称
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DCL);		// 状态
		entity.setWorkStartTime(Calendar.getInstance().getTime());	// 作业开始时间
		this.saveOrUpdate(entity);
		return entity;
	}
	
	/**
	 * <li>说明：暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tempEntity 包含业务处理信息的临时实体对象
	 * @return PartsRdpNotice
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public PartsRdpNotice saveTemporary(PartsRdpNotice tempEntity) throws BusinessException, NoSuchFieldException {
		PartsRdpNotice entity = this.getModelById(tempEntity.getIdx());
		
		entity.setWorkStartTime(tempEntity.getWorkStartTime());						// 作业开始时间
		entity.setWorkEndTime(tempEntity.getWorkEndTime());							// 作业结束时间
		entity.setWorkEmpID(this.partsRdpWorkerManager.formatWorkerID(tempEntity.getWorkEmpID()));			// 作业人员
		entity.setWorkEmpName(this.partsRdpWorkerManager.formatWorkerName(tempEntity.getWorkEmpName()));	// 作业人员名称
		entity.setSolution(tempEntity.getSolution());								// 处理方案描述
		
		this.saveOrUpdate(entity);
		return entity;
	}

	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 工单实体列表
	 * @param status 记录的预期状态
	 * @return String[]
	 */
	public String[] validateStatus(List<PartsRdpNotice> entityList, String status) {
		List<String> errMsgs = new ArrayList<String>(entityList.size());
		String validateMsg = null;
		for (PartsRdpNotice entity : entityList) {
			validateMsg = this.validateStatus(entity, status);
			if (null == validateMsg) {
				continue;
			}
			errMsgs.add(validateMsg);
		}
		if (errMsgs.size() > 0) {
			return errMsgs.toArray(new String[errMsgs.size()]);
		}
		return null;
	}

	/**
	 * <li>说明：功能操作前的记录状态验证，如果操作发生时，记录的状态不是预期的状态，则返回相应的状态的提示信息
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 提票工单id
	 * @param status 记录的预期状态
	 * @return String
	 */
	public String validateStatus(String idx, String status) {
		return this.validateStatus(getModelById(idx), status);
	}
	
	/**
	 * <li>说明：功能操作前的记录状态验证，如果操作发生时，记录的状态不是预期的状态，则返回相应的状态的提示信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 提票工单
	 * @param status 记录的预期状态
     * @return String 验证消息
	 */
	private String validateStatus(PartsRdpNotice entity, String status) {
	    // 验证工单的状态
	    String errMsg = PartsRdpRecordCardManager.checkEntityStatus(status, entity.getStatus());
        if (null == errMsg) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getNoticeNo()).append(Constants.BRACKET_L).append(entity.getNoticeDesc()).append(Constants.BRACKET_R);
        return sb.append(errMsg).toString();
	}
	
	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 工单实体对象idx主键数组
	 * @param status 记录的预期状态
	 * @return String[]
	 */
	public String[] validateStatus(String[] ids, String status) {
		List<String> errMsgs = new ArrayList<String>(ids.length);
		PartsRdpNotice entity = null;
		String validateMsg = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			validateMsg = this.validateStatus(entity, status);
			if (null == validateMsg) {
				continue;
			}
			errMsgs.add(validateMsg);
		}
		if (errMsgs.size() > 0) {
			return errMsgs.toArray(new String[errMsgs.size()]);
		}
		return null;
	}
	
	/**
	 * <li>说明：检验所有记录是否都已处理完成
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 提票单list
	 * @param msgList 提示信息list
	 * @throws BusinessException
	 */
	public void checkFinishStatus(List<PartsRdpNotice> entityList, List<String> msgList) throws BusinessException {
		for (PartsRdpNotice entity : entityList) {
			// 检验“提票工单”是否修竣
			if (!entity.getStatus().equals(IPartsRdpStatus.CONST_STR_STATUS_XJ)) {
				msgList.add("提票工单：" + entity.getNoticeNo() + "（" + entity.getNoticeDesc() + "）还未修竣！");
				return;
			}
		}
	}
	
	/**
	 * <li>说明：开放工单，设置状态为“待领取”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 *  
	 * @param entityList 提票单list
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void startUp(List<PartsRdpNotice> entityList) throws BusinessException, NoSuchFieldException {
		String[] errMsg = this.validateStatus(entityList, IPartsRdpStatus.CONST_STR_STATUS_WKF);
		if (null != errMsg) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < errMsg.length; i++) {
				sb.append(i).append(". ").append(errMsg[i]).append("<br>");
			}
			throw new BusinessException(sb.toString());
		}
		for (PartsRdpNotice entity : entityList) {
			entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DLQ);
			this.saveOrUpdate(entity);
		}
	}
	
	/**
	 * <li>说明：提票
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-22
	 * <li>修改人：何涛
	 * <li>修改日期：2016-02-22
	 * <li>修改内容：由于当前设计不在有“领活”的操作，因为在提票时，默认设置票活的开工时间为提票时间
	 * 
	 * @param entity (含基本信息的提票工单实例) {
			rdpIDX:"8a8284f249abf9720149ac1f0f380005",
			noticeNo: "TPD20150114028",
			noticeDesc: "施修过程处理不规范，请重新检修",
		}
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void submitNotice(PartsRdpNotice entity) throws BusinessException, NoSuchFieldException {
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		entity.setNoticeEmpID(omEmployee.getEmpid());					// 提报人
		entity.setNoticeEmpName(omEmployee.getEmpname());				// 提报人名称
		entity.setNoticeTime(Calendar.getInstance().getTime());			// 提报时间
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DCL);
        entity.setWorkStartTime(Calendar.getInstance().getTime());
		this.saveOrUpdate(entity);
	}
    
     /**
     * <li>说明：统计指定配件检修作业下回修提票的处理情况
     * <li>创建人：何涛
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修作业主键
     * @param status 配件检修回修提票状态，未处理："wcl"；已处理："ycl"
     * @param rdpNodeIDX 配件检修节点IDX
     * @return 指定状态的回修提票数
     */
    public int getCount(String rdpIDX, String status, String rdpNodeIDX) {
        StringBuilder sb = new StringBuilder("From PartsRdpNotice Where recordStatus = 0");
        if (!StringUtil.isNullOrBlank(rdpIDX)) {
            sb.append(" And rdpIDX = '").append(rdpIDX).append("'");
        }
        if (!StringUtil.isNullOrBlank(rdpNodeIDX)) {
            sb.append(" And rdpNodeIDX = '").append(rdpNodeIDX).append("'");
        }
        if (IPartsRdpStatus.STATUS_WCL.equals(status)) {
            sb.append(" And status In ('")
                .append(IPartsRdpStatus.CONST_STR_STATUS_WKF).append("','")
                .append(IPartsRdpStatus.CONST_STR_STATUS_DLQ).append("','")
                .append(IPartsRdpStatus.CONST_STR_STATUS_DCL).append("')");
        }
        if (IPartsRdpStatus.STATUS_YCL.equals(status)) {
            sb.append(" And status = '").append(IPartsRdpStatus.CONST_STR_STATUS_XJ).append("'");
        }
        return this.daoUtils.getCount(sb.toString());
    }
	
}