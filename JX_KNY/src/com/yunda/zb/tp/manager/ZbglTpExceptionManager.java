package com.yunda.zb.tp.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.entity.ZbglTpException;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpException业务类,提票例外放行
 * <li>创建人：程锐
 * <li>创建日期：2015-03-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglTpExceptionManager")
public class ZbglTpExceptionManager extends JXBaseManager<ZbglTpException, ZbglTpException> {
    
    /** 提票业务类 */
    @Resource
    ZbglTpManager zbglTpManager;
    @Resource
    OmEmployeeManager omEmployeeManager;
    
    /**
     * <li>说明：提票例外放行
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：刘国栋
     * <li>修改日期：2016-9-13
     * <li>修改内容：
     * @param tpExceptionAry 提票例外放行实体数组
     * @throws Exception
     */
    public void saveForLwfx(ZbglTpException[] tpExceptionAry) throws Exception {
        List<ZbglTp> tpList = new ArrayList<ZbglTp>();
        List<ZbglTpException> entityList = new ArrayList<ZbglTpException>();
        OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
        for (ZbglTpException tpException : tpExceptionAry) {
            ZbglTp tp = zbglTpManager.getModelById(tpException.getTpIDX());
            if (tp == null)
                throw new BusinessException("id为" + tpException.getTpIDX() + "的提票单为空");
            if (!ZbglTp.STATUS_DRAFT.equals(tp.getFaultNoticeStatus()) && !ZbglTp.STATUS_OPEN.equals(tp.getFaultNoticeStatus()))
                throw new BusinessException("id为" + tpException.getTpIDX() + "的提票单状态不为【未处理】");
            if(null != emp){
                tpException.setHandlePersonId(emp.getEmpid());
                tpException.setHandlePersonName(emp.getEmpname());
                tpException.setHandleTime(new Date());
                tpException.setRdpIDX(tp.getRdpIDX());
            }
            entityList.add(tpException);
            tp.setFaultNoticeStatus(ZbglTp.STATUS_INIT);
            tp.setRdpIDX("");
            tp.setRevPersonId(null);
            tp.setRevPersonName("");
            tp.setRepairTimes(0);
            tpList.add(tp);
        }
        saveOrUpdate(entityList);
        zbglTpManager.saveOrUpdate(tpList);
    }
    
    /**
     * <li>说明：获取提票例外放行分页对象
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 提票例外放行实体包装类
     * @return 提票例外放行分页对象
     * @throws BusinessException
     */
    public Page<ZbglTpException> findTpExceptionPageList(SearchEntity<ZbglTpException> searchEntity) throws BusinessException {
        StringBuilder selectSb = new StringBuilder();
        selectSb
            .append("select new ZbglTpException(b.idx, b.rdpIDX, b.tpIDX, b.exceptionReason, b.handlePersonName, b.handleTime,")
            .append("a.trainTypeShortName, a.trainNo, a.noticePersonName, a.noticeTime, a.faultFixFullName, a.faultName, a.faultDesc, a.faultOccurDate, a.faultNoticeCode)");
        StringBuilder fromSb = new StringBuilder();
        fromSb.append(" from ZbglTp a, ZbglTpException b, ZbglRdp c where a.recordStatus = 0 and b.recordStatus = 0 and a.idx = b.tpIDX and b.rdpIDX = c.idx and c.rdpStatus <> 'COMPLETE' ");
        StringBuffer awhere = new StringBuffer();
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getRdpIDX())) {
            awhere.append(" and b.rdpIDX = '").append(searchEntity.getEntity().getRdpIDX()).append("'");
        }
        Order[] orders = searchEntity.getOrders();
        awhere.append(HqlUtil.getOrderHql(orders));
        fromSb.append(awhere);
        String hql = selectSb.append(fromSb).toString();
        String totalHql = "select count(b.idx) ".concat(fromSb.toString());
        return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：取消提票例外放行
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tpExceptionAry 提票例外放行实体数组
     * @throws Exception
     */
    public void updateForCancel(ZbglTpException[] tpExceptionAry) throws Exception {
        List<ZbglTp> tpList = new ArrayList<ZbglTp>();
        List<ZbglTpException> entityList = new ArrayList<ZbglTpException>();
        for (ZbglTpException tpException : tpExceptionAry) {
            ZbglTp tp = zbglTpManager.getModelById(tpException.getTpIDX());
            if (tp == null)
                throw new BusinessException("id为" + tpException.getTpIDX() + "的提票单为空");
            tpException = EntityUtil.setSysinfo(tpException);
            tpException = EntityUtil.setDeleted(tpException);
            entityList.add(tpException);
            tp.setRdpIDX(tpException.getRdpIDX());//整备单idx
            tp.setFaultNoticeStatus(ZbglTp.STATUS_DRAFT);//活票状态
            tpList.add(tp);
        }
        daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
        zbglTpManager.saveOrUpdate(tpList);
    }
    
    /**
     * <li>说明：获取同一机车整备单的提票例外放行数量
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的提票例外放行数量
     */
    public int getCountByRdp(String rdpIDX) {
        List<ZbglTpException> list = getListByRdp(rdpIDX);
        return CommonUtil.getListSize(list);
    }
    
    /**
     * <li>说明：获取同一机车整备单的提票例外放行列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的提票例外放行列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTpException> getListByRdp(String rdpIDX) {
        if (StringUtil.isNullOrBlank(rdpIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIDX", rdpIDX);
        return getTpExceptionList(paramMap);
    }
    
    /**
     * <li>说明：获取提票例外放行列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 提票例外放行列表
     */
    @SuppressWarnings("unchecked")
    private List<ZbglTpException> getTpExceptionList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglTpException where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
    


    /**
     * <li>说明：通过提票idx查询提票例外是否存在
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return true 存在 false 不存在
     */
    public Boolean isTpExceptionByTpIDX(Map paramMap) {
        List<ZbglTpException> zbglTpException =  getTpExceptionList(paramMap);
        if(null != zbglTpException && zbglTpException.size()>0 )
            return true;
        return false;
    }
}
