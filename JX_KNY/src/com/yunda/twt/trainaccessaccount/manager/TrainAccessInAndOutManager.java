package com.yunda.twt.trainaccessaccount.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;
import com.yunda.freight.base.vehicle.manager.TrainVehicleTypeManager;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrainView;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail;
import com.yunda.jx.scdd.enforceplan.manager.TrainEnforcePlanDetailManager;
import com.yunda.jx.scdd.repairplan.manager.RunningKMManager;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.webservice.common.util.DefaultUserUtilManager;
import com.yunda.zb.common.manager.TrainNoManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainAccessAccount业务类,机车出入段业务类
 * <li>设置为单例模式，保证多线程处理下只产生一个，以保证同步方法在多线程下有效。
 * <li>创建人：汪东良
 * <li>创建日期：2016-07-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 信息系统事业部开发部
 * @version 1.0
 */
@Scope(value = "singleton")
@Service(value = "trainAccessInAndOutManager")
public class TrainAccessInAndOutManager extends JXBaseManager<TrainAccessAccount, TrainAccessAccount> {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());

	/** 车号业务类 */
	@Autowired
	private TrainNoManager trainNoManager;

	/** 人员业务类 */
	@Resource
	private IOmEmployeeManager omEmployeeManager;

	/** 机车出入段台账业务类 */
	@Autowired
	private TrainAccessAccountManager trainAccessAccountManager;
    
    /** ZbglRdp业务类,机车整备单 */
    @Autowired
    private ZbglRdpManager zbglRdpManager;
    
    /** TrainType业务类,机车车型编码 */
    @Autowired
    private TrainTypeManager trainTypeManager;
    
    /** 机车检修作业计划查询业务类 */
    @Resource    
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager;
    
    /** 机车施修计划明细 */
    @Resource
    private TrainEnforcePlanDetailManager trainEnforcePlanDetailManager ;
    
    /**
     * 车辆车型业务类
     */
    @Resource
    private TrainVehicleTypeManager trainVehicleTypeManager;
    
    /**
     * 走形公里
     */
    @Resource
    private RunningKMManager runningKMManager ;

	/**
	 * 机车入段处理入段处理逻辑,方法保持同步以保证多线程，执行逻辑：
	 * 
	 * <pre>
	 * 1、判断机车别名和车型车号是否为空
	 * if(机车别为空&amp;&amp;（车型或车号为空）){
	 * 		return “机车别名和车型车号不能为空，入段操作失败！” 
	 * 	}
	 * 2、根据非空的（机车别名、车型车号或机车GUID）查询机车入段台账
	 * （机车入段的标准是：A、查询得到机车入段台账；B、在系统配置的时间段内能查询到机车出段台账）;
	 * if(查询的结果不为空){
	 * 	if(机车出入段台账为出段){
	 * 	 修改机车入段台账为入段状态；
	 * 	 return “机车入段成功！”;
	 * 		}
	 * 		返回机车已经入段的操作说明&gt;
	 * 	return;
	 * }else if(查询的结果为空){
	 * 	构造机车入段信息自行机车入段；
	 * }
	 * </pre>
	 * 
	 * @param trainAccessAccount
	 *            机车入段台账信息
	 * @return 机车入段实体，如果未入段成功则返回null;
	 * @throws Exception 
	 * @throws BusinessException
	 */
	public synchronized InAndOutMes<OperateReturnMessage,TrainAccessAccount> saveOrUpdateTrainAccessIn(TrainAccessAccount trainAccessAccount) throws 
			Exception {
		// 将全角空格进行转换 by wujl
		trimBlank(trainAccessAccount);
        
		if (StringUtil.isNullOrBlank(trainAccessAccount.getTrainAliasName())
				&& (StringUtil.isNullOrBlank(trainAccessAccount.getTrainTypeShortName()) || StringUtil.isNullOrBlank(trainAccessAccount.getTrainNo()))) {
			logger.error("入段车辆信息为空，不能执行入段操作！/n车辆入段信息：" + trainAccessAccount.toString());
			return new InAndOutMes<OperateReturnMessage,TrainAccessAccount>(OperateReturnMessage.newFailsInstance("入段车辆信息为空，不能执行入段操作！"),null) ;
		}
        
        TrainVehicleType vehicleType = trainVehicleTypeManager.getModelById(trainAccessAccount.getTrainTypeIDX());
        if(vehicleType == null){
            throw new BusinessException("该车型不存在！");
        }
        trainAccessAccount.setTrainTypeIDX(vehicleType.getIdx());// 设置车型ID
        trainAccessAccount.setTrainTypeShortName(vehicleType.getTypeCode());// 设置车型简称
        
		// 3、根据非空的（机车别名、车型车号或机车GUID）查询机车入段台账（机车入段的标准是：A、查询得到机车入段台账；B、在系统配置的最近一段时间段内的机车出段台账）；
		TrainAccessAccount inOrLatestOutAccount = getInOrLatestOutAccount(trainAccessAccount);
		InAndOutMes<OperateReturnMessage,TrainAccessAccount> inAndOutMes = null;
		if (inOrLatestOutAccount != null) {
			// 提示重复入段
			inAndOutMes = new InAndOutMes<OperateReturnMessage,TrainAccessAccount>(OperateReturnMessage.newFailsInstance("该车辆已经入段,不能重复入段！"),inOrLatestOutAccount);
			if (inOrLatestOutAccount.getOutTime() != null) {
				// 清除机车出段信息
				cleanOutInfo(inOrLatestOutAccount);
				// 对于已经出段的 在规定时间内再入段，默认为在段提示入段成功
				inAndOutMes = new InAndOutMes<OperateReturnMessage,TrainAccessAccount>(new OperateReturnMessage(),inOrLatestOutAccount);
			}
			// 更新机车位置及入段别名信息；
			updateInAccountinfo(inOrLatestOutAccount, trainAccessAccount);
            
            if (StringUtils.isNotBlank(trainAccessAccount.getToGo())) {
                //由于重复入段可能修改入段去向
                inOrLatestOutAccount.setToGo(trainAccessAccount.getToGo());
            }else {
//              判断系统是否配置了默认入段去向
                String isDefaultToGo = SystemConfigUtil.getValue("ck.jczb.isDefaultToGo");//配置项名称,获取到的值为基础管理-业务字典-台位图相关-字典项维护（例如0101，0301）
                if (StringUtils.isNotBlank(isDefaultToGo)) {
                    inOrLatestOutAccount.setToGo(isDefaultToGo);
                }
            }
            // 添加入段台账与检修作业的关联 by wujl 20161013
            saveTrainWorkPlanAccess(trainAccessAccount);
			// 更新机车入段台账信息
			this.update(inOrLatestOutAccount);
			return  inAndOutMes;
		}
		// 4、无法查找到机车入段信息，则执行机车入段操作；
		 return new InAndOutMes<OperateReturnMessage,TrainAccessAccount>(new OperateReturnMessage(),saveTrainAccessIn(trainAccessAccount)) ;

	}
	
	/**
	 * 去除车型简称、车号、机车简称前后空格，包括全角空格
	 * @param trainAccessAccount
	 */
	private void trimBlank(TrainAccessAccount trainAccessAccount) {
		trainAccessAccount.setTrainNo(StringUtil.convertQjBlank(trainAccessAccount.getTrainNo()).replaceAll("\\s*", ""));
		trainAccessAccount.setTrainTypeShortName(StringUtil.convertQjBlank(trainAccessAccount.getTrainTypeShortName()).replaceAll("\\s*", ""));
		trainAccessAccount.setTrainAliasName(StringUtil.convertQjBlank(trainAccessAccount.getTrainAliasName()).replaceAll("\\s*", ""));
	}

	/**
	 * <li>说明:执行机车入段，入段成功返回入段台账信息；
	 * <li>创建人：汪东良
	 * <li>创建日期：2016-07-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainAccessAccount
	 * @return 入段后的入段台账信息
	 * @throws NoSuchFieldException
	 * @throws BusinessException
	 */

	private TrainAccessAccount saveTrainAccessIn(TrainAccessAccount trainAccessAccount) throws BusinessException, NoSuchFieldException {
		// 构建机车入段信息
		buildTrainInEntity(trainAccessAccount);
		this.saveOrUpdate(trainAccessAccount);
		daoUtils.flush();
        
//		if (!StringUtil.isNullOrBlank(trainAccessAccount.getToGo())) {// 如果入段去向不为空则执行入段去向相关信息。
		try {
			trainAccessAccountManager.updateBizByToGO(trainAccessAccount);
            
            // 添加入段台账与检修作业的关联 by wujl 20161013
            saveTrainWorkPlanAccess(trainAccessAccount);
            // 入段的时候回写机车月计划实际入段日期
            saveTrainEnforcePlanDetail(trainAccessAccount,"in");
            // 对于客车，入段需要更新走行公里
            if(trainAccessAccount.getVehicleType().equals("20")){
                runningKMManager.updateRunningKM(trainAccessAccount.getTrainTypeIDX(), trainAccessAccount.getTrainTypeShortName(), trainAccessAccount.getTrainNo());
            }
		} catch (Exception e) {
			logger.error("执行机车入段去向时出现异常，无法根据机车入段去向生成后续业务！", e);
            throw new BusinessException(e.getMessage());
		}
//		}

		return trainAccessAccount;
	}
    
    /**
     * <li>说明:保存机车入段台账与机车检修作业计划的关联
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param trainAccessAccount 机车入段台账
     * @return 
     */
    public void saveTrainWorkPlanAccess(TrainAccessAccount trainAccessAccount){
        if (!StringUtil.isNullOrBlank(trainAccessAccount.getTrainTypeIDX())
            && !StringUtil.isNullOrBlank(trainAccessAccount.getTrainNo())
            && !StringUtil.isNullOrBlank(trainAccessAccount.getIdx())){
            List<TrainWorkPlan> list = trainWorkPlanQueryManager.getTrainWorkPlanListByTrain(trainAccessAccount.getTrainTypeIDX(), trainAccessAccount.getTrainNo());
            if(list != null && list.size() > 0){
                TrainWorkPlan plan = list.get(0);
                if(StringUtil.isNullOrBlank(plan.getTrainAccessAccountIDX())){
                    plan.setTrainAccessAccountIDX(trainAccessAccount.getIdx());
                    daoUtils.saveOrUpdate(plan);
                }
            }
        }
    }
    
    /**
     * <li>说明:入段的时候回写机车月计划实际入段日期（机车入段的时候查询月计划，更新最早的实际入段时间，出段时更新最早的实际离段日期）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccount 机车入段台账
     * @param type 回写类型 in 入段，out出段
     * @return 
     */
    public void saveTrainEnforcePlanDetail(TrainAccessAccount trainAccessAccount,String type){
        if (!StringUtil.isNullOrBlank(trainAccessAccount.getTrainTypeIDX())
            && !StringUtil.isNullOrBlank(trainAccessAccount.getTrainNo())){
            List<TrainEnforcePlanDetail> list = trainEnforcePlanDetailManager.getTrainEnforcePlanDetailListByTrain(trainAccessAccount.getTrainTypeIDX(), trainAccessAccount.getTrainNo());
            if(list != null && list.size() > 0){
                for (int i = 0; i < list.size(); i++) {
                    boolean flag = false ;
                    TrainEnforcePlanDetail detail = list.get(i);
                    if("in".equals(type) && detail.getRealStartDate() == null){
                        detail.setRealStartDate(new Date());
                        flag = true ;
                    }else if("out".equals(type) && detail.getRealEndDate() == null){
                        detail.setRealEndDate(new Date());
                        flag = true ;
                    }
                    if(flag){
                        daoUtils.saveOrUpdate(detail);
                        break ;
                    }
                }
            }
        }
    }

	/**
	 * <li>说明:根据机车别名解析机车机车信息
	 * <li>创建人：汪东良
	 * <li>创建日期：2016-07-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainInfo
	 *            大于6位字符 由车型简称和车号构成，如果股道自动化自动入段则是后四位为车号、前两位为车型，如果是车号识别入段则是由“车型+车号”构成
	 * @return 返回车型车号信息 第一个字符是车型简称、第二个车号；
	 */
	@SuppressWarnings("unchecked")
	private String[] getTrainTypeByTrainInfo(String trainInfo) {
		String typeName = "" ; // 车型
		String trainNo = "" ; // 车号
		// 机车信息不足6位则返回空
		if (trainInfo == null || trainInfo.length() < 4) {
			throw new BusinessException("不符合规则的机车信息，无法解析！/n机车信息：" + trainInfo);
		}else if( 4 <= trainInfo.length() &&  trainInfo.length() < 6){
			// 1、获取车号
			int lastIndex = trainInfo.length() - 1;
			typeName = trainInfo.substring(0, 2);
			trainNo = trainInfo.substring(2, trainInfo.length());
			// 获取最后一个字符
			String lastchar = trainInfo.substring(lastIndex);
			if ("A".equalsIgnoreCase(lastchar) || "B".equalsIgnoreCase(lastchar)) {// 如果机车最后一位是A或B则从倒数第二位获取车号
				trainNo = "000000".substring(0,5-trainNo.length()) + trainNo;
			}else{
				trainNo = "000000".substring(0,4-trainNo.length()) + trainNo ;
			}
		}else{
			// 1、获取车号
			int lastIndex = trainInfo.length() - 1;
			StringBuilder trainNoSb = new StringBuilder();
			// 获取最后一个字符
			String lastchar = trainInfo.substring(lastIndex);
			if ("A".equalsIgnoreCase(lastchar) || "B".equalsIgnoreCase(lastchar)) {// 如果机车最后一位是A或B则从倒数第二位获取车号
				lastIndex--;
				trainNoSb.append(lastchar);
			}
			StringBuilder temptrainNoSb = new StringBuilder();
			for (int i = 0; i < 4; i++) {// 从后获取四位数字直到字符不为数字结束。
				char tempChar = trainInfo.charAt(lastIndex);
				if (!Character.isDigit(tempChar)) {// 如果不是数字则返回
					break;
				}
				temptrainNoSb.append(tempChar);
				lastIndex--;
			}
			
			if (temptrainNoSb.length() < 4) {// 如果车号长度不足4位则在补“0”
				for (int j = 0; j <= 4 - temptrainNoSb.length(); j++) {
					temptrainNoSb.append("0");
				}
			}
			// 将车号组装起来
			trainNoSb.append(temptrainNoSb).reverse();
			// 2、获取车型，除了车号则都视为车型,根据解析后的车型查询车型信息；
			typeName = trainInfo.substring(0, lastIndex + 1) ;
			trainNo = trainNoSb.toString() ;
			// 判断机车信息长度，如果为6为字符则按照前两位是车型、后四位是车号的规则；
		}
		// 返回车型车号数组
		if(!StringUtil.isNullOrBlank(typeName) && !StringUtil.isNullOrBlank(trainNo)){
			String[] trainTypeAndNO = {typeName, trainNo} ;
			return trainTypeAndNO;
		}
		return null;
	}
	

	/**
	 * 根据机车GUID，机车别名或车型车号查询机车入段或最近（系统配置机车出段超时时长“ck.jczb.trainAccessAccount.trainAccessTimeOut”中设置的时间区间内，默认为“2小时”）出段的机车出入段台账；
	 * 
	 * @param trainAccessAccount
	 * @return
	 */
	private TrainAccessAccount getInOrLatestOutAccount(TrainAccessAccount trainAccessAccount) {
		// 构造hql语句
		StringBuilder sb = new StringBuilder("from TrainAccessAccount where recordStatus=0 and (outTime is null or outTime > to_date('").append(
				getTrainAccessTimeOutTimeStr(trainAccessAccount.getInTime())).append("','YYYY-MM-DD HH24:Mi:SS')) and (");
		StringBuilder subSt = new StringBuilder();
		if (!StringUtil.isNullOrBlank(trainAccessAccount.getTrainGUID())) {
			subSt.append("or trainGUID = '").append(trainAccessAccount.getTrainGUID()).append("' ");
		}
		if (!StringUtil.isNullOrBlank(trainAccessAccount.getTrainAliasName())) {
			subSt.append("or trainAliasName = '").append(trainAccessAccount.getTrainAliasName()).append("' ");
		}
		if (!StringUtil.isNullOrBlank(trainAccessAccount.getTrainTypeShortName()) && !StringUtil.isNullOrBlank(trainAccessAccount.getTrainNo())) {
			subSt.append("or (trainTypeShortName = '").append(trainAccessAccount.getTrainTypeShortName()).append("' and trainNo = '").append(
					trainAccessAccount.getTrainNo()).append("') ");
		}
		sb.append(subSt.substring(subSt.indexOf("or") + 2));
		sb.append(")");
		return this.findSingle(sb.toString());
	}

	/**
	 * 获取机车出段超时时间字符串返回时间格式为“yyyy-MM-dd HH:mm:ss”
	 * 
	 * @return String 机车出段超时时间字符串
	 */
	private String getTrainAccessTimeOutTimeStr(Date inTime) {
		// 获取系统配置的机车出段超时时长，并构造系统入段台账信息
		int timeOut = Integer.parseInt(StringUtil.nvlTrim(SystemConfigUtil.getValue("ck.jczb.trainAccessAccount.trainAccessTimeOut"), "0"));
		if (timeOut <= 0) {
			// 如果设置的时长为0则默认设置为2小时
			// timeOut = 2 * 60;
		}
		Calendar nowTime = Calendar.getInstance();
		if(inTime != null){
			nowTime.setTime(inTime);
		}
		nowTime.add(Calendar.MINUTE, -timeOut); // 当前分钟减去规定时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(nowTime.getTime());
	}

	/**
	 * <li>说明：清除出段信息，设置机车为入段状态
	 * <li>创建人：汪东良
	 * <li>创建日期：2016-7-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param inOrLatestOutAccount
	 *            机车入段台账
	 */
	private void cleanOutInfo(TrainAccessAccount inOrLatestOutAccount) {
		inOrLatestOutAccount.setOutTime(null);
		inOrLatestOutAccount.setOutHandlePersonID(null);
		inOrLatestOutAccount.setOutHandlePersonName("");
	}

	/**
	 * <li>说明：更新已入段机车位置信息
	 * <li>创建人：汪东良
	 * <li>创建日期：2016-7-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param account
	 *            现数据库中机车入段实体对象
	 * @param entity
	 *            台位图传递的入段实体
	 * @return 机车入段实体对象列表
	 */
	private void updateInAccountinfo(TrainAccessAccount account, TrainAccessAccount entity) {
		account.setEquipClass(entity.getEquipClass());// 设置机车所上设备类型
		account.setEquipNo(entity.getEquipNo());// 设置机车上设备编号
		account.setEquipName(entity.getEquipName());// 设置机车上设备名称
		account.setOnEquipTime(entity.getOnEquipTime());// 设置机车上设备时间
		account.setTrainAliasName(entity.getTrainAliasName());// 设置机车别名
		account.setTrainGUID(entity.getTrainGUID());// 设置机车GUID
		account.setEquipGUID(entity.getEquipGUID());// 设置机车设备GUID
		account.setEquipOrder(entity.getEquipOrder());// 设置设备顺序
		if(!StringUtil.isNullOrBlank(entity.getSiteID())){
			account.setSiteID(entity.getSiteID());// 更新站点ID
		}
		if(!StringUtil.isNullOrBlank(entity.getTrainAliasName())){
			account.setTrainAliasName(entity.getTrainAliasName());
		}else{
			account.setTrainAliasName(entity.getTrainTypeShortName() + entity.getTrainNo());
		}
	}

	/**
	 * <li>说明：构建机车入库台账实体
	 * <li>创建人：程锐
	 * <li>创建日期：2015-1-16
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainAccessAccount
	 *            机车入库台账实体
	 * @return 机车入库台账实体
	 * @throws NoSuchFieldException
	 */
	private void buildTrainInEntity(TrainAccessAccount trainAccessAccount) throws NoSuchFieldException {
		trainAccessAccount.setInTime(trainAccessAccount.getInTime() == null ? new Date() : trainAccessAccount.getInTime());
		if (SystemContext.getAcOperator() == null) {// 如果当前线程的登录操作员为空则设置默认操作员。
			DefaultUserUtilManager.setDefaultOperator();
		}
		OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
		trainAccessAccount.setInHandlePersonID(emp != null ? emp.getEmpid() : null);
		trainAccessAccount.setInHandlePersonName(emp != null ? emp.getEmpname() : "");
		trainAccessAccount.setStartTime(new Date());
		trainAccessAccount = EntityUtil.setSysinfo(trainAccessAccount);
		trainAccessAccount = EntityUtil.setNoDelete(trainAccessAccount);
		// 设置机车配属段信息
		if (StringUtil.isNullOrBlank(trainAccessAccount.getDID())) {// 如果机车配属为空则设置机车配属
			JczlTrainView jczlTrain = getJczlTrainByTrain(trainAccessAccount.getTrainTypeIDX(), trainAccessAccount.getTrainNo());
			if (jczlTrain != null) {
				trainAccessAccount.setDID(jczlTrain.getDId());
				trainAccessAccount.setDName(jczlTrain.getDName());
			}
		}
		// 构造车型别名
		if(StringUtil.isNullOrBlank(trainAccessAccount.getTrainAliasName())){
			trainAccessAccount.setTrainAliasName(trainAccessAccount.getTrainTypeShortName()+trainAccessAccount.getTrainNo());
		}
	}

	/**
	 * <li>说明：根据车型车号获取机车信息
	 * <li> 此业务逻辑依据【前台js选车号带出配属段信息】
	 * <li>创建人：程锐
	 * <li>创建日期：2015-1-19
	 * <li>修改人：程锐
	 * <li>修改日期：2016-5-27
	 * <li>修改内容：根据车型车号获取机车信息
	 * 
	 * @param trainTypeIDX
	 *            车型主键
	 * @param trainNo
	 *            车号
	 * @return 机车信息
	 */
	@SuppressWarnings("unchecked")
	private JczlTrainView getJczlTrainByTrain(String trainTypeIDX, String trainNo) {
		List<JczlTrainView> list = new ArrayList<JczlTrainView>();
		try {
			if (StringUtil.isNullOrBlank(trainTypeIDX))
				return null;
			Map<String, String> queryParamsMap = new HashMap<String, String>();
			queryParamsMap.put("trainTypeIDX", trainTypeIDX);
			queryParamsMap.put("trainNo", trainNo);
			queryParamsMap.put("isAll", "yes");
			queryParamsMap.put("isCx", "no");
			queryParamsMap.put("isRemoveRun", "yes");
			Map<String, Object> map = trainNoManager.page(queryParamsMap, 0, 10000);
			list = (List<JczlTrainView>) map.get("root");
		} catch (BusinessException e) {
			ExceptionUtil.process(e, logger);
			return null;
		}
		if (list == null || list.size() < 1)
			return null;
		return list.get(0);
	}
	
	public class InAndOutMes<O,T>{
		
		public InAndOutMes(O operateReturnMessage,T trainAccessAccount){
			this.operateReturnMessage=operateReturnMessage;
			this.trainAccessAccount=trainAccessAccount;
		}
		
		O operateReturnMessage ;
		
		T trainAccessAccount;
		
		public T getTrainAccessAccount() {
			return trainAccessAccount;
		}
		
		public void setTrainAccessAccount(T trainAccessAccount) {
			this.trainAccessAccount = trainAccessAccount;
		}
		
		public O getOperateReturnMessage() {
			return operateReturnMessage;
		}
		
		public void setOperateReturnMessage(O operateReturnMessage) {
			this.operateReturnMessage = operateReturnMessage;
		}

	}
}
