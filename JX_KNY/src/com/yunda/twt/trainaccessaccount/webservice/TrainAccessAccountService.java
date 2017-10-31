package com.yunda.twt.trainaccessaccount.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.IEosDictEntryManager;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountAutoManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessInAndOutManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessInAndOutManager.InAndOutMes;
import com.yunda.twt.twtinfo.entity.SiteTrack;
import com.yunda.twt.twtinfo.manager.SiteTrackManager;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.webservice.common.util.DefaultUserUtilManager;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.common.manager.TrainNoManager;
import com.yunda.zb.common.webservice.ITerminalCommonService;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车出入段接口实现类
 * <li>创建人：程锐
 * <li>创建日期：2015-1-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainAccessAccountService")
public class TrainAccessAccountService implements ITrainAccessAccountService {

	/** 日志工具 */
	Logger logger = Logger.getLogger(this.getClass());

	/** 车型业务类 */
	@Autowired
	private TrainTypeManager trainTypeManager;

	/** 操作者业务类 */
	@Autowired
	private AcOperatorManager acOperatorManager;

	/** 机车出入段台账自动出入段业务类 */
	@Autowired
	private TrainAccessAccountAutoManager trainAccessAccountAutoManager;

	/** 机车出入段台账业务类 */
	@Autowired
	private TrainAccessAccountManager trainAccessAccountManager;

	/** 机车出入段台账查询业务类 */
	@Autowired
	private TrainAccessAccountQueryManager trainAccessAccountQueryManager;

	/** 数据字典接口查询功能 */
	@Autowired
	private IEosDictEntryManager iEosDictEntryManager;

	/**
	 * 机车出入段业务操作实体
	 */
	@Autowired
	private TrainAccessInAndOutManager trainAccessInAndOutManager;

	/**
	 * 工位终端共有接口
	 */
	@Autowired
	private ITerminalCommonService terminalCommonService;

	/** 车号业务类 */
	@Autowired
	private TrainNoManager trainNoManager;

	/** 股道业务类 */
	@Resource
	private SiteTrackManager siteTrackManager;

	/** 系统出错信息 */
	private static final String MESSAGE_ERROR = "error";

