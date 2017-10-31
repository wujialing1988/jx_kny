package com.yunda.jxpz.workplace.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jxpz.workplace.entity.WorkPlaceToOrg;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 站点标示关联组织机构业务类
 * <li>创建人：张凡
 * <li>创建日期：2014-4-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.0.1
 */
@Service(value="workPlaceToOrgManager")
public class WorkPlaceToOrgManager extends JXBaseManager<WorkPlaceToOrg, WorkPlaceToOrg> {
    
    /* 人员查询业务类 */
    private OmEmployeeManager omEmployeeManager;
    /* 站点位置业务类 */
    private WorkPlaceManager workPlaceManager;
    /* 机构查询业务类 */
    private OmOrganizationManager omOrganizationManager;

    public OmEmployeeManager getOmEmployeeManager() {
        return omEmployeeManager;
    }
    
    public void setOmEmployeeManager(OmEmployeeManager omEmployeeManager) {
        this.omEmployeeManager = omEmployeeManager;
    }

    public WorkPlaceManager getWorkPlaceManager() {
        return workPlaceManager;
    }
    
    public void setWorkPlaceManager(WorkPlaceManager workPlaceManager) {
        this.workPlaceManager = workPlaceManager;
    }
    
    public OmOrganizationManager getOmOrganizationManager() {
        return omOrganizationManager;
    }
    
    public void setOmOrganizationManager(OmOrganizationManager omOrganizationManager) {
        this.omOrganizationManager = omOrganizationManager;
    }
    
    /**
     * <li>方法说明：物理删除站点对应机构 
     * <li>方法名称：logicDelete
     * <li>@param ids 主键数组
     * <li>创建人：张凡
     * <li>创建时间：2014-1-14 下午04:12:48
     * <li>修改人：
     * <li>修改内容：
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        
        StringBuilder sql = new StringBuilder("delete from JXPZ_WorkPlace_TO_ORG where idx in(");
        for (Serializable id : ids) {
            sql.append("'").append(id).append("',");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        
        daoUtils.executeSql(sql.toString());
    }
    
    /**
     * <li>方法说明：新增站点对应机构 
     * <li>方法名称：saveWorkPlaceToOrg
     * <li>@param workPlaceToOrg 站点对应机构实体数组
     * <li>@return String
     * <li>创建人：张凡
     * <li>创建时间：2014-1-14 下午04:12:48
     * <li>修改人：
     * <li>修改内容：
     */
    public String saveWorkPlaceToOrg(WorkPlaceToOrg[] workPlaceToOrg){
        List<WorkPlaceToOrg> list = new ArrayList<WorkPlaceToOrg>();
        StringBuilder sb = new StringBuilder();
        for(WorkPlaceToOrg org : workPlaceToOrg){
            sb.append("'").append(org.getOrgid()).append("',");
            list.add(org);
        }
        sb.deleteCharAt(sb.length() -1);
        String checkResult = checkHasOrg(sb.toString());
        if(!checkResult.equals("")){
            return checkResult;
        }
        
        daoUtils.bluckInsert(list);
        return null;
    }
    
    /**
     * <li>说明： 获取当前siteID关联的站点标识组织列表
     * <li>创建人：程锐
     * <li>创建日期：2015-8-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param siteID 站点ID
     * @return 当前siteID关联的站点标识组织列表
     */
    @SuppressWarnings("unchecked")
    public List<WorkPlaceToOrg> getListBySiteID(String siteID) {
        String hql = "from WorkPlaceToOrg where workPlaceCode = '" + siteID + "'";
        return daoUtils.find(hql);
    }
    
