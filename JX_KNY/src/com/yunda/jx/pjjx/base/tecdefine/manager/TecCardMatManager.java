package com.yunda.jx.pjjx.base.tecdefine.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCardMat;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明： TecCardMat业务类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:22:40
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="tecCardMatManager")
public class TecCardMatManager extends JXBaseManager<TecCardMat, TecCardMat>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：验证“物料编码”字段的唯一性
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-23
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
	public String[] validateUpdate(TecCardMat t) {
		String hql = "From TecCardMat Where recordStatus = 0 And tecCardIDX = ?";
		List<TecCardMat> list = this.daoUtils.find(hql, new Object[]{t.getTecCardIDX()});
		if (null == list || list.size() <= 0) {
			return null;
		}
		for (TecCardMat entity : list) {
			if (entity.getIdx().equals(t.getIdx())) {
				continue;
			}
			if (t.getMatCode().equals(entity.getMatCode())) {
				return new String[]{"物料编码：" + t.getMatCode() + "已经存在，不能重复添加！"};
			}
		}
		return null;
	}
	
	/**
	 * <li>说明：根据“检修工艺卡主键”获取“配件检修所需物料”列表
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tecCardIDX 检修工艺卡主键
	 * @return List<TecCardMat> 配件检修所需物料集合
	 */
	@SuppressWarnings("unchecked")
	public List<TecCardMat> getModelsByTecCardIDX(String tecCardIDX) {
		String hql = "From TecCardMat Where recordStatus = 0 And tecCardIDX = ?";
		return this.daoUtils.find(hql, new Object[]{tecCardIDX});
	}

	/**
	 * <li>说明：保存 配件检修所需物料
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param list 配件检修所需物料集合
	 * @return String[]保存的验证消息
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public String[] saveTecCardMats(TecCardMat[] list) throws BusinessException, NoSuchFieldException {
		List<TecCardMat> entityList = new ArrayList<TecCardMat>();
		for(TecCardMat mat : list) {
			// 验证”物料编码“是否唯一
			String[] msg = this.validateUpdate(mat);
			if (null != msg) {
				return msg;
			}
			entityList.add(mat);
		}
		if (entityList.size() > 0) {
			this.saveOrUpdate(entityList);
		}
		return null;
	}
	
	
	/**
	 * <li>说明：分页查询，联合查询物料信息表，用以查询物料的最新单价
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体
	 * @return Page<TecCardMat> 物料实体集合
	 */
	public Page<TecCardMat> queryPageList(SearchEntity<TecCardMat> searchEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("Select new TecCardMat(a.idx, a.tecCardIDX, a.matCode, a.matDesc, a.unit, a.qty, b.price) From TecCardMat a, MatTypeList b Where a.matCode = b.matCode And a.recordStatus = 0 And b.recordStatus = 0");
		TecCardMat entity = searchEntity.getEntity();
		// 查询条件 - 检修工艺卡idx主键
		if (!StringUtil.isNullOrBlank(entity.getTecCardIDX())) {
			sb.append(" And a.tecCardIDX = '").append(entity.getTecCardIDX()).append("'");
		}
		// 查询条件 - 物料编码
		if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
			sb.append(" And a.matCode Like '%").append(entity.getMatCode()).append("%'");
		}
		// 查询条件 - 物料描述
		if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
			sb.append(" And a.matDesc Like '%").append(entity.getMatDesc()).append("%'");
		}
		// 排序
		Order[] orders = searchEntity.getOrders();
		if (null != orders) {
			for (Order order : orders) {
				if (order.toString().contains("price")) {
					sb.append(" Order By b.").append(order.toString());
				} else {
					sb.append(" Order By a.").append(order.toString());
				}
			}
		}
		String hql = sb.toString();
		String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
		return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
}