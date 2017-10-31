package com.yunda.zb.tp.manager;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault;
import com.yunda.jx.jxgc.tpmanage.entity.RepairEmpBean;
import com.yunda.jx.pjwz.partsBase.professionaltype.entity.ProfessionalType;
import com.yunda.jxpz.utils.CodeRuleUtil;
import com.yunda.jxpz.workplace.entity.WorkPlaceToOrg;
import com.yunda.jxpz.workplace.manager.WorkPlaceToOrgManager;
import com.yunda.webservice.common.util.DefaultUserUtilManager;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpTempRepair;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.entity.ZbglTpRepair;
import com.yunda.zb.trainwarning.manager.ZbglWarningManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTp业务类,JT6提票
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglTpManager")
public class ZbglTpManager extends JXBaseManager<ZbglTp, ZbglTp> {
    
    /** 机车整备单业务类 */
    @Resource
    private ZbglRdpManager zbglRdpManager;
    
    /** 站点标示关联组织机构业务类 */
    @Resource
    private WorkPlaceToOrgManager workPlaceToOrgManager;
    
    /** 人员业务类 */
    @Resource
    private IOmEmployeeManager omEmployeeManager;
    
    /** 操作者业务类 */
    @Resource
    private AcOperatorManager acOperatorManager;
    
    /** 组织机构业务类 */
    @Resource
    private IOmOrganizationManager omOrganizationManager;
    
    /** 机车检测预警业务类 */
    @Resource
    private ZbglWarningManager zbglWarningManager;
    
    /** 附件管理业务类 */
    @Resource
    private AttachmentManager attachmentManager; 

    /** 人员选择业务类 */
    @Resource
    private OmEmployeeSelectManager omEmployeeSelectManager;
    
    /** 提票返修业务类 */
    @Resource
    private ZbglTpRepairManager zbglTpRepairManager ;
    /** 遗留活业务类 */
    @Resource
    private ZbglTpExceptionManager zbglTpExceptionManager;
    private static final String TRAINTYPEIDX = "trainTypeIDX";
    
    private static final String TRAINNO = "trainNo";
    
    /**
     * <li>说明：根据提票记录idx获取录音文件的idx
     * <li>创建人：刘晓斌
     * <li>创建日期：2016-3-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param tpIdxs 提票记录idx列表
     * @return 录音文件的idx
     */
    public Map<String,String> findAudioIdx(Collection<String> tpIdxs){
        if(null == tpIdxs || tpIdxs.size() < 0) return null;
        Map<String,String> audioIdxMap = new HashMap<String,String>();
        for (String tpIdx : tpIdxs) {
            List<Attachment> atts = attachmentManager.findListByKey(tpIdx, ZbConstants.UPLOADPATH_TP_AUDIO);
            if(null == atts || atts.size() < 1) continue;
            audioIdxMap.put(tpIdx, atts.get(0).getIdx());
        }
        return audioIdxMap;
    }
    
    /**
     * <li>说明：检查有无同车同位置同故障现象的未处理的碎修票
     * <li>创建人：程锐
     * <li>创建日期：2015-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 提票实体对象
     * @return true 有 false 无
     */
	public boolean checkData(ZbglTp entity) {
		List<ZbglTp> tpList = getModelList(entity.getTrainTypeIDX(), 
										   entity.getTrainNo(),
										   entity.getFaultID(), 
										   entity.getFaultFixFullName(),
										   entity.getFaultDesc());		
		return tpList != null && tpList.size() > 0; 
	}
    
	/**
     * <li>说明：保存并实例化提票
     * <li>创建人：程锐
     * <li>创建日期：2015-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entities 提票实体列表
     * @param entity 提票实体对象
     * @throws Exception
     */
    public void saveTpAndInst(ZbglTp[] entities, ZbglTp entity) throws Exception {
        for (ZbglTp tp : entities) {
            tp = buildEntity(tp);
            saveOrUpdate(tp);
            zbglWarningManager.updateForTp(tp);
        }
        daoUtils.flush();
        ZbglRdp rdp = zbglRdpManager.getRunningRdpByTrain(entity.getTrainTypeShortName(), entity.getTrainNo());
        if (rdp != null) {
            saveForInstanceTp(rdp);
            daoUtils.flush();
            // 如果整备单状态为“完成”，则提票的的数据修改为“检验后提票” by wujl JX-677
            if(ZbglRdp.STATUS_HANDLED.equals(rdp.getRdpStatus())){
                for (ZbglTp tp : entities) {
                    tp.setJyStatus(ZbglTp.IS_JY_STATUS_YES);
                    this.saveOrUpdate(tp);
                }
            }
        }
    }
    
	/**
     * <li>说明：实例化提票
     * <li>创建人：程锐
     * <li>创建日期：2015-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdp 整备单对象
     */
    public void saveForInstanceTp(ZbglRdp rdp) {
		String sql = SqlMapUtil.getSql("zb-tp:saveForInstanceTp")
                               .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
							   .replace("#STATUS_DRAFT#", ZbglTp.STATUS_DRAFT)
							   .replace("#rdpIDX#", rdp.getIdx())
							   .replace("#REPAIRCLASS#", rdp.getRepairClass()) 							   
							   .replace("#STATUS_INIT#", ZbglTp.STATUS_INIT)
							   .replace("#NO_DELETE#", Constants.NO_DELETE + "")
							   .replace("#trainTypeShortName#", rdp.getTrainTypeShortName())
							   .replace("#trainNo#", rdp.getTrainNo());
		daoUtils.executeSql(sql);
	}
    