    /**
     * <li>方法说明：检查是否有已存在的机构，返回存在的机构名称 
     * <li>方法名称：checkHasOrg
     * <li>@param orgids
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-1-15 下午07:42:58
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private String checkHasOrg(String orgids){
        
        String sql = "select to_char(wm_concat(orgname)) from JXPZ_WorkPlace_TO_ORG where orgid in ("  + orgids+ ")";
        
        List list = daoUtils.executeSqlQuery(sql);
        
        String result = "";
        for(int i = 0; i < list.size(); i++){
            result  = StringUtil.nvl(list.get(i));
        }
        
        return result;
    }
    /**
     * <li>方法说明：当前用户获取站点标识 
     * <li>方法名称：getWorkPlaceCodeByCurrentUser
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-1-16 上午10:18:53
     * <li>修改人：
     * <li>修改内容：
     */
    public String getWorkPlaceCodeByCurrentUser(){
        AcOperator acoperator = SystemContext.getAcOperator();
        if(acoperator != null){           
            OmEmployee omemployee = omEmployeeManager.findByOperator(acoperator.getOperatorid());
            if (omemployee == null) {
				return "";
			}
            WorkPlaceToOrg workPlaceOrg = getWorkPlaceToOrgByCurrentUserOrgId(omemployee.getOrgid());
            if(workPlaceOrg == null){
                return "";
            }
            return workPlaceOrg.getWorkPlaceCode();
        }
        return "";
    }
    /**
     * <li>说明：当前用户获取站点标识名称
     * <li>创建人：程锐
     * <li>创建日期：2015-3-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param siteID 站场标示ID 
     * @return 站点标识名称
     */
    public String getWorkPlaceNameByCurrentUser(String siteID) {
        if (!StringUtil.isNullOrBlank(siteID)) {
            return workPlaceManager.getModelById(siteID) != null ? workPlaceManager.getModelById(siteID).getWorkPlaceName() : "";
        }
        AcOperator acoperator = SystemContext.getAcOperator();
        if (acoperator != null) {
            OmEmployee omemployee = omEmployeeManager.findByOperator(acoperator.getOperatorid());
            if (omemployee == null) {
                return "";
            }
            WorkPlaceToOrg workPlaceOrg = getWorkPlaceToOrgByCurrentUserOrgId(omemployee.getOrgid());
            if (workPlaceOrg != null && !StringUtil.isNullOrBlank(workPlaceOrg.getWorkPlaceCode())
                    && workPlaceManager.getModelById(workPlaceOrg.getWorkPlaceCode()) != null) {
                return workPlaceManager.getModelById(workPlaceOrg.getWorkPlaceCode()).getWorkPlaceName();
            }
        }
        return "";
    }
    /**
	 * <li>方法说明：根据当前用户所属机构ID查询所属段对应站点
	 * <li>方法名称：getWorkPlaceToOrgByCurrentUserOrgId
	 * <li>
	 * @param orgid
	 * <li>
	 * @return
	 * <li>return: WorkPlaceToOrg
	 * <li>创建人：张凡
	 * <li>创建时间：2014-1-16 上午10:18:11
	 * <li>修改人：
	 * <li>修改内容：
	 */
    public  WorkPlaceToOrg getWorkPlaceToOrgByCurrentUserOrgId(Long orgid){
      	WorkPlaceToOrg org = null;
        //查询出登录人员对应ORGID的所有上级ORGID，组成数组
        String sql = "select orgid  from om_organization start with orgid = '"+orgid+"' connect by prior parentorgid = orgid ";
        List list = daoUtils.executeSqlQuery(sql);
         
        if(list != null && list.size()!=0){
            for(int i = 0; i < list.size(); i++){
            	if(list.get(i)!=null){
            		Long orgidTmp = Long.valueOf(list.get(i).toString());
            		org = getWorkPlaceOrgBySegmentOrgId(orgidTmp);
            		if(org!=null)
            			break;
            	}
            }
        }
        return org;
    }
    
    /**
     * <li>方法说明：根据段级机构ID查询实体
     * <li>方法名称：getWorkPlaceOrgBySegmentOrgId
     * <li>@param orgid
     * <li>@return
     * <li>return: WorkPlaceToOrg
     * <li>创建人：张凡
     * <li>创建时间：2014-1-16 上午10:13:46
     * <li>修改人：
     * <li>修改内容：
     */
    private WorkPlaceToOrg getWorkPlaceOrgBySegmentOrgId(Long orgid){
        
        String hql = "from WorkPlaceToOrg where orgid = ?";
        WorkPlaceToOrg org = (WorkPlaceToOrg)daoUtils.findSingle(hql, orgid);
        return org;
    }
    
}
