package com.yunda.jx.webservice.stationTerminal.base;



/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 工位终端接口(重构)
 * <li>创建人：程锐
 * <li>创建日期：2015-7-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
public interface IStationTerminal {
    
    /** webservice.properties 工位终端配置文件名称*/ 
    public static final String WEB_SERVICE_PROPERTIES = "webservice.properties";
    
    /** stationTerminalFunctionName key字符串*/ 
    public static final String STATION_TERMINAL_FUNCTION_NAME = "stationTerminalFunctionName";
    
    /**
     * <li>说明：登录
     * <li>创建人：张凡
     * <li>创建日期：2013-4-27
     * <li>修改人： 程梅
     * <li>修改日期：2016年5月3日16:56:27
     * <li>修改内容：增加登录日志
     * 
     * @param userid 登录名
     * @param pwd 登录密码
     * @param loginLocation   登录位置
     * @return 操作成功与否
     */
    public String login(String operatorid, String operatorPwd, String loginLocation);    
    
    /**
     * <li>说明：工长派工接口
     * <li>创建人：谭诚
     * <li>创建日期：2013-9-22
     * <li>修改人： 王治龙
     * <li>修改日期：2013年12月24日
     * <li>修改内容：返回结果包装
     * @param searchJson 查询条件
     * {
     *      "rdpIDX":"8acf30704154e53d014197162c6d004a",        --兑现单ID
     *      "nodeCaseIDX":"E83625267D065577E04438EAA752862A",   --工艺节点ID
     *      "fixplace_fullname":"和谐D3/车顶/原边高压电压互感器",    --位置
     *      "activity_name":"喷漆预处理",                            --检修活动名称
     *      "workCardName":"车内集尘器除尘",                       --作业工单名称
     *      "haveDefaultPerson":"#1#",                          --已派工（这里采用###来代表是否已派工，方法体内进行字符串替换）
     *      "workStationBelongTeam":"#2#"                       --当前登录人员所属工作组ID
     *      "workers":"#3#",                                    --作业人员
     * }
     * @param start 起始页
     * @param limit 每页条数
     * @param operatorid 操作者ID
     * @param workerName 作业人员姓名
     * @param mode 已派工/未派工 (0 未派工 1为已派工)
     * @return 查询结果
     */
    public String newForemanDispatcher(String searchJson,int start,int limit, String mode,Long operatorid, String workerName);
    
    /**
     * <li>说明：根据查询json获取工长派工查询页面中的生产任务单信息
     * <li>创建人：谭诚
     * <li>创建日期：2013-10-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件Json
     * @param start 起始页
     * @param limit 每页条数
     * @return 生产任务单
     * @throws 抛出异常列表
     */
    public String findRdpByForemanDispatcher(String searchJson, int start, int limit);
    
    /**
     * <li>说明：获取工艺节点数据（工长派工查询）
     * <li>创建人：谭诚
     * <li>创建日期：2013-11-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIdx 上级节点IDX
     * @param rdpIdx 兑现但idx
     * @param operatorid 操作员id
     * @param isDispatcher 已派工/未派工 'y'/'n'
     * @return 工艺节点树
     */
    public String findTecNodeByFormanDispatcher(String parentIdx, String rdpIdx, Long operatorid, String isDispatcher);
    
    /**
     * <li>方法名称：findTeamEmp
     * <li>方法说明：获取工作派工作业人员 
     * <li>@param operatorid
     * <li>@param workCardIdx
     * <li>@param mode
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-1 下午02:35:49
     * <li>修改人：
     * <li>修改内容：
     */
    public String findTeamEmp(Long operatorid, String workCardIdx, String mode);
    
    /**
     * <li>方法名称：saveDispatching
     * <li>方法说明：保存工长派工信息 
     * <li>@param operatorid
     * <li>@param workCardIdx
     * <li>@param empids
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-1 下午02:58:58
     * <li>修改人：
     * <li>修改内容：
     */
    public String saveDispatching(Long operatorid, String workCardIdx, String empids);
    
