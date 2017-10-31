
package com.yunda.jx.component.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.component.entity.GyjcFactory;
import com.yunda.jx.component.manager.GyjcFactoryManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：GyjcFactory控制器, 工厂编码
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-09-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class GyjcFactoryAction extends JXBaseAction<GyjcFactory, GyjcFactory, GyjcFactoryManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：生产厂家列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void factoryList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            GyjcFactory entity = JSONUtil.read(searchJson, GyjcFactory.class);
            String factoryname = StringUtil.nvlTrim(req.getParameter("factoryname"), "");
            // query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String query = StringUtil.nvlTrim(req.getParameter("query"), "");
            String queryHql = req.getParameter("queryHql");
            SearchEntity<GyjcFactory> searchEntity = new SearchEntity<GyjcFactory>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, factoryname, queryHql, query).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：根据id查询制造厂家信息
     * <li>创建人：程梅
     * <li>创建日期：2013-5-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void getFactory(){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			GyjcFactory factory = this.manager.getModelById(id);
			map.put("factory", factory);  
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			try {
				JSONUtil.write(this.getResponse(), map);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
