package com.yunda.freight.zb.detain.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;
import com.yunda.freight.base.vehicle.manager.TrainVehicleTypeManager;
import com.yunda.freight.zb.detain.entity.DetainTrain;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
import com.yunda.jx.jczl.attachmanage.manager.TrainStatusChangeManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 扣车管理业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-20 17:26:28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("detainTrainManager")
public class DetainTrainManager extends JXBaseManager<DetainTrain, DetainTrain> {

    /**
     * 车型业务类
     */
    @Resource
    private TrainVehicleTypeManager trainVehicleTypeManager;
    
    /**
     * 车辆信息业务类
     */
    @Resource
    private JczlTrainManager jczlTrainManager;
    
    
    /**
     * 车辆状态业务类
     */
    @Resource
    private TrainStatusChangeManager trainStatusChangeManager;
    
    
    
    
    /**
     * <li>说明：提票上报时发起扣车申请（建议先不调用）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void applyGztpToDetain(DetainTrain entity) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        TrainVehicleType vehicleType = trainVehicleTypeManager.getModelById(entity.getTrainTypeIdx());
        if(vehicleType == null){
            return ; // 扣车车型不存在
        }
        String msg = trainStatusChangeManager.verificationOperation(entity.getTrainTypeIdx(), entity.getTrainNo(), new Integer[]{JczlTrain.TRAIN_STATE_REPAIR,JczlTrain.TRAIN_STATE_DETAIN}, "扣车申请");
        if(!StringUtil.isNullOrBlank(msg)){
            return ; // 检修中的车不能发起扣车申请
        }
        DetainTrain train = this.getDetainTrainByTypeAndNo(entity.getTrainTypeIdx(), entity.getTrainNo());
        if(train != null){
            return ; // 车辆已经发起过扣车申请
        }
        // 如果是拒绝后再申请 ， 拒绝的数据不能基于以前的数据重新生成
        DetainTrain entitySave = null;
        if(entitySave == null){
            entitySave = new DetainTrain();
        }
        buildVoToEntity(entity, vehicleType, entitySave);  // 构建数据
        this.saveOrUpdate(entitySave);
    }
    
    /**
     * <li>说明：扣车申请
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 申请实体
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws NoSuchFieldException 
     */    
    public void applyDetainTrain(DetainTrain entity) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        TrainVehicleType vehicleType = trainVehicleTypeManager.getModelById(entity.getTrainTypeIdx());
        if(vehicleType == null){
            throw new BusinessException("该车型不存在！");
        }
        String msg = trainStatusChangeManager.verificationOperation(entity.getTrainTypeIdx(), entity.getTrainNo(), new Integer[]{JczlTrain.TRAIN_STATE_REPAIR,JczlTrain.TRAIN_STATE_DETAIN}, "扣车申请");
        if(!StringUtil.isNullOrBlank(msg)){
            throw new BusinessException(msg);
        }
        DetainTrain train = this.getDetainTrainByTypeAndNo(entity.getTrainTypeIdx(), entity.getTrainNo());
        if(train != null){
            throw new BusinessException("该车辆已发出扣车申请！");
        }
        // 如果是拒绝后再申请 ， 拒绝的数据不能基于以前的数据重新生成
        DetainTrain entitySave = null;
//        if(!StringUtil.isNullOrBlank(entity.getIdx())){
//            entitySave = this.getModelById(entity.getIdx());
//        }
        if(entitySave == null){
            entitySave = new DetainTrain();
        }
        buildVoToEntity(entity, vehicleType, entitySave);  // 构建数据
        this.saveOrUpdate(entitySave);
    }

    /**
     * <li>说明：构建数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 数据源
     * @param vehicleType 车型实体
     * @param entitySave 待保存数据
     */
    private void buildVoToEntity(DetainTrain entity, TrainVehicleType vehicleType, DetainTrain entitySave) {
        OmEmployee emp = SystemContext.getOmEmployee();
        if(emp != null){
            entitySave.setProposerIdx(emp.getEmpid()); // 申请人ID
            entitySave.setProposerName(emp.getEmpname()); // 申请人名称
        }
        entitySave.setProposerDate(new Date()); // 申请时间
        entitySave.setDetainStatus(DetainTrain.TRAIN_STATE_APPLY);  // 扣车状态
        entitySave.setTrainTypeIdx(vehicleType.getIdx());           // 车型主键
        entitySave.setTrainTypeCode(vehicleType.getTypeCode()) ;    // 车型编码
        entitySave.setTrainTypeName(vehicleType.getTypeName()) ;    // 车型名称
        entitySave.setTrainNo(entity.getTrainNo());                 // 车号
        entitySave.setDetainReason(entity.getDetainReason());       // 扣车原因
        entitySave.setDetainTypeCode(entity.getDetainTypeCode());   // 扣车类型编码
        entitySave.setDetainTypeName(entity.getDetainTypeName());   // 扣车类型名称
        entitySave.setVehicleType(entity.getVehicleType());         // 客货类型
        entitySave.setApproveIdx(null);                             // 审批人ID
        entitySave.setApproveName(null);                            // 审批人姓名
        entitySave.setApproveDate(null);                            // 审批时间
        entitySave.setApproveOpinion(null);                         // 审批意见
        entitySave.setOrderNo(null);                                // 命令号
        entitySave.setOrderUser(null);                              // 命令发布者
        entitySave.setOrderDate(null);                              // 命令发布时间
    }

    /**
     * <li>说明：撤销扣车
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 扣车主键
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void deleteDetainTrain(String idx) throws BusinessException, NoSuchFieldException {
        if(StringUtil.isNullOrBlank(idx)){
           return ; 
        }
        DetainTrain entity = this.getModelById(idx);
        if(entity == null){
            throw new BusinessException("未找到需要【撤销】的数据！");
        }else if(!entity.getDetainStatus().equals(DetainTrain.TRAIN_STATE_APPLY)){
            throw new BusinessException("扣车申请已审批，不能撤销！");
        }
        this.logicDelete(idx);
    }
    
    /**
     * <li>说明：通过车型车号获取申请中的扣车记录
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx
     * @param trainNo
     * @return
     */
    public DetainTrain getDetainTrainByTypeAndNo(String trainTypeIdx,String trainNo){
        StringBuffer hql =  new StringBuffer(" From DetainTrain where recordStatus = 0 and detainStatus = ? and trainTypeIdx = ? and trainNo = ? order by updateTime desc ");
        return (DetainTrain)this.daoUtils.findSingle(hql.toString(), new Object[]{DetainTrain.TRAIN_STATE_APPLY,trainTypeIdx,trainNo});
    }
    
    /**
     * 审批前验证
     */
    @Override
    public String[] validateUpdate(DetainTrain entity) {
        String[] errorMsg = super.validateUpdate(entity);
        if (null != errorMsg) {
            return errorMsg;
        }
        DetainTrain detainTrain = this.getModelById(entity.getIdx());
        if(detainTrain == null){
            return new String[]{"数据不存在，请刷新列表重试！"};
        }
        if (detainTrain.getDetainStatus().equals(DetainTrain.TRAIN_STATE_APPROVED)) {
            return new String[]{"该数据已审批完成，请刷新列表重试！"};
        }
        return null;
    }

    /**
     * <li>说明：审批扣车申请
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void saveDetainTrain(DetainTrain entity) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        DetainTrain entitySave = this.getModelById(entity.getIdx());
        String msg = trainStatusChangeManager.verificationOperation(entitySave.getTrainTypeIdx(), entitySave.getTrainNo(), new Integer[]{JczlTrain.TRAIN_STATE_REPAIR,JczlTrain.TRAIN_STATE_DETAIN}, "扣车审批");
        if(!StringUtil.isNullOrBlank(msg)){
            throw new BusinessException(msg);
        }
        buildEntity(entity, entitySave);
        this.saveOrUpdate(entitySave);
        // 如果审批通过，则修改机车信息，改状态为【扣车】
        if(entitySave.getDetainStatus().equals(DetainTrain.TRAIN_STATE_APPROVED)){
            trainStatusChangeManager.saveChangeRecords(entitySave.getTrainTypeIdx(), entitySave.getTrainNo(), JczlTrain.TRAIN_STATE_DETAIN, entitySave.getIdx(), "扣车");
        }
    }

    /**
     * <li>说明：审批时构建数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     * @param entitySave
     */
    private void buildEntity(DetainTrain entity, DetainTrain entitySave) {
        OmEmployee emp = SystemContext.getOmEmployee();
        if(emp != null){
            entitySave.setApproveIdx(emp.getEmpid());               // 审批人ID
            entitySave.setApproveName(emp.getEmpname());            // 审批人名称
        }
        entitySave.setApproveDate(new Date());                      // 审批时间
        entitySave.setApproveOpinion(entity.getApproveOpinion());   // 审批意见
        entitySave.setOrderNo(entity.getOrderNo());                 // 命令号
        entitySave.setOrderUser(entity.getOrderUser());             // 命令发布者
        entitySave.setOrderDate(entity.getOrderDate());             // 命令发布时间
        entitySave.setDetainStatus(entity.getDetainStatus());       // 数据状态
    }
    
   
}