	/**
     * <li>说明：查询处理中的提票活分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param repairClass 票活类型
     * @param faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 处理中的提票活分页列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public Page<ZbglTp> queryTpList(String searchJson,
							        String repairClass, 
							        String faultNoticeStatus, 
							        Long operatorid,
							        int start, 
							        int limit) throws Exception {		
		Map<String, String> queryMap = new HashMap<String, String>();        
		if (!StringUtil.isNullOrBlank(searchJson)) {
			queryMap = JSONUtil.read(searchJson, Map.class); 
		}
        return queryTpList(queryMap, repairClass, faultNoticeStatus, operatorid, start, limit);
	}
    
    /**
     * <li>说明：查询处理中的提票活分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param queryMap 查询条件Map
     * @param repairClass 票活类型
     * @param faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 处理中的提票活分页列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Page<ZbglTp> queryTpList(Map<String, String> queryMap,
                                    String repairClass, 
                                    String faultNoticeStatus, 
                                    Long operatorid,
                                    int start, 
                                    int limit) throws Exception {
        StringBuilder selectSb = new StringBuilder();
        selectSb.append("select new ZbglTp(idx, faultFixFullName, faultName, ")
                .append("faultDesc, faultNoticeCode, noticePersonName, noticeTime, trainTypeShortName||' '||trainNo, professionalTypeIdx, professionalTypeName, professionalTypeSeq,discoverID,discover,faultID,faultFixFullCode,repairTimes,");
        
        selectSb.append("methodDesc, repairResult, repairEmp, repairEmpID, faultReason, repairDesc");
        selectSb.append(")");
        
        StringBuilder fromSb = getQueryTpFromHql(queryMap, repairClass, faultNoticeStatus, operatorid);
        String hql = selectSb.append(fromSb).toString();
        String totalHql = "select count(idx) ".concat(fromSb.toString());
        Page<ZbglTp> page = findPageList(totalHql, hql, start, limit);
        
        List<ZbglTp> tpList = page.getList();
        if(null != tpList && tpList.size() > 0){
            List<String> tpIdxs = new ArrayList<String>();
            for (ZbglTp tp : tpList) {
                tpIdxs.add(tp.getIdx());
                Map paramMap = new HashMap<String, String>();
                paramMap.put("tpIDX", tp.getIdx());
                tp.setIsTpException(zbglTpExceptionManager.isTpExceptionByTpIDX(paramMap));
            }
            Map<String,String> audioIdxMap = findAudioIdx(tpIdxs);
            if(null != audioIdxMap && audioIdxMap.size() > 0){
                for (ZbglTp tp : tpList) {
                    tp.setAudioAttIdx(audioIdxMap.get(tp.getIdx()));
                  
                }
                page.setList(tpList);
            }
        }
        return page;
    }

    /**
     * <li>说明：领取提票活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param repairClass 票活类型
     * @throws Exception
     */
    public void receiveTp(Long operatorid, String idxs, String repairClass) throws Exception {
		String idxsStr = CommonUtil.buildInSqlStr(idxs);
        if (StringUtil.isNullOrBlank(idxsStr))
            throw new BusinessException("以,号分隔的idx字符串为空");
		OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
		OmOrganization org = omOrganizationManager.findByOperator(operatorid);
        String sql = "";
        if (ZbConstants.REPAIRCLASS_SX.equals(repairClass))
            sql = SqlMapUtil.getSql("zb-tp:receiveSXTp")
                            .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace("#revTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace("#revPersonId#", emp != null ? emp.getEmpid().toString() : null)
                            .replace("#revPersonName#", emp != null ? emp.getEmpname() : "")
                            .replace("#revOrgID#", org != null ? org.getOrgid().toString() : null)
                            .replace("#revOrgName#", org != null ? org.getOrgname() : "")
                            .replace("#revOrgSeq#", org != null && !StringUtil.isNullOrBlank(org.getOrgseq())? org.getOrgseq() : "")
                            .replace("#STATUS_OPEN#", ZbglTp.STATUS_OPEN)
                            .replace(ZbConstants.IDXS, idxsStr);   
        else if (ZbConstants.REPAIRCLASS_LX.equals(repairClass))
            sql = SqlMapUtil.getSql("zb-tp:receiveLXTp")
                            .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace("#revTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace("#revPersonId#", emp != null ? emp.getEmpid().toString() : null)
                            .replace("#revPersonName#", emp != null ? emp.getEmpname() : "")
                            .replace("#STATUS_OPEN#", ZbglTp.STATUS_OPEN)
                            .replace(ZbConstants.IDXS, idxsStr);   
		daoUtils.executeSql(sql);		
	}
	
	/**
     * <li>说明：销活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：2016-8-30
     * <li>修改内容： 工位终端其它处理人员可选可输
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param tpData 提票处理信息实体
     * @throws Exception
     */
    public void handleTp(Long operatorid, String idxs, ZbglTp tpData) throws Exception {
		String idxsStr = CommonUtil.buildInSqlStr(idxs);
        if (StringUtil.isNullOrBlank(idxsStr))
            throw new BusinessException("以,号分隔的idx字符串为空");
		if (tpData == null) throw new BusinessException("提票活销票时未记录处理结果等记录");
		OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
		SystemContext.setAcOperator(acOperatorManager.findLoginAcOprator(operatorid));
		String handleSiteID = EntityUtil.findSysSiteId("");
		String handleSiteName = EntityUtil.findSysSiteName(handleSiteID, "");
        
        String otherEmp = tpData.getRepairEmpID();  // 其它处理人id
        String otherEmpName = tpData.getRepairEmp(); // 其它处理人名称
    
        StringBuilder workerIDS = new StringBuilder().append(emp.getEmpid());      
        StringBuilder workerNames = new StringBuilder().append(emp.getEmpname());
        if (!StringUtil.isNullOrBlank(otherEmp)) {
            if(otherEmp.contains(workerIDS.toString())){  //  判断其它处理人是否包含了当前登录人
                workerIDS =  new StringBuilder().append(otherEmp);
            }else{
                workerIDS.append(Constants.JOINSTR).append(otherEmp);  
            }
           
//            String[] otherWorkerIDArray = StringUtil.tokenizer(otherEmp, Constants.JOINSTR);
//            for (String otherWorkerID : otherWorkerIDArray) {
//                OmEmployee otherEmpEntity = omEmployeeManager.getModelById(Long.valueOf(otherWorkerID));
//                if (otherEmpEntity != null)
//                    workerNames.append(otherEmpEntity.getEmpname()).append(Constants.JOINSTR);
//            }
        }
        if (!StringUtil.isNullOrBlank(otherEmpName)) {
            if(otherEmpName.contains(workerNames.toString())){
                workerNames = new StringBuilder().append(otherEmpName);
            }else{
                workerNames.append(Constants.JOINSTR).append(otherEmpName);
            }
           
        }
        if (workerIDS.toString().endsWith(Constants.JOINSTR))
            workerIDS.deleteCharAt(workerIDS.length() - 1);
        if (workerNames.toString().endsWith(Constants.JOINSTR))
            workerNames.deleteCharAt(workerNames.length() - 1);
        
//        StringBuilder completeEmp = new StringBuilder().append(emp.getEmpname());
//        if (!StringUtil.isNullOrBlank(otherEmp)) 
//            completeEmp.append(Constants.JOINSTR).append(otherEmp);
//        
		String sql = SqlMapUtil.getSql("zb-tp:handleTp")
                               .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#handleTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
							   .replace("#methodDesc#", StringUtil.nvlTrim(tpData.getMethodDesc(),""))
							   .replace("#repairResult#", StringUtil.nvlTrim(tpData.getRepairResult(),""))
							   .replace("#repairDesc#", StringUtil.nvlTrim(tpData.getRepairDesc(),""))
							   .replace("#handlePersonId#", emp != null ? emp.getEmpid().toString() : null)
							   .replace("#handlePersonName#", emp != null ? emp.getEmpname() : "")
							   .replace("#handleSiteID#", handleSiteID)
							   .replace("#handleSiteName#", handleSiteName)
							   .replace("#professionalTypeIdx#", StringUtil.nvlTrim(tpData.getProfessionalTypeIdx(),""))
							   .replace("#professionalTypeName#", StringUtil.nvlTrim(tpData.getProfessionalTypeName(),""))
							   .replace("#professionalTypeSeq#", StringUtil.nvlTrim(tpData.getProfessionalTypeSeq(),""))
							   .replace("#faultFixFullName#", StringUtil.nvlTrim(tpData.getFaultFixFullName(),""))
							   .replace("#faultReason#", StringUtil.nvlTrim(tpData.getFaultReason(),""))
							   .replace("#repairEmpID#", workerIDS.toString())
							   .replace("#repairEmp#", workerNames.toString())
                               .replace("#STATUS_OVER#", ZbglTp.STATUS_OVER)
							   .replace(ZbConstants.IDXS, idxsStr);
		daoUtils.executeSql(sql);
        
        // 回写提票返修单数据
        if(StringUtil.isNullOrBlank(tpData.getIdx())){
            List<ZbglTpRepair> repairLists = zbglTpRepairManager.getRepairByJt6IDX(tpData.getIdx());
            if(repairLists != null && repairLists.get(0) != null){
                ZbglTpRepair repair = repairLists.get(0);
                repair.setFaultFixFullName(StringUtil.nvlTrim(tpData.getFaultFixFullName(),""));
                repair.setFaultReason(StringUtil.nvlTrim(tpData.getFaultReason(),""));
                repair.setMethodDesc(StringUtil.nvlTrim(tpData.getMethodDesc(),""));
                repair.setProfessionalTypeName(StringUtil.nvlTrim(tpData.getProfessionalTypeName(),""));
                repair.setRepairDesc(StringUtil.nvlTrim(tpData.getRepairDesc(),""));
                repair.setRepairPersonIDX(emp != null ? emp.getEmpid().toString() : null);
                repair.setRepairPersonName(emp != null ? emp.getEmpname().toString() : null);
                zbglTpRepairManager.saveOrUpdate(repair);
            }
        }
	}
    
    /**
     * <li>说明：撤销领活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param repairClass 票活类型
     */
    public void cancelReceivedTp(Long operatorid, String idxs, String repairClass) throws Exception {
		String idxsStr = CommonUtil.buildInSqlStr(idxs);
        if (StringUtil.isNullOrBlank(idxsStr))
            throw new BusinessException("以,号分隔的idx字符串为空");
        String sql = "";
        if (ZbConstants.REPAIRCLASS_SX.equals(repairClass))
            sql = SqlMapUtil.getSql("zb-tp:cancelReceivedSXTp")
                            .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
							.replace("#STATUS_DRAFT#", ZbglTp.STATUS_DRAFT)
							.replace(ZbConstants.IDXS, idxsStr);
        else if (ZbConstants.REPAIRCLASS_LX.equals(repairClass))
            sql = SqlMapUtil.getSql("zb-tp:cancelReceivedLXTp")
                            .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace("#STATUS_DRAFT#", ZbglTp.STATUS_DRAFT)
                            .replace(ZbConstants.IDXS, idxsStr);
		daoUtils.executeSql(sql);		
	}
    
	/**
     * <li>说明：提票转临修
     * <li>创建人：程锐
     * <li>创建日期：2015-1-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tpIDXAry 提票实体对象IDX数组
     * @param zlxData 转临修实体对象
     * @throws Exception
     */
    public void updateForZLX(String[] tpIDXAry, ZbglRdpTempRepair zlxData) throws Exception {
		if (tpIDXAry == null || tpIDXAry.length < 1)
            return;
        for (String idx : tpIDXAry) {
            if (StringUtil.isNullOrBlank(idx))
                throw new BusinessException("提票实体IDX为空");
            ZbglTp oldTp = getModelById(idx);
            if (oldTp == null)
                throw new BusinessException("idx为" + idx + "的转临修单提票实体为空.");
            oldTp.setRepairClass(ZbConstants.REPAIRCLASS_LX);
            if (zlxData != null) {
                oldTp.setRevOrgID(zlxData.getHandleOrgID() != null ? zlxData.getHandleOrgID() : null);
                oldTp.setRevOrgName(!StringUtil.isNullOrBlank(zlxData.getHandleOrgName()) ? zlxData.getHandleOrgName() : "");
                oldTp.setRevOrgSeq(!StringUtil.isNullOrBlank(zlxData.getHandleOrgSeq()) ? zlxData.getHandleOrgSeq() : "");
            }
            if (ZbglTp.STATUS_OVER.equals(oldTp.getFaultNoticeStatus()) && ZbglTp.REPAIRRESULT_ZLX == oldTp.getRepairResult()) {
                oldTp = buildZLXAndCompleteEntity(oldTp);               
            }
            saveOrUpdate(oldTp);
        }
	}
    
	/**
     * <li>说明：临修票调度派工
     * <li>创建人：程锐
     * <li>创建日期：2015-1-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param data 提票实体对象
     * @param tpIDXAry 提票IDX数组
     * @throws Exception
     */
    public void updateForLxDdpg(ZbglTp data, String[] tpIDXAry) throws Exception {
        if (tpIDXAry == null || tpIDXAry.length < 1)
            throw new BusinessException("提票实体IDX为空");
		String idxs = StringUtil.join(tpIDXAry);
		String idxsStr = CommonUtil.buildInSqlStr(idxs);
        if (StringUtil.isNullOrBlank(idxsStr))
            throw new BusinessException("以,号分隔的提票idx字符串为空");
		String sql = SqlMapUtil.getSql("zb-tp:updateForLxDdpg")
                               .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
		   					   .replace("#revOrgID#", data.getRevOrgID().toString())
							   .replace("#revOrgName#", data.getRevOrgName())
							   .replace("#revOrgSeq#", data.getRevOrgSeq())
		   					   .replace(ZbConstants.IDXS, idxsStr);
		daoUtils.executeSql(sql);
	}
    
    /**
     * <li>说明：获取同一机车整备单的已处理的提票数量
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的已处理的提票数量
     */
    public int getHandledTpCountByRdp(String rdpIDX) {
        List<ZbglTp> list = getHandledTpListByRdp(rdpIDX);
        return CommonUtil.getListSize(list);
    }
    
    /**
     * <li>说明：获取同一机车整备单的已处理的提票列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的已处理的提票列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTp> getHandledTpListByRdp(String rdpIDX) {
        if (StringUtil.isNullOrBlank(rdpIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIDX", rdpIDX);
        paramMap.put("faultNoticeStatus", "in '" + ZbglTp.STATUS_OVER + "','" + ZbglTp.STATUS_CHECK + "'");
        return getTpList(paramMap);
    }
    
    /**
     * <li>说明：获取同一机车整备单的提票数量
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @param repairClass 检修类型
     * @return 同一机车整备单的提票数量
     */
    public int getAllTpCountByRdp(String rdpIDX, String repairClass) {
        List<ZbglTp> list = getAllTpListByRdp(rdpIDX, repairClass);
        return CommonUtil.getListSize(list);
    }
    
    /**
     * <li>说明：获取同一机车整备单的提票列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @param repairClass 检修类型
     * @return 同一机车整备单的提票列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTp> getAllTpListByRdp(String rdpIDX, String repairClass) {
        if (StringUtil.isNullOrBlank(rdpIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIDX", rdpIDX);
        if (!StringUtil.isNullOrBlank(repairClass))
            paramMap.put("repairClass", repairClass);
        return getTpList(paramMap);
    }
    
    /**
     * <li>说明：逻辑删除提票及关联的附件
     * <li>创建人：程锐
     * <li>创建日期：2015-3-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 提票单id数组 
     * @return 错误信息，如果为null表示操作成功
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public List<String> logicDeleteTP(String... ids) throws BusinessException, NoSuchFieldException {
        List<ZbglTp> entityList = new ArrayList<ZbglTp>();
        List<String> errors = new ArrayList<String>();
        for (String id : ids) {            
            String err = delAtt(id);
            if (!StringUtil.isNullOrBlank(err)) {
                errors.add(err);
                continue;
            }
            ZbglTp tp = getModelById(id);
            tp = EntityUtil.setSysinfo(tp);
            tp = EntityUtil.setDeleted(tp);
            entityList.add(tp);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
        return errors;
    }
    
    /**
     * 
     * <li>说明：获取提票活数量
     * <li>创建人：程锐
     * <li>创建日期：2015-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param repairClass 票活类型
     * @param faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
     * @param operatorid 操作者ID
     * @return 提票活数量
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public int getTpCount(String searchJson,
                          String repairClass, 
                          String faultNoticeStatus, 
                          Long operatorid) throws Exception {
        Map<String, String> queryMap = new HashMap<String, String>();        
        if (!StringUtil.isNullOrBlank(searchJson)) {
            queryMap = JSONUtil.read(searchJson, Map.class); 
        }
        StringBuilder fromSb = getQueryTpFromHql(queryMap, repairClass, faultNoticeStatus, operatorid);
        String totalHql = "select count(idx) ".concat(fromSb.toString());
        List list = daoUtils.find(totalHql);
        return Integer.parseInt(list.get(0).toString());
    }
    
    /**
     * <li>说明：获取同车同位置同故障现象的未处理的碎修票列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @param faultID 故障现象ID
     * @param faultFixFullName 故障位置全名
     * @param faultDesc 故障描述
     * @return 同车同位置同故障现象的未处理的碎修票列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    private List<ZbglTp> getModelList(String trainTypeIDX, 
                                      String trainNo, 
                                      String faultID,
                                      String faultFixFullName, 
                                      String faultDesc) throws BusinessException {
        Map paramMap = new HashMap<String, String>();        
        if (!StringUtil.isNullOrBlank(trainTypeIDX)) {
            paramMap.put(TRAINTYPEIDX, trainTypeIDX);
        }
        if (!StringUtil.isNullOrBlank(trainNo)) {
            paramMap.put(TRAINNO, trainNo);
        }        
        if (!StringUtil.isNullOrBlank(faultID)) {
            if (PlaceFault.OTHERID.equals(faultID)) {                
                paramMap.put("faultDesc", faultDesc);
            } else {
                paramMap.put("faultID", faultID);
            }
        }
        if (!StringUtil.isNullOrBlank(faultFixFullName)) {
            paramMap.put("faultFixFullName", faultFixFullName);
        }
        paramMap.put("faultNoticeStatus", ZbglTp.STATUS_DRAFT);
        paramMap.put("repairClass", ZbConstants.REPAIRCLASS_SX);
        return getTpList(paramMap);
    }
    
    /**
     * <li>说明：构建提票实体对象的默认通用属性值
     * <li>创建人：程锐
     * <li>创建日期：2015-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 提票实体对象
     * @return 提票实体对象
     */
    private ZbglTp buildEntity(ZbglTp entity) {
        entity.setFaultNoticeStatus(ZbglTp.STATUS_INIT);
        entity.setRepairClass(ZbConstants.REPAIRCLASS_SX);
        entity.setNoticeSource(ZbglTp.NOTICESOURCE_ZB);
        entity.setFaultNoticeCode(CodeRuleUtil.getRuleCode("JCZL_FAULT_NOTICE_FAULT_NOTICE_CODE"));// FIXME 使用检修系统的提票编码
        DefaultUserUtilManager.setDefaultOperator();
        OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
        entity.setNoticePersonId(emp != null ? emp.getEmpid() : null);
        entity.setNoticePersonName(emp != null ? emp.getEmpname() : "");
        entity.setNoticeTime(new Date());
        entity.setFaultOccurDate(entity.getFaultOccurDate() != null ? entity.getFaultOccurDate() : new Date());
        entity.setProfessionalTypeSeq(getProSeqByProIDX(entity.getProfessionalTypeIdx()));
        return entity;
    }
    
    /**
     * <li>说明：删除提票单关联的附件
     * <li>创建人：程锐
     * <li>创建日期：2015-3-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param tpIDX 提票IDX
     * @return 错误信息，如果为null表示操作成功
     */ 
    private String delAtt(String tpIDX) {
        List<Attachment> attList = attachmentManager.findListByKey(tpIDX, ZbConstants.UPLOADPATH_TP);
        List<Attachment> attImgList = attachmentManager.findListByKey(tpIDX, ZbConstants.UPLOADPATH_TP_IMG);
        List<Attachment> attAudioList = attachmentManager.findListByKey(tpIDX, ZbConstants.UPLOADPATH_TP_AUDIO);
        List<Attachment> list = new ArrayList<Attachment>();
        if(null != attList && attList.size() > 0)   list.addAll(attList);
        if(null != attImgList && attImgList.size() > 0) list.addAll(attImgList);
        if(null != attAudioList && attAudioList.size() > 0) list.addAll(attAudioList);
        StringBuilder sb = new StringBuilder();
        for (Attachment attachment : list) {
            String err = attachmentManager.delete(attachment.getIdx());
            if(!StringUtil.isNullOrBlank(err)) 
                 sb.append(err).equals(Constants.JOINSTR);            
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    /**
     * <li>说明：获取提票列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 提票列表
     */
    @SuppressWarnings("unchecked")
    private List<ZbglTp> getTpList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglTp where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：转临修时构造处理结果为转临修的已处理的碎修票实体
     * <li>创建人：程锐
     * <li>创建日期：2015-3-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 处理结果为转临修的已处理的碎修票实体
     * @return 搭载转临修后的提票实体
     */
    private ZbglTp buildZLXAndCompleteEntity(ZbglTp entity) {
        entity.setHandleTime(null);
        entity.setMethodDesc("");
        entity.setRevTime(null);
        entity.setFaultNoticeStatus(ZbglTp.STATUS_DRAFT);
        entity.setRevPersonId(null);
        entity.setRevPersonName("");
        entity.setRepairResult(null);
        entity.setRepairDesc("");
        entity.setHandlePersonId(null);
        entity.setHandlePersonName("");
        entity.setHandleSiteID("");
        entity.setHandleSiteName("");
        return entity;
    }
    
    /**
     * <li>说明：根据专业类型Idx获取专业类型序列
     * <li>创建人：程锐
     * <li>创建日期：2015-3-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param professionalTypeIdx 专业类型Idx
     * @return 专业类型序列
     */
    @SuppressWarnings("unchecked")
    private String getProSeqByProIDX(String professionalTypeIdx) {
        if (StringUtil.isNullOrBlank(professionalTypeIdx))
            return "";
        ProfessionalType p = new ProfessionalType();
        p.setProfessionalTypeID(professionalTypeIdx);
        p.setRecordStatus(Constants.NO_DELETE);
        p.setStatus(ProfessionalType.status_start);
        List<ProfessionalType> list = daoUtils.getHibernateTemplate().findByExample(p);
        if (list == null || list.size() < 1)
            return "";
        return list.get(0).getProSeq();
    }
    
    /**
     * <li>说明：获取查询提票活hql的from部分
     * <li>创建人：程锐
     * <li>创建日期：2015-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param queryMap 查询条件Map
     * @param repairClass 票活类型
     * @param faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
     * @param operatorid 操作者ID
     * @return 查询提票活hql的from部分
     */
    private StringBuilder getQueryTpFromHql(Map<String, String> queryMap,
                                            String repairClass, 
                                            String faultNoticeStatus, 
                                            Long operatorid) {
        StringBuilder fromSb = new StringBuilder();
        fromSb.append(" from ZbglTp where recordStatus = 0")
              .append(" and repairClass = '")
              .append(repairClass)
              .append("'")
//              .append(" and rdpIDX in (select idx from ZbglRdp where rdpStatus = '")
              .append(" and rdpIDX in (select idx from ZbglRdp where ")
//              .append(ZbglRdp.STATUS_HANDLING)
              .append(" recordStatus = 0 and siteID = '")
              .append(EntityUtil.findSysSiteId(""))
              .append("')");    
        if (ZbConstants.REPAIRCLASS_LX.equals(repairClass) && operatorid != null) {           
            OmOrganization org = omOrganizationManager.findByOperator(operatorid);
            if (org != null)
                fromSb.append(" and revOrgID is not null and revOrgID = ")
                      .append(org.getOrgid());
        }
        if (ZbglTp.STATUS_DRAFT.equals(faultNoticeStatus)) {
            fromSb.append(" and faultNoticeStatus = '")
                  .append(ZbglTp.STATUS_DRAFT)
                  .append("'")
                  .append(" and revPersonId is null");
        } else if (ZbglTp.STATUS_OPEN.equals(faultNoticeStatus)) {
            fromSb.append(" and faultNoticeStatus = '")
                  .append(ZbglTp.STATUS_OPEN)
                  .append("'");
            if (operatorid != null) {
                OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
                if (emp != null)
                    fromSb.append(" and revPersonId = ")
                          .append(emp.getEmpid())
                          .append(" and handlePersonId is null");
            }
        }
        fromSb.append(CommonUtil.buildParamsHql(queryMap));
        fromSb.append(" order by updateTime desc ");
        return fromSb;
    }

    /**
     * <li>说明：临碎修票活分页查询（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-9
     * <li>修改人：何涛
     * <li>修改日期：2016-05-12
     * <li>修改内容：增加查询条件的空值验证
     * @param searchEntity 查询条件对象实体
     * @param search 查询条件
     * @return Page
     * @throws Exception 
     */
    public Page<ZbglTp> findPageQuery(SearchEntity<ZbglTp> searchEntity, String search) throws Exception {
        // 查询实体
        ZbglTp entity = searchEntity.getEntity();
        
        Map<String, String> map = new HashMap<String, String>();
        // 查询条件 - 车型主键
        if (!StringUtil.isNullOrBlank(entity.getTrainTypeIDX())) {
            map.put(TRAINTYPEIDX, entity.getTrainTypeIDX());
        }
        // 查询条件 - 车型简称
        if (!StringUtil.isNullOrBlank(entity.getTrainTypeShortName())) {
            map.put("trainTypeShortName", entity.getTrainTypeShortName());
        }
        // 查询条件 - 车号
        if (!StringUtil.isNullOrBlank(entity.getTrainNo())) {
            map.put(TRAINNO, entity.getTrainNo());
        }
        
        StringBuilder sb = new StringBuilder("From ZbglTp Where recordStatus = 0");
        sb.append(CommonUtil.buildParamsHql(map));
        String siteID = EntityUtil.findSysSiteId(null);
        
        if (ZbConstants.REPAIRCLASS_LX.equals(entity.getRepairClass())) {
            // 临修
            sb.append(" And revOrgID = '").append(SystemContext.getOmOrganization().getOrgid()).append("'");
            
        } else if (ZbConstants.REPAIRCLASS_SX.equals(entity.getRepairClass())) {
            // 碎修
            sb.append(" And siteID ='").append(siteID).append("'");
        }
        // 条件筛选模糊查询
        String strPercent = "%'";
        if (!StringUtil.isNullOrBlank(search)) {
            sb.append(" And ( faultNoticeCode like '%").append(search).append(strPercent);
            sb.append(" or trainTypeShortName like '%").append(search).append(strPercent);
            sb.append(" or faultFixFullName like '%").append(search).append(strPercent);
            sb.append(" or faultName like '%").append(search).append(strPercent);
            sb.append(" or faultDesc like '%").append(search).append(strPercent);
            sb.append(" or trainNo like '%").append(search).append(strPercent);
            // 括号未加 by wujl 2016-05-10
            sb.append(")");
        }
        // 查询条件 - 检修类型
        if (!StringUtil.isNullOrBlank(entity.getRepairClass())) {
            sb.append(" And repairClass = '").append(entity.getRepairClass()).append("'");
        }
        // 查询条件 - 票活状态
        String faultNoticeStatus = entity.getFaultNoticeStatus();
        if (!StringUtil.isNullOrBlank(faultNoticeStatus)) { 
            sb.append(" And faultNoticeStatus = '").append(faultNoticeStatus).append("'");
            // 查询待销活记录时应遵循“谁领谁销”的权限限制
            if (ZbglTp.STATUS_OPEN.equals(faultNoticeStatus)) {
                sb.append(" And revPersonId = '").append(SystemContext.getOmEmployee().getEmpid()).append("'");
            }
        }
        // 排序
        sb.append(" Order by trainTypeShortName ASC, trainNo ASC, faultFixFullName ASC");
        
        String hql = sb.toString();
        String totalHql = "Select count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        
        return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * 
     * <li>说明：获取碎修、临修质量检验分页列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 当前系统操作员id
     * @param start 查询起始索引
     * @param limit 查询记录条数
     * @param repairClass 检修类型：临修、碎修
     * @param queryString 查询条件json，数据格式为： 
     * <code>
     * <br/>{
     *      <br/>trainTypeIDX：'106',
     *      <br/>trainNo：'0005',
     *      <br/>faultNoticeCode：'TP-00001785'
     * <br/>}
     * </code> 
     * @return Page
     * @throws Exception
     */
    public Page getZbglTpQCPageList(Long empid, int start, int limit, String repairClass, String queryString) throws Exception {   
        String repairClassString = "";
        if((ZbConstants.REPAIRCLASS_SX + "").equals(repairClass)) {          
            repairClassString = " AND Repair_Class = '" + ZbConstants.REPAIRCLASS_SX + "'";  //碎修
        } else if((ZbConstants.REPAIRCLASS_LX + "").equals(repairClass)) {
            repairClassString = " AND Repair_Class = '" + ZbConstants.REPAIRCLASS_LX + "'" ;   //临修
        }
        StringBuffer fromSql = new StringBuffer(" select t.idx as \"idx\",t.train_Type_IDX as \"trainTypeIDX\",t.train_No as \"trainNo\", ")
                         .append(" t.train_Type_ShortName as \"trainTypeShortName\",t.PROFESSIONAL_TYPE_IDX as \"professionalTypeIdx\", ")
                         .append(" t.PROFESSIONAL_TYPE_NAME as \"professionalTypeName\",t.FAULT_FIX_FULLCODE as \"faultFixFullCode\", ")
                         .append(" t.FAULT_FIX_FULLNAME as \"faultFixFullName\",to_char(t.FAULT_OCCUR_DATE,'yyyy-MM-dd HH24:mi') AS \"faultOccurDateV\",")
                         .append(" t.FAULT_ID as \"faultID\",t.FAULT_NAME as \"faultName\",t.FAULT_DESC as \"faultDesc\",t.FAULT_NOTICE_CODE as \"faultNoticeCode\", ")
                         .append(" t.Notice_Person_Name as \"noticePersonName\", t.Notice_Time as \"noticeTime\", ")
                         .append(" t.Method_Desc as \"methodDesc\", t.Repair_Result as \"repairResult\", t.REPAIR_DESC as \"repairDesc\" ")
                         .append(" From ZB_ZBGL_JT6 t where t.record_Status=0 ")
                                .append(" and t.Fault_Notice_Status = '").append(ZbglTp.STATUS_OVER).append("'")  //票活状态为“已处理”
                                .append(repairClassString);  //检修类型，碎修or临修
        
        String synSiteID = EntityUtil.findSysSiteId(null);
        if (null != synSiteID && synSiteID.trim().length() > 0) {
            fromSql.append(" And SITEID = '").append(synSiteID).append("'");
        }
        StringBuilder multyAwhere = new StringBuilder();
        if (!StringUtil.isNullOrBlank(queryString)) {
            Map queryMap = JSONUtil.read(queryString, Map.class); 
            
            if(queryMap.containsKey(TRAINTYPEIDX)) {
                String trainTypeIDX = String.valueOf(queryMap.get(TRAINTYPEIDX));
                if(!StringUtil.isNullOrBlank(trainTypeIDX)){
                    multyAwhere.append(" AND t.Train_Type_IDX = '").append(trainTypeIDX).append("' ");
                }
            }
            if(queryMap.containsKey(TRAINNO)) {
                String trainNo = String.valueOf(queryMap.get(TRAINNO));
                if(!StringUtil.isNullOrBlank(trainNo)){
                    multyAwhere.append(" AND t.Train_No LIKE '%").append(trainNo).append("%' ");
                }
            }
            //提票单号
            if(queryMap.containsKey("faultNoticeCode")) {
                String faultNoticeCode = String.valueOf(queryMap.get("faultNoticeCode"));
                if(!StringUtil.isNullOrBlank(faultNoticeCode)){
                    multyAwhere.append(" AND t.Fault_Notice_Code LIKE '%").append(faultNoticeCode).append("%' ");
                }
            }
        }
        String querySql = fromSql.append(multyAwhere).append(" ORDER BY T.Fault_Notice_Code").toString();
        String totalSql = "select count(1) " + querySql.substring(querySql.indexOf("From"));
        Page<ZbglTp> page = findPageList(totalSql, querySql, start, limit , null ,null);
        return page;
        
    }
    
    /**
     * <li>说明：完成临碎修提票质量检验项（工位终端）
     * <li>创建人：程梅
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglTpArray 检验完成的临碎修活票实体数组
     * @throws Exception
     */
    public void updateFinishQCResult(ZbglTp[] zbglTpArray) throws Exception {
        if (zbglTpArray == null || zbglTpArray.length < 1) 
            throw new BusinessException("未选择提票质量检验项！");
        OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
        
        Long qcEmpID = zbglTpArray[0].getAccPersonId() == null ? emp.getEmpid() : zbglTpArray[0].getAccPersonId();
        String qcEmpName = StringUtil.isNullOrBlank(zbglTpArray[0].getAccPersonName()) ? emp.getEmpname() : zbglTpArray[0].getAccPersonName();
        Date qcTime = zbglTpArray[0].getAccTime() == null ? new Date() : zbglTpArray[0].getAccTime();
        
        StringBuilder idx = new StringBuilder();
        for (ZbglTp tp : zbglTpArray) {
            idx.append(tp.getIdx()).append(Constants.JOINSTR);
        }
        if (idx.toString().endsWith(Constants.JOINSTR))
            idx.deleteCharAt(idx.length() - 1);
        String idxsStr = CommonUtil.buildInSqlStr(idx.toString());
        StringBuffer sql = new StringBuffer("UPDATE ZB_ZBGL_JT6 SET FAULT_NOTICE_STATUS = '").append(ZbglTp.STATUS_CHECK) //票活状态更新为“已验收”
                            .append("',ACC_PERSON_ID = '").append(qcEmpID)  //验收人id
                            .append("',ACC_PERSON_NAME = '").append(qcEmpName)  //验收人名称
                            .append("',ACC_TIME = TO_DATE('").append(DateUtil.yyyy_MM_dd_HH_mm_ss.format(qcTime)).append("','yyyy-mm-dd hh24:mi:ss')")//验收时间
                            .append(",UPDATE_TIME = TO_DATE('").append(DateUtil.yyyy_MM_dd_HH_mm_ss.format(new Date())).append("','yyyy-mm-dd hh24:mi:ss')")//更新时间
                            .append(" WHERE IDX IN ").append(idxsStr) ;
        daoUtils.executeSql(sql.toString());
        // 修改提票返修处理状态
        zbglTpRepairManager.updateRepairStatus(zbglTpArray);
    }
    
    /**
     * <li>说明：完成临碎修提票质量检验项（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 主键数组
     * @param accEntity 检验信息实体（可以为null），数据格式为： 
     * <code>
     * <br/>{
     *      <br/>accPersonId："106",
     *      <br/>accPersonName："王谦",
     *      <br/>accTime："2015-08-17 14:50",
     * <br/>}
     * </code>
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateFinishQCResult(String[] ids, ZbglTp accEntity) throws BusinessException, NoSuchFieldException {
        Long accPersonId = null;            // 检验人员id
        String accPersonName = null;        // 检验人员名称
        Date accTime = null;                // 检验日期
        if (null != accEntity) {
            accPersonId = accEntity.getAccPersonId();
            accPersonName = accEntity.getAccPersonName();
            accTime = accEntity.getAccTime();
        } else {
            OmEmployee emp = SystemContext.getOmEmployee();
            accPersonId = emp.getEmpid();
            accPersonName = emp.getEmpname();
            accTime = Calendar.getInstance().getTime();
        }
        List<ZbglTp> entityList = new ArrayList<ZbglTp>(ids.length);
        for (String idx : ids) {
            ZbglTp entity = this.getModelById(idx);
            // 设置检验人员id
            entity.setAccPersonId(accPersonId);
            // 设置检验人员名称
            entity.setAccPersonName(accPersonName);
            // 设置检验日期
            entity.setAccTime(accTime);
            // 设置票活状态
            entity.setFaultNoticeStatus(ZbglTp.STATUS_CHECK);
            entityList.add(entity);
        }
        // 批量保存
        this.saveOrUpdate(entityList);
    }

    /**
     * <li>说明：查询提票综合统计（根据条件动态显示字段）
     * <li>创建人：林欢
     * <li>创建日期：2016-3-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws Exception
     * @return List<ZbglTp> 返回结果集
     * @param searchEntity 查询条件
     * @param fields 前台多选框传递的值，动态显示查询字段
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTp> findTpPageListByParm(SearchEntity<ZbglTp> searchEntity, String fields) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // 查询实体
        ZbglTp entity = searchEntity.getEntity();
        List<ZbglTp> list = new ArrayList<ZbglTp>();
        List<ZbglTp> objList = new ArrayList<ZbglTp>();
        Map<String, String> map = new HashMap<String, String>();
        final String onString = "on";
        final String zzjString = " zzj.";
        
        // 查询条件 - 车型主键
        if (!StringUtil.isNullOrBlank(entity.getTrainTypeIDX()) && !onString.equals(entity.getTrainTypeIDX())) {
            map.put(TRAINTYPEIDX, entity.getTrainTypeIDX());
        }
        // 查询条件 - 车号
        if (!StringUtil.isNullOrBlank(entity.getTrainNo()) && !onString.equals(entity.getTrainNo())) {
            map.put(TRAINNO, entity.getTrainNo());
        }
        StringBuilder sb = new StringBuilder(" select zzj.trainTypeShortName,");  //From ZbglTp Where recordStatus = 0
        //拼接SQL
        if (StringUtils.isNotBlank(fields)) {
            String fieldsArry[] = fields.split(Constants.JOINSTR);
            int a = 0;
            //遍历数组
            for (String string : fieldsArry) {
                //如果不空，那么拼接字段
                if (StringUtils.isNotBlank(string)) {
                    if (a == fieldsArry.length - 1) {
                        sb.append(zzjString).append(string);
                        break;
                    }else{
                        sb.append(zzjString).append(string).append(",");
                    }
                }
                a++;
            }
        }else {
            sb.append(" zzj.trainNo,zzj.professionalTypeName,zzj.faultFixFullName,zzj.faultName ");
        }
        
        sb.append(" ,count(zzj) from ZbglTp zzj where zzj.recordStatus = 0 ");
        sb.append(CommonUtil.buildParamsHql(map));
        
//      条件筛选模糊查询
        String strPercent = "%'";
        //专业类型
        if (StringUtils.isNotBlank(entity.getProfessionalTypeName()) && !onString.equals(entity.getProfessionalTypeName())) {
            sb.append(" And zzj.professionalTypeName like '%").append(entity.getProfessionalTypeName()).append(strPercent);
        }
        
        //故障位置
        if (StringUtils.isNotBlank(entity.getFaultFixFullName()) && !onString.equals(entity.getFaultFixFullName())) {
            sb.append(" And zzj.faultFixFullName like '%").append(entity.getFaultFixFullName()).append(strPercent);
        }
        
        //故障现象
        if (StringUtils.isNotBlank(entity.getFaultName()) && !onString.equals(entity.getFaultName())) {
            sb.append(" And zzj.faultName like '%").append(entity.getFaultName()).append(strPercent);
        }
        
        //查询条件 - 提票时间(开始)
        if (!StringUtil.isNullOrBlank(entity.getStartDate())) {
            sb.append(" and  to_char(zzj.noticeTime,'yyyy-mm-dd')>='").append(entity.getStartDate()).append("'");
        }
        //查询条件 - 提票时间（结束）
        if (!StringUtil.isNullOrBlank(entity.getOverDate())){
            sb.append(" and  to_char(zzj.noticeTime,'yyyy-mm-dd')<='").append(entity.getOverDate()).append("'");
        }
        
        //准备字段存储数组游标
        int trainNoindex = 999;
        int professionalTypeNameindex = 999;
        int faultFixFullNameindex = 999;
        int faultNameindex = 999;
        
        //分组条件
        sb.append(" group by ");
        if (StringUtils.isNotBlank(fields)) {
            String fieldsArry[] = fields.split(",");
            int b = 0;
            for (String string : fieldsArry) {
                
                //如果这个字段在数组中存在，那么保存该数组游标值
                if("trainNo".equals(string)){
                    trainNoindex = b + 1;
                }else if ("professionalTypeName".equals(string)) {
                    professionalTypeNameindex = b + 1;
                }else if ("faultFixFullName".equals(string)) {
                    faultFixFullNameindex = b + 1;
                }else if ("faultName".equals(string)) {
                    faultNameindex = b + 1;
                }
                
                if (StringUtils.isNotBlank(string)) {
                    if (b == fieldsArry.length - 1) {
                        sb.append(zzjString).append(string);
                        break;
                    }else{
                        sb.append(zzjString).append(string).append(",");
                    }
                }
                b++;
            }
        }else {
            sb.append(" zzj.trainNo,zzj.professionalTypeName,zzj.faultFixFullName,zzj.faultName ");
        }
        sb.append(" ,zzj.trainTypeShortName order by count(zzj) desc");
     
   
        String hql = sb.toString();
        //获取list，由于该list中存放的是object不是bean在转json的时候会转换为数组String而不是bean
        list =  (List<ZbglTp>) this.find(hql);
        Object[] objArry = list.toArray();
        for (int i = 0; i < objArry.length; i++) {
            Object[] arr = (Object[]) objArry[i];
            ZbglTp z = new ZbglTp();
            for (int j = 0; j < arr.length; j++) {
                
                z.setTrainTypeShortName(String.valueOf(arr[0]));
                if(trainNoindex != 999){
                    z.setTrainNo(String.valueOf(arr[trainNoindex]));
                }
                if(professionalTypeNameindex != 999){
                    z.setProfessionalTypeName(String.valueOf(arr[professionalTypeNameindex] == null ? "" : arr[professionalTypeNameindex]));
                }
                if(faultFixFullNameindex != 999){
                    z.setFaultFixFullName(String.valueOf(arr[faultFixFullNameindex] == null ? "" : arr[faultFixFullNameindex]));
                }
                if(faultNameindex != 999){
                    z.setFaultName(String.valueOf(arr[faultNameindex] == null ? "" : arr[faultNameindex]));
                }
                z.setZbthtjCount(String.valueOf(arr[arr.length - 1]));
            }
            objList.add(z);
        }

        return objList;
    }

    /**
     * <li>说明：查询提票综合统计明细(根据所选行)
     * <li>创建人：林欢
     * <li>创建日期：2016-3-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws ParseException 
     * @throws Exception
     * @param searchEntity 查询条件
     * @return List<ZbglTp> 返回结果集
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTp> findTpDeteailPageListByParm(SearchEntity<ZbglTp> searchEntity) throws ParseException {
        List<ZbglTp> list = new ArrayList<ZbglTp>();
        List<ZbglTp> objList = new ArrayList<ZbglTp>();
        final String onString = "on";
//      查询实体
        ZbglTp entity = searchEntity.getEntity();
        
        Map<String, String> map = new HashMap<String, String>();
        
//      查询条件 - 车型主键
        map.put(TRAINNO, entity.getTrainNo());
        
        StringBuilder sb = new StringBuilder(" select ");  //From ZbglTp Where recordStatus = 0
        //拼接SQL
        sb.append(" zzj.trainTypeShortName, ");
        sb.append(" zzj.trainNo, ");
        sb.append(" zzj.faultNoticeCode, ");
        sb.append(" zzj.faultOccurDate, ");
        sb.append(" zzj.repairClass, ");
        sb.append(" zzj.professionalTypeName, ");
        sb.append(" zzj.noticePersonName, ");
        sb.append(" zzj.faultFixFullName, ");
        sb.append(" zzj.faultName, ");
        sb.append(" zzj.faultDesc, ");
        //销票人 = 施修人
        sb.append(" zzj.handlePersonName, ");
        sb.append(" zzj.repairResult, ");
        sb.append(" zzj.handleTime ");
        sb.append(" from ZbglTp zzj where zzj.recordStatus = 0 ");
        sb.append(CommonUtil.buildParamsHql(map));
        
////      条件筛选模糊查询
//        String strPercent = "%'";
        String strPercent = "'";
        //查询条件 - 提票时间(开始)
        if (!StringUtil.isNullOrBlank(entity.getStartDate())) {
            sb.append(" and  to_char(zzj.noticeTime,'yyyy-mm-dd')>='").append(entity.getStartDate()).append("'");
        }
        //查询条件 - 提票时间（结束）
        if (!StringUtil.isNullOrBlank(entity.getOverDate())){
            sb.append(" and  to_char(zzj.noticeTime,'yyyy-mm-dd')<='").append(entity.getOverDate()).append("'");
        }
        //专业类型
        if ("".equals(entity.getProfessionalTypeName()) && !onString.equals(entity.getProfessionalTypeName())) {
            sb.append(" And zzj.professionalTypeName is null ");
        }else if(entity.getProfessionalTypeName() != null){
//            sb.append(" And zzj.professionalTypeName like '%").append(entity.getProfessionalTypeName()).append(strPercent);
            sb.append(" And zzj.professionalTypeName = '").append(entity.getProfessionalTypeName()).append(strPercent);
        }
        //车型编码
        if ("".equals(entity.getTrainTypeShortName()) && !onString.equals(entity.getTrainTypeShortName())) {
            sb.append(" And zzj.trainTypeShortName is null ");
        }else if(entity.getTrainTypeShortName() != null){
            sb.append(" And zzj.trainTypeShortName = '").append(entity.getTrainTypeShortName()).append(strPercent);
        }
//      故障位置
        if ("".equals(entity.getFaultFixFullName()) && !onString.equals(entity.getFaultFixFullName())){
            sb.append(" And zzj.faultFixFullName is null ");
        }else if (entity.getFaultFixFullName() != null) {
//            sb.append(" And zzj.faultFixFullName like '%").append(entity.getFaultFixFullName()).append(strPercent);
            sb.append(" And zzj.faultFixFullName = '").append(entity.getFaultFixFullName()).append(strPercent);
        }
//      故障现象
        if ("".equals(entity.getFaultName()) && !onString.equals(entity.getFaultName())){
            sb.append(" And zzj.faultName is null ");
        } else if(entity.getFaultName() != null) {
//            sb.append(" And zzj.faultName like '%").append(entity.getFaultName()).append(strPercent);
            sb.append(" And zzj.faultName = '").append(entity.getFaultName()).append(strPercent);
        }
        
        String hql = sb.toString();
        
//      获取list，由于该list中存放的是object不是bean在转json的时候会转换为数组String而不是bean
//      值基本上都是固定的。非动态
        list =  (List<ZbglTp>) this.find(hql);
        Object[] objArry = list.toArray();
        for (int i = 0; i < objArry.length; i++) {
            Object[] arr = (Object[]) objArry[i];
            ZbglTp z = new ZbglTp();
            
            z.setTrainTypeShortName(String.valueOf(arr[0]));
            z.setTrainNo(String.valueOf(arr[1]));
            z.setFaultNoticeCode(String.valueOf(arr[2]));
            z.setFaultOccurDate(DateUtil.parse(String.valueOf(arr[3])));
            z.setRepairClass(String.valueOf(arr[4]));
            z.setProfessionalTypeName(String.valueOf(arr[5] == null ? "" : arr[5]));
            z.setNoticePersonName(String.valueOf(arr[6] == null ? "" : arr[6]));
            z.setFaultFixFullName(String.valueOf(arr[7] == null ? "" : arr[7]));
            z.setFaultName(String.valueOf(arr[8] == null ? "" : arr[8]));
            z.setFaultDesc(String.valueOf(arr[9] == null ? "" : arr[9]));
            z.setHandlePersonName(String.valueOf(arr[10] == null ? "" : arr[10]));
            z.setRepairResult(Integer.valueOf(String.valueOf(arr[11] == null ? "0" : arr[11])));
            z.setHandleTime((arr[12] == null ? null : DateUtil.parse(String.valueOf(arr[12]))));
            
            objList.add(z);
        }

        return objList;
    }
    
    /**
     * <li>说明：获取提票处理其他作业人员列表(取当前操作者所在班组的其他人员列表)
     * <li>创建人：程锐
     * <li>创建日期：2016-3-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 当前操作人员id 
     * @return 提票处理其他作业人员列表
     */
    public List<RepairEmpBean> getOtherWorkerByTP(Long empid) {
        OmEmployee omEmployee = omEmployeeManager.getModelById(empid);
        if (omEmployee == null) {
            return new ArrayList<RepairEmpBean>();
        }
        List list = omEmployeeSelectManager.findTeamEmpList(omEmployee.getOrgid());
        List<RepairEmpBean> repairEmpList = new ArrayList<RepairEmpBean>();
        for (int i = 0; i < list.size(); i++) {
            Object[] obj = (Object[]) list.get(i);
            Long empId = obj[0] != null ? Long.valueOf(obj[0].toString()) : null;
            if (empId == null)
                continue;
            if (empId == Long.valueOf(empid))
                continue;
            RepairEmpBean repairEmpBean = new RepairEmpBean();
            repairEmpBean.setWorkerID(Long.valueOf(obj[0].toString()));
            repairEmpBean.setWorkerName(obj[2] != null ? obj[2].toString() : "");
            repairEmpList.add(repairEmpBean);
        }
        return repairEmpList;
    }

    /**
     * FIXME(已处理) linh代码审查点，请检查注释规范，代码提交前必须确保checkStyle工具检查通过
     * <li>方法说明：获取整备管理提票或者整备管理回票待处理任务数量 
     * <li>方法名称：findZBGLTPOrZBGLHPCount 只返回zb系统的，无论临修碎修，状态为待销活或者待接活
     * <li>创建人：林欢
     * <li>创建时间：2016-04-19 下午03:20:04
     * <li>修改人：
     * <li>修改内容：
     * <li>@param operatorid
     * <li>@param faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
     * <li>@return Integer[]
     */
    @SuppressWarnings("unchecked")
    public Integer[] findZBGLTPOrZBGLHPCount(long operatorid,String faultNoticeStatus) {
        
        //faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
        
        
        
        //分别查询LX和SX的中和 10碎修 20临修
        //临修
        StringBuilder lx = getQueryTpFromHql(new HashMap<String, String>(), ZbConstants.REPAIRCLASS_LX, faultNoticeStatus, operatorid);
        
        String hqlLx = "select count(idx) ".concat(lx.toString());
        List<Long> countLx = (List<Long>) this.find(hqlLx);
        
        Integer[] inArrayLx = new Integer[countLx.size()];
        for (int i = 0; i < countLx.size(); i++) {
            inArrayLx[i] = Integer.valueOf(String.valueOf(countLx.get(i)));
        }
        
        //碎修
        StringBuilder sx = getQueryTpFromHql(new HashMap<String, String>(), ZbConstants.REPAIRCLASS_SX, faultNoticeStatus, operatorid);
        String hqlSx = "select count(idx) ".concat(sx.toString());
        List<Long> countSx = (List<Long>) this.find(hqlSx);
        
        Integer[] inArraySx = new Integer[countSx.size()];
        for (int i = 0; i < countSx.size(); i++) {
            inArraySx[i] = Integer.valueOf(String.valueOf(countSx.get(i)));
        }
        
        Integer[] inArrayTotle = new Integer[inArraySx.length];
        for (int i = 0; i < inArrayTotle.length; i++) {
            inArrayTotle[i] = inArrayLx[i] + inArraySx[i];
        }
        
        return inArrayTotle;
    }

    /**
     * FIXME(已处理) linh代码评审点
     * FIXME(已处理) linh代码审查点，请检查注释规范，代码提交前必须确保checkStyle工具检查通过
     * <li>方法说明：获取整备管理提票待碎修、临修质量检验任务数量 
     * <li>方法名称：getCount
     * <li>创建人：林欢
     * <li>创建时间：2016-04-19 下午03:20:04
     * <li>修改人：
     * <li>修改内容：
     * <li>@param operatorid 操作人id
     * <li>@return Integer[] 返回任务数量
     */
    @SuppressWarnings("unchecked")
    public Integer[] findZBGLTPOrZBGLQCCount(long operatorid) {
        StringBuffer fromSql = new StringBuffer()
        .append(" From ZbglTp t where t.recordStatus=0 ")
        .append(" and t.faultNoticeStatus = '").append(ZbglTp.STATUS_OVER).append("'");  //票活状态为“已处理”
        OmEmployee omemployee = omEmployeeManager.findByOperator(operatorid);
        WorkPlaceToOrg workPlaceOrg =workPlaceToOrgManager.getWorkPlaceToOrgByCurrentUserOrgId(omemployee.getOrgid());
        String synSiteID=workPlaceOrg.getWorkPlaceCode();
        //根据传递过来的操作人idx，查询该操作人的所属的组织架构的站点   
        if (null != synSiteID && synSiteID.trim().length() > 0) {
            fromSql.append(" And SITEID = '").append(synSiteID).append("'");
        }
        
        String totalSql = "select count(t) " + fromSql.toString();
        List<Integer> count = (List<Integer>) this.find(totalSql);
        Integer[] inArray = new Integer[count.size()];
        for (int i = 0; i < count.size(); i++) {
            inArray[i] = Integer.valueOf(String.valueOf(count.get(i)));
        }
        
        return inArray;
    }
    
    /**
     * <li>说明：获取提票列表
     * <li>创建人：林欢
     * <li>创建日期：2016-5-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param faultNoticeStatus 提票状态数组 [STATUS_OPEN,STATUS_DRAFT]
     * @param paramMap 查询参数map key：字段名 value:字段值 trainNo,trainTypeShortName
     * @return 提票列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTp> getDoingTpList(Map paramMap,String[] faultNoticeStatus) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglTp where 1 = 1 and ");
        
        //处理多种状态情况
        hql.append("(");
        for (int i = 0; i < faultNoticeStatus.length; i++) {
            String faultNoticeStatu = faultNoticeStatus[i];
            if (i == faultNoticeStatus.length - 1) {
                hql.append(" faultNoticeStatus = '").append(faultNoticeStatu).append("'");
            }else {
                hql.append(" faultNoticeStatus = '").append(faultNoticeStatu).append("' or ");
            }
        }
        hql.append(")");
        hql.append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }

    /**
     * <li>说明：通过整备单信息存储过程修改jt6状态
     * <li>创建人：林欢
     * <li>创建日期：2016-5-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param param 参数，整备单id
     */
    public void saveZbglTpByProc(String[] param) {
        daoUtils.executeProc("sp_update_jt6", param);
        daoUtils.flush();
    }

    /**
     * <li>说明：同车同位置同故障现象次数
     * <li>创建人：林欢
     * <li>创建日期：2016-7-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 查询条件
     * {
           trainTypeShortName:'HXD1',
           trainNo:'0024',
           faultFixFullCode:'040202',
           faultID:'2070'
       }
     * @return 同位置同故障现象次数{"count":"1"}
     */
    public List<ZbglTp> findZbglTpByParam(Map<String, Object> mapParam) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" from ZbglTp a where a.recordStatus = 0 ");
        //添加车型车号条件
        String[] trainTypeShortNameAndTrainNoArray = mapParam.get("trainTypeShortNameAndTrainNo").toString().split(" ");
        sb.append(" and a.trainTypeShortName = '").append(trainTypeShortNameAndTrainNoArray[0]).append("'");
        sb.append(" and a.trainNo = '").append(trainTypeShortNameAndTrainNoArray[1]).append("'");
        
        //添加故障位置条件
        sb.append(" and a.faultFixFullCode = '").append(mapParam.get("faultFixFullCode")).append("'");
        
        //添加故障现象条件
        sb.append(" and a.faultID = '").append(mapParam.get("faultID")).append("'");
        
        //添加提票单状态
        sb.append(" and a.faultNoticeStatus in (").append(mapParam.get("faultNoticeStatus")).append(")");
         
        return (List<ZbglTp>) this.find(sb.toString());
    }

    
    
    /**
     * <li>说明：通过jt6整备单的主键idx，定位获取到这个idx代表的整备单的实体类对象,（业务类）
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jt6idx主键
     * @return ZbglTp实体对象
     */
	public ZbglTp getZbglTpById(String jt6IDX) {
		return this.getModelById(jt6IDX);
	}


	@Resource 
	private ZbglTpTrackRdpRecordCenterManager zbglTpTrackRdpRecordCenterManager;
	/**
     * <li>说明：在跟踪单流程中提交提票信息后生成新的jt6提票单，同时将跟踪单的idx主键和新生成的jt6提票单传递给ZbglTpTrackRdpRecordCenterManager关系表的业务类（业务类）
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ZbglTp[] entities jt6提票单实体对象组
     * @param ZbglTp entity 一个jt6提票单实体对象
     * @param trackIDX 跟踪单的idx主键
	 * @throws Exception 
     */
	public void saveTpAndInstAndTrack(ZbglTp[] entities, ZbglTp entity, String trackIDX) throws Exception {
		for (ZbglTp tp : entities) {
            tp = buildEntity(tp);
            saveOrUpdate(tp);
            zbglWarningManager.updateForTp(tp);
            zbglTpTrackRdpRecordCenterManager.saveToTrackRdpRecordCenter(trackIDX, tp);
        }
        daoUtils.flush();
        ZbglRdp rdp = zbglRdpManager.getRunningRdpByTrain(entity.getTrainTypeShortName(), entity.getTrainNo());
        if (rdp != null) {
            saveForInstanceTp(rdp);
        }
	}

	
	
	/**
     * <li>说明：查询状态是TODO，待接活的所有提票
     * <li>创建人：刘国栋
     * <li>创建日期：2016-9-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return List<ZbglTp> 提票
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public Page<ZbglTp> findTpWhenToDo(SearchEntity<ZbglTp> searchEntity, String startDate, String overDate) {
		ZbglTp entity = searchEntity.getEntity();
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.* ");
		sb.append(" from zb_zbgl_jt6 a ");
		sb.append(" where a.FAULT_NOTICE_STATUS = 'TODO' and a.RECORD_STATUS = 0 ");
//		判断车型
        if (StringUtils.isNotBlank(entity.getTrainTypeIDX())) {
        	sb.append(" and a.train_type_idx = '").append(entity.getTrainTypeIDX()).append("'");
		}
//      判断车号
        if (StringUtils.isNotBlank(entity.getTrainNo())) {
        	sb.append(" and a.train_no like '%").append(entity.getTrainNo()).append("%'");
		}
//      判断提票单号
        if (StringUtils.isNotBlank(entity.getFaultNoticeCode())) {
        	sb.append(" and a.FAULT_NOTICE_CODE like '%").append(entity.getFaultNoticeCode()).append("%'");
		}
//      发现人
        if (StringUtils.isNotBlank(entity.getDiscover())) {
        	sb.append(" and a.DISCOVERER like '%").append(entity.getDiscover()).append("%'");
		}
        if (!StringUtil.isNullOrBlank(startDate)) {
            sb.append(" and  to_char(a.NOTICE_TIME,'yyyy-mm-dd hh24:mi:ss')>='").append(startDate).append("'");
        }
        if (!StringUtil.isNullOrBlank(overDate)) {
            sb.append(" and to_char(a.NOTICE_TIME,'yyyy-mm-dd hh24:mi:ss') <= '").append(overDate).append("'");
        }
        sb.append(" order by a.NOTICE_TIME desc");
        String sql = sb.toString();
        String totalSql = "select count(*) as rowcount " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, ZbglTp.class);
	}

	
	
	/**
     * <li>说明：查询状态是TODO和ONGOING的所有提票
     * <li>创建人：刘国栋
     * <li>创建日期：2016-9-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return List<ZbglTp> 提票
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public Page<ZbglTp> findTpWhenToDoAndOnGoing(SearchEntity<ZbglTp> searchEntity, String startDate, String overDate) {
		ZbglTp entity = searchEntity.getEntity();
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.* ");
		sb.append(" from zb_zbgl_jt6 a ");
		sb.append(" where a.FAULT_NOTICE_STATUS in ('TODO' , 'ONGOING') and a.RECORD_STATUS = 0 ");
//		判断票状态
        if (StringUtils.isNotBlank(entity.getFaultNoticeStatus())) {
        	sb.append(" and a.FAULT_NOTICE_STATUS in ('").append(entity.getFaultNoticeStatus().replace(",", "','")).append("')");
		}
//		判断车型
        if (StringUtils.isNotBlank(entity.getTrainTypeIDX())) {
        	sb.append(" and a.train_type_idx = '").append(entity.getTrainTypeIDX()).append("'");
		}
//      判断车号
        if (StringUtils.isNotBlank(entity.getTrainNo())) {
        	sb.append(" and a.train_no like '%").append(entity.getTrainNo()).append("%'");
		}
//      判断提票单号
        if (StringUtils.isNotBlank(entity.getFaultNoticeCode())) {
        	sb.append(" and a.FAULT_NOTICE_CODE like '%").append(entity.getFaultNoticeCode()).append("%'");
		}
//      发现人
        if (StringUtils.isNotBlank(entity.getDiscover())) {
        	sb.append(" and a.DISCOVERER like '%").append(entity.getDiscover()).append("%'");
		}
        if (!StringUtil.isNullOrBlank(startDate)) {
            sb.append(" and  to_char(a.NOTICE_TIME,'yyyy-mm-dd hh24:mi:ss')>='").append(startDate).append("'");
        }
        if (!StringUtil.isNullOrBlank(overDate)) {
            sb.append(" and to_char(a.NOTICE_TIME,'yyyy-mm-dd hh24:mi:ss') <= '").append(overDate).append("'");
        }
        sb.append(" order by a.NOTICE_TIME desc");
        String sql = sb.toString();
        String totalSql = "select count(*) as rowcount " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, ZbglTp.class);
	}

    /**
     * <li>说明：查询临碎修质量检查数量 
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询参数 
     * @param repairclass 临碎修类型
     * @param operatorid 操作人id
     * @return 数量 
     * @throws Exception
     */
    public int getTpQcCount(String searchJson, String repairclass, Long operatorid) throws Exception {
        OmEmployee emp = omEmployeeSelectManager.getByOperatorid(operatorid);
        return getZbglTpQCPageList(emp.getEmpid(), 0, 1000, repairclass, searchJson).getTotal();    
    }

	
}
