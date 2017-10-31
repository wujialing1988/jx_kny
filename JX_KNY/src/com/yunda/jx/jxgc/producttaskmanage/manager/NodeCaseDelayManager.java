package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.frame.baseapp.message.manager.IMessageManager;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.NodeCaseDelay;
import com.yunda.jx.jxgc.producttaskmanage.entity.NodeCaseDelayQuery;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;
import com.yunda.jx.jxgc.workplanmanage.manager.VJobProcessNodeManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工序延误记录业务类
 * <li>创建人：张凡
 * <li>创建日期：2013-5-6 下午03:32:05
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "nodeCaseDelayManager")
public class NodeCaseDelayManager extends JXBaseManager<NodeCaseDelay, NodeCaseDelay> {
    
    /** 消息管理业务接口 */
    @Resource
    private IMessageManager messageManager;
    @Resource
    private  EosDictEntrySelectManager eosDictEntrySelectManager;
    /** 机车检修管理信息系统 */
    @Resource
    private VJobProcessNodeManager vJobProcessNodeManager ;
    
    /** 机车检修计划流程 */
    @Resource
    private JobProcessNodeManager jobProcessNodeManager ;
    /**
     * <li>方法说明：更新前验证，如果该作业节点已经存在有延误记录，则进行更新
     * <li>创建人：何涛
     * <li>创建时间：2015-09-01
     * <li>修改人：
     * <li>修改内容：
     * <li>@param t 新增（或更新）的延误记录实体
     * <li>@return 验证消息
     */
    @Override
    public String[] validateUpdate(NodeCaseDelay t) {
        NodeCaseDelay entity = this.getEntityByNodeCaseIdx(t.getNodeCaseIdx());
        if (null != entity) {
            t.setIdx(entity.getIdx());
            entity = null;
        }
        return super.validateUpdate(t);
    }
    
    
    protected JobProcessNodeManager getJobProcessNodeManager() {
        return (JobProcessNodeManager) Application.getSpringApplicationContext().getBean("jobProcessNodeManager");
    } 
    
    /**
     * <li>方法名称：getEntityByNodeCaseIdx
     * <li>方法说明：根据工艺节点实例IDX找到实体 
     * <li>@param nodeCaseIdx 流程节点IDX
     * <li>@return NodeCaseDelay
     * <li>创建人：张凡
     * <li>创建时间：2013-5-6 下午03:32:05
     * <li>修改人：
     * <li>修改内容：
     */
    public NodeCaseDelay getEntityByNodeCaseIdx(String nodeCaseIdx){
        String hql = "from NodeCaseDelay where nodeCaseIdx = '" + nodeCaseIdx + "' and recordStatus = 0";        
        NodeCaseDelay entity = (NodeCaseDelay)daoUtils.findSingle(hql);
        return entity;
    }
    
    /**
     * <li>说明：根据节点ID找到实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点ID
     * @param workPlanIDX 计划ID
     * @return 
     */
    public NodeCaseDelay getEntityByNodedx(String nodeIDX,String workPlanIDX){
        String hql = "from NodeCaseDelay where nodeIDX = '" + nodeIDX + "' and rdpIDX = '"+workPlanIDX+"' and recordStatus = 0";        
        NodeCaseDelay entity = (NodeCaseDelay)daoUtils.findSingle(hql);
        return entity;
    }
    
    /**
     * <li>说明：获取工期延误集合
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点ID
     * @param workPlanIDX 计划ID
     * @return
     */
    public List<NodeCaseDelay> getNodeCaseDelaysByNodedx(String nodeIDX,String workPlanIDX){
        String hql = "from NodeCaseDelay where nodeIDX = '" + nodeIDX + "' and rdpIDX = '"+workPlanIDX+"' and recordStatus = 0";        
        return daoUtils.find(hql);
    }
    
