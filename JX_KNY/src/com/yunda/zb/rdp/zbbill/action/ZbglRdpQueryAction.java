package com.yunda.zb.rdp.zbbill.action;

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
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpQueryManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车整备查询action
 * <li>创建人：程锐
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@SuppressWarnings("serial")
public class ZbglRdpQueryAction extends JXBaseAction<ZbglRdp, ZbglRdp, ZbglRdpQueryManager>{

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：转临修处理
     * <li>创建人：程锐
     * <li>创建日期：2016-05-07
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findListByTrain() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			ZbglRdp objEntity = (ZbglRdp)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<ZbglRdp> searchEntity = new SearchEntity<ZbglRdp>(objEntity, getStart(), getLimit(), getOrders());
			map = this.manager.findListByTrain(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	
    	
    }
}
