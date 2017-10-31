package com.yunda.jwpt.business.job;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.StringUtil;
import com.yunda.jwpt.business.entity.TZbglJt6;
import com.yunda.jwpt.business.entity.TZbglLxrwd;
import com.yunda.jwpt.business.entity.TZbglPjgh;
import com.yunda.jwpt.business.manager.TZbglJt6Manager;
import com.yunda.jwpt.business.manager.TZbglLxrwdManager;
import com.yunda.jwpt.business.manager.TZbglPjghManager;
import com.yunda.jwpt.common.BaseBusinessDataUpdateJob;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.jwpt.utils.BaseBusinessDataUpdateJobUtils;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.manager.ZbglTpManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备电子合格证，JT6提票(机务平台数据同步官方模型)更新业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "zb_zbgl_jt6")
public class ZbglTpUpdateJob extends BaseBusinessDataUpdateJob{
    
    //获取容器中的对象
    /** ZbglTp业务类,JT6提票 */
    ZbglTpManager zbglTpManager = (ZbglTpManager) BaseBusinessDataUpdateJobUtils.instence.getBean(ZBGL_TP_MANAGER);
    
    //获取容器中的对象
    /** TZbglJt6Manager 整备场电子地图(机务平台数据同步官方模型)(碎修) 业务类 */
    TZbglJt6Manager tZbglJt6Manager = (TZbglJt6Manager) BaseBusinessDataUpdateJobUtils.instence.getBean(T_ZBGL_JT6_MANAGER);
    
    //获取容器中的对象
    /** TZbglLxrwdManager 机车整备电子合格证-JT6提票(机务平台数据同步官方模型)（临修 处理后上传结果） 业务类 */
    TZbglLxrwdManager tZbglLxrwdManager = (TZbglLxrwdManager) BaseBusinessDataUpdateJobUtils.instence.getBean(T_ZBGL_LXRWD_MANAGER);
    
    //获取容器中的对象
    /** TZbglPjghManager 机车整备电子合格证- JT6提票（配件更换）(机务平台数据同步官方模型) 业务类 */
    TZbglPjghManager tZbglPjghManager = (TZbglPjghManager) BaseBusinessDataUpdateJobUtils.instence.getBean(T_ZBGL_PJGH_MANAGER);
    
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
        return tZbglJt6Manager;
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
//      操作 3删除 5更新 9新增
        String opeart = jdsct.getOperat();
        
//      通过业务主表idx查询业务表 JT6提票 对象(碎修以及碎修)
//      通过业务主表idx查询业务表 ZB_ZBGL_JT6 对象
        ZbglTp businessObj = zbglTpManager.getModelById(businessIDX);
        businessObjMap.put(BaseBusinessDataUpdateJob.BUSINESS_OBJ, businessObj);
        
            
//      通过业务表主键idx查询官方对应的中间表(碎修)
        TZbglJt6 businessCenterObj = tZbglJt6Manager.getModelById(businessIDX);
        
