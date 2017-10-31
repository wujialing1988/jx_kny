package com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager;


import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine;
import com.yunda.jx.jxgc.base.jcqcitemdefine.manager.JCQCItemDefineManager;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResultVO;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QualityControlCheckInfoVO;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.TrainRdpQCBackRepairLog;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;
import com.yunda.jx.jxgc.workplanmanage.manager.NodeRunner;
import com.yunda.jx.webservice.stationTerminal.base.entity.ProcessTaskListBean;
import com.yunda.webservice.common.StateRecord;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 质量检验结果管理类
 * <li>创建人：汪东良
 * <li>创建日期：2014-11-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "qCResultManager")
public class QCResultManager extends JXBaseManager<QCResult, QCResult> {
    
	/** 质量检查参与者业务类对象 */
	@Resource
	private QCParticipantManager qcParticipantManager;
	
	/** 质量检查查询业务类对象 */
	@Resource
	private QCResultQueryManager qcResultQueryManager;
    
	/** JCQCItemDefine业务类 */
	@Resource
	private JCQCItemDefineManager jCQCItemDefineManager;
    
	/** 组织机构业务类 */
	@Resource
	private IOmOrganizationManager omOrganizationManager;
    
	/** 人员业务类 */
	@Resource
	private IOmEmployeeManager omEmployeeManager;
    /** 人员业务类 */
    @Resource
    private WorkCardManager workCardManager;
    /** 返修日志业务类 */
    @Resource
    TrainRdpQCBackRepairLogManager trainRdpQCBackRepairLogManager;
	/**
	 * <li>说明：指派质量检查项参与者并开放质量检查项
	 * <li>处理逻辑： 1 有指派：保存指派的质量检查参与者
	 * <li>         2 根据作业班组和质检项配置保存质检项参与者
	 * <li>         3 根据作业卡IDX开放质量检查项
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workCardIDX 作业工单主键
	 * @param result 质量检验结果信息对象数组
	 * @param empId 人员ID
	 * @throws Exception
	 */
	public void updateOpenQCResultByWorkCardIDX(String workCardIDX,
											   QCResultVO[] result, 
											   Long empId) throws Exception {
		OmOrganization org = omOrganizationManager.findByEmpId(empId);
		String checkOrgID = org != null ? org.getOrgid() + "" : "";
		if (StringUtil.isNullOrBlank(checkOrgID)) throw new BusinessException("质量检查项指派参与者时无对应组织机构！");
		if ((result != null && result.length > 0)) {
			for (QCResultVO resultVO : result) {			
				qcParticipantManager.saveIsAssignParticiant(workCardIDX, 
															resultVO.getQcEmpID() + "", 
															omEmployeeManager.getModelById(resultVO.getQcEmpID()).getEmpname(), 
															resultVO.getCheckItemCode());
			}	
			
		}
		qcParticipantManager.saveNotAssignParticiant(workCardIDX, checkOrgID);
		updateOpenQCResultByWorkCardIDX(workCardIDX);
	}
    
	/**
     * <li>说明：根据作业卡IDX开放质量检查项
     * <li>创建人：汪东良
     * <li>创建日期：2014-11-22
     * <li>修改人：何涛
     * <li>修改日期：2016-04-13
     * <li>修改内容：代码重构，摈弃sql更新
     * @param workCardIDX 作业卡idx主键
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
     */
	@SuppressWarnings("unchecked")
	public void updateOpenQCResultByWorkCardIDX(String workCardIDX) throws BusinessException, NoSuchFieldException   {
        List<QCResult> entityList = this.getModelsByWorkCardIDX(workCardIDX);
        if (null == entityList || entityList.isEmpty()) {
            return;
        }
        for (QCResult entity : entityList) {
            entity.setStatus(QCResult.STATUS_DCL);          // 设置质量检验状态为：待处理
        }
        this.saveOrUpdate(entityList);
	}
    
