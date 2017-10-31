package com.yunda.frame.baseapp.todojob;

import org.apache.log4j.Logger;

import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.jx.jxgc.tpmanage.manager.FaultQCResultQueryManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质量检查待办事宜（不需要）
 * <li>创建人：程锐
 * <li>创建日期：2015-7-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
public class UntreatedJobForTpZljc extends UnTreatedJobPermissionManager <TodoJob,TodoJob> implements IUntreatedJob {

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <br/>
     * <li>说明：获取待办事宜 <br/>
     * <li>创建人：程锐 <br/>
     * <li>创建日期：2015-7-22 <br/>
     * <li>修改人： <br/>
     * <li>修改日期： <br/>
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @return 待办事宜
     */
    public TodoJob getJob(String operatorid) {
        int count = getCount(Long.parseLong(operatorid));
        if(count <= 0)
            return null;
        TodoJob tdj = new TodoJob();
        tdj.setJobType(FUNC_TPZLJC_NAME);
        tdj.setJobText("未处理(" + count + ")");
        tdj.setJobUrl("/jsp/jx/jxgc/tpmanage/FaultQCResult.jsp");
        tdj.setJobNum(count + "");
        return tdj;
    }
    
    /**
     * <li>说明：获取待处理任务数量 
     * <li>创建人：程锐
     * <li>创建日期：2015-7-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 待处理任务数量 
     */
    private int getCount(long operatorid){
        FaultQCResultQueryManager faultQCResultQueryManager = (FaultQCResultQueryManager)getBean("faultQCResultQueryManager");
        int count = 0;
        try {
            count = faultQCResultQueryManager.queryQCCount(operatorid);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        }
        return count;
    }
}
