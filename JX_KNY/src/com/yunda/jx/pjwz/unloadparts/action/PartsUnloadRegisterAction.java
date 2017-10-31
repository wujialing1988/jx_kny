package com.yunda.jx.pjwz.unloadparts.action;

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
import com.yunda.jx.component.entity.EquipPart;
import com.yunda.jx.pjjx.workplan.entity.TrainWorkPlanView;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegister;
import com.yunda.jx.pjwz.unloadparts.manager.PartsUnloadRegisterManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsUnloadRegister控制器, 下车配件登记单
 * <li>创建人：程梅
 * <li>创建日期：2015-04-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class PartsUnloadRegisterAction extends JXBaseAction<PartsUnloadRegister, PartsUnloadRegister, PartsUnloadRegisterManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：根据过滤条件查询机车兑现单列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findWorkPlanList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
            TrainWorkPlanView objEntity = (TrainWorkPlanView) JSONUtil.read(searchJson, TrainWorkPlanView.class);
            SearchEntity<TrainWorkPlanView> searchEntity = new SearchEntity<TrainWorkPlanView>(objEntity, getStart(), getLimit(), getOrders());
            Page<TrainWorkPlanView> page = this.manager.findWorkPlanList(searchEntity);
            
            map = page.extjsResult();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：登帐
     * <li>创建人：程梅
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveUnloadRegister() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String regData = StringUtil.nvlTrim(getRequest().getParameter("regData"), Constants.EMPTY_JSON_OBJECT);
            PartsUnloadRegister register = (PartsUnloadRegister) JSONUtil.read(regData, PartsUnloadRegister.class);
            PartsUnloadRegister[] registerArray = (PartsUnloadRegister[]) JSONUtil.read(getRequest(), PartsUnloadRegister[].class);
            this.manager.saveUnloadRegister(register, registerArray);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：登帐（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveUnloadRegisterNew() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String regData = StringUtil.nvlTrim(getRequest().getParameter("regData"), Constants.EMPTY_JSON_OBJECT);
            PartsUnloadRegister register = (PartsUnloadRegister) JSONUtil.read(regData, PartsUnloadRegister.class);
            PartsUnloadRegister[] registerArray = (PartsUnloadRegister[]) JSONUtil.read(getRequest(), PartsUnloadRegister[].class);
            this.manager.saveUnloadRegisterNew(register, registerArray);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    
    
    /**
     * <li>说明：保存下车配件信息（范围内下车为更新数据，范围外下车为新增数据）【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsUnloadRegister() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String regData = StringUtil.nvlTrim(getRequest().getParameter("registerData"), Constants.EMPTY_JSON_OBJECT);
            PartsUnloadRegister register = (PartsUnloadRegister) JSONUtil.read(regData, PartsUnloadRegister.class);
            if(register != null){
                PartsUnloadRegister[] registerArray = {register};
                this.manager.saveUnloadRegisterNew(register, registerArray);
            }
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
     * <li>创建日期：2015-10-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateUnloadRegisterForCancel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if(!StringUtil.isNullOrBlank(id)){
                String[] ids = {id}; 
                this.manager.updateUnloadRegisterForCancelNew(ids);
                map.put(Constants.SUCCESS, true);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：撤销（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateUnloadRegisterForCancelNew() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updateUnloadRegisterForCancelNew(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
     * <li>说明：根据配件编号和规格型号查询最新的【非在册】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
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
            String jcpjbm = StringUtil.nvlTrim( getRequest().getParameter("jcpjbm"), "" );
            PartsAccount account = (PartsAccount)JSONUtil.read(searchJson, PartsAccount.class);
            List<PartsAccount> accountList = this.manager.getPartsAccountList(account,jcpjbm);
            map.put("accountList", accountList) ;
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    /**
     * <li>说明：下车配件登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePartsUnloadRegisterForCheck() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updatePartsUnloadRegisterForCheck(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：根据过滤条件查询机车兑现单列表【web端下车登记】
     * <li>创建人：程梅
     * <li>创建日期：2015-11-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findWorkPlanListWeb() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
            TrainWorkPlanView objEntity = (TrainWorkPlanView) JSONUtil.read(searchJson, TrainWorkPlanView.class);
            SearchEntity<TrainWorkPlanView> searchEntity = new SearchEntity<TrainWorkPlanView>(objEntity, getStart(), getLimit(), getOrders());
            Page<TrainWorkPlanView> page = this.manager.findWorkPlanListWeb(searchEntity);
            
            map = page.extjsResult();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：查询【下车配件登记】下车位置
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findEquipPartList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
            PartsUnloadRegister objEntity = (PartsUnloadRegister) JSONUtil.read(searchJson, PartsUnloadRegister.class);
            SearchEntity<PartsUnloadRegister> searchEntity = new SearchEntity<PartsUnloadRegister>(objEntity, getStart(), getLimit(), getOrders());
            Page<EquipPart> page = this.manager.findEquipPartList(searchEntity);
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询未登记列表集合
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findUnRegisterPartsList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String workPlanId = req.getParameter("workPlanId");// 计划ID
            String jcpjmc = req.getParameter("jcpjmc");// 计划ID
            map = this.manager.findUnRegisterPartsList(getStart(),getLimit(),workPlanId,jcpjmc).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    
    /**
     * <li>说明：联合查询大部件与配件相关信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findPartsUnloadRegisterAll() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String workPlanId = req.getParameter("workPlanId");// 计划ID
            map = this.manager.findPartsUnloadRegisterAll(getStart(),getLimit(),workPlanId).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    
    /**
     * <li>说明：查询未登记的大部件列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findPartsNotRegisterAll() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String workPlanId = req.getParameter("workPlanId");// 计划ID
            map = this.manager.findPartsNotRegisterAll(getStart(),getLimit(),workPlanId).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
   
}
