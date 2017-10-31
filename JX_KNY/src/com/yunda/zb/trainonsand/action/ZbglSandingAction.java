package com.yunda.zb.trainonsand.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.trainonsand.entity.ZbglSanding;
import com.yunda.zb.trainonsand.manager.ZbglSandingManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglSanding控制器, 机车上砂记录
 * <li>创建人：王利成
 * <li>创建日期：2015-01-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglSandingAction extends JXBaseAction<ZbglSanding, ZbglSanding, ZbglSandingManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：web页面查询已完成的上砂列表
     * <li>创建人：王利成
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */

    public void findSandingList() throws Exception{
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                HttpServletRequest req = getRequest();
                String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
                ZbglSanding entity = (ZbglSanding) JSONUtil.read(searchJson, ZbglSanding.class);
                SearchEntity<ZbglSanding> searchEntity = new SearchEntity<ZbglSanding>(entity, getStart(), getLimit(), getOrders());
                map = this.manager.findSandingList(searchEntity).extjsStore();
            } catch (Exception e) {
                ExceptionUtil.process(e, logger, map);
            } finally {
                JSONUtil.write(this.getResponse(), map);
            }
    }
    
    /**
     * <li>说明：获取机车出入段台账关联的机车上砂实体
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getEntityByAccountIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String trainAccessAccountIDX = StringUtil.nvlTrim(req.getParameter("trainAccessAccountIDX"), "");
            ZbglSanding sanding = this.manager.getEntityByAccountIDX(trainAccessAccountIDX);
            map.put("entity", sanding);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
