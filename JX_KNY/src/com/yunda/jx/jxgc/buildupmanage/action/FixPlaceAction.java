
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
import com.yunda.jx.jxgc.buildupmanage.entity.FixPlace;
import com.yunda.jx.jxgc.buildupmanage.manager.FixPlaceManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FixPlace控制器, 组成型号安装位置
 * <li>创建人：程锐
 * <li>创建日期：2012-10-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class FixPlaceAction extends JXBaseAction<FixPlace, FixPlace, FixPlaceManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：获取下级位置列表
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void childPlaceList() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            // 上级安装位置主键
            String parentIdx = req.getParameter("parentIdx");
            //所属组成型号主键
            String partsBuildUpTypeIdx = req.getParameter("partsBuildUpTypeIdx");
            
            entity = (FixPlace) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<FixPlace> searchEntity = new SearchEntity<FixPlace>(entity, getStart(), getLimit(), null);            
            map = this.manager.childPlaceList(searchEntity,parentIdx, partsBuildUpTypeIdx).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：保存位置信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
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
            FixPlace fixPlace = (FixPlace)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(fixPlace);
            if (errMsg == null || errMsg.length < 1) {
                if(!StringUtil.isNullOrBlank(fixPlace.getPartsBuildUpTypeIdx()) && !StringUtil.isNullOrBlank(fixPlace.getParentIdx())){
                    fixPlace.setFixPlaceFullCode(this.manager.getPlaceFullCode(fixPlace.getFixPlaceCode(), fixPlace.getParentIdx(), fixPlace.getPartsBuildUpTypeIdx()));
                    fixPlace.setFixPlaceFullName(this.manager.getPlaceFullName(fixPlace.getFixPlaceName(), fixPlace.getParentIdx(), fixPlace.getPartsBuildUpTypeIdx()));
                }                
                this.manager.saveOrUpdate(fixPlace);
//              返回记录保存成功的实体对象
                map.put("entity", fixPlace);  
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
    /* *
     * 
     * <li>说明：根据安装位置主键获取安装位置信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
          代码重构
    public void getFixPlaceByIdx() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            // 组成主键
            String fixPlaceIdx = req.getParameter("fixPlaceIdx");
            FixPlace fixPlace = new FixPlace();
            if (!StringUtil.isNullOrBlank(fixPlaceIdx)) {
                fixPlace = this.manager.getModelById(fixPlaceIdx);
            }
            map.put("entity", fixPlace);
            map.put("success", "true");
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    } */
}
