package com.yunda.jx.jxgc.producttaskmanage.manager;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.ProcessTask;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 流程工单查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="processTaskQueryManager")
public class ProcessTaskQueryManager extends JXBaseManager<ProcessTask, ProcessTask>{
	/** 获取流程工单业务类 */
	protected ProcessTaskManager getProcessTaskManager(){
        return (ProcessTaskManager)Application.getSpringApplicationContext().getBean("processTaskManager");
    }
	
}
