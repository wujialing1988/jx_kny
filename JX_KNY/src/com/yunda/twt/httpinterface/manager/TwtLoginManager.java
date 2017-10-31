package com.yunda.twt.httpinterface.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcFuncgroup;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.IAcRoleManager;
import com.yunda.frame.yhgl.manager.ISysFunctionManager;
import com.yunda.frame.yhgl.manager.SysFuncGroupManager;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.util.BeanUtils;
import com.yunda.util.PurviewUtil;
import com.yunda.webservice.employee.TrainEmployeeService;
import com.yunda.webservice.employee.entity.AcFunctionBean;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 台位图登陆业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-12-8
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Service(value = "twtLoginManager")
public class TwtLoginManager extends JXBaseManager<Object, Object>{
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
     * 机车出入段台账查询业务类
     */
    @Resource
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
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
     * <li>说明：台位图登陆
     * <li>创建人：程锐
     * <li>创建日期：2015-12-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param userid 用户登陆名
     * @return 操作员对象
     * @throws Exception
     */
    public AcOperator loginByUserid(String userid) throws Exception {
        AcOperator operator = acOperatorManager.findByUserId(userid);
        if (operator == null)
            throw new BusinessException("操作员对象为空");
        if (PurviewUtil.isSuperUsers(operator)) {
            return null;// 超级管理员返回null字符串
        }
        return operator;
    }
    
    /**
     * <li>说明：根据应用功能编码获取此应用功能下的所有权限列表
     * <li>创建人：程锐
     * <li>创建日期：2015-12-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param appCode 应用功能编码 枚举值，appCode参见：appCode说明
     *  appCode     appCode说明
        TWTClient   台位图客户端应用编码
        TWTStation  台位图台位相关应用编码
        Terminal    工位终端应用编码       

     * @param operatorId 操作者ID
     * @return 所有权限列表
     * [{
            funccode: "工长派工",       // 功能编号
            funcname:”工长派工”,        //功能名称
            funcaction:”工长派工”,  //功能调用入口
        },{
            funccode: "调度派工",       // 功能编号
            funcname:”调度派工”,        //功能名称
            funcaction:”调度派工”,  //功能调用入口
        }]     
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<AcFunctionBean> getRoleFuncByAppCode(String appCode, String operatorId) throws Exception {
        List <Object> roleIdList = acRoleManager.findRoleIdByOperatorId(Long.parseLong(operatorId));
        List<AcFunction> functionList = sysFunctionManager.findByOperatorIdAndAppCode(roleIdList, appCode);
       
//      判断当前的应用是否属于整备(机车入段) 检修(查看机车信息，检修作业计划编辑) []
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
        return beanList;
        
    }

    /**
     * <li>说明：通过右键台位图，判断当前车是否属于检修 整备 通用
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAliasName 车型名称简写
     * @return Map<String, Object> 封装数据 只有一个键值对 key：returnState value:(TrainEmployeeService.COMMONCLIENT,TrainEmployeeService.JXCLIENT,TrainEmployeeService.ZBCLIENT)
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * @throws Exception
     */
    public Map<String, Object> getTrainStateByMessage(String trainAliasName) throws JsonParseException, JsonMappingException, IOException {
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("returnState", TrainEmployeeService.COMMONCLIENT);
        
        TrainAccessAccount account = new TrainAccessAccount();
        account.setTrainAliasName(trainAliasName);
        TrainAccessAccount trainAccessAccount = trainAccessAccountQueryManager.getAccountByTrainInfo(JSONUtil.write(account));
        if (trainAccessAccount == null) {
            throw new BusinessException("台位图右键，机车出入段台账对象查询为空!");
        }
        //判断车是否属于检修
        if (trainAccessAccountManager.isRunningTrainWorkPlan(trainAccessAccount)) {
            map.put("returnState", TrainEmployeeService.JXCLIENT);
        }
        //判断车是否属于整备
        ZbglRdp zbglRdp = zbglRdpManager.getRunningRdpByTrain(trainAccessAccount.getTrainTypeShortName(), trainAccessAccount.getTrainNo());
        if (zbglRdp != null) {
            map.put("returnState", TrainEmployeeService.ZBCLIENT);
        }
        
        return map;
    }
    
}
