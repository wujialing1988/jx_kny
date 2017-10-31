package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcFuncgroup;
import com.yunda.frame.yhgl.manager.SysFuncGroupManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统应用功能管理-应用功能组action
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class SysFuncGroupAction extends JXBaseAction <AcFuncgroup, AcFuncgroup, SysFuncGroupManager> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：获得系统应用功能组树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void tree() throws Exception{
		String appid = getRequest().getParameter("appid");//应用ID
//		String funcgroupid = getRequest().getParameter("funcgroupid");//功能组ID
		List<Map> childNodeList = this.manager.getFuncGroupByApp(appid,null);
		JSONUtil.write(getResponse(),childNodeList);
	}
	
	/**
	 * 
	 * <li>说明：重写基类保存方法，获取页面传入的参数isAdd，用以判断是否新增操作
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-7
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			AcFuncgroup t = (AcFuncgroup)JSONUtil.read(getRequest(), entity.getClass());
			String isAdd = getRequest().getParameter("isAdd");
			String[] errMsg = this.manager.validateUpdate(t,isAdd);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t,isAdd);
				//返回记录保存成功的实体对象
				map.put("entity", t);  
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
}
