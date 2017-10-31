package com.yunda.sb.repair.scope.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.sb.base.order.AbstractOrderManager2;
import com.yunda.sb.repair.scope.entity.RepairScope;
import com.yunda.sb.repair.scope.entity.RepairScopeDetails;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairScopeDetails管理器，数据表：E_REPAIR_SCOPE_DETAILS
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service
public class RepairScopeDetailsManager extends AbstractOrderManager2<RepairScopeDetails, RepairScopeDetails> {

	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-07
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return int 记录总数
	 */
	@Override
	public int count(RepairScopeDetails t) {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And scopeIdx = ?";
		return this.daoUtils.getCount(hql, t.getScopeIdx());
	}

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-07
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t	实体对象
	 * @return 结果集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RepairScopeDetails> findAll(RepairScopeDetails t) {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And scopeIdx = ?";
		return this.daoUtils.find(hql, new Object[] { t.getScopeIdx() });
	}

	/**
	 * <li>说明：获取指定顺序号后的所有实体，即：如参数t的顺序号为1，则获取该实体排序范围内，所有顺序号大于1的实体列表(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-07
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 指定顺序号的实体
	 * @return List<T> 实体集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RepairScopeDetails> findAllBySN(RepairScopeDetails t) throws Exception {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And seqNo >= ? And scopeIdx = ?";
		return this.daoUtils.find(hql, new Object[] { t.getSeqNo(), t.getScopeIdx() });
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RepairScopeDetails> updateMoveBottomHelper(RepairScopeDetails entity) {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And seqNo > ? And scopeIdx = ?";
		return this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getScopeIdx() });
	}

	@Override
	public RepairScopeDetails updateMoveDownHelper(RepairScopeDetails entity) {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And seqNo = ? And scopeIdx = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		return (RepairScopeDetails) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1, entity.getScopeIdx() });
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RepairScopeDetails> updateMoveTopHelper(RepairScopeDetails entity) {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And seqNo < ? And scopeIdx = ?";
		return this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getScopeIdx() });
	}

	@Override
	public RepairScopeDetails updateMoveUpHelper(RepairScopeDetails entity) {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And seqNo = ? And scopeIdx = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		return (RepairScopeDetails) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1, entity.getScopeIdx() });
	}

	/**
	 * <li>说明：根据设备检修范围idx主键获取下属的检修范围明细实体集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scopeIdx 设备检修范围idx主键
	 * @return 设备检修范围明细实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<RepairScopeDetails> getModelsByScopeIdx(String scopeIdx) {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And scopeIdx = ?";
		return this.daoUtils.find(hql, scopeIdx);
	}

	/**
	 * <li>说明：保存设备检修范围作业内容
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scope 正在保存的设备检修范围实体
	 * @param scopeIdx 通过选择新增选择的设备检修范围idx主键，用于查询该范围下的作业内容
	 * @throws NoSuchFieldException
	 */
	public void save(RepairScope scope, String scopeIdx) throws NoSuchFieldException {
		List<RepairScopeDetails> rsDetails = this.getModelsByScopeIdx(scopeIdx);
		if (null == rsDetails || rsDetails.isEmpty()) {
			return;
		}
		RepairScopeDetails entity = null;
		for (RepairScopeDetails t : rsDetails) {
			entity = new RepairScopeDetails();
			entity.setScopeIdx(scope.getIdx()); // 检修范围idx主键
			entity.setWorkContent(t.getWorkContent()); // 作业内容
			entity.setProcessStandard(t.getProcessStandard()); // 工艺标准
			entity.setSeqNo(this.getNextSeqNo(scope.getIdx())); // 顺序号
			this.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：获取下一个可用的顺序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scopeIdx 设备检修范围主键
	 * @return 下一个可用的顺序号
	 */
	private Integer getNextSeqNo(String scopeIdx) {
		String hql = "From RepairScopeDetails Where recordStatus = 0 And scopeIdx = ? Order By seqNo DESC";
		Object obj = this.daoUtils.findSingle(hql, scopeIdx);
		if (null == obj) {
			return 1;
		}
		RepairScopeDetails t = (RepairScopeDetails) obj;
		if (null == t.getSeqNo()) {
			return 1;
		}
		return t.getSeqNo() + 1;
	}

	/**
	 * <li>说明：导入设备检修范围明细数据
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param map 已存储的设备检修范围实体散列表，key：检修范围编号，value：设备检修范围实体对象
	 * @param workbook 设备检修范围_导入模板.xls工作薄对象
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	public void insertByImport(Map<String, RepairScope> map, Workbook workbook) throws NoSuchFieldException {
		// sheet2 检修检测项
		Sheet sheet2 = workbook.getSheetAt(2);
		// 获取【检修检测项】sheet的最后一行的索引
		int lastRowNum = sheet2.getLastRowNum();
		Row row = null;
		RepairScopeDetails rsd = null;
		Map<String, List<RepairScopeDetails>> rsdMap = new HashMap<String, List<RepairScopeDetails>>();
		for (int i = RepairScopeManager.IMPORT_VALID_ROW_NUM; i <= lastRowNum; i++) {
			row = sheet2.getRow(i);
			// Modified by hetao on 2017-02-15 解决row可能为空时的空指针异常
			if (null == row || null == row.getCell(0) || Cell.CELL_TYPE_BLANK == row.getCell(0).getCellType()) {
				continue;
			}

			// 检修范围编号
			String repaireScopeNo = null;
			try {
				repaireScopeNo = row.getCell(0).getStringCellValue();
			} catch (IllegalStateException e) {
				Double d = row.getCell(0).getNumericCellValue();
				repaireScopeNo = d.intValue() + "";
			}
			RepairScope repairScope = map.get(repaireScopeNo);
			if (null == repairScope) {
				continue;
			}
			String scopeIdx = repairScope.getIdx(); // 设备检修范围主键
			String workContent = row.getCell(2).getStringCellValue(); // 作业项（作业内容）
			// 验证是否已经存在
			if (null != this.getModel(workContent, scopeIdx)) {
				continue;
			}
			String processStandard = row.getCell(3).getStringCellValue(); // 工艺标准

			rsd = new RepairScopeDetails();
			rsd.setWorkContent(workContent);
			rsd.setProcessStandard(processStandard);
			rsd.setScopeIdx(scopeIdx);
			List<RepairScopeDetails> list = rsdMap.get(scopeIdx);
			if (null == list) {
				list = new ArrayList<RepairScopeDetails>();
				rsdMap.put(scopeIdx, list);
			}
			list.add(rsd);
		}
		if (rsdMap.isEmpty()) {
			return;
		}
		// 执行数据存储
		Set<Entry<String, List<RepairScopeDetails>>> entrySet = rsdMap.entrySet();
		for (Iterator<Entry<String, List<RepairScopeDetails>>> j = entrySet.iterator(); j.hasNext();) {
			Entry<String, List<RepairScopeDetails>> next = j.next();
			List<RepairScopeDetails> list = next.getValue();
			// 获取下一个可用的顺序号
			// Modified by hetao on 2017-02-22 修改seqNo初始值获取的方式（原：int seqNo = 0;），避免第二次导入部分数据时，排序错误的情况发生
			int seqNo = this.getNextSeqNo(list.get(0).getScopeIdx());
			for (RepairScopeDetails entity : list) {
				entity.setSeqNo(seqNo++);
				this.saveOrUpdate(entity);
			}
		}

	}

	/**
	 * <li>说明：根据“安全风险点”和“设备检修范围主键”获取安全风险点实体对象
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param riskItem 风险点
	 * @param scopeIdx 范围主键
	 * @return 设备检修安全风险点实体对象
	 */
	private RepairScopeDetails getModel(String workContent, String scopeIdx) {
		String hql = "From RepairScopeDetails Where workContent = ? And scopeIdx = ? And recordStatus = 0";
		return (RepairScopeDetails) this.daoUtils.findSingle(hql, workContent, scopeIdx);
	}

}
