package com.yunda.jx.jxgc.processdef.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.processdef.entity.JobProcessDef;
import com.yunda.jx.jxgc.processdef.manager.JobProcessDefManager;
import com.yunda.jx.third.edo.entity.ProjectData;
import com.yunda.jx.third.edo.entity.Result;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessDef控制器
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class JobProcessDefAction extends JXBaseAction<JobProcessDef, JobProcessDef, JobProcessDefManager> {
    
    /** 日志工具 */
    Logger logger = Logger.getLogger(getClass());
    
    /**
     * <li>说明：更新作业流程状态，包含【启用】【作废】
     * <li>创建人：何涛
     * <li>创建日期：2015-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void updateStatus() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String status = req.getParameter("status");
            this.manager.updateStatus(ids, Integer.parseInt(status));
        } catch (Exception e) {
            map.put("success", false);
            map.put("errMsg", e.getMessage());
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：机车检修作业计划甘特图展示
     * <li>创建人：何涛
     * <li>创建日期：2015-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void planOrderGantt() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errorMessage = "";
        try {
            String idx = getRequest().getParameter("idx");
            if (StringUtil.isNullOrBlank(idx)) {
                errorMessage = "机车检修作业流程主键为空";
            }
            Result result = this.manager.planOrderGantt(idx);
            if (result == null) {
                errorMessage = "机车检修作业流程不存在";
            }
            ProjectData data = new ProjectData();
            data.setResult(result);
            logger.info("-----------------------甘特图json：" + JSONUtil.write(result));
            JSONUtil.write(getResponse(), data);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errorMessage);
        }
    }
    
    /**
     * <li>说明：保存机车作业流程节点定义
     * 业务逻辑：如果是修改车型，那么就会把该流程下所有节点下的工单全部清除（因为节点下的作业卡是根据车型选择出来的）
     * <li>创建人：林欢   
     * <li>创建日期：2016-7-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveJobProcessDefInfo() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            JobProcessDef jobProcessDef = (JobProcessDef)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(jobProcessDef);
            if (errMsg == null || errMsg.length < 1) {
//                this.manager.saveOrUpdate(jobProcessDef);
                this.manager.saveJobProcessDefInfo(jobProcessDef);
//              返回记录保存成功的实体对象
                map.put("entity", jobProcessDef);  
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
     * <li>说明：获取作业流程
     * <li>创建人：张迪
     * <li>创建日期：2016-8-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModelsByTrainTypeIDXAndRcIDX() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String trainTypeIDX = getRequest().getParameter("trainTypeIDX");
            String rcIDX = getRequest().getParameter("rcIDX");
            List<JobProcessDef> processDefList = this.manager.getModelsByTrainTypeIDXAndRcIDX(trainTypeIDX, rcIDX);
            // 返回记录保存成功的实体对象
            map.put("processDefList", processDefList);   
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
}
