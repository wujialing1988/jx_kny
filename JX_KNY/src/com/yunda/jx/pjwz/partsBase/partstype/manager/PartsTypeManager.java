package com.yunda.jx.pjwz.partsBase.partstype.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件规格型号业务类
 * <li>创建人：王治龙
 * <li>创建日期：2012-08-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value="partsTypeManager")
public class PartsTypeManager extends JXBaseManager<PartsType, PartsType>{
    
    private static final String REPAIRORGID = "repairOrgID";
    
    private static final String MATCODE = "matCode";
    
    private static final String TEXT = "text";

	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return false;
	}	
	
	/**
     * <li>说明：更新配件型号状态，用于启用和作废
     * <li>创建人：程梅
     * <li>创建日期：2012-8-23
     * <li>修改人：何涛
     * <li>修改日期：2016-04-08
     * <li>修改内容：代码优化
     * @param flag 标记是启用还是作废操作
     * @param ids 所选记录的idx数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateStatus(String flag, Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<PartsType> entityList = new ArrayList<PartsType>();
        PartsType type = null;
        for (Serializable id : ids) {
            type = getModelById(id);
            if ("start".equals(flag)) {
                type.setStatus(PartsType.STATUS_USE);// 启用
            } else {
                type.setStatus(PartsType.STATUS_INVALID);//作废
            }
            entityList.add(type);
        }
        this.saveOrUpdate(entityList);
    }
    
	/**
	 * 
	 * <li>说明：查询符合条件的规格型号列表信息并且进行分页
	 * <li>创建人：程梅
	 * <li>创建日期：2012-8-31
	 * <li>修改人： 程锐
	 * <li>修改日期：2012-09-13
	 * <li>修改内容：添加配件名称、规格型号条件模糊查询过滤
	 * 			   刘晓斌2013-11-14 使用查询缓存
	 * @param searchEntity 条件查询对象
	 * @param status 配件型号状态
	 * @return 返回分页查询列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
    public Page<PartsType> findPageList(SearchEntity<PartsType> searchEntity, String status) throws BusinessException{
		String totalHql = "select count(*) from PartsType t where t.recordStatus=0 ";
		String hql = "From PartsType t where t.recordStatus=0 ";
		StringBuffer awhere =  new StringBuffer();
		if(!StringUtil.isNullOrBlank(status)){
			awhere.append(" and t.status in ("+status+Constants.BRACKET_R);
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getJcpjbm())){
			awhere.append(" and t.jcpjbm = '"+searchEntity.getEntity().getJcpjbm() + "'");
		}
		
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getPartsClassIdx())){
			String sql = "select idx from pjwz_parts_class start with idx='"+searchEntity.getEntity().getPartsClassIdx()+"' connect by prior idx=parent_idx";			
			List<String> list = daoUtils.executeSqlQuery(sql);
			String idxs = "";
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					idxs +="'"+list.get(i)+"'";
					if(i<list.size()-1){
						idxs += ",";
					}
				}
			}
			awhere.append(" and t.partsClassIdx in("+idxs+Constants.BRACKET_R);
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getPartsName())){
			awhere.append(" and t.partsName like '%"+searchEntity.getEntity().getPartsName()+Constants.LIKE_PIPEI);
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getSpecificationModel())){
			awhere.append(" and t.specificationModel like '%"+searchEntity.getEntity().getSpecificationModel()+Constants.LIKE_PIPEI);
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getMatCode())){
			awhere.append(" and t.matCode like '%"+searchEntity.getEntity().getMatCode()+Constants.LIKE_PIPEI);
		}
		if(searchEntity.getEntity().getIsHasSeq() != null){
			awhere.append(" and t.isHasSeq ="+searchEntity.getEntity().getIsHasSeq());
		}
		if(searchEntity.getEntity().getIsHighterPricedParts() != null){
			awhere.append(" and t.isHighterPricedParts ="+searchEntity.getEntity().getIsHighterPricedParts());
		}
		if(searchEntity.getEntity().getJcpjbm() != null){
			awhere.append(" and t.jcpjbm = '"+searchEntity.getEntity().getJcpjbm() +"'");
		}
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.updateTime DESC");
        }
		totalHql += awhere;
		hql += awhere;
		if(enableCache()){
			return super.cachePageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
		} else {
			return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		}
    }
    
    /**
     * <li>说明：规格型号唯一性验证，【在所有有效记录中唯一】
     * <li>创建人：程梅
     * <li>创建日期：2012-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 配件规格型号对象
     * @return 错误信息
     */
	@Override
	public String[] validateUpdate(PartsType entity) {
		List<String> errMsg = new ArrayList<String>();
		PartsType type = this.getModelById(entity.getIdx()); //获取当前对象在数据库中的值
		if("".equals(entity.getIdx()) || !type.getSpecificationModel().equals(entity.getSpecificationModel()) || !type.getPartsName().equals(entity.getPartsName())){ //新增时验证
			String hql = "Select count(*) From PartsType where specificationModel='"+
			entity.getSpecificationModel()+"' and partsName='"+entity.getPartsName()+"' and recordStatus=0";
			int count = this.daoUtils.getCount(hql);
			if(count > 0){
				errMsg.add("规格型号【"+ entity.getSpecificationModel() +"】,配件名称【"+ entity.getPartsName()+"】已存在！");
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
	 * <li>说明：根据”配件名称“和”规格型号“获取配件型号idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param partsName 配件名称
	 * @param specificationModel 规格型号
	 * @param specificationModelCode 规格型号编码
	 * @return 配件型号idx
	 */
	public String getIdxByNameAndModel(String partsName, String specificationModel, String specificationModelCode) {
		String hql = "From PartsType Where recordStatus=0 And partsName = ? And specificationModel = ? And specificationModelCode = ?";
		Object object = daoUtils.findSingle(hql, new Object[]{partsName, specificationModel, specificationModelCode});
		if (null == object) {
			return null;
		}
		return ((PartsType) object).getIdx();
	}
	
	/**
	 * <li>说明：根据”规格型号“获取配件型号实体对象
	 * <li>创建人：何涛
	 * <li>创建日期：2015-05-21
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param specificationModel 规格型号
	 * @return PartsType
	 */
	public PartsType getModelBySpecificationModel(String specificationModel) {
        String hql = "From PartsType Where recordStatus = 0 And specificationModel = ?";
        return (PartsType) this.daoUtils.findSingle(hql, new Object[]{specificationModel});
	}
	
	/**
	 * <li>说明：查询PartsType在库存定额中的使用记录(查询的记录是已经启用的配件/材料规格型号)
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-28
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param searchEntity 查询实体对象
	 * @param typeTableName 查询的类型表名称
	 * @param warehouseIDX 库房IDX
	 * @return 分页列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Page findList(SearchEntity<PartsType> searchEntity,String typeTableName , String warehouseIDX) throws BusinessException{
		String hql = "From "+typeTableName+" t where t.recordStatus=0 and t.status=1";	
		String totalHql = "select count(*) From "+typeTableName+" t where t.recordStatus=0 and t.status=1";
		StringBuffer awhere =  new StringBuffer();
		if(!StringUtil.isNullOrBlank(warehouseIDX)){
				awhere.append(" and t.idx not in (select s.partsTypeIDX From StockQuota s where s.recordStatus=0 ");
				awhere.append(" and s.partsTypeIDX = t.idx");
				awhere.append(" and s.warehouseIDX = '"+warehouseIDX+Constants.BRACKET_MARK_R);
			}
			if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getPartsName())){
				awhere.append(" and t.partsName = '"+searchEntity.getEntity().getPartsName()+"'");
			}
			if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getSpecificationModel())){
				awhere.append(" and t.specificationModel = '"+searchEntity.getEntity().getSpecificationModel()+"'");
			}
			totalHql += awhere;
			hql += awhere;
			if(enableCache()){
				return super.cachePageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
			} else {
				return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
			}
    }
	/**
	 * <li>说明：查询配件定额中配件规格选择列表
	 * <li>创建人：程梅
	 * <li>创建日期：2012-9-3
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param searchEntity 条件查询对象
	 * @return 返回分页查询列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Page findListForQuota(SearchEntity<PartsType> searchEntity) throws BusinessException{
		String totalHql = "select count(*) from PartsType t where t.recordStatus=0 and t.status=1 and " +
				"t.idx not in (select q.partsTypeIDX from PartsQuota q where q.recordStatus=0)";
		String hql = "From PartsType t where t.recordStatus=0 and t.status=1 and " +
				"t.idx not in (select q.partsTypeIDX from PartsQuota q where q.recordStatus=0)";
		StringBuffer awhere =  new StringBuffer();
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getPartsName())){
			awhere.append(" and t.partsName like '%"+searchEntity.getEntity().getPartsName()+Constants.LIKE_PIPEI);
		}
		totalHql += awhere;
		hql += awhere;
		if(enableCache()){
			return super.cachePageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		} else {
			return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		}
    }
	/**
	 * 
	 * <li>说明：查询生成了定额信息的配件规格型号信息
	 * <li>创建人：程梅
	 * <li>创建日期：2012-10-17
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param searchEntity 条件查询对象
	 * @return 返回分页查询列表
	 * @throws BusinessException
	 */
	public Page<PartsType> findListInQuota(SearchEntity<PartsType> searchEntity) throws BusinessException{
		String totalHql = "select count(*) from PartsType t where t.recordStatus=0 and t.status=1 and " +
				"t.idx in (select q.partsTypeIDX from PartsQuota q where q.recordStatus=0)";
		String hql = "From PartsType t where t.recordStatus=0 and t.status=1 and " +
				"t.idx in (select q.partsTypeIDX from PartsQuota q where q.recordStatus=0)";
		StringBuffer awhere =  new StringBuffer();
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getSpecificationModel())){
			awhere.append(" and t.specificationModel like '%"+searchEntity.getEntity().getSpecificationModel()+Constants.LIKE_PIPEI);
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getPartsName())){
			awhere.append(" and t.partsName like '%"+searchEntity.getEntity().getPartsName()+Constants.LIKE_PIPEI);
		}
        Order[] orders = searchEntity.getOrders();
        awhere.append(HqlUtil.getOrderHql(orders));

		totalHql += awhere;
		hql += awhere;
		if(enableCache()){
			return super.cachePageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
		} else {
			return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		}
    }
	
	/**
	 * <li>说明：根据idx, 查询配件规格型号表对应的唯一记录,并返回实体
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-6-3
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param idx  配件规格型号表Id
	 * @return 配件型号对象
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public PartsType findPartsTypeForIdx(String idx) throws BusinessException{
		//如果参数为空,则返回null
		if(StringUtil.isNullOrBlank(idx)) return null;
		//如果参数不为空,则调用查询语句
		String hql = SqlMapUtil.getSql("pjwl_warehouse:findPartsTypeForIdx");
		Object[] param = new Object[]{idx};//配件规格型号表Idx
		List <PartsType> list = daoUtils.find(enableCache(), hql, param);
		if(list == null || list.size()<1){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	/**
	 * 
	 * <li>说明：查询用料申领中配件规格选择列表
	 * <li>创建人：程梅
	 * <li>创建日期：2012-9-11
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param searchEntity 条件查询对象
	 * @param materialApplyIDX 用料申领idx
	 * @return 分页对象
	 * @throws BusinessException
	 */
	public Page<PartsType> findListForMaterialApply(SearchEntity<PartsType> searchEntity,String materialApplyIDX) throws BusinessException{
		String totalHql = "select count(*) from PartsType t where t.recordStatus=0 and t.status=1 " ;
//		目前因工位是修改时才填入的数据，所以只判断在同一用料申领单下，配件规格型号唯一	
		//【都不做判断】
//		" and t.idx not in (select q.partsTypeIDX from MaterialApplyDetail q where q.recordStatus=0 and q.materialApplyIDX = '"+materialApplyIDX+"') ";
		String hql = "From PartsType t where t.recordStatus=0 and t.status=1 " ;
//				" and t.idx not in (select q.partsTypeIDX from MaterialApplyDetail q where q.recordStatus=0 and q.materialApplyIDX = '"+materialApplyIDX+"') ";
		StringBuffer awhere =  new StringBuffer();
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getPartsName())){
			awhere.append(" and t.partsName like '%"+searchEntity.getEntity().getPartsName()+Constants.LIKE_PIPEI);
		}
		totalHql += awhere;
		hql += awhere;
		if(enableCache()){
			return super.cachePageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
		} else {
			return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		}
    }
    /**
     * 
     * <li>说明：获取所有配件型号列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-14
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
     * @return List<PartsType> 配件型号列表
     */
	@SuppressWarnings("unchecked")
	public List<PartsType> getPartsTypeList(){
		return daoUtils.find(enableCache(), "from PartsType where recordStatus = 0 and status = " + PartsType.STATUS_USE);
    }
	/**
	 * 
	 * <li>说明：配件规格型号树列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-5-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param params 车型、修程、承修部门... 
	 * @return 配件规格型号树列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> findPartsTypeTreeData(Map<String,String> params) throws BusinessException {
		String hql = "from PartsType where recordStatus = 0 and status="+PartsType.STATUS_USE ;
		StringBuilder awhere = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			if (params.get("trainTypeIDX") != null) {
				awhere.append(" and idx in (select partsTypeIDX from TrainTypeToParts where recordStatus = 0 and trainTypeIDX = '")
					  .append(params.get("trainTypeIDX")).append("'");
					  
				if (params.get("repairClassIDX") != null) {
					awhere.append(" and idx in (select trainTypeToPartsIDX from RcClassToParts where recordStatus = 0 and repairClassIDX = '")
						  .append(params.get("repairClassIDX"))
						  .append(Constants.BRACKET_MARK_R);
				}
				awhere.append(Constants.BRACKET_R);
			}else if (params.get(REPAIRORGID) != null) {
				awhere.append(" and idx in (select partsTypeIDX from PartsRepairList where recordStatus = 0 and repairOrgID = '")
					  .append(String.valueOf(params.get(REPAIRORGID))).append(Constants.BRACKET_MARK_R);
			}else if (params.get("madeFactoryID") != null) {
				awhere.append(" and idx in (select partsTypeIDX from PartsOutsourcingList where recordStatus = 0 ")
//					  .append("and madeFactoryID = '")
//					  .append(params.get("madeFactoryID"))
//					  .append("'")
					  .append(Constants.BRACKET_R);
			}else if (params.get(MATCODE) != null) {
				awhere.append(" and matCode like '%")
				  .append(params.get(MATCODE))
				  .append("%')");
    		}else if (params.get("partsLoan") != null) {   //查询借用规格型号目录
                awhere.append(" and idx in (select partsTypeIDX from PartsLoanRegister where recordStatus = 0 ")
                  .append(Constants.BRACKET_R);
    		}
		}
		hql += awhere;
		List<PartsType> partsList = daoUtils.find(hql);
		List<HashMap> children = new ArrayList<HashMap>();
		for (PartsType type : partsList) {
			HashMap nodeMap = new HashMap();
            nodeMap.put("id", type.getIdx());  //配件类型主键
            if(!StringUtil.isNullOrBlank(type.getMatCode())){
            	nodeMap.put(TEXT, type.getPartsName()+Constants.BRACKET_L+type.getSpecificationModel()+")|"+type.getMatCode()); //配件类型名称  王斌添加：显示配件名称+规格型号名称+物料编码
            }else nodeMap.put(TEXT, type.getPartsName()+Constants.BRACKET_L+type.getSpecificationModel()+Constants.BRACKET_R); //配件类型名称  王斌添加：显示配件名称+规格型号名称
            nodeMap.put(MATCODE, type.getMatCode()); // 物料编码
            nodeMap.put("specificationModel", type.getSpecificationModel()); //
            nodeMap.put("specificationModelCode", type.getSpecificationModelCode()); //
            nodeMap.put("unit", type.getUnit()); //
            nodeMap.put("chartNo", type.getChartNo()); //
            nodeMap.put("isHighterPricedParts", type.getIsHighterPricedParts()); //
            nodeMap.put("leaf", true);
            nodeMap.put("partsName", type.getPartsName());//配件名称 王斌添加
            children.add(nodeMap);
		}
		return children;
	}
    
	/**
	 * <li>说明：基于不使用缓存的规格型号选择
	 * <li>创建人：王斌
	 * <li>创建日期：2014-1-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param query QueryCriteria对象
	 * @return 分页列表
	 * @throws BusinessException
	 */	
	@SuppressWarnings("unchecked")
	public Page<PartsType> findPageListForType(final QueryCriteria<PartsType> query) throws BusinessException{
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (Page<PartsType>)template.execute(new HibernateCallback(){
			public Page<PartsType> doInHibernate(Session s){
				try {
					return query.getPage(s, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
    }	
	/**
	 * 
	 * <li>说明：根据物料编码查询规格型号信息
	 * <li>创建人：程梅
	 * <li>创建日期：2014-8-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param matCode 物料编码
	 * @return 配件列表
	 */
	@SuppressWarnings("unchecked")
	public List<PartsType> getPartsTypeByMatCode(String matCode){
		String hql = "select a From PartsType a where a.recordStatus=0 and a.matCode='"+matCode+"'";
		List<PartsType> list = (List<PartsType>)daoUtils.find(hql);
		return list;
	}

	/**
	 * <li>说明：查询作业流程适用配件
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 程锐
	 * <li>修改日期：2015-12-18
	 * <li>修改内容：增加“只能选择启用状态的配件”的限制
	 * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @param isInWP true:表示查询作业流程适用配件 false:表示查询作业流程未适用配件
	 * @return 分页对象
	 */
	public Page findPageListForWP(SearchEntity<PartsType> searchEntity, String wPIDX, boolean isInWP) {
		StringBuilder sb = new StringBuilder();
		sb.append("From PartsType Where recordStatus = ").append(Constants.NO_DELETE).append(" and status = ").append(PartsType.STATUS_USE);
		if (isInWP) {
			sb.append(" And idx In (Select partsTypeIDX From WPUnionParts Where recordStatus = ");
		} else {
			sb.append(" And idx Not In (Select partsTypeIDX From WPUnionParts Where recordStatus = ");
		}
		sb.append(Constants.NO_DELETE).append(" And wPIDX ='").append(wPIDX).append(Constants.BRACKET_MARK_R);
		PartsType entity = searchEntity.getEntity();
		// 查询条件 - 编号
		if (!StringUtil.isNullOrBlank(entity.getSpecificationModelCode())) {
			sb.append(" And specificationModelCode Like '%").append(entity.getSpecificationModelCode()).append(Constants.LIKE_PIPEI);
		}
		// 查询条件 - 配件名称
		if (!StringUtil.isNullOrBlank(entity.getPartsName())) {
			sb.append(" And partsName Like '%").append(entity.getPartsName()).append(Constants.LIKE_PIPEI);
		}
		// 查询条件 - 规格型号名称
		if (!StringUtil.isNullOrBlank(entity.getSpecificationModel())) {
			sb.append(" And specificationModel Like '%").append(entity.getSpecificationModel()).append(Constants.LIKE_PIPEI);
		}
		// 查询条件 - 物料编码
		if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
			sb.append(" And matCode Like '%").append(entity.getMatCode()).append(Constants.LIKE_PIPEI);
		}
		// 排序字段
		sb.append(HqlUtil.getOrderHql(searchEntity.getOrders()));
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：查询作业流程适用配件
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
     * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @return 分页对象
	 */
	public Page findPageListForWP(SearchEntity<PartsType> searchEntity, String wPIDX) {
		return this.findPageListForWP(searchEntity, wPIDX, true);
	}
	
	
	/**
	 * <li>说明：查询作业流程适用配件 - 候选配件
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
     * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @return 分页对象
	 */
	public Page findPageListForWPSelect(SearchEntity<PartsType> searchEntity, String wPIDX) {
		return this.findPageListForWP(searchEntity, wPIDX, false);
	}
    
	/**
	 * <li>说明：查询自修目录配件中检修班组为当前班组的配件规格型号  findPartsTypeTreeData
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param params 过滤条件
	 * @return List<HashMap> 规格型号列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> findRepairListPartsType(Map<String,String> params) throws BusinessException {
		String hql = "from PartsType where recordStatus = 0 " ;
		StringBuilder awhere = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			if (params.get(REPAIRORGID) != null) {
				awhere.append(" and idx in (select partsTypeIDX from PartsRepairList where recordStatus = 0 and repairOrgID = '")
					  .append(String.valueOf(params.get(REPAIRORGID))).append(Constants.BRACKET_MARK_R);
			}
		}
		hql += awhere;
		List<PartsType> partsList = daoUtils.find(hql);
		List<HashMap> children = new ArrayList<HashMap>();
		for (PartsType type : partsList) {
			HashMap nodeMap = new HashMap();
            nodeMap.put("id", type.getIdx());  //配件类型主键
            if(!StringUtil.isNullOrBlank(type.getMatCode())){
            	nodeMap.put(TEXT, type.getPartsName()+Constants.BRACKET_L+type.getSpecificationModel()+")|"+type.getMatCode()); //配件类型名称  王斌添加：显示配件名称+规格型号名称+物料编码
            }else nodeMap.put(TEXT, type.getPartsName()+Constants.BRACKET_L+type.getSpecificationModel()+Constants.BRACKET_R); //配件类型名称  王斌添加：显示配件名称+规格型号名称
            nodeMap.put(MATCODE, type.getMatCode()); // 物料编码
            nodeMap.put("specificationModel", type.getSpecificationModel()); //
            nodeMap.put("specificationModelCode", type.getSpecificationModelCode()); //
            nodeMap.put("unit", type.getUnit()); //
            nodeMap.put("chartNo", type.getChartNo()); //
            nodeMap.put("isHighterPricedParts", type.getIsHighterPricedParts()); //
            nodeMap.put("leaf", true);
            nodeMap.put("partsName", type.getPartsName());//配件名称 王斌添加
            children.add(nodeMap);
		}
		return children;
	}
    /**
     * 
     * <li>说明：获取所有配件规格型号列表【用于手持终端接口】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsWHRegister> 分页对象
     * @throws BusinessException
     */
    public Page<PartsType> findPartsTypeList(SearchEntity<PartsType> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsType t where t.recordStatus=0 and t.status=").append(PartsType.STATUS_USE);
        PartsType type = searchEntity.getEntity() ;
        String specificationModel = type.getSpecificationModel() ;
        String jcpjbm = type.getJcpjbm();
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(specificationModel)){
            awhere.append(" and (t.specificationModel like '%").append(specificationModel).append("%' or t.partsName like '%").append(specificationModel).append("%' )") ;
        }
        // 大部件编码
        if(!StringUtil.isNullOrBlank(jcpjbm)){
            awhere.append(" and t.jcpjbm = '"+jcpjbm+"'");
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.updateTime desc");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }

    /**
     * <li>说明：分页查询，查询当前系统操作人员所在班组可以承修的配件型号（工位终端）
     * <li>创建人：何涛
     * <li>创建日期：2016-3-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @param orders 排序字段
     * @return 配件型号集合
     */
    public Page<PartsType> findPageForPartsType(int start, int limit, Order[] orders) {
        StringBuilder sb = new StringBuilder("Select a From PartsType a, PartsRepairList b Where a.idx = b.partsTypeIDX");
        sb.append(" And a.recordStatus = 0 And b.recordStatus = 0");
        // 查询当前系统操作员所在班组承修的配置
        sb.append(" And b.repairOrgID = '").append(SystemContext.getOmOrganization().getOrgid()).append("'");
        
        // 只查询待修配件的配件型号
        sb.append(" And a.idx In (");
        sb.append(" Select Distinct partsTypeIDX From PartsAccount Where recordStatus = 0");
        sb.append(" And partsStatus Like '").append(PartsAccount.PARTS_STATUS_DX).append("%'");
        sb.append(" )");
        
        // 排序
        if (null == orders) {
            sb.append(" Order By a.partsName");
        } else {
            sb.append(" Order By a." + orders[0].toString());
        }
        
        String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
        return super.findPageList(totalHql, sb.toString(), start, limit);
    }
    
    /**
     * 
     * <li>说明：更新规格型号的机车零部件编码
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param Jcpjbm
     * @param ids
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateJcpjbm(String Jcpjbm,PartsType[] entitys) throws BusinessException, NoSuchFieldException {
        List<PartsType> entityList = new ArrayList<PartsType>();
        for (PartsType partsType : entitys) {
        	partsType = getModelById(partsType.getIdx());
        	partsType.setJcpjbm(Jcpjbm);
            entityList.add(partsType);
        }
        this.saveOrUpdate(entityList);
    }
    
    
    /**
     * <li>说明：通过大部件编码查询所有启用的关联规格型号
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param Jcpjbm 大部件编码
     */
    public List getPartsTypeListByJcpjbm(String Jcpjbm){
        String hql = "From PartsType Where recordStatus = 0 and status = "+PartsType.STATUS_USE+" And jcpjbm = ?";
        return this.daoUtils.find(hql, new Object[]{Jcpjbm});
    }
}