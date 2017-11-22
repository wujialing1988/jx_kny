package com.xx.grade.personal.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xx.grade.personal.entity.PersonalDuty;
import com.xx.grade.personal.entity.PersonalGrade;
import com.xx.grade.personal.entity.PersonalGradeResult;
import com.xx.grade.personal.entity.PersonalGradeResultDetails;
import com.xx.grade.personal.vo.PersonalDutyVo;
import com.xx.grade.personal.vo.PersonalGradeResultDetailsVo;
import com.xx.grade.personal.vo.PersonalGradeResultVo;
import com.xx.grade.personal.vo.PersonalGradeVo;
import com.xx.grade.personal.vo.ScoreVo;
import com.xx.system.common.exception.BusinessException;
import com.xx.system.common.vo.ListVo;
import com.xx.system.user.entity.User;

/**
 * 个人评分服务
 * 
 * @author wujialing
 */
public interface IPersonalGradeService {
	
	/**
	 * 获取个人评分列表
	 * 
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public ListVo<PersonalGradeVo> getPersonalGradeList(Map<String, String> paramMap)
            throws BusinessException;

	/**
	 * 获取个人评分实体
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public PersonalGradeVo getPersonalGradeById(int id) 
			throws BusinessException;
	
	/**
	 * 获取个人评分实体
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public PersonalGrade getPersonalGradeEntityById(int id) 
			throws BusinessException;

	/**
	 * 修改个人评分
	 * 
	 * @param grade
	 * @throws BusinessException
	 */
	public void editPersonalGrade(PersonalGrade grade)
			throws BusinessException;

	/**
	 * 获取职责明细列表
	 * 
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public ListVo<PersonalDutyVo> getPersonalDutyList(Map<String, String> paramMap);

	/**
	 * 获取职责明细实体
	 * 
	 * @param parseInt
	 * @return
	 * @throws BusinessException
	 */
	public PersonalDuty getPersonalDutyBy(int parseInt)
			throws BusinessException;

	/**
	 * 修改职责明细
	 * 
	 * @param duty
	 * @throws BusinessException
	 */
	public void updatePersonalDuty(PersonalDuty duty)
			throws BusinessException;

	/**
	 * 提交个人评分
	 * 
	 * @param ids
	 * @return
	 */
	public String submitPersonalGrade(String ids,User currentUser);
	
	/**
	 * 生成评分结果
	 * 
	 * @param ids
	 * @param curUser
	 */
	public void generatePersonalGradeResult(String ids,User curUser);

	/**
	 * 导出工作职责
	 * 
	 * @param dutyMap
	 * @param file 
	 * @return
	 */
	public HSSFWorkbook exportPersonalDuty(Map<String, String> dutyMap, File file);

	/**
	 * 导入工作职责
	 * 
	 * @param fileUrl
	 * @param paramsMap
	 * @return
	 */
	public String uploadPersonalDutyExcel(String fileUrl,
			Map<String, String> paramsMap);

	/**
	 * 获取个人评分结果列表
	 * 
	 * @param paramMap
	 * @return
	 */
	public ListVo<PersonalGradeResultVo> getPersonalGradeResultList(
			Map<String, String> paramMap);

	/**
	 * 根据id获取个人评分结果
	 * 
	 * @param id
	 * @return
	 */
	public PersonalGradeResultVo getPersonalGradeResultById(int id);
	
	/**
	 * 根据id获取个人评分结果实体
	 * 
	 * @param parseInt
	 * @return
	 */
	public PersonalGradeResult getPersonalGradeResultEntityById(int parseInt);

	/**
	 * 编辑个人评分结果
	 * 
	 * @param result
	 */
	public void editPersonalGradeResult(PersonalGradeResult result);

	/**
	 * 提交个人评分结果
	 * 
	 * @param ids
	 * @return
	 */
	public String submitPersonalGradeResult(String ids);

	/**
	 * 生成个人评分
	 * 
	 * @param gradeYear
	 * @param currentUser
	 * @return
	 */
	public String generatePersonalGrade(String gradeYear, User currentUser);

	/**
	 * 生成个人评分汇总表
	 * 
	 * @param paramMap
	 * @param file 
	 * @return
	 */
	public HSSFWorkbook exportPersonalGradeAll(Map<String, String> paramMap, File file);

	/**
	 * 获取个人评分结果明细
	 * 
	 * @param paramMap
	 * @return
	 */
	public ListVo<PersonalGradeResultDetailsVo> getPersonalResultDetailsList(Map<String, String> paramMap);

	/**
	 * 通过id获取个人评分结果明细
	 * 
	 * @param id
	 * @return
	 */
	public PersonalGradeResultDetails getPersonalGradeResultDetailsById(int id);

	/**
	 * 修改个人评分结果明细
	 * 
	 * @param detail
	 */
	public void updatePersonalGradeResultDetails(PersonalGradeResultDetails detail);

	/**
	 * 获取员工评分明细
	 * 
	 * @param paramMap
	 * @return
	 */
	public ListVo<PersonalGradeResultVo> getPersonalGradeResultDetailsList(
			Map<String, String> paramMap);

	/**
	 * 获取个人评分分数列表
	 * @param personalGradeId
	 * @return
	 */
	public List<ScoreVo> getScoreList(String id);

	/**
	 * 撤回个人评分
	 * @param id
	 */
	public void backCommit(String id);

	/**
	 * 刷新个人得分
	 * 
	 * @param id
	 */
	public void refreshScore(String id);

	/**
	 * 删除个人评分
	 * 
	 * @param ids
	 */
	public void deletePersonalGrade(String ids);
	
}