    /**
     * <li>方法说明：根据工艺实例主键发送延误通知（用于填写延误原因之后） 
     * <li>方法名称：sendDelayNotify
     * <li>@param tecProcessNodeCaseIdx
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-7-11 下午01:47:05
     * <li>修改人：程锐 2015-5-13
     * <li>修改内容：修改工序延误算法：如果某一工序节点实际检修工期超过了该工序的计划检修工期，系统则认为该工序发生工序延误，修改工序延误查询的实体类，由TecProcessNodeCase、TrainEnforcePlanRdp更换为JobProcessNode、TrainWorkPlan
     */
    @SuppressWarnings("unchecked")
    public void sendDelayNotify(String tecProcessNodeCaseIdx){
        String sql = SqlMapUtil.getSql("jxgc-gdgl:findStatisticsDelay");
        List<Object[]> list = daoUtils.executeSqlQuery(sql + " and t.idx = '" + tecProcessNodeCaseIdx + "'");
        StringBuffer msg = null;
        for(Object[] item : list){
            msg = new StringBuffer("有新的工序延误产生<br>");
            msg.append("车型车号：");
            msg.append(item[2]);
            msg.append("|");
            msg.append(item[3]);
            msg.append("<br>修程修次：");
            msg.append(item[4]);
            msg.append("|");
            msg.append(item[5]);
            msg.append("<br>工序名称："+ item[1]);
            msg.append("<br>计划开始时间：" + item[6]);
            msg.append("<br>计划结束时间：" + item[7]);
            msg.append("<br>实际开始时间：" + (item[8] == null ? "尚未开始" : item[9]));
            msg.append("<br>延误原因：" + item[9]);
            
            messageManager.sendByFunCode("SCDD_DelayWarning", msg.toString());
            daoUtils.executUpdateOrDelete("update JobProcessNode set msgRemind = 1 where idx = '" + item[0] + "'");
        }
    } 
    /**
	 * <li>说明：工序延误
	 * 			1 工序延误处理 ：查询延误原因为空（未处理）的工序延误记录
	 *          2 工序延误查询 ：查询全部的工序延误记录
	 *          3 工序延误历史 ：查询延误原因不为空（已处理）的工序延误记录
	 * <li>创建人：张凡
	 * <li>创建时间：2013-5-6 下午01:31:53
	 * <li>修改人：程锐
	 * <li>修改内容：修改工序延误算法：如果某一工序节点实际检修工期超过了该工序的计划检修工期后仍然有任务未进行完工确认，系统则认为该工序发生工序延误
	 * <li>修改人：程锐 2014-1-27
	 * <li>修改内容：修改工序延误算法：如果某一工序节点实际检修工期超过了该工序的计划检修工期，系统则认为该工序发生工序延误
     * <li>修改人：程锐 2015-5-13
     * <li>修改内容：检修V3.2版本：修改工序延误查询的实体类，由TecProcessNodeCase更换为JobProcessNode
	 * @param orgid 组织机构ID
	 * @param start 开始行
     * @param limit 每页记录数
     * @param orders 排序对象
	 * @param searchJson 查询json字符串
	 * @param isSearch 是否查询页面
	 * @param isHis 是否历史页面
	 * @return 工序延误分页列表
	 */
    public Page<JobProcessNode> findWorkSeqPutOff(String orgid,
    											  int start,
    											  int limit, 
    											  Order[] orders, 
    											  String searchJson, 
    											  String isSearch, 
    											  String isHis) throws Exception {
        String select = SqlMapUtil.getSql("jxgc-gdgl:findWorkSeqPutOff_select");
		String from = SqlMapUtil.getSql("jxgc-gdgl:findWorkSeqPutOff_from");
		// 工序延误处理
		if (!StringUtil.isNullOrBlank(orgid)) {
			from += " AND CX.WORK_STATION_BELONG_TEAM = '" + orgid + "'";
		}
		// 工序延误处理
		if (StringUtil.isNullOrBlank(isSearch) && StringUtil.isNullOrBlank(isHis)) {
			from += " AND D.DELAY_REASON IS NULL";
		}
		// 工序延误历史记录
		else if (!StringUtil.isNullOrBlank(isHis)) {
			from += " AND D.DELAY_REASON IS NOT NULL";
		}		
		
		String totalSql = "SELECT COUNT(1) FROM (SELECT DISTINCT T.IDX " + from + ")";
		String sql = select + from;
		if (orders != null) {
			String order = orders[0].toString();
			String[] orderStrings = StringUtil.tokenizer(order, " ");
			String entityField = orderStrings[0].trim();
			if ("tempDelayTime".equals(entityField)) {
				sql += " ORDER BY TO_CHAR(T.REAL_WORKMINUTES - T.RATED_WORKMINUTES) " + orderStrings[1];
				orders = null;
			} else if ("planBeginTimeStr".equals(entityField)) {
				sql += " ORDER BY T.PLAN_BEGIN_TIME " + orderStrings[1];
				orders = null;
			} else if ("planEndTimeStr".equals(entityField)) {
				sql += " ORDER BY T.PLAN_END_TIME " + orderStrings[1];
				orders = null;
			} else if ("realBeginTimeStr".equals(entityField)) {
				sql += " ORDER BY T.REAL_BEGIN_TIME " + orderStrings[1];
				orders = null;
			} else if ("realEndTimeStr".equals(entityField)) {
				sql += " ORDER BY T.REAL_END_TIME " + orderStrings[1];
				orders = null;
			}
		}
		Page<JobProcessNode> page = getJobProcessNodeManager().findPageList(totalSql, sql, start, limit, searchJson, orders);
		return page;
    }

