package com.yunda.sb.inspect.scope.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.base.order.AbstractOrderManager2;
import com.yunda.sb.classfication.entity.Classification;
import com.yunda.sb.classfication.manager.ClassificationManager;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.inspect.scope.entity.InspectScope;
import com.yunda.sb.repair.scope.entity.RepairScope;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectScope业务类
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "inspectScopeManager")
public class InspectScopeManager extends AbstractOrderManager2<InspectScope, InspectScope> {

	/** EquipmentClassification管理器，数据表：E_EQUIPMENT_CLASSES */
	@Resource
	private ClassificationManager classificationManager;

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** 设备巡检项目（标准）数据导入 - excel单元格有效数据开始行下标 */
	public static final int IMPORT_VALID_ROW_NUM = 3;

	/**
	 * <li>说明：重写执行新增/修改前的验证业务方法，验证设备巡检项目（标准）不能重复
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月31日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 验证错误消息数组
	 */
	@Override
	public String[] validateUpdate(InspectScope t) {
		String hql = "From InspectScope Where checkItem = ? And classCode = ?";
		InspectScope entity = (InspectScope) this.daoUtils.findSingle(hql, t.getCheckItem(), t.getClassCode());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { String.format("巡检标准【%s】已经存在，不能重复添加！", t.getCheckItem()) };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：批量保存巡检范围
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月12日
	 * <li>修改人：  何涛
	 * <li>修改内容：增加设备类别名称首拼的存储
	 * <li>修改日期：	2016年9月21日
	 * @param scopes 巡检范围实体对象数组
	 * @param classCode 设备类别编码
	 * @param repairType 检修类型， 1：机械、2：电气
	 * @throws NoSuchFieldException 
	 */
	public void save(InspectScope[] scopes, String classCode, Integer repairType) throws NoSuchFieldException {
		int seqNo = this.getNextSeqNo(classCode, repairType);
		for (InspectScope entity : scopes) {
			entity.setClassCode(classCode);
			entity.setRepairType(repairType); // 设置检修类型
			// 如果是选择添加的检查项目，则有可能顺序号为空，此时自动设置一个顺序号
			entity.setSeqNo(seqNo++);
			this.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：重写保存方法，增加对设备类别相关信息的存储
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月12日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @param t 巡检范围实体对象
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void saveOrUpdate(InspectScope t) throws NoSuchFieldException {
		Classification classification = classificationManager.getModelByClassCode(t.getClassCode());
		// Modified by hetao on 2017-02-15 对于同类型不同设备可能存在设备检修范围不同的情况，修改classCode可以存储设备idx主键，以此上面的查询结果可能会为null
		if (null == classification) {
			EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelByEquipmentCode(t.getClassCode());
			if (null == epi) {
				throw new BusinessException(String.format("数据异常，未查询到编号为%s的设备类别或固资设备！", t.getClassCode()));
			}
			t.setClassName(epi.getEquipmentName());
			t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(epi.getEquipmentName()));
		} else {
			t.setClassName(classification.getClassName()); // 设备类别名称
			t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(classification.getClassName()));
		}
		t.setCheckItemPY(ChineseCharToEn.getInstance().getAllFirstLetter(t.getCheckItem()));
		t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(t.getClassName()));
		super.saveOrUpdate(t);
	}

	/**
	 * <li>说明：获取下一个可用的顺序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备编码
	 * @param repairType 检修类型， 1：机械、2：电气
	 * @return 下一个可用的顺序号
	 */
	private int getNextSeqNo(String classCode, Integer repairType) {
		String hql = "From InspectScope Where recordStatus = 0 And classCode = ? And repairType = ? Order By seqNo DESC";
		Object obj = this.daoUtils.findSingle(hql, classCode, repairType);
		if (null == obj) {
			return 1;
		}
		InspectScope t = (InspectScope) obj;
		if (null == t.getSeqNo()) {
			return 1;
		}
		return t.getSeqNo() + 1;
	}

