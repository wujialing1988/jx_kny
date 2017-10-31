package com.yunda.freight.base.classMaintain.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.base.classMaintain.entity.ClassMaintain;
import com.yunda.freight.base.stationTrack.entity.StationTrack;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 班次维护业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-13 16:30:29
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("classMaintainManager")
public class ClassMaintainManager extends JXBaseManager<ClassMaintain, ClassMaintain> implements IbaseCombo, IbaseComboTree {
    
    /**
     * 验证列检所编码唯一
     */
    @Override
    public String[] validateUpdate(ClassMaintain t) {
        String[] errorMsg = super.validateUpdate(t);
        if (null != errorMsg) {
            return errorMsg;
        }
        String hql = "From ClassMaintain Where recordStatus = 0 And classNo = ? ";
        ClassMaintain entity = (ClassMaintain) this.daoUtils.findSingle(hql, new Object[]{ t.getClassNo()});
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            return new String[]{"班次编码：" + t.getClassNo() + "已经存在，不能重复添加！"};
        }
        return null;
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
        // 站点code
        String workplaceCode = String.valueOf(queryParamsMap.get("workplaceCode"));
        if(StringUtil.isNullOrBlank(workplaceCode)){
            workplaceCode = EntityUtil.findSysSiteId(null);
        }
        if(StringUtil.isNullOrBlank(workplaceCode)){
            return null ;
        }
        StringBuffer hql = new StringBuffer(" select t From ClassMaintain t Where recordStatus = 0 And workplaceCode =  '"+workplaceCode+"'") ;
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by seqNo");
        hql.append(" order by seqNo");
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
   
}
