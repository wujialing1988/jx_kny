package com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine;
import com.yunda.jx.jxgc.producttaskmanage.entity.ProcessTask;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardBean;
import com.yunda.jx.jxgc.producttaskmanage.manager.ProcessTaskManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.webservice.entity.TrainWorkPlanBean;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivityDTO;
import com.yunda.jx.jxgc.workplanmanage.manager.WorkPlanRepairActivityManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.ProcessTaskListBean;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 质量检查查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-11-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "qCResultQueryManager")
public class QCResultQueryManager extends JXBaseManager<ProcessTaskListBean, ProcessTaskListBean> {
    
    @Resource
    private ProcessTaskManager processTaskManager;
    @Resource
    private WorkPlanRepairActivityManager workPlanRepairActivityManager;
    @Resource
    private WorkCardManager workCardManager;
    
    private OmEmployeeManager getOmEmployeeManager() {
        return (OmEmployeeManager) Application.getSpringApplicationContext().getBean("omEmployeeManager");
    }
    
    /**
     * <li>说明：查询当前人员的质量检查项列表-工位终端调用
     * <li>创建人：程锐
     * <li>创建日期：2014-11-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param uname 用户名
     * @param start 开始行
     * @param limit 本页记录数
     * @param mode 质检类型
     * @param queryString 查询字符串
     * @return 分页对象
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public Page<ProcessTaskListBean> getQCPageList(String uname, int start, int limit, String mode, String queryString) throws Exception{
		Long empid = getOmEmployeeManager().findByName(uname).getEmpid();
		Page<ProcessTaskListBean> page = getQCPageList(empid, start, limit, mode, queryString);
		List<ProcessTaskListBean> listBean = new ArrayList<ProcessTaskListBean>();
		ProcessTaskListBean bean = null;
		for (ProcessTaskListBean srcBean : page.getList()) {
			bean = new ProcessTaskListBean();
			BeanUtils.copyProperties(bean, srcBean);
			bean.setTaskDepict("作业工单名称：" + srcBean.getWorkCardName() + ";");
			if ((JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "").equals(mode)) {
				bean.setToken(ProcessTask.TYPE_WORKCARD);
			} else if ((JCQCItemDefine.CONST_INT_CHECK_WAY_CJ + "").equals(mode)) {
				bean.setToken(ProcessTask.TYPE_TP_ASY_QUA);
			}
			listBean.add(bean);
		}
		return new Page<ProcessTaskListBean>(page.getTotal(),listBean);
	}
    
    /**
     * <li>说明：查询当前人员的质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2014-11-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 用户id
     * @param start 开始行
     * @param limit 本页记录数
     * @param mode 质检类型，1：抽检、2：必检
     * @param queryString 查询字符串
     * @param vehicleType 客货类型
     * @return 当前人员的质量检查项列表
     * @throws Exception
     */
	public Page getQCPageList(Long empid, int start, int limit, String mode, String queryString) throws Exception {
        
        String querySql = getQuerySql(empid, mode, queryString, "getQCList");
        String totalSql = "select count(1) from (" + querySql + ")";
        Page<ProcessTaskListBean> page = findPageList(totalSql, querySql, start, limit, null, null);
        return page;
        
    }
    
