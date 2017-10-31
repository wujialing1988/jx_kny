package com.yunda.jx.jxgc.producttaskmanage.action; 


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTaskResultView;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkTaskResultViewManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkTaskResultViewAction控制器, 作业任务结果查看
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkTaskResultViewAction extends JXBaseAction<WorkTaskResultView, WorkTaskResultView, WorkTaskResultViewManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：查询作业项对应的三检一验人员信息
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-5-8
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param QR:(qunilty Record)质量检查记录
	 * @return void
	 * @throws Exception
	 */	
	public void pageListForTaskQR() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			WorkTaskResultView entity = (WorkTaskResultView)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<WorkTaskResultView> searchEntity = new SearchEntity<WorkTaskResultView>(entity, getStart(), getLimit(), getOrders());
			List<Map<String,String>> mapRecord = this.manager.pageListForTaskQR(searchEntity);
			map.put("id", EntityUtil.IDX);
			map.put("root", mapRecord);
			map.put("totalProperty", mapRecord == null ? 0 : mapRecord.size() );
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
}