    /**
     * <li>说明：产品化新增批量工长派工公用方法
     * <li>创建人：王治龙
     * <li>创建日期：2014-3-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param searchJson 工长派工界面查询参数
     * @param workCardIds 作业工单ID集合
     * @param empids 派工人员ID集合
     * @param dispatchType 派工类型
     * 派工类型：
     *'batch'：选择多条记录批量派工；
     *'defaultBatch'：选择多条记录默认上次派工；
     *'allBatch'：根据查询条件全部批量派工；
     * @return String
     */
    public String saveBatchDispatch(Long operatorid, String searchJson, String workCardIds,String empids,String dispatchType);
        
    /**
     * <li>方法名称：getWorkSeqDelay
     * <li>方法说明：工序延误 
     * <li>@param operatorid
     * <li>@param start
     * <li>@param limit
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-8 下午02:41:10
     * <li>修改人：
     * <li>修改内容：
     */
    public String getWorkSeqDelay(Long operatorid, int start,int limit);
    
    /**
     * <li>方法名称：getDelayInfo
     * <li>方法说明：获取延迟信息 
     * <li>@param nodeCaseIdx
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-8 下午04:09:06
     * <li>修改人：
     * <li>修改内容：
     */
    public String getDelayInfo(String nodeCaseIdx);
    
    /**
     * <li>方法名称：saveWorkSeqDelay
     * <li>方法说明：保存工序延误信息 
     * <li>@param operatorid
     * <li>@param nodeCaseIdx
     * <li>@param idx
     * <li>@param delayReason
     * <li>@param delayType
     * <li>@param delayTime
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-8 下午04:08:20
     * <li>修改人：
     * <li>修改内容：
     */
    public String saveWorkSeqDelay(Long operatorid, String nodeCaseIdx, String idx, String delayReason, String delayType, int delayTime);
    
    /**
     * <li>方法名称：getDict
     * <li>方法说明：获取字典数据 
     * <li>@param dicttypeid
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-8 下午02:59:22
     * <li>修改人：
     * <li>修改内容：
     */
    public String getDict(String dicttypeid);
        
    /**
     * <li>方法说明：根据班组ID查询班组人员 
     * <li>方法名称：findTeamEmps
     * <li>@param orgid
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-9-13 上午10:06:29
     * <li>修改人：
     * <li>修改内容：
     */
    public String findTeamEmps(Long orgid);
            
    /**
     * <li>方法名称：saveCardNo
     * <li>方法说明：保存CardNo 
     * <li>@param userId
     * <li>@param cardNo
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-20 下午08:21:42
     * <li>修改人：
     * <li>修改内容：
     */
    public String saveCardNo(String userId, String cardNo);
    
    /**
     * <li>方法名称：loginByCardNo
     * <li>方法说明：根据卡号登陆 
     * <li>@param cardNo
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-20 下午08:28:16
     * <li>修改人：
     * <li>修改内容：
     */
    public String loginByCardNo(String cardNo, String loginLocation);
    
    
    /**
     * <li>方法说明：任务数量统计 
     * <li>方法名称：findTaskCount
     * <li>@param operatorid
     * <li>@param model
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-31 下午04:57:47
     * <li>修改人：
     * <li>修改内容：
     */
    public String findTaskCount(Long operatorid, String model);
    
    /**
     * <li>说明：根据操作员ID查询人员EMPID
     * <li>创建人：张凡
     * <li>创建日期：2014-8-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 人员EMPID
     */
    public Long getEmployee(Long operatorid);
    
    /**
     * <li>说明：获取作业工单信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 作业工单IDX
     * @return 作业工单信息
     */
    public String workCard(String idx);
    
    /**
     * <li>说明：获取作业任务信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员主键
     * @param workCardIdx 作业工单主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @param mode 标识是未处理还是已处理的作业任务；1表示已处理。
     * @return 作业任务信息
     */
    @SuppressWarnings("unchecked")
    public String workTask(Long operatorid, String workCardIdx, int start, int limit, String mode);
    
