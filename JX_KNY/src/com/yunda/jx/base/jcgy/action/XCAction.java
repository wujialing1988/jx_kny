package com.yunda.jx.base.jcgy.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.XC;
import com.yunda.jx.base.jcgy.manager.XCManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：XC控制器, 修程编码
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class XCAction extends JXBaseAction<XC, XC, XCManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
    /**
     * <li>说明：单表分页查询修程，返回单表分页查询记录的json
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public void pageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            String rcTypeIDX = StringUtil.nvlTrim( req.getParameter("rcTypeIDX"),""); //修程类型主键（用于过滤修程类型对应的修程）
            String undertakeTrainTypeIDX = StringUtil.nvlTrim( req.getParameter("undertakeTrainTypeIDX"),""); //承修车型主键（用于过滤承修车型对应的修程）
            XC entity = (XC)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<XC> searchEntity = new SearchEntity<XC>(entity, getStart(), getLimit(), getOrders());
            Page page = this.manager.findPageList(searchEntity, rcTypeIDX , undertakeTrainTypeIDX);
            map.put("id", "typeID");
            map.put("root", page.getList());
            map.put("totalProperty", page.getTotal() == null ? page.getList().size() : page.getTotal());
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /* *
     * <li>说明：页面获取所有修程信息
     * <li>创建人：袁健
     * <li>创建日期：2013-3-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     代码重构
    @SuppressWarnings("unchecked")
	public void getRepairType() throws Exception {
    	List<XC> list = (List<XC>) this.manager.find("from XC");
    	Map<String, Object> xcs = new HashMap<String, Object>();
    	for (XC xc : list) {
			xcs.put(xc.getXcID(), xc.getXcName());
		}
    	JSONUtil.write(this.getResponse(), xcs);
    }*/
}