package com.yunda.freight.base.classTransfer.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.freight.base.classTransfer.entity.ClassTransfer;
import com.yunda.freight.base.classTransfer.entity.ClassTransferDetails;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 班次交接业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-18 11:35:19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("classTransferManager")
public class ClassTransferManager extends JXBaseManager<ClassTransfer, ClassTransfer> {
    
    /**
     * 交接项详情业务类
     */
    @Resource
    private ClassTransferDetailsManager classTransferDetailsManager ;

    /**
     * <li>说明：保存方法
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param classTransfer
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveClassTransfer(ClassTransfer classTransfer) throws BusinessException, NoSuchFieldException {
        // TODO Auto-generated method stub
        this.saveOrUpdate(classTransfer);
        List<ClassTransferDetails> details = classTransfer.getDetails();
        for (ClassTransferDetails detail : details) {
            detail.setTransferIdx(classTransfer.getIdx());
        }
        classTransferDetailsManager.deteleDetailsByByTransfer(classTransfer.getIdx());
        classTransferDetailsManager.saveOrUpdate(details);
    }
    
   
}
