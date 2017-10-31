package com.yunda.frame.baseapp.todojob;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明： 反射当前包下所有IUntreatedJob接口的实现类，返回实现类类名列表
 * <li>创建人：谭诚
 * <li>创建日期：2014-01-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class ClassUtils {
	
	/**
	 * <li>说明：根据入参接口，获取其所在的包路径，并调用函数扫描该路径及下级包，获取入参接口的所有实现类
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-01-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param c 接口
	 * @return List<Class> 入参接口的所有实现类集合
	 */	
	@SuppressWarnings("unchecked")
	public static List<Class> getAllImpClassesByInterface(Class c){
		List <Class> classList = new ArrayList<Class>(); //返回结果
		if(!c.isInterface()) return null; //如果入参不是接口，则不做处理
		String packageName = c.getPackage().getName(); //获得当前包名
		try{
			List <Class> allClass = getClassesByPackageName(packageName); //获得当前包以及下级包下的所有类
			for(int i=0;i<allClass.size();i++){
				/** 该类是否是入参接口本身或者是入参接口的实现类，如果是，返回true，否则返回否 */
				if(c.isAssignableFrom(allClass.get(i))){
					/** 该类是否是入参接口本身，如果是，返回true，否则返回否 */
					if(!c.equals(allClass.get(i))){
						classList.add(allClass.get(i)); //将入参接口的实现类填充至classList中
					}
				}
			}
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return classList;
	}
	
	/**
	 * <li>说明：根据入参包名，获取该路径下的所有class类集合
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-01-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param packageName 包名
	 * @return List<Class> 包下所有类的集合
	 */	
	private static List<Class> getClassesByPackageName(String packageName) throws IOException, ClassNotFoundException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace(".", "/");
		Enumeration<URL> resources = classLoader.getResources(path);
		List <File> dirs = new ArrayList<File>();
		while(resources.hasMoreElements()){
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for(File directory:dirs){
			classes.addAll(findClasses(directory,packageName));
		}
		return classes;
	}
	
	/**
	 * <li>说明：根据入参文件目录和包名，获取该文件目录下所有的class文件名，然后根据文件名获取class对象后返回
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-01-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 文件目录 
	 * @param packageName 包名
	 * @return List<Class> 包下所有类的集合
	 */	
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException{
		List<Class> classes = new ArrayList<Class>();
		if(!directory.exists()){
			return classes; //如文件目录不存在，则不做处理
		}
		File[] files = directory.listFiles(); //获取目录下的文件（含子目录）
		for(File file : files){
			if(file.isDirectory()){
				//递归查找文件夹下面的所有文件
				assert !file.getName().contains("."); 
				classes.addAll(findClasses(file,packageName+"."+file.getName()));//递归调用
			} else if (file.getName().endsWith(".class")){
				classes.add(Class.forName(packageName+"."+file.getName().replace(".class", ""))); //根据文件名获取类
			}
		}
		return classes;
	}
}
