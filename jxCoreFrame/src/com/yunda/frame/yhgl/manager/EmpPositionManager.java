package com.yunda.frame.yhgl.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmpposition;
import com.yunda.frame.yhgl.entity.OmEmppositionId;
import com.yunda.frame.yhgl.entity.OmPosition;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 人员-岗位对应关系
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="empPositionManager")
public class EmpPositionManager extends JXBaseManager {
	/** 岗位查询接口 */
	@Resource(name="omPositionManager")
	private IOmPositionManager omPositionManager;
	
	
	/**
	 * <li>说明：新增/更新人员-机构关联 (机构人员管理)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void addEmpPositionCorrelation(Long positionid, Long empid, String isMain){
		OmPosition op = omPositionManager.findByEmpid(empid,isMain); //调用查询接口，查询该用户是否有对应的岗位
		if(op == null) {
			//用户没有对应的机构，执行新增操作
			OmEmppositionId eoId = new OmEmppositionId();
			OmEmpposition eo = new OmEmpposition();
			eoId.setEmpid(empid);
			eoId.setPositionid(positionid);
			eo.setId(eoId);
			eo.setIsmain(isMain);
			daoUtils.saveOrUpdate(eo);
		} else {
			//用户已存在对应机构，如与当前机构id不一致，执行更新操作
			if(op.getPositionid()!=positionid){
				String sql = "update OM_EMPPOSITION set positionid = " + positionid + ", ismain = '"+ isMain + "' where empid = " + empid + " and ismain = '"+isMain+"'";
				daoUtils.executeSql(sql);
			}
		}
	}
	
	/**
	 * 更新人员-岗位表关联关系
	 * @param positionid 新岗位ID
	 * @param empid      人员ID
	 * @param oldpositionid 原岗位ID
	 * @param isMain     是否主岗位
	 */
	public void updateOmEmpPositionByWidget(Long positionid, Long empid, Long oldpositionid, String isMain){
		String updateSQL = "update om_empposition set positionid = " + positionid + " where empid = " + empid + " and positionid = " + oldpositionid + " and isMain = '" + isMain+ "'";
		this.daoUtils.executeSql(updateSQL);
	}
	
	/**
	 * <li>说明：保存岗位-人员关系 (工作组管理)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位id
	 * @param empid 人员ID
	 * @throws 抛出异常列表
	 */
	public void addOmEmpPosition(Long positionid,Long empid, Boolean isMain)  throws BusinessException {
		OmEmppositionId eoId = new OmEmppositionId();
		OmEmpposition eo = new OmEmpposition();
		eoId.setEmpid(empid);
		eoId.setPositionid(positionid);
		eo.setId(eoId);
		if(isMain){
			eo.setIsmain("y");
		}
		this.daoUtils.saveOrUpdate(eo);
	}
	
	/**
	 * <li>说明：移除人员-岗位关联关系 (工作组管理)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位id
	 * @return empid 人员id
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void delOmEmpPosition(Long positionid,Long empid) throws BusinessException {
		String hql = "from OmEmpposition where id.positionid = "+positionid+" and id.empid = "+empid;
		List <OmEmpposition> list = this.daoUtils.find(hql);
		for(OmEmpposition eg : list){
			this.daoUtils.getHibernateTemplate().delete(eg);
		}
	}
	
	/**
	 * <li>说明：根据工作组ID，移除其下级工作组（岗位）中的人员-岗位关联关系 (工作组管理-调整工作组)
	 * <li>人员对应的工作组调整后，原工作组下的所有岗位与人员的关联全部清除
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-04-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位id
	 * @return empid 人员id
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void delOmEmpPositionByGroupId(Long groupId, Long empid) throws BusinessException {
		//获取工作组下所有岗位ID（包含子工作组下的岗位及岗位下的子岗位）
		List <OmPosition> list = omPositionManager.findAllPertainToWorkGroup(groupId);
		for(OmPosition op:list){
			delOmEmpPosition(op.getPositionid(),empid);
		}
	}
	
	
	
	/**
	 * 根据岗位ID删除岗位下的岗位与人员关联关系数据，并调用人员删除方法删除人员信息
	 * @param positionId
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public void deleteByPositionId(Long positionId) throws BusinessException {
		String hql = "from OmEmpposition where id.positionid = "+positionId;
		List <OmEmpposition> list = this.daoUtils.find(hql);
		EmployeeManager employeeManager = (EmployeeManager)Application.getSpringApplicationContext().getBean("employeeManager");
		Long empid = null;
		for(OmEmpposition eg : list){
			empid = eg.getId().getEmpid();
			this.daoUtils.getHibernateTemplate().delete(eg); //删除人员-岗位关联关系
			employeeManager.deleteByIds(empid);//删除人员
			
		}
	}
	
	/**
	 * 根据岗位ID删除岗位下的岗位与人员关联关系数据，但不删除人员信息
	 * @param positionId
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public void deleteByPositionId2(Long positionId) throws BusinessException {
		String hql = "from OmEmpposition where id.positionid = "+positionId;
		List <OmEmpposition> list = this.daoUtils.find(hql);
		for(OmEmpposition eg : list){
			this.daoUtils.getHibernateTemplate().delete(eg); //删除人员-岗位关联关系
		}
	}
	
	/**
	 * 根据人员ID删除岗位下的岗位与人员关联关系数据
	 * @param positionId
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public void deleteByEmployeeId(Long empId) throws BusinessException {
		String hql = "from OmEmpposition where id.empid = "+empId;
		List <OmEmpposition> list = this.daoUtils.find(hql);
		for(OmEmpposition eg : list){
			this.daoUtils.getHibernateTemplate().delete(eg);
		}
	}
	
	/**
	 * 根据人员id查询其关联的岗位信息
	 * @param empid  人员ID
	 * @param ismain 是否主岗位
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmpposition>  findEmpPosiEntity(Long empid, String isMain){
		String hql = "from OmEmpposition where id.empid = " + empid + " and ismain = '"+isMain+"'";
		List <OmEmpposition> list = this.daoUtils.find(hql);
		return list;
	}
	
	/**
	 * 根据岗位id查询其关联的人员信息
	 * @param empid  人员ID
	 * @param ismain 是否主岗位
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmpposition>  findEmpPosiEntityByPositionId(Long positionId){
		String hql = "from OmEmpposition where id.positionid = " + positionId;
		List <OmEmpposition> list = this.daoUtils.find(hql);
		return list;
	}
	
}
