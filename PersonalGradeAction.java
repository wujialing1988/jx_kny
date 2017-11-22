package com.xx.grade.personal.action;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.xx.grade.personal.entity.PersonalDuty;
import com.xx.grade.personal.entity.PersonalGrade;
import com.xx.grade.personal.entity.PersonalGradeResult;
import com.xx.grade.personal.entity.PersonalGradeResultDetails;
import com.xx.grade.personal.service.IPersonalGradeService;
import com.xx.grade.personal.vo.PersonalDutyVo;
import com.xx.grade.personal.vo.PersonalGradeResultDetailsVo;
import com.xx.grade.personal.vo.PersonalGradeResultVo;
import com.xx.grade.personal.vo.PersonalGradeVo;
import com.xx.grade.personal.vo.ScoreVo;
import com.xx.system.common.action.BaseAction;
import com.xx.system.common.util.DateUtil;
import com.xx.system.common.util.FileUtil;
import com.xx.system.common.util.JsonUtil;
import com.xx.system.common.util.RequestUtil;
import com.xx.system.common.util.StringUtil;
import com.xx.system.common.vo.ListVo;
import com.xx.system.common.vo.ResponseVo;
import com.xx.system.dict.action.DictAction;
import com.xx.system.user.entity.User;

/**
 * 个人评分action
 * 
 * @author wujialing
 */
public class PersonalGradeAction extends BaseAction {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("personalGradeService")
	private IPersonalGradeService personalGradeService;

	/**
	 * @Fields id : ID
	 */
	private int id;

	/**
	 * 评分实体
	 */
	private PersonalGrade grade;
	
	private PersonalGradeResult result ;
	
	/**
     * @Fields uploadAttach :
     */
    private File uploadAttach;
    
    /**
     * @Fields downloadType :
     */
    private String downloadType;
    
    private String filename;
    
    public File getUploadAttach() {
        return uploadAttach;
    }
    
    public void setUploadAttach(File uploadAttach) {
        this.uploadAttach = uploadAttach;
    }
    
