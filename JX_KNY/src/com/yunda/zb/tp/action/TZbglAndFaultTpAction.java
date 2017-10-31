package com.yunda.zb.tp.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.tp.entity.TZbglAndFaultTp;
import com.yunda.zb.tp.manager.TZbglAndFaultTpManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TZbglAndFaultTp控制器, 整备修程修提票综合查询
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class TZbglAndFaultTpAction extends JXBaseAction<TZbglAndFaultTp, TZbglAndFaultTp, TZbglAndFaultTpManager> {
    
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：查询提票综合统计（根据条件动态显示字段）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findTpPageListByParm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<TZbglAndFaultTp> list = new LinkedList<TZbglAndFaultTp>();
        try {
            String fields = StringUtil.nvlTrim(getRequest().getParameter("fields"), "");
            list = getTpZhTjMapByFlag(1,null,fields);
            map = new Page<TZbglAndFaultTp>(list.size(), list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询提票综合统计明细(根据所选行)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findTpDeteailPageListByParm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<TZbglAndFaultTp> list = new LinkedList<TZbglAndFaultTp>();
        try {
            list = getTpZhTjMapByFlag(2,null,"");
            map = new Page<TZbglAndFaultTp>(list.size(), list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据flag标示返回对应提票List flag = 1 表示主表 flag = 2表示选择当前主表信息的明细
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param flag 标示，flag = 1 表示主表 flag = 2表示选择当前主表信息的明细
     * @param z ZbglTp对象，里面其实就是封装了查询参数
     * @param f 传入的字段，用于前台展示
     * @return List<ZbglTp> 结果集
     * @throws Exception
     */
    private List<TZbglAndFaultTp> getTpZhTjMapByFlag(Integer flag,TZbglAndFaultTp z,String f) throws Exception {
        List<TZbglAndFaultTp> list = new LinkedList<TZbglAndFaultTp>();
        HttpServletRequest req = getRequest();
        if (z == null) {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            entity = (TZbglAndFaultTp) JSONUtil.read(searchJson, entitySearch.getClass());
        }else {
            entity = z;
        }
        SearchEntity<TZbglAndFaultTp> searchEntity = new SearchEntity<TZbglAndFaultTp>(entity, 0, 999999999, getOrders());
        if (flag == 1) {
            //传入分组字段，也是返回字段
            list = this.manager.findTpPageListByParm(searchEntity,f);
        }else if (flag == 2) {
            list = this.manager.findTpDeteailPageListByParm(searchEntity);
        }
        return list;
    }
    
    
}
