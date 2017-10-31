package com.yunda.jx.wlgl.movewh.action; 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.wlgl.movewh.entity.MatMoveWH;
import com.yunda.jx.wlgl.movewh.manager.MatMoveWHManager;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatMoveWH控制器, 移库单
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
public class MatMoveWHAction extends JXBaseAction<MatMoveWH, MatMoveWH, MatMoveWHManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
    /**
     * <li>说明：移入确认中：退回原库
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void backOriginal() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.backOriginal(ids);         
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * <li>说明：移库移出退回中：退回
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void back() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.back(ids);         
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
}