
package com.yunda.jx.pjwz.partsmanage.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccountRelation;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsAccountRelation业务类，配件信息关系
 * <li>创建人：何涛
 * <li>创建日期：2015-3-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "partsAccountRelationManager")
public class PartsAccountRelationManager extends JXBaseManager<PartsAccountRelation, PartsAccountRelation> {
    

    @Resource
    /** PartsAccount业务类，配件周转台账---配件信息 */
    private PartsAccountManager partsAccountManager;
    
    /**
     * <li>说明：查询配件上配件的上下级关系，一般情况下，一个配件只能唯一上到另一个配件上
     * <li>创建人：何涛
     * <li>创建日期：2015-3-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsAccountIDX 配件信息主键
     * @return PartsAccountRelation 配件信息关系实体
     */
    private PartsAccountRelation getModelByPartsAccountIDX(String partsAccountIDX) {
        String hql = "From PartsAccountRelation Where recordStatus = 0 And partsAccountIDX = ?";
        return (PartsAccountRelation) this.daoUtils.findSingle(hql, new Object[]{partsAccountIDX});
    }
    
    /**
     * <li>说明：查询下级配件关系
     * <li>创建人：何涛
     * <li>创建日期：2015-3-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentPartsAccountIDX 上级配件信息主键
     * @return List 配件信息关系列表
     */ 
    private List getModelsByParentPartsAccountIDX(String parentPartsAccountIDX) {
        String hql = "From PartsAccountRelation Where recordStatus = 0 And parentPartsAccountIDX = ?";
        return this.daoUtils.find(hql, new Object[]{parentPartsAccountIDX});
    }

    /**
     * <li>说明：更新配件上配件的上下级关系
     * <li>创建人：何涛
     * <li>创建日期：2015-3-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsAccountIDX 配件信息主键
     * @param parentPartsAccountIDX 上级配件信息主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsAccountRelation(String partsAccountIDX, String parentPartsAccountIDX) throws BusinessException, NoSuchFieldException {
        PartsAccountRelation relation = this.getModelByPartsAccountIDX(partsAccountIDX);
        if (null == relation) {
            relation = new PartsAccountRelation(partsAccountIDX, parentPartsAccountIDX);
        } else if (!parentPartsAccountIDX.equals(relation.getParentPartsAccountIDX())) {
            relation.setParentPartsAccountIDX(parentPartsAccountIDX);
        }
        this.saveOrUpdate(relation);
    }

    /**
     * <li>说明：更新该配件可能存在的子配件的状态为已上车 and 更新子配件的上车记录为父配件的上车记录
     * <li>创建人：何涛
     * <li>创建日期：2015-3-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsAccountIDX 配件信息主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void updateAboardStatus(String partsAccountIDX) throws BusinessException, NoSuchFieldException {
        List<PartsAccount> entityList = new ArrayList<PartsAccount>();
        PartsAccount parentAccount = partsAccountManager.getModelById(partsAccountIDX);
        this.findAllPartsAccountRelation(partsAccountIDX, entityList , parentAccount);
        if (0 >= entityList.size()) {
            return;
        }
        this.partsAccountManager.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：递归获取父配件下属的所有子配件（含父配件）
     * <li>创建人：程梅
     * <li>创建日期：2015-7-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsAccountIDX 配件周转台账idx主键
     * @param entityList 父配件下属的所有子配件（含父配件）集合
     */
    @SuppressWarnings("unchecked")
    private void findAllPartsAccountRelation(String partsAccountIDX, List<PartsAccount> entityList ,PartsAccount parentAccount) {
        List<PartsAccountRelation> list = this.getModelsByParentPartsAccountIDX(partsAccountIDX);
        if (null == list || list.size() <= 0) {
            return;
        }
        for (PartsAccountRelation par : list) {
            PartsAccount account = partsAccountManager.getModelById(par.getPartsAccountIDX());
            account.setPartsStatus(PartsAccount.PARTS_STATUS_YSC);                          // 配件状态为已上车
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_YSC, "已上车"));
            //更新子配件的上车记录为父配件的上车记录
            account.setAboardTrainTypeIdx(parentAccount.getAboardTrainTypeIdx());
            account.setAboardTrainType(parentAccount.getAboardTrainType());
            account.setAboardTrainNo(parentAccount.getAboardTrainNo());
            account.setAboardRepairTimeIdx(parentAccount.getAboardRepairTimeIdx());
            account.setAboardRepairTime(parentAccount.getAboardRepairTime());
            account.setAboardRepairClassIdx(parentAccount.getAboardRepairClassIdx());
            account.setAboardRepairClass(parentAccount.getAboardRepairClass());
            account.setAboardPlace(parentAccount.getAboardPlace());
            account.setAboardDate(parentAccount.getAboardDate());
            entityList.add(account);
            
            this.findAllPartsAccountRelation(par.getPartsAccountIDX(), entityList, parentAccount);
        }
    }
    
}
