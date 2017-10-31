package com.yunda.frame.baseapp.todojob;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.manager.AcRoleManager;
import com.yunda.frame.yhgl.manager.SysFunctionManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 获取待办事宜的查看权限业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-7-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Service(value = "unTreatedJobPermissionManager")
public abstract class UnTreatedJobPermissionManager <T, S> extends JXBaseManager<T, S> {
    
    /**
     * <li>说明：根据操作者和待办事宜类型及TodoJob功能组的角色权限检测是否有查看权限
     * <li>创建人：程锐
     * <li>创建日期：2015-7-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param jobType 待办类型
     * @return true 能查看 false 不能查看
     */
    public boolean checkPermission(long operatorid, String jobType) {
        AcRoleManager acRoleManager = (AcRoleManager) getBean("acRoleManager");
        List<Object> roleIdList = acRoleManager.findRoleIdByOperatorId(operatorid);
        SysFunctionManager sysFunctionManager = (SysFunctionManager) getBean("sysFunctionManager");
        List<AcFunction> functionList = sysFunctionManager.findByOperatorIdAndAppCode(roleIdList, "TodoJob");
        if (functionList == null || functionList.size() < 1)
            return false;
        for (AcFunction acFunction : functionList) {
            if (jobType.equals(acFunction.getFuncname()))
                return true;
        }
        return false;
    }
    
    /**
     * <li>方法说明：获取Bean 
     * <li>方法名称：getBean
     * <li>@param bean
     * <li>@return
     * <li>return: Object
     * <li>创建人：张凡
     * <li>创建时间：2014-3-27 下午04:48:42
     * <li>修改人：
     * <li>修改内容：
     */
    public Object getBean(String bean) {
        return Application.getSpringApplicationContext().getBean(bean);
    }
    
//    /**
//     * <li>说明：根据操作者和待办事宜类型及TodoJob功能组的角色权限检测是否有查看权限
//     * <li>创建人：程锐
//     * <li>创建日期：2015-7-22
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param operatorid 操作者ID
//     * @param jobType 待办类型
//     * @return true 能查看 false 不能查看
//     */
//    public boolean checkPermissionForZB(long operatorid, String jobType) {
//        AcRoleManager acRoleManager = (AcRoleManager) getBean("acRoleManager");
//        List<Object> roleIdList = acRoleManager.findRoleIdByOperatorId(operatorid);
//        SysFunctionManager sysFunctionManager = (SysFunctionManager) getBean("sysFunctionManager");
//        List<AcFunction> functionList = sysFunctionManager.findByOperatorIdAndAppCode(roleIdList, "ZBTodoJob");
//        if (functionList == null || functionList.size() < 1)
//            return false;
//        for (AcFunction acFunction : functionList) {
//            if (jobType.equals(acFunction.getFuncname()))
//                return true;
//        }
//        return false;
//    }
}
