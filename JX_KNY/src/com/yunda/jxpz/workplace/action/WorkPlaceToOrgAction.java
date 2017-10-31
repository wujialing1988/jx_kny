package com.yunda.jxpz.workplace.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jxpz.workplace.entity.WorkPlaceToOrg;
import com.yunda.jxpz.workplace.manager.WorkPlaceToOrgManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 站点标示关联组织机构控制器
 * <li>创建人：张凡
 * <li>创建日期：2014-4-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.0.1
 */
public class WorkPlaceToOrgAction extends JXBaseAction<WorkPlaceToOrg, WorkPlaceToOrg, WorkPlaceToOrgManager> {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * <li>方法说明：新增站点对应机构 
     * <li>方法名称：siteAddOrg
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-1-14 下午03:10:22
     * <li>修改人：
     * <li>修改内容：
     */
    public void siteAddOrg() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        
        try {
            WorkPlaceToOrg[] workPlaceToOrg = (WorkPlaceToOrg[])JSONUtil.read(getRequest(), WorkPlaceToOrg[].class);
            String result = this.manager.saveWorkPlaceToOrg(workPlaceToOrg);
            if(result == null){
                map.put("success", true);
            }else{
                map.put("errMsg", "机构“" + result + "”已存在，请取消后再保存！");
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }     
    }
    
    /**
     * <li>说明：siteID有没配置站点标示
     * <li>创建人：程锐
     * <li>创建日期：2015-8-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void hasWorkplaceBySiteID() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String siteID = getRequest().getParameter("siteID");
            List<WorkPlaceToOrg> list = manager.getListBySiteID(siteID);
            map.put("hasWorkplaceBySiteID", list != null && list.size() > 0);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