	/**
	 * <li>说明：完成质量检验结果-工位终端
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param checkInfo 质量检验结果信息对象
	 * @param listBean 任务列表对象数组
	 * @throws Exception
	 */
	public void updateFinishQCResult(QualityControlCheckInfoVO checkInfo, ProcessTaskListBean[] listBean) throws Exception {	

		StringBuilder workCardIDXAndCheckCode = new StringBuilder();		
		if (listBean == null || listBean.length < 1) {
			throw new BusinessException("未选择质量检验项！");
		}
		int checkWay = jCQCItemDefineManager.getModelByQCItemNo(listBean[0].getCheckItemCode()).getCheckWay();
		for (ProcessTaskListBean task : listBean) {			
			workCardIDXAndCheckCode.append(task.getSourceIdx()).append(task.getCheckItemCode()).append(Constants.JOINSTR);
		}
		if (workCardIDXAndCheckCode.length() > 0) {
			workCardIDXAndCheckCode.deleteCharAt(workCardIDXAndCheckCode.length() - 1);
			updateFinishQCResult(checkInfo.getCheckPersonIdx(), checkInfo.getCheckPersonName(), checkInfo.getCheckTime(), checkInfo.getRemarks(), workCardIDXAndCheckCode, listBean, checkWay);
		}
	}
	
	/**
     * <li>说明：完成质量检验结果（web端）
     * <li>创建人：汪东良
     * <li>创建日期：2014-11-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qCResultVOList 质量检验结果值对象数组
     * @throws Exception
     */
	public void updateFinishQCResult(QCResultVO[] qCResultVOList) throws Exception {
		if (qCResultVOList == null || qCResultVOList.length < 1) {
			throw new BusinessException("未选择质量检验项！");
		}
		int checkWay = jCQCItemDefineManager.getModelByQCItemNo(qCResultVOList[0].getCheckItemCode()).getCheckWay();
		StringBuilder workCardIDXAndCheckCode = new StringBuilder();
		for (QCResultVO resultVO : qCResultVOList) {			
			workCardIDXAndCheckCode.append(resultVO.getWorkCardIDX()).append(resultVO.getCheckItemCode()).append(Constants.JOINSTR);
		}
		if (workCardIDXAndCheckCode.length() > 0) {
			QCResultVO resultVO = qCResultVOList[0];
			workCardIDXAndCheckCode.deleteCharAt(workCardIDXAndCheckCode.length() - 1);
			updateFinishQCResult(resultVO.getQcEmpID()+ "", resultVO.getQcEmpName(), resultVO.getQcTime(), resultVO.getRemarks(), workCardIDXAndCheckCode, qCResultVOList, checkWay);
		}
	}
	
	/**
	 * <li>说明：批量处理质检（web端）
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param qcEmpID 质检人id
	 * @param qcEmpName 质检人姓名
	 * @param qcTime 质检时间
	 * @param remarks 备注
	 * @param workCardIDXAndCheckCode 作业工单主键+质检项编码
	 * @param qCResultVOList 质检结果对象数组
	 * @param checkWay 质检类型（抽检/必检）
	 * @throws Exception
	 */
	private void updateFinishQCResult(String qcEmpID, 
									  String qcEmpName, 
									  Date qcTime, 
									  String remarks, 
									  StringBuilder workCardIDXAndCheckCode, 
									  QCResultVO[] qCResultVOList,
									  int checkWay) throws Exception{
	   updateFinishQCResultSql(qcEmpID, qcEmpName, qcTime, remarks, workCardIDXAndCheckCode, checkWay);
	   if (checkWay == JCQCItemDefine.CONST_INT_CHECK_WAY_BJ) {
           Set<String> workCardIDXSet = new HashSet<String>();
           for (QCResultVO resultVO : qCResultVOList) {
               workCardIDXSet.add(resultVO.getWorkCardIDX());
           }
           for (String workCardIDX : workCardIDXSet) {
               updateWorkCardAndNodeStatus(workCardIDX);
           }
	   }   
	}
    
