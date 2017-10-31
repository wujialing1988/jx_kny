package com.yunda.zb.trainclean.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.trainclean.entity.ZbglCleaning;
import com.yunda.zb.trainclean.manager.ZbglCleaningManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglCleaning控制器, 机车保洁记录
 * <li>创建人：程梅
 * <li>创建日期：2015-02-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglCleaningAction extends JXBaseAction<ZbglCleaning, ZbglCleaning, ZbglCleaningManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：查询机车保洁列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getCleaningList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            ZbglCleaning cleaning = (ZbglCleaning) JSONUtil.read(searchJson, entitySearch.getClass());
            String startDate = StringUtil.nvlTrim(req.getParameter("startDate"));
            String overDate = StringUtil.nvlTrim(req.getParameter("overDate"));
            SearchEntity<ZbglCleaning> searchEntity = new SearchEntity<ZbglCleaning>(cleaning, getStart(), getLimit(), getOrders());
            map = this.manager.getCleaningList(searchEntity, startDate, overDate).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取机车整备单关联的机车保洁实体
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
            ZbglCleaning clean = this.manager.getEntityByRdp(rdpIDX);
            map.put("entity", clean);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
