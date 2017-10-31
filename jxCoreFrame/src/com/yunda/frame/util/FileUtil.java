package com.yunda.frame.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 封装了对文件目录以及文件的操作，包括创建目录,创建文件,删除目录,删除文件,目录移除,文件移除,判断文件是否存在,取文件名与文件扩展名等方法。
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class FileUtil {
    /**
     * <li>说明：将本地一个文件复制另一个目录中
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String src：源文件绝对路径
     * @param String desDir: 目标目录绝对路径
     * @return boolean false复制失败，true复制成功
     * @throws 
     */
    public static boolean copyFile(String src, String desDir){
        src = convertSeparartor(src);
        desDir = convertSeparartor(desDir);
        File srcFile = new File(src);
        //不存在源目录返回false
        if (!srcFile.exists()) {
            return false;
        }
        //如果目标目录不存在，则自动创建
        File dir = new File(desDir);
        if (!dir.exists()) {
        	dir.mkdirs();
        }
		int idx = src.lastIndexOf(File.separator);
		String filename = src.substring(idx + 1);        
        File desFile = new File(desDir + File.separator + filename);
        return copyFile(srcFile, desFile);
    }
    /**
     * <li>说明：将本地一个文件复制另一个目录中
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param File src：源文件对象
     * @param File des: 目标文件对象
     * @return boolean false复制失败，true复制成功
     * @throws 
     */
    public static boolean copyFile(File src, File des){
		InputStream in = null;
		BufferedInputStream inBuff = null;
		FileOutputStream out = null;
		BufferedOutputStream outBuff = null;    	
        try {
			in = new FileInputStream(src);
			inBuff = new BufferedInputStream(in);
			out = new FileOutputStream(des);
			outBuff = new BufferedOutputStream(out);
			
			byte[] buffer = new byte[1024 * 5];
			int len;
			while((len = inBuff.read(buffer)) != -1){
				outBuff.write(buffer, 0, len);
			}
			outBuff.flush();

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
		}
        return false;
    }    
	/**
	 * <li>说明：根据操作系统自动转化文件路径分隔符
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String pathName：文件（目录）路径
	 * @return String 转换后返回字符串
	 * @throws 
	 */
    public static String convertSeparartor(String pathName) {
        return pathName.replace('\\', File.separatorChar)
                .replace('/', File.separatorChar);
    }

    /**
     * <li>说明：删除本地目录（或文件）,将删除该目录下所有文件夹以及文件
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String pathName：目录所在完全路径(绝对路径 如: D:\\temp\\test.txt)
     * @return boolean false删除失败，true删除成功
     * @throws 
     */
    public static boolean deleteDir(String pathName) {
        boolean isDeleted = false;
        pathName = convertSeparartor(pathName);
        File dir = new File(pathName);
        if (!dir.exists()) {
            return false; //目录不存在返回
        }
        if (dir.isFile()) {
            //是文件直接删除删除返回
            return dir.delete();
        }
        File[] files = dir.listFiles();
        //取得目录下文件和目录数之和
        int length = files == null ? 0 : files.length;
        //如果是空目录直接删除返回
        if (length == 0) {
            return dir.delete();
        }
        for (int i = 0; i < length; i++) {
            if (files[i].isDirectory()) {
                //删除目录
                isDeleted = deleteDir(files[i].getPath());
            } else {
                //删除文件
                isDeleted = files[i].delete();
            }
        }
        if (dir.exists()) {
            isDeleted = dir.delete();
        }
        return isDeleted;
    }

    /**
     * <li>说明：移动本地文件
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String srcFile：源文件路径
     * @param String desFile: 目标文件路径
     * @return boolean false移动失败，true移动成功
     * @throws 
     */
    public static boolean moveFile(String srcFile, String desFile) {
        boolean isMoved = false;
        srcFile = convertSeparartor(srcFile);
        desFile = convertSeparartor(desFile);
        try {
            BufferedInputStream bin = new BufferedInputStream(new
                    FileInputStream(srcFile));
            BufferedOutputStream bout = new BufferedOutputStream(new
                    FileOutputStream(desFile));
            while (true) {
                int input = bin.read();
                if (input == -1) {
                    break;
                }
                bout.write(input);
            }
            bin.close();
            bout.close();
            File src = new File(srcFile);
            isMoved = src.delete();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        return isMoved;
    }

    /**
     * <li>说明：将本地一个目录中文件移动本地另一个目录中.包括所有文件夹和文件，如果目标目录不存在程序自动创建。
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String src：源目录绝对路径
     * @param String des: 目标目录绝对路径
     * @return boolean false移动失败，true移动成功
     * @throws 
     */
    public static boolean moveDir(String src, String des) {
        boolean isMoved = false;
        src = convertSeparartor(src);
        des = convertSeparartor(des);
        File srcDir = new File(src);
        //不存在源目录返回false
        if (!srcDir.exists()) {
            return false;
        }
        File[] files = srcDir.listFiles();
        int length = files == null ? 0 : files.length;

        File desDir = new File(des);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        for (int i = 0; i < length; i++) {
            if (files[ i ].isDirectory()) {
                String path = files[ i ].getPath();
                int lastIndex = path.lastIndexOf(File.separatorChar);
                //取得子目录subDir路径
                String subDir = path.substring(lastIndex + 1, path.length());
                isMoved = moveDir(path, des + File.separator + subDir);
                if (isMoved) {
                    //删除源子目录
                    deleteDir(path);
                }
            } else {  //移动文件
                String srcFile = files[ i ].getAbsolutePath();
                String desFile = des + File.separator + files[ i ].getName();
                isMoved = moveFile(srcFile, desFile);
            }
        }
        if (srcDir.exists()) {
            isMoved = srcDir.delete();
        }
        return isMoved;
    }

    /**
     * <li>说明：创建本地文本文件，若目录不存在将自动创建。
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String fullFileName：全路径文件名：文件路径+文件名称 如D:\\MyDocument\\readme.txt
     * @param String content: 文本文件内容(字符串)
     * @return boolean false新建成功，true新建失败
     * @throws 
     */
    public static boolean createFile(String fullFileName, String content) {
        content = content == null ? "" : content;
        fullFileName = convertSeparartor(fullFileName);
        int lastIndex = fullFileName.lastIndexOf(File.separator);
        String dirName = fullFileName.substring(0, lastIndex);
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(fullFileName));
            pw.print(content);
            pw.close();
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    /**
     * <li>说明：创建此抽象路径名指定的目录，包括创建必需但不存在的父目录。注意，如果此操作失败，可能已成功创建了一些必需的父目录。
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String dirName：目录名称
     * @return 当且仅当已创建该目录以及所有必需的父目录时，返回 true；否则返回 false
     * @throws 
     */
    public static boolean createDir(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }
  
    /**
     * <li>说明：删除本地文件
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String fullFileName：文件绝对路径+文件名称 如 D:\\temp\\test.txt
     * @return boolean false删除失败，true删除成功
     * @throws 
     */
    public static boolean deleteFile(String fullFileName) {
        fullFileName = convertSeparartor(fullFileName);
        File file = new File(fullFileName);
        if (file.exists()) {
            return file.delete();
        } 
        return true;
    }
    
    /**
     * <li>说明：获取全路径文件的扩展名
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String fullFileName：文件绝对路径+文件名称 如 D:\\temp\\test.txt
     * @return String 返回文件扩展名
     * @throws 
     */
    public static String getExtName(String fullFileName) {
        fullFileName = StringUtil.nvlTrim(fullFileName);
        int lastIndex = fullFileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        } else {
            return fullFileName.substring(lastIndex + 1);
        }
    }
    
    /**
     * <li>说明：获取不包含扩展名的全路径文件名
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String fullFileName：文件绝对路径+文件名称 如 D:\\temp\\test.txt
     * @return String 返回不包含扩展名的全路径文件名
     * @throws 
     */
    public static String getNameNoExt(String fullFileName) {
        fullFileName = StringUtil.nvlTrim(fullFileName);
        int lastIndex = fullFileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return fullFileName;
        } else {
            return fullFileName.substring(0, lastIndex);
        }
    }
    
    /**
     * <li>说明：判断本地目录或者文件是否存在
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String fullPath：文件绝对路径+文件名称 如 D:\\temp\\test.txt
     * @return boolean false不存在，true存在
     * @throws 
     */
    public static boolean isExist(String fullPath) {
        if (null == fullPath) {
            return false;
        }
        fullPath = convertSeparartor(fullPath);
        File file = new File(fullPath);
        return file.exists();
    }
    
    /**
     * <li>说明：重命名本地文件（或目录）
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String oldName：原文件绝对路径+文件名称 如 D:\\temp\\test.txt
     * @param String newName: 重命名后文件名（不带路径）
     * @return boolean 当且仅当重命名成功时，返回 true；否则返回 false
     * @throws 
     */
    public static boolean rename(String oldFullName, String newName) {
        if (oldFullName == null || "".equals(oldFullName) || newName == null || "".equals(newName)) {
            return false;
        }
        oldFullName = convertSeparartor(oldFullName);
        int lastIndex = oldFullName.lastIndexOf(File.separator);
        String path = oldFullName.substring(0, lastIndex);
        File oldFile = new File(oldFullName);
        File newFile = new File(path + File.separator + newName);
        return oldFile.renameTo(newFile);
    }
    /**
     * <li>说明：将本地一个文件复制另一个目录中
     * <li>创建人：程锐
     * <li>创建日期：2015-1-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String src：源文件绝对路径
     * @param String desDir: 目标目录绝对路径
     * @param String fileName：目标文件名称
     * @return 文件大小
     * @throws 
     */
    public static long copyFile(String src, String desDir, String fileName){
        src = convertSeparartor(src);
        desDir = convertSeparartor(desDir);
        File srcFile = new File(src);
        //不存在源目录返回false
        if (!srcFile.exists()) {
            return 0;
        }
        //如果目标目录不存在，则自动创建
        File dir = new File(desDir);
        if (!dir.exists()) {
        	dir.mkdirs();
        }     
        File desFile = new File(desDir + File.separator + fileName);
        return copyFileByName(srcFile, desFile);
    }
    /**
     * 
     * <li>说明：将本地一个文件复制另一个目录中
     * <li>创建人：程锐
     * <li>创建日期：2015-1-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param src
     * @param des
     * @return 文件大小
     */
    public static long copyFileByName(File src, File des){
		InputStream in = null;
		BufferedInputStream inBuff = null;
		FileOutputStream out = null;
		BufferedOutputStream outBuff = null;    	
        try {
			in = new FileInputStream(src);
			inBuff = new BufferedInputStream(in);
			out = new FileOutputStream(des);
			outBuff = new BufferedOutputStream(out);
			
			byte[] buffer = new byte[1024 * 5];
			int len;
			long fileSize = 0;
			while((len = inBuff.read(buffer)) != -1){
				fileSize += len;
				outBuff.write(buffer, 0, len);
			}
			outBuff.flush();

			return fileSize;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
		}
        return 0;
    }    
}
