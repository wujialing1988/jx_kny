package com.yunda.sb.classfication.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.base.constant.BizConstant;
import com.yunda.sb.classfication.entity.Classification;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备类别管理业务管理器
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "classificationManager")
public class ClassificationManager extends JXBaseManager<Classification, Classification> {

	/**
	 * <li>说明： 重写新增修改前验证方法，保证设备类别编码唯一
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-4-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param parentCode 父设备类别编号
	 * @param findEquipment 查询设备，如果为true：则
	 * @return 树集合
	 */
	@Override
	public String[] validateUpdate(Classification t) {
		Classification entity = this.getModelByClassCode(t.getClassCode());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { String.format("设备类别编码【%s】已经存在，请重新输入！", t.getClassCode()) };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明： 重写逻辑删除
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-4-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@param ids 要删除的id
	 *@throws NoSuchFieldException
	 */
	@Override
	public void logicDelete(Serializable... ids) throws NoSuchFieldException {
		List<Serializable> idxList = new ArrayList<Serializable>();
		for (Serializable idx : ids) {
			idxList.add(idx);
		}
		this.logicDelete2(idxList);

		// 删除完成后，更新其父节点的叶子状态
		String parentIdx;
		Classification parent;
		List<Classification> children;
		for (Serializable idx : ids) {
			parentIdx = this.getModelById(idx).getParentIdx();
			if (StringUtil.isNullOrBlank(parentIdx)) {
				continue;
			}
			parent = this.getModelById(parentIdx);
			if (null == parent) {
				continue;
			}
			children = this.getModelsByParentIdx(parentIdx);
			if (null == children || children.isEmpty()) {
				parent.setLeaf(Classification.IS_LEAF_YES);
				this.saveOrUpdate(parent);
			}
		}
	}

	/**
	 * <li>说明： 递归删除下级设备类别
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-4-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@param idxList 要删除的id集合
	 *@throws NoSuchFieldException
	 */
	public void logicDelete2(List<Serializable> idxList) throws NoSuchFieldException {
		if (null == idxList || idxList.isEmpty()) {
			return;
		}
		List<Classification> children = null;
		for (Serializable idx : idxList) {
			children = this.getModelsByParentIdx((String) idx);
			if (null == children || children.isEmpty()) {
				continue;
			}
			List<Serializable> childrenList = new ArrayList<Serializable>(children.size());
			for (Classification child : children) {
				childrenList.add(child.getIdx());
			}
			// 【递归】
			this.logicDelete2(childrenList);
		}
		Serializable[] ids = new Serializable[idxList.size()];
		super.logicDelete(idxList.toArray(ids));
	}

	/**
	 * <li>说明： 新增或更改后
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-4-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t Classification实体
	 * @param isNew 是否为新增，true：新增（插入）、false：更新
	 */
	@Override
	protected void updateAfter(Classification t, boolean isNew) {
		if (isNew) {
			// 新增后，修改其父类数据“叶子节点”标识，0：不是叶子节点，1：是叶子节点
			Classification parent = this.getModelById(t.getParentIdx());
			if (null != parent) {
				if (Classification.IS_LEAF_YES == parent.getLeaf().intValue()) {
					parent.setLeaf(Classification.IS_LEAF_NO);
					try {
						this.saveOrUpdate(parent);
					} catch (NoSuchFieldException e) {
						throw new BusinessException(e);
					}
				}
			}
		}
	}

	/**
	 * <li>说明： 查询设备类别树
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-4-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param parentCode 父设备类别编号
	 * @param findEquipment 查询设备，如果为true：则 
	 * @return 树集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTree(String parentCode, boolean findEquipment) {
		String hql = "From Classification Where recordStatus = 0";
		if ("root".equals(parentCode)) {
			hql += " And parentIdx Is Null Order By classCode";
		} else {
			hql += " And parentIdx = (Select idx From Classification Where recordStatus = 0 And classCode = '" + parentCode + "') Order By classCode";
		}
		List<Classification> list = this.daoUtils.find(hql);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		if (null != list && !list.isEmpty()) {
			for (Classification c : list) {
				map = new HashMap<String, Object>();
				map.put("id", c.getClassCode()); // 设备类别编号
				map.put("idx", c.getIdx());
				map.put("parentIdx", c.getParentIdx());
				map.put("text", c.getClassCode() + ":" + c.getClassName());
				map.put("leaf", findEquipment ? false : c.getLeaf().equals(1));
				map.put("classCode", c.getClassCode());
				map.put("className", c.getClassName());
				map.put("type", "equipmentClass");
				result.add(map);
			}
		} else {
			// 查询指定设备类别下系统操作员所在单位（段、局、部）下属的固资设备（动态为：新购、调入）
			hql = "From EquipmentPrimaryInfo Where recordStatus = 0 And dynamic In(?, ?) And fixedAssetNo Is Not Null And classCode = ? AND fixedAssetValue >= ? And orgId Like '" + SystemContext.getOmEmployee().getOrgid()
					+ "%'";
			List<EquipmentPrimaryInfo> epis = this.daoUtils.find(hql, EquipmentPrimaryInfo.Dynamic.NEW_BUY.getCode(), EquipmentPrimaryInfo.Dynamic.IN.getCode(), parentCode, BizConstant.FIXED_ASSET_VALUE);
			for (EquipmentPrimaryInfo epi : epis) {
				map = new HashMap<String, Object>();
				map.put("id", epi.getEquipmentCode()); 
				map.put("idx", epi.getIdx());
				map.put("text", epi.getEquipmentCode() + ":" + epi.getEquipmentName() + "【固资设备】");
				map.put("iconCls", "cmpIcon"); // 树节点图标样式
				map.put("leaf", true);
				map.put("equipmentName", epi.getEquipmentName());
				map.put("type", "equipment");
				result.add(map);
			}
		}
		return result;
	}

	/**
	 * <li>说明： 根据设备类别编码查询设备类别实体对象
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-4-28
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@param classCode 设备类别编码
	 *@return Classification实体对象
	 */
	public Classification getModelByClassCode(String classCode) {
		String hql = "From Classification Where recordStatus = 0 And classCode = ?";
		return (Classification) this.daoUtils.findSingle(hql, new Object[] { classCode });
	}

	/**
	 * <li>说明：根据上级idx主键获取其下属设备类别集合
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年4月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param parentIdx 上级idx主键
	 * @return 下属设备类别集合
	 */
	@SuppressWarnings("unchecked")
	public List<Classification> getModelsByParentIdx(String parentIdx) {
		String hql = "From Classification Where parentIdx Is Null And recordStatus = 0";
		if (null != parentIdx) {
			hql = "From Classification Where parentIdx = '" + parentIdx + "' And recordStatus = 0";
		}
		hql += " Order By classCode";
		return this.daoUtils.find(hql);
	}

}
