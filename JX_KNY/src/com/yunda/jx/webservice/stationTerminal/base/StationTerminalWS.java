package com.yunda.jx.webservice.stationTerminal.base;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.xfire.transport.http.XFireServletController;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.LogonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.LoginLog;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.frame.yhgl.manager.LoginLogManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.frame.yhgl.manager.OperatorManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.NodeCaseDelay;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.ProcessTaskManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkTaskManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketStatisticsTotalVo;
import com.yunda.jx.jxgc.tpmanage.manager.FaultQCResultQueryManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckAffirmManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckRecordManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeQueryManager;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpQueryManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRQueryManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeBean;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeQueryManager;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.webservice.stationTerminal.base.entity.AcOperatorBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.FindRdpByForemanDispatcherBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.FindTeamEmpBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.NewForemanDispatcherBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.NodeCaseDelayBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkCardBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkSeqDelayBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;
import com.yunda.util.BeanUtils;
import com.yunda.util.PurviewUtil;
import com.yunda.webservice.common.WsConstants;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 工位终端接口实现类(重构)
 * <li>创建人：程锐
 * <li>创建日期：2015-7-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Service(value="stationTerminalWS")
public class StationTerminalWS implements IStationTerminal {
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    private static final String ROOT = "root";
    private static final String ONE = "1";
    private static final String TOTALPROPERTY = "totalProperty";
    
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
    public String login(String userid, String pwd, String loginLocation) {
        AcOperator operator = null;
        try {
            operator = getAcOperatorManager().findLoginAcOprator(userid, LogonUtil.getPassword(pwd));
            if (operator == null) {
                return "{'error':'用户名和密码错误！'}";// 操作员为空返回空字符串
            }
            //新增登录日志记录
            LoginLog log = new LoginLog();
            log.setOperatoridx(operator.getOperatorid().toString());
            log.setUserid(userid);
            log.setOperatorname(operator.getOperatorname());
            log.setLoginInTime(new Date());
            log.setLoginType(LoginLog.TYPE_SGDL);//手工登录
            log.setLoginClient(LoginLog.CLIENT_GWZD);//工位终端登录
            HttpServletRequest request = XFireServletController.getRequest();
            String remoteAddr = request.getRemoteAddr();
            log.setIp(remoteAddr);
            log.setLoginLocation(loginLocation);
//            log.setIp(InetAddress.getLocalHost().getHostAddress()) ;//获取ip地址
            this.getLoginLogManager().saveOrUpdate(log);//新增登录日志
            if (PurviewUtil.isSuperUsers(operator)) {
                return "null";// 超级管理员返回null字符串
            }
            AcOperatorBean acOperatorBean = new AcOperatorBean();
            BeanUtils.copyProperties(acOperatorBean, operator);
            return JSONUtil.write(acOperatorBean);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            // 发生异常返回错误
            return "{'error':'登录失败！'}";
        }
    }
    
