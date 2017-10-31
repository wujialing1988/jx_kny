package com.yunda.zb.trainclean.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpNodeManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpQueryManager;
import com.yunda.zb.trainclean.entity.ZbglCleaning;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglCleaning业务类,机车保洁记录
 * <li>创建人：程梅
 * <li>创建日期：2015-02-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "zbglCleaningManager")
public class ZbglCleaningManager extends JXBaseManager<ZbglCleaning, ZbglCleaning> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Resource
    private IOmEmployeeManager omEmployeeManager;
    
    @Resource
    private ZbglRdpQueryManager zbglRdpQueryManager;
    
    /**流程节点业务类**/
    @Resource    
    private ZbglRdpNodeManager zbglRdpNodeManager;    
    
    private String configValue = ZbConstants.NODENAME_CLEANING;
    
    /**
     * <li>说明：查询机车保洁列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件
     * @param startDate 入段时间（开始）
     * @param overDate 入段时间（截止）
     * @return 机车交接列表
     */
    public Page<ZbglCleaning> getCleaningList(SearchEntity<ZbglCleaning> searchEntity, String startDate, String overDate) {
        String selectHql =
            "select new ZbglCleaning(a.idx, a.trainTypeIDX, a.trainTypeShortName, a.trainNo, a.dutyPersonName, a.cleaningLevel, a.trainLevel, a.cleaningTime,a.remarks,c.dName, c.inTime, c.siteName,c.toGo)";
        String fromHql =
            " from ZbglCleaning a, ZbglRdp b, TrainAccessAccount c where a.rdpIdx = b.idx and b.trainAccessAccountIDX = c.idx and b.recordStatus = 0 and c.recordStatus = 0";
        StringBuffer awhere = new StringBuffer();
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getSiteName())) {
            awhere.append(" and c.siteName like '%").append(searchEntity.getEntity().getSiteName()).append("%' ");
        }
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainTypeShortName())) {
            awhere.append(" and a.trainTypeShortName like '%").append(searchEntity.getEntity().getTrainTypeShortName()).append("%' ");
        }
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainNo())) {
            awhere.append(" and a.trainNo like '%").append(searchEntity.getEntity().getTrainNo()).append("%' ");
        }
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getCleaningLevel())) {
            awhere.append(" and a.cleaningLevel like '%").append(searchEntity.getEntity().getCleaningLevel()).append("%'");
        }
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainLevel())) {
            awhere.append(" and a.trainLevel like '%").append(searchEntity.getEntity().getTrainLevel()).append("%'");
        }
        //开始时间
        if (!StringUtil.isNullOrBlank(startDate)) {
            awhere.append(" and  to_char(c.inTime,'yyyy-mm-dd hh24:mi:ss')>='").append(startDate).append("'");
        }
//      结束时间
        if (!StringUtil.isNullOrBlank(overDate)) {
            awhere.append(" and to_char(c.inTime,'yyyy-mm-dd hh24:mi:ss') <= '").append(overDate).append("'");
        }
        Order[] orders = searchEntity.getOrders();
        String sortString = " order by c.inTime desc";// 排序hql字符串
        if (orders != null && orders.length > 0) {
            for (Order order : orders) {
                String[] orderStrings = StringUtil.tokenizer(order.toString(), " ");
                if (orderStrings != null && orderStrings.length == 2) {
                    if (orderStrings[0].equals("inTime") || orderStrings[0].equals("dName") || orderStrings[0].equals("siteName"))
                        sortString =
                            new StringBuilder(" order by ").append("c.").append(orderStrings[0]).append(" ").append(orderStrings[1]).toString();
                    else
                        sortString =
                            new StringBuilder(" order by ").append("a.").append(orderStrings[0]).append(" ").append(orderStrings[1]).toString();
                }
            }
        }
        awhere.append(sortString);
        StringBuffer totalHql = new StringBuffer("select count(*) ").append(fromHql);
        totalHql.append(awhere);
        String hql = selectHql + fromHql + awhere;
        return findPageList(totalHql.toString(), hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：保存机车保洁记录
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param clean 机车保洁信息
     * @throws Exception
     */
    public void savaTrainCleaning(ZbglCleaning clean) throws Exception {
        OmEmployee emp = getEmpByOperaor(clean.getOperatorid());
        if (null != emp) {
            clean.setDutyPersonId(emp.getEmpid());
            clean.setDutyPersonName(emp.getEmpname());
        }
        clean.setCleaningTime(new Date());
        saveOrUpdate(clean);
        ZbglRdp rdp = zbglRdpQueryManager.getModelById(clean.getRdpIdx());
        if (rdp != null)
            zbglRdpNodeManager.updateNodeForEnd(configValue, new Date(), rdp.getIdx(), ZbglRdpNode.STATUS_COMPLETE);
    }
    
    /**
     * <li>说明：根据操作员id获取人员对象
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员id
     * @return 人员对象
     */
    private OmEmployee getEmpByOperaor(String operatorid) {
        OmEmployee emp = null;
        // 工位终端
        if (!StringUtil.isNullOrBlank(operatorid))
            emp = omEmployeeManager.findByOperator(Long.parseLong(operatorid));
        else
            emp = SystemContext.getOmEmployee();
        return emp;
    }
    
    /**
     * <li>说明：获取机车整备单关联的机车保洁完成情况
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 机车整备单关联的机车保洁完成情况
     */
    public String getIsFinish(String rdpIDX) {
        ZbglCleaning clean = getEntityByRdp(rdpIDX);
        if (clean == null)
            return "无";
        else
            return "完成";
    }
    
    /**
     * <li>说明：获取机车整备单关联的机车保洁实体
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 机车整备单关联的机车保洁实体
     */
    @SuppressWarnings("unchecked")
    public ZbglCleaning getEntityByRdp(String rdpIDX) {
        ZbglCleaning clean = new ZbglCleaning();
        clean.setRdpIdx(rdpIDX);
        List<ZbglCleaning> list = daoUtils.getHibernateTemplate().findByExample(clean);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
