package com.yunda.jwpt.business.job;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jwpt.business.entity.TZbglZbrwd;
import com.yunda.jwpt.business.manager.TZbglZbrwdManager;
import com.yunda.jwpt.common.BaseBusinessDataUpdateJob;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.jwpt.utils.BaseBusinessDataUpdateJobUtils;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备任务单-整备范围、普查、预警、整治等（主表）更新业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "zb_zbgl_rdp_wi")
public class ZbglRdpWiUpdateJob extends BaseBusinessDataUpdateJob{
    
    //获取容器中的对象
    /** ZbglRdpWi业务类,机车整备任务单 */
    ZbglRdpWiManager zbglRdpWiManager = (ZbglRdpWiManager) BaseBusinessDataUpdateJobUtils.instence.getBean(ZBGL_RDP_WI_MANAGER);
    
    //获取容器中的对象
    /** TZbglZbrwdManager 机车整备任务单-整备范围、普查、预警、整治等（主表）(机务平台数据同步官方模型) 业务类 */
    TZbglZbrwdManager tZbglZbrwdManager = (TZbglZbrwdManager) BaseBusinessDataUpdateJobUtils.instence.getBean(T_ZBGL_ZBRWD_MANAGER);
    
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
        return tZbglZbrwdManager;
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
        ZbglRdpWi businessObj = zbglRdpWiManager.getModelById(businessIDX);
        businessObjMap.put(BaseBusinessDataUpdateJob.BUSINESS_OBJ, businessObj);
        //通过业务表主键idx查询官方对应的中间表
        TZbglZbrwd businessCenterObj = tZbglZbrwdManager.getModelById(businessIDX);
        
        if (businessCenterObj == null) {
            businessCenterObj = new TZbglZbrwd();
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
    protected void setbusinessCenterObjProperty(Object object) {
//      获取 机车整备任务单 对象
        ZbglRdpWi businessObj = (ZbglRdpWi) businessObjMap.get(BaseBusinessDataUpdateJob.BUSINESS_OBJ);
        TZbglZbrwd businessCenterObj = (TZbglZbrwd) object;
//      设置自定义主键id
        if (StringUtil.isNullOrBlank(businessCenterObj.getZbrwdID())) {
            businessCenterObj.setZbrwdID(businessObj.getIdx());
        }
        
        //设置功能当前时间
        Date date = new Date();
        
//      设置业务字段值
        businessCenterObj.setZbhgzID(businessObj.getRdpIDX());//整备电子合格证ID not null
        businessCenterObj.setZbrwdZyrwlxdm(Integer.valueOf(businessObj.getWiClass() == null ? "0" : businessObj.getWiClass()));//作业任务类型代码:1、正常整备范围；2、普查和专项整治；3、预警；
        businessCenterObj.setZbrwdZyxmID(businessObj.getWiIDX());//作业项目ID（对应任务类型中不同类型的任务定义的主键）
        businessCenterObj.setZbrwdZyrw(businessObj.getWiName());//作业任务: 作业任务名称，1、正常整备范围，对应表T_ZBGL_ZBRWDMB中的ZBRWD_ZYXM字段；2、普查及专项整治范围，对应表T_ZBGL_PCJZXZZ_RWXM中的PC_XM_XMMC3、预警范围，对应表T_ZBGL_JCYJ中的JCYJ_YJX
        //TODO 作业任务: 作业任务名称，1、正常整备范围，对应表T_ZBGL_ZBRWDMB中的ZBRWD_ZYXM字段；2、普查及专项整治范围，对应表T_ZBGL_PCJZXZZ_RWXM中的PC_XM_XMMC3、预警范围，对应表T_ZBGL_JCYJ中的JCYJ_YJX 未确定？
        businessCenterObj.setZbrwdZysjbz(new Date());//作业时间标准
        businessCenterObj.setZbrwdZyrbm(businessObj.getHandlePersonID() == null ? " " : businessObj.getHandlePersonID().toString());//作业人编码
        businessCenterObj.setZbrwdZyrmc(businessObj.getHandlePersonName() == null ? " " : businessObj.getHandlePersonName().toString());//作业人名称
        businessCenterObj.setZbrwdLhsj(businessObj.getFetchTime());//领活时间
        businessCenterObj.setZbrwdXhsj(businessObj.getHandleTime());//销活时间
        //TODO 互检人员编码 未确定？
        businessCenterObj.setZbrwdHjrybm(" ");//互检人员编码
//      TODO 互检人员姓名 未确定？
        businessCenterObj.setZbrwdHjryxm(" ");//互检人员姓名
//      TODO 互检人员编码 未确定？
        businessCenterObj.setZbrwdHjsj(date);//互检人员编码
//      TODO 验收人编码 未确定？
        businessCenterObj.setZbrwdYsrbm(" ");//验收人编码
//      TODO 验收人姓名 未确定？
        businessCenterObj.setZbrwdYsrmc(" ");//验收人姓名
//      TODO 验收时间 未确定？
        businessCenterObj.setZbrwdYssj(date);//验收时间
    }

}
