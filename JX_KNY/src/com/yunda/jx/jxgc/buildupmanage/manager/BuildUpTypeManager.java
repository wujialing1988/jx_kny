
package com.yunda.jx.jxgc.buildupmanage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpToPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
import com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType;
import com.yunda.jx.jxgc.configmanage.entity.TrainConfigInfo;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.jxpz.utils.CodeRuleUtil;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：BuildUpType业务类,组成型号
 * <li>创建人：程锐
 * <li>创建日期：2012-10-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "buildUpTypeManager")
public class BuildUpTypeManager extends JXBaseManager<BuildUpType, BuildUpType> {
	
	/** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /** 机车车型编码业务类 */
    @Resource
    private TrainTypeManager trainTypeManager;
    
    /** 配件规格型号业务类 */
    @Resource
    private PartsTypeManager partsTypeManager;
    
    /** 组成位置业务类 */
    @Resource
    private BuildUpPlaceManager buildUpPlaceManager;
    
    /** 可安装组成型号业务类 */
    @Resource
    private FixBuildUpTypeManager fixBuildUpTypeManager;
     
    /**
     * <li>说明：分页查询，返回实体类的分页列表对象，基于单表实体类分页查询
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-11-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param SearchEntity<T> 包装了实体类查询条件的对象
     * @return Page<E> 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<BuildUpType> findPage(final SearchEntity<BuildUpType> searchEntity) throws BusinessException {
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<BuildUpType>) template.execute(new HibernateCallback() {
            
            public Page<BuildUpType> doInHibernate(Session s) {
                try {
                    BuildUpType entity = searchEntity.getEntity();
                    // 过滤逻辑删除记录
                    BeanUtils.forceSetProperty(entity, EntityUtil.RECORD_STATUS, Constants.NO_DELETE);
                    // 过滤掉idx、siteID、creator、createTime、updator、updateTime的查询条件
                    Example exp =
                        Example.create(entity).excludeProperty(EntityUtil.IDX).excludeProperty(EntityUtil.SITE_ID)
                            .excludeProperty(EntityUtil.CREATOR).excludeProperty(EntityUtil.CREATE_TIME)
                            .excludeProperty(EntityUtil.UPDATOR).excludeProperty(EntityUtil.UPDATE_TIME)
                            .excludeProperty("trainTypeIDX").excludeProperty("partsTypeIDX").enableLike().enableLike(
                                MatchMode.ANYWHERE);
                    // 总记录数
                    Criteria totalCriteria = s.createCriteria(entity.getClass()).add(exp);
                    // 分页列表
                    Criteria criteria =
                        s.createCriteria(entity.getClass()).add(exp).setFirstResult(searchEntity.getStart())
                            .setMaxResults(searchEntity.getLimit());
                    if (entity.getTrainTypeIDX() != null) {
                        totalCriteria.add(Expression.eq("trainTypeIDX", entity.getTrainTypeIDX()));
                        criteria.add(Expression.eq("trainTypeIDX", entity.getTrainTypeIDX()));
                    }
                    if (entity.getPartsTypeIDX() != null) {
                        totalCriteria.add(Expression.eq("partsTypeIDX", entity.getPartsTypeIDX()));
                        criteria.add(Expression.eq("partsTypeIDX", entity.getPartsTypeIDX()));
                    }
                    // 设置排序规则
                    Order[] orders = searchEntity.getOrders();
                    if (orders != null) {
                        for (Order order : orders) {
                            criteria.addOrder(order);
                        }
                    }
                    criteria.addOrder(Order.desc("updateTime"));
                    // 查询总记录数
                    int total =
                        ((Integer) totalCriteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
                    return new Page<BuildUpType>(total, criteria.list());
                } catch (Exception e) {
                	ExceptionUtil.process(e,logger);
                }
                return null;
            }
        });
    }
    
    /**
     * <li>说明：分页查询组成型号，返回实体类的分页列表对象，基于单表实体类分页查询
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param SearchEntity<T> 包装了实体类查询条件的对象
     * @return Page<E> 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<BuildUpType> findPageBuildType(final SearchEntity<BuildUpType> searchEntity) throws BusinessException {
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<BuildUpType>) template.execute(new HibernateCallback() {
            
            public Page<BuildUpType> doInHibernate(Session s) {
                try {
                    String systemOrgcode = JXSystemProperties.OVERSEA_ORGCODE;
                    OmOrganization systemOrg = OmOrganizationSelectManager.getOrgByOrgcode(systemOrgcode);
                    StringBuffer sql = new StringBuffer();
                    sql.append(" Train_Type_IDX in (select u.train_type_idx from JCZL_UNDERTAKE_TRAIN_TYPE u ");
                    sql.append("where u.record_status=0 and u.undertake_orgid='").append(systemOrg.getOrgid()).append(
                        "')");
                    BuildUpType entity = searchEntity.getEntity();
                    // 过滤逻辑删除记录
                    BeanUtils.forceSetProperty(entity, EntityUtil.RECORD_STATUS, Constants.NO_DELETE);
                    // 过滤掉idx、siteID、creator、createTime、updator、updateTime的查询条件
                    Example exp =
                        Example.create(entity).excludeProperty(EntityUtil.IDX).excludeProperty(EntityUtil.SITE_ID)
                            .excludeProperty(EntityUtil.CREATOR).excludeProperty(EntityUtil.CREATE_TIME)
                            .excludeProperty(EntityUtil.UPDATOR).excludeProperty(EntityUtil.UPDATE_TIME)
                            .excludeProperty("trainTypeIDX").excludeProperty("partsTypeIDX").enableLike().enableLike(
                                MatchMode.ANYWHERE);
                    // 总记录数
                    Criteria totalCriteria = s.createCriteria(entity.getClass()).add(exp);
                    // 分页列表
                    Criteria criteria =
                        s.createCriteria(entity.getClass()).add(exp).setFirstResult(searchEntity.getStart())
                            .setMaxResults(searchEntity.getLimit());
                    if (entity.getType() != null && entity.getType() == BuildUpType.TYPE_TRAIN) { // 机车组成（只查询承修车型对应的组成型号）
                        totalCriteria.add(Restrictions.sqlRestriction(sql.toString()));
                        criteria.add(Restrictions.sqlRestriction(sql.toString()));
                    }
                    // 设置排序规则
                    Order[] orders = searchEntity.getOrders();
                    if (orders != null) {
                        for (Order order : orders) {
                            criteria.addOrder(order);
                        }
                    }
                    criteria.addOrder(Order.asc("buildUpTypeCode"));
                    // 查询总记录数
                    int total =
                        ((Integer) totalCriteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
                    return new Page<BuildUpType>(total, criteria.list());
                } catch (Exception e) {
                	ExceptionUtil.process(e,logger);
                }
                return null;
            }
        });
    }
    
    /**
     * <li>说明：添加标准组成记录
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-11-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param int type ：记录类型，BuildUpType.TYPE_TRAIN机车组成型号,BuildUpType.TYPE_PARTS配件组成型号
     * @param id 机车主键或配件主键             
     * @return void
     * @throws Exception 
     */
    public void addDefault(int type, Serializable id) throws Exception {
        switch (type) {
            case BuildUpType.TYPE_TRAIN: // 机车组成型号
                TrainType trainType = trainTypeManager.getModelById(id);
                this.addDefault(trainType);
                break;
            case BuildUpType.TYPE_PARTS: // 配件组成型号
                PartsType partsType = partsTypeManager.getModelById(id);
                this.addDefault(partsType);
                break;
            default:
                throw new BusinessException(
                    "addDefault:type参数错误，组成型号记录类型只有（BuildUpType.TYPE_TRAIN机车组成型号,BuildUpType.TYPE_PARTS配件组成型号）");
        }
    }
    
