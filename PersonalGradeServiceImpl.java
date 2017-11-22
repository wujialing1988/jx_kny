package com.xx.grade.personal.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xx.grade.personal.entity.IndexTypeRoleWeight;
import com.xx.grade.personal.entity.PersonalDuty;
import com.xx.grade.personal.entity.PersonalGrade;
import com.xx.grade.personal.entity.PersonalGradeDetails;
import com.xx.grade.personal.entity.PersonalGradeResult;
import com.xx.grade.personal.entity.PersonalGradeResultDetails;
import com.xx.grade.personal.entity.PersonalWeight;
import com.xx.grade.personal.service.IPersonalGradeService;
import com.xx.grade.personal.service.IPersonalWeightService;
import com.xx.grade.personal.vo.PersonalDutyVo;
import com.xx.grade.personal.vo.PersonalGradeResultDetailsVo;
import com.xx.grade.personal.vo.PersonalGradeResultVo;
import com.xx.grade.personal.vo.PersonalGradeVo;
import com.xx.grade.personal.vo.ScoreVo;
import com.xx.system.common.constant.Constant;
import com.xx.system.common.dao.IBaseDao;
import com.xx.system.common.exception.BusinessException;
import com.xx.system.common.util.DateUtil;
import com.xx.system.common.util.StringUtil;
import com.xx.system.common.vo.ListVo;
import com.xx.system.deptgrade.entity.FinalScore;
import com.xx.system.dict.entity.Dictionary;
import com.xx.system.dict.service.IDictService;
import com.xx.system.org.entity.Duty;
import com.xx.system.org.entity.OrgUser;
import com.xx.system.org.entity.Organization;
import com.xx.system.role.entity.Role;
import com.xx.system.user.entity.User;
import com.xx.system.user.util.HSSFUtils;

/**
 * 个人评分服务实现类
 * 
 * @author wujialing
 *
 */
@SuppressWarnings("unchecked")
@Service("personalGradeService")
public class PersonalGradeServiceImpl implements IPersonalGradeService {

	@Autowired
	@Qualifier("baseDao")
	private IBaseDao baseDao;

	@Autowired
	@Qualifier("dictService")
	private IDictService dictService;

	@Autowired
	@Qualifier("personalWeightService")
	private IPersonalWeightService personalWeightService;

	@Override
	public ListVo<PersonalGradeVo> getPersonalGradeList(Map<String, String> paramMap) throws BusinessException {
		ListVo<PersonalGradeVo> result = new ListVo<PersonalGradeVo>();
		List<PersonalGradeVo> list = new ArrayList<PersonalGradeVo>();
		int totalSize = 0;
		int start = NumberUtils.toInt(paramMap.get("start"));
		int limit = NumberUtils.toInt(paramMap.get("limit"));
		// 用户ID 用户自评只能看自己的数据
		String userId = paramMap.get("userId");
		// 年份
		String gradeYear = paramMap.get("gradeYear");
		// 状态
		String status = paramMap.get("status");
		// 人员姓名
		String inputGradeUser = paramMap.get("inputGradeUser");
		// 部门
		String canpDeptQuery = paramMap.get("canpDeptQuery");

		StringBuffer hql = new StringBuffer();
		StringBuffer counthql = new StringBuffer();
		hql.append(" From PersonalGrade pg where 1=1 and pg.isDelete = 0 ");
		counthql.append(" select count(*) From PersonalGrade pg where 1=1 and pg.isDelete = 0 ");
		//如果评分人或者被评分人失效 禁用或者删除 则删除人员的数据
		hql.append(" and pg.user.status = 0 and pg.user.enable = 1 ");
		counthql.append(" and pg.user.status = 0 and pg.user.enable = 1 ");
		
		if (StringUtil.isNotEmpty(userId)) {
			hql.append(" and pg.user.userId = " + Integer.parseInt(userId));
			counthql.append(" and pg.user.userId = " + Integer.parseInt(userId));
		}
		if (StringUtil.isNotEmpty(gradeYear)) {
			hql.append(" and pg.gradeYear = '" + gradeYear + "'");
			counthql.append(" and pg.gradeYear = '" + gradeYear + "'");
		}
		if (StringUtil.isNotEmpty(status)) {
			hql.append(" and pg.status in (" + status+")");
			counthql.append(" and pg.status in (" + status+")");
		}

		if (StringUtil.isNotEmpty(inputGradeUser)) {
			hql.append(" and pg.user.realname like '%" + inputGradeUser + "%'");
			counthql.append(" and pg.user.realname like '%" + inputGradeUser + "%'");
		}

		if (StringUtil.isNotEmpty(canpDeptQuery) && !"0".equals(canpDeptQuery)) {
			String userIds = getAllUserIdsByOrgId(canpDeptQuery);
			// 找到该部门下所有人员，如果人员为空，则没有数据
			if (StringUtil.isNotEmpty(userIds)) {
				hql.append(" and pg.user.userId in (" + userIds + ")");
				counthql.append(" and pg.user.userId in (" + userIds + ")");
			} else {
				hql.append(" and 1=0 ");
				counthql.append(" and 1=0 ");
			}
		}

		totalSize = baseDao.getTotalCount(counthql.toString(), new HashMap<String, Object>());
		List<PersonalGrade> personalGradeLists = (List<PersonalGrade>) baseDao.queryEntitysByPage(start, limit,
				hql.toString(), new HashMap<String, Object>());
		for (PersonalGrade grade : personalGradeLists) {
			PersonalGradeVo vo = new PersonalGradeVo();
			buildEntityToVo(grade, vo);
			list.add(vo);
		}
		result.setList(list);
		result.setTotalSize(totalSize);
		return result;
	}

	/**
	 * 个人评分实体转vo
	 * 
	 * @param grade
	 * @param vo
	 */
	private void buildEntityToVo(PersonalGrade grade, PersonalGradeVo vo) {
		vo.setId(grade.getId());
		vo.setTitle(grade.getTitle());
		if (grade.getCompositeScores() != null) {
			vo.setCompositeScores(String.valueOf(grade.getCompositeScores()));
		}
		vo.setGradeYear(grade.getGradeYear());
		vo.setProblem(grade.getProblem());
		vo.setStatus(grade.getStatus());
		if (grade.getUser() != null) {
			vo.setUserId(grade.getUser().getUserId());
			vo.setUserName(grade.getUser().getRealname());
			if (grade.getUser().getResponsibilities() != null) {
				vo.setResponsibilities(grade.getUser().getResponsibilities().getName());
			}
			if (grade.getUser().getOrgUsers() != null) {
				for (OrgUser orguser : grade.getUser().getOrgUsers()) {
					vo.setOrgName(orguser.getOrganization().getOrgName());
					break;
				}
			}
		}
		vo.setTotalPersonCount(getResultCounts(null, grade.getId()));
		vo.setCommitPersonCount(getResultCounts(1, grade.getId()));
		vo.setWorkPlan(grade.getWorkPlan());
		setIsScoreChange(grade,vo);
	}
	
	/**
	 * 计算部门得分是否更新
	 * 
	 * @param grade
	 * @param vo
	 */
	private void setIsScoreChange(PersonalGrade grade, PersonalGradeVo vo){
		if (grade.getStatus() != 2) {
			vo.setIsScoreChange(0);
		}else{
			Set<PersonalGradeDetails> details = grade.getDetails();
			String orgScore = "" ;
			String newOrgScore = "" ;
			for (PersonalGradeDetails detail : details) {
				if (detail.isGrade() == null || detail.isGrade() == 0) {
					orgScore = detail.getScore() == null ? "" : detail.getScore().toString();
					User user = grade.getUser();
					Organization organization = null;
					for (OrgUser ou : user.getOrgUsers()) {
						if (ou.getOrganization() != null) {
							organization = ou.getOrganization();
						}
					}
					if (organization != null) {
						if (getBmScoreByOrg(organization, grade.getGradeYear()) != null) {
							newOrgScore = getBmScoreByOrg(organization, grade.getGradeYear()).toString();
						}
					}
				}
				break;
			}
			//部门得分有变化
			if (!orgScore.equals(newOrgScore)) {
				vo.setIsScoreChange(1);
			}else{
				vo.setIsScoreChange(0);
			}
		}
	}

	/**
	 * 通过个人评分获取该评分总数(status传null获取全部，传1获取提交人数，传0获取未提交reshuffle)
	 * 
	 * @param status
	 * @param personalGradeId
	 * @return
	 */
	private int getResultCounts(Integer status, int personalGradeId) {
		int count = 0;
		StringBuffer hql = new StringBuffer();
		hql.append(" select count(*) from PersonalGradeResult r where r.personalGrade.id = " + personalGradeId);
		hql.append(" and r.gradeUser.status = 0 and r.gradeUser.enable = 1 ");
		if (status != null) {
			hql.append(" and r.state = " + status);
		}
		count = baseDao.getTotalCount(hql.toString(), new HashMap<String, Object>());
		return count;
	}

	@Override
	public PersonalGradeVo getPersonalGradeById(int id) throws BusinessException {
		PersonalGrade grade = (PersonalGrade) baseDao.queryEntityById(PersonalGrade.class, id);
		PersonalGradeVo vo = new PersonalGradeVo();
		buildEntityToVo(grade, vo);
		return vo;
	}

	@Override
	public void editPersonalGrade(PersonalGrade grade) throws BusinessException {
		baseDao.updateEntity(grade);
	}

	@Override
	public PersonalGrade getPersonalGradeEntityById(int id) throws BusinessException {
		PersonalGrade grade = (PersonalGrade) baseDao.queryEntityById(PersonalGrade.class, id);
		return grade;
	}

