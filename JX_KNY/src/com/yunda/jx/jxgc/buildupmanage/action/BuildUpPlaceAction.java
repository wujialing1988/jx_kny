package com.yunda.jx.jxgc.buildupmanage.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpPlace;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpPlaceManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：BuildUpPlace控制器, 组成位置
 * <li>创建人：程锐
 * <li>创建日期：2013-01-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class BuildUpPlaceAction extends JXBaseAction<BuildUpPlace, BuildUpPlace, BuildUpPlaceManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：获取组成位置列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void placeList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String placeTypes = req.getParameter("placeTypes");
            String buildUpTypeIdx = req.getParameter("buildUpTypeIdx");
            String isFix = req.getParameter("isFix");
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            BuildUpPlace entity = (BuildUpPlace)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<BuildUpPlace> searchEntity = new SearchEntity<BuildUpPlace>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.placeList(searchEntity, placeTypes, buildUpTypeIdx, isFix).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：获取下级位置列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void childPlaceList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String placeTypes = req.getParameter("placeTypes");
            String buildUpTypeIDX = req.getParameter("buildUpTypeIDX");
            String parentIdx = req.getParameter("parentIdx");
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            BuildUpPlace entity = (BuildUpPlace)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<BuildUpPlace> searchEntity = new SearchEntity<BuildUpPlace>(entity, getStart(), getLimit(), getOrders());
            if(!StringUtil.isNullOrBlank(placeTypes) && !StringUtil.isNullOrBlank(buildUpTypeIDX) && !StringUtil.isNullOrBlank(parentIdx)){
                map = this.manager.childPlaceList(searchEntity.getStart(), searchEntity.getLimit(), placeTypes, buildUpTypeIDX, parentIdx, searchEntity.getOrders()).extjsStore();
            }            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * <li>说明：保存安装位置
     * <li>创建人：程锐
     * <li>创建日期：2013-02-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
    public void saveOrUpdate() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            BuildUpPlace buildUpPlace = (BuildUpPlace)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(buildUpPlace, "");
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveOrUpdate(buildUpPlace);
//              返回记录保存成功的实体对象
                map.put("entity", buildUpPlace);  
                map.put("success", true);
            } else {
                map.put("success", false);
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
     * <li>说明：保存结构位置及组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-1-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void saveStructure() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            BuildUpPlace buildUpPlace = (BuildUpPlace)JSONUtil.read(getRequest(), entity.getClass());
            String placeIDX = buildUpPlace.getIdx();
            String buildUpTypeIDX = StringUtil.nvlTrim(req.getParameter("buildUpTypeIDX"),"");
            String parentIdx = StringUtil.nvlTrim(req.getParameter("parentIdx"),"");
            String[] errMsg = this.manager.validateUpdate(buildUpPlace, buildUpTypeIDX);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveStructure(buildUpPlace, buildUpTypeIDX, parentIdx, placeIDX);
//              返回记录保存成功的实体对象
                map.put("entity", buildUpPlace);  
                map.put("success", true);
            } else {
                map.put("success", false);
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
     * <li>说明：保存安装位置或虚拟位置及组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void savePlace() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            BuildUpPlace buildUpPlace = (BuildUpPlace)JSONUtil.read(getRequest(), entity.getClass());
            String placeIDX = buildUpPlace.getIdx();
            String buildUpTypeIDX = StringUtil.nvlTrim(req.getParameter("buildUpTypeIDX"),"");
            String parentIdx = StringUtil.nvlTrim(req.getParameter("parentIdx"),"");
            String[] errMsg = this.manager.validateUpdate(buildUpPlace, buildUpTypeIDX);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.savePlace(buildUpPlace, buildUpTypeIDX, parentIdx, placeIDX);
//              返回记录保存成功的实体对象
                map.put("entity", buildUpPlace);  
                map.put("success", true);
            } else {
                map.put("success", false);
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
     * <li>说明：验证是否有位置名称重复
     * <li>创建人：程锐
     * <li>创建日期：2013-3-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void checkReduplicateName() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            BuildUpPlace buildUpPlace = (BuildUpPlace)JSONUtil.read(getRequest(), entity.getClass());
            String buildUpTypeIDX = StringUtil.nvlTrim(getRequest().getParameter("buildUpTypeIDX"),""); 
            String[] errMsg = this.manager.checkReduplicateName(buildUpPlace, buildUpTypeIDX);
            if (errMsg != null && errMsg.length > 0) {
                map.put("errMsg", errMsg);  
                map.put("success", true);
            } else {
                map.put("errMsg", errMsg);
                map.put("success", false);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }      
    }
    /**
     * 
     * <li>说明：逻辑删除安装位置或虚拟位置并级联删除关联的可安装组成型号
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void deletePlace() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.deletePlace(ids);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }           
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}