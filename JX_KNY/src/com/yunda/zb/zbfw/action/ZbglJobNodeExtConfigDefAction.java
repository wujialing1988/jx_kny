package com.yunda.zb.zbfw.action; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.zbfw.entity.ZbglJobNodeExtConfigDef;
import com.yunda.zb.zbfw.manager.ZbglJobNodeExtConfigDefManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglJobNodeExtConfigDef控制器, 整备扩展配置
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbglJobNodeExtConfigDefAction extends JXBaseAction<ZbglJobNodeExtConfigDef, ZbglJobNodeExtConfigDef, ZbglJobNodeExtConfigDefManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：批量保存
     * <li>创建人：程梅
     * <li>创建日期：2016-04-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void save() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            ZbglJobNodeExtConfigDef[] array = JSONUtil.read(getRequest(), ZbglJobNodeExtConfigDef[].class);
             List< ZbglJobNodeExtConfigDef> entityList = new ArrayList< ZbglJobNodeExtConfigDef>();
             for (ZbglJobNodeExtConfigDef configDef : array) {
                 String[] errMsg = this.manager.validateUpdate(configDef);
                 if (errMsg == null || errMsg.length < 1) {
                     entityList.add(configDef);
                 } else {
                     map.put(Constants.SUCCESS, false);
                     map.put(Constants.ERRMSG, errMsg);
                 }
             }
             this.manager.saveOrUpdate(entityList);
             map.put(Constants.SUCCESS, true);
             map.put("list", entityList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
}