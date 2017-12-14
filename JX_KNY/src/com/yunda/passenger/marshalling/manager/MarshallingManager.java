package com.yunda.passenger.marshalling.manager;

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
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
import com.yunda.passenger.marshalling.entity.Marshalling;
import com.yunda.passenger.marshalling.entity.MarshallingTrain;
import com.yunda.passenger.routing.entity.Routing;
import com.yunda.passenger.routing.manager.RoutingManager;
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

}
