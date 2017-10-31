package com.yunda.frame.baseapp.cache.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.yunda.frame.baseapp.cache.entity.CacheInfo;
import com.yunda.frame.common.JXBaseManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: hibernate缓存管理操作类
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-11-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="cacheManager")
public class CacheManager extends JXBaseManager<Object, Object>{
//	/** 日志工具 */
//	private Logger logger = Logger.getLogger(getClass().getName());	
	/** 缓存描述信息列表 */
	private static List<CacheInfo> infoList = new ArrayList<CacheInfo>();
	private static Map<String, CacheInfo> infoMap = new HashMap<String, CacheInfo>();
	
	static{
		//用户管理的缓存实体
		CacheInfo info = new CacheInfo("Ac_Menu", "系统菜单", com.yunda.frame.yhgl.entity.AcMenu.class, "否", "是", "V2.5.2"); 
		infoList.add(info);
		infoMap.put(info.getTableName(), info);
		info = new CacheInfo("Ac_Operator", "操作员", com.yunda.frame.yhgl.entity.AcOperator.class, "否", "是", "V2.5.2");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);
		info = new CacheInfo("Eos_Dict_Type", "业务字典分类", com.yunda.frame.yhgl.entity.EosDictType.class,	 "否", "是", "V2.5.2");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);		
		info = new CacheInfo("Eos_Dict_Entry", "业务字典项（明细）", com.yunda.frame.yhgl.entity.EosDictEntry.class, "否", "是", "V2.5.2");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);			
		info = new CacheInfo("Om_Duty", "职务", com.yunda.frame.yhgl.entity.OmDuty.class, "否", "是", "V2.5.2");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);		
		info = new CacheInfo("Om_Employee", "人员", com.yunda.frame.yhgl.entity.OmEmployee.class, "否", "是", "V2.5.2");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);		
		info = new CacheInfo("Om_Group", "工作组", com.yunda.frame.yhgl.entity.OmGroup.class, "否", "是", "V2.5.2");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);		
		info = new CacheInfo("Om_Organization", "机构", com.yunda.frame.yhgl.entity.OmOrganization.class, "否", "是", "V2.5.2");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);		
		info = new CacheInfo("Om_Position", "岗位", com.yunda.frame.yhgl.entity.OmPosition.class, "否", "是", "V2.5.2");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);
		
		
		//基本应用服务的缓存实体
		infoList.add(info);
		infoMap.put(info.getTableName(), info);
		info = new CacheInfo("X_MsgCfgFunction", "消息服务配置-功能点定义", com.yunda.frame.baseapp.message.entity.MsgCfgFunction.class, "是", "是", "V2.5.4");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);
		info = new CacheInfo("X_MsgCfgReceive", "消息服务配置-接收方定义", com.yunda.frame.baseapp.message.entity.MsgCfgReceive.class, "是", "是", "V2.5.4");
		infoList.add(info);
		infoMap.put(info.getTableName(), info);
	}
	/**
	 * <li>说明：获取系统中CacheInfo.json中使用hibernate二级缓存和查询缓存的数据库表（实体类）等描述信息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 缓存描述信息列表
	 */
	public List<CacheInfo> getCacheInfo(){
		return infoList;
	}
	
	/**
	 * <li>说明：清空所有注册的实体对象的hibernate二级缓存和查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 */
	public void evictAll(){
		daoUtils.getSessionFactory().evictQueries();
		evict();
	}
	/**
	 * <li>说明：清空所有在CacheInfo.json文件中注册的实体对象的hibernate二级缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 */
	public void evict(){
		List<CacheInfo> infos = getCacheInfo();
		if(infos == null || infos.size() < 1)	return;
		SessionFactory sf = daoUtils.getSessionFactory();
		for (CacheInfo info : infos) {
			sf.evict(info.getEntityClass());
		}
	}
	/**
	 * <li>说明：清空所有查询缓存, 并根据全路径类名称指定的实体对象的hibernate二级缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param tableName 表名
	 * @return void
	 */
	public void evictEntityAndQueries(String tableName){
		CacheInfo info = infoMap.get(tableName);
		daoUtils.getSessionFactory().evict(info.getEntityClass());
		daoUtils.getSessionFactory().evictQueries();
	}
	
}