package com.yunda.jx.wlgl.expend.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.expend.entity.MatTrainExpendAccount;
import com.yunda.jx.wlgl.expend.entity.MatTrainExpendAccountDetail;
import com.yunda.jx.wlgl.expend.manager.MatTrainExpendAccountManager;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatTrainExpendAccount控制器, 机车用料消耗记录
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-28 下午03:19:47
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class MatTrainExpendAccountAction extends JXBaseAction<MatTrainExpendAccount, MatTrainExpendAccount, MatTrainExpendAccountManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：保存机车用料消耗记录，包含【暂存】和【登帐】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	public void saveTemporary() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			MatTrainExpendAccount acoount = (MatTrainExpendAccount) JSONUtil.read(getRequest(), MatTrainExpendAccount.class);
			String[] errMsg = null;
			try {
				errMsg = this.manager.saveTemporary(acoount);
			} catch (StockLackingException e) {
				errMsg = new String[] { e.getMessage() };
			}
			if (errMsg == null || errMsg.length < 1) {
				map.put("success", "true");
			} else {
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
	 * <li>说明：新增
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	public void saveTemporaryForAdd() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String entityJson = StringUtil.nvlTrim( getRequest().getParameter("matTrainExpendAccount"), "{}" );
			MatTrainExpendAccount account = (MatTrainExpendAccount)JSONUtil.read(entityJson, MatTrainExpendAccount.class);
			MatTrainExpendAccountDetail[] detailList = (MatTrainExpendAccountDetail[])JSONUtil.read(getRequest(), MatTrainExpendAccountDetail[].class);
			String[] errMsg = null;
			try {
				errMsg = this.manager.saveTemporaryForAdd(account, detailList);
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
	 * <li>说明：登账
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	public void saveEntryAccount() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] errMsg = null;
			try {
				errMsg = this.manager.saveEntryAccount(ids);
			} catch (StockLackingException e) {
				errMsg = new String[] { e.getMessage() };
			}
			if (errMsg == null || errMsg.length < 1) {
				map.put("success", "true");
			} else {
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