package com.yunda.jx.jxgc.tpmanage.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRole;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckRoleManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：提票角色控制器
 * <li>创建人：张迪
 * <li>创建日期：2016-12-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.13
 */
@SuppressWarnings(value = "serial")
public class FaultTicketCheckRoleAction extends JXBaseAction<FaultTicketCheckRole, FaultTicketCheckRole, FaultTicketCheckRoleManager> {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：批量保存角色
     * <li>创建人：张迪
     * <li>创建日期：2016-12-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveCheckRoles() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            FaultTicketCheckRole[] checkRoles = (FaultTicketCheckRole[]) JSONUtil.read(getRequest(), FaultTicketCheckRole[].class);
            this.manager.saveCheckRoles(checkRoles);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
