package com.yunda.zb.rdp.zbbill.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.util.BeanUtils;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNodeExtConfig;
import com.yunda.zb.zbfw.entity.ZbglJobNodeExtConfigDef;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeDef;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车整备作业节点业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service(value = "zbglRdpNodeManager")
public class ZbglRdpNodeManager extends JXBaseManager<ZbglRdpNode, ZbglRdpNode> {
    
    /** 机车整备作业流程-前置节点业务类 */
    @Resource
    private ZbglRdpNodeRelManager zbglRdpNodeRelManager;
    
    /** 机车整备作业流程-节点扩展配置业务类 */
    @Resource
    private ZbglRdpNodeExtConfigManager zbglRdpNodeExtConfigManager;

    /** 机车整备作业流程-节点查询业务类 */
    @Resource
    private ZbglRdpNodeQueryManager zbglRdpNodeQueryManager;
    
    /**
     * <li>说明：机车整备兑现时生成作业节点及节点前置关系、节点扩展配置
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdp 机车整备单实体
     * @throws Exception
     */
    public void saveNodeAndSeq(ZbglRdp rdp) throws Exception {
        saveNode(rdp, "ROOT_0", "");
        daoUtils.flush();
        List<ZbglRdpNode> nodeList = zbglRdpNodeQueryManager.getNodeListByRdp(rdp.getIdx());
        saveSeq(rdp, nodeList);
        zbglRdpNodeExtConfigManager.saveByNode(nodeList, rdp.getIdx());
    }

    /**
     * <li>说明：更新开始节点信息
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param configValue 扩展配置值
     * @param realBeginTime 开始时间
     * @param rdpIDX 机车整备IDX
     * @param status 节点状态
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateNodeForStart (String configValue, Date realBeginTime, String rdpIDX, String status) throws BusinessException, NoSuchFieldException {
        List<ZbglRdpNodeExtConfig> configList = zbglRdpNodeExtConfigManager.getFwListByRdp(rdpIDX, ZbglJobNodeExtConfigDef.EXT_DICT_TYPE);
        if (configList == null || configList.size() < 1)
            return;
        for (ZbglRdpNodeExtConfig config : configList) {
            if (configValue.equals(config.getConfigValue())) {
                ZbglRdpNode node = getModelById(config.getNodeIDX());
                if (node == null)
                    continue;
                if (!ZbglRdpNode.STATUS_UNSTART.equals(node.getStatus())) 
                        continue;
                node.setRealBeginTime(realBeginTime);
                node.setStatus(status);
                saveOrUpdate(node);
                
//              当该节点保存完毕之后，修改其所有上级节点的状态为处理中
                //调用递归查询父节点的上级情况
                updateParentNodeInfoRecursion(node,status);
                
                return;
            }
        }
    }

    /**
     * <li>说明：更新完成节点信息
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param configValue 扩展配置值
     * @param realEndTime 完成时间
     * @param rdpIDX 机车整备IDX
     * @param status 节点状态
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateNodeForEnd (String configValue, Date realEndTime, String rdpIDX, String status) throws BusinessException, NoSuchFieldException {
        //通过整备单idx查询扩展配置信息，必须有（在数据字段中配置，配置的值和节点的名字一致）
        List<ZbglRdpNodeExtConfig> configList = zbglRdpNodeExtConfigManager.getFwListByRdp(rdpIDX, ZbglJobNodeExtConfigDef.EXT_DICT_TYPE);
        if (configList == null || configList.size() < 1)
            return;
        //便利查询，将查询出来的扩展信息和传递过来的数据比较
        for (ZbglRdpNodeExtConfig config : configList) {
            if (configValue.equals(config.getConfigValue())) {
                //如果相等，取出节点对象，修改它的结束时间和状态
                ZbglRdpNode node = getModelById(config.getNodeIDX());
                if (node == null)
                    continue;
                if (realEndTime != null)
                    node.setRealEndTime(realEndTime);
                node.setStatus(status);
                saveOrUpdate(node);
                
                //当该节点保存完毕之后，判断该节点的上级节点下的节点是否都已经完成，如果都完成了，修改父节点的状态
                //调用递归查询父节点的上级情况
                updateParentNodeInfoRecursion(node,status);
                
                return;
            }
        }
    }
    
    /**
     * <li>说明：更新上级节点信息
     * <li>创建人：林欢
     * <li>创建日期：2016-8-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点idx
     * @param status 状态
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @SuppressWarnings("unused")
    private void updateParentNodeInfoRecursion(ZbglRdpNode node,String status) throws BusinessException, NoSuchFieldException {
        
        //标识符，判断是否修改状态
        Integer flag = 1;
        
        String nodeParentIDX = node.getParentIDX();
        //判断是否为null，如果为null终止方法
        if (!StringUtil.isNullOrBlank(nodeParentIDX)) {
//          根据父节点idx查询其子节点状态
            List<ZbglRdpNode> childNodeList = this.get1ZbglChildNodeListByParentIDX(nodeParentIDX);
            if (childNodeList != null && childNodeList.size() > 0) {
                //循环判断子节点状态
                for (ZbglRdpNode childNode : childNodeList) {
                    
                    //判断status是处理中还是已处理
                    //如果是处理中，只要子节点中有一个是处理中，其所有上级节点的状态均跟新为处理中
                    if (ZbglRdpNode.STATUS_GOING.equals(status)) {
                        flag = 0;
//                      当有子节点的状态为传递过来的状态的时候,同步更新
                        if (status.equals(childNode.getStatus())) {
                            //查询该子节点的父节点是否状态更新
                            ZbglRdpNode parentNode = this.getModelById(nodeParentIDX);
                            parentNode.setStatus(status);
                            //当有一个子节点修改状态后，同步修改父节点的实际开始时间为当前时间
                            parentNode.setRealBeginTime(new Date());
                            this.saveOrUpdate(parentNode);
                            updateParentNodeInfoRecursion(parentNode, status);
                        }
                        
                    //如果是已处理，那么需要判断是否所有的子节点都是已处理，如果是其第一上级节点状态更新为已处理，并递归调用该方法判断其第二上级节点是否也需要同步更新
                    }else if (ZbglRdpNode.STATUS_COMPLETE.equals(status)) {
//                      当有子节点的状态不为传递过来的状态的时候，修改标志flag = 1
                        if (!status.equals(childNode.getStatus())) {
                            flag = 0;
                        }
                    }
                }
                
                //判断flag状态，如果flag状态为1,递归调用方法3
//              查询该子节点的父节点是否状态更新
                if (flag == 1) {
                    ZbglRdpNode parentNode = this.getModelById(nodeParentIDX);
                    parentNode.setStatus(status);
                    //同时修改实际结束时间
                    parentNode.setRealEndTime(new Date());
                    this.saveOrUpdate(parentNode);
                    updateParentNodeInfoRecursion(parentNode, status);
                }
            }
        }
    }

    /**
     * <li>说明：通过上级节点idx查询子节点list
     * <li>创建人：林欢
     * <li>创建日期：2016-8-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点idx
     */
    @SuppressWarnings("unchecked")
    private List<ZbglRdpNode> get1ZbglChildNodeListByParentIDX(String parentIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ZbglRdpNode a where a.recordStatus = 0 and a.parentIDX = '").append(parentIDX).append("'");
        return (List<ZbglRdpNode>) this.find(sb.toString());
    }
    
