package com.yunda.jxpz.coderule.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jxpz.coderule.entity.CodeRuleConfigProp;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigPropManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：CodeRuleConfigProp控制器, 业务编码规则配置属性
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
public class CodeRuleConfigPropAction extends JXBaseAction<CodeRuleConfigProp, CodeRuleConfigProp, CodeRuleConfigPropManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：物理删除编码规则属性信息
	 * <li>创建人：程梅
	 * <li>创建日期：2012-10-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值为空
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("all")
	public void deleteConfigByIds() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
//				if (ids.length == 1 && ids[0].indexOf(",") != -1) {
//					ids = ids[0].split(",");
//				}
				this.manager.deleteByIds(ids);
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
}