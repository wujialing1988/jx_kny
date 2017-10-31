package com.yunda.jx.jczl.attachmanage.manager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;
import com.yunda.freight.base.vehicle.manager.TrainVehicleTypeManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrainBean;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrainDTO;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordManager.PartsRdpRecord;
import com.yunda.jx.webservice.stationTerminal.base.entity.TrainNoBean;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：JczlTrain业务类,机车信息
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="jczlTrainManager")
public class JczlTrainManager extends JXBaseManager<JczlTrain, JczlTrain> implements IbaseCombo{
	
	private static final String YES = "yes";
    private static final String TRAIN_TYPE_IDX = "trainTypeIDX";
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    @Resource
	private OmOrganizationManager omOrganizationManager;
    
    /**
     * 车型业务类
     */
    @Resource
    private TrainVehicleTypeManager trainVehicleTypeManager;
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-23
	 * <li>修改人： 程锐
	 * <li>修改日期：2013-04-29
	 * <li>修改内容：验证该车型是否已存在车号
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 */		
	@Override
	public String[] validateUpdate(JczlTrain entity) {
        List<String> errMsg = new ArrayList<String>();   
        if(!StringUtil.isNullOrBlank(entity.getVehicleType()) && !StringUtil.isNullOrBlank(entity.getTrainNo())){
            List<JczlTrain> trains = getModelList(entity.getTrainTypeIDX(), entity.getTrainNo(),null, entity.getIdx(),entity.getVehicleType());
            if(trains.size() > 0){
                errMsg.add("车辆【"+entity.getTrainNo()+"】已经存在！");
            }
        }
        if(!StringUtil.isNullOrBlank(entity.getIdx())){
            JczlTrain train = getModelById(entity.getIdx());
            if(train.getAssetState() != null && train.getAssetState() == JczlTrain.TRAIN_ASSET_STATE_SCRAP){
                errMsg.add("【"+entity.getTrainTypeShortName()+"】车型、【"+entity.getTrainNo()+"】车号的机车已报废，不能编辑！");
            }
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
	}
    
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型ID
     * @param trainTypeShortName 车型名称
     * @param trainNo 车号
     * @param vehicleType 车辆类型 10 货车 20 客车 
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveOrUpdateNewTrain(String trainTypeIDX,String trainTypeShortName,String trainNo,String vehicleType) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        JczlTrain train = new JczlTrain();
        train.setTrainTypeIDX(trainTypeIDX);
        train.setTrainTypeShortName(trainTypeShortName);
        train.setTrainNo(trainNo);
        train.setVehicleType(vehicleType);
        this.saveOrUpdateTransfer(train);
    }
    
