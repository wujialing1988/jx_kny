package com.yunda.jx.jxgc.repairrequirement.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult;
import com.yunda.jx.jxgc.repairrequirement.manager.WorkStepResultManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 作业项对应的处理结果 控制器
 * <li>创建人：程锐
 * <li>创建日期：2013-5-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class WorkStepResultAction  extends JXBaseAction<WorkStepResult, WorkStepResult, WorkStepResultManager>{

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：保存检测/检修结果
     * <li>创建人：程锐
     * <li>创建日期：2013-5-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void saveWorkStepResult() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WorkStepResult workStepResult = (WorkStepResult)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(workStepResult);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveWorkStepResult(workStepResult);
//              返回记录保存成功的实体对象
                map.put("entity", workStepResult);  
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
     * <li>方法名称：getDefaultValue
     * <li>方法说明：获取默认值 
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-5-16 下午03:27:35
     * <li>修改人：
     * <li>修改内容：
     */
    public void getDefaultValue() throws JsonMappingException, IOException{
        
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            
            String stepIdx = getRequest().getParameter("stepIdx");
            
            entity = this.manager.getCurrentValue(stepIdx);
            if(entity!=null){
                map.put("value", entity.getResultName());
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}
