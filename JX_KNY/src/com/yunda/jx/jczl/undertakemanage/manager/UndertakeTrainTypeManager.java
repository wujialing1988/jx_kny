package com.yunda.jx.jczl.undertakemanage.manager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrain;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainTypeRC;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UndertakeTrainType业务类,承修车型
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="undertakeTrainTypeManager")
public class UndertakeTrainTypeManager extends JXBaseManager<UndertakeTrainType, UndertakeTrainType>{
	/**定义承修车型对应修程业务类*/
    private UndertakeTrainTypeRCManager undertakeTrainTypeRCManager ;
    /** 承修机车业务类 */
    private UndertakeTrainManager undertakeTrainManager; 
    /**
     * 组成型号业务类
     */
    private BuildUpTypeManager buildUpTypeManager;
    
    
    /** 组织机构业务类：OmOrganizationManager */
    @Resource
    private OmOrganizationManager omOrganizationManager;
	
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}    
	/**
	 * <li>说明：新增修改保存前的实体对象前的批量验证业务
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	public String[] validateUpdate(UndertakeTrainType[] entityList) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        for(UndertakeTrainType obj : entityList){
            List<UndertakeTrainType> countList = this.getModelList(obj.getTrainTypeIDX(), obj.getUndertakeOrgId());
            if(countList.size() > 0){
                errMsg.add("【"+obj.getTrainTypeShortName()+"】车型，已经存在！");
            }
            if (errMsg.size() > 0) {
                String[] errArray = new String[errMsg.size()];
                errMsg.toArray(errArray);
                return errArray;
            }
        }
	    return null;
	}
    /**
     * <li>说明：通过车型主键
     * <li>创建人：王治龙
     * <li>创建日期：2012-11-6
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-11-17
     * <li>修改内容：根据缓存开关来执行查询
     * @param trainTypeIDX：车型主键；
     * @param undertakeOrgId：承修单位主键；
     * @return List<UndertakeTrainType> 返回承修车型集合
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<UndertakeTrainType> getModelList(String trainTypeIDX,Long undertakeOrgId) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("From UndertakeTrainType t where t.recordStatus=").append(Constants.NO_DELETE);
        hql.append(" and t.trainTypeIDX = ? and t.undertakeOrgId = ? ");
        return daoUtils.find(enableCache(), hql.toString(), new Object[]{trainTypeIDX,undertakeOrgId});
//        return this.daoUtils.find(hql.toString(), new Object[]{trainTypeIDX,undertakeOrgId});
    }
    /**
     * 
     * <li>说明：通过车型主键获取承修车型对象
     * <li>创建人：程锐
     * <li>创建日期：2014-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX
     * @return 承修车型对象
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public UndertakeTrainType getModel(String trainTypeIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("from UndertakeTrainType t where t.recordStatus=").append(Constants.NO_DELETE);
        hql.append(" and t.trainTypeIDX = ? ");
        return (UndertakeTrainType) daoUtils.findSingle(hql.toString(), new Object[]{trainTypeIDX});
    }
    
    /**
     * <li>说明：查询承修车型对应修程
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-31
     * <li>修改人：何涛
     * <li>修改日期：2015-08-19
     * <li>修改内容：修改WM_CONCAT()函数在unix系统下回报错的问题
     * @param searchJson 查询的Json对象
     * @param start 分页开始
     * @param limit 分页结束
     * @param orders 排序对象
     * @return page
     * @throws BusinessException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public Page<UndertakeTrainType> findPageLinkList(String searchJson,int start,int limit, Order[] orders) throws BusinessException, JsonParseException, JsonMappingException, IOException{
        searchJson = StringUtil.nvl(searchJson, "{}");
        UndertakeTrainType entity = JSONUtil.read(searchJson, UndertakeTrainType.class);
        StringBuilder sb = new StringBuilder("SELECT IDX, A.TRAIN_TYPE_IDX, A.TRAIN_TYPE_SHORTNAME, A.UNDERTAKE_ORGID, A.UNDERTAKE_ORGNAME, B.T_TYPE_NAME AS typeName FROM JCZL_UNDERTAKE_TRAIN_TYPE A, J_JCGY_TRAIN_TYPE B WHERE A.TRAIN_TYPE_IDX=B.T_TYPE_ID AND A.RECORD_STATUS = 0");
        if (null != entity.getUndertakeOrgId()) {
            sb.append(" AND A.UNDERTAKE_ORGID = '").append(entity.getUndertakeOrgId()).append("'");
        }
        String sql = sb.toString();
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
        Page<UndertakeTrainType> page = super.findPageList(totalSql, sql, start , limit, null, orders);
        List<UndertakeTrainType> list = page.getList();
        for (UndertakeTrainType type : list) {
            // 根据承修车型主键获取修程名称，多个修程之间用逗号分隔
            type.setRcGroup(this.undertakeTrainTypeRCManager.getRcGroupByUndertakeTrainTypeIDX(type.getIdx()));
        }
        return page;
    }
    
    /**
     * <li>说明：整备产品化整备范围查询查询
     * <li>创建人：王利成
     * <li>创建日期：2015-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
     * @param start 开始数
     * @param limit 限制数
     * @param orders 排序
     * @return Page<UndertakeTrainType> 
     * @throws BusinessException
     */
    public Page<UndertakeTrainType> findTrainForZBList(SearchEntity<UndertakeTrainType> searchEntity,int start,int limit, Order[] orders) throws BusinessException{
        String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
        OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
        String orgseq = org.getOrgseq();
        UndertakeTrainType underTakeTrainType = searchEntity.getEntity();
        StringBuilder hqlSec = new StringBuilder(" select u ");
        StringBuilder hqlFrom = new StringBuilder();
        hqlFrom.append("from UndertakeTrainType u ,TrainType t  where 1=1 and u.trainTypeIDX = t.typeID and u.recordStatus=0 ");
        if (!StringUtil.isNullOrBlank(orgseq)) {
            hqlFrom.append(" and u.undertakeOrgId in (select orgid from OmOrganization where orgseq like '").append(orgseq).append(
                "%' and status='running')");
        }
        if(!StringUtil.isNullOrBlank(underTakeTrainType.getTrainTypeIDX())){
            hqlFrom.append(" and u.trainTypeIDX like '%").append(underTakeTrainType.getTrainTypeIDX()).append("%'");
        }
        if(!StringUtil.isNullOrBlank(underTakeTrainType.getTrainTypeShortName())){
            hqlFrom.append(" and u.trainTypeShortName like '%").append(underTakeTrainType.getTrainTypeShortName()).append("%'");
        }
        hqlSec.append(hqlFrom);
        String hql = hqlSec.toString();
        String totalHql = "select count(*) " + hqlFrom; 
        return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * <li>说明：批量设置承修车型
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param objList：对象集合
     * @return String[] 返回错误信息列表
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public String[] saveOrUpdateList(UndertakeTrainType[] objList) throws BusinessException, NoSuchFieldException{
        String[] errMsg = this.validateUpdate(objList);  //验证
        List<UndertakeTrainType> entityList = new ArrayList<UndertakeTrainType>();
        if (errMsg == null || errMsg.length < 1) {
            for(UndertakeTrainType t : objList){ //循环新增是为了验证方便
                entityList.add(t);
            }
            this.saveOrUpdate(entityList);
        }
        return errMsg;
    }
    
    /**
     * <li>说明：逻辑删除记录承修车型和车型对应的修程，根据制定的实体身份标示数组进行批量删除实体
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-08-07
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 实体类主键idx数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */ 
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<UndertakeTrainType> entityList = new ArrayList<UndertakeTrainType>();
        for (Serializable id : ids) {
            UndertakeTrainType t = getModelById(id);
            //级联删除承修修程
            List<UndertakeTrainTypeRC> typeRCList =  undertakeTrainTypeRCManager.getModelList(t.getIdx());
            undertakeTrainTypeRCManager.logicDelete(typeRCList);

            //级联删除承修机车
            List<UndertakeTrain> trainList = undertakeTrainManager.findByTrainIDX(t.getIdx());
            undertakeTrainManager.logicDelete(trainList);
            
//          级联删除此车型对应的组成型号信息
            List<BuildUpType> buildTypeList = buildUpTypeManager.findByTrainTypeIDX(t.getTrainTypeIDX());
            buildUpTypeManager.logicDelete(buildTypeList);
//            t = EntityUtil.setSysinfo(t);
////          设置逻辑删除字段状态为已删除
//            t = EntityUtil.setDeleted(t);
            entityList.add(t);
        }
        super.logicDelete(entityList);
//        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }

    
    public UndertakeTrainTypeRCManager getUndertakeTrainTypeRCManager() {
        return undertakeTrainTypeRCManager;
    }

    
    public void setUndertakeTrainTypeRCManager(UndertakeTrainTypeRCManager undertakeTrainTypeRCManager) {
        this.undertakeTrainTypeRCManager = undertakeTrainTypeRCManager;
    }

	public UndertakeTrainManager getUndertakeTrainManager() {
		return undertakeTrainManager;
	}

	public void setUndertakeTrainManager(UndertakeTrainManager undertakeTrainManager) {
		this.undertakeTrainManager = undertakeTrainManager;
	}

	public BuildUpTypeManager getBuildUpTypeManager() {
		return buildUpTypeManager;
	}

	public void setBuildUpTypeManager(BuildUpTypeManager buildUpTypeManager) {
		this.buildUpTypeManager = buildUpTypeManager;
	}
}