	/**
	 * <li>说明：获取承修车型列表
	 * <li>创建人：程锐
	 * <li>创建日期：2015-1-19
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @return 承修车型列表
	 */
	@SuppressWarnings("unchecked")
	public String getUndertakeTrainType() {
		try {
			OmOrganization org = OmOrganizationSelectManager.getOrgByOrgcode(JXSystemProperties.OVERSEA_ORGCODE);
			Map map = trainTypeManager.page("", JSONUtil.read("{}", Map.class), 0, 100, "", "yes", org.getOrgseq());
			List<TrainType> list = (List<TrainType>) map.get("root");
			return JSONUtil.write(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("TrainAccessAccountService.getUndertakeTrainType：异常:" + e + "\n原因" + e.getMessage());
			return "error";
		}
	}

	/**
	 * <li>说明：根据车型获取车号列表
	 * <li>创建人：程锐
	 * <li>创建日期：2015-4-8
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainTypeShortName 车型简称
	 * @return 车号列表JSON字符串
	 */
	@SuppressWarnings("unchecked")
	public String getTrainNoByTrainType(String trainTypeShortName) {
		try {
			Map queryParamsMap = new HashMap();
			queryParamsMap.put("trainTypeShortName", trainTypeShortName);
			queryParamsMap.put("isAll", "yes");
			queryParamsMap.put("isCx", "no");
			queryParamsMap.put("isRemoveRun", "true");
			Map map = trainNoManager.page(queryParamsMap, 0, 10000);
			return JSONUtil.write(map);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return MESSAGE_ERROR;
		}
	}

	/**
	 * <li>说明：机车入段（手工入段）
	 * <li>创建人：程锐
	 * <li>创建日期：2015-1-19
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainTypeIdx 车型ID
	 * @param trainno  车号
	 * @param siteID  站场ID
	 * @param uid 操作者ID
	 * @param toGo 入段去向
	 * @return 操作成功或操作错误信息
	 */
	public String intoWareHouseForPDA(String trainTypeIdx, String trainno, String siteID, String uid, String toGo) {
		AcOperator ac = acOperatorManager.findLoginAcOprator(uid);
		SystemContext.setAcOperator(ac);
		TrainType trainType = trainTypeManager.getModelById(trainTypeIdx);

		if (trainType == null) {
			return "车型为空";
		}
		try {
			// 2016-07-21：将trainAccessAccountManager.saveOrUpdateInCHSB(trainInfo,
			// inTime);方法替换成trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(trainAccessAccount);
			TrainAccessAccount trainAccessAccount = new TrainAccessAccount();
			trainAccessAccount.setTrainTypeIDX(trainType.getTypeID());
			trainAccessAccount.setTrainTypeShortName(trainType.getShortName());
			trainAccessAccount.setToGo(toGo);
			trainAccessAccount.setTrainNo(trainno);
			trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(trainAccessAccount);
			// trainAccessAccountManager.saveOrUpdateInPDA(trainno, trainType,
			// toGo);
			return "true";
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return "提交失败!" + e.toString();
		}
	}

	/**
	 * <li>说明：自动入段机车（台位图）
	 * <li>创建人：程锐
	 * <li>创建日期：2015-2-9
	 * <li>修改人：汪东良
	 * <li>修改日期：2016-07-21
	 * <li>修改内容：修改机车自动入段的方式，将所有机车自动入段都修改为统一通过trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(accessAccount)进行调用。
	 * 
	 * @param trainList 入段的机车信息列表
	 * @return 操作成功或失败信息
	 */
	public String intoAccessByTWT(String trainList) {
		try {
			return intoAccessBatch(trainList);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return WsConstants.OPERATE_FALSE;
		}

	}

	/**
	 * <li>说明：机车批量入段
	 * <li>创建人：汪东良
	 * <li>创建日期：2016-07-21
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainList
	 *            入段的机车信息列表
	 * @return 操作成功或失败信息
	 * @throws IOException
	 */
	private String intoAccessBatch(String trainList) throws IOException {
		OperateReturnMessage msg = new OperateReturnMessage();
		TrainAccessAccount[] entityArray = JSONUtil.read(trainList, TrainAccessAccount[].class);
		List<TrainAccessAccount> accessAccountList = new ArrayList<TrainAccessAccount>();
		if (entityArray == null || entityArray.length < 1) {
			return JSONUtil.write(msg.setFaildFlag("操作失败！传入的参数为空trainList：" + trainList));
		}
		StringBuffer errMsg = new StringBuffer();
		for (TrainAccessAccount accessAccount : entityArray) {
			try {
				InAndOutMes<OperateReturnMessage,TrainAccessAccount> inAndOutMes = trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(accessAccount);
				if("true".equals(inAndOutMes.getOperateReturnMessage().getFlag())){
					accessAccountList.add(accessAccount);
	            }else{
	            	if(!StringUtil.isNullOrBlank(accessAccount.getTrainAliasName())){
	            		errMsg.append("【别名】："+accessAccount.getTrainAliasName()+"入段失败，原因:");
	            	}else{
	            		errMsg.append("【车型简称】："+accessAccount.getTrainTypeShortName()+"【车号】："+accessAccount.getTrainNo()+"入段失败，原因:");
	            	}
	            	errMsg.append(inAndOutMes.getOperateReturnMessage().getMessage()+"；");
	            }
			} catch (Exception e) {
				ExceptionUtil.process(e, logger);
			}
		}
		if (accessAccountList.size() < 1) {
			if(!StringUtil.isNullOrBlank(errMsg.toString())){
				msg.setFaildFlag("操作失败！"+errMsg.toString());
			}else{
				msg.setFaildFlag("操作失败！");
			}
		} else {
			if(!StringUtil.isNullOrBlank(errMsg.toString())){
				msg.setSucessFlag("操作成功:" + accessAccountList.size() + "条,操作失败" + (entityArray.length - accessAccountList.size()) + "条！"+errMsg.toString());
			}else{
				msg.setSucessFlag("操作成功:" + accessAccountList.size() + "条,操作失败" + (entityArray.length - accessAccountList.size()) + "条！");
			}
		}
		return JSONUtil.write(msg);

	}

	/**
	 * <li>说明：自动入段机车（车号识别，小石坝用，以后合并）
	 * <li>创建人：程锐
	 * <li>创建日期：2015-2-6
	 * <li>修改人：汪东良
	 * <li>修改日期：2016-07-21
	 * <li>修改内容：修改机车自动入段的方式，将所有机车自动入段都修改为统一通过trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(accessAccount)进行调研。
	 * 
	 * @param trainInfo
	 *            车型车号字符串，后四位为车号，前面字符为车型全称
	 * @param inTime
	 *            入段时间
	 * @return 操作成功或操作错误信息
	 */
	public String intoAccessByCHSB(String trainInfo, String inTime) {
		OperateReturnMessage msg = new OperateReturnMessage();
		try {
			// 2016-07-21：将trainAccessAccountManager.saveOrUpdateInCHSB(trainInfo,
			// inTime);方法替换成trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(trainAccessAccount);
			TrainAccessAccount trainAccessAccount = new TrainAccessAccount();
			trainAccessAccount.setTrainAliasName(trainInfo);
			trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(trainAccessAccount);
			// trainAccessAccountManager.saveOrUpdateInCHSB(trainInfo, inTime);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONObject.toJSONString(msg);
	}

	/**
	 * <li>说明：自动入段机车（车号识别）
	 * <li>创建人：程锐
	 * <li>创建日期：2015-2-6
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainInfo
	 *            车型车号字符串，后四位为车号，前面字符为车型全称
	 * @param inTime
	 *            入段时间
	 * @param siteID
	 *            站场标示
	 * @param trackName
	 *            股道名称
	 * @return 操作成功或操作错误信息
	 */
	public String intoAccessByCHSB(String trainInfo, String inTime, String siteID, String trackName) {
		OperateReturnMessage msg = new OperateReturnMessage();
		try {
			TrainAccessAccount trainAccessAccount = new TrainAccessAccount();
			trainAccessAccount.setTrainAliasName(trainInfo);
			trainAccessAccount.setSiteID(siteID);
			// 获取并设置股道信息
			SiteTrack track = siteTrackManager.getTrackBySiteIDAndName(siteID, trackName);
			if (track != null) {
				trainAccessAccount.setEquipClass(TrainAccessAccount.EQUIPCLASS_GD_CH);
				trainAccessAccount.setEquipName(trackName);
				trainAccessAccount.setEquipNo(track.getTrackCode());
			}
			trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(trainAccessAccount);
			// trainAccessAccountManager.saveOrUpdateInCHSB(trainInfo, inTime,
			// siteID, trackName);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONObject.toJSONString(msg);
	}

	/**
	 * <li>说明：机车出段（台位图）
	 * <li>创建人：程锐
	 * <li>创建日期：2015-2-6
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainList
	 *            出段的机车信息列表
	 * @return 入段机车信息列表JSON字符串或操作失败信息
	 */
	public String outAccessByTWT(String trainList) {
		try {
			TrainAccessAccount[] entityArray = JSONUtil.read(trainList, TrainAccessAccount[].class);
			logger.info("-------------------------------台位图出段机车列表:" + trainList);
			String errMsg = trainAccessAccountAutoManager.saveOrUpdateListOut(entityArray);
			if (errMsg == null)
				return WsConstants.OPERATE_SUCCESS;
			else
				return CommonUtil.buildFalseJSONMsg(errMsg, logger);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return WsConstants.OPERATE_FALSE;
		}
	}

	/**
	 * <li>说明：机车出段（车号识别）
	 * <li>创建人：程锐
	 * <li>创建日期：2015-2-6
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainInfo
	 *            车型车号字符串，后四位为车号，前面字符为车型全称
	 * @param outTime
	 *            出段时间
	 * @return 操作成功或操作错误信息
	 */
	public String outAccessByCHSB(String trainInfo, String outTime) {
		OperateReturnMessage msg = new OperateReturnMessage();
		try {
			trainAccessAccountManager.saveOrUpdateOutCHSB(trainInfo, outTime);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONObject.toJSONString(msg);
	}

	/**
	 * <li>说明：删除机车入库信息及终止相关流程
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-3
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainInfo
	 *            由A,B部分组成A部分为前两位，是车型（事先约定，如H3,S3,S7），B部分为车号，如不足4位，则在B部分前面补0，直到满足4位。例如:
	 *            “H30355”
	 * @return String
	 */
	public String updateStatusForTerminateProcess(String trainInfo) {
		// TODO
		return null;
	}

	/**
	 * <li>说明：台位图通过台位图服务子系统获取当前站点的所有在段机车列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-5-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param siteID
	 *            站点标识
	 * @return 所有在段机车列表
	 */
	public String getInAccountTrainList(String siteID) {
		List<TrainAccessAccount> list = trainAccessAccountQueryManager.getInTrainListBySiteID(siteID);
		try {
			List<TrainAccessAccountBean> beanList = new ArrayList<TrainAccessAccountBean>();
			for (TrainAccessAccount account : list) {
				TrainAccessAccountBean bean = new TrainAccessAccountBean();
				BeanUtils.copyProperties(bean, account);
				bean.setEquipClass(StringUtil.nvlTrim(account.getEquipClass(), ""));
				bean.setCtfx(StringUtil.nvlTrim(account.getCtfx(), ""));
				bean.setTrainAliasName(StringUtil.nvlTrim(account.getTrainAliasName(), ""));
				bean.setTrainTypeShortName(StringUtil.nvlTrim(account.getTrainTypeShortName(), ""));
				bean.setTrainNo(StringUtil.nvlTrim(account.getTrainNo(), ""));
				bean.setTrainAliasName(StringUtil.nvlTrim(account.getTrainAliasName(), ""));
				bean.setRepairClassName(StringUtil.nvlTrim(account.getRepairClassName(), ""));
				bean.setInTime(account.getInTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getInTime()) : "");
                //添加整备机车在段倒计时
                bean.setLastTime(StringUtil.nvlTrim(ZbConstants.ZB_LAST_TIME, ""));
				bean.setOnEquipTime(account.getOnEquipTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getOnEquipTime()) : "");
				beanList.add(bean);
			}
			logger.info("---------------------在段机车列表:" + JSONUtil.write(beanList));
			return JSONUtil.write(beanList);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return WsConstants.OPERATE_FALSE;
		}
	}

	/**
	 * <li>说明：获取机车入段去向数据字典列表
	 * <li>创建人：程锐
	 * <li>创建日期：2015-3-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @return 机车入段去向数据字典列表
	 */
	public String getTrainToGo() {
		try {
			String hql = "from EosDictEntry where status = '1' and id.dicttypeid='TWT_TRAIN_ACCESS_ACCOUNT_TOGO' and parentid is not null order by id.dictid, sortno";
			List<EosDictEntry> eosdyList = iEosDictEntryManager.findToList(hql);
			List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
			for (EosDictEntry eos : eosdyList) {
				EosDictEntryBean bean = new EosDictEntryBean();
				bean.setDictid(eos.getId().getDictid());
				bean.setDictname(eos.getDictname());
				beanlist.add(bean);
			}
			return JSONUtil.write(beanlist);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return WsConstants.OPERATE_FALSE;
		}
	}

	/**
	 * <li>说明：根据操作员id获取在段机车列表
	 * <li>创建人：程锐
	 * <li>创建日期：2015-4-2
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param operatorid
	 *            操作员id
	 * @return 在段机车列表
	 */
	public String getInAccountTrainListByUser(Long operatorid) {
		terminalCommonService.setAcOperatorById(operatorid);
		String siteID = EntityUtil.findSysSiteId("");
		List<TrainAccessAccount> list = trainAccessAccountQueryManager.getInTrainListBySiteID(siteID);
		try {
			List<TrainAccessAccountBean> beanList = new ArrayList<TrainAccessAccountBean>();
			for (TrainAccessAccount account : list) {
				if (StringUtil.isNullOrBlank(account.getTrainTypeShortName()) || StringUtil.isNullOrBlank(account.getTrainNo()))
					continue;
				TrainAccessAccountBean bean = new TrainAccessAccountBean();
				BeanUtils.copyProperties(bean, account);
				bean.setInTime(account.getInTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getInTime()) : "");
				bean.setOnEquipTime(account.getOnEquipTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getOnEquipTime()) : "");
				beanList.add(bean);
			}
			logger.info("---------------------根据操作者获取在段机车列表:" + JSONUtil.write(beanList));
			return JSONUtil.write(beanList);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return WsConstants.OPERATE_FALSE;
		}
	}

