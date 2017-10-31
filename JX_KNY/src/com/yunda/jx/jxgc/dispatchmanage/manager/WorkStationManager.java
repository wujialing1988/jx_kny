
package com.yunda.jx.jxgc.dispatchmanage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStation业务类,工位
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "workStationManager")
public class WorkStationManager extends JXBaseManager<WorkStation, WorkStation> {
    
    /** WorkStationWorker业务类,工位作业人员 */
    @Resource
    private WorkStationWorkerManager workStationWorkerManager;
    
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}    
    /**
     * <li>说明：新增修改保存前的实体对象前的验证业务
     * <li>创建人：程锐
     * <li>创建日期：2012-12-07
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param entity 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(WorkStation entity) throws BusinessException {
        String[] errs = null;
        List<WorkStation> list = getWorkStationListByCode(entity.getWorkStationCode(), entity.getIdx());
        if (list != null && list.size() > 0) {
            errs = new String[1];
            errs[0] = "工位编码已存在！";
            return errs;
        }
        return null;
    }   
    
    /**
     * <li>说明：分页查询，返回实体类的分页列表对象
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人：刘晓斌
     * <li>修改日期：2013-11-21
     * <li>修改内容：利用查询缓存
     * 
     * @param searchEntity 工位实体对象
     * @param status 状态查询参数
     * @return Page 分页查询对象
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page findPageList(final SearchEntity<WorkStation> searchEntity, String status) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("from WorkStation where recordStatus = 0");
        WorkStation workStation = searchEntity.getEntity();
        if (!StringUtil.isNullOrBlank(workStation.getRepairLineIdx())) {
            hql.append(" and repairLineIdx = '").append(workStation.getRepairLineIdx()).append("'");
        }
        if (!StringUtil.isNullOrBlank(status)) {
            hql.append(" and status in (").append(status).append(")");
        }
        
        if (!StringUtil.isNullOrBlank(workStation.getWorkStationCode())) {
            hql.append(" and workStationCode like '%").append(workStation.getWorkStationCode()).append(Constants.LIKE_PIPEI);
        }
        if (!StringUtil.isNullOrBlank(workStation.getWorkStationName())) {
            hql.append(" and workStationName like '%").append(workStation.getWorkStationName()).append(Constants.LIKE_PIPEI);
        }
        if (!StringUtil.isNullOrBlank(workStation.getIdx())) {
            hql.append(" and idx = '").append(workStation.getIdx()).append("'");
        }
        if ("ROOT_0".equals(workStation.getParentIDX()) || StringUtil.isNullOrBlank(workStation.getParentIDX())) {
            hql.append(" and (parentIDX is null or parentIDX = 'ROOT_0')");
        } else {
            hql.append(" and parentIDX ='").append(workStation.getParentIDX()).append("'");
        }
        Order[] orders = searchEntity.getOrders();
        hql.append(HqlUtil.getOrderHql(orders));

        String totalHql = "select count(*) " + hql.toString();
        if(enableCache()){
        	return super.cachePageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
        } else {
        	return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
        }
    }
    
    /**
     * <li>说明：更新工位记录的业务状态，可变更为启用或废弃状态
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param status 状态值
     * @param ids 工位主键数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateStatus(int status, Serializable... ids) throws BusinessException, NoSuchFieldException {
        for (Serializable id : ids) {
            WorkStation workStation = this.getModelById(id);
            // 更新状态
            workStation.setStatus(status);
            if (WorkStation.NULLIFY_STATUS == status) {
                // 作废时，删除该工位下的作业人员
                workStationWorkerManager.deleteByWorkStationIDX((String)id);
            }
            // 如果是“作废”则递归作废下属所有子工位
            List<WorkStation> list = this.findChildren((String)id, workStation.getRepairLineIdx(), WorkStation.USE_STATUS);
            if (0 < list.size()) {
                Serializable[] array = new Serializable[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i).getIdx();
                }
                this.updateStatus(status, array);
            }
            this.saveOrUpdate(workStation);
        }
    }
    
    /**
     * <li>说明：根据工位编码、主键获取工位对象list列表
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人：刘晓斌
     * <li>修改日期：2013-11-21
     * <li>修改内容：利用查询缓存
     * @param workStationCode 工位编码
     * @param idx 工位主键
     * @return List<WorkStation> 工位list列表
     */
    @SuppressWarnings("unchecked")
    public List<WorkStation> getWorkStationListByCode(String workStationCode, String idx) {
        String hql = "from WorkStation where recordStatus = 0 and workStationCode='" + workStationCode + "'";
        if (!StringUtil.isNullOrBlank(idx)) {
            hql += " and idx != '" + idx + "'";
        }
        return daoUtils.find(enableCache(), hql);
    }
    
    /**
     * <li>说明：根据流水线主键获取非作废状态的工位list列表
     * <li>创建人：程锐
     * <li>创建日期：2012-12-10
     * <li>修改人：刘晓斌
     * <li>修改日期：2013-11-21
     * <li>修改内容：利用查询缓存
     * @param repairLineIdx 流水线主键
     * @return List<WorkStation> 工位list列表
     */
    @SuppressWarnings("unchecked")
    public List<WorkStation> getWorkStationListByRepairId(Serializable repairLineIdx) {
        String hql = "from WorkStation where recordStatus = 0 and repairLineIdx='" + repairLineIdx + 
        			 "' and status != " + WorkStation.NULLIFY_STATUS;
        return daoUtils.find(enableCache(), hql);
    }    
    /**
     * 
     * <li>说明：获取工位选择控件前台Map对象
     * <li>创建人：程锐
     * <li>创建日期：2013-3-12
     * <li>修改人：刘晓斌
     * <li>修改日期：2013-11-21
     * <li>修改内容：利用查询缓存
     * @param searchEntity 工位实体对象包装类
     * @param start 开始行
     * @param limit 每页记录数
     * @param queryHql 查询Hql字符串 
     * @return Map<String,Object> 分页页面Map对象
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> page(final SearchEntity<WorkStation> searchEntity,int start, int limit, String queryHql) throws ClassNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();
        String hql = "";
        String totalHql = "";
        StringBuilder awhere = new StringBuilder();
        WorkStation workStation = searchEntity.getEntity();
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql = queryHql;
            int beginPos = hql.toLowerCase().indexOf(Constants.FROM);
            totalHql = " select count (*) " + hql.substring(beginPos);
        }        
        // queryHql配置项为空则按entity拼接Hql
        else {
            hql = " from WorkStation where 1=1 and recordStatus=0 ";
            totalHql = " select count(*) from WorkStation where 1=1 and recordStatus=0 ";
        }
        if (!StringUtil.isNullOrBlank((workStation.getWorkStationName()))) {
            awhere.append(" and workStationName like '%").append(workStation.getWorkStationName()).append(Constants.LIKE_PIPEI);
        }
        if (!StringUtil.isNullOrBlank((workStation.getWorkStationCode()))) {
            awhere.append(" and workStationCode like '%").append(workStation.getWorkStationCode()).append(Constants.LIKE_PIPEI);
        }
        if (!StringUtil.isNullOrBlank((workStation.getRepairLineName()))) {
            awhere.append(" and repairLineName like '%").append(workStation.getRepairLineName()).append(Constants.LIKE_PIPEI);
        }
        hql += awhere.toString();
        totalHql += awhere.toString();
        Page page = null;
        if(enableCache()){
        	page = super.cachePageList(totalHql, hql, start, limit);
        } else {
        	page = super.findPageList(totalHql, hql, start, limit);
        }   
        map = page.extjsStore();
        return map;
    }    
        
    /**
     * 
     * <li>说明：工长默认派工页面列表
     * <li>创建人：程锐
     * <li>创建日期：2013-7-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 工位实体对象包装类
     * @param orderString 排序字符串
     * @return 分页列表
     */
    public Page<WorkStation> workStationPageList(SearchEntity<WorkStation> searchEntity, String orderString){
        SystemContext.getOmOrganization().getOrgid();
        String selectSql = SqlMapUtil.getSql("jxgc-repairline:gzforeman-select");
        String fromSql = SqlMapUtil.getSql("jxgc-repairline:gzforeman-from")
            .replace("#teamOrgId#", SystemContext.getOmOrganization().getOrgid().toString())
            .replace("#stationStatus#", String.valueOf(WorkStation.USE_STATUS));
        String sql = selectSql + fromSql;
        String totalSql = "select count(a.idx) " + fromSql;
        StringBuffer awhere =  new StringBuffer();
        WorkStation workStation = searchEntity.getEntity();
        if(!StringUtil.isNullOrBlank(workStation.getIdx())){
            awhere.append(" and a.idx = '").append(workStation.getIdx()).append("'");
        }
        if(!StringUtil.isNullOrBlank(workStation.getRepairLineName())){
            awhere.append(" and d.REPAIR_LINE_NAME like '%").append(workStation.getRepairLineName()).append(Constants.LIKE_PIPEI);
        }
        if(!StringUtil.isNullOrBlank(workStation.getWorkStationCode())){
            awhere.append(" and a.WORK_STATION_CODE like '%").append(workStation.getWorkStationCode()).append(Constants.LIKE_PIPEI);
        }
        if(!StringUtil.isNullOrBlank(workStation.getWorkStationName())){
            awhere.append(" and a.WORK_STATION_NAME like '%").append(workStation.getWorkStationName()).append(Constants.LIKE_PIPEI);
        }
        totalSql += awhere;
        sql += awhere + " " + orderString;
        return findPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), null, searchEntity.getOrders());
    }
    
    /**
     * <li>说明：查询工位信息用于工艺节点关联工位时选择
     * <li>创建人：程梅
     * <li>创建日期：2013-7-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @param searchEntity 查询条件实体
     * @param tecProcessNodeIdx 工艺节点主键
     * @return 分页列表
     * @throws BusinessException
     */
    public Page<WorkStation> findListForTecProcessNode(final SearchEntity<WorkStation> searchEntity, String tecProcessNodeIdx) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("select new WorkStation(w.idx, w.workStationCode, w.workStationName, r.repairLineName) from WorkStation w,RepairLine r where w.recordStatus = 0 and r.recordStatus = 0 and w.repairLineIdx=r.idx and w.status=");
        hql.append(WorkStation.USE_STATUS);
        hql.append(" and r.status=").append(RepairLine.USE_STATUS);        
        hql.append(" and w.idx not in (select t.workStationIdx from TecnodeUnionStation t where t.recordStatus = 0 ");
        hql.append(" and t.tecProcessNodeIdx='"+tecProcessNodeIdx+"')");
        
        WorkStation station = searchEntity.getEntity();
        if(!StringUtil.isNullOrBlank(station.getWorkStationName())){
        	hql.append(" and w.workStationName like '%").append(station.getWorkStationName()).append(Constants.LIKE_PIPEI);
        }
        hql.append("and r.repairLineType = ");
        if ((RepairLine.TYPE_TRAIN + "").equals(station.getLineType())) {
            hql.append(RepairLine.TYPE_TRAIN);
        } else if ((RepairLine.TYPE_PARTS + "").equals(station.getLineType())) {
            hql.append(RepairLine.TYPE_PARTS);
        }
        String totalHql = "select count(*) as rowcount " + hql.substring(hql.indexOf(Constants.FROM));
        return findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    
    /**
     * <li>说明：工位选择控件查询方法
     * <li>创建人：王利成
     * <li>创建日期：2015-4-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件实体
     * @return 分页列表
     * @throws BusinessException
     */
    public Page findSelectPageList(final SearchEntity<WorkStation> searchEntity) throws BusinessException {
        WorkStation workStation = searchEntity.getEntity();
        StringBuilder hql = new StringBuilder();
        hql.append("select new WorkStation(a.idx, a.workStationCode, a.workStationName, a.repairLineIdx, a.repairLineName, a.teamOrgId, a.teamOrgName, a.teamOrgSeq) from WorkStation a where");
        if(!StringUtil.isNullOrBlank(workStation.getNodeIDX())){
            hql.append(" a.idx in ");
            hql.append(" ( ");
            hql.append(" select b.workStationIDX from JobNodeStationDef b where b.recordStatus = 0 and b.nodeIDX = '").append(workStation.getNodeIDX()).append("'");
            hql.append(" ) ");
            hql.append(" and ");
        }
        hql.append(" a.recordStatus = 0 and a.status = ");
        hql.append(WorkStation.USE_STATUS);
        if (!StringUtil.isNullOrBlank(workStation.getWorkStationCode())) {
            hql.append(" and a.workStationCode like '%").append(workStation.getWorkStationCode()).append(Constants.LIKE_PIPEI);
        }
        if (!StringUtil.isNullOrBlank(workStation.getWorkStationName())) {
            hql.append(" and a.workStationName like '%").append(workStation.getWorkStationName()).append(Constants.LIKE_PIPEI);
        }
        Order[] orders = searchEntity.getOrders();
        hql.append(HqlUtil.getOrderHql(orders));
        
        String totalHql = "select count(*) as rowcount " + hql.substring(hql.indexOf(Constants.FROM));
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * 
     * <li>说明：根据工位编码查询工位信息
     * <li>创建人：程梅
     * <li>创建日期：2015-5-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workStationCode 工位编码
     * @return 工位信息记录集合
     */
    @SuppressWarnings("unchecked")
    public List<WorkStation> getWorkStationByCode(String workStationCode){
        String hql = "select a From WorkStation a where a.recordStatus=0 and status="+WorkStation.USE_STATUS + " and deskCode is null and deskName is null ";
        if(!StringUtil.isNullOrBlank(workStationCode)){
            hql += " and a.workStationCode='"+workStationCode+"'";
        }
        List<WorkStation> list = (List<WorkStation>)daoUtils.find(hql);
        return list;
    }
    
    /**
     * <li>说明：根据输入的工位编码实现台位绑定工位
     * <li>创建人：程梅
     * <li>创建日期：2015-5-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param stationCode 所属台位编码 检修V3.2 存台位的guid 
     * @param stationName 所属台位名称
     * @param mapcode 所属图 检修V3.2 存siteID
     * @param workStationCode 工位编码
     * @return 已绑定的工位记录集合
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public List saveStationByCode(String stationCode, String stationName, String mapcode, String workStationCode) throws BusinessException, NoSuchFieldException {
        //根据工位编码查询工位信息
        List<WorkStation> entityList = getWorkStationByCode(workStationCode);
        if(null != entityList && entityList.size() > 0 ){
            saveStation(stationCode , stationName ,mapcode ,entityList);
            return entityList ;
        }else return null ;
        
    }
    /**
     * 
     * <li>说明：根据所选工位信息实现台位绑定工位
     * <li>创建人：程梅
     * <li>创建日期：2015-5-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param stationCode 所属台位编码 检修V3.2 存台位的guid 
     * @param stationName 所属台位名称
     * @param mapcode 所属图 检修V3.2 存siteID
     * @param stationList 工位信息记录集合
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveStation(String stationCode,String stationName,String mapcode ,List<WorkStation> stationList) throws BusinessException, NoSuchFieldException {
        List<WorkStation> saveList = new ArrayList<WorkStation>();
        for (WorkStation station : stationList) {
            WorkStation workStation = getModelById(station.getIdx());
            workStation.setDeskCode(stationCode) ;
            workStation.setDeskName(stationName) ;
            workStation.setOwnerMap(mapcode) ;//所属图
            workStation = EntityUtil.setSysinfo(workStation);
//          设置逻辑删除字段状态为未删除
            workStation = EntityUtil.setNoDelete(workStation);
            saveList.add(workStation);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(saveList);
    }

    /**
     * <li>说明：删除【解除绑定】
     * <li>创建人：程梅
     * <li>创建日期：2015-5-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 工位主键数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void logicUpdate(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<WorkStation> entityList = new ArrayList<WorkStation>();
        for (Serializable id : ids) {
            WorkStation station = getModelById(id);
            station.setDeskCode("") ;
            station.setDeskName("") ;
            station.setOwnerMap("") ;//所属图
            station = EntityUtil.setSysinfo(station);
            entityList.add(station);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * 
     * <li>说明：查询工位列表【台位绑定工位中选择工位】
     * <li>创建人：程梅
     * <li>创建日期：2015-5-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
     * @return 工位分页查询Page
     * @throws BusinessException
     */
    public Page<WorkStation> findListForBindWorkStation(final SearchEntity<WorkStation> searchEntity) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("select new WorkStation(w.idx, w.workStationCode, w.workStationName, w.repairLineIdx, w.equipIDX, w.equipName, r.repairLineName, ");
        hql.append("w.status, w.remarks ) from WorkStation w,RepairLine r where w.recordStatus = 0 and r.recordStatus = 0 and w.repairLineIdx=r.idx and w.status=");
        hql.append(WorkStation.USE_STATUS);
        hql.append(" and r.status=").append(RepairLine.USE_STATUS);        
        hql.append(" and w.deskCode is null and w.deskName is null ");
        
        WorkStation station = searchEntity.getEntity();
        if(!StringUtil.isNullOrBlank(station.getWorkStationName())){
            hql.append(" and w.workStationName like '%").append(station.getWorkStationName()).append(Constants.LIKE_PIPEI);
        }
        if(!StringUtil.isNullOrBlank(station.getWorkStationCode())){
            hql.append(" and w.workStationCode like '%").append(station.getWorkStationCode()).append(Constants.LIKE_PIPEI);
        }
        if(!StringUtil.isNullOrBlank(station.getRepairLineName())){
            hql.append(" and w.repairLineName like '%").append(station.getRepairLineName()).append(Constants.LIKE_PIPEI);
        }
        if((RepairLine.TYPE_TRAIN + "").equals(station.getLineType())){
           hql.append("and r.repairLineType = " + RepairLine.TYPE_TRAIN);
        }else if((RepairLine.TYPE_PARTS + "").equals(station.getLineType())){
            hql.append("and r.repairLineType = " + RepairLine.TYPE_PARTS);
        }
        
        int beginPos = hql.toString().toLowerCase().indexOf(Constants.FROM);
        StringBuffer totalHql = new StringBuffer(" select count(*) ");
        totalHql.append(hql.toString().substring(beginPos));
        return findPageList(totalHql.toString(), hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    
    /**
     * <li>说明：工位树
     * <li>创建人：何涛
     * <li>创建日期：2015-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级节点idx主键
     * @param repairLineIdx 流水线主键
     * @param checkedTree 树节点是否支持多选的标识
     * @return List<HashMap<String, Object>> 实体集合
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree(String parentIDX, String repairLineIdx, boolean checkedTree) {
        StringBuilder sb = new StringBuilder("From WorkStation Where recordStatus = 0");
        // 流水线主键
        if (!StringUtil.isNullOrBlank(repairLineIdx)) {
            sb.append(" And repairLineIdx = '").append(repairLineIdx).append("'");
        }
        // 启用状态
        sb.append(" And status = '").append(WorkStation.USE_STATUS).append("'");
        
        if ("ROOT_0".equals(parentIDX)) {
            sb.append(" And (parentIDX Is Null Or parentIDX = '").append(parentIDX).append("')");
        } else {
            sb.append(" And parentIDX = '").append(parentIDX).append("'");
        }
        // 默认以工位名称排序
        sb.append(" Order By workStationName ASC");
        List<WorkStation> list = (List<WorkStation>) this.daoUtils.find(sb.toString());
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (WorkStation t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            // 节点idx主键
            nodeMap.put("id", t.getIdx()); 
            // 树节点显示名称
            nodeMap.put("text", new StringBuilder(t.getWorkStationName()).append("(").append(t.getWorkStationCode()).append(")")); 
            // 是否是叶子节点
            nodeMap.put("leaf", this.isLeaf(t.getIdx(), repairLineIdx)); 
            if (checkedTree) {
                nodeMap.put("checked", false); 
            }
            nodeMap.put("repairLineIdx", t.getRepairLineIdx()); 
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：查询指定工位下属子工位集合
     * <li>创建人：何涛
     * <li>创建日期：2015-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 工位主键
     * @param repairLineIdx 流水线主键
     * @param status 工位状态
     * @return 指定工位下属子工位集合
     */
    @SuppressWarnings("unchecked")
    private List<WorkStation> findChildren(String idx, String repairLineIdx, int status) {
        String hql = "From WorkStation Where recordStatus = 0 And parentIDX = ? And repairLineIdx = ? And status = ?";
        if (!StringUtil.isNullOrBlank(repairLineIdx)) {
            return this.daoUtils.find(hql, new Object[]{idx, repairLineIdx, status});
        }
        hql = "From WorkStation Where recordStatus = 0 And parentIDX = ? And status = ?";
        return this.daoUtils.find(hql, new Object[]{idx, status});
    }
    
    /**
     * <li>说明：查询指定工位是否为叶子节点
     * <li>创建人：何涛
     * <li>创建日期：2015-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 工位主键
     * @param repairLineIdx 流水线主键
     * @return 是否为叶子节点 true：是，false：否
     */
    private boolean isLeaf(String idx, String repairLineIdx) {
        return findChildren(idx, repairLineIdx, WorkStation.USE_STATUS).size() > 0 ? false : true;
    }
    
    /**
     * <li>说明：获取指定工位及其子工位的主键集合
     * <li>创建人：何涛
     * <li>创建日期：2015-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 工位主键 
     * @param list 工位的主键集合
     */
    public void findChildrenIds(String idx, List<String> list) {
        list.add(idx);
        List<WorkStation> entityList = this.findChildren(idx, null, WorkStation.USE_STATUS);
        for (WorkStation entity : entityList) {
            findChildrenIds(entity.getIdx(), list);
        }
    }
    
}