	@Override
	public ListVo<PersonalDutyVo> getPersonalDutyList(Map<String, String> paramMap) {
		ListVo<PersonalDutyVo> result = new ListVo<PersonalDutyVo>();
		List<PersonalDutyVo> list = new ArrayList<PersonalDutyVo>();
		// 用户ID 用户自评只能看自己的数据
		String personalGradeId = paramMap.get("personalGradeId");
		StringBuffer hql = new StringBuffer();
		hql.append(" From PersonalDuty pg where 1=1  ");
		if (StringUtil.isNotEmpty(personalGradeId)) {
			hql.append(" and pg.personalGrade.id = " + Integer.parseInt(personalGradeId));
		} else {

		}
		List<PersonalDuty> personalDutyLists = (List<PersonalDuty>) baseDao.queryEntitys(hql.toString());
		for (PersonalDuty duty : personalDutyLists) {
			PersonalDutyVo vo = new PersonalDutyVo();
			buildDutyEntityToVo(duty, vo);
			list.add(vo);
		}
		result.setList(list);
		result.setTotalSize(list.size());
		return result;
	}

	/**
	 * 用户自评职责明细实体转换
	 * 
	 * @param duty
	 * @param vo
	 */
	private void buildDutyEntityToVo(PersonalDuty duty, PersonalDutyVo vo) {
		vo.setId(duty.getId());
		vo.setWorkDuty(duty.getWorkDuty());
		vo.setCompletion(duty.getCompletion());
	}

	@Override
	public PersonalDuty getPersonalDutyBy(int id) throws BusinessException {
		PersonalDuty duty = (PersonalDuty) this.baseDao.queryEntityById(PersonalDuty.class, id);
		return duty;
	}

