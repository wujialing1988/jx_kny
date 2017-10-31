package com.yunda.jx.jxgc.processdef.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.processdef.decorateentity.JobNodeExtConfigDefDec;
import com.yunda.jx.jxgc.processdef.decorateentity.JobProcessNodeDefDec;
import com.yunda.jx.jxgc.processdef.decorateentity.JobProcessNodeDefEntityDec;
import com.yunda.jx.jxgc.processdef.decorateentity.JobProcessNodeRelDefDec;
import com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef;
import com.yunda.jx.jxgc.processdef.entity.JobNodeStationDef;
import com.yunda.jx.jxgc.processdef.entity.JobNodeUnionWorkSeq;
import com.yunda.jx.jxgc.processdef.entity.JobProcessDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeRelDef;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessDef业务类,检修作业流程
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="jobProcessDefCopyManager")
public class JobProcessDefCopyManager extends JXBaseManager<JobProcessDef, JobProcessDef> {
    
    /** JobProcessNodeDef业务类 */
    @Resource
    private JobProcessNodeDefManager jobProcessNodeDefManager;
    
    /** JobProcessNodeRelDef业务类,节点前后置关系 */
    @Resource
    private JobProcessNodeRelDefManager jobProcessNodeRelDefManager;
    
//    /** JobNodeProjectDefManager业务类,关联作业项目 */
//    @Resource
//    private JobNodeProjectDefManager jobNodeProjectDefManager;
    
    /** JobNodeStationDefManager业务类,关联作业工位 */
    @Resource
    private JobNodeStationDefManager jobNodeStationDefManager;
    
    /** CodeRuleConfig业务类,业务编码规则配置 */
    @Resource
    CodeRuleConfigManager codeRuleConfigManager;
    
    /** JobNodeExtConfigDefManager业务类,扩展配置 */
    @Resource
    private  JobNodeExtConfigDefManager jobNodeExtConfigDefManager;
    
    /** 业务类 作业节点所挂记录卡 */
    @Resource
    private  JobNodeUnionWorkSeqManager jobNodeUnionWorkSeqManager;
    
