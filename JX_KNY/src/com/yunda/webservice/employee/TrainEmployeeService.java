package com.yunda.webservice.employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.LogonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcFuncgroup;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.IAcRoleManager;
import com.yunda.frame.yhgl.manager.ISysFunctionManager;
import com.yunda.frame.yhgl.manager.SysFuncGroupManager;
import com.yunda.twt.httpinterface.manager.TwtLoginManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.util.BeanUtils;
import com.yunda.util.PurviewUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.webservice.employee.entity.AcFunctionBean;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
/**
 * <li>标题: 重庆整备管理信息系统
 * <li>说明：此接口用于扩展人员组织机构需对接的方法
 * <li>创建人：easy
 * <li>创建日期：2014-04-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="trainEmployeeWS")
public class TrainEmployeeService implements ITrainEmployeeService {
//    整备ZBClient 检修JXClient 通用CommonClient
    /** 整备 */
    public static final String ZBCLIENT = "ZBClient";
    /** 检修 */
    public static final String JXCLIENT = "JXClient";
    /** 通用 */
    public static final String COMMONCLIENT = "CommonClient";
	
	/**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 操作员Manager对象
     */
    @Autowired
    private AcOperatorManager acOperatorManager;
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
     * TrainAccessAccount业务类,机车出入段台账
     */
    @Resource
    private TrainAccessAccountManager trainAccessAccountManager;
    /**
     * ZbglRdp业务类,机车整备单
     */
    @Resource
    private ZbglRdpManager zbglRdpManager;
    /**
     * 台位图登陆业务类
     */
    @Resource
    private TwtLoginManager twtLoginManager;
    /**
     * 
     * <li>说明：通过人员ID取得人员信息
     * <li>创建人：easy
     * <li>创建日期：2014-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empId ：人员ID
     * @return String
     * @throws Exception
     */
	public String getEmployeeByEmpID(String empId) {
		return null;
	}
    /**
     * 
     * <li>说明：通过班组ID取得班组人员信息
     * <li>创建人：easy
     * <li>创建日期：2014-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param orgId ：班组ID
     * @return String
     * @throws Exception
     */
	public String getEmployeesByOrgID(String orgId) {
		return null;
	}
	/**
     * <li>方法名称：login
     * <li>方法说明：登陆实现方法 
     * <li>@param userid 登陆名 
     * <li>@param pwd 登陆密码 
     * <li>@return 当前登陆用户JSON字符串
     * <li>创建人：easy
     * <li>创建时间：2013-6-20 上午11:57:47
     * <li>修改人：
     * <li>修改内容：
     */
	public String login(String userid, String pwd) {
		AcOperator operator = null;
		try {
			// 查询操作员对象
			operator = acOperatorManager.findLoginAcOprator(userid, LogonUtil
					.getPassword(pwd));
			if (operator == null) {
				return WsConstants.OPERATE_FALSE;// 操作员为空返回错误
			}
			if (PurviewUtil.isSuperUsers(operator)) {
				return "null";// 超级管理员返回null字符串
			}
			return JSONUtil.write(operator);
		} catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
		}
	}    
    
	/**
	 * <li>说明：根据应用功能编码获取此应用功能下的所有权限列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-5-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param appCode 应用功能编码 枚举值，appCode参见：appCode说明
	 *  appCode	    appCode说明
		TWTClient	台位图客户端应用编码
        TWTStation  台位图台位相关应用编码
		PDAClient	PDA客户端应用编码
		Terminal	工位终端应用编码       

	 * @param operatorId 操作者ID
	 * @return 所有权限列表
	 * [{
			funccode: "工长派工",		// 功能编号
			funcname:”工长派工”,		//功能名称
			funcaction:”工长派工”,	//功能调用入口
		},{
			funccode: "调度派工",		// 功能编号
			funcname:”调度派工”,		//功能名称
			funcaction:”调度派工”,	//功能调用入口
		}]

	 */
	@SuppressWarnings("unchecked")
	public String getRoleFuncByAppCode(String appCode, String operatorId) {
        
		try {
			List <Object> roleIdList = acRoleManager.findRoleIdByOperatorId(Long.parseLong(operatorId));
            
            //获取所有的引用功能
			List<AcFunction> functionList = sysFunctionManager.findByOperatorIdAndAppCode(roleIdList, appCode);
            
            //判断当前的应用是否属于整备(机车入段) 检修(查看机车信息，检修作业计划编辑) []
            //通用(台位图操作权限，修改机车状态，确定入段去向，机车列表，查看机车信息，机车出段)
            if ("TWTClient".equals(appCode)) {
                for (AcFunction function : functionList) {
                    //通过组ID查询组名称是否是 整备ZBClient 检修JXClient 通用CommonClient
                    AcFuncgroup acFuncgroup = sysFuncGroupManager.getModelById(function.getFuncgroupid());
                    if (COMMONCLIENT.equals(acFuncgroup.getFuncgroupname())) {
                        function.setFuncname(function.getFuncname() + "," + COMMONCLIENT);
                    }else if (JXCLIENT.equals(acFuncgroup.getFuncgroupname())) {
                        function.setFuncname(function.getFuncname() + "," + JXCLIENT);
                    }else if (ZBCLIENT.equals(acFuncgroup.getFuncgroupname())) {
                        function.setFuncname(function.getFuncname() + "," + ZBCLIENT);
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
    
    /**
     * <li>说明：登陆（台位图）
     * <li>创建人：程锐
     * <li>创建日期：2015-3-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param userid 登陆名
     * @return 人员信息列表
     */
    public String loginByUserid(String userid) {
        OperateReturnMessage message = new OperateReturnMessage();
        
        AcOperator operator = null;
        try {
            // 查询操作员对象
            operator = acOperatorManager.findByUserId(userid);
            if (operator == null) {
                return WsConstants.OPERATE_FALSE;// 操作员为空返回错误
            }
            if (PurviewUtil.isSuperUsers(operator)) {
                return "null";// 超级管理员返回null字符串
            }
            return JSONUtil.write(operator);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：通过右键台位图，判断当前车是否属于检修 整备 通用
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 封装的数据{"trainTypeShortName":"sb001"}
     * @return String json字符串 {"returnState":"CommonClient"}
     * @throws Exception
     */
    public String getTrainStateByMessage(String jsonObject) {
        OperateReturnMessage message = new OperateReturnMessage();
        
        if (StringUtil.isNullOrBlank(jsonObject)) {
            message.setFaildFlag("右键台位图，传递数据为空!");
            return JSONObject.toJSONString(message);
        }
        
        JSONObject ob = JSONObject.parseObject(jsonObject);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
//          获取数据
            String trainTypeShortName = ob.getString("trainTypeShortName");
            map = twtLoginManager.getTrainStateByMessage(trainTypeShortName);
            return JSONObject.toJSONString(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
            return JSONObject.toJSONString(message);
        }
        
    }
}
