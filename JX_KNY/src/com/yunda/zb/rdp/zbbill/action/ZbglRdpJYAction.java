package com.yunda.zb.rdp.zbbill.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpJYManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备交验控制器类
 * <li>创建人：程锐
 * <li>创建日期：2015-3-12
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class ZbglRdpJYAction extends JXBaseAction<ZbglRdp, ZbglRdp, ZbglRdpJYManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：获取机车整备交验列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findRdpListForJY() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            ZbglRdp objEntity = (ZbglRdp) JSONUtil.read(searchJson, ZbglRdp.class);
            SearchEntity<ZbglRdp> searchEntity = new SearchEntity<ZbglRdp>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findRdpListForJY(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取机车整备单关联的任务列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findTaskList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            ZbglRdp objEntity = (ZbglRdp) JSONUtil.read(searchJson, ZbglRdp.class);
            SearchEntity<ZbglRdp> searchEntity = new SearchEntity<ZbglRdp>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findTaskList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：验证机车整备单关联的整备任务单、提票单、普查整治任务单是否全部完成
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void validateForJY() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String rdpIDX = StringUtil.nvlTrim(req.getParameter("rdpIDX"), "");
            String[] errMsg = this.manager.validateUpdate(rdpIDX);
            map.put("success", true);
            map.put("errMsg", errMsg);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：确认机车整备交验
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateForJY() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            ZbglRdp rdp = (ZbglRdp) JSONUtil.read(req, entity.getClass());
            String rdpIDX = StringUtil.nvlTrim(req.getParameter("rdpIDX"), "");
            this.manager.updateForJY(rdp, rdpIDX);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：通过出入段台账idx判断该车是否已经交验
     * <li>创建人：林欢
     * <li>创建日期：2016-8-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void trainIsDoZbglRdpJY() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String trainAccessAccountIDX = req.getParameter("trainAccessAccountIDX");
            Boolean flag = this.manager.trainIsDoZbglRdpJY(trainAccessAccountIDX);
            map.put("flag", flag);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
