package com.yunda.jx.jxgc.configmanage.manager;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.jx.jxgc.configmanage.entity.TrainConfigInfo;
import com.yunda.util.BeanUtils;

@Service(value = "trainConfigInfoManager")
public class TrainConfigInfoManager extends JXBaseManager<TrainConfigInfo, TrainConfigInfo>{
	
	/** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：分页查询，返回实体类的分页列表对象，基于单表实体类分页查询
     * <li>创建人：程锐
     * <li>创建日期：2013-02-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param SearchEntity<T> 包装了实体类查询条件的对象
     * @return Page<E> 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<TrainConfigInfo> findPageList(final SearchEntity<TrainConfigInfo> searchEntity) throws BusinessException{
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<TrainConfigInfo>)template.execute(new HibernateCallback(){
            public Page<TrainConfigInfo> doInHibernate(Session s){
                try {
                	TrainConfigInfo entity = searchEntity.getEntity();
                    //过滤逻辑删除记录
                    BeanUtils.forceSetProperty(entity, EntityUtil.RECORD_STATUS, Constants.NO_DELETE);                  
                    //过滤掉idx的查询条件
                    Example exp = Example.create(entity)
                        .excludeProperty(EntityUtil.IDX)                        
                        .enableLike().enableLike(MatchMode.ANYWHERE);
                    //查询总记录数
                    int total = ((Integer)s.createCriteria(entity.getClass())
                        .add(exp)
                        .setProjection(Projections.rowCount())
                        .uniqueResult())
                        .intValue();
                    //分页列表
                    Criteria criteria = s.createCriteria(entity.getClass()).add(exp)
                        .setFirstResult(searchEntity.getStart())
                        .setMaxResults(searchEntity.getLimit());
                    //设置排序规则
                    Order[] orders = searchEntity.getOrders();
                    if(orders != null){
                        for (Order order : orders) {
                            criteria.addOrder(order);
                        }                   
                    }
                    return new Page<TrainConfigInfo>(total, criteria.list());
                } catch (Exception e) {
                	ExceptionUtil.process(e,logger);
                }
                return null;
            }
        });
    }
    
}
