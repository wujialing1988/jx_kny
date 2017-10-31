
package com.yunda.jwpt.utils;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunda.Application;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jwpt.common.BaseBusinessDataUpdateJob;
import com.yunda.util.BeanUtils;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 业务更新接口基类实现
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
public enum BaseBusinessDataUpdateJobUtils {
    
    instence;
    
    /**
     * <li>方法说明：获取Bean 
     * <li>@param bean spring bean的名称例如:buildUpToPlaceManager
     * <li>@return 通过spring bean名称，获取的对应的对象
     * <li>return: Object
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 下午04:48:42
     * <li>修改人：
     * <li>修改内容：
     */
    public Object getBean(String bean) {
        return Application.getSpringApplicationContext().getBean(bean);
    }
    
    /**
     * <li>方法说明：更新公共字段信息(创建人，创建时间，修改人，修改时间)
     * <li>创建人：林欢
     * <li>创建时间：2016-5-17
     * <li>修改人：
     * <li>修改内容：
     * @param t 官方中间表对象
     * @throws NoSuchFieldException 
     */
    public void doSetCommonFiledValue(Object t) throws NoSuchFieldException {
        
        //设置创建人
        if(EntityUtil.contains(t.getClass(), EntityUtil.CREATOR)){
            BeanUtils.forceSetProperty(t, EntityUtil.CREATOR, 0);
        }
        //设置创建时间
        if(EntityUtil.contains(t.getClass(), EntityUtil.CREATE_TIME)){
            BeanUtils.forceSetProperty(t, EntityUtil.CREATE_TIME, new Date());
        }
        //设置修改人
        if(EntityUtil.contains(t.getClass(), EntityUtil.UPDATOR)){
            BeanUtils.forceSetProperty(t, EntityUtil.UPDATOR, 0);
        }
        //设置修改时间
        if(EntityUtil.contains(t.getClass(), EntityUtil.UPDATE_TIME)){
            BeanUtils.forceSetProperty(t, EntityUtil.UPDATE_TIME, new Date());
        }
    }
    
    /**
     * <li>方法说明：更新公共字段信息(状态，操作)
     * <li>创建人：林欢
     * <li>创建时间：2016-5-17
     * <li>修改人：
     * <li>修改内容：
     * @param t 官方中间表对象
     * @param newRecordValue 新的record值
     * @param newOpeartValue 新的opeart值
     * @throws NoSuchFieldException 
     */
    public void doSetCommonFiledValue2(Object t,Object newRecordValue,Object newOpeartValue) throws NoSuchFieldException {
        
        if (newRecordValue != null) {
//          设置状态
            if(EntityUtil.contains(t.getClass(), BaseBusinessDataUpdateJob.RECORD_STATUS)){
                BeanUtils.forceSetProperty(t, BaseBusinessDataUpdateJob.RECORD_STATUS, newRecordValue);
            }
        }
        
        if (newOpeartValue != null) {
//          设置操作
            if(EntityUtil.contains(t.getClass(), BaseBusinessDataUpdateJob.OPERATE_TYPE)){
                BeanUtils.forceSetProperty(t, BaseBusinessDataUpdateJob.OPERATE_TYPE, newOpeartValue);
            }
        }
    }
    
    /**
     * <li>方法说明：判断字符串非空操作
     * <li>创建人：林欢
     * <li>创建时间：2016-5-25
     * <li>修改人：
     * <li>修改内容：
     * @param oldValue 旧的record值
     * @param newValue 新的opeart值
     * @return 返回的正确值
     * @throws NoSuchFieldException 
     */
    public String checkStringIsNull(String oldValue,String newValue){
        return oldValue == null ? newValue : oldValue;
    }
    
    /**
     * <li>方法说明：判断浮点型非空操作
     * <li>创建人：林欢
     * <li>创建时间：2016-5-25
     * <li>修改人：
     * <li>修改内容：
     * @param oldValue 旧的record值
     * @param newValue 新的opeart值
     * @return 返回的正确值
     * @throws NoSuchFieldException 
     */
    public Double checkDoubleIsNull(Double oldValue,Double newValue){
        return oldValue == null ? newValue : oldValue;
    }
    
    /**
     * <li>方法说明：判断时间非空操作
     * <li>创建人：林欢
     * <li>创建时间：2016-5-25
     * <li>修改人：
     * <li>修改内容：
     * @param oldValue 旧的record值
     * @param newValue 新的opeart值
     * @return 返回的正确值
     * @throws NoSuchFieldException 
     */
    public Date checkDateIsNull(Date oldValue,Date newValue){
        return oldValue == null ? newValue : oldValue;
    }
    
    /**
     * <li>方法说明：判断长整形非空操作
     * <li>创建人：林欢
     * <li>创建时间：2016-5-25
     * <li>修改人：
     * <li>修改内容：
     * @param oldValue 旧的record值
     * @param newValue 新的opeart值
     * @return 返回的正确值
     * @throws NoSuchFieldException 
     */
    public Long checkLongIsNull(Long oldValue,Long newValue){
        return oldValue == null ? newValue : oldValue;
    }
    
    /**
     * <li>方法说明：判断整形非空操作
     * <li>创建人：林欢
     * <li>创建时间：2016-5-25
     * <li>修改人：
     * <li>修改内容：
     * @param oldValue 旧的record值
     * @param newValue 新的opeart值
     * @return 返回的正确值
     * @throws NoSuchFieldException 
     */
    public Integer checkIntegerIsNull(Integer oldValue,Integer newValue){
        return oldValue == null ? newValue : oldValue;
    }
    
    /**
     * <li>方法说明：通过段编码，查寻段名称
     * <li>创建人：林欢
     * <li>创建时间：2016-5-31
     * <li>修改人：
     * <li>修改内容：
     * @param overseaOrgcode 段编码
     * @return 返回的正确值
     * @throws NoSuchFieldException 
     */
    public String getOverseaOrgNameByCode(String overseaOrgcode){
        OmOrganizationManager omOrganizationManager = (OmOrganizationManager) getBean("omOrganizationManager");
        OmOrganization oo = omOrganizationManager.findOrgForCode(overseaOrgcode);
        return oo.getOrgname();
    }
    
}