    /**
     * <li>说明：修改登录密码
     * <li>创建人：程锐
     * <li>创建日期：2015-7-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param newPWD 新密码
     * @return 操作成功与否
     */
    public String updateOperatorPWD(long operatorid, String newPWD);
    
    /**
     * <li>说明：获取登录人同一班组的人员列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 登录人同一班组的人员列表
     */
    public String findEmpList(Long operatorid);    

    /**
     * <li>方法名称：getProcessTaskList
     * <li>方法说明：获取任务列表 
     * <li>@param uid
     * <li>@param uname
     * <li>@param start
     * <li>@param limit
     * <li>@param mode
     * <li>@param queryString
     *  {
     *      "trainType":"",
     *      "trainNo":"",
     *      "workItemName":"",
     *      "taskDepict":""
     *  }
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-5 下午06:32:11
     * <li>修改人：程锐
     * <li>修改内容：添加queryString查询字符串按工作项名称、车型、车号、任务描述等模糊查询
     */
//    public String getProcessTaskList(String uid, String uname, int start, int limit, String mode, String queryString);
    
    /**
     * <li>说明：根据operatorid获取影响生产因素处理任务记录
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-5-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String json字符串
     * {
     * 	operatorid:426,			//操作员id，长整数
     * 	start:0,				//开始行
     *  limit:20,				//结束行
     * }
     * @return String json字符串
	 * {
	 * 	id:"40283e813ed0cc90013ed0f045cb0007",				//记录的主键字段名称
	 *  root: [												//记录列表，InfluFactorSolution实体类转换成的json字符串
		 {
		 	"idx":"40283e813eda4345013eda6783630007","influFactorIDX":"40283e813eda4345013eda67835b0006","orgID":353,"orgName":"机车检修部",
		 	"receivePerson":813,"receivePersonName":"李国强","receiveTime":1369464000000,"solution":"调整配件","planSolveTime":1369478460000,
		 	"confirmPerson":813,"confirmPersonName":"李国强","confirmTime":1369485720000,"stauts":30,
		 	"siteID":"TJJD","recordStatus":0,"creator":1,"createTime":1369463817000,"updator":221,
		 	"updateTime":1369464192000,"trainTypeShortName":null,"trainNo":null,"repairClassName":null,
		 	"nodeCaseName":null,"factorType":"缺料","factorReason":"没有配件"
		 }
	 *  ],				
	 *  totalProperty:135		//记录总数
	 * }
     * @throws 抛出异常列表
     */
//    public String getFactorSolution(String json);
    /**
     * <li>说明：工位终端接口，影响生产因素处理 接受任务
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-5-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param Long operatorid //操作员id，长整数
     * @param String json字符串
     * {
		idx:"40283e813ed0cc90013ed0f045cb0007",					//记录idx主键
		receivePerson:"",		//接受人empid主键
		receivePersonName:"",	//接受人名称
		receiveTime:"",			//接受时间
		solution:"",			//解决方案
		planSolveTime:""		//预计完成时间
     * }
     * @return String json字符串
	 * {
	 * 	success:true,			//处理结果，true成功，false失败
	 *  errMsg:"",				//处理失败success==false时，errMsg错误信息， 当success=true时errMsg空
	 * }
     * @throws 抛出异常列表
     */	
//	public String acceptFactorSolution(Long operatorid, String json);
    /**
     * <li>说明：工位终端接口，影响生产因素处理 完成确认
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-5-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param Long operatorid //操作员id，长整数
     * @param String json字符串
     * {
		idx:"40283e813ed0cc90013ed0f045cb0007",					//记录idx主键
		confirmPerson:"",		//确认人empid主键
		confirmPersonName:"",	//确认人名称
		confirmTime:"",			//确认时间
     * }
     * @return String json字符串
	 * {
	 * 	success:true,			//处理结果，true成功，false失败
	 *  errMsg:"",				//处理失败success==false时，errMsg错误信息， 当success=true时errMsg空
	 * }
     * @throws 抛出异常列表
     */	
//	public String confirmFactorSolution(Long operatorid, String json);
	
    
    
