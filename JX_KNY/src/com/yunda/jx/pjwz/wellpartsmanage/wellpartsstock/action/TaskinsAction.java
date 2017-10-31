package com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity.Taskins;
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.manager.TaskinsManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: Taskins控制器, 立体仓库_作业任务表
 * <li>创建人：何涛
 * <li>创建日期：2015-12-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2.4
 */
@SuppressWarnings(value="serial")
public class TaskinsAction extends JXBaseAction<Taskins, Taskins, TaskinsManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：生成任务指令单
     * <li>创建人：张迪
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void insert() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Taskins entity = JSONUtil.read(getRequest(), Taskins.class);  
            String[] errMsg = this.manager.validateInsert(entity);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.insert(entity);
                map.put("success", true);
            }
            else {
                map.put("success", false);
                map.put("errMsg", errMsg);
        }          
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
}