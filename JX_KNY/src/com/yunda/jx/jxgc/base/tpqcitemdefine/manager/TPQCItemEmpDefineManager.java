package com.yunda.jx.jxgc.base.tpqcitemdefine.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemEmpDefine;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemEmpOrgDefine;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质量检查人员基础配置
 * <li>创建人：程锐
 * <li>创建日期：2015-6-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "tPQCItemEmpDefineManager")
public class TPQCItemEmpDefineManager extends JXBaseManager<TPQCItemEmpDefine, TPQCItemEmpDefine> {
    
    @Resource
    TPQCItemEmpOrgDefineManager tPQCItemEmpOrgDefineManager;
    
    /**
     * <li>说明：根据质量检查项主键获取提票质量检查人员基础配置列表
     * <li>创建人：程锐
     * <li>创建日期：2015-6-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qCItemIDX 质量检查项主键
     * @return 提票质量检查人员基础配置列表
     */
    @SuppressWarnings("unchecked")
    public List<TPQCItemEmpDefine> getModelsByQCItemIDX(String qCItemIDX) {
        String hql = "From TPQCItemEmpDefine Where recordStatus = 0 And qCItemIDX = ?";
        return this.daoUtils.find(hql, new Object[] { qCItemIDX });
    }
    
    /**
     * <li>说明：保存前验证
     * <li>创建人：程锐
     * <li>创建日期：2015-6-25
     * <li>修改人：
     * <li>修改日期：
     * @param entity 提票质量检查人员基础配置实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(TPQCItemEmpDefine entity) {
        String hql = "From TPQCItemEmpDefine Where recordStatus = 0 And qCItemIDX = ? And checkEmpID = ?";
        List list = this.daoUtils.find(hql, new Object[] { entity.getQCItemIDX(), entity.getCheckEmpID() });
        if (null == list || list.size() <= 0) {
            return null;
        }
        return new String[] { "人员" + entity.getCheckEmpName() + "，编码[" + entity.getCheckEmpID() + "]已经存在，无需重复添加！" };
    }
    
    /**
     * <li>说明：逻辑删除记录
     * <li>创建人：程锐
     * <li>创建日期：2012-08-07
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 提票质量检查人员基础配置实体类主键idx数组
     * @throws BusinessException
     * NoSuchFieldException
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        for (Serializable idx : ids) {
            List<TPQCItemEmpOrgDefine> list = tPQCItemEmpOrgDefineManager.getModelsByQCEmpIDX((String) idx);
            tPQCItemEmpOrgDefineManager.logicDelete(list);
        }
        super.logicDelete(ids);
    }
    
    /**
     * <li>说明：质量检查人员分页查询（用于解决unix系统下，WN_CONCAT函数不能使用的错误）
     * <li>创建人：何涛
     * <li>创建日期：2015-8-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
     * @return 分页列表
     */
    public Page<TPQCItemEmpDefine> queryPageList(SearchEntity<TPQCItemEmpDefine> searchEntity) {
        StringBuilder sb = new StringBuilder("From TPQCItemEmpDefine Where recordStatus = 0");
        TPQCItemEmpDefine entity = searchEntity.getEntity();
        // 查询条件 - 质量检查项主键
        if (!StringUtil.isNullOrBlank(entity.getQCItemIDX())) {
            sb.append(" And qCItemIDX = '").append(entity.getQCItemIDX()).append("'");
        }
        // 以人员名称进行排序
        sb.append(" Order By checkEmpName ASC");
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<TPQCItemEmpDefine> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        for (TPQCItemEmpDefine qcEmp : page.getList()) {
            // 设置检查班组的组合字段字面值
            qcEmp.setCheckOrg(this.tPQCItemEmpOrgDefineManager.getCheckOrgByQCEmpIDX(qcEmp.getIdx()));
        }
        return page;
    }
    
}
