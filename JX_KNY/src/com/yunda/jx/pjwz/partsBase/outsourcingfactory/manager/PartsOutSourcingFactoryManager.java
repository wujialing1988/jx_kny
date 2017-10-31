package com.yunda.jx.pjwz.partsBase.outsourcingfactory.manager;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.outsourcingfactory.entity.PartsOutSourcingFactory;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatClass业务类,材料分类
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsOutSourcingFactoryManager")
public class PartsOutSourcingFactoryManager extends JXBaseManager<PartsOutSourcingFactory, PartsOutSourcingFactory>{
	
	/**
	 * 
	 * <li>说明：配件委外厂家列表
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page<PartsOutSourcingFactory> findPageList(SearchEntity<PartsOutSourcingFactory> searchEntity, String factoryname, String queryHql, String query)
    throws BusinessException {
    String totalHql = "select count(*) from PartsOutSourcingFactory  where 1=1 and recordStatus=0";
    String hql = "from PartsOutSourcingFactory where 1=1 and recordStatus=0";
    // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
    if (!StringUtil.isNullOrBlank(queryHql)) {
        hql = queryHql;
        int beginPos = hql.toLowerCase().indexOf("from");
        totalHql = " select count (*) " + hql.substring(beginPos);
    }
    StringBuilder awhere = new StringBuilder();
    if (!"".equals(factoryname)) {
        awhere.append(" and factoryName like '%").append(factoryname).append("%'");
    }
    // 关键字查询
    if (!"".equals(query)) {
        awhere.append(" and factoryName like '%").append(query).append("%'");
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
//    return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
}
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：王斌
	 * <li>创建日期：2012-08-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(PartsOutSourcingFactory entity) throws BusinessException {
		String [] errMsg=null;
		if(entity==null) return new String[]{"数据异常"};
		//if(entity.getId())
		String hql="FROM PartsOutSourcingFactory where id=?";
		Object[] param=new Object[]{entity.getId()};
		String hql1="";
		Object[] param1=null;
		List factorys=null;
		List<PartsOutSourcingFactory> list=this.daoUtils.getHibernateTemplate().find(hql, param);
		if(list!=null && list.size()>0){//修改
			hql1="FROM PartsOutSourcingFactory where factoryName=? and id<>?";
			param1=new Object[]{entity.getFactoryName(),entity.getId()};
		    factorys= this.daoUtils.getHibernateTemplate().find(hql1,param1);
		    if(factorys!=null && factorys.size()>0){
		    	errMsg=new String[]{"已存在委外厂家名称为【"+entity.getFactoryName()+"】的委外厂家"};
		    	return errMsg;
		    }else {
		    	return null;
		    }
		}else {//新增
			hql1="FROM PartsOutSourcingFactory where factoryName=?";
			param1=new Object[]{entity.getFactoryName()};
		    factorys= this.daoUtils.getHibernateTemplate().find(hql1,param1);
		    if(factorys!=null && factorys.size()>0){
		    	errMsg=new String[]{"已存在委外厂家名称为【"+entity.getFactoryName()+"】的委外厂家"};
		    	return errMsg;
		    }else {
		    	return null;
		    }
		}
	}
	/**
	 * <li>说明：删除前的实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateDelete(Serializable... ids) {
		// TODO Auto-generated method stub
		return super.validateDelete(ids);
	}
	
	
}