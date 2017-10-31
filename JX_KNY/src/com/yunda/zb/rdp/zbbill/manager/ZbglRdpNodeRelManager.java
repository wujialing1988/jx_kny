package com.yunda.zb.rdp.zbbill.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.util.BeanUtils;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNodeRel;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeRelDef;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车整备节点前置关系业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service(value = "zbglRdpNodeRelManager")
public class ZbglRdpNodeRelManager extends JXBaseManager<ZbglRdpNodeRel, ZbglRdpNodeRel>{
    
    /**
     * <li>说明：生成整备单-根据前置节点定义生成前置节点关系
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdp 整备单实体
     * @param nodeList 整备单关联的所有节点列表
     * @throws Exception
     */
    //如无前置节点定义则按原逻辑也生成一条无前置节点的记录
    @SuppressWarnings("unchecked")
    public void saveByNode(ZbglRdp rdp, List<ZbglRdpNode> nodeList) throws Exception{
        List<ZbglRdpNodeRel> list = new ArrayList<ZbglRdpNodeRel>();
        String hql = SqlMapUtil.getSql("zb-rdpNode:getRelDefListByZbfw").replace("#zbfwIDX#", rdp.getZbfwIDX());
        List<ZbglJobProcessNodeRelDef> relDefList = daoUtils.find(hql);
        Map<String, List<ZbglJobProcessNodeRelDef>> relDefMap = new HashMap<String, List<ZbglJobProcessNodeRelDef>>();
        for (ZbglJobProcessNodeRelDef def : relDefList) {
            if (!relDefMap.containsKey(def.getNodeIDX())) {
                List<ZbglJobProcessNodeRelDef> tempList = new ArrayList<ZbglJobProcessNodeRelDef>();
                tempList.add(def);
                relDefMap.put(def.getNodeIDX(), tempList);
            } else {
                relDefMap.get(def.getNodeIDX()).add(def);
            }
        }
        Map<String, ZbglRdpNode> nodeMap = new HashMap<String, ZbglRdpNode>();
        for (ZbglRdpNode node : nodeList) {
            nodeMap.put(node.getNodeDefIDX(), node);
        }
        for (ZbglRdpNode node : nodeList) {
            List<ZbglJobProcessNodeRelDef> relDefListByNode = relDefMap.get(node.getNodeDefIDX());
            if (relDefListByNode == null || relDefListByNode.isEmpty()) {
                ZbglRdpNodeRel rel = new ZbglRdpNodeRel();
                rel.setNodeIDX(node.getIdx());
                list.add(rel);
            } else {
                for (ZbglJobProcessNodeRelDef relDef : relDefListByNode) {
                    ZbglRdpNodeRel rel = new ZbglRdpNodeRel();
                    BeanUtils.copyProperties(rel, relDef);
                    rel.setIdx("");
                    rel.setNodeIDX(node.getIdx());
                    ZbglRdpNode preNode = nodeMap.get(relDef.getPreNodeIDX());
                    if (preNode == null)
                        throw new BusinessException("生成前置节点关系时，前置节点为空");
                    rel.setPreNodeIDX(preNode.getIdx());
                    list.add(rel);
                }  
            }
        }
        saveOrUpdate(list);
    }
    
}
