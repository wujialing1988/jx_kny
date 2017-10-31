package com.yunda.jx.component.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.StringUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 通用下拉列表控件件业务类
 * <li>创建人：程锐
 * <li>创建日期：2012-9-7
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "baseComboManager")
public class BaseComboManager extends JXBaseManager<Object, Object> {
    
    /**
     * <li>说明：获取下拉列表前台store所需Map对象
     * <li>创建人：程锐
     * <li>创建日期：2012-9-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 实体对象全名
     * @param queryParams 查询参数Map对象
     * @param start 开始行
     * @param limit 每页记录数
     * @param queryHql 查询Hql字符串
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> page(String entity,
    								String queryName,
    								String queryValue, 
    								Map queryParams, 
    								int start, 
    								int limit, 
    								String queryHql) throws ClassNotFoundException {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		String totalHql = "";		
		if (!StringUtil.isNullOrBlank(queryHql)) {
			hql = queryHql;
			int beginPos = hql.toLowerCase().indexOf("from");
			totalHql = " select count (*) " + hql.substring(beginPos);
		}
		else if (!StringUtil.isNullOrBlank(entity)) {
			hql = " from " + entity + " where 1=1";
			totalHql = " select count(*) from " + entity + " where 1=1";
			if (!queryParams.isEmpty()) {
				Set<Map.Entry<String, String>> set = queryParams.entrySet();
				Iterator it = set.iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
					String key = entry.getKey();
					String value = entry.getValue();
					if (!StringUtil.isNullOrBlank(value)) {
						hql += " and " + key + " = '" + value + "'";
						totalHql += " and " + key + " = '" + value + "'";
					}
				}
			}
			if (!StringUtil.isNullOrBlank(queryName) && !StringUtil.isNullOrBlank(queryValue)) {
				hql += "and lower(" + queryName + ") like '%" + queryValue.toLowerCase() + "%'";
				totalHql += "and lower(" + queryName + ") like '%" + queryValue.toLowerCase() + "%'";
			}

		}
		Page page = findPageList(totalHql, hql, start, limit, entity);
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
