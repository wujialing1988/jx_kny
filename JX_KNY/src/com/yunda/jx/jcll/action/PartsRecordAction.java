package com.yunda.jx.jcll.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jcll.entity.PartsRecord;
import com.yunda.jx.jcll.manager.PartsRecordManager;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件履历
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class PartsRecordAction extends JXBaseAction<PartsRecord, PartsRecord, PartsRecordManager> {
    
    
    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    
    /**
     * <li>说明：配件履历查询
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void pageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            getRequest().getParameter("idx");
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            PartsRecord objEntity = (PartsRecord)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<PartsRecord> searchEntity = new SearchEntity<PartsRecord>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findPartsRecords(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
}
