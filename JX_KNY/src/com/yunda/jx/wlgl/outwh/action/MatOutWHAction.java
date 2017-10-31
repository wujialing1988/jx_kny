package com.yunda.jx.wlgl.outwh.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.outwh.entity.MatOutWH;
import com.yunda.jx.wlgl.outwh.manager.MatOutWHManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatOutWH控制器, 出库单
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatOutWHAction extends JXBaseAction<MatOutWH, MatOutWH, MatOutWHManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：新增物料出库
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveMatOutWH() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            MatOutWH matOutWH = (MatOutWH)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(matOutWH);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveMatOutWH(matOutWH);
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
     * <li>说明：查找待移入的出库单
     * <li>创建人：张迪
     * <li>创建日期：2016-5-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findMoveInList() throws JsonMappingException, IOException {
          Map<String, Object> map = new HashMap<String, Object>();
          HttpServletRequest req = getRequest();
          try {
                String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
                MatOutWH entity = (MatOutWH) JSONUtil.read(searchJson, entitySearch.getClass());
                SearchEntity<MatOutWH> searchEntity = new SearchEntity<MatOutWH>(
                        entity, getStart(), getLimit(), getOrders());
                map = this.manager.findMoveInList(searchEntity).extjsStore();
          } catch (Exception e) {
              ExceptionUtil.process(e, logger);
          } finally {
              JSONUtil.write(getResponse(), map);
          }
   }
    /**
     * <li>说明：查找待退回原库的出库单
     * <li>创建人：张迪
     * <li>创建日期：2016-5-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findBackMoveInList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
            MatOutWH entity = (MatOutWH) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<MatOutWH> searchEntity = new SearchEntity<MatOutWH>(
                entity, getStart(), getLimit(), getOrders());
            map = this.manager.findBackMoveInList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：称库状态为退回原库的入库确认
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveMoveBackInMat() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            MatOutWH matOutWH = (MatOutWH)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(matOutWH);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveMoveBackInMat(matOutWH);
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