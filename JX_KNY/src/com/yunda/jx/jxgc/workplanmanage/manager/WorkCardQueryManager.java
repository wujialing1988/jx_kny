package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.Worker;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkerManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 作业工单查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-4-29
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "workCardQueryManager")
public class WorkCardQueryManager extends JXBaseManager<WorkCard, WorkCard> {
    @Resource
    private WorkerManager workerManager;
    
    @Resource
    private QCResultManager qCResultManager;
    
    /**
     * <li>说明：获取流程节点关联的作业工单分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
     * @return 流程节点关联的作业工单分页列表
     * @throws BusinessException
     */
    public Page<WorkCardQueryBean> getWorkCardListByNode(SearchEntity<WorkCard> searchEntity) throws BusinessException {
        
        WorkCard entity = searchEntity.getEntity();
        // 查询条件 - 车型
        if (StringUtil.isNullOrBlank(entity.getNodeCaseIDX())) 
            return new Page<WorkCardQueryBean>();
        String sql = SqlMapUtil.getSql("jxgc-workcardquery:getWorkCardListByNode")
                               .replace("#nodeIDX#", entity.getNodeCaseIDX());
        StringBuilder sb = new StringBuilder(sql);
        // 查询条件 - 作业名称
        if (!StringUtil.isNullOrBlank(entity.getWorkCardName())) {
            sb.append(" AND A.WORK_CARD_NAME LIKE '%").append(entity.getWorkCardName()).append("%'");
        }
        AbstractEntityPersister meta = (AbstractEntityPersister) getDaoUtils().getSessionFactory().getClassMetadata(WorkCardQueryBean.class);
        String sortString = "";
        Order[] orders = searchEntity.getOrders();
        if (orders != null && orders.length > 0) {
            for (Order order : orders) {
                String[] orderStrings = StringUtil.tokenizer(order.toString(), " ");
                if (orderStrings == null || orderStrings.length != 2)
                    continue;
                if (orderStrings[0].equals("workCardCode") || 
                    orderStrings[0].equals("workCardName") ||                      
                    orderStrings[0].equals("status")) {
                    sortString = CommonUtil.buildOrderSql("A.", meta, orderStrings);
                } 
            }
            sb.append(sortString);
        } else {
            sb.append(" ORDER BY A.WORK_CARD_CODE ASC");
        }
        sql = sb.toString();
        StringBuilder totalSql = new StringBuilder("SELECT COUNT(*) AS ROWCOUNT FROM (").append(sql).append(Constants.BRACKET_R);
        Page<WorkCardQueryBean> page = this.acquirePageList(totalSql.toString(), sql, searchEntity.getStart(), searchEntity.getLimit(), false); 
        
        //准备拼接作业人员姓名
        for (WorkCardQueryBean w : page.getList()) {
            StringBuffer stringBuffer = new StringBuffer(300);
            
            //如果worker为空说明工单为处理中，处理人通过作业工单id反查pjgc_worker表的worker人员
            if (StringUtil.isNullOrBlank(w.getWorker())) {
                Worker worker = new Worker();
                worker.setWorkCardIDX(w.getIdx());
                //通过worCardIDX查询对应的work信息，用,分割
                List<Worker> list = workerManager.findList(worker);
                int listSize = list.size();
                for (int i = 0; i < listSize; i++) {
                    Worker wo = list.get(i);
                    if (i == listSize - 1) {
                    }else {
                        stringBuffer.append(wo.getWorkerName()).append(",");
                    }
                }
            //如果是处理完成的，直接取worker字段中的值即可
            }else{
                stringBuffer.append(w.getWorker());
            }
            w.setWorker(stringBuffer.toString());
        }
        
        return page;
    }
    
