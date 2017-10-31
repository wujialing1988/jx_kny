package com.yunda.zb.tp.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.tp.entity.ZbglTpException;
import com.yunda.zb.tp.manager.ZbglTpExceptionManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpException控制器, 提票例外放行
 * <li>创建人：程锐
 * <li>创建日期：2015-03-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglTpExceptionAction extends JXBaseAction<ZbglTpException, ZbglTpException, ZbglTpExceptionManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：提票例外放行
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveForLwfx() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ZbglTpException[] tpExceptionAry = JSONUtil.read(getRequest().getParameter("tpExceptionAry"), ZbglTpException[].class);
            this.manager.saveForLwfx(tpExceptionAry);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取提票例外放行列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findTpExceptionPageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            entity = (ZbglTpException) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<ZbglTpException> searchEntity = new SearchEntity<ZbglTpException>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findTpExceptionPageList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：取消提票例外放行
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateForCancel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ZbglTpException[] tpExceptionAry = JSONUtil.read(getRequest().getParameter("tpExceptionAry"), ZbglTpException[].class);
            this.manager.updateForCancel(tpExceptionAry);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
