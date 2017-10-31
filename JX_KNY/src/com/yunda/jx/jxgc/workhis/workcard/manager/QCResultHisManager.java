package com.yunda.jx.jxgc.workhis.workcard.manager;


import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.workhis.workcard.entity.QCResultHis;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 质量检验结果管理类
 * <li>创建人：汪东良
 * <li>创建日期：2014-11-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "qCResultHisManager")
public class QCResultHisManager extends JXBaseManager<QCResultHis, QCResultHis> {
    /**
     * 
     * <li>说明：查询当前工单的质量检查项分页列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询包装类
     * @return 当前工单的质量检查项分页列表
     * @throws BusinessException
     */
    @SuppressWarnings("deprecation")
    public Page<QCResultHis> findQCResultList(SearchEntity<QCResultHis> searchEntity) throws BusinessException {
        String sql = SqlMapUtil.getSql("jxgc-his:findQCResultList").replace("#workCardIDX#", searchEntity.getEntity().getWorkCardIDX());
        Order[] orders = searchEntity.getOrders();
        String totalSql = "select count(1) from (" + sql + ")";
        return findPageList(totalSql.toString(), sql, searchEntity.getStart(), searchEntity.getLimit(), null, orders);      
  }

    /**
     * <li>说明：通过记录卡查询质量检查项
     * <li>创建人：张迪
     * <li>创建日期：2016-9-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 记录卡idx
     * @return 质量检查项结果集
     */
    @SuppressWarnings("unchecked")
    public List<QCResult> findListByWorkCard(String workCardIDX) {     
        String hql = "select new QCResult(a.idx, a.relationIDX, a.workCardIDX, a.checkItemCode, a.checkItemName, a.qcEmpID, a.qcEmpName, a.status)" +
                     " from QCResultHis a where recordStatus = 0 and workCardIDX = '" + workCardIDX + "'";
        return daoUtils.find(hql);
    }
}