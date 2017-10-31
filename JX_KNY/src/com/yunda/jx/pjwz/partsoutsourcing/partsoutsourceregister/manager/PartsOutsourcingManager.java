package com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IEosDictEntryManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcingOutBean;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件委外登记业务处理类
 * <li>创建人：王斌
 * <li>创建日期：2014-5-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsOutsourcingManager")
public class PartsOutsourcingManager extends JXBaseManager<PartsOutsourcing, PartsOutsourcing> {
	private PartsAccountManager partsAccountManager;

	private IEosDictEntryManager dictManager;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    private String statusId = "PJWZ_PARTS_ACCOUNT_STATUS" ;
    
	/**
	 * <li>说明：获取状态树（配件委外中配件状态专用方法）
	 * <li>创建人：王斌
	 * <li>创建日期：2014-7-3
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param paramMap 过滤参数
	 * @return List<HashMap> 状态列表
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> getStatusByEosDicEntity(Map<String, Object> paramMap)
			throws BusinessException {
		List<EosDictEntry> list = new ArrayList<EosDictEntry>();
		List<HashMap> children = new ArrayList<HashMap>();
		if (paramMap != null && paramMap.size() > 0) {
			String parentIDX = paramMap.get("parentIDX").toString();
			String dicttypeid = paramMap.get("dicttypeid").toString();
			if (parentIDX.equals("ROOT_0")) {
				list = dictManager.findRoots(dicttypeid);
			} else {
				list = this.findChildsByIds(paramMap);
			}
			for (EosDictEntry dict : list) {
				Boolean isLeaf = isLeaf(dict.getId().getDictid());
				HashMap nodeMap = new HashMap();
				nodeMap.put("id", dict.getId().getDictid());
				nodeMap.put("text", dict.getDictname());
				nodeMap.put("leaf", isLeaf);
				nodeMap.put("parentid", dict.getParentid());
				// if("false".equals(isChecked)){
				// nodeMap.put("checked", false);
				// }
				children.add(nodeMap);
			}
			return children;
		}
		return null;
	}

	/**
	 * <li>创建人：王斌
	 * <li>创建日期：2014-7-3
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @return true 是子节点，false 非子节点
	 * @throws BusinessException
	 */
	private boolean isLeaf(String idx) throws BusinessException {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) From EosDictEntry t where t.status = 1 ");
		if (!StringUtil.isNullOrBlank(idx)) {
			hql.append(" and t.parentid='" + idx + "'");
		}
		int count = daoUtils.getInt(enableCache(), hql.toString());
		return count == 0 ? true : false;
	}

	/**
	 * <li>说明：根据参数查询状态树（配件委外中配件状态专用方法）
	 * <li>创建人：王斌
	 * <li>创建日期：2014-7-3
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @param paramMap 过滤参数
     * @return List<EosDictEntry> 状态列表
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public List<EosDictEntry> findChildsByIds(Map<String, Object> paramMap)
			throws BusinessException {
		if (paramMap != null && paramMap.size() > 0) {
			String dicttypeid = paramMap.get("dicttypeid").toString();
			String dictid = paramMap.get("parentIDX").toString();
			if (StringUtil.isNullOrBlank(dicttypeid))
				throw new RuntimeException("参数异常: 入参dicttypeid为空");
			if (StringUtil.isNullOrBlank(dictid))
				throw new RuntimeException("参数异常: 入参dictid为空");
			String hql = "from EosDictEntry where id.dicttypeid = ? and parentid = ? and id.dictid not like '0102%'";
			List<EosDictEntry> list = null;
			Object[] param = new Object[] { dicttypeid, dictid };
			// 是否利用查询缓存
			if (isJcglQueryCacheEnabled()) {
				list = daoUtils.find(true, hql, param); // 使用缓存查询
			} else {
				list = daoUtils.getHibernateTemplate().find(hql, param); // 不使用缓存查询
			}
			return list;
		}
		return null;
	}

	/**
	 * <li>说明：获取委外配件信息列表
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-22
	 * <li>修改人： 何涛
	 * <li>修改日期：2014-08-28
	 * <li>修改内容：优化代码、增加对extendNoJson（扩展编号json）的查询
	 * @param whereList 过滤条件
     * @param orderList 排序对象
     * @param start 开始行
     * @param limit 每页记录数
	 * @return Page<PartsOutsourcing> 委外信息分页对象
	 * @throws BusinessException
	 */
	public Page<PartsOutsourcing> findPageListForOutSource(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit) throws BusinessException {
		StringBuilder sb = new StringBuilder();
		sb.append("Select new com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing(");
		sb.append(" o.partsAccountIDX, o.partsTypeIdx, o.partsName, o.specificationModel, o.partsOutNo, o.extendNoJson,");
		sb.append(" o.status, a.configDetail, o.out sourcingFactoryId, o.outsourcingFactory, o.outsourcingDate)");
		sb.append(" From PartsOutsourcing o,PartsAccount a Where o.recordStatus=0 And a.recordStatus=0 And o.partsAccountIDX=a.idx");

		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				String propName = String.valueOf(condition.getPropName());
				String propValue = String.valueOf(condition.getPropValue());
				if ("status".equals(propName) && !StringUtil.isNullOrBlank(propValue)) {
					sb.append(" and  o.status = ").append("'" + propValue + "'");
				}
				if ("partsNo".equals(propName) && !StringUtil.isNullOrBlank(propValue)) {
					sb.append(" and  o.partsOutNo like ").append("'%" + propValue + "%'");
				}
				if ("factoryId".equals(propName) && !StringUtil.isNullOrBlank(propValue)) {
					sb.append(" and o.outsourcingFactoryId = ").append("'" + propValue + "'");
				}
				if ("specificationModel".equals(propName) && !StringUtil.isNullOrBlank(propValue)) {
					sb.append(" and o.specificationModel like ").append("'%" + propValue + "%'");
				}
			}
		}
		String hql = sb.toString();
		String totalHql = "select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return super.findPageList(totalHql, hql, start, limit);
	}

	/**
	 * 
	 * <li>说明：根据配件编号，查询委外的配件
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-22
	 * <li>修改人： 何涛
	 * <li>修改日期：2014-08-28
	 * <li>修改内容：增加对extendNoJson（扩展编号json）的查询
	 * 
	 * @param paramMap 过滤条件
	 * @return Object 委外配件信息
	 */
	@SuppressWarnings("unchecked")
	public Object findPartsOutforNo(Map<String, Object> paramMap)
			throws BusinessException {
		String partsNo = "";
		String status = "";
		if (paramMap != null && paramMap.size() > 0) {
			partsNo = (String) paramMap.get("partsNo");
			status = (String) paramMap.get("partsStatus");
		}
		String sql = "select o.parts_account_idx partsaccoutnidx,o.parts_type_idx partstypeidx,"
				+ "o.parts_name partsname,o.specification_model specificationmodel,"
				+ "o.parts_out_no partsoutno, a.config_detail configdetail, o.EXTENDNOJSON "
				+ " from pjwz_parts_outsourcing o,pjwz_parts_account a "
				+ " WHERE O.RECORD_STATUS=0 AND a.record_status=0 and o.parts_account_idx=a.idx"
				+ " and o.parts_out_no='"
				+ partsNo
				+ "' and o.status in "
				+ status;
		List<Object> list = this.daoUtils.executeSqlQuery(sql);
		if (list != null && list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			return obj;
		} else {
			return null;
		}
	}

	
	/**
	 * <li>说明：检验数据是否异常，判断配件是否已委外，如果已委外，则不能重复委外
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param outsourcing 委外对象
	 * @param partsOutsourcings 委外对象数组
	 * @return String[] 错误提示
	 */
	private String[] checkValid(PartsOutsourcing outsourcing, PartsOutsourcing[] partsOutsourcings) {
		if (null == outsourcing || null == partsOutsourcings || partsOutsourcings.length <= 0) {
			return new String[] { "数据异常" };
		}
		List<String> errorList = new ArrayList<String>();
		List temp = null;
		for (PartsOutsourcing po : partsOutsourcings) {
			StringBuilder sb = new StringBuilder();
			sb.append("select o.* from pjwz_parts_outsourcing o where o.record_status=0");
			sb.append(" and o.status='" + PartsOutsourcing.PARTS_STATUS_WW + "'");
			sb.append(" and o.parts_account_idx='" + po.getPartsAccountIDX() + "'");
			temp = this.daoUtils.executeSqlQuery(sb.toString());
			if (temp != null && temp.size() > 0) {
				errorList.add("配件编号为【" + po.getPartsOutNo() + "】的配件已委外，不能重复委外！");
			}
		}
		if (errorList.size() <= 0) {
			return null;
		}
		String[] errorMsg = new String[errorList.size()];
		errorList.toArray(errorMsg);
		return errorMsg;
	}

	/**
	 * 
	 * <li>说明：新增配件委外登记单
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-21
	 * <li>修改人： 何涛
	 * <li>修改日期：2014-08-28
	 * <li>修改内容：增加对“扩展编号json”的存储
	 * <li>修改人： 何涛
	 * <li>修改日期：2016-04-08
	 * <li>修改内容：代码审查，使用异常处理业务验证失败的信息
	 * @param common 配件委外登记单公共信息
	 * @param details 配件委外登记单详细信息
	 * @param emp 人员信息
	 * @return String[] 错误提示
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public void savePartsOutBill(PartsOutsourcing common, PartsOutsourcing[] details, OmEmployee emp) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		// 检验数据是否异常，判断配件是否已委外，如果已委外，则不能重复委外 
		String[] errorMsg = this.checkValid(common, details);
		if (null != errorMsg) {
			throw new BusinessException(errorMsg[0]);
		}

		// 设置经办人所在部门
		// 经办人所在部门信息已修改为由前端隐藏字段传到后台，此处仅是为了防止前段传输部门信息不全
		if (StringUtil.isNullOrBlank(common.getOutOrg()) || null == common.getOutOrgId()) {
			common = this.setOutOrgNameAndId(common);
		}
		
		List<PartsAccount> paList = new ArrayList<PartsAccount>();// 保存配件信息实体集合
		List<PartsOutsourcing> poList = new ArrayList<PartsOutsourcing>();// 保存配件委外登记单实体集合
		for (PartsOutsourcing detail : details) {
			PartsOutsourcing partsOutsourcing = adapteFrom(common, detail);
			poList.add(partsOutsourcing);
			
			PartsAccount account = this.partsAccountManager.getModelById(detail.getPartsAccountIDX());// 取得配件信息实体
			account.setPartsStatus(PartsAccount.PARTS_STATUS_WWX);// 委外状态编码
			account.setPartsStatusName(partsAccountManager.getPartsStatusName(statusId, PartsAccount.PARTS_STATUS_WWX, PartsAccount.PARTS_STATUS_WWX_CH));
			account.setPartsStatusUpdateDate(common.getOutsourcingDate());// 配件状态更新时间为委外日期
			paList.add(account);
		}
		this.saveOrUpdate(poList);
		this.partsAccountManager.saveOrUpdate(paList);
	}

	/**
	 * <li>说明：根据配件委外实体“公共信息”和“详细信息”构造配件委外实体
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param common 配件委外登记单公共信息
	 * @param detail 配件委外登记单详细信息
	 * @return PartsOutsourcing 委外信息
	 */
	private PartsOutsourcing adapteFrom(PartsOutsourcing common, PartsOutsourcing detail) {
		PartsOutsourcing partsOutsourcing = new PartsOutsourcing();
		partsOutsourcing.setPartsAccountIDX(detail.getPartsAccountIDX());						// 配件信息主键
		partsOutsourcing.setPartsTypeIdx(detail.getPartsTypeIdx());								// 配件规格型号主键
		partsOutsourcing.setPartsName(detail.getPartsName());									// 配件名称
		partsOutsourcing.setSpecificationModel(detail.getSpecificationModel());					// 规格型号
		partsOutsourcing.setUnit(detail.getUnit());												// 计量单位
		partsOutsourcing.setPartsOutNo(detail.getPartsOutNo());									// 配件委外编号
		partsOutsourcing.setExtendNoJson(detail.getExtendNoJson());								// 扩展编号
		partsOutsourcing.setOutsourcingReasion(detail.getOutsourcingReasion());					// 委外检修原因
		partsOutsourcing.setRepairContent(detail.getRepairContent());							// 检修内容
		partsOutsourcing.setOutsourcingFactoryId(common.getOutsourcingFactoryId());				// 委外厂家编号
		partsOutsourcing.setOutsourcingFactory(common.getOutsourcingFactory());					// 委外厂家
		partsOutsourcing.setCarNo(common.getCarNo());											// 车牌号
		partsOutsourcing.setOutsourcingDate(common.getOutsourcingDate());						// 委外日期
		partsOutsourcing.setOutOrgId(common.getOutOrgId());										// 经办部门主键
		partsOutsourcing.setOutOrg(common.getOutOrg());											// 经办部门
		partsOutsourcing.setOutEmpId(common.getOutEmpId());										// 经办人主键
		partsOutsourcing.setOutEmp(common.getOutEmp());											// 经办人
		partsOutsourcing.setCreatorName(common.getCreatorName());								// 制单人
		partsOutsourcing.setStatus(PartsOutsourcing.PARTS_STATUS_WW);							// 委外
		return partsOutsourcing;
	}
	
	/**
	 * <li>说明：设置经办人部门信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件委外实体
	 * @return PartsOutsourcing 委外信息
	 */
	private PartsOutsourcing setOutOrgNameAndId(PartsOutsourcing entity) {
		String sql = "select o.orgid, o.orgname from om_organization o where o.orgid ='" + entity.getOutEmpId() + "'";
		List list = this.daoUtils.executeSqlQuery(sql);
		if (list != null && list.size() == 1) {
			Object[] obj = (Object[]) list.get(0);
			entity.setOutOrgId(Integer.parseInt(obj[0].toString()));
			entity.setOutOrg(obj[1].toString());
		}
		return entity;
	}
	
	/**
	 * <li>说明：根据配件信息主键查询配件委外实体
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param partsAccountIDX 配件信息主键
	 * @return PartsOutsourcing 委外信息
	 */
	@SuppressWarnings("unused")
    private PartsOutsourcing getModelByPartsAccountIDX(String partsAccountIDX) {
		StringBuilder sb = new StringBuilder();
		sb.append("From PartsOutsourcing Where recordStatus = 0 And partsAccountIDX='").append(partsAccountIDX).append("'");
		String hql = sb.toString();
		return this.findSingle(hql);
	}
    
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsOutsourcing> 分页对象
     * @throws BusinessException
     */
    public Page<PartsOutsourcing> findPageList(SearchEntity<PartsOutsourcing> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsOutsourcing t where t.recordStatus=0 ");
        PartsOutsourcing register = searchEntity.getEntity() ;
        StringBuffer awhere =  new StringBuffer();
        //状态过滤【未回段、已回段】
        if(!StringUtil.isNullOrBlank(register.getStatus())){
            String identificationCode = register.getIdentificationCode();
            if(!StringUtil.isNullOrBlank(identificationCode)){
                //未回段
                if(PartsOutsourcing.PARTS_STATUS_WW.equals(register.getStatus())){
                        awhere.append(" and (t.partsOutNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
                        .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.takeOverOrg like '%").append(identificationCode)
                        .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
                }else if(PartsOutsourcing.PARTS_STATUS_YFH.equals(register.getStatus())){   //已回段
                        awhere.append(" and (t.partsBackNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
                        .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.takeOverOrg like '%").append(identificationCode)
                        .append("%' or t.identificationCodeBack like '%").append(identificationCode).append("%' )") ;
                }
            }
            awhere.append(" and t.status = '" + register.getStatus() + "'");
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            //未回段
            if(PartsOutsourcing.PARTS_STATUS_WW.equals(register.getStatus())){
                awhere.append(" order by t.outsourcingDate asc,t.updateTime desc");
            }else if(PartsOutsourcing.PARTS_STATUS_YFH.equals(register.getStatus())){   //已回段
                awhere.append(" order by t.updateTime desc");
            }
            
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：委外登记前验证此配件是否已被委外【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param outsourcing 需被验证的委外对象
     * @return String 错误提示
     */
    @SuppressWarnings("unused")
    public String validateSingle(PartsOutsourcing outsourcing) {
        if (null == outsourcing) {
            return new String("数据异常");
        }
        String error = "";
        StringBuilder sb = new StringBuilder();
        sb.append("select o.* from pjwz_parts_outsourcing o where o.record_status=0");
        sb.append(" and o.status='" + PartsOutsourcing.PARTS_STATUS_WW + "'");
        sb.append(" and o.parts_account_idx='" + outsourcing.getPartsAccountIDX() + "'");
        List temp = this.daoUtils.executeSqlQuery(sb.toString());
        if (temp != null && temp.size() > 0) {
            error = "配件编号为【" + outsourcing.getPartsOutNo() + "】的配件已委外，不能重复委外！";
        }
        return error;
    }
    /**
     * 
     * <li>说明：委外回段前验证
     * <li>创建人：程梅
     * <li>创建日期：2016-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param outsourcing
     */
    @SuppressWarnings("unused")
    public void validateBack(PartsOutsourcing outsourcing) {
        if (null == outsourcing) {
            throw new BusinessException("数据异常");
        }
        //判断该配件是否已经委外回段入库了
        String whHql="select w from PartsOutsourcing w where w.recordStatus=0 " +
                " and w.partsAccountIDX='"+outsourcing.getPartsAccountIDX()+"' and w.status='"+PartsOutsourcing.PARTS_STATUS_WW+"'";
        List whList=this.daoUtils.find(whHql);
        if(null == whList || whList.size() <= 0){
            throw new BusinessException("配件编号为【"+outsourcing.getPartsBackNo()+"】的配件已经回段，不能重复回段");
        }
        String partsNoHql = "select a From PartsAccount a where a.recordStatus=0 and a.partsNo='"+outsourcing.getPartsBackNo()+"' and a.partsTypeIDX='" +
        outsourcing.getPartsTypeIdx() + "' and a.partsStatus like '"+PartsAccount.PARTS_STATUS_ZC+"%' and a.idx <> '"+outsourcing.getPartsAccountIDX()+"'";
        List list = daoUtils.find(partsNoHql);
        if(list != null && list.size()>0){
            throw new BusinessException("配件编号"+outsourcing.getPartsBackNo()+"已存在，不能重复添加！");
        }
        String idCodeHql = "select a From PartsAccount a where a.recordStatus=0 and a.identificationCode='"+outsourcing.getIdentificationCodeBack()+"' " +
        "and a.partsStatus like '"+PartsAccount.PARTS_STATUS_ZC+"%' and a.idx <> '"+outsourcing.getPartsAccountIDX()+"'";
        List codeList = daoUtils.find(idCodeHql);
        if(codeList != null && codeList.size()>0){
            throw new BusinessException("识别码"+outsourcing.getIdentificationCodeBack()+"已存在，不能重复添加！");
        }
    }
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：委外配件登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param outsourcing 需保存的委外信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    public void savePartsOutsourcing(PartsOutsourcing outsourcing) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        // 检验数据是否异常，判断配件是否已委外，如果已委外，则不能重复委外 
        String error = this.validateSingle(outsourcing);
        if (!StringUtil.isNullOrBlank(error)) {
            throw new BusinessException(error);
        }
        PartsAccount account = this.partsAccountManager.getModelById(outsourcing.getPartsAccountIDX());// 取得配件信息实体
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DBF)){
            throw new BusinessException("【" + account.getPartsStatusName() + "】的配件不能委外，请重新添加！");
        }
        //日志描述=承修单位(委外厂家)
        String logDesc = outsourcing.getOutsourcingFactory() ;
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJWW, logDesc);
        log = partsManageLogManager.initLog(log, account);
        account.setPartsStatus(PartsAccount.PARTS_STATUS_WWX);// 委外状态编码
        account.setPartsStatusName(partsAccountManager.getPartsStatusName(statusId, PartsAccount.PARTS_STATUS_WWX, PartsAccount.PARTS_STATUS_WWX_CH));
        account.setPartsStatusUpdateDate(outsourcing.getOutsourcingDate());// 配件状态更新时间为委外日期
        OmEmployee emp = SystemContext.getOmEmployee();
        OmOrganization om = SystemContext.getOmOrganization();
        if(null == outsourcing.getOutEmpId()){
            outsourcing.setOutEmpId(Integer.parseInt(emp.getEmpid().toString()));
            outsourcing.setOutEmp(emp.getEmpname());
            outsourcing.setOutOrgId(Integer.parseInt(om.getOrgid().toString()));
            outsourcing.setOutOrg(om.getOrgname());
        }
        outsourcing.setCreatorName(emp.getEmpname());
        outsourcing.setStatus(PartsOutsourcing.PARTS_STATUS_WW);
        this.saveOrUpdate(outsourcing);
        log.eventIdx(outsourcing.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
        this.partsAccountManager.saveOrUpdate(account);
//    this.oldPartsStockManager.saveOrUpdate(oldPartsStock);
    }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：撤销委外
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销的委外登记主键
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateOutsourcingForCancel(String id) throws BusinessException, NoSuchFieldException {
            PartsOutsourcing outsouring = getModelById(id);
            if(null != outsouring){
                    PartsAccount account = partsAccountManager.getModelById(outsouring.getPartsAccountIDX()); //查询该配件周转台账信息
                    if(!PartsAccount.PARTS_STATUS_WWX.equals(account.getPartsStatus()) || Constants.DELETED == account.getRecordStatus()){
                        throw new BusinessException("只有【委外修】状态的配件才能撤销！");
                    }else{
                        //根据配件id和委外登记id查询配件日志信息
                        PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                        //配件状态回滚到委外登记前
                        account = partsManageLogManager.getAccountFromLog(log, account);
                        partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                        this.logicDelete(id);    //删除委外登记信息
                        partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
                    }
                    
              }else {
                  throw new BusinessException("数据有误！");
              }
        }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：修改委外配件登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param outsourcing 需保存的委外信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    public void updatePartsOutsourcing(PartsOutsourcing outsourcing) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        // 判断此委外配件是否已返回
        if (PartsOutsourcing.PARTS_STATUS_YFH.equals(outsourcing.getStatus())) {
            throw new BusinessException("此记录已返回，请刷新后重试！");
        }
        PartsAccount account = this.partsAccountManager.getModelById(outsourcing.getPartsAccountIDX());// 取得配件信息实体
        if(!PartsAccount.PARTS_STATUS_WWX.equals(account.getPartsStatus())){
            throw new BusinessException("只有【委外修】状态的配件才能修改，请重新添加！");
        }
        account.setPartsStatus(PartsAccount.PARTS_STATUS_WWX);// 委外状态编码
        account.setPartsStatusName(partsAccountManager.getPartsStatusName(statusId, PartsAccount.PARTS_STATUS_WWX, PartsAccount.PARTS_STATUS_WWX_CH));
        account.setPartsStatusUpdateDate(outsourcing.getOutsourcingDate());// 配件状态更新时间为委外日期
        outsourcing.setStatus(PartsOutsourcing.PARTS_STATUS_WW);
        this.saveOrUpdate(outsourcing);
        this.partsAccountManager.saveOrUpdate(account);
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【待修】配件周转台账信息（除在库和非在册外）
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人：何涛
     * <li>修改日期：2016-04-08
     * <li>修改内容：审查规范：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
     * @param account 查询实体
     * @return PartsAccount 实体对象
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getPartsAccount(PartsAccount account) {
        PartsAccount pa = this.partsAccountManager.getAccount(account);
        if (null == pa) {
            account.setPartsNo(account.getIdentificationCode());
            account.setIdentificationCode(null);
            pa = this.partsAccountManager.getAccount(account);
        }
        if (null == pa) {
            throw new BusinessException("此配件未登记！");
        }
        if(!pa.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) && !pa.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DBF)){
            throw new BusinessException("只有【待修】和【待报废】配件才能委外，请重新添加！");
        }
        return pa;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【待修、待报废】配件周转台账信息列表
     * <li>创建人：程梅
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param account 查询实体
     * @return List<PartsAccount> 台账信息列表
     */
    public List<PartsAccount> getPartsAccountList(PartsAccount account) {
      List<PartsAccount> entityList = this.partsAccountManager.getAccountList(account);
      if(null == entityList || entityList.size() == 0){
          account.setPartsNo(account.getIdentificationCode());
          account.setIdentificationCode("");
          entityList = this.partsAccountManager.getAccountList(account);
      }
      if(null != entityList && entityList.size() > 0){
          // 先将list进行复制 然后再遍历
          List<PartsAccount> entityListV = new ArrayList<PartsAccount>();
          entityListV.addAll(entityList);
          for(PartsAccount entity : entityListV){
              if (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DBF)) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("只有【待修】和【待报废】配件才能委外，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：配件委外登记【web端】
     * <li>创建人：程梅
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 配件委外登记信息数组
     * @return 错误信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void saveBatchPartsOutsourcing(PartsOutsourcing[] registers) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        for (PartsOutsourcing register : registers) {
            savePartsOutsourcing(register);
        }
    }
    /**
     * 
     * <li>说明：委外配件回段【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-4-18
     * <li>修改人： 黄杨
     * <li>修改日期：2016-9-28
     * <li>修改内容：配件返回识别码为空时保存问题
     * @param outsourcing
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsOutsourcingForBack(PartsOutsourcing outsourcing) throws BusinessException, NoSuchFieldException{
        PartsOutsourcing register = this.getModelById(outsourcing.getIdx());
        register.setPartsBackNo(outsourcing.getPartsBackNo());
        register.setIdentificationCodeBack(outsourcing.getIdentificationCodeBack());
        register.setBackDate(outsourcing.getBackDate());
        register.setTakeOverOrgId(outsourcing.getTakeOverOrgId());
        register.setTakeOverOrg(outsourcing.getTakeOverOrg());
        register.setTakeOverType(outsourcing.getTakeOverType());
        register.setTakeOverOrgSeq(outsourcing.getTakeOverOrgSeq());
        register.setBackPartsStatus(outsourcing.getBackPartsStatus());
        register.setBackPartsStatusName(outsourcing.getBackPartsStatusName());
        // 检验数据是否异常，判断该委外数据是否已回段、配件编号是否重复
        this.validateBack(register);
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIDX());// 取得配件信息实体
        //委外状态的配件才能回段
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_WWX)){
            throw new BusinessException("【" + account.getPartsStatusName() + "】的配件不能回段，请重新添加！");
        }
        //获取当前登录操作员
        OmEmployee emp = SystemContext.getOmEmployee();
        if (null != emp) {
            register.setCreatorName(emp.getEmpname());
            register.setTakeOverEmpId(Integer.parseInt(emp.getEmpid().toString()));
            register.setTakeOverEmp(emp.getEmpname());  
        }
        register.setStatus(PartsOutsourcing.PARTS_STATUS_YFH);//状态为已回段
        this.saveOrUpdate(register);
        if(null != account){
            //日志描述=接收部门
            String logDesc = "";
            if(!StringUtil.isNullOrBlank(register.getTakeOverOrg())) logDesc = register.getTakeOverOrg();
            PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_WWHD, logDesc);
            log = partsManageLogManager.initLog(log, account);
            if(null != register.getTakeOverOrgId()) account.setManageDeptId(register.getTakeOverOrgId().toString());  //责任部门id为接收部门id
            account.setManageDept(register.getTakeOverOrg()); //责任部门名称为接收部门名称
            account.setManageDeptType(register.getTakeOverType());//责任部门类型为接收部门类型
            account.setPartsStatusUpdateDate(register.getBackDate());//配件状态更新日期为回段日期
            // 接收部门类型为【机构】
            if(PartsAccount.MANAGE_DEPT_TYPE_ORG == register.getTakeOverType()){
                account.setManageDeptOrgseq(register.getTakeOverOrgSeq());//责任部门序列为接收部门序列
            }else if(PartsAccount.MANAGE_DEPT_TYPE_WH == register.getTakeOverType()){  //接收部门类型为【库房】
                account.setManageDeptOrgseq(null);//责任部门序列为空
            }
            //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
//            account.setPartsStatus(PartsAccount.PARTS_STATUS_LH);//配件状态为良好
//            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_LH, PartsAccount.PARTS_STATUS_LH_CH));
            account.setPartsStatus(register.getBackPartsStatus());
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", register.getBackPartsStatus(), register.getBackPartsStatusName()));
            
            if(!register.getPartsBackNo().equals(account.getPartsNo())){
                account.setOldPartsNo(account.getPartsNo());
                account.setPartsNo(register.getPartsBackNo());
            }
            if(register.getIdentificationCodeBack() != null && !register.getIdentificationCodeBack().equals(account.getIdentificationCode())){
                account.setIdentificationCode(register.getIdentificationCodeBack());
            }
            account.setLocation(register.getTakeOverOrg());//存放地点=接收部门
            account.setConfigDetail(register.getConfigDetail());//详细配置
            account = EntityUtil.setSysinfo(account);
            this.partsAccountManager.saveOrUpdate(account);
            log.eventIdx(register.getIdx());
            partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
        }
    }
    /**
     * <li>说明：撤销委外回段
     * <li>创建人：程梅
     * <li>创建日期：2016-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销的委外登记主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateOutsourcingForCancelBack(String id) throws BusinessException, NoSuchFieldException {
            PartsOutsourcing outsouring = getModelById(id);
            if(null != outsouring){
                    PartsAccount account = this.partsAccountManager.getModelById(outsouring.getPartsAccountIDX());// 取得配件信息实体
                    List<PartsManageLog> logList = partsManageLogManager.getLogListByIdx(outsouring.getPartsAccountIDX()) ;
                    PartsManageLog log = new PartsManageLog();
                    if(null != logList && logList.size() > 0){
                        log = logList.get(0); //获取最新的日志记录
                        //操作类型不为【委外回段】，则表示此配件已进行了下一步操作，不能撤销
                        if(!log.getEventType().equals(PartsManageLog.EVENT_TYPE_WWHD) || Constants.DELETED == account.getRecordStatus()){
                            throw new BusinessException("配件已进入下一环节，不能撤销！");
                        }else{
                            //配件状态回滚到委外回段前
                            account = partsManageLogManager.getAccountFromLog(log, account);
                            account.setIdentificationCode(outsouring.getIdentificationCode());
                            account.setPartsNo(account.getOldPartsNo());
                            account.setOldPartsNo(null);
                            partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                            outsouring.setPartsBackNo(null);
                            outsouring.setIdentificationCodeBack(null);
                            outsouring.setBackDate(null);
                            outsouring.setTakeOverType(null);
                            outsouring.setTakeOverOrgId(null);
                            outsouring.setTakeOverOrgSeq(null);
                            outsouring.setTakeOverOrg(null);
                            outsouring.setStatus(PartsOutsourcing.PARTS_STATUS_WW);//状态回滚到【未回段】
                            outsouring.setBackPartsStatus(null);                           
                            outsouring.setBackPartsStatusName(null);//配件状态回滚
                            this.saveOrUpdate(outsouring);    //回滚委外登记信息
                            //删除配件委外回段日志信息
                            String hql = "delete From PartsManageLog Where eventIdx = '"+id+"' and partsStatusHis = '" + PartsAccount.PARTS_STATUS_WWX + "' ";
                            this.getDaoUtils().executUpdateOrDelete(hql);
    //                        partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
                    }
                 }  
              }else {
                  throw new BusinessException("数据有误！");
              }
        }
    /**
     * 
     * <li>说明：返回识别码唯一性验证
     * <li>创建人：程梅
     * <li>创建日期：2016-4-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param outsourcing
     */
    public void findIdentificationCodeValidate(PartsOutsourcing outsourcing) {
        if (null == outsourcing) {
            throw new BusinessException("数据异常");
        }
        String idCodeHql = "select a From PartsAccount a where a.recordStatus=0 and a.identificationCode='"+outsourcing.getIdentificationCodeBack()+"' " +
        "and a.partsStatus like '"+PartsAccount.PARTS_STATUS_ZC+"%' and a.idx <> '"+outsourcing.getPartsAccountIDX()+"'";
        List codeList = daoUtils.find(idCodeHql);
        if(codeList != null && codeList.size()>0){
            throw new BusinessException("识别码"+outsourcing.getIdentificationCodeBack()+"已存在，不能重复添加！");
        }
    }
    /**
     * 
     * <li>说明：批量撤销【web段】
     * <li>创建人：程梅
     * <li>创建日期：2016-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateForCancelBackBatch(String[] ids) throws BusinessException, NoSuchFieldException {
        for (String id : ids) {
            this.updateOutsourcingForCancelBack(id);
        }
    }
    /**
     * 
     * <li>说明：查询状态为委外的配件委外登记记录
     * <li>创建人：程梅
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 委外登记list
     */
    @SuppressWarnings("unchecked")
    public List<PartsOutsourcing> getOutPartsOutsourcing() {
        StringBuffer hql = new StringBuffer("select new PartsOutsourcing(outEmpId, outEmp, outsourcingDate, outsourcingFactoryId, outsourcingFactory, partsOutNo, nameplateNo, identificationCode, partsName, specificationModel, outsourcingReasion, repairContent)")
        .append(" From PartsOutsourcing Where recordStatus = 0 And status = '").append(PartsOutsourcing.PARTS_STATUS_WW).append("'");
        return this.daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：查询状态为已返回的配件委外登记记录
     * <li>创建人：程梅
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 委外回段登记list
     */
    @SuppressWarnings("unchecked")
    public List<PartsOutsourcing> getBackPartsOutsourcing() {
        StringBuffer hql = new StringBuffer("select new PartsOutsourcing(takeOverEmpId, takeOverEmp, outsourcingDate, backDate, outsourcingFactoryId, outsourcingFactory, partsOutNo, ")
                .append(" partsBackNo, nameplateNo, identificationCode, identificationCodeBack, partsName, specificationModel, outsourcingReasion, repairContent, takeOverOrgId, takeOverOrg, takeOverOrgSeq)")
                .append(" From PartsOutsourcing Where recordStatus = 0 And status = '").append(PartsOutsourcing.PARTS_STATUS_YFH).append("'");
        return this.daoUtils.find(hql.toString());
    }
    
    
    /**
     * <li>说明：批量委外配件回段
     * <li>创建人：张迪
     * <li>创建日期：2016-11-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param outsourcing 委外回段实体
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void updatePartsOutsourcingForBackNew(PartsOutsourcing[] outsourcings) throws BusinessException, NoSuchFieldException {
        for (PartsOutsourcing outsourcing : outsourcings) {
            updatePartsOutsourcingForBack(outsourcing);
        }
    }
    
	public PartsAccountManager getPartsAccountManager() {
		return partsAccountManager;
	}

	public void setPartsAccountManager(PartsAccountManager partsAccountManager) {
		this.partsAccountManager = partsAccountManager;
	}

	public IEosDictEntryManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(IEosDictEntryManager dictManager) {
		this.dictManager = dictManager;
	}


    
    /**
     * <li>说明：配件委外登记查询（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始条数
     * @param limit 限制条数
     * @param workPlanId 任务单ID
     * @return
     */
    public Page<PartsOutsourcingOutBean> findPartsoutsourcingOutAll(Integer start, Integer limit, String workPlanId) {
        String sql = SqlMapUtil.getSql("pjwl-query:findPartsoutsourcingOutAll")
        .replace("#RDP_IDX#", workPlanId);
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("From"));
        return this.queryPageList(totalSql, sql, start, 1000, false, PartsOutsourcingOutBean.class);
    }

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids
     * @throws NoSuchFieldException 
     * @throws Exception 
     */
    public void updateOutsourcingForCancelBatch(String[] ids) throws Exception, NoSuchFieldException {
        for (String id : ids) {
            this.updateOutsourcingForCancel(id);
        }
    }

    /**
     * <li>说明：配件委外登记 pad
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 
     * @param type "1":未登记；"2":已登记
     * @return
     */
    public Page<PartsOutsourcingOutBean> findPartsoutsourcingOutAllForPad(SearchEntity<PartsOutsourcingOutBean> searchEntity, String type) {
        PartsOutsourcingOutBean entity = searchEntity.getEntity();
        String sql = SqlMapUtil.getSql("pjwl-query:findPartsoutsourcingOutAll")
        .replace("#RDP_IDX#", entity.getRdpIdx());
        // 查询未登记或已登记列表
        if(type.equals("1")){
            sql += " and t.idx is null ";
        }else{
            sql += " and t.idx is not null ";
        }
        // 模糊查询
        if(!StringUtil.isNullOrBlank(entity.getPartsName())){
            sql += " and ( t.parts_name like '%"+entity.getPartsName()+"%' or t.PARTS_NO like '%"+entity.getPartsName()+"%' or t.SPECIFICATION_MODEL like '%"+entity.getPartsName()+"%'  ) ";
        }
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("From"));
        Page<PartsOutsourcingOutBean> page = this.queryPageList(totalSql, sql, 0, 1000, false, PartsOutsourcingOutBean.class);
        return page;
    }

}