    /**
     * <li>说明：新增车辆信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX
     * @param trainNo
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public JczlTrain saveOrUpdateNewTrainNew(String trainTypeIDX,String trainNo) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        TrainVehicleType vehicleType = trainVehicleTypeManager.getModelById(trainTypeIDX);
        if(vehicleType == null){
            return null ;
        }
        JczlTrain train = getJczlTrainByTypeAndNo(trainTypeIDX, trainNo);
        if(train != null){
            return train ;
        }
        train = new JczlTrain();
        train.setTrainTypeIDX(trainTypeIDX);
        train.setTrainTypeShortName(vehicleType.getTypeCode());
        train.setTrainNo(trainNo);
        train.setVehicleType(vehicleType.getVehicleType());
        this.saveOrUpdateTransfer(train);
        return train ;
    }
	
	/**
	 * <li>说明：保存机车信息
     * <li>创建人：程锐
     * <li>创建日期：2013-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param train 机车信息实体对象
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    public void saveOrUpdateTransfer(JczlTrain train) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        OmEmployee emp = SystemContext.getOmEmployee();
        train.setAssetState(JczlTrain.TRAIN_ASSET_STATE_USE);
        if(train.getTrainState() == null){
            train.setTrainState(JczlTrain.TRAIN_STATE_USE);
        }
        train.setIsHaveResume(0);
        train.setRegisterPerson(emp.getEmpid());
        train.setRegisterPersonName(emp.getEmpname());
        train.setRegisterTime(new Date());
        if(train.getLeaveDate() == null){
        	train.setLeaveDate(new Date()); // 出厂时间
        }
        saveOrUpdate(train);
    }
	/**
	 * 
	 * <li>方法名称：findPageDataList
	 * <li>方法说明：查询数据列表 
	 * @param searchJson searchJson
	 * @param start start
	 * @param limit limit
	 * @param orders orders
	 * @param flag 查询标志，如果等于UndertakeTrain表示查询当前单位承修的车否则即为全部
	 * @return
	 * return: Page
	 * <li>创建人：张凡
	 * <li>创建时间：2012-10-25 下午03:56:44
	 * <li>修改人：
	 * <li>修改内容：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Page findPageDataList(String searchJson,int start,int limit, Order[] orders,String flag) throws JsonParseException, JsonMappingException, IOException{
        String systemOrgcode = JXSystemProperties.OVERSEA_ORGCODE;
        OmOrganization systemOrg = OmOrganizationSelectManager.getOrgByOrgcode(systemOrgcode);
		String querySQL = "select t.idx as \"idx\"," +
						 "t.train_Type_IDX as \"trainTypeIDX\", " +
						 "t.train_No as \"trainNo\", " +
						 "t.train_Type_ShortName as \"trainTypeShortName\", " +
						 "t.asset_State as \"assetState\", " +
						 "t.train_State as \"trainState\", " +
						 "u.t_use_name as \"trainUse\", " +
						 "t.hold_OrgId as \"holdOrgId\", " +
						 "t.used_OrgId as \"usedOrgId\", " +
						 "t.old_Hold_OrgId as \"oldHoldOrgId\", " +
						 "t.make_Factory_IDX as \"makeFactoryIDX\", " +
						 "t.make_Factory_Name as \"makeFactoryName\", " +
						 "t.leave_Date as \"leaveDate\", " +
                         "t.BuildUp_Type_IDX as \"buildUpTypeIDX\", " +
						 "t.buildUp_Type_Code as \"buildUpTypeCode\", " +
						 "t.buildUp_Type_Name as \"buildUpTypeName\", " +			
						 "t.register_Person as \"registerPerson\", " +
						 "t.register_Person_Name as \"registerPersonName\", " +
						 "t.register_Time as \"registerTime\", " +
						 "t.record_Status as \"recordStatus\", " +
						 "t.siteID as \"siteID\", " +
						 "t.creator as \"creator\", " +
						 "t.create_Time as \"createTime\", " +			
						 "t.update_Time as \"updateTime\", " +
						 "t.is_Have_Resume as \"isHaveResume\", " +
						 "t.remarks, " +
                         "t.b_Id as \"bId\", " +
                         "t.d_Id as \"dId\", " +
                         "b.b_name as \"bName\", " +
                         "b.shortName as \"bShortName\", " +
                         "d.d_name as \"dName\", " +
                         "d.shortName as \"dShortName\", " +
						 "psdw.orgname as \"holdOrgName\", " +
						 "zpdw.orgname as \"usedOrgName\", " +
						 "ypsdw.orgname as \"oldHoldOrgName\" ";
		
		String fromSql = " from Jczl_Train t , OM_ORGANIZATION psdw, OM_ORGANIZATION zpdw, OM_ORGANIZATION ypsdw, j_jcgy_train_use u , j_gyjc_bureau b, J_GYJC_DEPORT d " +
						" where 1=1 and t.hold_OrgId = psdw.orgid(+) and t.used_OrgId = zpdw.orgid(+) and t.old_Hold_OrgId = ypsdw.orgid(+) " +
                        " and t.b_id = b.b_id(+) and t.d_id = d.d_id(+)" +
						" and t.train_Use = u.t_use_id(+) and t.record_status='0'" ;
        if("UndertakeTrain".equals(flag)){//可承修车查询
            fromSql = fromSql.concat(" and t.train_Type_IDX in (select u.train_type_idx from ");
            fromSql = fromSql.concat(" JCZL_UNDERTAKE_TRAIN_TYPE u where u.record_status=0 and u.undertake_orgid='"+systemOrg.getOrgid()+"')");
            
            fromSql = fromSql.concat(" and t.train_no in (select ut.train_no from JCZL_UNDERTAKE_TRAIN ut where ut.record_status=0 ");
//            增加承修机车车号过滤
            if (searchJson != null) {
            	Map map =  JSONUtil.read(searchJson, Map.class);
            	if(map.get(TRAIN_TYPE_IDX) != null){
            		fromSql = fromSql.concat(" and ut.train_type_idx='" + map.get(TRAIN_TYPE_IDX) + "'");
            	}
			}
            fromSql = fromSql.concat(")");
            if (searchJson != null) {
            	Map map =  JSONUtil.read(searchJson, Map.class);
            	if(map.get(TRAIN_TYPE_IDX) != null){
            		fromSql = fromSql.concat(" and t.train_type_idx='" + map.get(TRAIN_TYPE_IDX) + "'");
            	}
			}
        
        }else if("moveIn".equals(flag)){ //查询可调入机车
            fromSql = fromSql.concat(" and t.hold_OrgId <> '"+systemOrg.getOrgid()+"'");
            fromSql = fromSql.concat(" and t.asset_state <> "+JczlTrain.TRAIN_ASSET_STATE_SCRAP);
        }else{ //配属车查询
            fromSql = fromSql.concat(" and t.hold_OrgId='"+systemOrg.getOrgid()+"'");
            fromSql = fromSql.concat(" and t.asset_state <> "+JczlTrain.TRAIN_ASSET_STATE_SCRAP);
        }
		StringBuffer sqlWhere =  new StringBuffer("   order by t.train_No, t.update_Time DESC");
		String totalSql = "select count(t.idx) " + fromSql + sqlWhere;
		String sql =querySQL+ fromSql + sqlWhere;
		return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    }	

	/**
	 * <li>方法说明： 根据车型查询机车
	 * <li>方法名：findTrainByTrains
	 * @param searchJson searchJson
	 * @param start start
	 * @param limit limit
	 * @param orders orders
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015年11月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public Page<JczlTrain> findTrainByTrains(String searchJson,int start,int limit, Order[] orders){
	    StringBuilder s = new StringBuilder("select x.*, dx.d_name as \"usedOrgName\", dx.shortname as \"dShortName\" from ");
	    StringBuilder core = new StringBuilder("(select t.train_type_idx,");
	    core.append(" t.train_type_shortname, t.b_id, b.b_name, t.d_id, d.d_name,t.train_no, t.used_id from jczl_train t, ");
	    core.append("j_gyjc_bureau b, J_GYJC_DEPORT d where t.b_id = b.b_id and t.d_id = d.d_id) x ");
	    s.append(core);
	    s.append("left join J_GYJC_DEPORT dx on dx.d_id = x.used_id");
	    
	    String total = "select count(1) from " + core;
	    
	    return super.findPageList(total, s.toString(), start, limit, searchJson, orders);
	}
	
	
	/**
	 * <li>方法名称：findJczlTrainTreeData
	 * <li>方法说明：机车履历树查询（前期开发临时使用）
     * @param trainNo 车型
     * @param id 树父节点ID
	 * @return
	 * return: List<HashMap>
	 * <li>创建人：张凡
	 * <li>创建时间：2012-10-31 下午04:02:58
	 * <li>修改人：程锐 2013-07-11
	 * <li>修改内容：删除“是否有履历”的过滤条件
	 * <li>修改人：张凡 2013-08-07
	 * <li>修改内容：修改从局到车的查询
	 */
	@SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> findJczlTrainTreeData(String trainNo, String id) {
	    String sql = null;
	    boolean isLeaf = true;
	    String idToken = null; //ID标识
	    if("ROOT_0".equals(id)){//为第一级，查出下级所有
	        sql = "select distinct b.b_name,b.b_id " +
	        		"from jczl_train t,j_gyjc_bureau b " +
	        		"where t.b_id = b.b_id and t.record_status=0 " ;
	        isLeaf = false;
	        idToken = "J_";
	    }else if(id.indexOf("J_") == 0){//为铁路局，查出下级所有
	        sql = "select distinct d.d_name, d.d_id " +
	        		"from jczl_train t, j_gyjc_deport d " +
	        		"where d.d_id = t.d_id and t.b_id='" + id.substring(2) + "' and t.record_status=0";
	        isLeaf = false;
	        idToken = "D_";
	    }else if(id.indexOf("D_") == 0){//查出车型
	        sql = "select distinct t.train_type_shortname,t.train_type_idx||','||d.d_id " +
	        		"from jczl_train t, j_gyjc_deport d " +
	        		"where d.d_id = t.d_id and t.d_id='" + id.substring(2) + "' and t.record_status=0";
	        isLeaf = false;
            idToken = "T_";
	    }else if(id.indexOf("T_")==0){//查出机车
	        String[] param = id.substring(2).split(",");
            
            sql="select distinct t.idx, t.train_type_shortname,t.train_no,train_state," +
                "t.train_use,b.b_name, d.d_name,'',make_factory_name,leave_date," +
                "remarks,u.t_use_name,train_type_idx, b.SHORTNAME, d.SHORTNAME " +
                "from Jczl_Train t, j_jcgy_train_use u, J_GYJC_BUREAU b, J_GYJC_DEPORT d " +
                "where t.train_Use = u.t_use_id(+) and t.B_ID = b.B_ID(+) and t.D_ID = d.D_ID(+) " +
                " and t.record_status=0 and t.d_id=" + param[1] +" and t.train_type_idx=" + param[0];

	    }
	    
	    if(trainNo != null && !trainNo.trim().equals("")){
	        sql += " and t.train_no like '" + trainNo + "%'";
	    }
	    if(isLeaf){
	        sql += " order by t.train_no";
	    }
	    	   
        List<Object[]> trainList = daoUtils.executeSqlQuery(sql);
       
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        if(isLeaf){
            for (Object[] train : trainList) {           
                HashMap<String, Object> nodeMap = new HashMap<String, Object>();
                nodeMap.put("id", train[0]);
                nodeMap.put("text", train[2]);
                nodeMap.put("trainTypeIdx", train[12]);
                nodeMap.put("trainTypeName", train[1]);
                nodeMap.put("trainState", train[3]);
                nodeMap.put("trainUse", train[4]);
                nodeMap.put("holdOrgName", train[5]);
                nodeMap.put("usedOrgName", train[6]);
                //nodeMap.put("oldHoldOrgName", train[7]); //此字段为空
                nodeMap.put("makeFactoryName", train[8]);
                nodeMap.put("leaveDate", train[9]);
                nodeMap.put("remarks", train[10]);
                nodeMap.put("trainUseName", train[11]);
                nodeMap.put("leaf", isLeaf);
                nodeMap.put("bShortName", train[13]);
                nodeMap.put("dShortName", train[14]);
                children.add(nodeMap);
            }
        }else{
            for (Object[] train : trainList) {           
                HashMap<String, Object> nodeMap = new HashMap<String, Object>();
                nodeMap.put("id", idToken + train[1]);
                nodeMap.put("text", train[0]);
                nodeMap.put("leaf", isLeaf);
                children.add(nodeMap);
            }
        }
        return children;
    }
	
