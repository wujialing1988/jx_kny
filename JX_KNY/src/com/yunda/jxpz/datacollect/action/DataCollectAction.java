package com.yunda.jxpz.datacollect.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.datacollect.entity.DataCollect;
import com.yunda.jxpz.datacollect.entity.DataCollectId;
import com.yunda.jxpz.datacollect.manager.DataCollectManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DataCollect控制器, 常用数据收藏夹
 * <li>创建人：程梅
 * <li>创建日期：2015-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class DataCollectAction extends JXBaseAction<DataCollect, DataCollect, DataCollectManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：根据收藏者主键查询常用数据收藏夹列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageListByEmpId() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String collectEmpId = getRequest().getParameter("collectEmpId");  //收藏者主键
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            DataCollect objEntity = (DataCollect)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<DataCollect> searchEntity = new SearchEntity<DataCollect>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.pageListByEmpId(searchEntity, collectEmpId).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：保存常用数据收藏信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveDataCollect() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
//            DataCollectId collectId = (DataCollectId)JSONUtil.read(getRequest(), DataCollectId.class);
            HttpServletRequest req = getRequest();
            String collectJson = StringUtil.nvlTrim( req.getParameter("collectData"), "{}" );
            DataCollectId collect = (DataCollectId)JSONUtil.read(collectJson, DataCollectId.class);
            String[] errMsg = this.manager.validateSave(collect);
          if (errMsg == null || errMsg.length < 1) {
              this.manager.saveDataCollect(collect);
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