	/**
	 * <li>说明：批量处理质检（工位终端）
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param qcEmpID 质检人id
	 * @param qcEmpName 质检人姓名
	 * @param qcTime 质检时间
	 * @param remarks 备注
	 * @param workCardIDXAndCheckCode 作业工单主键+质检项编码
	 * @param listBean 任务列表对象数组
	 * @param checkWay 质检类型（抽检/必检）
	 * @throws Exception
	 */
	private void updateFinishQCResult(String qcEmpID, 
									  String qcEmpName, 
									  Date qcTime, 
									  String remarks, 
									  StringBuilder workCardIDXAndCheckCode, 
									  ProcessTaskListBean[] listBean,
									  int checkWay) throws Exception{
	   updateFinishQCResultSql(qcEmpID, qcEmpName, qcTime, remarks, workCardIDXAndCheckCode, checkWay);
	   if (checkWay == JCQCItemDefine.CONST_INT_CHECK_WAY_BJ) {
            Set<String> workCardIDXSet = new HashSet<String>();
            for (ProcessTaskListBean bean : listBean) {
                workCardIDXSet.add(bean.getSourceIdx());
            }
            for (String workCardIDX : workCardIDXSet) {
                updateWorkCardAndNodeStatus(workCardIDX);
            }
        }	   
	}
    
	/**
	 * <li>说明：全部处理质检（工位终端中的必检）
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param qcEmpID 质检人id
	 * @param qcEmpName 质检人姓名
	 * @param qcTime 质检时间
	 * @param remarks 备注
	 * @param queryString 查询字符串
	 * @param listBean 任务列表对象数组
	 * @throws Exception
	 */
	public void updateAllFinishQCResult(String qcEmpID, 
									    String qcEmpName, 
									    Date qcTime, 
									    String remarks,
									    String queryString,
									    List<ProcessTaskListBean> listBean) throws Exception{
		String querySql = qcResultQueryManager.getQuerySql(Long.valueOf(qcEmpID), JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "", queryString, "getAllQCList");
		String sql = SqlMapUtil.getSql("jxgc-qc:updateAllFinishQCResult")
							   .replace("#STATUS_YCL#", QCResult.STATUS_YCL + "")
							   .replace("#empid#", qcEmpID)
							   .replace("#empname#", qcEmpName)
							   .replace("#remarks#", remarks)
							   .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
							   .replace("#STATUS_DCL#", QCResult.STATUS_DCL + "")
							   .replace("#CONST_INT_CHECK_WAY_BJ#", JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "")
							   .replace("#idx#", "idx in (" + querySql + ")")
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
		daoUtils.executeSql(sql);
        Set<String> workCardIDXSet = new HashSet<String>();
		for (ProcessTaskListBean bean : listBean) {		
            workCardIDXSet.add(bean.getSourceIdx());
		}
        for (String workCardIDX : workCardIDXSet) {
            updateWorkCardAndNodeStatus(workCardIDX);
        }
	}
    
	/**
	 * <li>说明：更新质检处理结果表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param qcEmpID 质检人id
	 * @param qcEmpName 质检人姓名
	 * @param qcTime 质检时间
	 * @param remarks 备注
	 * @param workCardIDXAndCheckCode 作业工单主键+质检项编码
	 * @param checkWay 质检类型（抽检/必检）
	 * @throws Exception
	 */
	private void updateFinishQCResultSql(String qcEmpID, 
										 String qcEmpName,
										 Date qcTime,
										 String remarks, 
										 StringBuilder workCardIDXAndCheckCode,
										 int checkWay) throws Exception{
		String idxAndCode = workCardIDXAndCheckCode.toString().replace(Constants.JOINSTR, "','");
        qcTime = qcTime == null ? new Date() : qcTime;  
		String qcTimeStr = "to_date('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(qcTime) + "', 'yyyy-mm-dd hh24:mi:ss')";
		String sql = SqlMapUtil.getSql("jxgc-qc:updateFinishQCResult")
							   .replace("#STATUS_YCL#", QCResult.STATUS_YCL + "")
							   .replace("#empid#", qcEmpID)
							   .replace("#empname#", qcEmpName)
							   .replace("#qcTime#", qcTimeStr)
							   .replace("#remarks#", StringUtil.nvlTrim(remarks, ""))
							   .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
							   .replace("#STATUS_DCL#", QCResult.STATUS_DCL + "")
							   .replace("#CONST_INT_CHECK_WAY#", checkWay + "")
							   .replace("#workCardIDXAndCheckCode#", "'" + idxAndCode + "'")
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
	   daoUtils.executeSql(sql);	
	}
    
