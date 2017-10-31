package com.yunda.zb.zbfw.manager;

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
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
import com.yunda.util.BeanUtils;
import com.yunda.zb.zbfw.decorateentity.ZbglJobNodeExtConfigDefDec;
import com.yunda.zb.zbfw.decorateentity.ZbglJobProcessNodeDefDec;
import com.yunda.zb.zbfw.decorateentity.ZbglJobProcessNodeDefEntityDec;
import com.yunda.zb.zbfw.decorateentity.ZbglJobProcessNodeRelDefDec;
import com.yunda.zb.zbfw.entity.ZbFw;
import com.yunda.zb.zbfw.entity.ZbFwWi;
import com.yunda.zb.zbfw.entity.ZbFwWidi;
import com.yunda.zb.zbfw.entity.ZbglJobNodeExtConfigDef;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeDef;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeRelDef;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbFw业务类,整备作业范围
 * <li>创建人：程梅
 * <li>创建日期：2016-4-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="zbFwCopyManager")
public class ZbFwCopyManager extends JXBaseManager<ZbFw, ZbFw> {
    
    /** ZbglJobProcessNodeDef业务类 */
    @Resource
    private ZbglJobProcessNodeDefManager zbglJobProcessNodeDefManager;
    
    /** ZbglJobProcessNodeRelDef业务类,节点前后置关系 */
    @Resource
    private ZbglJobProcessNodeRelDefManager zbglJobProcessNodeRelDefManager;
    
    /** ZbFw业务类,整备范围 */
    @Resource
    private ZbFwManager zbFwManager;
    
    /** ZbFwWi业务类,关联作业项目 */
    @Resource
    private ZbFwWiManager zbFwWiManager;
    
    /** ZbFwWidi业务类,整备作业项目数据项 */
    @Resource
    private ZbFwWidiManager zbFwWidiManager;
    
    /** CodeRuleConfig业务类,业务编码规则配置 */
    @Resource
    CodeRuleConfigManager codeRuleConfigManager;
    
    /** ZbglJobNodeExtConfigDef业务类,扩展配置 */
    @Resource
    private  ZbglJobNodeExtConfigDefManager zbglJobNodeExtConfigDefManager;
    
