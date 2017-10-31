package com.yunda.jx.jxgc.recordscan.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.recordscan.entity.RecordScanQuery;
import com.yunda.jx.jxgc.recordscan.manager.RecordScanQueryManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 扫描二维码查看检修记录单
 * <li>创建人：张迪
 * <li>创建日期：2016-8-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class RecordScanQueryAction extends JXBaseAction<RecordScanQuery, RecordScanQuery, RecordScanQueryManager>{

    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName()); 
    
    /**
     * <li>说明：根据检修记录单查找检修兑现单
     * <li>创建人：张迪
     * <li>创建日期：2016-8-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findRdpRecord() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            map = this.manager.findRdpRecord(id);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}