	/**
	 * <li>说明：完成工单并准备过工序
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workCardIDX 作业工单主键
	 */
	private void updateWorkCardAndNodeStatus(String workCardIDX) {
		if (isAllQcCompleteForWorkCard(workCardIDX)) {
			WorkCardManager manager = (WorkCardManager) Application.getSpringApplicationContext().getBean("workCardManager");
			manager.updateWorkCardStatusToComplete(workCardIDX);
			// 准备过工序,节点状态为【处理中】的才过工序
            String nodeCaseIdx = manager.getModelById(workCardIDX).getNodeCaseIDX();
            if (!manager.canFinishNode(nodeCaseIdx))
                return;
            JobProcessNodeManager nodeManager = (JobProcessNodeManager) Application.getSpringApplicationContext().getBean("jobProcessNodeManager");
            JobProcessNode node = nodeManager.getModelById(nodeCaseIdx);
            if (JobProcessNode.STATUS_GOING.equals(node.getStatus()))
                NodeRunner.runner(nodeCaseIdx);
		}
	}
    
	/**
	 * <li>说明：该工单能否完工（工单对应的质量检查必检项都已完成则能完工）
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-26
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workCardIDX 作业工单主键
	 * @return 是否能完工 true 能 false 否
	 */
	private boolean isAllQcCompleteForWorkCard(String workCardIDX) {
		String hql = "from QCResult where recordStatus = 0 and workCardIDX = ? and status != " + QCResult.STATUS_YCL + " and checkWay = " + JCQCItemDefine.CONST_INT_CHECK_WAY_BJ;
		List list = daoUtils.find(hql, new Object[] {workCardIDX});
		return list == null || list.size() < 1;
	} 
    
	/**
	 * <li>说明：终止质量检验结果
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param rdpIDX 兑现单IDX
	 * @throws Exception
	 */
	public void updateTerminateQCResult(String rdpIDX) throws Exception {
		String sql = SqlMapUtil.getSql("jxgc-qc:updateTerminateQCResult")
							   .replace("#STATUS_YZZ#", QCResult.STATUS_YZZ + "")
							   .replace("#STATUS_HANDLED#", WorkCard.STATUS_HANDLED + "")
							   .replace("#STATUS_FINISHED#", WorkCard.STATUS_FINISHED + "")                               
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
							   .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
							   .replace("#rdpIDX#", rdpIDX);
		daoUtils.executeSql(sql);
	}
	
	/**
	 * <li>说明：根据兑现单idx终止未处理的质量检验结果，在机车作业流程完成时调用
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param rdpIDX 兑现单IDX
	 * @throws Exception
	 */
	public void updateTerminateWclQCResult(String rdpIDX) throws Exception {
		String sql = SqlMapUtil.getSql("jxgc-qc:updateTerminateWclQCResult")
							   .replace("#STATUS_YZZ#", QCResult.STATUS_YZZ + "")
							   .replace("#STATUS_YCL#", QCResult.STATUS_YCL + "")
							   .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
							   .replace("#rdpIDX#", rdpIDX)
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
		daoUtils.executeSql(sql);
	}
    
