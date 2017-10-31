package com.yunda.passenger.marshalling.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.passenger.marshalling.entity.MarshallingTrainCountView;
/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 编组车辆信息视图
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Service("marshallingTrainCountViewManager")
public class MarshallingTrainCountViewManager extends JXBaseManager<MarshallingTrainCountView, MarshallingTrainCountView> {

	/**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param marshallingCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getMarshallingTrainCountStr(String marshallingCode) {
		 // 初始值 为 编组编号
		 String str = marshallingCode;
		 StringBuffer hql = new StringBuffer(" select t from MarshallingTrainCountView t where t.marshallingCode = ? ") ;
		 List<MarshallingTrainCountView> entityList = (List<MarshallingTrainCountView>) this.daoUtils.find(hql.toString(), new Object[]{marshallingCode}); 
		 if(null == entityList|| entityList.size()<=0)  return str;
		 str += "(";
		 for(MarshallingTrainCountView entity : entityList){
			 str += entity.getVehicleKindNameCount();
		 }
		 str += ")";
		 return str;	 
	}



}

