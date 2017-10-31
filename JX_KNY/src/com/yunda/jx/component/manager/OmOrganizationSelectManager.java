package com.yunda.jx.component.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jcgy.entity.JgyjcBureau;
import com.yunda.jcgy.entity.JgyjcDeport;
import com.yunda.jcgy.manager.JgyjcDeportManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 组织机构选择控件业务类
 * <li>创建人：程锐
 * <li>创建日期：2012-9-2
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "omOrganizationSelectManager")
public class OmOrganizationSelectManager extends JXBaseManager<OmOrganization, OmOrganization> {
    
    /** 组织机构业务类：OmOrganizationManager */
    @Resource(name = "omOrganizationManager")
    private IOmOrganizationManager omOrganizationManager;
    
    /** 段业务类：JgyjcDeportManager */
    @Resource
    private JgyjcDeportManager jgyjcDeportManager;
    /**
     * <li>说明：查询组织列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上一级orgid
     * @return List<HashMap> 查询列表所需List列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<Map> findOrgTree(String parentIDX) throws BusinessException {
        return omOrganizationManager.findOrgTree(Long.valueOf(parentIDX), Constants.BUREAU_LEVEL);
    }
    
    /**
     * <li>说明：查询组织列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-23
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上一级orgid
     * @param queryHql 查询Hql字符串
     * @param fullNameDegree 全名
     * @param isChecked 是否多选
     * @return List<HashMap> 查询列表所需List列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<Map> findOrgTree(String parentIDX, String queryHql, String fullNameDegree, String isChecked) throws BusinessException {
        return omOrganizationManager.findOrgTree(Long.valueOf(parentIDX), queryHql, fullNameDegree, isChecked);
    }
    
    /**
     * <li>说明：查询组织列表,并根据参数决定是否只查询当前层级;
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上一级orgid
     * @param queryHql 查询Hql字符串
     * @param getNextLevels 下一级
     * @param isChecked 是否多选
     * @return List<HashMap> 查询列表所需List列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<Map> findOrgTreeForDict(String parentIDX, String queryHql, boolean getNextLevels, String isChecked) throws BusinessException {
        return omOrganizationManager.findOrgTree(queryHql, getNextLevels, isChecked);
    }
    
    /**
     * <li>说明：查询局列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param queryHql 查询Hql字符串
     * @param leaf 是否子节点
     * @return List<HashMap> 查询列表所需List列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> bureauTree(String queryHql, boolean leaf) throws BusinessException {
        String hql = "from JgyjcBureau where bId != '00' order by sort ";
        // 优先按查询Hql查询结果
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql = queryHql;
        }
        List<JgyjcBureau> orgList = (List<JgyjcBureau>) getDaoUtils().find(hql);
        List<HashMap> children = new ArrayList<HashMap>();
        for (JgyjcBureau org : orgList) {
            HashMap nodeMap = new HashMap();
            nodeMap.put("id", org.getBId());
            nodeMap.put("text", org.getBName());
            nodeMap.put("orgname", org.getShortName());
            nodeMap.put("isleaf", true);
            nodeMap.put("leaf", leaf);
            nodeMap.put("code", org.getCode());
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：查询段列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 所属局ID
     * @param queryHql 查询Hql字符串
     * @return List<HashMap> 查询列表所需List列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> deportTree(String parentIDX, String queryHql) throws BusinessException {
        String hql = "from JgyjcDeport where attribute = '1' and ownBureau = ? order by dId ";
        // 优先按查询Hql查询结果
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql = queryHql;
        }
        List<JgyjcDeport> orgList = (List<JgyjcDeport>) getDaoUtils().find(hql, new Object[] { parentIDX });
        List<HashMap> children = new ArrayList<HashMap>();
        for (JgyjcDeport org : orgList) {
            HashMap nodeMap = new HashMap();
            nodeMap.put("id", org.getDId());
            nodeMap.put("text", org.getDName());
            nodeMap.put("delegateDName", org.getDName());
            nodeMap.put("delegateDShortName", org.getShortName());
            nodeMap.put("orgname", org.getShortName());
            nodeMap.put("isleaf", true);
            nodeMap.put("leaf", true);
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：查询到某级机构树
     * <li>创建人：张凡
     * <li>创建日期：2012-11-13
     * <li>修改人：程锐
     * <li>修改日期：2013-04-29
     * <li>修改内容：修改显示车间或班组错误的bug
     * @param parentIDX 上一级orgid
     * @param orgdegree 要查询到的级degree
     * @param fullNameDegree 全名
     * @param isChecked 是否多选
     * @return List<HashMap> 查询列表所需List列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<Map> findSomeLevelOrgTree(String parentIDX, String orgdegree, String fullNameDegree, String isChecked) throws BusinessException {
        return omOrganizationManager.findSomeLevelOrgTree(Long.valueOf(parentIDX), orgdegree, fullNameDegree, isChecked);
    }
    
    /**
     * <li>说明：根据orgcode获取OmOrganization对象
     * <li>创建人：程锐
     * <li>创建日期：2012-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param orgcode 组织机构代码
     * @return OmOrganization 组织机构实体对象
     */
    public static OmOrganization getOrgByOrgcode(String orgcode) {
        OmOrganizationManager omOrganizationManager =
            (OmOrganizationManager) Application.getSpringApplicationContext().getBean("omOrganizationManager");
        return omOrganizationManager.findOrgForCode(orgcode);
    }
    