    /**
     * <li>说明：基于sql查询语句的分页查询
     * <li>创建人：程锐
     * <li>创建日期：2015-1-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param totalSql 查询总记录数的sql语句
     * @param sql 查询语句
     * @param start 开始行
     * @param limit 每页记录数
     * @param isQueryCacheEnabled 是否启用查询缓存
     * @return Page<ZbglJYRdpBean> 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<WorkCardQueryBean> acquirePageList(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled)
        throws BusinessException {
        final int beginIdx = start < 0 ? 0 : start;
        final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
        final String total_sql = totalSql;
        final String fSql = sql;
        final Boolean useCached = isQueryCacheEnabled;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<WorkCardQueryBean>) template.execute(new HibernateCallback() {
            
            public Page<WorkCardQueryBean> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = s.createSQLQuery(total_sql);
                    query.addScalar("rowcount", Hibernate.INTEGER);
                    query.setCacheable(useCached); // 缓存开关
                    int total = ((Integer) query.uniqueResult()).intValue();
                    query.setCacheable(false);
                    int begin = beginIdx > total ? total : beginIdx;
                    query = (SQLQuery) s.createSQLQuery(fSql).addEntity(WorkCardQueryBean.class).setFirstResult(begin).setMaxResults(pageSize);
                    query.setCacheable(useCached); // 缓存开关
                    return new Page<WorkCardQueryBean>(total, query.list());
                } catch (HibernateException e) {
                    throw e;
                } finally {
                    if (query != null)
                        query.setCacheable(false);
                }
            }
        });
    }
    
    /**
     * <li>说明：获取节点关联的已完成的作业工单数量
     * <li>创建人：程锐
     * <li>创建日期：2015-5-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 节点关联的已完成的作业工单数量
     */
    public int getCompleteWorkCardCountByNode(String nodeIDX) {
        return CommonUtil.getListSize(getCompleteWorkCardByNode(nodeIDX));
    }
    
    /**
     * <li>说明：获取节点关联的作业工单数量
     * <li>创建人：程锐
     * <li>创建日期：2015-5-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 节点关联的作业工单数量
     */
    public int getWorkCardCountByNode(String nodeIDX) {
        return CommonUtil.getListSize(getWorkCardByNode(nodeIDX));
    }
    
    /**
     * <li>说明：获取节点关联的已处理或已终止的作业工单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-22
     * <li>修改人： 程锐
     * <li>修改日期：2015-6-4
     * <li>修改内容：加入质检中状态的作业工单
     * @param nodeIDX 节点IDX
     * @return 节点关联的已处理或已终止的作业工单列表
     */
    @SuppressWarnings("unchecked")
    public List<WorkCard> getCompleteWorkCardByNode(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("nodeCaseIDX", nodeIDX);
        StringBuilder status = new StringBuilder();
        status.append("in'")
              .append(WorkCard.STATUS_HANDLED)
              .append("','")
              .append(WorkCard.STATUS_TERMINATED)
              .append("','")
              .append(WorkCard.STATUS_FINISHED)
              .append("'");
        paramMap.put("status", status.toString());
        return getWorkCardList(paramMap);
    }
    
    /**
     * <li>说明：获取节点关联的作业工单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX 
     * @return 节点关联的作业工单列表
     */
    @SuppressWarnings("unchecked")
    public List<WorkCard> getWorkCardByNode(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("nodeCaseIDX", nodeIDX);
        return getWorkCardList(paramMap);
    }
    