    /**
     * <li>说明：获取某一节点，及其下级所有子节点的工序延误信息
     * <li>创建人：何涛
     * <li>创建日期：2015-9-1
     * <li>修改人：何涛
     * <li>修改日期：2016-05-04
     * <li>修改内容：优化查询，使用sql联合查询延误原因类型名称，节点名称
     * @param nodeCaseIdx 机车检修作业流程节点主键
     * @return 某一节点，及其下级所有子节点的工序延误信息集合
     */
    @SuppressWarnings("unchecked")
    public List<NodeCaseDelayBean> findChildren(String nodeCaseIdx) {
        // 递归查询作业流程节点及其下属所有子节点的主键
        String ids = "SELECT IDX FROM JXGC_JOB_PROCESS_NODE T WHERE T.RECORD_STATUS = 0 START WITH T.IDX ='" + nodeCaseIdx + "' CONNECT BY PRIOR T.IDX = T.PARENT_IDX";
        // 联合查询
        StringBuilder sb = new StringBuilder("SELECT T.*, N.NODE_NAME, E.DICTNAME AS \"DELAY_TYPE_NAME\" FROM JXGC_NODE_CASE_DELAY T, EOS_DICT_ENTRY E, JXGC_Job_Process_Node N WHERE E.DICTTYPEID ='JXGC_WORK_SEQ_DELAY' AND T.DELAY_TYPE = E.DICTID AND T.NODE_CASE_IDX = N.IDX AND N.RECORD_STATUS = 0 AND T.RECORD_STATUS = 0");
        sb.append(" AND T.NODE_CASE_IDX IN (").append(ids).append(")");
        
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        final String sql = sb.toString();
        return (List<NodeCaseDelayBean>)template.execute(new HibernateCallback(){
            public List<NodeCaseDelayBean> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = (SQLQuery)s.createSQLQuery(sql).addEntity(NodeCaseDelayBean.class);
                    query.setCacheable(false); //缓存开关
                    return query.list();
                } catch (HibernateException e) {
                    throw e;
                } finally{
                    if(query != null)   query.setCacheable(false);
                }
            }
        });
    }
    
   
    /**
     * <li>说明：获取节点及其下级所有子节点的工序延误信息（延误类型改为多选）
     * <li>创建人：张迪
     * <li>创建日期：2016-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCaseIdx
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<NodeCaseDelayQuery> findChildrenNew(String nodeCaseIdx,String workPlanIDX) {
        // 递归查询作业流程节点及其下属所有子节点的主键
        String ids = "SELECT IDX FROM JXGC_JOB_PROCESS_NODE T WHERE T.RECORD_STATUS = 0 START WITH T.IDX ='" + nodeCaseIdx + "' CONNECT BY PRIOR T.IDX = T.PARENT_IDX";
        // 联合查询
        StringBuilder sb = new StringBuilder("SELECT T.*, N.NODE_NAME, O.EMPNAME, OM.ORGNAME FROM JXGC_NODE_CASE_DELAY T, JXGC_Job_Process_Node N, jwzh.OM_EMPLOYEE O, jwzh.OM_ORGANIZATION OM WHERE T.NODE_CASE_IDX = N.IDX AND N.RECORD_STATUS = 0 AND T.RECORD_STATUS = 0 AND T.CREATOR = O.OPERATORID AND O.ORGID = OM.ORGID");
        sb.append(" AND T.NODE_CASE_IDX IN (").append(ids).append(")");
        final String sql = sb.toString(); 
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        List<NodeCaseDelayQuery> nodeCaseDelaylist = (List<NodeCaseDelayQuery>)template.execute(new HibernateCallback(){
            public List<NodeCaseDelayQuery> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = (SQLQuery)s.createSQLQuery(sql).addEntity(NodeCaseDelayQuery.class);
                    query.setCacheable(false); //缓存开关
                    return query.list();
                } catch (HibernateException e) {
                    throw e;
                } finally{
                    if(query != null)   query.setCacheable(false);
                }
            }
        });
        
        // 父亲节点
        JobProcessNode processNode = jobProcessNodeManager.getModelById(nodeCaseIdx);
        // 未填写延期提醒
        List<JobProcessNode> jobProcessNodes = vJobProcessNodeManager.getProcessNodeList(nodeCaseIdx, workPlanIDX);
        if(processNode != null){
            jobProcessNodes.add(processNode);
        }
        for (JobProcessNode node : jobProcessNodes) {
            if(!isExistJobProcessNode(node.getIdx(),nodeCaseDelaylist)){
                NodeCaseDelayQuery queryTemp = new NodeCaseDelayQuery();
                queryTemp.setNodeCaseIdx(node.getIdx());
                queryTemp.setNodeName(node.getNodeName());
                nodeCaseDelaylist.add(queryTemp);
            }
        }
        
     //  查询延误类型字典名称
        for(NodeCaseDelayQuery nodeCaseDelay: nodeCaseDelaylist){
            if(StringUtil.isNullOrBlank(nodeCaseDelay.getDelayType())){
                continue ;
            }
            String typeId[] = nodeCaseDelay.getDelayType().split(",");
            String delayName = "";
            for(int i=0; i<typeId.length; i++){      
                EosDictEntry dictEntry=  eosDictEntrySelectManager.getEosDictEntry("JXGC_WORK_SEQ_DELAY",typeId[i]);
                if(null == dictEntry)continue;
                delayName += dictEntry.getDictname()+",";
            }
            nodeCaseDelay.setDelayTypeName(delayName.substring(0, delayName.length()-1));
        }
        return nodeCaseDelaylist;
    }
  
    /**
     * <li>说明：判断是否已经填写延误
     * <li>创建人：张迪
     * <li>创建日期：2017-3-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx
     * @param nodeCaseDelaylist
     * @return
     */
    private boolean isExistJobProcessNode(String nodeIdx, List<NodeCaseDelayQuery> nodeCaseDelaylist) {
        boolean flag = false ;
        for (NodeCaseDelayQuery query : nodeCaseDelaylist) {
            if(nodeIdx.equals(query.getNodeCaseIdx())){
                flag = true ;
                break;
            }
        }
        return flag;
    }


    /**
     * <li>说明：查询延误类型字典名称
     * <li>创建人：张迪
     * <li>创建日期：2016-7-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 返回字典实体列表
     */
    public static List<EosDictEntry> getDalayType() {
        EosDictEntrySelectManager eosDictEntrySelectManager = (EosDictEntrySelectManager) Application.getSpringApplicationContext().getBean("eosDictEntrySelectManager");
        return eosDictEntrySelectManager.findByDicTypeID("JXGC_WORK_SEQ_DELAY");
    }
    
    /**
     * <li>标题: 机车检修管理信息系统
     * <li>说明: 节点延误原因查询封装实体
     * <li>创建人：何涛
     * <li>创建日期：2016-5-4
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部检修系统项目组
     * @version 1.0
     */
    @Entity
    public static final class NodeCaseDelayBean {
        @Id
        private String idx;
        
        @Column(name = "node_case_Idx")
        private String nodeCaseIdx;
        
        @Column(name = "node_idx")
        private String nodeIDX;
        
        @Column(name = "Tec_Process_Case_IDX")
        private String tecProcessCaseIDX;
        
        @Column(name = "rdp_idx")
        private String rdpIDX;
        
        @Column(name = "delay_type")
        private String delayType;
        
        @Column(name = "delay_type_name")
        private String delayTypeName;
        
        @Column(name = "delay_time")
        private Integer delayTime;
        
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "plan_begin_time")
        private Date planBeginTime;
        
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "plan_end_time")
        private Date planEndTime;
        
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "real_begin_time")
        private Date realBeginTime;
        
        @Column(name = "delay_reason")
        private String delayReason;
        
        @Column(name = "node_name")
        private String nodeName;

        
        public String getDelayReason() {
            return delayReason;
        }

        
        public void setDelayReason(String delayReason) {
            this.delayReason = delayReason;
        }

        
        public Integer getDelayTime() {
            return delayTime;
        }

        
        public void setDelayTime(Integer delayTime) {
            this.delayTime = delayTime;
        }

        
        public String getDelayType() {
            return delayType;
        }

        
        public void setDelayType(String delayType) {
            this.delayType = delayType;
        }

        
        public String getDelayTypeName() {
            return delayTypeName;
        }

        
        public void setDelayTypeName(String delayTypeName) {
            this.delayTypeName = delayTypeName;
        }

        
        public String getIdx() {
            return idx;
        }

        
        public void setIdx(String idx) {
            this.idx = idx;
        }

        
        public String getNodeCaseIdx() {
            return nodeCaseIdx;
        }

        
        public void setNodeCaseIdx(String nodeCaseIdx) {
            this.nodeCaseIdx = nodeCaseIdx;
        }

        
        public String getNodeIDX() {
            return nodeIDX;
        }

        
        public void setNodeIDX(String nodeIDX) {
            this.nodeIDX = nodeIDX;
        }

        
        public Date getPlanBeginTime() {
            return planBeginTime;
        }

        
        public void setPlanBeginTime(Date planBeginTime) {
            this.planBeginTime = planBeginTime;
        }

        
        public Date getPlanEndTime() {
            return planEndTime;
        }

        
        public void setPlanEndTime(Date planEndTime) {
            this.planEndTime = planEndTime;
        }

        
        public String getRdpIDX() {
            return rdpIDX;
        }

        
        public void setRdpIDX(String rdpIDX) {
            this.rdpIDX = rdpIDX;
        }

        
        public Date getRealBeginTime() {
            return realBeginTime;
        }

        
        public void setRealBeginTime(Date realBeginTime) {
            this.realBeginTime = realBeginTime;
        }

        
        public String getTecProcessCaseIDX() {
            return tecProcessCaseIDX;
        }

        
        public void setTecProcessCaseIDX(String tecProcessCaseIDX) {
            this.tecProcessCaseIDX = tecProcessCaseIDX;
        }
        
        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }
        
    }
    
}
