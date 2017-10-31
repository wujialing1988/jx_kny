package com.yunda.sb.repair.scope.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.baseapp.ChineseCharToEn;
import com.yunda.common.BusinessException;
import com.yunda.sb.base.order.AbstractOrderManager2;
import com.yunda.sb.classfication.entity.Classification;
import com.yunda.sb.classfication.manager.ClassificationManager;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.repair.scope.entity.RepairScope;
import com.yunda.sb.repair.scope.entity.RepairScopeDetails;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairScope业务类，数据表：E_REPAIR_SCOPE
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改内容：
 * <li>修改日期：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service(value = "repairScopeManager")
public class RepairScopeManager extends AbstractOrderManager2<RepairScope, RepairScope> {

	/** EquipmentClassification管理器，数据表：E_EQUIPMENT_CLASSES  */
	@Resource
	private ClassificationManager classificationManager;

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO  */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** RepairScopeDetails管理器，数据表：E_REPAIR_SCOPE_DETAILS  */
	@Resource
	private RepairScopeDetailsManager repairScopeDetailsManager;

	/** RepairScopeRiskWarning管理器，数据表：E_REPAIR_RISK_WARNING  */
	@Resource
	private RepairScopeRiskWarningManager repairScopeRiskWarningManager;

	/** 设备检修范围数据导入 - excel单元格有效数据开始行下标 */
	public static final int IMPORT_VALID_ROW_NUM = 3;

