package com.yunda.jx.wlgl.expend.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.util.SqlFilter;
import com.yunda.jx.wlgl.expend.entity.MatTrainExpendAccount;
import com.yunda.jx.wlgl.expend.entity.MatTrainExpendAccountDetail;
import com.yunda.jx.wlgl.outstock.entity.MatOutWHTrain;
import com.yunda.jx.wlgl.outstock.entity.MatOutWHTrainDetail;
import com.yunda.jx.wlgl.outstock.manager.MatOutWHTrainDetailManager;
import com.yunda.jx.wlgl.partsBase.manager.WhOrgManager;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatTrainExpendAccount业务类,机车用料消耗记录
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-28 下午02:30:20
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="matTrainExpendAccountManager")
public class MatTrainExpendAccountManager extends JXBaseManager<MatTrainExpendAccount, MatTrainExpendAccount>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** MatOutWHTrainDetail业务类,机车用料明细 */
	@Resource
	MatOutWHTrainDetailManager matOutWHTrainDetailManager;
	
	/** 机构服务类 */
	@Resource
	OmOrganizationManager omOrganizationManager;
	
	/** MATSTOCK业务类,物料库存台账 */
	@Resource
	MatStockManager matStockManager;
	
	/** WhOrg业务类,库房与班组关系维护（用于消耗即扣库的模式） */
	@Resource
	WhOrgManager whOrgManager;

	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体
	 * @return 
	 */
	public Page<MatTrainExpendAccount> findPageList(SearchEntity<MatTrainExpendAccount> searchEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatTrainExpendAccount Where 1=1");
//		Long orgid = SystemContext.getOmOrganization().getOrgid();
//		sb.append(" And expendOrgId = '").append(orgid).append("'");
		MatTrainExpendAccount entity = searchEntity.getEntity();
		// 查询条件 - 车型
		if (!StringUtil.isNullOrBlank(entity.getTrainTypeIDX())) {
			sb.append(" And trainTypeIDX = '").append(entity.getTrainTypeIDX()).append("'");
		}
		// 查询条件 - 车号
		if (!StringUtil.isNullOrBlank(entity.getTrainNo())) {
			sb.append(" And trainNo = '").append(entity.getTrainNo()).append("'");
		}
		// 查询条件 - 修程
		if (!StringUtil.isNullOrBlank(entity.getXcId())) {
			sb.append(" And xcId = '").append(entity.getXcId()).append("'");
		}
		// 查询条件 - 修次
		if (!StringUtil.isNullOrBlank(entity.getRtId())) {
			sb.append(" And rtId = '").append(entity.getRtId()).append("'");
		}
		// 查询条件 - 消耗班组
		if (!StringUtil.isNullOrBlank(entity.getExpendOrg())) {
			sb.append(" And expendOrg Like '%").append(entity.getExpendOrg()).append("%'");
		}
		// 查询条件 - 物料编码
		if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
			sb.append(" And matCode Like '%").append(entity.getMatCode()).append("%'");
		}
		// 查询条件 - 物料描述
		if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
			sb.append(" And matDesc Like '%").append(entity.getMatDesc()).append("%'");
		}
		// 查询条件 - 数据来源
		if (!StringUtil.isNullOrBlank(entity.getDataSource())) {
			sb.append(" And dataSource = '").append(entity.getDataSource()).append("'");
		}
		// 查询条件 - 消耗日期
		SqlFilter.filterDate(sb, "expendDate", entity.getStartDate(), entity.getEndDate());
		// 排序字段
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			sb.append(" Order By ");
			sb.append(orders[0].toString());
			for (int i = 1; i < orders.length; i++) {
				sb.append(", ");
				sb.append(orders[i].toString());
			}
		} else {
			sb.append(" Order By expendDate DESC");
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：根据机车用料单保存该用料单的机车用料明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-28
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matOutWHTrain 机车用料单 实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void save(MatOutWHTrain matOutWHTrain) throws BusinessException, NoSuchFieldException {
		MatTrainExpendAccount account = null;
		List<MatOutWHTrainDetail> list = matOutWHTrainDetailManager.getModelsByMatOutWHTrainIDX(matOutWHTrain.getIdx());
		
		for (MatOutWHTrainDetail detail : list) {
			account = new MatTrainExpendAccount();
			// 车型编码
			account.setTrainTypeIDX(matOutWHTrain.getTrainTypeIDX());
			// 车型简称
			account.setTrainTypeShortName(matOutWHTrain.getTrainTypeShortName());
			// 车号
			account.setTrainNo(matOutWHTrain.getTrainNo());
			// 修程编码
			account.setXcId(matOutWHTrain.getXcId());
			// 修程名称
			account.setXcName(matOutWHTrain.getXcName());
			// 修次编码 
			account.setRtId(matOutWHTrain.getRtId());
			// 修次名称
			account.setRtName(matOutWHTrain.getRtName());
			// 消耗机构ID
			account.setExpendOrgId(matOutWHTrain.getGetOrgId());
			// 消耗机构名称
			account.setExpendOrg(matOutWHTrain.getGetOrg());
			// 消耗机构序列
			account.setExpendOrgSeq(matOutWHTrain.getGetOrgSeq());
			// 消耗时间
			account.setExpendDate(matOutWHTrain.getGetDate());
			// 物料编码
			account.setMatCode(detail.getMatCode());
			// 物料描述
			account.setMatDesc(detail.getMatDesc());
			// 单位
			account.setUnit(detail.getUnit());
			// 数量
			account.setQty(detail.getQty());
			// 单价
			account.setPrice(detail.getPrice());
			// 登账人
			account.setRegistEmp(matOutWHTrain.getRegistEmp());
			// 登帐日期
			account.setRegistDate(matOutWHTrain.getRegistDate());
			// 设置数据来源 - 出库
//			account.setDataSource(MatTrainExpendAccount.CONST_STR_DATASOURCE_OUTWH);
			
			this.saveOrUpdate(account);
			// 更新【机车用料明细】 - 机车用料消耗记录主键
			detail.setMatTrainExpendAccountIDX(account.getIdx());
		}
		matOutWHTrainDetailManager.saveOrUpdate(list);
	}
	
	/**
	 * <li>说明：回滚操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 实体
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void updateRollBack(MatOutWHTrain matOutWHTrain) throws BusinessException, NoSuchFieldException {
		// 获取机车用料明细
		List<MatOutWHTrainDetail> list = matOutWHTrainDetailManager.getModelsByMatOutWHTrainIDX(matOutWHTrain.getIdx());
		String[] ids = new String[list.size()]; 
		for (int i = 0; i < list.size(); i++) {
			ids[i] = list.get(i).getMatTrainExpendAccountIDX();
		}
		if (ids.length > 0) {
			logicDelete(ids);
		}
	}

	/**
	 * <li>说明：修改机车用料消耗记录
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param acoount 可能包含修改属性的实体对象
	 * @return
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public String[] saveTemporary(MatTrainExpendAccount acoount) throws BusinessException, NoSuchFieldException {
		// 因为是修改操作，因此数据库中应有真正被修改的数据记录
		MatTrainExpendAccount matTrainExpendAccount = getModelById(acoount.getIdx());
		if (null == matTrainExpendAccount) {
			return new String[] { "数据异常" };
		}
		// 根据修改情况进行赋值
		this.setProperties(matTrainExpendAccount, acoount);
		this.saveOrUpdate(matTrainExpendAccount);
		return null;
	}
	
	/**
	 * <li>说明：新增
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param account
	 * @param detailList
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public String[] saveTemporaryForAdd(MatTrainExpendAccount account, MatTrainExpendAccountDetail[] detailList) throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
		List<MatTrainExpendAccount> entityList = new ArrayList<MatTrainExpendAccount>(detailList.length);
		MatTrainExpendAccount entity = null;
		for (MatTrainExpendAccountDetail detail : detailList) {
			entity = new MatTrainExpendAccount();
			// 拷贝公共属性
			BeanUtils.copyProperties(entity, account);
			// 设置私有属性
			entity.setMatCode(detail.getMatCode());			// 物料编码
			entity.setMatDesc(detail.getMatDesc());			// 物料描述
			entity.setQty(detail.getQty());					// 数量
			entity.setUnit(detail.getUnit());				// 计量单位
			entity.setPrice(detail.getPrice());				// 单价
			// 设置数据来源
//			entity.setDataSource(MatTrainExpendAccount.CONST_STR_DATASOURCE_EXPENDACCOUNT);
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
		return null;
	}

	/**
	 * <li>说明：根据实体属性修改情况进行赋值
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 数据库映射实体对象
	 * @param acoount 可能包含修改属性的实体对象
	 */
	private void setProperties(MatTrainExpendAccount entity, MatTrainExpendAccount acoount) {
		// 如果修改了“车型”
		if (!entity.getTrainTypeIDX().equals(acoount.getTrainTypeIDX())) {
			entity.setTrainTypeIDX(acoount.getTrainTypeIDX());
			entity.setTrainTypeShortName(acoount.getTrainTypeShortName());
		}
		// 如果修改了“车号”
		if (!entity.getTrainNo().equals(acoount.getTrainNo())) {
			entity.setTrainNo(acoount.getTrainNo());
		}
		// 如果修改了“修程”
		if (!entity.getXcId().equals(acoount.getXcId())) {
			entity.setXcId(acoount.getXcId());
			entity.setXcName(acoount.getXcName());
		}
		// 如果修改了“修次”
		if (null != entity.getRtId() && !entity.getRtId().equals(acoount.getRtId())) {
			entity.setRtId(entity.getRtId());
			entity.setRtName(entity.getRtName());
		}
		// 如果修改了“消耗班组”
		if (!entity.getExpendOrgId().equals(acoount.getExpendOrgId())) {
			entity.setExpendOrgId(acoount.getExpendOrgId());
			entity.setExpendOrg(acoount.getExpendOrg());
			// 验证“消耗班组序列”是否为空，如果为空则在后台进行设置
			if (StringUtil.isNullOrBlank(acoount.getExpendOrgSeq())) {
				OmOrganization organization = omOrganizationManager.getModelById(acoount.getExpendOrgId());
				entity.setExpendOrgSeq(organization.getOrgseq());
			} else {
				entity.setExpendOrgSeq(acoount.getExpendOrgSeq());
			}
		}
		// 如果修改了“消耗日期”
		if (!entity.getExpendDate().equals(acoount.getExpendDate())) {
			entity.setExpendDate(acoount.getExpendDate());
		}
		// 如果修改了“物料编码”
		if (!entity.getMatCode().equals(acoount.getMatCode())) {
			entity.setMatCode(acoount.getMatCode());
			entity.setMatDesc(acoount.getMatDesc());
			entity.setPrice(acoount.getPrice());
			entity.setUnit(acoount.getUnit());
		}
		// 如果修改了“数量”
		if (entity.getQty() != acoount.getQty()) {
			entity.setQty(acoount.getQty());
		}
	}

	/**
	 * <li>说明：登账
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 单次登账操作所包含记录的idx数组（目前单次操作只有一条记录）
	 * @return
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public synchronized String[] saveEntryAccount(String[] ids) throws BusinessException, NoSuchFieldException {
		if (isEntryAcount(ids)) {
			return new String[] { "正在操作的记录已经被登账，请刷新后重试！" };
		}
		List<MatTrainExpendAccount> entityList = new ArrayList<MatTrainExpendAccount>(ids.length);
		for (String idx : ids) {
			MatTrainExpendAccount entity = this.getModelById(idx);
			// 出库
			this.saveEntryAccount(entity);
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
		return null;
	}
	
	/**
	 * <li>说明：检验正在被登账的记录是否有已经被登账的
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids
	 * @return true: 有记录被登账 false：都未被登账
	 */
	private boolean isEntryAcount(String[] ids) {
		for (String idx : ids) {
			MatTrainExpendAccount entity = this.getModelById(idx);
		}
		return false;
	}

	/**
	 * <li>说明：退库时，修改“机车检修用料记录”的出库数量
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 机车检修用料明细实体
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void saveBackWh(MatOutWHTrainDetail entity) throws BusinessException, NoSuchFieldException {
		MatTrainExpendAccount account = this.getModelById(entity.getMatTrainExpendAccountIDX());
		if (null == account) {
			return;
		}
		// 计算公式：数量 = 出库数量 - 退库数量
		account.setQty(entity.getQty() - entity.getBackQty());
		this.saveOrUpdate(account);
	}
	

	/**
	 * <li>说明：根据【机车用料消耗记录】检验消耗配件库存数量是否充足
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matOutWHTrain 机车用料单 实体
	 * @return
	 */
	private String checkMatStockQty(MatTrainExpendAccount matTrainExpendAccount) {
		String whIdx = whOrgManager.getWhIdxByOrgId(matTrainExpendAccount.getExpendOrgId());
		if (null == whIdx) {
			return "组织机构[" + matTrainExpendAccount.getExpendOrg() + "]没有已维护的库房信息！";
		}
		// 库存信息
		MatStock matStock = matStockManager.getModel(whIdx, matTrainExpendAccount.getMatCode());
		if (null == matStock) {
			return "物料：" + matTrainExpendAccount.getMatDesc() + "(" + matTrainExpendAccount.getMatCode() + ")库存不足！";
		} else {
			int num = matStock.getQty() - matTrainExpendAccount.getQty();			// 计算出库后的库存数量
			if (num < 0) {
				return "物料：" + matTrainExpendAccount.getMatDesc() + "(" + matTrainExpendAccount.getMatCode() + ")库存不足！";
			}
		}
		return null;
	}

	/**
	 * <li>说明：根据【机车用料消耗记录】出库
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matTrainExpendAccount 机车用料消耗记录实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveEntryAccount(MatTrainExpendAccount matTrainExpendAccount) throws BusinessException, NoSuchFieldException {
		String errorMsg = this.checkMatStockQty(matTrainExpendAccount);
		if (null != errorMsg) {
			throw new StockLackingException(errorMsg);
		}
		// 库房idx主键
		String whIdx = whOrgManager.getWhIdxByOrgId(matTrainExpendAccount.getExpendOrgId());
		MatStock matStock = matStockManager.getModel(whIdx, matTrainExpendAccount.getMatCode());
		matStock.setQty(matStock.getQty() - matTrainExpendAccount.getQty());
		matStockManager.saveOrUpdate(matStock);
	}
	
	/**
	 * <li>说明：检验是否已经执行了回滚操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 实体类主键idx数组
	 * @return
	 */
	private String[] isRollBacked(String[] ids) {
		List<String> errorMsg = new ArrayList<String>();
		for (String idx: ids) {
			MatTrainExpendAccount entity = this.getModelById(idx);
			if (!SqlFilter.isInLastMonth(entity.getExpendDate())) {
				errorMsg.add("消耗日期超过一个月的单据不能回滚");
			}
		}
		if (0 < errorMsg.size()) {
			return errorMsg.toArray(new String[errorMsg.size()]);
		}
		return null;
	}
	
	/**
	 * <li>说明：回滚操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 实体类主键idx数组
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public String[] updateRollBack(String[] ids) throws BusinessException, NoSuchFieldException {
		// 检验是否已经回滚
		String[] checkMsg = this.isRollBacked(ids);
		if (null != checkMsg) {
			return checkMsg;
		}
		for (String idx : ids) {
			MatTrainExpendAccount entity = this.getModelById(idx);
			if (null != entity) {
				// 回滚库存记录
				this.updateRollBack(entity);
				this.saveOrUpdate(entity);
			}
		}
		return null;
	}
	
	/**
	 * <li>说明：回滚操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	private void updateRollBack(MatTrainExpendAccount entity) throws BusinessException, NoSuchFieldException {
		// 库存信息
		String whIdx = whOrgManager.getWhIdxByOrgId(entity.getExpendOrgId());
		MatStock matStock = matStockManager.getModel(whIdx, entity.getMatCode());
		// 更新库存数量
		matStock.setQty(matStock.getQty() + entity.getQty());
		matStockManager.saveOrUpdate(matStock);
	}
	
}