	/**
     * <li>说明：全部质检使用
     * <li>创建人：程锐
     * <li>创建日期：2014-11-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 用户id
     * @param mode 质检类型
     * @param queryString 查询字符串
     * @return 分页对象
     * @throws Exception
     */
	public List<ProcessTaskListBean> getQCList(Long empid, String mode, String queryString) throws Exception{
		String querySql = getQuerySql(empid, mode, queryString, "getQCList");
		List list = daoUtils.executeSqlQuery(querySql);
		List<ProcessTaskListBean> listBean = new ArrayList<ProcessTaskListBean>();
		ProcessTaskListBean bean = null;
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[]) list.get(i);
			bean = new ProcessTaskListBean();
			bean.setCheckItemCode(objs[0] != null ? objs[0].toString(): "");
			bean.setSourceIdx(objs[9] != null ? objs[9].toString(): "");
			listBean.add(bean);
			
		}
		return listBean;
	}
    
    /**
     * <li>说明：获取当前操作者待处理的质检项数量
     * <li>创建人：程锐
     * <li>创建日期：2014-11-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 用户id
     * @param modle 抽检必检模式
     * @param query 查询参数
     * @return 当前操作者待处理的质检项数量
     * @throws Exception
     */
	public int getQCCount(Long empid,String modle,String query) throws Exception {
		List<ProcessTaskListBean> list = getQCList(empid, modle, query);
		return list.size();
	}
    
    /**
     * <li>说明：根据查询条件获取查询sql
     * <li>创建人：程锐
     * <li>创建日期：2014-11-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 用户id
     * @param mode 质检类型，1：抽检、2：必检
     * @param queryString 查询字符串
     * @param vehicleType 客货类型
     * @param sqlName 查询sql名称
     * @return 查询sql
     * @throws Exception
     */
	public String getQuerySql(Long empid, String mode, String queryString, String sqlName) throws Exception {
		String checkString = "";
		String workCardStatus = " STATUS in ('" + WorkCard.STATUS_FINISHED + "','" + WorkCard.STATUS_HANDLED + "')";
		if((JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "").equals(mode)) {			
			checkString = " AND CHECK_WAY = " + JCQCItemDefine.CONST_INT_CHECK_WAY_BJ ;
			workCardStatus = " STATUS = '" + WorkCard.STATUS_FINISHED + "'";//必检只查在【质量检查中】状态的工单
		} else if((JCQCItemDefine.CONST_INT_CHECK_WAY_CJ + "").equals(mode)) {
			checkString = " AND CHECK_WAY = " + JCQCItemDefine.CONST_INT_CHECK_WAY_CJ ;
			workCardStatus = " STATUS in ('" + WorkCard.STATUS_FINISHED + "','" + WorkCard.STATUS_HANDLED + "')";//抽检查【质量检查中】和【已处理】的工单
		}
        String selectSql = SqlMapUtil.getSql("jxgc-qc:" + sqlName);
        String fromSql = SqlMapUtil.getSql("jxgc-qc:getQCListFrom")
	        					   .replace("#STATUS_DCL#", QCResult.STATUS_DCL + "")
	        					   .replace("#CHECKWAY#", checkString)
	        					   .replace("#STATUS_FINISHED#", workCardStatus)
	        					   .replace("#CURRENTEMPID#", empid + "");
        StringBuilder multyAwhere = new StringBuilder();
        if (!StringUtil.isNullOrBlank(queryString)) {
        	Map queryMap = JSONUtil.read(queryString, Map.class); 
            
            // 客货类型
            if(queryMap.containsKey("vehicleType")) {
                String vehicleType = String.valueOf(queryMap.get("vehicleType"));
                if(!StringUtil.isNullOrBlank(vehicleType)){
                    multyAwhere.append(" AND C.T_VEHICLE_TYPE = '").append(vehicleType).append("' ");
                }
            }            
            
            if(queryMap.containsKey("rdpIDX")) {
                String rdpIDXString = String.valueOf(queryMap.get("rdpIDX"));
                if(!StringUtil.isNullOrBlank(rdpIDXString)){
                    multyAwhere.append(" AND C.IDX = '").append(rdpIDXString).append("' ");
                }
            }
            if(queryMap.containsKey("workItemName")) {
                String workItemNameString = String.valueOf(queryMap.get("workItemName"));
                if(!StringUtil.isNullOrBlank(workItemNameString)){
                    multyAwhere.append(" AND A.CHECK_ITEM_NAME LIKE '%").append(workItemNameString).append("%' ");
                }
            }
            if(queryMap.containsKey("taskDepict")) {
                String taskDepictString = String.valueOf(queryMap.get("taskDepict"));
                if(!StringUtil.isNullOrBlank(taskDepictString)){
                    multyAwhere.append(" AND D.WORK_CARD_NAME LIKE '%").append(taskDepictString).append("%' ");
                }
            }
            if(queryMap.containsKey("workCardName")) {
                String workCardNameString = String.valueOf(queryMap.get("workCardName"));
                if(!StringUtil.isNullOrBlank(workCardNameString)){
                    multyAwhere.append(" AND D.WORK_CARD_NAME LIKE '%").append(workCardNameString).append("%' ");
                }
            }
            // 添加页面模糊查询框，匹配车型车号
            if(queryMap.containsKey("search")) {
                String searchString = String.valueOf(queryMap.get("search"));
                if(!StringUtil.isNullOrBlank(searchString)){
                    multyAwhere.append(" AND (C.TRAIN_TYPE_SHORTNAME LIKE '%").append(searchString).append("%' ");
                    multyAwhere.append(" OR C.TRAIN_NO LIKE '%").append(searchString).append("%') ");
                }
            }
		}
        
        return selectSql + " " + fromSql + multyAwhere.toString();
	}
    
    /**
     * <li>说明：查询兑现单
     * <li>创建人：程锐
     * <li>创建日期：2014-11-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 用户id
     * @param checkWay 质检类型
     * @param start 开始行
     * @param limit 本页记录数
     * @return map对象
     */
	public Map<String, Object> getRdpOfQuery(Long empid, String checkWay, int start, int limit){                
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = SqlMapUtil.getSql("jxgc-qc:findRdpForBaseCombo")
        						.replace("#STATUS_DCL#", QCResult.STATUS_DCL + "")
        						.replace("#CHECKWAY#", checkWay)
        						.replace("#CURRENTEMPID#", empid + "");        
        String totalSql = "select count(1) from (" + sql + ")";        
        Page<ProcessTask> page = processTaskManager.findPageList(totalSql, sql, start, limit, null, null);
        map = page.extjsStore();
        return map;
    }
    
    /**
     * <li>说明：查询勾选的质量检查项
     * <li>创建人：程锐
     * <li>创建日期：2014-12-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param relationIDX 关联主键
     * @return 勾选的质量检查项
     * @throws BusinessException
     */
	@SuppressWarnings("unchecked")
    public List<QCResult> getModelList(String relationIDX) throws BusinessException {
        Map paramMap = new HashMap<String, String>();
        if(!StringUtil.isNullOrBlank(relationIDX))
            paramMap.put("relationIDX", relationIDX);
        return getQCList(paramMap);
    }
    
    /**
     * <li>说明：根据节点获取未处理的必检质检项数量
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDXS 节点IDXS
     * @return 未处理的质检项数量
     */
    public int getWClQCCountByNode(String nodeIDXS) {        
        return daoUtils.getCountSQL(getWCLQCListByNode(nodeIDXS));
    }
    
    /**
     * <li>说明：根据节点获取未处理的质检项SQL
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDXS 节点IDXS
     * @return 未处理的质检项SQL
     */    
    private String getWCLQCListByNode(String nodeIDXS) {
        StringBuilder sb = new StringBuilder();
        String sql = SqlMapUtil.getSql("jxgc-qc:getQCListByNode")
                               .replace("#nodeIDXS#", nodeIDXS);        
        sb.append(sql)
          .append(" AND STATUS IN ('")
          .append(QCResult.STATUS_WKF)
          .append("','")
          .append(QCResult.STATUS_DCL)
          .append("')")
          .append(" AND CHECK_WAY = ")
          .append(JCQCItemDefine.CONST_INT_CHECK_WAY_BJ);
        return sb.toString();
    }
    
    /**
     * <li>说明：查询质检项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 质检项列表
     */
    @SuppressWarnings("unchecked")
    public List<QCResult> getQCList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from QCResult where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
    
    
    /**
     * <li>说明：查询当前人员存在质量检查项的idx的sqL
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 人员id
     * @param selectSqlName 查询sqL
     * @return 返回sql字符串
     */
    @SuppressWarnings("unchecked")
    public String getIDXSql(Long empid,  String selectSqlName){
        String workCardStatus = " STATUS in ('" + WorkCard.STATUS_FINISHED + "','" + WorkCard.STATUS_HANDLED + "')"; 
        String selectSql = SqlMapUtil.getSql("jxgc-qc:" + selectSqlName );
        String fromSql = SqlMapUtil.getSql("jxgc-qc:getRdpQcIdxFromList")
                                    .replace("#STATUS_DCL#", QCResult.STATUS_DCL + "")
                                    .replace("#STATUS_FINISHED#", workCardStatus)
                                    .replace("#CURRENTEMPID#", empid + "");
        return selectSql +" "+ fromSql;
    }
    
    /**
     * <li>说明：查询当前用户存在质量检修的机车信息
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 人员id
     * @param entity 查询封装实体
     * @return 质量检修的机车信息分布集合
     */
    @SuppressWarnings("unchecked")
    public Page<TrainWorkPlanBean> getRdpQCList(Long operatorId, SearchEntity<TrainWorkPlanBean> searchEntity){     
        Long empid = getOmEmployeeManager().findByOperator(operatorId).getEmpid(); 
        
        TrainWorkPlanBean entity = searchEntity.getEntity();
        
        //  存在质量检查项的rdpIdx集合的sql
        String rdpIdxs = getIDXSql(empid,"getRdpIdxList"); 
        String sql = SqlMapUtil.getSql("jxgc-rdp:getTrainWorkPlanInfo")+ " AND IDX IN ( " +  rdpIdxs +")";
        if(entity != null && !StringUtil.isNullOrBlank(entity.getTrainTypeShortName())){
            sql += "AND ( Train_Type_ShortName like '%"+entity.getTrainTypeShortName()+"%' or Train_No like '%"+entity.getTrainTypeShortName()+"%') ";
        }
        
        // 客货类型
        if(entity != null && !StringUtil.isNullOrBlank(entity.getVehicleType())){
            sql += "AND  T_VEHICLE_TYPE = '"+entity.getVehicleType()+"'";
        }
        
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " +sql.substring(sql.indexOf("FROM"));
        Page<TrainWorkPlanBean>  page = this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, TrainWorkPlanBean.class);         
        List<TrainWorkPlanBean>  listBean =  page.getList();
        if(null == listBean || listBean.size()<= 0) return page ;
        // 设置作业工单状态常量
        String workCardStatus = " STATUS in ('" + WorkCard.STATUS_FINISHED + "','" + WorkCard.STATUS_HANDLED + "')";  
        // 查询质量检修项及数量
        for (TrainWorkPlanBean bean: listBean ) {   
            String sqlqc = SqlMapUtil.getSql("jxgc-qc:getRdpQcCountList")
                                    .replace("#STATUS_DCL#", QCResult.STATUS_DCL + "")
                                    .replace("#STATUS_FINISHED#", workCardStatus)
                                    .replace("#CURRENTEMPID#", empid + "")
                                    .replace("#RDPIDX#", bean.getIdx()).replace("#RepairActivityIDX#","1=1 ");
            List rdpQCResultList = daoUtils.executeSqlQuery(sqlqc);
            if(null != rdpQCResultList && rdpQCResultList.size()> 0){
                String qCResultStr = "";
                for (int i = 0; i < rdpQCResultList.size(); i++) {
                    Object[] objs = (Object[]) rdpQCResultList.get(i);
                    qCResultStr += objs[1] + "(" + objs[2] + ")  ";                   
                }
                bean.setQCResultStr(qCResultStr); 
            }
        }
        return page;     
    }
    /**
     * <li>说明：获取检修记录单列表
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 带有参数的json字符串
     * @return 检修记录单列表
     * @throws NoSuchFieldException 
     * @throws RuntimeException 
     */
    @SuppressWarnings("unchecked")
    public Page<WorkPlanRepairActivityDTO> getRecordQCList(String jsonObject) throws RuntimeException, NoSuchFieldException{ 
        //获取数据
        JSONObject jo = JSONObject.parseObject(jsonObject); 
        String rdpIDX = jo.getString("rdpIDX");//检修作业计划主键idx
        Long operatorId = jo.getLong(Constants.OPERATOR_ID); //操作人idx
        Long empid = getOmEmployeeManager().findByOperator(operatorId).getEmpid(); 
        WorkCard workCard = new WorkCard(); // 检修记录卡实体
        workCard.setRdpIDX(rdpIDX);
        //  获取记录单列表 
        Page<WorkPlanRepairActivityDTO>  page = workPlanRepairActivityManager.searchWorkPlanRepairActivityByWorkPlanIDX(jsonObject, 0, 100, null);
        List<WorkPlanRepairActivityDTO> list = page.getList();
        if(null == list && list.size()<=0)  return page;
        // 设置质量检查项值 
        for(WorkPlanRepairActivityDTO record: list){
            workCard.setRepairActivityIDX(record.getIdx());           
            List qCResultBeanList= getQCListByRecordIdx(empid, workCard); 
            if(null != qCResultBeanList && qCResultBeanList.size()>0){
                String qCResultStr = "";
                record.setRdpQCResultList(qCResultBeanList);
                for (int i = 0; i < qCResultBeanList.size(); i++) {
                    Object[] objs = (Object[]) qCResultBeanList.get(i);
                    qCResultStr += objs[1] + "(" + objs[2] + ")  ";                   
                }
                record.setRdpQCResult(qCResultStr);
            }
                
        }  
        return page;
    }  
    
      /**
     * <li>说明：通过检修记录单及兑现单idx获取质检项
     * <li>创建人：张迪
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 人员id
     * @param entity 记录卡实体
     * @return 质检项列表及数量
     */
    public List getQCListByRecordIdx (Long empid, WorkCard entity){
        // 设置作业工单状态常量
          String workCardStatus = " STATUS in ('" + WorkCard.STATUS_FINISHED + "','" + WorkCard.STATUS_HANDLED + "')";    
          String sql = SqlMapUtil.getSql("jxgc-qc:getRdpQcCountList")
                                .replace("#STATUS_DCL#", QCResult.STATUS_DCL + "")
                                .replace("#STATUS_FINISHED#", workCardStatus)
                                .replace("#CURRENTEMPID#", empid + "")
                                .replace("#RDPIDX#", entity.getRdpIDX())
                                .replace("#RepairActivityIDX#", "B.Repair_Activity_IDX = '"+ entity.getRepairActivityIDX()+ "'");     
          return daoUtils.executeSqlQuery(sql);      
    }

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2016-9-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorId
     * @param rdpRecordIDX
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    public List<WorkCardBean> findWorkCardInfoByWorkPlanRepairActivityIDX(Long operatorId, String rdpRecordIDX) throws BusinessException, Exception {
        List<WorkCardBean> workCardBean = workCardManager.findWorkCardInfoByWorkPlanRepairActivityIDX(rdpRecordIDX);
        Long empid = getOmEmployeeManager().findByOperator(operatorId).getEmpid(); 
       
        // 设置质量检查项值 
        for(WorkCardBean card: workCardBean){
            //  获取检修活动的idx 
            String recordIdxs = getIDXSql(empid, "getWorkCardIDXList");   
            String whereStr = " and A.WORK_CARD_IDX= '"+ card.getIdx() + "'" + " and D.Repair_Activity_IDX= '"+ rdpRecordIDX + "'";
            card.setWorkcardQcList(daoUtils.executeSqlQuery(recordIdxs + whereStr));
        }  
        return workCardBean;
    }
}
