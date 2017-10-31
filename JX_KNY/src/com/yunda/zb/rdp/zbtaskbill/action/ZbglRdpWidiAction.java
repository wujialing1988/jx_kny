package com.yunda.zb.rdp.zbtaskbill.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidi;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidiVo;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWidiManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpWidi控制器, 机车整备任务单数据项
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
public class ZbglRdpWidiAction extends JXBaseAction<ZbglRdpWidi, ZbglRdpWidi, ZbglRdpWidiManager> {
    
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());

    /**
     * <li>说明：根据任务单ID查询任务单数据项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModels() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 任务单ID
            String rdpWiIDX = getRequest().getParameter("rdpWiIDX");
            List<ZbglRdpWidi> models = this.manager.getModels(rdpWiIDX );
            map.put("list", models);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：查询车辆列检详情
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findZbglRdpWidisByRdpIdx() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 车辆列检实例ID
            String rdpIdx = getRequest().getParameter("rdpIdx");
            List<ZbglRdpWidiVo> models = this.manager.findZbglRdpWidisByRdpIdx(rdpIdx);
            map.put("root", models);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
}
