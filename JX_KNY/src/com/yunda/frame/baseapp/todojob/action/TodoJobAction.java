package com.yunda.frame.baseapp.todojob.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.baseapp.todojob.manager.TodoJobManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 待办事宜控制器类
 * <li>创建人：程锐
 * <li>创建日期：2015-8-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@SuppressWarnings(value = "serial")
public class TodoJobAction extends JXBaseAction<TodoJob, TodoJob, TodoJobManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：获取当前登录人待办事宜列表，构造html字符串给前台
     * <li>创建人：程锐
     * <li>创建日期：2015-8-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getInfo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String jsonStr = "";
        try {
            jsonStr = manager.getTodoJobInfo().toString();
            map.put("info", jsonStr);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