	/**
	 * <li>说明：根据作业工单idx获取需要指派的质量检查项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workCardIDXS  作业工单idx,多个idx用,分隔
	 * @return 需要指派的质量检查项列表
	 * @throws Exception
	 */
	public List<QCResult> getIsAssignCheckItems(String workCardIDXS) throws Exception{
		workCardIDXS = workCardIDXS.replaceAll(Constants.JOINSTR, "','");
		workCardIDXS = "'" + workCardIDXS + "'";
		String sql = SqlMapUtil.getSql("jxgc-qc:getIsAssignCheckItems")
							   .replace("#WORKCARDIDXS#", workCardIDXS)
							   .replace("#CONST_INT_IS_ASSIGN_Y#", JCQCItemDefine.CONST_INT_IS_ASSIGN_Y + "");
		List list = daoUtils.executeSqlQuery(sql);
		List<QCResult> qcResultList = new ArrayList<QCResult>();
		if (list != null && list.size() > 0) {
			for(int i = 0; i < list.size(); i++) {				
				Object[] objs = (Object[]) list.get(i);
				if (objs[0] != null && objs[1] != null) {
					QCResult result = new QCResult();
					result.setCheckItemCode(objs[0].toString());
					result.setCheckItemName(objs[1].toString());
					qcResultList.add(result);
				}				
			}
		}
		return qcResultList;
	}
    
	/**
	 * <li>说明：查询当前工单的质量检查项分页列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 查询包装类
	 * @return 当前工单的质量检查项分页列表
	 * @throws BusinessException
	 */
	public Page<QCResult> findQCResultList(SearchEntity<QCResult> searchEntity) throws BusinessException {
		String sql = SqlMapUtil.getSql("jxgc-qc:findQCResultList").replace("#workCardIDX#", searchEntity.getEntity().getWorkCardIDX());
		Order[] orders = searchEntity.getOrders();
		String totalSql = "select count(1) from (" + sql + ")";
		return findPageList(totalSql.toString(), sql, searchEntity.getStart(), searchEntity.getLimit(), null, orders);      
    }

	/**
     * <li>说明：保存质量检查项结果
     * <li>创建人：程锐
     * <li>创建日期：2014-12-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qc 质检项对象数组
     * @param relationIDX 质量检查项配置在作业工单上则为workCardIDX，质量检查项配置在作业任务上则为workTaskIDX
     * @param workCardIDX 作业工单IDX
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
	public void saveQCData(QCResult[] qc, String relationIDX, String workCardIDX) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, BusinessException, NoSuchFieldException {
		String hql = "delete QCResult where relationIDX = ?";
        daoUtils.execute(hql, workCardIDX);
        QCResult qcr = null;
        for(QCResult entity : qc){
        	// 根据“检查项编码”获取【质量检查项】
        	JCQCItemDefine qcItem = jCQCItemDefineManager.getModelByQCItemNo(entity.getCheckItemCode());
            qcr = new QCResult();
            qcr.setCheckItemCode(qcItem.getQCItemNo());					// 检查项编码
            qcr.setCheckItemName(qcItem.getQCItemName());				// 检查项名称
            qcr.setCheckWay(qcItem.getCheckWay());						// 检验方式（抽检|必检）
            qcr.setSeqNo(qcItem.getSeqNo());							// 顺序号
            qcr.setRelationIDX(relationIDX);							// 关联主键
            qcr.setWorkCardIDX(workCardIDX);							// 作业卡主键
            qcr.setStatus(QCResult.STATUS_WKF);							// 状态
            qcr.setIsAssign(qcItem.getIsAssign());						// 是否指派
            this.saveOrUpdate(qcr);
        }
	}

	/** 
	 * 根据“作业卡主键”和“检查项编码”查询【机车检修质量检验结果】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-27
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workCardIDX 作业工单IDX
	 * @param checkItemCode 质检项编码
	 * @return 机车检修质量检验结果
	 */
	public QCResult getModel(String workCardIDX, String checkItemCode) {
		String hql = "From QCResult Where recordStatus = 0 And workCardIDX = ? And checkItemCode = ?";
		return (QCResult) this.daoUtils.findSingle(hql, new Object[]{workCardIDX, checkItemCode});
	}
    
    /**
     * <li>说明： 根据“作业卡主键”和“是否指派”查询机车检修质量检验结果集合
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业卡主键
     * @param isAssign 是否指派. 1：需要指派（JCQCItemDefine.CONST_INT_IS_ASSIGN_Y），0：无需指派(JCQCItemDefine.CONST_INT_IS_ASSIGN_N)
     * @return 机车检修质量检验结果集合
     */
    @SuppressWarnings("unchecked")
    public List<QCResult> getModels(String workCardIDX, Integer isAssign) {
        String hql = "From QCResult Where recordStatus = 0 And workCardIDX = ? And isAssign = ?";
        return this.daoUtils.find(hql, new Object[]{workCardIDX, isAssign});
    }
    
