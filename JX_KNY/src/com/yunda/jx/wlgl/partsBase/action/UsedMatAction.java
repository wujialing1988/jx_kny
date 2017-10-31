package com.yunda.jx.wlgl.partsBase.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.partsBase.entity.UsedMat;
import com.yunda.jx.wlgl.partsBase.entity.UsedMatDetail;
import com.yunda.jx.wlgl.partsBase.manager.UsedMatManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UsedMat控制器, 常用物料清单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class UsedMatAction extends
		JXBaseAction<UsedMat, UsedMat, UsedMatManager> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * <li>说明：保存常用物料清单及明细
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveUsedMatAndDetail() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String matData = StringUtil.nvlTrim(getRequest().getParameter("matData"), "{}");
			UsedMat usedMat = (UsedMat) JSONUtil.read(matData, UsedMat.class);
			UsedMatDetail[] detailList = (UsedMatDetail[]) JSONUtil.read(getRequest(), UsedMatDetail[].class);
			usedMat = this.manager.saveUsedMatAndDetail(usedMat, detailList);
			map.put("entity", usedMat);
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}

	}
}