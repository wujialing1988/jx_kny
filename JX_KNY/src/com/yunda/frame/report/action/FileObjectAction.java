package com.yunda.frame.report.action; 

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.report.entity.FileObject;
import com.yunda.frame.report.manager.FileObjectManager;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FileObject控制器, 报表文件对象
 * <li>创建人：何涛
 * <li>创建日期：2015-01-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class FileObjectAction extends JXBaseAction<FileObject, FileObject, FileObjectManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 报表打印模板的File实体 */
	private File report; //上传的文件

    /** 报表打印模板文件名称 */
    private String reportFileName; //文件名称
    
    /** 报表打印模板文件MIME类型 */
    private String reportContentType;
    
    /** 文件描述 */
    private String fileDesc;
    
	/**
	 * <li>说明：保存上传的报表文件（*.cpt）
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-28
	 * <li>修改人: 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void saveUpload() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
			String printerModuleIDX = req.getParameter("printerModuleIDX");
			String fileUploadPath = req.getParameter("fileUploadPath");
			this.manager.saveUpload(this.report, this.fileDesc, fileUploadPath, printerModuleIDX);
			map.put("success", true);
			map.put("errMsg", null);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
	        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
	        ServletActionContext.getResponse().setContentType("text/html");
	        ServletActionContext.getResponse().getWriter().write(JSONUtil.write(map));
		}
	}
	
	/**
	 * <li>说明：下载报表文件
	 * <li>创建：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public void download() {
		HttpServletResponse rsp = getResponse();
		InputStream is = null;
		try {
			// 获取报表文件实体
			FileObject fo = this.manager.getModelById(id);
			// 获取报表文件的输入流
			is = fo.getFileObject().getBinaryStream();
			// 设置html头文件类型
			rsp.setContentType("application/x-msdownload");
			rsp.setHeader("Content-Disposition", "attachment;filename="
					+ StringUtil.toISO(fo.getFileName()));
			// 获取http输出流
			OutputStream out = rsp.getOutputStream();
			// 输出文件
			byte[] buf = new byte[1024 * 5];
			int len = is.read(buf);
			while (len > 0) {
				out.write(buf, 0, len);
				len = is.read(buf);
			}
		} catch (Exception e) {
			try {
				rsp.getOutputStream().write("文件不存在或已经被删除！".getBytes());
			} catch (IOException ex) {
				ExceptionUtil.process(ex,logger);
			}
			ExceptionUtil.process(e,logger);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					ExceptionUtil.process(e,logger);
				}
				is = null;
			}
		}
	}
	
	/**
	 * <li>说明：设置指定报表文件对象的当前标识为启用
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-28
	 * <li>修改人: 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void updateCurrentFlag() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
			String idx = req.getParameter("idx");
			this.manager.updateCurrentFlag(this.manager.getModelById(idx));
			map.put("success", true);
			map.put("errMsg", null);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}

	/**
	 * @return 获取文件描述
	 */
	public String getFileDesc() {
		return fileDesc;
	}

	/**
	 * @param fileDesc 设置文件描述
	 */
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	/**
	 * @return 获取报表打印模板的File实体
	 */
	public File getReport() {
		return report;
	}

	/**
	 * @param report 设置报表打印模板的File实体
	 */
	public void setReport(File report) {
		this.report = report;
	}

	/**
	 * @return 获取报表打印模板文件MIME类型
	 */
	public String getReportContentType() {
		return reportContentType;
	}

	/**
	 * @param reportContentType 设置报表打印模板文件MIME类型
	 */
	public void setReportContentType(String reportContentType) {
		this.reportContentType = reportContentType;
	}

	/**
	 * @return 获取报表打印模板文件名称
	 */
	public String getReportFileName() {
		return reportFileName;
	}

	/**
	 * @param reportFileName 设置报表打印模板文件名称
	 */
	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}
	
}