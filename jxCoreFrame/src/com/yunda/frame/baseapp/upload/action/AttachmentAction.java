package com.yunda.frame.baseapp.upload.action; 

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.baseapp.upload.manager.IAttachmentManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.FileUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Attachment控制器, 附件上传下载管理。
 * 附件存放目录取系统属性文件中配置的目录地址作为根路径。上传的附件存放在以关联业务表命名的目录下。
 * 例如： 2012年10月27日某人员上传的附件存放在 系统配置上传目录/关联业务表命名/2012/10/文件名
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class AttachmentAction extends JXBaseAction<Attachment, Attachment, AttachmentManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/** 上传文件对象 */
	private File file;
	/** 上传文件名称 */
	private String fileFileName;
	/** 上传文件内容类型 */
	private String fileContentType;
	
	/**
	 * <li>说明：删除附件以及相关记录
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-05
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	public void del() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			List<String> errMsg = this.manager.del(ids);
			if (errMsg == null || errMsg.size() < 1) {
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	/**
	 * <li>说明：删除附件以及相关记录，覆盖原逻辑删除方法（原方法不适用），删除动作应包括同时删除记录和文件
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-05
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */
	@Override
	public void logicDelete() throws Exception{
		del();
	}
	/**
	 * <li>说明：下载制定附件
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-05
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	public void download(){
		HttpServletResponse rsp = getResponse();		
		FileInputStream in = null;
		try {
			Attachment att = this.manager.getModelById(id);
            //修改 2015-02-02 汪东良 将file 局部变量修改成obj开头，保证与属性变量命名不同。
			File objFile = new File(AttachmentManager.uploadFilepath(att));
			in = new FileInputStream(objFile);
			rsp.setContentType("application/x-msdownload");
			rsp.setHeader("Content-Disposition", "attachment;filename=" + StringUtil.toISO(att.getAttachmentRealName()));
			OutputStream out = rsp.getOutputStream();
			byte[] b = new byte[1024 * 5];
			int len = in.read(b);
			while(len > 0){
				out.write(b, 0, len);
				len = in.read(b);
			}
		} catch (Exception e){
			try {
				rsp.getOutputStream().write("文件不存在或已经被删除！".getBytes());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
		}
	}
	/**
	 * <li>说明：接受SWFUpload控件上传的附件，基于AJAX方式。操作结果（success：操作结果成功失败），返回客户端的是JSON字符串
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws 
	 */
	public void swfUpload() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		InputStream in = null;
		BufferedInputStream inBuff = null;
		FileOutputStream out = null;
		BufferedOutputStream outBuff = null; 
		try{ 
			//获取扩展名
			String extName = "";
			int idx = fileFileName.lastIndexOf(".");
			if(idx != -1)	extName = fileFileName.substring(idx + 1);
			
			Attachment att = new Attachment();
			att.setAttachmentKeyIDX(this.entity.getAttachmentKeyIDX());
			att.setAttachmentKeyName(this.entity.getAttachmentKeyName());
			att.setAttachmentRealName(fileFileName);
			
			Date now = new Date();
//			String saveName = fileFileName.substring(0, idx) + "_" + DateUtil.yyyyMMddHHmmssSSS.format(now) + "." + extName;
			String saveName = att.getAttachmentKeyIDX() + "_" + DateUtil.yyyyMMddHHmmssSSS.format(now) + "." + extName; //通过对象主键+时间戳生成保存文件名称
			att.setAttachmentSaveName(saveName);
			att.setFileType(extName);
			
			//获取当前登录操作员
			AcOperator acOperator = SystemContext.getAcOperator();
			OmEmployee employee = SystemContext.getOmEmployee();
			String personName = acOperator.getOperatorname();
			if(employee != null && employee.getEmpname() != null){
				personName = employee.getEmpname();
			}
			att.setUploadPerson(acOperator.getOperatorid());
			att.setUploadPersonName(personName);
			att.setUploadTime(now);
			//设置业务无关的附加信息
			att = EntityUtil.setNoDelete(att);
			att = EntityUtil.setSysinfo(att, now);			
			
			String saveDir = AttachmentManager.uploadDir(att.getAttachmentKeyName());		//获取文件保存目录
			FileUtil.createDir(saveDir);													//如果文件保存目录不存在，自动创建
            String attSaveName = att.getAttachmentSaveName();
//            if(!System.getProperty("os.name").startsWith("Windows")){  //判断操作系统信息
//                attSaveName = StringUtil.encodeISO8859_To_GB2312(attSaveName);
//            }
			File desFile = new File(saveDir + File.separator + attSaveName);//服务器存放文件对象
			//使用缓冲流读写文件
			in = new FileInputStream(file);
			inBuff = new BufferedInputStream(in);
			out = new FileOutputStream(desFile);
			outBuff = new BufferedOutputStream(out);
			
			byte[] buffer = new byte[1024 * 5];				//设置缓冲区大小
			long fileSize = 0;								//文件大小（单位：字节）
			int len;
			while((len = inBuff.read(buffer)) != -1){
				fileSize += len;
				outBuff.write(buffer, 0, len);
			}
			outBuff.flush();
			
			att.setAttachmentSize(fileSize);
			this.manager.add(att);
			map.put("success", true);
		}catch(Exception e){ 
			ExceptionUtil.process(e, logger, map);
		}finally{
			//释放I/O资源
			if(outBuff != null)	{
				try {
					outBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				outBuff = null;
			}
			if(inBuff != null){
				try {
					inBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				inBuff = null;
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				out = null;
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
			//向客户端返回结果信息
			JSONUtil.write(this.getResponse(), map);			
		}
	}
	
	/**
	 * <li>说明：显示图片
	 * <li>创建人：何东
	 * <li>创建日期：2012-11-05
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	public void whowImg(){
		HttpServletResponse rsp = getResponse();		
		FileInputStream in = null;
		try {
			Attachment att = this.manager.getModelById(id);
            //修改 2015-02-02 汪东良 将file 局部变量修改成obj开头，保证与属性变量命名不同。
			File objFile = new File(AttachmentManager.uploadFilepath(att));
			in = new FileInputStream(objFile);
			rsp.setContentType("image/jpeg");
			OutputStream out = rsp.getOutputStream();
			byte[] b = new byte[1024 * 5];
			int len = in.read(b);
			while(len > 0){
				out.write(b, 0, len);
				len = in.read(b);
			}
		} catch (Exception e){
			try {
				rsp.getOutputStream().write("文件不存在或已经被删除！".getBytes());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
		}
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
    
    /**
     * <li>说明：删除附件
     * <li>创建人：林欢
     * <li>创建日期：2016-09-02
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
    public void deleteAttachment() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] filePaths = JSONUtil.read(getRequest().getParameter("filePath"), String[].class);
            this.manager.deleteAttachment(filePaths);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
	 * <li>说明：查询图片
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017年5月2日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String images() {
		String key = getRequest().getParameter("key");
		List<Attachment> list = null;
		// 如果指定了businessName，则使用指定的业务管理器去查询附件信息
		String businessName = getRequest().getParameter("businessName");
		if (!StringUtil.isNullOrBlank(businessName)) {
			Object obj = Application.getSpringApplicationContext().getBean(businessName);
			if (obj instanceof IAttachmentManager) {
				list = ((IAttachmentManager)obj).findImages(key);
			}
		}
		if (null == list) {
			list = this.manager.findImages(key);
		}
		getRequest().setAttribute("list", list);
		return SUCCESS;
	}
	
	/**
	 * <li>说明：请求附件图片
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void image() {
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		String attachmentKeyName = req.getParameter("attachmentKeyName");
		String year = req.getParameter("year");
		String month = req.getParameter("month");
		String attachmentSaveName = req.getParameter("attachmentSaveName");
	    StringBuilder sb = new StringBuilder(JXConfig.getInstance().getUploadPath()); 
	    sb.append(File.separatorChar).append(attachmentKeyName).
	    	append(File.separatorChar).append(Integer.parseInt(year)).
		    append(File.separatorChar).append(Integer.parseInt(month)).
		    append(File.separatorChar).append(attachmentSaveName);
	    File file = new File(sb.toString());
	    if (!file.exists()) {
	    	return;
	    }
	    
	    resp.setContentType("image/png");
	    resp.setHeader("Content-Disposition", "attachment;filename=");
	    
	    FileInputStream in = null;
	    OutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = resp.getOutputStream();
			byte[] buffer = new byte[1024];
			while (0 < in.read(buffer)) {
				out.write(buffer);
			}
		} catch (IOException e) {
			ExceptionUtil.process(e, logger, new HashMap<String, Object>());
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			} catch (Exception e2) {
				ExceptionUtil.process(e2, logger, new HashMap<String, Object>());
			}
		}
	}
	
}