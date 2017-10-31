package com.yunda.jx.jxgc.workplanmanage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.jxgc.workplanmanage.entity.WSGroupItem;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WSGroupItem业务类, 工位组明细
 * <li>创建人：何涛
 * <li>创建日期：2015-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="wSGroupItemManager")
public class WSGroupItemManager extends AbstractOrderManager<WSGroupItem, WSGroupItem> {
    
    /**
     * <li>说明：查询当前对象在数据库中的记录总数
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 工位组明细实体对象
     * @return int 记录总数
     */
    @Override
    public int count(WSGroupItem t) {
        String hql = "Select Count(*) From WSGroupItem Where wsGroupIDX = ?";
        return this.daoUtils.getCount(hql, new Object[] { t.getWsGroupIDX() });
    }
    
    /**
     * <li>说明：获取同一个组的工位组明细
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param wsGroupIDX 工位组idx主键
     * @return List<WSGroupItem> 作业流程节点实体集
     */
    @SuppressWarnings("unchecked")
    public List<WSGroupItem> getModels(String wsGroupIDX) {
        String hql = "From WSGroupItem Where wsGroupIDX = ?";
        return this.daoUtils.find(hql, new Object[] { wsGroupIDX });
    }
    
    /**
     * <li>说明：获取排序范围内的同类型的所有记录
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 实体对象
     * @return List<UnionNode> 结果集
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<WSGroupItem> findAll(WSGroupItem t) {
        return getModels(t.getWsGroupIDX());
    }
    
    /**
     * <li>说明：置底
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveBottom(String idx) throws Exception {
        WSGroupItem entity = this.getModelById(idx);
        int count = this.count(entity);
        // 获取被【置底】记录被置底前，在其后的所有记录
        String hql = "From WSGroupItem Where seqNo > ? And wsGroupIDX = ?";
        List<WSGroupItem> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getWsGroupIDX() });
        // 设置被【置底】记录的排序号为当前记录总数
        entity.setSeqNo(count);
        List<WSGroupItem> entityList = new ArrayList<WSGroupItem>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后依次减一
        for (WSGroupItem recordCard : list) {
            recordCard.setSeqNo(recordCard.getSeqNo() - 1);
            entityList.add(recordCard);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：下移
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveDown(String idx) throws Exception {
        WSGroupItem entity = this.getModelById(idx);
        String hql = "From WSGroupItem Where seqNo = ? And wsGroupIDX = ?";
        // 获取被【下移】记录被下移前，紧随其后的记录
        WSGroupItem nextEntity =
            (WSGroupItem) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1, entity.getWsGroupIDX() });
        List<WSGroupItem> entityList = new ArrayList<WSGroupItem>(2);
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
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveTop(String idx) throws Exception {
        WSGroupItem entity = this.getModelById(idx);
        // 获取被【置顶】记录被置顶前，在其前的所有记录
        String hql = "From WSGroupItem Where seqNo < ? And wsGroupIDX = ?";
        List<WSGroupItem> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getWsGroupIDX() });
        // 设置被【置顶】记录的排序号为1
        entity.setSeqNo(1);
        List<WSGroupItem> entityList = new ArrayList<WSGroupItem>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后一次加一
        for (WSGroupItem recordCard : list) {
            recordCard.setSeqNo(recordCard.getSeqNo() + 1);
            entityList.add(recordCard);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：上移
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveUp(String idx) throws Exception {
        WSGroupItem entity = this.getModelById(idx);
        String hql = "From WSGroupItem Where seqNo = ? And wsGroupIDX = ?";
        // 获取被【上移】记录被上移移前，紧随其前的记录
        WSGroupItem nextEntity =
            (WSGroupItem) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1, entity.getWsGroupIDX() });
        List<WSGroupItem> entityList = new ArrayList<WSGroupItem>(2);
        // 设置被【上移】记录的排序号-1
        entity.setSeqNo(entity.getSeqNo() - 1);
        entityList.add(entity);
        // 设置被【上移】记录前的记录的排序号+1
        nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>获取已知工位组明细之后的所有同级工位组明细
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 工位组明细实体对象
     * @return 实体集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<WSGroupItem> findAllBySN(WSGroupItem t) throws Exception {
        String hql = "From WSGroupItem Where seqNo >= ? And wsGroupIDX = ?";
        return this.daoUtils.find(hql, new Object[] { t.getSeqNo(), t.getWsGroupIDX() });
    }
    
    /**
     * <li>分页查询
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件实体
     * @return Page<WSGroupItem> 实体分页集合
     */
    @Override
    public Page<WSGroupItem> findPageList(SearchEntity<WSGroupItem> searchEntity) throws BusinessException {
        StringBuilder sb = new StringBuilder("Select new WSGroupItem(a.idx, a.wsIDX, a.wsGroupIDX, a.seqNo, b.workStationCode, b.workStationName, b.repairLineIdx, b.repairLineName) From WSGroupItem a, WorkStation b Where a.wsIDX = b.idx And b.recordStatus = 0");
        // 只查询已启用的工位
        sb.append(" And b.status = '").append(WorkStation.USE_STATUS).append("'");
        WSGroupItem entity = searchEntity.getEntity();
        // 查询条件 - 工位组主键
        if (!StringUtil.isNullOrBlank(entity.getWsGroupIDX())) {
            sb.append(" And a.wsGroupIDX = '").append(entity.getWsGroupIDX()).append("'");
        }
        Order[] orders = searchEntity.getOrders();
        // 默认已顺序号进行排序
        if (null == orders || orders.length <= 0) {
            sb.append(" Order By a.seqNo");
        } else {
            String order = orders[0].toString();
            if (order.contains("seqNo")) {
                sb.append(" Order By a.").append(order);
            } else {
                sb.append(" Order By b.").append(order);
            }
        }
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>物理删除后对记录进行重排序
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要删除的实体idx主键数组
     */
    @Override
    public void deleteByIds(Serializable... ids) throws BusinessException {
        if (null == ids || 0 >= ids.length) {
            return;
        }
        WSGroupItem t = this.getModelById(ids[0]);
        super.deleteByIds(ids);
        try {
            updateSort(this.findAll(t));
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
    
}
