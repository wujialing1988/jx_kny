package com.yunda.jx.pjwz.partsBase.location.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.location.entity.Location;
import com.yunda.jx.pjwz.partsBase.location.manager.LocationManager;
import com.yunda.jxpz.phrasedic.entity.PhraseDicItem;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：wellPartsRegister控制器, 良好配件登记
 * <li>创建人：程梅
 * <li>创建日期：2015-10-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class LocationAction extends JXBaseAction<Location, Location, LocationManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：查询存放位置列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findLocationList () throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            String flag = req.getParameter("flag") ; //标示查询已收藏的存放位置还是未被收藏的存放位置
            List<PhraseDicItem> list = this.manager.findLocationList(flag);
            map = Page.extjsStore("dictItemId", list);

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：分页查询存放位置列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findLocationPage() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String flag = req.getParameter("flag") ; //标示查询已收藏的存放位置还是未被收藏的存放位置
            String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
            PhraseDicItem entity = (PhraseDicItem)JSONUtil.read(searchJson, PhraseDicItem.class);
            SearchEntity<PhraseDicItem> searchEntity = new SearchEntity<PhraseDicItem>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.getPageByDictTypeId(searchEntity,flag).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * 
     * <li>说明：收藏存放位置
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveLocationCollect() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            OmEmployee emp=(OmEmployee) getSession().getAttribute("emp");
            String[] errMsg = this.manager.saveLocationCollect(id, emp) ;
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
    public void deleteLocationCollect() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            OmEmployee emp=(OmEmployee) getSession().getAttribute("emp");
            this.manager.deleteLocationCollect(id, emp) ;
            map.put("success", "true");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：保存存放位置
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveLocation() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String locationName = req.getParameter("locationName") ; //存放位置名称
            String[] errMsg = this.manager.saveLocation(locationName) ;
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
}