    /**
     * 
     * <li>说明：复制流程
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fw 需复制的流程实体
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void copyJobProcessDef(ZbFw fw) throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException{
            ZbFw oldProcess = getModelById(fw.getIdx()) ;
            ZbFw newProcess = new ZbFw();
            
            BeanUtils.copyProperties(newProcess, oldProcess) ;
            
            newProcess.setIdx("") ;
            newProcess.setTrainTypeIDX(fw.getTrainTypeIDX());
            newProcess.setTrainTypeName(fw.getTrainTypeName());
            newProcess.setTrainTypeShortName(fw.getTrainTypeShortName());
            newProcess.setFwName(fw.getFwName());
            newProcess.setTrainVehicleCode(null);
            newProcess.setTrainVehicleName(null);
            //生成新的流程编码
            newProcess.setFwCode(codeRuleConfigManager.makeConfigRule(ZbFw.CODE_RULE_JOB_PROCESS_CODE));
            String[] errMsg = zbFwManager.validateUpdate(newProcess);
            if (null != errMsg) {
                for(String err : errMsg){
                    throw new BusinessException(err); 
                }
            }
            //复制整备作业流程
            saveOrUpdate(newProcess) ;
            // 获取同一个作业流程的所有作业节点
            List<ZbglJobProcessNodeDef> nodeList = zbglJobProcessNodeDefManager.getModelsByZbfwIDX(fw.getIdx());
            Map<String, ZbglJobProcessNodeDefDec> nodeMap = new LinkedHashMap<String, ZbglJobProcessNodeDefDec>();
            //获取同一个作业流程的所有节点前后置关系
            List<ZbglJobProcessNodeRelDef> nodeRelList = zbglJobProcessNodeRelDefManager.getModelsByZbfwIDX(fw.getIdx()) ;
            //获取同一个作业流程的所有节点扩展配置信息
            List<ZbglJobNodeExtConfigDef> nodeExtConfigList = zbglJobNodeExtConfigDefManager.getModelsByZbfwIDX(fw.getIdx()) ;
            //前后置关系装饰类list
            List<ZbglJobProcessNodeRelDefDec> nodeRelDefDecList ;
            ZbglJobProcessNodeDefEntityDec nodeDefEntityDec ;  //流程节点实体装饰类
            ZbglJobProcessNodeRelDefDec nodeRelDefDec ;  //前后置关系装饰类
            ZbglJobProcessNodeDef newNode  ; //新节点实体对象
            ZbglJobProcessNodeDefDec nodeDefDec ;  //节点装饰类对象
            //扩展配置装饰类list
            List<ZbglJobNodeExtConfigDefDec> nodeExtConfigDefDecList ;
            //扩展配置装饰类对象
            ZbglJobNodeExtConfigDefDec nodeExtConfigDefDec ;
            for(ZbglJobProcessNodeDef nodeDef : nodeList){
                nodeDefEntityDec = new ZbglJobProcessNodeDefEntityDec();
                newNode = new ZbglJobProcessNodeDef() ;
                nodeDefDec = new ZbglJobProcessNodeDefDec();
                BeanUtils.copyProperties(newNode, nodeDef) ;
                newNode.setIdx(UUID.randomUUID().toString().replace("-", ""));  //设置新id
                newNode.setZbfwIDX(newProcess.getIdx());//新范围主键
                nodeDefEntityDec.setNewNode(newNode) ;
                nodeDefEntityDec.setOldIdx(nodeDef.getIdx());
                nodeDefEntityDec.setOldParentIdx(nodeDef.getParentIDX());
                nodeDefDec.setNewNode(nodeDefEntityDec);//流程节点实体装饰类
                nodeRelDefDecList = new ArrayList<ZbglJobProcessNodeRelDefDec>();//前后置关系装饰类list
                
                //循环将前后置关系根据节点分开
                for(ZbglJobProcessNodeRelDef nodeRelDef : nodeRelList){
                    nodeRelDefDec = new ZbglJobProcessNodeRelDefDec();
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
                nodeExtConfigDefDecList = new ArrayList<ZbglJobNodeExtConfigDefDec>();//扩展配置装饰类list
                //循环将扩展配置信息根据节点分开
                for(ZbglJobNodeExtConfigDef nodeExtConfigDef : nodeExtConfigList){
                    nodeExtConfigDefDec = new ZbglJobNodeExtConfigDefDec();
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
    }

    /**
     * 
     * <li>说明：复制整备范围下的节点信息
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeMap 装饰类map对象
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    private void copyProcessNodeDef( Map<String, ZbglJobProcessNodeDefDec> nodeMap ) throws NoSuchFieldException {
        Set<Map.Entry<String, ZbglJobProcessNodeDefDec>> set = nodeMap.entrySet();
        Iterator it = set.iterator();
        //流程节点装饰类
        ZbglJobProcessNodeDefDec value ;
        //前后置关系装饰类List
        List<ZbglJobProcessNodeRelDefDec> newJobProcessNodeRelDefList ;
        //扩展配置装饰类List
        List<ZbglJobNodeExtConfigDefDec> newJobNodeExtConfigDefList ;
        //流程节点实体装饰类
        ZbglJobProcessNodeDefEntityDec newNodeDec;
        while (it.hasNext()) {
                 value = new ZbglJobProcessNodeDefDec() ;
                 newJobNodeExtConfigDefList = new ArrayList<ZbglJobNodeExtConfigDefDec>();
                 newNodeDec = new ZbglJobProcessNodeDefEntityDec();
                 Map.Entry<String, ZbglJobProcessNodeDefDec> entry = (Map.Entry<String, ZbglJobProcessNodeDefDec>) it.next();
                 value = entry.getValue();  
                 //获取装饰类信息
                 newNodeDec = value.getNewNode();
                 newJobNodeExtConfigDefList = value.getNewJobNodeExtConfigDefList();//获取装饰类中扩展配置信息
                 //TODO 父子级关系and前后置关系
                 ZbglJobProcessNodeDef newNode  = newNodeDec.getNewNode();
                 //父节点id不为“ROOT_0”，即有父节点信息
                 if(!"ROOT_0".equals(newNode.getParentIDX())){
                     //找到父节点对应的新节点信息
                     ZbglJobProcessNodeDefDec newNodeDefDec = nodeMap.get(newNode.getParentIDX());
                     newNode.setParentIDX(newNodeDefDec.getNewNode().getNewNode().getIdx());  //更新新节点id
                 }
                 //保存复制后的流程节点
                 newNode = EntityUtil.setSysinfo(newNode) ;
                 this.daoUtils.getHibernateTemplate().save(newNode);
                 //newJobNodeExtConfigDefList不为空，即该节点有扩展配置信息
                 if(null != newJobNodeExtConfigDefList && newJobNodeExtConfigDefList.size() > 0){
                     for(ZbglJobNodeExtConfigDefDec newNodeExtConfigDec : newJobNodeExtConfigDefList){
                         ZbglJobNodeExtConfigDef newNodeExtDef = newNodeExtConfigDec.getNewNodeExtDef(); //获取装饰类中的扩展配置对象
                         newNodeExtDef.setNodeIDX(newNode.getIdx()); 
                         newNodeExtDef = EntityUtil.setSysinfo(newNodeExtDef) ;
                         zbglJobNodeExtConfigDefManager.insert(newNodeExtDef) ;  //保存复制后的扩展配置信息
                     }
                 }
                 //叶子节点
                 if(ZbglJobProcessNodeDef.CONST_INT_IS_LEAF_NO != newNode.getIsLeaf()){
                     //是叶子节点,复制关联检修项目
                     copyProjectDef(newNodeDec.getOldIdx() , newNode.getIdx(), newNode.getZbfwIDX());
                 }
        }
        Set<Map.Entry<String, ZbglJobProcessNodeDefDec>> setV = nodeMap.entrySet();
        Iterator itV = setV.iterator();
        while (itV.hasNext()) {
            value = new ZbglJobProcessNodeDefDec() ;
            newJobProcessNodeRelDefList = new ArrayList<ZbglJobProcessNodeRelDefDec>();
            newNodeDec = new ZbglJobProcessNodeDefEntityDec();
            Map.Entry<String, ZbglJobProcessNodeDefDec> entry = (Map.Entry<String, ZbglJobProcessNodeDefDec>) itV.next();
            value = entry.getValue();  
            //获取装饰类信息
            newJobProcessNodeRelDefList = value.getNewJobProcessNodeRelDefList();
            newNodeDec = value.getNewNode();
            //TODO 父子级关系and前后置关系
            ZbglJobProcessNodeDef newNode  = newNodeDec.getNewNode();
            //newJobProcessNodeRelDefList不为空，即该节点有前后置关系信息
            if(null != newJobProcessNodeRelDefList && newJobProcessNodeRelDefList.size() > 0){
                for(ZbglJobProcessNodeRelDefDec newNodeRelDec : newJobProcessNodeRelDefList){
                    ZbglJobProcessNodeRelDef newNodeRelDef = newNodeRelDec.getNewNodeRelDef();
                    if(!StringUtil.isNullOrBlank(newNodeRelDef.getPreNodeIDX())){
                        //找到前置节点id对应的新节点信息
                        ZbglJobProcessNodeDefDec newNodeDefDec = nodeMap.get(newNodeRelDef.getPreNodeIDX());
                        newNodeRelDef.setPreNodeIDX(newNodeDefDec.getNewNode().getNewNode().getIdx());//更新新节点id
                    }
                    newNodeRelDef.setNodeIDX(newNode.getIdx()); 
                    newNodeRelDef = EntityUtil.setSysinfo(newNodeRelDef) ;
                    zbglJobProcessNodeRelDefManager.insert(newNodeRelDef) ;  //保存复制后的前后置关系信息
                }
            }
   }
    }
    /**
     * 
     * <li>说明：复制节点关联的作业项目信息
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param oldNodeIdx 旧节点id
     * @param newNodeIdx 复制后的新节点id
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void copyProjectDef(String oldNodeIdx, String newNodeIdx, String newZbfwIDX) throws BusinessException, NoSuchFieldException{
            List<ZbFwWi> projectDefList = zbFwWiManager.getModelByNodeIDX(oldNodeIdx);
            if(null != projectDefList && projectDefList.size() > 0){
                for(ZbFwWi projectDef : projectDefList){
                    List<ZbFwWidi> diList = zbFwWidiManager.getModelByZbfwwiIDX(projectDef.getIdx());
                    projectDef.setIdx(UUID.randomUUID().toString().replace("-", ""));  //设置新id
                    projectDef.setNodeIDX(newNodeIdx);
                    projectDef.setZbfwIDX(newZbfwIDX);//整备范围id
                    //复制关联作业项目
                    zbFwWiManager.insert(projectDef);
                    for(ZbFwWidi di : diList){
                        di.setIdx(UUID.randomUUID().toString().replace("-", ""));  //设置新id
                        di.setZbfwwiIDX(projectDef.getIdx());
//                      复制关联作业项目数据项
                        zbFwWidiManager.insert(di) ;
                    }
                }
            }
    }  
}