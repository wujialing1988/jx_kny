package com.yunda.jx.jxgc.producttaskmanage.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.Worker;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkerManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Worker控制器, 作业人员
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkerAction extends JXBaseAction<Worker, Worker, WorkerManager>{
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName()); 
    
    /**
     * <li>说明：获取作业工单的其他作业人员列表（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getOtherWorkerByWorkCard() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {            
            // 作业工单idx
            String workCardIDX = getRequest().getParameter("workCardIDX");
            // 当前系统登录人员id
            Long empid = SystemContext.getOmEmployee().getEmpid();
            List<Worker> workers = this.manager.getOtherWorkerByWorkCard(workCardIDX, empid.toString());
            map.put("list", workers);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }    
    }
    
}