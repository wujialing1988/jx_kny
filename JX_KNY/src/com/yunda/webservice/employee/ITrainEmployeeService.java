package com.yunda.webservice.employee;

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
public interface ITrainEmployeeService {

	/**
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
	public String getEmployeeByEmpID(String empId);
	
	/**
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
	public String getEmployeesByOrgID(String orgId);
    
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
	public String login(String userid, String pwd);
    
    /**
     * <li>说明：根据应用功能编码获取此应用功能下的所有权限列表
     * <li>创建人：程锐
     * <li>创建日期：2014-5-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param appCode 应用功能编码 枚举值，appCode参见：appCode说明
     *  appCode     appCode说明
        TWTClient   台位图客户端应用编码
        PDAClient   PDA客户端应用编码
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

     */
	public String getRoleFuncByAppCode(String appCode, String operatorId);
    
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
    public String loginByUserid(String userid);

    /**
     * <li>说明：通过右键台位图，判断当前车是否属于检修 整备 通用
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return String
     * @throws Exception
     */
    public String getTrainStateByMessage(String jsonObject);
}
