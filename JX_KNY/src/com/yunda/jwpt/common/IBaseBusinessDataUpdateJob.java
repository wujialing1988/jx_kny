package com.yunda.jwpt.common;

import java.util.List;

import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明： 业务更新接口规范
 * <li>创建人：林欢
 * <li>创建日期：2016-05-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息事业部
 * @version 1.0
 */
public interface IBaseBusinessDataUpdateJob {
    
    /** 新增字符串 insert 9*/
    public final static int INSERT = 9;
    /** 更新字符串 update 5*/
    public final static int UPDATE = 5;
    /** 删除字符串 delete 3*/
    public final static int DELETE = 3;
    
    /** 状态值 0 recordStatus */
    public final static int RECORD_STATUS_NOT_DELETE = 0;
    
    //==============================业务manager字符串设置开始============================================
    
    /** trainAccessAccountManager 字符串  */
    public final static String TRAINACCESS_ACCOUNT_MANAGER = "trainAccessAccountManager";
    
    /** trainAccessAccountManager 字符串  */
    public final static String ZBGL_RDP_WI_DI_MANAGER = "zbglRdpWidiManager";
    
    /** zbglRdpManager 字符串  */
    public final static String ZBGL_RDP_MANAGER = "zbglRdpManager";
    
    /** trainAccessAccountManager 字符串  */
    public final static String ZBGL_TP_MANAGER = "zbglTpManager";
    
    /** zbglRdpWiManager 字符串  */
    public final static String ZBGL_RDP_WI_MANAGER = "zbglRdpWiManager";
    
    /** oilconSumptionManager 字符串  */
    public final static String OIL_CONSUMPTION_MANAGER = "oilconSumptionManager";
    
    
    //==============================业务manager字符串设置结束============================================
    
    //==============================官方中间表manager字符串设置开始============================================
    
    /** tZbglZbdzdtManager 字符串  */
    public final static String T_ZBGL_JT6_MANAGER = "tZbglJt6Manager";
    
    /** tZbglLxrwdManager 字符串  */
    public final static String T_ZBGL_LXRWD_MANAGER = "tZbglLxrwdManager";
    
    /** tZbglZbdzdtManager 字符串  */
    public final static String T_ZBGL_ZBDZHGZ_MANAGER = "tZbglZbdzhgzManager";

    /** tZbglZbdzdtManager 字符串  */
    public final static String T_ZBGL_ZBRWD_JCJG_MANAGER = "tZbglZbrwdJcjgManager";
    
    /** tZbglZbdzdtManager 字符串  */
    public final static String T_ZBGL_ZBDZDT_MANAGER = "tZbglZbdzdtManager";
    
    /** tZbglZbrwdManager 字符串  */
    public final static String T_ZBGL_ZBRWD_MANAGER = "tZbglZbrwdManager";
    
    /** tZbglJyxhtzManager 字符串  */
    public final static String T_ZBGL_JYXHTZ_MANAGER = "tZbglJyxhtzManager";
    
    /** tZbglPjghManager 字符串  */
    public final static String T_ZBGL_PJGH_MANAGER = "tZbglPjghManager";
    
    //==============================官方中间表manager字符串设置结束============================================
    
    /**
     * <li>方法说明：更新对应业务的数据
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * <LI>重要业务逻辑:
     * 当官方中间表中，当前数据为9=insert:【1】当系统中间表为5=update的时候，依旧是新增，数据为更新的数据
     *                               【2】当系统中间表为3=delete的时候，删除官方中间表当前数据
     *                      5=update:【1】当系统中间表为3=delete的时候，修改状态为删除状态（3=删除）
     * 
     * @param dsctList 传递对应业务表的系统中间表数据
     * @return 返回系统中间表数据对象，方便作统一删除操作
     * @throws NoSuchFieldException 
     */
    public List<JwptDataSynchronizationCenterTable> updateDataByBusiness(List<JwptDataSynchronizationCenterTable> dsctList);
}
