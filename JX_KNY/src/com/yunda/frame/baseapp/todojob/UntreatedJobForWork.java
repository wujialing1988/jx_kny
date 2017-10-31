package com.yunda.frame.baseapp.todojob;


import com.yunda.Application;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;

/**
 * <li>标题：作业工单待处理项统计
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-2-17
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 */
public class UntreatedJobForWork extends UnTreatedJobPermissionManager <TodoJob,TodoJob> implements IUntreatedJob {
    
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
		Integer count = getCount(Long.parseLong(operatorid));
        if (null == count) {
            return null;
        }
		String str = fillStr(count);
		if(str == null){
		    return null;
		}
		TodoJob tdj = new TodoJob();
		tdj.setJobType(FUNC_ZYGD_NAME);
		tdj.setJobText(str);
		tdj.setJobUrl("/jsp/jx/jxgc/WorkTask/WorkTask.jsp");
		tdj.setJobNum((count) + "");
		return tdj;
	}

	/**
	 * <li>方法说明：填充字符串 
	 * <li>方法名称：fillStr
	 * <li>@param count
	 * <li>@return
	 * <li>return: String
	 * <li>创建人：张凡
	 * <li>创建时间：2014-2-17 下午03:30:43
	 * <li>修改人：
	 * <li>修改内容：
	 */
	private String fillStr(Integer count){
	    StringBuilder sb = new StringBuilder();
	    if(count > 0){
	        sb.append("待处理(").append(count).append("),");   
	    }
	    
	    if(sb.length() > 0){
	        return sb.deleteCharAt(sb.length() - 1).toString();
	    }
	    return null;
	}

	/**
	 * <li>方法说明：统计任务数量 
	 * <li>方法名称：getCount
	 * <li>@return
	 * <li>return: Integer[]
	 * <li>创建人：张凡
	 * <li>创建时间：2014-2-17 下午03:31:19
	 * <li>修改人：
	 * <li>修改内容：
     * @param operatorid 操作员ID
	 */
    private Integer getCount(long operatorid) {
        OmOrganizationSelectManager omOrganizationSelectManager = (OmOrganizationSelectManager)getBean("omOrganizationSelectManager");
        OmOrganization org = omOrganizationSelectManager.getOrgByAcOperator(operatorid);
        if (null == org) {
            return null;
        }
        WorkCardManager workCardManager = (WorkCardManager)Application.getSpringApplicationContext().getBean("workCardManager");
        OmEmployeeSelectManager omEmployeeSelectManager = (OmEmployeeSelectManager)getBean("omEmployeeSelectManager");
        OmEmployee emp = omEmployeeSelectManager.findEmpByOperator(operatorid);
        return workCardManager.findTaskCount(emp.getEmpid(), org.getOrgid());
    }
}
