package com.yunda.jx.pjjx.partsrdp.wpinst.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeSeq;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpNodeSeq业务类,作业流程节点前后置关系
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpNodeSeqManager")
public class PartsRdpNodeSeqManager extends JXBaseManager<PartsRdpNodeSeq, PartsRdpNodeSeq>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	
	/**
	 * <li>获取【配件检修作业节点】所有直接“后置节点”关系
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @param wpNodeIDX 流程节点主键
	 * @return List<PartsRdpNodeSeq> 作业流程节点前后置关系实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpNodeSeq> getDirectAfters(String rdpIDX, String wpNodeIDX) {
		String hql = "From PartsRdpNodeSeq Where recordStatus = 0 And rdpIDX = ? And preWPNodeIDX = ?";
		return this.daoUtils.find(hql, new Object[]{rdpIDX, wpNodeIDX});
	}
	
	/**
	 * <li>检验指定的“作业节点”和“流程节点主键”所表示的【配件检修作业节点】是否有直接“后置节点”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @param wpNodeIDX 流程节点主键
	 * @return boolean 如果有直接后置节点则返回true，否则返回false
	 */
	public boolean hasDirectAfters(String rdpIDX, String wpNodeIDX) {
		String hql = "Select Count(*) From PartsRdpNodeSeq Where recordStatus = 0 And rdpIDX = ? And preWPNodeIDX = ?";
		Object object = this.daoUtils.findSingle(hql, new Object[]{rdpIDX, wpNodeIDX});
		if (null == object) {
			return false;
		}
		int count = ((Long)object).intValue();
		return count <= 0 ? false : true;
		
	}
	/**
	 * <li>检验指定的“作业节点”和“流程节点主键”所表示的【配件检修作业节点】是否有直接“前置节点”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @param wpNodeIDX 流程节点主键
     * @return boolean 如果有直接前置节点则返回true，否则返回false
	 */
	public boolean hasDirectBefores(String rdpIDX, String wpNodeIDX) {
		String hql = "Select Count(*) From PartsRdpNodeSeq Where recordStatus = 0 And rdpIDX = ? And wPNodeIDX = ?";
		Object object = this.daoUtils.findSingle(hql, new Object[]{rdpIDX, wpNodeIDX});
		if (null == object) {
			return false;
		}
		int count = ((Long)object).intValue();
		return count <= 0 ? false : true;
		
	}
	
}