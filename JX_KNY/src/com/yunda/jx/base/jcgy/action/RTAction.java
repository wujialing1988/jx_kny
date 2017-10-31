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
import com.yunda.jx.base.jcgy.entity.RT;
import com.yunda.jx.base.jcgy.manager.RTManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RT控制器, 修次(RepairTime)
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
public class RTAction extends JXBaseAction<RT, RT, RTManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：单表分页查询修次，返回单表分页查询记录的json
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-30
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
            String rcIDX = StringUtil.nvlTrim( req.getParameter("rcIDX"),"");
            RT entity = (RT)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<RT> searchEntity = new SearchEntity<RT>(entity, getStart(), getLimit(), getOrders());
            Page page = this.manager.findPageList(searchEntity, rcIDX);
            map.put("id", "typeID");
            map.put("root", page.getList());
            map.put("totalProperty", page.getTotal() == null ? page.getList().size() : page.getTotal());
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}