package com.yunda.jx.base.jcgy.manager;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.jx.base.jcgy.entity.RT;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RT业务类,修次(RepairTime)
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="rTManager")
public class RTManager extends JXBaseManager<RT, RT>{
	/** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
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
     * <li>说明：分页查询修次，返回实体类的分页列表对象，基于单表实体类分页查询
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @param rcIDX 修程IDX
     * @return Page<E> 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<RT> findPageList(final SearchEntity<RT> searchEntity,final String rcIDX) throws BusinessException{
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<RT>)template.execute(new HibernateCallback(){
            public Page<RT> doInHibernate(Session s){
                try {
                    String sql = "1=1";
                    if(!"".equals(rcIDX)){
                        sql = "RT_ID not in (select nvl(t.repair_time_idx,'-1') from JCZL_RC_RT t where t.record_status=0 and t.rc_idx = '"+rcIDX+"')";
                    }
                    RT entity = searchEntity.getEntity();
                    Example exp = Example.create(entity)
                        .enableLike().enableLike(MatchMode.ANYWHERE);
                    //查询总记录数
                    int total = ((Integer)s.createCriteria(entity.getClass())
                        .add(exp)
                        .add(Restrictions.sqlRestriction(sql))
                        .setProjection(Projections.rowCount())
                        .uniqueResult())
                        .intValue();
                    //分页列表
                    Criteria criteria = s.createCriteria(entity.getClass()).add(exp)
                        .add(Restrictions.sqlRestriction(sql))
                        .setFirstResult(searchEntity.getStart())
                        .setMaxResults(searchEntity.getLimit());
                    //设置排序规则
                    Order[] orders = searchEntity.getOrders();
                    if(orders != null){
                        for (Order order : orders) {
                            criteria.addOrder(order);
                        }                   
                    }
                    return new Page<RT>(total, criteria.list());
                } catch (Exception e) {
                	ExceptionUtil.process(e,logger);
                }
                return null;
            }
        });
    }
    
    /**
     * <li>说明：新增或更新一组实体对象
     * @param t 实体对象
     */ 
    @Override
    public void saveOrUpdate(RT t) {       
        this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
    }
    
    /**
     * <li>说明：重写父类方法 logicDelete 此表无法逻辑删除，因此是物理删除
     * @param ids 实体类主键idx数组
     */ 
    @Override
    public void logicDelete(Serializable... ids) {
        for (Serializable id : ids) {
            RT t = getModelById(id);
            daoUtils.getHibernateTemplate().delete(t);
        }
    }
    
    /**
     * <li>方法说明：根据修次名称查询实体
     * <li>方法名：getByName
     * @param name 修次名称
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月5日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public RT getByName(String name){
        String hql = "from RT where rtName = ?";
        return (RT) daoUtils.findSingle(hql, name);
    }
}