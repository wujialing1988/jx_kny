package com.yunda.jx.jxgc.base.tpqcitemdefine.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemEmpOrgDefine;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质量检查范围基础配置业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-6-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "tPQCItemEmpOrgDefineManager")
public class TPQCItemEmpOrgDefineManager extends JXBaseManager<TPQCItemEmpOrgDefine, TPQCItemEmpOrgDefine> {
    
    /**
     * <li>说明：根据质量检查人员主键获取提票质量检查范围基础配置实体列表
     * <li>创建人：程锐
     * <li>创建日期：2015-6-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qcEmpIDX 质量检查人员主键
     * @return 提票质量检查范围基础配置实体列表
     */
    @SuppressWarnings("unchecked")
    public List<TPQCItemEmpOrgDefine> getModelsByQCEmpIDX(String qcEmpIDX) {
        String hql = "From TPQCItemEmpOrgDefine Where recordStatus = 0 And qcEmpIDX = ?";
        return this.daoUtils.find(hql, new Object[] { qcEmpIDX });
    }
    
    /**
     * <li>说明：保存前验证
     * <li>创建人：程锐
     * <li>创建日期：2015-6-25
     * <li>修改人：
     * <li>修改日期：
     * @param entity 提票质量检查范围基础配置实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(TPQCItemEmpOrgDefine entity) {
        String hql = "From TPQCItemEmpOrgDefine Where recordStatus = 0 And checkOrgID = ? And qcEmpIDX = ?";
        List list = this.daoUtils.find(hql, new Object[] { entity.getCheckOrgID(), entity.getQcEmpIDX() });
        if (null == list || list.size() <= 0) {
            return null;
        }
        return new String[] { entity.getCheckOrgName() + "已经存在，无需重复添加！" };
    }
    
    /**
     * <li>说明：根据“质量检查人员主键”获取检查班组的组合字段字面值
     * <li>创建人：何涛
     * <li>创建日期：2015-8-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param qcEmpIDX 质量检查人员主键
     * @return 检查班组的组合字段字面值
     */
    public String getCheckOrgByQCEmpIDX(String qcEmpIDX) {
        List<TPQCItemEmpOrgDefine> list = this.getModelsByQCEmpIDX(qcEmpIDX);
        if (null == list || list.size() <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (TPQCItemEmpOrgDefine org : list) {
            sb.append(",").append(org.getCheckOrgName());
        }
        return sb.substring(1);
    }
}
