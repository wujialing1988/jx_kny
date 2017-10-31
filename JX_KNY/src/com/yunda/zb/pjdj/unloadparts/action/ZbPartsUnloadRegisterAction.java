package com.yunda.zb.pjdj.unloadparts.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.zb.pjdj.unloadparts.entity.ZbPartsUnloadRegister;
import com.yunda.zb.pjdj.unloadparts.manage.ZbPartsUnloadRegisterManager;
import com.yunda.zb.zbglrdpmanage.entity.VZbglRdp;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbPartsUnloadRegister控制器, 配件下车登记单
 * <li>创建人：黄杨
 * <li>创建日期：2016-09-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbPartsUnloadRegisterAction extends JXBaseAction<ZbPartsUnloadRegister, ZbPartsUnloadRegister, ZbPartsUnloadRegisterManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：保存下车配件信息（范围内下车为更新数据，范围外下车为新增数据）【用于手持终端】
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsUnloadRegister() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String registerData = StringUtil.nvlTrim(getRequest().getParameter("registerData"), Constants.EMPTY_JSON_OBJECT);
            ZbPartsUnloadRegister register = (ZbPartsUnloadRegister) JSONUtil.read(registerData, ZbPartsUnloadRegister.class);
            // id不为空并且状态为已登帐，则为修改，否则都是新登记
            this.manager.savePartsUnloadRegister(register);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：撤销【用于手持终端】
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateUnloadRegisterForCancel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updateUnloadRegisterForCancel(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据所选机车查询其下的配件登记情况
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void registerDetailsUnderOne() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	String entity = getRequest().getParameter("entity");
        	VZbglRdp zbglRdp = JSONUtil.read(entity, VZbglRdp.class);
        	List<ZbPartsUnloadRegister> entityList = this.manager.registerDetailsUnderOne(zbglRdp);
        	map.put("entityList", entityList);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【非在册】配件周转台账信息
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getPartsAccount() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String searchJson = StringUtil.nvlTrim( getRequest().getParameter("searchJson"), "{}" );
            PartsAccount account = (PartsAccount)JSONUtil.read(searchJson, PartsAccount.class);
            account = this.manager.getPartsAccount(account);
            map.put("account", account) ;
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【非在册】配件周转台账信息列表
     * <li>创建人：程梅
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getPartsAccountList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String searchJson = StringUtil.nvlTrim( getRequest().getParameter("searchJson"), "{}" );
            PartsAccount account = (PartsAccount)JSONUtil.read(searchJson, PartsAccount.class);
            List<PartsAccount> accountList = this.manager.getPartsAccountList(account);
            map.put("accountList", accountList) ;
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    
}
