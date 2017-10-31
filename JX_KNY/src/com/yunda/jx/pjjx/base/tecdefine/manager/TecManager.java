package com.yunda.jx.pjjx.base.tecdefine.manager;

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
import com.yunda.jx.pjjx.base.tecdefine.entity.Tec;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCard;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: Tec业务类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:23:52
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="tecManager")
public class TecManager extends JXBaseManager<Tec, Tec>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Resource
	TecCardManager tecCardManager;
	
	/**
	 * <li>说明：验证“工艺编号”字段的唯一性
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(Tec t) {
		String tecNo = t.getTecNo();
		String hql = "From Tec Where recordStatus = 0 And tecNo = ?";
		List<Tec> list = this.daoUtils.find(hql, new Object[]{tecNo});
		if (null == list || list.size() <= 0) {
			return null;
		}
		for (Tec entity : list) {
			if (entity.getIdx().equals(t.getIdx())) {
				continue;
			}
			if (t.getTecNo().equals(entity.getTecNo())) {
				return new String[]{"工艺编号：" + t.getTecNo() + "已经存在，不能重复添加！"};
			}
		}
		return null;
	}

	/**
	 * <li>说明：级联删除【配件检修工艺卡】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 待删除的idx数组
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		// 级联删除【配件检修工艺卡】
		for (int i = 0; i < ids.length; i++) {
			List<TecCard> list = this.tecCardManager.getModelsByTecIDX((String)ids[0]);
			if (null != list && 0 < list.size()) {
				this.tecCardManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明：查询作业流程所用工艺
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @param isInWP true:表示查询作业流程使用到的工艺 false:表示查询作业流程未使用到的工艺
	 * @return Page<Tec> 检修工艺实体集合
	 */
	private Page<Tec> findPageListForWP(SearchEntity<Tec> searchEntity, String wPIDX, boolean isInWP) {
		StringBuilder sb = new StringBuilder();
		sb.append("From Tec Where recordStatus = ").append(Constants.NO_DELETE);
		if (isInWP) {
			sb.append(" And idx In (Select tecIDX From WPUnionTec Where recordStatus = ");
		} else {
			sb.append(" And idx Not In (Select tecIDX From WPUnionTec Where recordStatus = ");
		}
		sb.append(Constants.NO_DELETE).append(" And wPIDX ='").append(wPIDX).append("')");
		Tec entity = searchEntity.getEntity();
		// 查询条件 - 编号
		if (!StringUtil.isNullOrBlank(entity.getTecNo())) {
			sb.append(" And tecNo Like '%").append(entity.getTecNo()).append("%'");
		}
		// 查询条件 - 名称
		if (!StringUtil.isNullOrBlank(entity.getTecName())) {
			sb.append(" And tecName Like '%").append(entity.getTecName()).append("%'");
		}
		// 查询条件 - 描述
		if (!StringUtil.isNullOrBlank(entity.getTecDesc())) {
			sb.append(" And tecDesc Like '%").append(entity.getTecDesc()).append("%'");
		}
		// 排序字段
		sb.append(HqlUtil.getOrderHql(searchEntity.getOrders()));
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：查询作业流程所用工艺
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @return Page<Tec> 检修工艺实体集合
	 */
	public Page<Tec> findPageListForWP(SearchEntity<Tec> searchEntity, String wPIDX) {
		return this.findPageListForWP(searchEntity, wPIDX, true);
	}
	
	
	/**
	 * <li>说明：查询作业流程所用工艺 - 候选工艺
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 	查询实体
	 * @param wPIDX			作业流程主键
	 * @return Page<Tec> 检修工艺实体集合
	 */
	public Page<Tec> findPageListForWPSelect(SearchEntity<Tec> searchEntity, String wPIDX) {
		return this.findPageListForWP(searchEntity, wPIDX, false);
	}
	
}