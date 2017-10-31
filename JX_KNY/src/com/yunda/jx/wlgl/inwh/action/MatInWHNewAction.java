package com.yunda.jx.wlgl.inwh.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.wlgl.inwh.entity.MatInWHNew;
import com.yunda.jx.wlgl.inwh.manager.MatInWHNewManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatInWHNew控制器, 入库单
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatInWHNewAction extends JXBaseAction<MatInWHNew, MatInWHNew, MatInWHNewManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：新增物料入库
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveMatInWHNew() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            MatInWHNew matInWHNew = (MatInWHNew)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(matInWHNew);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveMatInWHNew(matInWHNew);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
	
}