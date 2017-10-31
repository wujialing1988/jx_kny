package com.yunda.jxpz.coderule.action; 

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.coderule.entity.CodeRuleConfig;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：CodeRuleConfig控制器, 业务编码规则配置
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-09
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class CodeRuleConfigAction extends JXBaseAction<CodeRuleConfig, CodeRuleConfig, CodeRuleConfigManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：保存实现ID的传递
     * <li>创建人：程梅
     * <li>创建日期：2012-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws 抛出异常列表
     */
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			CodeRuleConfig config = (CodeRuleConfig)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(config);
			if (errMsg == null || errMsg.length < 1) {
//				判断实体类idx主键，若为“”空白字符串，设置实体类主键idx为null
				String idx = (String)BeanUtils.forceGetProperty(config, "idx");
				idx = StringUtil.nvlTrim(idx, null);
				BeanUtils.forceSetProperty(config, "idx", idx);
				this.manager.saveOrUpdate(config);
				map.put("idx", config.getIdx());  //返回保存成功的IDX
				map.put(Constants.SUCCESS, "true");
			} else {
				map.put(Constants.SUCCESS, "false");
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
	 * <li>说明：物理删除编码规则信息
	 * <li>创建人：程梅
	 * <li>创建日期：2012-10-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void deleteConfigByIds() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete((Serializable[])ids);
			if (errMsg == null || errMsg.length < 1) {
//				if (ids.length == 1 && ids[0].indexOf(",") != -1) {
//					ids = ids[0].split(",");
//				}
				this.manager.deleteByIds((Serializable[])ids);
				map.put(Constants.SUCCESS, "true");
			} else {
				map.put(Constants.SUCCESS, "false");
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
	 * <li>说明：根据编码规则配置信息生成编号
	 * <li>创建人：程梅
	 * <li>创建日期：2012-10-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void getConfigRule() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String ruleFunction = getRequest().getParameter("ruleFunction");
			String rule = this.manager.makeConfigRule(ruleFunction);
			map.put("rule", rule);  //返回生成的配件编码
			map.put(Constants.SUCCESS, "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
    /**
     * <li>说明：批量生成编号
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void getBatchConfigRule() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String ruleFunction = getRequest().getParameter("ruleFunction");
			String num = getRequest().getParameter("num");
			String rule = "";
			String[] array = null;
			String ruleStr = "";
			if (!StringUtil.isNullOrBlank(num) && !StringUtil.isNullOrBlank(ruleFunction)) {
				array = this.manager.batchMakeConfigRule(ruleFunction, Integer.parseInt(num)); 
				rule = array[0];
				ruleStr = array[1];
			}
			map.put("rule", rule);  
			map.put("ruleStr", ruleStr);  
			map.put(Constants.SUCCESS, true);
			JSONUtil.write(this.getResponse(), map);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} 
	}
}