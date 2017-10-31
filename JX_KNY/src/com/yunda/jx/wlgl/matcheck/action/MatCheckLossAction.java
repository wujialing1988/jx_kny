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
import com.yunda.jx.wlgl.matcheck.entity.MatCheckLoss;
import com.yunda.jx.wlgl.matcheck.entity.MatCheckLossDetail;
import com.yunda.jx.wlgl.matcheck.manager.MatCheckLossManager;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatCheckLoss控制器, 物料损耗处理
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
public class MatCheckLossAction extends JXBaseAction<MatCheckLoss, MatCheckLoss, MatCheckLossManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：保存损耗单以及明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	public void saveLossAndDetail() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String sMatCheckLoss = StringUtil.nvlTrim( getRequest().getParameter("matCheckLoss"), "{}" );
			// 损耗单
			MatCheckLoss oMatCheckLoss = (MatCheckLoss)JSONUtil.read(sMatCheckLoss, MatCheckLoss.class);
			// 损耗单明细
			MatCheckLossDetail[] detailList = (MatCheckLossDetail[])JSONUtil.read(getRequest(), MatCheckLossDetail[].class);
			String[] errMsg = null;
			try {
				errMsg = this.manager.saveLossAndDetail(oMatCheckLoss, detailList);
			} catch (StockLackingException e) {
				errMsg = new String[]{e.getMessage()};
			}
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
			MatCheckLoss entity = (MatCheckLoss)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<MatCheckLoss> searchEntity = new SearchEntity<MatCheckLoss>(entity, getStart(), getLimit(), getOrders());
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