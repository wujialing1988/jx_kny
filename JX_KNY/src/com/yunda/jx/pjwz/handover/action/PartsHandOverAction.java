package com.yunda.jx.pjwz.handover.action; 

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
import com.yunda.jx.pjwz.handover.entity.PartsHandOver;
import com.yunda.jx.pjwz.handover.manager.PartsHandOverManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsHandOver控制器, 配件交接
 * <li>创建人：程梅
 * <li>创建日期：2016年5月31日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsHandOverAction extends JXBaseAction<PartsHandOver, PartsHandOver, PartsHandOverManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
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
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            PartsHandOver objEntity = (PartsHandOver)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<PartsHandOver> searchEntity = new SearchEntity<PartsHandOver>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsHandOver> page = this.manager.findPageList(searchEntity);
            
            map = page.extjsResult();

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【不在库】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getPartsAccount() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String searchJson = StringUtil.nvlTrim( getRequest().getParameter("searchJson"), "{}" );
            PartsAccount account = (PartsAccount)JSONUtil.read(searchJson, PartsAccount.class);
            account = this.manager.getPartsAccount(account);
            map.put("account", account);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【责任部门为当前班组且状态为不在库】配件周转台账信息列表
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
     * <li>说明：配件交接登记【web端/手持终端批量交接】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsHandOverBatch() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String registerData = StringUtil.nvlTrim( getRequest().getParameter("registerDatas"), "[]" );
            PartsHandOver[] registers = (PartsHandOver[])JSONUtil.read(registerData, PartsHandOver[].class);
            this.manager.savePartsHandOverBatch(registers);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * <li>说明：撤销
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePartsHandOverForCancel() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updatePartsHandOverForCancel(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}