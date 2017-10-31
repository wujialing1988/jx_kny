package com.yunda.jx.jxgc.repairrequirement.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.RepairProject;
import com.yunda.jx.jxgc.repairrequirement.manager.ProjectToRecordCardManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车检修作业工单处理优化-转化以前的表关系至新的表关系 
 * <li>创建人：程锐
 * <li>创建日期：2016-6-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@SuppressWarnings(value = "serial")
public class ProjectToRecordCardAction  extends JXBaseAction<RepairProject, RepairProject, ProjectToRecordCardManager>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：机车检修作业工单处理优化-转化以前的表关系至新的表关系 
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateProject() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updateProject();            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
