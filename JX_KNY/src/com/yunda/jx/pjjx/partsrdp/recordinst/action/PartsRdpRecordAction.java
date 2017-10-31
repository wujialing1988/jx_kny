package com.yunda.jx.pjjx.partsrdp.recordinst.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecord控制器, 配件检修记录单实例
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsRdpRecordAction extends JXBaseAction<PartsRdpRecord, PartsRdpRecord, PartsRdpRecordManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：分页查询
     * <li>创建人：何涛
     * <li>创建日期：2016-1-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void queryPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entityJson = StringUtil.nvl(getRequest().getParameter("entityJson"), "{}");
            PartsRdpRecord entity = JSONUtil.read(entityJson, PartsRdpRecord.class);
            
            SearchEntity<PartsRdpRecord> searchEntity = new SearchEntity<PartsRdpRecord>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.queryPageList(searchEntity).extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * <li>说明：查询检修记录单
     * <li>创建人：张迪
     * <li>创建日期：2016-7-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void queryRecordPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entityJson = StringUtil.nvl(getRequest().getParameter("entityJson"), "{}");
            PartsRdpRecord entity = JSONUtil.read(entityJson, PartsRdpRecord.class);
            
            SearchEntity<PartsRdpRecord> searchEntity = new SearchEntity<PartsRdpRecord>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.queryRecordPageList(searchEntity).extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
	
}