	/**
	 * <li>说明：巡检范围选择添加的数据分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月13日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @param searchEntity 查询实体对象
	 * @param checked 是否只查询同类型的检查项目，默认为是：true
	 * @return 对象分页列表
	 */
	@SuppressWarnings("deprecation")
	public Page<InspectScope> pageSelectQuery(SearchEntity<InspectScope> searchEntity, boolean checked) {
		StringBuilder sb = new StringBuilder(
				"SELECT DISTINCT T.CLASS_CODE, T.CLASS_NAME, T.CLASS_NAME_PY, T.CHECK_ITEM, T.CHECK_ITEM_PY, T.CHECK_STANDARD, T.SEQ_NO FROM E_INSPECT_SCOPE T WHERE T.RECORD_STATUS = 0");
		InspectScope entity = searchEntity.getEntity();
		if (!StringUtil.isNullOrBlank(entity.getClassNamePY())) {
			String classNamePY = entity.getClassNamePY().toLowerCase();
			sb.append(" AND (");
			sb.append(" T.CLASS_NAME_PY LIKE '%").append(classNamePY).append("%'");
			sb.append(" OR ");
			sb.append(" T.CLASS_CODE LIKE '%").append(classNamePY).append("%'");
			sb.append(" OR ");
			sb.append(" T.CLASS_NAME LIKE '%").append(classNamePY).append("%'");
			sb.append(" )");
		}

		sb.append(" AND T.REPAIR_TYPE ='").append(entity.getRepairType()).append("'");

		// 根据设备编码过滤该设备类型下没有关联的检查项目
		sb.append(" AND T.CHECK_ITEM NOT IN (SELECT CHECK_ITEM FROM E_INSPECT_SCOPE WHERE RECORD_STATUS = 0 AND CLASS_CODE = '?')");

		// 只查询同类型的检查项目
		if (checked) {
			sb
					.append(" AND T.CLASS_CODE IN ( SELECT CLASS_CODE FROM E_EQUIPMENT_CLASSES WHERE RECORD_STATUS = 0 AND PARENT_IDX IN ( SELECT PARENT_IDX FROM E_EQUIPMENT_CLASSES WHERE RECORD_STATUS = 0 AND CLASS_CODE = '?'))");
		}

		// 按设备类别名称升序排序
		sb.append(" ORDER BY T.CLASS_NAME_PY ASC, T.SEQ_NO ASC");

		String sql = sb.toString().replace("?", entity.getClassCode());

		// 查询记录总数的SQL后需加别名（如：A），否则在执行数据库查询时会报异常
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT FROM (" + sql + ") A";
		return this.findPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), null, searchEntity.getOrders());
	}

	/**
	 * <li>说明：根据设备类别编码获取巡检范围集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：何涛
	 * <li>修改内容：增加对方法参数classCode的非空验证
	 * <li>修改日期：2016年10月12日
	 * <li>修改人：何涛
	 * <li>修改内容：针对同类型不同设备会存在不同巡检范围的情况，优先使用设备编号查询巡检范围，未找到结果时再根据设备类别编码进行查询
	 * <li>修改日期：2017年2月16日
	 * @param equipmentCode 设备编号
	 * @param classCode 设备类别编码
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @return 巡检范围对象集合
	 */
	@SuppressWarnings("unchecked")
	public List<InspectScope> getModelsByClassCode(String equipmentCode, String classCode, Integer repairType) {
		if (StringUtil.isNullOrBlank(classCode)) {
			return null;
		}
		String hql = "From InspectScope Where recordStatus = 0 And classCode = ?";
		if (null != repairType) {
			hql = hql + " And repairType = '" + repairType + "'";
		}
		// Modified by hetao on 2017-02-16 针对同类型不同设备会存在不同巡检范围的情况，优先使用设备编号查询巡检范围，未找到结果时再根据设备类别编码进行查询
		List<InspectScope> list = this.daoUtils.find(hql, equipmentCode);
		if (null != list && !list.isEmpty()) {
			return list;
		}
		return this.daoUtils.find(hql, classCode);
	}

	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return int 记录总数
	 */
	@Override
	public int count(InspectScope t) {
		String hql = "From InspectScope Where recordStatus = 0 And classCode = ? And repairType = ?";
		return this.daoUtils.getCount(hql, t.getClassCode(), t.getRepairType());
	}

	/**
	 * <li>说明：获取指定顺序号后的所有实体，即：如参数t的顺序号为1，则获取该实体排序范围内，所有顺序号大于1的实体列表(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 指定顺序号的实体
	 * @return List<T> 实体集合
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<InspectScope> findAllBySN(InspectScope t) throws Exception {
		String hql = "From InspectScope Where recordStatus = 0 And seqNo >= ? And classCode = ? And repairType = ?";
		return this.daoUtils.find(hql, new Object[] { t.getSeqNo(), t.getClassCode(), t.getRepairType() });
	}

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t	实体对象
	 * @return 结果集
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<InspectScope> findAll(InspectScope t) {
		String hql = "From InspectScope Where recordStatus = 0 And classCode = ? And repairType = ?";
		return this.daoUtils.find(hql, new Object[] { t.getClassCode(), t.getRepairType() });
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<InspectScope> updateMoveBottomHelper(InspectScope entity) {
		String hql = "From InspectScope Where recordStatus = 0 And seqNo > ? And classCode = ? And repairType = ?";
		return this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getClassCode(), entity.getRepairType() });
	}

	@Override
	public InspectScope updateMoveDownHelper(InspectScope entity) {
		String hql = "From InspectScope Where recordStatus = 0 And seqNo = ? And classCode = ? And repairType = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		return (InspectScope) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1, entity.getClassCode(), entity.getRepairType() });
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<InspectScope> updateMoveTopHelper(InspectScope entity) {
		String hql = "From InspectScope Where recordStatus = 0 And seqNo < ? And classCode = ? And repairType = ?";
		return this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getClassCode(), entity.getRepairType() });
	}

	@Override
	public InspectScope updateMoveUpHelper(InspectScope entity) {
		String hql = "From InspectScope Where recordStatus = 0 And seqNo = ? And classCode = ? And repairType = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		return (InspectScope) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1, entity.getClassCode(), entity.getRepairType() });
	}

	/**
	 * <li>说明：初始化，设置设备类别名称首拼
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月21日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws NoSuchFieldException
	 */
	public void initClassNamePY() throws NoSuchFieldException {
		@SuppressWarnings("unchecked")
		List<InspectScope> list = this.daoUtils.find("From InspectScope Where recordStatus = 0 And classNamePY Is Null");
		if (null == list || list.isEmpty()) {
			return;
		}
		for (InspectScope t : list) {
			t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(t.getClassName()));
		}
		this.saveOrUpdate(list);
	}

	/**
	 * <li>说明：导入设备巡检项目（标准）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param file 上传的设备检修范围模板文件
	 * @throws IOException 
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	public void importInspectScope(File file) throws IOException, NoSuchFieldException {
		if (null == file) {
			throw new NullPointerException("导入设备检修范围错误，未获取到导入模板文件！");
		}
		Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
		int i = 0;
		// 获取导入模板excel工作薄中的单个sheet页对象
		Sheet sheet = workbook.getSheetAt(i);
		// 如果导入模板中分了多个sheet页，则通过遍历的方法保证导入所有数据
		while (null != sheet) {
			// 按sheet页分别导入设备巡检项目（标准）基础数据
			this.importInspectScope(sheet);
			try {
				// 循环
				sheet = workbook.getSheetAt(++i);
			} catch (IllegalArgumentException e) {
				// 捕获sheet下标越界异常
				break;
			}
		}
		if (null != workbook) {
			workbook.close();
		}
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
				|| !"顺序号*".equals(row.getCell(2).getStringCellValue()) || !"检查项目*".equals(row.getCell(3).getStringCellValue())
				|| !"检查标准".equals(row.getCell(4).getStringCellValue()) || !"检修类型*".equals(row.getCell(5).getStringCellValue())) {
			return false;
		}
		return true;
	}

	/**
	 * <li>说明：导入设备巡检项目（标准）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param sheet 上传的设备检修范围模板文件中的单个sheet工作表
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	private void importInspectScope(Sheet sheet) throws NoSuchFieldException {
		// 获取sheet页有效数据最后一行的下标
		int lastRowNum = sheet.getLastRowNum();
		// 如果有效数据最后一行的下标小于有效数据开始行下标 ，则表示该sheet页未包含有效数据
		if (lastRowNum < IMPORT_VALID_ROW_NUM) {
			return;
		}
		// 验证导入模板格式是否有误
		if (!isValidImportMoudle(sheet.getRow(1))) {
			throw new BusinessException("导入模板格式有误，请选择正确的《设备巡检项目（标准）_导入模板.xls》后重试！");
		}
		InspectScope entity = null; // 要导入的巡检项目实体对象
		// 将导入的电气巡检项目（标准）集合
		Map<String, List<InspectScope>> map2Elc = new HashMap<String, List<InspectScope>>();
		// 将导入的机械巡检项目（标准）集合
		Map<String, List<InspectScope>> map2Mac = new HashMap<String, List<InspectScope>>();
		for (int i = IMPORT_VALID_ROW_NUM; i <= lastRowNum; i++) {
			// 根据导入数据获取“设备巡检项目（标准）”实体对象，如果巡检项目已经存在（重复导入），则返回空对象
			entity = this.getInspectScope(sheet.getRow(i), i);
			if (null == entity) {
				continue;
			}
			List<InspectScope> list = null;
			// 按检修类型将获取的实体对象分别缓存到巡检项目（标准）集合里
			if (RepairScope.REPAIR_TYPE_DQ == entity.getRepairType().intValue()) {
				list = map2Elc.get(entity.getClassCode());
				if (null == list) {
					list = new ArrayList<InspectScope>();
					map2Elc.put(entity.getClassCode(), list);
				}
			} else {
				list = map2Mac.get(entity.getClassCode());
				if (null == list) {
					list = new ArrayList<InspectScope>();
					map2Mac.put(entity.getClassCode(), list);
				}
			}
			list.add(entity);
		}
		// 导入设备电气巡检项目（标准）
		this.importInspectScope(map2Elc);
		// 导入设备机械巡检项目（标准）
		this.importInspectScope(map2Mac);
	}

	/**
	 * <li>说明：导入设备巡检项目（标准）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param map 从数据导入模板中获取的设备巡范围导入集合，key：设备类别编码，value：设备巡检范围实体集合
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	private void importInspectScope(Map<String, List<InspectScope>> map) throws NoSuchFieldException {
		if (null == map || map.isEmpty()) {
			return;
		}
		Set<Entry<String, List<InspectScope>>> entrySet = map.entrySet();
		// map遍历
		for (Iterator<Entry<String, List<InspectScope>>> i = entrySet.iterator(); i.hasNext();) {
			Entry<String, List<InspectScope>> next = i.next();
			// 该集合对象不包含数据库已经存在的巡检项目对象
			List<InspectScope> list = next.getValue();
			// 排序
			Collections.sort(list);
			// 获取下一个可用的顺序号
			// Modified by hetao on 2017-02-22 修改seqNo初始值获取的方式（原：int seqNo = 0;），避免第二次导入部分数据时，排序错误的情况发生
			int seqNo = this.getNextSeqNo(list.get(0).getClassCode(), list.get(0).getRepairType());
			for (InspectScope entity : list) {
				// 设置获取到的排序号为空，避免导入模板中的同一类型设备的巡检项目的顺序号可能不是从1开始排列的异常情况
				entity.setSeqNo(seqNo++);
				this.saveOrUpdate(entity);
			}
		}
	}

	/**
	 * <li>说明：根据导入数据构造设备巡检项目实体对象，如果巡检项目已经存在（重复导入），则返回空对象
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param row 导入模板行对象
	 * @param rowIndex 当前数据所在行索引
	 * @return 设备巡检项目实体对象
	 */
	private InspectScope getInspectScope(Row row, int rowIndex) {
		// Modified by hetao on 2017-02-16 解决row可能为空时的空指针异常
		if (null == row || null == row.getCell(0) || Cell.CELL_TYPE_BLANK == row.getCell(0).getCellType()) {
			return null;
		}
		// 获取设备类别编码、名称，并验证该类别是否在3.0系统中存在，如果不存在则终止导入，提醒用户修改
		String classCode = null;
		if (Cell.CELL_TYPE_NUMERIC == row.getCell(0).getCellType()) {
			Double numericCellValue = row.getCell(0).getNumericCellValue();
			classCode = numericCellValue.intValue() + "";
		} else if (Cell.CELL_TYPE_STRING == row.getCell(0).getCellType()) {
			classCode = row.getCell(0).getStringCellValue();
		}
		// 获取设备管理系统3.0中的设备类别实体对象
		Classification classification = this.classificationManager.getModelByClassCode(classCode);
		if (null == classification) {
			EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelByEquipmentCode(classCode);
			if (null == epi) {
				throw new NullPointerException(String.format("数据异常，系统未查询到编号为【%s】的设备类别或固资设备，请确认修改后重试，所在行数：%d！", classCode, rowIndex + 1));
			}
		}

		// 检查项目
		String checkItem = row.getCell(3).getStringCellValue();

		// 检修类型：电气、机械
		String rt = row.getCell(5).getStringCellValue();
		if (StringUtil.isNullOrBlank(rt)) {
			throw new NullPointerException(String.format("数据异常 - 检修类型为空，请确认修改后重试，所在行数：%d！", classCode, rowIndex + 1));
		}
		Integer repairType = "电气".equals(rt) ? RepairScope.REPAIR_TYPE_DQ : RepairScope.REPAIR_TYPE_JX;

		// 验证巡检检查项目是否已经存在
		if (isExist(classCode, checkItem, repairType)) {
			return null;
		}

		// 顺序号
		Integer seqNo = null;
		if (null == row.getCell(2)) {
			throw new BusinessException("导入模板【顺序号*】字段不能为空，请设置顺序号后重试！");
		}
		if (Cell.CELL_TYPE_NUMERIC == row.getCell(2).getCellType()) {
			Double numericCellValue = row.getCell(2).getNumericCellValue();
			seqNo = numericCellValue.intValue();
		} else if (Cell.CELL_TYPE_STRING == row.getCell(0).getCellType()) {
			seqNo = Integer.valueOf(row.getCell(2).getStringCellValue());
		}
		// 检查标准
		String checkStandard = null;
		if (null != row.getCell(4) && Cell.CELL_TYPE_BLANK == row.getCell(4).getCellType()) {
			checkStandard = row.getCell(4).getStringCellValue();
		}
		// new设备巡检范围实体对象
		return new InspectScope(classCode, seqNo, checkItem, checkStandard, repairType);
	}

	/**
	 * <li>说明：验证巡检检查项目是否已经存在
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @param checkItem 检查项目
	 * @param repairType 检修类型（1：机械、2：电气、3：其它
	 * @return 查项目是否已经存在，true：已经存在，false：不存在
	 */
	private boolean isExist(String classCode, String checkItem, Integer repairType) {
		String hql = "From InspectScope Where recordStatus = 0 And classCode = ? And checkItem = ? And repairType = ?";
		int count = this.daoUtils.getCount(hql, classCode, checkItem, repairType);
		return 0 < count;
	}

}
