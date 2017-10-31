package com.yunda.zb.trainwarning.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.trainwarning.entity.ZbglWarning;
import com.yunda.zb.trainwarning.manager.ZbglWarningManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglWarning控制器, 机车检测预警
 * <li>创建人：程锐
 * <li>创建日期：2015-03-04
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglWarningAction extends JXBaseAction<ZbglWarning, ZbglWarning, ZbglWarningManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：下发班组
     * <li>创建人：程锐
     * <li>创建日期：2015-3-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateForXfbz() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updateForXfbz(ids);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：取消预警
     * <li>创建人：程锐
     * <li>创建日期：2015-3-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateForCancel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updateForCancel(ids);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}
