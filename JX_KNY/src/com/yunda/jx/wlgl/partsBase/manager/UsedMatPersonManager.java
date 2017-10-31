package com.yunda.jx.wlgl.partsBase.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.wlgl.partsBase.entity.UsedMat;
import com.yunda.jx.wlgl.partsBase.entity.UsedMatPerson;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UsedMatPerson业务类,常用物料清单使用人
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="usedMatPersonManager")
public class UsedMatPersonManager extends JXBaseManager<UsedMatPerson, UsedMatPerson>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：保存物料清单信息时，将当前数据操作者的信息保存到【常用物料清单使用人】数据表中
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param usedMat
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void insertByMatInWh(UsedMat usedMat) throws BusinessException, NoSuchFieldException {
		// 获取当前登录用户的信息实体
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		
		Long empId = omEmployee.getEmpid();
		String usedMatIdx = usedMat.getIdx();
		// 检验是否已经在【常用物料清单使用人】数据表有相应的数据记录
		if (isExist(empId, usedMatIdx)) {
			return;
		}
		UsedMatPerson person = new UsedMatPerson();
		person.setEmpId(empId);								// 人员ID
		person.setEmpCode(omEmployee.getEmpcode());			// 人员编码
		person.setEmpName(omEmployee.getEmpname());			// 人员姓名
		person.setUsedMatIdx(usedMatIdx);					// 清单idx主键
		String[] validateUpdate = this.validateUpdate(person);
		if (null == validateUpdate) {
			this.saveOrUpdate(person);
		}
	}
	
	/**
	 * <li>说明：检验当前操作用户是否已经在【常用物料清单使用人】数据表有相应的数据记录
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param empId
	 * @param usedMatIdx
	 * @return
	 */
	public boolean isExist(Long empId, String usedMatIdx) {
		String hql = "From UsedMatPerson Where recordStatus = 0 And usedMatIdx = ? And empId = ?";
		List list = this.daoUtils.find(hql, new Object[]{usedMatIdx, empId});
		if (null == list || list.size() <= 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * <li>说明： 批量保存人员信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-31
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param persons
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate(UsedMatPerson[] persons) throws BusinessException, NoSuchFieldException {
		List<UsedMatPerson> entityList = new ArrayList<UsedMatPerson>(persons.length);
		for (UsedMatPerson entity : persons) {
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体对象的idx主键数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */	
	@Override
	public String[] validateDelete(Serializable... ids) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(UsedMatPerson entity) throws BusinessException {
		String hql = "From UsedMatPerson Where recordStatus = 0 And usedMatIdx = ? And empId = ?";
		List list = this.daoUtils.find(hql, new Object[]{entity.getUsedMatIdx(), entity.getEmpId()});
		if (null == list || list.size() <= 0) {
			return null;
		}
		return new String[]{"人员" + entity.getEmpName() + "，编码[" + entity.getEmpCode() + "]已经存在，无需重复添加！"};
	}

	/**
	 * <li>说明： 新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-31
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @param persons
	 * @return
	 */
	public String[] validateUpdate(UsedMatPerson[] persons) {
		for (UsedMatPerson entity : persons) {
			// 验证“常用物料清单”是否已经维护了该适用人员
			String[] errorMsg = this.validateUpdate(entity);
			if (null != errorMsg) {
				return errorMsg;
			}
		}
		return null;
	}
	
}