	/**
	 * <li>方法说明：配属机车查询树结构查询 
	 * <li>方法名称：findJczlTrainSearchTree
	 * <li>@param id
	 * <li>@return
	 * <li>return: List<HashMap>
	 * <li>创建人：张凡
	 * <li>创建时间：2013-8-8 下午02:36:56
	 * <li>修改人：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> findJczlTrainSearchTree(String id) {
	    String sql = null;
	    boolean isLeaf = true;
	    if("0".equals(id)){//为第一级，查出下级所有
	        sql = "select distinct b.b_name as orgname,b.b_id, 'b' " +
	        "from jczl_train t,j_gyjc_bureau b " +
	        "where t.b_id = b.b_id and t.record_status=0 " ;
	        isLeaf = false;
	    }else{//为铁路局，查出下级所有
	        sql = "select distinct d.d_name as orgname, d.d_id, 'd' " +
	        "from jczl_train t, j_gyjc_deport d " +
	        "where d.d_id = t.d_id and t.b_id='" + id + "' and t.record_status=0";
	        isLeaf = true;
	    }
	    
	    sql += " order by orgname";
	    
	    List<Object[]> orgList = daoUtils.executeSqlQuery(sql);
	    
	    List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
	    
        for (Object[] org : orgList) {           
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id",  org[1]);
            nodeMap.put("text", org[0]);
            nodeMap.put("type", org[2]);
            nodeMap.put("leaf", isLeaf);
            children.add(nodeMap);
        }
	    
	    return children;
	}
	
    /**
     * 
     * <li>说明：获取下拉列表前台store所需Map对象
     * <li>创建人：程梅
     * <li>创建日期：2012-10-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param queryValue queryValue
     * @param queryParams 查询参数Map对象
     * @param start 开始行
     * @param limit 每页记录数
     * @param queryHql 查询Hql字符串
     * @param isAll isAll
     * @param isCx isCx
     * @param orgseq orgseq
     * @param isRemoveRun isRemoveRun
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> page(String queryValue,Map queryParams, int start, int limit, String queryHql,
        String isAll,String isCx,String orgseq,String isRemoveRun) {
        StringBuffer hql = new StringBuffer();
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql.append(queryHql);
        } else { 
        	//查询承修机车
            if(YES.equals(isCx)){
            	hql.append("select j from TrainConfigInfo j where 1=1 and j.recordStatus=0 ");
            }else if("no".equals(isCx)){  //查询配属机车
            	hql.append(" select new JczlTrain(j.trainNo,j.holdOrgId,o.orgname,o.orgseq,j.makeFactoryIDX,j.makeFactoryName,j.leaveDate,");
                hql.append(" j.buildUpTypeIDX,j.buildUpTypeCode,j.buildUpTypeName,j.trainUse,t.useName as trainUseName, j.bId as bId,");
                hql.append(" j.dId as dId, b.bName as bName, b.shortName as bShortName, d.dName as dName, d.shortName as dShortName)");
                hql.append(" from JczlTrain j,OmOrganization o ,TrainUse t, JgyjcBureau b, JgyjcDeport d  ");
                hql.append(" where 1=1 and j.holdOrgId=o.orgid and j.trainUse=t.useID and j.bId = b.bId and j.dId = d.dId and j.recordStatus=0 ");
            }
            if(!YES.equals(isAll)){
                if (!StringUtil.isNullOrBlank(orgseq)) {
                    hql.append(" and j.holdOrgId in (select orgid from OmOrganization where orgseq like '").append(orgseq).append(
                        "%' and status='running')");
                }
            }
            //isRemoveRun为true代表需排除在修机车
            if("true".equals(isRemoveRun)){
                //edit by peak 将机车兑现单类型换成视图实体 V3.2.1代码重构
            	hql.append(" and j.trainNo not in (select trainNo from TrainWorkPlan where recordStatus=0 and workPlanStatus in ('");
            	hql.append(TrainWorkPlan.STATUS_NEW).append("','").append(TrainWorkPlan.STATUS_HANDLING).append("')");
            	if(!queryParams.isEmpty() && !StringUtil.isNullOrBlank(queryParams.get(TRAIN_TYPE_IDX).toString()))
            		hql.append(" and typeIDX = '").append(queryParams.get(TRAIN_TYPE_IDX).toString()).append("'");
            	hql.append(")");
            }
            if (!StringUtil.isNullOrBlank(queryValue)) {
                hql.append(" and j.trainNo like '%").append(queryValue).append("%'"); //修改默认从首字开始查询匹配
            }
            //queryHql配置项为空则按queryParams配置项拼接Hql
            if (!queryParams.isEmpty()) {
                Set<Map.Entry<String, String>> set = queryParams.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!StringUtil.isNullOrBlank(value)) {
                        if(value.startsWith("<>")){   //如果查询value值包含不等于符号则，采用不等于查询
                            hql.append(" and j.").append(key).append(" <> '").append(value.substring(2)).append("'");
                        }else{
                            hql.append(" and j.").append(key).append(" = '").append(value).append("'");
                        }
                    }
                }
            }
        }
        hql.append(" order by j.trainNo");
        //根据hql构造totalHql
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
    
    /**
     * <li>说明：分页查询录入了新造试验的机车信息，返回实体类的分页列表对象，基于单表实体类分页查询
     * <li>创建人：王治龙
     * <li>创建日期：2012-11-1
     * <li>修改人：程梅
     * <li>修改日期：2013年3月5日
     * <li>修改内容：过滤已在承修机车里配置过的机车
     * @param searchEntity  SearchEntity<JczlTrain> 包装了实体类查询条件的对象
     * @param flag 查询的标志取值有：in，为表示查询已经生成试验记录的车型 not: 表示查询没有生成试验结果的车型
     * @param undertakeTrainTypeIDX  查询使用中的机车用于承修机车配置
     * @return Page 分页查询列表
     */
    @SuppressWarnings("unchecked")
    public Page findPageTrainList(final SearchEntity<JczlTrain> searchEntity , final String flag,final String undertakeTrainTypeIDX) {
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page)template.execute(new HibernateCallback(){
            public Page doInHibernate(Session s){
                try {
                    String sql = "1=1";
                    String sql2 = "1=1";
                    if("in".equals(flag)){
                        sql = "Train_Type_IDX in (select nvl(t.train_type_idx,'-1') from JCZL_Test_Resutlt t where t.record_status=0 and t.repair_account_idx is null )";
                        sql2 = "Train_No in (select  nvl(t.Train_No,'-1' ) from JCZL_Test_Resutlt t where t.record_status=0 and t.repair_account_idx is null)";
                    }
                    if("not".equals(flag)){
                        sql = "(Train_Type_IDX,Train_No)"+flag+" in (select distinct nvl(t.train_type_idx,'-1'),nvl(t.Train_No,'-1') from JCZL_Test_Resutlt t where t.record_status=0 and t.repair_account_idx is null)";
//                        sql2 = "Train_No "+flag+" in (select  nvl(t.Train_No,'-1' ) from JCZL_Test_Resutlt t where t.record_status=0 and t.repair_account_idx is null)";
                    }
                    if(!StringUtil.isNullOrBlank(undertakeTrainTypeIDX)){
                        sql = " Train_No not in (select nvl(Train_No,'-1') from JCZL_UNDERTAKE_TRAIN" +
                                " where record_status=0 and Undertake_Train_Type_IDX='"+undertakeTrainTypeIDX+"')"+
                                " and Asset_State="+JczlTrain.TRAIN_ASSET_STATE_USE;
                    }
                    String sql3 = "1=1";
                    if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainTypeIDX())){
                    	sql3 = " Train_Type_IDX = '" + searchEntity.getEntity().getTrainTypeIDX() + "'"; 
                    }
                    JczlTrain entity = searchEntity.getEntity();
                    //过滤逻辑删除记录
                    BeanUtils.forceSetProperty(entity, EntityUtil.RECORD_STATUS, Constants.NO_DELETE);                  
                    //过滤掉idx、siteID、creator、createTime、updator、updateTime的查询条件
                    Example exp = Example.create(entity)
                        .excludeProperty(EntityUtil.IDX)
                        .excludeProperty(EntityUtil.SITE_ID)
                        .excludeProperty(EntityUtil.CREATOR)
                        .excludeProperty(EntityUtil.CREATE_TIME)
                        .excludeProperty(EntityUtil.UPDATOR)
                        .excludeProperty(EntityUtil.UPDATE_TIME)
                        .enableLike().enableLike(MatchMode.ANYWHERE);
                    //查询总记录数
                    int total = ((Integer)s.createCriteria(entity.getClass())
                        .add(exp)
                        .add(Restrictions.sqlRestriction(sql))
                        .add(Restrictions.sqlRestriction(sql2))
                        .add(Restrictions.sqlRestriction(sql3))
                        .setProjection(Projections.rowCount())
                        .uniqueResult())
                        .intValue();
                    //分页列表
                    Criteria criteria = s.createCriteria(entity.getClass()).add(exp)
                        .add(Restrictions.sqlRestriction(sql))
                        .add(Restrictions.sqlRestriction(sql2))
                        .add(Restrictions.sqlRestriction(sql3))
                        .setFirstResult(searchEntity.getStart())
                        .setMaxResults(searchEntity.getLimit());
                    //设置排序规则
                    Order[] orders = searchEntity.getOrders();
                    if(orders != null){
                        for (Order order : orders) {
                            criteria.addOrder(order);
                        }                   
                    }
                    criteria.addOrder(Order.desc("updateTime"));
                    return new Page(total, criteria.list());
                } catch (Exception e) {
                	ExceptionUtil.process(e,logger);
                }
                return null;
            }
        });
    }
    
    /**
     * 车号下拉框（肯尼亚）
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        String queryParams = req.getParameter("queryParams");
        
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        StringBuffer hql = new StringBuffer(" select t from JczlTrain t where t.recordStatus = 0 ") ;
        // query参数是获取EXTJS的combox控件捕获的键盘输入文字
        String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");   
        if(!StringUtil.isNullOrBlank(queryValue)){
            hql.append(" and t.trainNo like '%").append(queryValue).append("%'");
        }
        
        // 客货类型
        String vehicleType = String.valueOf(queryParamsMap.get("vehicleType"));
        if(!StringUtil.isNullOrBlank(vehicleType) && queryParamsMap.get("vehicleType") != null){
            hql.append(" and t.vehicleType = '").append(vehicleType).append("'");
        }
        
        // 车型过滤
        String trainTypeIDX = String.valueOf(queryParamsMap.get("trainTypeIDX"));
        if(!StringUtil.isNullOrBlank(trainTypeIDX) && queryParamsMap.get("trainTypeIDX") != null){
            hql.append(" and t.trainTypeIDX = '").append(trainTypeIDX).append("'");
        }
        
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
    
    
    /**
     * 获取车号列表（肯尼亚）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getTrainNoByTrainType(Map queryParams, int start, int limit) throws Exception {
        
        StringBuffer hql = new StringBuffer(" select t from JczlTrain t where t.recordStatus = 0 ") ;
        // 客货类型
        String vehicleType = CommonUtil.getMapValue(queryParams, "vehicleType");
        if(!StringUtil.isNullOrBlank(vehicleType)){
            hql.append(" and t.vehicleType = '").append(vehicleType).append("'");
        }
        
        // 车型过滤
        String trainTypeIDX = CommonUtil.getMapValue(queryParams, "trainTypeIDX");
        if(!StringUtil.isNullOrBlank(vehicleType)){
            hql.append(" and t.trainTypeIDX = '").append(trainTypeIDX).append("'");
        }
        
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
    
    /**
     * <li>说明：重写getBaseComboData，获取下拉列表前台store所需Map对象
     * <li>创建人：程锐
     * <li>创建日期：2014-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param req HttpServletRequest对象    
     * @param start 开始行
     * @param limit 每页记录数
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData1(HttpServletRequest req, int start, int limit) throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();        
        String queryHql = req.getParameter("queryHql");
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        String isAll = req.getParameter("isAll");
        String isCx = YES;
        if(queryParamsMap.get("isCx") != null){
        	isCx = String.valueOf(queryParamsMap.get("isCx"));
            queryParamsMap.remove("isCx");
        } 
        String isIn = "false";
        if(queryParamsMap.get("isIn") != null){
        	isIn = String.valueOf(queryParamsMap.get("isIn"));
            queryParamsMap.remove("isIn");
        }
        String isRemoveRun = "false";
        if(queryParamsMap.get("isRemoveRun") != null){
        	isRemoveRun = String.valueOf(queryParamsMap.get("isRemoveRun"));
            queryParamsMap.remove("isRemoveRun");
        }
        if("true".equals(isIn)){//查询调入机车
        	Order[] orders = null;//TODO初始化order数组
            map = findPageDataList("{'trainTypeIDX':'" + queryParamsMap.get(TRAIN_TYPE_IDX) + "'}", start, limit, orders,"moveIn").extjsStore();
        }else{
//          query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");
        	String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
            OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
            map = page(queryValue,queryParamsMap, start, limit, queryHql,isAll,isCx,org.getOrgseq(),isRemoveRun);
        }
        return map;
    }
    /**
     * <li>方法名称：trainStatistics
     * <li>方法说明：统计机车信息 
     * @param entity 实体
     * @param searchJson searchJson
     * @param start start
     * @param limit limit
     * @param order order
     * @return
     * return: Page
     * <li>创建人：张凡
     * <li>创建时间：2012-11-28 下午04:12:59
     * <li>修改人：
     * <li>修改内容：
     */
    public Page trainStatistics(JczlTrain entity, String searchJson, int start, int limit,Order[] order){
        String sql = "select j.t_type_name as \"trainTypeName\"," +
        		"count(t.train_type_idx) as \"assetState\"," +
        		"t.train_type_shortname as \"trainTypeShortName\" ";
        String fromSql = "from jczl_train t,j_jcgy_train_type j " +
                "where 1=1 and t.train_type_idx = j.t_type_id ";
        StringBuffer sqlGroup =  new StringBuffer(" group by j.t_type_name,t.train_type_shortname");
        String querySql =sql+ fromSql + sqlGroup;
        String totalSql = "select count(1)  from (" + querySql + ")";
        return super.findPageList(totalSql, querySql, start , limit, searchJson.replace("\"usedOrgId\":"+entity.getUsedOrgId(), ""), order);
    }
    /**
     * 
     * <li>方法名称：findTrainList
     * <li>方法说明：查询机车列表 
     * @param entity entity
     * @param searchJson searchJson
     * @param start start
     * @param limit limit
     * @param orders orders
     * @return
     * return: Page
     * <li>创建人：张凡
     * <li>创建时间：2012-11-29 上午10:06:26
     * <li>修改人：
     * <li>修改内容：
     */
    public Page<JczlTrain> findTrainList(JczlTrain entity, String searchJson,int start,int limit, Order[] orders) {
        String querySQL = "select t.idx as \"idx\"," +
                         "t.train_Type_IDX as \"trainTypeIDX\", " +
                         "t.train_No as \"trainNo\", " +
                         "t.train_Type_ShortName as \"trainTypeShortName\", " +
                         "t.asset_State as \"assetState\", " +
                         "t.train_State as \"trainState\", " +
                         "u.t_use_name as \"trainUse\", " +                         
                         "t.make_Factory_IDX as \"makeFactoryIDX\", " +
                         "t.make_Factory_Name as \"makeFactoryName\", " +
                         "t.leave_Date as \"leaveDate\", " +
                         "t.BuildUp_Type_IDX as \"buildUpTypeIDX\", " +
                         "t.buildUp_Type_Code as \"buildUpTypeCode\", " +
                         "t.buildUp_Type_Name as \"buildUpTypeName\", " +           
                         "t.register_Person as \"registerPerson\", " +
                         "t.register_Person_Name as \"registerPersonName\", " +
                         "t.register_Time as \"registerTime\", " +
                         "t.record_Status as \"recordStatus\", " +
                         "t.siteID as \"siteID\", " +
                         "t.creator as \"creator\", " +
                         "t.create_Time as \"createTime\", " +          
                         "t.update_Time as \"updateTime\", " +
                         "t.is_Have_Resume as \"isHaveResume\", " +
                         "t.remarks, " +
                         "b.b_NAME as \"holdOrgName\", " +
                         "d.d_NAME as \"usedOrgName\" " ;
        
        String fromSql = " from Jczl_Train t , j_jcgy_train_use u, J_GYJC_BUREAU b, J_GYJC_DEPORT d " +
                        " where 1=1 and t.train_Use = u.t_use_id(+) and t.B_ID = b.B_ID(+) and t.D_ID = d.D_ID(+) " +                       
                        "and t.record_status='0' ";
        StringBuffer sqlWhere =  new StringBuffer(" order by t.update_Time DESC");
        String totalSql = "select count(t.idx) " + fromSql + sqlWhere;
        String sql =querySQL+ fromSql + sqlWhere;
        return super.findPageList(totalSql, sql, start , limit, searchJson.replace("\"usedOrgId\":"+entity.getUsedOrgId(),""), orders);
    }
    /**
     * <li>说明：根据车型查询车号是否存在查询机车是否存在
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型IDX ;
     * @param trainNo 车号;
     * @param holdOrgId 配属单位ID
     * @return List<JczlTrain> 机车实体对象集合
     */ 
    @SuppressWarnings("unchecked")
    public List<JczlTrain> getModelList(String trainTypeIDX, String trainNo,Long holdOrgId) {
        StringBuffer hql = new StringBuffer();
        hql.append("From JczlTrain t where t.recordStatus=0 ");
        if(!StringUtil.isNullOrBlank(trainTypeIDX)){
            hql.append(" and t.trainTypeIDX = '").append(trainTypeIDX).append("'");
        }
        if(!StringUtil.isNullOrBlank(trainNo)){
            hql.append(" and t.trainNo = '").append(trainNo).append("'");
        }
        if(holdOrgId != null){
            hql.append(" and t.holdOrgId = '").append(holdOrgId).append("'");
        }
        return (List<JczlTrain>)this.daoUtils.find(hql.toString());
    }
    
    
    
    /**
     * 
     * <li>说明：根据车型查询车号是否存在查询机车是否存在
     * <li>创建人：程锐
     * <li>创建日期：2013-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型IDX ;
     * @param trainNo 车号;
     * @param holdOrgId 配属单位ID;
     * @param idx 机车信息主键
     * @return List<JczlTrain> 机车实体对象集合
     */
    @SuppressWarnings("unchecked")
    public List<JczlTrain> getModelList(String trainTypeIDX, String trainNo,Long holdOrgId, String idx,String vehicleType) {
        StringBuffer hql = new StringBuffer();
        hql.append("from JczlTrain t where t.recordStatus=0 ");
        if(!StringUtil.isNullOrBlank(trainTypeIDX)){
            hql.append(" and t.trainTypeIDX = '").append(trainTypeIDX).append("'");
        }
        if(!StringUtil.isNullOrBlank(trainNo)){
            hql.append(" and t.trainNo = '").append(trainNo).append("'");
        }
        if(holdOrgId != null){
            hql.append(" and t.holdOrgId = '").append(holdOrgId).append("'");
        }
        if(!StringUtil.isNullOrBlank(vehicleType)){
            hql.append(" and t.vehicleType = '").append(vehicleType).append("'");
        }
        if(!StringUtil.isNullOrBlank(idx)){
            hql.append(" and t.idx != '").append(idx).append("'");
        }
        return (List<JczlTrain>)this.daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：机车信息维护列表（天津基地）
     * <li>创建人：程锐
     * <li>创建日期：2013-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始行
     * @param limit 每页记录数
     * @param searchJson 查询的json字符串
     * @param orders 排序对象
     * @return Page 分页对象
     * @throws JsonParseException @throws JsonMappingException @throws IOException
     */
    public Page<JczlTrain> jczlTrainList(String searchJson,int start,int limit, Order[] orders) throws JsonParseException, JsonMappingException, IOException{
        
        String querySql = "select t.idx as \"idx\"," +
                         "t.train_Type_IDX as \"trainTypeIDX\", " +
                         "t.train_No as \"trainNo\", " +
                         "t.train_Type_ShortName as \"trainTypeShortName\", " +
                         "t.asset_State as \"assetState\", " +
                         "t.train_State as \"trainState\", " +
                         "t.TRAIN_USE as \"trainUse\", " +
                         "u.t_use_name as \"trainUseName\", " +
                         "t.hold_OrgId as \"holdOrgId\", " +
                         "t.used_OrgId as \"usedOrgId\", " +
                         "t.old_Hold_OrgId as \"oldHoldOrgId\", " +
                         "t.make_Factory_IDX as \"makeFactoryIDX\", " +
                         "t.make_Factory_Name as \"makeFactoryName\", " +
                         "t.leave_Date as \"leaveDate\", " +
                         "t.BuildUp_Type_IDX as \"buildUpTypeIDX\", " +
                         "t.buildUp_Type_Code as \"buildUpTypeCode\", " +
                         "t.buildUp_Type_Name as \"buildUpTypeName\", " +           
                         "t.register_Person as \"registerPerson\", " +
                         "t.register_Person_Name as \"registerPersonName\", " +
                         "t.register_Time as \"registerTime\", " +
                         "t.record_Status as \"recordStatus\", " +
                         "t.siteID as \"siteID\", " +
                         "t.creator as \"creator\", " +
                         "t.create_Time as \"createTime\", " +          
                         "t.update_Time as \"updateTime\", " +
                         "t.is_Have_Resume as \"isHaveResume\", " +
                         "t.remarks, " +
                         "t.b_Id as \"bId\", " +
                         "t.d_Id as \"dId\", " +
                         "b.b_name as \"bName\", " +
                         "b.shortName as \"bShortName\", " +
                         "d.d_name as \"dName\", " +
                         "d.shortName as \"dShortName\", " +
                         "psdw.orgname as \"holdOrgName\", " +
                         "zpdw.orgname as \"usedOrgName\", " +
                         
                         //新增需求字段
                         "t.SCRAP_REASON as \"scrapReason\", " +
                         "t.status as \"status\", " +
                         "t.ORDER_NUMBER as \"orderNumber\", " +
                         "t.ATTACHMENT_TIME as \"attachmentTime\", " +
                         "t.RE_ATTACHMENT_TIME as \"reAttachmentTime\", " +
                         "t.SCRAP_TIME as \"scrapTime\", " +
                         "t.USED_ID as \"useDId\", " +
                         "d1.d_name as \"useDName\", " +
                         "t.RE_ATTACHMENT_DEPORT_ID as \"reAttachmentDeportId\", " +
                         "d2.d_name as \"reAttachmentDeportName\", " +
                         "ypsdw.orgname as \"oldHoldOrgName\" ";
        String fromSql = " from Jczl_Train t , OM_ORGANIZATION psdw, OM_ORGANIZATION zpdw, OM_ORGANIZATION ypsdw, j_jcgy_train_use u , j_gyjc_bureau b, J_GYJC_DEPORT d,J_GYJC_DEPORT d1,J_GYJC_DEPORT d2 " +
                        " where 1=1 and t.hold_OrgId = psdw.orgid(+) and t.used_OrgId = zpdw.orgid(+) and t.old_Hold_OrgId = ypsdw.orgid(+) " +
                        " and t.b_id = b.b_id(+) and t.d_id = d.d_id(+)" +
                        " and t.USED_ID = d1.d_id(+) and t.RE_ATTACHMENT_DEPORT_ID = d2.d_id(+)" +
                        " and t.train_Use = u.t_use_id(+) and t.record_status='0'" /*+
                        " and t.asset_state <> " + JczlTrain.TRAIN_ASSET_STATE_SCRAP*/;
        StringBuffer sqlWhere =  new StringBuffer("   order by t.update_Time DESC");
        String totalSql = "select count(t.idx) " + fromSql + sqlWhere;
        String sql =querySql+ fromSql + sqlWhere;
        return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    }
    /**
     * 
     * <li>说明：查询车号列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param start 起始页
     * @param limit 每页限制条数
     * @return 车号列表分页对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public Page queryTrainNoByTrainTypeList(String trainTypeIdx,String trainNo, int start, int limit) throws Exception {
    	OmOrganization org = OmOrganizationSelectManager.getOrgByOrgcode(JXSystemProperties.OVERSEA_ORGCODE);
        Map map = page(trainNo,JSONUtil.read("{\"trainTypeIDX\":\"" + trainTypeIdx+"\"}", Map.class), start, limit,"", YES,YES, org.getOrgseq(),"false");
        List<TrainNoBean> list = new ArrayList<TrainNoBean>();
        list = BeanUtils.copyListToList(TrainNoBean.class, (List)map.get("root"));
        return new Page((Integer)(map.get("totalProperty")),list);
    }
    
    /**
     * <li>说明：根据车型车号查询机车配属信息
     * <li>创建人：何涛
     * <li>创建日期：2015-07-07
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @return JczlTrain 机车实体对象
     */
    public JczlTrain getModel(String trainTypeIDX, String trainNo) {
        StringBuilder sb = new StringBuilder();

        sb.append("Select new JczlTrain(a.trainNo, a.makeFactoryIDX, a.makeFactoryName, a.leaveDate, a.buildUpTypeIDX, a.buildUpTypeCode, a.buildUpTypeName, a.trainUse, b.useName, a.bId, a.dId, c.bName, d.dName, c.shortName, d.shortName)");
        sb.append(" From JczlTrain a, TrainUse b, JgyjcBureau c, JgyjcDeport d");
        sb.append(" Where a.trainUse = b.useID And a.bId = c.bId And a.dId = d.dId");
        
        if(!StringUtil.isNullOrBlank(trainTypeIDX)){
            sb.append(" and a.trainTypeIDX = '").append(trainTypeIDX).append("'");
        }
        if(!StringUtil.isNullOrBlank(trainNo)){
            sb.append(" and a.trainNo = '").append(trainNo).append("'");
        }
        return (JczlTrain)this.daoUtils.findSingle(sb.toString());
    }
    
    /**
     * <li>方法说明：查询机车列表
     * <li>方法名：findTrainByTrain
     * @param trainTypes 车型s
     * @param trainNos 车号s
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015-11-3
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
	public List<JczlTrain> findTrainByTrain(String trainTypes, String trainNos) throws JsonParseException, JsonMappingException, IOException{
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"trainTypeIDX\":\"$[IN]$").append(trainTypes);
        sb.append("\",\"trainNo\":\"$[IN]$").append(trainNos).append("\"}");
    	Page<JczlTrain> page = findTrainByTrains(sb.toString(), 0, 100, null);
    	
    	/*StringBuilder s = new StringBuilder("from JczlTrain where trainTypeIDX in (");
    	s.append(trainTypes).append(") and trainNo in(").append(trainNos).append(")");
    	return daoUtils.find(s.toString());*/
    	return page.getList();
    }

	/**
     * <li>说明：获取所有机车信息
     * <li>创建人：林欢
     * <li>创建日期：2016-6-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityJson 传入参数
     * @param start 开始页
     * @param limit 总条数
     * @return Page 机车信息分页
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<JczlTrain> findJczlTrainInfoList(String entityJson, Integer start, Integer limit, Order[] orders) throws SecurityException, NoSuchFieldException, JsonParseException, JsonMappingException, IOException {
		JSONObject ob = JSONObject.parseObject(entityJson);
        
        //获取数据
        String trainNo = ob.getString("trainNo");//车号查询条件
        String trainTypeIDX = ob.getString("trainTypeIDX");//车型查询条件
        String idxReq = ob.getString("idx");
        String idx;
        if (StringUtil.isNullOrBlank(idxReq)) {
            idx = "";
        }else {
            idx = JSONUtil.read(ob.getString("idx"), String[].class)[0];//普查整治项idx
        }
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" from JczlTrain a where a.recordStatus = 0 ");
		sb.append(" and (a.trainNo,a.trainTypeShortName) not in ( ");
		sb.append(" select b.trainNo,b.trainTypeShortName from ZbglPczzItemToTraininfo b where b.zbglPczzItemIDX = '").append(idx).append("'");
		sb.append(" ) ");
		
//		过滤车号条件
		if (StringUtils.isNotBlank(trainNo)) {
			sb.append(" and a.trainNo like '%").append(trainNo).append("%'");
		}
//		过滤车型条件
		if (StringUtils.isNotBlank(trainTypeIDX)) {
			sb.append(" and a.trainTypeIDX = '").append(trainTypeIDX).append("'");
		}
		
		return (List<JczlTrain>) this.find(sb.toString());
		
		/*
		sb.append(" select a.* from JCZL_TRAIN a where a.record_status = 0 ");
//		where (a.train_no,a.train_type_shortname) not in (
//		sb.append(" select b.train_no,b.train_type_shortname from ZB_ZBGL_PCZZ_ITEM_TO_TRAININFO b where b.zbgl_pczz_item_idx = '").append(idx).append("'");
//		sb.append(" ) ");
		
		//过滤车号条件
		if (StringUtils.isNotBlank(trainNo)) {
			sb.append(" and a.train_No like '%").append(trainNo).append("%'");
		}
        
        // 排序处理
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = JczlTrain.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY ").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY ").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" ORDER BY update_time");
        }
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
        
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, JczlTrain.class);
        */
	}

    /**
     * <li>说明：根据配属段获取所有机车信息
     * <li>创建人：林欢
     * <li>创建日期：2016-6-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws Exception
     */
    public Page findJczlTrainInfoPageListByholdOrg(SearchEntity<JczlTrain> searchEntity,String flag) throws SecurityException, NoSuchFieldException {
        
        //传递数据 -1,1,0 0==本段 1==非本段
        String[] flagArray = flag.split(",");
        //配置文件中的本段配属段编码
        String jxConfigDID = JXConfig.getInstance().getDid();
        
        JczlTrain entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder();
        
        sb.append(" select a.idx,a.train_no,b.d_name ");
        sb.append(" from JCZL_TRAIN a,j_gyjc_deport b ");
        sb.append(" where a.train_no not in (select a1.train_no ");
        sb.append(" from zb_zbfw_train_center a1 ");
        sb.append(" where a1.train_type_idx = '").append(entity.getTrainTypeIDX()).append("'");
        
        //判断是否传递id过来
        if (StringUtils.isNotBlank(entity.getIdx())) {
            sb.append(" and a1.idx = '").append(entity.getIdx()).append("'");
        }
        sb.append(" ) and a.record_status = 0 ");
        
        sb.append(" and a.d_id = b.d_id(+) ");
        
        sb.append(" and a.train_type_idx = '").append(entity.getTrainTypeIDX()).append("'");
        //判断车号是否传递
        if (StringUtils.isNotBlank(entity.getTrainNo())) {
            sb.append(" and a.Train_No like '%").append(entity.getTrainNo()).append("%'");
        }
        
        //判断本段查新条件 
        //当长度为2的时候，说明传递过来的是 -1,0 或者-1,1
        if (flagArray.length == 2) {
            if ("0".equals(flagArray[1])) {
                sb.append(" and a.d_id = '").append(jxConfigDID).append("'");
            }else {
                sb.append(" and (a.d_id != '").append(jxConfigDID).append("'");
                sb.append(" or a.d_id is null )");
            }
        }
        
        // 排序处理
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = PartsRdpRecord.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY a.").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" ORDER BY a.idx");
        }
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, JczlTrainDTO.class);
    }

    /**
     * <li>说明：根据配属段获取机车配属段
     * <li>创建人：林欢
     * <li>创建日期：2016-7-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws Exception
     */
    public String getJczlTrainDIDByTrainNoAndTrainTpyeIDX(String trainNo, String trainTypeIDX) {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append(" from JczlTrain t ");
        sb.append(" where t.trainNo = '").append(trainNo).append("'");
        sb.append(" and t.trainTypeIDX = '").append(trainTypeIDX).append("'");
        
        JczlTrain jczlTrain = this.findSingle(sb.toString());
        if (jczlTrain == null) {
            return "1";
        }
        return jczlTrain.getDId();
        
    }

	/**
	 * <li>说明：查询未编组车辆列表
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchJson 查询参数
	 * @param start 起始页
	 * @param limit 
	 * @param orders 顺序
	 * @return 分页数据
	 */
	public Page findjczlTrainList(String searchJson,int start,int limit, Order[] orders) {
		JSONObject ob = JSONObject.parseObject(searchJson);
        //获取数据
        String trainNo = ob.getString("trainNo");//车号查询条件
        String trainTypeShortName = ob.getString("trainTypeShortName");//车型查询条件
        String vehicleKindCode = ob.getString("vehicleKindCode");//车型查询条件
        String vehicleType = ob.getString("vehicleType");// 客货类型 10 货车 20 客车
	    StringBuilder sb = new StringBuilder();
        sb.append(" select a.*, b.t_vehicle_kind_code, b.t_vehicle_kind_name ");
        sb.append(" from JCZL_TRAIN a, J_JCGY_VEHICLE_TYPE b  ");
        sb.append(" where a.train_type_idx = b.idx and a.record_status = 0 and b.record_status = 0 ");
        sb.append(" and not exists (select c.train_no, c.train_type_idx from K_MARSHALLING_TRAIN c where a.train_no = c.train_no and a.train_type_idx = c.train_type_idx and c.record_status = 0) ");
        
        //判断车号是否传递
        if (StringUtils.isNotBlank(trainNo)) {
            sb.append(" and a.Train_No like '%").append(trainNo).append("%'");
        }
        //判断车型是否传递
        if (StringUtils.isNotBlank(trainTypeShortName)) {
        	sb.append(" and  a.train_type_shortname like '%").append(trainTypeShortName).append("%'");
        }
        //判断车种是否传递
        if (StringUtils.isNotBlank(vehicleKindCode)) {
        	sb.append(" and  b.t_vehicle_kind_code like '%").append(vehicleKindCode).append("%'");
        }
        //判断类型是否传递
        if (StringUtils.isNotBlank(vehicleType)) {
            sb.append(" and  a.T_VEHICLE_TYPE = '").append(vehicleType).append("'");
        }
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
	     String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
		 return this.queryPageList(totalSql, sb.toString(), start,limit, false, JczlTrainBean.class);
	}
    
    /**
     * <li>说明：通过车型车号查询机车（货运车辆查询）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx
     * @param trainNo
     * @return
     */
    public JczlTrain getHYJczlTrainByTypeAndNo(String trainTypeIdx,String trainNo){
        StringBuffer hql = new StringBuffer(" From JczlTrain where recordStatus = 0 and vehicleType = '10' and trainTypeIDX = ? and trainNo = ? ");
        return (JczlTrain)this.daoUtils.findSingle(hql.toString(), new Object[]{trainTypeIdx,trainNo});
    }
    
    /**
     * <li>说明：设置货车状态
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型ID
     * @param trainNo 车号
     * @param trainState 设置状态 
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void setHYJczlTrainState(String trainTypeIdx,String trainNo,int trainState) throws BusinessException, NoSuchFieldException{
        JczlTrain jczlTrain = getHYJczlTrainByTypeAndNo(trainTypeIdx, trainNo);
        if(jczlTrain != null){
            jczlTrain.setTrainState(trainState);
            this.saveOrUpdate(jczlTrain);
        }
    }

	/**
	 * <li>说明：车种下拉树
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param vehicleType 车辆类型
	 * @return 下拉树
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>>  findVehicleKindTree(String vehicleType) {
	    StringBuilder sb = new StringBuilder();
        sb.append(" select distinct b.t_vehicle_kind_code, b.t_vehicle_kind_name from JCZL_TRAIN a, J_JCGY_VEHICLE_TYPE b  ");
        sb.append(" where a.train_type_idx = b.idx and a.record_status = 0 and b.record_status = 0 ");
        sb.append(" and not exists (select c.train_no, c.train_type_idx from K_MARSHALLING_TRAIN c where a.train_no = c.train_no and a.train_type_idx = c.train_type_idx and c.record_status = 0) ");
        //判断类型是否传递
        if (StringUtils.isNotBlank(vehicleType)) {
            sb.append(" and  a.T_VEHICLE_TYPE = '").append(vehicleType).append("'");
        }
        boolean isLeaf = true;
	    List<Object[]> vehicleKindList = daoUtils.executeSqlQuery(sb.toString());
	    
	    List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
	    
        for (Object[] vehicleKind : vehicleKindList) {           
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id",  vehicleKind[0]);
            nodeMap.put("text", vehicleKind[1]);
            nodeMap.put("leaf", isLeaf);
            children.add(nodeMap);
        }
	    return children;
	}
    
    /**
     * <li>说明：通过车型车号获取车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param trainNo 车号
     * @return
     */
    public JczlTrain getJczlTrainByTypeAndNo(String trainTypeIdx,String trainNo){
        StringBuffer hql = new StringBuffer(" From JczlTrain where recordStatus = 0 and assetState = 10 and trainTypeIDX = ? and trainNo = ? ");
        return (JczlTrain)this.daoUtils.findSingle(hql.toString(), new Object[]{trainTypeIdx,trainNo});
    }
    
    /**
     * <li>说明：通过车号查询车辆（货车）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainNo
     * @return
     */
    public JczlTrain getHCJczlTrainByNo(String trainNo){
        StringBuffer hql = new StringBuffer(" From JczlTrain where recordStatus = 0 and assetState = 10 and vehicleType = '10' and trainNo = ? ");
        return (JczlTrain)this.daoUtils.findSingle(hql.toString(), new Object[]{trainNo});
    }
    
    /**
     * <li>说明：通过车号查询车辆（客车）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainNo
     * @return
     */
    public JczlTrain getKCJczlTrainByNo(String trainNo){
        StringBuffer hql = new StringBuffer(" From JczlTrain where recordStatus = 0 and assetState = 10 and vehicleType = '20' and trainNo = ? ");
        return (JczlTrain)this.daoUtils.findSingle(hql.toString(), new Object[]{trainNo});
    }
}