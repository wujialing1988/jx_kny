package com.yunda.jx.webservice.stationTerminal;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.jx.pjjx.repairline.entity.PartsWorkStation;
import com.yunda.jx.pjjx.repairline.manager.PartsWorkStationManager;
import com.yunda.jx.webservice.stationTerminal.wsbean.WorkStationWSBean;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>说明：工位WS提供类
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-22
 * <li>成都运达科技股份有限公司
 */
@Service("workStationWS")
public class WorkStationImpl implements IWorkStation{

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="partsWorkStationManager")
	private PartsWorkStationManager manager;
	
	/**
	 * <li>方法说明： 获取所有配件工位
	 * <li>方法名：partsWorkStation
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-22
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public String partsWorkStation() {
		OperateReturnMessage msg = new OperateReturnMessage();
		try {
			List<PartsWorkStation> list = manager.findList(new PartsWorkStation(), null);
			return JSONArray.toJSONString(BeanUtils.copyListToList(WorkStationWSBean.class, list));
		} catch (Exception e) {
			msg.setFaildFlag(e.getMessage());
        	logger.error("工位WS接口异常", e);
		}
		return JSONObject.toJSONString(msg);
	}
}
