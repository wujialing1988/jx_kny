package com.yunda.jx.pjjx.partsrdp.manager;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarDetailManager;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarInfoUtil;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager;
import com.yunda.jx.pjjx.base.wpdefine.entity.WP;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNode;
import com.yunda.jx.pjjx.base.wpdefine.manager.WPManager;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR.PartsRdpSearcher;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRManager;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.manager.PartsRdpNoticeManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecCard;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecCardManager;
import com.yunda.jx.pjjx.partsrdp.worktime.manager.PartsRdpWorkTimeManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNode;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeQueryManager;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.util.MixedUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdp业务类,配件检修作业
 * <li>创建人：程梅
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpManager")
public class PartsRdpManager extends JXBaseManager<PartsRdp, PartsRdp>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** PartsAccount业务类，配件周转台账---配件信息 */
	@Resource
	private PartsAccountManager partsAccountManager ;	
	
	/** PartsType业务类 互换配件型号 */
	@Resource
	private PartsTypeManager partsTypeManager;
	
	/** PartsRdpTecCard业务类,配件检修工艺工单 */
	@Resource
	private PartsRdpTecCardManager partsRdpTecCardManager;
	
	/** PartsRdpRecordCard业务类,配件检修记录卡实例 */
	@Resource
	private PartsRdpRecordCardManager partsRdpRecordCardManager;
	
	/** PartsRdpNotice业务类,提票单 */
	@Resource
	private PartsRdpNoticeManager partsRdpNoticeManager;
	
	/** PartsRdpNode业务类,配件检修作业节点 */
	@Resource
	private PartsRdpNodeManager partsRdpNodeManager ;    

    /** PartsManageLogManager业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;    

    /** CalcPartsNodeWorkDateManager业务类,计算配件流程节点计划开完工时间、工期等逻辑的业务类 */
    @Resource
    private CalcPartsNodeWorkDateManager calcPartsNodeWorkDateManager ;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	/** 检修需求单业务类 */
	@Resource
	private WPManager wPManager;

    /** 业务处理常量 */
    private String statusCH = "状态";
    private String siteIDCH = "站点";
    private String operaterIdCH = "创建人";
    private String createTimeCH = "创建时间";
    private String rdpIdxCH = "任务单id";
    
    /** 工时业务类 */
    @Resource
    private PartsRdpWorkTimeManager partsRdpWorkTimeManager ;

    /** 配件检修质检业务类 */
    @Resource
    private PartsRdpQRManager partsRdpQRManager;    

    /** 配件检修节点查询业务类 */
    @Resource
    private PartsRdpNodeQueryManager partsRdpNodeQueryManager;
    
    /** 工作日历明细业务类 */
    @Resource
    private WorkCalendarDetailManager workCalendarDetailManager;
    
	/**
	 * <li>说明：格式化配件基本信息（可定制）
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param rdp 配件检修作业实体
	 * @return String
	 */
	private String formatPartsInfo(PartsRdp rdp) {
		StringBuilder sb = new StringBuilder();
		sb.append(null == rdp.getPartsNo()? "" : rdp.getPartsNo());								// 配件编号
		sb.append(rdp.getPartsName());															// 配件名称
		
		// 获取配件型号实体对象
		PartsType partsType = this.getPartsType(rdp);	
		sb.append(Constants.BRACKET_L).append(partsType.getSpecificationModel()).append(Constants.BRACKET_R);					// 规格型号
		sb.append(null == rdp.getExtendNo() ? "" : this.formatExtendNo(rdp.getExtendNo()));		// 扩展编号	
		return sb.toString();
	}
	
	/**
	 * <li>说明：查询配件检修作业处理的配件
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdp 配件检修作业实体
	 * @return PartsType
	 */
	private PartsType getPartsType(PartsRdp rdp) {
		return this.partsTypeManager.getModelById(rdp.getPartsTypeIDX());
	}
	
	/**
	 * <li>说明：查询当前系统操作人员施修的配件检修作业
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> partsTree() {
		// 查询当前系统操作人员施修的配件检修作业
		List<PartsRdp> list =  this.findList(SystemContext.getOmEmployee().getEmpid());		
		List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
        String temp = null;
		for (PartsRdp rdp : list) {
			map = new HashMap<String, Object>();
			map.put("id", rdp.getIdx());
			map.put("text", this.formatPartsInfo(rdp));
			map.put("leaf", true);			
			map.put("partsNo", null == rdp.getPartsNo()? "" : rdp.getPartsNo());											// 配件编号
			map.put("partsName", rdp.getPartsName());										// 配件名称
			map.put("specificationModel", getPartsType(rdp).getSpecificationModel());		// 规格型号
			map.put("extendNo", formatExtendNo(rdp.getExtendNo()));											// 扩展编号
            if (null != rdp.getUnloadTrainType()) {
                temp = rdp.getUnloadTrainType();
                if (null != rdp.getUnloadTrainNo()) {
                    temp += rdp.getUnloadTrainNo();
                }
                map.put("trainType", temp); 			// 车型车号
            }
            if (null != rdp.getUnloadRepairClass()) {
                temp = rdp.getUnloadRepairClass();
                if (null != rdp.getUnloadRepairTime()) {
                    temp += rdp.getUnloadRepairTime();
                }
                map.put("repair", temp); 		     	// 车型车号
            }
			map.put("planStartTime", sdf.format(rdp.getPlanStartTime()));					// 计划开始时间
			map.put("planEndTime", sdf.format(rdp.getPlanEndTime()));						// 计划结束时间
			map.put("realStartTime", sdf.format(rdp.getRealStartTime()));					// 实际开始时间
			map.put("realEndTime", sdf.format(rdp.getRealEndTime() == null ? new Date() : rdp.getRealEndTime()));						// 实际结束时间
			
			children.add(map);
		}
		return children;
	}
	
	/**
	 * <li>说明：查询指定操作人员id施修的配件检修作业
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param empId 当前作业处理人员id
	 * @return List
	 */
	public List findList(Long empId) {
		String hql = "From PartsRdp Where recordStatus = 0 And status = ? And idx In (Select rdpIDX From PartsRdpWorker Where recordStatus = 0 And workEmpID = ?) Order By partsName";
		return this.daoUtils.find(hql, new Object[]{IPartsRdpStatus.CONST_STR_STATUS_JXZ, empId});
	}
	
	/**
	 * <li>说明：查询当前系统操作人员施修的配件检修作业
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return List
	 */
	public List findList() {
		Long empId = SystemContext.getOmEmployee().getEmpid();
		return this.findList(empId);
	}
	
	/**
     *
	 * <li>说明：分页查询配件检修作业
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人：张迪 
	 * <li>修改日期：2016-06-06
	 * <li>修改内容：
	 * @param searchEntity 查询对象实体
	 * @return PartsRdp分页对象
	 * @throws BusinessException
	 */
	@Override
	public Page<PartsRdp> findPageList(SearchEntity<PartsRdp> searchEntity) throws BusinessException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("From PartsRdp Where recordStatus = 0");
        PartsRdp rdp = searchEntity.getEntity();
     
        if(!StringUtil.isNullOrBlank(rdp.getPartsNo())){
            sb.append(" and partsNo = '").append(rdp.getPartsNo()).append("'");
        }
       if( !StringUtil.isNullOrBlank(rdp.getIdentificationCode())){   
            sb.append(" and identificationCode = '").append(rdp.getIdentificationCode()).append("'");
        }
       if( !StringUtil.isNullOrBlank(rdp.getPartsAccountIDX())){   
           sb.append(" and partsAccountIDX = '").append(rdp.getPartsAccountIDX()).append("'");
       }
		String hql = sb.toString();
		String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
		return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：将扩展编号的JSON字符串格式化为以“|”分割的字符串形式
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param extendNo 扩展编号的JSON数组字符串
	 * @return String
	 */
	public String formatExtendNo(String extendNo) {
		try {
			ExtendNo[] entityArray = JSONUtil.read(extendNo, ExtendNo[].class);
			if (null != entityArray && entityArray.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (ExtendNo entity : entityArray) {
					sb.append("|").append(entity.getValue());
				}
				return sb.substring(1);
			}
		} catch (Exception e) {
			return extendNo;
		}
		return "";
		
	}
	
	/**
	 * <li>标题: 机车检修管理信息系统
	 * <li>说明：扩展编号格式化JSON实体类
	 * <li>创建人： 何涛
	 * <li>创建日期： 2014-12-29 下午04:46:10
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * 
	 * @author 测控部检修系统项目组
	 * @version 1.0
	 */
	@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
	private static class ExtendNo {
		/** 扩展编号值 */
		private	String value;
		/** 扩展编号字段 */
		private String extendNoField;
		/** 扩展编号字段名称 */
		private String extendNoName;
		
		/**
		 * @return 获取扩展编号字段
		 */
		public String getExtendNoField() {
			return extendNoField;
		}
		/**
		 * @param extendNoField 设置扩展编号字段
		 */
		public void setExtendNoField(String extendNoField) {
			this.extendNoField = extendNoField;
		}
		/**
		 * @return 获取扩展编号字段名称
		 */
		public String getExtendNoName() {
			return extendNoName;
		}
		/**
		 * @param extendNoName 设置扩展编号字段名称
		 */
		public void setExtendNoName(String extendNoName) {
			this.extendNoName = extendNoName;
		}
		/**
		 * @return 获取扩展编号值
		 */
		public String getValue() {
			return value;
		}
		/**
		 * @param value 设置扩展编号值
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
	}
	
	/**
	 * <li>方法说明：保存兑现单
	 * <li>方法名：savePartsRdp
	 * @param partsRdps 兑现单实体数组
	 * @throws Exception
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-22
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void savePartsRdpMulti(PartsRdp[] partsRdps) throws Exception{
		AcOperator ac = SystemContext.getAcOperator();
		for(PartsRdp t : partsRdps){
            savePartsRdpSingle(t,ac);
		}
	}
    
    /**
     * <li>方法说明：保存兑现单
     * <li>方法名：savePartsRdp
     * @param partsRdp 待保存的兑现单事例
     * @param acOperator 当前登录人员信息
     * @throws Exception
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-22
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void savePartsRdpSingle(PartsRdp partsRdp,AcOperator acOperator) throws Exception{
        //生成配件检修基本信息RDP单    
        saveRdpInfo(partsRdp, acOperator);
        //生成配件检修流程节点及前后置关系；
        createRdpNode(partsRdp, partsRdp.getSiteID(), acOperator.getOperatorid());
        //createRdpTec(partsRdp, partsRdp.getSiteID(), acOperator.getOperatorid());
        createRdpRecord(partsRdp, partsRdp.getSiteID(), acOperator.getOperatorid());
        createRdpEquip(partsRdp.getIdx(), partsRdp.getSiteID(), acOperator.getOperatorid());        
        createRdpNodeStation(partsRdp, partsRdp.getSiteID(), acOperator.getOperatorid());
        createRdpNodeMat(partsRdp, partsRdp.getSiteID(), acOperator.getOperatorid());
        daoUtils.flush();
        updateWorkPlanBeginEndTime(partsRdp);
        PartsAccount partsAccount = partsAccountManager.getModelById(partsRdp.getPartsAccountIDX());
        if (partsAccount == null)
            return;
        String partsStatus = partsAccount.getPartsStatus();
        String partsStatusName = partsAccount.getPartsStatusName();
        //配件状态为【待修】状态，配件检修作业“兑现生成”后，设置配件状态为“检修中”，同时往配件信息日志表中添加记录，记录待修状态
        if (partsStatus.contains(PartsAccount.PARTS_STATUS_DX)) {
            updatePartsLog(partsAccount, partsRdp, partsStatus, partsStatusName);
            updatePartsAccountStatus(partsAccount, PartsAccount.PARTS_STATUS_JXZ, PartsAccount.PARTS_STATUS_JXZ_CH);
        }
    }
    
    /**
     * <li>方法说明：保存并启动单个兑现单
     * <li>方法名：saveAndStartPartsRdpSingle
     * @param partsRdp 待保存的兑现单事例
     * @param acOperator 当前登录人员信息
     * @throws Exception
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-22
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void saveAndStartPartsRdpSingle(PartsRdp partsRdp,AcOperator acOperator) throws Exception{
        this.savePartsRdpSingle(partsRdp, acOperator);
        this.updateStartPartsRdp(partsRdp.getIdx());
    }
    
	/**
     * <li>方法说明：更新配件信息状态
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-22
     * <li>修改人：何涛
     * <li>修改内容：代码重构，删除未被使用的PartsRdp参数
     * <li>修改日期：
     * @param partsAccount 配件信息实体
     * @param partsStatus 配件状态
     * @param partsStatusName 配件状态中文名
     * @throws NoSuchFieldException
     * @throws BusinessException
     */
    public void updatePartsAccountStatus(PartsAccount partsAccount, String partsStatus, String partsStatusName) throws BusinessException,
        NoSuchFieldException {
        partsStatusName = partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", partsStatus, partsStatusName);
        partsAccount.setPartsStatus(partsStatus);
        partsAccount.setPartsStatusName(partsStatusName);
        partsAccount.setPartsStatusUpdateDate(Calendar.getInstance().getTime()); // 配件状态更新时间为当前时间
        this.partsAccountManager.saveOrUpdate(partsAccount);
    }
    
    /**
     * <li>说明：更新配件管理日志
     * <li>创建人：程锐
     * <li>创建日期：2015-11-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsAccount 配件信息实体
     * @param partsRdp 配件作业计划单实体
     * @param partsStatus 配件状态
     * @param partsStatusName 配件状态中文名
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void updatePartsLog(PartsAccount partsAccount, PartsRdp partsRdp, String partsStatus, String partsStatusName) throws BusinessException, NoSuchFieldException {
        PartsManageLog log = new PartsManageLog();
        log.setEventIdx(partsRdp.getIdx());
        log.setEventType(PartsManageLog.EVENT_TYPE_PJJX);
        log = partsManageLogManager.initLog(log, partsAccount);
        String eventDesc = partsRdp.getRepairOrgName();
        log.setEventDesc(eventDesc);
        partsManageLogManager.saveOrUpdate(log);
    }
	
	/**
	 * <li>方法说明：设置计划默认信息
	 * <li>方法名：setNewRdpDefaultInfo
	 * @param t 兑现单
	 * @param ac 操作员
	 * @throws Exception
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-22
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private void saveRdpInfo(PartsRdp t, AcOperator ac) throws Exception {
		WP wp = wPManager.getModelById(t.getWpIDX());//检修需求单信息
		//获取当前登录者所在机构信息
		OmOrganization org = SystemContext.getOmOrganization();
		//获取当前登录者信息
		t.setRatedPeriod(wp.getRatedPeriod());//额定工期
		t.setRatedWorkTime(wp.getRatedWorkTime());//额定工时
		//设置编制人、编制部门、编制日期信息
		t.setRdpEmpID(ac.getOperatorid());
		t.setRdpEmpName(ac.getOperatorname());
		t.setRdpOrgID(org.getOrgid());
		t.setRdpOrgName(org.getOrgname());
		t.setRdpTime(new Date());
		t.setStatus(PartsRdp.STATUS_WQD);        
        if (t.getRepairOrgID() == null)
            t.setRepairOrgID(org.getOrgid());
        if (StringUtil.isNullOrBlank(t.getRepairOrgName()))
            t.setRepairOrgName(org.getOrgname());
        if (StringUtil.isNullOrBlank(t.getRepairOrgSeq()))
            t.setRepairOrgSeq(org.getOrgseq());
		saveOrUpdate(t);
	}
    
    /**
     * <li>说明：生成配件检修作业节点和前后置关系
     * <li>创建人：程梅
     * <li>创建日期：2014-12-24
     * <li>修改人： 程锐
     * <li>修改日期：2015-10-9
     * <li>修改内容：生成节点时增加层级字段的生成，原生成sql作废
     * @param partsRdp 任务单
     * @param siteID 站点标示
     * @param operaterId 操作员id
     */
    private void createRdpNode(PartsRdp partsRdp, String siteID, Long operaterId) throws Exception {        
        
        //生成配件检修作业节点 [状态--未开放]
        SystemContextUtil.setSystemInfoByOperatorId(operaterId);
        String sql = SqlMapUtil.getSql("pjjx-rdp:insert_parts_rdp_node");
        Date date = new Date();
        MixedUtils.executeSQL(daoUtils, sql, partsRdp.getIdx(),IPartsRdpStatus.CONST_STR_STATUS_WKF, Constants.NO_DELETE, 
        		siteID, operaterId, date, operaterId, date, partsRdp.getCalendarIdx(), "ROOT_0", partsRdp.getWpIDX());
        updateNodeLeaf(partsRdp.getIdx());//对没有子节点的父节点这种情况：设置其为子节点
        //生成作业节点前后置关系
        String rdpNodeSeqSql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpNodeSeq");
        MixedUtils.executeSQL(daoUtils, rdpNodeSeqSql, partsRdp.getIdx(), siteID, operaterId, date, operaterId, date);
        calcPartsNodeWorkDateManager.updatePlanTimeByWorkPlan(partsRdp, true);
    }
    
    /**
     * <li>说明：生成节点和工位对应关系
     * <li>创建人：程锐
     * <li>创建日期：2015-11-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsRdp 任务单
     * @param siteID 站点标示
     * @param operaterId 操作员id
     * @throws Exception
     */
    private void createRdpNodeStation(PartsRdp partsRdp, String siteID, Long operaterId) throws Exception {      
        SystemContextUtil.setSystemInfoByOperatorId(operaterId);
        String sql = SqlMapUtil.getSql("pjjx-rdp:insertRdpNodeStation");
        Date date = new Date();
        MixedUtils.executeSQL(daoUtils, sql, Constants.NO_DELETE, siteID, operaterId, date, operaterId, date, partsRdp.getIdx());
    }
    
    /**
     * <li>说明：生成节点和检修用料对应关系
     * <li>创建人：张迪
     * <li>创建日期：2016-9-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsRdp 任务单
     * @param siteID 站点标示
     * @param operaterId 操作员id
     * @throws Exception
     */
    private void createRdpNodeMat(PartsRdp partsRdp, String siteID, Long operaterId) throws Exception {      
        SystemContextUtil.setSystemInfoByOperatorId(operaterId);
        String sql = SqlMapUtil.getSql("pjjx-rdp:insertRdpNodeMat");
        Date date = new Date();
        MixedUtils.executeSQL(daoUtils, sql, Constants.NO_DELETE, siteID, operaterId, date, operaterId, date, partsRdp.getIdx());
    }
    
    /**
     * <li>说明：生成配件检修记录单实例、记录卡实例、检测项实例、检测项、质量检查结果
     * <li>创建人：程梅
     * <li>创建日期：2014-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsRdp 配件兑现单
     * @param siteID 站点标示
     * @param operaterId 操作员id
     */
    private void createRdpRecord(PartsRdp partsRdp, String siteID, Long operaterId){
        String rdpRecordSql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpRecord");        
        Date date = new Date();
        MixedUtils.executeSQL(daoUtils, rdpRecordSql, partsRdp.getIdx(), siteID, operaterId, date, operaterId, date, partsRdp.getWpIDX());
        
        //生成配件检修记录卡实例  [状态--未开放]
        //先生成检修记录卡实例
        String rdpRecordCardSql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpRecordCard")
									        .replace(statusCH, IPartsRdpStatus.CONST_STR_STATUS_WKF)
									        .replace(siteIDCH, siteID)
									        .replace(operaterIdCH, operaterId.toString())
									        .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
									        .replace(rdpIdxCH, partsRdp.getIdx());
        daoUtils.executeSql(rdpRecordCardSql);
        
        //生成配件检修检测项实例  [状态--未开放]
        String rdpRecordRISql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpRecordRI")
                                            .replace(statusCH, IPartsRdpStatus.CONST_STR_STATUS_WKF)
                                            .replace(siteIDCH, siteID)
                                            .replace(operaterIdCH, operaterId.toString())
                                            .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
                                            .replace(rdpIdxCH, partsRdp.getIdx());
        daoUtils.executeSql(rdpRecordRISql);
        
        //生成配件检测项
        String rdpRecordDISql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpRecordDI")
        								  .replace(siteIDCH, siteID)
        								  .replace(operaterIdCH, operaterId.toString())
        								  .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
        								  .replace(rdpIdxCH, partsRdp.getIdx());

        daoUtils.executeSql(rdpRecordDISql);
        
        //生成质量检查结果  [状态--未开放]
        String rdpQRSql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpQR")
							        //状态为待处理--01
							        .replace(statusCH, IPartsRdpStatus.CONST_STR_STATUS_WKF)
							        .replace(siteIDCH, siteID)
							        .replace(operaterIdCH, operaterId.toString())
							        .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
							        .replace(rdpIdxCH, partsRdp.getIdx());

        daoUtils.executeSql(rdpQRSql);
        daoUtils.flush();
        //生成不拆卸配件记录
        String inseparablPartsSql = SqlMapUtil.getSql("pjjx-rdp:insertInseparablParts")
        									  .replace(rdpIdxCH, partsRdp.getIdx());
        daoUtils.executeSql(inseparablPartsSql);
        //生成拆卸配件登记
        String dismantlePartsSql = SqlMapUtil.getSql("pjjx-rdp:insertDismantleParts")
									        .replace(statusCH, PjwzConstants.STATUS_WAIT)
									        .replace(siteIDCH, siteID)
									        .replace(operaterIdCH, operaterId.toString())
									        .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
									        .replace(rdpIdxCH, partsRdp.getIdx());
        daoUtils.executeSql(dismantlePartsSql);
        //生成安装配件登记
        String installPartsSql = SqlMapUtil.getSql("pjjx-rdp:insertInstallParts")
									        .replace(statusCH, PjwzConstants.STATUS_WAIT)
									        .replace(siteIDCH, siteID)
									        .replace(operaterIdCH, operaterId.toString())
									        .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
									        .replace(rdpIdxCH, partsRdp.getIdx());
		daoUtils.executeSql(installPartsSql);
    }
    /**
     * 
     * <li>说明：生成配件检修工艺、工艺工单、工序实例
     * <li>创建人：程梅
     * <li>创建日期：2014-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsRdp 配件兑现单
     * @param siteID 站点标示
     * @param operaterId 操作员id
     */
    private void createRdpTec(PartsRdp partsRdp, String siteID, Long operaterId){
        //生成配件检修工艺
        String rdpTecSql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpTec");
        Date date = new Date();
        MixedUtils.executeSQL(daoUtils, rdpTecSql, partsRdp.getIdx(), siteID, operaterId, date, operaterId, date, partsRdp.getWpIDX());
        //生成配件检修工艺工单 [状态--未开放]
        //先生成检修工艺工单
        String rdpTecCardSql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpTecCard")
        .replace(statusCH, IPartsRdpStatus.CONST_STR_STATUS_WKF)
        .replace(siteIDCH, siteID)
        .replace(operaterIdCH, operaterId.toString())
        .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
        .replace(rdpIdxCH, partsRdp.getIdx());
        daoUtils.executeSql(rdpTecCardSql);
        
        //生成物料消耗数据
        String sql = SqlMapUtil.getSql("pjjx-rdp:insert_rdp_mat");
        MixedUtils.executeSQL(daoUtils, sql, siteID, operaterId, date, operaterId, date, partsRdp.getIdx());
        
        //生成配件检修工序实例 [状态--未开放]
        String rdpTecWSSql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpTecWS")
        .replace(statusCH, IPartsRdpStatus.CONST_STR_STATUS_WKF)
        .replace(siteIDCH, siteID)
        .replace(operaterIdCH, operaterId.toString())
        .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
        .replace(rdpIdxCH, partsRdp.getIdx());
        daoUtils.executeSql(rdpTecWSSql);
    }
    
    /**
     * <li>说明：生成机务设备工单实例和检测数据项
     * <li>创建人：程梅
     * <li>创建日期：2015-1-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx 任务单id
     * @param siteID 站点标示
     * @param operaterId 操作员id
     */
    private void createRdpEquip(String rdpIdx, String siteID, Long operaterId){
        //生成机务设备工单实例
        String rdpEquipCardSql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpEquipCard")
                                           .replace(statusCH, IPartsRdpStatus.CONST_STR_STATUS_WKF)
                                           .replace(siteIDCH, siteID)
                                           .replace(operaterIdCH, operaterId.toString())
                                           .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
                                           .replace(rdpIdxCH, rdpIdx);
        daoUtils.executeSql(rdpEquipCardSql);
        //生成机务设备检测数据项
        String rdpEquipDISql = SqlMapUtil.getSql("pjjx-rdp:insertPartsRdpEquipDI")
                                         .replace(siteIDCH, siteID)
                                         .replace(operaterIdCH, operaterId.toString())
                                         .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
                                         .replace(rdpIdxCH, rdpIdx);
        daoUtils.executeSql(rdpEquipDISql);
    }
    
	/**
	 * <li>说明：验证--同一配件不能重复兑现
	 * <li>创建人：程梅
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param id 配件id
	 * @return String[] 错误提示
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public String[] validateSave(String id) throws BusinessException {
		PartsAccount account = partsAccountManager.getModelById(id);//查询配件信息
        List<PartsRdp> list = (List<PartsRdp>) daoUtils.find(" from PartsRdp o where o.recordStatus = 0 and " +
            "o.partsAccountIDX = '" + id + "' " +
            "and (o.status = '"+ PartsRdp.STATUS_WQD +"' or o.status = '"+ PartsRdp.STATUS_JXZ +"' or o.status = '"+ PartsRdp.STATUS_DYS +"')");
        if(list != null && list.size() > 0){
            return new String[]{"规格型号【"+ account.getSpecificationModel() +"】,配件名称【"+ account.getPartsName()+"】已有施修任务在处理中，不能重复兑现！"};
        }
        return null;
    }
    
	/**
	 * <li>说明：验证--修改兑现单状态时验证【终止、验收】
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param flag 操作标示
	 * @param ids 兑现id
	 * @return String[] 错误提示
	 * @throws BusinessException
	 */
	public String[] validateUpdate(String flag,String[] ids) throws BusinessException {
        String strPartsNo = "配件编号【";
        String strPartsName = "】,配件名称【";
		List<String> errMsg = new ArrayList<String>();
		PartsRdp rdp ;
		//终止操作验证
		if(PartsRdp.STATUS_YZZ.equals(flag)){
			for(String id : ids){
				rdp = getModelById(id);
				if(null != rdp){
					if(PartsRdp.STATUS_YZZ.equals(rdp.getStatus())){
						errMsg.add(strPartsNo+ rdp.getPartsNo() +strPartsName+ rdp.getPartsName()+"】，此记录已终止，不能再进行终止操作！");
					}else if(PartsRdp.STATUS_JXHG.equals(rdp.getStatus())){
						errMsg.add(strPartsNo+ rdp.getPartsNo() +strPartsName+ rdp.getPartsName()+"】，此记录已检修合格，不能进行终止操作");
					}
				}
			}
		}else if(PartsRdp.STATUS_JXHG.equals(flag)){   //验收操作验证
			for(String id : ids){
				rdp = getModelById(id);
				if(null != rdp){
					if(!PartsRdp.STATUS_DYS.equals(rdp.getStatus())){
						errMsg.add(strPartsNo+ rdp.getPartsNo() +strPartsName+ rdp.getPartsName()+"】，不能进行验收操作，请刷新后重试！");
					} else {
                        // Modified by hetao on 2015-03-25 修改配件检修合格验收时，提示未处理的工单数量
                        String checkAcceptance = this.checkAcceptance(rdp);
                        if (null != checkAcceptance) {
                            errMsg.add(checkAcceptance);
                        }
                    }
				}
			}
		}
		if (errMsg.size() > 0) {
			String[] errArray = new String[errMsg.size()];
			errMsg.toArray(errArray);
			return errArray;
		}
		return null;
    }
    
    /**
     * <li>说明：配件检修合格验收操作前的验证处理，返回未处理工单的统计信息
     * <li>创建人：何涛
     * <li>创建日期：2016-3-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdp 配件检修作业对象
     * @return 未处理工单统计信息
     */
    public String checkAcceptance(PartsRdp rdp) {
        StringBuilder sb = new StringBuilder();
        String hql = "From PartsRdpRecordCard Where recordStatus = 0 And rdpIDX = ? And status <> ?";
        int count = this.daoUtils.getCount(hql, rdp.getIdx(), IPartsRdpStatus.CONST_STR_STATUS_XJ);
        if (0 < count) {
            sb.append("有" + count + "条检修记录未完成！<br/>");
        }
        hql = "From PartsRdpTecCard Where recordStatus = 0 And rdpIDX = ? And status <> ?";
        count = this.daoUtils.getCount(hql, rdp.getIdx(), IPartsRdpStatus.CONST_STR_STATUS_XJ);
        if (0 < count) {
            sb.append("有" + count + "条作业工单未完成！<br/>");
        }
        hql = "From PartsRdpNotice Where recordStatus = 0 And rdpIDX = ? And status <> ?";
        count = this.daoUtils.getCount(hql, rdp.getIdx(), IPartsRdpStatus.CONST_STR_STATUS_XJ);
        if (0 < count) {
            sb.append("有" + count + "条提票工单未完成！<br/>");
        }
        return sb.length() <= 0 ? null : "合格验收失败：<br/>" + sb.toString();
    }
    
	/**
	 * <li>说明： 配件检修作业 完工
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 程锐
	 * <li>修改日期：2015-11-12
	 * <li>修改内容：无法修复时，终止所有未完成的质量检查项（包括必检和抽检）
	 * @param id 配件检修作业主键
	 * @param repairResultDesc 检修结果描述
	 * @param status 完工类型，一般分“修竣待验收(03)”和“无法修复（0402）”两种类型
	 * @throws Exception 
	 */
	public void finishPartsRdp(Serializable id, String repairResultDesc, String status) throws Exception {
		PartsRdp entity = this.getModelById(id);		
		entity.setStatus(status);										
		entity.setRealEndTime(Calendar.getInstance().getTime());		
		if (IPartsRdpStatus.CONST_STR_STATUS_WFXF.equals(status)) {
//		if (status.equals(IPartsRdpStatus.CONST_STR_STATUS_WFXF)) {
			// 无法修复需要填写无法修复原因；
			entity.setRepairResultDesc(repairResultDesc);				// 检修结果描述
            partsRdpQRManager.updateTerminateQCResult(String.valueOf(id));
            PartsAccount partsAccount = partsAccountManager.getModelById(entity.getPartsAccountIDX());
            //配件状态为【检修中】状态，配件检修作业“无法修复”后，设置配件状态为“待报废”
            if (partsAccount != null && partsAccount.getPartsStatus().contains(PartsAccount.PARTS_STATUS_JXZ)) {
                updatePartsAccountStatus(partsAccount, PartsAccount.PARTS_STATUS_DBF, partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DBF, PartsAccount.PARTS_STATUS_DBF_CH));
            }            
		} else {
			entity.setRepairResultDesc("修竣提交");						// 检修结果描述
		}		
		this.saveOrUpdate(entity);
		//获取当前登录者信息
        AcOperator ac = SystemContext.getAcOperator();
        //平均分配工时
        partsRdpWorkTimeManager.saveWorkTimeAuto(entity.getIdx(), entity.getSiteID(), ac.getOperatorid());
	}
    
	/**
	 * <li>说明： 配件检修作业 完工操作前的数据验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param id 配件检修作业主键
	 * @param status 完工类型，一般分“修竣待验收(03)”和“无法修复（0402）”两种类型
	 * @return String[]
	 */
	public String[] validateFinishPartsRdp(Serializable id, String status) {
		List<String> errList = new ArrayList<String>();
		PartsRdp entity = this.getModelById(id);
		if (null == entity) {
			errList.add("数据异常，请刷新页面后重试！");
		// 如果是“修竣提交”操作，是否需要检查其相关的作业工单是否已经都完成处理
		} else if (status.equals(IPartsRdpStatus.CONST_STR_STATUS_JXDYS)) {
			this.checkFinishStatus(entity, errList);
		}
		
		if (errList.size() > 0) {
			return errList.toArray(new String[errList.size()]);
		}
		return null;
	}
    
	/**
	 * <li>说明： 检验作业计划单下属所有工单是否均已“修竣”，包括下属工单下属的其他作业项均已处理
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 配件检修作业实体
	 * @param msgList 检验结果列表
	 */
	@SuppressWarnings("unchecked")
	public void checkFinishStatus(PartsRdp entity, List<String> msgList)  {
	    // 检验下属检修工艺工单是否都已处理
        List entityList = partsRdpTecCardManager.getModels(entity.getIdx());
	    if (null != entityList && entityList.size() > 0) {
	        partsRdpTecCardManager.checkFinishStatus(entityList, msgList);
	        if (msgList.size() > 0) {
	            return;
	        }
	    }
	    // 检验下属检修记录工单是否都已处理
        entityList = partsRdpRecordCardManager.getModels(entity.getIdx());
	    if (null != entityList && entityList.size() > 0) {
	        partsRdpRecordCardManager.checkFinishStatus(entityList, msgList);
            if (msgList.size() > 0) {
                return;
            }
	    }
		// 检验下属检修记录工单是否都已处理
		entityList = partsRdpNoticeManager.getModels(entity.getIdx());
		if (null != entityList && entityList.size() > 0) {
			partsRdpNoticeManager.checkFinishStatus(entityList, msgList);
		}
	}
    
	/**
	 * <li>说明：flag为PartsRdp.STATUS_YZZ---终止任务单【级联终止检修作业节点、检修工艺工单、检修记录卡实例、提票单】
	 * 			flag为PartsRdp.STATUS_JXHG----验收【更新任务单状态和配件状态】
	 * <li>创建人：程梅
	 * <li>创建日期：2014-12-23
	 * <li>修改人： 程锐
	 * <li>修改日期：2015-11-12
	 * <li>修改内容：合格验收、终止任务时终止所有未完成的质量检查项（包括必检和抽检）
	 * @param flag 操作标示
	 * @param ids 任务单id
	 * @throws Exception 
	 */
	public void updateStatus(String flag, String[] ids) throws Exception {
        PartsRdp rdp = null;
        // 终止操作
        if (PartsRdp.STATUS_YZZ.equals(flag)) {
            for (String id : ids) {
                updateTerminationPlan(id);
            }
        } else if (PartsRdp.STATUS_JXHG.equals(flag)) { // 验收操作
            for (String id : ids) {
                rdp = getModelById(id);// 查询任务单
                if (null == rdp) {
                    throw new BusinessException("数据错误，未查询到配件检修作业计划信息！");
                }
                // 配件检修作业合格验收
                this.update2Acceptance(rdp);
            }
        } else if (PartsRdp.STATUS_JXZ.equals(flag)) { // 返修操作
            for (String id : ids) {
                rdp = getModelById(id);// 查询任务单
                if (rdp == null) {
                    continue;
                }
                rdp.setStatus(PartsRdp.STATUS_JXZ);// 状态更新为【检修中】
                rdp.setRepairResultDesc("");
                rdp.setRealEndTime(null);
                this.saveOrUpdate(rdp);
            }
        }
    }
    
    /**
     * <li>说明：配件检修作业合格验收
     * <li>创建人：何涛
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdp 配件检修作业实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void update2Acceptance(PartsRdp rdp) throws BusinessException, NoSuchFieldException {
        rdp.setStatus(PartsRdp.STATUS_JXHG);            // 状态更新为【检修合格】
        
        // Modified by hetao on 2016-04-11 在配件合格验收时，自动设置当前系统操作人员为验收人
        OmEmployee emp = SystemContext.getOmEmployee();
        if (null == emp) {
            throw new BusinessException("系统错误，无法获取当前系统操作人员信息！");
        }
        rdp.setAcceptanceEmpID(emp.getEmpid());         // 验收人
        rdp.setAcceptanceEmpName(emp.getEmpname());     // 验收人名称
        
        this.saveOrUpdate(rdp);
        try {
            partsRdpQRManager.updateTerminateQCResult(rdp.getIdx());
        } catch (Exception e) {
            throw new BusinessException("终止未处理的质检项时发生异常！");
        }
        List<PartsRdpRecordCard> rcList = this.partsRdpRecordCardManager.getModels(rdp.getIdx());
        this.partsRdpRecordCardManager.updateStatus(rcList, IPartsRdpStatus.CONST_STR_STATUS_XJ);
        
        PartsAccount partsAccount = this.partsAccountManager.getModelById(rdp.getPartsAccountIDX());
        if (null == partsAccount) {
            throw new BusinessException("数据错误，未查询到配件周转信息！");
        }
        // 配件状态为【检修中】状态，配件检修作业“合格验收”后，设置配件状态为“良好不在库”
        if (partsAccount.getPartsStatus().contains(PartsAccount.PARTS_STATUS_JXZ)) {
            //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
            this.updatePartsAccountStatus(partsAccount, PartsAccount.PARTS_STATUS_LH, PartsAccount.PARTS_STATUS_LH_CH);
        }
    }
    
	/**
	 * <li>说明：配件检修质量检查 查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-23
	 * <li>修改人：何涛
	 * <li>修改日期：2015-01-13
	 * <li>修改内容：增加分页查询功能，暂时未实现页面排序
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-21
     * <li>修改内容：1、增加查询字段：下车车型（unloadTrainType），组合匹配下车车型、下车车号，
     *             2、修改查询字段：名称规格型号（specificationModel），组合匹配配件规格型号、配件名称
	 * @param searchEntity 查询对象实体
	 * @param checkWay 检验方式“抽检[1]/必检[2]”
	 * @param qcContet 质量检验项，多个质量检查项则已逗号“,”进行分隔
	 * @return Page<PartsRdpSearcher> 配件检修质量检查实体集合
	 */
	@SuppressWarnings("unchecked")
	public Page<PartsRdpSearcher> findPartRdpQCItems(SearchEntity<PartsRdp> searchEntity, String checkWay, String qcContet) {
		String sql = SqlMapUtil.getSql("pjjx-partsRdp:findPartRdpQCItems");
		
		// 权限限制，只查询当前登录用户可检查的工单
		Long empId = SystemContext.getOmEmployee().getEmpid();
		sql = sql.replace("?", String.valueOf(empId));
		
		StringBuilder sb = new StringBuilder(sql);
		// 只查询当前登录人员可检查的质量检查项
		sb.append(" AND T.QC_ITEM_NO IN (SELECT QC_ITEM_NO FROM PJJX_Parts_Rdp_QC_Participant WHERE RECORD_STATUS = 0 AND QC_EmpID = ").append(empId).append(Constants.BRACKET_R);
		
		// 查询条件 - 抽检\必检
        if (!StringUtil.isNullOrBlank(checkWay))
            sb.append(" AND T.CHECK_WAY ='").append(checkWay).append(Constants.SINGLE_QUOTE_MARK);
		
		// 查询条件 - 质量检查项
		if (!StringUtil.isNullOrBlank(qcContet)) {
			sb.append(" AND T.QC_ITEM_NO IN('").append(qcContet.replace(",", "','")).append(Constants.SINGLE_QUOTE_MARK).append(Constants.BRACKET_R);
		}
		
		PartsRdp rdp = searchEntity.getEntity();
        final String temp = "%'";
		// 查询条件 - 下车车型主键
		if(!StringUtil.isNullOrBlank(rdp.getUnloadTrainTypeIdx())) {
			sb.append(" AND T.UNLOAD_TRAINTYPE_IDX LIKE '%").append(rdp.getUnloadTrainTypeIdx()).append(temp);
		}
		// 查询条件 - 下车车型车号
		if(!StringUtil.isNullOrBlank(rdp.getUnloadTrainNo())) {
			sb.append(" AND T.UNLOAD_TRAINNO LIKE '%").append(rdp.getUnloadTrainNo()).append(temp);
		}
		// 查询条件 - 下车修程主键
		if(!StringUtil.isNullOrBlank(rdp.getUnloadRepairClassIdx())) {
			sb.append(" AND T.UNLOAD_REPAIR_CLASS_IDX LIKE '%").append(rdp.getUnloadRepairClassIdx()).append(temp);
		}
		// 查询条件 - 配件规格型号主键
		if(!StringUtil.isNullOrBlank(rdp.getPartsTypeIDX())) {
			sb.append(" AND T.PARTS_TYPE_IDX LIKE '%").append(rdp.getPartsTypeIDX()).append(temp);
		}
		// 查询条件 - 配件规格型号
		if(!StringUtil.isNullOrBlank(rdp.getSpecificationModel())) {
			sb.append(" AND LOWER(T.PARTS_NAME || T.SPECIFICATION_MODEL) Like '%").append(rdp.getSpecificationModel().toLowerCase()).append("%'");
		}
         // 查询条件 - 下车车型号
        if (!StringUtil.isNullOrBlank(rdp.getUnloadTrainType())) {
            sb.append(" AND LOWER(T.UNLOAD_TRAINTYPE || T.UNLOAD_TRAINNO) Like '%").append(rdp.getUnloadTrainType().toLowerCase()).append("%'");
        }
		// 配件识别码和配件编号使用同一个字段进行匹配时的处理
		if(!StringUtil.isNullOrBlank(rdp.getPartsNo()) && !StringUtil.isNullOrBlank(rdp.getIdentificationCode())) {
            sb.append("AND (");
            sb.append("T.PARTS_NO LIKE '%").append(rdp.getPartsNo()).append(temp);
            sb.append(" OR T.IDENTIFICATION_CODE LIKE '%").append(rdp.getIdentificationCode()).append(temp);
            sb.append(" OR T.PARTS_ACCOUNT_IDX IN (SELECT PARTS_ACCOUNT_IDX FROM PJWZ_PARTS_ACCOUNT A, PJJX_RECORDCODE_BIND B WHERE A.IDX = B.PARTS_ACCOUNT_IDX AND B.RECORD_CODE LIKE '%").append(rdp.getIdentificationCode()).append(temp).append(")");
            sb.append(")");
		}
		// 查询条件 - 配件名称
		if(!StringUtil.isNullOrBlank(rdp.getPartsName())) {
			sb.append(" AND T.PARTS_NAME LIKE '%").append(rdp.getPartsName()).append(temp);
		}
		sb.append(" ORDER BY T.PARTS_NAME, T.PARTS_NO");
		sql = sb.toString();
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
        return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpSearcher.class);
	}
    
    /**
     * <li>说明：获取配件检修质量检验代办项记录总数（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param checkWay 检验方式，抽检：1，必检2；
     * @return 代办项记录总数
     */
    public int getTotalCount(Integer checkWay) {
        String sql = SqlMapUtil.getSql("pjjx-partsRdp:findPartRdpQCItems");
        
        // 权限限制，只查询当前登录用户可检查的工单
        Long empId = SystemContext.getOmEmployee().getEmpid();
        sql = sql.replace("?", String.valueOf(empId));
        
        StringBuilder sb = new StringBuilder(sql);
        // 只查询当前登录人员可检查的质量检查项
        sb.append(" AND QC_ITEM_NO IN (SELECT QC_ITEM_NO FROM PJJX_Parts_Rdp_QC_Participant WHERE RECORD_STATUS = 0 AND QC_EmpID = ").append(empId).append(Constants.BRACKET_R);
        
        if (null != checkWay) {
            // 查询条件 - 抽检\必检
            sb.append(" AND CHECK_WAY ='").append(checkWay).append(Constants.SINGLE_QUOTE_MARK);
        }
        
        String query = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        List list = this.daoUtils.executeSqlQuery(query);
        if (null != list && list.size() > 0) {
            return ((BigDecimal)list.get(0)).intValue();
        }
        return -1;
    }
    
	/**
	 * <li>该作业下有“领活”操作发生时，将该作业的“实际开始时间”字段设置为“领活”操作发生时的实际时间
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 配件检修作业主键
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void updateRealStartTime(String idx) throws BusinessException, NoSuchFieldException {
		PartsRdp entity = this.getModelById(idx);
		if (null == entity) {
			throw new BusinessException("数据异常-未查询到【配件检修作业节点】对象 - idx[" + idx + "]");
		}
		this.updateRealStartTime(entity);
	}
	
	/**
	 * <li>该作业节点下有“领活”操作发生时，将该作业节点的“实际开始时间”字段设置为“领活”操作发生时的实际时间
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 配件检修作业实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void updateRealStartTime(PartsRdp entity) throws BusinessException, NoSuchFieldException {
		if (null != entity.getRealStartTime()) {
			return;
		}
		// 设置“实际开始时间”为当前时间
		entity.setRealStartTime(Calendar.getInstance().getTime());
		this.saveOrUpdate(entity);
	}
	
	/**
	 * <li>更新【配件检修作业】的状态为“已处理”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 配件检修作业实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	private void updateFinishedStatus(PartsRdp entity) throws BusinessException, NoSuchFieldException {
		entity.setRealEndTime(Calendar.getInstance().getTime());			// 设置状态为“已处理”
		entity.setStatus(PartsRdpNode.CONST_STR_STATUS_YCL); 				// 设置实际结束时间为当前时间
		this.saveOrUpdate(entity);
	}
	
	/**
	 * <li>更新【配件检修作业】的状态为“已处理”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx  配件检修作业主键
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void updateFinishedStatus(String idx) throws BusinessException, NoSuchFieldException {
		PartsRdp entity = this.getModelById(idx);
		if (null == entity) {
			throw new BusinessException("数据异常-未查询到【配件检修作业】对象 - idx[" + idx + "]");
		}
		this.updateFinishedStatus(entity);
	}
    
	/**
	 * <li>说明：查询当前人员的配件检验项列表-移动终端调用
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-7
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param checkWay  检验类型  抽检/必检（1：抽检；2：必检）
	 * @return 配件检验项列表
	 * @throws BusinessException
	 */
	public Page<PartsRdpSearcher> getPartsRdpSearcherList(Integer checkWay) throws BusinessException{
		List<QCItem> itemList = QCItemManager.getQCContent(null);
		String qcContent = ""  ; //定义包含的检验项目
		for(QCItem item : itemList){
			if(checkWay == item.getCheckWay()){
				if("".equals(qcContent)){
					qcContent = item.getQCItemNo() ;
				}else{
					qcContent += "," + item.getQCItemNo(); 
				}
			}
		}
		SearchEntity<PartsRdp> searchEntity = new SearchEntity<PartsRdp>();
		searchEntity.setStart(0);
		searchEntity.setLimit(10000);
		searchEntity.setEntity(new PartsRdp());
		Page<PartsRdpSearcher> pageList = findPartRdpQCItems(searchEntity, checkWay +"", qcContent);
		return pageList;
	}
    
    /**
     * <li>说明：配件检修任务单-启动生产
     * <li>创建人：程锐
     * <li>创建日期：2015-10-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 配件检修任务单IDX
     * @throws Exception
     */
	public void updateStartPartsRdp(String idx) throws Exception{
        PartsRdp rdp = getModelById(idx);
        rdp.setRealStartTime(new Date());
        rdp.setStatus(PartsRdp.STATUS_JXZ);
        saveOrUpdate(rdp);
	    //    查询需激活的作业节点
        List<PartsRdpNode> list = partsRdpNodeManager.getFirstLevelRdpNode(idx, "ROOT_0");
        //循环激活作业节点
        for(PartsRdpNode node : list){
            partsRdpNodeManager.startUp(node.getIdx());
        }
    }
    
    /**
     * <li>方法说明：编辑保存计划
     * <li>方法名：updatePartsRdpPlan
     * @param rdp 兑现单
     * @throws Exception 
     */
    public void updatePartsRdpPlan(PartsRdp rdp) throws Exception{
    	PartsRdp t = getModelById(rdp.getIdx());
        updateRdpForTime(rdp, t);
    	t.setCalendarIdx(rdp.getCalendarIdx());
    	t.setCalendarName(rdp.getCalendarName());
    	t.setRepairOrgID(rdp.getRepairOrgID());
    	t.setRepairOrgName(rdp.getRepairOrgName());
    	t.setRepairOrgSeq(rdp.getRepairOrgSeq());
    	saveOrUpdate(t);
    }
    
    /**
     * <li>说明：编辑作业计划-根据前台的计划开始时间或日历IDX，更新配件检修作业计划的计划完成时间及流程节点的计划开完工时间
     * <li>创建人：程锐
     * <li>创建日期：2015-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 前台页面传递的配件检修作业计划对象
     * @param rdp 数据库已存的作业计划对象
     * @throws Exception
     */
    public void updateRdpForTime(PartsRdp entity, PartsRdp rdp) throws Exception{
        Date planBeginTime = entity.getPlanStartTime();
        String workCalendarIDX = entity.getCalendarIdx();
        if (!(DateUtil.yyyy_MM_dd_HH_mm_ss.format(rdp.getPlanStartTime())).equals(DateUtil.yyyy_MM_dd_HH_mm_ss.format(planBeginTime))
            || !workCalendarIDX.equals(rdp.getCalendarIdx())) { 
            if (!workCalendarIDX.equals(rdp.getCalendarIdx())) {
                //更新节点的工作日历
                String hql = "update PartsRdpNode set calendarIdx = ? where recordStatus = 0 and rdpIDX = ?";
                daoUtils.execute(hql, new Object[] {workCalendarIDX, rdp.getIdx()});
            }
            rdp.setPlanStartTime(planBeginTime);
            rdp.setCalendarIdx(entity.getCalendarIdx());
            saveOrUpdate(rdp);
            
            calcPartsNodeWorkDateManager.updatePlanTimeByWorkPlan(rdp, true);
            daoUtils.flush();
            updateWorkPlanBeginEndTime(rdp);
        }
    }
    
    /**
     * <li>说明：根据下级节点的最小开工时间和最大完工时间更新作业计划的计划开完工时间、计划工期
     * <li>创建人：程锐
     * <li>创建日期：2015-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 配件作业计划对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateWorkPlanBeginEndTime(PartsRdp workPlan) throws Exception {
        List<Object[]> list = partsRdpNodeQueryManager.getMinBeginAndMaxEndTimeByNode(workPlan.getIdx());        
        for (Object[] obj : list) {
            if (obj[0] == null || obj[1] == null)
                return;
            Date start = DateUtil.yyyy_MM_dd_HH_mm_ss.parse(obj[0].toString());
            Date end = DateUtil.yyyy_MM_dd_HH_mm_ss.parse(obj[1].toString());
            WorkCalendarInfoUtil wcInfoUtil = WorkCalendarInfoUtil.getInstance(start);
            wcInfoUtil.buildMap();
            long realWorkmunutes = workCalendarDetailManager.getRealWorkminutes(start, end, workPlan.getCalendarIdx());
            Double ratedWorkMinutes = Double.valueOf(realWorkmunutes / (60 * 60 * 1000));
            String ratedWorkDay = String.valueOf(new BigDecimal(ratedWorkMinutes.toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
            workPlan.setRatedPeriod(Double.valueOf(ratedWorkDay));
            workPlan.setPlanStartTime(start);
            workPlan.setPlanEndTime(end);
            saveOrUpdate(workPlan);
        }
    }
    
    /**
     * <li>方法说明： 终止任务单
     * <li>方法名：updateTerminationPlan
     * @param rdpIDX 任务单主键
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-23
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws Exception 
     */
    public void updateTerminationPlan(String rdpIDX) throws Exception{
    	
    	long operator = SystemContext.getAcOperator().getOperatorid();
    	Date date = new Date();    	
    	String hql = "update PartsRdp set status = ?, updator = ?, updateTime = ? where idx = ?";
    	daoUtils.execute(hql, PartsRdp.STATUS_YZZ, operator, date, rdpIDX);
    	//作业节点
    	hql = "update PartsRdpNode set status = ?, updator = ?, updateTime = ? where rdpIDX = ?";
    	daoUtils.execute(hql, PartsRdpNode.CONST_STR_STATUS_YZZ, operator, date, rdpIDX);
    	//检修工艺工单
    	hql = "update PartsRdpTecCard set status = ?, updator = ?, updateTime = ? where rdpIDX = ?";
    	daoUtils.execute(hql, PartsRdpTecCard.STATUS_YZZ, operator, date, rdpIDX);
    	//检修记录工单
    	hql = "update PartsRdpRecordCard set status = ?, updator = ?, updateTime = ? where rdpIDX = ?";
    	daoUtils.execute(hql, PartsRdpRecordCard.STATUS_YZZ, operator, date, rdpIDX);
		//检修提票单
    	hql = "update PartsRdpNotice set status = ?, updator = ?, updateTime = ? where rdpIDX = ?";
    	daoUtils.execute(hql, PartsRdpRecordCard.STATUS_YZZ, operator, date, rdpIDX);
		partsRdpQRManager.updateTerminateQCResult(rdpIDX);
        PartsRdp partsRdp = getModelById(rdpIDX);
        if (partsRdp == null)
            return;
        if (StringUtil.isNullOrBlank(partsRdp.getPartsAccountIDX()))
            return;
        PartsAccount partsAccount = partsAccountManager.getModelById(partsRdp.getPartsAccountIDX());
        if (partsAccount == null)
            return;
        PartsManageLog log = partsManageLogManager.getLogByIdx(partsRdp.getPartsAccountIDX(), rdpIDX);
        if (log == null)
            return;
        //配件状态为【检修中】状态，配件检修作业“终止”后，设置配件状态为“配件信息日志表”中记录的状态
        if (partsAccount != null && partsAccount.getPartsStatus().contains(PartsAccount.PARTS_STATUS_JXZ)) {
//            updatePartsAccountStatus(partsAccount, log.getPartsStatusHis(), log.getPartsStatusNameHis());
            partsAccount = partsManageLogManager.getAccountFromLog(log, partsAccount);
            partsAccount.setPartsStatusUpdateDate(Calendar.getInstance().getTime()); // 配件状态更新时间为当前时间
            this.partsAccountManager.saveOrUpdate(partsAccount);
            partsManageLogManager.deleteLogByEventIdx(partsRdp.getIdx());
        }
    }
    
    /**
     * <li>说明：获取配件检修作业统计信息，包含【检修记录】、【作业工单】、【回修提票】的处理情况
     * <li>创建人：何涛
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修作业主键
     * @return 配件检修统计信息，数据结构如下： 
     * {
     *      recordCard: {
     *          wcl: 2, 
     *          zjz: 1, 
     *          ycl: 0
     *      },
     *      tecCard: {
     *          wcl: 4, 
     *          ycl: 2
     *      },
     *      notice: {
     *          wcl: 1, 
     *          ycl: 0
     *      }
     * }
     * @throws IOException
     */
    public Map<String, Map<String, Integer>> getStatisticInfo(String rdpIDX) {
        Map<String, Map<String, Integer>> map = new HashMap<String, Map<String,Integer>>();
        
        // 统计检修记录处理情况
        Map<String, Integer> childMap = new HashMap<String, Integer>();
        // 未处理检修记录，含（未开放、待领取、待处理）；
        int count = this.partsRdpRecordCardManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_WCL, "");
        childMap.put(IPartsRdpStatus.STATUS_WCL, count);
        // 质检中检修记录，含（质量检验中）；
        count = this.partsRdpRecordCardManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_ZJZ, "");
        childMap.put(IPartsRdpStatus.STATUS_ZJZ, count);
        count = this.partsRdpRecordCardManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_YCL, "");
        // 已处理检修记录，含（修竣）；
        childMap.put(IPartsRdpStatus.STATUS_YCL, count);
        map.put("recordCard", childMap);
        
        // 统计作业工单处理情况
        childMap = new HashMap<String, Integer>();
        // 未处理作业工单，含（未开放、待领取、待处理）；
        count = this.partsRdpTecCardManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_WCL, "");
        childMap.put(IPartsRdpStatus.STATUS_WCL, count);
        // 已处理作业工单，含（修竣）；
        count = this.partsRdpTecCardManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_YCL, "");
        childMap.put(IPartsRdpStatus.STATUS_YCL, count);
        map.put("tecCard", childMap);
        
        // 统计回修提票处理情况
        childMap = new HashMap<String, Integer>();
        // 未处理回修提票，含（未开放、待领取、待处理）；
        count = this.partsRdpNoticeManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_WCL, "");
        childMap.put(IPartsRdpStatus.STATUS_WCL, count);
        // 已处理作业工单，含（修竣）；
        count = this.partsRdpNoticeManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_YCL, "");
        childMap.put(IPartsRdpStatus.STATUS_YCL, count);
        map.put("notice", childMap);
        
        return map;
    }
    
    /**
     * <li>方法说明：接口兑现时构造兑现单信息
     * <li>方法名：getEntityByWpIdxAndPaIdx
     * @param t 配件检修任务单实体
     * @return 兑现单
     * @throws Exception
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-29
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public PartsRdp getEntityByWpIdxAndPaIdx(PartsRdp t) throws Exception{
    	PartsAccount parts = partsAccountManager.getModelById(t.getPartsAccountIDX());
    	if(parts == null) throw new RuntimeException("未能找到相关配件信息");
    	WP wp = wPManager.getModelById(t.getWpIDX());
        if(wp == null) throw new RuntimeException("未能找到相关需求单");
    	t.setPartsTypeIDX(parts.getPartsTypeIDX());
    	t.setPartsName(parts.getPartsName());
    	t.setPartsNo(parts.getPartsNo());
        t.setNameplateNo(parts.getNameplateNo());//铭牌号
    	t.setUnloadRepairClass(parts.getUnloadRepairClass());
    	t.setUnloadRepairClassIdx(parts.getUnloadRepairClassIdx());
    	t.setUnloadTrainNo(parts.getUnloadTrainNo());
    	t.setUnloadTrainType(parts.getUnloadTrainType());
    	t.setUnloadTrainTypeIdx(parts.getUnloadTrainTypeIdx());
    	t.setUnloadRepairTime(parts.getUnloadRepairTime());
    	t.setUnloadRepairTimeIdx(parts.getUnloadRepairTimeIdx());
        Date planBeginTime = new Date();
        if (t.getPlanStartTime() != null)
            planBeginTime = t.getPlanStartTime();
    	t.setPlanStartTime(planBeginTime);
    	t.setWpDesc(wp.getWPDesc());
    	t.setWpIDX(wp.getIdx());
    	t.setWpNo(wp.getWPNo());
    	OmOrganization org = SystemContext.getOmOrganization();
    	t.setRepairOrgID(org.getOrgid());
    	t.setRepairOrgName(org.getOrgname());
    	t.setRepairOrgSeq(org.getOrgseq());
        t.setIdentificationCode(parts.getIdentificationCode());
    	t.setSpecificationModel(parts.getSpecificationModel());
//        t.setMatCode(parts.getMatCode());
		return t;
    }
    
    /**
     * <li>说明：更新配件作业计划的下级节点中无子节点的节点的【是否子节点】为【是】
     * <li>创建人：程锐
     * <li>创建日期：2015-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件作业计划IDX
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void updateNodeLeaf(String rdpIDX) throws BusinessException, NoSuchFieldException {
    	List<PartsRdpNode> parentNodeList = partsRdpNodeQueryManager.getParentNodeListByRdp(rdpIDX);
    	List<PartsRdpNode> updateList = new ArrayList<PartsRdpNode>();
    	for (PartsRdpNode node : parentNodeList) {
    		List<PartsRdpNode> childList = partsRdpNodeQueryManager.getDirectChildren(rdpIDX, node.getWpNodeIDX());
    		if (childList == null || childList.size() < 1) {
    			node.setIsLeaf(WPNode.CONST_INT_IS_LEAF_YES);
    			updateList.add(node);
    		}    			
		}
    	partsRdpNodeQueryManager.saveOrUpdate(updateList);
    }
    
    /**
     * <li>说明：根据配件拆卸登记情况查询配件检修作业任务树
     * <li>创建人：何涛
     * <li>创建日期：2016-1-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级配件idx主键
     * @return 配件检修作业任务树数据源
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> tree(String parentIDX) throws IOException {
        List<PartsRdp> entityList = null;
        StringBuilder sb = new StringBuilder("From PartsRdp Where recordStatus = 0");
        if (null != parentIDX && !"ROOT_0".equals(parentIDX)) {
            sb.append(" And partsAccountIDX In (");
            sb.append(" Select partsAccountIDX From PartsDismantleRegister Where recordStatus = 0 And rdpIdx = '").append(parentIDX).append("'");
            sb.append(" )");
        }
        sb.append(" Order By identificationCode");
        entityList = this.daoUtils.find(sb.toString());
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = null;
        for (PartsRdp rdp : entityList) {
            map = new HashMap<String, Object>();
            map.put("id", rdp.getIdx());
            map.put("text", rdp.getPartsName() + "(" + rdp.getSpecificationModel() + "_" + rdp.getIdentificationCode() +")");
            String hql = "From PartsDismantleRegister Where recordStatus = 0 And rdpIdx = '" + rdp.getIdx() + "'";
            int count = this.daoUtils.getCount(hql);
            map.put("leaf", count > 0 ? false : true);
            map.put("entity", JSONUtil.write(rdp));
            list.add(map);
        }
        return list;
    }

    /**
     * <li>方法说明：查询配件检修结果查询List
     * <li>创建人： 林欢
     * <li>创建日期：2016-5-12
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * 
     * @throws JsonMappingException
     * @throws IOException
     */
    public Page<PartsRdpBean> pagePartsRdpRdpInfoQuery(SearchEntity<PartsRdpBean> searchEntity) throws SecurityException, NoSuchFieldException {
        StringBuffer sb =new StringBuffer();
        sb.append(" from (select a.*,b.UNLOAD_Place from PJJX_Parts_Rdp a,PJWZ_PARTS_ACCOUNT b where a.PARTS_ACCOUNT_IDX = b.idx) t where 1=1");
        PartsRdpBean rdp = searchEntity.getEntity();
        
        // 下车车型
        if (!StringUtil.isNullOrBlank(rdp.getUnloadTrainTypeIdx())) {
            sb.append(" AND t.UNLOAD_TRAINTYPE_IDX = '").append(rdp.getUnloadTrainTypeIdx()).append("'");
        }
        // 下车车号
        if (!StringUtil.isNullOrBlank(rdp.getUnloadTrainNo())) {
            sb.append(" AND t.UNLOAD_TRAINNO LIKE '%").append(rdp.getUnloadTrainNo()).append(Constants.LIKE_PIPEI);
        }
        // 下车修程
        if (!StringUtil.isNullOrBlank(rdp.getUnloadRepairClassIdx())) {
            sb.append(" AND t.UNLOAD_REPAIR_CLASS_IDX = '").append(rdp.getUnloadRepairClassIdx()).append("'");
        }
        // 配件编号
        if (!StringUtil.isNullOrBlank(rdp.getPartsNo())) {
            sb.append(" AND t.PARTS_NO LIKE '%").append(rdp.getPartsNo()).append(Constants.LIKE_PIPEI);
        }
        // 规格型号
        if (!StringUtil.isNullOrBlank(rdp.getSpecificationModel())) {
            sb.append(" AND t.SPECIFICATION_MODEL = '").append(rdp.getSpecificationModel()).append("'");
        }
        // 识别码
        if (!StringUtil.isNullOrBlank(rdp.getIdentificationCode())) {
            sb.append(" AND t.IDENTIFICATION_CODE LIKE '%").append(rdp.getIdentificationCode()).append(Constants.LIKE_PIPEI);
        }
        //下车位置
        if (!StringUtil.isNullOrBlank(rdp.getUnloadPlace())) {
            sb.append(" AND t.UNLOAD_Place LIKE '%").append(rdp.getUnloadPlace()).append(Constants.LIKE_PIPEI);
        }
        
        
//      拼接排序参数
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            String dir = order[1];
            Class clazz = PartsRdpBean.class;
            Field field = clazz.getDeclaredField(sort);
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY t.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY t.").append(sort).append(" ").append(dir);
            }
        }
        
        StringBuffer totalSql = new StringBuffer(" SELECT COUNT(*) AS ROWCOUNT ").append(sb.toString());
        StringBuffer sql = new StringBuffer(" select t.* ").append(sb.toString());
        
        return super.queryPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpBean.class);
    }

    /**
     * <li>说明：通过配件编号查询配件兑现单信息
     * <li>创建人：张迪
     * <li>创建日期：2016-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsNo 配件编号
     * @return 兑现单
     */
    public PartsRdp findPartsRdpByPartsNo(String partsNo) {
        StringBuilder sb = new StringBuilder("From PartsRdp where recordStatus = 0 ");
        sb.append(" And partsNo ='").append(partsNo).append("'");
        return this.findSingle(sb.toString());
    }
   
}