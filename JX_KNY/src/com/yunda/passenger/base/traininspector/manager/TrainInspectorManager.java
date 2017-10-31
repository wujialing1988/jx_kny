package com.yunda.passenger.base.traininspector.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.passenger.base.traininspector.entity.TrainInspector;
import com.yunda.passenger.routing.entity.Routing;
import com.yunda.passenger.routing.manager.RoutingManager;
import com.yunda.passenger.traindemand.manager.TrainInspectorDemandManager;
/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Service("trainInspectorManager")
public class TrainInspectorManager extends JXBaseManager<TrainInspector, TrainInspector> implements IbaseCombo {
	 @Resource
     private RoutingManager routingManager;
	 @Resource
	 private TrainInspectorDemandManager trainInspectorDemandManager;

	 /**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param empList
	 * @return
	 * @throws BusinessException
	 */
	public String[] validateUpdateList(List<TrainInspector> empList) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        for(TrainInspector entity : empList){
            if( "".equals(entity.getIdx()) && entity.getEmpid() != null ){ //新增时验证
                String hql = "Select count(*) From TrainInspector where empId ='"+
                entity.getEmpid()+"' and orgid='"+entity.getOrgid()+"' and recordStatus=0";
                int count = this.daoUtils.getCount(hql);
                if(count > 0){
                    errMsg.add("人员【"+ entity.getEmpname() +"】在此机构中已存在！");
                }
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
	 * <li>说明：分页查询，返回实体类的分页列表对象，基于单表实体类分页查询
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity
	 * @return
	 * @throws BusinessException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public Page<TrainInspector> findInspectorList(SearchEntity<TrainInspector> searchEntity) throws BusinessException, ParseException{
		TrainInspector trainInspector = searchEntity.getEntity();
        StringBuffer hql = new StringBuffer(" select t from TrainInspector t, OmOrganization OX  where t.recordStatus = 0 and OX.orgid = t.orgid AND OX.status = 'running' ") ;
        // 查询参数
        String routingIdx = trainInspector.getRoutingIdx();
        String runningDate = trainInspector.getRunningDate();
        String empName = trainInspector.getEmpname();
        String orgseq = trainInspector.getOrgseq();
        // 根据交路信息及时间过滤已经排过班的人员
        if(!StringUtil.isNullOrBlank(routingIdx) && !StringUtil.isNullOrBlank(runningDate) ){
            // 查询交路信息
	        Routing  routing =  routingManager.getModelById(routingIdx);
	        //	  将字符串转化成日期
		    Date runningDateF = DateUtil.yyyy_MM_dd.parse(runningDate);
		    runningDate = DateUtil.yyyy_MM_dd.format(runningDateF);
	        // 获取整个行程开始及结束时间
	        Date startDate = DateUtil.yyyy_MM_dd_HH_mm.parse(runningDate+ " " + routing.getDepartureTime());
	        Date endDate = new Date((long) (startDate.getTime() + routing.getDuration()*1000*60));
	        String startDateStr = DateUtil.yyyy_MM_dd_HH_mm.format(startDate);
	        String endDateStr = DateUtil.yyyy_MM_dd_HH_mm.format(endDate);
	        hql.append(" and t.empid  not in( select b.empid from TrainInspectorDemand b where  b.backDate > to_date('");
	        hql.append(startDateStr).append("','yyyy-mm-dd hh24:mi') and b.runningDate < to_date('").append(startDateStr).append("','yyyy-mm-dd hh24:mi'");
	        hql.append(") or ( b.backDate > to_date('")
	        .append(endDateStr).append("','yyyy-mm-dd hh24:mi') and b.runningDate < to_date('").append(endDateStr).append("','yyyy-mm-dd hh24:mi')))");
        }
        if(!StringUtil.isNullOrBlank(empName)){
        	hql.append(" and empname like '%"+empName+"%' ");
        }
        if (!StringUtil.isNullOrBlank(orgseq)) {
        	hql.append(" AND OX.orgseq like '").append(orgseq).append("%'");
        }
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*) ");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by t.empname");
        hql.append(" order by t.empname");
        Page<TrainInspector> page  = findPageList(totalHql.toString(), hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
        List<TrainInspector>  trainInspectorList = page.getList();
        // 查询人员劳时
        if(null != trainInspectorList && trainInspectorList.size()>0 && !StringUtil.isNullOrBlank(runningDate)){
            Date runningDateF = DateUtil.yyyy_MM_dd.parse(runningDate);
   		    runningDate = DateUtil.yyyy_MM_dd.format(runningDateF);
   		    // 查询当月的劳时
   		    String month = runningDate.substring(0, 7);
	        for(TrainInspector temp: trainInspectorList){
	        	List list =	trainInspectorDemandManager.findInspectorList(month, temp.getEmpid());
	        	if(null == list || list.size() <=0) break;
                Object[] objs = (Object[]) list.get(0);
                temp.setWorkHours(Double.valueOf(objs[2].toString()));
	        }
        }
        return page;
    }
	
}