    /**
     * <li>说明：获取作业工单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 作业工单列表
     */
    @SuppressWarnings("unchecked")
    private List<WorkCard> getWorkCardList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from WorkCard where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：根据工艺节点获取处理中和已处理的作业工单列表
     * <li>创建人：程锐
     * <li>创建日期：2013-7-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCaseIDX 流程节点ID
     * @param workPlanIDX 作业计划IDX
     * @return 处理中和已处理的作业工单列表
     * @throws Exception
     */
    @SuppressWarnings("all")
    public List getOnGoingCardByNode(String nodeCaseIDX, String workPlanIDX) throws Exception{        
        String sql = SqlMapUtil.getSql("jxgc-workcardquery:getOnGoingCardByNode").replace("#nodeCaseIDX#", nodeCaseIDX)
                                                                                 .replace("#STATUS_HANDLING#", WorkCard.STATUS_HANDLING)
                                                                                 .replace("#STATUS_HANDLED#", WorkCard.STATUS_HANDLED)
                                                                                 .replace("#STATUS_FINISHED#", WorkCard.STATUS_FINISHED)
                                                                                 .replace("#tecProcessCaseIDX#", nodeCaseIDX)
                                                                                 .replace("#STATUS_GOING#", JobProcessNode.STATUS_GOING)
                                                                                 .replace("#STATUS_COMPLETE#", JobProcessNode.STATUS_COMPLETE)
                                                                                 .replace("#workPlanIDX#", workPlanIDX);
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：获取节点下无工位和班组的未完成的作业工单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 节点下无工位和班组的未完成的作业工单列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<WorkCard> getNoStationAndTeamListByNode(String nodeIDX) throws Exception {
        String hql = SqlMapUtil.getSql("jxgc-workcardquery:getNoStationAndTeamListByNode")
                               .replace("#nodeIDXS#", CommonUtil.buildInSqlStr(nodeIDX))
                               .replace("#STATUS_HANDLING#", WorkCard.STATUS_HANDLING)
                               .replace("#STATUS_NEW#", WorkCard.STATUS_NEW)
                               .replace("#STATUS_OPEN#", WorkCard.STATUS_OPEN);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：根据作业工单列表构造作业工单IDX为以,号分隔的sql字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 作业工单列表
     * @return 以,号分隔的作业工单IDX的sql字符串
     * @throws Exception
     */
    public String buildSqlIDXStr(List<WorkCard> list) throws Exception {
        return CommonUtil.buildInSqlStr(buildIDXBuilder(list).toString());
    }
    
    /**
     * <li>说明：根据作业工单列表构造作业工单IDX为以,号分隔的字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-5-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 作业工单列表
     * @return 以,号分隔的作业工单IDX字符串
     * @throws Exception
     */
    public StringBuilder buildIDXBuilder(List<WorkCard> list) throws Exception {
        StringBuilder idxs = new StringBuilder();
        for (WorkCard card : list) {
            idxs.append(card.getIdx()).append(",");
        }
        idxs.deleteCharAt(idxs.length() - 1);
        return idxs;
    }
    
    /**
     * <li>说明：根据作业工单列表构造作业工单IDX数组
     * <li>创建人：程锐
     * <li>创建日期：2015-5-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 作业工单列表
     * @return 作业工单IDX数组
     */
    public String[] buildIDXArray(List<WorkCard> list) {
        String[] ids = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).getIdx();
        }
        return ids;
    }  
    
    /**
     * <li>说明：获取作业工单的其他处理人员
     * <li>创建人：程锐
     * <li>创建日期：2015-7-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单IDX
     * @param empid 当前人员ID
     * @return 作业人员列表
     */
    public List<Worker> getOtherWorkerByWorkCard(String workCardIDX, String empid) {
        return workerManager.getOtherWorkerByWorkCard(workCardIDX, empid);
    }
    
    /**
     * <li>说明：根据作业工单idx获取需要指派的质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2014-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDXS  作业工单idx,多个idx用,分隔
     * @return 需要指派的质量检查项列表
     * @throws Exception
     */
    public List<QCResult> getIsAssignCheckItems(String workCardIDXS) throws Exception {
        return qCResultManager.getIsAssignCheckItems(workCardIDXS);
    }
    

    
    /**
     * <li>标题: 机车检修整备管理信息系统
     * <li>说明: 作业工单查询实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人:
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部检修整备系统项目组
     * @version 1.0
     */
    @Entity
    private static class WorkCardQueryBean {
        
        /* idx主键 */
        @Id
        private String idx;
        
        /* 作业卡编码 */
        private String workCardCode;
        
        /* 作业卡名称 */
        private String workCardName;
        
        /*质量检查*/
        private String qcName;
        
        /* 状态 */
        private String status;
        
        /* 状态 */
        private String extensionClass;
        
        /* 作业人员 */
        private String worker;
        
        /**
         * <li>说明：默认构造方法
         * <li>创建人：程锐
         * <li>创建日期：2015-4-29
         * <li>修改人：
         * <li>修改日期：
         */
        public WorkCardQueryBean() {
            
        }
        
        public String getWorker() {
            return worker;
        }

        
        public void setWorker(String worker) {
            this.worker = worker;
        }

        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public String getQcName() {
            return qcName;
        }
        
        public void setQcName(String qcName) {
            this.qcName = qcName;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public String getWorkCardCode() {
            return workCardCode;
        }
        
        public void setWorkCardCode(String workCardCode) {
            this.workCardCode = workCardCode;
        }
        
        public String getWorkCardName() {
            return workCardName;
        }
        
        public void setWorkCardName(String workCardName) {
            this.workCardName = workCardName;
        }
        
        public String getExtensionClass() {
            return extensionClass;
        }
        
        public void setExtensionClass(String extensionClass) {
            this.extensionClass = extensionClass;
        }
        
    }
}
