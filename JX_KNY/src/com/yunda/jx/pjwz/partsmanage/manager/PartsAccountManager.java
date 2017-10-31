package com.yunda.jx.pjwz.partsmanage.manager;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpQueryManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccountBean;
import com.yunda.zb.tp.entity.ZbglTp;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsAccount业务类，配件周转台账---配件信息
 * <li>创建人：程梅
 * <li>创建日期：2014-5-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsAccountManager")
public class PartsAccountManager extends JXBaseManager<PartsAccount, PartsAccount> {
    
	/** 业务字典选择控件业务类 */
    @Resource
	private EosDictEntrySelectManager eosDictEntrySelectManager;
    
    @Resource
    private PartsRdpQueryManager partsRdpQueryManager;
    
    
    /** LIKE匹配模式 - 左匹配，例如： %1234 */
    private static final String LIKE_MODE_LEFT = "LEFT_LIKE";
    
    /** LIKE匹配模式 - 右匹配，例如： 1234% */
    private static final String LIKE_MODE_RIGHT = "RIGHT_LIKE";
    
    /** LIKE匹配模式 - 全匹配，例如： %1234% */
    private static final String LIKE_MODE_ALL = "ALL_LIKE";
	
	/**
	 * 
	 * <li>说明：交旧后新增配件周转台账信息
	 * <li>创建人：程梅
	 * <li>创建日期：2014-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 * TODO 20140610汪东良：需要将此方法调整到下车配件交旧相关业务类中，不应该在配置周转台账中依赖，配件下车交旧相关业务实体类
	 */
//	public List<OldPartsWHDetail> saveFromOldPartsWH(OldPartsWH oldPartsWH,OldPartsWHDetail[] detailList) throws BusinessException, NoSuchFieldException {
//		PartsAccount account ;
//		List<OldPartsWHDetail> detailList_v = new ArrayList<OldPartsWHDetail>();
//		try {
//			for(OldPartsWHDetail detail : detailList){
//				account = new PartsAccount();
//				BeanUtils.copyProperties(account, oldPartsWH);
//				BeanUtils.copyProperties(account, detail);
//				account = EntityUtil.setSysinfo(account);
//				//设置逻辑删除字段状态为未删除
//				account = EntityUtil.setNoDelete(account);
//				account.setManageDeptId(oldPartsWH.getWhIdx());
//				account.setManageDept(oldPartsWH.getWhName());//责任部门为接收库房
//				account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_WH);//责任部门类型为库房
//				account.setManageDeptOrgseq("");//责任部门序列为空
//				account.setPartsStatusUpdateDate(oldPartsWH.getWhTime());//配件状态更新时间为交件日期
//				account.setPartsStatus(PartsAccount.PARTS_STATUS_DXZK);//配件状态为待修在库
////				account.setPartsStatusName("待修在库");
//				account.setPartsStatusName(this.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DXZK, "待修在库"));
//				account.setIsNewParts("旧");//是否新品---旧
//				if(detail.getWhLocationName() == null){
//					account.setLocation(oldPartsWH.getWhName());//存放地点=库房
//				}else account.setLocation(oldPartsWH.getWhName()+"("+detail.getWhLocationName()+")");//存放地点=库房+库位
//				account.setIdx(null);
//				account.setCreateTime(new Date());
//				this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
//				detail.setPartsAccountIDX(account.getIdx());
//				detailList_v.add(detail);
//			}
//			
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//		
//		return detailList_v;
//	}
    
