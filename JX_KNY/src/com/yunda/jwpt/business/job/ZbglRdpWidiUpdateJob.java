package com.yunda.jwpt.business.job;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jwpt.business.entity.TZbglZbrwdJcjg;
import com.yunda.jwpt.business.manager.TZbglZbrwdJcjgManager;
import com.yunda.jwpt.common.BaseBusinessDataUpdateJob;
import com.yunda.jwpt.common.IBaseBusinessDataUpdateJob;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.jwpt.utils.BaseBusinessDataUpdateJobUtils;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWidiManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备电子合格证-机车整备任务单（检查结果）更新业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "zb_zbgl_rdp_wi_di")
public class ZbglRdpWidiUpdateJob extends BaseBusinessDataUpdateJob{
    
    //获取容器中的对象
    /** ZbglRdpWidi业务类,机车整备任务单数据项 */
    ZbglRdpWidiManager zbglRdpWidiManager = (ZbglRdpWidiManager) BaseBusinessDataUpdateJobUtils.instence.getBean(ZBGL_RDP_WI_DI_MANAGER);
    
    //获取容器中的对象
    /** TZbglZbrwdJcjgManager 机车整备电子合格证-机车整备任务单（检查结果）(机务平台数据同步官方模型) 业务类 */
    TZbglZbrwdJcjgManager tZbglZbrwdJcjgManager = (TZbglZbrwdJcjgManager) BaseBusinessDataUpdateJobUtils.instence.getBean(T_ZBGL_ZBRWD_JCJG_MANAGER);
    
    /**
     * <li>方法说明：获取官方中间表manager对象
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @return 返回官方中间表业务manager对象 
     */
    @Override
    protected JXBaseManager getBusinessCenterManager() {
        return tZbglZbrwdJcjgManager;
    }

    /**
     * <li>方法说明：获取对应的业务对象(如果businessObjMap中没有官方的中间表对象，新增时候，需要赋值recordStatus，如果有operateType也需要赋值)
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：初始化的部分状态和操作需要子类实现
     * @param jdsct 系统中间表对象
     * @return 返回需要的业务对象 key=businessCenterObj【官方中间表，不能为空，一定要有个对象，new一个都行】，【businessObj系统业务表】 value=对应的实体对象
     */
    @Override
    protected Map<String, Object> getBusinessObjMap(JwptDataSynchronizationCenterTable jdsct) {
//      业务表主键idx
        String businessIDX = jdsct.getBusinessIDX();
        //操作 3删除 5更新 9新增
//      通过业务主表idx查询业务表 机车整备任务单数据项 对象
        ZbglRdpWidi businessObj = zbglRdpWidiManager.getModelById(businessIDX);
        businessObjMap.put(BaseBusinessDataUpdateJob.BUSINESS_OBJ, businessObj);
        //通过业务表主键idx查询官方对应的中间表
        TZbglZbrwdJcjg businessCenterObj = tZbglZbrwdJcjgManager.getModelById(businessIDX);
        
        if (businessCenterObj == null) {
            businessCenterObj = new TZbglZbrwdJcjg();
            
            //由于业务表没有该字段，设置默认值
            businessCenterObj.setRecordStatus(IBaseBusinessDataUpdateJob.RECORD_STATUS_NOT_DELETE);
        }
        
        businessObjMap.put(BaseBusinessDataUpdateJob.BUSINESS_CENTER_OBJ, businessCenterObj);
        
        return businessObjMap;
    }

    /**
     * <li>方法说明：设置业务类自有属性(由于官方中间表的id名称不一致，需要在此方法手动赋值)
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @param object 官方中间表业务对象
     */
    @Override
    protected void setbusinessCenterObjProperty(Object object) {
//      获取 机车整备任务单数据项 对象
        ZbglRdpWidi businessObj = (ZbglRdpWidi) businessObjMap.get(BaseBusinessDataUpdateJob.BUSINESS_OBJ);
        TZbglZbrwdJcjg businessCenterObj = (TZbglZbrwdJcjg) object;
        
//      从表无需设置主键，uuid
        
        //设置业务字段
        businessCenterObj.setJcjgRwID(businessObj.getRdpWiIDX());//与JWPT_JCZBRWD主键关联
        businessCenterObj.setJcjgSjxmc(businessObj.getDiName());//数据项名称
        businessCenterObj.setJcjgSjxjg(businessObj.getDiResult());//数据项结果
    }

}