    /**
     * <li>说明：按组成类型批量添加标准组成记录
     * <li>创建人：程锐
     * <li>创建日期：2013-1-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param type 组成类型
     * @return void
     * @throws Exception 
     */
    public void addAllDefault(int type) throws Exception {
        switch (type) {
            case BuildUpType.TYPE_TRAIN: // 机车组成型号
                List<TrainType> trainTypeList = trainTypeManager.getTrainTypeList();
                if (trainTypeList != null && trainTypeList.size() > 0) {
                    for (TrainType trainType : trainTypeList) {
                        this.addDefault(trainType);
                    }
                }
                break;
            case BuildUpType.TYPE_PARTS: // 配件组成型号
                List<PartsType> partsTypeList = partsTypeManager.getPartsTypeList();
                if (partsTypeList != null && partsTypeList.size() > 0) {
                    for (PartsType partsType : partsTypeList) {
                        this.addDefault(partsType);
                    }
                }
                break;
            default:
                throw new BusinessException(
                    "addDefault:type参数错误，组成型号记录类型只有（BuildUpType.TYPE_TRAIN机车组成型号,BuildUpType.TYPE_PARTS配件组成型号）");
        }
    }
    
    /**
     * <li>说明：根据车型添加标准机车组成型号，一个车型对应的组成型号只能存在一个标准组成 组成型号编码buildUpTypeCode ==
     * 简称shortName 组成型号名称buildUpTypeName == 车型名称typeName
     * 车型英文简称trainTypeShortName == 简称shortName
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-11-13
     * <li>修改人：程锐   
     * <li>修改日期：2013-03-27
     * <li>修改内容：修改组成型号编码buildUpTypeCode ==业务规则生成编码，组成型号名称buildUpTypeName == 车型简称shortName，组成型号描述buildUpTypeDesc == 车型名称typeName
     * 
     * @param TrainType trainType：车型
     * @return void
     * @throws Exception 
     */
    public void addDefault(TrainType trainType) throws Exception {
        BuildUpType buildType = new BuildUpType();
//        buildType.setBuildUpTypeCode(trainType.getShortName());
//        buildType.setBuildUpTypeName(trainType.getTypeName());
        buildType.setBuildUpTypeCode(CodeRuleUtil.getRuleCode("JXGC_BUILDUP_TYPE_BUILDUP_TYPE_CODE"));
        buildType.setBuildUpTypeName(trainType.getShortName());
        buildType.setBuildUpTypeDesc(trainType.getTypeName());
        buildType.setTrainTypeShortName(trainType.getShortName());
        buildType.setType(BuildUpType.TYPE_TRAIN);
        buildType.setTrainTypeIDX(trainType.getTypeID());
        buildType.setTrainTypeShortName(trainType.getShortName());
        buildType.setStatus(BuildUpType.NEW_STATUS);
        // 如果未存在标准组成且生成的组成编码唯一，将该记录设置为标准组成
        if (!hasDefaultBuildUp(buildType) && validateUpdate(buildType) == null) {
            buildType.setIsDefault(BuildUpType.ISDEFAULT_YES);
            EntityUtil.setNoDelete(buildType);
            EntityUtil.setSysinfo(buildType);
            this.daoUtils.saveOrUpdate(buildType);
        }
    }
    
    /**
     * <li>说明：根据配件规格型号添加默认配件组成型号，一个配件对应的组成型号只能存在一个缺省标准组成 组成型号编码buildUpTypeCode ==
     * 规格型号specificationModel 组成型号名称buildUpTypeName == 配件名称partsName
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-11-13
     * <li>修改人：程锐   
     * <li>修改日期：2013-03-27
     * <li>修改内容：修改组成型号编码buildUpTypeCode ==业务规则生成编码,组成型号名称buildUpTypeName == 规格型号specificationModel，组成型号描述buildUpTypeDesc == 配件名称partsName,添加专业类型ID和专业类型名称
     * 
     * @param PartsType partsType：配件规格型号
     * @return void
     * @throws Exception 
     */
    public void addDefault(PartsType partsType) throws Exception {
        BuildUpType buildType = new BuildUpType();
//        buildType.setBuildUpTypeCode(partsType.getSpecificationModel());
//        buildType.setBuildUpTypeName(partsType.getPartsName());
        buildType.setBuildUpTypeCode(CodeRuleUtil.getRuleCode("JXGC_BuildUp_Type_Code"));
        buildType.setBuildUpTypeName(partsType.getSpecificationModel());
        buildType.setBuildUpTypeDesc(partsType.getPartsName());        
        buildType.setType(BuildUpType.TYPE_PARTS);
        buildType.setPartsTypeIDX(partsType.getIdx());
        buildType.setPartsName(partsType.getPartsName());
        buildType.setSpecificationModel(partsType.getSpecificationModel());
        buildType.setProfessionalTypeIDX(partsType.getProfessionalTypeIdx());
        buildType.setProfessionalTypeName(partsType.getProfessionalTypeName());
        buildType.setStatus(BuildUpType.NEW_STATUS);
        // 如果未存在标准组成且生成的组成编码唯一，将该记录设置为标准组成
        if (!hasDefaultBuildUp(buildType) && validateUpdate(buildType) == null) {
            buildType.setIsDefault(BuildUpType.ISDEFAULT_YES);
            EntityUtil.setNoDelete(buildType);
            EntityUtil.setSysinfo(buildType);
            this.daoUtils.saveOrUpdate(buildType);
        }
    }
    
