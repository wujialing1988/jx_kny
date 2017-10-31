package com.yunda.jx.base.jcgy.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.jx.base.jcgy.entity.RT;
import com.yunda.jx.base.jcgy.entity.XC;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：XC业务类,修程编码
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="xCManager")
public class XCManager extends JXBaseManager<XC, XC> implements IbaseCombo {
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
     * <li>说明：分页查询修程，返回实体类的分页列表对象，基于单表实体类分页查询
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @param rcTypeIDX 修程类型主键(用于过滤修程类型对应的修程)
     * @param undertakeTrainTypeIDX 承修车型主键（用于过滤承修车型对应的修程）
     * @return Page<E> 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<RT> findPageList(final SearchEntity<XC> searchEntity,final String rcTypeIDX, final String undertakeTrainTypeIDX) throws BusinessException{
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<RT>)template.execute(new HibernateCallback(){
            public Page<RT> doInHibernate(Session s){
                try {
                    String sql = "1=1"; //用于过滤修程类型对应的修程
                    String sql2 = "1=1"; //用于过滤承修车型对应的修程
                    if(!"".equals(rcTypeIDX)){ // and t.rc_type_idx = '"+rcTypeIDX+"' 错误的限制，一个修程只能属于一个修程类型
                        sql = "XC_ID not in (select nvl(t.rc_idx,'-1') from JCZL_RC_TYPE_RC t where t.record_status=0 )";
                    }
                    if(!"".equals(undertakeTrainTypeIDX)){ 
                        sql2 = "XC_ID not in (select nvl(t.rc_idx,'-1') from JCZL_UNDERTAKE_TRAIN_TYPE_RC t where t.record_status=0 and t.undertake_train_type_idx='"+undertakeTrainTypeIDX+"' )";
                    }
                    XC entity = searchEntity.getEntity();
                    Example exp = Example.create(entity)
                        .enableLike().enableLike(MatchMode.ANYWHERE);
                    //查询总记录数
                    int total = ((Integer)s.createCriteria(entity.getClass())
                        .add(exp)
                        .add(Restrictions.sqlRestriction(sql))
                        .add(Restrictions.sqlRestriction(sql2))
                        .setProjection(Projections.rowCount())
                        .uniqueResult())
                        .intValue();
                    //分页列表
                    Criteria criteria = s.createCriteria(entity.getClass()).add(exp)
                        .add(Restrictions.sqlRestriction(sql))
                        .add(Restrictions.sqlRestriction(sql2))
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
     * <li>方法说明： 根据修程名称查询修程
     * <li>方法名：getByName
     * @param name 修程名称
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月5日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public XC getByName(String name){
        String hql = "from XC where xcName = ?";
        return (XC) daoUtils.findSingle(hql, name);
    }
    
    
    /**
     * <li>方法说明：下拉查询
     * <li>方法名称：getBaseComboData
     * @param req HttpServletRequest对象
     * @param start 开始页
     * @param limit 页长
     * @return page转换的map
     */
    @Override
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        
        String hql = "from XC";
        String query = req.getParameter("query");
        if(query != null && query.length() > 0)
            hql += "where xcName like '%'" + query + "%"; 
        
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page<XC> page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
}