package com.yunda.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * <li>类型名称：com.yunda.util.Struts2FileUploadUtil
 * <li>说明：用于支持Struts2的文件上传工具
 * <li>使用说明：
 * <li>页面：&lt;input type="file" name="files" /&gt;</code>
 * <li>代码：private List<File> files;private List<String> filesFileName;
 * <li>创建人： 赵宏波
 * <li>创建日期：2011-5-16
 * <li>修改人： 
 * <li>修改日期：
 */
public class Struts2FileUploadUtil {
	
	/**
	 * <li>方法名：upload
	 * <li> @param sourceFileName
	 * <li> @param sourceFile
	 * <li>说明：上传文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String upload(String sourceFileName, File sourceFile) throws Exception {
		if (sourceFileName == null || YDStringUtil.isBlank(sourceFileName)) throw new Exception("Source file path is null.");
		if (sourceFile == null) throw new Exception("Source file is null.");
		String filePathName = FileUploadUtil.getWebInfoPath() + FileUploadUtil.DEFAULT_SAVE_PATH + "/" + FileUploadUtil.getFileSaveName() + FileUploadUtil.getFileExtName(sourceFileName);
		File distFile = new File(filePathName);
		if (doUpload(distFile, sourceFile)) {
			return filePathName;
		}
		return "";
	}
	
	/**
	 * 
	 * <li>方法名：Gwzdupload
	 * <li>@param sourceFileName
	 * <li>@param sourceFile
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：String
	 * <li>说明：工位终端升级文件上传
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-9-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String Gwzdupload(String sourceFileName, File sourceFile) throws Exception {
		if (sourceFileName == null || YDStringUtil.isBlank(sourceFileName)) throw new Exception("Source file path is null.");
		if (sourceFile == null) throw new Exception("Source file is null.");
		String filePathName = FileUploadUtil.getWebInfoPath() + "/gwzdupdate/gwzd.rar";
		File distFile = new File(filePathName);
		if (doUpload(distFile, sourceFile)) {
			return filePathName;
		}
		return "";
	}
	
	
	/**
	 * 
	 * <li>方法名：stationMapUpload
	 * <li>@param sourceFileName
	 * <li>@param sourceFile
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：String
	 * <li>说明：上传战场图xml文件
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String stationMapUpload(String sourceFileName, File sourceFile,String orgid) throws Exception {
		if (sourceFileName == null || YDStringUtil.isBlank(sourceFileName)) throw new Exception("Source file path is null.");
		if (sourceFile == null) throw new Exception("Source file is null.");
		String filePathName = FileUploadUtil.getWebInfoPath() + "/stationmap/"+ orgid +".xml";
		File distFile = new File(filePathName);
		if (doUpload(distFile, sourceFile)) {
			return filePathName;
		}
		return "";
	}
	
	
	/**
	 * 
	 * <li>方法名：decpfiles
	 * <li>@param zipfilepath zip文件所在的位置
	 * <li>@param unzipDirectory 需要解压到的位置
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：String
	 * <li>说明：解压文件(文件类型zip)
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-9-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	/*public static String decfilesZip(String zipFilePath, String unzipDirectory) throws Exception {

		//创建文件对象
	  	File file = new File(zipFilePath);
	  	// 创建zip文件对象
	  	ZipFile zipFile = new ZipFile(file);
	  	// 创建本zip文件解压目录
	  	File unzipFile = new File(unzipDirectory + "/" + file.getName().substring(0, file.getName().lastIndexOf(".")));
	  	if (unzipFile.exists())
	   	unzipFile.delete();
	 	unzipFile.mkdir();
	  	// 得到zip文件条目枚举对象
	  	Enumeration zipEnum = zipFile.getEntries();
	  	// 定义输入输出流对象
	  	InputStream input = null;
	  	OutputStream output = null;
	  	// 定义对象
	  	ZipEntry entry = null;
	  	// 循环读取条目
	 	while (zipEnum.hasMoreElements()) {
	  	 	// 得到当前条目
	   		entry = (ZipEntry) zipEnum.nextElement();
	   		String entryName = new String(entry.getName());
	   		// 用/分隔条目名称
	   		String names[] = entryName.split("\\/");
	   		int length = names.length;
	   		String path = unzipFile.getAbsolutePath();
	   		for (int v = 0; v < length; v++) {
	   			if (v < length - 1) { // 最后一个目录之前的目录
	     			path += "/" + names[v] + "/";
	     			File dir = new File(path);
	     		  	if (dir.exists() == false)
	     		   	dir.mkdir();
	    		} else { // 最后一个
	     			if (entryName.endsWith("/")){ // 为目录,则创建文件夹
	     				File dir = new File(unzipFile.getAbsolutePath() + "/" + entryName);
		     			if (dir.exists() == false)
			     		   	dir.mkdir();
	     			}else { // 为文件,则输出到文件
	      				input = zipFile.getInputStream(entry);
	      				output = new FileOutputStream(new File(unzipFile .getAbsolutePath()+ "/" + entryName));
	     	 			byte[] buffer = new byte[1024 * 8];
	      				int readLen = 0;
	     				while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
	       					output.write(buffer, 0, readLen);
	      					// 关闭流
	      					input.close();
	      					output.flush();
	      					output.close();
	     				}
	    		    }
	   	        }
	    	}
		return "";
	}*/
	
	
	
	
	/**
	 * 
	 * <li>方法名：decFilesAar
	 * <li>@param sourceRar 文件目录
	 * <li>@param destDir 输出目录
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：解压缩文件（rar）
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-9-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void decFilesAar(String sourceRar,String destDir) throws Exception{
		Archive a = null;  
	    FileOutputStream fos = null;  
        try{  
            a = new Archive(new File(sourceRar));  
            FileHeader fh = a.nextFileHeader(); 
            String compressFileName="";
            String destFileName = "";  
            //String destDirName = ""; 
    		//int end;
            while(fh!=null){  
                if(!fh.isDirectory()){  
                    //1 根据不同的操作系统拿到相应的 destDirName 和 destFileName  
                	compressFileName = (fh.getFileNameString().trim()).replaceAll("\\\\", "/");  
                    //非windows系统  
                    if(File.separator.equals("/")){  
                        destFileName = destDir + sourceRar.substring(sourceRar.lastIndexOf("/"),sourceRar.lastIndexOf("."))+ "/"  + compressFileName;  
                        //destDirName = destFileName.substring(0, destFileName.lastIndexOf("/")) + sourceRar.substring(sourceRar.lastIndexOf("/"),sourceRar.lastIndexOf("."));  
                    //windows系统   
                    }else{  
                        destFileName = destDir + sourceRar.substring(sourceRar.lastIndexOf("/"),sourceRar.lastIndexOf(".")) + "/" + compressFileName;  
                       /* end = destFileName.lastIndexOf("/");
                        if(end!=-1){
                        	destDirName = destFileName.substring(0,end);
                        }*/
                    }  
                    //2创建文件夹  
                    File dir = new File(destDir+"/gwzd");  
                    if(!dir.exists()||!dir.isDirectory()){  
                        dir.mkdirs();  
                    }  
                    //3解压缩文件  
                    if(destFileName.lastIndexOf(".version")!= -1){
                    	fos = new FileOutputStream(new File(destFileName));  
                    	a.extractFile(fh, fos);
                    	fos.close();  
                        fos = null;  
                    }
                }  
                fh = a.nextFileHeader();  
            }  
            a.close();  
            a = null;  
        }catch(Exception e){  
            throw e;  
        }finally{  
            if(fos!=null){  
                try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}  
            }  
            if(a!=null){  
                try{a.close();a=null;}catch(Exception e){e.printStackTrace();}  
            }  
        }  
	}
	  
	
	
	 
	/**
	 * <li>方法名：readVersion
	 * <li>@return
	 * <li>@throws IOException
	 * <li>返回类型：String
	 * <li>说明：读取文件
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-9-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String readVersion(String filepath) throws Exception{
		String Version;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(filepath)));
			Version = br.readLine();
			br.close();
			return Version;
		} catch (Exception e) {
			System.out.println("软件版本文件不存在！"+'\n'); 
			return "";
		}		
	}

	

	/**
	 * 
	 * <li>方法名：deleteFile
	 * <li>@param file
	 * <li>返回类型：void
	 * <li>说明：删除文件
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-9-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static void deleteFile(String  filepath){ 
		File file = new File(filepath);
		if(file.exists()){                    //判断文件是否存在
			if(file.isFile()){                    //判断是否是文件
				file.delete();                       //delete()方法 
			}else if(file.isDirectory()){              //否则如果它是一个目录
				File files[] = file.listFiles();               //声明目录下所有的文件 files[];
				for(int i=0;i<files.length;i++){            //遍历目录下所有的文件
					files[i].delete();        //把每个文件 用这个方法进行迭代
				} 
			} 
			file.delete(); 
	   }else{ 
		   System.out.println("所需删除的文件不存在！"+'\n'); 
	   } 
	} 


	/**
	 * <li>方法名：upload
	 * <li> @param saveNamePrefix 文件前缀，除了扩展名
	 * <li> @param sourceFileName
	 * <li> @param sourceFile
	 * <li>说明：上传文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-17
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String upload(String saveNamePrefix, String sourceFileName, File sourceFile) throws Exception {
		if (sourceFileName == null || YDStringUtil.isBlank(sourceFileName)) throw new Exception("Source file path is null.");
		if (sourceFile == null) throw new Exception("Source file is null.");
		String filePathName = FileUploadUtil.getWebInfoPath() + FileUploadUtil.DEFAULT_SAVE_PATH + "/" + saveNamePrefix + FileUploadUtil.getFileExtName(sourceFileName);
		File distFile = new File(filePathName);
		if (doUpload(distFile, sourceFile)) {
			return filePathName;
		}
		return "";
	}
	
	/**
	 * <li>方法名：upload
	 * <li> @param saveNamePrefix 文件前缀，除了扩展名
	 * <li> @param savePathName 保存路径
	 * <li> @param sourceFileName
	 * <li> @param sourceFile
	 * <li>说明：上传文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-8-4
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String upload(String saveNamePrefix, String savePathName, String sourceFileName, File sourceFile) throws Exception {
		if (sourceFileName == null || YDStringUtil.isBlank(sourceFileName)) throw new Exception("Source file path is null.");
		if (sourceFile == null) throw new Exception("Source file is null.");
		String savePath = FileUploadUtil.getWebInfoPath() + FileUploadUtil.DEFAULT_SAVE_PATH + "/";
		if (!StringUtils.isBlank(savePathName)) {
			if (savePathName.startsWith("/")) {
				savePathName = savePathName.substring(savePathName.indexOf("/") + 1);
			}
			savePath += savePathName + "/";
			File newSaveFolder = new File(savePath);
			if (!newSaveFolder.exists()) {
				FileUtils.forceMkdir(newSaveFolder);
			}
			newSaveFolder = null;
		}
		String filePathName = savePath + saveNamePrefix + FileUploadUtil.getFileExtName(sourceFileName);
		File distFile = new File(filePathName);
		if (doUpload(distFile, sourceFile)) {
			return filePathName;
		}
		return "";
	}
	
	
	/**
	 * <li>方法名：upload
	 * <li> @param sourceFileNameList
	 * <li> @param sourceFileList
	 * <li>返回类型：List
	 * <li>说明：批量上传文件
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static List<String> upload(List<String> sourceFileNameList, List<File> sourceFileList) throws Exception {
		if (sourceFileNameList == null || sourceFileNameList.size() < 1) throw new Exception("Source file name list is null.");
		if (sourceFileList == null || sourceFileList.size() < 1) throw new Exception("Source file list is null.");
		List<String> filePathNameList = new ArrayList<String>();
		String filePathName = "";
		for (int i = 0; i < sourceFileNameList.size() && i < sourceFileList.size(); i++) {
			filePathName = upload(sourceFileNameList.get(i), sourceFileList.get(i));
			if (!YDStringUtil.isBlank(filePathName)) {
				filePathNameList.add(filePathName);
			}
		}
		return filePathNameList;
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
}
