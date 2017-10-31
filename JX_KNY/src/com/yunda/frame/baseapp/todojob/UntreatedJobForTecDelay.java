package com.yunda.frame.baseapp.todojob;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;

/**
 * <li>标题：工序延误数量统计(不需要)
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-2-17
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 */
public class UntreatedJobForTecDelay extends UnTreatedJobPermissionManager <TodoJob,TodoJob> implements IUntreatedJob {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName()); 
	
    /**
     * <li>说明：获取待办事宜
     * <li>创建人：程锐
     * <li>创建日期：2015-5-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return TodoJob对象
     * @throws BusinessException
     */
	public TodoJob getJob(String operatorid) {
        if(!checkPermission(Long.parseLong(operatorid), FUNC_GXYW_NAME))
            return null;
	    int count = getCount(Long.parseLong(operatorid));
	    if(count <= 0){
	        return null;
	    }
		TodoJob tdj = new TodoJob();
		tdj.setJobType(FUNC_GXYW_NAME);
		tdj.setJobText(FUNC_GXYW_NAME);
		tdj.setJobUrl("/jsp/jx/jxgc/WorkTask/WorkSeqPutOff.jsp");
		tdj.setJobNum(count + "");
		return tdj;
	}
    
    /**
     * <li>方法说明：获取数量 
     * <li>方法名称：getCount
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2014-2-17 下午04:18:15
     * <li>修改人：
     * <li>修改内容：
     * @param operatorid 操作员ID
     */
	private int getCount(long operatorid){
		NodeCaseDelayManager m = (NodeCaseDelayManager)getBean("nodeCaseDelayManager");
        OmEmployeeSelectManager omEmployeeSelectManager = (OmEmployeeSelectManager)getBean("omEmployeeSelectManager");
        OmEmployee emp = omEmployeeSelectManager.findEmpByOperator(operatorid);
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("workPlanStatus", TrainWorkPlan.STATUS_HANDLING);
	    Page<JobProcessNode> page = new Page<JobProcessNode>();
        if (null == emp.getOrgid()) {
            return -1;
        }
		try {
			page = m.findWorkSeqPutOff(emp.getOrgid().toString(), 0, 1, null, JSONUtil.write(map), null, null);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		}
	    return page.getTotal();
	}
}
