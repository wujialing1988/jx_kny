package com.yunda.jx.pjjx.partsrdp.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpBean;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.manager.PartsRdpNoticeManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecCardManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：任务单查询业务类
 * <li>创建人：程梅
 * <li>创建日期：2014-12-05
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsRdpQueryManager")
public class PartsRdpQueryManager extends JXBaseManager<PartsRdp, PartsRdp> {
    
    /** 操作类型 - 查询到多个待修配件 */
    public static final int OPERATE_TYPE_0 = 0;
    
    /** 操作类型 - 配件任务处理 */
    public static final int OPERATE_TYPE_1 = 1;
    
    /** 操作类型 - 生成配件检修任务 */
    public static final int OPERATE_TYPE_2 = 2;
    
    /** 操作类型 - 启动生产 */
    public static final int OPERATE_TYPE_3 = 3;
    
	/** PartsRdpTecCard业务类,配件检修工艺工单 */
	@Resource
	private PartsRdpTecCardManager partsRdpTecCardManager;
	
	/** PartsRdpRecordCard业务类,配件检修记录卡实例 */
	@Resource
	private PartsRdpRecordCardManager partsRdpRecordCardManager;
	
	/** PartsRdpNotice业务类,提票单 */
	@Resource
	private PartsRdpNoticeManager partsRdpNoticeManager;

