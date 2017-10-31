package com.yunda.jx.pjjx.base.recorddefine.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordCardQC;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RecordCardQC业务类,质量检查定义
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="recordCardQCManager")
public class RecordCardQCManager extends JXBaseManager<RecordCardQC, RecordCardQC>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：根据“记录卡主键”获取【质量检查定义】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param recordCardIDX 记录卡主键
	 * @return List<RecordCardQC> 质量检查定义实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<RecordCardQC> getModelsByRecordCardIDX(String recordCardIDX) {
		String hql = "From RecordCardQC Where recordStatus = 0 And recordCardIDX = ?";
		return this.daoUtils.find(hql, new Object[]{recordCardIDX});
	}
	
	/**
	 * <li>说明：检验已存储的【质量检查定义】是否被删除，其判断依据是质量检查项名称
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param recordCardQC 质量检查定义 实体
	 * @param qcItemNames 质量检查项 名称数组
	 * @return 如果已经删除则返回true，否则返回false
	 */
	public boolean isDeleted(RecordCardQC recordCardQC, String[] qcItemNames) {
		for (String qcItemName : qcItemNames) {
			// 如果存在已相应“质量检查项名称”的实体，则判断为未删除
			if (qcItemName.equals(recordCardQC.getQCItemName())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <li>说明：检验已存储的【质量检查定义】是否被删除，其判断依据是质量检查项名称
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param recordCardQC 质量检查定义实体
	 * @param qcContent 质量检验 是以字符“|”分割的多个质量检查项名称组成的字符串，例如：“互检|工长检|质检”
	 * @return 如果已经删除则返回true，否则返回false
	 */
	public boolean isDeleted(RecordCardQC recordCardQC, String qcContent) {
		if (null == qcContent || qcContent.trim().length() <= 0) {
			return true;
		}
		String[] qcItemNames = qcContent.split("\\|");
		return this.isDeleted(recordCardQC, qcItemNames);
	}
	
	/**
	 * <li>说明：检验【质量检查定义】是否存在
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param qCItemName 检查项名称
	 * @param recordCardIDX  记录卡主键
	 * @return 如果存在则返回true，否则返回false
	 */
	public boolean isExist(String qCItemName, String recordCardIDX) {
		RecordCardQC qc = this.getModelByQCItemName(qCItemName, recordCardIDX);
		return null == qc ? false : true;
	}
	
	/**
	 * <li>说明：根据“检查项名称”获取【质量检查定义】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param qCItemName 检查项名称
	 * @param recordCardIDX  记录卡主键
	 * @return RecordCardQC 质量检查定义实体
	 */
	public RecordCardQC getModelByQCItemName (String qCItemName, String recordCardIDX) {
		String hql = "From RecordCardQC Where recordStatus = 0 And qCItemName = ? And recordCardIDX = ?";
		return (RecordCardQC) this.daoUtils.findSingle(hql, new Object[]{qCItemName, recordCardIDX});
	}
	
}