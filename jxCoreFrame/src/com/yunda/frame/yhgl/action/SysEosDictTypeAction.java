/**
 * 
 */
package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictType;
import com.yunda.frame.yhgl.manager.SysEosDictTypeManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 业务字典分类管理-控制层
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 2.0
 */
@SuppressWarnings(value="serial")
public class SysEosDictTypeAction extends JXBaseAction<EosDictType,EosDictType,SysEosDictTypeManager> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：获得业务字典分类树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void tree() throws Exception{
		String nodeid = getRequest().getParameter("nodeid");//当前orgid
		if(StringUtil.isNullOrBlank(nodeid)||"ROOT_0".equals(nodeid)){
			nodeid = "ROOT_0";
		}
		List<Map> childNodeList = this.manager.getChildNodes(nodeid); 
		JSONUtil.write(getResponse(), childNodeList);
	}
	
	/**
	 * <li>说明： 调用检查函数检查是否业务字典分类ID已存在
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void validateTypeID() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String dicttypeid = getRequest().getParameter("dicttypeid");
			if(StringUtil.isNullOrBlank(dicttypeid)) return;
			EosDictType type = new EosDictType();
			type.setDicttypeid(dicttypeid);
			String[] errMsg = this.manager.validateUpdate(type);
			if (!(errMsg == null || errMsg.length < 1)) {
				map.put("errMsg", errMsg);
			} else {
				map.put("success", false);
			}
		} catch (Exception e){
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明： 重写父类方法， 去掉验证方法的调用，对于类型ID的验证，交给页面js调用处理
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			EosDictType t = (EosDictType)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = null;//this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
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