    /**
     * <li>说明：根据orgId查询组织机构对象。
     * <li>创建人：王治龙
     * <li>创建日期：2013-1-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param orgId 组织ID
     * @return OmOrganization
     */
    public static OmOrganization getOrgById(long orgId) {
        OmOrganizationManager omOrganizationManager =
            (OmOrganizationManager) Application.getSpringApplicationContext().getBean("omOrganizationManager");
        return omOrganizationManager.getModelById(orgId);
    }
    
    /**
     * <li>说明：根据orgid获取OmOrganization对象
     * <li>创建人：程锐
     * <li>创建日期：2012-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param orgId 单位orgid
     * @return OmOrganization 组织机构实体对象
     */
    public OmOrganization getOrgByOrgid(Long orgId) {
        return omOrganizationManager.getModelById(orgId);
    }
    
    /**
     * <li>说明：根据人员id获取所在单位全名，格式如:重庆西/重庆机务段/成都铁路局，最大单位到铁道部下一级
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 人员ID
     * @return 所在单位全名
     */
    public String getFullOrgNameByEmpid(Long empid) {
        OmOrganization org = omOrganizationManager.findByEmpId(empid);
        return omOrganizationManager.getFullName(org.getOrgid(), Constants.BUREAU_LEVEL);
    }
    
    /**
     * <li>方法名称：getTopOrgid
     * <li>方法说明：根据机构ID查询到上面某级的ID;
     * <li>
     * @param orgid
     *            <li>
     * @param orgdegree
     *            <li>
     * @return
     *            <li>return: String
     *            <li>创建人：张凡
     *            <li>创建时间：2013-5-1 下午05:40:27
     *            <li>修改人：
     *            <li>修改内容：
     * @throws Exception
     */
    public String getTopOrgid(Long orgid, String orgdegree) throws Exception {
        OmOrganization org = getUpOrgid(orgid, orgdegree);
        if (org == null)
            throw new NullPointerException("orgid为" + orgid + "的上级机构且orgdegree为" + orgdegree + "的机构为空，请检查数据");
        return org.getOrgid().toString();
    }
    
