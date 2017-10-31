package com.yunda.zb.rdp.zbbill.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
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
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.pczz.manager.ZbglPczzWIManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.manager.ZbglTpExceptionManager;
import com.yunda.zb.tp.manager.ZbglTpManager;
import com.yunda.zb.trainclean.manager.ZbglCleaningManager;
import com.yunda.zb.trainhandover.manager.ZbglHoCaseManager;
import com.yunda.zb.trainonsand.manager.ZbglSandingManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备交验业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-3-12
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglRdpJYManager")
public class ZbglRdpJYManager extends JXBaseManager<ZbglRdp, ZbglRdp> {
    
    /** 机车上砂业务类 */
    @Resource
    ZbglSandingManager zbglSandingManager;
    
    /** 机车整备任务单业务类 */
    @Resource
    ZbglRdpWiManager zbglRdpWiManager;
    
    /** 提票业务类 */
    @Resource
    ZbglTpManager zbglTpManager;
    
    /** 机车交接记录业务类 */
    @Resource
    ZbglHoCaseManager zbglHoCaseManager;
    
    /** 提票例外放行业务类 */
    @Resource
    ZbglTpExceptionManager zbglTpExceptionManager;
    
    /** 机车保洁业务类 */
    @Resource
    ZbglCleaningManager zbglCleaningManager;
    
    /** 普查整治任务单业务类 */
    @Resource
    ZbglPczzWIManager zbglPczzWIManager;
    
    /** 机车出入段台账业务类 */
    @Resource
    TrainAccessAccountManager trainAccessAccountManager;
    
    /**流程节点业务类**/
    @Resource    
    private ZbglRdpNodeManager zbglRdpNodeManager;
    
    /**ZbglRdp业务类,机车整备单**/
    @Resource    
    private ZbglRdpManager zbglRdpManager;
    
    private String configValue = ZbConstants.NODENAME_JY;
    
