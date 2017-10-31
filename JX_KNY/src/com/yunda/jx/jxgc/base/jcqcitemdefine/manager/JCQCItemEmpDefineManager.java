package com.yunda.jx.jxgc.base.jcqcitemdefine.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemEmpDefine;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemEmpOrgDefine;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: JCQCItemEmpDefine业务类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-12 下午01:36:34
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="jCQCItemEmpDefineManager")
public class JCQCItemEmpDefineManager extends JXBaseManager<JCQCItemEmpDefine, JCQCItemEmpDefine>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** QCEmpOrg业务类 */
	@Resource
	JCQCItemEmpOrgDefineManager jCQCItemEmpOrgDefineManager;
    
	/**
	 * <li>说明：根据“质量检查项主键”查询下属检查人员
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param qCItemIDX 质量检查项主键
	 * @return 质量检查人员列表
	 */
	@SuppressWarnings("unchecked")
	public List<JCQCItemEmpDefine> getModelsByQCItemIDX(String qCItemIDX) {
		String hql = "From JCQCItemEmpDefine Where recordStatus = 0 And qCItemIDX = ?";
		return this.daoUtils.find(hql, new Object[]{qCItemIDX});
	}
	
	/**
	 * <li>说明： 新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-12
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param entity 单个实体对象
	 * @return 验证消息
	 */
	@Override
	public String[] validateUpdate(JCQCItemEmpDefine entity) {
		String hql = "From JCQCItemEmpDefine Where recordStatus = 0 And qCItemIDX = ? And checkEmpID = ?";
		List list = this.daoUtils.find(hql, new Object[]{entity.getQCItemIDX(), entity.getCheckEmpID()});
		if (null == list || list.size() <= 0) {
			return null;
		}
		return new String[]{"人员" + entity.getCheckEmpName() + "，编码[" + entity.getCheckEmpID() + "]已经存在，无需重复添加！"};
	}

	/**
	 * <li>说明： 新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-12
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param entitys 多个实体对象
	 * @return 验证消息
	 */
	public String[] validateUpdate(JCQCItemEmpDefine[] entitys) {
		for (JCQCItemEmpDefine entity : entitys) {
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
	 * <li>创建日期：2014-11-12
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param entitys 质量检查人员数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate(JCQCItemEmpDefine[] entitys) throws BusinessException, NoSuchFieldException {
		List<JCQCItemEmpDefine> entityList = new ArrayList<JCQCItemEmpDefine>(entitys.length);
		for (JCQCItemEmpDefine entity : entitys) {
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明： 逻辑删除，删除的同时删除下属质量检测范围
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-12
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param ids  待删除的实体主键数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (Serializable idx : ids) {
			List<JCQCItemEmpOrgDefine> list = jCQCItemEmpOrgDefineManager.getModelsByQCEmpIDX((String)idx);
			jCQCItemEmpOrgDefineManager.logicDelete(list);
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明： 逻辑删除，删除的同时删除下属质量检测范围
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-13
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param entityList 待删除的实体列表
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@Override
	public void logicDelete(List<JCQCItemEmpDefine> entityList) throws BusinessException ,NoSuchFieldException {
		for (JCQCItemEmpDefine qcEmp : entityList) {
			List<JCQCItemEmpOrgDefine> list = jCQCItemEmpOrgDefineManager.getModelsByQCEmpIDX((qcEmp.getIdx()));
			if (null != list && list.size() > 0) {
				jCQCItemEmpOrgDefineManager.logicDelete(list);
			}
		}
		super.logicDelete(entityList);
	}

    /**
     * <li>说明：质量检查人员分页查询（用于解决unix系统下，WN_CONCAT函数不能使用的错误）
     * <li>创建人：何涛
     * <li>创建日期：2015-8-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
     * @return 质量检查人员列表
     */
    public Page<JCQCItemEmpDefine> queryPageList(SearchEntity<JCQCItemEmpDefine> searchEntity) {
        StringBuilder sb = new StringBuilder("From JCQCItemEmpDefine Where recordStatus = 0");
        JCQCItemEmpDefine entity = searchEntity.getEntity();
        // 查询条件 - 质量检查项主键
        if (!StringUtil.isNullOrBlank(entity.getQCItemIDX())) {
            sb.append(" And qCItemIDX = '").append(entity.getQCItemIDX()).append("'");
        }
        // 以人员名称进行排序
        sb.append(" Order By checkEmpName ASC");
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<JCQCItemEmpDefine> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        for (JCQCItemEmpDefine qcEmp : page.getList()) {
            // 设置检查班组的组合字段字面值
            qcEmp.setCheckOrg(this.jCQCItemEmpOrgDefineManager.getCheckOrgByQCEmpIDX(qcEmp.getIdx()));
        }
        return page;
    }
	
}