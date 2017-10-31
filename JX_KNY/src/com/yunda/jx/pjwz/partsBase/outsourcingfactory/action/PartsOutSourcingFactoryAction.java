package com.yunda.jx.pjwz.partsBase.outsourcingfactory.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.outsourcingfactory.entity.PartsOutSourcingFactory;
import com.yunda.jx.pjwz.partsBase.outsourcingfactory.manager.PartsOutSourcingFactoryManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatClass控制器, 材料分类
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsOutSourcingFactoryAction extends JXBaseAction<PartsOutSourcingFactory, PartsOutSourcingFactory, PartsOutSourcingFactoryManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：配件委外厂家列表
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void OutSourcingFactoryList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            PartsOutSourcingFactory entity = JSONUtil.read(searchJson, PartsOutSourcingFactory.class);
            String factoryname = StringUtil.nvlTrim(req.getParameter("factoryname"), "");
            // query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String query = StringUtil.nvlTrim(req.getParameter("query"), "");
            String queryHql = req.getParameter("queryHql");
            SearchEntity<PartsOutSourcingFactory> searchEntity = new SearchEntity<PartsOutSourcingFactory>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, factoryname, queryHql, query).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}