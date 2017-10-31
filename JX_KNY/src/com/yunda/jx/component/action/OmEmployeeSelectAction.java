package com.yunda.jx.component.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 人员选择控件控制器
 * <li>创建人：程锐
 * <li>创建日期：2012-9-2
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class OmEmployeeSelectAction extends JXBaseAction<OmEmployee, OmEmployee, OmEmployeeSelectManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    private static final String ORGID = "orgid";
    
    private static final String ORGSEQ = "orgseq";
    
    private static final String TYPE = "type";
    
	/** 人员业务类：omEmployeeManager */
	@Resource(name="omEmployeeManager")
	private IOmEmployeeManager omEmployeeManager;
    
    /**
     * <li>说明：分页查询人员列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
            OmOrganizationManager omOrganizationManager = (OmOrganizationManager) getManager("omOrganizationManager");
            OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
            
            String orgId = StringUtil.nvlTrim(req.getParameter(ORGID), String.valueOf(org.getOrgid()));
            String orgseq = req.getParameter(ORGSEQ);
            if (orgseq == null || "".equals(orgseq.trim())) {
                // 张凡修改：TeamEmployeeSelect.js调用此查询，没有orgseq需查询一次
                OmOrganization orgx = omOrganizationManager.getModelById(orgId);
                if (orgx != null) {
                    orgseq = orgx.getOrgseq();
                }
            }
            String empname = StringUtil.nvlTrim(req.getParameter("emp"), req.getParameter("query"));
            OmEmployee objEntity = new OmEmployee();
            if (null != orgId) {
                objEntity.setOrgid(Long.parseLong(orgId));
            }
            SearchEntity<OmEmployee> searchEntity = new SearchEntity<OmEmployee>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, empname, orgseq).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询可添加为库管员的人员信息
     * <li>创建人：王治龙
     * <li>创建日期：2012-9-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageListForKeeper() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String orgId = StringUtil.nvlTrim(req.getParameter(ORGID), "0");
            String wareHouseIDX = StringUtil.nvlTrim(req.getParameter("wareHouseIDX"));
            String keeper = StringUtil.nvlTrim(req.getParameter("keeper"), "0");
            String empname = StringUtil.nvlTrim(req.getParameter("emp"), "");
            if (!"0".equals(orgId)) {
                entity.setOrgid(Long.parseLong(orgId));
            }
            
            SearchEntity<OmEmployee> searchEntity = new SearchEntity<OmEmployee>(entity, getStart(), getLimit(), null);
            map = this.manager.findPageForKeeperList(searchEntity, Long.parseLong(orgId), empname, keeper, wareHouseIDX).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取工位对应车间（班组）下的人员列表
     * <li>创建人：程锐
     * <li>创建日期：2012-12-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageListForWorkStation() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            OmOrganization org = SystemContext.getOmOrganization();
            String orgseq = StringUtil.nvlTrim(req.getParameter(ORGSEQ), org.getOrgseq());
            String empname = StringUtil.nvlTrim(req.getParameter("empname"), "");
            String workStationIDX = StringUtil.nvlTrim(req.getParameter("workStationIDX"), "");
            SearchEntity<OmEmployee> searchEntity = new SearchEntity<OmEmployee>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.pageListForWorkStation(searchEntity, empname, orgseq, workStationIDX).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>方法名称：findListForWorkStationEmpSelect
     * <li>方法说明：查询工位下人员，用于工位人员选择
     * @throws JsonMappingException
     * @throws IOException
     *             <li>return: void
     *             <li>创建人：张凡
     *             <li>创建时间：2013-1-6 下午03:35:05
     *             <li>修改人：
     *             <li>修改内容：
     */
    public void findListForWorkStationEmpSelect() throws JsonMappingException, IOException {
        
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            HttpServletRequest req = getRequest();
            String cardid = req.getParameter("cardId");
            String orgseq = req.getParameter(ORGSEQ);
            boolean reDispatcher = req.getParameter(TYPE) != null;
            map = this.manager.findDispatcherPageList(cardid, orgseq, reDispatcher, getStart(), getLimit(), getOrders()).extjsStore();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：列检计划车辆派工
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findListForZbPlanRecordEmpSelect() throws JsonMappingException, IOException {
        
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            HttpServletRequest req = getRequest();
            String cardid = req.getParameter("cardId");
            String orgseq = req.getParameter(ORGSEQ);
            boolean reDispatcher = req.getParameter(TYPE) != null;
            map = this.manager.findListForZbPlanRecordEmpSelect(cardid, orgseq, reDispatcher, getStart(), getLimit(), getOrders()).extjsStore();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    
    
    /**
     * <li>说明：查询工位下人员及工种信息，用于工位人员选择
     * <li>创建人：程锐
     * <li>创建日期：2013-3-13
     * <li>修改人： 张凡
     * <li>修改日期：2013-07-02
     * <li>修改内容：修改查询
     * @throws Exception
     */
    public void findTpDataList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String faultIdx = req.getParameter("faultIdx");
            String orgid = req.getParameter(ORGID);
            boolean reDispatcher = req.getParameter(TYPE) != null;
            map = this.manager.findTpDataList(faultIdx, orgid, reDispatcher, getStart(), getLimit(), getOrders()).extjsStore();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询技术改造工长派工列表【质量】
     * <li>创建人：王治龙
     * <li>创建日期：2013-9-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findJsgzPersonList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String techReformCaseIdx = req.getParameter("techReformCaseIdx");
            String orgseq = req.getParameter(ORGSEQ);
            boolean reDispatcher = req.getParameter(TYPE) != null;
            map = this.manager.findJsgzPersonList(techReformCaseIdx, orgseq, reDispatcher, 0, 500, getOrders()).extjsStore();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询普查整治工长派工列表【质量
     * <li>创建人：程梅
     * <li>创建日期：2013-8-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findPczzPersonList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String planCaseIDX = req.getParameter("planCaseIDX");
            String orgseq = req.getParameter(ORGSEQ);
            boolean reDispatcher = req.getParameter(TYPE) != null;
            map = this.manager.findPczzPersonList(planCaseIDX, orgseq, reDispatcher, 0, 500, getOrders()).extjsStore();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询多个机构下的人员
     * <li>创建人：程梅
     * <li>创建日期：2014-12-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void pageListForPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String orgId = req.getParameter(ORGID); // 机构id
            String orgidStr = req.getParameter("orgidStr"); // 机构id字符串
            String empname = StringUtil.nvlTrim(req.getParameter("emp"), "");
            SearchEntity<OmEmployee> searchEntity = new SearchEntity<OmEmployee>(entity, getStart(), getLimit(), getOrders());
            String orgseq = "";
            if (!StringUtil.isNullOrBlank(orgidStr)) {
                String[] orgStr = orgidStr.split(";");
                List list = new ArrayList<OmEmployee>();
                for (int i = 0; i < orgStr.length; i++) {
                    OmOrganization orgx = ((OmOrganizationManager) getManager("omOrganizationManager")).getModelById(orgStr[i]);
                    if (orgx != null) {
                        orgseq = orgx.getOrgseq();
                    }
                    Page<com.yunda.jx.component.entity.OmEmployee> page = this.manager.findPageList(searchEntity, empname, orgseq);
                    List pagelist = page.getList();
                    list.addAll(pagelist);
                }
                map = new Page(list).extjsStore();
            } else {
                OmOrganization orgx = ((OmOrganizationManager) getManager("omOrganizationManager")).getModelById(orgId);
                if (orgx != null) {
                    orgseq = orgx.getOrgseq();
                }
                map = this.manager.findPageList(searchEntity, empname, orgseq).extjsStore();
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：根据部门序列查询人员列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findEmpListByOrgId() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String orgseq = req.getParameter(ORGSEQ); // 机构序列
            SearchEntity<OmEmployee> searchEntity = new SearchEntity<OmEmployee>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findEmpListByOrgId(searchEntity , orgseq).extjsStore();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：查询配件周转常用部门下所有人员列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findEmpListByAccountOrgId() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            OmEmployee emp = (OmEmployee)JSONUtil.read(searchJson, OmEmployee.class);
            SearchEntity<OmEmployee> searchEntity = new SearchEntity<OmEmployee>(emp, getStart(), getLimit(), getOrders());
            map = this.manager.findEmpListByAccountOrgId(searchEntity).extjsStore();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * 
     * <li>说明：根据工号查询员工信息
     * <li>创建人：林欢
     * <li>创建日期：2016-7-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findEmployeeByCardID() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String fromPersonId = req.getParameter("fromPersonId");
            if (StringUtils.isNotBlank(fromPersonId)) {
                OmEmployee emp = this.omEmployeeManager.findByCode(fromPersonId);
                if (emp != null) {
                    map.put("success", true);
                    map.put("fromPersonName", emp.getEmpname());
                }else{
                    map.put("success", false);
                    map.put("fromPersonName", "");
                }
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
