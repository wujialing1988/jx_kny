package com.yunda.jx.base.jcgy.manager;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.FaultMethod;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 故障处理方法业务类
 * <li>创建人：程锐
 * <li>创建日期：2013-4-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "faultMethodManager")
public class FaultMethodManager  extends JXBaseManager<FaultMethod, FaultMethod>{

    /**
     * <li>说明：选择处理方法列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 处理方法实体包装类
     * @param placeFaultIDX 故障现象主键
     * @return Page 处理方法列表
     * @throws BusinessException
     */
    public Page<FaultMethod> methodList(SearchEntity<FaultMethod> searchEntity, String placeFaultIDX) throws BusinessException {        
        String hql =
            "from FaultMethod where methodID not in (select methodID from PlaceFaultMethod where placeFaultIDX = '" + placeFaultIDX
                + "' and recordStatus = 0)" ;
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getMethodName())) {
            hql += " and methodName like '%" + searchEntity.getEntity().getMethodName() + "%'";
        }
        Order[] orders = searchEntity.getOrders();
        hql += HqlUtil.getOrderHql(orders);
        String totalHql = "select count(*) " + hql;
        return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
}
