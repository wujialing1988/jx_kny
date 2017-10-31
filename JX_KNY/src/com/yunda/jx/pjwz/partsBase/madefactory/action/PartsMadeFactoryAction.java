package com.yunda.jx.pjwz.partsBase.madefactory.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.madefactory.entity.PartsMadeFactory;
import com.yunda.jx.pjwz.partsBase.madefactory.manager.PartsMadeFactoryManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsMadeFactory控制类   配件生产厂家
 * <li>创建人：程梅
 * <li>创建日期：2013-7-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@SuppressWarnings(value="serial")
public class PartsMadeFactoryAction extends JXBaseAction<PartsMadeFactory, PartsMadeFactory, PartsMadeFactoryManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * /jx/js/component/pjwz/PartsMadeFactorySelect.js调用
	 * /jsp/jx/pjwz/partbase/MadeFactorySelectWin.js调用
	 * <li>说明：配件生产厂家列表
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public void factoryList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            PartsMadeFactory factory = JSONUtil.read(searchJson, PartsMadeFactory.class);
            String factoryname = StringUtil.nvlTrim(req.getParameter("factoryname"), "");
            // query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String query = StringUtil.nvlTrim(req.getParameter("query"), "");
            String queryHql = req.getParameter("queryHql");
            SearchEntity<PartsMadeFactory> searchEntity = new SearchEntity<PartsMadeFactory>(factory, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, factoryname, queryHql, query).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
	/**
     * 
     * <li>说明：查询某一个生产厂家信息
     * <li>创建人：程梅
     * <li>创建日期：2015-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
	 */
	public void factory() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String, Object>();
        try {
            PartsMadeFactory f = this.manager.getModelById(id);
            map.put("t", f);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
    /**
     * 
     * <li>说明：查询生产厂家列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findFactoryList () throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            String flag = req.getParameter("flag") ; //标示查询已收藏的生产厂家还是未被收藏的生产厂家
            String madeFactoryShortname = req.getParameter("madeFactoryShortname") ; //生产厂家简称
            List<PartsMadeFactory> list = this.manager.findFactoryList(flag,madeFactoryShortname);
            map = Page.extjsStore("id", list);

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：收藏生产厂家
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveFactoryCollect() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            OmEmployee emp=(OmEmployee) getSession().getAttribute("emp");
            String[] errMsg = this.manager.saveFactoryCollect(id, emp) ;
          if (errMsg == null || errMsg.length < 1) {
              map.put("success", "true");
          }else{
              map.put("errMsg", errMsg);
          }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：取消收藏存放位置
     * <li>创建人：程梅
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void deleteFactoryCollect() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            OmEmployee emp=(OmEmployee) getSession().getAttribute("emp");
            this.manager.deleteFactoryCollect(id, emp) ;
            map.put("success", "true");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}