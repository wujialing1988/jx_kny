package com.yunda.jx.pjwz.partsmanage.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.RecordCodeBind;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RecordCodeBind业务类,配件检修记录单识别码绑定
 * <li>创建人：程梅
 * <li>创建日期：2016-01-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "recordCodeBindManager")
public class RecordCodeBindManager extends JXBaseManager<RecordCodeBind, RecordCodeBind> {
    
    /** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /**
     * <li>说明：根据识别码查询配件周转信息
     * <li>创建人：程梅
     * <li>创建日期：2016-1-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param identificationCode 配件识别码
     * @return PartsAccount 配件周转信息
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getPartsAccount(String identificationCode) {
        PartsAccount account = new PartsAccount();
        account.setIdentificationCode(identificationCode) ;
        account.setPartsStatus(PartsAccount.PARTS_STATUS_ZC) ; //查询【在册】状态的周转信息
        PartsAccount accountV = this.partsAccountManager.getAccount(account);
        return accountV;
    }
    
    /**
     * <li>说明：根据配件信息主键查询配件检修记录单识别码绑定列表信息
     * <li>创建人：程梅
     * <li>创建日期：2016-1-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsAccountIdx 配件信息主键
     * @return List<RecordCodeBind> 绑定列表
     */
    @SuppressWarnings("unchecked")
    public List<RecordCodeBind> getBindListByPartsAccountIdx(String partsAccountIdx){
        String hql = "From RecordCodeBind Where partsAccountIdx = ? order by recordCode ";
        return this.daoUtils.find(hql, new Object[]{partsAccountIdx});
    }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：绑定【绑定前判断识别码是否被使用】
     * <li>创建人：程梅
     * <li>创建日期：2016-1-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param bind 绑定信息
     * @return String[] 错误提示
     */
    @SuppressWarnings("unchecked")
    public String[] saveRecordCodeBind (RecordCodeBind bind){  
        List<String> errMsg = new ArrayList<String>();
        if(null != bind){
            List<RecordCodeBind> bindList = this.daoUtils.find("From RecordCodeBind where recordCode=?", new Object[]{bind.getRecordCode()});
            if(null != bindList && bindList.size() > 0) errMsg.add("此识别码已绑定过，请重新录入！") ;
            List<PartsAccount> accountList = this.daoUtils.find("From PartsAccount where identificationCode=? and recordStatus=0", new Object[]{bind.getRecordCode()});
            if(null != accountList && accountList.size() > 0) errMsg.add("此识别码已作为配件识别码登记过，请重新录入！") ;
        }else{
            errMsg.add("参数不全！") ;
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }else this.daoUtils.getHibernateTemplate().saveOrUpdate(bind);
        return null ;
        }
    
}
