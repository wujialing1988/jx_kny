package com.yunda.jx.jxgc.repairrequirement.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.QualityControl;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStep;
import com.yunda.jx.jxgc.repairrequirement.manager.WorkStepManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStep控制器, 工步
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkStepAction extends JXBaseAction<WorkStep, WorkStep, WorkStepManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 
     * <li>说明：删除检测/检修项目及级联删除检测/检修结果、检测项
     * <li>创建人：程锐
     * <li>创建日期：2013-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void logicDeleteWorkStep() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.logicDeleteWorkStep(ids);
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
     * 
     * <li>说明：保存检测/检修项目及质量控制记录
     * <li>创建人：程锐
     * <li>创建日期：2013-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void saveWorkStep() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WorkStep step = (WorkStep)JSONUtil.read(getRequest(), entity.getClass());
            String qualityControls = StringUtil.nvlTrim( getRequest().getParameter("qualityControls"), "{}" );
            QualityControl[] qualityControlList = (QualityControl[])JSONUtil.read(qualityControls, QualityControl[].class);
            String[] errMsg = this.manager.validateUpdate(step);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveWorkStep(step,qualityControlList);
//              返回记录保存成功的实体对象
                map.put("entity", step);  
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
     * 
     * <li>说明：保存检测/检修项目
     * <li>创建人：程锐
     * <li>创建日期：2014-9-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveWorkStep_QrKey() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WorkStep step = (WorkStep)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(step);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveWorkStep_QrKey(step);
//              返回记录保存成功的实体对象
                map.put("entity", step);  
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
     * 
     * <li>说明：根据质量记录单主键获取其检测/修项目中的最大作业顺序
     * <li>创建人：程锐
     * <li>创建日期：2013-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void getMaxSeq()  throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String workSeqIDX = getRequest().getParameter("workSeqIDX");
            int seq = 1;
            if(!StringUtil.isNullOrBlank(workSeqIDX)){
                seq = this.manager.getMaxSeq(workSeqIDX);
            }
            map.put("seq", seq);  
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}