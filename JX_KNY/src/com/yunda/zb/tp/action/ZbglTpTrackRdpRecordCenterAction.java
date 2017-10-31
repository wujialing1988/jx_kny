package com.yunda.zb.tp.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.entity.ZbglTpTrackRdpRecordCenter;
import com.yunda.zb.tp.manager.ZbglTpTrackRdpRecordCenterManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpTrackRdpRecordCenterAction控制器, 提票跟踪记录单关联表
 * <li>创建人：林欢
 * <li>创建日期：2016-08-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglTpTrackRdpRecordCenterAction extends JXBaseAction<ZbglTpTrackRdpRecordCenter, ZbglTpTrackRdpRecordCenter, ZbglTpTrackRdpRecordCenterManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * <li>说明：通过提票跟踪单idx查询与之关联的jt6提票
     * <li>创建人：林欢
     * <li>创建日期：2016-8-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public void findZbglTpByTrackRdpIDX() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	String searchJson = StringUtil.nvlTrim(getRequest().getParameter("entityJson"), "{}" );
        	String zbglTpTrackIDX = getRequest().getParameter("zbglTpTrackIDX");
			ZbglTp objEntity = (ZbglTp)JSONUtil.read(searchJson, ZbglTp.class);
			SearchEntity<ZbglTp> searchEntity = new SearchEntity<ZbglTp>(objEntity, getStart(), getLimit(), getOrders());
			Page<ZbglTp> page = this.manager.findZbglTpByTrackRdpIDX(searchEntity, zbglTpTrackIDX);
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    
    /**
     * <li>说明：通过提票跟踪 记录单 idx查询与之关联的jt6提票
     * <li>创建人:刘国栋
     * <li>创建日期：2016-8-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */ 
    public void findZbglTpByTrackRdpRecordIDX()throws Exception{
    	Map<String, Object> map = new HashMap<String,Object>();
        try {
        	String searchJson = StringUtil.nvlTrim(getRequest().getParameter("entityJson"), "{}" );
        	String recordIDX = getRequest().getParameter("recordIDX");
			ZbglTp objEntity = (ZbglTp)JSONUtil.read(searchJson, ZbglTp.class);
			SearchEntity<ZbglTp> searchEntity = new SearchEntity<ZbglTp>(objEntity, getStart(), getLimit(), getOrders());
			Page<ZbglTp> page = this.manager.findZbglTpByTrackRdpRecordIDX(searchEntity, recordIDX);
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}
