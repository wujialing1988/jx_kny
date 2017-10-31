package com.yunda.zb.tecorder.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.tecorder.entity.ZbglTecOrder;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTecOrder业务类,机车技术指令措施
 * <li>创建人：王利成
 * <li>创建日期：2015-03-03
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="zbglTecOrderManager")
public class ZbglTecOrderManager extends JXBaseManager<ZbglTecOrder, ZbglTecOrder>{
    
    
    /** ZbglRdpWi业务类,机车整备任务单 */
    @Resource
    private ZbglRdpWiManager zbglRdpWiManager;
    /**
     * <li>说明：发布
     * <li>创建人：王利成
     * <li>创建日期：2015-3-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 主键
     * @return int
     */
    public int release(String[] ids){
        StringBuilder hql = new StringBuilder("update ZbglTecOrder set releasePersonID = ?, releasePersonName = ?" +
                ", releaseTime = ?, orderStatus = ? where idx in(");
        List<Object> list = new ArrayList<Object>();
        OmEmployee emp = SystemContext.getOmEmployee();
        if(emp != null){
            list.add(emp.getEmpid());
            list.add(emp.getEmpname());
        }
        list.add(new Date());
        list.add(ZbglTecOrder.STATUS_PUBLISH);
        for(String id : ids){
            hql.append("?,");
            list.add(id);
        }
        hql.deleteCharAt(hql.length() - 1).append(") and orderStatus = ? and recordStatus = 0");
        list.add(ZbglTecOrder.STATUS_NEW);
        return daoUtils.execute(hql.toString(), list.toArray());
    }
    
    /**
     * <li>说明：销号
     * <li>创建人：王利成
     * <li>创建日期：2015-3-2 下午05:33:28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 主键
     * @return  int
     */
    public int cancel( String[] ids){
        StringBuilder hql = new StringBuilder("update ZbglTecOrder set completePersonID = ?, completePersonName = ?, completeTime = ?, orderStatus = ? where idx in(");
        List<Object> list = new ArrayList<Object>(); 
        OmEmployee emp = SystemContext.getOmEmployee();
        if(emp != null){
            list.add(emp.getEmpid());
            list.add(emp.getEmpname());
        }
        list.add(new Date());
        list.add(ZbglTecOrder.STATUS_CANCEL);
        for(String id : ids){
            hql.append("?,");
            list.add(id);
        }
        hql.deleteCharAt(hql.length() - 1).append(") and orderStatus = ? and recordStatus = 0");
        list.add(ZbglTecOrder.STATUS_PUBLISH);
        return daoUtils.execute(hql.toString(), list.toArray());
    }
    /**
     * <li>说明：查询技术指令处理项 
     * <li>创建人：王利成
     * <li>创建日期：2015-3-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglTecIdx 指令主键
     * @return List<ZbglTecOrder>
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTecOrder> findList(String zbglTecIdx){
        String hql = "select new ZbglTecOrder(a.inTime, b.rdpStatus, c.handlePersonName, c.handleTime) " +
                "from TrainAccessAccount a, ZbglRdp b, ZbglRdpWi c where a.idx = b.trainAccessAccountIDX and b.idx = c.rdpIDX and c.wiIDX = ?";
        return daoUtils.find(hql, zbglTecIdx);
    }
    
    /**
     * <li>说明：技术措施生成临时任务单 
     * <li>创建人：王利成
     * <li>创建日期：2015-3-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglTecIdx 指令主键
     * @return String 
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String saveOrUpdateLsRdp(String zbglTecIdx) throws BusinessException, NoSuchFieldException{
        ZbglTecOrder tecOrder = this.getModelById(zbglTecIdx);
        StringBuffer sql = new StringBuffer();
        sql.append("select a.idx from ZB_ZBGL_RDP a where a.train_type_idx='").append(tecOrder.getTrainTypeIDX()).append("'").
        append("and a.train_no='").append(tecOrder.getTrainNo()).
        append("'").append("and a.rdp_status='").
        append(ZbglRdp.STATUS_HANDLING).append("'");
        List list = this.daoUtils.executeSqlQuery(sql.toString());
        if (null == list || list.size() <= 0) {
            throw new NullPointerException("机车不在整备状态");
        }
        String idx = (String)list.get(0);
        ZbglRdpWi zbglRdpWi = this.zbglRdpWiManager.getModel(idx, zbglTecIdx);
        if (null != zbglRdpWi) {
            throw new NullPointerException("临时任务单已经存在!");
        }
        //生成临时任务单
        zbglRdpWiManager.saveTecOrderRdpWi(idx,zbglTecIdx,tecOrder.getOrderContent());
        //如果该指令为单次销号，临时任务单生成之后，立即销号此记录
        if (tecOrder.getCompleteMethod().intValue() == ZbglTecOrder.COMPLETE_SINGAL) {
            String[] idxs = zbglTecIdx.split("\\,");
            this.cancel(idxs);
        }
        updateOrderHandleTimes(zbglTecIdx);
        return "临时任务单操作成功";
    }
    
    /**
     * 
     * <li>说明：方法实现功能说明
     * <li>创建人：王利成
     * <li>创建日期：2015-3-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 指令措施主键
     * @return String
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String saveOrUpdateLsRdps(String[] ids) throws BusinessException, NoSuchFieldException {
        for (String zbglTecIdx : ids) {
            try {
                this.saveOrUpdateLsRdp(zbglTecIdx);
            } catch (NullPointerException e) {
                return e.getMessage();
            }
        }
        return "临时任务单操作成功！";
    }
    
    /**
     * 
     * <li>方法说明：更新处理次数
     * <li>方法名称：updateOrderHandleTimes
     * <li>@param idx
     * <li>创建人：王利成
     * <li>创建时间：2015-3-3 上午11:22:41
     * <li>修改人：
     * <li>修改内容：
     */
    public void updateOrderHandleTimes(String idx){
        StringBuffer sqlUpdate = new StringBuffer();
        sqlUpdate.append("update ZB_ZBGL_Tec_Order set order_handle_times = ");
        if(idx != null){
            sqlUpdate.append("order_handle_times+1 where idx ='").append(idx).append("'");
        }
        //String sqlUpdate = "update ZB_ZBGL_Tec_Order set order_handle_times = order_handle_times+1 where idx = '"+idx+"'";
        this.daoUtils.executeSql(sqlUpdate.toString());
        this.daoUtils.flush();
    }

    /**
     * <li>说明：通过整备单信息存储过程生成技术指令范围活
     * <li>创建人：林欢
     * <li>创建日期：2016-5-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param param 参数，整备单id
     */
    public void saveZbglTecOrderByProc(String[] param) {
        daoUtils.executeProc("sp_create_zb_wi_tecorder", param);
        daoUtils.flush();
    }

    /**
     * <li>说明：根据范围活idx查询技术措施list
     * <li>创建人：林欢
     * <li>创建日期：2016-8-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglRdpWiIDX 范围活idx
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTecOrder> getZbglTecOrderByZbglRdpWiIDX(String zbglRdpWiIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ZbglTecOrder a where a.idx = '").append(zbglRdpWiIDX).append("'");
        return (List<ZbglTecOrder>) find(sb.toString());
    }
    
}