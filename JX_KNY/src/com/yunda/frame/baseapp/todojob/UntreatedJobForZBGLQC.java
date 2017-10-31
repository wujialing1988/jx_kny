package com.yunda.frame.baseapp.todojob;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.zb.tp.manager.ZbglTpManager;

/**
 * <li>标题：整备提票待碎修、临修质量检验数量统计
 * <li>说明：
 * <li>创建人：林欢
 * <li>创建时间：2016-2-20
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
public class UntreatedJobForZBGLQC extends UnTreatedJobPermissionManager <TodoJob,TodoJob> implements IUntreatedJob {
 
    /**
     * <br/>
     * <li>说明：获取待办事宜 <br/>
     * <li>创建人：林欢 <br/>
     * <li>创建日期：2016-04-19 <br/>
     * <li>修改人： <br/>
     * <li>修改日期： <br/>
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @return 待办事宜
     */
    public TodoJob getJob(String operatorid) {
        if(!checkPermission(Long.parseLong(operatorid), FUNC_LSXTPJC_NAME))
            return null;
        Integer[] count = getCount(Long.parseLong(operatorid));  
        if(count[0] <= 0){ 
            //没有派工任务
            return null;
        }
        TodoJob tdj = new TodoJob();
        tdj.setJobType(FUNC_LSXTPJC_NAME);
        tdj.setJobText("未处理(" + count[0] + ")");
        tdj.setJobUrl("");//碎修、临修质量检验
        tdj.setJobNum(count[0] + "");
        return tdj;
    }
    
    /**
     * <li>方法说明：获取整备管理提票待碎修、临修质量检验任务数量 
     * <li>方法名称：getCount
     * <li>@return operatorid
     * <li>@return faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
     * <li>return: Integer[]
     * <li>创建人：林欢
     * <li>创建时间：2016-04-19 下午03:20:04
     * <li>修改人：
     * <li>修改内容：
     * @param operatorid 操作员ID
     */
    private Integer[] getCount(long operatorid){
        
        ZbglTpManager zbglTpManager = (ZbglTpManager)getBean("zbglTpManager");
        return zbglTpManager.findZBGLTPOrZBGLQCCount(operatorid);
    }
}
