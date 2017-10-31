package com.yunda.frame.baseapp.todojob;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.Dispatcher4WorkCardManager;

/**
 * <li>标题：工长派工未派工数量统计（不需要）
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-2-17
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
public class UntreatedJobForForeman extends UnTreatedJobPermissionManager <TodoJob,TodoJob> implements IUntreatedJob {
    
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
	    if(!checkPermission(Long.parseLong(operatorid), FUNC_GZPG_NAME))
	        return null;
	    Integer[] count = getCount(Long.parseLong(operatorid));  
	    if(count[0] <= 0){ 
	        //没有派工任务
	        return null;
	    }
		TodoJob tdj = new TodoJob();
		tdj.setJobType(FUNC_GZPG_NAME);
		tdj.setJobText("未派工(" + count[0] + ")");
		tdj.setJobUrl("/jsp/jx/scdd/dispatch/foreman.jsp");
		tdj.setJobNum(count[0] + "");
		return tdj;
	}
	
	/**
	 * <li>方法说明：获取待处理任务数量 
	 * <li>方法名称：getCount
	 * <li>@return
	 * <li>return: Integer[]
	 * <li>创建人：张凡
	 * <li>创建时间：2014-2-17 下午03:20:04
	 * <li>修改人：
	 * <li>修改内容：
     * @param operatorid 操作员ID
	 */
	private Integer[] getCount(long operatorid){
	    Dispatcher4WorkCardManager m = (Dispatcher4WorkCardManager)getBean("dispatcher4WorkCardManager");
        OmEmployeeSelectManager omEmployeeSelectManager = (OmEmployeeSelectManager)getBean("omEmployeeSelectManager");
	    OmEmployee emp = omEmployeeSelectManager.findEmpByOperator(operatorid);
	    return m.findForemanCount(emp.getOrgid());
	}
}