    /**
     * <li>说明：查询任务单列表【派工情况】
     * <li>创建人：程梅
     * <li>创建日期：2014-12-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件
     * @return Page 分页对象
     * @throws 抛出异常列表
     */
    public Page findPartsRdpList(SearchEntity<PartsRdp> searchEntity) throws BusinessException {
        StringBuffer sb =
            new StringBuffer(
                "SELECT T.*, A.WORKNAMESTR FROM PJJX_PARTS_RDP T LEFT JOIN (SELECT W.RDP_IDX, TO_CHAR(WM_CONCAT(W.WORK_EMPNAME)) AS WORKNAMESTR " +
                "FROM PJJX_PARTS_RDP_WORKER W WHERE W.RECORD_STATUS=0 GROUP BY W.RDP_IDX) A ON T.IDX = A.RDP_IDX WHERE T.RECORD_STATUS = 0");
        // 查询检修中的记录
        sb.append(" AND T.STATUS = ").append(PartsRdp.STATUS_JXZ);
        // 查询检修班组为当前登录者所在机构的信息
        sb.append(" AND T.REPAIR_ORGID = ").append(SystemContext.getOmOrganization().getOrgid());
        PartsRdp rdp = searchEntity.getEntity();
        // 下车车型
        if (!StringUtil.isNullOrBlank(rdp.getUnloadTrainTypeIdx())) {
            sb.append(" AND T.UNLOAD_TRAINTYPE_IDX = '").append(rdp.getUnloadTrainTypeIdx()).append("'");
        }
        // 下车车号
        if (!StringUtil.isNullOrBlank(rdp.getUnloadTrainNo())) {
            sb.append(" AND T.UNLOAD_TRAINNO LIKE '%").append(rdp.getUnloadTrainNo()).append(Constants.LIKE_PIPEI);
        }
        // 下车修程
        if (!StringUtil.isNullOrBlank(rdp.getUnloadRepairClassIdx())) {
            sb.append(" AND T.UNLOAD_REPAIR_CLASS_IDX = '").append(rdp.getUnloadRepairClassIdx()).append("'");
        }
        // 配件编号
        if (!StringUtil.isNullOrBlank(rdp.getPartsNo())) {
            sb.append(" AND T.PARTS_NO LIKE '%").append(rdp.getPartsNo()).append(Constants.LIKE_PIPEI);
        }
        // 规格型号
        if (!StringUtil.isNullOrBlank(rdp.getSpecificationModel())) {
            sb.append(" AND T.SPECIFICATION_MODEL = '").append(rdp.getSpecificationModel()).append("'");
        }
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        return super.findPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), null, searchEntity.getOrders());
    }
    
    /**
     * <li>说明：根据配件识别码获取配件编号查询配件检修作业实体、或者配件周转台账实体对象
     * <li>创建人：何涛
     * <li>创建日期：2015-10-29
     * <li>修改人：何涛
     * <li>修改日期：2016-01-13
     * <li>修改内容：增加【配件检修记录识别码绑定】功能后，可以根据已绑定的其它“配件识别码”查询配件信息
     * <li>修改人：何涛
     * <li>修改日期：2016-03-18
     * <li>修改内容：删除无用的operatorId参数，增加使用配件信息主键作为查询条件的处理
     * @param partsRdp 用于封装查询条件的检修作业对象实体，形如：
     * { 
	 *		"partsAccountIDX": "8a8284f250e9e6430150ea3931340030"
     *      "identificationCode": "13134345", 
     *      "partsNo": "ox89245" 
     * }
     * @return 如果该配件正在检修，则返回检修作业实体对象，否则返回可能存在的配件周转台账实体对象
     */
    private Object findPartsProObject(PartsRdp partsRdp) {
        // ****************** 1 首先查询是否有该配件识别码或者配件编号对应的配件正在做检修的
        StringBuilder sb = new StringBuilder("From PartsRdp Where recordStatus = 0 And status in (?, ?, ?)");
        String identificationCode = partsRdp.getIdentificationCode();	// 配件识别码
        String partsNo = partsRdp.getPartsNo();							// 配件编号
        String partsAccountIDX = partsRdp.getPartsAccountIDX();			// 配件信息主键
        if (!StringUtil.isNullOrBlank(partsAccountIDX)) {
            sb.append(" And partsAccountIDX = '").append(partsAccountIDX).append("'");
        }
        // 查询条件 - 配件识别码及配件编号
        // Modified by hetao on 2016-01-13 修改可以根据已绑定的“配件识别码”查询配件
        if (!StringUtil.isNullOrBlank(identificationCode)) {
            sb.append(" And (");
            sb.append(" identificationCode Like '%").append(identificationCode).append(Constants.LIKE_PIPEI);
            sb.append(" Or partsAccountIDX In (").append("Select B.partsAccountIdx From PartsAccount A, RecordCodeBind B WHERE A.idx = B.partsAccountIdx AND B.recordCode LIKE '%").append(identificationCode).append(Constants.LIKE_PIPEI).append(Constants.BRACKET_R);
            sb.append(Constants.BRACKET_R);
        }
        if (!StringUtil.isNullOrBlank(partsNo)) {
            sb.append(" and partsNo Like '%").append(partsNo).append(Constants.LIKE_PIPEI);
        }
        // 如果有对应的配件检修作业，则返回配件检修作业实体对象
        Object object = this.daoUtils.findSingle(sb.toString(), new Object[] { PartsRdp.STATUS_WQD, PartsRdp.STATUS_JXZ, PartsRdp.STATUS_DYS });
        if (null != object) {
            return object;
        }
        
        // ****************** 2 如果没有则继续查询是否该该配件识别码或者配件编号进行检修
        sb = new StringBuilder("From PartsAccount Where recordStatus = 0");
        if (!StringUtil.isNullOrBlank(partsAccountIDX)) {
            sb.append(" And idx = '").append(partsAccountIDX).append("'");
        }
        // Modified by hetao on 2016-01-14 将待修状态的限制放在hql查询条件中
        sb.append(" And partsStatus = '").append(PartsAccount.PARTS_STATUS_DX).append("'");
        sb.append(" And manageDeptType = '").append(PartsAccount.MANAGE_DEPT_TYPE_ORG).append("'");
        // 查询条件 - 配件识别码及配件编号
        // Modified by hetao on 2016-01-13 修改可以根据已绑定的“配件识别码”查询配件
        if (!StringUtil.isNullOrBlank(identificationCode)) {
            sb.append(" And (");
            sb.append(" identificationCode Like '%").append(identificationCode).append(Constants.LIKE_PIPEI);
            sb.append(" Or idx In (").append("Select B.partsAccountIdx From PartsAccount A, RecordCodeBind B WHERE A.idx = B.partsAccountIdx AND B.recordCode LIKE '%").append(identificationCode).append(Constants.LIKE_PIPEI).append(Constants.BRACKET_R);
            sb.append(Constants.BRACKET_R);
        }
        if (!StringUtil.isNullOrBlank(partsNo)) {
            sb.append(" and partsNo Like '%").append(partsNo).append(Constants.LIKE_PIPEI);
        }
        int count = this.daoUtils.getCount(sb.toString());
        if (count > 1) {
            return OPERATE_TYPE_0;
        }
        object = this.daoUtils.findSingle(sb.toString());
        return null == object ? null : object;
    }
    
    /**
     * <li>说明：根据“配件识别码”或者“配件编号”获取配件当前的可操作类型及相关信息（配件检修作业信息、配件周转台账信息）
     * <li>创建人：何涛
     * <li>创建日期：2015-12-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param args 可能的“识别码”或者“配件编号”
     * @return 配件当前的可操作类型及相关信息（配件检修作业信息、配件周转台账信息），形如：{
     *      type: "2", 
     *      partsRdp: {}, // 配件检修作业信息 
     *      partsAccount: {} // 配件周转台账信息 
     * }
     */
    public Map<String, Object> findPartsProMap(String args) {
        PartsRdp partsRdp = new PartsRdp();
        // 根据“识别码"查询实体
        partsRdp.setIdentificationCode(args);
        Object object = this.findPartsProObject(partsRdp);
        // 如果“识别码”不能查询到实体，则再以“配件编号”查询
        if (null == object) {
            partsRdp.setIdentificationCode(null);
            partsRdp.setPartsNo(args);
            object = this.findPartsProObject(partsRdp);
        }
        if (null == object) {
            throw new BusinessException("没有查询到编号为：" + args + "的待修配件！");
        }
        return this.getPartsProMap(object);
    }
    
    
    /**
     * <li>说明：根据配件识别码获取配件当前的可操作类型及相关信息（配件检修作业信息、配件周转台账信息）（工位终端）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-29
     * <li>修改人：何涛
     * <li>修改日期：2016-03-18
     * <li>修改内容：删除无用的operatorId参数
     * @param partsRdp 用于封装查询条件的检修作业对象实体，形如：
     * { 
     *      "identificationCode": "13134345", 
     *      "partsNo": "ox89245" 
     * }
     * @return 配件当前的可操作类型及相关信息（配件检修作业信息、配件周转台账信息），形如：{
     *      type: "2", 
     *      partsRdp: {}, // 配件检修作业信息 
     *      partsAccount: {} // 配件周转台账信息 
     * }
     */
    public Map<String, Object> findPartsProMap(PartsRdp partsRdp) {
        Object object = this.findPartsProObject(partsRdp);
        //如果前台输入的识别码不存在，应该返回null
        if (object == null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", null);
            return map;
        }
        return getPartsProMap(object);
    }

    /**
     * <li>说明：根据配件识别码获取配件当前的可操作类型及相关信息（配件检修作业信息、配件周转台账信息）
     * <li>创建人：何涛
     * <li>创建日期：2015-12-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param object 配件检修作业实体、或者配件周转台账实体
     * @return 配件当前的可操作类型及相关信息（配件检修作业信息、配件周转台账信息），形如：{
     *      type: "2", 
     *      partsRdp: {}, // 配件检修作业信息 
     *      partsAccount: {} // 配件周转台账信息 
     * }
     */
    private Map<String, Object> getPartsProMap(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        PartsRdp partsRdp;
        final String type = "type";
        if (object instanceof PartsRdp) {
            partsRdp = (PartsRdp) object;
            // 配件任务处理
            if (PartsRdp.STATUS_JXZ.equals(partsRdp.getStatus()) 
                || PartsRdp.STATUS_DYS.equals(partsRdp.getStatus())) {
                map.put(type, PartsRdpQueryManager.OPERATE_TYPE_1);
            }
            // 启动生产
            if (PartsRdp.STATUS_WQD.equals(partsRdp.getStatus())) {
                map.put(type, PartsRdpQueryManager.OPERATE_TYPE_3);
            }
            map.put("partsRdp", partsRdp);
            
            // 查询配件信息台账
            PartsAccountManager partsAccountManager = (PartsAccountManager) Application.getSpringApplicationContext().getBean("partsAccountManager");
            map.put("partsAccount", partsAccountManager.getModelById(partsRdp.getPartsAccountIDX()));
        } else if (object instanceof PartsAccount) {
            // 生成配件检修任务
            map.put("type", PartsRdpQueryManager.OPERATE_TYPE_2);
            map.put("partsAccount", object);
        } else if (OPERATE_TYPE_0 == (Integer)object) {
            // 查询到多个待修配件
            map.put("type", PartsRdpQueryManager.OPERATE_TYPE_0);
        }
        return map;
    }
    
    /**
     * <li>说明：根据“配件识别码”或者“配件编号”获取配件作业计划实体
     * <li>创建人：何涛
     * <li>创建日期：2015-10-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param partsRdp partsRdp 用于封装查询条件的检修作业对象实体，形如：
     * { 
     *      "identificationCode": "13134345", 
     *      "partsNo": "ox89245" 
     * }
     * @return 配件作业计划实体
     */
    public PartsRdp getRdpForQC(PartsRdp partsRdp) {
        StringBuilder sb = new StringBuilder("From PartsRdp Where recordStatus = 0 And status in (?)");
        // 查询条件 - 配件识别码
        if (!StringUtil.isNullOrBlank(partsRdp.getIdentificationCode())) {
            sb.append(" And identificationCode = '").append(partsRdp.getIdentificationCode()).append("'");
        }
        // 查询条件 - 配件编号
        if (!StringUtil.isNullOrBlank(partsRdp.getPartsNo())) {
            sb.append(" And partsNo = '").append(partsRdp.getPartsNo()).append("'");
        }
        return (PartsRdp) daoUtils.findSingle(sb.toString(), new Object[] { PartsRdp.STATUS_JXZ });
    }
    
    /**
     * <li>说明：根据配件识别码获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param identityCode 配件识别码
     * @return 配件作业计划
     */
    @SuppressWarnings("unchecked")
    public PartsRdp getRdpByIdentityCode(String identityCode) {
         List<PartsRdp> list = getRdpListByIdentityCode(identityCode);
         if (list == null || list.size() < 1)
             return null;
         return list.get(0);
    }
    
    /**
     * <li>说明：根据配件编号获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param partsNo partsNo
     * @return 配件作业计划
     */
    @SuppressWarnings("unchecked")
    public PartsRdp getRdpByPartsNo(String partsNo) {
         List<PartsRdp> list = getRdpListByPartsNo(partsNo);
         if (list == null || list.size() < 1)
             return null;
         return list.get(0);
    }
    
    /**
     * <li>说明：根据配件识别码获取合格验收的配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param identityCode 配件识别码
     * @return 配件作业计划
     */
    @SuppressWarnings("unchecked")
    public PartsRdp getHgysRdpByIdentityCode(String identityCode) {
    	if (StringUtil.isNullOrBlank(identityCode)) {
			throw new NullPointerException("配件识别码为空");
		 }
         List<PartsRdp> list = getHgysRdpListByIdentityCode(identityCode);
         if (list == null || list.size() < 1)
             return null;
         return list.get(0);
    }
    
    /**
     * <li>说明：根据配件编号获取合格验收的配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param partsNo partsNo
     * @return 配件作业计划
     */
    @SuppressWarnings("unchecked")
    public PartsRdp getHgysRdpByPartsNo(String partsNo) {
    	 if (StringUtil.isNullOrBlank(partsNo)) {
			throw new NullPointerException("配件编号为空");
		 }
         List<PartsRdp> list = getHgysRdpListByPartsNo(partsNo);
         if (list == null || list.size() < 1)
             return null;
         return list.get(0);
    }
    
    /**
     * <li>说明：根据配件编号获取合格验收的配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-27
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：代码重构
     * @param partsNo partsNo
     * @return 配件作业计划列表
     */
    @SuppressWarnings("unchecked")
    private List<PartsRdp> getHgysRdpListByPartsNo(String partsNo) {
        String hql = "From PartsRdp Where recordStatus = 0 And partsNo = ? And status = ?";
        return this.daoUtils.find(hql, new Object[]{ partsNo, PartsRdp.STATUS_DYS });
    }
    
    /**
     * <li>说明：根据识别码获取合格验收的配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-27
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：修改可以使用绑定的多个记录单识别码进行查询
     * @param identityCode 配件识别码
     * @return 配件作业计划
     */
    @SuppressWarnings("unchecked")
    private List<PartsRdp> getHgysRdpListByIdentityCode(String identityCode) {
        StringBuilder sb = new StringBuilder("From PartsRdp Where recordStatus = 0");
        sb.append(" And (");
        sb.append(" identificationCode Like '%").append(identityCode).append("%'");
        sb.append(" Or");
        sb.append(" partsAccountIDX In (Select B.partsAccountIdx From PartsAccount A, RecordCodeBind B WHERE A.idx = B.partsAccountIdx AND B.recordCode LIKE '%").append(identityCode).append("%')");
        sb.append(" )");
        sb.append(" And status = '").append(PartsRdp.STATUS_DYS).append("'");
        return this.daoUtils.find(sb.toString());
    }
    
    /**
     * <li>说明：根据配件编号获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：代码重构
     * @param partsNo partsNo
     * @return 配件作业计划列表
     */
    @SuppressWarnings("unchecked")
    private List<PartsRdp> getRdpListByPartsNo(String partsNo) {
        String hql = "From PartsRdp Where recordStatus = 0 And partsNo = ?";
        return this.daoUtils.find(hql, new Object[]{ partsNo });
    }
    
    /**
     * <li>说明：根据识别码获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：修改可以使用绑定的多个记录单识别码进行查询
     * @param identityCode 配件识别码
     * @return 配件作业计划
     */
    @SuppressWarnings("unchecked")
    private List<PartsRdp> getRdpListByIdentityCode(String identityCode) {
        StringBuilder sb = new StringBuilder("From PartsRdp Where recordStatus = 0");
        sb.append(" And (");
        sb.append(" identificationCode Like '%").append(identityCode).append("%'");
        sb.append(" Or");
        sb.append(" partsAccountIDX In (Select partsAccountIdx From RecordCodeBind Where recordCode Like '%").append(identityCode).append("%')");
        sb.append(" )");
        return this.daoUtils.find(sb.toString());
    }
    
    /**
     * <li>说明：分页查询，联合查询配件的下车（配件）位置
     * <li>创建人：何涛
     * <li>创建日期：2016-3-11
     * <li>修改人：何涛
     * <li>修改日期：2016-03-21
     * <li>修改内容：1、增加查询字段：下车车型（unloadTrainType），组合匹配下车车型、下车车号，
                   2、增加查询字段：名称规格型号（specificationModel），组合匹配配件规格型号、配件名称
     * @param searchEntity 包装了实体类查询条件的对象
     * @return 分页查询列表
     * @throws BusinessException
     */
    public Page<PartsRdpBean> queryPageList(SearchEntity<PartsRdp> searchEntity) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT A.*, B.UNLOAD_PLACE FROM PJJX_PARTS_RDP A, PJWZ_PARTS_ACCOUNT B WHERE A.RECORD_STATUS = 0 AND B.RECORD_STATUS = 0 AND A.PARTS_ACCOUNT_IDX = B.IDX");
        PartsRdp rdp = searchEntity.getEntity();
        //配件任务进度查看
        if (StringUtil.isNullOrBlank(rdp.getStatus())) {
            sb.append(" AND A.STATUS IN ('");
            sb.append(PartsRdp.STATUS_WQD).append("','");               // 未启动
            sb.append(PartsRdp.STATUS_JXZ).append("','");               // 检修中
            sb.append(PartsRdp.STATUS_DYS);   
            sb.append("')");
        } 
        //查询配件合格验收
        else {
            sb.append(" AND STATUS = '").append(rdp.getStatus()).append("'");
        }
        // 配件识别码和配件编号使用同一个字段进行匹配时的处理
        if(!StringUtil.isNullOrBlank(rdp.getPartsNo()) && !StringUtil.isNullOrBlank(rdp.getIdentificationCode())) {
            sb.append(" AND (A.PARTS_NO LIKE '%").append(rdp.getPartsNo()).append(Constants.LIKE_PIPEI);
            sb.append(" OR (A.IDENTIFICATION_CODE LIKE '%").append(rdp.getIdentificationCode()).append(Constants.LIKE_PIPEI);
            sb.append(" OR PARTS_ACCOUNT_IDX IN (SELECT PARTS_ACCOUNT_IDX FROM PJJX_RECORDCODE_BIND WHERE RECORD_CODE LIKE '%").append(rdp.getIdentificationCode()).append("%')");
            sb.append("))");
        }
        // 查询条件 - 下车车型号
        if (!StringUtil.isNullOrBlank(rdp.getUnloadTrainType())) {
            sb.append(" AND LOWER(A.UNLOAD_TRAINTYPE || A.UNLOAD_TRAINNO) Like '%").append(rdp.getUnloadTrainType().toLowerCase()).append("%'");
        }
        // 查询条件 - 名称规格型号
        if (!StringUtil.isNullOrBlank(rdp.getSpecificationModel())) {
            sb.append(" AND LOWER(A.PARTS_NAME || A.SPECIFICATION_MODEL) Like '%").append(rdp.getSpecificationModel().toLowerCase()).append("%'");
        }
        // 查询条件 - 查询当前工位
        if (!StringUtil.isNullOrBlank(rdp.getWorkStationIDX())) {
            sb.append(" AND A.idx IN (SELECT N.Rdp_IDX FROM PJJX_PARTS_RDP_NODE N, PJJX_PARTS_RDP_NODE_STATION S WHERE N.IDX = S.RDP_NODE_IDX AND S.RECORD_STATUS = 0 AND S.IDX = '").append(rdp.getWorkStationIDX()).append("')");
        }
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpBean.class);
    }
    
    /**
     * <li>说明：获取配件检修任务单分页列表（工位终端、PDA、PAD调用配件合格验收的列表查询,工位终端的配件进度查看的列表查询）
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：何涛
     * <li>修改日期：2016-01-19
     * <li>修改内容：修改配件检修进度查询可以查询“未启动”，“检修中”和“待验收”的作业任务
     * <li>修改人：何涛
     * <li>修改日期：2016-03-10
     * <li>修改内容：重新该方法是为了在分页查询后，向配件检修作业设置配件的下车（配件）位置，新增方法queryPageList可以更好的实现该功能，因此不再推荐使用该方法
     * @param searchEntity 包装了实体类查询条件的对象
     * @return 分页查询列表
     * @throws BusinessException
     */
    @Override
    @Deprecated
    public Page<PartsRdp> findPageList(SearchEntity<PartsRdp> searchEntity) throws BusinessException {
        Page<PartsRdpBean> pageQuery = this.queryPageList(searchEntity);
        Page<PartsRdp> page = new Page<PartsRdp>();
        page.setTotal(pageQuery.getTotal());
        List<PartsRdp> list = new ArrayList<PartsRdp>();
        PartsRdp rdp = null;
        for (PartsRdpBean b : pageQuery.getList()) {
            rdp = new PartsRdp();
            try {
                BeanUtils.copyProperties(rdp, b);
            } catch (Exception e) {
                throw new BusinessException("对象属性赋值异常！");
            }
            list.add(rdp);
        }
        page.setList(list);
        return page;
    }
    
    /**
     * <li>说明：查询配件检修计划数量
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询包装类对象
     * @return 配件检修计划数量
     */
    public int queryRdpCount(SearchEntity<PartsRdp> searchEntity) {
        StringBuilder sb = new StringBuilder();        
        sb.append("From PartsRdp Where recordStatus = 0");
        PartsRdp rdp = searchEntity.getEntity();
        // 只查询状态为“检修中”的配件检修作业
        // Modified by hetao on 2016-01-19 修改配件检修进度查询可以查询“未启动”，“检修中”和“待验收”的作业任务
        if (StringUtil.isNullOrBlank(rdp.getStatus())) {
            sb.append(" And status In ('");
            sb.append(PartsRdp.STATUS_WQD).append("','");               // 未启动
            sb.append(PartsRdp.STATUS_JXZ).append("','");               // 检修中
            sb.append(PartsRdp.STATUS_DYS);                             // 待验收
            sb.append("')");
        } else {
            sb.append(" And status = '").append(rdp.getStatus()).append("'");
        }
        
        if(!StringUtil.isNullOrBlank(rdp.getPartsNo()) && !StringUtil.isNullOrBlank(rdp.getIdentificationCode())) {
            sb.append(" and (partsNo like '%").append(rdp.getPartsNo()).append(Constants.LIKE_PIPEI);
            sb.append(" or (identificationCode like '%").append(rdp.getIdentificationCode()).append(Constants.LIKE_PIPEI);
            sb.append(" or partsAccountIDX in (select partsAccountIdx from RecordCodeBind where recordCode like '%").append(rdp.getIdentificationCode()).append("%')");
            sb.append("))");
        }
        return this.daoUtils.getCount(sb.toString());
    }
    
    /**
     * <li>说明：获取配件检修计划中未处理的工单数量
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修计划IDX
     * @param rdpNodeIDX 配件检修节点IDX
     * @return 配件检修计划中未处理的工单数量
     */
    public Map<String, Integer> getWCLCountByIDX(String rdpIDX, String rdpNodeIDX) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        // 未处理检修记录，含（未开放、待领取、待处理）；
        int count = this.partsRdpRecordCardManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_WCL, rdpNodeIDX);
        map.put("recordCard", count);  
        
        // 未处理作业工单，含（未开放、待领取、待处理）；
        count = this.partsRdpTecCardManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_WCL, rdpNodeIDX);        
        map.put("tecCard", count);
        
        // 未处理回修提票，含（未开放、待领取、待处理）；
        count = this.partsRdpNoticeManager.getCount(rdpIDX, IPartsRdpStatus.STATUS_WCL, "");
        map.put("notice", count);        
        return map;
    }
    
}