    public String getDownloadType() {
        return downloadType;
    }
    
    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }

	/**
	 * 获取用户自评页面列表
	 * 
	 * @return
	 */
	public String getPersonalGradeForUserSelfList() {
		try {
			Map<String, String> paramMap = RequestUtil
					.getParameterMap(getRequest());
			User user = getCurrentUser();
			if (user != null) {
				paramMap.put("userId", user.getUserId().toString());
			}
			ListVo<PersonalGradeVo> personalGradeList = this.personalGradeService
					.getPersonalGradeList(paramMap);
			JsonUtil.outJson(personalGradeList);
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "获取用户自评列表失败", e,
					false);
		}
		return null;
	}
	
	/**
	 * 查询全部统计数据
	 * 
	 * @return
	 */
	public String getAllPersonalGradeList() {
		try {
			Map<String, String> paramMap = RequestUtil
					.getParameterMap(getRequest());
			ListVo<PersonalGradeVo> personalGradeList = this.personalGradeService
					.getPersonalGradeList(paramMap);
			JsonUtil.outJson(personalGradeList);
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "查询全部统计数据", e,
					false);
		}
		return null;
	}
	
	/**
	 * 获取评分结果列表
	 * 
	 * @return
	 */
	public String getPersonalGradeResultList() {
		try {
			Map<String, String> paramMap = RequestUtil
					.getParameterMap(getRequest());
			User user = getCurrentUser();
			if (user != null) {
				paramMap.put("userId", user.getUserId().toString());
			}
			ListVo<PersonalGradeResultVo> resultList = this.personalGradeService
					.getPersonalGradeResultList(paramMap);
			JsonUtil.outJson(resultList);
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "获取评分结果列表失败", e,
					false);
		}
		return null;
	}
	
	public String getPersonalGradeResultDetailsCountsList() {
		try {
			Map<String, String> paramMap = RequestUtil
					.getParameterMap(getRequest());
			ListVo<PersonalGradeResultVo> resultList = this.personalGradeService
					.getPersonalGradeResultList(paramMap);
			JsonUtil.outJson(resultList);
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "获取评分结果剩余人数列表失败", e,
					false);
		}
		return null;
	}
	
	/**
	 * 反提交
	 * @return
	 */
	  public String backCommit() {
        try {
            String id = this.getRequest().getParameter("id");
            personalGradeService.backCommit(id);
            JsonUtil.outJson("{success:true,msg:'撤回个人评分成功'}");
        } catch (Exception e) {
            JsonUtil.outJson("{success:false,msg:'撤回个人评分失败'}");
            this.excepAndLogHandle(PersonalGradeAction.class, "撤回个人评分", e, false);
        }
        return null;
    }
	  
	  
	  /**
	   * 刷新得分
	   * @return
	   */
	  public String refreshScore() {
	    try {
	        String id = this.getRequest().getParameter("id");
	        personalGradeService.refreshScore(id);
	        JsonUtil.outJson("{success:true,msg:'刷新个人得分成功'}");
	    } catch (Exception e) {
	    	JsonUtil.outJson("{success:false,msg:'刷新个人得分失败'}");
	    	this.excepAndLogHandle(PersonalGradeAction.class, "刷新个人得分", e, false);
	    }
	    return null;
	}

	/**
	 * 查询员工评分明细
	 * 
	 * @return
	 */
	public String getPersonalGradeResultDetailsList() {
		try {
			Map<String, String> paramMap = RequestUtil
					.getParameterMap(getRequest());
			ListVo<PersonalGradeResultVo> resultList = this.personalGradeService
					.getPersonalGradeResultDetailsList(paramMap);
			JsonUtil.outJson(resultList);
		} catch (Exception e) {
			e.printStackTrace();
			this.excepAndLogHandle(PersonalGradeAction.class, "获取评分结果列表失败", e,
					false);
		}
		return null;
	}
	
	

	/**
	 * 获取用户自评职责明细
	 * 
	 * @return
	 */
	public String getPersonalDutyList() {
		try {
			Map<String, String> paramMap = RequestUtil
					.getParameterMap(getRequest());
			ListVo<PersonalDutyVo> personalDutyList = this.personalGradeService
					.getPersonalDutyList(paramMap);
			JsonUtil.outJson(personalDutyList);
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "获取用户自评职责明细列表失败",
					e, false);
		}
		return null;
	}
	
	/**
	 * 获取个人评分结果明细
	 * 
	 * @return
	 */
	public String getPersonalResultDetailsList() {
		try {
			Map<String, String> paramMap = RequestUtil
					.getParameterMap(getRequest());
			ListVo<PersonalGradeResultDetailsVo> personalGradeResultDetails = this.personalGradeService
					.getPersonalResultDetailsList(paramMap);
			JsonUtil.outJson(personalGradeResultDetails);
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "获取个人评分结果明细列表失败",
					e, false);
		}
		return null;
	}
	

	/**
	 * 获取个人评分实体
	 * 
	 * @return
	 */
	public String getPersonalGradeById() {
		try {
			ResponseVo rv = new ResponseVo();
			PersonalGradeVo vo = this.personalGradeService
					.getPersonalGradeById(id);
			rv.setData(vo);
			JsonUtil.outJson(rv);
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "根据ID获取个人评分", e,
					false);
		}
		return null;
	}
	
	/**
	 * 根据id获取个人评分结果
	 * @return
	 */
	public String getPersonalGradeResultById() {
		try {
			ResponseVo rv = new ResponseVo();
			PersonalGradeResultVo vo = this.personalGradeService
					.getPersonalGradeResultById(id);
			rv.setData(vo);
			JsonUtil.outJson(rv);
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "根据id获取个人评分结果", e,
					false);
		}
		return null;
	}
	
	

	/**
	 * 编辑个人评分
	 * 
	 * @return
	 */
	public String editPersonalGrade() {
		try {
			grade = parseGradeFormRequest();
			this.personalGradeService.editPersonalGrade(grade);
			JsonUtil.outJson("{success:true,msg:'修改个人评分成功！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "修改个人评分信息", null,
					true);
		} catch (Exception e) {
			JsonUtil.outJson("{success:false,msg:'修改个人评分失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "修改个人评分信息", e,
					false);
			return null;
		}
		return null;
	}
	
	/**
	 * 编辑个人评分结果
	 * 
	 * @return
	 */
	public String editPersonalGradeResult() {
		try {
			result = parseGradeResultFormRequest();
			this.personalGradeService.editPersonalGradeResult(result);
			JsonUtil.outJson("{success:true,msg:'编辑评分成功！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "编辑评分", null,
					true);
		} catch (Exception e) {
			JsonUtil.outJson("{success:false,msg:'编辑评分失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "编辑评分", e,
					false);
			return null;
		}
		return null;
	}
	

	/**
	 * 更改职责明细
	 * 
	 * @return
	 */
	public String updatePersonalDuty() {
		try {
			Map<String, String> dutyMap = RequestUtil.getParameterMap(super
					.getRequest());
			String id = dutyMap.get("id");
			String completion = dutyMap.get("completion");
			PersonalDuty duty = this.personalGradeService
					.getPersonalDutyBy(Integer.parseInt(id));
			duty.setCompletion(completion);
			this.personalGradeService.updatePersonalDuty(duty);
			JsonUtil.outJson("{success:true,msg:'修改个人评分职责明细成功！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "修改个人评分职责明细信息",
					null, true);
		} catch (Exception e) {
			JsonUtil.outJson("{success:false,msg:'修改个人评分职责明细信息失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "修改个人评分职责明细信息",
					e, false);
			return null;
		}
		return null;
	}
	
	/**
	 * 修改个人评分结果明细
	 * 
	 * @return
	 */
	public String updatePersonalResultDetails() {
		try {
			Map<String, String> map = RequestUtil.getParameterMap(super
					.getRequest());
			String id = map.get("id");
			String score = map.get("score");
			PersonalGradeResultDetails detail = this.personalGradeService
					.getPersonalGradeResultDetailsById(Integer.parseInt(id));
			if (detail != null && StringUtil.isNotEmpty(score)) {
				detail.setScore(Double.valueOf(score));
				this.personalGradeService.updatePersonalGradeResultDetails(detail);
			}
			JsonUtil.outJson("{success:true,msg:'修改个人评分结果明细成功！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "修改个人评分结果明细信息",
					null, true);
		} catch (Exception e) {
			JsonUtil.outJson("{success:false,msg:'修改个人评分结果明细失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "修改个人评分结果明细信息",
					e, false);
			return null;
		}
		return null;
	}

	/**
	 * 导出个人职责
	 * 
	 * @return
	 */
	public String exportPersonalDuty() {
		try {
			Map<String, String> dutyMap = RequestUtil.getParameterMap(super
					.getRequest());
			HttpServletRequest request = this.getRequest();
			ServletContext servletContext = request.getSession().getServletContext();
			File file=new File(servletContext.getRealPath("/template/dutyTemplate.xls")); 
			HSSFWorkbook workBook = this.personalGradeService
					.exportPersonalDuty(dutyMap,file);
			if (workBook != null) {
				this.getRequest().getSession()
						.setAttribute("personalDutyWorkBook", workBook);
				JsonUtil.outJson("{success:true,msg:'导出个人职责明细成功！'}");
				this.excepAndLogHandle(PersonalGradeAction.class, "导出个人职责明细信息",
						null, true);
			} else {
				JsonUtil.outJson("{success:false,msg:'导出个人职责明细失败！'}");
				this.excepAndLogHandle(PersonalGradeAction.class, "导出个人职责明细信息",
						null, false);
			}
		} catch (Exception e) {
			JsonUtil.outJson("{success:false,msg:'导出个人职责明细信息失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "导出个人职责明细信息", e,
					false);
			return null;
		}
		return null;
	}

	/**
	 * 导出excel
	 * 
	 * @return
	 */
	public String exportPersonalDutyFile() {
		HttpServletRequest request = this.getRequest();
		try {
			HSSFWorkbook workBook = (HSSFWorkbook) request.getSession()
					.getAttribute("personalDutyWorkBook");
			this.getResponse().reset();
			this.getResponse().setContentType(
					"application/msexcel;charset=UTF-8");
			try {
				this.getResponse().addHeader(
						"Content-Disposition",
						"attachment;filename=\""
								+ new String(("中储粮成都粮食储藏科学研究所员工年度考核登记表" + ".xls")
										.getBytes("GBK"), "ISO8859_1") + "\"");
				OutputStream out = this.getResponse().getOutputStream();
				//FileOutputStream fout = new FileOutputStream("E:/students.xls"); 
				workBook.write(out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getSession().removeAttribute("personalDutyWorkBook");
		return null;
	}
	
	
	/**
	 * 导出个人评分汇总
	 * 
	 * @return
	 */
	public String exportPersonalGradeAll() {
		try {
			Map<String, String> paramMap = RequestUtil.getParameterMap(super
					.getRequest());
			HttpServletRequest request = this.getRequest();
			ServletContext servletContext = request.getSession().getServletContext();
			File file=new File(servletContext.getRealPath("/template/resultTemplate.xls"));  
			HSSFWorkbook workBook = this.personalGradeService
					.exportPersonalGradeAll(paramMap,file);
			if (workBook != null) {
				this.getRequest().getSession()
						.setAttribute("personalGradeAllWorkBook", workBook);
				JsonUtil.outJson("{success:true,msg:'导出个人评分汇总成功！'}");
				this.excepAndLogHandle(PersonalGradeAction.class, "导出个人评分汇总信息",
						null, true);
			} else {
				JsonUtil.outJson("{success:false,msg:'导出个人评分汇总失败！'}");
				this.excepAndLogHandle(PersonalGradeAction.class, "导出个人评分汇总信息",
						null, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JsonUtil.outJson("{success:false,msg:'导出个人职责明细信息失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "导出个人职责明细信息", e,
					false);
			return null;
		}
		return null;
	}

	/**
	 * 导出个人评分汇总excel
	 * 
	 * @return
	 */
	public String exportPersonalGradeAllFile() {
		HttpServletRequest request = this.getRequest();
		try {
			HSSFWorkbook workBook = (HSSFWorkbook) request.getSession()
					.getAttribute("personalGradeAllWorkBook");
			this.getResponse().reset();
			this.getResponse().setContentType(
					"application/msexcel;charset=UTF-8");
			try {
				this.getResponse().addHeader(
						"Content-Disposition",
						"attachment;filename=\""
								+ new String(("个人评分汇总表" + ".xls")
										.getBytes("GBK"), "ISO8859_1") + "\"");
				OutputStream out = this.getResponse().getOutputStream();
				workBook.write(out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getSession().removeAttribute("personalGradeAllWorkBook");
		return null;
	}

	public String uploadPersonalDutyExcel() {
		try {
			Map<String, String> paramsMap = RequestUtil
					.getParameterMap(getRequest());
			HttpServletRequest request = this.getRequest();
			ServletContext servletContext = request.getSession()
					.getServletContext();
			String uploadPath = servletContext.getRealPath("/")
					+ "personalDuty" + File.separator;
			String dateStr = DateUtil.dateToString(new Date(),
					DateUtil.DATE_FORMAT);
			String fileUrl = FileUtil.upload(uploadAttach, filename, uploadPath
					+ dateStr);
			String message = personalGradeService.uploadPersonalDutyExcel(fileUrl,
					paramsMap);
			this.excepAndLogHandle(PersonalGradeAction.class, "通过上传EXCEl文件，个人评分职责导入",
					null, true);
			JsonUtil.outJson("{success:true,msg:'" + message + "'}");
		} catch (Exception e) {
			this.excepAndLogHandle(PersonalGradeAction.class, "通过上传EXCEl文件，个人评分职责导入",
					e, false);
		}
		return null;
	}

	/**
	 * 组装个人评分实体
	 * 
	 * @return
	 */
	private PersonalGrade parseGradeFormRequest() {
		PersonalGrade grade = null;
		try {
			Map<String, String> gradeMap = RequestUtil.getParameterMap(super
					.getRequest());
			if (gradeMap.get("id") != null
					&& StringUtil.isNotBlank(gradeMap.get("id"))) {
				grade = this.personalGradeService
						.getPersonalGradeEntityById(Integer.parseInt(gradeMap
								.get("id")));
			}
			if (null != grade) {
				grade.setWorkPlan(gradeMap.get("workPlan"));
				grade.setProblem(gradeMap.get("problem"));
			} else {
				JsonUtil.outJson("{success:'false',msg:'编辑个人评分失败，未找到该数据！'}");
			}
		} catch (Exception e) {
			JsonUtil.outJson("{success:'false',msg:'编辑个人评分失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "编辑个人评分失败", e,
					false);
		}
		return grade;
	}
	
	/**
	 * 组装个人评分结果实体
	 * 
	 * @return
	 */
	private PersonalGradeResult parseGradeResultFormRequest() {
		PersonalGradeResult result = null;
		try {
			Map<String, String> gradeMap = RequestUtil.getParameterMap(super
					.getRequest());
			if (gradeMap.get("id") != null
					&& StringUtil.isNotBlank(gradeMap.get("id"))) {
				result = this.personalGradeService
						.getPersonalGradeResultEntityById(Integer.parseInt(gradeMap
								.get("id")));
			}
			if (null != result) {
				result.setEvaluation(gradeMap.get("evaluation"));
				result.setEvaluation1(gradeMap.get("evaluation1"));
				result.setEvaluation2(gradeMap.get("evaluation2"));
				result.setEvaluation3(gradeMap.get("evaluation3"));
			} else {
				JsonUtil.outJson("{success:'false',msg:'评分失败，未找到该数据！'}");
			}
		} catch (Exception e) {
			JsonUtil.outJson("{success:'false',msg:'编辑个人评分结果失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "编辑个人评分结果失败", e,
					false);
		}
		return result;
	}
	
	

	/**
	 * 提交个人评分数据
	 * 
	 * @return
	 */
	public String submitPersonalGrade() {
		try {
			String ids = this.getRequest().getParameter("ids");
			String result = personalGradeService.submitPersonalGrade(ids,getCurrentUser());
			JsonUtil.outJson(result);
			this.excepAndLogHandle(PersonalGradeAction.class, "提交个人评分", null,
					true);
		} catch (Exception e) {
			e.printStackTrace();
			JsonUtil.outJson("{success:false,msg:'提交个人评分失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "提交个人评分", e,
					false);
		}
		return null;
	}
	
	/**
	 * 生成个人评分
	 * 
	 * @return
	 */
	public String generatePersonalGrade() {
		try {
			String gradeYear = this.getRequest().getParameter("gradeYear");
			//生成个人评分
			String result = personalGradeService.generatePersonalGrade(gradeYear, getCurrentUser());
			JsonUtil.outJson(result);
			this.excepAndLogHandle(PersonalGradeAction.class, "生成个人评分", null,
					true);
		} catch (Exception e) {
			JsonUtil.outJson("{success:false,msg:'生成个人评分失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "生成个人评分", e,
					false);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 提交个人评分结果
	 * 
	 * @return
	 */
	public String submitPersonalGradeResult() {
		try {
			String ids = this.getRequest().getParameter("ids");
			String result = personalGradeService.submitPersonalGradeResult(ids);
			JsonUtil.outJson(result);
			this.excepAndLogHandle(PersonalGradeAction.class, "提交个人评分结果", null,
					true);
		} catch (Exception e) {
			JsonUtil.outJson("{success:false,msg:'提交个人评分失败！'}");
			this.excepAndLogHandle(PersonalGradeAction.class, "提交个人评分结果", e,
					false);
		}
		return null;
	}
	
	/**
	 * 获取个人评分分数下拉列表
	 */
    public void getScoreList() {
        try {
        	//评分结果明细id
        	String id = this.getRequest().getParameter("id");
            List<ScoreVo> scoreList = personalGradeService.getScoreList(id);
/*            for (int i = 120; i >= 0; i--) {
            	ScoreVo vo = new ScoreVo();
            	vo.setScore(String.valueOf(i));
            	scoreList.add(vo);
			}*/
            JsonUtil.outJsonArray(scoreList);
        }
        catch (Exception e) {
            // 发生异常是，进行日志入库和生成日志文件
            this.excepAndLogHandle(PersonalGradeAction.class, "获取个人评分分数下拉列表", e, false);
        }
    }
    
    /**
     * 删除个人评分
     * 
     * @return
     */
    public String deletePersonalGrade() {
        try {
            String ids = this.getRequest().getParameter("ids");
            personalGradeService.deletePersonalGrade(ids);
            JsonUtil.outJson("{success:true,msg:'删除个人评分成功'}");
            this.excepAndLogHandle(PersonalGradeAction.class, "删除个人评分", null, true);
        }
        catch (Exception e) {
            JsonUtil.outJson("{success:false,msg:'删除个人评分失败'}");
            this.excepAndLogHandle(PersonalGradeAction.class, "删除个人评分", e, false);
        }
        return null;
    }
	

	/**
	 * get && set
	 * 
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PersonalGrade getGrade() {
		return grade;
	}

	public void setGrade(PersonalGrade grade) {
		this.grade = grade;
	}

	public PersonalGradeResult getResult() {
		return result;
	}

	public void setResult(PersonalGradeResult result) {
		this.result = result;
	}
	
	

}
