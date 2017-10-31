/**
 * <li>文件名：UntreatedJobForQualityCheck.java
 * <li>标题：
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建日期：2014-8-7
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 */
package com.yunda.frame.baseapp.todojob;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;


/**
 * <li>标题：质量检查任务数量统计类（客车检修质量检查）
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-8-7
 * <li>修改人： 程锐
 * <li>修 改时间： 2014-11-27
 * <li>修改内容： 
 * @author PEAK-CHEUNG
 * 
 */
public class UntreatedJobForQualityCheckHC extends UnTreatedJobPermissionManager <TodoJob,TodoJob> implements IUntreatedJob {
    
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
        tdj.setJobType(FUNC_GDZLJC_HC_NAME);
        tdj.setJobText("待处理(" + count + ")");
        tdj.setJobUrl("/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle.jsp?vehicleType=10");
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
     * @param operatorid 操作员ID
     * @return 待处理任务数量 
     */
    private int getCount(long operatorid){
        OmEmployeeSelectManager omEmployeeSelectManager = (OmEmployeeSelectManager)getBean("omEmployeeSelectManager");
        OmEmployee emp = omEmployeeSelectManager.findEmpByOperator(operatorid);
        QCResultQueryManager qCResultQueryManager = (QCResultQueryManager)getBean("qCResultQueryManager");
        int count = 0;
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("vehicleType", "10");
            String query = JSONUtil.write(map);
			count = qCResultQueryManager.getQCCount(emp.getEmpid(),"",query);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		}
		return count;
    }
}
