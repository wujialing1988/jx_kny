package com.yunda.jx.wlgl.stockmanage.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MATSTOCK控制器, 物料库存台账
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatStockAction extends JXBaseAction<MatStock, MatStock, MatStockManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：列表信息【用于物料选择控件】
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findStockList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            MatStock entity = JSONUtil.read(searchJson, MatStock.class);
            String matCode = StringUtil.nvlTrim(req.getParameter("matCode"), "");
            String whIdx = StringUtil.nvlTrim(req.getParameter("whIdx"), "");
            // query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String query = StringUtil.nvlTrim(req.getParameter("query"), "");
            String queryHql = req.getParameter("queryHql");
            SearchEntity<MatStock> searchEntity = new SearchEntity<MatStock>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, matCode, whIdx, queryHql, query).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}