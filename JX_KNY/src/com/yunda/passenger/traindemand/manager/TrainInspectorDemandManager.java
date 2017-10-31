package com.yunda.passenger.traindemand.manager;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.passenger.base.traininspector.entity.TrainInspector;
import com.yunda.passenger.traindemand.entity.TrainDemand;
import com.yunda.passenger.traindemand.entity.TrainInspectorDemand;

/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 列车需求乘车列检员日志业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Service("trainInspectorDemandManager")
public class TrainInspectorDemandManager extends JXBaseManager<TrainInspectorDemand, TrainInspectorDemand>{

	/**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity
	 * @param isAddOrEdit 是新增还是编辑 true 新增， false 编辑
	 * @throws Exception
	 * @throws InvocationTargetException
	 */
	public void updateInspectorListByDemand(TrainDemand entity, boolean isAddOrEdit) throws Exception, InvocationTargetException {
		 // 如果是编辑则删除之前的数据
		  if(!isAddOrEdit){
			  String sql = "DELETE  K_TRAIN_INSPECTOR_DEMAND WHERE RECORD_STATUS = 0 AND TRAIN_DEMAND_IDX = '" + entity.getIdx() + "'";
		      daoUtils.executeSql(sql);
		  }
		  String trainInspectorID = entity.getTrainInspectorID();
		  String trainInspectorName = entity.getTrainInspectorName();
		  Date runningDate = entity.getRunningDate();
		  Double duration = entity.getDuration();
		  // 开始时间加上往返的时间间隔为结束时间
		  Date backDate = new Date( (long)(runningDate.getTime() + duration * 60 * 1000*2));
		  if(!StringUtil.isNullOrBlank(trainInspectorID)){
			  String[] empids = trainInspectorID.split(";");
			  String[] empnames = trainInspectorName.split(";");
			 // 编组车辆日志
			  List<TrainInspectorDemand>  inspectorList = new ArrayList<TrainInspectorDemand>();
			  for(int i=0; i< empids.length; i++){
				  TrainInspectorDemand trainInspectorDemand = new TrainInspectorDemand();
				  trainInspectorDemand.setEmpid(Long.valueOf(empids[i]));
				  trainInspectorDemand.setEmpname(empnames[i]);
				  trainInspectorDemand.setTrainDemandIdx(entity.getIdx());
				  trainInspectorDemand.setRunningDate(runningDate);
				  trainInspectorDemand.setDuration(entity.getDuration());
				  trainInspectorDemand.setBackDate(backDate);
				  trainInspectorDemand.setStrips(entity.getStrips());
				  trainInspectorDemand.setBackStrips(entity.getBackStrips());
				  trainInspectorDemand.setBackDate(backDate);
				  inspectorList.add(trainInspectorDemand);
			}
			this.saveOrUpdate(inspectorList);
		}
	}
	/**
	 * <li>说明：统计当月劳时
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param month
	 * @param empid
	 * @return
	 * @throws BusinessException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public  List findInspectorList(String month, Long empid) throws BusinessException, ParseException{
		  StringBuilder sql = new StringBuilder(" SELECT * From (SELECT  D.EMPID, D.EMPNAME, SUM(DURATION*2) AS WORK_HOUSRS  FROM K_TRAIN_INSPECTOR_DEMAND D WHERE D.RECORD_STATUS = 0 AND TO_CHAR(D.RUNNING_DATE,'yyyy-mm') = '");
		  sql.append(month).append("' group by D.EMPID, D.EMPNAME) where 1=1 ");
		  if(null != empid && 0!= empid){
			  sql.append("AND  EMPID = ").append(empid);
		  }
		  return daoUtils.executeSqlQuery(sql.toString());
	}
	/**
	 * <li>说明：统计当月劳时
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 查询参数
	 * @return 分页信息
	 * @throws BusinessException
	 * @throws ParseException
	 */
	@Override
	public Page<TrainInspectorDemand> findPageList(SearchEntity<TrainInspectorDemand> searchEntity) throws BusinessException{
		 TrainInspectorDemand entity = searchEntity.getEntity();
		 String month = entity.getMonth();
		 Page<TrainInspectorDemand>  page = super.findPageList(searchEntity);
		 List<TrainInspectorDemand> trainInspectorDemandlist = page.getList();
		 if(null != trainInspectorDemandlist && trainInspectorDemandlist.size()>0){
			 for(TrainInspectorDemand trainInspectorDemand: trainInspectorDemandlist){
				 try {
					List list = this.findInspectorList(month, trainInspectorDemand.getEmpid());
					if(null == list || list.size() <=0) break;
	                Object[] objs = (Object[]) list.get(0);
	                trainInspectorDemand.setWorkHours(Double.valueOf(objs[2].toString()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 }
		 return page;
	}
}
