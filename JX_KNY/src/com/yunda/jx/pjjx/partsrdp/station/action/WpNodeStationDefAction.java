package com.yunda.jx.pjjx.partsrdp.station.action; 

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
import com.yunda.jx.pjjx.partsrdp.station.entity.WpNodeStationDef;
import com.yunda.jx.pjjx.partsrdp.station.manager.WpNodeStationDefManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WpNodeStationDef控制器, 关联作业工位定义
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WpNodeStationDefAction extends JXBaseAction<WpNodeStationDef, WpNodeStationDef, WpNodeStationDefManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：根据流程节点id查询该节点下的作业工位信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findPageListForWPNode() throws JsonMappingException, IOException {
        HttpServletRequest req = getRequest();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 查询实体
            String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
            WpNodeStationDef def = (WpNodeStationDef)JSONUtil.read(searchJson, WpNodeStationDef.class);
            SearchEntity<WpNodeStationDef> searchEntity = new SearchEntity<WpNodeStationDef>(def, getStart(), getLimit(), getOrders());
            map = this.manager.findPageListForWPNode(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：保存作业节点关联作业工位信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveStationDefs() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WpNodeStationDef[] stationList = (WpNodeStationDef[])JSONUtil.read(getRequest(), WpNodeStationDef[].class);
            String[] errMsg = this.manager.validateSave(stationList);
            
          if (errMsg == null || errMsg.length < 1) {
              this.manager.saveStationDefs(stationList);
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