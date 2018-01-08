package com.yunda.freight.zb.gztp.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.zb.gztp.entity.Gztp;
import com.yunda.freight.zb.gztp.manager.GztpManager;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeUse;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检_故障提票Action
 * <li>创建人：何东
 * <li>创建日期：2017-04-12 10:48:28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class GztpAction extends JXBaseAction<Gztp, Gztp, GztpManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：web端保存故障登记
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveGztps() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String gztpData = StringUtil.nvlTrim(getRequest().getParameter("gztpData"), Constants.EMPTY_JSON_OBJECT);
            Gztp gztp = (Gztp) JSONUtil.read(gztpData, Gztp.class);
            MatTypeUse[] matUses = (MatTypeUse[]) JSONUtil.read(getRequest(), MatTypeUse[].class);
            this.manager.saveGztps(gztp, matUses);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：货车故障提票分类统计
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */ 
    public void findGzFlStatisticsHC() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            map.put("root",this. manager.findGzFlStatisticsHC());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
    
    
    /**
     * <li>说明：客车故障提票分类统计
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */ 
    public void findGzFlStatisticsKC() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            map.put("root",this. manager.findGzFlStatisticsKC());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
    
    
}
