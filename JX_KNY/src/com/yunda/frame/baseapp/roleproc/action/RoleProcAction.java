package com.yunda.frame.baseapp.roleproc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.yunda.frame.baseapp.roleproc.manager.RoleProcManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcFuncgroup;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.manager.IAcRoleManager;
import com.yunda.frame.yhgl.manager.ISysFunctionManager;
import com.yunda.frame.yhgl.manager.SysFuncGroupManager;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.employee.TrainEmployeeService;
import com.yunda.webservice.employee.entity.AcFunctionBean;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 权限控制器
 * <li>创建人：程锐
 * <li>创建日期：2015-11-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@SuppressWarnings("serial")
public class RoleProcAction extends JXBaseAction<Object, Object, RoleProcManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 角色查询功能接口
     */
    @Resource
    private IAcRoleManager acRoleManager;
    
    /**
     * 系统应用功能查询功能接口
     */
    @Resource
    private ISysFunctionManager sysFunctionManager;
    
    /**
     * 系统应用功能组查询功能接口
     */
    @Resource
    private SysFuncGroupManager sysFuncGroupManager;
    
	/**
	 * <li>说明：查询移动客户端下此登录人的所有权限列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-10
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryRoleFuncList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		List<AcFunctionBean> list = new ArrayList<AcFunctionBean>();
		try {			
			list = this.getManager().queryRoleFuncList();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
	}
	
	/**
	 * <li>说明：查询PDA客户端下此登录人的所有权限列表
	 * <li>创建人：程锐
	 * <li>创建日期：2015-11-6
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryPDARoleFuncList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		List<AcFunctionBean> list = new ArrayList<AcFunctionBean>();
		String clientFlag = getRequest().getParameter("clientFlag");
		try {
			list = this.getManager().queryPDARoleFuncList(clientFlag);
			map.put(Constants.SUCCESS, true);
			map.put("root", list);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
    
    /**
     * <li>说明：根据应用功能编码获取此应用功能下的所有权限列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 所有权限列表
     * @throws Exception
     */
    public String getRoleFuncByAppCode() throws Exception {
            try {
                String appCode = getRequest().getParameter("appCode");
                String operatorId = getRequest().getParameter("operatorId");
                List <Object> roleIdList = acRoleManager.findRoleIdByOperatorId(Long.parseLong(operatorId));
                //获取所有的引用功能
                List<AcFunction> functionList = sysFunctionManager.findByOperatorIdAndAppCode(roleIdList, appCode);
                
                //判断当前的应用是否属于整备(机车入段) 检修(查看机车信息，检修作业计划编辑) []
                //通用(台位图操作权限，修改机车状态，确定入段去向，机车列表，查看机车信息，机车出段)
                if ("TWTClient".equals(appCode)) {
                    for (AcFunction function : functionList) {
                        //通过组ID查询组名称是否是 整备ZBClient 检修JXClient 通用CommonClient
                        AcFuncgroup acFuncgroup = sysFuncGroupManager.getModelById(function.getFuncgroupid());
                        if (TrainEmployeeService.COMMONCLIENT.equals(acFuncgroup.getFuncgroupname())) {
                            function.setFuncname(function.getFuncname() + "," + TrainEmployeeService.COMMONCLIENT);
                        }else if (TrainEmployeeService.JXCLIENT.equals(acFuncgroup.getFuncgroupname())) {
                            function.setFuncname(function.getFuncname() + "," + TrainEmployeeService.JXCLIENT);
                        }else if (TrainEmployeeService.ZBCLIENT.equals(acFuncgroup.getFuncgroupname())) {
                            function.setFuncname(function.getFuncname() + "," + TrainEmployeeService.ZBCLIENT);
                        }
                    }
                }
                List<AcFunctionBean> beanList = BeanUtils.copyListToList(AcFunctionBean.class, functionList);
                return JSONUtil.write(beanList);
            } catch (Exception e) {
                ExceptionUtil.process(e, logger);
                return WsConstants.OPERATE_FALSE;
            }
        }
}