    /**
     * <li>说明：根据车型主键或配件规格型号主键，查找该车型或配件是否已经存在缺省的标准组成
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-11-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param BuildUpType buildUpType：组成型号对象
     * @return boolean true：已存在缺省标准组成，false：不存在
     * @throws BusinessException
     */
    public boolean hasDefaultBuildUp(BuildUpType buildUpType) throws BusinessException {
        BuildUpType exp = new BuildUpType();
        exp.setIsDefault(BuildUpType.ISDEFAULT_YES);
        switch (buildUpType.getType()) {
            case BuildUpType.TYPE_TRAIN:
                exp.setTrainTypeIDX(buildUpType.getTrainTypeIDX());
                break;
            case BuildUpType.TYPE_PARTS:
                exp.setPartsTypeIDX(buildUpType.getPartsTypeIDX());
                break;
            default:
                throw new BusinessException(
                    "hasDefaultBuildUp:type参数错误，组成型号记录类型只有（BuildUpType.TYPE_TRAIN机车组成型号,BuildUpType.TYPE_PARTS配件组成型号）");
        }
        List<BuildUpType> buildList = this.findList(exp, null);
        if (buildList == null || buildList.size() < 1)
            return false;
        if (buildList.size() > 1)
            return true;
        return buildList.get(0).getIdx().equals(buildUpType.getIdx()) ? false : true;
    }
    
    /**
     * <li>说明：更新组成型号记录的业务状态，可变更为启用或废弃状态
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-11-13
     * <li>修改人：程锐
     * <li>修改日期：2012-11-29
     * <li>修改内容：对缺省组成作废时，将相同车型或配件类型的下一条记录设为缺省
     * 
     * @param int
     *            status：业务状态，BuildUpType.USE_STATUS启用，BuildUpType.NULLIFY_STATUS废弃
     * @return void
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void updateStatus(int status, Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<BuildUpType> entityList = new ArrayList<BuildUpType>();
        for (Serializable id : ids) {
            BuildUpType buildUpType = this.getModelById(id);
            buildUpType = EntityUtil.setSysinfo(buildUpType); // 根据IDX主键设置创建人创建时间等基本信息
            switch (status) {
                case BuildUpType.USE_STATUS: // 启用
                    buildUpType.setStatus(BuildUpType.USE_STATUS);
                    break;
                case BuildUpType.NULLIFY_STATUS: // 作废
                    buildUpType.setStatus(BuildUpType.NULLIFY_STATUS);
                    // 对缺省组成作废时，将相同车型或配件类型的下一条记录设为缺省
                    if (buildUpType.getIsDefault() != null && buildUpType.getIsDefault() == BuildUpType.ISDEFAULT_YES) {
                        List<BuildUpType> list = null;
                        switch (buildUpType.getType()) {
                            case BuildUpType.TYPE_TRAIN:
                                String hql =
                                    "from BuildUpType where trainTypeIDX = '" + buildUpType.getTrainTypeIDX()
                                        + "' and status != " + BuildUpType.NULLIFY_STATUS
                                        + " and recordStatus = 0 and type = " + BuildUpType.TYPE_TRAIN
                                        + " and idx != '" + id + "'";
                                list = daoUtils.find(hql);
                                break;
                            case BuildUpType.TYPE_PARTS:
                                hql =
                                    "from BuildUpType where partsTypeIDX = '" + buildUpType.getPartsTypeIDX()
                                        + "' and status != " + BuildUpType.NULLIFY_STATUS
                                        + " and recordStatus = 0 and type = " + BuildUpType.TYPE_PARTS
                                        + " and idx != '" + id + "'";
                                list = daoUtils.find(hql);
                                break;
                            default:
                                throw new BusinessException(buildUpType.getBuildUpTypeName() + "组成型号：不能更新为该业务状态值"
                                    + status);
                        }
                        if (list != null && list.size() > 0) {
                            list.get(0).setIsDefault(BuildUpType.ISDEFAULT_YES);
                        }
                        buildUpType.setIsDefault(BuildUpType.ISDEFAULT_NO);
                    }
                    break;
                default:
                    throw new BusinessException(buildUpType.getBuildUpTypeName() + "组成型号：不能更新为该业务状态值" + status);
            }
            entityList.add(buildUpType);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * <li>说明：添加默认组成前验证是否存在对应机车（配件）的组成信息，存在则不能再添加默认组成;验证按车型简称或配件规格型号生成的组成编码是否唯一，不唯一则不能再添加默认组成
     * <li>创建人：程锐
     * <li>创建日期：2012-11-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param id 车型id或配件id
     * @param type 组成类型
     * @return 验证信息
     * @throws BusinessException
     */
    public String[] validateHasBuildUpType(Serializable id, int type) throws BusinessException {
        String[] errs = null;
        List<BuildUpType> trainList = null;
        List<BuildUpType> partsList = null;
        TrainType trainType = null;
        PartsType partsType = null;
        switch (type) {
            case BuildUpType.TYPE_TRAIN:
                trainList = getTrainBuildList(id);
                trainType = trainTypeManager.getModelById(id);
                break;
            case BuildUpType.TYPE_PARTS:
                partsList = getPartsBuildList(id);
                partsType = partsTypeManager.getModelById(id);
                break;
            default:
                errs = new String[1];
                errs[0] = "组成类型值为" + type + "，不是合法范围内的值。";
                return errs;
        }
        if (trainList != null && trainList.size() > 0) {
            errs = new String[1];
            errs[0] = "机车简称【" + trainList.get(0).getTrainTypeShortName() + "】已存在对应机车组成，不能再添加默认组成";
            return errs;
        }
        if (partsList != null && partsList.size() > 0) {
            errs = new String[1];
            errs[0] = "配件名称【" + partsList.get(0).getPartsName() + "】已存在对应配件组成，不能再添加默认组成";
            return errs;
        }
        if (trainType != null) {
            BuildUpType buildUpType = new BuildUpType();
            buildUpType.setBuildUpTypeName(trainType.getShortName());
            errs = validateUpdate(buildUpType);
            return errs;
        }
        if (partsType != null) {
            BuildUpType buildUpType = new BuildUpType();
            buildUpType.setBuildUpTypeName(partsType.getSpecificationModel());
            errs = validateUpdate(buildUpType);
            return errs;
        }
        return null;
    }
    
