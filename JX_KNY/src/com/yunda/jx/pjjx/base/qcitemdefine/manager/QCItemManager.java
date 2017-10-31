package com.yunda.jx.pjjx.base.qcitemdefine.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCEmp;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;

/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: QCItem业务类 质量检查项
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-12 下午01:37:06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "qCItemManager")
public class QCItemManager extends AbstractOrderManager<QCItem, QCItem> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** QCEmp业务类 */
    @Resource
    private QCEmpManager qCEmpManager;
    
    /**
     * <li>说明：新增（更新）时，验证质量检查编码和质量检查项名称不能重复
     * <li>创建人：何涛
     * <li>创建日期：2015-07-20
     * <li>修改人:
     * <li>修改日期：
     * @param t 对象实体
     * @return String[] 验证消息
     */
    @Override
    public String[] validateUpdate(QCItem t) {
        // 验证质量检查项编码不能重复
        QCItem entity = this.getModelByQCItemNo(t.getQCItemNo());
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
     * <li>说明： 对“已指派”的记录，删除质量检查项下属人员信息
     * <li>创建人：何涛
     * <li>创建日期：2014-11-13
     * <li>修改人:
     * <li>修改日期：
     * @param t 对象实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @Override
    public void saveOrUpdate(QCItem t) throws BusinessException, NoSuchFieldException {
        // 如果记录被更新为“指派”，则查询并逻辑删除该记录下所维护的人员数据
        if (QCItem.CONST_INT_IS_ASSIGN_Y == t.getIsAssign()) {
            // 判断idx是否为空来确定记录是否为更新（非新增）操作
            String idx = t.getIdx();
            if (!StringUtil.isNullOrBlank(idx)) {
                List<QCEmp> list = this.qCEmpManager.getModelsByQCItemIDX(idx);
                qCEmpManager.logicDelete(list);
            }
        }
        super.saveOrUpdate(t);
    }
    
    /**
     * <li>说明：根据“检查项名称”获取【质量检查项】
     * <li>创建人：何涛
     * <li>创建日期：2014-11-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qCItemName 检查项名称
     * @return QCItem 质量检查项
     */
    public QCItem getModelByQCItemName(String qCItemName) {
        String hql = "From QCItem Where recordStatus = 0 And qCItemName = ?";
        return (QCItem) this.daoUtils.findSingle(hql, new Object[] { qCItemName });
    }
    
    /**
     * <li>说明：根据“检查项编码”获取【质量检查项】
     * <li>创建人：何涛
     * <li>创建日期：2015-09-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qCItemNo 检查项编码
     * @return QCItem 质量检查项
     */
    public QCItem getModelByQCItemNo(String qCItemNo) {
        String hql = "From QCItem Where recordStatus = 0 And qCItemNo = ?";
        return (QCItem) this.daoUtils.findSingle(hql, new Object[] { qCItemNo });
    }
    
    /**
     * <li>说明：根据“检查项名称”获取“检查项编码”
     * <li>创建人：何涛
     * <li>创建日期：2014-12-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qCItemName 质量检查项名称
     * @return String 质量检查项编码
     */
    public String getQCItemNoByQCItemName(String qCItemName) {
        QCItem entity = this.getModelByQCItemName(qCItemName);
        if (null != entity) {
            return entity.getQCItemNo();
        }
        return null;
    }
    
    /**
     * <li>说明：根据多个以|分割的“检查项名称”获取【质量检查项】列表
     * <li>创建人：何涛
     * <li>创建日期：2014-11-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qCContent 多个以|分割的“检查项名称”
     * @return List<QCItem> 质量检查集合
     */
    @SuppressWarnings("unchecked")
    public List<QCItem> getModelsByQCContent(String qCContent) {
        if (null == qCContent || qCContent.trim().length() <= 0) {
            return null;
        }
        String[] array = qCContent.split("\\|");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(",");
            sb.append("'").append(array[i]).append("'");
        }
        String hql = "From QCItem Where recordStatus = 0 And qCItemName In (" + sb.substring(1) + ")";
        return this.daoUtils.find(hql);
    }
    
    /**
     * <li>说明：获取所有已维护的”质量检查项“
     * <li>创建人：何涛
     * <li>创建日期：2014-12-04
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param isAssign 是否指派
     * @return List<QCItem> 质量检查集合
     */
    public static List<QCItem> getQCContent(Integer isAssign) {
        QCItemManager qCItemManager = (QCItemManager) Application.getSpringApplicationContext().getBean("qCItemManager");
        QCItem qcItem = new QCItem();
        qcItem.setRecordStatus(Constants.NO_DELETE);
        if (null != isAssign) {
            qcItem.setIsAssign(isAssign);
        }
        return qCItemManager.findList(qcItem);
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
            List<QCEmp> list = this.qCEmpManager.getModelsByQCItemIDX((String) idx);
            if (null != list && list.size() > 0) {
                this.qCEmpManager.logicDelete(list);
            }
            this.logicDelete(idx);
        }
    }
    
}
