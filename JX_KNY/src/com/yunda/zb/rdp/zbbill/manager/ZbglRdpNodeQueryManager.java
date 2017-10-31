package com.yunda.zb.rdp.zbbill.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车整备节点查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service(value = "zbglRdpNodeQueryManager")
public class ZbglRdpNodeQueryManager extends JXBaseManager<ZbglRdpNode, ZbglRdpNode> {
    
    /** 根节点 */
    private String ROOT_0 = "ROOT_0";
    
    private static final String NODEIDX = "#nodeIDX#";
    /**
     * <li>说明：获取整备单的所有节点列表
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 整备单IDX
     * @return 整备单的所有节点列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpNode> getNodeListByRdp(String rdpIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIDX", rdpIDX);
        return getNodeList(paramMap);
    }    

    /**
     * <li>说明：获取节点列表
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 节点列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpNode> getNodeList(Map paramMap) {
        return daoUtils.find(getNodeHql(paramMap));
    }
    
    /**
     * <li>说明：查询第一级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人： 张迪
     * <li>修改日期：2016-4-27
     * <li>修改内容：
     * @param rdpIDX 机车整备IDX
     * @return 第一级节点列表
     * @throws Exception
     */
    public List<ZbglRdpNode> getFirstNodeList(String rdpIDX) throws Exception {
        List<ZbglRdpNode> entityList = this.getNodeListByRdpIDX(rdpIDX, ROOT_0);
        if(null == entityList || 0>= entityList.size()) {
            return null;
         }
        Calendar calendar = Calendar.getInstance(); 
        for (ZbglRdpNode node : entityList) {
            node =  sort(node); //递归查询结点的前置结点
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR, 6*node.getFlag());
            node.setShowBeginTime(calendar.getTime());
            calendar.add(Calendar.MINUTE, 350);
            node.setShowEndTime(calendar.getTime());
        }
        return entityList;
    }
    
    /**
     * <li>说明：递规实现结点顺序
     * <li>创建人：张迪
     * <li>创建日期：2016-5-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param node 结点
     * @return 排序后的结点
     */
    private  ZbglRdpNode  sort(ZbglRdpNode node){
        List<ZbglRdpNode> preNodeList = getPrevNodeList(node.getIdx());
        if(null == preNodeList || 0>=preNodeList.size() ) {
            node.setFlag(0);
        }else if(1== preNodeList.size()){
            sort(preNodeList.get(0));
            node.setFlag(preNodeList.get(0).getFlag()+1);
        }else{  
            for(int i=0; i < preNodeList.size()-1; i++){
                ZbglRdpNode node1 =  sort(preNodeList.get(i));
                ZbglRdpNode node2 = sort(preNodeList.get(i+1));
                node.setFlag(preNodeList.get(0).getFlag()+1); 
                if(node1.getFlag()<= node2.getFlag()){
                    node.setFlag(node2.getFlag()+1); 
                }   
            }
        }
        return node;
    }
   
    /**
     * <li>说明：获取前置结点
     * <li>创建人：张迪
     * <li>创建日期：2016-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 结点id
     * @return 返回前置结点列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpNode> getPrevNodeList(String nodeIDX) {
        String hql = SqlMapUtil.getSql("zb-rdpNode:getPrevNodeList").replace(NODEIDX, nodeIDX);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：获取后置结点
     * <li>创建人：张迪
     * <li>创建日期：2016-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 整备作业结点id
     * @return 返回后置结点列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpNode> getNextNodeList(String nodeIDX) {
        String hql = SqlMapUtil.getSql("zb-rdpNode:getNextNodeList").replace(NODEIDX, nodeIDX);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：获取下级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @param parentIDX 节点IDX
     * @return 下级节点列表
     */
    @SuppressWarnings("unchecked")
    private List<ZbglRdpNode> getNodeListByRdpIDX(String rdpIDX, String parentIDX) {
        StringBuilder sb = new StringBuilder("From ZbglRdpNode Where recordStatus = 0");
        sb.append(" And rdpIDX = '").append(rdpIDX).append("'");
        
        if (null == parentIDX || ROOT_0.equals(parentIDX)) {
            sb.append(" And (parentIDX Is Null Or parentIDX ='").append(ROOT_0).append("')");
        } else {
            sb.append(" And parentIDX = '").append(parentIDX).append("'");
        }
        sb.append(" Order By seqNo ASC");
        return this.daoUtils.find(sb.toString());
    }
    
    /**
     * <li>说明：查询流程节点hql
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 查询流程节点hql
     */
    private String getNodeHql(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglRdpNode where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return hql.toString();
    }
}
