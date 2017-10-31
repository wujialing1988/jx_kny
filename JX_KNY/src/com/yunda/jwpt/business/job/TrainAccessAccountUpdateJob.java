package com.yunda.jwpt.business.job;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.StringUtil;
import com.yunda.jwpt.business.entity.TZbglZbdzdt;
import com.yunda.jwpt.business.manager.TZbglZbdzdtManager;
import com.yunda.jwpt.common.BaseBusinessDataUpdateJob;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.jwpt.utils.BaseBusinessDataUpdateJobUtils;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车出入段台账更新业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "twt_train_access_account")
public class TrainAccessAccountUpdateJob extends BaseBusinessDataUpdateJob{
	
	//准备一个全局map存放值
	public static final Map<String, String> trainStatusMap = new HashMap<String, String>(); 
	
	//总公司同步需要 
	/*
	 * 01 待检
	 * 02 正在检查（修）
	 * 03 良好
	 * 04 非运用
	 */
	static {
		trainStatusMap.put(TrainAccessAccount.TRAINSTATUS_DAIJIAN, "01");
		trainStatusMap.put(TrainAccessAccount.TRAINSTATUS_ZHENGZAIJIANCHA, "02");
		trainStatusMap.put(TrainAccessAccount.TRAINSTATUS_LIANGHAO, "03");
		trainStatusMap.put(TrainAccessAccount.TRAINSTATUS_FEIYUNYONG, "04");
		
	}
    
    //获取容器中的对象
    /** TrainAccessAccount业务类,机车出入段台账 */
    TrainAccessAccountManager trainAccessAccountManager = (TrainAccessAccountManager) BaseBusinessDataUpdateJobUtils.instence.getBean(TRAINACCESS_ACCOUNT_MANAGER);
    
