package com.yunda.zb.zbfw.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.zbfw.entity.ZbFwWi;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeDef;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglJobProcessNodeDef业务类,整备作业流程节点
 * <li>创建人：程梅
 * <li>创建日期：2016年4月7日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglJobProcessNodeDefManager")
public class ZbglJobProcessNodeDefManager extends AbstractOrderManager<ZbglJobProcessNodeDef, ZbglJobProcessNodeDef>{
    /** ZbglJobProcessNodeRelDef业务类,节点前后置关系 */
    @Resource
    private ZbglJobProcessNodeRelDefManager zbglJobProcessNodeRelDefManager;
    /** ZbFwWiManager业务类，整备作业项目 */
    @Resource
    private ZbFwWiManager zbFwWiManager ;
    /**
     * 
     * <li>说明：分页查询作业节点列表，因为unix系统下的oracle不支持WM_CONCAT函数，所以单独处理了对前置节点的查询
     * <li>创建人：程梅
     * <li>创建日期：2016-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @return
     * @throws BusinessException
     */
    @Override
    public Page<ZbglJobProcessNodeDef> findPageList(SearchEntity<ZbglJobProcessNodeDef> searchEntity) throws BusinessException {
        StringBuilder sb = new StringBuilder("From ZbglJobProcessNodeDef Where recordStatus = 0");
        ZbglJobProcessNodeDef entity = searchEntity.getEntity();
        // 查询条件 - 范围主键
        sb.append(" And zbfwIDX = '").append(entity.getZbfwIDX()).append("'");
        // 查询条件 - 上级主键
        sb.append(" And parentIDX = '").append(entity.getParentIDX()).append("'");
        // 排序
        if (null != searchEntity.getOrders()) {
            String order = HqlUtil.getOrderHql(searchEntity.getOrders());
            if (!order.contains("preNodeSeqNo")) {
                sb.append(order);
            }
        } else {
            sb.append(" Order By seqNo ASC");
        }
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<ZbglJobProcessNodeDef> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        List<ZbglJobProcessNodeDef> list = page.getList();
        if (null != list || list.size() > 0) {
            for (ZbglJobProcessNodeDef nodeDef : list) {
                nodeDef.setPreNodeSeqNo(this.zbglJobProcessNodeRelDefManager.getPreNodeSeqNo(nodeDef.getIdx()));
            }
        }
        return page;
    }
    /**
     * <li>说明：作业节点树
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级节点idx主键
     * @param zbfwIDX 范围主键
     * @return List<HashMap<String, Object>> 实体集合
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree(String parentIDX, String zbfwIDX) {
        String idx = "";
        if ("ROOT_0".equals(parentIDX)) {
            idx = parentIDX;
        } else {
            ZbglJobProcessNodeDef parentObj = this.getModelById(parentIDX); // 获取父类对象
            idx = parentObj.getIdx();
        }
        String hql = "from ZbglJobProcessNodeDef where parentIDX = ? And zbfwIDX = ? And recordStatus = " + Constants.NO_DELETE + " order by seqNo";
        List<ZbglJobProcessNodeDef> list = (List<ZbglJobProcessNodeDef>) this.daoUtils.find(hql, new Object[] { idx, zbfwIDX });
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (ZbglJobProcessNodeDef t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getIdx()); // 节点idx主键
            nodeMap.put("text", formatDisplayInfo(t)); // 树节点显示名称
            nodeMap.put("leaf", t.getIsLeaf() == 0 ? false : true); // 是否是叶子节点 0:否；1：是
            nodeMap.put("nodeName", t.getNodeName()); // 节点名称
            nodeMap.put("nodeDesc", t.getNodeDesc()); // 节点描述
            nodeMap.put("seqNo", t.getSeqNo()); // 顺序号
            nodeMap.put("editable ", true); 
            children.add(nodeMap);
        }
        return children;
    }
    /**
     * <li>说明：格式化作业节点顺序号显示
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 当前节点的
     * @return 实体界面显示文本
     */
    private String formatDisplayInfo(ZbglJobProcessNodeDef entity) {
        List<Integer> list = new ArrayList<Integer>();
        this.listParentsSeqNo(entity, list);
        int length = list.size();
        StringBuilder sb = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            sb.append(list.get(i));
            if(i == 0) {
                sb.append("、");
            } else {
                sb.append(".");
            }
        }
        // 节点名称
        sb.append(entity.getNodeName());
        return sb.toString();
    }
    /**
     * <li>说明：递归获取作业节点的顺序号列
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 当前节点的【作业节点】
     * @param list 顺序号集合
     */
    private void listParentsSeqNo(ZbglJobProcessNodeDef entity, List<Integer> list) {
        list.add(entity.getSeqNo());
        if (!entity.getParentIDX().equals("ROOT_0")) {
            listParentsSeqNo(this.getModelById(entity.getParentIDX()), list);
        }
    }
    /**
     * <li>说明：通过拖拽的方式调整整备作业流程节点顺序
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 被拖拽的节点（idx主键）
     * @param oldParent 旧父节点（idx主键）
     * @param newParent 新父节点（idx主键）
     * @param index 索引值
     * @throws Exception 
     * @throws NumberFormatException 
     */
    @SuppressWarnings("unchecked")
    public void moveNode(String nodeIDX, String oldParent, String newParent, String index) throws NumberFormatException, Exception {
        // 获取被拖拽的节点实体
        ZbglJobProcessNodeDef entity = this.getModelById(nodeIDX);
        // 验证节点所在父节点是否已经在后台已经被更新，但页面没有刷新
        if (!oldParent.equals(entity.getParentIDX())) {
            throw new BusinessException("操作失败，请刷新后重试！");
        }
        
        // 被拖拽的节点旧的顺序号
        int oldSeqNo = entity.getSeqNo().intValue();
        // 被拖拽的节点新的顺序号
        int newSeqNo = Integer.parseInt(index) + 1;
        
        String hql = null;
        List<ZbglJobProcessNodeDef> list = null;
        // 同级下的节点进行拖拽
        if (oldParent.equals(newParent)) {
            // 根据被拖拽后节点的新旧顺序号计算偏移量
            int offset = 0;
            if (newSeqNo < oldSeqNo) {
                // 上移
                offset++;
            } else {
                // 下移
                offset--;
            }
            // 获取被拖拽的节点原顺序号与新顺序号之间的节点实体集合
            int startIndex = newSeqNo < oldSeqNo ? newSeqNo : oldSeqNo;
            int endIndex =  newSeqNo < oldSeqNo ? oldSeqNo : newSeqNo;
            hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And seqNo >= ? And seqNo <= ? And zbfwIDX = ? And parentIDX = ?";
            list = this.daoUtils.find(hql, new Object[]{startIndex, endIndex, entity.getZbfwIDX(), newParent});
            // 更新顺序号
            for (ZbglJobProcessNodeDef nodeDef : list) {
                if (nodeDef.getIdx().equals(nodeIDX)) {
                    nodeDef.setSeqNo(newSeqNo);
                    continue;
                }
                nodeDef.setSeqNo(nodeDef.getSeqNo() + offset);
            }
            this.saveOrUpdate(list);
        // 不同级下的节点进行拖拽
        } else {
            // 插入到新的父节点下后，更新顺序号
            hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And seqNo >= ? And zbfwIDX = ? And parentIDX = ?";
            list = this.daoUtils.find(hql, new Object[]{newSeqNo, entity.getZbfwIDX(), newParent});
            if (list.size() > 0) {
                for (ZbglJobProcessNodeDef nodeDef : list) {
                    nodeDef.setSeqNo(nodeDef.getSeqNo() + 1);
                }
                this.saveOrUpdate(list);
            }
            
            // 更新父节点为新的父节点（idx主键）
            entity.setParentIDX(newParent);
            // 更新顺序号
            entity.setSeqNo(newSeqNo);
            this.saveOrUpdate(entity);
            // 重排旧父节点下的节点顺序
            list = this.getModels(entity.getZbfwIDX(), oldParent);
            if (null != list && list.size() > 0) {
                super.updateSort(list);
            }
            
            // 不同层次间的拖拽完成后，清空可能存在的节点前后置关系
            this.zbglJobProcessNodeRelDefManager.logicDeleteByMoveNode(nodeIDX);
        }
    }
    /**
     * <li>说明：获取同一个作业流程、同级的作业节点
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIDX 范围主键
     * @param parentIDX 父作业节点主键
     * @return List<ZbglJobProcessNodeDef> 作业流程节点实体集
     */
    @SuppressWarnings("unchecked")
    public List<ZbglJobProcessNodeDef> getModels(String zbfwIDX, String parentIDX) {
        String hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And zbfwIDX = ? And parentIDX = ? Order By seqNo";
        return this.daoUtils.find(hql, new Object[] { zbfwIDX, parentIDX });
    }
    /**
     * <li>说明：获取同一个范围的所有作业节点
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIDX 整备范围idx主键
     * @return List<ZbglJobProcessNodeDef> 范围节点实体集
     */
    @SuppressWarnings("unchecked")
    public List<ZbglJobProcessNodeDef> getModelsByZbfwIDX(Serializable zbfwIDX) {
        String hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And zbfwIDX = ? ";
        return this.daoUtils.find(hql, new Object[] { zbfwIDX });
    }
    /**
     * <li>如果在子节点下增加下级，则在保存新增的节点时要更新其上级节点为父节点
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级节点对象idx主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateParentWPNodes(String parentIDX) throws BusinessException, NoSuchFieldException {
        if (StringUtil.isNullOrBlank(parentIDX) || "ROOT_0".equals(parentIDX)) {
            return;
        }
        // 获取父节点对象实例
        ZbglJobProcessNodeDef parent = this.getModelById(parentIDX);
        if (ZbglJobProcessNodeDef.CONST_INT_IS_LEAF_NO != parent.getIsLeaf().intValue()) {
            parent.setIsLeaf(ZbglJobProcessNodeDef.CONST_INT_IS_LEAF_NO);
        }
        // 调用该方法递归更新父节点的父节点对象
        this.saveOrUpdate(parent);
    }
    
    /**
     * <li>说明：重写保存方法
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业节点实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @Override
    public void saveOrUpdate(ZbglJobProcessNodeDef t) throws BusinessException, NoSuchFieldException {
        // 如果在子节点下增加下级，则在保存新增的节点时要更新其上级节点为父节点
        if (null == t.getIdx() || t.getIdx().trim().length() <= 0) {
            t.setIdx(null);
        }
        super.saveOrUpdate(t);
        // 更新父节点相关信息（递归）
        this.updateParentWPNodes(t.getParentIDX());
    }
    /**
     * <li>说明：逻辑删除作业项目、数据项，物理删除前后置关系、扩展配置
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 要删除的作业流程节点实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void logicDelete(ZbglJobProcessNodeDef node) throws BusinessException, NoSuchFieldException {
        super.logicDelete(node.getIdx());
        //级联逻辑删除关联的作业项目和数据项
        List<ZbFwWi> projectList = this.zbFwWiManager.getModelByNodeIDX(node.getIdx());
        if (null != projectList && projectList.size() > 0) {
            this.zbFwWiManager.logicDelete(projectList);
        }
        // 级联物理删除节点所关联的所有前后置关系
        StringBuffer relHql = new StringBuffer(" delete from ZbglJobProcessNodeRelDef where (nodeIDX = ? Or preNodeIDX = ?) ");
        this.daoUtils.execute(relHql.toString(), node.getIdx(), node.getIdx());
        //级联物理删除节点所关联的所有扩展配置
        StringBuffer configHql = new StringBuffer(" delete from ZbglJobNodeExtConfigDef where nodeIDX = ? ");
        this.daoUtils.execute(configHql.toString(), node.getIdx());
        // 删除下级节点
        List<ZbglJobProcessNodeDef> list = this.getModels(node.getZbfwIDX(), node.getIdx());
        if (null == list || 0 >= list.size()) {
            return;
        }
        for (ZbglJobProcessNodeDef entity : list) {
            this.logicDelete(entity);
        }
    }
    
    /**
     * <li>说明：逻辑删除作业项目、数据项，物理删除前后置关系、扩展配置
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要删除的实体主键数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        for (Serializable idx : ids) {
            ZbglJobProcessNodeDef entity = this.getModelById(idx);
            // 逻辑删除
            this.logicDelete(this.getModelById(idx));
            if (null != entity) {
                // 如果在子节点下增加下级，则在保存新增的节点时要更新其上级节点为父节点
                this.updateParentWPNodes(entity.getParentIDX());
            }
        }
    }
    
    /**
     * <li>说明：逻辑删除作业项目、数据项，物理删除前后置关系、扩展配置
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityList 要删除的实体集
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @Override
    public void logicDelete(List<ZbglJobProcessNodeDef> entityList) throws BusinessException, NoSuchFieldException {
        super.logicDelete(entityList);
        for (ZbglJobProcessNodeDef node : entityList) {
            //级联逻辑删除关联的作业项目和数据项
            List<ZbFwWi> projectList = this.zbFwWiManager.getModelByNodeIDX(node.getIdx());
            if (null != projectList && projectList.size() > 0) {
                this.zbFwWiManager.logicDelete(projectList);
            }
            // 级联物理删除节点所关联的所有前后置关系
            StringBuffer relHql = new StringBuffer(" delete from ZbglJobProcessNodeRelDef where (nodeIDX = ? Or preNodeIDX = ?) ");
            this.daoUtils.execute(relHql.toString(), node.getIdx(), node.getIdx());
            //级联物理删除节点所关联的所有扩展配置
            StringBuffer configHql = new StringBuffer(" delete from ZbglJobNodeExtConfigDef where nodeIDX = ? ");
            this.daoUtils.execute(configHql.toString(), node.getIdx());
        }
    }
    /**
     * <li>说明：获取设置指定节点主键的直接后置节点
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业节点主键
     * @return List<ZbglJobProcessNodeDef> 作业节点实体集合
     */
    @SuppressWarnings("unchecked")
    private List<ZbglJobProcessNodeDef> getAfterWPNodes(String nodeIDX) {
        String hql =
            "From ZbglJobProcessNodeDef Where idx In (Select nodeIDX From ZbglJobProcessNodeRelDef Where preNodeIDX = ? ) And recordStatus = 0";
        return this.daoUtils.find(hql, new Object[] { nodeIDX });
    }
    
    /**
     * <li>说明：获取设置指定节点主键的直接前置节点
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业节点主键
     * @return List<ZbglJobProcessNodeDef> 作业节点实体集合
     */
    @SuppressWarnings("unchecked")
    private List<ZbglJobProcessNodeDef> getBeforeWPNodes(String nodeIDX) {
        String hql =
            "From ZbglJobProcessNodeDef Where idx In (Select preNodeIDX From ZbglJobProcessNodeRelDef Where nodeIDX = ? ) And recordStatus = 0";
        return this.daoUtils.find(hql, new Object[] { nodeIDX });
    }
    /**
     * <li>说明：递归获取设置指定节点主键的所有直接和间接后置节点
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业节点主键
     * @param entityList 作业节点实体集合
     */
    public void listAfterNodes(String nodeIDX, List<ZbglJobProcessNodeDef> entityList) {
        List<ZbglJobProcessNodeDef> afterWPNodes = this.getAfterWPNodes(nodeIDX);
        if (null != afterWPNodes && 0 < afterWPNodes.size()) {
            for (ZbglJobProcessNodeDef wpNode : afterWPNodes) {
                if (!entityList.contains(wpNode)) {
                    entityList.add(wpNode);
                }
                listAfterNodes(wpNode.getIdx(), entityList);
            }
        }
    }
    
    /**
     * <li>说明：递归获取设置指定节点主键的所有直接和间接前置节点
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业节点主键
     * @param entityList 作业节点实体集合
     */
    public void listBeforeNodes(String nodeIDX, List<ZbglJobProcessNodeDef> entityList) {
        List<ZbglJobProcessNodeDef> beforeWPNodes = this.getBeforeWPNodes(nodeIDX);
        if (null != beforeWPNodes && 0 < beforeWPNodes.size()) {
            for (ZbglJobProcessNodeDef wpNode : beforeWPNodes) {
                if (!entityList.contains(wpNode)) {
                    entityList.add(wpNode);
                }
                listBeforeNodes(wpNode.getIdx(), entityList);
            }
        }
    }
    /**
     * <li>说明：置底
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveBottom(String idx) throws Exception {
        ZbglJobProcessNodeDef entity = this.getModelById(idx);
        int count = this.count(entity);
        // 获取被【置底】记录被置底前，在其后的所有记录
        String hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And seqNo > ? And zbfwIDX = ? And parentIDX = ?";
        List<ZbglJobProcessNodeDef> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getZbfwIDX(), entity.getParentIDX() });
        // 设置被【置底】记录的排序号为当前记录总数
        entity.setSeqNo(count);
        List<ZbglJobProcessNodeDef> entityList = new ArrayList<ZbglJobProcessNodeDef>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后依次减一
        for (ZbglJobProcessNodeDef recordCard : list) {
            recordCard.setSeqNo(recordCard.getSeqNo() - 1);
            entityList.add(recordCard);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：下移
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveDown(String idx) throws Exception {
        ZbglJobProcessNodeDef entity = this.getModelById(idx);
        String hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And seqNo = ? And zbfwIDX = ? And parentIDX = ?";
        // 获取被【下移】记录被下移前，紧随其后的记录
        ZbglJobProcessNodeDef nextEntity =
            (ZbglJobProcessNodeDef) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1, entity.getZbfwIDX(), entity.getParentIDX() });
        List<ZbglJobProcessNodeDef> entityList = new ArrayList<ZbglJobProcessNodeDef>(2);
        // 设置被【下移】记录的排序号+1
        entity.setSeqNo(entity.getSeqNo() + 1);
        entityList.add(entity);
        // 设置被【下移】记录后的记录的排序号-1
        nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：置顶
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveTop(String idx) throws Exception {
        ZbglJobProcessNodeDef entity = this.getModelById(idx);
        // 获取被【置顶】记录被置顶前，在其前的所有记录
        String hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And seqNo < ? And zbfwIDX = ? And parentIDX = ?";
        List<ZbglJobProcessNodeDef> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getZbfwIDX(), entity.getParentIDX() });
        // 设置被【置顶】记录的排序号为1
        entity.setSeqNo(1);
        List<ZbglJobProcessNodeDef> entityList = new ArrayList<ZbglJobProcessNodeDef>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后一次加一
        for (ZbglJobProcessNodeDef recordCard : list) {
            recordCard.setSeqNo(recordCard.getSeqNo() + 1);
            entityList.add(recordCard);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：上移
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveUp(String idx) throws Exception {
        ZbglJobProcessNodeDef entity = this.getModelById(idx);
        String hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And seqNo = ? And zbfwIDX = ? And parentIDX = ?";
        // 获取被【上移】记录被上移移前，紧随其前的记录
        ZbglJobProcessNodeDef nextEntity =
            (ZbglJobProcessNodeDef) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1, entity.getZbfwIDX(), entity.getParentIDX() });
        List<ZbglJobProcessNodeDef> entityList = new ArrayList<ZbglJobProcessNodeDef>(2);
        // 设置被【上移】记录的排序号-1
        entity.setSeqNo(entity.getSeqNo() - 1);
        entityList.add(entity);
        // 设置被【上移】记录前的记录的排序号+1
        nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
    /**
     * <li>说明：查询当前对象在数据库中的记录总数
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业节点实体对象
     * @return int 记录总数
     */
    @Override
    public int count(ZbglJobProcessNodeDef t) {
        String hql = "Select Count(*) From ZbglJobProcessNodeDef Where recordStatus = 0 And zbfwIDX = ? And parentIDX = ?";
        return this.daoUtils.getCount(hql, new Object[] { t.getZbfwIDX(), t.getParentIDX() });
    }
    /**
     * <li>获取已知作业节点之后的所有同级作业节点
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业节点实体对象
     * @return 实体集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ZbglJobProcessNodeDef> findAllBySN(ZbglJobProcessNodeDef t) throws Exception {
        String hql = "From ZbglJobProcessNodeDef Where recordStatus = 0 And seqNo >= ? And zbfwIDX = ? And parentIDX = ?";
        return this.daoUtils.find(hql, new Object[] { t.getSeqNo(), t.getZbfwIDX(), t.getParentIDX() });
    }
    /**
     * <li>说明：查询作业节点的所有父节点，并以从叶子到根的顺序存入list
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级作业节点主键
     * @param list 作业节点实体集合
     */
    public void listParentsWPNodes(String parentIDX, List<ZbglJobProcessNodeDef> list) {
        ZbglJobProcessNodeDef node = this.getModelById(parentIDX);
        if (null != node) {
            list.add(node);
            listParentsWPNodes(node.getParentIDX(), list);
        }
    }
    /**
     * <li>说明：获取排序范围内的同类型的所有记录
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 实体对象
     * @return List<ZbglJobProcessNodeDef> 结果集
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ZbglJobProcessNodeDef> findAll(ZbglJobProcessNodeDef t) {
        return getModels(t.getZbfwIDX(), t.getParentIDX());
    }
}