package com.yunda.jx.jcll.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jcll.entity.TrainRecord;
import com.yunda.jx.jcll.manager.TrainRecordInstanceManager;
import com.yunda.jx.jcll.manager.TrainRecordManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车履历控制层
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class TrainRecordAction extends JXBaseAction<TrainRecord, TrainRecord, TrainRecordManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车履历主要部件实例业务类 */
    @Resource
    private TrainRecordInstanceManager trainRecordInstanceManager ;
    

    /**
     * <li>说明：保存机车履历主表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveOrUpdate() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            logger.info("记录日志");
            logger.error("记录日志");
            logger.error("记录日志1");
            TrainRecord record = (TrainRecord)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(record);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveOrUpdate(record);
                // 生成主要部件实例
                trainRecordInstanceManager.generateInstances(record);
                map.put("entity", record);  
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
