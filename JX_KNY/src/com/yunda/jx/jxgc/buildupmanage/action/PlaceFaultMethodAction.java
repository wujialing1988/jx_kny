package com.yunda.jx.jxgc.buildupmanage.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFaultMethod;
import com.yunda.jx.jxgc.buildupmanage.manager.PlaceFaultMethodManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 故障现象处理方法控制器
 * <li>创建人：程锐
 * <li>创建日期：2013-4-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class PlaceFaultMethodAction extends JXBaseAction<PlaceFaultMethod, PlaceFaultMethod, PlaceFaultMethodManager>{
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 
     * <li>说明：批量保存故障现象处理方法
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void saveOrUpdateList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();        
        try {
            PlaceFaultMethod[] list = (PlaceFaultMethod[]) JSONUtil.read(getRequest(), PlaceFaultMethod[].class); // 获取json数组转换成对象数组
            this.manager.saveOrUpdateList(list);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：设为默认
     * <li>创建人：程锐
     * <li>创建日期：2013-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void setIsDefault() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PlaceFaultMethod placeFaultMethod = this.manager.getModelById(id);
            this.manager.setIsDefault(placeFaultMethod);
            map.put("success", "true");            
            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>方法名称：findDefaultMethod
     * <li>方法说明：获取默认处理方法 
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-4-24 下午03:04:14
     * <li>修改人：
     * <li>修改内容：
     */
    public void findDefaultMethod() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String placeIdx = getRequest().getParameter("PlaceIdx");
            PlaceFaultMethod placeFaultMethod = this.manager.getDefaultMethodByPlace(placeIdx);
            map.put("success", "true");            
            map.put("entity",placeFaultMethod);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
