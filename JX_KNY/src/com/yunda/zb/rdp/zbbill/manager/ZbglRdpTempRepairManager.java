package com.yunda.zb.rdp.zbbill.manager;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpBean;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpTempRepair;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.tp.entity.ZbglTp;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpTempRepair业务类,转临修
 * <li>创建人：程锐
 * <li>创建日期：2015-01-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglRdpTempRepairManager")
public class ZbglRdpTempRepairManager extends JXBaseManager<ZbglRdpTempRepair, ZbglRdpTempRepair> {
    
    /**
     * <li>说明：查询整备单列表-碎修
     * <li>创建人：程锐
     * <li>创建日期：2015-1-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 机车整备单bean实体包装类
     * @return 整备单分页列表-碎修
     * @throws BusinessException
     */
    public Page<ZbglRdpBean> findSXRdpList(SearchEntity<ZbglRdpBean> searchEntity) throws BusinessException {
        return findRdpList(searchEntity, "findSXRdpList");
    }
    
    /**
     * <li>说明：查询整备单列表-临修
     * <li>创建人：程锐
     * <li>创建日期：2015-1-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 机车整备单bean实体包装类
     * @return 整备单分页列表-临修
     * @throws BusinessException
     */
    public Page<ZbglRdpBean> findLXRdpList(SearchEntity<ZbglRdpBean> searchEntity) throws BusinessException {
        return findRdpList(searchEntity, "findLXRdpList");
    }
    
    /**
     * <li>说明：查询整备单分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 机车整备单bean实体包装类
     * @param sqlName 查询sql名称
     * @return 整备单分页列表
     * @throws BusinessException
     */
    private Page<ZbglRdpBean> findRdpList(SearchEntity<ZbglRdpBean> searchEntity, String sqlName) throws BusinessException {
        String sql = getSql(sqlName);
        StringBuilder sb = new StringBuilder(sql);
        ZbglRdpBean entity = searchEntity.getEntity();
        // 查询条件 - 车型
        if (!StringUtil.isNullOrBlank(entity.getTrainTypeShortName())) {
            sb.append(" AND A.TRAIN_TYPE_SHORTNAME LIKE '%").append(entity.getTrainTypeShortName()).append("%'");
        }
        // 查询条件 - 车号
        if (!StringUtil.isNullOrBlank(entity.getTrainNo())) {
            sb.append(" AND A.TRAIN_NO LIKE '%").append(entity.getTrainNo()).append("%'");
        }
        // 查询条件 - 配属段
        if (!StringUtil.isNullOrBlank(entity.getDname())) {
            sb.append(" AND A.D_NAME LIKE '%").append(entity.getDname()).append("%'");
        }
        AbstractEntityPersister meta = (AbstractEntityPersister) getDaoUtils().getSessionFactory().getClassMetadata(ZbglRdpBean.class);
        String sortString = "";
        Order[] orders = searchEntity.getOrders();
        if (orders != null && orders.length > 0) {
            for (Order order : orders) {
                String[] orderStrings = StringUtil.tokenizer(order.toString(), " ");
                if (orderStrings == null || orderStrings.length != 2)
                    continue;
                if (orderStrings[0].equals("trainTypeShortName") || 
                    orderStrings[0].equals("trainNo") || 
                    orderStrings[0].equals("siteName") || 
                    orderStrings[0].equals("rdpStartTime")) {
                    sortString = CommonUtil.buildOrderSql("A.", meta, orderStrings);
                } else if (orderStrings[0].equals("toGo") || orderStrings[0].equals("inTime")) {
                    sortString = CommonUtil.buildOrderSql("B.", meta, orderStrings);
                } else if (orderStrings[0].equals("dname")) {
                    String[] newOrderStrings = new String[2];
                    newOrderStrings[0] = "dname"; 
                    newOrderStrings[1] = orderStrings[1];
                    sortString = CommonUtil.buildOrderSql("A.", meta, newOrderStrings);
                }  
            }
            sb.append(sortString);
        } else {
            sb.append(" ORDER BY B.IN_TIME DESC");
        }

        sql = sb.toString();
        StringBuilder totalSql = new StringBuilder("SELECT COUNT(*) AS ROWCOUNT FROM (").append(sql).append(")");
        return this.acquirePageList(totalSql.toString(), sql, searchEntity.getStart(), searchEntity.getLimit(), false);
    }
    
