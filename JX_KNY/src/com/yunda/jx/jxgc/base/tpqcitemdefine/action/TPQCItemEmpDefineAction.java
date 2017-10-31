package com.yunda.jx.jxgc.base.tpqcitemdefine.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemEmpDefine;
import com.yunda.jx.jxgc.base.tpqcitemdefine.manager.TPQCItemEmpDefineManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质量检查人员基础配置控制器类
 * <li>创建人：程锐
 * <li>创建日期：2015-6-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class TPQCItemEmpDefineAction extends JXBaseAction<TPQCItemEmpDefine, TPQCItemEmpDefine, TPQCItemEmpDefineManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * <li>说明：质量检查人员分页查询（用于解决unix系统下，WN_CONCAT函数不能使用的错误）
     * <li>创建人：何涛
     * <li>创建日期：2015-8-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void queryPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            // 查询实体
            TPQCItemEmpDefine objEntity = JSONUtil.read(searchJson, TPQCItemEmpDefine.class);
            SearchEntity<TPQCItemEmpDefine> searchEntity = new SearchEntity<TPQCItemEmpDefine>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.queryPageList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
}