    //获取容器中的对象
    /** tZbglZbdzdtManager 整备场电子地图(机务平台数据同步官方模型) 业务类 */
    TZbglZbdzdtManager tZbglZbdzdtManager = (TZbglZbdzdtManager) BaseBusinessDataUpdateJobUtils.instence.getBean(T_ZBGL_ZBDZDT_MANAGER);
    
//    /**
//     * <li>方法说明：更新对应业务的数据
//     * <li>创建人：林欢
//     * <li>创建时间：2016-5-14 
//     * <li>修改人：
//     * <li>修改内容：
//     * <LI>重要业务逻辑:
//     * 当官方中间表中，当前数据为9=insert:【1】当系统中间表为5=update的时候，依旧是新增，数据为更新的数据
//     *                               【2】当系统中间表为3=delete的时候，删除官方中间表当前数据
//     *                      5=update:【1】当系统中间表为3=delete的时候，修改状态为删除状态（3=删除）
//     * TODO 未处理当系统中间表JwptDataSynchronizationCenterTable 同一个业务ID下，既有insert又有delete的情况
//     * @param 系统中间表list数据
//     * @return 成功更新业务后，对应的系统中间表对象，方便统一删除
//     * @throws NoSuchFieldException 
//     * @throws BusinessException 
//     * @throws InvocationTargetException 
//     * @throws IllegalAccessException 
//     * @throws NoSuchMethodException 
//     * @throws IllegalArgumentException 
//     * @throws SecurityException 
//     */
//    public List<JwptDataSynchronizationCenterTable> updateDataByBusiness(List<JwptDataSynchronizationCenterTable> dsctList) throws BusinessException, Exception{
//        
//        //准备一个执行业务操作后返回系统中间表表DataSynchronizationCenterTable 主键IDX的list
//        List<JwptDataSynchronizationCenterTable> returnIDXList = new ArrayList<JwptDataSynchronizationCenterTable>();
//        
//        //遍历传递过来的，机车出入段台账相关信息
//        for (JwptDataSynchronizationCenterTable jdsct : dsctList) {
//            String businessIDX = jdsct.getBusinessIDX();//业务主键idx
//            String operat = jdsct.getOperat();//操作(9=insert 5=update 3=delete)
//  
//            //通过业务主表idx查询业务表 机车出入段台账 对象
//            TrainAccessAccount businessObj = trainAccessAccountManager.getModelById(businessIDX);
//            
//            //通过业务表主键idx查询官方对应的中间表
//            TZbglZbdzdt businessCenterObj = tZbglZbdzdtManager.getModelById(businessIDX);
//            
//            if (businessCenterObj == null) {
//                businessCenterObj = new TZbglZbdzdt();
//                
//                businessCenterObj.setDtID(businessObj.getIdx());//idx
//                businessCenterObj.setRecordStatus(businessObj.getRecordStatus());//记录状态
//                businessCenterObj.setOperateType(Integer.valueOf(operat));//记录类型（3删除，5修改，9原始）
//            }else {
//                if (businessCenterObj.getOperateType() == IBaseBusinessDataUpdateJob.INSERT) {
//                    if (IBaseBusinessDataUpdateJob.UPDATE == Integer.valueOf(operat)) {
//                        businessCenterObj.setOperateType(Integer.valueOf(IBaseBusinessDataUpdateJob.INSERT));//记录类型（3删除，5修改，9原始）
//                    }else if (Integer.valueOf(operat) == IBaseBusinessDataUpdateJob.DELETE) {
//                        //由于该条数据既有9=新增，又有3=删除，在官方中间表中没有存在意义，删除
//                        tZbglZbdzdtManager.getDaoUtils().remove(businessCenterObj);
//                        returnIDXList.add(jdsct);
//                        continue;
//                    }
//                }else if (businessCenterObj.getOperateType() == IBaseBusinessDataUpdateJob.UPDATE) {
//                    if (Integer.valueOf(operat) == IBaseBusinessDataUpdateJob.DELETE) {
//                        //update-delete，修改官方中间表状态为删除
//                        businessCenterObj.setRecordStatus(1);//记录状态
//                        businessCenterObj.setOperateType(Integer.valueOf(IBaseBusinessDataUpdateJob.DELETE));//记录类型（3删除，5修改，9原始）
//                    }
//                }else {
//                    //delete状态跳过，不会出现删除后更新，或者新增情况
//                    //由于数据处理过，删除即便是都是最后一个出现
//                    returnIDXList.add(jdsct);
//                    continue;
//                }
//            }
//            
//            //设置业务字段值
//            businessCenterObj.setDtCxbm(businessObj.getTrainTypeIDX());//车型ID(来源于全路基础编码数据库) not null
//            businessCenterObj.setDtCxjc(businessObj.getTrainTypeShortName());//车型简称(来源于全路基础编码数据库) not null
//            businessCenterObj.setDtCh(businessObj.getTrainNo());//车号(四位数字，不足四位前补0) not null
//            businessCenterObj.setDtDqjczt(businessObj.getTrainStatus() == null ? " " : businessObj.getTrainStatus());//当前机车状态(待检、正在检查(修)、良好、非运用（临修、段备、局备、小辅修、大修、中修、封存、其它） not null
//            businessCenterObj.setDtZtzbsj(businessObj.getStartTime());//当前机车状态转变时间 not null
//            businessCenterObj.setDtSbbh(businessObj.getEquipGUID() == null ? " " : businessObj.getEquipGUID());//电子地图设备编号 not null
//            businessCenterObj.setDtDzdth(businessObj.getSiteID());//电子地图号，唯一识别各整备场电子地图（用全路基础编码中的整备场代码） not null
//            businessCenterObj.setDtJcssbsj(businessObj.getStartTime());//机车上设备时间 not null
//            businessCenterObj.setDtPxh(businessObj.getEquipOrder() == null ? 0 : Integer.valueOf(businessObj.getEquipOrder()));//机车在同一设备上的显示顺序 not null
//            businessCenterObj.setDtZbdbm("");//整备段编码（来源于全路基础编码数据库）
//            businessCenterObj.setDtRdsj(businessObj.getInTime());//入段时间
//            businessCenterObj.setDtCdsj(businessObj.getOutTime());//出段时间
//            
//            
////          设置公共字段值(创建人，创建时间，修改人，修改时间)
//            setCommonFiledValue(businessCenterObj);
//            
//            tZbglZbdzdtManager.getDaoUtils().getHibernateTemplate().saveOrUpdate(businessCenterObj);
//            
//            //记录当前操作成功的，系统中间表IDX，方便做统一删除处理
//            returnIDXList.add(jdsct);
//            
//        }
//        
//        return returnIDXList;
//    }

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
        return tZbglZbdzdtManager;
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
//      通过业务主表idx查询业务表 机车出入段台账 对象
        TrainAccessAccount businessObj = trainAccessAccountManager.getModelById(businessIDX);
        businessObjMap.put(BaseBusinessDataUpdateJob.BUSINESS_OBJ, businessObj);
        //通过业务表主键idx查询官方对应的中间表
        TZbglZbdzdt businessCenterObj = tZbglZbdzdtManager.getModelById(businessIDX);
        
