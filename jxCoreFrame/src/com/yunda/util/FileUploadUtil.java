
package com.yunda.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <li>类型名称：com.yunda.util.FileUploadUtil
 * <li>说明：文件上传工具
 * <li>创建人： 赵宏波
 * <li>创建日期：2011-5-16
 * <li>修改人： 
 * <li>修改日期：
 */
public class FileUploadUtil {
	
	/**
	 * 上传下载缓冲区大小
	 */
	public static final int BUFFER = 8 * 1024;
	
	/**
	 * 默认保存到WEB-INF/upload/
	 */
	public static final String DEFAULT_SAVE_PATH = "/upload";
	
	//当WEB-INF不存在upload时创建一个
	static {
		try {
			File file = new File(getWebInfoPath() + DEFAULT_SAVE_PATH + "/");
			if (!file.exists()) {
				FileUtils.forceMkdir(file);
				System.out.println("Create a default save folder named 'upload'.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <li>方法名：upload
	 * <li> @param filePath 文件路径如：/upload
	 * <li> @param fileName
	 * <li> @param sourceFile
	 * <li>返回类型：void
	 * <li>说明：上传文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void upload(String filePath, String fileName, File sourceFile) throws Exception {
		
		if (filePath == null || YDStringUtil.isBlank(filePath)) throw new Exception("File path is null.");
		if (fileName == null || YDStringUtil.isBlank(fileName)) throw new Exception("File name is null.");
		if (sourceFile == null) throw new Exception("Source file is null");
		
		String filePathName = "";
		if (filePath.endsWith("/")) { 
			filePathName = filePath + fileName;
		} else {
			filePathName = filePath + "/" + fileName;
		}
		upload(filePathName, sourceFile);
	}
	
	/**
	 * <li>方法名：upload
	 * <li> @param filePathName
	 * <li> @param sourceFile
	 * <li>说明：上传文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void upload(String filePathName, File sourceFile) throws Exception {
		if (filePathName == null || YDStringUtil.isBlank(filePathName)) throw new Exception("File path and name is null.");
		if (sourceFile == null) throw new Exception("Source file is null.");
		doUpload(new File(filePathName), sourceFile);
	}
	
	/**
	 * <li>方法名：upload
	 * <li> @param distFile
	 * <li> @param sourceFile
	 * <li>返回类型：void
	 * <li>说明：上传文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void upload(File distFile, File sourceFile) throws Exception {
		doUpload(distFile, sourceFile);
	}
	
	/**
	 * <li>方法名：upload
	 * <li> @param distFile
	 * <li> @param sourceFile
	 * <li>返回类型：void
	 * <li>说明：执行上传
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	private static boolean doUpload(File distFile, File sourceFile) {
		boolean uploadFlag = true;
		try {
			FileUtils.copyFile(sourceFile, distFile);
		} catch (Exception e) {
			uploadFlag = false;
			e.printStackTrace();
		}
		return uploadFlag;
	}
	
	/**
	 * <li>方法名：upload
	 * <li>返回类型：String
	 * <li>说明：单独上传一个文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String upload(File sourceFile) throws Exception {
		if (sourceFile == null) throw new Exception("Source file is null.");
		String filePathName = getWebInfoPath() + DEFAULT_SAVE_PATH + "/" + getFileSaveName() + getFileExtName(sourceFile.getName());
		if (doUpload(new File(filePathName), sourceFile)) {
			return filePathName;
		}
		return "";
	}
	
	/**
	 * <li>方法名：upload
	 * <li> @param fileList
	 * <li>返回类型：List
	 * <li>说明：批量上传文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static List<String> upload(List<File> fileList) throws Exception {
		List<String> filePathNameList = new ArrayList<String>();
		String filePathName = "";
		for (File file : fileList) {
			filePathName = upload(file);
			if (!YDStringUtil.isBlank(filePathName)) {
				filePathNameList.add(filePathName);
			}
		}
		return filePathNameList;
	}
	
	/**
	 * <li>方法名：getClassPath
	 * <li>返回类型：String
	 * <li>说明：取得应用下的classes的绝对路径
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String getClassPath() {
		String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		classPath = classPath.substring(classPath.indexOf("/") + 1);
		return classPath;
	}
	
	/**
	 * <li>方法名：getWebInfoPath
	 * <li>返回类型：String
	 * <li>说明：取得应用下的WEB-INFO的绝对路径
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String getWebInfoPath() {
		String webInfoPath = getClassPath();
		webInfoPath = webInfoPath.replaceAll("/classes/", "");
		return webInfoPath;
	}
	
	/**
	 * <li>方法名：getFileExtName
	 * <li>返回类型：String
	 * <li>说明：取得给定文件名的扩展名
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String getFileExtName(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	
	/**
	 * <li>方法名：getFileSaveName
	 * <li> @param generateType 生成类型 0:UUID, 1:日期格式
	 * <li>返回类型：String
	 * <li>说明：取得保存到服务器上的文件的文件名称
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String getFileSaveName(int generateType) {
		String fileSaveName = "";
		if (generateType == 0) {
			fileSaveName =  UUID.randomUUID().toString();
		} else if (generateType == 1) {
			fileSaveName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis()));
		}
		return fileSaveName;
	}
	
	/**
	 * <li>方法名：getFileSaveName
	 * <li> @param fileName
	 * <li> @param request
	 * <li> @param response
	 * <li>返回类型：void
	 * <li>说明：参照getFileSaveName(int generateType)
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String getFileSaveName() {
		return getFileSaveName(0);
	}
	
	/**
	 * <li>方法名：getFileInputStream
	 * <li> @param file
	 * <li>返回类型：InputStream
	 * <li>说明：强制删除指定文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void delete(String fileName) throws Exception {
		if (fileName == null || YDStringUtil.isBlank(fileName)) throw new Exception("File name is null.");
		FileUtils.forceDelete(new File(getWebInfoPath() + DEFAULT_SAVE_PATH + "/" + fileName));
	}
	
	/**
	 * <li>方法名：getFileInputStream
	 * <li> @param file
	 * <li>返回类型：InputStream
	 * <li>说明：取得文件的输入流
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static InputStream getFileInputStream(String file) throws Exception {
		return FileUtils.openInputStream(new File(getWebInfoPath() + DEFAULT_SAVE_PATH + "/" + file));
	}
	
	/**
	 * 
	 * <li>方法名：getFileInputStreamfile
	 * <li>@param file
	 * <li>@param filepath //需要带斜杠
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：InputStream
	 * <li>说明：更具指定路径下载文件
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-9-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static InputStream getFileInputStreamfile(String file,String filepath) throws Exception {
		if(StringUtils.isBlank(filepath)){
			return getFileInputStream(file);
		}
		return FileUtils.openInputStream(new File(getWebInfoPath() + filepath + "/" + file));
	}
	/**
	 * <li>方法名：download
	 * <li>返回类型：String
	 * <li>说明：下载文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-17
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void download(String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		download(fileName,request,response,"");
	}
	
	/**
	 * 
	 * <li>方法名：downloadfile
	 * <li>@param fileName //文件名字
	 * <li>@param request
	 * <li>@param response
	 * <li>@param filepath //文件路径
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-9-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void download(String fileName, HttpServletRequest request, HttpServletResponse response,String filepath) throws Exception {
		if (fileName == null) throw new Exception("File name is null.");
		
		String contentType = request.getSession().getServletContext().getMimeType(fileName);
		if(contentType == null) {
			contentType = "application/octet-stream;charset=UTF-8";
		}
		response.setContentType(contentType + ";charset=UTF-8");
		fileName = URLDecoder.decode(fileName, "UTF-8");
		response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		InputStream in = new BufferedInputStream(FileUploadUtil.getFileInputStreamfile(fileName,filepath));
		OutputStream out = new BufferedOutputStream(response.getOutputStream(), FileUploadUtil.BUFFER);
		byte[] fileBuffer = new byte[FileUploadUtil.BUFFER];
		while (in.read(fileBuffer) > 0) {
			out.write(fileBuffer);
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
	}
	
}
