package com.yunda.zb.trainhandover.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.trainhandover.entity.ZbglHoCase;
import com.yunda.zb.trainhandover.manager.ZbglHoCaseManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglHoCase控制器, 机车交接记录
 * <li>创建人：程梅
 * <li>创建日期：2015-02-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglHoCaseAction extends JXBaseAction<ZbglHoCase, ZbglHoCase, ZbglHoCaseManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：查询机车交接列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getCaseList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            ZbglHoCase hoCase = (ZbglHoCase) JSONUtil.read(searchJson, entitySearch.getClass());
            String startDate = StringUtil.nvlTrim(req.getParameter("startDate"));
            String overDate = StringUtil.nvlTrim(req.getParameter("overDate"));
            SearchEntity<ZbglHoCase> searchEntity = new SearchEntity<ZbglHoCase>(hoCase, getStart(), getLimit(), getOrders());
            map = this.manager.getCaseList(searchEntity, startDate, overDate).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取机车整备单关联的机车交接实体
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getEntityByRdp() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String rdpIDX = StringUtil.nvlTrim(req.getParameter("rdpIDX"), "");
            ZbglHoCase hoCase = this.manager.getEntityByRdp(rdpIDX);
            map.put("entity", hoCase);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