	/**
     * <li>说明：根据工位终端编码返回当前工位终端软件所绑定的台位列表
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-6-27
     * @param String terminalID 工位终端ID
     * @return 台位信息列表，如果没有找到数据返回false
     * [
     * 	{
     * 		idx:"2efe1e16-958c-44a4-a7e4-ed2928636c6b",	//台位GUID
     * 		stationCode:"10",							//台位编码
     * 		stationName:"C6-组装3",						//台位名称
     * 		ownerWarehouse:"C",							//所属库，该台位是属于A库还是B库
     * 		ownerMap:"天津电力机车有限公司"					//所属图名称，该台位属于哪张台位图
     * 	},
     * 	{idx:"4977f388-748e-4a5b-ac32-c588761e5491",stationCode:"7",stationName:"C6-检查2",ownerWarehouse:"C",ownerMap:"天津电力机车有限公司"}	
     * ]
     */
//    public String getPosition(String terminalID);
	/**
     * <li>说明：调用台位图服务接口，移动机车到指定的台位
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String positionGuid 移动到目的台位的guid
     * @param String trainType 车型
     * @param String trainNumber 车号 
     * @return String 移动操作是否成功  true|false 机车移动失败的原因肯定是：当前机车不在台位上！
     */
//    public String moveTrain(String positionGuid, String trainType, String trainNumber);
    
    /**
     * <li>说明：根据操作员返回当前可以移动的车型、车号列表（根据用户所在部门来过滤）
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String operatorid 操作员id
     * @return String 车型、车号的JSON，如果没有找到数据返回false
     * [
     * 	{trainType:"HXD3", trainNumbers:["1234","5678"]},
     * 	{trainType:"HXD3C", trainNumbers:["5521","2048"]}
     * ]
     */
//    public String getTrainTypeNumber(String operatorid);

    /**
     * <li>说明：根据车型、车号获取当前机车所在的台位
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String trainType 车型
     * @param String trainNumber 车号
     * @return String 返回机车当前所在的台位名称，如果机车未在台位上返回false
     */
//    public String getCurrentPosition(String trainType, String trainNumber);
    
    /**
     * 
     * <li>说明：申请转临修
     * <li>创建人：easy
     * <li>创建日期：2013-6-27
     * <li>@param sourceIDX  申请票ID
     * <li>@param uid  登录人ID
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String sourceIDX 提票ID
     * @return void
     * @throws Exception
     */
//    public String applyLX(String sourceIDX,String uid,String businessIDX,Long workitemid,String workItemName,Long processInstID);
    
    /**
	 * <li>说明：找到此申请单中通过flag 找到此申请单中对应的票据 ，untake为未搭载 ，take为搭载， 
	 * <li>创建人：easy
	 * <li>创建日期：2012-08-07
	 * <li>@param rdpIDX  兑现单ID
	 * <li>@param flag 找到此申请单中对应的票据 ，untake为未搭载 ，take为搭载， 
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	 
//	public String pageUnTakeFaultNotice(String rdpIDX,String flag);
	
	/**
	 * 
	 * <li>说明：保存转临修票关系
	 * <li>创建人:EASY
	 * <li>创建日期：2013-06-27
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * <li>@param  uid 人员ID
     * <li>@param workitemid 工作ID
     * <li>@param processInstID 流程ID
     * <li>@param workItemName 工作点名称
     * <li>@param businessIDX taskID
      <li>@param idsStr 搭载票IDX
     * @param rdpIDX 兑现单
	 * <li>修改人：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */ 
//	public String saveFaultNoticeLXRelation(Long uid, Long workitemid, Long processInstID, String workItemName, String businessIDX,String rdpIDX,String idsStr);
	