	@Override
	public void updatePersonalDuty(PersonalDuty duty) throws BusinessException {
		this.baseDao.saveOrUpdate(duty);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public String submitPersonalGrade(String ids,User currentUser) {
		try {
			if (StringUtil.isNotEmpty(ids)) {
				StringBuffer sql = new StringBuffer();
				sql.append(" update T_PERSONAL_GRADE t set t.STATUS = 1 ");
				sql.append(" where t.id in ('").append(ids).append("')");
				this.baseDao.executeNativeSQL(sql.toString());
				//生成个人评分结果表
				generatePersonalGradeResult(ids, currentUser);
			}
			return "{success:true,msg:'提交个人评分成功！'}";
		} catch (Exception e) {
			return "{success:false,msg:'提交个人评分失败！'}";
		}
	}

	@Override
	public HSSFWorkbook exportPersonalDuty(Map<String, String> dutyMap,File file) {
		String personalGradeId = dutyMap.get("personalGradeId");
		HSSFWorkbook wb = null ;
		try {
			PersonalGrade grade = (PersonalGrade)baseDao.queryEntityById(PersonalGrade.class, Integer.parseInt(personalGradeId));
			//如果为空 不导出
			if (grade == null) {
				return null ;
			}
			Set<PersonalDuty> personalDutys = grade.getPersonalDutys();
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			// 读取excel模板
			wb = new HSSFWorkbook(fs);
			HSSFSheet aSheet = wb.getSheetAt(0);
			// 插入表头信息
			HSSFRow row0 = aSheet.getRow(0);
			HSSFCell cell0 = row0.getCell(0);
			cell0.setCellValue(grade.getGradeYear()+"年员工年度考核登记表");
			// 插入职员个人信息
			HSSFRow row1 = aSheet.getRow(1);
			HSSFRow row2 = aSheet.getRow(2);
			HSSFRow row3 = aSheet.getRow(3);
			HSSFCell cell11 = row1.getCell(1);
			HSSFCell cell13 = row1.getCell(4);
			HSSFCell cell15 = row1.getCell(6);

			HSSFCell cell21 = row2.getCell(1);
			HSSFCell cell23 = row2.getCell(4);
			HSSFCell cell25 = row2.getCell(6);

			HSSFCell cell31 = row3.getCell(1);
			HSSFCell cell33 = row3.getCell(6);

			if (grade.getUser() != null) {
				cell11.setCellValue(grade.getUser().getRealname());
				cell13.setCellValue(grade.getUser().getGender());
				cell15.setCellValue(grade.getUser().getBirthDay());
				cell21.setCellValue(grade.getUser().getPoliticsStatus());
				cell23.setCellValue(grade.getUser().getEducationBackground());
				cell25.setCellValue(grade.getUser().getJobStartDate());
				if (grade.getUser().getResponsibilities() != null) {
					cell31.setCellValue(grade.getUser().getResponsibilities().getName());
				}
				// 现任岗位时间
				cell33.setCellValue(grade.getUser().getRespChangeDate());
			}
			
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			
			HSSFCellStyle styleBold = getNewCenterStyle(wb);
			// 获取单元格格式
			int newRow = 6; // 从第几行开始插入
			int rows = personalDutys.size();// 设定插入几行
			if (personalDutys != null && personalDutys.size() > 0) {
				aSheet.shiftRows(newRow, aSheet.getLastRowNum(), rows, true, true);
				int rowSize = 0;
				for (PersonalDuty duty : personalDutys) {
					HSSFRow sourceRow = aSheet.getRow(newRow + rowSize);
						if (sourceRow == null) {
							sourceRow = aSheet.createRow(newRow + rowSize);
						}
						System.err.println(getExcelCellAutoHeight(duty.getWorkDuty(), 20));
						sourceRow.setHeight((short) 400);
						// 合并 单元格 操作* 第一个参数 0 表示 起始 行* 第二个参数 a表示 起始 列* 第三个参数 0
						// 表示结束行* 第四个参数 b表示结束列
						Region region =  new Region(newRow + rowSize, (short) 0, newRow + rowSize, (short) 2);
						setRegionStyle(aSheet,region,styleBold);//设置合并单元格的风格（加边框）
						aSheet.addMergedRegion(region); //
						HSSFCell cew2 = sourceRow.createCell((short) 0);
						cew2.setCellValue(duty.getWorkDuty());
						cew2.setCellStyle(styleBold);
						
						Region region1 =  new Region(newRow + rowSize, (short) 3, newRow + rowSize, (short) 6);
						setRegionStyle(aSheet,region1,styleBold);
						aSheet.addMergedRegion(region1); //
						HSSFCell cew3 = sourceRow.createCell((short) 3);
						cew3.setCellValue(duty.getCompletion());
						cew3.setCellStyle(styleBold);
						rowSize++;
				}
			}

			// 写入其他信息
			HSSFCellStyle cellStyle2 = wb.createCellStyle();
			cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			
			HSSFRow row4 = aSheet.getRow(newRow + personalDutys.size() + 1);
			HSSFCell cell41 = row4.getCell(0);
			cell41.setCellStyle(cellStyle2);
			cell41.setCellValue(grade.getProblem());

			HSSFRow row5 = aSheet.getRow(newRow + personalDutys.size() + 3);
			HSSFCell cell51 = row5.getCell(0);
			cell51.setCellStyle(cellStyle2);
			cell51.setCellValue(grade.getWorkPlan());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	@Override
	public String uploadPersonalDutyExcel(String fileUrl, Map<String, String> paramsMap) {
		// 标示
		String message = "importSuccess";
		String personalGradeId = paramsMap.get("personalGradeId");
		String[][] content = null;
		try {
			content = HSSFUtils.extractTextFromExcel(fileUrl);
		} catch (Exception e) {
			message = "解析excel出错！";
		}

		if (null == content) {
			message = "不是有效的Excel文件,请按照模版来定义！";
		} else {
			File attachFile = new File(fileUrl);
			attachFile.delete();
			int col = content.length;
			List<PersonalDuty> duties = new ArrayList<PersonalDuty>();
			PersonalGrade grade = (PersonalGrade) baseDao.queryEntityById(PersonalGrade.class,
					Integer.parseInt(personalGradeId));
			for (int i = 6; i < col-5 ; i++) {
				String workDuty = content[i][0];
				String completion = content[i][3];
				PersonalDuty duty = getPersonalDutyByGradeAndWorkDuty(grade, workDuty);
				if (duty != null) {
					duty.setCompletion(completion.equals("null")?"":completion);
					duties.add(duty);
				}
			}
			this.baseDao.saveOrUpdate(duties);
			//修改工作计划和总结
			String workPlan = content[col-2][0];
			String problem = content[col-4][0];
			grade.setProblem(problem);
			grade.setWorkPlan(workPlan);
			this.baseDao.update(grade);
		}
		return message;
	}
	
	/**
	 * 获取员工职责实体
	 * 
	 * @param grade
	 * @param workDuty
	 * @return
	 */
	private PersonalDuty getPersonalDutyByGradeAndWorkDuty(PersonalGrade grade,String workDuty){
		PersonalDuty duty = null ;
		String hql = "From PersonalDuty d where d.personalGrade.id=" + grade.getId() + " and workDuty = '"+workDuty+"'";
		List<PersonalDuty> dutys =  baseDao.queryEntitys(hql.toString());
		if (dutys != null && dutys.size() > 0) {
			duty = dutys.get(0);
		}
		return duty ;
	}

	/**
	 * 生成个人评分结果表 20151028改版
	 */
	public void generatePersonalGradeResult(String ids, User curUser) {
		if (StringUtil.isNotEmpty(ids) && curUser != null) {
			StringBuffer hql = new StringBuffer();
			hql.append(" From PersonalGrade pg where pg.status = 1 ");
			hql.append(" and pg.id in ('" + ids + "')");
			List<PersonalGrade> grades = baseDao.queryEntitys(hql.toString());

			// 获取当前登录人部门
			Set<OrgUser> currentOrgs = curUser.getOrgUsers();
			Organization currentOrg = null;
			for (OrgUser orgUser : currentOrgs) {
				if (orgUser.getOrganization() != null) {
					currentOrg = orgUser.getOrganization();
					break;
				}
			}

			for (PersonalGrade grade : grades) {
				Dictionary classification = grade.getClassification();
				// 获取该权重分类下的所有权重维护
				List<PersonalWeight> pws = personalWeightService
						.getPersonalWeightByClassification(classification.getPkDictionaryId());

				// 生成指标评分明细历史
				for (PersonalWeight pw : pws) {
					PersonalGradeDetails gradeDetail = getGradeDetailsByGrade(grade, pw.getIndexType());
					if (gradeDetail == null) {
						gradeDetail = new PersonalGradeDetails();
						gradeDetail.setPersonalGrade(grade);
						gradeDetail.setIndexType(pw.getIndexType());
						gradeDetail.setPercentage(pw.getPercentage());
						gradeDetail.setGrade(pw.getIsGrade());
						baseDao.save(gradeDetail);
					}
				}

				for (PersonalWeight pw : pws) {
					// 对于参与评分的指标
					if (pw.getIsGrade() != null && pw.getIsGrade() == 1) {
						// 获取该指标下所有角色权重
						Set<IndexTypeRoleWeight> rws = pw.getIndexTypeRoles();
						Iterator<IndexTypeRoleWeight> it =rws.iterator();
						while (it.hasNext()) {
							IndexTypeRoleWeight rw = it.next();
							Role role = rw.getRole();
							List<User> users = getUsersExcludeSelf(role, grade.getUser().getUserId(), currentOrg);
							for (User user : users) {
								PersonalGradeResult result = getResultByUserAndGrade(user, grade);
								if (result == null) {
									result = new PersonalGradeResult();
									result.setPersonalGrade(grade);
									result.setGradeUser(user);
									result.setState(0);
									result.setGradeUserType(0);
									baseDao.save(result);
								}
								PersonalGradeResultDetails detail = getDetailsByRoleAndIndexAndResult(role,
										pw.getIndexType(), result);
								if (detail == null) {
									detail = new PersonalGradeResultDetails();
									detail.setPersonalGradeResult(result);
									detail.setIndexType(pw.getIndexType());
									detail.setRole(role);
									detail.setPercentage(rw.getPercentage());
									baseDao.save(detail);
								}
							}
						}
					}
				}
				//对于部门主任或副主任评分的角色，需过滤掉同一人为分管领导和副所长的角色
				if (classification.getDictCode().equals(Constant.QZFL_BMLD)) {
					deleteDetailsForSameRole(grade);
				}
			
			}
		}
	}

	/**
	 * 对于部门主任或副主任评分的角色，需过滤掉同一人为分管领导和副所长的角色
	 * 
	 * @param grade
	 */
	private void deleteDetailsForSameRole(PersonalGrade grade){
		List<PersonalGradeResult> results = baseDao.queryEntitys(" From PersonalGradeResult r where r.personalGrade.id = "+grade.getId());
		for (PersonalGradeResult result : results) {
			List<PersonalGradeResultDetails> details = baseDao.queryEntitys(" From PersonalGradeResultDetails r where r.personalGradeResult.id = "+result.getId());
			List<PersonalGradeResultDetails> roleA = new ArrayList<PersonalGradeResultDetails>() ;
			List<PersonalGradeResultDetails> roleB = new ArrayList<PersonalGradeResultDetails>() ;
			for (PersonalGradeResultDetails detail : details) {
				if ("分管领导".equals(detail.getRole().getRoleName())) {
					roleA.add(detail) ;
				}else if("副所长".equals(detail.getRole().getRoleName())){
					roleB.add(detail) ;
				}
			}
			
			String delIds = "" ;
			
			//如果两个角色都不为空
			if (roleA.size() > 0 && roleB.size() > 0) {
				for (PersonalGradeResultDetails detailB : roleB) {
					for (PersonalGradeResultDetails detailA : roleA) {
						//如果评分类型相同
						if (detailA.getIndexType().getDictCode().equals(detailB.getIndexType().getDictCode())) {
							delIds += detailB.getId() + "," ;
							break;
						}
					}
				}
			}
			
			if (StringUtil.isNotEmpty(delIds)) {
				delIds = delIds.substring(0, delIds.length()-1);
				baseDao.executeHql(" delete from PersonalGradeResultDetails d where d.id in ("+delIds+")");
			}
		}
	}

	/**
	 * 获取个人评分指标历史
	 * 
	 * @param grade
	 * @param indexType
	 * @return
	 */
	private PersonalGradeDetails getGradeDetailsByGrade(PersonalGrade grade, Dictionary indexType) {
		PersonalGradeDetails gradeDetail = null;
		StringBuffer hql = new StringBuffer();
		hql.append(" From PersonalGradeDetails pd where pd.personalGrade.id=" + grade.getId());
		hql.append(" and pd.indexType.pkDictionaryId=" + indexType.getPkDictionaryId());
		List<PersonalGradeDetails> gradeDetails = baseDao.queryEntitys(hql.toString());
		if (gradeDetails != null && gradeDetails.size() > 0) {
			gradeDetail = gradeDetails.get(0);
		}
		return gradeDetail;
	}

	/**
	 * 通过角色 指标 结果表查找详情明细表
	 * 
	 * @param role
	 * @param indexType
	 * @param result
	 * @return
	 */
	private PersonalGradeResultDetails getDetailsByRoleAndIndexAndResult(Role role, Dictionary indexType,
			PersonalGradeResult result) {
		PersonalGradeResultDetails detail = null;
		StringBuffer hql = new StringBuffer();
		hql.append(" From PersonalGradeResultDetails d where d.personalGradeResult.id =" + result.getId()
				+ " and d.indexType.pkDictionaryId=" + indexType.getPkDictionaryId() + " and d.role.roleId="
				+ role.getRoleId());
		List<PersonalGradeResultDetails> details = baseDao.queryEntitys(hql.toString());
		if (details != null && details.size() > 0) {
			detail = details.get(0);
		}
		return detail;
	}

	/**
	 * 通过个人得分与评分人找结果
	 * 
	 * @param user
	 * @param grade
	 * @return
	 */
	private PersonalGradeResult getResultByUserAndGrade(User user, PersonalGrade grade) {
		PersonalGradeResult result = null;
		StringBuffer hql = new StringBuffer();
		hql.append(" From PersonalGradeResult r where r.personalGrade.id =" + grade.getId() + " and r.gradeUser.userId="
				+ user.getUserId());
		List<PersonalGradeResult> results = baseDao.queryEntitys(hql.toString());
		if (results != null && results.size() > 0) {
			result = results.get(0);
		}
		return result;
	}

	/**
	 * 查找角色对应的人员，极为重要的方法，此方法决定了评分人
	 * 
	 * @param role
	 * @param userId
	 * @param currentOrg
	 * @return
	 */
	private List<User> getUsersExcludeSelf(Role role, Integer userId, Organization currentOrg) {
		List<User> users = null;
		StringBuffer hql = new StringBuffer();
		// 如果是一般员工角色，则需要排除自己
		if (role.getRoleName().equals("一般员工")) {
			String currentOrgUserIds = getUserIdsByCurrentOrg(currentOrg);
			hql.append(" select rs.user From RoleMemberScope rs  where rs.role.roleId =" + role.getRoleId()
					+ "  and rs.user.userId <> " + userId);
			hql.append(" and rs.user.status = 0 and rs.user.enable = 1 ");
			if (StringUtil.isNotEmpty(currentOrgUserIds)) {
				hql.append(" and rs.user.userId in ( "+currentOrgUserIds+")");
			}else {
				hql.append(" and 1=0 ");
			}
		} // 如果是与部门挂钩的角色，则需要找到对应角色范围属于该部门的员工
		else if (role.getRoleName().equals("部门主任") || role.getRoleName().equals("部门副主任")
				|| role.getRoleName().equals("分管领导") || role.getRoleName().equals("分管副所长")
				|| role.getRoleName().equals("部门负责人")) {
			hql.append(" select sm.roleMemberScope.user From ScopeMember sm where sm.org.orgId = "
					+ currentOrg.getOrgId() + " and sm.roleMemberScope.role.roleId = " + role.getRoleId());
			hql.append(" and sm.roleMemberScope.user.status = 0 and sm.roleMemberScope.user.enable = 1 ");
			if ("部门主任".equals(role.getRoleName())) {
				//hql.append(" and sm.roleMemberScope.user.userId <> " + userId) ;
			}
		} // 其他角色，直接取对于角色下的所有人
		else {
			hql.append(" select rs.user From RoleMemberScope rs where rs.role.roleId =" + role.getRoleId());
			hql.append(" and rs.user.status = 0 and rs.user.enable = 1 ");
			//hql.append(" and rs.user.userId <> " + userId) ;
		}
		users = baseDao.queryEntitys(hql.toString());
		return users;
	}

	/**
	 * 获取部门下所有员工
	 * @param currentOrg 
	 * 
	 * @return
	 */
	private String getUserIdsByCurrentOrg(Organization currentOrg) {
		String userIds = "" ;
		StringBuffer hql = new StringBuffer();
		hql.append(" From OrgUser ou where ou.organization.orgId = "+currentOrg.getOrgId());
		List<OrgUser> list = baseDao.queryEntitys(hql.toString());
		if (list != null && list.size() > 0) {
			for (OrgUser orgUser : list) {
				if (orgUser.getUser() != null) {
					userIds += ","+orgUser.getUser().getUserId() ;
				}
			}
		}
		if (StringUtil.isNotEmpty(userIds)) {
			userIds = userIds.substring(1,userIds.length());
		}
		return userIds;
	}
	/**
	 * 生成个人评分结果表（已停用）
	 * 
	 * @param ids
	 * @param curUser
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void generatePersonalGradeResultOld(String ids, User curUser) {
		if (StringUtil.isNotEmpty(ids) && curUser != null) {
			StringBuffer hql = new StringBuffer();
			hql.append(" From PersonalGrade pg where pg.status = 1 ");
			hql.append(" and pg.id in ('" + ids + "')");
			List<PersonalGrade> grades = baseDao.queryEntitys(hql.toString());

			// 获取当前登录人部门
			Set<OrgUser> currentOrgs = curUser.getOrgUsers();
			Organization currentOrg = null;
			for (OrgUser orgUser : currentOrgs) {
				if (orgUser.getOrganization() != null) {
					currentOrg = orgUser.getOrganization();
					break;
				}
			}

			// 获取该人员组织下所有人员（排除领导和自己）
			List<User> resultUser = getResultUserByCurrentOrg(curUser);
			List<PersonalGradeResult> gradeResults = new ArrayList<PersonalGradeResult>();
			for (PersonalGrade grade : grades) {
				// 如果是部门领导（主任），则评分人为其他三位领导，否则为部门其他人和四位领导评分
				if (currentOrg != null && currentOrg.getDeptHead() != null
						&& currentOrg.getDeptHead().getUserId() == curUser.getUserId()) {

				} else {
					for (User user : resultUser) {
						PersonalGradeResult result = new PersonalGradeResult();
						result.setPersonalGrade(grade);
						result.setGradeUser(user);
						result.setState(0);
						result.setGradeUserType(0);
						gradeResults.add(result);
					}
					// 添加部门领导 分管领导 协管领导 所长
					// 添加部门领导
					if (currentOrg.getDeptHead() != null) {
						PersonalGradeResult result = new PersonalGradeResult();
						result.setPersonalGrade(grade);
						result.setGradeUser(currentOrg.getDeptHead());
						result.setState(0);
						result.setGradeUserType(1);
						gradeResults.add(result);
					}
				}
				// 分管领导
				if (currentOrg.getBranchedLeader() != null) {
					PersonalGradeResult result = new PersonalGradeResult();
					result.setPersonalGrade(grade);
					//result.setGradeUser(currentOrg.getBranchedLeader());
					result.setState(0);
					result.setGradeUserType(2);
					gradeResults.add(result);
				}
				// 协管领导
				if (currentOrg.getOtherSup() != null) {
					PersonalGradeResult result = new PersonalGradeResult();
					result.setPersonalGrade(grade);
					//result.setGradeUser(currentOrg.getOtherSup());
					result.setState(0);
					result.setGradeUserType(3);
					gradeResults.add(result);
				}
				// 所领导
				if (currentOrg.getSuperintendent() != null) {
					PersonalGradeResult result = new PersonalGradeResult();
					result.setPersonalGrade(grade);
					result.setGradeUser(currentOrg.getSuperintendent());
					result.setState(0);
					result.setGradeUserType(4);
					gradeResults.add(result);
				}
			}
			// 如果有数据 批量保存
			if (gradeResults != null && gradeResults.size() > 0) {
				baseDao.saveOrUpdate(gradeResults);
			}
		}
	}

	/**
	 * 获取当前组织下所有人员及所有上级组织领导
	 * 
	 * @param currentOrg
	 * @param curUser
	 * @return
	 */
	private List<User> getResultUserByCurrentOrg(User curUser) {
		String userId = "";
		// 获取当前组织下所有人员

		Set<OrgUser> currentOrgs = curUser.getOrgUsers();
		Organization currentOrg = null;
		for (OrgUser orgUser : currentOrgs) {
			if (orgUser.getOrganization() != null) {
				currentOrg = orgUser.getOrganization();
				break;
			}
		}

		StringBuffer OrgUserhql = new StringBuffer();
		OrgUserhql.append(" From OrgUser ou where ou.isDelete = 0 and ou.organization.orgId =" + currentOrg.getOrgId());
		OrgUserhql.append(" and ou.user.userId <> " + curUser.getUserId());
		// 排除领导
		if (currentOrg.getDeptHead() != null) {
			OrgUserhql.append(" and ou.user.userId <> " + currentOrg.getDeptHead().getUserId());
		}
		if (currentOrg.getBranchedLeader() != null) {
			//OrgUserhql.append(" and ou.user.userId <> " + currentOrg.getBranchedLeader().getUserId());
		}
		if (currentOrg.getOtherSup() != null) {
			//OrgUserhql.append(" and ou.user.userId <> " + currentOrg.getOtherSup().getUserId());
		}
		if (currentOrg.getSuperintendent() != null) {
			OrgUserhql.append(" and ou.user.userId <> " + currentOrg.getSuperintendent().getUserId());
		}
		List<OrgUser> orgUsers2 = baseDao.queryEntitys(OrgUserhql.toString());
		for (OrgUser orgUser : orgUsers2) {
			if (orgUser.getUser() != null && orgUser.getUser().getEnable() == 1) {
				userId += "," + orgUser.getUser().getUserId();
			}
		}
		if (StringUtil.isNotEmpty(userId)) {
			userId = userId.substring(1, userId.length());
		}
		// 获取所有上级组织领导 
		StringBuffer hql = new StringBuffer();
		hql.append(" From User u where u.userId in (" + userId + ")");
		List<User> users = baseDao.queryEntitys(hql.toString());
		return users;
	}

	/**
	 * 获取部门下所有的人员id集合
	 * 
	 * @param orgId
	 * @return
	 */
	private String getAllUserIdsByOrgId(String orgId) {
		String userId = "";
		StringBuffer OrgUserhql = new StringBuffer();
		OrgUserhql.append(" From OrgUser ou where ou.isDelete = 0 and ou.organization.orgId ='" + orgId + "'");
		List<OrgUser> orgUsers2 = baseDao.queryEntitys(OrgUserhql.toString());
		for (OrgUser orgUser : orgUsers2) {
			if (orgUser.getUser() != null && orgUser.getUser().getEnable() == 1) {
				userId += "," + orgUser.getUser().getUserId();
			}
		}
		if (StringUtil.isNotEmpty(userId)) {
			userId = userId.substring(1, userId.length());
		}
		return userId;
	}

	@Override
	public ListVo<PersonalGradeResultVo> getPersonalGradeResultList(Map<String, String> paramMap) {
		ListVo<PersonalGradeResultVo> result = new ListVo<PersonalGradeResultVo>();
		List<PersonalGradeResultVo> list = new ArrayList<PersonalGradeResultVo>();
		int totalSize = 0;
		int start = NumberUtils.toInt(paramMap.get("start"));
		int limit = NumberUtils.toInt(paramMap.get("limit"));
		// 用户ID 用户自评只能看自己的数据
		String userId = paramMap.get("userId");
		String state = paramMap.get("state");
		// 个人评分 对应的
		String personalGradeId = paramMap.get("personalGradeId");
		String inputGradeUser = paramMap.get("inputGradeUser");
		String inputUserName = paramMap.get("inputUserName");
		String canpDeptQuery = paramMap.get("canpDeptQuery");
		// 标题
		String inputTitle = paramMap.get("inputTitle");
		StringBuffer hql = new StringBuffer();
		StringBuffer counthql = new StringBuffer();
		hql.append(" From PersonalGradeResult pgr where 1=1");
		counthql.append(" select count(*) From PersonalGradeResult pgr where 1=1 ");
		
		hql.append(" and pgr.gradeUser.status = 0 and pgr.gradeUser.enable =1 ");
		counthql.append(" and pgr.gradeUser.status = 0 and pgr.gradeUser.enable =1 ");
		
		if (StringUtil.isNotEmpty(userId)) {
			hql.append(" and pgr.gradeUser.userId = " + Integer.parseInt(userId));
			counthql.append(" and pgr.gradeUser.userId = " + Integer.parseInt(userId));
		}

		if (StringUtil.isNotEmpty(state)) {
			hql.append(" and pgr.state = " + Integer.parseInt(state));
			counthql.append(" and pgr.state = " + Integer.parseInt(state));
		}

		
		if (StringUtil.isNotEmpty(inputGradeUser)) {
			hql.append(" and pgr.personalGrade.user.realname like '%" + inputGradeUser + "%'");
			counthql.append(" and pgr.personalGrade.user.realname like '%" + inputGradeUser + "%'");
		}

		// 标题
		if (StringUtil.isNotEmpty(inputTitle)) {
			hql.append(" and pgr.personalGrade.title like '%" + inputTitle + "%'");
			counthql.append(" and pgr.personalGrade.title like '%" + inputTitle + "%'");
		}

		if (StringUtil.isNotEmpty(inputUserName)) {
			hql.append(" and pgr.gradeUser.realname like '%" + inputUserName + "%'");
			counthql.append(" and pgr.gradeUser.realname like '%" + inputUserName + "%'");
		}

		if (StringUtil.isNotEmpty(personalGradeId)) {
			hql.append(" and pgr.personalGrade.id = '" + personalGradeId + "'");
			counthql.append(" and pgr.personalGrade.id = '" + personalGradeId + "'");
		}

		if (StringUtil.isNotEmpty(canpDeptQuery) && !"0".equals(canpDeptQuery)) {
			String userIds = getAllUserIdsByOrgId(canpDeptQuery);
			// 找到该部门下所有人员，如果人员为空，则没有数据
			if (StringUtil.isNotEmpty(userIds)) {
				hql.append(" and pgr.personalGrade.user.userId in (" + userIds + ")");
				counthql.append(" and pgr.personalGrade.user.userId in (" + userIds + ")");
			} else {
				hql.append(" and 1=0 ");
				counthql.append(" and 1=0 ");
			}
		}
		
		hql.append(" and pgr.personalGrade.isDelete = 0  ");
		counthql.append(" and pgr.personalGrade.isDelete = 0  ");
		
		// 评分状态排序 满足点击评分人员列表需求
		hql.append(" order by pgr.state");

		totalSize = baseDao.getTotalCount(counthql.toString(), new HashMap<String, Object>());
		List<PersonalGradeResult> personalGradeResults = (List<PersonalGradeResult>) baseDao.queryEntitysByPage(start,
				limit, hql.toString(), new HashMap<String, Object>());
		for (PersonalGradeResult gradeResult : personalGradeResults) {
			PersonalGradeResultVo vo = new PersonalGradeResultVo();
			buildResultEntityToVo(gradeResult, vo);
			list.add(vo);
		}
		result.setList(list);
		result.setTotalSize(totalSize);
		return result;
	}

	private void buildResultEntityToVo(PersonalGradeResult gradeResult, PersonalGradeResultVo vo) {
		vo.setId(gradeResult.getId());
		vo.setUserName(gradeResult.getGradeUser().getRealname());
		if (gradeResult.getGradeDate() != null) {
			vo.setGradeDate(DateUtil.dateToString(gradeResult.getGradeDate(), "yyyy-MM-dd HH:mm:ss"));
		}
		vo.setState(gradeResult.getState());
		vo.setGradeUserType(getGradeUserTypeByResult(gradeResult));
		vo.setEvaluation(gradeResult.getEvaluation());
		vo.setEvaluation1(gradeResult.getEvaluation1());
		vo.setEvaluation2(gradeResult.getEvaluation3());
		vo.setEvaluation3(gradeResult.getEvaluation3());
		vo.setIsHaveGrade(isHaveGrade(gradeResult));
		if (gradeResult.getPersonalGrade() != null) {
			PersonalGrade grade = gradeResult.getPersonalGrade();
			vo.setGradeYear(grade.getGradeYear());
			vo.setPersonalGradeId(grade.getId());
			if (grade.getUser() != null) {
				User user = grade.getUser();
				vo.setGradeUser(user.getRealname());
				if (user.getResponsibilities() != null) {
					vo.setResponsibilities(user.getResponsibilities().getName());
				}
				vo.setJobStartDate(user.getJobStartDate());
				vo.setRespChangeDate(user.getRespChangeDate());
				vo.setBirthDay(user.getBirthDay());
				vo.setGender(user.getGender());
				vo.setPoliticsStatus(user.getPoliticsStatus());
				vo.setEducationBackground(user.getEducationBackground());
				if (user.getOrgUsers() != null) {
					for (OrgUser orguser : user.getOrgUsers()) {
						vo.setGradeOrg(orguser.getOrganization().getOrgName());
						break;
					}
				}
			}
			vo.setTitle(grade.getTitle());
			vo.setProblem(grade.getProblem());
			vo.setWorkPlan(grade.getWorkPlan());
		}
	}

	/**
	 * 获取当前评分人是否具有领导职位
	 */
	private String getGradeUserTypeByResult(PersonalGradeResult gradeResult) {
		String result = "";
		String isBmld = "false";
		String isfgld = "false";
		String isqtsld = "false";
		String issld = "false";
		Set<PersonalGradeResultDetails> details = gradeResult.getDetails();
		Iterator<PersonalGradeResultDetails> it = details.iterator();
		while (it.hasNext()) {
			PersonalGradeResultDetails detail = it.next();
			if ("部门主任".equals(detail.getRole().getRoleName())) {
				isBmld = "true";
			}
			if ("分管领导".equals(detail.getRole().getRoleName())) {
				isfgld = "true";
			}
			if ("分管副所长".equals(detail.getRole().getRoleName())) {
				isqtsld = "true";
			}
			if ("所长".equals(detail.getRole().getRoleName())) {
				issld = "true";
			}
		}
		result = isBmld + "," + isfgld + "," + isqtsld + "," + issld;
		return result;
	}

	@Override
	public PersonalGradeResultVo getPersonalGradeResultById(int id) {
		PersonalGradeResult result = (PersonalGradeResult) baseDao.queryEntityById(PersonalGradeResult.class, id);
		PersonalGradeResultVo vo = new PersonalGradeResultVo();
		buildResultEntityToVo(result, vo);
		return vo;
	}

	@Override
	public PersonalGradeResult getPersonalGradeResultEntityById(int id) {
		PersonalGradeResult result = (PersonalGradeResult) baseDao.queryEntityById(PersonalGradeResult.class, id);
		return result;
	}

	@Override
	public void editPersonalGradeResult(PersonalGradeResult result) {
		baseDao.updateEntity(result);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public String submitPersonalGradeResult(String ids) {
		try {
			String msg = "" ;
			if (StringUtil.isNotEmpty(ids)) {
				String[] idsArr = ids.split(",");
				for (String id : idsArr) {
					PersonalGradeResult result = (PersonalGradeResult) baseDao
							.queryEntityById(PersonalGradeResult.class, Integer.parseInt(id));
					Set<PersonalGradeResultDetails> details = result.getDetails();
					for (PersonalGradeResultDetails detail : details) {
						if (detail.getScore() == null) {
							msg += result.getPersonalGrade().getTitle()+"<br/>" ;
							break ;
						}
					}
					if (result != null && StringUtil.isEmpty(msg)) {
						result.setState(1);
						result.setGradeDate(new Date());
						baseDao.saveOrUpdate(result);
						generateCompositeScoresNew(result);
					}
				}
			}
			if (StringUtil.isNotEmpty(msg)) {
				return "{success:false,msg:'"+msg+"存在未评分项，请先进行评分'}";
			}else{
				return "{success:true,msg:'提交成功！'}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{success:false,msg:'提交失败！'}";
		}
	}
	
	/**
	 * 是否评分
	 * 
	 * @param result
	 * @return
	 */
	private int isHaveGrade(PersonalGradeResult result){
		int isHaveGrade = 1 ;
		Set<PersonalGradeResultDetails> details = result.getDetails();
		for (PersonalGradeResultDetails detail : details) {
			if (detail.getScore() == null) {
				isHaveGrade = 0 ;
				break ;
			}
		}
		return isHaveGrade ;
	}

	/**
	 * 生成个人评分综合评分，如果该职工已被所有人评完，则生成总得分； 生成规则：评分人大于3人时，去掉最高最低，然后取平均分
	 * 
	 * @param result
	 */
	@SuppressWarnings("unused")
	private void generateCompositeScores(PersonalGradeResult result) {
		PersonalGrade grade = result.getPersonalGrade();
		if (grade != null) {
			StringBuffer hql = new StringBuffer();
			StringBuffer hqlCount = new StringBuffer();
			hql.append(" From PersonalGradeResult r where r.state=1 and r.personalGrade.id=" + grade.getId());
			hql.append(" order by r.score");
			hqlCount.append(" select count(*) from PersonalGradeResult r where r.personalGrade.id= " + grade.getId());
			int totalSize = baseDao.getTotalCount(hqlCount.toString(), new HashMap<String, Object>());
			List<PersonalGradeResult> results = baseDao.queryEntitys(hql.toString());
			// 判断是否已经提交完成
			if (results != null && results.size() == totalSize) {
				// 如果小于3，直接求平均分
				double totalScore = 0;
				int userCount = 0;
				if (totalSize < 3) {
					for (int i = 0; i < results.size(); i++) {
						PersonalGradeResult gradeResult = results.get(i);
						totalScore += gradeResult.getScore();
					}
					userCount = totalSize;
				}
				// 如果大于等于3，则需去掉两头
				else {
					for (int i = 1; i < results.size() - 1; i++) {
						PersonalGradeResult gradeResult = results.get(i);
						totalScore += gradeResult.getScore();
					}
					userCount = totalSize - 2;
				}
				int scale = 2; // 小数点精度
				BigDecimal a = new BigDecimal(String.valueOf(totalScore));
				BigDecimal b = new BigDecimal(String.valueOf(userCount));
				BigDecimal r = a.divide(b, scale, BigDecimal.ROUND_HALF_UP);
				grade.setCompositeScores(r.doubleValue());
				grade.setStatus(2);
				baseDao.saveOrUpdate(grade);
			}
		}
	}

	/**
	 * 生成个人评分综合评分 新版
	 * 
	 * @param result
	 */
	private void generateCompositeScoresNew(PersonalGradeResult result) {
		PersonalGrade grade = result.getPersonalGrade();
		if (grade != null) {
			StringBuffer hql = new StringBuffer();
			StringBuffer hqlCount = new StringBuffer();
			hql.append(" From PersonalGradeResult r where r.state=1 and r.personalGrade.id=" + grade.getId());
			hql.append(" and r.gradeUser.status = 0 and r.gradeUser.enable = 1");
			hqlCount.append(" select count(*) from PersonalGradeResult r where r.personalGrade.id= " + grade.getId());
			hqlCount.append(" and r.gradeUser.status = 0 and r.gradeUser.enable = 1");
			int totalSize = baseDao.getTotalCount(hqlCount.toString(), new HashMap<String, Object>());
			List<PersonalGradeResult> results = baseDao.queryEntitys(hql.toString());
			// 判断是否已经提交完成
			if (results != null && results.size() == totalSize) {
				Double totalScore = 0d;
				Set<PersonalGradeDetails> gradeDetails = grade.getDetails();
				Iterator<PersonalGradeDetails> it = gradeDetails.iterator();
				while (it.hasNext()) {
					PersonalGradeDetails gradeDetail = it.next();
					Double indexTypeTotal = getIndexTypeTotal(grade, gradeDetail);
					if (indexTypeTotal != null && StringUtil.isNotEmpty(gradeDetail.getPercentage())) {
						totalScore += (new BigDecimal(indexTypeTotal)
								.multiply(new BigDecimal(gradeDetail.getPercentage())))
										.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					gradeDetail.setScore(indexTypeTotal);
					baseDao.saveOrUpdate(gradeDetail);
				}
				grade.setCompositeScores(totalScore);
				grade.setStatus(2);
				baseDao.saveOrUpdate(grade);
			}
		}
	}

	/**
	 * 获取个人评分对应指标的个人评分总和
	 * 
	 * @param grade
	 * @param gradeDetail
	 * @return
	 */
	private Double getIndexTypeTotal(PersonalGrade grade, PersonalGradeDetails gradeDetail) {
		StringBuffer sql = new StringBuffer();
		Double result = 0d;
		sql.append(" select d.FK_ROLE_ID ,d.FK_INDEX_TYPE ,d.PERCENTAGE, ");
		sql.append(
				" CASE count(d.ID) > 3 WHEN TRUE then (SUM(d.score)-MAX(d.score)-MIN(d.score))/(count(d.ID)-2) ELSE SUM(d.score)/count(d.ID) END as roleScore ");
		sql.append(" from T_PERSONAL_GRADE_RESULT_DETAILS d ");
		sql.append(" INNER JOIN T_PERSONAL_GRADE_RESULT r on r.ID = d.PERSONAL_GRADE_RESULT_ID ");
		sql.append(" INNER JOIN T_PERSONAL_GRADE g on g.ID = r.PERSONAL_GRADE_ID ");
		sql.append(" INNER JOIN t_user u on u.USER_ID = r.GRADE_USER_ID ");
		sql.append(" where 1=1  ");
		sql.append(" AND u.ISENABLE = 1 and u.STATUS  = 0");
		sql.append(" AND d.FK_INDEX_TYPE = " + gradeDetail.getIndexType().getPkDictionaryId());
		sql.append(" and g.ID = " + grade.getId());
		sql.append(" GROUP BY d.FK_ROLE_ID ,d.FK_INDEX_TYPE,d.PERCENTAGE ");
		List<Map> maps = baseDao.querySQLForMap(sql.toString());
		for (Map map : maps) {
			String percentage = (String) map.get("PERCENTAGE");
			Double roleScore = (Double) map.get("roleScore");
			if (StringUtil.isNotEmpty(percentage) && roleScore != null) {
				result += (new BigDecimal(percentage).multiply(new BigDecimal(roleScore)))
						.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		}
		System.err.println(result);
		// 如果是不参与个人评分的就是组织 TODO
		if (gradeDetail.isGrade() == null || gradeDetail.isGrade() == 0) {
			User user = grade.getUser();
			Organization organization = null;
			for (OrgUser ou : user.getOrgUsers()) {
				if (ou.getOrganization() != null) {
					organization = ou.getOrganization();
				}
			}
			if (organization != null) {
				result = getBmScoreByOrg(organization, grade.getGradeYear());
			} else {
				result = 0d;
			}
		}
		return result;
	}

	/**
	 * 通过部门获取部门得分
	 * 
	 * @param organization
	 * @param year
	 * @return
	 */
	private Double getBmScoreByOrg(Organization organization, String year) {
		Double result = 0d;
		StringBuffer hql = new StringBuffer();
		hql.append(" From FinalScore fs where fs.org.orgId = " + organization.getOrgId());
		hql.append(" and fs.electYear = '" + year + "'");
		List<FinalScore> finalScores = baseDao.queryEntitys(hql.toString());
		if (finalScores != null && finalScores.size() > 0) {
			FinalScore finalScore = finalScores.get(0);
			result = StringUtil.isEmpty(finalScore.getScore()) ? 0d : Double.valueOf(finalScore.getScore());
		}
		return result;
	}

	@Override
	public String generatePersonalGrade(String gradeYear, User currentUser) {
		String result = "{success:true,msg:'生成个人评分成功！'}";
		try {
			StringBuffer hql = new StringBuffer();
			hql.append(" From PersonalGrade where gradeYear = '" + gradeYear + "'");
			hql.append(" and user.userId=" + currentUser.getUserId());
			hql.append(" and isDelete = 0 ");
			List<PersonalGrade> grades = baseDao.queryEntitys(hql.toString());
			if (grades != null && grades.size() > 0) {
				result = "{success:false,msg:'生成个人评分失败，已存在数据！'}";
			} else {
				// 查找当前人个人评分角色
				List<Role> roles = getRoleListByCurrentUser(currentUser);
				if (roles != null) {
					// 获取个人评分人所属类型
					Dictionary classification = getClassification(roles);
					if (classification != null) {
						PersonalGrade grade = new PersonalGrade();
						grade.setTitle(currentUser.getRealname() + gradeYear + "年个人评分表");
						grade.setUser(currentUser);
						grade.setIsDelete(0);
						grade.setStatus(0);
						grade.setGradeYear(gradeYear);
						grade.setClassification(classification);
						baseDao.save(grade);
						// 生成职责表
						if (currentUser.getResponsibilities() != null) {
							StringBuffer hqlDuty = new StringBuffer();
							hqlDuty.append(" From Duty where responsibilities.pkRespId = "
									+ currentUser.getResponsibilities().getPkRespId());
							List<Duty> duties = baseDao.queryEntitys(hqlDuty.toString());
							for (Duty duty : duties) {
								PersonalDuty personalDuty = new PersonalDuty();
								personalDuty.setWorkDuty(duty.getDutyContent());
								personalDuty.setPersonalGrade(grade);
								baseDao.save(personalDuty);
							}
						}
					} else {
						result = "{success:false,msg:'生成个人评分失败，未配置个人评分相关角色！'}";
					}
				} else {
					result = "{success:false,msg:'该用户未配置个人评分角色，请联系管理员！'}";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "{success:false,msg:'生成个人评分失败！'}";
		}
		return result;
	}

	/**
	 * 
	 * @param roles
	 * @return
	 */
	private Dictionary getClassification(List<Role> roles) {
		Dictionary classification = null;
		boolean isYbyg = false;// 一般人员
		boolean isBmzr = false;// 部门主任
		boolean isLd = false;// 领导
		// 此处先通过名称判断
		for (Role role : roles) {
			if (role.getRoleName().equals("一般员工")) {
				isYbyg = true;
			}
			if (role.getRoleName().equals("部门主任") || role.getRoleName().equals("部门副主任")) {
				isBmzr = true;
			}
			if (role.getRoleName().equals("总工/副总工") || role.getRoleName().equals("副总工")
					|| role.getRoleName().equals("总工") || role.getRoleName().equals("质检中心总工")) {
				isLd = true;
			}
		}
		if (isLd) {
			classification = dictService.getDictByTypeAndValue(Constant.GRADE_QZFL, Constant.QZFL_GSLD);
		} else if (isBmzr) {
			classification = dictService.getDictByTypeAndValue(Constant.GRADE_QZFL, Constant.QZFL_BMLD);
		} else if (isYbyg) {
			classification = dictService.getDictByTypeAndValue(Constant.GRADE_QZFL, Constant.QZFL_YBYG);
		}
		return classification;
	}

	/**
	 * 获取当前登录人个人评分角色列表
	 * 
	 * @param currentUser
	 * @return
	 */
	private List<Role> getRoleListByCurrentUser(User currentUser) {
		StringBuffer hql = new StringBuffer();
		String roleIds = getRoleIdsByCurrentUser(currentUser);
		hql.append(" From Role r where isDelete = 0 and r.roleType.dictCode ='" + Constant.ROLE_GRPF
				+ "' and r.roleId in (" + roleIds + ")");
		List<Role> result = baseDao.queryEntitys(hql.toString());
		return result;
	}

	/**
	 * 获取用户所有角色id集合
	 * 
	 * @param currentUser
	 * @return
	 */
	private String getRoleIdsByCurrentUser(User currentUser) {
		String ids = "";
		StringBuffer sql = new StringBuffer();
		sql.append(
				" select DISTINCT t.ROLE_ID  from T_ROLE_MEMBER_SCOPE t where t.USER_ID = " + currentUser.getUserId());
		List<Map> roleIds = baseDao.querySQLForMap(sql.toString());
		for (Map map : roleIds) {
			ids += ",'" + map.get("ROLE_ID") + "'";
		}
		if (StringUtil.isNotEmpty(ids)) {
			ids = ids.substring(1, ids.length());
		}
		return ids;
	}

	@Override
	public HSSFWorkbook exportPersonalGradeAll(Map<String, String> paramMap, File file) {
		HSSFWorkbook wb = null;
		try {
			String personalGradeId = paramMap.get("personalGradeId");
			PersonalGrade grade = (PersonalGrade) baseDao.queryEntityById(PersonalGrade.class,
					Integer.parseInt(personalGradeId));
			// 如果为空不进行导出
			if (grade == null) {
				return null;
			}
			// 获取职责 职责需要插入到中间行
			Set<PersonalDuty> personalDutys = grade.getPersonalDutys();
			// 获取评价
			Map<String, String> evaluationMaps = getEvaluationMaps(grade);
			// 分别为部门主任，分管领导，其他所领导，所领导评价
			String evaluation = evaluationMaps.get("evaluation");
			String evaluation1 = evaluationMaps.get("evaluation1");
			String evaluation2 = evaluationMaps.get("evaluation2");
			String evaluation3 = evaluationMaps.get("evaluation3");

			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			// 读取excel模板
			wb = new HSSFWorkbook(fs);
			//封面
			HSSFSheet fSheet = wb.getSheetAt(0);
			HSSFRow frow1 = fSheet.getRow(9);
			HSSFRow frow2 = fSheet.getRow(17);
			HSSFRow frow3 = fSheet.getRow(19);
			HSSFRow frow4 = fSheet.getRow(21);
			
			//年度
			HSSFCell fcell1 = frow1.getCell(3);
			//部门
			HSSFCell fcell2 = frow2.getCell(4);
			//姓名
			HSSFCell fcell3 = frow3.getCell(4);
			//填表日期
			HSSFCell fcell4 = frow4.getCell(4);
			fcell1.setCellValue("（ "+grade.getGradeYear()+" 年度）");
			if (grade.getUser() != null) {
				//部门
				if (grade.getUser().getOrgUsers() != null) {
					Set<OrgUser> orgUsers = grade.getUser().getOrgUsers();
					for (OrgUser orgUser : orgUsers) {
						if (orgUser.getOrganization() != null) {
							fcell2.setCellValue(orgUser.getOrganization().getOrgName());
							break;
						}
					}
				}
				//姓名
				fcell3.setCellValue(grade.getUser().getRealname());
			}
			//填表日期
			fcell4.setCellValue(DateUtil.getNowDate("yyyy-MM-dd"));
			
			//个人评分汇总表
			HSSFSheet aSheet = wb.getSheetAt(1);
			// 插入职员个人信息
			HSSFRow row1 = aSheet.getRow(1);
			HSSFRow row2 = aSheet.getRow(2);
			HSSFRow row3 = aSheet.getRow(3);
			HSSFCell cell11 = row1.getCell(1);
			HSSFCell cell13 = row1.getCell(3);
			HSSFCell cell15 = row1.getCell(5);

			HSSFCell cell21 = row2.getCell(1);
			HSSFCell cell23 = row2.getCell(3);
			HSSFCell cell25 = row2.getCell(5);

			HSSFCell cell31 = row3.getCell(1);
			HSSFCell cell33 = row3.getCell(5);

			if (grade.getUser() != null) {
				cell11.setCellValue(grade.getUser().getRealname());
				cell13.setCellValue(grade.getUser().getGender());
				cell15.setCellValue(grade.getUser().getBirthDay());
				cell21.setCellValue(grade.getUser().getPoliticsStatus());
				cell23.setCellValue(grade.getUser().getEducationBackground());
				cell25.setCellValue(grade.getUser().getJobStartDate());
				if (grade.getUser().getResponsibilities() != null) {
					cell31.setCellValue(grade.getUser().getResponsibilities().getName());
				}
				// 现任岗位时间
				cell33.setCellValue(grade.getUser().getRespChangeDate());
			}

			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			
			HSSFCellStyle styleBold = getNewCenterStyle(wb);
			// 获取单元格格式
			int newRow = 6; // 从第几行开始插入
			int rows = personalDutys.size();// 设定插入几行
			if (personalDutys != null && personalDutys.size() > 0) {
				aSheet.shiftRows(newRow, aSheet.getLastRowNum(), rows, true, true);
				int rowSize = 0;
				for (PersonalDuty duty : personalDutys) {
					HSSFRow sourceRow = aSheet.getRow(newRow + rowSize);
						if (sourceRow == null) {
							sourceRow = aSheet.createRow(newRow + rowSize);
						}
						//System.err.println(getExcelCellAutoHeight(duty.getWorkDuty(), 20));
						//sourceRow.setHeight((short) 400);
						sourceRow.setHeightInPoints(10+getExcelCellAutoHeight(duty.getWorkDuty(), 19));
						// 合并 单元格 操作* 第一个参数 0 表示 起始 行* 第二个参数 a表示 起始 列* 第三个参数 0
						// 表示结束行* 第四个参数 b表示结束列
						Region region =  new Region(newRow + rowSize, (short) 0, newRow + rowSize, (short) 2);
						setRegionStyle(aSheet,region,styleBold);//设置合并单元格的风格（加边框）
						aSheet.addMergedRegion(region); //
						HSSFCell cew2 = sourceRow.createCell((short) 0);
						cew2.setCellValue(duty.getWorkDuty());
						cew2.setCellStyle(styleBold);
						
						Region region1 =  new Region(newRow + rowSize, (short) 3, newRow + rowSize, (short) 5);
						setRegionStyle(aSheet,region1,styleBold);
						aSheet.addMergedRegion(region1); //
						HSSFCell cew3 = sourceRow.createCell((short) 3);
						cew3.setCellValue(duty.getCompletion());
						cew3.setCellStyle(styleBold);
						rowSize++;
				}
			}

			// 写入其他信息
			HSSFCellStyle cellStyle2 = wb.createCellStyle();
			cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			
			HSSFRow row4 = aSheet.getRow(newRow + personalDutys.size() + 1);
			HSSFCell cell41 = row4.getCell(0);
			cell41.setCellStyle(cellStyle2);
			cell41.setCellValue(grade.getProblem());

			HSSFRow row5 = aSheet.getRow(newRow + personalDutys.size() + 3);
			HSSFCell cell51 = row5.getCell(0);
			cell51.setCellStyle(cellStyle2);
			cell51.setCellValue(grade.getWorkPlan());

			HSSFRow row6 = aSheet.getRow(newRow + personalDutys.size() + 4);
			HSSFCell cell61 = row6.getCell(1);
			cell61.setCellStyle(cellStyle2);
			cell61.setCellValue(evaluation);

			HSSFRow row7 = aSheet.getRow(newRow + personalDutys.size() + 7);
			HSSFCell cell71 = row7.getCell(1);
			cell71.setCellStyle(cellStyle2);
			cell71.setCellValue(evaluation1);

			HSSFRow row8 = aSheet.getRow(newRow + personalDutys.size() + 10);
			HSSFCell cell81 = row8.getCell(1);
			cell81.setCellStyle(cellStyle2);
			cell81.setCellValue(evaluation2);

			HSSFRow row9 = aSheet.getRow(newRow + personalDutys.size() + 13);
			HSSFCell cell91 = row9.getCell(1);
			cell91.setCellStyle(cellStyle2);
			cell91.setCellValue(evaluation3);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}

	private static HSSFCellStyle getNewCenterStyle(HSSFWorkbook workBook) {
		HSSFCellStyle style = workBook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setWrapText(true);
		return style;
	}
	
	/**
	 * 计算高度
	 * str 是单元格需要放入的 字符串 fontCountInline 是该单元格每行多少个汉字 全角为1 英文或符号为0.5
	 * @param str
	 * @param fontCountInline
	 * @return
	 */
	public static float getExcelCellAutoHeight(String str, float fontCountInline) {
        float defaultRowHeight = 12.00f;//每一行的高度指定
        float defaultCount = 0.00f;
        for (int i = 0; i < str.length(); i++) {
            float ff = getregex(str.substring(i, i + 1));
            defaultCount = defaultCount + ff;
        }
        return ((int) (defaultCount / fontCountInline) + 1) * defaultRowHeight;//计算
    }

    public static float getregex(String charStr) {
        
        if(charStr==" ")
        {
            return 0.5f;
        }
        // 判断是否为字母或字符
        if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
            return 0.5f;
        }
        // 判断是否为全角

        if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
            return 1.00f;
        }
        //全角符号 及中文
        if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
            return 1.00f;
        }
        return 0.5f;

    }


	@SuppressWarnings("deprecation")
	private void setRegionStyle(HSSFSheet sheet, Region region, HSSFCellStyle cs) {
		for (int i = region.getRowFrom(); i <= region.getRowTo(); i++) {
			HSSFRow row = sheet.getRow(i);
			if (region.getColumnFrom() != region.getColumnTo()) {
				for (int j = region.getColumnFrom(); j <= region.getColumnTo(); j++) {
					HSSFCell cell = row.getCell((short) j);
					if (cell == null) {
						 cell = row.createCell((short) j);	
					}
					cell.setCellStyle(cs);
				}
			}
		}
	}

	/**
	 * 获取评价集合
	 * 
	 * @param grade
	 * @return
	 */
	private Map<String, String> getEvaluationMaps(PersonalGrade grade) {
		Map<String, String> result = new HashMap<String, String>();
		String evaluation = "";
		String evaluation1 = "";
		String evaluation2 = "";
		String evaluation3 = "";
		Set<PersonalGradeResult> gradeResults = grade.getResult();
		for (PersonalGradeResult personalGradeResult : gradeResults) {
			if (StringUtil.isNotEmpty(personalGradeResult.getEvaluation())) {
				evaluation += personalGradeResult.getEvaluation() + "("
						+ personalGradeResult.getGradeUser().getRealname() + ");";
			}
			if (StringUtil.isNotEmpty(personalGradeResult.getEvaluation1())) {
				evaluation1 += personalGradeResult.getEvaluation1() + "("
						+ personalGradeResult.getGradeUser().getRealname() + ");";
			}
			if (StringUtil.isNotEmpty(personalGradeResult.getEvaluation2())) {
				evaluation2 += personalGradeResult.getEvaluation2() + "("
						+ personalGradeResult.getGradeUser().getRealname() + ");";
			}
			if (StringUtil.isNotEmpty(personalGradeResult.getEvaluation3())) {
				evaluation3 += personalGradeResult.getEvaluation3() + "("
						+ personalGradeResult.getGradeUser().getRealname() + ");";
			}
		}
		result.put("evaluation", evaluation);
		result.put("evaluation1", evaluation1);
		result.put("evaluation2", evaluation2);
		result.put("evaluation3", evaluation3);
		return result;
	}

	@Override
	public ListVo<PersonalGradeResultDetailsVo> getPersonalResultDetailsList(Map<String, String> paramMap) {
		ListVo<PersonalGradeResultDetailsVo> result = new ListVo<PersonalGradeResultDetailsVo>();
		List<PersonalGradeResultDetailsVo> list = new ArrayList<PersonalGradeResultDetailsVo>();
		String personalGradeResultId = paramMap.get("personalGradeResultId");
		StringBuffer hql = new StringBuffer();
		hql.append(" From PersonalGradeResultDetails d where 1=1  ");
		hql.append(" and d.personalGradeResult.personalGrade.isDelete = 0 ");
		if (StringUtil.isNotEmpty(personalGradeResultId)) {
			hql.append(" and d.personalGradeResult.id = " + Integer.parseInt(personalGradeResultId));
		} else {
			hql.append(" and 1= 0");
		}
		List<PersonalGradeResultDetails> detailsList = (List<PersonalGradeResultDetails>) baseDao
				.queryEntitys(hql.toString());
		for (PersonalGradeResultDetails detail : detailsList) {
			PersonalGradeResultDetailsVo vo = new PersonalGradeResultDetailsVo();
			buildDutyDetailsToVo(detail, vo);
			list.add(vo);
		}
		result.setList(list);
		result.setTotalSize(list.size());
		return result;
	}

	/**
	 * 转化个人评分结果明细与vo
	 * 
	 * @param detail
	 * @param vo
	 */
	private void buildDutyDetailsToVo(PersonalGradeResultDetails detail, PersonalGradeResultDetailsVo vo) {
		vo.setId(detail.getId());
		vo.setScore(detail.getScore() == null ? "" : String.valueOf(detail.getScore()));
		if (detail.getIndexType() != null) {
			vo.setIndexTypeId(detail.getIndexType().getPkDictionaryId());
			vo.setIndexTypeName(detail.getIndexType().getDictionaryName());
		}
		if (detail.getRole() != null) {
			vo.setRoleId(detail.getRole().getRoleId());
			vo.setRoleName(detail.getRole().getRoleName());
		}
	}

	@Override
	public PersonalGradeResultDetails getPersonalGradeResultDetailsById(int id) {
		PersonalGradeResultDetails detail = (PersonalGradeResultDetails) baseDao
				.queryEntityById(PersonalGradeResultDetails.class, id);
		return detail;
	}

	@Override
	public void updatePersonalGradeResultDetails(PersonalGradeResultDetails detail) {
		baseDao.update(detail);
	}

	@Override
	public ListVo<PersonalGradeResultVo> getPersonalGradeResultDetailsList(Map<String, String> paramMap) {
		ListVo<PersonalGradeResultVo> result = new ListVo<PersonalGradeResultVo>();
		List<PersonalGradeResultVo> list = new ArrayList<PersonalGradeResultVo>();
		int totalSize = 0;
		int start = NumberUtils.toInt(paramMap.get("start"));
		int limit = NumberUtils.toInt(paramMap.get("limit"));
		// 用户ID 用户自评只能看自己的数据
		String userId = paramMap.get("userId");
		String state = paramMap.get("state");
		// 个人评分 对应的
		String personalGradeId = paramMap.get("personalGradeId");
		String inputGradeUser = paramMap.get("inputGradeUser");
		String inputUserName = paramMap.get("inputUserName");
		String canpDeptQuery = paramMap.get("canpDeptQuery");
		// 标题
		String inputTitle = paramMap.get("inputTitle");
		StringBuffer hql = new StringBuffer();
		StringBuffer counthql = new StringBuffer();
		hql.append(" From PersonalGradeResultDetails pgr where 1=1");
		counthql.append(" select count(*) From PersonalGradeResultDetails pgr where 1=1 ");
		
		hql.append(" and pgr.personalGradeResult.gradeUser.status = 0 and pgr.personalGradeResult.gradeUser.enable = 1 ");
		counthql.append(" and pgr.personalGradeResult.gradeUser.status = 0 and pgr.personalGradeResult.gradeUser.enable = 1 ");
		
		if (StringUtil.isNotEmpty(userId)) {
			hql.append(" and pgr.personalGradeResult.gradeUser.userId = " + Integer.parseInt(userId));
			counthql.append(" and pgr.personalGradeResult.gradeUser.userId = " + Integer.parseInt(userId));
		}

		if (StringUtil.isNotEmpty(state)) {
			hql.append(" and pgr.personalGradeResult.state = " + Integer.parseInt(state));
			counthql.append(" and pgr.personalGradeResult.state = " + Integer.parseInt(state));
		}

		if (StringUtil.isNotEmpty(inputGradeUser)) {
			hql.append(" and pgr.personalGradeResult.personalGrade.user.realname like '%" + inputGradeUser + "%'");
			counthql.append(" and pgr.personalGradeResult.personalGrade.user.realname like '%" + inputGradeUser + "%'");
		}

		// 标题
		if (StringUtil.isNotEmpty(inputTitle)) {
			hql.append(" and pgr.personalGradeResult.personalGrade.title like '%" + inputTitle + "%'");
			counthql.append(" and pgr.personalGradeResult.personalGrade.title like '%" + inputTitle + "%'");
		}

		if (StringUtil.isNotEmpty(inputUserName)) {
			hql.append(" and pgr.personalGradeResult.gradeUser.realname like '%" + inputUserName + "%'");
			counthql.append(" and pgr.personalGradeResult.gradeUser.realname like '%" + inputUserName + "%'");
		}

		if (StringUtil.isNotEmpty(personalGradeId)) {
			hql.append(" and pgr.personalGradeResult.personalGrade.id = '" + personalGradeId + "'");
			counthql.append(" and pgr.personalGradeResult.personalGrade.id = '" + personalGradeId + "'");
		}

		if (StringUtil.isNotEmpty(canpDeptQuery) && !"0".equals(canpDeptQuery)) {
			String userIds = getAllUserIdsByOrgId(canpDeptQuery);
			// 找到该部门下所有人员，如果人员为空，则没有数据
			if (StringUtil.isNotEmpty(userIds)) {
				hql.append(" and pgr.personalGradeResult.personalGrade.user.userId in (" + userIds + ")");
				counthql.append(" and pgr.personalGradeResult.personalGrade.user.userId in (" + userIds + ")");
			} else {
				hql.append(" and 1=0 ");
				counthql.append(" and 1=0 ");
			}
		}
		
		hql.append(" and pgr.personalGradeResult.personalGrade.isDelete = 0 ");
		counthql.append(" and pgr.personalGradeResult.personalGrade.isDelete = 0 ");
		// 评分状态排序 满足点击评分人员列表需求
		hql.append(" order by pgr.personalGradeResult.personalGrade.id,pgr.indexType.pkDictionaryId");

		totalSize = baseDao.getTotalCount(counthql.toString(), new HashMap<String, Object>());
		List<PersonalGradeResultDetails> personalGradeResults = (List<PersonalGradeResultDetails>) baseDao
				.queryEntitysByPage(start, limit, hql.toString(), new HashMap<String, Object>());
		for (PersonalGradeResultDetails gradeResultDetail : personalGradeResults) {
			PersonalGradeResultVo vo = new PersonalGradeResultVo();
			buildResultDetailsEntityToVo(gradeResultDetail, vo);
			list.add(vo);
		}
		result.setList(list);
		result.setTotalSize(totalSize);
		return result;
	}

	private void buildResultDetailsEntityToVo(PersonalGradeResultDetails gradeResultDetail, PersonalGradeResultVo vo) {
		if (gradeResultDetail.getScore() != null) {
			vo.setScore(gradeResultDetail.getScore());
		}
		vo.setPercentage(gradeResultDetail.getPercentage());
		vo.setDetailsId(gradeResultDetail.getId());
		if (gradeResultDetail.getIndexType() != null) {
			vo.setIndexTypeName(gradeResultDetail.getIndexType().getDictionaryName());
		}
		if (gradeResultDetail.getRole() != null) {
			vo.setRoleName(gradeResultDetail.getRole().getRoleName());
		}
		buildResultEntityToVo(gradeResultDetail.getPersonalGradeResult(), vo);
		vo.setId(gradeResultDetail.getId());
	}

	@Override
	public List<ScoreVo> getScoreList(String id) {
		List<ScoreVo> scores = new ArrayList<ScoreVo>();
		int maxScore = 120 ;
		//id 此id为PersonalGradeResultDetails对象的id
		if (StringUtil.isNotEmpty(id)) {
			PersonalGradeResultDetails detail = (PersonalGradeResultDetails)baseDao.queryEntityById(PersonalGradeResultDetails.class, Integer.parseInt(id));
			if (detail != null 
					&& detail.getPersonalGradeResult() != null
					&& detail.getPersonalGradeResult().getPersonalGrade() != null) {
				//评分人
				PersonalGradeResult result = detail.getPersonalGradeResult() ;
				//当前人，即评分人
				User currentUser = result.getGradeUser();
				//被评分人
				PersonalGrade grade = detail.getPersonalGradeResult().getPersonalGrade();
				//如果为个人评分分类
				if (grade.getClassification() != null && grade.getClassification().getDictCode().equals(Constant.QZFL_YBYG)) {
					//找到评分人组织
					Organization gradeOrg = null ;
					User gradeUser = grade.getUser() ;
					for (OrgUser ou : gradeUser.getOrgUsers()) {
						if (ou != null && ou.getOrganization() != null) {
							gradeOrg = ou.getOrganization() ;
							break;
						}
					}
					//如果组织不为空
					if (gradeOrg != null
							&& gradeOrg.getExcellentCount() != null 
							&& gradeOrg.getExcellentScore() != null) {
						//获取该组织下所有的人员
						String gradeUserIds = getUserIdsByCurrentOrg(gradeOrg);
						StringBuffer hql = new StringBuffer();
						hql.append(" From PersonalGradeResultDetails d where d.personalGradeResult.gradeUser.userId = "+currentUser.getUserId());
						//过滤掉自己
						hql.append(" and d.id <> "+detail.getId());
						//过滤当前分类
						hql.append(" and d.indexType.pkDictionaryId = "+detail.getIndexType().getPkDictionaryId());
						//过滤个人评分类型
						hql.append(" and d.personalGradeResult.personalGrade.classification.dictCode = '"+Constant.QZFL_YBYG+"'");
						//过滤评分人部门
						hql.append(" and d.personalGradeResult.personalGrade.user.userId in ("+gradeUserIds+")");
						//过滤分数大于部门协定优秀分数的员工
						hql.append(" and d.score >= "+gradeOrg.getExcellentScore());
						//查出满足条件的评分集合
						List<PersonalGradeResultDetails> details = (List<PersonalGradeResultDetails>)baseDao.queryEntitys(hql.toString());
						if (details != null 
								&& details.size() > 0
								&& details.size() >= gradeOrg.getExcellentCount()) {
							maxScore = gradeOrg.getExcellentScore();
						}
					}
				}
			}
		}
		//设置分数上线
		for (int i = maxScore; i >= 0; i--) {
			ScoreVo scoreVo = new ScoreVo();
			scoreVo.setScore(String.valueOf(i));
			scores.add(scoreVo);
		}
		return scores;
	}

	@Override
	public void backCommit(String id) {
		if (StringUtil.isNotEmpty(id)) {
			PersonalGradeResult gradeResult = (PersonalGradeResult)baseDao.queryEntityById(PersonalGradeResult.class, Integer.parseInt(id));
			if (gradeResult != null) {
				PersonalGrade grade = gradeResult.getPersonalGrade();
				if (grade != null) {
					grade.setStatus(1);
					gradeResult.setState(0);
					baseDao.update(grade);
					baseDao.update(gradeResult);
				}
			}
		}
	}

	@Override
	public void refreshScore(String id) {
		if (StringUtil.isNotEmpty(id)) {
			PersonalGrade grade = (PersonalGrade)baseDao.queryEntityById(PersonalGrade.class, Integer.parseInt(id));
			if (grade != null) {
				if (grade.getResult() != null) {
					for (PersonalGradeResult result : grade.getResult()) {
						if (result != null) {
							generateCompositeScoresNew(result);
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void deletePersonalGrade(String ids) {
        StringBuffer delHql =
                new StringBuffer(" update PersonalGrade set isDelete = 1 where id in ("
                    + ids + ")");
            baseDao.executeHql(delHql.toString());
	}
	
	
}
