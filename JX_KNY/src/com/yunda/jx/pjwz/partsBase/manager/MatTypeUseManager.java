package com.yunda.jx.pjwz.partsBase.manager;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeUse;

@Service(value="matTypeUseManager")
public class MatTypeUseManager extends JXBaseManager<MatTypeUse, MatTypeUse>{

    /**
     * <li>说明：保存故障登记下的消耗信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param matUseArray 物料消耗信息
     * @param gztpIdx   故障登记ID
     */
    public void saveMatUsesForGZTP(MatTypeUse[] matUseArray, String gztpIdx) {
        // 先删除该登记下的物料消耗数据
        this.deleteMatUsesForGZTP(gztpIdx);
        // 保存登记下的物料消耗 
        for (MatTypeUse use : matUseArray) {
            use.setIdx(null);
            use.setGztpIdx(gztpIdx);
            this.save(use);
        }
    }
    
    /**
     * <li>说明：删除故障登记下的物料消耗信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param gztpIdx
     */
    public void deleteMatUsesForGZTP(String gztpIdx){
        String delHql = " delete From MatTypeUse where gztpIdx = ? " ;
        this.daoUtils.execute(delHql, new Object[]{gztpIdx});
    }
   
    
}