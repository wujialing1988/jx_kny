package com.yunda.jx.jxgc.base.jcqcitemdefine.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemEmpOrgDefine;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: QCEmpOrg业务类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-13 上午10:45:56
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="jCQCItemEmpOrgDefineManager")
public class JCQCItemEmpOrgDefineManager extends JXBaseManager<JCQCItemEmpOrgDefine, JCQCItemEmpOrgDefine>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：根据“质量检查人员主键”查询下属检查范围
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param qcEmpIDX 质量检查人员主键
	 * @return 下属检查范围集合
	 */
	@SuppressWarnings("unchecked")
	public List<JCQCItemEmpOrgDefine> getModelsByQCEmpIDX(String qcEmpIDX) {
		String hql = "From JCQCItemEmpOrgDefine Where recordStatus = 0 And qcEmpIDX = ?";
		return this.daoUtils.find(hql, new Object[]{qcEmpIDX});
	}
	
	/**
	 * <li>说明： 新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-13
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param entity 单个实体对象
	 * @return 验证信息
	 */
	@Override
	public String[] validateUpdate(JCQCItemEmpOrgDefine entity) {
		String hql = "From JCQCItemEmpOrgDefine Where recordStatus = 0 And checkOrgID = ? And qcEmpIDX = ?";
		List list = this.daoUtils.find(hql, new Object[]{ entity.getCheckOrgID(), entity.getQcEmpIDX()});
		if (null == list || list.size() <= 0) {
			return null;
		}
		return new String[]{entity.getCheckOrgName() + "已经存在，无需重复添加！"};
	}

	/**
	 * <li>说明： 新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-13
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param entitys 多个实体对象
	 * @return 验证信息
	 */
	public String[] validateUpdate(JCQCItemEmpOrgDefine[] entitys) {
		for (JCQCItemEmpOrgDefine entity : entitys) {
			String[] errorMsg = this.validateUpdate(entity);
			if (null != errorMsg) {
				return errorMsg;
			}
		}
		return null;
	}

	/**
	 * <li>说明： 批量保存或更新
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-13
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param entitys 保存实体数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate(JCQCItemEmpOrgDefine[] entitys) throws BusinessException, NoSuchFieldException {
		List<JCQCItemEmpOrgDefine> entityList = new ArrayList<JCQCItemEmpOrgDefine>(entitys.length);
		for (JCQCItemEmpOrgDefine entity : entitys) {
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
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
        List<JCQCItemEmpOrgDefine> list = this.getModelsByQCEmpIDX(qcEmpIDX);
        if (null == list || list.size() <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (JCQCItemEmpOrgDefine org : list) {
            sb.append(",").append(org.getCheckOrgName());
        }
        return sb.substring(1);
    }
	
}