package com.yunda.frame.yhgl.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmGroup;
import com.yunda.frame.yhgl.entity.OmGroupposi;
import com.yunda.frame.yhgl.entity.OmGroupposiId;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工作组-岗位关联
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="groupposiManager")
public class GroupposiManager extends JXBaseManager <OmGroupposi,OmGroupposi>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 工作组查询接口 */
	@Resource(name="omGroupManager")
	private IOmGroupManager omGroupManager; 
	
	/**
	 * <li>说明：新增/更新工作组-岗位关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void addGroupposiCorrelation(Long groupid, Long positionid){
		OmGroup omGroup = omGroupManager.findByPosId(positionid);
		if(omGroup == null){
			//岗位没有对应的工作组，执行新增操作
			OmGroupposiId gpId = new OmGroupposiId();
			OmGroupposi gp = new OmGroupposi();
			gpId.setGroupid(groupid);
			gpId.setPositionid(positionid);
			gp.setId(gpId);
			this.daoUtils.saveOrUpdate(gp);
		} else {
			//岗位存在对应的工作组，对比其工作组id，如果不一致，则执行更新操作
			if(omGroup.getGroupid()!=groupid){
				String sql = "update OM_GROUPPOSI set groupid = " + groupid + " where positionid = " + positionid;
				daoUtils.executeSql(sql);
			}
		}
	}
	
	/**
	 * <li>说明：删除该工作组与岗位的关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 人员id数组
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void deleteByPositionId(Long groupid) throws BusinessException {
		if(groupid == null) return;
		try {
			String hql = "from OmGroupposi where id.groupid = " + String.valueOf(groupid);
			List <OmGroupposi> ogplist = this.daoUtils.find(hql); //根据工作组ID获取与岗位关联的数据集合
			if(ogplist == null || ogplist.size()<1) return;
			PositionManager positionManager = (PositionManager)Application.getSpringApplicationContext().getBean("positionManager");
			for(OmGroupposi ogp : ogplist){
				this.daoUtils.getHibernateTemplate().delete(ogp); //【物理删除工作组-岗位的关联信息】
				positionManager.deleteByIds(ogp.getId().getPositionid()); //【物理删除工作组下的岗位】
				
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：根据岗位ID，删除该岗位与工作组的关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-04-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 人员id数组
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void deleteByPositionId2(Long positionid) throws BusinessException {
		if(positionid == null) return;
		try {
			String hql = "from OmGroupposi where id.positionid = " + String.valueOf(positionid);
			List <OmGroupposi> ogplist = this.daoUtils.find(hql); //根据工作组ID获取与岗位关联的数据集合
			if(ogplist == null || ogplist.size()<1) return;
			for(OmGroupposi ogp : ogplist){
				daoUtils.getHibernateTemplate().delete(ogp);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
}