        if (businessCenterObj == null) {
            businessCenterObj = new TZbglZbdzdt();
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
//      获取 机车出入段台账 对象
        TrainAccessAccount businessObj = (TrainAccessAccount) businessObjMap.get(BaseBusinessDataUpdateJob.BUSINESS_OBJ);
        TZbglZbdzdt businessCenterObj = (TZbglZbdzdt) object;
//      设置自定义主键id
        if (StringUtil.isNullOrBlank(businessCenterObj.getDtID())) {
            businessCenterObj.setDtID(businessObj.getIdx());
        }
        
//      设置业务字段值
        businessCenterObj.setDtCxbm(businessObj.getTrainTypeIDX());//车型ID(来源于全路基础编码数据库) not null
        businessCenterObj.setDtCxjc(businessObj.getTrainTypeShortName());//车型简称(来源于全路基础编码数据库) not null
        businessCenterObj.setDtCh(businessObj.getTrainNo());//车号(四位数字，不足四位前补0) not null
        businessCenterObj.setDtDqjczt(trainStatusMap.get(businessObj.getTrainStatus()));//当前机车状态(待检、正在检查(修)、良好、非运用（临修、段备、局备、小辅修、大修、中修、封存、其它） not null
        businessCenterObj.setDtZtzbsj(businessObj.getStartTime());//当前机车状态转变时间 not null
        businessCenterObj.setDtSbbh(businessObj.getEquipGUID() == null ? "D1G" : businessObj.getEquipGUID());//电子地图设备编号 not null
        //针对郑州配属段修改
        if ("501".equals(businessObj.getSiteID())) {
        	businessCenterObj.setDtDzdth("0" + businessObj.getSiteID());//电子地图号，唯一识别各整备场电子地图（用全路基础编码中的整备场代码） not null
		}else {
			businessCenterObj.setDtDzdth(businessObj.getSiteID());//电子地图号，唯一识别各整备场电子地图（用全路基础编码中的整备场代码） not null
		}
        businessCenterObj.setDtJcssbsj(businessObj.getOnEquipTime());//机车上设备时间 not null
        businessCenterObj.setDtPxh(businessObj.getEquipOrder() == null ? 0 : Integer.valueOf(businessObj.getEquipOrder()));//机车在同一设备上的显示顺序 not null
//      针对郑州配属段修改
        if ("501".equals(JXConfig.getInstance().getDid())) {
        	businessCenterObj.setDtZbdbm("0" + JXConfig.getInstance().getDid());//整备段编码（来源于全路基础编码数据库）
		}else {
			businessCenterObj.setDtZbdbm(JXConfig.getInstance().getDid());//整备段编码（来源于全路基础编码数据库）
		}
        
        businessCenterObj.setDtRdsj(businessObj.getInTime());//入段时间
        businessCenterObj.setDtCdsj(businessObj.getOutTime());//出段时间
    }

}
