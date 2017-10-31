package com.yunda.jx.pjjx.partsrdp.tecinst.manager;

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
import com.yunda.jx.pjjx.partsrdp.expendmat.entity.PartsRdpExpendMat;
import com.yunda.jx.pjjx.partsrdp.expendmat.manager.PartsRdpExpendMatManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpWorkerManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecCard;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpTecCard业务类,配件检修工艺工单
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpTecCardManager")
public class PartsRdpTecCardManager extends JXBaseManager<PartsRdpTecCard, PartsRdpTecCard>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** PartsRdpNode业务类,配件检修作业节点 */
	@Resource
	private PartsRdpTecWSManager partsRdpTecWPManager;
	
	/** PartsRdpWorker业务类,作业人员 */
	@Resource
	private PartsRdpWorkerManager partsRdpWorkerManager;
	
	/** PartsRdpTecWS业务类,配件检修工序实例 */
	@Resource
	private PartsRdpTecWSManager PartsRdpTecWSManager;
	
	/** PartsRdpNode业务类,配件检修作业节点 */
	@Resource
	private PartsRdpNodeManager partsRdpNodeManager;
	
	/** PartsRdp业务类,配件检修作业 */
	@Resource
	private PartsRdpManager partsRdpManager;
	
	/** PartsRdpExpendMat业务类,物料消耗记录 */
	@Resource
	private PartsRdpExpendMatManager partsRdpExpendMatManager;
	
	
	
	/**
	 * <li>说明：批量撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 工单实体对象idx主键数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void giveUpBatchJob(String[] ids) throws BusinessException, NoSuchFieldException {
		for (String idx : ids) {
			this.giveUpJob(this.getModelById(idx));
		}
	}
	
	/**
	 * <li>说明：撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 工艺工单实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void giveUpJob(PartsRdpTecCard entity) throws BusinessException, NoSuchFieldException {
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DLQ);		// 状态
		entity.setHandleEmpID(null);								// 领活人
		entity.setHandleEmpName(null);								// 领活人名称
		entity.setWorkEmpID(null);									// 领活人
		entity.setWorkEmpName(null);								// 领活人名称
		entity.setWorkStartTime(null);								// 作业开始时间
		entity.setWorkEndTime(null);								// 作业结束时间		
		this.saveOrUpdate(entity);
		
		// 撤销工艺工序的处理历史记录
		List<PartsRdpTecWS> wsList = this.PartsRdpTecWSManager.getModels(entity.getIdx());
		if (null != wsList && wsList.size() > 0) {
			this.PartsRdpTecWSManager.giveUpJob(wsList);
		}
		
		// 清空物料消耗情况
		List<PartsRdpExpendMat> emList = this.partsRdpExpendMatManager.getModelByRdpTecCardIDX(entity.getIdx());
		if (null != emList || emList.size() > 0) {
			this.partsRdpExpendMatManager.logicDelete(emList);
		}
	}
	
	/**
	 * <li>说明：批量销活，批量销活时，要更新工单下属所有工序实例的状态为“已处理”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 工单实体对象idx主键数组
	 * @param tempEntity 包含业务处理信息的临时实体对象
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void finishBatchJob(String[] ids, PartsRdpTecCard tempEntity) throws BusinessException, NoSuchFieldException {
		PartsRdpTecCard entity;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			
			//  处理【配件检修工序实例】
			List<PartsRdpTecWS> prtWPs = partsRdpTecWPManager.getModels(entity.getIdx());
			if (null != prtWPs && prtWPs.size() > 0) {
				partsRdpTecWPManager.finishBatchWS(prtWPs);
			}
			this.finish(entity, tempEntity);
		}
	}
	
	/**
	 * <li>说明：销活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tempEntity 包含业务处理信息的临时实体对象 包含业务处理信息的临时实体对象
	 * @return PartsRdpTecCard 配件检修工艺工单实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public PartsRdpTecCard finishJob(PartsRdpTecCard tempEntity) throws BusinessException, NoSuchFieldException {
		PartsRdpTecCard entity = this.getModelById(tempEntity.getIdx());
		// 销活
		this.finish(entity, tempEntity);
		return entity;
	}
    
    /**
     * <li>说明：修改已处理的检修工艺工单
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param tempEntity 包含业务处理信息的临时实体对象
     * @return 配件检修工艺工单实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public PartsRdpTecCard updateJob(PartsRdpTecCard tempEntity) throws BusinessException, NoSuchFieldException {
        PartsRdpTecCard entity = this.getModelById(tempEntity.getIdx());
        // 销活
        this.updateJob(entity, tempEntity);
        return entity;
    }
	
	/**
	 * <li>说明：销活功能的统一接口，其他对外暴露的【销活】功能均必须调用该方法，销活时要检验工单下属工序状态是否都是“已处理”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 被销活工单的实例对象
	 * @param tempEntity 包含销活信息的封装的临时对象
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	private void finish(PartsRdpTecCard entity, PartsRdpTecCard tempEntity) throws BusinessException, NoSuchFieldException {
		// 检验“开工时间”是否填写,如果没有填写则提示
		if (null != tempEntity.getWorkStartTime()) {
			entity.setWorkStartTime(tempEntity.getWorkStartTime());
		}
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getTecCardNo()).append(Constants.BRACKET_L).append(entity.getTecCardDesc()).append(Constants.BRACKET_R);
		if (null == entity.getWorkStartTime()) {
			throw new NullPointerException(sb.append("）开工时间未设置！").toString());
		}
		// 检验“完工时间”是否填写，如果没有填写则设置为系统当前时间
		if (null != tempEntity.getWorkEndTime()) {
			entity.setWorkEndTime(tempEntity.getWorkEndTime());
		}
		if (null == entity.getWorkEndTime()) {
			entity.setWorkEndTime(new Date());
		}
		//  验证工艺工单下属工序状态是否均为“已处理”
		/*if (!this.validateFinish(entity)) {
			throw new BusinessException(sb.append("还有未被处理的工序，不能进行销活！").toString());
		}*/
		
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_XJ);												// 设置状态为“修竣”
		entity.setWorkEmpID(this.partsRdpWorkerManager.formatWorkerID(tempEntity.getWorkEmpID()));			// 作业人员
		entity.setWorkEmpName(this.partsRdpWorkerManager.formatWorkerName(tempEntity.getWorkEmpName()));	// 作业人员名称
		this.saveOrUpdate(entity);
		
		// 销活后，要反向更新该工单所挂属作业节点的状态为“已处理”