        if (businessCenterObj == null) {
            businessCenterObj = new TZbglJt6();
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
        
//      获取 JT6提票 对象(碎修以及碎修)
        ZbglTp businessObj = (ZbglTp) businessObjMap.get(BaseBusinessDataUpdateJob.BUSINESS_OBJ);
        
        //处理空数据 如果整备单为空，不生成数据
        if (StringUtils.isNotBlank(businessObj.getRdpIDX())) {
	        TZbglJt6 businessCenterObj = (TZbglJt6) object;
	//          设置自定义主键id
	        if (StringUtil.isNullOrBlank(businessCenterObj.getJt6ID())) {
	            businessCenterObj.setJt6ID(businessObj.getIdx());
	        }
	        
	//          设置业务字段值
	        businessCenterObj.setZbhgzID(businessObj.getRdpIDX());//整备电子合格证ID not null
	        businessCenterObj.setZbhgzCxbm(businessObj.getTrainTypeIDX());//车型编码 not null
	        businessCenterObj.setZbhgzCxpym(businessObj.getTrainTypeShortName());//车型拼音码 not null
	        businessCenterObj.setZbhgzCh(businessObj.getTrainNo());//车号 not null
	        
//	     	针对郑州配属段修改
	        if ("501".equals(JXConfig.getInstance().getDid())) {
	        	businessCenterObj.setZbhgzPsdbm("0" + JXConfig.getInstance().getDid());//配属段编码 not null
			}else {
				businessCenterObj.setZbhgzPsdbm(JXConfig.getInstance().getDid());//配属段编码 not null
			}
	        businessCenterObj.setZbhgzPsdmc(JXConfig.getInstance().getDname());//配属段名称 not null
	        businessCenterObj.setJt6Tpdh(businessObj.getFaultNoticeCode());//提票单号 not null
	        businessCenterObj.setJt6Tpsj(businessObj.getNoticeTime());//提票时间 not null
	        businessCenterObj.setJt6Tprbm(businessObj.getNoticePersonId().toString());//提票人编码 not null
	        businessCenterObj.setJt6Tprxm(businessObj.getNoticePersonName());//提票人姓名 not null
	        businessCenterObj.setJt6Tplymc(businessObj.getNoticeSource());//提票来源名称：1、运用；2、整备；3、质检；4、技术； not null
	        businessCenterObj.setJt6Jprbm(businessObj.getRevPersonId() == null ? " " : businessObj.getRevPersonId().toString());//接票人编码
	        businessCenterObj.setJt6Jprxm(businessObj.getRevPersonName() == null ? " " : businessObj.getRevPersonName());//接票人姓名
	        businessCenterObj.setJt6Jpsj(businessObj.getRevTime());//接票时间
	        businessCenterObj.setJt6Xprbm(businessObj.getHandlePersonId() == null ? " " : businessObj.getHandlePersonId().toString());//销票人编码
	        businessCenterObj.setJt6Xprxm(businessObj.getHandlePersonName());//销票人姓名
	        businessCenterObj.setJt6Xpsj(businessObj.getHandleTime());//销票时间
	        businessCenterObj.setJt6Ysrbm(businessObj.getAccPersonId() == null ? " " : businessObj.getAccPersonId().toString());//验收人编码
	        businessCenterObj.setJt6Ysrxm(businessObj.getAccPersonName());//验收人姓名
	        businessCenterObj.setJt6Yssj(businessObj.getAccTime());//验收时间
	        businessCenterObj.setJt6JcgzID(businessObj.getFaultID());//故障ID （基础码表）
	        businessCenterObj.setJt6Gzbjbm(businessObj.getFaultFixFullCode());//故障部件编码（基础码表）
	        businessCenterObj.setJt6Gzbjmc(businessObj.getFaultFixFullName());//故障部件名称（系统/部位/部件）
	        businessCenterObj.setJt6Gzms(businessObj.getFaultDesc());//故障描述格式（系统/部位/部件/故障描述）
	        businessCenterObj.setJt6Cljg(businessObj.getRepairResult());//处理结果  1：修复、2：观察运用、3：转机统28、4：转临修；5、返本段修；6、扣车等件；
	        businessCenterObj.setJt6Sxff(businessObj.getRepairDesc());//施修方法描述（应提供常用处理方法字典，如更换、焊修、紧固、清扫等）
	        businessCenterObj.setJt6Gzfsrq(businessObj.getFaultOccurDate());//故障发生日期
		}
    }
    