	/**
	 * <li>说明：重写更新验证方法，验证检修范围名称不能重复
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 */
	@Override
	public String[] validateUpdate(RepairScope t) {
		String hql = "From RepairScope Where recordStatus = 0 And classCode = ? And repairScopeName = ? And repairType = ?";
		RepairScope entity = (RepairScope) this.daoUtils.findSingle(hql, t.getClassCode(), t.getRepairScopeName(), t.getRepairType());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { "检修范围：" + t.getRepairScopeName() + "已经存在，请重新输入！" };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：重写保存方法，后台设置设备类别相关字段数据
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-05
	 * <li>修改人：何涛
	 * <li>修改内容：对于同类型不同设备可能存在设备检修范围不同的情况，修改classCode可以存储设备idx主键，以实现该功能
	 * <li>修改日期：2017年2月16日
	 * @param t 实体对象
	 */
	@Override
	public void saveOrUpdate(RepairScope t) throws NoSuchFieldException {
		Classification classification = classificationManager.getModelByClassCode(t.getClassCode());
		// Modified by hetao on 2017-02-15 对于同类型不同设备可能存在设备检修范围不同的情况，修改classCode可以存储设备idx主键，以此上面的查询结果可能会为null
		if (null == classification) {
			EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelByEquipmentCode(t.getClassCode());
			if (null == epi) {
				throw new BusinessException(String.format("数据异常，未查询到编号为%s的设备类别或固资设备！", t.getClassCode()));
			}
			// 固资设备设置类别名称为设备名称
			t.setClassName(epi.getEquipmentName());
			t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(epi.getEquipmentName()));
		} else {
			t.setClassName(classification.getClassName()); // 设备类别名称
			t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(classification.getClassName()));
		}
		super.saveOrUpdate(t);
	}

	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return int 记录总数
	 */
	@Override
	public int count(RepairScope t) {
		String hql = "From RepairScope Where recordStatus = 0 And classCode = ? And repairType = ?";
		return this.daoUtils.getCount(hql, t.getClassCode(), t.getRepairType());
	}

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t	实体对象
	 * @return 结果集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RepairScope> findAll(RepairScope t) {
		String hql = "From RepairScope Where recordStatus = 0 And classCode = ? And repairType = ?";
		return this.daoUtils.find(hql, new Object[] { t.getClassCode(), t.getRepairType() });
	}

	/**
	 * <li>说明：获取指定顺序号后的所有实体，即：如参数t的顺序号为1，则获取该实体排序范围内，所有顺序号大于1的实体列表(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 指定顺序号的实体
	 * @return List<T> 实体集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RepairScope> findAllBySN(RepairScope t) throws Exception {
		String hql = "From RepairScope Where recordStatus = 0 And seqNo >= ? And classCode = ? And repairType = ?";
		return this.daoUtils.find(hql, new Object[] { t.getSeqNo(), t.getClassCode(), t.getRepairType() });
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RepairScope> updateMoveBottomHelper(RepairScope entity) {
		String hql = "From RepairScope Where recordStatus = 0 And seqNo > ? And classCode = ? And repairType = ?";
		return this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getClassCode(), entity.getRepairType() });
	}

	@Override
	public RepairScope updateMoveDownHelper(RepairScope entity) {
		String hql = "From RepairScope Where recordStatus = 0 And seqNo = ? And classCode = ? And repairType = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		return (RepairScope) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1, entity.getClassCode(), entity.getRepairType() });
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RepairScope> updateMoveTopHelper(RepairScope entity) {
		String hql = "From RepairScope Where recordStatus = 0 And seqNo < ? And classCode = ? And repairType = ?";
		return this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getClassCode(), entity.getRepairType() });
	}

	@Override
	public RepairScope updateMoveUpHelper(RepairScope entity) {
		String hql = "From RepairScope Where recordStatus = 0 And seqNo = ? And classCode = ? And repairType = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		return (RepairScope) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1, entity.getClassCode(), entity.getRepairType() });
	}

	/**
	 * <li>说明：重写逻辑删除方法，级联删除设备检修范围作业内容
	 * <li>创建人：何涛
	 * <li>创建日期：2016-07-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 设备检修范围idx主键数组
	 */
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		Serializable[] array = null;
		for (Serializable idx : ids) {
			List<RepairScopeDetails> list = this.repairScopeDetailsManager.getModelsByScopeIdx((String) idx);
			if (null == list || list.isEmpty()) {
				continue;
			}
			array = new String[list.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = list.get(i).getIdx();
			}
			this.repairScopeDetailsManager.logicDelete(array);
		}
		super.logicDelete(ids);
	}

	/**
	 * <li>说明：批量保存巡检范围
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：何涛
	 * <li>修改内容：对于同类型不同设备可能存在设备检修范围不同的情况，修改classCode可以存储设备编号，以实现该功能
	 * <li>修改日期：2017年2月16日
	 * @param 巡检范围实体对象数组
	 * @param classCode 设备类别编码或者设备编号
	 * @throws NoSuchFieldException 
	 */
	public void save(RepairScope[] scopes, String classCode) throws NoSuchFieldException {
		int seqNo = this.getNextSeqNo(classCode, scopes[0].getRepairType());
		String idx = null; // 选择新增时，已选择的原设备检修范围idx主键，临时存储该主键，用于复制该检修范围下的作业内容
		for (RepairScope entity : scopes) {
			idx = entity.getIdx();
			entity.setClassCode(classCode);
			entity.setIdx(null);
			// 如果是选择添加的检查项目，则有可能顺序号为空，此时自动设置一个顺序号
			entity.setSeqNo(seqNo++);
			this.saveOrUpdate(entity);

			// 保存设备检修范围作业内容
			this.repairScopeDetailsManager.save(entity, idx);
		}
	}

	/**
	 * <li>说明：获取下一个可用的顺序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备编码
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @return 下一个可用的顺序号
	 */
	private int getNextSeqNo(String classCode, Integer repairType) {
		String hql = "From RepairScope Where recordStatus = 0 And classCode = ? And repairType = ? Order By seqNo DESC";
		Object obj = this.daoUtils.findSingle(hql, classCode, repairType);
		if (null == obj) {
			return 1;
		}
		RepairScope t = (RepairScope) obj;
		if (null == t.getSeqNo()) {
			return 1;
		}
		return t.getSeqNo() + 1;
	}

	/**
	 * <li>说明: 根据“设备idx主键”获取设备检修范围实体集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：何涛
	 * <li>修改内容：2017年2月16日
	 * <li>修改日期：对同类型不同设备设备检修范围不同的情况，优先使用设备编号查询检修范围，未查询到时再根据设备类别编号查询检修范围
	 * @param equipmentIdx 设备idx主键
	 * @param repairClass 修程，1:小修、2：中修、3：项修 
	 * @return 设备检修范围实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<RepairScope> getModelsByEquipmentIdx(String equipmentIdx, Short repairClass) {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(equipmentIdx);
		if (null == epi) {
			throw new BusinessException(String.format("数据异常，未查询到idx主键为%s的设备信息！", equipmentIdx));
		}
		StringBuilder sb = new StringBuilder("From RepairScope Where recordStatus = 0 And classCode = ?");
		switch (repairClass) {
		case 1:
			sb.append(" And repairClassSmall = 1");
			break;
		case 2:
			sb.append(" And repair_class_medium = 1");
			break;
		case 3:
			sb.append(" And repairClassSubject = 1");
			break;
		default:
			break;
		}
		sb.append(" Order By seqNo ASC");
		String hql = sb.toString();
		List<RepairScope> list = this.daoUtils.find(hql, epi.getEquipmentCode());
		if (null != list && !list.isEmpty()) {
			return list;
		}
		return this.daoUtils.find(hql, epi.getClassCode());
	}

	/**
	 * <li>说明：验证导入模板格式是否有误
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月22日
	 * <li>修改人：何涛
	 * <li>修改内容：修改导入模板列头关键字，同步修改验证规则
	 * <li>修改日期：2017年2月22日
	 * @param row 导入模板列头行对象
	 * @return 是否是一个合法的导入模板，true：合法，false：非法
	 */
	private boolean isValidImportMoudle(Row row) {
		if (null == row || !"设备（类别）编号*".equals(row.getCell(0).getStringCellValue()) || !"设备（类别）名称".equals(row.getCell(1).getStringCellValue())
				|| !"模板编号*".equals(row.getCell(2).getStringCellValue()) || !"备注".equals(row.getCell(3).getStringCellValue())) {
			return false;
		}
		return true;
	}

	/**
	 * <li>说明：导入设备检修范围
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月8日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param file 上传的设备检修范围模板文件
	 * @throws IOException 
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	public void importRepairScope(File file) throws IOException, NoSuchFieldException {
		if (null == file) {
			throw new NullPointerException("导入设备检修范围错误，未获取到导入模板文件！");
		}
		Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
		// sheet0 是检修范围模板，包含设备类别编码、名称信息
		Sheet sheet0 = workbook.getSheetAt(0);

		if (!isValidImportMoudle(sheet0.getRow(1))) {
			workbook.close();
			throw new BusinessException("导入模板格式有误，请选择正确的《设备检修范围_导入模板.xls》后重试！");
		}

		// 获取【检修范围模板】sheet的最后一行的索引
		int lastRowNum = sheet0.getLastRowNum();
		Row row = null;
		Classification classification = null; // 设备类别
		for (int i = IMPORT_VALID_ROW_NUM; i <= lastRowNum; i++) {
			row = sheet0.getRow(i);
			// Modified by hetao on 2017-02-15 解决row可能为空时的空指针异常
			if (null == row || null == row.getCell(0) || Cell.CELL_TYPE_BLANK == row.getCell(0).getCellType()) {
				continue;
			}
			// 获取设备类别编码、名称，并验证该类别是否在3.0系统中存在，如果不存在则终止导入，提醒用户修改
			// Modified by hetao on 2017-02-16 对同类型不同设备设备检修范围不同的情况，因此该字段可能会是设备编号
			String classCode = null;
			String className = null;
			if (Cell.CELL_TYPE_NUMERIC == row.getCell(0).getCellType()) {
				Double numericCellValue = row.getCell(0).getNumericCellValue();
				classCode = numericCellValue.intValue() + "";
			} else if (Cell.CELL_TYPE_STRING == row.getCell(0).getCellType()) {
				classCode = row.getCell(0).getStringCellValue();
			}
			// 获取设备管理系统3.0中的设备类别实体对象
			classification = this.classificationManager.getModelByClassCode(classCode);
			if (null != classification) {
				// 设备类别名称
				className = classification.getClassName();
			} else {
				EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelByEquipmentCode(classCode);
				if (null == epi) {
					throw new NullPointerException(String.format("数据异常，系统未查询到编号为【%s】的设备类别或固资设备，请确认修改后重试，所在行数：%d！", classCode, i + 1));
				}
			}
			// 模板编号
			String mouldNo = null;
			if (Cell.CELL_TYPE_NUMERIC == row.getCell(2).getCellType()) {
				Double numericCellValue = row.getCell(2).getNumericCellValue();
				mouldNo = numericCellValue.intValue() + "";
			} else if (Cell.CELL_TYPE_STRING == row.getCell(2).getCellType()) {
				mouldNo = row.getCell(2).getStringCellValue();
			}

			// 根据设备类别 —— “设备类别编码”、“设备类别名称”导入设备检修范围数据
			this.insertByImport(classCode, className, mouldNo, workbook);
		}
		if (null != workbook) {
			workbook.close();
		}
	}

	/**
	 * <li>说明：导入设备检修范围数据
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月8日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @param className 设备类别名称
	 * @param mouldNo 模板编号
	 * @param workbook 设备检修范围_导入模板.xls工作薄对象
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	private void insertByImport(String classCode, String className, String mouldNo, Workbook workbook) throws NoSuchFieldException {
		// sheet1 是检修范围
		Sheet sheet1 = workbook.getSheetAt(1);
		// 获取【检修范围】sheet的最后一行的索引
		int lastRowNum = sheet1.getLastRowNum();
		Row row = null;
		RepairScope repairScope = null;
		List<RepairScope> repairScope2Mac = new ArrayList<RepairScope>(); // 机械检修范围
		List<RepairScope> repairScope2Elc = new ArrayList<RepairScope>(); // 电气检修范围

		// 设备检修范围实体对象散列表，key：检修范围编号，value：设备检修范围实体对象
		Map<String, RepairScope> map = new HashMap<String, RepairScope>();
		for (int i = IMPORT_VALID_ROW_NUM; i <= lastRowNum; i++) {
			row = sheet1.getRow(i);
			// Modified by hetao on 2017-02-15 解决row可能为空时的空指针异常
			if (null == row || null == row.getCell(0) || Cell.CELL_TYPE_BLANK == row.getCell(0).getCellType()) {
				continue;
			}
			// 模板编号
			String mouldNo2 = null;
			try {
				mouldNo2 = row.getCell(0).getStringCellValue();
			} catch (IllegalStateException e) {
				Double d = row.getCell(0).getNumericCellValue();
				mouldNo2 = d.intValue() + "";
			}
			if (!mouldNo.equals(mouldNo2)) {
				continue;
			}

			// 检修范围编号
			String repairScopeName = row.getCell(2).getStringCellValue();

			repairScope = new RepairScope();
			repairScope.setClassCode(classCode);
			repairScope.setRepairScopeName(repairScopeName);

			// 小修适用
			this.setRepairClass(row.getCell(4), repairScope);
			// 中修适用
			this.setRepairClass(row.getCell(5), repairScope);
			// 项修适用
			this.setRepairClass(row.getCell(6), repairScope);

			// 检修类型（1：机械、2：电气、3：其它）
			Integer repairType = "电气".equals(row.getCell(3).getStringCellValue()) ? RepairScope.REPAIR_TYPE_DQ : RepairScope.REPAIR_TYPE_JX;
			repairScope.setRepairType(repairType);

			// 检修范围编号
			String repaireScopeNo = row.getCell(1).getStringCellValue();
			RepairScope entity = this.getModel(classCode, repairScopeName, repairScope.getRepairType());
			// 更新
			if (null != entity) {
				entity.setRepairClassSmall(repairScope.getRepairClassSmall());
				entity.setRepairClassMedium(repairScope.getRepairClassMedium());
				entity.setRepairClassSubject(repairScope.getRepairClassSubject());
				map.put(repaireScopeNo, entity);
				// 新增
			} else {
				map.put(repaireScopeNo, repairScope);
			}
			RepairScope rs = map.get(repaireScopeNo);
			if (RepairScope.REPAIR_TYPE_DQ == rs.getRepairType()) {
				repairScope2Elc.add(rs);
			} else {
				repairScope2Mac.add(rs);
			}
		}
		// 保存电气检修范围
		if (null != repairScope2Elc && !repairScope2Elc.isEmpty()) {
			// 获取下一个可用的顺序号
			int seqNo = 1;
			for (int j = 0; j < repairScope2Elc.size(); j++) {
				repairScope2Elc.get(j).setSeqNo(seqNo++);
				this.saveOrUpdate(repairScope2Elc.get(j));
			}
		}
		// 保存机械检修范围
		if (null != repairScope2Mac && !repairScope2Mac.isEmpty()) {
			int seqNo = 1;
			for (int j = 0; j < repairScope2Mac.size(); j++) {
				repairScope2Mac.get(j).setSeqNo(seqNo++);
				this.saveOrUpdate(repairScope2Mac.get(j));
			}
		}
		// 导入设备检修范围明细数据
		this.repairScopeDetailsManager.insertByImport(map, workbook);
		// 导入设备检修范围安全风险点
		this.repairScopeRiskWarningManager.insertByImport(map, workbook);
	}

	/**
	 * <li>说明：设置设备检修范围适用的修程，小修、中修、项修
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param cell 导入模板excel单元格实体对象
	 * @param repairScope 设备检修范围实体对象
	 */
	private void setRepairClass(Cell cell, RepairScope repairScope) {
		if (null == cell) {
			return;
		}
		Double temp;
		boolean flag = false;
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			temp = cell.getNumericCellValue();
			if (temp.intValue() == 1) {
				flag = true;
			}
		} else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
			if ("1".equals(cell.getStringCellValue())) {
				flag = true;
				repairScope.setRepairClassSmall(true);
			}
		}
		int columnIndex = cell.getColumnIndex();
		if (4 == columnIndex) {
			repairScope.setRepairClassSmall(flag);
		} else if (5 == columnIndex) {
			repairScope.setRepairClassMedium(flag);
		} else if (6 == columnIndex) {
			repairScope.setRepairClassSubject(flag);
		}
	}

	/**
	 * <li>说明：获取设备检修范围实体对象
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @param repairScopeName 检修范围名称
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @return 设备检修范围实体对象
	 */
	private RepairScope getModel(String classCode, String repairScopeName, Integer repairType) {
		String hql = "From RepairScope Where recordStatus = 0 And classCode = ? And repairScopeName = ? And repairType = ?";
		return (RepairScope) this.daoUtils.findSingle(hql, classCode, repairScopeName, repairType);
	}

}
