package com.yunda.jx.jxgc.workplanmanage.manager;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeUpdateApply;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeUpdateApplyBean;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：张迪
 * <li>创建日期：2017-1-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */

@Service(value = "jobProcessNodeUpdateApplyManager")
public class JobProcessNodeUpdateApplyManager extends JXBaseManager<JobProcessNodeUpdateApply, JobProcessNodeUpdateApply> implements IbaseCombo{

    /** 机车检修作业计划-流程节点业务类 */
    @Resource
    private JobProcessNodeNewManager jobProcessNodeNewManager;
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-1-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点idx
     * @return 申请节点信息
     */
    public JobProcessNodeUpdateApply getEntityByNodeIDX(String nodeIDX) {
        String hql = " from JobProcessNodeUpdateApply where  editStatus = 1 and nodeIDX ='" + nodeIDX + "' order by updateTime desc";
        return (JobProcessNodeUpdateApply) daoUtils.findSingle(hql);
    }
    /**
     * <li>说明：vis界面上拖动节点申请延期 修改时间
     * <li>创建人：张迪
     * <li>创建日期：2017-1-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param node 节点信息
     * @throws Exception
     */
    public void updateLeafNodeTimeApply(JobProcessNodeUpdateApply nodeApply) throws Exception {
        JobProcessNode oldNode = jobProcessNodeNewManager.getModelById(nodeApply.getNodeIDX());
       // 判断节点是否已经处理
        if(!(JobProcessNode.STATUS_UNSTART.equals(oldNode.getStatus()) || JobProcessNode.STATUS_GOING.equals(oldNode.getStatus()))){
            throw new BusinessException("节点-[" + oldNode.getNodeName() + "] 已经处理不能修改时间 ！");
        }
        JobProcessNodeUpdateApply oldNodeApply = new JobProcessNodeUpdateApply(); 
        // 判断是新增还是修改  
        if(StringUtil.isNullOrBlank(nodeApply.getIdx())){  
            nodeApply.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_WAIT);
            nodeApply.setUpdator(SystemContext.getAcOperator().getOperatorid());
            nodeApply.setUpdateTime(new Date());
            nodeApply.setCreator(SystemContext.getAcOperator().getOperatorid());
            nodeApply.setCreateTime(new Date());
            this.saveOrUpdate(nodeApply);
        }else{
            oldNodeApply = this.getModelById(nodeApply.getIdx());
            oldNodeApply.setNewPlanBeginTime(nodeApply.getNewPlanBeginTime());
            oldNodeApply.setNewPlanEndTime(nodeApply.getNewPlanEndTime());
            oldNodeApply.setPlanBeginTime(nodeApply.getPlanBeginTime());
            oldNodeApply.setPlanEndTime(nodeApply.getPlanEndTime());
            oldNodeApply.setReason(nodeApply.getReason());
            this.saveOrUpdate(oldNodeApply);
        }
        oldNode.setNewPlanBeginTime(nodeApply.getNewPlanBeginTime());
        oldNode.setNewPlanEndTime(nodeApply.getNewPlanEndTime());
        oldNode.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_WAIT);
        jobProcessNodeNewManager.saveOrUpdate(oldNode);
    }
    
    /**
     * <li>说明：查询父节点下所有子节点申请延期的节点记录
     * <li>创建人：张迪
     * <li>创建日期：2017-1-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点idx
     * @return 申请列表 
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeUpdateApply> findChildrenNodeApplyList(String parentIDX, String editStatus) {
    
        String hql = " from JobProcessNodeUpdateApply where " +
                " nodeIDX in( select idx from JobProcessNode where parentIDX ='" + parentIDX + "')";
        if(!StringUtil.isNullOrBlank(editStatus)){
            hql += " and editStatus =" + editStatus;
        }
        hql += " order by updateTime desc ";
        return daoUtils.find(hql);
    }
    /**
     * <li>说明：段调度审批申请记录
     * <li>创建人：张迪
     * <li>创建日期：2017-1-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param applyNodeEntity 申请实体
     * @throws Exception 
     */
    public void updateConfirmApply(JobProcessNodeUpdateApply applyNodeEntity) throws Exception {
        String idx = applyNodeEntity.getIdx();
        String nodeIDX = applyNodeEntity.getNodeIDX();
        String opinions = applyNodeEntity.getOpinions();   // 审核意见     
        Integer editStatus = applyNodeEntity.getEditStatus();        
        JobProcessNode node = jobProcessNodeNewManager.getModelById(nodeIDX);
        JobProcessNodeUpdateApply applyNode = this.getModelById(idx);
        if(editStatus == JobProcessNodeUpdateApply.EDIT_STATUS_ON){       // 同意申请
            node.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_ON);
            node.setPlanBeginTime(node.getNewPlanBeginTime());
            node.setPlanEndTime(node.getNewPlanEndTime());
            node.setNewPlanBeginTime(null);
            node.setNewPlanEndTime(null);
            node.setRatedWorkMinutes(jobProcessNodeNewManager.calWorkMinutes(node));
            applyNode.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_ON);
            
        }else if(editStatus == JobProcessNodeUpdateApply.EDIT_STATUS_UN){  // 取消申请
            node.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_UN);  
            applyNode.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_UN);
        }             
        applyNode.setApprovalDate(new Date());
        applyNode.setApprovalEmpID(applyNodeEntity.getApprovalEmpID());
        applyNode.setApprovalEmpName(applyNodeEntity.getApprovalEmpName());
        applyNode.setOpinions(opinions);
        jobProcessNodeNewManager.saveOrUpdate(node);  // 更新流程节点状态
        this.saveOrUpdate(applyNode); // 更新申请状态
     }

    /**
     * <li>说明：查找节点所对应的申请记录
     * <li>创建人：张迪
     * <li>创建日期：2017-1-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param applyNode 节点申请记录
     * @return 申请列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeUpdateApplyBean> findNodeApplyList(JobProcessNodeUpdateApply applyNode) {
        String nodeIDX = applyNode.getNodeIDX();
        StringBuilder sb = new StringBuilder("from JobProcessNodeUpdateApply  where nodeIDX = ?");
        if(!StringUtil.isNullOrBlank(applyNode.getEditStatusStr())){
            sb.append(" And editStatus In (").append(applyNode.getEditStatusStr()).append(")");
        }
        sb.append(" order by updateTime desc ");
        return daoUtils.find(sb.toString(), nodeIDX);
    }
    /**
     * <li>说明：流程节点保存时更新申请记录
     * <li>创建人：张迪
     * <li>创建日期：2017-1-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param oldNode 节点实体
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveOrUpdateWaitConfirm(JobProcessNode oldNode) throws BusinessException, NoSuchFieldException {
        String nodeIDX = oldNode.getIdx();
        JobProcessNodeUpdateApply applyNode = getEntityByNodeIDX(nodeIDX);
        if(null == applyNode){
            applyNode = new  JobProcessNodeUpdateApply();
            applyNode.setNodeIDX(nodeIDX);
            applyNode.setPlanBeginTime(oldNode.getPlanBeginTime());
            applyNode.setPlanEndTime(oldNode.getPlanEndTime());
            applyNode.setNewPlanBeginTime(oldNode.getNewPlanBeginTime());
            applyNode.setNewPlanEndTime(oldNode.getNewPlanEndTime());
            applyNode.setReason(oldNode.getReason());
            applyNode.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_WAIT);
        }
        applyNode.setNodeName(oldNode.getNodeName());
        this.saveOrUpdate(applyNode);
    }
    
    /**
     * <li>说明：段调度调整更新大节点时间，取消所有子节点的延期申请
     * <li>创建人：张迪
     * <li>创建日期：2017-2-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param node 节点
     * @throws Exception
     */
    public void cancelApplyRecord(JobProcessNode node ) throws Exception {
        List<JobProcessNodeUpdateApply> nodeApplyRecords = findChildrenNodeApplyList(node.getIdx(), JobProcessNodeUpdateApply.EDIT_STATUS_WAIT.toString());
        if(null == nodeApplyRecords || nodeApplyRecords.size()<0) return;
        for(JobProcessNodeUpdateApply nodeApplyRecord: nodeApplyRecords){
            nodeApplyRecord.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_UN); 
            nodeApplyRecord.setOpinions("取消，段调度已调整时间");
            this.updateConfirmApply(nodeApplyRecord);
        }
    }
    /**
     * <li>说明：父节点申请延期审批
     * <li>创建人：张迪
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 子节点延期申请的主键idx
     * @param 申请说明
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws ParseException 
     */
    public void saveApproveParentNode(String idx, String reason) throws BusinessException, NoSuchFieldException, ParseException {
        JobProcessNodeUpdateApply applyNode = this.getModelById(idx);
        if( null == applyNode || !((JobProcessNodeUpdateApply.EDIT_STATUS_WAIT).equals(applyNode.getEditStatus()))){
            throw new BusinessException("节点已经处理不能重复操作 ！");
        }
        JobProcessNode parentNode = jobProcessNodeNewManager.getApproveParentNode(applyNode.getNodeIDX());
        parentNode.setReason(reason);
        parentNode.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_WAIT);
        saveOrUpdateWaitConfirm(parentNode);
        // 同时修改子节点状态
        applyNode.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_WAIT);
        this.saveOrUpdate(applyNode);
    }
    
    /**
     * <li>说明：查询机车作业计划下所有大节点的延期申请
     * <li>创建人：张迪
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划idx
     * @return  申请列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeUpdateApply> findFirstNodeApplyList(String workPlanIDX) {
        String hql = " from JobProcessNodeUpdateApply where " +
                " nodeIDX in( select idx from JobProcessNode where (parentIDX is null OR parentIDX = 'ROOT_0') and workPlanIDX = '" + workPlanIDX + "')";
        hql += " order by updateTime desc ";
        return daoUtils.find(hql);
    }
    /**
     * <li>说明：取消段调度申请，取消所有子节点的延期申请
     * <li>创建人：张迪
     * <li>创建日期：2017-2-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param node 段调度第一级节点的延期申请
     * @throws Exception
     */
    public void cancelFirstNodeApplyRecord(JobProcessNodeUpdateApply firstNode ) throws Exception {
        // 查询所有子节点的延期申请
        List<JobProcessNodeUpdateApply> nodeApplyRecords = findChildrenNodeApplyList(firstNode.getNodeIDX(), JobProcessNodeUpdateApply.EDIT_STATUS_WAIT.toString());
        if(null == nodeApplyRecords || nodeApplyRecords.size()<0) return;
        for(JobProcessNodeUpdateApply nodeApplyRecord: nodeApplyRecords){
            nodeApplyRecord.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_UN); 
            nodeApplyRecord.setOpinions(firstNode.getOpinions());
            this.updateConfirmApply(nodeApplyRecord);
        }
    }
    /**
     * <li>说明：审批第一级节点的延期申请，确认或取消
     * <li>创建人：张迪
     * <li>创建日期：2017-4-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param applyNodeEntity 申请节点
     * @throws Exception
     */
    public void updateFirstNodeApply(JobProcessNodeUpdateApply applyNodeEntity) throws Exception {
        String idx = applyNodeEntity.getIdx();
        String nodeIDX = applyNodeEntity.getNodeIDX();
        String opinions = applyNodeEntity.getOpinions();   // 审核意见     
        Integer editStatus = applyNodeEntity.getEditStatus();        
        JobProcessNode node = jobProcessNodeNewManager.getModelById(nodeIDX);
        JobProcessNodeUpdateApply applyNode = this.getModelById(idx);
        applyNode.setApprovalDate(new Date());
        applyNode.setApprovalEmpID(applyNodeEntity.getApprovalEmpID());
        applyNode.setApprovalEmpName(applyNodeEntity.getApprovalEmpName());
        applyNode.setOpinions(opinions);
        if(editStatus == JobProcessNodeUpdateApply.EDIT_STATUS_ON){       // 同意申请
            node.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_ON);
            node.setPlanBeginTime(applyNodeEntity.getNewPlanBeginTime());
            node.setPlanEndTime(applyNodeEntity.getNewPlanEndTime());
            node.setNewPlanBeginTime(null);
            node.setNewPlanEndTime(null);
            node.setRatedWorkMinutes(jobProcessNodeNewManager.calWorkMinutes(node));
            applyNode.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_ON);
            jobProcessNodeNewManager.saveOrUpdateFirstNodeAndRel(node, null,true);  // 更新流程节点状态
            this.saveOrUpdate(applyNode); // 更新申请状态
        }else if(editStatus == JobProcessNodeUpdateApply.EDIT_STATUS_UN){  // 取消申请
            node.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_UN);  
            applyNode.setEditStatus(JobProcessNodeUpdateApply.EDIT_STATUS_UN);
            // 取消所有子节点的申请
            cancelFirstNodeApplyRecord(applyNode);
        }             
     }

}
