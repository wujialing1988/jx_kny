package com.yunda.passenger.marshalling.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.scdd.repairplan.manager.RepairWarningKCManager;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
import com.yunda.passenger.marshalling.entity.Marshalling;
import com.yunda.passenger.marshalling.entity.MarshallingTrain;
import com.yunda.passenger.routing.entity.Routing;
import com.yunda.passenger.routing.manager.RoutingManager;
import com.yunda.passenger.traindemand.manager.TrainDemandManager;
/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 编组基础信息业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Service("marshallingManager")
public class MarshallingManager extends JXBaseManager<Marshalling, Marshalling> implements IbaseCombo {
	 /** CodeRuleConfig业务类,业务编码规则配置 */
    @Resource
    private CodeRuleConfigManager codeRuleConfigManager;
    @Resource
    private MarshallingTrainManager marshallingTrainManager;
    @Resource
    private MarshallingTrainCountViewManager marshallingTrainCountViewManager;
    @Resource
    private RoutingManager routingManager;
    @Resource
    private RepairWarningKCManager repairWarningKCManager;    
    
    @Resource
    private TrainDemandManager trainDemandManager ;
    
   // 业务字典配置的 编组编号生产规则 名称
    private static final String K_P_MARSHALLING_CODE = "K_P_MARSHALLING_CODE";
    