	/**
	 * <li>说明：检查范围活是否做完，做完才能转临修
	 * <li>创建人：easy
	 * <li>创建日期：2013-06-27
	 * <li>@param rdpIDX  兑现单ID
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
//	public String checkApplyLX(String rdpIDX);
	
	/**
	 * <li>说明：转临修
	 * <li>创建人：easy
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * <li>@param  uid 人员ID
     * <li>@param workitemid 工作ID
     * <li>@param processInstID 流程ID
     *<li>@param workItemName 工作点名称
     *<li>@param businessIDX taskID
     *<li>@param idsStr 搭载票IDX
	 *<li>@param businessIDX taskID
     *<li>@param idsStr 搭载票IDX
     *<li>@param applyData 申请单实体
     *<li>@param isDC 是否调车 1要 0不
     *<li>@param isLX 是否临修 1要 0不
     *<li>@param lxType 临修类型
     *<li>@param fixDepId 承修部门ID
     *<li>@param fixDepName 承修部门名称
     *<li>@param turnWayRoad 调车道
     *<li>@param turnWayStation 调车位
     *<li>@param turnWayReason  调车原因
     *<li>@param turnWayTime  调车时间
     * @param rdpIDX 兑现单
	 * @return void
	 * @throws Exception
	 */
	
//	public String toLX(Long uid, Long workitemid, Long processInstID, String workItemName, String businessIDX,String lxType,String fixDepId,String fixDepName,String turnWayRoadm,String turnWayStation,String turnWayReason,String turnWayTime,String rdpIdx,String isDC,String isLX);
	
    /**
     * <li>说明：根据RDPIDX查找调车单
     * <li>创建人：easy
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * <li>@param rdpIDX  兑现单ID
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws 抛出异常列表
     */
//    public String getTrainTurnWayRdp(String rdpIDX);
    
	
	/**
	 * <li>说明：转临修
	 * <li>创建人：easy
	 * <li>创建日期：2012-08-07
     * <li>@param  uid 人员ID
     * <li>@param workitemid 工作ID
     * <li>@param processInstID 流程ID
     * <li>@param workItemName 工作点名称
     * <li>@param businessIDX taskID
     * @param rdpIDX 兑现单
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */ 
//    public String finishTrainTurnWayRdp(Long uid, Long workitemid, Long processInstID, String workItemName, String businessIDX,String rdpIDX);

    
    /**
     * <li>方法名称：getLXTYPE
     * <li>方法说明：获取临修类型 
     * <li>@return
     * <li>return: String
     * <li>创建人：easy
     * <li>创建时间：2013-5-3 下午03:11:21
     * <li>修改人：
     * <li>修改内容：
     */
//    public String getLXTYPE();
    
    


    /**
     * <li>说明： 普查整治(派工页面)
     * <li>创建人：程锐
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 登录人ID
     * @param start 开始行数
     * @param limit 分页行数
     * @param depID 判断是否派工 is null 未派工 is not null 已派工
     * @return 分页列表字符串
     */
//    public String pageTrainInspectResult(Long operatorid, int start, int limit, String depID);
    /**
     * 
     * <li>说明：普查整治-工长派工页面
     * <li>创建人：程锐
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     
     * @param operatorid 登录人ID
     * @param start 开始行数
     * @param limit 分页行数
     * @param hasInspectPerson false 未派工 true 已派工
     * @return 分页列表字符串
     */
//    public String pageTrainInspectResultForeman(Long operatorid, int start, int limit, String hasInspectPerson);
    /**
     * 
     * <li>说明：普查整治-处理页面列表
     * <li>创建人：程锐
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 登录人ID
     * @param start 开始行数
     * @param limit 分页行数
     * @param inspectStatus 0 待处理  2 已处理
     * @return 分页字符串
     */
//    public String pageTrainInspectResultHandle(Long operatorid, int start, int limit, String inspectStatus);
    /**
     * 
     * <li>说明：普查整治-处理人员列表页面
     * <li>创建人：程锐
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     
     * @param operatorid 登录人ID
     * @param start 开始行数
     * @param limit 分页行数
     * @param inspectResultId 普查整治项目ID
     * @return 分页字符串
     */
//    public String pageTrainInspectPersonHandle(Long operatorid, int start, int limit, String inspectResultId);
    
