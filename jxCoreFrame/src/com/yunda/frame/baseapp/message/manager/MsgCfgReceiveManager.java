package com.yunda.frame.baseapp.message.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.baseapp.message.entity.MsgCfgReceive;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MsgCfgReceive业务类,消息服务配置-接收方定义
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="msgCfgReceiveManager")
public class MsgCfgReceiveManager extends JXBaseManager<MsgCfgReceive, MsgCfgReceive>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/** 依赖注入：人员组件实例对象 */
	@Resource
	private IOmEmployeeManager omEmployeeManager;
	/** 依赖注入：组织机构组件实例对象 */
	@Resource
	private IOmOrganizationManager omOrganizationManager;	
	
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}	
	/**
	 * <li>说明：根据接收方定义返回所有要通知的人员
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-20
	 * <li>修改内容：使用统一的人员组织机构接口
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Set<OmEmployee> getEmployee(List<MsgCfgReceive> receiveList){
		if(receiveList == null || receiveList.size() < 1)	return null;
		Set<OmEmployee> empSet = new HashSet<OmEmployee>();
		Set<Long> orgidSet = new HashSet<Long>();
		for (MsgCfgReceive receive : receiveList) {
			int type = receive.getType();
			String receiveid = receive.getReceiverID();
			switch (type) {
			case Constants.EMP:
				OmEmployee emp = omEmployeeManager.getModelById(Long.parseLong(receiveid));
				if(emp != null)	empSet.add(emp);
				break;
			case Constants.ORG:
				List<OmOrganization> orgList = omOrganizationManager.findAllChilds(Long.parseLong(receiveid), true);
				if(orgList != null && orgList.size() > 0){
					for (OmOrganization orgn : orgList) {
						orgidSet.add(orgn.getOrgid());
					}
				}
				break;
			case Constants.GROUP:
			case Constants.POSITION:
			case Constants.DUTY:
			case Constants.ROLE:
			default:
				break;
			}			
		}
//		获取组织机构及子机构下所有人员
		if(orgidSet.size() > 0){
			Long[] orgidAry = new Long[ orgidSet.size() ];
			orgidSet.toArray(orgidAry);
			List<OmEmployee> empList = omEmployeeManager.findByOrgIds(orgidAry);
			for (OmEmployee emp : empList) {
				empSet.add(emp);
			}
		}

		return empSet;
	}
	/**
	 * <li>说明：根据接收方定义返回所有要通知的人员
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Set<OmEmployee> getEmployee(MsgCfgReceive receive){
		if(receive == null)		return null;
		List<MsgCfgReceive> list = new ArrayList<MsgCfgReceive>();
		list.add(receive);
		return getEmployee(list);
	}
	/**
	 * <li>说明：根据功能点定义主键（funidx）查找返回所有要通知的人员
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String funidx 功能点编码主键
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Set<OmEmployee> getEmployeeByFunidx(String funidx){
		return getEmployee(findByFunidx(funidx));
	}
	/**
	 * <li>说明：根据功能点定义主键（funidx）查找返回记录
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-20
	 * <li>修改内容：利用查询缓存方法
	 * @param String funidx 功能点编码主键
	 * @return List<MsgCfgReceive>
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List<MsgCfgReceive> findByFunidx(String funidx){
		MsgCfgReceive receive = new MsgCfgReceive();
		receive.setFunidx(funidx);
		return super.findList(receive, null, null);
	}
	
	
	/**
	 * <li>说明：根据功能点定义主键（funidx） 站点id SITEID查找返回记录
	 * <li>创建人：easy
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-20
	 * <li>修改内容：利用查询缓存方法
	 * @param String funidx 功能点编码主键
	 * @return List<MsgCfgReceive>
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List<MsgCfgReceive> findByFunidxAndSiteID(String funidx,String siteID){
		MsgCfgReceive receive = new MsgCfgReceive();
		receive.setFunidx(funidx);
		receive.setWorkplaceCode(siteID);
		return super.findList(receive, null, null);
	}
}