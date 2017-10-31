package com.yunda.freight.zb.qualitycontrol.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControlItemDefine;
import com.yunda.freight.zb.qualitycontrol.manager.ZbglQualityControlItemDefineManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglQualityControlItemDefineAction控制器, 检查项基础配置
 * <li>创建人：林欢
 * <li>创建日期：2016-10-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
public class ZbglQualityControlItemDefineAction extends JXBaseAction<ZbglQualityControlItemDefine, ZbglQualityControlItemDefine, ZbglQualityControlItemDefineManager>{
    
    Logger logger = Logger.getLogger(this.getClass());
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 
     * <li>说明：获取所有的质量检验项
     * <li>创建人：林欢
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void findZbglQualityControlItemDefineList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String businessCode = getRequest().getParameter("businessCode");//业务菜单编码（pda菜单名称）
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("businessCode", businessCode);//
            List<ZbglQualityControlItemDefine> zbglQualityControlItemDefineList = this.manager.findZbglQualityControlItemDefineList(paramsMap);
            map = new Page(zbglQualityControlItemDefineList).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}