    /**
     * <li>方法说明：当需要持久化从表的时候使用，该方法内单独处理更新官方中间表从表信息
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     */
    @Override
    protected void saveBusinessCenterFromTable() throws NoSuchFieldException{
        
        //=========================保存机车整备电子合格证-JT6提票（临修 处理后上传结果）(临修 repair_class=20)(机务平台数据同步官方模型)开始=====================================

        //      获取 JT6提票 对象(碎修以及碎修)
        ZbglTp businessObj = (ZbglTp) businessObjMap.get(BaseBusinessDataUpdateJob.BUSINESS_OBJ);
        if (ZbConstants.REPAIRCLASS_LX.equals(businessObj.getRepairClass())) {
//          封装查询条件
            TZbglLxrwd paramTZbglLxrwd = new TZbglLxrwd();
            paramTZbglLxrwd.setLxJt6ID(businessObj.getIdx());
            //调用方法查询是否有从表数据关联
            TZbglLxrwd tZbglLxrwd = tZbglLxrwdManager.findTZbglLxrwdByJt6ID(paramTZbglLxrwd);
            if (tZbglLxrwd == null) {
                tZbglLxrwd = new TZbglLxrwd();
                tZbglLxrwd.setLxJt6ID(businessObj.getIdx());//主键 not null
            }
            
//          设置业务字段值
            tZbglLxrwd.setLxJprbm(businessObj.getRevPersonId() == null ? "null" : businessObj.getRevPersonId().toString());//接票人编码 not null
            tZbglLxrwd.setLxJprxm(businessObj.getRevPersonName() == null ? "null" : businessObj.getRevPersonName().toString());//接票人姓名 not null
            tZbglLxrwd.setLxJpsj(businessObj.getRevTime());//接票时间
            tZbglLxrwd.setLxXprbm(businessObj.getHandlePersonId() == null ? "null" : businessObj.getHandlePersonId().toString());//销票人编码 not null
            tZbglLxrwd.setLxXprxm(businessObj.getHandlePersonName() == null ? "null" : businessObj.getHandlePersonName().toString());//销票人姓名 not null
            tZbglLxrwd.setLxXpsj(businessObj.getHandleTime());//销票时间 not null
            tZbglLxrwd.setLxYsrbm(businessObj.getAccPersonId() == null ? "null" : businessObj.getAccPersonId().toString());//验收人编码
            tZbglLxrwd.setLxYsrxm(businessObj.getAccPersonName() == null ? "null" : businessObj.getAccPersonName().toString());//验收人姓名
            tZbglLxrwd.setLxYssj(businessObj.getAccTime());//验收时间
            tZbglLxrwd.setLxCljg(businessObj.getRepairResult());//1：修复、2：转段修、3：返中修、4：返大修；5、扣车等件；6、返本段修； not null
            tZbglLxrwd.setLxSxff(businessObj.getRepairDesc() == null ? "null" : businessObj.getRepairDesc());//施修方法描述（应提供常用处理方法字典，如更换、焊修、紧固、清扫等） not null
            
//          调用工具方法赋值
            BaseBusinessDataUpdateJobUtils.instence.doSetCommonFiledValue(tZbglLxrwd);
            tZbglLxrwdManager.getDaoUtils().saveOrUpdate(tZbglLxrwd);
        }
        
        //=========================保存机车整备电子合格证-JT6提票（临修 处理后上传结果）(临修 repair_class=20)(机务平台数据同步官方模型)结束=====================================
        
//        //=========================保存机车整备电子合格证- JT6提票（配件更换）(机务平台数据同步官方模型)开始=====================================
//        
//        //通过主表idx查询是否保存了主表（情况：系统中间表先insert再delete在官方中间表中不会出现该条业务表记录）
//        
//        
////      封装查询条件
//        TZbglPjgh paramTZbglPjgh = new TZbglPjgh();
//        paramTZbglPjgh.setPjghJt6ID(businessObj.getIdx());
//        
//        //调用方法查询是否有从表数据关联
//        TZbglPjgh tZbglPjgh = tZbglPjghManager.findTZbglPjghByJt6ID(paramTZbglPjgh);
//        
//        if (tZbglPjgh == null) {
//            tZbglPjgh = new TZbglPjgh();
//            tZbglPjgh.setPjghJt6ID(businessObj.getIdx());//JT6ID 来源于表T_ZBGL_JT6中的主键 not null
//        }
//        
//        //TODO 机车构型码（来源于基础编码表） 未确定?
//        tZbglPjgh.setPjghJcgxbm(" ");//机车构型码（来源于基础编码表） not null
//        tZbglPjgh.setPjghJcbjmc(" ");//机车部件名称 not null
//        tZbglPjgh.setPjghJcbjmc(" ");//机车部件名称 not null
//        tZbglPjgh.setPjghXcbjbm(" ");//下车部件编码 not null
//        tZbglPjgh.setPjghGhbjbm(" ");//更换部件编码 not null
//        tZbglPjgh.setRecordStatus(0);
//        
//        //调用工具方法赋值
//        BaseBusinessDataUpdateJobUtils.instence.doSetCommonFiledValue(tZbglPjgh);
//        tZbglPjghManager.getDaoUtils().saveOrUpdate(tZbglPjgh);
//        
//        //=========================保存机车整备电子合格证- JT6提票（配件更换）(机务平台数据同步官方模型)结束=====================================
    }
    
    /**
     * <li>方法说明：当需要删除从表的时候使用，该方法内单独处理更新官方中间表从表信息
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     */
    @Override
    protected void deleteBusinessCenterFromTable(){
        
//      =========================删除机车整备电子合格证-JT6提票（临修 处理后上传结果）(临修 repair_class=20)(机务平台数据同步官方模型)开始=====================================
        
//      获取 JT6提票 对象(碎修以及碎修)
        ZbglTp businessObj = (ZbglTp) businessObjMap.get(BaseBusinessDataUpdateJob.BUSINESS_OBJ);
        
        //封装查询条件
        TZbglLxrwd tZbglLxrwd = new TZbglLxrwd();
        tZbglLxrwd.setLxJt6ID(businessObj.getIdx());
        
        //调用方法查询是否有从表数据关联
        TZbglLxrwd lxrwdList = tZbglLxrwdManager.findTZbglLxrwdByJt6ID(tZbglLxrwd);
        if (lxrwdList != null) {
            tZbglLxrwdManager.getDaoUtils().remove(lxrwdList);
        }
        
//      =========================删除机车整备电子合格证-JT6提票（临修 处理后上传结果）(临修 repair_class=20)(机务平台数据同步官方模型)结束=====================================
        
//      =========================删除机车整备电子合格证- JT6提票（配件更换）(机务平台数据同步官方模型)开始=====================================
        
        //封装查询条件
        TZbglPjgh tZbglPjgh = new TZbglPjgh();
        tZbglPjgh.setPjghID(businessObj.getIdx());
        
        //调用方法查询是否有从表数据关联
        TZbglPjgh pjghList = tZbglPjghManager.findTZbglPjghByJt6ID(tZbglPjgh);
        if (pjghList != null) {
            tZbglPjghManager.getDaoUtils().remove(pjghList);
        }
        
//      =========================删除机车整备电子合格证- JT6提票（配件更换）(机务平台数据同步官方模型)结束=====================================
    }
}
