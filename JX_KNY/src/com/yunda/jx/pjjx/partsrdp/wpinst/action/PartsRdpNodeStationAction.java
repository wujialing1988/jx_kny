package com.yunda.jx.pjjx.partsrdp.wpinst.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeStation;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeStationManager;

/**
 * <li>说明：节点工位
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月4日
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpNodeStationAction
		extends
		JXBaseAction<PartsRdpNodeStation, PartsRdpNodeStation, PartsRdpNodeStationManager> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	 /**
	 * <li>方法说明：根据需求单节点查询
	 * <li>方法名：finByWpNode
	 * @throws JsonMappingException
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-21
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void finByWpNode() throws JsonMappingException, IOException{
		Map<String, Object> map = null;
		try {
			String nodeIdx = getRequest().getParameter("nodeIdx");
			String rdpIdx = getRequest().getParameter("rdpIdx");
			map = manager.findByWpNode(nodeIdx, rdpIdx, getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			map = new HashMap<String, Object>(2);
			ExceptionUtil.process(e, logger, map);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>方法说明：批量保存
	 * <li>方法名：batchSave
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-22
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void batchSave() throws JsonMappingException, IOException{
		Map<String, Object> map = new HashMap<String, Object>(2);
		try {
			PartsRdpNodeStation[] stations = JSONUtil.read(getRequest(), PartsRdpNodeStation[].class);
			manager.saves(stations);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}

}
