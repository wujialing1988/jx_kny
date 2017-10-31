package com.yunda.sb.pointcheck.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
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
import com.yunda.sb.pointcheck.entity.PointCheckScope;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheckScope业务类，数据表：SBJX_POINT_CHECK_SCOPE
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "pointCheckScopeManager")
public class PointCheckScopeManager extends AbstractOrderManager2<PointCheckScope, PointCheckScope> {

	/** 设备类别业务类 */
	@Resource
	private ClassificationManager classificationManager;

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** 设备点检项目（标准）数据导入 - excel单元格有效数据开始行下标 */
	public static final int IMPORT_VALID_ROW_NUM = 3;

	/**
	 * <li>说明：重写执行新增/修改前的验证业务方法，验证点检内容不能重复
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 验证错误消息数组
	 */
	@Override
	public String[] validateUpdate(PointCheckScope t) {
		String hql = "From PointCheckScope Where checkContent = ? And classCode = ?";
		PointCheckScope entity = (PointCheckScope) this.daoUtils.findSingle(hql, t.getCheckContent(), t.getClassCode());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { String.format("点检内容【%s】已经存在，不能重复添加！", t.getCheckContent()) };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：重写删除方法，在删除后对排序范围内的记录进行重新排序
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月16日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids   实体id数组
	 */
	@Override
	public void deleteByIds(Serializable... ids) {
		if (null == ids || 0 >= ids.length) {
			return;
		}
		PointCheckScope entity = this.getModelById(ids[0]);
		super.deleteByIds(ids);
		try {
			updateSort(this.findAll(entity));
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * <li>说明：批量保存设备点检范围
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人： 何涛
	 * <li>修改内容：增加设备类别名称首拼的存储
	 * <li>修改日期：	2016年9月21日
	 * @param scopes 设备点检范围实例对象数组
	 * @param classCode 设备类别编码
	 * @throws NoSuchFieldException 
	 */
	public void save(PointCheckScope[] scopes, String classCode) throws NoSuchFieldException {
		// 获得可用的顺序号
		int seqNo = this.getNextSeqNo(classCode);
		for (PointCheckScope entity : scopes) {
			entity.setClassCode(classCode);
			// 如果是选择添加的检查项目，则有可能顺序号为空，此时自动设置一个顺序号
			entity.setSeqNo(seqNo++);
			this.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：重写保存方法，增加对设备类别相关信息的存储
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @param t 设备点检范围实体对象
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void saveOrUpdate(PointCheckScope t) throws NoSuchFieldException {
		Classification classification = classificationManager.getModelByClassCode(t.getClassCode());
		if (null == classification) {
			EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelByEquipmentCode(t.getClassCode());
			if (null == epi) {
				throw new BusinessException(String.format("数据异常，未查询到编号为%s的设备类别或固资设备！", t.getClassCode()));
			}
			t.setClassName(epi.getEquipmentName());
			t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(epi.getEquipmentName()));
		} else {
			// 设备类别名称
			t.setClassName(classification.getClassName());
			// 设备类别名称首拼
			t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(classification.getClassName()));
		}
		t.setCheckContentPY(ChineseCharToEn.getInstance().getAllFirstLetter(t.getCheckContent()));
		super.saveOrUpdate(t);
	}

	/**
	 * <li>说明：获取下一个可用的顺序号
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @return 下一个可用的顺序号
	 */
	private int getNextSeqNo(String classCode) {
		String hql = "From PointCheckScope Where classCode = ? Order By seqNo DESC";
		Object obj = this.daoUtils.findSingle(hql, classCode);
		if (null == obj) {
			return 1;
		}
		PointCheckScope t = (PointCheckScope) obj;
		if (null == t.getSeqNo()) {
			return 1;
		}
		return t.getSeqNo() + 1;
	}

	/**
	 * <li>说明：点检范围选择添加的数据分页查询
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @param searchEntity 查询实体对象
	 * @param checked 是否只查询同类型的点检范围，默认为是：true
	 * @return 对象分页列表
	 */
	@SuppressWarnings("deprecation")
	public Page<PointCheckScope> pageSelectQuery(SearchEntity<PointCheckScope> searchEntity, boolean checked) {
		StringBuilder sb = new StringBuilder(
				"SELECT DISTINCT T.CLASS_CODE, T.CLASS_NAME, T.CLASS_NAME_PY, T.CHECK_CONTENT, T.CHECK_CONTENT_PY, T.SEQ_NO FROM E_SBJX_POINT_CHECK_SCOPE T WHERE 1 = 1 ");
		PointCheckScope entity = searchEntity.getEntity();
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

		// 根据设备编码过滤该设备类型下没有关联的点检内容
		sb.append("AND t.CHECK_CONTENT NOT IN (SELECT CHECK_CONTENT FROM E_SBJX_POINT_CHECK_SCOPE WHERE CLASS_CODE = '?')");

		// 只查询同类型的点检内容
		if (checked) {
			sb
					.append(" AND t.CLASS_CODE IN ( SELECT CLASS_CODE FROM E_EQUIPMENT_CLASSES WHERE PARENT_IDX IN ( SELECT PARENT_IDX FROM E_EQUIPMENT_CLASSES WHERE RECORD_STATUS = 0 AND CLASS_CODE = '?'))");
		}

		// 按设备类别名称升序排序
		sb.append(" ORDER BY T.CLASS_NAME_PY ASC, T.SEQ_NO ASC");

		String sql = sb.toString().replace("?", entity.getClassCode());

		// 查询记录总数的SQL后需加别名（如：A），否则在执行数据库查询时会报异常
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT FROM (" + sql + ") A";
		return this.findPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), null, searchEntity.getOrders());
	}

	/**
	 * <li>说明：根据设备类别编码获取设备点检范围集合
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人：何涛
	 * <li>修改内容：针对同类型不同设备会存在不同点检范围的情况，优先使用设备编号查询点检范围，未找到结果时再根据设备类别编码进行查询
	 * <li>修改日期：2017年2月24日
	 * @param equipmentCode 设备编号
	 * @param classCode 设备类别编码
	 * @return 点检范围对象集合
	 */
	@SuppressWarnings("unchecked")
	public List<PointCheckScope> getModelsByClassCode(String equipmentCode, String classCode) {
		String hql = "From PointCheckScope Where classCode = ?";
		// Modified by hetao on 2017-02-24
		// 针对同类型不同设备会存在不同点检范围的情况，优先使用设备编号查询点检范围，未找到结果时再根据设备类别编码进行查询
		List<PointCheckScope> list = this.daoUtils.find(hql, equipmentCode);
		if (null != list && !list.isEmpty()) {
			return list;
		}
		return (List<PointCheckScope>) this.daoUtils.getHibernateTemplate().find(hql, classCode);
	}

	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 点检范围实例对象
	 * @return int 记录总数
	 */
	@Override
	public int count(PointCheckScope t) {
		String hql = "From PointCheckScope Where classCode = ?";
		return this.daoUtils.getCount(hql, t.getClassCode());
	}

	/**
	 * <li>说明：获取指定顺序号后的所有实体，即：如参数t的顺序号为1，则获取该实体排序范围内，所有顺序号大于1的实体列表(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 指定顺序号的实体
	 * @return List<T> 实体集合
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PointCheckScope> findAllBySN(PointCheckScope t) throws Exception {
		String hql = "From PointCheckScope Where seqNo >= ? And classCode = ?";
		return this.daoUtils.find(hql, new Object[] { t.getSeqNo(), t.getClassCode() });
	}

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t	点检范围实例对象
	 * @return 结果集
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PointCheckScope> findAll(PointCheckScope t) {
		String hql = "From PointCheckScope Where classCode = ?";
		return this.daoUtils.find(hql, new Object[] { t.getClassCode() });
	}

	/**
	 * <li>说明：获取被【置底】记录被置底前，紧随其后的记录
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 被移动的对象
	 * @return 被【置底】记录被置底前，紧随其后的记录
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PointCheckScope> updateMoveBottomHelper(PointCheckScope entity) {
		String hql = "From PointCheckScope Where seqNo > ? And classCode = ?";
		return this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getClassCode() });
	}

	/**
	 * <li>说明：获取被【下移】记录被下移前，紧随其后的记录
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 被移动的对象
	 * @return 获取被【下移】记录被下移前，紧随其后的记录
	 */
	@Override
	public PointCheckScope updateMoveDownHelper(PointCheckScope entity) {
		String hql = "From PointCheckScope Where seqNo = ? And classCode = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		return (PointCheckScope) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1, entity.getClassCode() });
	}

	/**
	 * <li>说明：获取被【置顶】记录被置顶前，紧随其前的记录
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 被移动的对象
	 * @return 获取被【置顶】记录被置顶前，紧随其前的记录
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PointCheckScope> updateMoveTopHelper(PointCheckScope entity) {
		String hql = "From PointCheckScope Where seqNo < ? And classCode = ?";
		return this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getClassCode() });
	}

	/**
	 * <li>说明：获取被【上移】记录被上移前，紧随其前的记录
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 被移动的对象
	 * @return 被【上移】记录被上移前，紧随其前的记录
	 */
	@Override
	public PointCheckScope updateMoveUpHelper(PointCheckScope entity) {
		String hql = "From PointCheckScope Where seqNo = ? And classCode = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		return (PointCheckScope) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1, entity.getClassCode() });
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
		List<PointCheckScope> list = this.daoUtils.find("From PointCheckScope Where classNamePY Is Null");
		if (null == list || list.isEmpty()) {
			return;
		}
		for (PointCheckScope t : list) {
			t.setClassNamePY(ChineseCharToEn.getInstance().getAllFirstLetter(t.getClassName()));
		}
		this.saveOrUpdate(list);
	}

	/**
	 * <li>说明：导入设备点检范围
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年9月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param file 上传的设备点检范围模板文件
	 * @throws IOException 
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	public void importPointCheckScope(File file) throws IOException, NoSuchFieldException {
		if (null == file) {
			throw new NullPointerException("导入设备点检范围错误，未获取到导入模板文件！");
		}
		Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
		int i = 0;
		// 获取导入模板excel工作薄中的单个sheet页对象
		Sheet sheet = workbook.getSheetAt(i);
		// 如果导入模板中分了多个sheet页，则通过遍历的方法保证导入所有数据
		while (null != sheet) {
			// 按sheet页分别导入设备点检项目（标准）基础数据
			this.importPointCheckScope(sheet);
			try {
				// 循环
				sheet = workbook.getSheetAt(++i);
			} catch (IllegalArgumentException e) {
				// 捕获sheet下标越界异常
				break;
			}
		}
		// 关闭工作薄
		if (null != workbook) {
			workbook.close();
		}
	}

	/**
	 * <li>说明：导入设备点检范围
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年9月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param sheet 上传的设备点检范围模板文件中的单个sheet工作表
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	private void importPointCheckScope(Sheet sheet) throws NoSuchFieldException {
		// 获取sheet页有效数据最后一行的下标
		int lastRowNum = sheet.getLastRowNum();
		// 如果有效数据最后一行的下标小于有效数据开始行下标 ，则表示该sheet页未包含有效数据
		if (lastRowNum < IMPORT_VALID_ROW_NUM) {
			return;
		}
		// 验证导入模板格式是否有误
		if (!isValidImportMoudle(sheet.getRow(1))) {
			throw new BusinessException("导入模板格式有误，请选择正确的《设备点检项目_导入模板.xls》后重试！");
		}
		PointCheckScope entity = null; // 要导入的点检项目实体对象
		// 将导入的点检项目（标准）集合
		Map<String, List<PointCheckScope>> map = new HashMap<String, List<PointCheckScope>>();
		for (int i = IMPORT_VALID_ROW_NUM; i <= lastRowNum; i++) {
			// 根据导入数据获取“设备点检范围”实体对象，如果点检项目已经存在（重复导入），则返回空对象
			entity = this.getPointCheckScope(sheet.getRow(i), i);
			if (null == entity) {
				continue;
			}
			List<PointCheckScope> list = map.get(entity.getClassCode());
			if (null == list) {
				list = new ArrayList<PointCheckScope>();
				map.put(entity.getClassCode(), list);
			}
			list.add(entity);
		}
		// 导入设备点检项目（标准）
		this.importPointCheckScope(map);
	}

	/**
	 * <li>说明：导入设备点检项目（标准）
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年9月26日
	 * <li>修改人：
	 * <li>修改内容:
	 * <li>修改日期：
	 * @param map 从数据导入模板中获取的设备点检范围导入集合，key：设备类别编码，value：设备点检范围实体集合
	 * @throws NoSuchFieldException 
	 */
	@Transactional
	private void importPointCheckScope(Map<String, List<PointCheckScope>> map) throws NoSuchFieldException {
		if (null == map || map.isEmpty()) {
			return;
		}
		Set<Entry<String, List<PointCheckScope>>> entrySet = map.entrySet();
		// map遍历
		for (Iterator<Entry<String, List<PointCheckScope>>> i = entrySet.iterator(); i.hasNext();) {
			Entry<String, List<PointCheckScope>> next = i.next();
			// 该集合对象不包含数据库已经存在的点检项目对象
			List<PointCheckScope> list = next.getValue();
			// 排序
			Collections.sort(list);
			// 获取下一个可用的顺序号
			// Modified by hetao on 2017-02-22 修改seqNo初始值获取的方式（原：int seqNo = 0;），避免第二次导入部分数据时，排序错误的情况发生
			int seqNo = this.getNextSeqNo(list.get(0).getClassCode());
			for (PointCheckScope entity : list) {
				// 设置获取到的排序号为空，避免导入模板中的同一类型设备的点检项目的顺序号可能不是从1开始排列的异常情况
				entity.setSeqNo(seqNo++);
				this.saveOrUpdate(entity);
			}
		}

	}

	/**
	 * <li>说明：根据导入数据构造设备点检项目实体对象，如果点检项目已经存在（重复导入），则返回空对象
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年9月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param row 导入模板行对象
	 * @param rowIndex 当前数据所在行索引
	 * @return 设备点检项目实体对象
	 */
	private PointCheckScope getPointCheckScope(Row row, int rowIndex) {
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

		// 点检内容
		String checkContent = row.getCell(3).getStringCellValue();

		// 验证点检检查项目是否已经存在
		if (isExist(classCode, checkContent)) {
			return null;
		}

		// 顺序号
		Integer seqNo = null;
		if (Cell.CELL_TYPE_NUMERIC == row.getCell(2).getCellType()) {
			Double numericCellValue = row.getCell(2).getNumericCellValue();
			seqNo = numericCellValue.intValue();
		} else if (Cell.CELL_TYPE_STRING == row.getCell(0).getCellType()) {
			seqNo = Integer.valueOf(row.getCell(2).getStringCellValue());
		}
		// new设备点检范围实体对象
		return new PointCheckScope(classCode, seqNo, checkContent);
	}

	/**
	 * <li>说明：验证点检检查项目是否已经存在
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年9月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @param checkItem 点检项目
	 * @return 点检项目是否已经存在，true：已经存在，false：不存在
	 */
	private boolean isExist(String classCode, String checkContent) {
		String hql = "From PointCheckScope Where classCode = ? And checkContent = ?";
		int count = this.daoUtils.getCount(hql, classCode, checkContent);
		return 0 < count;
	}

	/**
	 * <li>说明：验证导入模板格式是否有误
	 * <li>创建人：兰佳 妮
	 * <li>创建日期：2016年9月26日
	 * <li>修改人：何涛
	 * <li>修改内容：修改导入模板列头关键字，同步修改验证规则
	 * <li>修改日期：2017年2月22日
	 * @param row 导入模板列头行对象
	 * @return 是否是一个合法的导入模板，true：合法，false：非法
	 */
	private boolean isValidImportMoudle(Row row) {
		if (null == row || !"设备（类别）编号*".equals(row.getCell(0).getStringCellValue()) || !"设备（类别）名称".equals(row.getCell(1).getStringCellValue())
				|| !"顺序号*".equals(row.getCell(2).getStringCellValue()) || !"点检内容*".equals(row.getCell(3).getStringCellValue())) {
			return false;
		}
		return true;
	}
}
