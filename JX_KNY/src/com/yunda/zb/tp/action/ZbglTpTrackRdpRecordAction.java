package com.yunda.zb.tp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.tp.entity.ZbglTpTrackRdpRecord;
import com.yunda.zb.tp.manager.ZbglTpTrackRdpRecordManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpTrackRdpRecord控制器, 提票跟踪记录单
 * <li>创建人：林欢
 * <li>创建日期：2016-08-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglTpTrackRdpRecordAction extends JXBaseAction<ZbglTpTrackRdpRecord, ZbglTpTrackRdpRecord, ZbglTpTrackRdpRecordManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    
    /**
     * <li>说明：提票跟踪记录装填修改
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-09
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void saveAndTrack() throws JsonMappingException, IOException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	HttpServletRequest reg = getRequest();
        String trackRdpIDX = reg.getParameter("trackRdpIDX");
        String trackReason = reg.getParameter("trackReason");
        String flag = reg.getParameter("flag");
        try {
        	this.manager.saveOrUpdateTrackingRecord(trackRdpIDX, trackReason,flag);
        	map.put("success",true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
