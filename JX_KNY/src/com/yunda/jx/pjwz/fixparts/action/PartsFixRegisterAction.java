package com.yunda.jx.pjwz.fixparts.action;

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
import com.yunda.jx.pjwz.fixparts.entity.PartsFixRegister;
import com.yunda.jx.pjwz.fixparts.manager.PartsFixRegisterManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsFixRegister控制器, 上车配件登记单
 * <li>创建人：程梅
 * <li>创建日期：2015-10-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class PartsFixRegisterAction extends JXBaseAction<PartsFixRegister, PartsFixRegister, PartsFixRegisterManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：根据过滤条件查询机车兑现单列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-3
     * <li>修改人：何涛
     * <li>修改日期：2016-04-08
     * <li>修改内容：使用Constants.ENTITY_JSON，Constants.EMPTY_JSON_OBJECT替换硬编码
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findWorkPlanList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
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
     * <li>说明：登帐【web端】
     * <li>创建人：程梅
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveFixRegisterBatch() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String registerData = StringUtil.nvlTrim(getRequest().getParameter("registerDatas"), "[]");
            PartsFixRegister[] registers = (PartsFixRegister[]) JSONUtil.read(registerData, PartsFixRegister[].class);
            this.manager.saveFixRegisterBatch(registers);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存上车配件信息（范围内上车为更新数据，范围外上车为新增数据）【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsFixRegister() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String registerData = StringUtil.nvlTrim(getRequest().getParameter("registerData"), Constants.EMPTY_JSON_OBJECT);
            PartsFixRegister register = (PartsFixRegister) JSONUtil.read(registerData, PartsFixRegister.class);
            // id不为空并且状态为已登帐，则为修改，否则都是新登记
            // if(!StringUtil.isNullOrBlank(register.getIdx()) && PjwzConstants.STATUS_ED.equals(register.getStatus())) errMsg =
            // this.manager.updatePartsFixRegister(register);
            this.manager.savePartsFixRegisterNew(register);
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
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateFixRegisterForCancel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updateFixRegisterForCancel(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【良好不在库】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人：何涛
     * <li>修改日期：2016-04-08
     * <li>修改内容：审查规范：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
     * <li>修改内容：使用Constants.EMPTY_JSON_OBJECT替换硬编码
     * @throws Exception
     */
    public void getPartsAccount() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.SUCCESS, false);
        try {
            String searchJson = StringUtil.nvlTrim(getRequest().getParameter("searchJson"), Constants.EMPTY_JSON_OBJECT);
            PartsAccount account = (PartsAccount) JSONUtil.read(searchJson, PartsAccount.class);
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
     * <li>说明：根据配件编号和规格型号查询【良好不在库】配件周转台账信息列表
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
     * <li>说明：配件上车登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateFixRegisterForCheck() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updateFixRegisterForCheck(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询【上车配件登记】下车位置
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
            PartsFixRegister objEntity = (PartsFixRegister) JSONUtil.read(searchJson, PartsFixRegister.class);
            SearchEntity<PartsFixRegister> searchEntity = new SearchEntity<PartsFixRegister>(objEntity, getStart(), getLimit(), getOrders());
            Page<EquipPart> page = this.manager.findEquipPartList(searchEntity);
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询登记情况(ipad)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findFixRegisterPartsList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String workPlanId = req.getParameter("workPlanId");// 计划ID
            String jcpjmc = req.getParameter("jcpjmc");// 大部件名称
            map = this.manager.findFixRegisterPartsList(getStart(),getLimit(),workPlanId,jcpjmc).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：联合查询大部件与配件相关信息(上车配件登录（新）【web端】)
     * <li>创建人：张迪
     * <li>创建日期：2016-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findPartsAboardRegisterAll() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String workPlanId = req.getParameter("workPlanId");// 计划ID
            map = this.manager.findPartsAboardRegisterAll(getStart(),1000,workPlanId).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    
    /**
     * <li>说明：查询未登记的上车配件列表
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
            map = this.manager.findPartsNotRegisterAll(getStart(),1000,workPlanId).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
   
    /**
     * <li>说明：保存上车配件登录【web端】（新）
     * <li>创建人：张迪
     * <li>创建日期：2016-11-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveFixRegisterNew() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
//            String registerData = StringUtil.nvlTrim(getRequest(), "[]");
            PartsFixRegister[] registers = (PartsFixRegister[]) JSONUtil.read(getRequest(), PartsFixRegister[].class);
            this.manager.saveFixRegisterNew(registers);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