    /**
     * <li>说明：获取机车整备交验列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 机车整备单实体包装类
     * @return 机车整备交验列表
     * @throws BusinessException
     */
    public Page<ZbglJYRdpBean> findRdpListForJY(SearchEntity<ZbglRdp> searchEntity) throws BusinessException {
    	ZbglRdp entity = searchEntity.getEntity();
    	String sql = "";
    	//判断是否交验查询条件
    	if (entity.getRdpStatus().contains(ZbglRdp.STATUS_HANDLING)) {
//    		未交验
        	sql = SqlMapUtil.getSql("zb-rdp:findRdpListForJYOngoing")
            .replace("#STATUS_HANDLED#", ZbglRdpWi.STATUS_HANDLED)
            .replace("#STATUS_OVER#", ZbglTp.STATUS_OVER)
            .replace("#STATUS_CHECK#", ZbglTp.STATUS_CHECK)
            .replace("#siteID#", EntityUtil.findSysSiteId(""));
		}
    	if (entity.getRdpStatus().contains(ZbglRdp.STATUS_HANDLED)) {
//    		已校验
        	sql = SqlMapUtil.getSql("zb-rdp:findRdpListForJYComplete")
            .replace("#siteID#", EntityUtil.findSysSiteId(""));
    	}
        
        StringBuilder sb = new StringBuilder(sql);
        
        // 查询条件 - 车型
        if (!StringUtil.isNullOrBlank(entity.getTrainTypeShortName())) {
            sb.append(" AND A.TRAIN_TYPE_SHORTNAME LIKE '%").append(entity.getTrainTypeShortName()).append("%'");
        }
        // 查询条件 - 车号
        if (!StringUtil.isNullOrBlank(entity.getTrainNo())) {
            sb.append(" AND A.TRAIN_NO LIKE '%").append(entity.getTrainNo()).append("%'");
        }
        // 查询条件 - 配属段
        if (!StringUtil.isNullOrBlank(entity.getDName())) {
            sb.append(" AND A.D_NAME LIKE '%").append(entity.getDName()).append("%'");
        }
        //查询条件 - 入段时间【开始】
        if(null != entity.getRdpStartTime()){
            sb.append(" and b.IN_TIME >= to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getRdpStartTime())).append("','yyyy-mm-dd hh24:mi:ss') ");
        }
        //查询条件 - 入段时间【结束】
        if(null != entity.getRdpEndTime()){
            sb.append(" and b.IN_TIME <= to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getRdpEndTime())).append("','yyyy-mm-dd hh24:mi:ss') ");
        }
        // 查询条件 - 是否交验
        if (!StringUtil.isNullOrBlank(entity.getRdpStatus())) {
            sb.append(" AND A.RDP_STATUS in ('").append(entity.getRdpStatus().replace(",", "','")).append("')");
//            if (ZbglRdp.STATUS_HANDLING.equals(entity.getRdpStatus())) {
//                sb.append(" AND B.OUT_TIME IS NULL");
//            }
        }
        AbstractEntityPersister meta = (AbstractEntityPersister) getDaoUtils().getSessionFactory().getClassMetadata(ZbglJYRdpBean.class);
        String sortString = "";
        Order[] orders = searchEntity.getOrders();
        if (orders != null && orders.length > 0) {
            for (Order order : orders) {
                String[] orderStrings = StringUtil.tokenizer(order.toString(), " ");
                if (orderStrings == null || orderStrings.length != 2)
                    continue;
                if (orderStrings[0].equals("trainTypeShortName") || 
                    orderStrings[0].equals("trainNo") ||                      
                    orderStrings[0].equals("siteName") || 
                    orderStrings[0].equals("repairClass") || 
                    orderStrings[0].equals("rdpStartTime")) {
                    sortString = CommonUtil.buildOrderSql("A.", meta, orderStrings);
                } else if (orderStrings[0].equals("toGo") || orderStrings[0].equals("inTime")) {
                    sortString = CommonUtil.buildOrderSql("B.", meta, orderStrings);
                } else if (orderStrings[0].equals("dname")) {
                    String[] newOrderStrings = new String[2];
                    newOrderStrings[0] = "dName"; 
                    newOrderStrings[1] = orderStrings[1];
                    sortString = CommonUtil.buildOrderSql("A.", meta, newOrderStrings);
                }   
            }
            sb.append(sortString);
        } else {
            sb.append(" ORDER BY B.IN_TIME DESC");
        }
        sql = sb.toString();
        StringBuilder totalSql = new StringBuilder("SELECT COUNT(*) AS ROWCOUNT FROM (").append(sql).append(Constants.BRACKET_R);
        return this.acquirePageList(totalSql.toString(), sql, searchEntity.getStart(), searchEntity.getLimit(), false);
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
    public Page<ZbglJYRdpBean> acquirePageList(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled)
        throws BusinessException {
        final int beginIdx = start < 0 ? 0 : start;
        final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
        final String total_sql = totalSql;
        final String fSql = sql;
        final Boolean useCached = isQueryCacheEnabled;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<ZbglJYRdpBean>) template.execute(new HibernateCallback() {
            
            public Page<ZbglJYRdpBean> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = s.createSQLQuery(total_sql);
                    query.addScalar("rowcount", Hibernate.INTEGER);
                    query.setCacheable(useCached); // 缓存开关
                    int total = ((Integer) query.uniqueResult()).intValue();
                    query.setCacheable(false);
                    int begin = beginIdx > total ? total : beginIdx;
                    query = (SQLQuery) s.createSQLQuery(fSql).addEntity(ZbglJYRdpBean.class).setFirstResult(begin).setMaxResults(pageSize);
                    query.setCacheable(useCached); // 缓存开关
                    return new Page<ZbglJYRdpBean>(total, query.list());
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
     * <li>说明：获取机车整备单关联的任务列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 机车整备单实体包装类
     * @return 机车整备单关联的任务列表
     */
    public Page<ZbglJYRdpBean> findTaskList(SearchEntity<ZbglRdp> searchEntity) {
        ZbglRdp bean = searchEntity.getEntity();
        List<ZbglJYRdpBean> list = new LinkedList<ZbglJYRdpBean>();
        list.add(getSandingTask(bean.getTrainAccessAccountIDX()));
        list.add(getHoCaseTask(bean.getIdx()));
        list.add(getRdpTask(bean.getIdx()));
        list.add(getTpTask(bean.getIdx()));
        list.add(getTpExceptionTask(bean.getIdx()));
        list.add(getPczzTask(bean.getIdx()));
        list.add(getCleanTask(bean.getIdx()));
        return new Page<ZbglJYRdpBean>(list.size(), list);
    }
    
    /**
     * <li>说明：验证机车整备单关联的整备任务单、提票单、普查整治任务单是否全部完成
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 验证信息
     */
    public String[] validateUpdate(String rdpIDX) {
        int handledRdpWiCount = zbglRdpWiManager.getHandledRdpWiCountByRdp(rdpIDX);
        int allRdpWiCount = zbglRdpWiManager.getAllRdpWiCountByRdp(rdpIDX);
        List<String> errMsgList = new ArrayList<String>();
        
        if (handledRdpWiCount < allRdpWiCount) {
            String errMsg = "整备任务单未全部处理完成";
            errMsgList.add(errMsg);
        }
        int handledTpCount = zbglTpManager.getHandledTpCountByRdp(rdpIDX);
        int allTpCount = zbglTpManager.getAllTpCountByRdp(rdpIDX, "");
        if (handledTpCount < allTpCount) {
            String errMsg = "提票活未全部处理完成";
            errMsgList.add(errMsg);
        }
        int handledPczzWiCount = zbglPczzWIManager.getHandledPczzWiCountByRdp(rdpIDX);
        int allPczzWiCount = zbglPczzWIManager.getAllPczzWiCountByRdp(rdpIDX);
        if (handledPczzWiCount < allPczzWiCount) {
            String errMsg = "普查整治任务未全部处理完成";
            errMsgList.add(errMsg);
        }
        if (errMsgList.size() > 0) {
            String[] errArray = new String[errMsgList.size()];
            errMsgList.toArray(errArray);
            return errArray;
        }
        return null;
    }
    
    /**
     * <li>说明：确认机车整备交验
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车整备单实体对象
     * @param rdpIDX 机车整备单IDX
     * @throws Exception 
     */
    public void updateForJY(ZbglRdp entity, String rdpIDX) throws Exception {
        ZbglRdp rdp = getModelById(rdpIDX);
        if (rdp == null)
            throw new BusinessException("id为" + rdpIDX + "的整备单为空");
        if (ZbglRdp.STATUS_HANDLED.equals(rdp.getRdpStatus()))
            throw new BusinessException("id为" + rdpIDX + "的整备单已交验");
        rdp.setFromPersonId(entity.getFromPersonId());
        rdp.setFromPersonName(entity.getFromPersonName());
        rdp.setOutOrder(entity.getOutOrder());
        rdp.setToPersonId(entity.getToPersonId());
        rdp.setToPersonName(entity.getToPersonName());
        rdp.setToGo(entity.getToGo());
        rdp.setRemarks(entity.getRemarks());
        rdp.setRdpEndTime(entity.getRdpEndTime());
        rdp.setRdpStatus(ZbglRdp.STATUS_HANDLED);
        rdp.setUpdateTime(new Date());
        saveOrUpdate(rdp);
        trainAccessAccountManager.updateTrainStatus(rdp.getTrainAccessAccountIDX(), TrainAccessAccount.TRAINSTATUS_LIANGHAO);
        zbglRdpNodeManager.updateNodeForEnd(configValue, new Date(), rdp.getIdx(), ZbglRdpNode.STATUS_COMPLETE);
    }
    
    /**
     * <li>说明：获取机车上砂信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccountIDX 机车出入段台账IDX
     * @return ZbglJYRdpBean实体对象
     */
    private ZbglJYRdpBean getSandingTask(String trainAccessAccountIDX) {
        ZbglJYRdpBean bean = new ZbglJYRdpBean();
        StringBuilder taskName = new StringBuilder();
        taskName.append("机车上砂 (").append(zbglSandingManager.getIsFinish(trainAccessAccountIDX)).append(Constants.BRACKET_R);
        bean.setTaskName(taskName.toString());
        bean.setTaskType(ZbConstants.TASKTYPE_SANDING);
        return bean;
    }
    
    /**
     * <li>说明：获取机车交接信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return ZbglJYRdpBean实体对象
     */
    private ZbglJYRdpBean getHoCaseTask(String rdpIDX) {
        ZbglJYRdpBean bean = new ZbglJYRdpBean();
        StringBuilder taskName = new StringBuilder();
        taskName.append("机车交接 (").append(zbglHoCaseManager.getIsFinish(rdpIDX)).append(Constants.BRACKET_R);
        bean.setTaskName(taskName.toString());
        bean.setTaskType(ZbConstants.TASKTYPE_HANDOVER);
        return bean;
    }
    
    /**
     * <li>说明：获取机车整备任务单信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return ZbglJYRdpBean实体对象
     */
    private ZbglJYRdpBean getRdpTask(String rdpIDX) {
        ZbglJYRdpBean bean = new ZbglJYRdpBean();
        StringBuilder taskName = new StringBuilder();
        taskName.append("整备任务单 (")
                .append(zbglRdpWiManager.getHandledRdpWiCountByRdp(rdpIDX))
                .append("/")
                .append(zbglRdpWiManager.getAllRdpWiCountByRdp(rdpIDX))
                .append(Constants.BRACKET_R);
        bean.setTaskName(taskName.toString());
        bean.setTaskType(ZbConstants.TASKTYPE_RDP);
        return bean;
    }
    
    /**
     * <li>说明：获取提票信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return ZbglJYRdpBean实体对象
     */
    private ZbglJYRdpBean getTpTask(String rdpIDX) {
        ZbglJYRdpBean bean = new ZbglJYRdpBean();
        StringBuilder taskName = new StringBuilder();
        taskName.append("提票活 (")
                .append(zbglTpManager.getHandledTpCountByRdp(rdpIDX))
                .append("/")
                .append(zbglTpManager.getAllTpCountByRdp(rdpIDX, ""))
                .append(Constants.BRACKET_R);
        bean.setTaskName(taskName.toString());
        bean.setTaskType(ZbConstants.TASKTYPE_TP);
        return bean;
    }
    
    /**
     * <li>说明：获取例外放行信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return ZbglJYRdpBean实体对象
     */
    private ZbglJYRdpBean getTpExceptionTask(String rdpIDX) {
        ZbglJYRdpBean bean = new ZbglJYRdpBean();
        StringBuilder taskName = new StringBuilder();
        taskName.append("例外放行(").append(zbglTpExceptionManager.getCountByRdp(rdpIDX)).append(Constants.BRACKET_R);
        bean.setTaskName(taskName.toString());
        bean.setTaskType(ZbConstants.TASKTYPE_LWFX);
        return bean;
    }
    
    /**
     * <li>说明：获取普查整治任务单信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return ZbglJYRdpBean实体对象
     */
    private ZbglJYRdpBean getPczzTask(String rdpIDX) {
        ZbglJYRdpBean bean = new ZbglJYRdpBean();
        StringBuilder taskName = new StringBuilder();
        taskName.append("普查整治 (")
                .append(zbglPczzWIManager.getHandledPczzWiCountByRdp(rdpIDX))
                .append("/")
                .append(zbglPczzWIManager.getAllPczzWiCountByRdp(rdpIDX)).append(Constants.BRACKET_R);
        bean.setTaskName(taskName.toString());
        bean.setTaskType(ZbConstants.TASKTYPE_PCZZ);
        return bean;
    }
    
    /**
     * <li>说明：获取机车保洁 信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return ZbglJYRdpBean实体对象
     */
    private ZbglJYRdpBean getCleanTask(String rdpIDX) {
        ZbglJYRdpBean bean = new ZbglJYRdpBean();
        StringBuilder taskName = new StringBuilder();
        taskName.append("机车保洁 (").append(zbglCleaningManager.getIsFinish(rdpIDX)).append(Constants.BRACKET_R);
        bean.setTaskName(taskName.toString());
        bean.setTaskType(ZbConstants.TASKTYPE_CLEAN);
        return bean;
    }
    
    /**
     * <li>标题: 机车整备管理信息系统
     * <li>说明: 机车整备交验实体bean
     * <li>创建人：程锐
     * <li>创建日期：2015-3-9
     * <li>修改人:
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部整备系统项目组
     * @version 1.0
     */
    @Entity
    private static class ZbglJYRdpBean {
        
        /* idx主键 */
        @Id
        private String idx;
        
        /* 车型简称 */
        @Column(name = "Train_Type_ShortName")
        private String trainTypeShortName;
        
        /* 车号 */
        @Column(name = "Train_No")
        private String trainNo;
        
        /* 配属段名称 */
        @Column(name = "D_Name")
        private String dName;
        
        /* 整备站场名称 */
        private String siteName;
        
        /* 整备开始时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Rdp_Start_Time")
        private java.util.Date rdpStartTime;
        
        /* 入段时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "In_Time")
        private java.util.Date inTime;
        
        /* 入段去向 */
        @Column(name = "To_Go")
        private String toGo;
        
        /* 出段后去向 */
        private String afterToGo;
        
        /* 检修类型 */
        @Column(name = "REPAIR_CLASS")
        private String repairClass;
        
        /* 机车出入段台账主键 */
        @Column(name = "Train_Access_Account_IDX")
        private String trainAccessAccountIDX;
        
        /* 整备单状态 */
        @Column(name = "Rdp_Status")
        private String rdpStatus;
        
        /* 交验交车人 */
        @Column(name = "From_PersonId")
        private Long fromPersonId;
        
        /* 交验交车人名称 */
        @Column(name = "From_PersonName")
        private String fromPersonName;
        
        /* 交验接车人 */
        @Column(name = "To_PersonId")
        private Long toPersonId;
        
        /* 交验接车人名称 */
        @Column(name = "To_PersonName")
        private String toPersonName;
        
        /* 出段车次 */
        @Column(name = "Out_Order")
        private String outOrder;
        
        /* 交验情况描述 */
        private String remarks;
        
        /* 整备结束时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Rdp_End_Time")
        private java.util.Date rdpEndTime;
        
        /* 已完成检查任务数量 */
        private String handledRdpCount;
        
        /* 全部检查任务数量 */
        private String allRdpCount;
        
        /* 已完成提票任务数量 */
        private String handledTpCount;
        
        /* 全部提票任务数量 */
        private String allTpCount;
        
        /* 已完成普查整治任务数量 */
        private String handledPczzCount;
        
        /* 全部普查整治任务数量 */
        private String allPczzCount;
        
        /* 任务名称 */
        @Transient
        private String taskName;
        
        /* 任务类型 */
        @Transient
        private String taskType;
        
        /**
         * <li>说明：默认构造方法
         * <li>创建人：程锐
         * <li>创建日期：2015-3-12
         * <li>修改人：
         * <li>修改日期：
         */
        public ZbglJYRdpBean() {
        }
        
        public String getAfterToGo() {
            return afterToGo;
        }
        
        public void setAfterToGo(String afterToGo) {
            this.afterToGo = afterToGo;
        }
        
        public String getDName() {
            return dName;
        }
        
        public void setDName(String name) {
            dName = name;
        }
        
        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public java.util.Date getInTime() {
            return inTime;
        }
        
        public void setInTime(java.util.Date inTime) {
            this.inTime = inTime;
        }
        
        
        public java.util.Date getRdpStartTime() {
            return rdpStartTime;
        }
        
        public void setRdpStartTime(java.util.Date rdpStartTime) {
            this.rdpStartTime = rdpStartTime;
        }
        
        public String getRepairClass() {
            return repairClass;
        }
        
        public void setRepairClass(String repairClass) {
            this.repairClass = repairClass;
        }
        
        /**
         * @return String 获取机车出入段台账主键
         */
        public String getTrainAccessAccountIDX() {
            return trainAccessAccountIDX;
        }
        
        /**
         * @param trainAccessAccountIDX 设置机车出入段台账主键
         */
        public void setTrainAccessAccountIDX(String trainAccessAccountIDX) {
            this.trainAccessAccountIDX = trainAccessAccountIDX;
        }
        
        public String getSiteName() {
            return siteName;
        }
        
        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }
        
        public String getToGo() {
            return toGo;
        }
        
        public void setToGo(String toGo) {
            this.toGo = toGo;
        }
        
        
        public String getTrainNo() {
            return trainNo;
        }
        
        public void setTrainNo(String trainNo) {
            this.trainNo = trainNo;
        }
        
        public String getTrainTypeShortName() {
            return trainTypeShortName;
        }
        
        public void setTrainTypeShortName(String trainTypeShortName) {
            this.trainTypeShortName = trainTypeShortName;
        }
        
        public String getTaskName() {
            return taskName;
        }
        
        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }
        
        public String getTaskType() {
            return taskType;
        }
        
        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }
        
        public String getRdpStatus() {
            return rdpStatus;
        }
        
        public void setRdpStatus(String rdpStatus) {
            this.rdpStatus = rdpStatus;
        }
        
        public Long getFromPersonId() {
            return fromPersonId;
        }
        
        public void setFromPersonId(Long fromPersonId) {
            this.fromPersonId = fromPersonId;
        }
        
        public String getFromPersonName() {
            return fromPersonName;
        }
        
        public void setFromPersonName(String fromPersonName) {
            this.fromPersonName = fromPersonName;
        }
        
        public String getOutOrder() {
            return outOrder;
        }
        
        public void setOutOrder(String outOrder) {
            this.outOrder = outOrder;
        }
        
        public String getRemarks() {
            return remarks;
        }
        
        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
        
        public Long getToPersonId() {
            return toPersonId;
        }
        
        public void setToPersonId(Long toPersonId) {
            this.toPersonId = toPersonId;
        }
        
        public String getToPersonName() {
            return toPersonName;
        }
        
        public void setToPersonName(String toPersonName) {
            this.toPersonName = toPersonName;
        }
        
        public java.util.Date getRdpEndTime() {
            return rdpEndTime;
        }
        
        public void setRdpEndTime(java.util.Date rdpEndTime) {
            this.rdpEndTime = rdpEndTime;
        }
        
        public String getAllPczzCount() {
            return allPczzCount;
        }
        
        public void setAllPczzCount(String allPczzCount) {
            this.allPczzCount = allPczzCount;
        }
        
        public String getAllRdpCount() {
            return allRdpCount;
        }
        
        public void setAllRdpCount(String allRdpCount) {
            this.allRdpCount = allRdpCount;
        }
        
        public String getAllTpCount() {
            return allTpCount;
        }
        
        public void setAllTpCount(String allTpCount) {
            this.allTpCount = allTpCount;
        }
        
        public String getHandledPczzCount() {
            return handledPczzCount;
        }
        
        public void setHandledPczzCount(String handledPczzCount) {
            this.handledPczzCount = handledPczzCount;
        }
        
        public String getHandledRdpCount() {
            return handledRdpCount;
        }
        
        public void setHandledRdpCount(String handledRdpCount) {
            this.handledRdpCount = handledRdpCount;
        }
        
        public String getHandledTpCount() {
            return handledTpCount;
        }
        
        public void setHandledTpCount(String handledTpCount) {
            this.handledTpCount = handledTpCount;
        }
        
    }

    /**
     * <li>说明：通过出入段台账idx判断该车是否已经交验
     * <li>创建人：林欢
     * <li>创建日期：2016-8-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public Boolean trainIsDoZbglRdpJY(String trainAccessAccountIDX) {

        //通过车型车号查询最新一条整备单信息
        ZbglRdp zbglRdp = zbglRdpManager.getRdpByAccount(trainAccessAccountIDX);
        //判断rdpStatus
        if (StringUtils.isNotBlank(zbglRdp.getRdpStatus()) && ZbglRdp.STATUS_HANDLED.equals(zbglRdp.getRdpStatus())) {
            return true;
        }
        return false;
    }

    /**
     * <li>方法说明：获取整备管理合格交验待处理任务数量 
     * <li>方法名称：getCount
     * <li>创建人：林欢
     * <li>创建时间：2016-02-20 下午03:20:04
     * <li>修改人：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @return Integer[]
     */
    public Integer[] findZbglHgjyCount(long operatorid) {
        
        //获取所有的整备单信息
        Map<String,String> paramMap = new HashMap<String, String>();
        paramMap.put("rdpStatus", ZbglRdp.STATUS_HANDLING);//查询整备中的整备单信息
        paramMap.put("siteID", JXConfig.getInstance().getSynSiteID());//查询整备中的整备单信息
        
        StringBuilder hql = new StringBuilder();
        hql.append(" select z.* from ZB_ZBGL_RDP z,TWT_Train_Access_Account t where 1 = 1 and t.idx = z.Train_Access_Account_IDX and t.record_Status = 0 and z.record_Status = 0");
        hql.append(" and z.Rdp_Status = '").append(ZbglRdp.STATUS_HANDLING).append("'");
        hql.append(" and z.siteID = '").append(JXConfig.getInstance().getSynSiteID()).append("'");
        List<ZbglRdp> zbglRdpList = zbglRdpManager.getDaoUtils().executeSqlQueryEntity(hql.toString(), ZbglRdp.class);
        
//        List<ZbglRdp> zbglRdpList = zbglRdpManager.getRdpList(paramMap);
        
        Integer count = 0;
        
        for (ZbglRdp rdp : zbglRdpList) {
            
            Boolean flag = true;
            
            String rdpIDX = rdp.getIdx();//整备单idx
            int handledRdpWiCount = zbglRdpWiManager.getHandledRdpWiCountByRdp(rdpIDX);//已经处理的范围活
            int allRdpWiCount = zbglRdpWiManager.getAllRdpWiCountByRdp(rdpIDX);//全部范围活
            if (handledRdpWiCount != allRdpWiCount) {
                flag = false;
                continue;
            }
            int handledTpCount = zbglTpManager.getHandledTpCountByRdp(rdpIDX);//已经处理的提票
            int allTpCount = zbglTpManager.getAllTpCountByRdp(rdpIDX, "");//全部提票
            if (handledTpCount != allTpCount) {
                flag = false;
                continue;
            }
            
            if (flag) {
                count++;
            }
        }
        
        return new Integer[]{count};
    }
    
}
