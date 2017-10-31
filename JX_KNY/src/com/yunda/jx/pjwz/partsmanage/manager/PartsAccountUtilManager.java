package com.yunda.jx.pjwz.partsmanage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件信息通用业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-6-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsAccountUtilManager")
public class PartsAccountUtilManager extends JXBaseManager<PartsAccount, PartsAccount>  {
	@Autowired
	private IOmOrganizationManager orgManager;
	/**
	 * 
	 * <li>说明：获取树型列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param id 上级id
	 * @param orgid 自定义的组织机构根节点id
	 * @return 树型列表
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> getTree(String id, String orgid) {
		boolean isLeaf = true;
	    String idToken = null; //ID标识
	    List<HashMap> children = new ArrayList<HashMap>();
	    //根节点下显示列表
	    if("ROOT_0".equals(id)) {
	    	children = getRootList(children);
	    } 
	    //责任部门-机构
	    else if (id.contains("Z_")) {
	    	idToken = "Z_";
			String orgseqs = getManageDeptOrgseqs(PartsAccount.MANAGE_DEPT_TYPE_ORG);
			//责任机构根节点
			if (!StringUtil.isNullOrBlank(orgid) && id.equals("Z_")) {
	            children = getRootOrgList(orgid, idToken, orgseqs, isLeaf, children);
			} 
			//责任机构下级节点
			else {
				children = getChildOrgList(id, isLeaf, orgseqs, idToken, children);					
			}		
		} 
		//管理库房
	    else if (id.contains("K_")) {
	    	children = getDutyWHList(idToken, isLeaf, children);
		}
	    return children;
	}
	/**
	 * 
	 * <li>说明：获取根节点显示列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param children
	 * @return 根节点显示列表
	 */
	@SuppressWarnings("unchecked")
	private List<HashMap> getRootList(List<HashMap> children) {
		HashMap nodeMap = new HashMap();
    	nodeMap.put("id", "K_");  //管理库房
        nodeMap.put("text", "管理库房");
        nodeMap.put("leaf", false);
        children.add(nodeMap);
        nodeMap = new HashMap();
        nodeMap.put("id", "Z_");  //责任部门
        nodeMap.put("text", "责任部门");
        nodeMap.put("leaf", false);
        children.add(nodeMap);
        return children;
	}
	/**
	 * 
	 * <li>说明：获取责任机构根节点列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgid 
	 * @param idToken
	 * @param orgseqs
	 * @param isLeaf
	 * @return 责任机构根节点列表
	 */
	@SuppressWarnings("unchecked")
	private List<HashMap> getRootOrgList(String orgid, String idToken, String orgseqs, boolean isLeaf, List<HashMap> children) {
		OmOrganization org = orgManager.getModelById(Long.parseLong(orgid));	
		HashMap nodeMap = new HashMap();
        nodeMap.put("id", idToken + orgid);  //责任部门
        nodeMap.put("text", org.getOrgname());
        nodeMap.put("orgseq", org.getOrgseq());
        List<OmOrganization> allChildList = orgManager.findAllChilds(Long.parseLong(orgid), false);		            
        for (OmOrganization organization : allChildList) {
			String orgseq = organization.getOrgseq();
			if (orgseqs.contains(orgseq)) {
				isLeaf = false;
				break;
			}
		}
        nodeMap.put("leaf", isLeaf);
        children.add(nodeMap);
        return children;
	}
	/**
	 * 
	 * <li>说明：获取责任机构下级节点列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param id
	 * @param isLeaf
	 * @param orgseqs
	 * @param idToken
	 * @param children
	 * @return 责任机构下级节点列表
	 */
	@SuppressWarnings("unchecked")
	private List<HashMap> getChildOrgList(String id, boolean isLeaf, String orgseqs, String idToken, List<HashMap> children) {
		id = id.substring(2);					
		List<OmOrganization> childList = orgManager.findChildsById(Long.parseLong(id));					
		for (OmOrganization organization : childList) {
			isLeaf = true;
			isLeaf = getIsLeaf(organization, orgseqs, isLeaf);
            if (isLeaf) {
            	OmOrganization org = orgManager.getModelById(organization.getOrgid());
            	if (!orgseqs.contains(org.getOrgseq())) {
            		continue;
            	}
			}
            HashMap nodeMap = new HashMap();
            nodeMap.put("id", idToken + organization.getOrgid());  //责任部门
            nodeMap.put("text", organization.getOrgname());
            nodeMap.put("orgseq", organization.getOrgseq());
            nodeMap.put("leaf", isLeaf);
            children.add(nodeMap);
		}
		return children;
	}
	/**
	 * 
	 * <li>说明：获取管理库房列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idToken
	 * @param isLeaf
	 * @param children
	 * @return 管理库房列表
	 */
	@SuppressWarnings("unchecked")
	private List<HashMap> getDutyWHList(String idToken, boolean isLeaf, List<HashMap> children) {
		idToken = "K_";
		List list = getManageDeptList(PartsAccount.MANAGE_DEPT_TYPE_WH);
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[]) list.get(i);
			HashMap nodeMap = new HashMap();
            nodeMap.put("id", idToken + objs[1] != null ? objs[1].toString() : "");  
            nodeMap.put("text", objs[0] != null ? objs[0].toString() : "");
            nodeMap.put("orgseq", objs[2] != null ? objs[2].toString() : "");
            nodeMap.put("leaf", isLeaf);
            children.add(nodeMap);
		}
		return children;
	}
	/**
	 * 
	 * <li>说明：判断是否是子节点
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param organization
	 * @param orgseqs
	 * @param isLeaf
	 * @return true 是子节点 false 非子节点
	 */
	private boolean getIsLeaf(OmOrganization organization, String orgseqs, boolean isLeaf) {
		List<OmOrganization> allChildList = orgManager.findAllChilds(organization.getOrgid(), false);		            
        for (OmOrganization childOrg : allChildList) {
			String orgseq = childOrg.getOrgseq();
			if (orgseqs.contains(orgseq)) {
				isLeaf = false;
				break;
			}
		}
        return isLeaf;
	}
	/**
	 * 
	 * <li>说明：根据责任部门类型获取orgseq列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param manageDeptType 责任部门类型
	 * @return orgseq列表
	 */
	@SuppressWarnings("unchecked")
	private List<String> getManageDeptOrgseqList(Integer manageDeptType) {
		String sql = "select distinct(manage_dept_orgseq) from pjwz_parts_account where record_status = 0 and manage_dept_orgseq is not null and manage_dept_type = " + manageDeptType;
		return daoUtils.executeSqlQuery(sql);
	}
	/**
	 * 
	 * <li>说明：根据责任部门类型获取责任部门信息列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param manageDeptType 责任部门类型
	 * @return 责任部门信息列表
	 */
	@SuppressWarnings("unchecked")
	private List getManageDeptList(Integer manageDeptType) {
		String sql = "select distinct(manage_dept), manage_dept_id, manage_dept_orgseq from pjwz_parts_account where record_status = 0 and  manage_dept_type = " + manageDeptType;
		return daoUtils.executeSqlQuery(sql);
	}
	/**
	 * 
	 * <li>说明：根据责任部门类型获取以,分隔的orgseq字符串
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param manageDeptType 责任部门类型
	 * @return 以,分隔的orgseq字符串
	 */
	private String getManageDeptOrgseqs(Integer manageDeptType) {
		List<String> list = getManageDeptOrgseqList(manageDeptType);
		String[] orgseqs = list.toArray(new String[list.size()]);
		return StringUtil.join(orgseqs);
	}
	
	
}
