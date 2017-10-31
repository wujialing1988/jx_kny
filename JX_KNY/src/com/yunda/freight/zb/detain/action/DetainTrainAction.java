package com.yunda.freight.zb.detain.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.zb.detain.entity.DetainTrain;
import com.yunda.freight.zb.detain.manager.DetainTrainManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 扣车管理Action
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-20 17:26:28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class DetainTrainAction extends JXBaseAction<DetainTrain, DetainTrain, DetainTrainManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：审批扣车申请
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveDetainTrain() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            DetainTrain entity = (DetainTrain)JSONUtil.read(getRequest(), DetainTrain.class);
            String[] errMsg = this.manager.validateUpdate(entity);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveDetainTrain(entity);
                map.put("entity", entity);  
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