    /**
     * <li>说明：获取查询sql字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-1-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param sqlName 查询sql名称
     * @return 查询sql字符串
     */
    private String getSql(String sqlName) {
        String sql = "";
        if ("findSXRdpList".equals(sqlName)) {
			sql = SqlMapUtil.getSql("zb-rdp:findSXRdpList")
						    .replace("#STATUS_HANDLED#", ZbglRdpWi.STATUS_HANDLED)
						    .replace("#STATUS_OVER#", ZbglTp.STATUS_OVER)
						    .replace("#STATUS_HANDLING#", ZbglRdp.STATUS_HANDLING)
						    .replace("#REPAIRCLASS_SX#", ZbConstants.REPAIRCLASS_SX)
						    .replace("#siteID#", EntityUtil.findSysSiteId(""));
		}else if ("findLXRdpList".equals(sqlName)) {
			sql = SqlMapUtil.getSql("zb-rdp:findLXRdpList")
						    .replace("#STATUS_HANDLED#", ZbglRdpWi.STATUS_HANDLED)
						    .replace("#STATUS_OVER#", ZbglTp.STATUS_OVER)
						    .replace("#STATUS_HANDLING#", ZbglRdp.STATUS_HANDLING)
						    .replace("#REPAIRCLASS_LX#", ZbConstants.REPAIRCLASS_LX)
						    .replace("#siteID#", EntityUtil.findSysSiteId(""));
		}
		return sql;
	}
    
	/**
     * <li>说明：基于sql查询语句的分页查询
     * <li>创建人：程锐
     * <li>创建日期：2015-1-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param totalSql 查询总记录数的sql语句
     * @param sql 查询语句
     * @param start 开始行
     * @param limit 每页记录数
     * @param isQueryCacheEnabled 是否启用查询缓存
     * @return Page<ZbglRdpBean> 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<ZbglRdpBean> acquirePageList(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled) throws BusinessException {
        final int beginIdx = start < 0 ? 0 : start;
        final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
        final String total_sql = totalSql;
        final String fSql = sql;
        final Boolean useCached = isQueryCacheEnabled;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<ZbglRdpBean>) template.execute(new HibernateCallback() {
            
            public Page<ZbglRdpBean> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = s.createSQLQuery(total_sql);
                    query.addScalar("rowcount", Hibernate.INTEGER);
                    query.setCacheable(useCached); // 缓存开关
                    int total = ((Integer) query.uniqueResult()).intValue();
                    query.setCacheable(false);
                    int begin = beginIdx > total ? total : beginIdx;
                    query = (SQLQuery) s.createSQLQuery(fSql).addEntity(ZbglRdpBean.class).setFirstResult(begin).setMaxResults(pageSize);
                    query.setCacheable(useCached); //缓存开关
                    return new Page<ZbglRdpBean>(total, query.list());
                } catch (HibernateException e) {
                    throw e;
                } finally {
                    if (query != null)
                        query.setCacheable(false);
                }
            }
        });
    }

    /**
     * <li>说明：通过整备单idx查询临修票对象
     * <li>创建人：林欢
     * <li>创建日期：2016-8-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param inOrLatestOutAccount 前台传递过来的出入段台账对象
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpTempRepair> findZbglRdpTempRepairByZbglRdpIDX(String zbglRdpIDX) {

        StringBuffer sb = new StringBuffer();
        
        sb.append(" from ZbglRdpTempRepair a where a.rdpIDX = '").append(zbglRdpIDX).append("'");
        
        return (List<ZbglRdpTempRepair>) find(sb.toString());
    }
}
