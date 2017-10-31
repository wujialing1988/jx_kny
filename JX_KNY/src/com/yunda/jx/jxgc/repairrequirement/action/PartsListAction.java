package com.yunda.jx.jxgc.repairrequirement.action; 

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
import com.yunda.jx.jxgc.repairrequirement.entity.PartsList;
import com.yunda.jx.jxgc.repairrequirement.manager.PartsListManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsList控制器, 配件清单
 * <li>创建人：程梅
 * <li>创建日期：2016-1-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsListAction extends JXBaseAction<PartsList, PartsList, PartsListManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());	
    /**
     * 
     * <li>说明：配件清单列表
     * <li>创建人：程梅
     * <li>创建日期：2016-1-8
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
            PartsList objEntity = (PartsList)JSONUtil.read(searchJson, PartsList.class);
            SearchEntity<PartsList> searchEntity = new SearchEntity<PartsList>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsList> page = this.manager.findPartsList(searchEntity);
            
            map = page.extjsResult();

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：批量保存新增的配件清单
     * <li>创建人：程梅
     * <li>创建日期：2016-1-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void save() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            PartsList[] parts = JSONUtil.read(getRequest(), PartsList[].class);
            String[] errMsg = this.manager.validateUpdate(parts);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveOrUpdate(parts);
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }           
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}