    /**
     * <li>说明：根据车型主键获取机车组成信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param id 车型主键
     * @return List<BuildUpType> 机车组成信息列表
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpType> getTrainBuildList(Serializable id) {
        String hql = "from BuildUpType where trainTypeIDX = '" + id + "' and status != " + BuildUpType.NULLIFY_STATUS +
        			 " and recordStatus = 0 and type = " + BuildUpType.TYPE_TRAIN;
		List<BuildUpType> list = daoUtils.find(hql);
		return list;
    }
    
    /**
	 * <li>说明：根据车型主键获取机车组成信息
	 * <li>创建人：程锐
	 * <li>创建日期：2012-11-14
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param trainTypeIDX 车型主键
	 * @param idx 组成主键
	 * @return List<BuildUpType> 机车组成信息列表
	 */
    @SuppressWarnings("unchecked")
    public List<BuildUpType> getTrainBuildList(Serializable trainTypeIDX, String idx) {
        String hql = "from BuildUpType where trainTypeIDX = '" + trainTypeIDX + "' and status != " + BuildUpType.NULLIFY_STATUS +
           			 " and recordStatus = 0 and type = " + BuildUpType.TYPE_TRAIN + " and idx != '" + idx + "'";
		List<BuildUpType> list = daoUtils.find(hql);
        return list;
    }
    
    /**
     * <li>说明：根据配件主键获取配件组成信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param id 配件主键
     * @return List<BuildUpType> 配件组成信息列表
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpType> getPartsBuildList(Serializable id) {
        String hql = "from BuildUpType where partsTypeIDX = '" + id + "' and status != " + BuildUpType.NULLIFY_STATUS +
            		 " and recordStatus = 0 and type = " + BuildUpType.TYPE_PARTS;
        List<BuildUpType> list = daoUtils.find(hql);
        return list;
    }
    
    /**
     * <li>说明：根据配件主键获取配件组成信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param partsTypeIDX 配件主键
     * @param idx 组成主键
     * @return List<BuildUpType> 配件组成信息列表
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpType> getPartsBuildList(Serializable partsTypeIDX, String idx) {
        String hql = "from BuildUpType where partsTypeIDX = '" + partsTypeIDX + "' and status != " + BuildUpType.NULLIFY_STATUS +
        			 " and recordStatus = 0 and type = " + BuildUpType.TYPE_PARTS + " and idx != '" + idx + "'";
        List<BuildUpType> list = daoUtils.find(hql);
        return list;
    }
    
    /**
     * <li>说明：新增修改保存前的实体对象前的验证业务
     * <li>创建人：程锐
     * <li>创建日期：2012-10-24
     * <li>修改人：刘晓斌
     * <li>修改日期：2012-11-14
     * <li>修改内容：增加业务逻辑，新增或更新记录时需要验证组成型号编码唯一
     * 
     * @param buildUpType 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(BuildUpType buildUpType) throws BusinessException {
        String[] errs = null;
        String buildUpTypeName = buildUpType.getBuildUpTypeName();
        List<BuildUpType> list = getBuildUpListByCode(buildUpTypeName, buildUpType.getIdx());
        if (list != null && list.size() > 0) {
            errs = new String[1];
            errs[0] = "组成型号已存在！";
            return errs;
        }
        return null;
    }
    
    /**
     * <li>说明：根据组成编码获取组成信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param buildUpTypeName 组成名称
     * @param idx 组成主键
     * @return List<BuildUpType> 组成型号信息列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpType> getBuildUpListByCode(String buildUpTypeName, String idx) {
        String hql = "from BuildUpType where recordStatus = 0 and buildUpTypeName='" + buildUpTypeName + "'";
        if (!StringUtil.isNullOrBlank(idx)) {
            hql += " and idx != '" + idx + "'";
        }
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：设置默认缺省组成前作验证，验证该组成的状态是否为作废，作废的组成不能设为缺省
     * <li>创建人：程锐
     * <li>创建日期：2012-11-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param id 需设置默认缺省组成的组成主键
     * @return 验证信息
     * @throws BusinessException
     */
    public String[] validateDefault(Serializable id) throws BusinessException {
        BuildUpType buildUpType = getModelById(id);
        String[] errs = null;
        // 验证该车型或配件是否已存在默认缺省组成，如有不允许再设置
        // boolean hasDefault = false;
        // switch (buildUpType.getType()) {
        // case BuildUpType.TYPE_PARTS:
        // hasDefault = this.hasDefaultBuildUp(buildUpType);
        // break;
        // case BuildUpType.TYPE_TRAIN:
        // hasDefault = this.hasDefaultBuildUp(buildUpType);
        // break;
        // default:
        // errs = new String[1];
        // errs[0] = buildUpType.getBuildUpTypeName() + "的组成类型值为" +
        // buildUpType.getType() + "，不是合法范围内的值。";
        // return errs;
        // }
        // if (hasDefault) {
        // errs = new String[1];
        // errs[0] = buildUpType.getBuildUpTypeName() +
        // "不能设置为缺省标准组成，该车型（配件规格型号）已存在一条缺省标准组成";
        // return errs;
        // }
        if (buildUpType.getStatus() == BuildUpType.NULLIFY_STATUS) {
            errs = new String[1];
            errs[0] = buildUpType.getBuildUpTypeName() + "的组成状态为作废,不能设为默认缺省组成";
            return errs;
        }
        return null;
    }
    
