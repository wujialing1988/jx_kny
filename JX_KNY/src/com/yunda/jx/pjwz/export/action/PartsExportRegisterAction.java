package com.yunda.jx.pjwz.export.action;

import java.io.IOException;
import java.io.Serializable;
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
import com.yunda.jx.pjwz.export.entity.PartsExportRegister;
import com.yunda.jx.pjwz.export.manager.PartsExportRegisterManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsExportRegister控制器, 配件调出登记
 * <li>创建人：程梅
 * <li>创建日期：2015-10-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class PartsExportRegisterAction extends JXBaseAction<PartsExportRegister, PartsExportRegister, PartsExportRegisterManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
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
    public void findPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
            PartsExportRegister objEntity = (PartsExportRegister) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<PartsExportRegister> searchEntity = new SearchEntity<PartsExportRegister>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsExportRegister> page = this.manager.findPageList(searchEntity);
            
            map = page.extjsResult();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存配件调出登记信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @throws Exception
     */
    public void savePartsExport() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String registerData = StringUtil.nvlTrim(getRequest().getParameter("registerData"), Constants.EMPTY_JSON_OBJECT);
            PartsExportRegister register = (PartsExportRegister) JSONUtil.read(registerData, PartsExportRegister.class);
            if (StringUtil.isNullOrBlank(register.getIdx())) {
                this.manager.savePartsExport(register);
            } else {
                this.manager.updatePartsExport(register);
            }
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
     * <li>创建日期：2015-10-13
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @throws Exception
     */
    public void updatePartsExportForCancel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updatePartsExportForCancel(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：撤销【web端】
     * <li>创建人：王利成
     * <li>创建日期：2015-10-10
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @throws Exception
     */
    public void updatePartsExportCancelWeb() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (Serializable id : ids) {
                this.manager.updatePartsExportForCancel(id.toString());
            }
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
     * <li>说明：根据配件编号和规格型号查询最新的【在册】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @throws Exception
     */
    public void getPartsAccount() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.SUCCESS, false);
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
     * <li>说明：根据配件编号和规格型号查询【良好】配件周转台账信息列表
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
     * <li>说明:保存配件调出【web端】
     * <li>创建人：王利成
     * <li>创建日期：2015-11-10
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @throws Exception
     */
    public void savePartdExportRegister() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String formData = StringUtil.nvlTrim(getRequest().getParameter("formData"), Constants.EMPTY_JSON_OBJECT);
            PartsExportRegister exportParts = (PartsExportRegister) JSONUtil.read(formData, PartsExportRegister.class);
            PartsAccount[] detailList = (PartsAccount[]) JSONUtil.read(getRequest(), PartsAccount[].class);
            this.manager.savePartsExportRegister(exportParts, detailList);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：配件调出登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePartsExportRegisterForCheck() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updatePartsExportRegisterForCheck(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}
