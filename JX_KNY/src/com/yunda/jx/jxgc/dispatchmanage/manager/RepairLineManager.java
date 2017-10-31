package com.yunda.jx.jxgc.dispatchmanage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Table;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RepairLine业务类,检修流水线
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "repairLineManager")
public class RepairLineManager extends JXBaseManager<RepairLine, RepairLine> {
    
    /** 工位业务对象 */
    @Resource
    private WorkStationManager workStationManager;
    
    /**
     * <li>说明：确定该业务类是否使用查询缓存
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-11-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return true使用查询缓存，false不使用
     */
    @Override
    protected boolean enableCache() {
        return true;
    }
    
    /**
     * <li>说明：新增修改保存前的实体对象前的验证业务
     * <li>创建人：程锐
     * <li>创建日期：2012-12-07
     * <li>修改人：
     * <li>修改日期：2012-12-09
     * <li>修改内容：增加业务逻辑，新增或更新记录时需要验证流水线编码唯一
     * @param entity 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(RepairLine entity) throws BusinessException {
        // 根据流水线编码获取流水线对象
        RepairLine line = getRepairLineListByCode(entity.getRepairLineCode());
        // 如果没有该流水线编码对于的流水线，或者改流水线编码对应的就是正在被更新的流水线，则验证通过；否则返回错误消息
        return null == line || line.getIdx().equals(entity.getIdx()) ?
            null : new String[] { "流水线编码：" + entity.getRepairLineCode() + "已存在！" };
        
    }
    
    /**
     * <li>说明：作废流水线时验证，如有对应未作废的工位信息则不能作废
     * <li>创建人：程锐
     * <li>创建日期：2012-12-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param status 设置状态
     * @param ids 实体对象的idx主键数组
     * @return 返回更新状态操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    public String[] validateUpdateStatus(int status, Serializable... ids) throws BusinessException {
        if (RepairLine.NULLIFY_STATUS != status) {
            return null;
        }
        List<String> errMsg = new ArrayList<String>();
        RepairLine repairLine = null;
        StringBuilder sb = null;
        for (Serializable id : ids) {
            List<WorkStation> list = workStationManager.getWorkStationListByRepairId(id);
            if (null == list || list.size() <= 0) {
                continue;
            }
            repairLine = getModelById(id);
            sb = new StringBuilder("流水线：");
            sb.append(repairLine.getRepairLineName()) // 流水线名称
                .append("（").append(repairLine.getRepairLineCode()) // 流水线编码
                .append("）").append("有对应工位，不能作废！");
            errMsg.add(sb.toString());
        }
        return 0 >= errMsg.size() ? null : errMsg.toArray(new String[errMsg.size()]);
    }
    
    /**
     * <li>说明：分页查询，返回实体类的分页列表对象
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 流水线实体对象
     * @param status 状态查询参数
     * @param repairLineType 类型查询参数
     * @return Page 分页查询对象
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page findPageList(final SearchEntity<RepairLine> searchEntity, String status, String repairLineType) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("from RepairLine where recordStatus = 0");
        RepairLine repairLine = searchEntity.getEntity();
        if (!StringUtil.isNullOrBlank(status)) {
            hql.append(" and status in (").append(status).append(")");
        }
        if (!StringUtil.isNullOrBlank(repairLineType)) {
            hql.append(" and repairLineType in (").append(repairLineType).append(")");
        }
        if (!StringUtil.isNullOrBlank(repairLine.getRepairLineCode())) {
            hql.append(" and repairLineCode like '%").append(repairLine.getRepairLineCode()).append("%'");
        }
        if (!StringUtil.isNullOrBlank(repairLine.getRepairLineName())) {
            hql.append(" and repairLineName like '%").append(repairLine.getRepairLineName()).append("%'");
        }
        if (!StringUtil.isNullOrBlank(String.valueOf(repairLine.getPlantOrgId()))) {
            hql.append(" and plantOrgId = ").append(repairLine.getPlantOrgId());
        }
        Order[] orders = searchEntity.getOrders();
        hql.append(HqlUtil.getOrderHql(orders));
        
        String totalHql = "select count(*) " + hql.toString();
        if (enableCache()) {
            return super.cachePageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
        }
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：更新流水线记录的业务状态，可变更为启用或废弃状态
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param status 状态值
     * @param ids 流水线主键数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateStatus(int status, Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<RepairLine> entityList = new ArrayList<RepairLine>();
        for (Serializable id : ids) {
            RepairLine repairLine = this.getModelById(id);
            repairLine = EntityUtil.setSysinfo(repairLine); // 根据IDX主键设置创建人创建时间等基本信息
            switch (status) {
                case RepairLine.USE_STATUS:         // 启用
                    repairLine.setStatus(RepairLine.USE_STATUS);
                    break;
                case RepairLine.NULLIFY_STATUS:     // 作废
                    repairLine.setStatus(RepairLine.NULLIFY_STATUS);
                    break;
                default:
                    throw new BusinessException(repairLine.getRepairLineCode() + "流水线：不能更新为该业务状态值" + status);
            }
            entityList.add(repairLine);
        }
        if (entityList.size() <= 0) {
            return;
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * <li>说明：根据流水线编码获取流水线对象
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param repairLineCode 流水线编码
     * @return 流水线对象
     */
    @SuppressWarnings("unchecked")
    public RepairLine getRepairLineListByCode(String repairLineCode) {
        String hql = "from RepairLine where recordStatus = 0 and repairLineCode = ?";
        return (RepairLine) this.daoUtils.findSingle(hql, new Object[] { repairLineCode });
    }
    
