
package com.yunda.jx.component.manager;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.component.entity.GyjcFactory;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：GyjcFactory业务类,工厂编码
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-09-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "gyjcFactoryManager")
public class GyjcFactoryManager extends JXBaseManager<GyjcFactory, GyjcFactory> {
    
    /**
     * <li>说明：生产厂家列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param searchEntity 生产厂家实体对象包装类
     * @param factoryname 生产厂家名称
     * @param queryHql 查询字符串
     * @param query 关键字查询字符串
     * @return Page 分页查询列表
     * @throws BusinessException
     */
    public Page<GyjcFactory> findPageList(SearchEntity<GyjcFactory> searchEntity, String factoryname, String queryHql, String query)
        throws BusinessException {
        String totalHql = "select count(*) from GyjcFactory  where 1=1";
        String hql = "from GyjcFactory where 1=1";
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql = queryHql;
            int beginPos = hql.toLowerCase().indexOf("from");
            totalHql = " select count (*) " + hql.substring(beginPos);
        }
        StringBuilder awhere = new StringBuilder();
        if (!"".equals(factoryname)) {
            awhere.append(" and fName like '%").append(factoryname).append("%'");
        }
        // 关键字查询
        if (!"".equals(query)) {
            awhere.append(" and fName like '%").append(query).append("%'");
        }
        Order[] orders = searchEntity.getOrders();
        awhere.append(HqlUtil.getOrderHql(orders));
        totalHql += awhere.toString();
        hql += awhere.toString();
        return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
}