    /**
     * <li>说明：设置默认缺省组成,同时设置其他相同机车主键或配件主键的组成的缺省组成为否
     * <li>创建人：程锐
     * <li>创建日期：2012-11-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param buildUpType 需设置默认缺省组成的组成实体对象
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void setIsDefault(BuildUpType buildUpType) throws BusinessException, NoSuchFieldException {
        String trainTypeIdx = buildUpType.getTrainTypeIDX();
        String partsTypeIdx = buildUpType.getPartsTypeIDX();
        int type = buildUpType.getType();
        List<BuildUpType> list = null;
        if (type == BuildUpType.TYPE_TRAIN) {
            list = getTrainBuildList(trainTypeIdx, buildUpType.getIdx());
        } else if (type == BuildUpType.TYPE_PARTS) {
            list = getPartsBuildList(partsTypeIdx, buildUpType.getIdx());
        }
        if (list != null && list.size() > 0) {
            for (BuildUpType build : list) {
                build.setIsDefault(BuildUpType.ISDEFAULT_NO);
                saveOrUpdate(build);
            }
        }
        buildUpType.setIsDefault(BuildUpType.ISDEFAULT_YES);
        saveOrUpdate(buildUpType);
    }
    
    /**
     * <li>说明：根据安装位置获取默认配件组成型号信息
     * <li>创建人：程锐
     * <li>创建日期：2012-10-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param fixPlaceIdx 安装位置主键
     * @return BuildUpType 配件组成型号实体对象
     * @throws BusinessException
     */
    public BuildUpType getDefaultBuildUpTypeByFixPlace(String fixPlaceIdx) throws BusinessException {
        String hql =  "from BuildUpType where idx in (select buildUpTypeIdx from FixBuildUpType where fixPlaceIdx = '" +
        			  fixPlaceIdx + "' and isDefault = " + FixBuildUpType.DEFAULT + " and recordStatus=0) and recordStatus=0 and status=" +
        			  BuildUpType.USE_STATUS + " and type = " + BuildUpType.TYPE_PARTS;
        return findSingle(hql);
    }
    
    /**
     * <li>说明：根据组成类型和组成编码获取标准组成信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param trainType 组成类型
     * @param typeIDX 车型或配件主键
     * @return BuildUpType 组成实体对象
     * @throws BusinessException
     */
    public BuildUpType getBuildUpTypeByTypeIDX(String type, String typeIDX) throws BusinessException {
        String hql = "from BuildUpType where recordStatus = 0 and status= " + BuildUpType.USE_STATUS + " and isDefault = "
                + BuildUpType.ISDEFAULT_YES + " and type = " + type;
        if(type.equals(String.valueOf(BuildUpType.TYPE_TRAIN))){
            hql+= " and trainTypeIDX = '" + typeIDX + "'";
        }else if(type.equals(String.valueOf(BuildUpType.TYPE_PARTS))){
            hql+= " and partsTypeIDX = '" + typeIDX + "'";
        }            
        return findSingle(hql);
    }
    