    /**
     * <li>方法名称：getUpOrgid
     * <li>方法说明：根据机构ID查询到上面某级的ORG对象;
     * <li>
     * @param orgid
     *            <li>
     * @param orgdegree
     *            <li>
     * @return
     *            <li>return: String
     *            <li>创建人：张凡
     *            <li>创建时间：2013-5-1 下午05:40:27
     *            <li>修改人：
     *            <li>修改内容：
     */
    public OmOrganization getUpOrgid(Long orgid, String orgdegree) throws Exception {
        List<OmOrganization> list = omOrganizationManager.findUpperByDegree(orgid, orgdegree);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * <li>方法名称：getTeamOfDept
     * <li>方法说明：查询某部门下级所有机构
     * <li>
     * @param orgid
     *            <li>
     * @return
     *            <li>return: List<OmOrganization>
     *            <li>创建人：张凡
     *            <li>创建时间：2013-5-9 下午03:16:13
     *            <li>修改人：
     *            <li>修改内容：
     */
    public List<OmOrganization> getTeamOfDept(String orgid) {
        return omOrganizationManager.findChildsById(Long.valueOf(orgid));
    }
    
    /**
     * <li>方法名称：getOrgByAcOperator
     * <li>方法说明：根据operatorid查询机构
     * <li>
     * @param operatorid
     *            <li>
     * @return
     *            <li>return: OmOrganization
     *            <li>创建人：张凡
     *            <li>创建时间：2013-5-10 下午06:34:37
     *            <li>修改人：
     *            <li>修改内容：
     */
    public OmOrganization getOrgByAcOperator(Long operatorid) {
        return omOrganizationManager.findByOperator(operatorid);
    }
    
    /**
     * <li>说明：根据登录人所属部门id获取与承修部门数据字典匹配的机构实体对象
     * <li>创建人：程锐
     * <li>创建日期：2013-5-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param plantId 登录人所属部门id
     * @return OmOrganization 组织机构实体对象
     */
    public OmOrganization getUnderTakeByEmp(String plantId) {
        return omOrganizationManager.getUnderTakeByEmp(plantId);
    }
    
    /**
     * <li>说明：根据bid查询配属局信息。
     * <li>创建人：王治龙
     * <li>创建日期：2013-9-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param bId 配属局ID
     * @return JgyjcBureau
     */
    @SuppressWarnings("unchecked")
    public JgyjcBureau getBureauById(String bId) {
        if (!StringUtil.isNullOrBlank(bId)) {
            String hql = "from JgyjcBureau where bId ='" + bId + "'";
            List<JgyjcBureau> orgList = (List<JgyjcBureau>) getDaoUtils().find(hql);
            return orgList.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * <li>说明：根据did查询配属段信息。
     * <li>创建人：王治龙
     * <li>创建日期：2013-9-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param dId 配属段ID
     * @return JgyjcDeport
     */
    @SuppressWarnings("unchecked")
    public JgyjcDeport getDeportById(String dId) {
        if (!StringUtil.isNullOrBlank(dId)) {
            String hql = "from JgyjcDeport where dId ='" + dId + "'";
            List<JgyjcDeport> orgList = (List<JgyjcDeport>) getDaoUtils().find(hql);
            return orgList.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * <li>说明：查询段机构树列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param orgid 组织机构ID
     * @param operatorid 操作人员ID
     * @return 查询段机构树列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map> querySegmentOrgTreeList(Long orgid, Long operatorid) throws Exception {
        if (orgid == -1L) {
            OmOrganization org = getOrgByAcOperator(operatorid);
            orgid = getUpOrgid(org.getOrgid(), Constants.SEGMENT_LEVEL).getOrgid();
        }
        return omOrganizationManager.findSomeLevelOrgTree(orgid, "degree]tream", null, null);
    }
    
    /**
     * <li>说明：查询作业班组列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 作业班组列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map> queryTeamList(Long operatorid) throws Exception {
        OmOrganization org = getOrgByAcOperator(operatorid);
        String orgid = getTopOrgid(org.getOrgid(), Constants.DEPARTMENT_LEVEL);
        return findSomeLevelOrgTree(orgid, "tream|fullName", Constants.DEPARTMENT_LEVEL, null);
    }
    
    /**
     * <li>说明：根据站点标识维护查询当前站场维护的班组信息（iPad应用），如果在【站点标识维护】模块没有维护班组信息，则查询该操作员所在班组级别的所有班组
     * <li>创建人：何涛
     * <li>创建日期：2015-8-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List queryTeamListBySite() throws Exception {
        String siteId = EntityUtil.findSysSiteId(null);
        StringBuilder sb = new StringBuilder("From OmOrganization");
        sb.append(" Where orgid In (Select orgid From WorkPlaceToOrg Where workPlaceCode = '").append(siteId).append("')");
        List list = this.daoUtils.find(sb.toString());
        if (null != list && list.size() > 0) {
            return list;
        }
        return this.queryTeamList(SystemContext.getAcOperator().getOperatorid());
    }
    /**
     * 
     * <li>说明：配属段列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<JgyjcDeport> 配属段分页对象
     * @throws BusinessException
     */
    public Page<JgyjcDeport> getDeportList(SearchEntity<JgyjcDeport> searchEntity)
            throws BusinessException {
            JgyjcDeport deport = searchEntity.getEntity() ;
            StringBuilder fromSql = new StringBuilder(" from j_gyjc_deport ");
            if(!StringUtil.isNullOrBlank(deport.getDName())){
                fromSql.append(" where D_ID like '%").append(deport.getDName()).append("%' or D_NAME like '%").append(deport.getDName()).append("%' ") ;
            }
            StringBuffer buffTotal = new StringBuffer("select count(*) ").append(fromSql);
            StringBuffer buffFrom = new StringBuffer("select * ").append(fromSql);
            Order[] orders = searchEntity.getOrders();
            if(orders != null && orders.length > 0){            
                buffFrom.append(HqlUtil.getOrderHql(orders));
            }else{
                buffFrom.append(" order by D_ID ASC");
            }       
            return jgyjcDeportManager.findPageList(buffTotal.toString(), buffFrom.toString(), searchEntity.getStart(), searchEntity.getLimit(), null, orders);
        }
}
