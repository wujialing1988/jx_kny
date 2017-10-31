package com.yunda.zb.tp.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.zb.tp.entity.ZbglTpTrackRdp;
import com.yunda.zb.tp.entity.ZbglTpTrackRdpDTO;
import com.yunda.zb.tp.manager.ZbglTpTrackRdpManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpTrackRdpAction控制器, 提票跟踪单
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
public class ZbglTpTrackRdpAction extends JXBaseAction<ZbglTpTrackRdp, ZbglTpTrackRdp, ZbglTpTrackRdpManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：根据jt6提票idx查询是否有提票跟踪单
     * <li>创建人：林欢
     * <li>创建日期：2016-8-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void existZbglTpTrackRdpByJT6IDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	String jt6IDX = getRequest().getParameter("jt6IDX");
        	map = this.manager.existZbglTpTrackRdpByJT6IDX(jt6IDX,map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存提票跟踪单相关信息
     * <li>创建人：林欢
     * <li>创建日期：2016-8-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveZbglTpTrackRdpInfo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	ZbglTpTrackRdp zbglTpTrackRdp = (ZbglTpTrackRdp)JSONUtil.read(getRequest(), entity.getClass());
        	
//        	获取当前登录操作员
    		AcOperator acOperator = SystemContext.getAcOperator();
    		zbglTpTrackRdp.setTrackPersonIDX(acOperator.getOperatorid().toString());
    		zbglTpTrackRdp.setTrackPersonName(acOperator.getOperatorname());
        	
        	this.manager.saveZbglTpTrackRdpInfo(zbglTpTrackRdp);
        	map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：提票跟踪主查询(正在跟踪/结束跟踪)
     * <li>创建人：林欢
     * <li>创建日期：2016-8-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findZbglTpTrackRdpPageList() throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entityJson = StringUtil.nvl(getRequest().getParameter("entityJson"), "{}");
            ZbglTpTrackRdp entity = JSONUtil.read(entityJson, ZbglTpTrackRdp.class);
            SearchEntity<ZbglTpTrackRdp> searchEntity = new SearchEntity<ZbglTpTrackRdp>(entity, getStart(), getLimit(), getOrders());
            Page<ZbglTpTrackRdpDTO> page = this.manager.findColorZbglTpTrackRdpPageList(searchEntity);
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
  
    
    /**
     * <li>说明：创建跟踪记录单，并修改跟踪单状态为开始跟踪，返回跟踪记录单idx
     * <li>创建人：刘国栋
     * <li>创建日期：2016-8-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void createRecordAndStartTrack() throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
        	HttpServletRequest reg = getRequest(); 
        	String trackIDX = reg.getParameter("trackIDX");
        	String rdpIDX = reg.getParameter("rdpIDX");
        	String singleStatus = reg.getParameter("singleStatus");
        	String inTime = reg.getParameter("inTime");
            
            String recordDX = this.manager.createRecordAndStartTrack(trackIDX, rdpIDX, singleStatus, inTime);
            map.put("recordDX", recordDX);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}
