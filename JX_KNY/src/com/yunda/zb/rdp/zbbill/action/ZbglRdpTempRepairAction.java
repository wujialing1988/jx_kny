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
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpBean;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpTempRepair;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpTempRepairManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpTempRepair控制器, 转临修
 * <li>创建人：程锐
 * <li>创建日期：2015-01-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglRdpTempRepairAction extends JXBaseAction<ZbglRdpTempRepair, ZbglRdpTempRepair, ZbglRdpTempRepairManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：查询整备单列表-碎修
     * <li>创建人：程锐
     * <li>创建日期：2015-1-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findSXRdpList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            ZbglRdpBean entity = (ZbglRdpBean) JSONUtil.read(searchJson, ZbglRdpBean.class);
            SearchEntity<ZbglRdpBean> searchEntity = new SearchEntity<ZbglRdpBean>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findSXRdpList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询整备单列表-临修
     * <li>创建人：程锐
     * <li>创建日期：2015-1-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findLXRdpList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            ZbglRdpBean entity = (ZbglRdpBean) JSONUtil.read(searchJson, ZbglRdpBean.class);
            SearchEntity<ZbglRdpBean> searchEntity = new SearchEntity<ZbglRdpBean>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findLXRdpList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
