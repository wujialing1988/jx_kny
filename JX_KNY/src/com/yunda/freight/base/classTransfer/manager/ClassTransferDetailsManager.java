package com.yunda.freight.base.classTransfer.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.freight.base.classTransfer.entity.ClassTransferDetails;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 交接明细业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-18 11:38:53
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("classTransferDetailsManager")
public class ClassTransferDetailsManager extends JXBaseManager<ClassTransferDetails, ClassTransferDetails> {
    
    /**
     * <li>说明：通过交接查询其交接项
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param transferIdx 班次交接主键
     * @return
     */
    public List<ClassTransferDetails> findClassTransferDetailsByTransfer(String transferIdx){
        StringBuffer hql = new StringBuffer(" From ClassTransferDetails where transferIdx = ? ");
        return this.daoUtils.find(hql.toString(), new Object[]{transferIdx});
    }
    
    /**
     * <li>说明：通过交接删除交接项
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param transferIdx
     */
    public void deteleDetailsByByTransfer(String transferIdx){
        StringBuffer hql = new StringBuffer(" delete From ClassTransferDetails where transferIdx = ? ");
        this.daoUtils.execute(hql.toString(), new Object[]{transferIdx});
    }
   
}
