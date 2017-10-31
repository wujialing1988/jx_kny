package com.yunda.sb.classfication.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.classfication.entity.Classification;
import com.yunda.sb.classfication.manager.ClassificationManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备类别管理控制器
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ClassificationAction extends JXBaseAction<Classification, Classification, ClassificationManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>说明： 查询设备类别树
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-4-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void findTree() throws JsonMappingException, IOException {
		List<Map<String, Object>> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpServletRequest req = getRequest();
			String parentCode = req.getParameter("parentCode");
			String findEquipment = StringUtil.nvl(req.getParameter("findEquipment"), "false");
			list = this.manager.findTree(parentCode, Boolean.parseBoolean(findEquipment));
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
	}

}
