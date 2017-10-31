package com.yunda.jx.wlgl.matcheck.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.matcheck.entity.MatCheckProfit;
import com.yunda.jx.wlgl.matcheck.entity.MatCheckProfitDetail;
import com.yunda.jx.wlgl.matcheck.manager.MatCheckProfitManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatCheckProfit控制器, 物料盘盈处理
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatCheckProfitAction extends JXBaseAction<MatCheckProfit, MatCheckProfit, MatCheckProfitManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：保存盘盈单以及明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	public void saveProfitAndDetail() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String sMatCheckProfit = StringUtil.nvlTrim( getRequest().getParameter("matCheckProfit"), "{}" );
			// 盘盈单
			MatCheckProfit oMatCheckProfit = (MatCheckProfit)JSONUtil.read(sMatCheckProfit, MatCheckProfit.class);
			// 盘盈单明细
			MatCheckProfitDetail[] detailList = (MatCheckProfitDetail[])JSONUtil.read(getRequest(), MatCheckProfitDetail[].class);
			String[] errMsg = this.manager.saveProfitAndDetail(oMatCheckProfit, detailList);
			if (errMsg == null || errMsg.length < 1) {
				map.put("success", "true");
			}else{
				map.put("success", "false");
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
     * <li>说明：分页查询
     * <li>创建人：何涛
     * <li>创建日期：2014-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void pageList() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			// 检验是否是查询页面的查询请求
			boolean isQueryPage = Boolean.parseBoolean(StringUtil.nvlTrim(req.getParameter("isQueryPage"), "false"));
			MatCheckProfit entity = (MatCheckProfit)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<MatCheckProfit> searchEntity = new SearchEntity<MatCheckProfit>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageList(searchEntity, isQueryPage).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：回滚操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-28
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public void updateRollBack() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.updateRollBack(ids);
			if (errMsg == null || errMsg.length < 1) {
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