    /**
     * 
     * <li>说明：普查整治(派工处理)
     * <li>创建人：程锐
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param depID 派工班组id
     * @param depSeq 
     * @param depName
     * @param ids
     * @return 成功与否
     */
//    @SuppressWarnings("all")
//    public String saveTrainInspectResultDDPG(String depID, String depSeq, String depName, String[] ids);
    /**
     * 
     * <li>说明：普查整治(工长派工处理)
     * <li>创建人：程锐
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param inspectResultIdx 
     * @param empids 
     * @param operatorid
     * @return 成功与否
     */
//    @SuppressWarnings("all")
//    public String updateForemanDispater(String inspectResultIdx, String empids, Long operatorid);
    
    
    /**
     * 
     * <li>说明：普查整治(任务处理)
     * <li>创建人：程锐
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param inspectResultIdx 
     * @param inspectResult 
     * @param inspectDate
     * @param operatorid 
     * @return 成功与否
     */
//    public String updateForhandle(String inspectResultIdx, String inspectResult, String inspectDate, Long operatorid);
    /**
     * 
     * <li>说明：工长派工选人
     * <li>创建人：程锐
     * <li>创建日期：2013-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param inspectResultIdx
     * @param operatorid
     * @param isDispatcher
     * @param start
     * @param limit
     * @return void
     * @throws Exception
     */
//    public String findPczzDataList(String inspectResultIdx,Long operatorid,boolean isDispatcher,int start,int limit);
    
    
     /**
     * 
     * <li>说明：影响生产因素--填写原因分页列表
     * <li>创建人：程锐
     * <li>创建日期：2013-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始行数
     * @param limit 分页行数
     * @param status 状态，10：待通知；20：已通知
     * @return 分页字符串 如无数据则返回{}
     */
//    public String influFactorPageList( int start, int limit, String status);
    /**
     * 
     * <li>说明：影响生产因素--填写原因--获取可选取的责任部门列表（天津电力机车有限公司下）
     * <li>创建人：程锐
     * <li>创建日期：2013-7-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return 责任部门列表json字符串
     */
//    public String orgList();
    /**
     * 
     * <li>说明：影响生产因素--填写原因--选择工艺节点--获取承修机车
     * <li>创建人：程锐
     * <li>创建日期：2013-7-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始行数
     * @param limit 分页行数
     * @return 分页字符串 如无数据则返回{}
     */
//    public String rdpListForInflufactor( int start, int limit);
    /**
     * 
     * <li>说明：影响生产因素--填写原因--选择工艺节点--获取工艺节点列表
     * <li>创建人：程锐
     * <li>创建日期：2013-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始行数
     * @param limit 分页行数
     * @param rdpIdx 机车兑现单主键
     * @param status 工艺节点状态 NOTSTARTED未启动 RUNNING运行 COMPLETED完成 TERMINATED终止
     * @return 分页字符串 如无数据则返回{}
     */
//    public String nodeCaseListForInflufactor( int start, int limit, String rdpIdx, String status);
    /**
     * <li>说明：工位终端接口，新增或更新影响生产进度的因素记录，同时要保存从表“影响生产进度因素处理”的数据
     * <li>创建人：程锐
     * <li>创建日期：2013-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param Long operatorid //操作员id，长整数
     * @param String factorjson字符串
     * {
        idx:"",                 //记录idx主键,值""表示新增，值不为空表示编辑
        factorType:"",            //类型
        factorReason:"",        //原因
        nodeCaseIdx:"",        //工艺节点实例主键
        nodeIDX:"",            //工艺节点主键
        tecProcessCaseIDX:"",  //工艺实例主键
        rdpIDX:""              //兑现单主键
     * }
     * @param String factorSolutionjson字符串
     * [{
     *     orgID:,//长整数 责任部门ID
     *     orgName:""//责任部门名称 
     * }]
     * @return String json字符串
     * {
     *  success:true,           //处理结果，true成功，false失败
     *  errMsg:"",              //处理失败success==false时，errMsg错误信息， 当success=true时errMsg空
     * }
     * @throws 抛出异常列表
     */
//    public String saveFactorAndSolution(Long operatorid, String factorjson, String factorSolutionjson);
    /**
     * 
     * <li>说明：提交修改影响生产进度的因素记录状态，通知各责任部门下的所有人员接受任务
     * <li>创建人：程锐
     * <li>创建日期：2013-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 影响生产进度的因素实体对象主键数组
     * @return String json字符串
     * {
     *  success:true,           //处理结果，true成功，false失败
     *  errMsg:"",              //处理失败success==false时，errMsg错误信息， 当success=true时errMsg空
     * }
     */
//    @SuppressWarnings("all")
//    public String submitNotify(String[] ids);
    /**
     * 
     * <li>说明：逻辑删除影响生产进度的因素记录及相关的影响生产进度因素处理的数据
     * <li>创建人：程锐
     * <li>创建日期：2013-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 影响生产进度的因素实体对象主键数组
     * @return String json字符串 
     * {
     *  success:true,           //处理结果，true成功，false失败
     *  errMsg:"",              //处理失败success==false时，errMsg错误信息， 当success=true时errMsg空
     * }
     */
//    @SuppressWarnings("all")
//    public String deleteFactorAndSolu(String[] ids);
    
