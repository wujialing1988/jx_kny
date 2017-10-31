package com.yunda.freight.base.classOrganization.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.base.classOrganization.entity.ClassOrganization;
import com.yunda.freight.base.classOrganization.entity.ClassOrganizationUser;
import com.yunda.freight.base.classOrganization.entity.ClassOrganizationUserVo;
import com.yunda.freight.base.classOrganization.manager.ClassOrganizationManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 班次班组维护Action
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-13 16:44:12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class ClassOrganizationAction extends JXBaseAction<ClassOrganization, ClassOrganization, ClassOrganizationManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：保存组织
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveOrganizations() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 保存组织
            ClassOrganization[] itemList = (ClassOrganization[])JSONUtil.read(getRequest(), ClassOrganization[].class);
            this.manager.saveOrganizations(itemList);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：查询管理班组下的人员
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findOrganizationUsers() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 班次班组
            String classOrgIdx = getRequest().getParameter("classOrgIdx");
            // 班组
            String orgIdx = getRequest().getParameter("orgIdx");
            List<ClassOrganizationUserVo> models = this.manager.findOrganizationUsers(classOrgIdx,orgIdx);
            map.put("root", models);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存分队信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveOrganizationUsers() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ClassOrganizationUser[] datas = (ClassOrganizationUser[]) JSONUtil.read(getRequest(), ClassOrganizationUser[].class);
            this.manager.saveOrganizationUsers(datas);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}