    /**
     * <li>说明：构建组成位置树
     * <li>创建人：程锐
     * <li>创建日期：2012-11-12
     * <li>修改人：程锐
     * <li>修改日期：2013-1-17
     * <li>修改内容：修改组成树的构造方法
     * 
     * @param parentIDX 上级父节点idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @param ctx 应用程序根目录
     * @return List<HashMap> 返回树的集合
     * @throws BusinessException
     */
    public List<HashMap<String, Object>> tree(String parentIDX, String partsBuildUpTypeIdx, String ctx, String isPartsBuildUp) throws BusinessException {
        if ("true".equals(isPartsBuildUp)) {
           return findPartsBuildTypeNode(parentIDX, ctx);
        }
        // 获取下级位置列表
        List<BuildUpToPlace> buildPlaceList = this.buildUpPlaceManager.childPlaceList(partsBuildUpTypeIdx, parentIDX);
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        if (buildPlaceList == null || buildPlaceList.size() < 1) return children;
        for (BuildUpToPlace buildUpToPlace : buildPlaceList) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            Boolean isLeaf = true;
            // 查询此位置节点是否有下级位置
            List<BuildUpToPlace> childPlaceList = this.buildUpPlaceManager.childPlaceList(buildUpToPlace.getBuildUpTypeIdx(), 
            																			  buildUpToPlace.getBuildUpPlaceIdx());
            if (childPlaceList != null && childPlaceList.size() > 0) {
                isLeaf = false;
            }
            nodeMap.put("isPartsBuildUp", "false");// 是否下挂组成
            // 此位置为虚拟位置，找出其安装的虚拟组成并根据组成获取组成的下级位置列表以判断是否为叶子节点
            if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_VIRTUAL) {
                List<FixBuildUpType> fixBuildUpTypeList =
                    this.fixBuildUpTypeManager.getFixBuildList(buildUpToPlace.getBuildUpPlaceIdx());
                if (fixBuildUpTypeList != null && fixBuildUpTypeList.size() > 0) {
                    isLeaf = false;
                    nodeMap.put("isPartsBuildUp", "true");// 是否下挂组成
                    nodeMap.put("virtualBuildUpTypeIdx", fixBuildUpTypeList.get(0).getBuildUpTypeIdx());// 虚拟组成主键
                }
                
                nodeMap.put("icon", ctx + "/jsp/jx/images/builduptree/leaf.gif");
            } else {
                if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_STRUCTURE && isLeaf) {
                    nodeMap.put("icon", ctx + "/jsp/jx/images/builduptree/folder.gif");
                }
                if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_FIX) {
                    List<FixBuildUpType> fixBuildUpTypeList =
                        this.fixBuildUpTypeManager.getFixBuildList(buildUpToPlace.getBuildUpPlaceIdx());
                    if (fixBuildUpTypeList != null && fixBuildUpTypeList.size() > 0) {
                        isLeaf = false;
                        nodeMap.put("isPartsBuildUp", "true");// 是否下挂组成
                    }
                    nodeMap.put("icon", ctx + "/jsp/jx/images/builduptree/place.png");
                }
            }
            nodeMap.put("buildUpPlaceIdx", buildUpToPlace.getBuildUpPlaceIdx());// 组成位置主键
            nodeMap.put("text", buildUpToPlace.getBuildUpPlaceName());// 节点显示名称
            nodeMap.put("buildUpPlaceCode", buildUpToPlace.getBuildUpPlaceCode());// 组成位置编码
            nodeMap.put("buildUpPlaceShortName", buildUpToPlace.getBuildUpPlaceShortName());// 组成位置简称
            nodeMap.put("chartNo", buildUpToPlace.getChartNo());// 图号
            nodeMap.put("trainTypeIDX", buildUpToPlace.getTrainTypeIDX());// 车型主键
            nodeMap.put("partsTypeIDX", buildUpToPlace.getPartsTypeIDX());// 配件型号主键
            nodeMap.put("professionalTypeIDX", buildUpToPlace.getProfessionalTypeIDX());// 专业类型主键
            nodeMap.put("professionalTypeName", buildUpToPlace.getProfessionalTypeName());// 专业类型名称
            nodeMap.put("partID", buildUpToPlace.getPartID());// 位置主键
            nodeMap.put("partName", buildUpToPlace.getPartName());// 位置名称
            nodeMap.put("placeType", buildUpToPlace.getPlaceType());// 位置类型
            nodeMap.put("leaf", isLeaf);// 是否为叶子节点
            nodeMap.put("buildUpTypeIdx", buildUpToPlace.getBuildUpTypeIdx());// 组成主键
            nodeMap.put("buildUpPlaceFullName", buildUpToPlace.getBuildUpPlaceFullName());// 组成位置全名
            nodeMap.put("buildUpPlaceSEQ", StringUtil.nvlTrim(buildUpToPlace.getBuildUpPlaceSEQ(), "0"));// 组成位置序号
            
            children.add(nodeMap);
        }
        return children;
    }
    /**
     * 
     * <li>说明：生成配件组成或虚拟组成节点
     * <li>创建人：程锐
     * <li>创建日期：2013-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 安装位置主键
     * @param ctx 应用程序根目录
     * @return List<HashMap> 返回树的集合
     * @throws BusinessException
     */
    public List<HashMap<String, Object>> findPartsBuildTypeNode(String idx, String ctx) throws BusinessException {
        List<FixBuildUpType> fixBuildUpTypeList = this.fixBuildUpTypeManager.getFixBuildList(idx);
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        if (fixBuildUpTypeList != null && fixBuildUpTypeList.size() > 0) {
            for(FixBuildUpType fixBuildUpType : fixBuildUpTypeList){
                BuildUpType buildUpType = getModelById(fixBuildUpType.getBuildUpTypeIdx());
                if(buildUpType != null){
                    HashMap<String, Object> nodeMap = new HashMap<String, Object>();
                    nodeMap.put("text", buildUpType.getBuildUpTypeName());
                    nodeMap.put("leaf", true);
                    nodeMap.put("isPartsBuildUp", "false");
                    nodeMap.put("partsBuildUpTypeIdx", fixBuildUpType.getBuildUpTypeIdx());
                    nodeMap.put("parentPlaceIdx", idx);
                    nodeMap.put("partsTypeIDX", buildUpType.getPartsTypeIDX());
                    nodeMap.put("icon", ctx + "/frame/resources/images/toolbar/train.gif");
                    List<BuildUpToPlace> placeList = buildUpPlaceManager.childPlaceList(fixBuildUpType.getBuildUpTypeIdx(), fixBuildUpType.getBuildUpTypeIdx());
                    if(placeList != null && placeList.size() > 0){
                        nodeMap.put("leaf", false);
                    }
                    children.add(nodeMap);
                }
            }
        }        
        return children;
    }
    /**
     * TODO 配件检修业务变化，待删除
     * <li>说明：构建组成树（供工艺流程节点挂接检修项目使用）
     * <li>创建人：程锐
     * <li>创建日期：2013-1-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param parentIDX 上级父节点idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @param ctx 应用程序根目录
     * @return List<HashMap> 返回树的集合
     * @throws BusinessException
     */
    public List<HashMap<String, Object>> findBuildUpTypeTree(String parentIDX, String partsBuildUpTypeIdx, String ctx)
        throws BusinessException {
        
        List<BuildUpType> buildUpTypeList = getBuildUpList(partsBuildUpTypeIdx);
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (BuildUpType buildUpType : buildUpTypeList) {
            Boolean isLeaf = true;
            List<BuildUpType> childBuildList = getBuildUpList(buildUpType.getIdx());
            if (childBuildList != null && childBuildList.size() > 0) {
                isLeaf = false;
            }
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("text", buildUpType.getBuildUpTypeName());// 组成名称
            nodeMap.put("buildUpTypeCode", buildUpType.getBuildUpTypeCode());// 组成编码
            nodeMap.put("partsBuildUpTypeIdx", buildUpType.getIdx());// 组成主键
            nodeMap.put("leaf", isLeaf);
            nodeMap.put("icon", ctx + "/frame/resources/images/toolbar/train.gif");
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：根据组成型号获取其下级位置可安装组成列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param partsBuildUpTypeIdx 组成型号主键
     * @return List<BuildUpType> 组成列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpType> getBuildUpList(String partsBuildUpTypeIdx) throws BusinessException {
        String hql =
            "from BuildUpType where idx in (select buildUpTypeIdx from FixBuildUpType where recordStatus = 0 and fixPlaceIdx in ("
                + "select buildUpPlaceIdx from BuildUpToPlace  where buildUpTypeIdx = ? )) "
                + "and recordStatus = 0 and status = " + BuildUpType.USE_STATUS;
        List<BuildUpType> buildUpTypeList =
            (List<BuildUpType>) getDaoUtils().find(hql, new Object[] { partsBuildUpTypeIdx });
        return buildUpTypeList;
    }
    
    /**
     * <li>说明：根据安装位置查出可安装组成型号列表
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param start 开始行
     * @param limit 每页记录数
     * @param fixPlaceIdx 安装位置主键
     * @return 分页查询列表
     * @throws BusinessException
     */
    public Page<BuildUpType> list(int start, int limit, String fixPlaceIdx) throws BusinessException {
        String sql =
            "select a.idx as idx,a.Is_Default as \"isDefault\",b.BuildUp_Type_Code as buildUpTypeCode,b.BuildUp_Type_Name as buildUpTypeName,b.Specification_Model as specificationModel";
        String from_sql =
            " from JXGC_FIX_BuildUp_Type a left join JXGC_BuildUp_Type b on a.BUILDUP_TYPE_IDX = b.idx "
                + "where a.record_status=0 and b.record_status = 0 and b.STATUS = " + BuildUpType.USE_STATUS
                + " and a.FIX_PLACE_IDX = '" + fixPlaceIdx + "'";
        String query_sql = sql + from_sql;
        String total_sql = " select count(a.idx) " + from_sql;
        return findPageList(total_sql, query_sql, start, limit, null, null);
    }
    
    /**
     * <li>方法说明：查询位置可安装配件型号 
     * <li>方法名称：findBuildUpType
     * <li>@param fixPlaceIdx
     * <li>@return
     * <li>return: List<BuildUpType>
     * <li>创建人：张凡
     * <li>创建时间：2013-5-26 下午06:01:13
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpType> findBuildUpType(String fixPlaceIdx){
        String hql = "select b from FixBuildUpType a, BuildUpType b " +
        		"where a.buildUpTypeIdx = b.idx and a.recordStatus=0 and b.recordStatus=0" +
        		" and b.status='" + BuildUpType.USE_STATUS + "' and a.fixPlaceIdx='" + fixPlaceIdx + "'";
        List<BuildUpType> list = daoUtils.find(hql);
        return list;
    }
    
    /**
     * <li>说明：根据安装位置idx过滤上级组成型号及已添加可安装组成型号
     * <li>创建人：程锐
     * <li>创建日期：2012-11-14
     * <li>修改人：程锐
     * <li>修改日期：2013-1-16
     * <li>修改内容：去除过滤上级组成型号条件
     * 
     * @param searchEntity 查询条件包装类
     * @param fixPlaceIdx 安装位置主键
     * @param type 组成类型
     * @param trainTypeIDX 车型主键
     * @param partsTypeIDX 配件主键
     * @return 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("all")
    public Page<BuildUpType> fixBuildUpTypeList(SearchEntity<BuildUpType> searchEntity, String fixPlaceIdx, String type, String trainTypeIDX, String partsTypeIDX)
        throws BusinessException {
        
        String hql =
            "from BuildUpType where idx not in (select buildUpTypeIdx from FixBuildUpType where fixPlaceIdx = '"
                + fixPlaceIdx + "' and recordStatus = 0)" + " and recordStatus = 0  and status = "
                + BuildUpType.USE_STATUS + " and type = " + type;
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getBuildUpTypeName())) {
            hql += " and buildUpTypeName like '%" + searchEntity.getEntity().getBuildUpTypeName() + "%'";
        }
        //过滤-只选择车型对应虚拟组成
        if(Integer.valueOf(type) == BuildUpType.TYPE_VIRTUAL && !StringUtil.isNullOrBlank(trainTypeIDX)){
            hql += " and trainTypeIDX = '" + trainTypeIDX + "'";
        }
        //过滤-不能选择配件对应组成
        if(!StringUtil.isNullOrBlank(partsTypeIDX)){
            hql += " and idx not in (select idx from BuildUpType where partsTypeIDX = '" + partsTypeIDX + "' and status != " + BuildUpType.NULLIFY_STATUS
                + " and recordStatus = 0 and type = " + BuildUpType.TYPE_PARTS + ")";
        }
        List<String> buildUpTypeList = new ArrayList<String>();
        //过滤上级组成-不能选择上级组成，否则会死循环
        getPartsBuildListByPlace(buildUpTypeList, fixPlaceIdx);//获取上级组成主键列表
        List<BuildUpType> list = daoUtils.find(hql);
        List<BuildUpType> newList = new ArrayList<BuildUpType>();
        if (list != null && list.size() > 0) {
			for (BuildUpType buildUpType : list) {
				boolean hasBuild = false;
				if (buildUpTypeList == null || buildUpTypeList.size() < 1)
					continue;
				for (String buildUpTypeIdx : buildUpTypeList) {
					if (buildUpTypeIdx.equals(buildUpType.getIdx())) {
						hasBuild = true;
						break;
					}
				}
				if (!hasBuild) {
					newList.add(buildUpType);
				}
			}
		}
        int start = searchEntity.getStart();
		int limit = searchEntity.getLimit();
		int pageSize = newList.size() > (limit + start) ? (limit + start) : newList.size();
		List<BuildUpType> newPageList = new ArrayList<BuildUpType>();
		for (int i = start; i < pageSize; i++) {
			newPageList.add(newList.get(i));
		}
        return new Page<BuildUpType>(newList.size(), newPageList);
    }
    /**
     * 
     * <li>说明：递归获取上级配件组成并添加到list组成主键列表中
     * <li>创建人：程锐
     * <li>创建日期：2013-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeList 组成主键列表
     * @param fixPlaceIdx 位置主键
     * @return void
     */
    @SuppressWarnings("unchecked")
    public void getPartsBuildListByPlace(List<String> buildUpTypeList, String fixPlaceIdx){
        String hql = "from BuildUpToPlace where buildUpPlaceIdx = '" + fixPlaceIdx + "'";//获取该位置的组成位置关系
        List<BuildUpToPlace> buildUpToPlaceList = daoUtils.find(hql);
        if (buildUpToPlaceList == null || buildUpToPlaceList.size() < 1) return;
        for(BuildUpToPlace buildUpToPlace : buildUpToPlaceList){                
            buildUpTypeList.add(buildUpToPlace.getBuildUpTypeIdx());
            hql = "from FixBuildUpType where recordStatus = 0 and buildUpTypeIdx = '" + buildUpToPlace.getBuildUpTypeIdx() + "'";//获取该位置所属配件组成挂接的位置（即找到安装了这种组成的位置）
            List<FixBuildUpType> fixBuildUpTypeList = daoUtils.find(hql);
            if (fixBuildUpTypeList == null || fixBuildUpTypeList.size() < 1) continue;
            for (FixBuildUpType fixBuildUpType : fixBuildUpTypeList) {
				BuildUpType build = getModelById(fixBuildUpType.getBuildUpTypeIdx());
				// 如非配件组成则终止递归
				if (build != null && build.getType() == BuildUpType.TYPE_PARTS) {
					getPartsBuildListByPlace(buildUpTypeList, fixBuildUpType.getFixPlaceIdx());
				}
			}        
        }    
    }
    /**
	 * <li>说明：根据车型主键获取已启用的标准机车组成
	 * <li>创建人：程锐
	 * <li>创建日期：2012-12-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param trainTypeIDX 车型主键
	 * @return BuildUpType 组成实体对象
	 * @throws BusinessException
	 */
    public BuildUpType getDefaultBuildByTrain(String trainTypeIDX) throws BusinessException {
        String hql =
            "from BuildUpType where recordStatus = 0 and trainTypeIDX = '" + trainTypeIDX + "' and status = "
                + BuildUpType.USE_STATUS + " and isDefault = " + BuildUpType.ISDEFAULT_YES + " and type = "
                + BuildUpType.TYPE_TRAIN;
        return (BuildUpType) daoUtils.findSingle(hql);
    }
    
    /**
     * <li>说明：根据配件类型主键获取已启用的标准配件组成
     * <li>创建人：程锐
     * <li>创建日期：2012-12-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param partsTypeIDX 配件主键
     * @return BuildUpType 组成实体对象
     * @throws BusinessException
     */
    public BuildUpType getDefaultBuildByParts(String partsTypeIDX) throws BusinessException {
        String hql =
            "from BuildUpType where recordStatus = 0 and partsTypeIDX = '" + partsTypeIDX + "' and status = "
                + BuildUpType.USE_STATUS + " and isDefault = " + BuildUpType.ISDEFAULT_YES + " and type = "
                + BuildUpType.TYPE_PARTS;
        return (BuildUpType) daoUtils.findSingle(hql);
    }
    /**
     * 
     * <li>说明：根据车型车号获取对应组成
     * <li>创建人：程锐
     * <li>创建日期：2013-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号主键 
     * @return BuildUpType 组成实体对象
     */
    public BuildUpType getBuildByTrain(String trainTypeIDX, String trainNo) {
		BuildUpType buildUpType = null;
		String hql = "from TrainConfigInfo where recordStatus = 0 and trainTypeIDX = '" + trainTypeIDX + "' and trainNo = '" + trainNo + "'";
		TrainConfigInfo trainConfigInfo = (TrainConfigInfo) daoUtils.findSingle(hql);
		if (trainConfigInfo != null && !StringUtil.isNullOrBlank(trainConfigInfo.getBuildUpTypeIDX())) {
			buildUpType = getModelById(trainConfigInfo.getBuildUpTypeIDX());
			if (BuildUpType.USE_STATUS != buildUpType.getStatus())
				buildUpType = getDefaultBuildByTrain(trainTypeIDX);
		} else {
			buildUpType = getDefaultBuildByTrain(trainTypeIDX);
		}
		return buildUpType;
	}    
    
    /**
	 * <li>说明：根据车型id查询组成型号信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-5-9
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
    @SuppressWarnings("unchecked")
	public List<BuildUpType> findByTrainTypeIDX(String trainTypeIDX) throws BusinessException{
        StringBuffer hql = new StringBuffer();
        hql.append("From BuildUpType t where t.recordStatus=0 and t.trainTypeIDX = '").append(trainTypeIDX).append("'");
        return (List<BuildUpType>)daoUtils.find(hql.toString());
    }
    
    /**
     * <li>方法说明：查询组成关联配件型号 
     * <li>方法名称：findBuildUpJoinPartsType
     * <li>@param start
     * <li>@param limit
     * <li>@param queryHql
     * <li>@param partsName
     * <li>@param specificationModel
     * <li>@param buildUpTypeName
     * <li>@param orders
     * <li>@return
     * <li>@throws ClassNotFoundException
     * <li>return: Map<String,Object>
     * <li>创建人：张凡
     * <li>创建时间：2013-8-29 下午05:08:42
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> findBuildUpJoinPartsType(int start, int limit, String queryHql, String partsName,
        String specificationModel, String buildUpTypeName, Order[] orders) throws ClassNotFoundException {
        String totalHql = "";
        if(queryHql.equals("")){
            
            queryHql = "select new BuildUpType(t.idx, t.buildUpTypeCode, t.buildUpTypeName, p.partsName, p.specificationModel) from  BuildUpType t, PartsType p where p.idx = t.partsTypeIDX";
        }
        
        StringBuilder awhere = new StringBuilder();
        
        int beginPos = queryHql.toLowerCase().indexOf("from");
        totalHql = " select count (*) " + queryHql.substring(beginPos);
                
        if (!"".equals(partsName)) {
            awhere.append(" and t.partsName like '%").append(partsName).append("%'");
        }
        if (!"".equals(specificationModel)) {
            awhere.append(" and t.specificationModel like '%").append(specificationModel).append("%'");
        }
        if (!"".equals(buildUpTypeName)) {
            awhere.append(" and t.buildUpTypeName like '%").append(buildUpTypeName).append("%'");
        }
        awhere.append(HqlUtil.getOrderHql(orders));
        queryHql += awhere.toString();
        totalHql += awhere.toString();
        
        Page page = findPageList(totalHql, queryHql, start, limit);
        return page.extjsStore();
    }       
}
