package com.yunda.frame.baseapp.message.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.message.entity.MsgCfgFunction;
import com.yunda.frame.baseapp.message.entity.MsgCfgReceive;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MsgCfgFunction业务类,消息服务配置-功能点定义
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="msgCfgFunctionManager")
public class MsgCfgFunctionManager extends JXBaseManager<MsgCfgFunction, MsgCfgFunction>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/** 消息服务配置-接收方定义 业务类 */
	private MsgCfgReceiveManager msgCfgReceiveManager;
	
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
	 * <li>说明：根据功能点编码（funCode）查找返回所有要通知的人员
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-20
	 * <li>修改内容：利用查询缓存方法
	 * @param String funCode 功能点编码
	 * @return List<OmEmployee>
	 * @throws 抛出异常列表
	 */
	public Set<OmEmployee> getEmployeeByFunCode(String funCode){
		MsgCfgFunction fun = findByFunCode(funCode);
		if (fun == null) return null;
		return msgCfgReceiveManager.getEmployeeByFunidx(fun.getIdx());
	}
	
	/**
	 * <li>说明：根据功能点编码（funCode）查找返回消息配置功能点对象
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-12-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param funCode 功能点编码
	 * @return 消息配置功能点对象
	 */
	public MsgCfgFunction findByFunCode(String funCode){
		MsgCfgFunction fun = new MsgCfgFunction();
		fun.setFunCode(funCode);
		List list =  super.findList(fun, null, null);
		if(list == null || list.size() < 1)	return null;
		return (MsgCfgFunction)list.get(0);
	}
	/**
	 * <li>说明：根据功能点名称（funName）查找返回所有要通知的机构
	 * <li>创建人：easy
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-20
	 * <li>修改内容：利用查询缓存方法
	 * @param String funCode 功能点编码
	 * @return List<OmEmployee>
	 * @throws 抛出异常列表
	 */
	public List<MsgCfgReceive> getMsgCfgReceiveByFunNameAndSiteID(String funName,String siteID){
		MsgCfgFunction fun = new MsgCfgFunction();
		fun.setFunName(funName);
		List list =  super.findList(fun, null, null);
//		List list = daoUtils.getHibernateTemplate().findByExample(fun);
		if(list == null || list.size() < 1)	return null;
		fun = (MsgCfgFunction)list.get(0);
		return msgCfgReceiveManager.findByFunidxAndSiteID(fun.getIdx(),siteID);
	}
	
	/**
	 * <li>说明：根据idx主键物理批量删除，同时级联删除 消息服务配置-接收方定义
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-06-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Serializable... ids 实体对象的idx主键数组
	 * @return 
	 * @throws BusinessException
	 */
	@SuppressWarnings("all")
	public void deleteByIds(Serializable... ids) throws BusinessException {
		List<String> idxList = new ArrayList<String>();
		for (Serializable id : ids) {
			List<MsgCfgReceive> list = msgCfgReceiveManager.findByFunidx(id.toString());
			if(list == null || list.size() < 1)	continue;
			for (MsgCfgReceive receive : list) {
				idxList.add(receive.getIdx());
			}
		}
		if(idxList.size() > 0)	msgCfgReceiveManager.deleteByIds(idxList.toArray(new String[ idxList.size() ]));
		super.deleteByIds(ids);
	}
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * 规则：1.功能点编码必须唯一。
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-06-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(MsgCfgFunction entity) throws BusinessException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("funCode", entity.getFunCode());
		String idx = StringUtil.nvlTrim(entity.getIdx(), null);
		if(idx == null)	params.put("idx", "");
		else	params.put("idx", " and idx != '" + idx + "'");
		String hql = SqlMapUtil.getSql("baseapp:MsgCfgFunctionManager.uniqueFunCode", params);
		int count = daoUtils.getCount(hql);
		if(count > 0)	return new String[]{"功能点编码必须唯一"};
		return null;
	}
	public MsgCfgReceiveManager getMsgCfgReceiveManager() {
		return msgCfgReceiveManager;
	}
	public void setMsgCfgReceiveManager(MsgCfgReceiveManager msgCfgReceiveManager) {
		this.msgCfgReceiveManager = msgCfgReceiveManager;
	}
}