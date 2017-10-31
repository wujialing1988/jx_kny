package com.yunda.zb.trainhandover.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.zb.trainhandover.entity.ZbglHoModelItem;
import com.yunda.zb.trainhandover.manager.ZbglHoModelItemManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglHoModelItem控制器, 机车交接项模板—交接项
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
public class ZbglHoModelItemAction extends JXBaseAction<ZbglHoModelItem, ZbglHoModelItem, ZbglHoModelItemManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 业务字典选择控件业务类 */
    @Resource
    private EosDictEntrySelectManager eosDictEntrySelectManager;
    
    /**
     * <li>说明：交接项树
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getTree() throws Exception {
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), "0");
        List<HashMap<String, Object>> children = manager.getTree(parentIDX);
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>说明：获取机车交接项状态数据字典列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void findStatusByDicTypeID() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("entity", eosDictEntrySelectManager.findByDicTypeID("KNY_HC_JJX"));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：逻辑删除机车交接项模板及其所有子节点及其关联的机车交接情况模板
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void deleteModel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.deleteModel(ids);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findItemList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String parentName = StringUtil.nvlTrim(getRequest().getParameter("parentName"), "");
            List<ZbglHoModelItem> list = this.manager.findItemList(parentName);
            map.put("list", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