    /**
     * <li>说明：根据作业计划主键和父节点ID查询整备任务单
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx 作业计划主键
     * @param parentIDX 父节点ID
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpNode> get1ZbglNodeListByRdpIDX(String rdpIdx,String parentIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ZbglRdpNode a where a.recordStatus = 0 and a.rdpIDX = '").append(rdpIdx).append("' ");
        if(StringUtil.isNullOrBlank(parentIDX)){
            sb.append(" and a.parentIDX is null ");// 如果传入空值，则查询父节点为空的数据
        }else{
            sb.append(" and a.parentIDX = '").append(parentIDX).append("'");
        }
        sb.append(" order by a.seqNo ");
        return (List<ZbglRdpNode>) this.find(sb.toString());
    }

    /**
     * <li>说明：生成节点
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdp 机车整备单
     * @param parentNodeIDX 上级节点定义IDX
     * @param parentIDX 上级节点IDX
     * @throws Exception
     */
    private void saveNode(ZbglRdp rdp, String parentNodeIDX, String parentIDX) throws Exception {
        List<ZbglJobProcessNodeDef> list = getSameZbfwAndSameLevelList(rdp.getZbfwIDX(), parentNodeIDX);
        if (list == null || list.size() < 1) {
            ZbglRdpNode parentNode = getModelById(parentIDX);
            if (parentNode != null) {
                parentNode.setIsLeaf(ZbglJobProcessNodeDef.CONST_INT_IS_LEAF_YES);
                saveOrUpdate(parentNode);
            }
            return;
        }
        for (ZbglJobProcessNodeDef def : list) {
            ZbglRdpNode node = new ZbglRdpNode();
            BeanUtils.copyProperties(node, def);
            node.setIdx("");
            node.setNodeDefIDX(def.getIdx());
            node.setParentIDX(parentIDX);
            node.setRdpIDX(rdp.getIdx());
            node.setStatus(ZbglRdpNode.STATUS_UNSTART);
            
            //实例化时候同时赋值工位信息
            node.setWorkStationIDX(def.getWorkStationIDX());
            node.setWorkStationName(def.getWorkStationName());
            
            
            saveOrUpdate(node);
            if (def.getIsLeaf() == ZbglJobProcessNodeDef.CONST_INT_IS_LEAF_NO)
                saveNode(rdp, def.getIdx(), node.getIdx());
        }
    }
    
    /**
     * <li>说明：得到同一整备范围下同一级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIDX 整备范围IDX
     * @param parentIDX 父节点定义IDX
     * @return 同一整备范围下同一级节点列表
     */
    @SuppressWarnings("unchecked")
    private List<ZbglJobProcessNodeDef> getSameZbfwAndSameLevelList(String zbfwIDX, String parentIDX) {
        String hql = SqlMapUtil.getSql("zb-rdpNode:getSameZbfwAndSameLevelList");
        Object[] param = new Object[] { zbfwIDX, parentIDX };              
        List <ZbglJobProcessNodeDef> list = daoUtils.getHibernateTemplate().find(hql,param);
        return list;
    }
    
    /**
     * <li>说明：生成整备单-根据流程前置节点定义新增流程前置节点并计算流程下所有节点的计划时间
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdp 整备单实体
     * @param nodeList 整备单关联的所有节点列表
     * @throws Exception
     */
    private void saveSeq(ZbglRdp rdp, List<ZbglRdpNode> nodeList) throws Exception { 
        zbglRdpNodeRelManager.saveByNode(rdp, nodeList);
    }
}