//		partsRdpNodeManager.updateFinishedStatus(entity.getRdpNodeIDX());
	}
    
    /**
     * <li>说明：修改已处理的检修工艺工单
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 配件检修工艺工单实体
     * @param tempEntity 包含业务处理信息的临时实体对象 
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void updateJob(PartsRdpTecCard entity, PartsRdpTecCard tempEntity) throws BusinessException, NoSuchFieldException {
        // 检验“开工时间”是否填写,如果没有填写则提示
        if (null != tempEntity.getWorkStartTime()) {
            entity.setWorkStartTime(tempEntity.getWorkStartTime());
        } else
            entity.setWorkStartTime(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getTecCardNo()).append(Constants.BRACKET_L).append(entity.getTecCardDesc()).append(Constants.BRACKET_R);
        
        // 检验“完工时间”是否填写，如果没有填写则设置为系统当前时间
        if (null != tempEntity.getWorkEndTime()) {
            entity.setWorkEndTime(tempEntity.getWorkEndTime());
        } else {
            entity.setWorkEndTime(new Date());
        }                                          // 设置状态为“修竣”
        entity.setWorkEmpID(this.partsRdpWorkerManager.formatWorkerID(tempEntity.getWorkEmpID()));          // 作业人员
        entity.setWorkEmpName(this.partsRdpWorkerManager.formatWorkerName(tempEntity.getWorkEmpName()));    // 作业人员名称
        this.saveOrUpdate(entity);
    }
	
	/**
	 * <li>说明：销活时要检验该工单下属所有工序的状态是否都是“已处理”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 被销活工单的实例对象
	 * @return boolean 下属所有工序的状态是否都是“已处理”则返回true，否则返回false
	 */
	public boolean validateFinish(PartsRdpTecCard entity) {
		// 检验下属工序是否都已被处理
		List<PartsRdpTecWS> entityList = partsRdpTecWPManager.getModels(entity.getIdx());
		String[] msg = partsRdpTecWPManager.validateStatus(entityList, PartsRdpTecWS.CONST_STR_STATUS_YCL);
		if (null == msg) {
			return true;
		}
		return false;
	}
	
	/**
	 * <li>说明：根据“作业主键”获取【配件检修工艺工单】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @return List 配件检修工艺工单集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpTecCard> getModels(String rdpIDX) {
		String hql = "From PartsRdpTecCard Where recordStatus = 0 And rdpIDX = ?";
		return this.daoUtils.find(hql,  new Object[]{rdpIDX});
	}
	
	/**
	 * <li>说明：根据“作业主键”和“作业节点主键”获取【配件检修工艺工单】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @param rdpNodeIDX 作业节点主键
	 * @return List<PartsRdpTecCard> 配件检修工艺工单实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpTecCard> getModels(String rdpIDX, String rdpNodeIDX) {
		String hql = "From PartsRdpTecCard Where recordStatus = 0 And rdpIDX = ? And rdpNodeIDX = ?";
		return this.daoUtils.find(hql,  new Object[]{rdpIDX, rdpNodeIDX});
	}
    
	/**
	 * <li>说明：根据“作业主键”获取“作业节点id”为空的【配件检修工艺工单】
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
     * 
     * @param rdpIDX 作业主键
     * @return List<PartsRdpTecCard> 配件检修工艺工单实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpTecCard> getModelsForStart(String rdpIDX) {
		String hql = "From PartsRdpTecCard Where recordStatus = 0 And rdpIDX = ? And rdpNodeIDX is null ";
		return this.daoUtils.find(hql,  new Object[]{rdpIDX});
	}
    
	/**
	 * <li>说明：批量领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-05
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
	 * <li>创建日期：2014-12-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 工单实体对象idx主键
	 * @return PartsRdpTecCard 配件检修工艺工单实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public PartsRdpTecCard startUpJob(String idx) throws BusinessException, NoSuchFieldException {
		PartsRdpTecCard entity = this.getModelById(idx);
		// 获取当前登录用户的信息
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		
		entity.setHandleEmpID(omEmployee.getEmpid());				// 领活人
		entity.setHandleEmpName(omEmployee.getEmpname());			// 领活人名称
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DCL);		// 状态
		entity.setWorkStartTime(Calendar.getInstance().getTime());	// 作业开始时间
		this.saveOrUpdate(entity);
		
		String rdpNodeIDX = entity.getRdpNodeIDX();
		if (StringUtil.isNullOrBlank(rdpNodeIDX)) {
			this.partsRdpManager.updateRealStartTime(entity.getRdpIDX());
		} else {
			// 领活成功后要反向更新该工单所挂属作业节点的实际开始时间（如果该字段还没有设置）
			this.partsRdpNodeManager.updateRealStartTime(entity.getRdpNodeIDX());
		}
		return entity;
	}
	
	/**
	 * <li>说明：暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tempEntity 包含业务处理信息的临时实体对象
     * @return PartsRdpTecCard 配件检修工艺工单实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public PartsRdpTecCard saveTemporary(PartsRdpTecCard tempEntity) throws BusinessException, NoSuchFieldException {
		PartsRdpTecCard entity = this.getModelById(tempEntity.getIdx());
		
		entity.setWorkStartTime(tempEntity.getWorkStartTime());						// 作业开始时间
		entity.setWorkEndTime(tempEntity.getWorkEndTime());							// 作业结束时间
		entity.setWorkEmpID(this.partsRdpWorkerManager.formatWorkerID(tempEntity.getWorkEmpID()));			// 作业人员
		entity.setWorkEmpName(this.partsRdpWorkerManager.formatWorkerName(tempEntity.getWorkEmpName()));	// 作业人员名称
		
		this.saveOrUpdate(entity);
		return entity;
	}

	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 工单实体列表
	 * @param status 记录的预期状态
	 * @return String[] 验证消息
	 */
	public String[] validateStatus(List<PartsRdpTecCard> entityList, String status) {
		List<String> errMsgs = new ArrayList<String>(entityList.size());
		String validateMsg = null;
		for (PartsRdpTecCard entity : entityList) {
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
	 * @param idx 配件检修工艺工单主键
	 * @param status 记录的预期状态
     * @return String[] 验证消息
	 */
	public String validateStatus(String idx, String status) {
		return this.validateStatus(getModelById(idx), status);
	}

	/**
	 * <li>说明：功能操作前的记录状态验证，如果操作发生时，记录的状态不是预期的状态，则返回相应的状态的提示信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件检修工艺工单
	 * @param status 记录的预期状态
     * @return String 验证消息
	 */
	private String validateStatus(PartsRdpTecCard entity, String status) {
	    // 验证工单的状态
	    String errMsg = PartsRdpRecordCardManager.checkEntityStatus(status, entity.getStatus());
        if (null == errMsg) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getTecCardNo()).append(Constants.BRACKET_L).append(entity.getTecCardDesc()).append(Constants.BRACKET_R);
        return sb.append(errMsg).toString();
	}
	
	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 工单实体对象idx主键数组
	 * @param status 记录的预期状态
     * @return String[] 验证消息
	 */
	public String[] validateStatus(String[] ids, String status) {
		List<String> errMsgs = new ArrayList<String>(ids.length);
		PartsRdpTecCard entity = null;
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
	 * @param entityList 配件检修工艺工单实体集合
	 * @param msgList 验证消息集合
	 * @return
	 * @throws BusinessException
	 */
	public void checkFinishStatus(List<PartsRdpTecCard> entityList, List<String> msgList) throws BusinessException {
		for (PartsRdpTecCard entity : entityList) {
			// 检验“检修记录工单”是否修竣
			if (!entity.getStatus().equals(IPartsRdpStatus.CONST_STR_STATUS_XJ)) {
				msgList.add("检修工艺工单：" + entity.getTecCardNo() + "（" + entity.getTecCardDesc() + "）还未修竣！");
				return;
			}
			//程锐修改20160421 不需要再验证检修检测项是否处理 检验“检修记录工单”记录工单下属“检修检测项”是否都已处理 
//			List<PartsRdpTecWS> list = PartsRdpTecWSManager.getModels(entity.getIdx());
//			PartsRdpTecWSManager.checkFinishStatus(list, msgList);
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
     * @param entityList 配件检修工艺工单实体集合
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void startUp(List<PartsRdpTecCard> entityList) throws BusinessException, NoSuchFieldException {
		String[] errMsg = this.validateStatus(entityList, IPartsRdpStatus.CONST_STR_STATUS_WKF);
		if (null != errMsg) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < errMsg.length; i++) {
				sb.append(i).append(". ").append(errMsg[i]).append("<br>");
			}
			throw new BusinessException(sb.toString());
		}
		for (PartsRdpTecCard entity : entityList) {
            entity.setWorkStartTime(Calendar.getInstance().getTime());
			entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DCL);
			this.saveOrUpdate(entity);
		}
	}
    
    /**
     * <li>说明：统计指定配件检修作业下工艺工单的处理情况
     * <li>创建人：何涛
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修作业主键
     * @param status 配件检修工艺工单状态，未处理："wcl"；已处理："ycl"
     * @param rdpNodeIDX 配件检修节点IDX
     * @return 指定状态的工艺工单数
     */
    public int getCount(String rdpIDX, String status, String rdpNodeIDX) {
        StringBuilder sb = new StringBuilder("From PartsRdpTecCard Where recordStatus = 0");
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