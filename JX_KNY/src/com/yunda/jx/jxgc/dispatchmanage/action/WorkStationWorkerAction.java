package com.yunda.jx.jxgc.dispatchmanage.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStationWorker;
import com.yunda.jx.jxgc.dispatchmanage.manager.WorkStationWorkerManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStationWorker控制器, 工位作业人员
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkStationWorkerAction extends JXBaseAction<WorkStationWorker, WorkStationWorker, WorkStationWorkerManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * /jsp/jx/jxgc/dispatchmanage/WorkStationWorker.js
     * <li>说明：工长默认派工-批量添加工位作业人员
     * <li>创建人：程锐
     * <li>创建日期：2012-12-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void saveOrUpdateBatch() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WorkStationWorker[] workStationWorker = (WorkStationWorker[])JSONUtil.read(getRequest(), WorkStationWorker[].class);
            this.manager.saveOrUpdateBatch(workStationWorker);
            map.put("success", true);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}