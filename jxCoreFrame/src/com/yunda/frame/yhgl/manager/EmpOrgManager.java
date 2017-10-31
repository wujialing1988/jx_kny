package com.yunda.frame.yhgl.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmporg;
import com.yunda.frame.yhgl.entity.OmEmporgId;
import com.yunda.frame.yhgl.entity.OmOrganization;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="empOrgManager")
public class EmpOrgManager extends JXBaseManager<OmEmporg,OmEmporg>{
	
	/** 组织机构查询接口 */
	@Resource(name="omOrganizationManager")
	private IOmOrganizationManager OmOrganizationManager;
	/**
	 * <li>说明：新增/更新人员-机构关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void addEmpOrgCorrelation(Long orgid, Long empid, String isMain){
		OmOrganization org = OmOrganizationManager.findByEmpId(empid); //调用查询接口，查询该用户是否有对应的机构
		if(org == null) {
			//用户没有对应的机构，执行新增操作
			OmEmporgId eoId = new OmEmporgId();
			OmEmporg eo = new OmEmporg();
			eoId.setEmpid(empid);
			eoId.setOrgid(orgid);
			eo.setId(eoId);
			eo.setIsmain(isMain);
			daoUtils.saveOrUpdate(eo);
		} else {
			//用户已存在对应机构，如与当前机构id不一致，执行更新操作
			if(org.getOrgid()!=orgid){
				String sql = "delete Om_Empposition where positionid in (select positionid from om_position where orgid = "+org.getOrgid()+") and empid = " + empid; //【删除人员-岗位关联关系】
				daoUtils.executeSql(sql);
				sql = "update OM_EMPORG set orgid = " + orgid + ", ismain = '"+ isMain + "' where empid = " + empid;
				daoUtils.executeSql(sql);
			}
		}
	}
	/**
	 * 根据人员ID删除人员-机构关联关系
	 * @param empId 人员ID
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public void deleteByEmployeeId(Long empId) throws BusinessException {
		String hql = "from OmEmporg where id.empid = "+empId;
		List <OmEmporg> list = this.daoUtils.find(hql);
		for(OmEmporg eg : list){
			this.daoUtils.getHibernateTemplate().delete(eg);
		}
	}
	
	/**
	 * 删除机构下的机构与人员关联关系数据
	 * @param orgId 机构ID
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public void deleteByOrganizationId(Long orgId) throws BusinessException {
		String hql = "from OmEmporg where id.orgid = "+orgId;
		List <OmEmporg> list = this.daoUtils.find(hql);
		EmployeeManager employeeManager = (EmployeeManager)Application.getSpringApplicationContext().getBean("employeeManager");
		Long empid = null;
		for(OmEmporg eg : list){
			empid = eg.getId().getEmpid();
			this.daoUtils.getHibernateTemplate().delete(eg); //【删除人员-机构关联关系】
			employeeManager.deleteByIds(empid);//【删除人员】
		}
	}
}
