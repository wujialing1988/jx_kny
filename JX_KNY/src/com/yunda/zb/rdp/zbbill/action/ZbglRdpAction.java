package com.yunda.zb.rdp.zbbill.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpTempRepair;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdp控制器, 机车整备单
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglRdpAction extends JXBaseAction<ZbglRdp, ZbglRdp, ZbglRdpManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：转临修处理
     * <li>创建人：程锐
     * <li>创建日期：2015-1-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateForZLX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String rdpIDX = req.getParameter("idx");
            String[] tpIDXAry = (String[]) JSONUtil.read(req.getParameter("tpIDXAry"), String[].class);
            ZbglRdpTempRepair zlxData = (ZbglRdpTempRepair) JSONUtil.read(req.getParameter("zlxData"), ZbglRdpTempRepair.class);
            String errMsg = this.manager.updateForZLX(rdpIDX, tpIDXAry, zlxData);
            if (StringUtil.isNullOrBlank(errMsg)) {
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
    
    /**
     * <li>说明：查询整备单（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModel() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletResponse response = getResponse();
        try {
            ZbglRdp entity = this.manager.getModelById(id);
            map.put(Constants.ENTITY, entity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializationConfig().setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/json");
            mapper.writeValue(response.getWriter(), map);
        }
    }
}
