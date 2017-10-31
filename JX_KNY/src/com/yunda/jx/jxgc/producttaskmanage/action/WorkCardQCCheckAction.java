package com.yunda.jx.jxgc.producttaskmanage.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardQCCheck;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardQCCheck.WorkCardQCCheckSearcher;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardQCCheckManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WorkCardQCCheck控制器，质量检验查询
 * <li>创建人：何涛
 * <li>创建日期：2014-11-26 下午02:31:50
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkCardQCCheckAction extends JXBaseAction<WorkCardQCCheck, WorkCardQCCheck, WorkCardQCCheckManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：查询质量检查信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findPageList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
			// 检验状态（0:未开放;1:待处理;2:已处理;3已终止），默认查询待处理
			String status = StringUtil.nvlTrim(req.getParameter("status"), "1");
			// 检验方式（1：抽检，2：必检），默认查询必检数据
			String checkWay = StringUtil.nvlTrim(req.getParameter("checkWay"), "2");
			WorkCardQCCheckSearcher entity = (WorkCardQCCheckSearcher) JSONUtil.read(searchJson, WorkCardQCCheckSearcher.class);
			SearchEntity<WorkCardQCCheckSearcher> searchEntity = new SearchEntity<WorkCardQCCheckSearcher>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.pageList(searchEntity, status, checkWay).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
}