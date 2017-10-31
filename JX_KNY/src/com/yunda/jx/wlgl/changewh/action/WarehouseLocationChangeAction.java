package com.yunda.jx.wlgl.changewh.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.wlgl.changewh.entity.WarehouseLocationChange;
import com.yunda.jx.wlgl.changewh.manager.WarehouseLocationChangeManager;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 调整库位控制器
 * <li>创建人：张迪
 * <li>创建日期：2016-5-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WarehouseLocationChangeAction extends JXBaseAction<WarehouseLocationChange, WarehouseLocationChange, WarehouseLocationChangeManager> {
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：调整仓库
     * <li>创建人：张迪
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void changeWhl() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
            try {
                WarehouseLocationChange whlChange = (WarehouseLocationChange)JSONUtil.read(getRequest(), WarehouseLocationChange.class);
                String[] errMsg = null;
                try {
                    errMsg = this.manager.changeWhl(whlChange);
                } catch (StockLackingException e) {
                    errMsg = new String[] { e.getMessage() };
                }
                if (errMsg == null || errMsg.length < 1) {
                    map.put("success", "true");
                }else{
                    map.put("success", "false");
                    map.put("errMsg", errMsg);
                }
            } catch (Exception e) {
                ExceptionUtil.process(e, logger, map);
            } finally {
                JSONUtil.write(this.getResponse(), map);
            }       
    }
}
