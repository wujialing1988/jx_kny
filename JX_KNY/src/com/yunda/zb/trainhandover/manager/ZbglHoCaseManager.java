package com.yunda.zb.trainhandover.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpNodeManager;
import com.yunda.zb.trainhandover.entity.ZbglHoCase;
import com.yunda.zb.trainhandover.entity.ZbglHoCaseItem;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglHoCase业务类,机车交接记录
 * <li>创建人：程梅
 * <li>创建日期：2015-02-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "zbglHoCaseManager")
public class ZbglHoCaseManager extends JXBaseManager<ZbglHoCase, ZbglHoCase> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 人员相关数据查询接口
     */
    @Resource
    private IOmEmployeeManager omEmployeeManager;
    
    /**
     * 机车交接单项业务类
     */
    @Resource
    private ZbglHoCaseItemManager zbglHoCaseItemManager;
    /**
     * 机车整备单业务类
     */
    @Resource
    private ZbglRdpManager zbglRdpManager;
    /**
     * 机车出入段台账业务类
     */
    @Resource
    private TrainAccessAccountManager trainAccessAccountManager ;
    
    /**流程节点业务类**/
    @Resource    
    private ZbglRdpNodeManager zbglRdpNodeManager;
    
    private String configValue = ZbConstants.NODENAME_HANDOVER;
    
    /**
     * <li>说明：查询机车交接列表
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
    public Page<ZbglHoCase> getCaseList(SearchEntity<ZbglHoCase> searchEntity, String startDate, String overDate) {
        String selectHql =
            "select new ZbglHoCase(a.idx, a.trainTypeIDX, a.trainTypeShortName, a.trainNo, a.fromPersonName, a.toPersonName, a.handOverTime, a.handOverTrainOrder,a.remarks, c.dName, c.inTime, c.siteName,c.toGo,a.arrivedTime )";
        String fromHql =
            " from ZbglHoCase a, ZbglRdp b, TrainAccessAccount c where a.rdpIdx = b.idx and b.trainAccessAccountIDX = c.idx and a.recordStatus = 0 and b.recordStatus = 0 and c.recordStatus = 0";
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
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getHandOverTrainOrder())) {
            awhere.append(" and a.handOverTrainOrder like '%").append(searchEntity.getEntity().getHandOverTrainOrder()).append("%'");
        }
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getDName())) {
            awhere.append(" and c.dName like '%").append(searchEntity.getEntity().getDName()).append("%'");
        }
        if (!StringUtil.isNullOrBlank(startDate)) {
            awhere.append(" and  to_char(c.inTime,'yyyy-mm-dd hh24:mi:ss')>='").append(startDate).append("'");
        }
        if (!StringUtil.isNullOrBlank(overDate)) {
            awhere.append(" and to_char(c.inTime,'yyyy-mm-dd hh24:mi:ss') <= '").append(overDate).append("'");
        }
        //添加入段去向查询条件
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainToGo())) {
            awhere.append(" and c.toGo like '%").append(searchEntity.getEntity().getTrainToGo()).append("%'");
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
     * <li>说明：判断是否为数字
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param str 需判断的字符串
     * @return true or false
     */
    public boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <li>说明：根据操作员id获取人员对象
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员id
     * @return 人员对象
     */
    private OmEmployee getEmpByOperaor(String operatorid) {
        OmEmployee emp = SystemContext.getOmEmployee();
        // 工位终端
        if (!StringUtil.isNullOrBlank(operatorid))
            emp = omEmployeeManager.findByOperator(Long.parseLong(operatorid));
        return emp;
    }
    
    /**
     * <li>说明：保存机车交接单信息
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param hoCase 交接单信息
     * @throws BusinessException
     */
    private void saveRdp(ZbglHoCase hoCase) throws BusinessException, NoSuchFieldException {
        OmEmployee emp = getEmpByOperaor(SystemContext.getAcOperator().getOperatorid().toString());
        if (null != emp) {
            hoCase.setToPersonId(emp.getEmpid());
            hoCase.setToPersonName(emp.getEmpname());
        }
        hoCase.setHandOverTime(new Date());
        saveOrUpdate(hoCase);
    }
    
    /**
     * <li>说明：保存交接单和交接项信息
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param hoCase 交接单信息
     * @param items 交接项信息
     * @throws Exception
     */
    public void saveHoCaseAndItems(ZbglHoCase hoCase, ZbglHoCaseItem[] items) throws Exception {
        // 保存机车交接单信息
        saveRdp(hoCase);
        //更新出入段台账状态
        if(!StringUtil.isNullOrBlank(hoCase.getRdpIdx())){
            ZbglRdp rdp = zbglRdpManager.getModelById(hoCase.getRdpIdx());
            if(null != rdp && !StringUtil.isNullOrBlank(rdp.getTrainAccessAccountIDX())){
                trainAccessAccountManager.updateTrainStatus(rdp.getTrainAccessAccountIDX(), TrainAccessAccount.TRAINSTATUS_ZHENGZAIJIANCHA);
                zbglRdpNodeManager.updateNodeForEnd(configValue, new Date(), rdp.getIdx(), ZbglRdpNode.STATUS_COMPLETE);
            }
        }
        zbglHoCaseItemManager.saveItemsByCaseId(hoCase.getIdx(), items);
        
    }
    
    /**
     * <li>说明：获取机车整备单关联的机车交接完成情况
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 机车整备单关联的机车交接完成情况
     */
    public String getIsFinish(String rdpIDX) {
        ZbglHoCase hoCase = getEntityByRdp(rdpIDX);
        if (hoCase == null)
            return "无";
        else
            return "完成";
    }
    
    /**
     * <li>说明：获取机车整备单关联的机车交接实体
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 机车整备单关联的机车交接实体
     */
    @SuppressWarnings("unchecked")
    public ZbglHoCase getEntityByRdp(String rdpIDX) {
        ZbglHoCase hoCase = new ZbglHoCase();
        hoCase.setRdpIdx(rdpIDX);
        hoCase.setRecordStatus(Constants.NO_DELETE);
        List<ZbglHoCase> list = daoUtils.getHibernateTemplate().findByExample(hoCase);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
