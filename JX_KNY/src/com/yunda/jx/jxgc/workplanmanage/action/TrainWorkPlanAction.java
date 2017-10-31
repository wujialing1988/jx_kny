package com.yunda.jx.jxgc.workplanmanage.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlanDTO;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;
import com.yunda.jx.third.edo.entity.ProjectData;
import com.yunda.jx.third.edo.entity.Result;
import com.yunda.third.poi.excel.ColumnPattern;
import com.yunda.third.poi.excel.ExcelExport;
import com.yunda.third.poi.excel.ExportExcelDTO;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainWorkPlan控制器, 机车检修作业计划
 * <li>创建人：程锐
 * <li>创建日期：2015-04-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@SuppressWarnings(value = "serial")
public class TrainWorkPlanAction extends JXBaseAction<TrainWorkPlan, TrainWorkPlan, TrainWorkPlanManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
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
     * <li>说明：生成机车检修作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void generateWorkPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainWorkPlan workPlan = (TrainWorkPlan) JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(workPlan);
            if (errMsg == null || errMsg.length < 1) {
                workPlan = this.manager.generateWorkPlan(workPlan);
                map.put("entity", workPlan);
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
     * <li>说明：获取机车检修作业计划实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getEntityByIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainWorkPlan workPlan = this.manager.getModelById(getRequest().getParameter("workPlanIDX"));
            map.put("entity", workPlan);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：启动作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void startPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.updateForStartPlan(id);
            if (errMsg == null)
                map.put(Constants.SUCCESS, true);
            else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：终止作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateForTerminatePlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            this.manager.updateForTerminatePlan(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据实体查询机车检修作业计划
     * <li>创建人：何涛
     * <li>创建日期：2015-5-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getModelByEntiy() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainWorkPlan plan = JSONUtil.read(getRequest(), TrainWorkPlan.class);
            TrainWorkPlan entity = this.manager.getModelByEntiy(plan);
            map.put(Constants.ENTITY, entity);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, e.getMessage());
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取在修机车树列表
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception {
        Map<String, String> params = JSONUtil.read(getRequest().getParameter("searchParams"), Map.class);
        List<HashMap> children = manager.findRdpTreeData(params);
        JSONUtil.write(getResponse(), children);
    }
    
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
            TrainWorkPlan entity = this.manager.getModelById(id);
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
     * <li>说明：机车检修安全生产日报查询
     * <li>创建人：林欢
     * <li>创建日期：2016-6-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void searchTrainWorkPlanDailtReportInfo() throws JsonMappingException, IOException{
        
        //错误信息封装map
        Map<String, Object> map = new HashMap<String, Object>();
        //参数map
        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<TrainWorkPlanDTO> list = new LinkedList<TrainWorkPlanDTO>();
        
        //获取时间查询条件
        String searchDate = getRequest().getParameter("searchDate").substring(0,10);
        paramMap.put("searchDate", searchDate);
        try {
            list = this.manager.searchTrainWorkPlanDailtReportInfo(paramMap);
            map = new Page<TrainWorkPlanDTO>(list.size(), list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：机车检修安全生产日报查询导出
     * <li>创建人：林欢
     * <li>创建日期：2016-6-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public void exportTrainWorkPlanDTOListByParm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
//      参数map
        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<TrainWorkPlanDTO> list = new LinkedList<TrainWorkPlanDTO>();
        List<ExportExcelDTO> expList = new ArrayList<ExportExcelDTO>();
        try {
            
//          获取时间查询条件
            String searchDate = getRequest().getParameter("searchDate").substring(0,10);
            ColumnPattern[] col = JSONUtil.read(getRequest().getParameter("patterns"), ColumnPattern[].class);
            paramMap.put("searchDate", searchDate);
            
            
            ExportExcelDTO dto1 = new ExportExcelDTO();
            list = this.manager.searchTrainWorkPlanDailtReportInfo(paramMap);
            dto1.setPages(new Page<TrainWorkPlanDTO>(list.size(), list));
            dto1.setSheetName("主列表信息");
            dto1.setPattern(col);
            expList.add(dto1);
            
            ExcelExport.exportExcel(expList, searchDate + "机车检修安全生产日报", getResponse());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            getResponse().getWriter().print("导出文件发生错误！");
        }
    }
    
    /**
     * <li>说明：车辆检修校验
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void jyTrainWorkPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainWorkPlan workPlan = (TrainWorkPlan) JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateJY(workPlan);
            if (errMsg == null || errMsg.length < 1) {
                workPlan = this.manager.jyTrainWorkPlan(workPlan);
                map.put("entity", workPlan);
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
    
}
