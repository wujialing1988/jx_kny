package com.yunda.jx.pjjx.base.recorddefine.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.recorddefine.entity.PartsFwList;
import com.yunda.jx.pjjx.base.recorddefine.manager.PartsFwListManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件清单控制器
 * <li>创建人：程锐
 * <li>创建日期：2016-2-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@SuppressWarnings(value="serial")
public class PartsFwListAction extends JXBaseAction<PartsFwList, PartsFwList, PartsFwListManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
	
    /**
     * <li>说明：查询配件清单列表
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
	public void findPartsList () throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT );
            PartsFwList objEntity = (PartsFwList)JSONUtil.read(searchJson, PartsFwList.class);
            SearchEntity<PartsFwList> searchEntity = new SearchEntity<PartsFwList>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsFwList> page = this.manager.findPartsList(searchEntity);            
            map = page.extjsResult();

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
	
    /**
     * <li>说明：保存配件清单
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
	public void save() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	PartsFwList[] parts = JSONUtil.read(getRequest(), PartsFwList[].class);
        	this.manager.saveOrUpdate(parts);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}