   /**
     * <li>说明：获取影响生产因素类型
     * <li>创建人：程锐
     * <li>创建日期：2013-7-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
//    public String getFactorTypes();
    
    /**
     * <li>说明：审核转临修
     * <li>创建人：王治龙
     * <li>创建日期：2013-11-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid：操作员ID
     * @param jsonData：审核信息JSON对象
     * @return String
     */
//    public String verifyToLX(Long operatorid,String jsonData);
    /**
     * <li>说明：查询转临修的提票数据
     * <li>创建人：王治龙
     * <li>创建日期：2013-11-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx：兑现单主键
     * @return String
     */
//    public String findTakeToLXFaults(String rdpIdx);
//    /**
//	 * <li>说明：保存搭载工作票，并完成工作项-工位终端方法
//	 * <li>创建人：王治龙
//	 * <li>创建日期：2013-11-26
//	 * <li>修改人： 
//	 * <li>修改日期：
//	 * <li>修改内容：
//	 * @param idsStr：搭载票IDX信息集合
//	 * @param rdpIDX：兑现单主键
//	 * @param disposeData：处理意见对象Json字符串
//	 * @return String
//	 */
//    public String saveTakeFaultNoticeAndFinishWorkItem(String idsStr,String rdpIDX,String disposeData);
	
    
    /**
     * <li>方法说明：提票调度派工方式 
     * <li>方法名称：getFaultDispatcherMode
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-8-28 下午02:08:19
     * <li>修改人：
     * <li>修改内容：
     */
//    public String getFaultDispatcherMode();
    
    /**
     * <li>方法说明：获取工位终端链接配置文件信息
     * <li>方法名称：getstationTerminalInfo
     * <li>创建人：林欢
     * <li>创建时间：2016-5-20 下午02:08:19
     * <li>修改人：
     * <li>修改内容：
     * @return json字符串
     * 格式:{stationTerminalIp : "www.baidu.com",stationTerminalFunctionName:"linhuanTest"}
     */
//    public String getstationTerminalInfo();
    
    
    /**
     * <li>说明：工位终端未登陆时获取各模块【未处理】数据数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    public String getMessageForUndoList();
   
}

