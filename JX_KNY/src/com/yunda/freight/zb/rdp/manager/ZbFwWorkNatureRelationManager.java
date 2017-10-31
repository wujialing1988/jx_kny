package com.yunda.freight.zb.rdp.manager;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.freight.zb.rdp.entity.ZbFwWorkNatureRelation;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检范围适用作业性质
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbFwWorkNatureRelationManager")
public class ZbFwWorkNatureRelationManager extends JXBaseManager<ZbFwWorkNatureRelation, ZbFwWorkNatureRelation> {

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIdx 范围ID
     * @param workNatureCodeArray 作业性质Code集合
     * @param getWorkNatureArray 作业性质名称集合
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveWorkNatureRelations(String zbfwIdx, String[] workNatureCodeArray, String[] workNatureArray) throws BusinessException, NoSuchFieldException {
        // 删除范围与作业性质关系
        deleteWorkNatureRelations(zbfwIdx);
        // 保存范围与作业性质关系
        for (int i = 0; i < workNatureCodeArray.length; i++) {
            String workNatureCode = workNatureCodeArray[i];
            String workNature = workNatureArray[i];
            ZbFwWorkNatureRelation relation = new ZbFwWorkNatureRelation();
            relation.setZbfwIdx(zbfwIdx);
            relation.setWorkNatureCode(workNatureCode);
            relation.setWorkNature(workNature);
            this.saveOrUpdate(relation);
        }
    }
    
    /**
     * <li>说明：删除范围与作业性质关系
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIdx 范围ID
     */
    public void deleteWorkNatureRelations(String zbfwIdx) {
        StringBuffer hql = new StringBuffer(" delete From ZbFwWorkNatureRelation where zbfwIdx = ? ");
        this.daoUtils.execute(hql.toString(), new Object[]{zbfwIdx});
    }

    
}
