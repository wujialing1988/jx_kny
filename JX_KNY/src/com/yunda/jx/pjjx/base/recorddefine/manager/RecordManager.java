package com.yunda.jx.pjjx.base.recorddefine.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.recorddefine.entity.Record;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Record业务类,记录单
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="recordManager")
public class RecordManager extends JXBaseManager<Record, Record>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** RecordCard业务类,记录卡 */
	@Resource
	RecordCardManager recordCardManager;
	
	/** ReportTmplManage业务类,记录单报表模板管理 */
	@Resource
	ReportTmplManageManager reportTmplManageManager;
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(Record t) throws BusinessException {
		List<Record> list = this.daoUtils.find("From Record Where recordStatus = 0");
		if (null == list || list.size() <= 0) {
			return null;
		}
		for (Record entity : list) {
			if (entity.getIdx().equals(t.getIdx())) {
				continue;
			}
			if (t.getRecordNo().equals(entity.getRecordNo())) {
				return new String[]{"记录单编号：" + t.getRecordNo() + "已经存在，不能重复添加！"};
			}
		}
		return null;
	}
	
	/**
	 * <li>说明：级联删除【记录卡】和【记录单报表模板管理】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 待删除的idx数组
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		String idx = null;
		for (int i = 0; i < ids.length; i++) {
			// 记录单idx主键
			idx = (String)ids[0];
			// 级联删除【记录卡】
			List list = this.recordCardManager.getModelsByRecordIDX(idx);
			if (null != list && 0 < list.size()) {
				this.recordCardManager.logicDelete(list);
			} 
			// 级联删除【记录单报表模板管理】
			list = this.reportTmplManageManager.getModelsByRecordIDX(idx);
			if (null != list && 0 < list.size()) {
				this.reportTmplManageManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明：查询作业流程所用记录单
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @param isInWP true:表示查询作业流程使用到的记录单 false:表示查询作业流程未使用到的记录单
	 * @return Page<Record> 记录单集合
	 */
	private Page<Record> findPageListForWP(SearchEntity<Record> searchEntity, String wPIDX, boolean isInWP) {
		StringBuilder sb = new StringBuilder();
		sb.append("From Record Where recordStatus = ").append(Constants.NO_DELETE);
		if (isInWP) {
			sb.append(" And idx In (Select recordIDX From WPUnionRecord Where recordStatus = ");
		} else {
			sb.append(" And idx Not In (Select recordIDX From WPUnionRecord Where recordStatus = ");
		}
		sb.append(Constants.NO_DELETE).append(" And wPIDX ='").append(wPIDX).append("')");
		Record entity = searchEntity.getEntity();
		// 查询条件 - 编号
		if (!StringUtil.isNullOrBlank(entity.getRecordNo())) {
			sb.append(" And recordNo Like '%").append(entity.getRecordNo()).append("%'");
		}
		// 查询条件 - 名称
		if (!StringUtil.isNullOrBlank(entity.getRecordName())) {
			sb.append(" And recordName Like '%").append(entity.getRecordName()).append("%'");
		}
		// 查询条件 - 描述
		if (!StringUtil.isNullOrBlank(entity.getRecordDesc())) {
			sb.append(" And recordDesc Like '%").append(entity.getRecordDesc()).append("%'");
		}
		// 排序字段
		sb.append(HqlUtil.getOrderHql(searchEntity.getOrders()));
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	/**
	 * <li>说明：查询作业流程所用记录单
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @return Page<Record> 记录单集合
	 */
	public Page<Record> findPageListForWP(SearchEntity<Record> searchEntity, String wPIDX) {
		return this.findPageListForWP(searchEntity, wPIDX, true);
	}
	
	
	/**
	 * <li>说明：查询作业流程所用记录单 - 候选记录单
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @return Page<Record> 记录单集合
	 */
	public Page<Record> findPageListForWPSelect(SearchEntity<Record> searchEntity, String wPIDX) {
		return this.findPageListForWP(searchEntity, wPIDX, false);
	}
	
}