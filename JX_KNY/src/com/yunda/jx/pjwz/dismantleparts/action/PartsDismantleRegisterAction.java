package com.yunda.jx.pjwz.dismantleparts.action;

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
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjwz.dismantleparts.entity.PartsDismantleRegister;
import com.yunda.jx.pjwz.dismantleparts.manager.PartsDismantleRegisterManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsDismantleRegister控制器, 配件拆卸登记
 * <li>创建人：程梅
 * <li>创建日期：2016-01-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class PartsDismantleRegisterAction extends JXBaseAction<PartsDismantleRegister, PartsDismantleRegister, PartsDismantleRegisterManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：根据过滤条件查询配件兑现单列表
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findPartsRdpList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
            PartsRdp objEntity = (PartsRdp) JSONUtil.read(searchJson, PartsRdp.class);
            SearchEntity<PartsRdp> searchEntity = new SearchEntity<PartsRdp>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsRdp> page = this.manager.findPartsRdpList(searchEntity);
            
            map = page.extjsResult();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存配件拆卸信息（范围内拆卸为更新数据，范围外拆卸为新增数据）【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsDismantleRegister() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String registerData = StringUtil.nvlTrim(getRequest().getParameter("registerData"), Constants.EMPTY_JSON_OBJECT);
            PartsDismantleRegister register = (PartsDismantleRegister) JSONUtil.read(registerData, PartsDismantleRegister.class);
            // id不为空并且状态为已登帐，则为修改，否则都是新登记
            // if (!StringUtil.isNullOrBlank(register.getIdx()) && PjwzConstants.STATUS_ED.equals(register.getStatus()))
            // errMsg = this.manager.updatePartsDismantleRegister(register);
            // else
            this.manager.savePartsDismantleRegister(register);
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
     * <li>创建日期：2016-01-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePartsDismantleRegisterForCancel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updatePartsDismantleRegisterForCancel(id);
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
     * <li>创建日期：2016-01-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getPartsAccount() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.SUCCESS, false);
        try {
            String searchJson = StringUtil.nvlTrim(getRequest().getParameter("searchJson"), Constants.EMPTY_JSON_OBJECT);
            // 解析查询对象
            PartsAccount searchEntity = (PartsAccount) JSONUtil.read(searchJson, PartsAccount.class);
            if (StringUtil.isNullOrBlank(searchEntity.getPartsNo()) && StringUtil.isNullOrBlank(searchEntity.getSpecificationModel())
                && StringUtil.isNullOrBlank(searchEntity.getIdentificationCode())) {
                map.put(Constants.ERRMSG, "参数不全！");
                return;
            }
            // 查询配件周转台账对象实体
            PartsAccount account = this.manager.getPartsAccount(searchEntity);
            // 只有【良好不在库】状态的配件才能安装！
            if (null !=account && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_FZC)) {
                map.put(Constants.ERRMSG, "此配件已登记，不能重复登记！");
                return;
            }
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