	/**
     * <li>说明：根据配件编号+库房+配件状态查询配件信息，用于配件领旧、配件上车领用
     * <li>创建人：程梅
     * <li>创建日期：2014-5-20
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：重构
     * @param partsNo 配件编号
     * @param whIDX 库存主键
     * @param partsStatus 配件状态
     * @return 配件周转台账集合
     */
    @SuppressWarnings("unchecked")
    public List<PartsAccount> getPartsAccountByPartsNoAndWH(String partsNo, String whIDX, String partsStatus) {
        PartsAccount entity = new PartsAccount();
        entity.setPartsNo(partsNo);
        entity.setManageDeptId(whIDX);
        entity.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_WH); // 责任部门类型：库房
        entity.setPartsStatus(partsStatus);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("partsStatus", LIKE_MODE_ALL);
        return this.findByEntity(entity, map);
    }
    
	/**
     * <li>说明：根据配件编号在配件信息表中找到相应的配件
     * <li>创建人：王斌
     * <li>创建日期：2014-5-19
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：重构
     * @param partsNo 配件编号
     * @param partsStatus 配件状态
     * @return 配件周转台账实体
     */
	@SuppressWarnings("unchecked")
    public PartsAccount findPartsForNo(String partsNo, String partsStatus) {
        String hql = "ROM PartsAccount p where p.recordStatus=0 and partsNo ='" + partsNo + "' and p.partsStatus in " + partsStatus;
        return (PartsAccount) this.daoUtils.findSingle(hql);
	}
    
	/**
	 * <li>说明：根据配件编号+配件状态查询配件信息，用于修竣配件入库【状态为自修包含的所有配件】或配件编号唯一性验证【状态为在册】
	 * <li>创建人：程梅
	 * <li>创建日期：2014-5-26
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：重构
     * @param partsNo 配件编号
     * @param partsStatus 配件状态
     * @return 配件周转台账集合
	 */
	@SuppressWarnings("unchecked")
    public List<PartsAccount> getPartsAccountByPartsNo(String partsNo, String partsStatus){
        PartsAccount entity = new PartsAccount();
        entity.setPartsNo(partsNo);
        entity.setPartsStatus(partsStatus);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("partsStatus", PartsAccountManager.LIKE_MODE_RIGHT);
        return this.findByEntity(entity, map);
	}
    
	/**
	 * <li>说明：根据配件状态值查询在业务字典中对应的配件状态名称
	 * <li>创建人：程梅
	 * <li>创建日期：2014-6-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
     * @param dictTypeId 业务字典类型标示
     * @param dictid 业务字典ID
     * @param defaultName 没有查询到时的默认返回值
	 * @return 字典名称
	 * @throws BusinessException
	 */
	public String getPartsStatusName(String dictTypeId, String dictid, String defaultName) throws BusinessException {
        EosDictEntry entry = null;
        try {
            entry = eosDictEntrySelectManager.getEosDictEntry(dictTypeId, dictid);
        } catch (Exception e) {
            return defaultName;
        }
        return null == entry ? defaultName : entry.getDictname();
    }
     
	/**
	 * 
	 * <li>说明：新品检验责任部门树列表
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param params 查询参数
	 * @return 配件周转责任部门树
	 */
	@SuppressWarnings("unchecked")
    public List<HashMap> deptTree(Map<String, String> params) {
		StringBuilder sb = new StringBuilder("select distinct t.manage_dept_id, t.manage_dept from PJWZ_PARTS_ACCOUNT t where t.record_status = 0");
		// 列出状态为“新品试验【0104】”的所有周转件中的责任部门
        //去掉了新品试验状态
//		sb.append(" and t.parts_status = '" + PartsAccount.PARTS_STATUS_XPSY_CODE + "'");
		String sql = sb.toString();
		List<Object[]> list = this.daoUtils.executeSqlQuery(sql);
		List<HashMap> children = new ArrayList<HashMap>();
		HashMap<String, Object> nodeMap = null;
		for (Object[] objects : list) {
			nodeMap = new HashMap<String, Object>();
			// 责任部门id
            nodeMap.put("manageDeptId", StringUtil.nvl(objects[0]));
            // 责任部门
            nodeMap.put("text", StringUtil.nvl(objects[1]));
            // 界面显示格式字符
            nodeMap.put("leaf", true);
            children.add(nodeMap);
		}
		return children;
	}

	/**
	 * <li>说明：分页查询，联合查询配件“物料编码”和新品出库的“领件日期”
	 * <li>创建人：何涛
	 * <li>创建日期：2016-03-10
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * @param searchEntity 查询实体
     * @return 分页列表
     * @throws BusinessException
	 */
	public Page<PartsAccountBean> queryPageList(SearchEntity<PartsAccount> searchEntity) throws BusinessException {
        PartsAccount entity = searchEntity.getEntity();
	    StringBuilder sb = new StringBuilder("SELECT");
        sb.append(" T.*,");
        sb.append(" (select a.mat_code from pjwz_new_parts_out_wh_detail a, pjwz_new_parts_out_account b where a.record_status = 0 and a.idx = b.new_parts_out_wh_idx and b.parts_account_idx = t.idx ) as \"MAT_CODE\",");
        sb.append(" (select c.get_date from pjwz_new_parts_out_wh c where c.idx = (select new_parts_out_wh_idx from pjwz_new_parts_out_wh_detail where idx = (select new_parts_out_wh_idx from pjwz_new_parts_out_account where parts_account_idx = t.idx))) as \"GET_DATE\"");
        sb.append(" FROM");
        sb.append(" PJWZ_PARTS_ACCOUNT T");
        sb.append(" WHERE");
        sb.append(" T.RECORD_STATUS = 0");
        if (!StringUtil.isNullOrBlank(entity.getPartsStatus())) {
            sb.append(" AND T.PARTS_STATUS = '").append(entity.getPartsStatus()).append("'");
        }
        sb.append(" ORDER BY T.UPDATE_TIME DESC");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsAccountBean.class);
	}
	
	/**
     * <li>说明：查询配件信息用于退库【根据上车日期倒序排序】
     * <li>创建人：程梅
     * <li>创建日期：2014-9-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param partsNo 配件编号
	 * @param whIDX 库房主键
	 * @param partsStatus 配件状态
	 * @param getOrgId 零件班组id
	 * @return 配件信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsAccount> getPartsAccountByForBack(String partsNo,String whIDX,String partsStatus,String getOrgId){
		String from = SqlMapUtil.getSql("pjwl-query:findYsxList_from");
		StringBuffer whereStr = new StringBuffer(" and p.parts_no like '%").append(partsNo).append("%' and t.wh_idx like '%").append(whIDX);
		whereStr.append("%' and p.parts_status = ").append(partsStatus).append(" and t.get_org_id like '%").append(getOrgId).append("%'"); 
		StringBuffer sql = new StringBuffer("select p.* ").append(from).append(whereStr).append(" order by m.aboard_date desc");
		List<PartsAccount> list = (List<PartsAccount>)daoUtils.executeSqlQueryEntity(sql.toString(),PartsAccount.class);
		return list;
	}
    /**
     * 
     * <li>说明：查询自修目录配件中检修班组为当前班组并且不在检修中的配件,用于兑现（工位终端）
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @param repairOrgId 检修班组id
     * @param isNewParts 是否新品
     * @param specificationModel 规格型号
     * @return Page 配件分页对象
     * @throws Exception
     */
    public Page getListForPartsRdp(SearchEntity<PartsAccount> searchEntity,String repairOrgId,String isNewParts,String specificationModel) throws Exception {   
        PartsAccount entity = searchEntity.getEntity();
        StringBuffer fromSql = new StringBuffer(" select t.* From PJWZ_PARTS_ACCOUNT t where t.record_Status=0 ")
        .append("and t.idx not in (select nvl(PARTS_ACCOUNT_IDX,'0') from PJJX_Parts_Rdp where Record_Status=0 ")
        .append("and status in ('").append(PartsRdp.STATUS_WQD).append("','"+PartsRdp.STATUS_JXZ).append("','").append(PartsRdp.STATUS_DYS)
        .append("')) and t.Parts_Type_IDX in (select Parts_Type_IDX from PJWZ_Parts_Repair_List where Record_Status = 0 and Repair_OrgID='")
        .append(repairOrgId).append("')");
        StringBuilder multyAwhere = new StringBuilder();
        //下车车型
        if (!StringUtil.isNullOrBlank(entity.getUnloadTrainTypeIdx())) {
            multyAwhere.append(" AND t.UNLOAD_TRAINTYPE_IDX = '").append(entity.getUnloadTrainTypeIdx()).append("' ");
        }
        //下车车号
        if (!StringUtil.isNullOrBlank(entity.getUnloadTrainNo())) {
            multyAwhere.append(" AND t.UNLOAD_TRAINNO like'%").append(entity.getUnloadTrainNo()).append("%' ");
        }
        //下车修程
        if (!StringUtil.isNullOrBlank(entity.getUnloadRepairClassIdx())) {
            multyAwhere.append(" AND t.UNLOAD_Repair_Class_IDX = '").append(entity.getUnloadRepairClassIdx()).append("' ");
        }
        //配件状态
        if (!StringUtil.isNullOrBlank(entity.getPartsStatus())) {
            multyAwhere.append(" AND t.Parts_Status like '").append(entity.getPartsStatus()).append("%' ");
        }else multyAwhere.append(" AND t.Parts_Status like '").append(PartsAccount.PARTS_STATUS_ZC).append("%' "); //默认查询在册状态
        //配件编号
        if (!StringUtil.isNullOrBlank(entity.getPartsNo())) {
            multyAwhere.append(" AND t.Parts_NO like '%").append(entity.getPartsNo()).append("%' ");
        }
        //规格型号
        if (!StringUtil.isNullOrBlank(specificationModel)) {
            multyAwhere.append(" AND t.Specification_Model like '%").append(specificationModel).append("%' ");
        }
        //责任部门id
        if (!StringUtil.isNullOrBlank(repairOrgId)) {
            multyAwhere.append(" AND t.MANAGE_DEPT_ID like '%").append(repairOrgId).append("%' ");
        }
        //是否新品
        if (!StringUtil.isNullOrBlank(isNewParts)) {
            multyAwhere.append(" AND t.IS_NEW_PARTS in (").append(isNewParts).append(")");
        }
        String querySql = fromSql.append(multyAwhere).toString();
        String totalSql = "select count(1) " + querySql.substring(querySql.indexOf("From"));
        Page<ZbglTp> page = findPageList(totalSql, querySql, searchEntity.getStart(), searchEntity.getLimit() , null ,searchEntity.getOrders());
        return page;
        
    }
    
    /**
     * <li>说明：根据配件识别码获取配件信息
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 何涛
     * <li>修改日期：2016-01-15
     * <li>修改内容：修改可以根据已绑定的配件检修记录单识别码查询配件周转信息
     * @param idCode 配件识别码
     * @return 配件信息
     */
    public PartsAccount getAccountByIdCode(String idCode) {
        StringBuilder sb = new StringBuilder("From PartsAccount where recordStatus = 0");
        sb.append(" And partsStatus Like '").append(PartsAccount.PARTS_STATUS_ZC).append("%'");
        sb.append(" And (");
            sb.append(" identificationCode ='").append(idCode).append("'");
            sb.append(" Or idx In (");
                sb.append("Select B.partsAccountIdx From PartsAccount A, RecordCodeBind B Where A.idx = B.partsAccountIdx And B.recordCode ='").append(idCode).append("'");
            sb.append(")");
        sb.append(" )");
        return this.findSingle(sb.toString());
    }
    
    /**
     * <li>说明：根据配件编号获取配件信息
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsNo 配件编号
     * @return 配件信息
     */
    public PartsAccount getAccountByPartsNo(String partsNo) {
        List<PartsAccount> list = this.getPartsAccountByPartsNo(partsNo, PartsAccount.PARTS_STATUS_ZC);
        return null == list || list.size() <= 0 ? null : list.get(0);
    }
    
    /**
     * <li>方法说明：查询待修配件
     * <li>方法名：findAwaitRepairParts
     * @param partsType 配件型号
     * @param partsNo 配件编号
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-22
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    @SuppressWarnings("unchecked")
	public List<PartsAccount> findAwaitRepairParts(String partsType, String partsNo){
        PartsAccount entity = new PartsAccount();
        entity.setPartsNo(partsNo);
        entity.setPartsTypeIDX(partsType);
        entity.setPartsStatus(PartsAccount.PARTS_STATUS_DX);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("partsStatus", PartsAccountManager.LIKE_MODE_RIGHT);
        return this.findByEntity(entity, map);
    }
    
    /**
     * <li>说明：根据过滤条件查询最新的配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人： 何涛
     * <li>修改日期： 2016-01-15
     * <li>修改内容： 修改可以根据已绑定的配件检修记录单识别码查询配件周转信息
     * <li>修改人： 何涛
     * <li>修改日期： 2016-03-25
     * <li>修改内容： 将调用该该方法时的查询参数验证，放在该方法里统一处理
     * @param account 查询实体
     * @return PartsAccount 实体对象
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getAccount(PartsAccount account) {
        if (StringUtil.isNullOrBlank(account.getPartsNo()) && StringUtil.isNullOrBlank(account.getSpecificationModel())
            && StringUtil.isNullOrBlank(account.getIdentificationCode())) {
//            throw new BusinessException("操作失败，未指定查询参数！");  
             // 不抛出异常 返回空，否则某些配件没有识别码，首先通过识别码只有配件编号
            return null ;
        }
        StringBuffer hql = new StringBuffer("From PartsAccount Where recordStatus = 0 ");
        if(!StringUtil.isNullOrBlank(account.getPartsNo())){
            hql.append(" And partsNo ='").append(account.getPartsNo()).append("'");
        }
        if(!StringUtil.isNullOrBlank(account.getSpecificationModel())){
            hql.append(" And specificationModel ='").append(account.getSpecificationModel()).append("'");
        }
        if(!StringUtil.isNullOrBlank(account.getIdentificationCode())){
            hql.append(" And (");
                hql.append(" identificationCode ='").append(account.getIdentificationCode()).append("'");
                hql.append(" Or idx In (");
                    hql.append("Select B.partsAccountIdx From PartsAccount A, RecordCodeBind B Where A.idx = B.partsAccountIdx And B.recordCode ='").append(account.getIdentificationCode()).append("'");
                hql.append(")");
            hql.append(")");
        }
        if(!StringUtil.isNullOrBlank(account.getPartsStatus())){
            hql.append(" And partsStatus Like '").append(account.getPartsStatus()).append("%'");
        }
        hql.append(" Order By updateTime Desc");
        return (PartsAccount) this.daoUtils.findSingle(hql.toString());
    }
    /**
     * 
     * <li>说明：根据过滤条件查询配件周转台账信息列表
     * <li>创建人：程梅
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param account 查询实体
     * @return List<PartsAccount> 台账列表
     */
    @SuppressWarnings("unchecked")
    public List<PartsAccount> getAccountList(PartsAccount account) {
        if (StringUtil.isNullOrBlank(account.getPartsNo()) && StringUtil.isNullOrBlank(account.getSpecificationModel())
            && StringUtil.isNullOrBlank(account.getIdentificationCode())) {
            throw new BusinessException("操作失败，未指定查询参数！");
        }
        StringBuffer hql = new StringBuffer("From PartsAccount Where recordStatus = 0 ");
        if(!StringUtil.isNullOrBlank(account.getPartsNo())){
            hql.append(" And partsNo ='").append(account.getPartsNo()).append("'");
        }
        if(!StringUtil.isNullOrBlank(account.getSpecificationModel())){
            hql.append(" And specificationModel ='").append(account.getSpecificationModel()).append("'");
        }
        if(!StringUtil.isNullOrBlank(account.getIdentificationCode())){
            hql.append(" And (");
                hql.append(" identificationCode ='").append(account.getIdentificationCode()).append("'");
                hql.append(" Or idx In (");
                    hql.append("Select B.partsAccountIdx From PartsAccount A, RecordCodeBind B Where A.idx = B.partsAccountIdx And B.recordCode ='").append(account.getIdentificationCode()).append("'");
                hql.append(")");
            hql.append(")");
        }
        if(!StringUtil.isNullOrBlank(account.getPartsStatus())){
            hql.append(" And partsStatus Like '").append(account.getPartsStatus()).append("%'");
        }
        hql.append(" Order By updateTime Desc");
        return this.daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：根据过滤条件查询最新的配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人： 何涛
     * <li>修改日期： 2016-01-15
     * <li>修改内容： 修改可以根据已绑定的配件检修记录单识别码查询配件周转信息
     * @param account 查询实体
     * @param statusHql 状态过滤hql语句
     * @return PartsAccount 实体对象
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getAccountByStatus(PartsAccount account, String statusHql) {
        StringBuffer hql = new StringBuffer("From PartsAccount Where recordStatus = 0 ");
        if(!StringUtil.isNullOrBlank(account.getPartsNo())){
            hql.append(" And partsNo='").append(account.getPartsNo()).append("' ");
        }
        if(!StringUtil.isNullOrBlank(account.getSpecificationModel())){
            hql.append(" And specificationModel='").append(account.getSpecificationModel()).append("' ");
        }
        if(!StringUtil.isNullOrBlank(account.getIdentificationCode())){
            hql.append(" And (");
            hql.append(" identificationCode ='").append(account.getIdentificationCode()).append("'");
            hql.append(" Or idx In (");
                hql.append("Select B.partsAccountIdx From PartsAccount A, RecordCodeBind B Where A.idx = B.partsAccountIdx And B.recordCode ='").append(account.getIdentificationCode()).append("'");
            hql.append(")");
            hql.append(")");
        }
        //拼接状态过滤
        if(!StringUtil.isNullOrBlank(statusHql)){
            hql.append(statusHql);
        }
        hql.append(" Order By updateTime Desc");
        return this.findSingle(hql.toString());
    }
    /**
     * 
     * <li>说明：查询在册配件列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件封装实体
     * @return 配件周转台账集合
     * @throws BusinessException
     */
    public Page<PartsAccount> findPageListByStatus(SearchEntity<PartsAccount> searchEntity) throws BusinessException {
        StringBuilder sb = new StringBuilder(" From PartsAccount Where recordStatus = 0 and partsStatus like '").append(PartsAccount.PARTS_STATUS_ZC).append("%' ");
        PartsAccount account = searchEntity.getEntity() ;
        String identificationCode = account.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (partsNo like '%").append(identificationCode).append("%' or partsName like '%").append(identificationCode)
            .append("%' or specificationModel like '%").append(identificationCode).append("%' or manageDept like '%").append(identificationCode)
            .append("%' or identificationCode like '%").append(identificationCode).append("%' or partsStatusName like '%").append(identificationCode).append("%' )") ;
        }
        // 排序字段
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            awhere.append(" Order By ");
            awhere.append(orders[0].toString());
            for (int i = 1; i < orders.length; i++) {
                awhere.append(", ");
                awhere.append(orders[i].toString());
            }
        } else {
            // 设置默认的排序字段
            awhere.append(" Order By partsStatusUpdateDate DESC");
        }
        sb.append(awhere);
        String totalHql = "select count(*) " + sb;
        Page<PartsAccount> page = super.findPageList(totalHql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit());
        return page;
    }
    
    /**
     * <li>说明： 配件识别码绑定
     * <li>创建人： 程梅
     * <li>创建日期： 2015-11-5
     * <li>修改人： 何涛
     * <li>修改日期： 2016-01-16
     * <li>修改内容： 修改可以根据已绑定的配件检修记录单识别码查询配件周转信息
     * @param account 保存信息实体
     * @return 操作异常信息，无异常则返回null
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public String[] updateAccountForBinding(PartsAccount account) throws BusinessException, NoSuchFieldException  {
        PartsAccount entity = this.getAccountByIdCode(account.getIdentificationCode());
        if (null != entity) {
            if (!account.getIdx().equals(entity.getIdx())) {
                return new String[]{ "识别码【"+account.getIdentificationCode()+"】已存在，不能重复绑定！" };
            } else {
                return null;
            }
        }
        PartsAccount tempEntity = this.getModelById(account.getIdx());
        tempEntity.setIdentificationCode(account.getIdentificationCode());
        this.saveOrUpdate(tempEntity);
        //程锐修改 2016-4-27 在配件识别码绑定时同时更新配件任务兑现单的识别码
        String hql = "from PartsRdp where recordStatus = 0 and partsAccountIDX = ?";
        List<PartsRdp> rdpList = daoUtils.find(hql, new Object[] {account.getIdx()});
        if (rdpList != null && rdpList.size() > 0) {
        	for (PartsRdp rdp : rdpList) {
        		rdp.setIdentificationCode(account.getIdentificationCode());        		
			}
        	partsRdpQueryManager.saveOrUpdate(rdpList);
        }
        return null ;
    }
    
    /**
     * <li>说明：根据实体进行查询
     * <li>创建人：何涛
     * <li>创建日期：2015-11-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 配件信息查询实体
     * @param filterByLike 使用LIKE查询条件的字段集（可以为null），例如：
     * {
     *      "partsStatus": "RIGHT_LIKE"
     *      "unloadPlace": "LEFT_LIKE"
     * }
     * @return 配件信息实体集合
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private List<PartsAccount> findByEntity(PartsAccount entity, Map<String, String> filterByLike) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("From PartsAccount where recordStatus = 0");
        
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Object fieldValue;
            try {
                field.setAccessible(true);
                fieldValue = field.get(entity);
            } catch (Throwable e) {
                throw new BusinessException(e);
            }
            // 返回类字段的所有修饰符
            int modifiers = field.getModifiers();
            if (null == fieldValue || fieldValue.toString().length() <= 0   // 不处理空值
                || fieldValue instanceof Date                               // 不处理日期类型字段的查询
                || Modifier.isStatic(modifiers)                             // 不处理类静态字段的查询
                || null != field.getAnnotation(Transient.class)             // 不处理临时字段的查询
            ) {  
                continue;
            }

            String fieldName = field.getName();
            if (null != filterByLike && filterByLike.containsKey(fieldName)) {
                String likeMode = filterByLike.get(fieldName);
                sb.append(" And ").append(fieldName);
                // 右LIKE匹配
                if (PartsAccountManager.LIKE_MODE_RIGHT.equals(likeMode)) {
                    sb.append(" Like '").append(fieldValue.toString()).append("%'");
                // 左LIKE匹配
                } else if (PartsAccountManager.LIKE_MODE_LEFT.equals(likeMode)) {
                    sb.append(" Like '%").append(fieldValue.toString()).append("'");
                // 全LIKE匹配
                } else {
                    sb.append(" Like '%").append(fieldValue.toString()).append("%'");
                }
            } else {
                sb.append(" And ").append(fieldName).append(" = '").append(fieldValue.toString()).append("'");
            }
        }
        return this.daoUtils.find(sb.toString());
    }
   
    /**
     * <li>说明：分页查询，根据当前系统操作人员，查询当前班组可以修理的配件信息（工位终端）
     * <li>创建人：何涛
     * <li>创建日期：2016-3-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity entityJson: {
            unloadTrainType: "HXD3C 0001",
            identificationCode: "PJ-20151102004",
            partsNo: "20151102004",
            partsTypeIDX: "402886814ce249bf014ce4dfb082019d"
        }
     * @param isSelfRepair 是否自修，是：则只查询本班组承修的配件
     * @return 配件信息分页集合
     */
    public Page<PartsAccount> findPageForRepair(SearchEntity<PartsAccount> searchEntity, boolean isSelfRepair) {
        StringBuilder sb =  new StringBuilder();
        sb.append("From PartsAccount Where recordStatus = 0 And partsStatus Like '").append(PartsAccount.PARTS_STATUS_DX).append("%'");
        PartsAccount entity = searchEntity.getEntity();
        if (StringUtil.isNullOrBlank(entity.getPartsTypeIDX()) && isSelfRepair) {
            sb.append(" And partsTypeIDX In (");
            sb.append(" Select partsTypeIDX From PartsRepairList Where recordStatus = 0 And repairOrgID ='").append(SystemContext.getOmOrganization().getOrgid()).append("'");
            sb.append(" )");
        } else if (!StringUtil.isNullOrBlank(entity.getPartsTypeIDX())) {
            sb.append(" And partsTypeIDX ='").append(entity.getPartsTypeIDX()).append("'");
        }

        // 根据“下车车型”或者“下车车号”查询
        if (!StringUtil.isNullOrBlank(entity.getUnloadTrainType())) {
            sb.append(" And Lower(UNLOAD_TRAINTYPE || UNLOAD_TRAINNO) Like '%").append(entity.getUnloadTrainType().toLowerCase()).append("%'");
        }
        // 根据“配件识别码”或者“配件编号”查询
        if (!StringUtil.isNullOrBlank(entity.getIdentificationCode()) && !StringUtil.isNullOrBlank(entity.getPartsNo())){
            sb.append(" And (");
            sb.append(" identificationCode Like '%").append(entity.getIdentificationCode()).append("%'");
            sb.append(" Or");
            sb.append(" partsNo Like '%").append(entity.getPartsNo()).append("%'");
            sb.append(" )");
        }
        String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
        return super.findPageList(totalHql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit());
    } 
    
    /**
     * <li>说明：查询上车配件信息
     * <li>创建人：张迪
     * <li>创建日期：2016-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @param isSelfRepair 
     * @return
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public Page<PartsAccount> findPartsForAboard(SearchEntity<PartsAccount> searchEntity, boolean isSelfRepair) throws SecurityException, NoSuchFieldException {
        StringBuilder sql =  new StringBuilder();
        sql.append("select a.* From  (select * from ( SELECT T.*,ROW_NUMBER() OVER (PARTITION BY t.parts_no ORDER BY T.CREATE_TIME desc)RV fROM PJWZ_PARTS_ACCOUNT T)  where RV=1  and PARTS_STATUS = '0201') a  where 1=1 ");
        PartsAccount entity = searchEntity.getEntity();     
        // 根据“上车车型”或者“上车车号”查询
        if (!StringUtil.isNullOrBlank(entity.getAboardTrainTypeIdx())) {
            sql.append(" And Lower(a.ABOARD_TRAINTYPE_IDX) Like '%").append(entity.getAboardTrainTypeIdx().toLowerCase()).append("%'");
        }
        // 根据“上车车型”或者“上车车号”查询
        if (!StringUtil.isNullOrBlank(entity.getAboardTrainNo())) {
            sql.append(" And  a.ABOARD_TRAINNO Like '%").append(entity.getAboardTrainNo()).append("%'");
        }
        
//      拼接排序参数
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            String dir = order[1];
            Class clazz = PartsAccount.class;
            Field field = clazz.getDeclaredField(sort);
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sql.append(" ORDER BY a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sql.append(" ORDER BY a.").append(sort).append(" ").append(dir);
            }
        }
        
        String totalSql = "Select Count(*) As rowcount " + sql.substring(sql.indexOf("From"));
        return super.queryPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsAccount.class);
    } 
    
    /**
     * 
     * <li>说明：根据规格型号，配件编号，配件识别码查询配件周转台账信息列表
     * <li>创建人：张迪
     * <li>创建日期：2016-11-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param account 查询实体
     * @return PartsAccount 台账信息
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getAccountBySpeAndParts(PartsAccount account) {
        PartsAccount accountBean = new PartsAccount();
        if (StringUtil.isNullOrBlank(account.getPartsNo()) && StringUtil.isNullOrBlank(account.getSpecificationModel())
            && StringUtil.isNullOrBlank(account.getIdentificationCode())) {
            throw new BusinessException("操作失败，未指定查询参数！");
        }
        StringBuffer hql = new StringBuffer("From PartsAccount Where recordStatus = 0 ");    
        hql.append(" And partsStatus Like '").append(PartsAccount.PARTS_STATUS_ZC).append("%'");
        if(!StringUtil.isNullOrBlank(account.getSpecificationModel())&& !StringUtil.isNullOrBlank(account.getPartsNo())){
            hql.append(" And specificationModel ='").append(account.getSpecificationModel()).append("'");
            hql.append(" And partsNo ='").append(account.getPartsNo()).append("'"); 
            hql.append(" Order By updateTime Desc");
            accountBean = (PartsAccount) this.daoUtils.findSingle(hql.toString());
        }
        if(null != accountBean){
            return accountBean;
        }else{               
            if(!StringUtil.isNullOrBlank(account.getIdentificationCode())){
                hql = new StringBuffer("From PartsAccount Where recordStatus = 0 ");  
                hql.append(" And partsStatus Like '").append(PartsAccount.PARTS_STATUS_ZC).append("%'");
                hql.append(" And (");
                    hql.append(" identificationCode ='").append(account.getIdentificationCode()).append("'");
                    hql.append(" Or idx In (");
                        hql.append("Select B.partsAccountIdx From PartsAccount A, RecordCodeBind B Where A.idx = B.partsAccountIdx And B.recordCode ='").append(account.getIdentificationCode()).append("'");
                    hql.append(")");
                hql.append(")");
            }  
            accountBean = (PartsAccount) this.daoUtils.findSingle(hql.toString());
            if(null != accountBean){
                return accountBean;
            }
        }  
        if(null == accountBean){
            throw new BusinessException("配件编号为"+ account.getPartsNo()+"的配件不存在，请重新添加！");          
        }
        return null;
    }
}