    /**
     * <li>说明： 根据“作业卡主键”查询机车检修质量检验结果集合
     * <li>创建人：何涛
     * <li>创建日期：2016-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业卡主键
     * @return  机车检修质量检验结果集合
     */
    @SuppressWarnings("unchecked")
    public List<QCResult> getModelsByWorkCardIDX(String workCardIDX) {
        String hql = "From QCResult Where recordStatus = 0 And workCardIDX = ?";
        return this.daoUtils.find(hql, new Object[]{workCardIDX});
    }
    
	/**
     * <li>说明：查询工单有无必检质量检查项
     * <li>创建人：程锐
     * <li>创建日期：2014-11-29
     * <li>修改人：何涛
     * <li>修改日期：2016-04-13
     * <li>修改内容：代码重构
     * @param workCardIDX 作业工单IDX
     * @return true 工单有必检质量检查项 false 无
     */
	@SuppressWarnings("unchecked")
    public boolean checkHasBJQC(String workCardIDX) {
        String hql = "From QCResult Where recordStatus = 0 And workCardIDX = ? And checkWay = ?";
        return this.daoUtils.getCount(hql, new Object[]{ workCardIDX, JCQCItemDefine.CONST_INT_CHECK_WAY_BJ}) > 0;
	}
    
    /**
     * <li>方法说明：检查是否有质量检查流程 
     * <li>方法名称：checkHasQCFlow
     * <li>@param cardIdx
     * <li>@return
     * <li>return: boolean
     * <li>创建人：张凡
     * <li>创建时间：2014-3-27 下午04:34:36
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public boolean checkHasQCFlow(String cardIdx){      
        if(StateRecord.HasValue()){
            StateRecord.Remove();
            return false;
        }       
      //查询三检一验
        String sql = SqlMapUtil.getSql("jxgc-gdgl:findHasQC").replace("工单主键", cardIdx);        
        int qcCount = 0;
        List<BigDecimal> qccount = this.getSQLQuery(sql);            
        qcCount = qccount.get(0).intValue();                            
        return qcCount > 0;
    }

    /**
     * <li>说明：保存质量检查相关信息，开放质量检查，包含对质量检查可参与人员的存储
     * <li>创建人：何涛
     * <li>创建日期：2016-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 机车检修作业工单实体
     * @param result 质量检验结果值对象
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveAndOpenQCResult(WorkCard workCard, QCResultVO[] result) throws BusinessException, NoSuchFieldException {
        // 保存无需指派的质量检查参与者
        this.qcParticipantManager.saveNotAssignParticiant(workCard);
        // 保存指派的质量检查参与者
        if (null != result && result.length > 0) {
            this.qcParticipantManager.saveIsAssignParticiant(workCard, result);
        }
        // 根据作业卡IDX开放质量检查项
        updateOpenQCResultByWorkCardIDX(workCard.getIdx());
    }
    
    /** 
     * 根据“作业卡主键”和“检查项编码”查询【机车检修质量检验结果】
     * <li>创建人：林欢
     * <li>创建日期：2016-04-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单IDX
     * @return 机车检修质量检验结果
     */
    @SuppressWarnings("unchecked")
    public List<QCResult> getModelList(String workCardIDX) {
        String hql = "From QCResult Where recordStatus = 0 And workCardIDX = ? ";
        return this.daoUtils.find(hql, new Object[]{workCardIDX});
    }
    
