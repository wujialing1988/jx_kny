
package com.yunda.jx.component.manager;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件选择控件业务类 ,以前版本为BaseGridManager
 * <li>创建人：程锐
 * <li>创建日期：2012-10-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsSelectManager")
public class PartsSelectManager extends JXBaseManager<Object, Object> {
    
    /**
     * <li>说明：获取列表前台store所需Map对象
     * <li>创建人：程锐
     * <li>创建日期：2012-9-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 实体对象全名
     * @param start 开始行
     * @param limit 每页记录数
     * @param queryHql 查询Hql字符串
     * @param partsName 互换配件名称
     * @param specificationModel 互换配件规格型号
     * @param nameplateNo 配件铭牌号
     * @param partsNo 配件编号
     * @param orders 排序对象数组
     * @return Map<String,Object> 分页页面Map对象
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> page(String entity, 
    								int start, 
    								int limit, 
    								String queryHql, 
    								String partsName,
    								String specificationModel, 
    								String nameplateNo, 
    								String partsNo, 
    								Order[] orders) throws ClassNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();
        String hql = "";
        String totalHql = "";
        StringBuilder awhere = new StringBuilder();
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql = queryHql;
            int beginPos = hql.toLowerCase().indexOf("from");
            totalHql = " select count (*) " + hql.substring(beginPos);
        } else if (!StringUtil.isNullOrBlank(entity)) {
            hql = " from " + entity + " where 1=1 and recordStatus=0 ";
            totalHql = " select count(*) from " + entity + " where 1=1 and recordStatus=0 ";
        }
        if (!"".equals(partsName)) {
            awhere.append(" and partsName like '%").append(partsName).append("%'");
        }
        if (!"".equals(specificationModel)) {
            awhere.append(" and specificationModel like '%").append(specificationModel).append("%'");
        }
        if (!"".equals(nameplateNo)) {
            awhere.append(" and nameplateNo like '%").append(nameplateNo).append("%'");
        }
        if (!"".equals(partsNo)) {
            awhere.append(" and partsNo like '%").append(partsNo).append("%'");
        }
        awhere.append(HqlUtil.getOrderHql(orders));
        hql += awhere.toString();
        totalHql += awhere.toString();        
        Page page = findPageList(totalHql, hql, start, limit, entity);
        map = page.extjsStore();
        return map;
    }
    
    /**
     * <li>方法说明：根据HQL查询列表 
     * <li>方法名称：page2
     * <li>@param start
     * <li>@param limit
     * <li>@param queryHql
     * <li>@param partsName
     * <li>@param specificationModel
     * <li>@param nameplateNo
     * <li>@param partsNo
     * <li>@param orders
     * <li>@return
     * <li>@throws ClassNotFoundException
     * <li>return: Map<String,Object>
     * <li>创建人：张凡
     * <li>创建时间：2013-7-15 上午10:51:44
     * <li>修改人：
     * <li>修改内容：此方法配件中在使用，请勿注销。
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> page2(int start, int limit, String queryHql, String partsName,
        String specificationModel, String nameplateNo, String partsNo, Order[] orders) throws ClassNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();
        String totalHql = "";
        StringBuilder awhere = new StringBuilder();
        
        int beginPos = queryHql.toLowerCase().indexOf("from");
        totalHql = " select count (*) " + queryHql.substring(beginPos);
                
        if (!"".equals(partsName)) {
            awhere.append(" and t.partsName like '%").append(partsName).append("%'");
        }
        if (!"".equals(specificationModel)) {
            awhere.append(" and t.specificationModel like '%").append(specificationModel).append("%'");
        }
        if (!"".equals(nameplateNo)) {
            awhere.append(" and t.nameplateNo like '%").append(nameplateNo).append("%'");
        }
        if (!"".equals(partsNo)) {
            awhere.append(" and t.partsNo like '%").append(partsNo).append("%'");
        }
        awhere.append(HqlUtil.getOrderHql(orders));
        queryHql += awhere.toString();
        totalHql += awhere.toString();
        
        Page page = findPageList(totalHql, queryHql, start, limit, null);
        map = page.extjsStore();
        return map;
    }
    
    /**
     * <li>说明：根据实体名获取对应page对象
     * <li>创建人：程锐
     * <li>创建日期：2012-9-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param totalHql 查询总记录数的hql语句
     * @param hql 查询语句
     * @param start 开始行
     * @param limit 每页记录数
     * @param entity 实体对象全名
     * @return Page<E> 分页查询列表
     * @throws BusinessException, ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public <E> Page<E> findPageList(String totalHql, String hql, int start, int limit, String entity)
        throws BusinessException, ClassNotFoundException {
        final int beginIdx = start < 0 ? 0 : start;
        final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
        final String total_hql = totalHql;
        final String fHql = hql;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<E>) template.execute(new HibernateCallback() {
            
            public Page<E> doInHibernate(Session s) {
                // 获取总记录数
                Query q = s.createQuery(total_hql);
                int total = ((Long) q.uniqueResult()).intValue();
                int begin = beginIdx > total ? total : beginIdx;
                q = s.createQuery(fHql).setFirstResult(begin).setMaxResults(pageSize);
                return new Page<E>(total, q.list());
            }
        });
    }
    
}
