package com.yunda.jx.base.jcgy.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.FaultMethod;
import com.yunda.jx.base.jcgy.manager.FaultMethodManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 故障处理方法控制器类
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
public class FaultMethodAction extends JXBaseAction<FaultMethod, FaultMethod, FaultMethodManager>{
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：选择处理方法列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void methodList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            FaultMethod entity = (FaultMethod) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<FaultMethod> searchEntity = new SearchEntity<FaultMethod>(entity, getStart(), getLimit(), getOrders());
            // 位置主键
            String placeFaultIDX = req.getParameter("placeFaultIDX");
            map = this.manager.methodList(searchEntity, placeFaultIDX).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);        	
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：查询故障处理方法列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void queryMethodList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			FaultMethod entity = (FaultMethod)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<FaultMethod> searchEntity = new SearchEntity<FaultMethod>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageList(searchEntity).extjsStore();
			map.put("id", "methodID");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
}
