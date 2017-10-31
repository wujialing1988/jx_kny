package com.yunda.jx.jxgc.configmanage.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.configmanage.entity.TrainConfigInfo;
import com.yunda.jx.jxgc.configmanage.manager.TrainConfigInfoManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车配置视图控制器类
 * <li>创建人：程锐
 * <li>创建日期：2014-3-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class TrainConfigInfoAction extends JXBaseAction<TrainConfigInfo, TrainConfigInfo, TrainConfigInfoManager> {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：单表分页查询，返回单表分页查询记录的json
     * <li>创建人：程锐
     * <li>创建日期：2013-02-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void pageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            TrainConfigInfo entity = (TrainConfigInfo)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<TrainConfigInfo> searchEntity = new SearchEntity<TrainConfigInfo>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}
