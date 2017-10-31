package com.yunda.jx.pjwz.partsmanage.action; 

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
import com.yunda.jx.pjwz.partsmanage.entity.RecordCodeBind;
import com.yunda.jx.pjwz.partsmanage.manager.RecordCodeBindManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RecordCodeBind控制器, 配件检修记录单识别码绑定
 * <li>创建人：程梅
 * <li>创建日期：2016-01-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class RecordCodeBindAction extends JXBaseAction<RecordCodeBind, RecordCodeBind, RecordCodeBindManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
     * <li>说明：根据识别码查询配件周转信息及记录单识别码绑定列表信息
     * <li>创建人：程梅
     * <li>创建日期：2016-1-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getAccountAndBindByCode() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String identificationCode = getRequest().getParameter("identificationCode");
            PartsAccount accountV = this.manager.getPartsAccount(identificationCode);
            if(null != accountV){
                List<RecordCodeBind> bindList = this.manager.getBindListByPartsAccountIdx(accountV.getIdx()) ;//查询该配件的记录单识别码绑定列表
                map.put("account", accountV) ;
                map.put("bindList", bindList) ;
                map.put(Constants.SUCCESS, true);
            }else{
                String str = "此配件未登记！";
                map.put(Constants.ERRMSG, str);
                map.put(Constants.SUCCESS, false);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：绑定【绑定前判断识别码是否被使用】
     * <li>创建人：程梅
     * <li>创建日期：2016-1-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void save() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String dataJson = StringUtil.nvlTrim( getRequest().getParameter("dataJson"), Constants.EMPTY_JSON_OBJECT );
            RecordCodeBind bind = (RecordCodeBind)JSONUtil.read(dataJson, RecordCodeBind.class);
            String[] errMsg = this.manager.saveRecordCodeBind(bind);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.ERRMSG, errMsg);
                map.put(Constants.SUCCESS, false);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
}