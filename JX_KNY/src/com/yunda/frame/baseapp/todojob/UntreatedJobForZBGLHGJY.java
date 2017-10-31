package com.yunda.frame.baseapp.todojob;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpJYManager;

/**
 * <li>标题：整备合格交验数量统计
 * <li>说明：
 * <li>创建人：林欢
 * <li>创建时间：2016-9-20
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
public class UntreatedJobForZBGLHGJY extends UnTreatedJobPermissionManager <TodoJob,TodoJob> implements IUntreatedJob {

    /**
     * <br/>
     * <li>说明：获取待办事宜 <br/>
     * <li>创建人：林欢 <br/>
     * <li>创建日期：2016-09-20 <br/>
     * <li>修改人： <br/>
     * <li>修改日期： <br/>
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @return 待办事宜
     */
    public TodoJob getJob(String operatorid) {
        if(!checkPermission(Long.parseLong(operatorid), FUNC_ZBHGJY_NAME))
            return null;
        Integer[] count = getCount(Long.parseLong(operatorid));  
        if(count[0] <= 0){ 
            //没有派工任务
            return null;
        }
        TodoJob tdj = new TodoJob();
        tdj.setJobType(FUNC_ZBHGJY_NAME);
        tdj.setJobText("未处理(" + count[0] + ")");
        tdj.setJobUrl("");//机车提票
        tdj.setJobNum(count[0] + "");
        return tdj;
    }
    
    /**
     * <li>方法说明：获取整备管理合格交验待处理任务数量 
     * <li>方法名称：getCount
     * <li>创建人：林欢
     * <li>创建时间：2016-02-20 下午03:20:04
     * <li>修改人：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @return Integer[]
     */
    private Integer[] getCount(long operatorid){
        ZbglRdpJYManager zbglRdpJYManager = (ZbglRdpJYManager)getBean("zbglRdpJYManager");
        return zbglRdpJYManager.findZbglHgjyCount(operatorid);
    }
}
