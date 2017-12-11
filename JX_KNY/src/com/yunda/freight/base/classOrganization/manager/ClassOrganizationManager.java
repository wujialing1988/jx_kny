package com.yunda.freight.base.classOrganization.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.freight.base.classOrganization.entity.ClassOrganization;
import com.yunda.freight.base.classOrganization.entity.ClassOrganizationUser;
import com.yunda.freight.base.classOrganization.entity.ClassOrganizationUserVo;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 班次班组维护业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-13 16:44:12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("classOrganizationManager")
public class ClassOrganizationManager extends JXBaseManager<ClassOrganization, ClassOrganization> implements IbaseCombo, IbaseComboTree {

    
    /** 人员选择业务类 */
    @Resource
    private OmEmployeeSelectManager omEmployeeSelectManager;
    
    /**
     * <li>说明：保存组织
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param itemList
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveOrganizations(ClassOrganization[] itemList) throws BusinessException, NoSuchFieldException {
        for (ClassOrganization organization : itemList) {
            this.saveOrUpdate(organization);
        }
    }
    
    /**
     * 班次下拉框
     */
    @Override
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        
        String queryParams = req.getParameter("queryParams");
        
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        // 班次主键
        String classIdx = String.valueOf(queryParamsMap.get("classIdx"));
        if(StringUtil.isNullOrBlank(classIdx)){
            return null ;
        }
        StringBuffer hql = new StringBuffer(" select t From ClassOrganization t Where classIdx =  '"+classIdx+"'") ;
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by idx");
        hql.append(" order by idx");
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param classOrgIdx 班次班组ID
     * @param orgIdx 班组ID
     * @return
     */
    public List<ClassOrganizationUserVo> findOrganizationUsers(String classOrgIdx,String orgIdx) {
        List<ClassOrganizationUserVo> list = new ArrayList<ClassOrganizationUserVo>();
        if(StringUtil.isNullOrBlank(classOrgIdx) || StringUtil.isNullOrBlank(orgIdx)){
            return list ;
        }
        List omEmployees = (List<OmEmployee>)omEmployeeSelectManager.findTeamEmpList(Long.parseLong(orgIdx));
        if(omEmployees != null && omEmployees.size() > 0){
            Map<String,ClassOrganizationUser> userMap = this.findClassOrganizationUserMap(classOrgIdx, orgIdx);
            for (int i = 0; i < omEmployees.size(); i++) {
                Object[] employee = (Object[])omEmployees.get(i);
                ClassOrganizationUserVo vo = new ClassOrganizationUserVo(employee[0] + "" , employee[0] + "" ,employee[2] + "");
                vo.setClassOrgIdx(classOrgIdx);
                vo.setOrgIdx(orgIdx);
                if(userMap != null){
                    ClassOrganizationUser user = userMap.get(employee[0] + "");
                    if(user != null){
                        vo.setQueueCode(user.getQueueCode());
                        vo.setQueueName(user.getQueueName());
                        vo.setOrgUserIdx(user.getIdx());
                        vo.setPositionNo(user.getPositionNo());
                        vo.setPositionName(user.getPositionName());
                    }
                }
                list.add(vo);
            }
        }
        return list;
    }
    
    /**
     * <li>说明：查询已经分配的队列
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param classOrgIdx
     * @param orgIdx
     * @return
     */
    private Map<String,ClassOrganizationUser> findClassOrganizationUserMap(String classOrgIdx,String orgIdx){
        Map<String,ClassOrganizationUser> result = null ;
        StringBuffer hql = new StringBuffer(" From ClassOrganizationUser where classOrgIdx = ? and orgIdx = ? ");
        List<ClassOrganizationUser> list = this.daoUtils.find(hql.toString(), new Object[]{classOrgIdx,orgIdx});
        if(list != null && list.size() > 0){
            result = new HashMap<String, ClassOrganizationUser>();
            for (ClassOrganizationUser user : list) {
                result.put(user.getWorkPersonIdx(), user);
            }
        }
        return result ;
    }

    /**
     * <li>说明：保存分队信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param datas
     */
    public void saveOrganizationUsers(ClassOrganizationUser[] datas) {
        for (ClassOrganizationUser entity : datas) {
            this.daoUtils.saveOrUpdate(entity);
        }
    }

   
}
