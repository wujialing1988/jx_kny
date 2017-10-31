package com.yunda.jx.pjwz.wellpartsmanage.repairedpartswh.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.wellpartsmanage.repairedpartswh.entity.PartsWHRegister;
import com.yunda.jx.pjwz.wellpartsmanage.repairedpartswh.manager.PartsWHRegisterManager;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsWHRegister控制器，修竣配件入库单
 * <li>创建人：程梅
 * <li>创建日期：2015-10-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PartsWHRegisterAction extends JXBaseAction<PartsWHRegister, PartsWHRegister, PartsWHRegisterManager> {

	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findPageList () throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT );
            PartsWHRegister objEntity = (PartsWHRegister)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<PartsWHRegister> searchEntity = new SearchEntity<PartsWHRegister>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsWHRegister> page = this.manager.findPageList(searchEntity);
            
            map = page.extjsResult();

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：修竣配件入库登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsWHRegister() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String registerJson = StringUtil.nvlTrim( req.getParameter("registerData"), Constants.EMPTY_JSON_OBJECT );
            PartsWHRegister register = (PartsWHRegister)JSONUtil.read(registerJson, PartsWHRegister.class);
            if(StringUtil.isNullOrBlank(register.getIdx()))  this.manager.savePartsWHRegister(register);
            else this.manager.updatePartsWHRegister(register);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：撤销修竣配件入库登记
     * <li>创建人：程梅
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateWHRegisterForCancel() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updateWHRegisterForCancel(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
     * <li>说明：根据配件编号和规格型号查询最新的【除在库以外的在册】配件周转台账信息（除在库和非在册外）
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getPartsAccount() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String searchJson = StringUtil.nvlTrim( getRequest().getParameter("searchJson"), Constants.EMPTY_JSON_OBJECT );
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
     * <li>说明：根据配件编号和规格型号查询【除在库、在修以外的在册】配件周转台账信息列表
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
    /**
     * <li>说明：修竣配件入库登记【web端/手持终端批量入库】
     * <li>创建人：程梅
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsWHRegisterBatch() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String registerData = StringUtil.nvlTrim( getRequest().getParameter("registerDatas"), "[]" );
            PartsWHRegister[] registers = (PartsWHRegister[])JSONUtil.read(registerData, PartsWHRegister[].class);
            this.manager.savePartsWHRegisterBatch(registers);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：修竣配件入库登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePartsWHRegisterForCheck() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updatePartsWHRegisterForCheck(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}
