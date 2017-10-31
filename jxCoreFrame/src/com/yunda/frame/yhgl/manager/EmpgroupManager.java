package com.yunda.frame.yhgl.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmpgroup;
import com.yunda.frame.yhgl.entity.OmEmpgroupId;
import com.yunda.frame.yhgl.entity.OmGroup;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工作组-人员关联
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="empgroupManager")
public class EmpgroupManager extends JXBaseManager <OmEmpgroup, OmEmpgroup>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 工作组查询接口 */
	@Resource(name="omGroupManager")
	private IOmGroupManager omGroupManager;
	
	/**
	 * <li>说明：保存工作组-人员关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组Id
	 * @return empid 人员id
	 * @throws 抛出异常列表
	 */
	public void addOmEmpgroup(Long groupid,Long empid) throws BusinessException {
		OmEmpgroupId id = new OmEmpgroupId();
		id.setEmpid(empid);
		id.setGroupid(groupid);
		OmEmpgroup eg = new OmEmpgroup();
		eg.setId(id);
		this.daoUtils.saveOrUpdate(eg);
	}
	
	/**
	 * <li>说明：保存工作组-人员关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组Id
	 * @return empid 人员id
	 * @throws 抛出异常列表
	 */
	public void addOmEmpgroupByWidget(Long groupid,Long empid) throws BusinessException {
		OmGroup group = omGroupManager.findByEmpId(empid);//调用查询接口，查询该用户是否有对应的工作组
		if(group == null) {
			//用户没有对应的工作组，执行新增操作
			OmEmpgroupId id = new OmEmpgroupId();
			id.setEmpid(empid);
			id.setGroupid(groupid);
			OmEmpgroup eg = new OmEmpgroup();
			eg.setId(id);
			this.daoUtils.saveOrUpdate(eg);
		}  else {
			//用户已存在对应工作组，如与当前工作组id不一致，执行更新操作
			if(group.getGroupid()!= groupid){
				String sql = "update om_empgroup set groupid = " + groupid + " where empid = " + empid;
				daoUtils.executeSql(sql);
			}
		}
	}
	
	/**
	 * <li>说明：修改工作组-人员关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-2
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组Id
	 * @return empid 人员id
	 * @throws 抛出异常列表
	 */
	public void updateOmEmpgroupByWidget(Long groupid,Long empid,Long oldgroupid) throws BusinessException {
		String updateSQL = "UPDATE OM_EMPGROUP SET GROUPID = " + groupid + " WHERE EMPID = " + empid + " AND GROUPID = " + oldgroupid;
		this.daoUtils.executeSql(updateSQL);
	}
	
	/**
	 * <li>说明：移除人员-工作组关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组id
	 * @return empid 人员id
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void delOmEmpgroup(Long groupid,Long empid) throws BusinessException {
		String hql = "from OmEmpgroup where id.groupid = "+groupid+" and id.empid = "+empid;
		List <OmEmpgroup> list = this.daoUtils.find(hql);
		for(OmEmpgroup eg : list){
			this.daoUtils.getHibernateTemplate().delete(eg);
		}
	}
	
	/**
	 * 根据工作组ID，删除其下所有直属人员的关联关系
	 * @param groupid 工作组ID
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public void deleteByGroupId(Long groupid) throws BusinessException {
		String hql = "from OmEmpgroup where id.groupid = " + String.valueOf(groupid);
		List <OmEmpgroup> list = this.daoUtils.find(hql);
		for(OmEmpgroup eg : list){
			this.daoUtils.getHibernateTemplate().delete(eg);
		}
	}
	
	/**
	 * 根据人员ID获取其与工作组的对应关系
	 * @param empid
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List <OmEmpgroup> findEmpGroupEntity(Long empid) throws BusinessException {
		String hql = "from OmEmpgroup where id.empid = " + empid;
		List <OmEmpgroup> list = this.daoUtils.find(hql);
		return list;
	}
}
