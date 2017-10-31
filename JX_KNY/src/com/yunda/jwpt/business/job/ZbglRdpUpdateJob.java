package com.yunda.jwpt.business.job;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.StringUtil;
import com.yunda.jwpt.business.entity.TZbglZbdzhgz;
import com.yunda.jwpt.business.manager.TZbglZbdzhgzManager;
import com.yunda.jwpt.common.BaseBusinessDataUpdateJob;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.jwpt.utils.BaseBusinessDataUpdateJobUtils;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备电子合格证更新业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "zb_zbgl_rdp")
public class ZbglRdpUpdateJob extends BaseBusinessDataUpdateJob{
    
    //获取容器中的对象
    /** ZbglRdp业务类,机车整备单 */
    ZbglRdpManager zbglRdpManager = (ZbglRdpManager) BaseBusinessDataUpdateJobUtils.instence.getBean(ZBGL_RDP_MANAGER);
    
    //获取容器中的对象
    /** TZbglZbdzhgzManager 机车整备电子合格证(机务平台数据同步官方模型) 业务类 */
    TZbglZbdzhgzManager tZbglZbdzhgzManager = (TZbglZbdzhgzManager) BaseBusinessDataUpdateJobUtils.instence.getBean(T_ZBGL_ZBDZHGZ_MANAGER);
    
    //获取容器中的对象
    /** TrainAccessAccount业务类,机车出入段台账 */
    TrainAccessAccountManager trainAccessAccountManager = (TrainAccessAccountManager) BaseBusinessDataUpdateJobUtils.instence.getBean(TRAINACCESS_ACCOUNT_MANAGER);
    
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
        return tZbglZbdzhgzManager;
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
        
//      通过业务主表idx查询业务表 机车整备单 对象
        ZbglRdp businessObj = zbglRdpManager.getModelById(businessIDX);
        businessObjMap.put(BaseBusinessDataUpdateJob.BUSINESS_OBJ, businessObj);
        
        //通过业务表主键idx查询官方对应的中间表
        TZbglZbdzhgz businessCenterObj = tZbglZbdzhgzManager.getModelById(businessIDX);
        
        if (businessCenterObj == null) {
            businessCenterObj = new TZbglZbdzhgz();
            businessCenterObj.setRecordStatus(businessObj.getRecordStatus());
            businessCenterObj.setOperateType(Integer.valueOf(opeart));
            
            String rdpStatus = businessObj.getRdpStatus();
            if (StringUtil.isNullOrBlank(rdpStatus)) {
            	businessCenterObj.setZbhgzZt("01");//未签
			}else {
				if (ZbglRdp.STATUS_HANDLED.equals(rdpStatus)) {
					businessCenterObj.setZbhgzZt("02");//已签
				}else {
					businessCenterObj.setZbhgzZt("01");//未签
				}
			}
            
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
//      获取 机车整备单 对象
        ZbglRdp businessObj = (ZbglRdp) businessObjMap.get(BaseBusinessDataUpdateJob.BUSINESS_OBJ);
        
        //通过 机车出入段台账主键idx查询对应的对象
        TrainAccessAccount trainAccessAccount = null;
//        if (StringUtils.isNotBlank(businessObj.getTrainAccessAccountIDX())) {
            trainAccessAccount = trainAccessAccountManager.getModelById(businessObj.getTrainAccessAccountIDX());
//        }else {
//            trainAccessAccount = new TrainAccessAccount();
//        }
        
        TZbglZbdzhgz businessCenterObj = (TZbglZbdzhgz) object;
//      设置自定义主键id
        if (StringUtil.isNullOrBlank(businessCenterObj.getZbhgzID())) {
            businessCenterObj.setZbhgzID(businessObj.getIdx());
        }
        
//      设置业务字段值
        businessCenterObj.setZbhgzCxbm(businessObj.getTrainTypeIDX());//车型ID（来源于全路基础编码数据库） not null
        businessCenterObj.setZbhgzCxpym(businessObj.getTrainTypeShortName());//车型拼音码 not null
        businessCenterObj.setZbhgzCh(businessObj.getTrainNo());//车号 not null
        businessCenterObj.setZbhgzPsdbm(JXConfig.getInstance().getDid());//配属段编码（来源于全路基础编码数据库） not null
        businessCenterObj.setZbhgzPsdmc(JXConfig.getInstance().getDname());//配属段名称 not null
        businessCenterObj.setZbhgzDdcc(trainAccessAccount.getArriveOrder());//到达车次
        businessCenterObj.setZbhgzRdsj(trainAccessAccount.getInTime());//入段时间 not null
        businessCenterObj.setZbhgzJhcc(trainAccessAccount.getPlanOrder());//计划车次
        businessCenterObj.setZbhgzJhcdsj(trainAccessAccount.getPlanOutTime());//计划出段时间
        businessCenterObj.setZbhgzCdsj(trainAccessAccount.getOutTime());//出段时间
        businessCenterObj.setZbhgzZbkssj(businessObj.getRdpStartTime());//整备开始时间
        businessCenterObj.setZbhgzZbjssj(businessObj.getRdpEndTime());//整备结束时间
//      针对郑州配属段修改
        if ("501".equals(JXConfig.getInstance().getOverseaOrgcode())) {
        	businessCenterObj.setZbhgzZbdbm("0" + JXConfig.getInstance().getOverseaOrgcode());//整备段编码（来源于全路基础编码数据库） not null
		}else {
			businessCenterObj.setZbhgzZbdbm(JXConfig.getInstance().getOverseaOrgcode());//整备段编码（来源于全路基础编码数据库） not null
		}
        businessCenterObj.setZbhgzZbdmc(BaseBusinessDataUpdateJobUtils.instence.getOverseaOrgNameByCode(JXConfig.getInstance().getOverseaOrgcode()));//整备段名称 not null
        //TODO 整备车间名称 来源不确定？
        businessCenterObj.setZbhgzZbcjmc(" ");//整备车间名称
        //TODO 整备后机车去向 来源不确定？
        businessCenterObj.setZbhgzZbhjcqx(" ");//整备后机车去向
    }

}