    /**
     * 
     * <li>说明：复制流程
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 需复制的流程id
     */
    public void copyJobProcessDef(String idx){
        try {
            JobProcessDef oldProcess = getModelById(idx) ;
            JobProcessDef newProcess = new JobProcessDef();
            
            BeanUtils.copyProperties(newProcess, oldProcess) ;
            
            newProcess.setIdx("") ;
            //生成新的流程编码
            newProcess.setProcessCode(codeRuleConfigManager.makeConfigRule(JobProcessDef.CODE_RULE_JOB_PROCESS_CODE));
            newProcess.setStatus(JobProcessDef.CONST_INT_STATUS_XZ);//流程状态为新增
            //复制检修作业流程
            saveOrUpdate(newProcess) ;
            // 获取同一个作业流程的所有作业节点
            List<JobProcessNodeDef> nodeList = jobProcessNodeDefManager.getModelsByProcessIDX(idx);
            Map<String, JobProcessNodeDefDec> nodeMap = new LinkedHashMap<String, JobProcessNodeDefDec>();
            //获取同一个作业流程的所有节点前后置关系
            List<JobProcessNodeRelDef> nodeRelList = jobProcessNodeRelDefManager.getModelsByProcessIDX(idx) ;
            //获取同一个作业流程的所有节点扩展配置信息
            List<JobNodeExtConfigDef> nodeExtConfigList = jobNodeExtConfigDefManager.getModelsByProcessIDX(idx) ;
            //前后置关系装饰类list
            List<JobProcessNodeRelDefDec> nodeRelDefDecList ;
            JobProcessNodeDefEntityDec nodeDefEntityDec ;  //流程节点实体装饰类
            JobProcessNodeRelDefDec nodeRelDefDec ;  //前后置关系装饰类
            JobProcessNodeDef newNode  ; //新节点实体对象
            JobProcessNodeDefDec nodeDefDec ;  //节点装饰类对象
            //扩展配置装饰类list
            List<JobNodeExtConfigDefDec> nodeExtConfigDefDecList ;
            //扩展配置装饰类对象
            JobNodeExtConfigDefDec nodeExtConfigDefDec ;
            for(JobProcessNodeDef nodeDef : nodeList){
                
                nodeDefEntityDec = new JobProcessNodeDefEntityDec();
                newNode = new JobProcessNodeDef() ;
                nodeDefDec = new JobProcessNodeDefDec();
                BeanUtils.copyProperties(newNode, nodeDef) ;
                
                //拷贝节点之获取最新的流程节点编码，通过公用方法获取生成新的流程编码
                String nodeCode = this.codeRuleConfigManager.makeConfigRule(JobProcessNodeDef.CODE_RULE_JOB_PROCESS_NODE_CODE);
                newNode.setNodeCode(nodeCode);
                
                newNode.setIdx(UUID.randomUUID().toString().replace("-", ""));  //设置新id
                newNode.setProcessIDX(newProcess.getIdx());//新流程主键
                nodeDefEntityDec.setNewNode(newNode) ;
                nodeDefEntityDec.setOldIdx(nodeDef.getIdx());
                nodeDefEntityDec.setOldParentIdx(nodeDef.getParentIDX());
                nodeDefDec.setNewNode(nodeDefEntityDec);//流程节点实体装饰类
                nodeRelDefDecList = new ArrayList<JobProcessNodeRelDefDec>();//前后置关系装饰类list
                
                //循环将前后置关系根据节点分开
                for(JobProcessNodeRelDef nodeRelDef : nodeRelList){
                    nodeRelDefDec = new JobProcessNodeRelDefDec();
                    //将该节点对应的前后置关系信息放到一个list中
                    if(nodeRelDef.getNodeIDX().equals(nodeDef.getIdx())){
                        nodeRelDefDec.setOldPreNodeIDX(nodeRelDef.getPreNodeIDX());
                        nodeRelDef.setIdx(UUID.randomUUID().toString().replace("-", ""));  //设置新id
                        nodeRelDef.setNodeIDX(newNode.getIdx());  //设置新的节点id
                        nodeRelDefDec.setNewNodeRelDef(nodeRelDef);
                        nodeRelDefDecList.add(nodeRelDefDec);
                    }
                }
                //该节点有前后置关系信息
                if(null != nodeRelDefDecList && nodeRelDefDecList.size() > 0){
                    nodeDefDec.setNewJobProcessNodeRelDefList(nodeRelDefDecList); //前后置关系装饰类List
                }
                nodeExtConfigDefDecList = new ArrayList<JobNodeExtConfigDefDec>();//扩展配置装饰类list
                //循环将扩展配置信息根据节点分开
                for(JobNodeExtConfigDef nodeExtConfigDef : nodeExtConfigList){
                    nodeExtConfigDefDec = new JobNodeExtConfigDefDec();
                    //将该节点对应的扩展配置信息放到一个list中
                    if(nodeExtConfigDef.getNodeIDX().equals(nodeDef.getIdx())){
                        nodeExtConfigDef.setIdx(UUID.randomUUID().toString().replace("-", ""));  //设置新id
                        nodeExtConfigDef.setNodeIDX(newNode.getIdx());  //设置新的节点id
                        nodeExtConfigDefDec.setNewNodeExtDef(nodeExtConfigDef) ;
                        nodeExtConfigDefDecList.add(nodeExtConfigDefDec);
                    }
                }
                //该节点有扩展配置信息
                if(null != nodeExtConfigDefDecList && nodeExtConfigDefDecList.size() > 0){
                    nodeDefDec.setNewJobNodeExtConfigDefList(nodeExtConfigDefDecList); //扩展配置装饰类List
                }
                nodeMap.put(nodeDef.getIdx(), nodeDefDec) ;
            }
            //复制流程节点
            copyProcessNodeDef(nodeMap);
            
        
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * <li>说明：复制检修流程下的节点信息
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeMap 装饰类map对象
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    private void copyProcessNodeDef( Map<String, JobProcessNodeDefDec> nodeMap ) throws NoSuchFieldException {
        Set<Map.Entry<String, JobProcessNodeDefDec>> set = nodeMap.entrySet();
        Iterator it = set.iterator();
        //流程节点装饰类
        JobProcessNodeDefDec value ;
        //前后置关系装饰类List
        List<JobProcessNodeRelDefDec> newJobProcessNodeRelDefList ;
        //扩展配置装饰类List
        List<JobNodeExtConfigDefDec> newJobNodeExtConfigDefList ;
        //流程节点实体装饰类
        JobProcessNodeDefEntityDec newNodeDec;
        while (it.hasNext()) {
                 value = new JobProcessNodeDefDec() ;
                 newJobNodeExtConfigDefList = new ArrayList<JobNodeExtConfigDefDec>();
                 newNodeDec = new JobProcessNodeDefEntityDec();
                 Map.Entry<String, JobProcessNodeDefDec> entry = (Map.Entry<String, JobProcessNodeDefDec>) it.next();
                 value = entry.getValue();  
                 //获取装饰类信息
                 newNodeDec = value.getNewNode();
                 newJobNodeExtConfigDefList = value.getNewJobNodeExtConfigDefList();//获取装饰类中扩展配置信息
                 //TODO 父子级关系and前后置关系
                 JobProcessNodeDef newNode  = newNodeDec.getNewNode();
                 //父节点id不为“ROOT_0”，即有父节点信息
                 if(!"ROOT_0".equals(newNode.getParentIDX())){
                     //找到父节点对应的新节点信息
                     JobProcessNodeDefDec newNodeDefDec = nodeMap.get(newNode.getParentIDX());
                     newNode.setParentIDX(newNodeDefDec.getNewNode().getNewNode().getIdx());  //更新新节点id
                 }
                 //保存复制后的流程节点
                 newNode = EntityUtil.setSysinfo(newNode) ;
//                 jobProcessNodeDefManager.insert(newNode);
                 this.daoUtils.getHibernateTemplate().save(newNode);
                 //newJobNodeExtConfigDefList不为空，即该节点有扩展配置信息
                 if(null != newJobNodeExtConfigDefList && newJobNodeExtConfigDefList.size() > 0){
                     for(JobNodeExtConfigDefDec newNodeExtConfigDec : newJobNodeExtConfigDefList){
                         JobNodeExtConfigDef newNodeExtDef = newNodeExtConfigDec.getNewNodeExtDef(); //获取装饰类中的扩展配置对象
                         newNodeExtDef.setNodeIDX(newNode.getIdx()); 
                         newNodeExtDef = EntityUtil.setSysinfo(newNodeExtDef) ;
                         jobNodeExtConfigDefManager.insert(newNodeExtDef) ;  //保存复制后的扩展配置信息
                     }
                 }
                 //叶子节点
                 if(JobProcessNodeDef.CONST_INT_IS_LEAF_NO != newNode.getIsLeaf()){
                     //是叶子节点,复制关联检修项目
                     copyJobNodeUnionWorkSeq(newNodeDec.getOldIdx() , newNode.getIdx());
                     //复制关联作业工位
                     copyStationDef(newNodeDec.getOldIdx() , newNode.getIdx());
                 }
        }
        Set<Map.Entry<String, JobProcessNodeDefDec>> setV = nodeMap.entrySet();
        Iterator itV = setV.iterator();
        while (itV.hasNext()) {
            value = new JobProcessNodeDefDec() ;
            newJobProcessNodeRelDefList = new ArrayList<JobProcessNodeRelDefDec>();
            newNodeDec = new JobProcessNodeDefEntityDec();
            Map.Entry<String, JobProcessNodeDefDec> entry = (Map.Entry<String, JobProcessNodeDefDec>) itV.next();
            value = entry.getValue();  
            //获取装饰类信息
            newJobProcessNodeRelDefList = value.getNewJobProcessNodeRelDefList();
            newNodeDec = value.getNewNode();
            //TODO 父子级关系and前后置关系
            JobProcessNodeDef newNode  = newNodeDec.getNewNode();
            //newJobProcessNodeRelDefList不为空，即该节点有前后置关系信息
            if(null != newJobProcessNodeRelDefList && newJobProcessNodeRelDefList.size() > 0){
                for(JobProcessNodeRelDefDec newNodeRelDec : newJobProcessNodeRelDefList){
                    JobProcessNodeRelDef newNodeRelDef = newNodeRelDec.getNewNodeRelDef();
                    if(!StringUtil.isNullOrBlank(newNodeRelDef.getPreNodeIDX())){
                        //找到前置节点id对应的新节点信息
                        JobProcessNodeDefDec newNodeDefDec = nodeMap.get(newNodeRelDef.getPreNodeIDX());
                        newNodeRelDef.setPreNodeIDX(newNodeDefDec.getNewNode().getNewNode().getIdx());//更新新节点id
                    }
                    newNodeRelDef.setNodeIDX(newNode.getIdx()); 
                    newNodeRelDef = EntityUtil.setSysinfo(newNodeRelDef) ;
                    jobProcessNodeRelDefManager.insert(newNodeRelDef) ;  //保存复制后的前后置关系信息
                }
            }
   }
    }
    /**
     * 
     * <li>说明：复制节点关联的检修项目信息
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param oldNodeIdx 旧节点id
     * @param newNodeIdx 复制后的新节点id
     */
    public void copyProjectDef(String oldNodeIdx, String newNodeIdx){
//        try {
//            List<JobNodeProjectDef> projectDefList = jobNodeProjectDefManager.getModelByNodeIDX(oldNodeIdx);
//    //        JobNodeProjectDef projectDef = new JobNodeProjectDef();
//            if(null != projectDefList && projectDefList.size() > 0){
//                for(JobNodeProjectDef projectDef : projectDefList){
//                    projectDef.setIdx("");
//                    projectDef.setNodeIDX(newNodeIdx);
//                }
//                //复制关联检修项目
//                jobNodeProjectDefManager.saveOrUpdate(projectDefList);
//                
//            }
//        } catch (BusinessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }  
    
    /**
     * 
     * <li>说明：复制节点关联的检修的记录卡信息
     * <li>创建人：林欢
     * <li>创建日期：2016-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param oldNodeIdx 旧节点id
     * @param newNodeIdx 复制后的新节点id
     */
    public void copyJobNodeUnionWorkSeq(String oldNodeIdx, String newNodeIdx){
        try {
            
            //查询该节点所有信息
            List<JobNodeUnionWorkSeq> jobNodeUnionWorkSeqList = jobNodeUnionWorkSeqManager.getJobNodeUnionWorkSeqListById(oldNodeIdx);
            JobNodeUnionWorkSeq jobNodeUnionWorkSeq = null;
            
            if(null != jobNodeUnionWorkSeqList && jobNodeUnionWorkSeqList.size() > 0){
                for(JobNodeUnionWorkSeq job : jobNodeUnionWorkSeqList){
                    jobNodeUnionWorkSeq = new JobNodeUnionWorkSeq();
                    jobNodeUnionWorkSeq.setNodeIDX(newNodeIdx);
                    jobNodeUnionWorkSeq.setRecordCardIDX(job.getRecordCardIDX());
                    jobNodeUnionWorkSeqManager.saveOrUpdate(jobNodeUnionWorkSeq);
                }
                //复制关联检修项目
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }  
    
    /**
     * 
     * <li>说明：复制节点关联的作业工位
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param oldNodeIdx 旧节点id
     * @param newNodeIdx 复制后的新节点id
     */
    public void copyStationDef(String oldNodeIdx, String newNodeIdx){
        try {
            List<JobNodeStationDef> stationDefList = jobNodeStationDefManager.getModelByNodeIDX(oldNodeIdx);
            if(null != stationDefList && stationDefList.size() > 0){
                for(JobNodeStationDef stationDef : stationDefList){
                    stationDef.setIdx("");
                    stationDef.setNodeIDX(newNodeIdx);
                }
                //复制关联作业工位
                jobNodeStationDefManager.saveOrUpdate(stationDefList);
                
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    } 
}