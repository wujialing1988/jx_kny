package com.yunda.jx.wlgl.movestock.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.movestock.entity.MatMoveStock;
import com.yunda.jx.wlgl.movestock.entity.MatMoveStockDetail;
import com.yunda.jx.wlgl.movestock.manager.MatMoveStockManager;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatMoveStock控制器, 物料移库单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatMoveStockAction extends JXBaseAction<MatMoveStock, MatMoveStock, MatMoveStockManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * <li>说明：保存消耗配件移库单以及明细
	 * <li>创建人：程梅
	 * <li>创建日期：2014-09-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	public void saveWhAndDetail() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String matInWh = StringUtil.nvlTrim( getRequest().getParameter("matMoveStock"), "{}" );
			MatMoveStock matMoveStock = (MatMoveStock)JSONUtil.read(matInWh, MatMoveStock.class);
			MatMoveStockDetail[] detailList = (MatMoveStockDetail[])JSONUtil.read(getRequest(), MatMoveStockDetail[].class);
			String[] errMsg = null;
			try {
				this.manager.saveWhAndDetail(matMoveStock, detailList);
			} catch (StockLackingException e) {
				errMsg = new String[] { e.getMessage() };
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
	 * 
	 * <li>说明：确认移入
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void updateMatMoveStock() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.updateMatMoveStock(id);
			if (errMsg == null || errMsg.length < 1) {
				map.put("success", "true");
			}else{
				map.put("success", "false");
				map.put("errMsg", errMsg);
			}
            map.put("success", "true");
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
	/**
	 * 
	 * <li>说明：分页查询
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void pageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String empId = getRequest().getParameter("empId");
			getRequest().getParameter("idx");
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			MatMoveStock entity = (MatMoveStock)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<MatMoveStock> searchEntity = new SearchEntity<MatMoveStock>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageList(searchEntity,empId).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：回滚操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
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