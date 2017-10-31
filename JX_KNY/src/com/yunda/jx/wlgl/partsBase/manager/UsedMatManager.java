package com.yunda.jx.wlgl.partsBase.manager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.jx.wlgl.partsBase.entity.UsedMat;
import com.yunda.jx.wlgl.partsBase.entity.UsedMatDetail;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UsedMat业务类,常用物料清单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="usedMatManager")
public class UsedMatManager extends JXBaseManager<UsedMat, UsedMat>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 常用物料清单明细业务类 */
	@Resource
	private UsedMatDetailManager usedMatDetailManager;
	
	/** UsedMatPerson业务类,常用物料清单使用人 */
	@Resource
	private UsedMatPersonManager usedMatPersonManager;
	
	/**
	 * 
	 * <li>说明：保存常用物料清单及明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param usedMat 常用物料清单 实体
	 * @param detailList  常用物料清单明细 实体集合
	 * @return
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UsedMat saveUsedMatAndDetail(UsedMat usedMat, UsedMatDetail[] detailList)throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException{
		if (null != usedMat.getIdx()) {
			UsedMat entity = this.getModelById(usedMat.getIdx());
			BeanUtils.copyProperties(entity, usedMat);
			this.saveOrUpdate(entity);
		} else {
			this.saveOrUpdate(usedMat);
		}
		// 获取物料信息清单实体idx主键
		String usedMatIdx = usedMat.getIdx();
		// 保存物料清单信息时，将当前数据操作者的信息保存到【常用物料清单使用人】数据表中 
		this.usedMatPersonManager.insertByMatInWh(usedMat);		
		List<UsedMatDetail> entityList = new ArrayList<UsedMatDetail>();
		// 根据物料信息清单主键获得该物料信息清单暂存的物料信息清单明细
		List<UsedMatDetail> oList = this.usedMatDetailManager.getModeList(usedMatIdx);
		// 检验以暂存的物料信息清单明细是否被删除，如果页面进行了删除，则在后台同步更新
		for (UsedMatDetail oDetail : oList) {
			if (isDeleted(oDetail, detailList)) {
				oDetail = EntityUtil.setSysinfo(oDetail);
				oDetail = EntityUtil.setDeleted(oDetail);
				entityList.add(oDetail);
			}
		}
		for (UsedMatDetail detail : detailList) {
			UsedMatDetail entity = null;
			if (null != detail.getIdx()) {
				entity = this.usedMatDetailManager.getModelById(detail.getIdx());
			} else {
				entity = detail;
				if (null == entity.getUsedMatIdx()) {
					entity.setUsedMatIdx(usedMatIdx);
				}
			}
			entity = EntityUtil.setSysinfo(entity);
			//设置逻辑删除字段状态为未删除
			entity = EntityUtil.setNoDelete(entity);
			entityList.add(entity);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
		return usedMat;
	}
	
	/**
	 * <li>说明：检验数据库中已存储的物料清单明细，是否已经在页面进行了删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param oDetail
	 * @param detailList
	 * @return
	 */
	private boolean isDeleted(UsedMatDetail oDetail, UsedMatDetail[] detailList) {
		for (UsedMatDetail detail : detailList) {
			if (oDetail.getIdx().equals(detail.getIdx())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <li>说明：重写删除方法，逻辑删除常用物料清单及明细
	 * <li>创建人：程梅
	 * <li>创建日期：2014-09-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 *  @param ids 常用物料清单idx主键数组
	 */
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		List<UsedMat> entityList = new ArrayList<UsedMat>();
		for (Serializable id : ids) {
			//查询明细
			List<UsedMatDetail> detailList = usedMatDetailManager.getModeList(id.toString());
			if(detailList != null && detailList.size() > 0){
				this.usedMatDetailManager.logicDelete(detailList);
			}
			UsedMat t = getModelById(id);
			entityList.add(t);
		}
		this.logicDelete(entityList);
	}
	
}