    /**
     * <li>说明：保存或更新流水线，更新流水线时同步更新流水线对应工位的流水线名称
     * <li>创建人：程锐
     * <li>创建日期：2013-12-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 流水线对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveRepairLine(RepairLine t) throws BusinessException, NoSuchFieldException {
        // 如果是新增的流水线，则没有更新对应工位的操作
        if (!StringUtil.isNullOrBlank(t.getIdx())) {
            List<WorkStation> workStationList = workStationManager.getWorkStationListByRepairId(t.getIdx());
            if (null != workStationList && workStationList.size() > 0) {
                List<WorkStation> list = new ArrayList<WorkStation>();
                for (WorkStation station : workStationList) {
                    // 检验流水线名称是否发生了修改
                    if (t.getRepairLineName().equals(station.getRepairLineName())) {
                        continue;
                    }
                    station.setRepairLineName(t.getRepairLineName());
                    list.add(station);
                }
                if (list.size() > 0) {
                    this.workStationManager.saveOrUpdate(list);
                }
            }
        }
        super.saveOrUpdate(t);
    }
    
    /**
     * <li>说明：流水线及下属工位树
     * <li>创建人：何涛
     * <li>创建日期：2015-9-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级主键，根节点主键默认为“ROOT_0”
     * @param checkedTree 树节点是否支持多选的标识
     * @return  List<HashMap<String, Object>> 实体集合
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree(String parentIDX, boolean checkedTree) {
        if ("ROOT_0".equals(parentIDX)) {
            String hql = "From RepairLine Where recordStatus = 0 And status = ? And repairLineType = ? Order By repairLineName ASC";
            List<RepairLine> list = this.daoUtils.find(hql, new Object[]{ RepairLine.USE_STATUS, RepairLine.TYPE_TRAIN });
            List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = null;
            for (RepairLine rl : list) {
                map = new HashMap<String, Object>();
                map.put("id", rl.getIdx());
                map.put("text", rl.getRepairLineName() + "(" + rl.getRepairLineCode() + ")");
                map.put("leaf", false);
                children.add(map);
            }
            return children;
        }
        RepairLine entity = this.getModelById(parentIDX);
        if (null != entity && "JXGC_REPAIR_LINE".equalsIgnoreCase(entity.getClass().getAnnotation(Table.class).name())) {
            return this.workStationManager.tree("ROOT_0", parentIDX, checkedTree);
        }
        return this.workStationManager.tree(parentIDX, null, checkedTree);
    }
    
}
