package com.yunda.jwpt.business.job;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.StringUtil;
import com.yunda.jwpt.business.entity.TZbglJyxhtz;
import com.yunda.jwpt.business.manager.TZbglJyxhtzManager;
import com.yunda.jwpt.common.BaseBusinessDataUpdateJob;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.jwpt.utils.BaseBusinessDataUpdateJobUtils;
import com.yunda.zb.oilconsumption.entity.OilconSumption;
import com.yunda.zb.oilconsumption.manager.OilconSumptionManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机油消耗台账 更新业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "zb_zbgl_oil_consumption")
public class OilconSumptionUpdateJob extends BaseBusinessDataUpdateJob{
    
    //获取容器中的对象
    /** OilconSumption业务类,机油消耗台账 */
    OilconSumptionManager oilconSumptionManager = (OilconSumptionManager) BaseBusinessDataUpdateJobUtils.instence.getBean(OIL_CONSUMPTION_MANAGER);
    
    //获取容器中的对象
    /** TZbglJyxhtzManager 机油消耗台账(机务平台数据同步官方模型) 业务类 */
    TZbglJyxhtzManager tZbglJyxhtzManager = (TZbglJyxhtzManager) BaseBusinessDataUpdateJobUtils.instence.getBean(T_ZBGL_JYXHTZ_MANAGER);
    
    /**
     * <li>方法说明：获取对应的业务对象
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @param 业务主键idx
     * @return 返回需要的业务对象 key=businessCenterObj【官方中间表】，【businessObj系统业务表】 value=对应的实体对象
     */
    @Override
    protected JXBaseManager getBusinessCenterManager() {
        return tZbglJyxhtzManager;
    }

    /**
     * <li>方法说明：获取官方中间表manager对象(如果businessObjMap中没有官方的中间表对象，新增时候，需要赋值recordStatus，如果有operateType也需要赋值)
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @param jdsct 系统中间表对象
     * @return 返回官方中间表业务manager对象 
     */
    @Override
    protected Map<String, Object> getBusinessObjMap(JwptDataSynchronizationCenterTable jdsct) {
        //业务表主键idx
        String businessIDX = jdsct.getBusinessIDX();
        //操作 3删除 5更新 9新增
        String opeart = jdsct.getOperat();
//      通过业务主表idx查询业务表 机车整备任务单 对象
        OilconSumption businessObj = oilconSumptionManager.getModelById(businessIDX);
        businessObjMap.put(BaseBusinessDataUpdateJob.BUSINESS_OBJ, businessObj);
        
        //通过业务表主键idx查询官方对应的中间表
        TZbglJyxhtz businessCenterObj = tZbglJyxhtzManager.getModelById(businessIDX);
            
        if (businessCenterObj == null) {
            businessCenterObj = new TZbglJyxhtz();
            businessCenterObj.setRecordStatus(businessObj.getRecordStatus());
            businessCenterObj.setOperateType(Integer.valueOf(opeart));
            
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
    protected void setbusinessCenterObjProperty(Object object){
//      获取 机车整备任务单 对象
        OilconSumption businessObj = (OilconSumption) businessObjMap.get(BaseBusinessDataUpdateJob.BUSINESS_OBJ);
        TZbglJyxhtz businessCenterObj = (TZbglJyxhtz) object;
//      设置自定义主键id
        if (StringUtil.isNullOrBlank(businessCenterObj.getJyID())) {
            businessCenterObj.setJyID(businessObj.getIdx());
        }
        
//      设置业务字段值
        businessCenterObj.setJyCxbm(businessObj.getTrainTypeIDX());//车型编码（来源于全路基础编码数据库） not null
        //TODO 车型简称（来源于全路基础编码数据库）未确定？
        businessCenterObj.setJyCxjc(" ");//车型简称（来源于全路基础编码数据库）
        businessCenterObj.setJyCxpym(businessObj.getTrainTypeShortName());//车型简称 not null
        businessCenterObj.setJyCh(businessObj.getTrainNo());//车号 not null
        businessCenterObj.setJyJyzlbm(businessObj.getJyCode());//机油种类编码,（基础编码统一规范） not null
        businessCenterObj.setJyJyzlmc(businessObj.getJyName());//机油种类名称 not null
        businessCenterObj.setJyJysl(new BigDecimal(businessObj.getConsumeQty() == null ? 0d : businessObj.getConsumeQty()).intValue());//机油数量 not null
        businessCenterObj.setJyJldw(businessObj.getDw());//计量单位 not null
        businessCenterObj.setJyLysj(businessObj.getFetchTime());//领用时间 not null
        //TODO 整备段编码（来源于全路基础编码数据库） 未确定？ not null
        businessCenterObj.setJyZbdbm(JXConfig.getInstance().getOverseaOrgcode());//整备段编码（来源于全路基础编码数据库）
        
    }

}
