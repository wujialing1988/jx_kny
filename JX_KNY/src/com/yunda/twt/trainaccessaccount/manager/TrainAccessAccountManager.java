package com.yunda.twt.trainaccessaccount.manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IEosDictEntryManager;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrainView;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrain;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType;
import com.yunda.jx.jczl.undertakemanage.manager.UndertakeTrainManager;
import com.yunda.jx.jczl.undertakemanage.manager.UndertakeTrainTypeManager;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.jxpz.utils.TrainTypeMappingUtil;
import com.yunda.twt.httpinterface.manager.TwtLoginManager;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.webservice.TrainAccessAccountBean;
import com.yunda.twt.twtinfo.entity.SiteTrack;
import com.yunda.twt.twtinfo.manager.SiteTrackManager;
import com.yunda.twt.twtinfo.manager.TrainStatusColorsManager;
import com.yunda.twt.webservice.client.ITWTTrainStatusService;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.util.DefaultUserUtilManager;
import com.yunda.zb.common.manager.TrainNoManager;
import com.yunda.zb.pczz.entity.ZbglPczz;
import com.yunda.zb.pczz.entity.ZbglPczzItem;
import com.yunda.zb.pczz.entity.ZbglPczzItemToTraininfo;
import com.yunda.zb.pczz.entity.ZbglPczzWI;
import com.yunda.zb.pczz.entity.ZbglPczzWiItem;
import com.yunda.zb.pczz.manager.ZbglPczzItemManager;
import com.yunda.zb.pczz.manager.ZbglPczzItemToTraininfoManager;
import com.yunda.zb.pczz.manager.ZbglPczzManager;
import com.yunda.zb.pczz.manager.ZbglPczzWIManager;
import com.yunda.zb.pczz.manager.ZbglPczzWiItemManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.tp.manager.ZbglTpManager;
import com.yunda.zb.trainwarning.manager.ZbglWarningManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainAccessAccount业务类,机车出入段台账
 * <li>创建人：程锐
 * <li>创建日期：2015-01-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainAccessAccountManager")
