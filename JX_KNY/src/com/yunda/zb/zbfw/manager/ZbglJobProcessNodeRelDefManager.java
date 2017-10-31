package com.yunda.zb.zbfw.manager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeDef;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeRelDef;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglJobProcessNodeRelDef业务类,整备作业流程节点前后置关系
 * <li>创建人：程梅
 * <li>创建日期：2016年4月7日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglJobProcessNodeRelDefManager")
public class ZbglJobProcessNodeRelDefManager extends JXBaseManager<ZbglJobProcessNodeRelDef, ZbglJobProcessNodeRelDef>{
    /** ZbglJobProcessNodeDef业务类，整备作业流程节点*/
    @Resource
    private ZbglJobProcessNodeDefManager zbglJobProcessNodeDefManager ;
    /**
     * 
     * <li>说明：获取节点的前置节点顺序号
     * <li>创建人：程梅
     * <li>创建日期：2016-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点主键
     * @return 节点的前置节点顺序号，例如（1,2）
     */
    @SuppressWarnings("unchecked")
    public String getPreNodeSeqNo(String nodeIDX) {
        StringBuilder sb = new StringBuilder("select a.seq_no from ZB_ZBGL_Job_Process_Node_Def a, ZB_ZBGL_Job_ProcessNode_RelDef b where a.record_status = 0 and a.idx = b.pre_node_idx ");
        sb.append(" and b.node_idx = '").append(nodeIDX).append("' order by a.seq_no");
        List<BigDecimal> list = this.daoUtils.executeSqlQuery(sb.toString());
        if (null == list || list.size() <= 0) {
            return null;
        }
        StringBuilder sBuilder = new StringBuilder();
        for (BigDecimal seqNo : list) {
            sBuilder.append(",").append(seqNo.intValue());
        }
        return sBuilder.substring(1);
    }
    /**
     * <li>说明：分页查询，因为数据表只存储了前置节点的主键，为了在节目显示节点名称，所以对前置节点的名称进行联合查询
     * <li>创建人：程梅
     * <li>创建日期：2016-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param searchEntity 查询对象实体
     * @return Page<ZbglJobProcessNodeRelDef> 实体集合
     */
    @Override
    public Page<ZbglJobProcessNodeRelDef> findPageList(SearchEntity<ZbglJobProcessNodeRelDef> searchEntity) throws BusinessException {
        StringBuilder sb =  new StringBuilder();
        sb.append("Select new ZbglJobProcessNodeRelDef(a.idx, a.nodeIDX, a.preNodeIDX, b.nodeName, b.seqNo) From ZbglJobProcessNodeRelDef a, ZbglJobProcessNodeDef b Where a.preNodeIDX = b.idx And b.recordStatus = 0");
        ZbglJobProcessNodeRelDef entity = searchEntity.getEntity();
        // 查询条件 - 节点主键
        if (!StringUtil.isNullOrBlank(entity.getNodeIDX())) {
            sb.append(" And a.nodeIDX = '").append(entity.getNodeIDX()).append("'");
        }
        if (null != searchEntity.getOrders()) {
            Order order = searchEntity.getOrders()[0];
            if (order.toString().contains("preNodeName")) {
                sb.append(" Order By ").append(order.toString().replace("preNodeName", "b.nodeName"));
            } else if (order.toString().contains("preSeqNo")){
                sb.append(" Order By ").append(order.toString().replace("preSeqNo", "b.seqNo"));
            } else {
                sb.append(" Order By ").append("a." + order);
            }
        } else {
            sb.append(" Order By b.seqNo");
        }
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * <li>说明：不同层次间的拖拽完成后，清空可能存在的节点前后置关系
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点主键
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @SuppressWarnings("unchecked")
    public void logicDeleteByMoveNode(String nodeIDX) throws BusinessException, NoSuchFieldException {
        String hql = "From ZbglJobProcessNodeRelDef Where (nodeIDX = ? Or preNodeIDX = ?)";
        List<ZbglJobProcessNodeRelDef> list = this.daoUtils.find(hql, new Object[]{ nodeIDX, nodeIDX });
        this.logicDelete(list);
    }
    /**
     * <li>说明：根据“节点主键”获取该节点参与的所有前后置关系
     * <li>创建人：程梅
     * <li>创建日期：2016-04-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param nodeIDX 节点主键
     * @return List<ZbglJobProcessNodeRelDef>实体集合
     */
    @SuppressWarnings("unchecked")
    public List<ZbglJobProcessNodeRelDef> listModels(Serializable nodeIDX) {
        String hql = "From ZbglJobProcessNodeRelDef Where (nodeIDX = ? Or preNodeIDX = ?)";
        return this.daoUtils.find(hql, new Object[]{nodeIDX, nodeIDX});
    }
    /**
     * <li>说明：根据“节点主键”获取【节点前后置关系】
     * <li>创建人：程梅
     * <li>创建日期：2016-04-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param nodeIDX 节点主键
     * @return List<JobProcessNodeRelDef>实体集合
     */
    @SuppressWarnings("unchecked")
    public List<ZbglJobProcessNodeRelDef> getModelsByWPNodeIDX(Serializable nodeIDX) {
        String hql = "From ZbglJobProcessNodeRelDef Where nodeIDX = ?";
        return this.daoUtils.find(hql, new Object[]{nodeIDX});
    }
    /**
     * 
     * <li>说明：获取同一个整备范围的所有节点前后置关系
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIDX 范围id
     * @return List<ZbglJobProcessNodeRelDef> 前后置关系list
     */
    @SuppressWarnings("unchecked")
    public List<ZbglJobProcessNodeRelDef> getModelsByZbfwIDX(String zbfwIDX) {
        String hql = "From ZbglJobProcessNodeRelDef Where nodeIDX in (select idx From ZbglJobProcessNodeDef Where recordStatus = 0 And zbfwIDX = ?)";
        return this.daoUtils.find(hql, new Object[]{zbfwIDX});
    }
    /**
     * <li>说明：唯一性验证，一个作业节点不能添加多个重复的前置作业节点
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */     
    @Override
    public String[] validateUpdate(ZbglJobProcessNodeRelDef entity) throws BusinessException {
        // 获取某一节点的所有前置节点关系
        List<ZbglJobProcessNodeRelDef> models = getModelsByWPNodeIDX(entity.getNodeIDX());
        for(ZbglJobProcessNodeRelDef t: models) {
            if (t.getIdx().equals(entity.getIdx()) && t.getPreNodeIDX().equals(entity.getPreNodeIDX())) {
                return null;
            }
            if (t.getPreNodeIDX().equals(entity.getPreNodeIDX())) {
                String preWPNodeName = zbglJobProcessNodeDefManager.getModelById(t.getPreNodeIDX()).getNodeName();
                String nodeName = zbglJobProcessNodeDefManager.getModelById(t.getNodeIDX()).getNodeName();
                return new String[]{"作业节点：" + nodeName + "已经配置了前置节点[" + preWPNodeName + "]，不能重复添加！"};
            }
        }
        
        // 验证是否有循环依赖（即：如有作业节点A、B、C，C的前置节点有A和B，则A或者B的前置节点不能有C）
        // 获取所有已该节点设置了前置节点的节点信息
        List<ZbglJobProcessNodeDef> list = new ArrayList<ZbglJobProcessNodeDef>();
        this.zbglJobProcessNodeDefManager.listAfterNodes(entity.getNodeIDX(), list);
        if (null != list && 0 < list.size()) {
            for (ZbglJobProcessNodeDef node : list) {
                if (node.getIdx().equals(entity.getPreNodeIDX())) {
                    return new String[]{"前置节点设置存在循环依赖，请检查！"};
                }
            }
        }
        
        // 验证是否有重复的前置节点关系（即：如有作业节点A、B、C，B的前置节点为A，C的前置节点为B，则C不能设置前置节点为A）
        list = new ArrayList<ZbglJobProcessNodeDef>();
        this.zbglJobProcessNodeDefManager.listBeforeNodes(entity.getNodeIDX(), list);
        if (null != list && 0 < list.size()) {
            for (ZbglJobProcessNodeDef nodeDef : list) {
                if (nodeDef.getIdx().equals(entity.getPreNodeIDX())) {
                    return new String[]{"不能设置重叠的前置节点关系，请检查！"};
                }
            }
        }
        
        return super.validateUpdate(entity);
    }
}