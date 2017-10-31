package com.yunda.jx.pjwz.partsBase.madefactory.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.madefactory.entity.PartsMadeFactory;
import com.yunda.jxpz.datacollect.entity.DataCollectId;
import com.yunda.jxpz.datacollect.manager.DataCollectManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsMadeFactory业务类   配件生产厂家
 * <li>创建人：程梅
 * <li>创建日期：2013-7-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="partsMadeFactoryManager")
public class PartsMadeFactoryManager extends JXBaseManager<PartsMadeFactory, PartsMadeFactory>{
    private String dictTypeId = "factorycode" ;//字典编码/收藏数据实体---生产厂家
    /** 常用收藏夹业务类*/
    @Resource
    private DataCollectManager dataCollectManager ;
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-22
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
     * <li>说明：生产厂家名称唯一性验证
     * <li>创建人：程梅
     * <li>创建日期：2013年7月8日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 生产厂家信息
     * @return String[] 错误提示
     * @throws BusinessException
     */
	@Override
	public String[] validateUpdate(PartsMadeFactory t) throws BusinessException {
		List<String> errMsg = new ArrayList<String>();
		PartsMadeFactory factory = this.getModelById(t.getId());
		if(factory == null || (factory != null && !factory.getMadeFactoryName().equals(t.getMadeFactoryName())) ){ //新增时验证
			String hql = "Select count(*) From PartsMadeFactory where madeFactoryName='"+
			t.getMadeFactoryName()+"' and recordStatus=0";
			int count = this.daoUtils.getCount(hql);
			if(count > 0){
				errMsg.add("生产厂家名称【"+ t.getMadeFactoryName() +"】已存在！");
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
	 * 
	 * <li>说明：配件生产厂家列表
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 过滤条件
     * @param factoryname 厂家名称
     * @param queryHql 查询sql
     * @param query 关键字
	 * @return Page<PartsMadeFactory> 厂家分页列表
	 * @throws 抛出异常列表
	 */
	public Page<PartsMadeFactory> findPageList(SearchEntity<PartsMadeFactory> searchEntity, String factoryname, String queryHql, String query)
	    throws BusinessException {
	    String totalHql = "select count(*) from PartsMadeFactory  where 1=1 and recordStatus=0";
	    String hql = "from PartsMadeFactory where 1=1 and recordStatus=0";
	    // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
	    if (!StringUtil.isNullOrBlank(queryHql)) {
	        hql = queryHql;
	        int beginPos = hql.toLowerCase().indexOf("from");
	        totalHql = " select count (*) " + hql.substring(beginPos);
	    }
	    StringBuilder awhere = new StringBuilder();
	    if (!"".equals(factoryname)) {
	        awhere.append(" and madeFactoryName like '%").append(factoryname).append("%'");
	    }
	    // 关键字查询
	    if (!"".equals(query)) {
	        awhere.append(" and madeFactoryName like '%").append(query).append("%'");
	    }
	    Order[] orders = searchEntity.getOrders();
	    awhere.append(HqlUtil.getOrderHql(orders));
	    totalHql += awhere.toString();
	    hql += awhere.toString();
	    if(enableCache()){
	    	return super.cachePageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	    } else {
	    	return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	    }
//	    return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
    /**
     * 
     * <li>说明：查询生产厂家列表【收藏数据实体为“factorycode”】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param flag 是否被收藏标示
     * @param madeFactoryShortname 生产厂家关键字
     * @return List<PartsMadeFactory> 生产厂家列表
     */
    @SuppressWarnings("unchecked")
    public List<PartsMadeFactory>  findFactoryList(String flag,String madeFactoryShortname){
        //当前登录用户基本信息
        OmEmployee emp = SystemContext.getOmEmployee();
        StringBuffer hql = null ;
        //flag为TRUE，则查询已收藏的生产厂家信息
        if("true".equals(flag)){
            hql = new StringBuffer("select i From PartsMadeFactory i ,DataCollect c where i.recordStatus=0 and i.id = c.id.dataIdx ")
            .append(" and c.id.collectEmpId = '").append(emp.getEmpid()).append("' and c.id.dataEntity = '").append(dictTypeId).append("'");
            if(!StringUtil.isNullOrBlank(madeFactoryShortname)){
                hql.append(" and i.madeFactoryName like '%").append(madeFactoryShortname).append("%' ") ;
            }
        }else if("false".equals(flag)){
            //flag为false，则查询未被收藏的生产厂家信息
            hql = new StringBuffer("select i From PartsMadeFactory i where i.recordStatus=0 and i.id not in (select c.id.dataIdx from DataCollect c where c.id.collectEmpId = '").append(emp.getEmpid()).
            append("' and c.id.dataEntity = '").append(dictTypeId).append("')");
            if(!StringUtil.isNullOrBlank(madeFactoryShortname)){
                hql.append(" and i.madeFactoryName like '%").append(madeFactoryShortname).append("%' ") ;
            }
        }else{
            hql = new StringBuffer("select i From PartsMadeFactory i where i.recordStatus=0 ") ;
            if(!StringUtil.isNullOrBlank(madeFactoryShortname)){
                hql.append(" and i.madeFactoryName like '%").append(madeFactoryShortname).append("%' ") ;
            }
        }
        hql.append(" order by i.madeFactoryName ") ; //按生产厂家名称排序
        List<PartsMadeFactory> list = daoUtils.find(hql.toString()) ;
        return list ;
    }
    /**
     * 
     * <li>说明：收藏生产厂家
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 生产厂家id
     * @param emp 人员信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String[] saveFactoryCollect(String id, OmEmployee emp) throws BusinessException, NoSuchFieldException {
        DataCollectId collectId = new DataCollectId();
        collectId.setDataIdx(id) ;
        collectId.setDataEntity(dictTypeId) ; //收藏数据实体---生产厂家
        collectId.setCollectEmpId(emp.getEmpid()) ;
        String[] errMsg = dataCollectManager.validateSave(collectId);
        if(null != errMsg && errMsg.length > 0){
            return errMsg ;
        }else {
            dataCollectManager.saveDataCollect(collectId) ;
            return null;
        }
    }
    /**
     * 
     * <li>说明：取消收藏生产厂家
     * <li>创建人：程梅
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 生产厂家id
     * @param emp 人员信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void deleteFactoryCollect(String id, OmEmployee emp) throws BusinessException, NoSuchFieldException {
        StringBuffer sql = new StringBuffer("delete from JXPZ_DATA_COLLECT where DATA_ENTITY= '").append(dictTypeId).
        append("' and DATA_IDX ='").append(id).append("' and COLLECT_EMP_ID='").append(emp.getEmpid()).append("'") ;
        daoUtils.executeSql(sql.toString()) ;
    }
}