	/**
	 * <li>说明：获取台位图-确定入段去向接口-机车出入段台账信息
	 * <li>创建人：程锐
	 * <li>创建日期：2015-4-8
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainInfo
	 *            机车信息JSON字符串
	 * @return 机车出入段台账信息
	 */
	public String getTrainInfoForToGo(String trainInfo) {
		try {
			TrainAccessAccount account = trainAccessAccountQueryManager.getAccountByTrainInfo(trainInfo);
			if (account == null)
				throw new BusinessException("无对应的入段机车");
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("idx", account.getIdx());
			jsonObj.put("trainTypeIDX", account.getTrainTypeIDX());
			jsonObj.put("trainTypeShortName", account.getTrainTypeShortName());
			jsonObj.put("trainNo", account.getTrainNo());
			jsonObj.put("inTime", account.getInTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getInTime()) : "");
			jsonObj.put("planOutTime", account.getPlanOutTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getPlanOutTime()) : "");
			jsonObj.put("toGo", account.getToGo());
			boolean isZB = false;
			if (!StringUtil.isNullOrBlank(account.getToGo())) {
				EosDictEntry entry = iEosDictEntryManager.findCacheEntry(account.getToGo(), "TWT_TRAIN_ACCESS_ACCOUNT_TOGO");
				if (entry != null)
					jsonObj.put("toGoCH", iEosDictEntryManager.findCacheEntry(account.getToGo(), "TWT_TRAIN_ACCESS_ACCOUNT_TOGO").getDictname());
				isZB = TrainAccessAccount.TRAINTOGO_ZB.equals(account.getToGo()) ? true : false;
			}
			jsonObj.put("isZB", isZB);
			return jsonObj.toJSONString();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return CommonUtil.buildFalseJSONMsg(e.getMessage(), logger);
		}
	}

	/**
	 * <li>说明：台位图-确定入段去向
	 * <li>创建人：程锐
	 * <li>创建日期：2015-4-8
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param jsonData
	 *            机车入段台账实体json字符串
	 * @return 操作成功与否
	 */
	public String confirmTogo(String jsonData) {
		try {
			TrainAccessAccount entity = JSONUtil.read(jsonData, TrainAccessAccount.class);
			trainAccessAccountManager.confirmTogo(entity);
			return WsConstants.OPERATE_SUCCESS;
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return WsConstants.OPERATE_FALSE;
		}
	}

	/**
	 * <li>说明：台位图-设置车头方向
	 * <li>创建人：程锐
	 * <li>创建日期：2016-5-7
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainAliasName
	 *            机车简称
	 * @param ctfx
	 *            车头方向
	 * @return 操作成功与否
	 */
	public String setCtfx(String trainAliasName, String ctfx) {
		try {
			TrainAccessAccount account = new TrainAccessAccount();
			account.setTrainAliasName(trainAliasName);
			TrainAccessAccount trainAccessAccount = trainAccessAccountQueryManager.getAccountByTrainInfo(JSONUtil.write(account));
			if (trainAccessAccount == null)
				return WsConstants.OPERATE_FALSE;
			trainAccessAccount.setCtfx(ctfx);
			DefaultUserUtilManager.setDefaultOperator();
			trainAccessAccountManager.saveOrUpdate(trainAccessAccount);
			return WsConstants.OPERATE_SUCCESS;
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			return WsConstants.OPERATE_FALSE;
		}
	}
}