    /**
     * <li>说明：联合分页查询，查询查询质量检查参与人员
     * <li>创建人：何涛
     * <li>创建日期：2016-05-06
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件封装实体
     * @return 质量检查实体集合
     */
    public Page<QCResultBean> acquirePageList(SearchEntity<QCResult> searchEntity) {
        StringBuilder sb = new StringBuilder("SELECT T.*, (SELECT WM_CONCAT(P.CHECK_PERSON_NAME) FROM JXGC_QC_PARTICIPANT P WHERE P.WORK_CARD_IDX = T.WORK_CARD_IDX AND P.CHECK_ITEM_CODE = T.CHECK_ITEM_CODE) AS \"QC_Participants\" FROM JXGC_QC_RESULT T WHERE T.RECORD_STATUS = 0");
        QCResult entity = searchEntity.getEntity();
        // 查询条件 - 作业工单idx主键
        if (!StringUtil.isNullOrBlank(entity.getWorkCardIDX())) {
            sb.append(" AND WORK_CARD_IDX = '").append(entity.getWorkCardIDX()).append("'");
        }
        sb.append(" ORDER BY T.SEQ_NO");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM", sb.indexOf("FROM") + 1));
        return queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, QCResultBean.class);
    }
    
    /**
     * <li>标题: 机车检修管理信息系统
     * <li>说明: QCResult查询实体类, 联合查询质量检查可参与人员
     * <li>创建人：何涛
     * <li>创建日期：2016-5-6
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部检修系统项目组
     * @version 1.0
     */
    @Entity
    public static final class QCResultBean {
        
        /* idx主键 */
        @Id
        private String idx;
        
        /* 关联主键（关联作业卡、作业任务主键，根据类型进行区分） */
        @Column(name = "Relation_IDX")
        private String relationIDX;
        
        /* 作业卡主键 */
        @Column(name = "Work_Card_IDX")
        private String workCardIDX;
        
        /* 检验项编码 */
        @Column(name = "Check_Item_Code")
        private String checkItemCode;
        
        /* 检验项名称 */
        @Column(name = "Check_Item_Name")
        private String checkItemName;
        
        /* 抽检/必检 */
        @Column(name = "Check_Way")
        private Integer checkWay;
        
        /* 是否指派 */
        @Column(name = "Is_Assign")
        private Integer isAssign;
        
        /* 顺序号 */
        @Column(name = "Seq_No")
        private Integer seqNo;
        
        /* 检验人员ID */
        @Column(name = "QC_EmpID")
        private Long qcEmpID;
        
        /* 检验人员名称 */
        @Column(name = "QC_EmpName")
        private String qcEmpName;
        
        /* 检验时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "QC_Time")
        private java.util.Date qcTime;
        
        /* 备注 */
        private String remarks;
        
        /* 检验状态（0:未开放;1:待处理;2:已处理;3已终止） */
        private Integer status;
        
        /* 已逗号分隔的质量检查可参与人员 */
        @Column(name = "QC_Participants")
        private String qcParticipants;
        
        public String getCheckItemCode() {
            return checkItemCode;
        }
        
        public void setCheckItemCode(String checkItemCode) {
            this.checkItemCode = checkItemCode;
        }
        
        public String getCheckItemName() {
            return checkItemName;
        }
        
        public void setCheckItemName(String checkItemName) {
            this.checkItemName = checkItemName;
        }
        
        public Integer getCheckWay() {
            return checkWay;
        }
        
        public void setCheckWay(Integer checkWay) {
            this.checkWay = checkWay;
        }
        
        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public Integer getIsAssign() {
            return isAssign;
        }
        
        public void setIsAssign(Integer isAssign) {
            this.isAssign = isAssign;
        }
        
        public Long getQcEmpID() {
            return qcEmpID;
        }
        
        public void setQcEmpID(Long qcEmpID) {
            this.qcEmpID = qcEmpID;
        }
        
        public String getQcEmpName() {
            return qcEmpName;
        }
        
        public void setQcEmpName(String qcEmpName) {
            this.qcEmpName = qcEmpName;
        }
        
        public String getQcParticipants() {
            return qcParticipants;
        }
        
        public void setQcParticipants(String qcParticipants) {
            this.qcParticipants = qcParticipants;
        }
        
        public java.util.Date getQcTime() {
            return qcTime;
        }
        
        public void setQcTime(java.util.Date qcTime) {
            this.qcTime = qcTime;
        }
        
        public String getRelationIDX() {
            return relationIDX;
        }
        
        public void setRelationIDX(String relationIDX) {
            this.relationIDX = relationIDX;
        }
        
        public String getRemarks() {
            return remarks;
        }
        
        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
        
        public Integer getSeqNo() {
            return seqNo;
        }
        
        public void setSeqNo(Integer seqNo) {
            this.seqNo = seqNo;
        }
        
        public Integer getStatus() {
            return status;
        }
        
        public void setStatus(Integer status) {
            this.status = status;
        }
        
        public String getWorkCardIDX() {
            return workCardIDX;
        }
        
        public void setWorkCardIDX(String workCardIDX) {
            this.workCardIDX = workCardIDX;
        }
        
    }

    /**
     * <li>说明：根据检修作业计划主键idx查询作业计划下所有完成的质量检查个数
     * <li>创建人：林欢
     * <li>创建日期：2016-6-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业计划主键idx
     * @return Integer 已经完成的质量检测个数
     */
    public Integer findFinishCountByRdpIDX(String rdpIDX) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select count(a) from WorkPlanRepairActivity a, WorkCard b,QCResult c ");
        sb.append(" where a.idx = b.repairActivityIDX ");
        sb.append(" and a.recordStatus = 0 ");
        sb.append(" and b.recordStatus = 0 ");
        sb.append(" and c.recordStatus = 0 ");
        sb.append(" and c.workCardIDX = b.idx ");
        sb.append(" and c.qcEmpID is not null ");
        sb.append(" and a.workPlanIDX = '").append(rdpIDX).append("'");
        
        List<Long> list = (List<Long>) this.find(sb.toString());
        if (list != null && list.size() > 0) {
            return Integer.valueOf(String.valueOf(list.get(0)));
        }else {
            return 0;
        }
    }

    /**
     * <li>说明：返修方法记录日志
     * <li>创建人：张迪
     * <li>创建日期：2016-9-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpWorkCardIDXs
     * @param qcItemNo
     * @throws Exception
     */
    public void updateToBack(String[] rdpWorkCardIDXs, String qcItemNo) throws Exception {
        // 增加一个临时列表，用于记录已返修的记录卡主键，防止多次返修
        List<String> list = new ArrayList<String>();
        WorkCard entity = new WorkCard();
        if (rdpWorkCardIDXs == null || rdpWorkCardIDXs.length < 1)
            return;
        entity = workCardManager.getModelById(rdpWorkCardIDXs[0]);
        for (String rdpWorkCardIDX : rdpWorkCardIDXs) {
            if (!list.contains(rdpWorkCardIDX)) {
                list.add(rdpWorkCardIDX);  
                QCResult  qcResult = getModel(rdpWorkCardIDX, qcItemNo);
                TrainRdpQCBackRepairLog trainRdpQCBackRepairLog = new TrainRdpQCBackRepairLog();
                trainRdpQCBackRepairLog.setRdpIDX(entity.getIdx());
                trainRdpQCBackRepairLog.setCheckItemCode(qcItemNo);
                trainRdpQCBackRepairLog.setCheckItemName(qcResult.getCheckItemName());
                trainRdpQCBackRepairLog.setQcResultIDX(qcResult.getIdx());
                trainRdpQCBackRepairLog.setWorkCardIDX(rdpWorkCardIDX);
                trainRdpQCBackRepairLogManager.saveOrUpdate(trainRdpQCBackRepairLog);  // 保存返修记录日志   
            }
        }      
    }
    
    /**
     * <li>说明：通过记录卡查询质量检查项
     * <li>创建人：张迪
     * <li>创建日期：2016-9-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 记录卡idx
     * @return 质量检查项结果集
     */
    @SuppressWarnings("unchecked")
    public List<QCResult> getListByWorkTask(String workCardIDX) {     
        String hql = "select new QCResult(a.idx, a.relationIDX, a.workCardIDX, a.checkItemCode, a.checkItemName, a.qcEmpID, a.qcEmpName, a.status)" +
                     " from QCResult a where recordStatus = 0 and workCardIDX = '" + workCardIDX + "'";
        return daoUtils.find(hql);
    }
    
}