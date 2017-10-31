
package com.yunda.jx.component.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jcgy.entity.JgyjcDeport;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 组织机构选择控件控制器类
 * <li>创建人：程锐
 * <li>创建日期：2012-9-2
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class OmOrganizationSelectAction extends
    JXBaseAction<OmOrganization, OmOrganization, OmOrganizationSelectManager> {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /** 组织机构业务类：OmOrganizationManager */
    private OmOrganizationManager omOrganizationManager;
    
    private static final String PARENTIDX = "parentIDX";
    
    private static final String QUERYHQL = "queryHql";
    
    private static final String TREAM = "tream";
    
    /**
     * <li>说明：查询组织列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception {
        String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
        OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter(PARENTIDX), String.valueOf(org.getOrgid()));
        List<Map> children = manager.findOrgTree(parentIDX);
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>说明：查询组织列表
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void orgTree() throws Exception {
        String parentIDX =
            StringUtil.nvlTrim(getRequest().getParameter(PARENTIDX), String.valueOf(SystemContext.getOmOrganization()
                .getOrgid()));
        List<Map> children = manager.findOrgTree(parentIDX);
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>说明：自定义hql查询组织列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-23
     * <li>修改人：张凡
     * <li>修改日期：2012-11-13
     * <li>修改内容：当queryHql为[degree]oversea时，则树叶子节点为orgdegree=oversea的段级
     *              <br>
     *              如果为[degree]plant，则叶子节点为车间那一级
     * 
     * @param
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void customTree() throws Exception {
        String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
        OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
        List<Map> children =null;
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter(PARENTIDX), String.valueOf(org.getOrgid()));
        String forDictHql = StringUtil.nvlTrim(getRequest().getParameter("forDictHql")); //获取标识,是否是根据数据字典查询组织机构树
        String queryHql = StringUtil.nvlTrim(getRequest().getParameter(QUERYHQL), "");
        String fullNameDegree = getRequest().getParameter("fullNameDegree");
        String isChecked = StringUtil.nvlTrim(getRequest().getParameter("isChecked"),null);
        
        //不根据数据字典查询
        if("".equals(forDictHql)){
	        if(queryHql.indexOf("[degree]") == 0){
	            //queryHql带[degree]，到查询到某级
	            children = manager.findSomeLevelOrgTree(parentIDX, queryHql.substring(8), fullNameDegree,isChecked);
	        }else{
	            children = manager.findOrgTree(parentIDX, queryHql, fullNameDegree,isChecked);
	        }
        } 
        //根据数据字典查询
        else {
        	//根据参数决定,是否只查询符合条件的第一层节点
        	if("[getNextLevels]".equals(forDictHql)){
        		//继续向下层节点检索
        		children = manager.findOrgTreeForDict(parentIDX, queryHql,true,isChecked);
        	} else {
        		//只检索当前层次节点
        		children = manager.findOrgTreeForDict(parentIDX, queryHql,false, isChecked);
        	}
        }
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>方法名称：getCurrentTeam
     * <li>方法说明：获取当前登陆人班组信息
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-3-30 上午11:17:55
     * <li>修改人：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void getCurrentTeam() throws JsonMappingException, IOException{
        Map<String, OmOrganization> map = new HashMap<String, OmOrganization>();
        HttpSession session = getRequest().getSession();
        if(session != null && session.getAttribute(TREAM) != null){
        	//session会话中，org存储的就是最下面一级
            OmOrganization o = (OmOrganization)session.getAttribute("org");
            map.put("team", o);
        }
        JSONUtil.write(getResponse(), map);
    }
    /**
     * 
     * <li>说明：查询局列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void bureauTree() throws Exception {        
        String queryHql = StringUtil.nvlTrim(getRequest().getParameter(QUERYHQL), "");
        List<HashMap> children = null;
        children = manager.bureauTree(queryHql, true);        
        JSONUtil.write(getResponse(), children);
    }
    /**
     * 
     * <li>说明：查询段列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void deportTree() throws Exception {   
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter(PARENTIDX));
        String queryHql = StringUtil.nvlTrim(getRequest().getParameter(QUERYHQL), "");
        List<HashMap> children = null;
        children = manager.deportTree(parentIDX, queryHql);        
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>方法说明：查询段，但是先查询局
     * <li>方法名称：deportTree2
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-8-30 下午03:45:26
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void deportTree2() throws JsonMappingException, IOException{
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter(PARENTIDX));
        String queryHql = StringUtil.nvlTrim(getRequest().getParameter(QUERYHQL), "");
        List<HashMap> children = null;
        if(parentIDX.equals("0")){
            children = manager.bureauTree(queryHql, false);
        }else{
            children = manager.deportTree(parentIDX, queryHql);
        }
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>方法名称：getUpOrg
     * <li>方法说明：获取到上某级机构 
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-5-6 下午07:59:52
     * <li>修改人：
     * <li>修改内容：
     */
    public void getUpOrg() throws Exception{
        HttpSession sessionx = getRequest().getSession();
        if(sessionx != null && sessionx.getAttribute(TREAM) != null){
            OmOrganization o = this.manager.getUpOrgid(Long.valueOf(sessionx.getAttribute(TREAM).toString()), getRequest().getParameter("level"));
            JSONUtil.write(getResponse(), o);
        }
    }
    
    /**
     * <li>方法名称：findTeamOfDept
     * <li>方法说明：获取登陆人某部下的所有班组 
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-5-9 下午10:14:39
     * <li>修改人：
     * <li>修改内容：
     */
    public void findTeamOfDept() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        HttpSession session = getRequest().getSession();
        OmOrganization org = (OmOrganization)session.getAttribute("org");
        if(org != null){
            String orgid = this.manager.getTopOrgid(org.getOrgid(), getRequest().getParameter("level"));
            List<OmOrganization> list = this.manager.getTeamOfDept(orgid);
            for(int i = list.size()-1; i >= 0; i--){
                OmOrganization o = list.get(i);
                if(!TREAM.equals(o.getOrgdegree())){
                    list.remove(o);
                }
            }
            map.put("id", "team");
            map.put("root", list);
            map.put("totalProperty", list.size());
        }
        JSONUtil.write(this.getResponse(), map);
        
    }
    /**
     * 
     * <li>说明：根据登录人所属部门id获取与承修部门数据字典匹配的机构实体对象
     * <li>创建人：程锐
     * <li>创建日期：2013-5-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @throws Exception
     */
    public void getUnderTakeByEmp() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String plantId = StringUtil.nvlTrim(getRequest().getParameter("plantId"), "");
            OmOrganization omOrganization = manager.getUnderTakeByEmp(plantId);
            map.put("success", true);
            map.put("entity", omOrganization);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }

    /**
     * 
     * <li>说明：查询段机构树列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void querySegmentOrgTreeList() throws Exception {
    	Map<String, Object> map = new HashMap<String,Object>();
    	List<Map> list = new ArrayList<Map>();
		try {
			Long orgid = Long.valueOf(getRequest().getParameter("orgid"));
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			list = this.manager.querySegmentOrgTreeList(orgid, operatorid);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
    }
    /**
     * 
     * <li>说明：查询作业班组列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void queryTeamList() throws Exception {
    	Map<String, Object> map = new HashMap<String,Object>();
    	List<Map> list = new ArrayList<Map>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			list = this.manager.queryTeamList(operatorid);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
    }
    
    /**
     * <li>说明：根据站点标识维护查询当前站场维护的班组信息（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-8-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void queryTeamListBySite() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        List list = null;
        try {
            list = this.manager.queryTeamListBySite();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), list);
        }
    }
    /**
     * 
     * <li>说明：配属段列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getDeportList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            JgyjcDeport deport = (JgyjcDeport)JSONUtil.read(searchJson, JgyjcDeport.class);
            SearchEntity<JgyjcDeport> searchEntity = new SearchEntity<JgyjcDeport>(deport, getStart(), getLimit(), getOrders());
            map = this.manager.getDeportList(searchEntity).extjsStore();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    } 
    public OmOrganizationManager getOmOrganizationManager() {
        return omOrganizationManager;
    }
    
    public void setOmOrganizationManager(OmOrganizationManager omOrganizationManager) {
        this.omOrganizationManager = omOrganizationManager;
    }
    
}
