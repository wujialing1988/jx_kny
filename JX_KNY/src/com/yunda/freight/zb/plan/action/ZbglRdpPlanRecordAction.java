package com.yunda.freight.zb.plan.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecord;
import com.yunda.freight.zb.plan.entity.ZbglRdpWorkerVo;
import com.yunda.freight.zb.plan.manager.ZbglRdpPlanRecordManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 车辆列检计划action
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class ZbglRdpPlanRecordAction extends JXBaseAction<ZbglRdpPlanRecord, ZbglRdpPlanRecord, ZbglRdpPlanRecordManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：车派人
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void dispatcher() throws IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String empids = getRequest().getParameter("empids");
            String empnames = getRequest().getParameter("empnames");
            String idxs = getRequest().getParameter("idxs");
            this.manager.dispatcher(idxs, empids,empnames); 
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：人派车
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void disrecord() throws IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String recordIdxs = getRequest().getParameter("recordIdxs");
            String workPersonIdxs = getRequest().getParameter("workPersonIdxs");
            String workPersonNames = getRequest().getParameter("workPersonNames");
            String rdpPlanIdx = getRequest().getParameter("rdpPlanIdx");
            this.manager.disrecord(recordIdxs, workPersonIdxs,workPersonNames,rdpPlanIdx); 
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：通过作业班组查询班组人员
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findWorkerList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 作业班组
            String orgIdx = getRequest().getParameter("orgIdx");
            // 列检计划
            String rdpPlanIdx = getRequest().getParameter("rdpPlanIdx");
            List<ZbglRdpWorkerVo> models = this.manager.findWorkerList(orgIdx,rdpPlanIdx);
            map.put("root", models);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：填写车号
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void writeTrainNo() throws IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String entityJson = getRequest().getParameter("entityJson");
            ZbglRdpPlanRecord record = (ZbglRdpPlanRecord)JSONUtil.read(entityJson, entity.getClass());
            this.manager.writeTrainNo(record); 
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询货车车辆派工分队信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findZbglRecordAndQueue() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 列检计划
            String rdpPlanIdx = getRequest().getParameter("rdpPlanIdx");
            List<Map<String, Object>> models = this.manager.findZbglRecordAndQueue(rdpPlanIdx);
            map.put("root", models);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：切换人员左右位
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void setDirect() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 列检计划
            String rdpPlanIdx = getRequest().getParameter("rdpPlanIdx");
            // 分队id
            String queueNo = getRequest().getParameter("queueNo");
            this.manager.setDirect(rdpPlanIdx,queueNo);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：查询队列选择列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findSelectQueneList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 班次id
            String classNo = getRequest().getParameter("classNo");
            // 班组id
            String orgIdx = getRequest().getParameter("orgIdx");
            List<Map<String, Object>> models = this.manager.findSelectQueneList(classNo,orgIdx);
            map.put("root", models);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：修改车辆对应的队列
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void changeRecordQueue() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 队列编号
            String queueNo = getRequest().getParameter("queueNo");
            // 车辆id集合
            String[] recordIds = getRequest().getParameterValues("recordIds");
            this.manager.changeRecordQueue(queueNo,recordIds);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    
}
