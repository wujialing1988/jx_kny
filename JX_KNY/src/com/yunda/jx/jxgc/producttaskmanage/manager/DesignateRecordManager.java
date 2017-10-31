package com.yunda.jx.jxgc.producttaskmanage.manager;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.DesignateRecord;
import com.yunda.jx.util.MixedUtils;
/**
 * <li>标题：派工记录业务类
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-2-19
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
@Service(value="designateRecordManager")
public class DesignateRecordManager extends JXBaseManager<DesignateRecord, DesignateRecord> {
        
    /**
     * <li>方法说明：新增工单派工记录 
     * <li>方法名称：saveWorkRecord
     * <li>@param type
     * <li>@param workCardIdx
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-7-17 下午05:07:20
     * <li>修改人：yuanj
     * <li>修改内容：增加短信通知方法
     */
    public void saveWorkRecord(String type, String... workCardIdx){
        
        saveRecord(type, "jxgc_work_card", workCardIdx);
    }

    /**
     * <li>方法说明：通用新增派工记录方法 
     * <li>方法名称：saveRecord
     * <li>@param type
     * <li>@param table
     * <li>@param idx
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-7-17 下午05:08:54
     * <li>修改人：
     * <li>修改内容：
     */
    private void saveRecord(String type, String table, String[] idx){
        if(idx.length == 0){
            return;
        }
        OmEmployee emp = getCurrentUser();
        if(emp==null){
        	return;
        }
        String insert = SqlMapUtil.getSql("jxgc-gdgl2:insertDesignateRecord");
        StringBuilder select = new StringBuilder("select sys_guid(), t.idx, '");
        select.append(type);
        select.append("',");
        select.append(emp.getEmpid());
        select.append(",");
        select.append("'");
        select.append(emp.getEmpname());
        select.append("',");
        select.append("'");
        select.append(emp.getEmpcode());
        select.append("', sysdate, 0, '");
        select.append(JXConfig.getInstance().getSynSiteID());
        select.append("', ");
        select.append(emp.getOperatorid());
        select.append(", sysdate, ");
        select.append(emp.getOperatorid());
        select.append(", sysdate from ");
        select.append(table);
        select.append(" t where t.idx in (");        
        
        MixedUtils.execInSql(insert + select, ")", idx, daoUtils);
    }

    /**
     * <li>方法说明：获取当前操作人员 
     * <li>方法名称：getCurrentUser
     * <li>@return
     * <li>return: OmEmployee
     * <li>创建人：张凡
     * <li>创建时间：2014-2-19 下午02:40:44
     * <li>修改人：
     * <li>修改内容：
     */
    private OmEmployee getCurrentUser() {
        OmEmployee emp = SystemContext.getOmEmployee();
        if(emp == null){
            OmEmployeeSelectManager m = (OmEmployeeSelectManager)Application.getSpringApplicationContext().getBean("omEmployeeSelectManager"); 
            emp = m.findEmpByOperator(SystemContext.getAcOperator().getOperatorid());
        }
        return emp;
    }
    
}