    /**
     * <li>说明：工长派工接口
     * <li>创建人：谭诚
     * <li>创建日期：2013-9-22
     * <li>修改人： 王治龙
     * <li>修改日期：2013年12月24日
     * <li>修改内容：返回结果包装
     * @param searchJson 查询条件
     * {
	 *		"rdpIDX":"8acf30704154e53d014197162c6d004a",		--兑现单ID
	 *		"nodeCaseIDX":"E83625267D065577E04438EAA752862A",	--工艺节点ID
	 *		"fixplace_fullname":"和谐D3/车顶/原边高压电压互感器",	--位置
	 *		"activity_name":"喷漆预处理",							--检修活动名称
	 *		"workCardName":"车内集尘器除尘",						--作业工单名称
	 *		"haveDefaultPerson":"#1#",							--已派工（这里采用###来代表是否已派工，方法体内进行字符串替换）
	 *		"workStationBelongTeam":"#2#"						--当前登录人员所属工作组ID
	 *		"workers":"#3#",									--作业人员
	 * }
     * @param start 起始页
     * @param limit 每页条数
     * @param operatorid 操作者ID
     * @param workerName 作业人员姓名
     * @param mode 已派工/未派工 (0 未派工 1为已派工)
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
	public String newForemanDispatcher(String searchJson,
									   int start,
									   int limit, 
									   String mode,
									   Long operatorid, 
									   String workerName){
    	if(start>0) start--;//传过来的第一页为1，而查询需要从0开始
        try {
        	Page pageList = new Page();
        	if(!StringUtil.isNullOrBlank(searchJson)){
        		//#1#：将代表派工状态的Json字符###，根据参数mode的值（mode==null代表未派工，mode==1代表已派工），转换为张凡业务方法内需要的表达式
	        	searchJson = searchJson.replace("#1#", (ONE.equals(mode) ? ONE:"$[OR]$0]|[$IS NULL$"));
	        	//#2#：因工位终端不能提供当前登录人所属部门（班组），只能提供当前操作者的ID。所以根据传入的参数operatorid，调用组织机构查询接口，根据操作员ID获取其所属组织机构（班组）ID
	        	Long teamId = getOmEmployeeManager().findByOperator(operatorid).getOrgid();
	        	searchJson = searchJson.replace("#2#", String.valueOf(teamId));
                if( StringUtil.isNullOrBlank(workerName) && searchJson.indexOf(",\"workers\":\"#3#\"")!=-1){
                    searchJson = searchJson.replace(",\"workers\":\"#3#\"", "");
                }
	            /**返回结果包装*/
	            Page<WorkCard> page = getWorkCardManager().findWorkCard(searchJson, start * limit, limit, null, mode, null) ;
	            List<NewForemanDispatcherBean> list = new ArrayList<NewForemanDispatcherBean>();
	            list = BeanUtils.copyListToList(NewForemanDispatcherBean.class, page.getList());
	            pageList = new Page(page.getTotal(),list);
        	}
        	return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：根据查询json获取工长派工查询页面中的生产任务单信息
     * <li>创建人：谭诚
     * <li>创建日期：2013-10-18
     * <li>修改人： 王治龙
     * <li>修改日期：2013-12-24
     * <li>修改内容：返回结果包装
     * @param searchJson 查询条件Json
     * @param start 起始页
     * @param limit 每页条数
     * @return 生产任务单
     */
    @SuppressWarnings("unchecked")
	public String findRdpByForemanDispatcher(String searchJson, int start, int limit){
    	if(start>0) start--;//传过来的第一页为1，而查询需要从0开始
    	Map map = null;
    	try {
    		if(StringUtil.isNullOrBlank(searchJson)) return null;
    		map = getTrainWorkPlanQueryManager().getWorkPlanByForeman(searchJson, start, limit);
    		/**返回结果包装*/
            List<FindRdpByForemanDispatcherBean> list = new ArrayList<FindRdpByForemanDispatcherBean>();
            list = BeanUtils.copyListToList(FindRdpByForemanDispatcherBean.class, (List)map.get(ROOT));
            Page pageList = new Page((Integer)(map.get(TOTALPROPERTY)),list);
    		return JSONUtil.write(pageList.extjsStore());
    	} catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取工艺节点数据（工长派工查询）
     * <li>创建人：谭诚
     * <li>创建日期：2013-11-15
     * <li>修改人： 王治龙
     * <li>修改日期：2013-12-25
     * <li>修改内容：封装返回值，业务数据返回结果暂不需优化，因优化意义不大。
     * @param parentIdx 上级节点IDX
     * @param rdpIdx 兑现但idx
     * @param operatorid 操作员id
     * @param isDispatcher 已派工/未派工 'y'/'n'
     * @return String
     */
    @SuppressWarnings("unchecked")
    public String findTecNodeByFormanDispatcher(String parentIdx, String rdpIdx, Long operatorid, String isDispatcher){
    	try {
    		Long teamId = getOmEmployeeManager().findByOperator(operatorid).getOrgid(); //根据当前操作员ID，调用查询接口，获取其所属班组
    		String pid = (StringUtil.isNullOrBlank(parentIdx)||parentIdx.startsWith("xnode"))?null:parentIdx; 
    		List<HashMap> children = getJobProcessNodeQueryManager().getNodeTree(rdpIdx,String.valueOf(teamId),pid,isDispatcher);
            return JSONUtil.write(children);
        } catch (IOException e) {
            ExceptionUtil.process(e,logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>方法名称：findTeamEmp
     * <li>方法说明：工长派工时查询班组人员信息
     * <li>@param operatorid
     * <li>@param workCardIdx
     * <li>@param mode
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-1 下午02:35:49
     * <li>修改人：王治龙
     * <li>修改内容：封装返回值
     */
    @SuppressWarnings("unchecked")
    public String findTeamEmp(Long operatorid, String workCardIdx, String mode){
        try {
            OmOrganization org = getOmOrganizationSelectManager().getOrgByAcOperator(operatorid);
            boolean reDispatcher = ONE.equals(mode);
            /**封装返回值*/
            Page<OmEmployee> page = getOmEmployeeSelectManager().findPageDataList(workCardIdx, org.getOrgseq(), reDispatcher, 0, 500, null) ;
            List<FindTeamEmpBean> list = new ArrayList<FindTeamEmpBean>();
            list = BeanUtils.copyListToList(FindTeamEmpBean.class, page.getList());
            Page<FindTeamEmpBean> pageList = new Page<FindTeamEmpBean>(page.getTotal(), list); //构造page对象并返回分页前的总数量
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e,logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
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
     * <li>修改人：王治龙
     * <li>修改内容：封装返回值 2013-12-25
     */
    public String saveDispatching(Long operatorid, String workCardIdx, String empids){
        try {
            AcOperator ac = getAcOperatorManager().getModelById(operatorid);            
            SystemContext.setAcOperator(ac);
            getWorkCardManager().getDispatcher().updateForemanDispater(workCardIdx, empids, operatorid);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
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
	 *'allDefaultBatch'：根据查询条件对默认上次作业人员全部批量派工；
     * @return String
     */
    public String saveBatchDispatch(Long operatorid, String searchJson, String workCardIds, String empids, String dispatchType) {
        try {
            AcOperator ac = getAcOperatorManager().getModelById(operatorid);
            SystemContext.setAcOperator(ac);            
            OmEmployee emp = getOmEmployeeManager().findByOperator(operatorid);
            SystemContext.setOmEmployee(emp);
            if ("defaultBatch".equals(dispatchType)) { // 默认上次派工
                String errMsg = getWorkCardManager().getDispatcher().updateDefaultLastTimeWorker(workCardIds);
                if (errMsg != null) 
                    return "{'flag':'false','message':'" + errMsg + "！'}";
            } else if ("batch".equals(dispatchType)) { // 批量派工
                getWorkCardManager().getDispatcher().updateForemanDispater(workCardIds, empids, operatorid);
            } else if ("allBatch".equals(dispatchType)) { // 全部派工
                searchJson = searchJson.replace("#2#", String.valueOf(emp.getOrgid())).replace("#1#", "0");
                getWorkCardManager().getDispatcher().foremanAllDispatcher(searchJson, empids, operatorid);
            } else if ("allDefaultBatch".equals(dispatchType)) {
                searchJson = searchJson.replace("#2#", String.valueOf(emp.getOrgid())).replace("#1#", "0");
                String errMsg = getWorkCardManager().getDispatcher().updateAllDefaultLastTimeWorker(searchJson);
                if (errMsg != null) 
                    return "{'flag':'false','message':'" + errMsg + "！'}";
            }
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
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
     * <li>修改人：王治龙
     * <li>修改内容：封装返回值 2013-12-26
     */
    @SuppressWarnings("unchecked")
    public String getWorkSeqDelay(Long operatorid, int start,int limit) {
        try {
            start--;
            OmOrganization org = getOmOrganizationSelectManager().getOrgByAcOperator(operatorid);
            Page page = getNodeCaseDelayManager().findWorkSeqPutOff(org.getOrgid().toString(), start * limit, limit, null, null, null, null);
            /**封装返回值*/
            List<WorkSeqDelayBean> beanList = new ArrayList<WorkSeqDelayBean>();
            beanList = BeanUtils.copyListToList(WorkSeqDelayBean.class, page.getList());
            Page pageList = new Page(page.getTotal(),beanList);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>方法名称：getDelayInfo
     * <li>方法说明：获取延迟信息 
     * <li>@param nodeCaseIdx
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-8 下午04:09:06
     * <li>修改人：王治龙
     * <li>修改内容： 封装返回值 2013-12-26 
     */
    public String getDelayInfo(String nodeCaseIdx){
        try {
            NodeCaseDelay delay = getNodeCaseDelayManager().getEntityByNodeCaseIdx(nodeCaseIdx);
            NodeCaseDelayBean nodeDelayBean = new NodeCaseDelayBean();
            BeanUtils.copyProperties(nodeDelayBean, delay);
            return JSONUtil.write(nodeDelayBean);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
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
     * <li>修改人：王治龙
     * <li>修改内容：封装返回值 2013-12-26
     */
    public String saveWorkSeqDelay(Long operatorid, String nodeCaseIdx, String idx, String delayReason, String delayType, int delayTime){
    	//通过操作员ID查询操作员所在的机构ID
    	OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
    	SystemContext.setOmEmployee(emp);
    	try {
            JobProcessNodeQueryManager m = getJobProcessNodeQueryManager();
            JobProcessNode nc = m.getModelById(nodeCaseIdx);
            NodeCaseDelay entity = new NodeCaseDelay();
            if(idx == null || "".equals(idx)){
                entity.setCreateTime(new Date());
                entity.setCreator(operatorid);
            }else{
                entity.setIdx(idx);
            }
            entity.setDelayReason(delayReason);
            entity.setDelayTime(delayTime);
            entity.setDelayType(delayType);
            entity.setNodeCaseIdx(nodeCaseIdx);
            entity.setNodeIDX(nc.getNodeIDX());
            entity.setPlanBeginTime(nc.getPlanBeginTime());
            entity.setPlanEndTime(nc.getPlanEndTime());
            entity.setRdpIDX(nc.getWorkPlanIDX());
            entity.setRealBeginTime(nc.getRealBeginTime());
            entity.setRecordStatus(0);
            entity.setSiteID(JXSystemProperties.SYN_SITEID);
            entity.setUpdateTime(new Date());
            entity.setUpdator(operatorid);
            getNodeCaseDelayManager().save(entity);
            getNodeCaseDelayManager().sendDelayNotify(nodeCaseIdx); //发送消息延误
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>方法名称：getDict
     * <li>方法说明：获取字典数据 
     * <li>@param dicttypeid
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-8 下午02:59:22
     * <li>修改人：王治龙
     * <li>修改内容：封装返回值 2013-12-26
     */
	public String getDict(String dicttypeid){
        try{
        	List<EosDictEntry> list = getEosDictEntrySelectManager().findByDicTypeID(dicttypeid);
        	List<EosDictEntryBean> beanList = new ArrayList<EosDictEntryBean>();
        	for (EosDictEntry eos : list) {
        		EosDictEntryBean bean=new EosDictEntryBean();
        		bean.setDictid(eos.getId().getDictid());
        		bean.setDictname(eos.getDictname());
        		beanList.add(bean);
			}
            return JSONUtil.write(beanList);
        }catch(Exception e){
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
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
    public String findEmpList(Long operatorid){
        try {
            OmOrganization org = getOmOrganizationSelectManager().getOrgByAcOperator(operatorid);
            return findTeamEmps(org.getOrgid());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
        
    /**
     * <li>方法说明：根据班组ID查询班组人员 
     * <li>方法名称：findTeamEmps
     * <li>@param orgid
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-9-13 上午10:06:29
     * <li>修改人：王斌
     * <li>修改内容：封装返回值
     */
    public String findTeamEmps(Long orgid){
        try {           
            List list = getOmEmployeeSelectManager().findTeamEmpList(orgid);
            List<OmEmployee> empList = new ArrayList<OmEmployee>();
            for (int i = 0; i < list.size(); i++) {
                OmEmployee emp = new OmEmployee();
                Object[] obj = (Object[]) list.get(i);
                emp.setEmpid(obj[0] != null ? Long.valueOf(obj[0].toString()) : null);
                emp.setEmpcode(obj[1] != null ? obj[1].toString() : "");
                emp.setEmpname(obj[2] != null ? obj[2].toString() : "");
                emp.setGender(obj[3] != null ? obj[3].toString() : "");               
                empList.add(emp);
            }
            return JSONUtil.write(empList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
        
    /**
     * <li>方法名称：saveCardNo
     * <li>方法说明：保存CardNo 
     * <li>@param userId
     * <li>@param cardNo
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-20 下午08:21:42
     * <li>修改人：王斌
     * <li>修改时间：2013-12-26 10:08
     * <li>修改内容：对返回格式进行封装成json格式
     */
    public String saveCardNo(String userId, String cardNo){
        try {
            int count = getOmEmployeeSelectManager().saveCardNo(userId, cardNo);
            if(count == 0){
                return "";
            }
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        } 
    }
    
    /**
     * <li>方法名称：loginByCardNo
     * <li>方法说明：根据卡号登陆 
     * <li>@param cardNo
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-20 下午08:28:16
     * <li>修改人：王斌
     * <li>修改时间：2013-12-26 10:02
     * <li>修改内容：对返回值进行封装
     */
    public String loginByCardNo(String cardNo, String loginLocation){
        try {
            AcOperator ac = getOmEmployeeSelectManager().loginByCardNo(cardNo);
            if(ac == null){
                return "{'flag':'false','message':'操作员无效！'}";
            }
           /**返回值封装*/
            AcOperatorBean acOperatorBean=new AcOperatorBean();
            BeanUtils.copyProperties(acOperatorBean, ac);
            //新增登录日志记录
            LoginLog log = new LoginLog();
            log.setOperatoridx(acOperatorBean.getOperatorid().toString());
            log.setUserid(acOperatorBean.getUserid());
            log.setOperatorname(acOperatorBean.getOperatorname());
            log.setLoginInTime(new Date());
            log.setLoginType(LoginLog.TYPE_SKSB);//刷卡识别
            log.setLoginClient(LoginLog.CLIENT_GWZD);//工位终端登录
            HttpServletRequest request = XFireServletController.getRequest();
            String remoteAddr = request.getRemoteAddr();
            log.setIp(remoteAddr);
            log.setLoginLocation(loginLocation);
//            log.setIp(InetAddress.getLocalHost().getHostAddress()) ;//获取ip地址
            this.getLoginLogManager().saveOrUpdate(log);//新增登录日志
            return JSONUtil.write(acOperatorBean);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>方法说明：任务数量统计 
     * <li>方法名称：findTaskCount
     * <li>@param operatorid
     * <li>@param model
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-31 下午04:57:47
     * <li>修改人：王治龙
     * <li>修改内容：封装返回值 2013-12-29
     */
    @SuppressWarnings("unchecked")
    public String findTaskCount(Long operatorid, String model) {
        try {
            OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
            Map<String, Integer> map = new HashMap<String, Integer>();
            if (model.equals("作业工单")) {
                int workCard = getWorkCardManager().findTaskCount(emp.getEmpid(), emp.getOrgid());
                map.put("作业工单待处理", workCard);
            } else if (model.equals("工序延误")) {
                OmOrganization org = getOmOrganizationSelectManager().getOrgByAcOperator(operatorid);
                Page page1 = getNodeCaseDelayManager().findWorkSeqPutOff(org.getOrgid().toString(), 0, 500, null, null, null, null);
                map.put("工序延误", page1.getTotal());
            } else if (model.equals("工长派工")) {
                Integer[] foreman = getWorkCardManager().getDispatcher().findForemanCount(emp.getOrgid());
                map.put("工长已派工", 0);
                map.put("工长未派工", foreman[0]);
            } else if (model.equals("提票工长派工")) {
                Integer isDispatcher = getFaultTicketManager().queryGzpgCount(true, operatorid);
                Integer noDispatcher = getFaultTicketManager().queryGzpgCount(false, operatorid);
                map.put("提票工长已派工", isDispatcher);
                map.put("提票工长未派工", noDispatcher);                
            } else if (model.equals("提票调度派工")) {
                Integer isDispatcher = getFaultTicketManager().queryDdpgCount("true");
                Integer noDispatcher = getFaultTicketManager().queryDdpgCount("false");
                map.put("提票调度已派工", isDispatcher);
                map.put("提票调度未派工", noDispatcher);
            } else if (model.equals("提票处理")) {
                map.put("提票处理", getFaultTicketManager().queryHandleCount(operatorid));
            } else if (model.equals("客车检修质量检验")) {
                Map<String, String> querymap = new HashMap<String, String>();
                querymap.put("vehicleType", "20");
                String query = JSONUtil.write(querymap);                
                map.put("客车检修质量检验", getQCResultQueryManager().getQCCount(getEmployee(operatorid),"",query));
            } else if (model.equals("货车检修质量检验")) {
                Map<String, String> querymap = new HashMap<String, String>();
                querymap.put("vehicleType", "10");
                String query = JSONUtil.write(querymap);                
                map.put("货车检修质量检验", getQCResultQueryManager().getQCCount(getEmployee(operatorid),"",query));
            } else if (model.equals("提票质量检查")) {
                map.put("提票质量检查", getFaultQCResultQueryManager().queryQCCount(operatorid));
            } else if (model.equals("配件检修")) {
            	SystemContextUtil.setSystemInfoByOperatorId(operatorid);
                PartsRdpNodeBean bean = new PartsRdpNodeBean();
                bean.setStatus("1");
            	int count = getPartsRdpNodeQueryManager().queryNodeCountByUser(new SearchEntity<PartsRdpNodeBean>(bean, 1, 1, null));
                map.put("配件检修", count);
            } else if (model.equals("配件质量检查")) {
            	int count = getPartsRdpQRQueryManager().queryDCLQCCount(operatorid);
                map.put("配件质量检查", count);
            } else if (model.equals("配件合格验收")) {
            	PartsRdp rdp = new PartsRdp();            
                rdp.setStatus(PartsRdp.STATUS_DYS);
            	int count = getPartsRdpQueryManager().queryRdpCount(new SearchEntity<PartsRdp>(rdp, null, null, null));
                map.put("配件合格验收", count);
            } else if (model.equals("货车检修任务处理")) {
                SystemContextUtil.setSystemInfoByOperatorId(operatorid);
                JobProcessNode bean = new JobProcessNode();
                bean.setStatus("1");
                int fwhcount = getJobProcessNodeQueryManager().queryNodeCountByWorkStation(new SearchEntity<JobProcessNode>(bean, 1, 1, null),"10");
                map.put("货车检修任务处理范围活", fwhcount);
                
                FaultTicket bean1 = new FaultTicket();
                bean1.setStatus(1);
                List<FaultTicket> list = getFaultTicketManager().queryFaultTicketList(bean1);
                map.put("机车检修任务处理提票活", list == null ? 0 : list.size());
            } else if (model.equals("机车提票活处理")) {
                FaultTicket bean1 = new FaultTicket();
                bean1.setStatus(1);
                List<FaultTicket> list = getFaultTicketManager().queryFaultTicketList(bean1);
                map.put("机车提票活处理", list == null ? 0 : list.size());
            } else if (model.equals("机车提票确认")) {
                List<FaultTicketStatisticsTotalVo> list = getFaultTicketCheckAffirmManager().queryCheckStatisticsListTotal(operatorid);
                for (FaultTicketStatisticsTotalVo vo : list) {
                    map.put("【"+vo.getRoleName()+"】", vo.getUndoCount()+vo.getDoneCount());
                }
            } else if (model.equals("机车提票验收")) {
                map.put("机车提票待验收", getFaultTicketCheckRecordManager().getCanCheckToatl());
            }
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>方法说明：根据操作员ID查询人员 
     * <li>方法名称：getEmployee
     * <li>@param operatorid
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-8-28 上午11:28:20
     * <li>修改人：
     * <li>修改内容：
     */
    public Long getEmployee(Long operatorid){       
        try {
            return getOmEmployeeSelectManager().findEmpByOperator(operatorid).getEmpid();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return 0L;
        }
    }
    
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
    public String workCard(String idx) {
        try {
            WorkCardManager m = getWorkCardManager();
            WorkCard workCard = m.getModelById(idx);
            String[] val = m.getInfo(idx);
            workCard.setTransinTimeStr(val[0]);
            workCard.setPlanTrainTimeStr(val[1]);
            workCard.setRepairActivityTypeName("检修活动");
            workCard.setRepairActivityName(val[3]);
            workCard.setNodeCaseName(val[3]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            if (workCard.getRealBeginTime() != null) {
                workCard.setRealBeginTimeStr(sdf.format(workCard.getRealBeginTime()));
            }
            if (workCard.getRealEndTime() != null) {
                workCard.setRealEndTimeStr(sdf.format(workCard.getRealEndTime()));
            }
            /** 封装方法： */
            WorkCardBean workCardBean = new WorkCardBean();
            BeanUtils.copyProperties(workCardBean, workCard);
            workCardBean.setNodeCaseName(val[3]);
            return JSONUtil.write(workCardBean);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
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
    public String workTask(Long operatorid, String workCardIdx, int start, int limit, String mode) {
        start--;    // 传过来的第一页为1，而查询需要从0开始
        try {
            String[] statusArray = new String[] { WorkTask.STATUS_WAITINGFORGET, WorkTask.STATUS_INIT };
            if ("1".equals(mode)) {
                // 查询已处理
                statusArray = new String[] { WorkTask.STATUS_HANDLED };
            }
            /** 返回结果包装：将查询对象page中的集合数据用指定的WorkTaskBean对象进行封装 */
            Page<WorkTaskBean> page = getWorkTaskManager().pageQuery2(workCardIdx, statusArray, start * limit, limit, null);
            return JSONUtil.write(page.extjsStore()); // 返回构造结果
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
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
    public String updateOperatorPWD(long operatorid, String newPWD) {
        try {
            AcOperator operator = getAcOperatorManager().getModelById(operatorid);
            getOperatorManager().updateOperatorPWD(operator, newPWD);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    protected AcOperatorManager getAcOperatorManager(){
        return (AcOperatorManager)Application.getSpringApplicationContext().getBean("acOperatorManager");
    }
    
    protected OmEmployeeManager getOmEmployeeManager(){
        return (OmEmployeeManager)Application.getSpringApplicationContext().getBean("omEmployeeManager");
    }
    
    protected OmOrganizationSelectManager getOmOrganizationSelectManager(){
        return (OmOrganizationSelectManager)Application.getSpringApplicationContext().getBean("omOrganizationSelectManager");
    }
    
    protected WorkCardManager getWorkCardManager(){
        return (WorkCardManager)Application.getSpringApplicationContext().getBean("workCardManager");
    } 

    protected EosDictEntrySelectManager getEosDictEntrySelectManager(){
        return (EosDictEntrySelectManager)Application.getSpringApplicationContext().getBean("eosDictEntrySelectManager");
    }
    
    protected ProcessTaskManager getProcessTaskManager(){
        return (ProcessTaskManager)Application.getSpringApplicationContext().getBean("processTaskManager");
    }    
    
    protected NodeCaseDelayManager getNodeCaseDelayManager(){
        return (NodeCaseDelayManager)Application.getSpringApplicationContext().getBean("nodeCaseDelayManager");
    }
    
    protected OmEmployeeSelectManager getOmEmployeeSelectManager(){
        return (OmEmployeeSelectManager)Application.getSpringApplicationContext().getBean("omEmployeeSelectManager");
    }
    
    protected QCResultManager getQCResultManager(){
        return (QCResultManager)Application.getSpringApplicationContext().getBean("qCResultManager");
    }
    
    protected QCResultQueryManager getQCResultQueryManager(){
        return (QCResultQueryManager)Application.getSpringApplicationContext().getBean("qCResultQueryManager");
    }
    
    protected TrainWorkPlanQueryManager getTrainWorkPlanQueryManager(){
        return (TrainWorkPlanQueryManager)Application.getSpringApplicationContext().getBean("trainWorkPlanQueryManager");
    }
    
    protected JobProcessNodeQueryManager getJobProcessNodeQueryManager(){
        return (JobProcessNodeQueryManager)Application.getSpringApplicationContext().getBean("jobProcessNodeQueryManager");
    }
    
    protected WorkTaskManager getWorkTaskManager() {
        return (WorkTaskManager) Application.getSpringApplicationContext().getBean("workTaskManager");
    }
    
    protected FaultTicketManager getFaultTicketManager() {
        return (FaultTicketManager) Application.getSpringApplicationContext().getBean("faultTicketManager");
    }
    
    protected FaultQCResultQueryManager getFaultQCResultQueryManager() {
        return (FaultQCResultQueryManager) Application.getSpringApplicationContext().getBean("faultQCResultQueryManager");
    }
    
    protected OperatorManager getOperatorManager() {
        return (OperatorManager) Application.getSpringApplicationContext().getBean("operatorManager");
    }
    
    protected PartsRdpNodeQueryManager getPartsRdpNodeQueryManager() {
        return (PartsRdpNodeQueryManager) Application.getSpringApplicationContext().getBean("partsRdpNodeQueryManager");
    }
    
    protected PartsRdpQRQueryManager getPartsRdpQRQueryManager() {
        return (PartsRdpQRQueryManager) Application.getSpringApplicationContext().getBean("partsRdpQRQueryManager");
    }
    
    protected PartsRdpQueryManager getPartsRdpQueryManager() {
        return (PartsRdpQueryManager) Application.getSpringApplicationContext().getBean("partsRdpQueryManager");
    }
    protected LoginLogManager getLoginLogManager() {
        return (LoginLogManager) Application.getSpringApplicationContext().getBean("loginLogManager");
    }
    protected FaultTicketCheckAffirmManager getFaultTicketCheckAffirmManager() {
        return (FaultTicketCheckAffirmManager) Application.getSpringApplicationContext().getBean("faultTicketCheckAffirmManager");
    }
    protected FaultTicketCheckRecordManager getFaultTicketCheckRecordManager() {
        return (FaultTicketCheckRecordManager) Application.getSpringApplicationContext().getBean("faultTicketCheckRecordManager");
    }

    /**
     * <li>说明：工位终端未登陆时获取各模块【未处理】数据数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    public String getMessageForUndoList() {
        try{
            List<EosDictEntry> list = getEosDictEntrySelectManager().findByDicTypeID("12");
            List<EosDictEntryBean> beanList = new ArrayList<EosDictEntryBean>();
            for (EosDictEntry eos : list) {
                EosDictEntryBean bean=new EosDictEntryBean();
                bean.setDictid(eos.getId().getDictid());
                bean.setDictname(eos.getDictname());
                beanList.add(bean);
            }
            return JSONUtil.write(beanList);
        }catch(Exception e){
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

//    /**
//     * <li>说明：根据operatorid获取影响生产因素处理任务记录
//     * <li>创建人：刘晓斌
//     * <li>创建日期：2013-5-25
//     */
//    public String getFactorSolution(String json){
//    	return influFactorSolutionManager.getFactorSolution(json);
//    }
//    /**
//     * <li>说明：工位终端接口，影响生产因素处理 接受任务
//     * <li>创建人：刘晓斌
//     * <li>创建日期：2013-5-25
//     */	
//	public String acceptFactorSolution(Long operatorid, String json){
//		return influFactorSolutionManager.acceptFactorSolution(operatorid, json);
//	}
//    /**
//     * <li>说明：工位终端接口，影响生产因素处理 完成确认
//     * <li>创建人：刘晓斌
//     * <li>创建日期：2013-5-25
//     */	
//	public String confirmFactorSolution(Long operatorid, String json){
//		return influFactorSolutionManager.confirmFactorSolution(operatorid, json);
//	}

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
     * @return 分页列表字符串 如无数据则返回{}
     */
//    @SuppressWarnings("all")
//    public String pageTrainInspectResult(Long operatorid, int start, int limit, String depID){
//        start--;//传过来的第一页为1，而查询需要从0开始
//        Map map = new HashMap();
//        try {
//            OmEmployee emp = omEmployeeManager.findOmEmployee(operatorid);
////            AcOperator acOperator = acOperatorManager.getModelById(operatorid);
//            AcOperator ac = acOperatorManager.findLoginAcOprator(operatorid);
//            SystemContext.setAcOperator(ac);
//            String siteID = JczbWorkPlaceOrganizationManager.getSiteIDByCurrEmpByUID();
//            //系统获取权限 
////           List<AcRole> acRoles = this.findAcOperatorRoles(acOperator, emp);
////            menuList = this.acMenuManager.findAcMenuByRoles(acOperator, acRoles,false);
////          工位终端调度角色
//            if(roleSetManager.permission(operatorid, "PCZZDD")){
//                TrainInspectResult result = new TrainInspectResult();
//                result.setDepID(depID);//depID is null 未派工 is not null 已派工
//                result.setSiteID(siteID);
//                SearchEntity<TrainInspectResult> searchEntity = new SearchEntity<TrainInspectResult>();
//                searchEntity.setEntity(result);
//                searchEntity.setStart(start * limit);
//                searchEntity.setLimit(limit);
//                Page page = trainInspectResultManager.pageTrainInspectResult(searchEntity);
//                if(page.getTotal() > 0 && page.getList().size() > 0)  map = page.extjsStore();
//               
//            }
//            return JSONUtil.write(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.pageTrainInspectResult：异常:"+ e + "\n原因" + e.getMessage());
//            return "{'flag':'false','message':'操作失败！'}";
//        }
//    }
//    /**
//     * 
//     * <li>说明：普查整治-工长派工页面
//     * <li>创建人：程锐
//     * <li>创建日期：2013-6-30
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     
//     * @param operatorid 登录人ID
//     * @param start 开始行数
//     * @param limit 分页行数
//     * @param hasInspectPerson false 未派工 true 已派工
//     * @return 分页列表字符串 如无数据则返回{}
//     */
//    @SuppressWarnings("unchecked")
//    public String pageTrainInspectResultForeman(Long operatorid, int start, int limit, String hasInspectPerson){
//        start--;//传过来的第一页为1，而查询需要从0开始
//        Map map = new HashMap();
//        try {
//            OmEmployee emp = omEmployeeManager.findOmEmployee(operatorid);
//            OmOrganization org = omOrganizationSelectManager.getModelById(emp.getOrgid());
//            AcOperator ac = acOperatorManager.findLoginAcOprator(operatorid);
//            SystemContext.setAcOperator(ac);
//            String siteID = JczbWorkPlaceOrganizationManager.getSiteIDByCurrEmpByUID();
//            //工位终端工长角色
//            if(roleSetManager.permission(operatorid, "PCZZGZ")){
//                TrainInspectResult result = new TrainInspectResult();
//                result.setDepSeq(org.getOrgseq());//当前登录人所属部门seq
//                result.setSiteID(siteID);
//                SearchEntity<TrainInspectResult> searchEntity = new SearchEntity<TrainInspectResult>();
//                searchEntity.setEntity(result);
//                searchEntity.setStart(start * limit);
//                searchEntity.setLimit(limit);
//                Page page = trainInspectResultManager.pageTrainInspectResultForeman(searchEntity, hasInspectPerson);
//                if(page.getTotal() > 0 && page.getList().size() > 0)  map = page.extjsStore();
//            }
//            return JSONUtil.write(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.pageTrainInspectResultForeman：异常:"+ e + "\n原因" + e.getMessage());
//            return "{'flag':'false','message':'操作失败！'}";
//        }
//    }
//    /**
//     * 
//     * <li>说明：普查整治-处理页面列表
//     * <li>创建人：程锐
//     * <li>创建日期：2013-6-30
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param operatorid 登录人ID
//     * @param start 开始行数
//     * @param limit 分页行数
//     * @param inspectStatus 0 待处理  2 已处理
//     * @return 分页字符串 如无数据则返回{}
//     */
//    @SuppressWarnings("unchecked")
//    public String pageTrainInspectResultHandle(Long operatorid, int start, int limit, String inspectStatus){
//        start--;//传过来的第一页为1，而查询需要从0开始
//        Map map = new HashMap();
//        try {
//            OmEmployee emp = omEmployeeManager.findOmEmployee(operatorid);  
//            AcOperator ac = acOperatorManager.findLoginAcOprator(operatorid);
//            SystemContext.setAcOperator(ac);
//            String siteID = JczbWorkPlaceOrganizationManager.getSiteIDByCurrEmpByUID();
//            TrainInspectResult result = new TrainInspectResult();
//            result.setSiteID(siteID);
//            if(!StringUtil.isNullOrBlank(inspectStatus))  result.setInspectStatus(Integer.parseInt(inspectStatus));
//            SearchEntity<TrainInspectResult> searchEntity = new SearchEntity<TrainInspectResult>();
//            searchEntity.setEntity(result);
//            searchEntity.setStart(start * limit);
//            searchEntity.setLimit(limit);
//            Page page = trainInspectResultManager.pageTrainInspectResultHandle(searchEntity, String.valueOf(emp.getEmpid()));
//            if(page.getTotal() > 0 && page.getList().size() > 0)  map = page.extjsStore();
//            return JSONUtil.write(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.pageTrainInspectResultHandle：异常:"+ e + "\n原因" + e.getMessage());
//            return "{'flag':'false','message':'操作失败！'}";
//        }
//    }
//    /**
//     * 
//     * <li>说明：普查整治-处理人员列表页面
//     * <li>创建人：程锐
//     * <li>创建日期：2013-6-30
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     
//     * @param operatorid 登录人ID
//     * @param start 开始行数
//     * @param limit 分页行数
//     * @param inspectResultId 普查整治项目ID
//     * @return 分页字符串 如无数据则返回{}
//     */
//    @SuppressWarnings("unchecked")
//    public String pageTrainInspectPersonHandle(Long operatorid, int start, int limit, String inspectResultId){
//        start--;//传过来的第一页为1，而查询需要从0开始
//        Map map = new HashMap();
//        try {
//            QueryCriteria<TrainInspectPerson> query = new QueryCriteria<TrainInspectPerson>();
//            query.setEntityClass(TrainInspectPerson.class);
//            List<Condition> whereList = new ArrayList<Condition>();
//            whereList.add(new Condition("inspectResultId", Condition.EQ, inspectResultId));
//            query.setWhereList(whereList);
//            query.setStart(start * limit);
//            query.setLimit(limit);
//            Page page = trainInspectPersonManager.findPageList(query);
//            if(page.getTotal() > 0 && page.getList().size() > 0)  map = page.extjsStore();
//            return JSONUtil.write(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.pageTrainInspectPersonHandle：异常:"+ e + "\n原因" + e.getMessage());
//            return "{'flag':'false','message':'操作失败！'}";
//        }
//    }
//    
//    /**
//     * 
//     * <li>说明：普查整治(派工处理)
//     * <li>创建人：程锐
//     * <li>创建日期：2013-6-30
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param depID 派工班组id
//     * @param depSeq 派工班组序列
//     * @param depName 派工班组名称
//     * @param ids 普查整治任务id数组
//     * @return String "true"为成功或失败信息
//     */
//    @SuppressWarnings("all")
//    public String saveTrainInspectResultDDPG(String depID, String depSeq, String depName, String[] ids){
//        try {
////            String[] errMsg = trainInspectResultManager.saveTrainInspectResultDDPG(depID, depSeq, depName, ids);
////            return "true";
//            String[] errMsg = trainInspectResultManager.saveTrainInspectResultDDPG(depID, depSeq, depName, ids);
//            if (errMsg == null || errMsg.length < 1) {
//                return "true";
//            } else {
//                return errMsg[0];
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.saveTrainInspectResultDDPG：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    }
//    /**
//     * 
//     * <li>说明：工长派工选人
//     * <li>创建人：程锐
//     * <li>创建日期：2013-7-4
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param inspectResultIdx 普查整治任务idx
//     * @param operatorid 登录人id
//     * @param isDispatcher 是否已派工 true 是，false 否
//     * @param start 开始行数
//     * @param limit 分页行数
//     * @return 分页字符串 如无数据则返回{}
//     * @throws BusinessException
//     */
//    public String findPczzDataList(String inspectResultIdx,Long operatorid,boolean isDispatcher,int start,int limit) throws BusinessException{
//        start--;//传过来的第一页为1，而查询需要从0开始
//        Map map = new HashMap();
//        try {
//            OmEmployee emp = omEmployeeManager.findOmEmployee(operatorid); 
//            OmOrganization org = omOrganizationSelectManager.getModelById(emp.getOrgid());
//            Page page = employeeToWorktypeManager.findPczzDataList(inspectResultIdx,org.getOrgseq(),isDispatcher, 0, 500, null);//目前未分页
//            if(page.getTotal() > 0)  map = page.extjsStore();
//            return JSONUtil.write(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.findPczzDataList：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    }
//    /**
//     * 
//     * <li>说明：普查整治(工长派工处理)
//     * <li>创建人：程锐
//     * <li>创建日期：2013-6-30
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param inspectResultIdx 普查整治任务idx
//     * @param empids 需指派的人员主键（多id以“,”分隔）
//     * @param operatorid 登录人ID
//     * @return String "true"为成功或失败信息
//     */
//    @SuppressWarnings("all")
//    public String updateForemanDispater(String inspectResultIdx, String empids, Long operatorid){
//        Map<String, Object> map = new HashMap<String,Object>();
//        try {
//            String[] errMsg = trainInspectResultManager.updateForemanDispater(inspectResultIdx, empids, operatorid);
//            if (errMsg == null || errMsg.length < 1) {
//                return "true";
//            } else {
//                return errMsg[0];
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.updateForemanDispater：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    }
//    
//    
//    /**
//     * 
//     * <li>说明：普查整治(任务处理)
//     * <li>创建人：程锐
//     * <li>创建日期：2013-6-30
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param inspectResultIdx：普查整治任务主键
//     * @param inspectResult：普查整治处理情况
//     * @param inspectDate：普查整治处理日期
//     * @param operatorid：登录人ID
//     * @return 成功与否
//     */
//    @SuppressWarnings("all")
//    public String updateForhandle(String inspectResultIdx, String inspectResult, String inspectDate, Long operatorid){
//        try {
//            OmEmployee emp = omEmployeeManager.findOmEmployee(operatorid);
//            trainInspectResultManager.updateForhandle(inspectResultIdx, inspectResult, inspectDate, String.valueOf(emp.getEmpid()));
//            return "true";
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.updateForhandle：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    }
    /************************************************影响生产因素--填写原因***********************************************************************/
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
//    @SuppressWarnings("unchecked")
//    public String influFactorPageList( int start, int limit, String status){
//        start--;//传过来的第一页为1，而查询需要从0开始
//        Map map = new HashMap();
//        try {
//            
//            QueryCriteria<FactorList> query = new QueryCriteria<FactorList>();
//            query.setEntityClass(FactorList.class);
//            List<Condition> whereList = new ArrayList<Condition>();
//            whereList.add(new Condition("status", Condition.EQ, status));
//            query.setWhereList(whereList);
//            query.setStart(start * limit);
//            query.setLimit(limit);
//            Page page = influFactorManager.getDaoUtils().findPageByQC(query);
//            List<FactorList> influFactorList = page.getList();
//            List<FactorList> reList = new ArrayList<FactorList>();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            for(FactorList influFactor:influFactorList){
//                String raiseTimeStr = influFactor.getRaiseTime() == null ? "":sdf.format(influFactor.getRaiseTime());
//                String noticeTimeStr = influFactor.getNoticeTime() == null ? "":sdf.format(influFactor.getNoticeTime());
//                influFactor.setRaiseTimeStr(raiseTimeStr);
//                influFactor.setNoticeTimeStr(noticeTimeStr);
//                reList.add(influFactor);
//            }
//            page.setList(reList);
//            if(page.getTotal() > 0 && page.getList().size() > 0)  map = page.extjsStore();
//            return JSONUtil.write(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.influFactorPageList：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    }
//    /**
//     * 
//     * <li>说明：影响生产因素--填写原因--获取可选取的责任部门列表（天津电力机车有限公司下）
//     * <li>创建人：程锐
//     * <li>创建日期：2013-7-11
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param 
//     * @return 责任部门列表json字符串
//     */
//    public String orgList(){
//        Map map = new HashMap();
//        try{
//            List<OmOrganization> list = influFactorSolutionManager.orgList();
//            List<InfluFactorSolution> soluList = new ArrayList<InfluFactorSolution>();
//            for (OmOrganization org : list) {
//                InfluFactorSolution solu = new InfluFactorSolution();
//                solu.setOrgID(org.getOrgid());
//                solu.setOrgName(org.getOrgname());
//                soluList.add(solu);
//            }           
//            if(soluList != null && soluList.size() > 0)  map = Page.extjsStore(soluList);
//            return JSONUtil.write(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.orgList：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    }
//    /**
//     * 
//     * <li>说明：影响生产因素--填写原因--选择工艺节点--获取承修机车
//     * <li>创建人：程锐
//     * <li>创建日期：2013-7-11
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param start 开始行数
//     * @param limit 分页行数
//     * @return 分页字符串 如无数据则返回{}
//     */
//    @SuppressWarnings("unchecked")
//    public String rdpListForInflufactor( int start, int limit){
//        start--;//传过来的第一页为1，而查询需要从0开始
//        Map map = new HashMap();
//        try {
//            QueryCriteria<TrainEnforcePlanRdp> query = new QueryCriteria<TrainEnforcePlanRdp>();
//            query.setEntityClass(TrainEnforcePlanRdp.class);
//            List<Condition> whereList = new ArrayList<Condition>();
//            whereList.add(new Condition("billStatus", Condition.EQ, TrainEnforcePlanRdp.STATUS_HANDLING));
//            query.setWhereList(whereList);
//            query.setStart(start * limit);
//            query.setLimit(limit);
//            Page page = trainEnforcePlanRdpManager.findPageList(query);
//            if(page.getTotal() > 0 && page.getList().size() > 0)  map = page.extjsStore();
//            return JSONUtil.write(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.rdpListForInflufactor：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    } 
//    /**
//     * 
//     * <li>说明：影响生产因素--填写原因--选择工艺节点--获取工艺节点列表
//     * <li>创建人：程锐
//     * <li>创建日期：2013-7-12
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param start 开始行数
//     * @param limit 分页行数
//     * @param rdpIdx 机车兑现单主键
//     * @param status 工艺节点状态 NOTSTARTED未启动 RUNNING运行 COMPLETED完成 TERMINATED终止
//     * @return 分页字符串 如无数据则返回{}
//     */
//    @SuppressWarnings("unchecked")
//    public String nodeCaseListForInflufactor( int start, int limit, String rdpIdx, String status){
//        start--;//传过来的第一页为1，而查询需要从0开始
//        Map map = new HashMap();
//        try {
//            QueryCriteria<FactorTrainSelect> query = new QueryCriteria<FactorTrainSelect>();
//            query.setEntityClass(FactorTrainSelect.class);
//            List<Condition> whereList = new ArrayList<Condition>();
//            if(!StringUtil.isNullOrBlank(status))  whereList.add(new Condition("status", Condition.EQ, status));
//            whereList.add(new Condition("rdpIDX", Condition.EQ, rdpIdx));
//            query.setWhereList(whereList);
//            query.setStart(start * limit);
//            query.setLimit(limit);
//            Page page = factorTrainSelectManager.findPageList(query);
//            if(page.getTotal() > 0 && page.getList().size() > 0)  map = page.extjsStore();
//            return JSONUtil.write(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.nodeCaseListForInflufactor：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    } 
//    /**
//     * <li>说明：工位终端接口，新增或更新影响生产进度的因素记录，同时要保存从表“影响生产进度因素处理”的数据
//     * <li>创建人：程锐
//     * <li>创建日期：2013-7-12
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param Long operatorid //操作员id，长整数
//     * @param String factorjson字符串
//     * {
//        idx:"",                 //记录idx主键,值""表示新增，值不为空表示编辑
//        factorType:"",            //类型
//        factorReason:"",        //原因
//        nodeCaseIdx:"",        //工艺节点实例主键
//        nodeIDX:"",            //工艺节点主键
//        tecProcessCaseIDX:"",  //工艺实例主键
//        rdpIDX:""              //兑现单主键
//     * }
//     * @param String factorSolutionjson字符串
//     * [{
//     *     orgID:,//长整数 责任部门ID
//     *     orgName:""//责任部门名称 
//     * }]
//     * @return String json字符串
//     * {
//     *  success:true,           //处理结果，true成功，false失败
//     *  errMsg:"",              //处理失败success==false时，errMsg错误信息， 当success=true时errMsg空
//     * }
//     * @throws 抛出异常列表
//     */
//    public String saveFactorAndSolution(Long operatorid, String factorjson, String factorSolutionjson){
//        return influFactorManager.saveFactorAndSolution(operatorid, factorjson, factorSolutionjson);
//    }
//    /**
//     * 
//     * <li>说明：提交修改影响生产进度的因素记录状态，通知各责任部门下的所有人员接受任务(有问题，报错，可能是调用传值的问题)
//     * <li>创建人：程锐
//     * <li>创建日期：2013-7-12
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param ids 影响生产进度的因素实体对象主键数组
//     * @return String json字符串
//     * {
//     *  success:true,           //处理结果，true成功，false失败
//     *  errMsg:"",              //处理失败success==false时，errMsg错误信息， 当success=true时errMsg空
//     * }
//     */
//    @SuppressWarnings("all")
//    public String submitNotify(String[] ids){
//        String ret = "{}";
//        Map map = new HashMap();
//        try {
//            influFactorManager.submitNotify(ids);
//            map.put("success", true);
//            ret = JSONUtil.write(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.submitNotify：异常:"+ e + "\n原因" + e.getMessage());
//            ExceptionUtil.process(e, logger);
//            ret = "{\"success\":false,\"errMsg\":\"Excetpion " + e.getCause() + "\"}";
//        }
//        return ret;
//    }
//    /**
//     * 
//     * <li>说明：逻辑删除影响生产进度的因素记录及相关的影响生产进度因素处理的数据
//     * <li>创建人：程锐
//     * <li>创建日期：2013-7-12
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param ids 影响生产进度的因素实体对象主键数组
//     * @return String json字符串 
//     * {
//     *  success:true,           //处理结果，true成功，false失败
//     *  errMsg:"",              //处理失败success==false时，errMsg错误信息， 当success=true时errMsg空
//     * }
//     */
//    @SuppressWarnings("all")
//    public String deleteFactorAndSolu(String[] ids){
//        String ret = "{}";
//        Map map = new HashMap();
//        try {
//            influFactorManager.logicDelete(ids);
//            map.put("success", true);
//            ret = JSONUtil.write(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.deleteFactorAndSolu：异常:"+ e + "\n原因" + e.getMessage());
//            ExceptionUtil.process(e, logger);
//            ret = "{\"success\":false,\"errMsg\":\"Excetpion " + e.getCause() + "\"}";
//        }
//        return ret;
//    }
//    /**
//     * 
//     * <li>说明：获取影响生产因素类型
//     * <li>创建人：程锐
//     * <li>创建日期：2013-7-25
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param 
//     * @return void
//     * @throws Exception
//     */
//    public String getFactorTypes() {
//        try {
//            List<EosDictEntry> list = eosDictEntrySelectManager.findByDicTypeID("SCDD_AffectProduceFacotr_Type");
//            return JSONUtil.write(list);            
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("StationTerminalWS.getFactorTypes：异常:"+ e + "\n原因" + e.getMessage());
//            return "error";
//        }
//    }
        	
    
    /**
     * <li>方法说明：提票调度派工方式
     * <li>方法名称：getFaultDispatcherMode
     * <li>
     * @return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-8-28 下午02:08:19
     * <li>修改人：
     * <li>修改内容：
     */
//    public String getFaultDispatcherMode(){
//        String s = SystemConfigUtil.getValue("ck.jxgc.producttaskmanage.faultnotice.terminal.dispatcherquery");
//        if("biz".equalsIgnoreCase(s)){
//            return "biz";
//        }else{
//            return "process";
//        }
//    }
    
    /**
     * <li>说明：审核转临修
     * <li>创建人：王治龙
     * <li>创建日期：2013-11-18
     * <li>修改人： 王斌
     * <li>修改日期：2013-12-25		
     * <li>修改内容：修改返回值格式为json格式
     * @param operatorid：操作员ID
     * @param jsonData：审核信息JSON对象
     * @return String
     */
//    public String verifyToLX(Long operatorid,String jsonData){
//    	AcOperator ac = acOperatorManager.findLoginAcOprator(operatorid);   
//    	OmEmployee emp = omEmployeeSelectManager.findEmpByOperator(operatorid);
//		SystemContext.setAcOperator(ac);
//		DisposeOpinion sign;
//		try {
//			sign = (DisposeOpinion)JSONUtil.read(jsonData, DisposeOpinion.class);
//			sign.setDisposePerson(emp.getEmpname()); //审批人
//			sign.setDisposePersonID(emp.getEmpid().toString());
//			sign.setDisposeTime(new Date());//审批时间
//			String msg = trainApplyLXRDPManager.updateVerifyApplyForLX(sign);
//			if(!StringUtil.isNullOrBlank(msg)){
//				return msg;
//			}
//			return "{'flag':'true','message':'操作成功！'}";
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("StationTerminalWS.verifyToLX：异常:"+ e + "\n原因" + e.getMessage());
//			return "{'flag':'false','message':'操作失败！'}";
//		}
//    }
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
//    public String findTakeToLXFaults(String rdpIdx){
//    	try {
//			Page<FaultNotice> page = faultNoticeManager.findTakeToLXFaults(rdpIdx);
//			return JSONUtil.write(page.extjsStore());
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("StationTerminalWS.findTakeToLXFaults：异常:"+ e + "\n原因" + e.getMessage());
//			return "error";
//		}
//    }
    /**
	 * <li>说明：保存搭载工作票，并完成工作项-工位终端方法
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-11-26
	 * <li>修改人： 王斌
	 * <li>修改日期：2013-12-25
	 * <li>修改内容：修改返回值格式为json格式
	 * @param idsStr：搭载票IDX信息集合
	 * @param rdpIDX：兑现单主键
	 * @param disposeData：处理意见对象Json字符串
	 * @return String
	 */
//    public String saveTakeFaultNoticeAndFinishWorkItem(String idsStr,String rdpIDX,String disposeData){
//    	try {
//			faultNoticeLXRelationManager.saveTakeFaultNoticeAndFinishWorkItem(idsStr, rdpIDX, disposeData);
//			return "{'flag':'true','message':'操作成功！'}";
//		} catch (Exception e) {
//			e.printStackTrace() ;
//			logger.error("StationTerminalWS.saveTakeFaultNoticeAndFinishWorkItem：异常:"+ e + "\n原因" + e.getMessage());
//			return "{'flag':'false','message':'操作失败！'}";
//		}
//    }
    
    
    
    
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
     *      "rdpIDX":""
     *      "workItemName":"",
     *      "taskDepict":""
     *  }
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-5 下午06:32:11
     * <li>修改人：程锐
     * <li>修改内容：添加queryString查询字符串按工作项名称、车型、车号、任务描述等模糊查询
     * <li>修改人：王治龙
     * <li>修改内容：封装返回结果 2013年12月24日
     */
//    public String getProcessTaskList(String uid, String uname, int start, int limit, String mode, String queryString){
//      start--;
//      try {
//          String type = null;
//          String in = null;
//            /*if(ONE.equals(mode) || "5".equals(mode)){//1为必检 ，5为抽检
//                return JSONUtil.write(getQCResultQueryManager().getQCPageList(uname, start * limit, limit, mode, queryString).extjsStore());
//
//          } else if (mode.equals("2") || mode.equals("3")) { // mode = 2查派工 3查作业
//              type = ProcessTask.TYPE_TP;// 查询提票
//          } else if (mode.equals("4")) {// mode = 4 流水线切换
//              type = ProcessTask.TYPE_SYS;
//          } else if (mode.equals("6")) {// 转临修审核
//              type = ProcessTask.TYPE_APPLY_LX;
//          } else if(mode.equals("faultDispatcher")){   //查询提票调度派工
//              type = ProcessTask.TYPE_TP;
//              in = FlowMappingUtil.getMappingValue("tp.dispatcher");
//          }*/
//          
//          /*if (mode.equals("3")) {
//
//              in = FlowMappingUtil.getMappingValue("tp.other.ongoing") + "," // 专检工长验证退票,专修工长申请退票"
//                      + FlowMappingUtil.getMappingValue("tp.other.complete") + "," + FlowMappingUtil.getMappingValue("tp.sx.complete");
//          } else if (mode.equals("2")) {
//              in = FlowMappingUtil.getMappingValue("tp.foreman") + "," + FlowMappingUtil.getMappingValue("tp.dispatcher");
//          } //else if (mode.equals("4")) {
//              // in = "切换流水线,流水线切换";
//          //}*/            
//            ProcessTaskParam param = new ProcessTaskParam();
//            param.uid = uid;
//            param.uname = uname;
//            param.start = start * limit;
//            param.limit = limit;
//            param.taskType = type;
//            param.queryString = queryString;
//            param.in = in;
//            Page<ProcessTask> page = getProcessTaskManager().findProcessTask(param);
//            
//            Map<String, Object> data = new HashMap<String, Object>();
//            data.put(TOTALPROPERTY, page.getTotal());
//            data.put(ROOT, page.getList());
//            /**返回值封装*/
//            List<ProcessTaskListBean> listBean = new ArrayList<ProcessTaskListBean>();
//            
//            ProcessTaskListBean bean = null;
//            ProcessTask task = null;
//            for (int i = 0; i < page.getList().size(); i++) {
//              bean = new ProcessTaskListBean();
//              task = (ProcessTask)page.getList().get(i);
//              BeanUtils.copyProperties(bean, task);
//              
//              if(StringUtil.nvl(task.getSpecificationModel()).equals("") == false
//                  && StringUtil.nvl(task.getNameplateNo()).equals("") == false){
//                  bean.setTrainType(task.getSpecificationModel());
//                  bean.setTrainNo(task.getNameplateNo());
//                  bean.setParts(true);
//              }
//              
//              bean.setActionType(FlowMappingUtil.getMpaaingCode(task.getActionUrl())); //设置数据类型标识
//              listBean.add(bean);
//          }
//            Page<ProcessTaskListBean> pageList = new Page<ProcessTaskListBean>(page.getTotal(),listBean);
//            return JSONUtil.write(pageList.extjsStore());
//        } catch (Exception e) {
//            ExceptionUtil.process(e, logger);
//            return WsConstants.OPERATE_FALSE;
//        }
//  }   
}
