package com.yunda.jx.pjwz.cs.action; 

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
import com.yunda.jx.pjwz.cs.entity.PartsCSIn;
import com.yunda.jx.pjwz.cs.manager.PartsCSInManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsCSIn控制器, 配件售后入段控制器
 * <li>创建人：程梅
 * <li>创建日期：2016年6月20日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsCSInAction extends JXBaseAction<PartsCSIn, PartsCSIn, PartsCSInManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：分页查询
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
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
            PartsCSIn objEntity = (PartsCSIn)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<PartsCSIn> searchEntity = new SearchEntity<PartsCSIn>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsCSIn> page = this.manager.findPageList(searchEntity);
            
            map = page.extjsResult();

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：配件售后入段登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsCSIn() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String registerData = StringUtil.nvlTrim( getRequest().getParameter("registerData"), "{}" );
            PartsCSIn register = (PartsCSIn)JSONUtil.read(registerData, PartsCSIn.class);
            this.manager.savePartsCSIn(register);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    
    /**
     * <li>说明：撤销【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePartsCSInForCancel() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updatePartsCSInForCancel(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【非在册】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getPartsAccount() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String searchJson = StringUtil.nvlTrim(getRequest().getParameter("searchJson"), Constants.EMPTY_JSON_OBJECT);
            PartsAccount account = (PartsAccount) JSONUtil.read(searchJson, PartsAccount.class);
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