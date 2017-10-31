package com.yunda.freight.zb.rdp.manager;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.freight.zb.rdp.entity.ZbFwVehicleRelation;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检范围适用车型
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbFwVehicleRelationManager")
public class ZbFwVehicleRelationManager extends JXBaseManager<ZbFwVehicleRelation, ZbFwVehicleRelation> {

    /**
     * <li>说明：保存列检范围与车辆关系
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIdx 范围ID
     * @param trianCodeArray 车辆集合
     * @param trianTypeArray 车辆名称集合
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveVehicleRelations(String zbfwIdx, String[] trianCodeArray, String[] trianTypeArray) throws BusinessException, NoSuchFieldException {
        // 删除范围与车辆关系
        deleteVehicleRelations(zbfwIdx);
        // 保存范围与车辆关系
        for (int i = 0; i < trianCodeArray.length; i++) {
            String trainVehicleCode = trianCodeArray[i];
            String trainVehicleName = trianTypeArray[i];
            ZbFwVehicleRelation relation = new ZbFwVehicleRelation();
            relation.setZbfwIdx(zbfwIdx);
            relation.setTrainVehicleCode(trainVehicleCode);
            relation.setTrainVehicleName(trainVehicleName);
            this.saveOrUpdate(relation);
        }
        
    }

    /**
     * <li>说明：删除范围与车辆关系
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIdx 范围ID
     */
    public void deleteVehicleRelations(String zbfwIdx) {
        StringBuffer hql = new StringBuffer(" delete From ZbFwVehicleRelation where zbfwIdx = ? ");
        this.daoUtils.execute(hql.toString(), new Object[]{zbfwIdx});
    }
    
    
}
