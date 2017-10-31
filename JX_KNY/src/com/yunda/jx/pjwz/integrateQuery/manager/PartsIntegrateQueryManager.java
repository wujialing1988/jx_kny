package com.yunda.jx.pjwz.integrateQuery.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.integrateQuery.entity.PartsIntegrateQuery;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件综合查询处理器
 * <li>创建人：张迪
 * <li>创建日期：2016-6-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="partsIntegrateQueryManager")
public class PartsIntegrateQueryManager extends JXBaseManager<PartsIntegrateQuery, PartsIntegrateQuery> {

	/**
	 * <li>说明： 分页查询
	 * <li>创建人：张迪
	 * <li>创建日期：2016-5-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体对象
	 * @return 综合查询集合
	 */		
	@Override
	public Page<PartsIntegrateQuery> findPageList(SearchEntity<PartsIntegrateQuery> searchEntity) {
        PartsIntegrateQuery entity = searchEntity.getEntity();
		StringBuilder sb = new StringBuilder();
		sb.append("From PartsIntegrateQuery Where 1=1 ");
         // 查询条件 - 退库库房idx主键
        if (!StringUtil.isNullOrBlank(entity.getPartsName())) {
            sb.append(" And partsName like'%").append(entity.getPartsName()).append("%'");
        }
        if (0!=entity.getByQty()) {
            sb.append(" And byQty > 0 ");
        }
		if (!StringUtil.isNullOrBlank(entity.getUnloadTrainTypeIdx())) {
            sb = new StringBuilder();
            sb.append(" select new PartsIntegrateQuery(p.idx,p.partsName,p.specificationModel,p.matCode, p.unit, p.limitQty,p.byQty, p.lhQty,p.dxQty,p.zxQty,p.wwxQty, p.djyQty, p.dbfQty, p.lhl, t.standardQty) From PartsIntegrateQuery p, TrainTypeToParts t  Where p.idx=t.partsTypeIDX And t.trainTypeIDX = '")
            .append(entity.getUnloadTrainTypeIdx()).append("' ");
            // 查询条件 - 退库库房idx主键
            if (!StringUtil.isNullOrBlank(entity.getPartsName())) {
                sb.append(" And p.partsName like'%").append(entity.getPartsName()).append("%'");
            }
            if (0!=entity.getByQty()) {
                sb.append(" And p.byQty > 0 ");
            }
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}

}