	/**
	 * <li>说明：保存时添加业务编码规则
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 编组基础信息实体类
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@Override
	public void saveOrUpdate(Marshalling entity) throws BusinessException, NoSuchFieldException {
		int trainCount = marshallingTrainManager.findTrainCountByCode(entity.getMarshallingCode());
		if(!StringUtil.isNullOrBlank(entity.getIdx()) && trainCount > entity.getTrainCount()){
			throw new BusinessException("额定总数小于已编组的车辆数！");
		}
		 // 根据业务编码规则自动生成“编组号”
		if(StringUtil.isNullOrBlank(entity.getMarshallingCode())){
			entity.setMarshallingCode(this.codeRuleConfigManager.makeConfigRule(K_P_MARSHALLING_CODE));
		}
//		entity.setTrainCount(trainCount);
		super.saveOrUpdate(entity);
	 }
	
	/**
	 * <li>说明： 分布查询
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 编组基础信息实体类
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@Override
	public Page<Marshalling> findPageList(SearchEntity<Marshalling> searchEntity) throws BusinessException{
		Page<Marshalling> page =  super.findPageList(searchEntity);
		List<Marshalling> MarshallingList = page.getList();
		for(Marshalling entity: MarshallingList){
			entity.setMarshallingTrainList(marshallingTrainManager.findTrainListByCode(entity.getMarshallingCode()));
		}
		return page;
	}
	/**
	 * <li>说明：编组下拉控件
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param req 
	 * @param start
	 * @param limit
	 */
    @SuppressWarnings("unchecked")
	@Override
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        StringBuffer hql = new StringBuffer(" select t from Marshalling t where t.recordStatus = 0") ;
        // 查询参数
        String routingIdx = String.valueOf(queryParamsMap.get("routingIdx"));
        String marshallingCode = String.valueOf(queryParamsMap.get("marshallingCode"));
        String runningDate = String.valueOf(queryParamsMap.get("runningDate"));
        if(!StringUtil.isNullOrBlank(routingIdx) && !StringUtil.isNullOrBlank(runningDate) ){
   
	        Routing  routing =  routingManager.getModelById(routingIdx);
	        // 将字符串转化成日期
	        Date runningDateF = DateUtil.yyyy_MM_dd.parse(runningDate);
	        // 取日期的年月日
	        runningDate = DateUtil.yyyy_MM_dd.format(runningDateF);
	        // 获取整个行程开始及结束时间
	        Date startDate = DateUtil.yyyy_MM_dd_HH_mm.parse(runningDate+ " " + routing.getDepartureTime());
	        Date endDate = new Date((long) (startDate.getTime() + routing.getDuration()*1000*60 *2));
	        String startDateStr = DateUtil.yyyy_MM_dd_HH_mm.format(startDate);
	        String endDateStr = DateUtil.yyyy_MM_dd_HH_mm.format(endDate);
	        hql.append(" and t.marshallingCode  not in( select b.marshallingCode from TrainDemand b where  (b.runningDate + numtodsinterval(b.duration*2 ,'minute')) > to_date('");
	        hql.append(startDateStr).append("','yyyy-mm-dd hh24:mi') and b.runningDate < to_date('").append(startDateStr).append("','yyyy-mm-dd hh24:mi'");
	        hql.append(") or (( b.runningDate + numtodsinterval(b.duration*2 ,'minute')) > to_date('")
	        .append(endDateStr).append("','yyyy-mm-dd hh24:mi') and b.runningDate < to_date('").append(endDateStr).append("','yyyy-mm-dd hh24:mi')))");
        }
        if(!StringUtil.isNullOrBlank(marshallingCode)){
        	hql.append(" and marshallingCode = '"+marshallingCode+"'");
        }
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*) ");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by t.marshallingCode");
        hql.append(" order by t.marshallingCode");
        Page<Marshalling> page  = findPageList(totalHql.toString(), hql.toString(), start, limit);
        List<Marshalling> entityList = page.getList();
        if(entityList.size()<=0 || null == entityList) page.extjsStore();
        for(Marshalling entity: entityList){
        	String marshallingTrainCountStr = marshallingTrainCountViewManager.getMarshallingTrainCountStr(entity.getMarshallingCode());
        	entity.setMarshallingTrainCountStr(marshallingTrainCountStr);
        }
        return page.extjsStore();
    }
    
    
	/**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param marshalling
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void updateMarshalling(Marshalling marshalling) throws BusinessException, NoSuchFieldException {
		Marshalling oldMarshalling = this.getModelById(marshalling.getIdx());
		// 判断是新增还是删除
		if(marshalling.getTrainCount()> oldMarshalling.getTrainCount())
			oldMarshalling.setTrainCount(oldMarshalling.getTrainCount()+1);
		else {
			oldMarshalling.setTrainCount(oldMarshalling.getTrainCount()-1);
			List<MarshallingTrain> marshallingTrainList = marshallingTrainManager.findTrainByCodeAndSeqNo(oldMarshalling.getMarshallingCode(), marshalling.getTrainCount(),"=");
			if(null != marshallingTrainList && marshallingTrainList.size()>0){
				marshallingTrainManager.logicDelete(marshallingTrainList.get(0).getIdx());
			}else{
			    marshallingTrainManager.updateSort(marshallingTrainManager.findTrainByCodeAndSeqNo(oldMarshalling.getMarshallingCode(), marshalling.getTrainCount(),">"));
			}
		}
		super.saveOrUpdate(oldMarshalling);
		
	}
	
	
	/**
     * <li>说明：查询客车编组主表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param params 查询条件
     * @return
     */  
	 public List<Map<String, Object>> findMarshallingList(Map<String, String> params){
	        String sql = SqlMapUtil.getSql("kny-repairwarning:findMarshallingList");
	        List<Map<String, Object>> result = this.queryListMap(sql); 
	        List<Map<String, Object>> list = null ;
	        for (Map<String, Object> map : result) {
	        	list = new ArrayList<Map<String,Object>>();
	        	String marshallingCode = map.get("marshallingCode")+"";
	        	int trainCounts = Integer.parseInt(map.get("trainCount") + "") ;
	        	String demandIdx = map.get("demandIdx")+""; // 编组任务
	        	List<MarshallingTrain> trains = marshallingTrainManager.findTrainListByCode(marshallingCode);
	        	Map<String, Object> trainMap = null ;
	        	for (int i = 0; i < trainCounts; i++) {
	        		trainMap = new HashMap<String, Object>();
	        		MarshallingTrain train = getMarshallingTrainBySeqNo(i+1,trains);
	        		if(train != null){
	        			trainMap.put("trainNo", train.getTrainNo());
	        			trainMap.put("trainTypeIdx", train.getTrainTypeIDX());
	        			trainMap.put("trainTypeShortname", train.getTrainTypeShortName());
	        			trainMap.put("seqNo", train.getSeqNo());
	        			trainMap.put("status", "30");	// 10 运用中（正在运行的车辆） 20（预留车位）  30（空闲编组） 40（未编组车辆） 50（检修车辆）
	        			if(!StringUtil.isNullOrBlank(demandIdx)){
	        				trainMap.put("status", "10");
	        			}
	        			// 查询走行及修程数据
	        			List<Map<String, Object>> warninglist = repairWarningKCManager.findTrainRepairInfosForKC(train.getTrainTypeIDX(), train.getTrainNo(),false,false);
	        			if(warninglist != null){
	        				Map<String, Object> warning = warninglist.get(0);
	        				trainMap.put("a1km", warning.get("a1km")+"");
	        				trainMap.put("a2km", warning.get("a2km")+"");
	        				trainMap.put("a3km", warning.get("a3km")+"");
	        				trainMap.put("a4km", warning.get("a4km")+"");
	        				trainMap.put("a5km", warning.get("a5km")+"");
	        				
	        				trainMap.put("beforeA1Date", warning.get("beforeA1Date") == null ? "" : warning.get("beforeA1Date")+"");
	        				trainMap.put("beforeA2Date", warning.get("beforeA2Date") == null ? "" : warning.get("beforeA2Date")+"");
	        				trainMap.put("beforeA3Date", warning.get("beforeA3Date") == null ? "" : warning.get("beforeA3Date")+"");
	        				trainMap.put("beforeA4Date", warning.get("beforeA4Date") == null ? "" : warning.get("beforeA4Date")+"");
	        				trainMap.put("beforeA5Date", warning.get("beforeA5Date") == null ? "" : warning.get("beforeA5Date")+"");
	        				
	        				trainMap.put("nextA1Date", warning.get("nextA1Date") == null ? "" : warning.get("nextA1Date")+"");
	        				trainMap.put("nextA2Date", warning.get("nextA2Date") == null ? "" : warning.get("nextA2Date")+"");
	        				trainMap.put("nextA3Date", warning.get("nextA3Date") == null ? "" : warning.get("nextA3Date")+"");
	        				trainMap.put("nextA4Date", warning.get("nextA4Date") == null ? "" : warning.get("nextA4Date")+"");
	        				trainMap.put("nextA5Date", warning.get("nextA5Date") == null ? "" : warning.get("nextA5Date")+"");
	        				
	        			}
	        		}else{
	        			trainMap.put("trainNo", "-");
	        			trainMap.put("trainTypeIdx", "");
	        			trainMap.put("trainTypeShortname", "-");
	        			trainMap.put("seqNo", i+1);
	        			trainMap.put("status", "20");	// 欠编
	        		}
	        		list.add(trainMap);
				}
	        	map.put("trains", list);
	        }
	        return result;
	 }
	 
	 
	 	/**
	     * <li>说明：查询客车备用车列表
	     * <li>创建人：伍佳灵
	     * <li>创建日期：2017-12-20
	     * <li>修改人： 
	     * <li>修改日期：
	     * <li>修改内容：
	     * @param params 查询条件
	     * @return
	     */  
		 public List<Map<String, Object>> findNotMarshallingList(Map<String, String> params){
			 return repairWarningKCManager.findTrainRepairInfosForKC(null,null,true,false);
		 }
		 
	 	/**
	     * <li>说明：查询客车检修车车列表
	     * <li>创建人：伍佳灵
	     * <li>创建日期：2017-12-20
	     * <li>修改人： 
	     * <li>修改日期：
	     * <li>修改内容：
	     * @param params 查询条件
	     * @return
	     */  
		 public List<Map<String, Object>> findJxMarshallingList(Map<String, String> params){
			 return repairWarningKCManager.findTrainRepairInfosForKC(null,null,false,true);
		 }		 
	 
		/**
	     * <li>说明：通过序号查询车辆
	     * <li>创建人：伍佳灵
	     * <li>创建日期：2017-12-20
	     * <li>修改人： 
	     * <li>修改日期：
	     * <li>修改内容：
	     * @param seqNo 序号
	     * @param trains 车辆
	     * @return
	     */  
	 private MarshallingTrain getMarshallingTrainBySeqNo(int seqNo,List<MarshallingTrain> trains){
		 MarshallingTrain result = null ;
		 for (MarshallingTrain marshallingTrain : trains) {
			if(marshallingTrain.getSeqNo().intValue() == seqNo){
				result = marshallingTrain ;
				break ;
			}
		}
		 return result ;
	 }

}
