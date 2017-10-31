package com.yunda.jx.jxgc.base.tpqcitemdefine.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemDefine;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemEmpDefine;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质量检查项基础配置业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-6-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "tPQCItemDefineManager")
public class TPQCItemDefineManager extends AbstractOrderManager<TPQCItemDefine, TPQCItemDefine> {
    
    /** QCEmp业务类 */
    @Resource
    TPQCItemEmpDefineManager tPQCItemEmpDefineManager;
    
    /**
     * <li>说明：新增或更新
     * <li>创建人：程锐
     * <li>创建日期：2012-08-07
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 提票质量检查项基础配置实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveOrUpdate(TPQCItemDefine t) throws BusinessException, NoSuchFieldException {
        // 如果记录被更新为“指派”，则查询并逻辑删除该记录下所维护的人员数据
        if (TPQCItemDefine.CONST_INT_IS_ASSIGN_Y == t.getIsAssign()) {
            // 判断idx是否为空来确定记录是否为更新（非新增）操作
            String idx = t.getIdx();
            if (!StringUtil.isNullOrBlank(idx)) {
                List<TPQCItemEmpDefine> list = this.tPQCItemEmpDefineManager.getModelsByQCItemIDX(idx);
                tPQCItemEmpDefineManager.logicDelete(list);
            }
        }
        
        super.saveOrUpdate(t);
    }
    
    /**
     * <li>说明：根据检查项名称获取检查项实体对象
     * <li>创建人：何涛
     * <li>创建日期：2015-09-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qCItemName 检查项名称
     * @return 检查项实体对象
     */
    public TPQCItemDefine getModelByQCItemName(String qCItemName) {
        String hql = "From TPQCItemDefine Where recordStatus = 0 And qCItemName = ?";
        return (TPQCItemDefine) this.daoUtils.findSingle(hql, new Object[] { qCItemName });
    }
    
    /**
     * <li>说明：根据检查项编码获取检查项实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-6-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qCItemNo 检查项编码
     * @return 检查项实体对象
     */
    public TPQCItemDefine getModelByQCItemNo(String qCItemNo) {
        String hql = "From TPQCItemDefine Where recordStatus = 0 And qCItemNo = ?";
        return (TPQCItemDefine) this.daoUtils.findSingle(hql, new Object[] { qCItemNo });
    }
    
    /**
     * <li>说明：保存前验证
     * <li>创建人：程锐
     * <li>创建日期：2015-6-25
     * <li>修改人：
     * <li>修改日期：
     * @param t 检查项实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(TPQCItemDefine t) {
        // 验证质量检查项编码不能重复
        TPQCItemDefine entity = this.getModelByQCItemNo(t.getQCItemNo());
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            return new String[] { "质量检查项编码【" + t.getQCItemNo() + "】已经存在，不能重复添加！" };
        }
        // 验证质量检查项名称不能重复
        entity = this.getModelByQCItemName(t.getQCItemName());
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            return new String[] { "质量检查项名称【" + t.getQCItemName() + "】已经存在，不能重复添加！" };
        }
        return super.validateUpdate(t);
    }
    
    /**
     * <li>说明：逻辑删除，级联删除质量检查人员
     * <li>创建人：何涛
     * <li>创建日期：2015-09-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 质量检查项主键数组
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        for (Serializable idx : ids) {
            List<TPQCItemEmpDefine> list = this.tPQCItemEmpDefineManager.getModelsByQCItemIDX((String) idx);
            if (null != list && list.size() > 0) {
                this.tPQCItemEmpDefineManager.logicDelete(list);
            }
            this.logicDelete(idx);
        }
    }
    
}
