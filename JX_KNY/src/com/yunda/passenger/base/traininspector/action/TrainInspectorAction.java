package com.yunda.passenger.base.traininspector.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.passenger.base.traininspector.entity.TrainInspector;
import com.yunda.passenger.base.traininspector.manager.TrainInspectorManager;

/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
public class TrainInspectorAction extends JXBaseAction<TrainInspector, TrainInspector, TrainInspectorManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
   
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-4-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void saveFromEmployee() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	List<TrainInspector> empList = JSONObject.parseObject(getRequest().getParameter("datas"), new TypeReference<List<TrainInspector>>(){});
            String[] errMsg = this.manager.validateUpdateList(empList);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveOrUpdate(empList);
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

	/**
	 * <li>说明：单表分页查询，返回单表分页查询记录的json
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void findInspectorList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}" );
			TrainInspector objEntity = (TrainInspector)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<TrainInspector> searchEntity = new SearchEntity<TrainInspector>(objEntity, getStart(), getLimit(), getOrders());
			map = this.manager.findInspectorList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
    
    
}
