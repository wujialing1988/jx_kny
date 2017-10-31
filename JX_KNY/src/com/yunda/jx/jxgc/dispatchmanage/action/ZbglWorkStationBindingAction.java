package com.yunda.jx.jxgc.dispatchmanage.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStationMobileDTO;
import com.yunda.jx.jxgc.dispatchmanage.entity.ZbglWorkStationBinding;
import com.yunda.jx.jxgc.dispatchmanage.manager.ZbglWorkStationBindingManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglWorkStationBindingAction控制器, 整备工位与人员关联
 * <li>创建人：林欢
 * <li>创建日期：2016-07-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbglWorkStationBindingAction extends JXBaseAction<ZbglWorkStationBinding, ZbglWorkStationBinding, ZbglWorkStationBindingManager>{
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：根据操作员ID获取人员绑定的工位信息
     * <li>创建人：林欢
     * <li>创建日期：2016-07-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getBindingWorkStationByOperatIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String operatIDX = req.getParameter("operatIDX");//登陆人idx
            List<WorkStationMobileDTO> list = this.manager.getBindingWorkStationByOperatIDX(operatIDX);
            map = new Page(list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据操作员ID获取人员未绑定的工位信息
     * <li>创建人：林欢
     * <li>创建日期：2016-07-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getUnBindingWorkStationByOperatIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String operatIDX = req.getParameter("operatIDX");//登陆人idx
            List<WorkStationMobileDTO> list = this.manager.getUnBindingWorkStationByOperatIDX(operatIDX);
            map = new Page(list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：绑定工位
     * <li>创建人：林欢
     * <li>创建日期：2016-07-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void bindWorkStation() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String operatIDX = req.getParameter("operatIDX");//登陆人idx
            String workStationIDX = req.getParameter("workStationIDX");//工位idx
            this.manager.bindWorkStation(operatIDX,workStationIDX);
            map.put("success", true);
            map.put("errMsg", "操作成功");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：解除绑定工位
     * <li>创建人：林欢
     * <li>创建日期：2016-07-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void unBindWorkStation() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String workStationBindingIDX = req.getParameter("workStationBindingIDX");//工位与人员关联表idx
            this.manager.unBindWorkStation(workStationBindingIDX);
            map.put("success", true);
            map.put("errMsg", "操作成功");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}