public class TrainAccessAccountManager extends JXBaseManager<TrainAccessAccount, TrainAccessAccount> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车信息业务类 */
    // FIXME 调用检修业务类
    @Resource
    private JczlTrainManager jczlTrainManager;
    
    /** 承修车型业务类 */
    // FIXME 调用检修业务类
    @Resource
    private UndertakeTrainTypeManager undertakeTrainTypeManager;
    
    /** 承修机车业务类 */
    // FIXME 调用检修业务类
    @Resource
    private UndertakeTrainManager undertakeTrainManager;
    
    /** 人员业务类 */
    @Resource
    private IOmEmployeeManager omEmployeeManager;
    
    /** 机车出入段台账查询业务类 */
    @Resource
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /** 机车状态历史业务类 */
    @Resource
    private TrainStatusHisManager trainStatusHisManager;
    
    /** 车型业务类 */
    @Autowired
    private TrainTypeManager trainTypeManager;
    
    /** 车号业务类 */
    @Autowired
    private TrainNoManager trainNoManager;
    
    /** 机车状态管理客户端业务类 */
    @Autowired
    private ITWTTrainStatusService twtTrainStatusService;
    
    /** 机车状态管理客户端业务类 */
    @Resource
    private IEosDictEntryManager iEosDictEntryManager;  
    
    /** 股道业务类 */
    @Resource
    private SiteTrackManager siteTrackManager;

    /** 整备单业务类 */
    @Resource
    private ZbglRdpManager zbglRdpManager;

    /** 机车检修作业计划查询业务类 */
    @Resource    
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager;
    
    /** ZbglWarning业务类,机车检测预警 */
    @Resource    
    private ZbglWarningManager zbglWarningManager;
    
    /** ZbglTp业务类,JT6提票 */
    @Resource    
    private ZbglTpManager zbglTpManager;
    
    /** ZbglPczzItemToTraininfoManager业务类,普查整治项中保存的机车信息 */
    @Resource    
    private ZbglPczzItemToTraininfoManager zbglPczzItemToTraininfoManager;
    
    /** ZbglPczzWI业务类,普查整治任务单 */
    @Resource    
    private ZbglPczzWIManager zbglPczzWIManager;
    
    /** ZbglPczz业务类,普查整治计划 */
    @Resource    
    private ZbglPczzManager zbglPczzManager;
    
    /** ZbglPczzItem业务类,普查整治计划项 */
    @Resource    
    private ZbglPczzItemManager zbglPczzItemManager;
    
    /** ZbglPczzWiItem业务类,普查整治任务项 */
    @Resource    
    private ZbglPczzWiItemManager zbglPczzWiItemManager;
    
    /** 台位图登陆业务类 */
    @Resource    
    private TwtLoginManager twtLoginManager;
    
    /** 机车出入段业务类 */
    @Resource
    private TrainAccessInAndOutManager trainAccessInAndOutManager;
    
    /**
     * <li>说明：机车入段（PDA端）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-7
     * <li>修改人：汪东良
     * <li>修改日期：2016-07-21
     * <li>修改内容：此方法不推荐使用，后期会删除被@see TrainAccessInAndOutManager.saveOrUpdateTrainAccessIn(TrainAccessAccount trainAccessAccount)替换；
     * @param trainno 车号
     * @param trainType 车型信息实体
     * @param toGo 入段去向
     * @throws Exception
     */
    @Deprecated
    public void saveOrUpdateInPDA(String trainno, TrainType trainType, String toGo) throws Exception {
        TrainAccessAccount entity = new TrainAccessAccount();
        entity = setInEntityProperty(trainno, trainType, toGo, entity);
        saveOrUpdateIn(entity);
    }
    
    /**
     * <li>说明：机车入段（车号识别）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-11
     * <li>修改人：汪东良
     * <li>修改日期：2016-07-21
     * <li>修改内容：此方法不推荐使用，后期会删除被@see TrainAccessInAndOutManager.saveOrUpdateTrainAccessIn(TrainAccessAccount trainAccessAccount)替换；
     *
     * @param trainInfo 车型车号字符串，后四位为车号，前面字符为车型全称
     * @param inTime 入段时间
     * @throws Exception
     */
    @Deprecated
    public void saveOrUpdateInCHSB(String trainInfo, String inTime) throws Exception {
        TrainType trainType = buildTrainTypeByCHSB(trainInfo);
        TrainAccessAccount entity = new TrainAccessAccount();
        entity.setInTime(!StringUtil.isNullOrBlank(inTime) ? DateUtil.yyyy_MM_dd_HH_mm_ss.parse(inTime) : null);
        entity = setInEntityProperty(trainType.getTrainNo(), trainType, "", entity);
        entity.setTrainAliasName(buildAliasName(trainType.getShortName()).concat(trainType.getTrainNo()));
        saveOrUpdateIn(entity);
    }
    
    /**
     * <li>说明：机车入段（车号识别）
     * <li>创建人：程锐
     * <li>创建日期：2015-11-27
     * <li>修改人：汪东良
     * <li>修改日期：2016-07-21
     * <li>修改内容：此方法不推荐使用，后期会删除被@see TrainAccessInAndOutManager.saveOrUpdateTrainAccessIn(TrainAccessAccount trainAccessAccount)替换；
     * 
     * @param trainInfo 车型车号字符串，后四位为车号，前面字符为车型全称
     * @param inTime 入段时间
     * @param siteID 站场标示
     * @param trackName 股道名称
     * @throws Exception
     */
    @Deprecated
    public void saveOrUpdateInCHSB(String trainInfo, String inTime, String siteID, String trackName) throws Exception {
        TrainType trainType = buildTrainTypeByCHSB(trainInfo);
        TrainAccessAccount entity = new TrainAccessAccount();
        entity.setInTime(!StringUtil.isNullOrBlank(inTime) ? DateUtil.yyyy_MM_dd_HH_mm_ss.parse(inTime) : null);
        entity = setInEntityProperty(trainType.getTrainNo(), trainType, "", entity);
        entity.setSiteID(siteID);
        entity.setTrainAliasName(buildAliasName(trainType.getShortName()));
        
        SiteTrack track = siteTrackManager.getTrackBySiteIDAndName(siteID, trackName);
        if (track != null) {
        	entity.setEquipClass(TrainAccessAccount.EQUIPCLASS_GD_CH);
        	entity.setEquipName(trackName);
        	entity.setEquipNo(track.getTrackCode());
        }
        saveOrUpdateIn(entity);
    }
    
    /**
     * <li>说明：机车入段
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：汪东良
     * <li>修改日期：2016-07-21
     * <li>修改内容：此方法不推荐使用，后期会删除被@see TrainAccessInAndOutManager.saveOrUpdateTrainAccessIn(TrainAccessAccount trainAccessAccount)替换；
     * 
     * @param entity 机车入段台账实体对象
     * @throws Exception
     */
    @Deprecated
    public void saveOrUpdateIn(TrainAccessAccount entity) throws Exception {
        if (!isAbleInTrain(entity.getTrainTypeIDX(), entity.getTrainNo(), entity.getIdx())) {
            throw new BusinessException("该机车已经入段,不能重复入段");
        }
        if (clearOutInfo(entity))
            return;
        saveJczlAndUndertakeTrain(entity);
        entity = buildTrainInEntity(entity);
        saveOrUpdate(entity);
        daoUtils.flush();
        if (!StringUtil.isNullOrBlank(entity.getTrainStatus()))
        	trainStatusHisManager.saveAndUpdateStatusHis(entity.getIdx(), entity.getTrainStatus());
        daoUtils.flush();
        updateBizByToGO(entity);        
    }
    
    /**
     * <li>说明：确认入段去向
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车入段台账实体对象
     * @throws Exception
     */
    public void confirmTogo(TrainAccessAccount entity) throws Exception {
        TrainAccessAccount account = validate(entity);
        boolean isZB = false;
        
        //判断入段去向是否在配置项中
        String togoString = SystemConfigUtil.getValue("ck.jczb.toGoForZbRdp");//配置项名称,获取到的值为基础管理-业务字典-台位图相关-字典项维护（例如0101，0301）
        // 如果已经选择了整备去向，说明已经生成了整备任务单，不允许修改入段去向 by wujl 20160725
        if(!StringUtil.isNullOrBlank(account.getToGo()) && togoString.contains(account.getToGo())){
        	isZB = true;
        } // 如果没有生成，但是页面传的值不在配置项中，也不能生成
        else if(!togoString.contains(entity.getToGo())){
        	isZB = true;
        }
        
        if (!isAbleInTrainByName(entity.getTrainTypeShortName(), entity.getTrainNo(), entity.getIdx())) {
            throw new BusinessException("该机车已经入段,不能重复入段,请重新选择机车");
        }
        account.setToGo(entity.getToGo());
        if (entity.getPlanOutTime() != null)
            account.setPlanOutTime(entity.getPlanOutTime());
        if (StringUtil.isNullOrBlank(account.getTrainTypeIDX())) {
            String trainTypeIDX = entity.getTrainTypeIDX();
            if (StringUtil.isNullOrBlank(trainTypeIDX) && !StringUtil.isNullOrBlank(entity.getTrainTypeShortName())) {
                TrainType trainType = trainTypeManager.getTrainType(entity.getTrainTypeShortName());
                trainTypeIDX = trainType == null ? "" : trainType.getTypeID();
            }
            account.setTrainTypeIDX(trainTypeIDX);
        }
        if (StringUtil.isNullOrBlank(account.getTrainTypeShortName())) {
            account.setTrainTypeShortName(entity.getTrainTypeShortName());
        }
        if (StringUtil.isNullOrBlank(account.getTrainNo())) {
            account.setTrainNo(entity.getTrainNo());
            saveJczlAndUndertakeTrain(entity);
        }
        DefaultUserUtilManager.setDefaultOperator();
        saveOrUpdate(account);
        if (isZB)
            return;
//        daoUtils.flush();        
        updateBizByToGO(account);
    }
    
    /**
     * <li>说明：机车出段
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车入段台账实体对象
     * @throws Exception
     */
    public void saveOrUpdateOut(TrainAccessAccount entity) throws Exception {
        TrainAccessAccount account = validate(entity);
        account = buildTrainOutEntity(account, entity);
        saveOrUpdate(account);
        // 出段时更新计划的实际出段时间
        trainAccessInAndOutManager.saveTrainEnforcePlanDetail(account, "out");
        trainStatusHisManager.updateOut(account.getIdx());
    }
    
    /**
     * <li>说明：机车出段（车号识别）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 车型车号字符串，后四位为车号，前面字符为车型全称
     * @param outTime 出段时间
     * @throws Exception
     */
    public void saveOrUpdateOutCHSB(String trainInfo, String outTime) throws Exception {
        TrainType trainType = buildTrainTypeByCHSB(trainInfo);
        TrainAccessAccount entity = new TrainAccessAccount();
        entity.setTrainNo(trainType.getTrainNo());
        entity.setTrainTypeIDX(trainType.getTypeID());
        entity.setTrainTypeShortName(trainType.getShortName());
        entity.setOutTime(!StringUtil.isNullOrBlank(outTime) ? DateUtil.yyyy_MM_dd_HH_mm_ss.parse(outTime) : null);
        TrainAccessAccount account = validate(entity);
        account = buildTrainOutEntity(account, entity);
        saveOrUpdate(account);
        trainStatusHisManager.updateOut(account.getIdx());
    }
    
    /**
     * <li>说明：编辑机车入段
     * <li>创建人：程锐
     * <li>创建日期：2015-3-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车入段台账实体对象
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public void updateIn(TrainAccessAccount entity) throws Exception {
        TrainAccessAccount account = validate(entity);
        
        String trainAccessAccountIDX = entity.getIdx();
        //通过出入段台账idx查询整备单对象，如果整备单已经交验，那么不允许修改入段去向
        ZbglRdp zbglRdp = zbglRdpManager.getRdpByAccount(trainAccessAccountIDX);
        if (zbglRdp != null) {
            if (zbglRdp.STATUS_HANDLED.equals(zbglRdp.getRdpStatus())) {
                throw new BusinessException("该机车已经交验,不能修改入段去向");
            }
        }
        
        boolean isZB = true;
        
        
        //20160804 林欢 根据业务需求，现在的入段去向可以修改
//        //判断入段去向是否在配置项中
//        String togoString = SystemConfigUtil.getValue("ck.jczb.toGoForZbRdp");//配置项名称,获取到的值为基础管理-业务字典-台位图相关-字典项维护（例如0101，0301）
//        // 如果已经选择了整备去向，说明已经生成了整备任务单，不允许修改入段去向 by wujl 20160725
//        if(!StringUtil.isNullOrBlank(account.getToGo()) && togoString.contains(account.getToGo())){
//        	isZB = true;
//        } // 如果没有生成，但是页面传的值不在配置项中，也不能生成
//        else if(!togoString.contains(entity.getToGo())){
//        	isZB = true;
//        }
        
        //判断当前车型车号是否有最近的检修作业计划关联，如果有，说明，该车进入流程，也不允许修改其入段去向
        TrainWorkPlan trainWorkPlan = trainWorkPlanQueryManager.findCurrentTrainWorkPlanInfo(entity.getTrainNo(), entity.getTrainTypeShortName());
        if (trainWorkPlan != null) {
        	//如果在，说明是整备去向，此时已经生成整备任务单，不允许修改入段去向了
            isZB = false;
        }
        
        if (!isAbleInTrain(entity.getTrainTypeIDX(), entity.getTrainNo(), trainAccessAccountIDX)) {
            throw new BusinessException("该机车已经入段,不能重复入段");
        }
        
        if (StringUtil.isNullOrBlank(account.getTrainTypeIDX()))
            saveJczlAndUndertakeTrain(entity);
        account.setTrainNo(entity.getTrainNo());
        account.setTrainTypeIDX(entity.getTrainTypeIDX());
        account.setTrainTypeShortName(entity.getTrainTypeShortName());
        account.setToGo(entity.getToGo());
        account.setDID(entity.getDID());
        account.setDName(entity.getDName());
        account.setInDriver(entity.getInDriver());
        account.setPlanOrder(entity.getPlanOrder());
        account.setArriveOrder(entity.getArriveOrder());
        account.setPlanOutTime(entity.getPlanOutTime());
        account.setInTime(entity.getInTime());
        account.setRepairClassIDX(entity.getRepairClassIDX());
        account.setRepairClassName(entity.getRepairClassName());
        account.setCtfx(entity.getCtfx());
        saveOrUpdate(account);
        zbglRdpManager.insertRdpOrInitRdpInfo(account,isZB);
//        if (isZB){
//            return;
//        }
        daoUtils.flush();
//        updateBizByToGO(account);      
    }
    
    /**
     * <li>说明：通过出入段台账idx查询入段去向
     * <li>创建人：程锐
     * <li>创建日期：2015-3-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车入段台账实体对象
     * @throws Exception
     */
    public String findToGoByTrainAccessAccointIDX(String trainAccessAccointIDX) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select a.To_Go from TWT_Train_Access_Account a where a.idx = '").append(trainAccessAccointIDX).append("'");
        
        return (String) this.getDaoUtils().executeSqlQuery(sb.toString()).get(0);
    }

    /**
     * <li>说明：根据系统配置的机车出段超时时间，在此时间内再入段不重新生成数据，只是清空前一记录的出段信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车出入段台账实体
     * @return true 机车再入段在系统配置的机车出段超时时间内，做清空前一记录出段信息操作， false 未满足条件，不做清空出段信息操作
     */
    public boolean clearOutInfo(TrainAccessAccount entity) {
        TrainAccessAccount account = trainAccessAccountQueryManager.getLatestOutedAccountByTrainIDX(entity.getTrainTypeIDX(), entity.getTrainNo(), entity.getTrainAliasName());        
        if (account != null && trainAccessAccountQueryManager.isClearOutInfo(account.getOutTime().getTime(), new Date().getTime())) {
            updateForClearOutInfo(account);
            return true;
        }
        return false;
    }
    
    /**
     * <li>说明：更新机车出入段台账的出段相关信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段台账实体对象
     */
    public void updateForClearOutInfo(TrainAccessAccount account) {
        account = setClearOutEntity(account);
        try {
            saveOrUpdate(account);
        } catch (BusinessException e) {
            ExceptionUtil.process(e, logger);
        } catch (NoSuchFieldException e) {
            ExceptionUtil.process(e, logger);
        }
    }
    
    /**
     * <li>说明：设置清空出段信息的实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 出段信息的实体
     * @return 机车出入段台账实体对象
     */
    public TrainAccessAccount setClearOutEntity(TrainAccessAccount account) {
        account.setOutDriver("");
        account.setOutHandlePersonID(null);
        account.setOutHandlePersonName("");
        account.setOutOrder("");
        account.setOutTime(null);
        return account;
    }
    
    /**
     * <li>说明：根据机车状态获取颜色值
     * <li>创建人：程锐
     * <li>创建日期：2015-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param status 机车状态
     * @return 颜色值
     */
    public static String getColorByStatus(String status) {
        TrainStatusColorsManager trainStatusColorsManager =
            (TrainStatusColorsManager) Application.getSpringApplicationContext().getBean("trainStatusColorsManager");
        return trainStatusColorsManager.getColorByStatus(status);
    }
    
    /**
     * <li>说明：机车入库时 ，如车号为系统不存在的情况，则添加机车基础信息:机车信息+承修车型信息+承修机车信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccount 机车出入段台账实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveJczlAndUndertakeTrain(TrainAccessAccount trainAccessAccount) throws BusinessException, NoSuchFieldException,
        IllegalAccessException, InvocationTargetException {
        if (!isExistInJczlTrain(trainAccessAccount.getTrainTypeIDX(), trainAccessAccount.getTrainNo())) {
            saveJczlTrain(trainAccessAccount);
        }
        if (!isExistInUndertakeTrain(trainAccessAccount.getTrainTypeIDX(), trainAccessAccount.getTrainNo())) {
            UndertakeTrainType undertakeTrainType = undertakeTrainTypeManager.getModel(trainAccessAccount.getTrainTypeIDX());
            if (undertakeTrainType == null) {
                undertakeTrainType = saveUndertakeTrainType(trainAccessAccount);
            }
            saveUndertakeTrain(trainAccessAccount, undertakeTrainType.getIdx());
        }
    }
    
    /**
     * <li>说明：构建机车入库台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccount 机车入库台账实体
     * @return 机车入库台账实体
     */
    public TrainAccessAccount buildTrainInEntity(TrainAccessAccount trainAccessAccount) {
        trainAccessAccount.setInTime(trainAccessAccount.getInTime() == null ? new Date() : trainAccessAccount.getInTime());
        DefaultUserUtilManager.setDefaultOperator();
        OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
        trainAccessAccount.setInHandlePersonID(emp != null ? emp.getEmpid() : null);
        trainAccessAccount.setInHandlePersonName(emp != null ? emp.getEmpname() : "");
//        trainAccessAccount.setTrainStatus(TrainAccessAccount.TRAINSTATUS_DAIJIAN);
        trainAccessAccount.setStartTime(new Date());
        return trainAccessAccount;
    }
    
    /**
     * <li>说明：构建机车出库台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccount 机车出库台账实体
     * @param entity 前端传入实体
     * @return 机车出库台账实体
     */
    public TrainAccessAccount buildTrainOutEntity(TrainAccessAccount trainAccessAccount, TrainAccessAccount entity) {
        trainAccessAccount.setOutTime(entity.getOutTime() == null ? new Date() : entity.getOutTime());
        DefaultUserUtilManager.setDefaultOperator();
        OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
        trainAccessAccount.setOutDriver(!StringUtil.isNullOrBlank(entity.getOutDriver()) ? entity.getOutDriver() : "");
        trainAccessAccount.setOutHandlePersonID(emp != null ? emp.getEmpid() : null);
        trainAccessAccount.setOutHandlePersonName(emp != null ? emp.getEmpname() : "");
        trainAccessAccount.setOutOrder(!StringUtil.isNullOrBlank(entity.getOutOrder()) ? entity.getOutOrder() : "");
        //JIRA JX——255 小石坝-机车出段时，不清除入段台账上的位置信息
//        trainAccessAccount.setEquipClass("");
//        trainAccessAccount.setEquipGUID("");
//        trainAccessAccount.setEquipName("");
//        trainAccessAccount.setEquipNo("");
//        trainAccessAccount.setEquipOrder("");
//        trainAccessAccount.setOnEquipTime(null);
//        trainAccessAccount.setTrainPositionHISIDX("");
        return trainAccessAccount;
    }
    
    /**
     * <li>说明：更新机车状态
     * <li>创建人：程锐
     * <li>创建日期：2015-3-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccountIDX 机车出入段台账IDX
     * @param trainStatus 机车状态
     * @throws Exception 
     */
    public void updateTrainStatus(String trainAccessAccountIDX, String trainStatus) throws Exception {
        TrainAccessAccount account = getModelById(trainAccessAccountIDX);
        if (account == null)
            return;
        updateTrainStatus(account, trainStatus);
    }
    
    /**
     * <li>说明：更新机车状态
     * <li>创建人：程锐
     * <li>创建日期：2015-3-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段台账实体对象
     * @param trainStatus 机车状态
     * @throws Exception 
     */
    public void updateTrainStatus(TrainAccessAccount account, String trainStatus) throws Exception {
        account.setTrainStatus(trainStatus);
        account.setUpdateTime(new Date());
        account.setStartTime(new Date());
        saveOrUpdate(account);
        twtTrainStatusService.updateTrainStatus(account, trainStatus);
        trainStatusHisManager.saveAndUpdateStatusHis(account.getIdx(), trainStatus);
    }
    
    /**
     * <li>说明：根据车型车号等设置机车出入段台账实体的配属局段等属性
     * <li>创建人：程锐
     * <li>创建日期：2015-1-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainNo 车号
     * @param trainType 车型实体
     * @param toGo 入段去向
     * @param entity 机车出入段台账实体
     * @return 机车出入段台账实体
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount setInEntityProperty(String trainNo, TrainType trainType, String toGo, TrainAccessAccount entity) throws Exception {
        entity.setTrainNo(trainNo);
        entity.setTrainTypeIDX(trainType == null ? "" : trainType.getTypeID());
        entity.setTrainTypeShortName(trainType == null ? "" : trainType.getShortName());
        entity.setToGo(toGo);
        entity = setDeportInfo(entity);
        return entity;
    }
    
    /**
     * <li>说明：是否运行中的机车作业计划的机车
     * <li>创建人：程锐
     * <li>创建日期：2016-4-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车入段台账实体
     * @return true 是运行中机车作业计划的机车 false 否
     */
    public boolean isRunningTrainWorkPlan(TrainAccessAccount entity) {
        if (StringUtil.isNullOrBlank(entity.getTrainTypeIDX()) || StringUtil.isNullOrBlank(entity.getTrainNo()))
            return false;
        List<TrainWorkPlan> list = trainWorkPlanQueryManager.getTrainWorkPlanListByTrain(entity.getTrainTypeIDX(), entity.getTrainNo());
        return list != null && list.size() > 0;
    }
    
    /**
     * <li>说明：验证并根据不同数据来源获取未出段的机车入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车出入段台账实体对象
     * @return 未出段的机车入段台账实体
     * @throws Exception
     */
    private TrainAccessAccount validate(TrainAccessAccount entity) throws Exception {
        if (entity == null)
            throw new BusinessException("系统出错，请联系系统管理员");
        TrainAccessAccount account = null;
        if (!StringUtil.isNullOrBlank(entity.getIdx())) {
            account = getModelById(entity.getIdx());
        } else if (!StringUtil.isNullOrBlank(entity.getTrainTypeShortName()) && !StringUtil.isNullOrBlank(entity.getTrainNo())) {
            account = trainAccessAccountQueryManager.findInAccountByTrainName(entity.getTrainTypeShortName(), entity.getTrainNo());
        } else if (!StringUtil.isNullOrBlank(entity.getTrainTypeIDX()) && !StringUtil.isNullOrBlank(entity.getTrainNo())) {
            account = trainAccessAccountQueryManager.findInAccountByTrainIDX(entity.getTrainTypeIDX(), entity.getTrainNo(), "");
        }
        if (account == null)
            throw new BusinessException("未找到相关入段机车");
        return account;
    } 
    
    /**
     * <li>说明：是否能入段
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @param idx 机车出入段台账ID
     * @return true 能入段，false 不能入段，因机车已入段
     */
    private boolean isAbleInTrain(String trainTypeIDX, String trainNo, String idx) {
        return trainAccessAccountQueryManager.findInAccountByTrainIDX(trainTypeIDX, trainNo, idx) == null;
    }
    
    /**
     * <li>说明：是否能入段
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型名称
     * @param trainNo 车号
     * @param idx 机车出入段台账ID
     * @return true 能入段，false 不能入段，因机车已入段
     */
    private boolean isAbleInTrainByName(String trainTypeShortName, String trainNo, String idx) {
        return trainAccessAccountQueryManager.findInAccountByTrainName(trainTypeShortName, trainNo, idx) == null;
    }
    
    /**
     * <li>说明：机车信息中是否存在此车型车号
     * <li>创建人：程锐
     * <li>创建日期：2014-7-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @return true 存在 false 不存在
     */
    public boolean isExistInJczlTrain(String trainTypeIDX, String trainNo) {
        List<JczlTrain> jczlTrainList = jczlTrainManager.getModelList(trainTypeIDX, trainNo, null);
        return jczlTrainList != null && jczlTrainList.size() > 0;
    }
    
    /**
     * <li>说明：保存机车信息
     * <li>创建人：程锐
     * <li>创建日期：2014-7-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车出入段台账实体对象
     * @return 机车信息实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public JczlTrain saveJczlTrain(TrainAccessAccount entity) throws BusinessException, NoSuchFieldException {
        JczlTrain train = new JczlTrain();
        train.setTrainTypeIDX(entity.getTrainTypeIDX());
        train.setTrainNo(entity.getTrainNo());
        train.setTrainTypeShortName(entity.getTrainTypeShortName());
        train.setDId(entity.getDID());
        train.setDName(entity.getDName());
        OmEmployee emp = SystemContext.getOmEmployee();
        train.setRegisterPerson(emp.getEmpid());
        train.setRegisterPersonName(emp.getEmpname());
        train.setRegisterTime(new Date());
        train.setAssetState(JczlTrain.TRAIN_ASSET_STATE_USE);
        jczlTrainManager.saveOrUpdate(train);
        return train;
    }
    
    /**
     * <li>说明：承修机车信息中是否存在此车型车号
     * <li>创建人：程锐
     * <li>创建日期：2014-7-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @return true 承修机车信息中存在此车型车号 false 不存在
     */
    private boolean isExistInUndertakeTrain(String trainTypeIDX, String trainNo) {
        List<UndertakeTrain> undertakeTrainList = undertakeTrainManager.getModelList(trainTypeIDX, trainNo);
        return undertakeTrainList != null && undertakeTrainList.size() > 0;
    }
    
    /**
     * <li>说明：保存承修车型信息
     * <li>创建人：程锐
     * <li>创建日期：2014-7-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车出入段台账实体对象
     * @return 承修车型实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private UndertakeTrainType saveUndertakeTrainType(TrainAccessAccount entity) throws BusinessException, NoSuchFieldException,
        IllegalAccessException, InvocationTargetException {
        UndertakeTrainType trainType = new UndertakeTrainType();
        BeanUtils.copyProperties(trainType, entity);
        trainType.setIdx("");
        undertakeTrainTypeManager.saveOrUpdate(trainType);
        return trainType;
    }
    
    /**
     * <li>说明：保存承修机车信息
     * <li>创建人：程锐
     * <li>创建日期：2014-7-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车出入段台账实体对象
     * @param undertakeTrainTypeIDX 承修车型主键
     * @return 承修机车实体对象
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private UndertakeTrain saveUndertakeTrain(TrainAccessAccount entity, String undertakeTrainTypeIDX) throws IllegalAccessException,
        InvocationTargetException, BusinessException, NoSuchFieldException {
        UndertakeTrain undertakeTrain = new UndertakeTrain();
        BeanUtils.copyProperties(undertakeTrain, entity);
        undertakeTrain.setIdx("");
        undertakeTrain.setUndertakeTrainTypeIDX(undertakeTrainTypeIDX);
        undertakeTrainManager.saveOrUpdate(undertakeTrain);
        return undertakeTrain;
    }
    
    /**
     * <li>说明：根据入段去向处理后续业务
     * <li>业务逻辑：入段去向为“正常整备”的则生成整备任务单（整备范围检查任务单、检测预警任务单、技术指令任务单, 普查整治， 提票...）
     * <li>创建人：程锐
     * <li>创建日期：2015-1-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车出入段实体对象
     * @throws Exception 
     */
    public void updateBizByToGO(TrainAccessAccount entity) throws Exception {
//        if (StringUtil.isNullOrBlank(entity.getToGo()))
//            return;
        
//        String zbStatus = SystemConfigUtil.getValue("ck.twt.trainStatus.".concat(TrainAccessAccount.TRAINTOGO_ZB_TYPE));
//        String jxStatus = SystemConfigUtil.getValue("ck.twt.trainStatus.".concat(TrainAccessAccount.TRAINTOGO_JX_TYPE));
//        String otherStatus = "";
//        String hql = "from EosDictEntry where status = '1' and id.dicttypeid='TWT_TRAIN_ACCESS_ACCOUNT_TOGO' and id.dictid = '" + TrainAccessAccount.TRAINTOGO_BZB + "' order by id.dictid, sortno";        
//        List<EosDictEntry> eosdyList = iEosDictEntryManager.findToList(hql);
//        if (eosdyList != null && eosdyList.size() > 0)            
//            otherStatus = eosdyList.get(0).getDictname();
        
        //判断入段去向是否在配置项中
        String togoString = SystemConfigUtil.getValue("ck.jczb.toGoForZbRdp");//配置项名称,获取到的值为基础管理-业务字典-台位图相关-字典项维护（例如0101，0301）
//      判断系统是否配置了默认入段去向
        String isDefaultToGo = SystemConfigUtil.getValue("ck.jczb.isDefaultToGo");//配置项名称,获取到的值为基础管理-业务字典-台位图相关-字典项维护（例如0101，0301）
        
        //判断入段时是否有选择入段去向
        if (StringUtils.isNotBlank(entity.getToGo())) {
        	String allStatus = SystemConfigUtil.getValue("ck.twt.trainStatus.".concat(entity.getToGo().substring(0, 2)));
            //如果当前传入的入段去向在配置文件中存在
            if (togoString.contains(entity.getToGo())) {
                //如果在，修改状态为01，同时调用saveRdp，待检，否则，不会生成整备单
                
                // JX-694   【神木】去掉生成机车检修计划活生成整备任务单时判断机车是否在做整备或检修的限制 by wujl 160912
//                if (isRunningTrainWorkPlan(entity))
//                    throw new BusinessException("该机车已经在检修中，不能做整备");
                saveRdp(entity);
                
//                if (!zbStatus.equals(entity.getTrainStatus())){
//                    updateTrainStatus(entity, zbStatus);
//                }else {
//                }
                updateTrainStatus(entity, allStatus);
            }
            
        if (entity.getToGo().startsWith(TrainAccessAccount.TRAINTOGO_ZB_TYPE)) {
            if (!StringUtil.isNullOrBlank(allStatus) && TrainAccessAccount.TRAINTOGO_JJL.equals(entity.getToGo()) && !allStatus.equals(entity.getTrainStatus())) {
                updateTrainStatus(entity, allStatus);
            }
        } else if (entity.getToGo().startsWith(TrainAccessAccount.TRAINTOGO_BZB)) 
            updateTrainStatus(entity, allStatus);
          else if (entity.getToGo().startsWith(TrainAccessAccount.TRAINTOGO_JX_TYPE))
            updateTrainStatus(entity, allStatus);
          else {
              updateTrainStatus(entity, allStatus);
        }
            
        //如果没有，那么通过系统配置走流程
        }else {
            //判断是否为空
            if (StringUtils.isNotBlank(isDefaultToGo)) {
                entity.setToGo(isDefaultToGo);
                String allStatus = SystemConfigUtil.getValue("ck.twt.trainStatus.".concat(entity.getToGo().substring(0, 2)));
                //判断默认的入段去向是否需要生成整备单,如果包含
                if (togoString.contains(isDefaultToGo)) {
                    if (isRunningTrainWorkPlan(entity))
                        throw new BusinessException("该机车已经在检修中，不能做整备");
                    saveRdp(entity);
                    
                    updateTrainStatus(entity, allStatus);
                }
            }
        }
    }
    
    /**
     * <li>说明：生成整备任务单（整备范围检查任务单、检测预警任务单、技术指令任务单, 普查整治， 提票...）-存储过程实现
     * <li>创建人：程锐
     * <li>创建日期：2015-1-19
     * <li>修改人：林欢
     * <li>修改日期：
     * <li>修改内容：当选择的去向在配置文件中的时候，只会生产整备单rdp和预警waring
     * @param entity 机车出入段台账实体
     * @throws Exception 
     */
    private void saveRdp(TrainAccessAccount entity) throws Exception {
//        if (!canExecZBProc(entity.getIdx())) 
//            return;
        
        //生成整备单
        ZbglRdp rdp = zbglRdpManager.saveByTrainAccessAccount(entity);
        
        //生成预警
        String[] param = { rdp.getIdx() };
        zbglWarningManager.saveZbglWarningByProc(param);
        //修改jt6状态
        zbglTpManager.saveZbglTpByProc(param);
        
        
//        ZbFw zbfw = zbFwManager.getZbFwByTrain(rdp.getTrainTypeIDX());
//        if (zbfw != null) {
//            rdp.setZbfwIDX(zbfw.getIdx());
//            zbglRdpManager.saveOrUpdate(rdp);
//            zbglRdpNodeManager.saveNodeAndSeq(rdp);
//        }            
//        String proc = "pkg_zb_rdp.sp_zb_rdp";
//        String[] param = { rdp.getIdx() };
//        daoUtils.executeProc(proc, param);
        
//      ===============================针对普查整治实例化对像开始===============================================
        //TODO 针对普查整治实例化对象
        //通过车型车号查询普查整治项的车型车号表中的list数据
        //封装查询参数
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("trainTypeShortName", entity.getTrainTypeShortName());//车型简称
        paramsMap.put("trainNo", entity.getTrainNo());//车号
        List<ZbglPczzItemToTraininfo> zbglPczzItemToTraininfoList = zbglPczzItemToTraininfoManager.findZbglPczzItemToTraininfoListByPczzItemID(paramsMap);
        for (ZbglPczzItemToTraininfo traininfo : zbglPczzItemToTraininfoList) {
            
            String zbglPczzIDX = traininfo.getZbglPczzIDX();//普查整治计划idx
            String wiStatus = ZbglPczzWI.STATUS_TODO + "," + ZbglPczzWI.STATUS_HANDLING + "," + ZbglPczzWI.STATUS_HANDLED;
            
            //先判断该普查整治是否在时间范围内
//          获取发布的普查整治计划对象
            ZbglPczz zbglPczz = zbglPczzManager.getReleasedZbglPczzByZbglPczzIDX(zbglPczzIDX);
            if (zbglPczz != null) {
//              通过普查整治计划查询是否已经生成对应的普查整治任务单（任务单状态 ！= CHECKED 【已检验】 未删除）
                
                //查询参数
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("zbglPczzIDX", zbglPczzIDX);
                params.put("wiStatus", wiStatus);
                params.put("trainNo", traininfo.getTrainNo());
                params.put("trainTypeShortName", traininfo.getTrainTypeShortName());
                
                ZbglPczzWI zbglPczzWI = null;
                //如果存在，那么不需要生成新的普查整治任务单对象，只需要生成新的普查整治任务项即可
                zbglPczzWI = zbglPczzWIManager.findZbglPczzWIByParams(params);
//              如果不存在，那么需要生成新的普查整治任务单对象，同时需要生成新的普查整治任务项
                if (zbglPczzWI == null) {
                    zbglPczzWI = new ZbglPczzWI();
                    zbglPczzWI.setTrainNo(entity.getTrainNo());//车号
                    zbglPczzWI.setTrainTypeShortName(entity.getTrainTypeShortName());//车型简称
                    zbglPczzWI.setTrainTypeIDX(entity.getTrainTypeIDX());//车型idx
                    zbglPczzWI.setZbglPczzIDX(zbglPczzIDX);//普查整治计划idx
                    zbglPczzWI.setWIStatus(ZbglPczzWI.STATUS_TODO);//任务单状态
                    zbglPczzWI.setRdpIdx(rdp.getIdx());//整备单idx
                    zbglPczzWI.setPczzReq(zbglPczz.getPczzReq());//任务要求
                    zbglPczzWI.setStartDate(zbglPczz.getStartDate());//开始日期
                    zbglPczzWI.setEndDate(zbglPczz.getEndDate());//结束日期
                    zbglPczzWI.setPczzName(zbglPczz.getPczzName());//普查计划名称
                    zbglPczzWIManager.saveOrUpdate(zbglPczzWI);
                }
                
                //通过普查整治计划项idx查询对象
                ZbglPczzItem zbglPczzItem = zbglPczzItemManager.getModelById(traininfo.getZbglPczzItemIDX());
                //生成普查整治任务项
                ZbglPczzWiItem ZbglPczzWiItem = new ZbglPczzWiItem();
                ZbglPczzWiItem.setZbglPczzWiIDX(zbglPczzWI.getIdx());//普查整治任务单对象
                ZbglPczzWiItem.setItemName(zbglPczzItem.getItemName());//项目名称
                ZbglPczzWiItem.setItemContent(zbglPczzItem.getItemContent());//项目内容
                ZbglPczzWiItem.setItemStatus(0);//项目完成状态 初始化0==未完毕
                zbglPczzWiItemManager.saveOrUpdate(ZbglPczzWiItem);
            }
        }
        //===============================针对普查整治实例化对像结束===============================================
    }
    
    /**
     * <li>说明：获取对应机车的配属段信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车出入段台账实体
     * @return 机车出入段台账实体
     */
    public TrainAccessAccount setDeportInfo(TrainAccessAccount entity) {
    	JczlTrainView jczlTrain = getJczlTrainByTrain(entity.getTrainTypeIDX(), entity.getTrainNo());
        if (jczlTrain == null)
            return entity;
        entity.setDID(jczlTrain.getDId());
        entity.setDName(jczlTrain.getDName());
        return entity;
    }
    
    /**
     * <li>说明：根据车型车号获取承修机车信息
     * <li> 此业务逻辑依据【前台js选车号带出配属段信息】
     * <li>创建人：程锐
     * <li>创建日期：2015-1-19
     * <li>修改人：程锐
     * <li>修改日期：2016-5-27
     * <li>修改内容：根据车型车号获取机车信息
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @return 机车信息
     */
    @SuppressWarnings("unchecked")
    private JczlTrainView getJczlTrainByTrain(String trainTypeIDX, String trainNo) {
        
        List<JczlTrainView> list = new ArrayList<JczlTrainView>();
        try {
        	if (StringUtil.isNullOrBlank(trainTypeIDX))
        		return null;
            Map queryParamsMap = new HashMap();
            queryParamsMap.put("trainTypeIDX", trainTypeIDX);
            queryParamsMap.put("trainNo", trainNo);
            queryParamsMap.put("isAll", "yes");
//            queryParamsMap.put("isCx", "yes");
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
    
    /**
     * <li>说明：根据车号识别的机车信息字符串构造机车信息实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 车号识别的机车信息字符串
     * @return 机车信息实体
     * @throws BusinessException
     */
    private TrainType buildTrainTypeByCHSB(String trainInfo) throws BusinessException{
        if (StringUtil.isNullOrBlank(trainInfo) || trainInfo.length() < 6)
            throw new BusinessException("机车信息不全");
        String trainTypeShortName = trainInfo.substring(0, trainInfo.length() - 4);
        String trainNo = trainInfo.substring(trainInfo.length() - 4);
        TrainType trainType = trainTypeManager.getTrainType(trainTypeShortName);
        String endChar = trainInfo.substring(trainInfo.length() - 1);
        try {
			Integer.parseInt(endChar);//判断最后一个字符是否是英文字符，如是英文字符则取后5位为车号，主要针对昆明小石坝有0001A这种车号的场景
		} catch (Exception e) {
			trainTypeShortName = trainInfo.substring(0, trainInfo.length() - 5);
	        trainNo = trainInfo.substring(trainInfo.length() - 5);
	        trainType = trainTypeManager.getTrainType(trainTypeShortName);
		}
		//考虑到有可能只传车号的可能性
		if (trainType == null) {
			trainType = new TrainType();
		}
        trainType.setTrainNo(trainNo);
        return trainType;
    }
    
    /**
     * <li>说明：能否执行整备兑现存储过程
     *          1 检修系统需检查有无【处理中】和【初始化】的机车作业计划，有则不执行整备兑现
     *          2 只有整备系统情况下可执行整备兑现
     * <li>创建人：程锐
     * <li>创建日期：2015-8-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账主键
     * @return true 能 false 否
     */
    private boolean canExecZBProc (String accountIDX) {
        if (StringUtil.isNullOrBlank(JXConfig.getInstance().getSystemType())) {
            TrainAccessAccount account = getModelById(accountIDX);
            String hql = "from TrainWorkPlan where recordStatus = 0 and trainTypeShortName = ? and trainNo = ? and workPlanStatus in (?, ?)";
            List list = daoUtils.find(hql, new Object[]{account.getTrainTypeShortName(), account.getTrainNo(), TrainWorkPlan.STATUS_NEW, TrainWorkPlan.STATUS_HANDLING});
            if (list == null || list.size() < 1)
                return true;
        }
        else if ("zb".equals(JXConfig.getInstance().getSystemType()))
            return true;
        return false;
    }
    
    /**
     * <li>说明：根据车型名称获取机车别名
     * <li>创建人：程锐
     * <li>创建日期：2015-12-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型名称
     * @return 机车别名
     */
    private String buildAliasName(String trainTypeShortName) {
        String trainAliasName = "";
        if (!StringUtil.isNullOrBlank(trainTypeShortName)) {
            trainAliasName = TrainTypeMappingUtil.getTrainShortName(trainTypeShortName);
        }
        return trainAliasName;
    }
    
    /**
     * <li>说明：台位图通过台位图服务子系统获取当前站点的所有在段机车列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param siteID 站点标识
     * @return List<TrainAccessAccountBean> 所有在段机车列表
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public List<TrainAccessAccountBean> getInAccountTrainList(String siteID) throws IllegalAccessException, InvocationTargetException {
        List<TrainAccessAccountBean> beanList = new ArrayList<TrainAccessAccountBean>();
        
        List<TrainAccessAccount> list = trainAccessAccountQueryManager.getInTrainListBySiteID(siteID);
        if (list != null && list.size() > 0) {
            for (TrainAccessAccount account : list) {
                TrainAccessAccountBean bean = new TrainAccessAccountBean();
                BeanUtils.copyProperties(bean, account);
                bean.setInTime(account.getInTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getInTime()) : "");
                bean.setOnEquipTime(account.getOnEquipTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getOnEquipTime()) : "");
                beanList.add(bean);
            }
        }
        
        return beanList;
    }

    /**
     * <li>说明：获取台位图-确定入段去向接口-机车出入段台账信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 车辆对象
     * @return JSONObject 机车出入段台账信息
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public JSONObject getTrainInfoForToGo(String trainInfo) throws JsonParseException, JsonMappingException, IOException {
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
        return jsonObj;
    }

    /**
     * <li>说明：获取承修车型列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param List<TrainType> 承修车型列表
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @SuppressWarnings("unchecked")
    public List<TrainType> getUndertakeTrainType() throws JsonParseException, JsonMappingException, ClassNotFoundException, IOException {
        OmOrganization org = OmOrganizationSelectManager.getOrgByOrgcode(JXSystemProperties.OVERSEA_ORGCODE);
        Map map = trainTypeManager.page("", JSONUtil.read("{}", Map.class), 0, 100, "", "yes", org.getOrgseq());
        List<TrainType> list = (List<TrainType>) map.get("root");
        return list;
    }

    /**
     * <li>说明：获取机车入段去向数据字典列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return List<EosDictEntryBean> 机车入段去向数据字典列表
     */
    public List<EosDictEntryBean> getTrainToGo() {
        String hql = "from EosDictEntry where status = '1' and id.dicttypeid='TWT_TRAIN_ACCESS_ACCOUNT_TOGO' and parentid is not null order by id.dictid, sortno";
        List<EosDictEntry> eosdyList = iEosDictEntryManager.findToList(hql);
        List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
        for (EosDictEntry eos : eosdyList) {
            EosDictEntryBean bean = new EosDictEntryBean();
            bean.setDictid(eos.getId().getDictid());
            bean.setDictname(eos.getDictname());
            beanlist.add(bean);
        }
        return beanlist;
    }

    /**
     * <li>说明：根据车型获取车号列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型简称
     * @return 车号列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public Map getTrainNoByTrainType(String trainTypeShortName) {
        Map queryParamsMap = new HashMap();
        queryParamsMap.put("trainTypeShortName", trainTypeShortName);
        queryParamsMap.put("isAll", "yes");
        queryParamsMap.put("isCx", "no");
        queryParamsMap.put("isRemoveRun", "true");
        Map map = trainNoManager.page(queryParamsMap, 0, 10000);
        return map;
    }
    
    /**
     * <li>说明：根据机车信息查询出入段台账列表（郑州上沙接口在使用）
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型简称
     * @return 车号列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public List<TrainAccessAccount> getTrainAccessAccountListByEntity(TrainAccessAccount entity) {
    	StringBuffer sb = new StringBuffer();
    	sb.append(" from TrainAccessAccount where trainNo = ? and trainTypeShortName = ? and recordStatus = 0 order by updateTime desc ");
    	return this.daoUtils.find(sb.toString(),new Object[]{entity.getTrainNo(),entity.getTrainTypeShortName()});
    }

    /**
     * <li>说明：判断该入段去向是否需要生成整备范围活
     * <li>创建人：林欢
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param toGo 入段去向
     * @param map 返回map success：true表示需要做整备  success：false表示不需要做整备
     */
    public Map<String, Object> existNeedToDoZbRdp(String toGo, Map<String, Object> map) {
        //获取配置项中，需要做整备的入段去向
        //判断入段去向是否在配置项中
        String togoString = SystemConfigUtil.getValue("ck.jczb.toGoForZbRdp");//配置项名称,获取到的值为基础管理-业务字典-台位图相关-字典项维护（例如0101，0301）
        //如果包含返回true
        if (togoString.contains(toGo)) {
            map.put("success", true);
        }else {
            map.put("success", false);
        }
        return map;
    }

    /**
     * <li>说明：通过右键台位图，判断当前车是否属于检修 整备 通用
     * <li>创建人：林欢
     * <li>创建日期：2016-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 封装的数据{"trainTypeShortName":"sb001"}
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws Exception
     */
	public Map<String, Object> getTrainStateByMessage(String trainTypeShortName) throws JsonParseException, JsonMappingException, IOException {
		return twtLoginManager.getTrainStateByMessage(trainTypeShortName);
	}
    
}
