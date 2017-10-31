package com.yunda.jx.jxgc.workhis.workplan.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.yunda.jx.jxgc.workhis.workplan.entity.TrainWorkPlanHis;
import com.yunda.jx.jxgc.workhis.workplan.manager.TrainWorkPlanHisManager;
import com.yunda.jx.third.edo.entity.ProjectData;
import com.yunda.jx.third.edo.entity.Result;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainWorkPlanHis控制器, 机车检修作业计划历史
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@SuppressWarnings(value = "serial")
public class TrainWorkPlanHisAction extends JXBaseAction<TrainWorkPlanHis, TrainWorkPlanHis, TrainWorkPlanHisManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    
    /**
     * <li>说明：查询机车检修作业计划（iPad应用）
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
            TrainWorkPlanHis entity = this.manager.getModelById(id);
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

    /**
     * <li>说明：机车检修作业计划甘特图展示
     * <li>创建人：程锐
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
            String workPlanIDX = getRequest().getParameter("workPlanIDX");
            String displayMode = getRequest().getParameter("displayMode");
            String nodeIdx = getRequest().getParameter("nodeIdx");
            if (StringUtil.isNullOrBlank(workPlanIDX))
                errorMessage = "机车检修作业计划主键为空";
            Result result = this.manager.planOrderGantt(workPlanIDX, displayMode, nodeIdx);
            if (result == null)
                errorMessage = "机车检修作业计划不存在";
            ProjectData data = new ProjectData();
            data.setResult(result);
            logger.info("-----------------------甘特图json：" + JSONUtil.write(result));
            JSONUtil.write(getResponse(), data);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errorMessage);
        }
    }
    /**
     * <li>说明：查找实际时间的最小最大值
     * <li>创建人：张迪
     * <li>创建日期：2016-8-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getMinAndMaxRealTime() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String workPlanIDX = getRequest().getParameter("workPlanIDX");
            Date[]  minAndMaxRealTime =  this.manager.getMinAndMaxRealTime(workPlanIDX);
            map.put("mkinAndMaxRealTime", minAndMaxRealTime);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
