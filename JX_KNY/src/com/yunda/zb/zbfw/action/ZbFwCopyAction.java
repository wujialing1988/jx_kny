package com.yunda.zb.zbfw.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.zbfw.entity.ZbFw;
import com.yunda.zb.zbfw.manager.ZbFwCopyManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbFw控制器
 * <li>创建人：程梅
 * <li>创建日期：2016-4-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbFwCopyAction extends JXBaseAction<ZbFw, ZbFw, ZbFwCopyManager> {
    
    /** 日志工具 */
    Logger logger = Logger.getLogger(getClass());
    
    /**
     * 
     * <li>说明：复制流程
     * <li>创建人：程梅
     * <li>创建日期：2015-7-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void copyJobProcessDef() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            ZbFw fw = (ZbFw)JSONUtil.read(getRequest(), entity.getClass());
            if (null == fw) {
                map.put("errMsg", "整备范围不存在");
            }else this.manager.copyJobProcessDef(fw);
            map.put("success", true);
        } catch (Exception e) {
            map.put("success", false);
            map.put("errMsg", e.getMessage());
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}
