package com.yunda.jx.pjjx.base.recorddefine.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.base.recorddefine.entity.PartsStepResult;
import com.yunda.jx.pjjx.base.recorddefine.manager.PartsStepResultManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件默认检测检修结果
 * <li>创建人：林欢
 * <li>创建日期：2016-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@SuppressWarnings(value="serial")
public class PartsStepResultAction extends JXBaseAction<PartsStepResult, PartsStepResult, PartsStepResultManager>{
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 
     * <li>说明：保存检测/检修结果
     * <li>创建人：林欢
     * <li>创建日期：2016-5-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsStepResult() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        
        try {
            PartsStepResult partsStepResult = (PartsStepResult)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(partsStepResult);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.savePartsStepResult(partsStepResult);
//              返回记录保存成功的实体对象
                map.put("entity", partsStepResult);  
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
     * <li>说明：删除检测/检修结果
     * <li>创建人：林欢
     * <li>创建日期：2016-5-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void deletePartsStepResult() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.deletePartsStepResult(ids);
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
