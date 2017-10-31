package com.yunda.sb.repair.scope.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.sb.repair.scope.entity.RepairScope;
import com.yunda.sb.repair.scope.entity.RepairScopeRiskWarning;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairScopeRiskWarning管理器，数据表：E_REPAIR_RISK_WARNING
 * <li>创建人：何涛
 * <li>创建日期：2016年7月8日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service
public class RepairScopeRiskWarningManager extends JXBaseManager<RepairScopeRiskWarning, RepairScopeRiskWarning> {

	/**
	 * <li>方法说明：重写保存更新前验证，验证数据的唯一性
	 * <li>创建人： 何涛
	 * <li>创建日期：2016年7月27日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public String[] validateUpdate(RepairScopeRiskWarning t) {
		String hql = "From RepairScopeRiskWarning Where recordStatus = 0 And riskItem = ? And scopeIdx = ?";
		RepairScopeRiskWarning entity = (RepairScopeRiskWarning) this.daoUtils.findSingle(hql, t.getRiskItem(), t.getScopeIdx());
		if (null != entity && entity.getRiskItem().equals(t.getRiskItem())) {
			return new String[] { "不可以添加重复的数据！" };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：导入设备检修范围安全风险点
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
		// sheet2 安全风险点
		Sheet sheet3 = workbook.getSheetAt(3);
		// 获取【检修检测项】sheet的最后一行的索引
		int lastRowNum = sheet3.getLastRowNum();
		Row row = null;
		List<RepairScopeRiskWarning> entityList = new ArrayList<RepairScopeRiskWarning>();
		RepairScopeRiskWarning rsrw = null;
		for (int i = RepairScopeManager.IMPORT_VALID_ROW_NUM; i <= lastRowNum; i++) {
			row = sheet3.getRow(i);
			// Modified by hetao on 2017-02-15 解决row可能为空时的空指针异常
			if (null == row || null == row.getCell(0) || Cell.CELL_TYPE_BLANK == row.getCell(0).getCellType()) {
				continue;
			}

			String repaireScopeNo = row.getCell(0).getStringCellValue(); // 检修范围编号
			RepairScope repairScope = map.get(repaireScopeNo);
			if (null == repairScope) {
				continue;
			}
			String scopeIdx = repairScope.getIdx(); // 设备检修范围主键		
			Cell cell = row.getCell(1);
			if (null == cell) {
				continue;
			}
			String riskItem = row.getCell(1).getStringCellValue(); // 安全风险点
			// 验证是否已经存在
			if (null != this.getModel(riskItem, scopeIdx)) {
				continue;
			}
			rsrw = new RepairScopeRiskWarning();
			rsrw.setScopeIdx(scopeIdx);
			rsrw.setRiskItem(riskItem);
			entityList.add(rsrw);
		}
		this.saveOrUpdate(entityList);
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
	private RepairScopeRiskWarning getModel(String riskItem, String scopeIdx) {
		String hql = "From RepairScopeRiskWarning Where riskItem = ? And scopeIdx = ?  And recordStatus = 0";
		return (RepairScopeRiskWarning) this.daoUtils.findSingle(hql, riskItem, scopeIdx);
	}

}
