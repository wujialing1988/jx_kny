package com.yunda.freight.jx.classwarning.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.jx.classwarning.entity.RepairClassWarning;
import com.yunda.freight.jx.classwarning.entity.RepairClassWarningBean;
import com.yunda.freight.jx.classwarning.manager.RepairClassWarningManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 修程预警Action
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-05-04 15:14:11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class RepairClassWarningAction extends JXBaseAction<RepairClassWarning, RepairClassWarning, RepairClassWarningManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    

    /**
     * <li>说明：查询客车修程预警
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findKCRepairClassWarningList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            RepairClassWarningBean objEntity = (RepairClassWarningBean)JSONUtil.read(searchJson, RepairClassWarningBean.class);
            SearchEntity<RepairClassWarningBean> searchEntity = new SearchEntity<RepairClassWarningBean>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findKCRepairClassWarningList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：查询货车修程预警
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findHCRepairClassWarningList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            RepairClassWarningBean objEntity = (RepairClassWarningBean)JSONUtil.read(searchJson, RepairClassWarningBean.class);
            SearchEntity<RepairClassWarningBean> searchEntity = new SearchEntity<RepairClassWarningBean>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findHCRepairClassWarningList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    
}
