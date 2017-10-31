package com.yunda.frame.report.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.report.entity.FileCatalog;
import com.yunda.frame.report.entity.FileObject;
import com.yunda.frame.util.DateUtil;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FileObject业务类,报表文件对象
 * <li>创建人：何涛
 * <li>创建日期：2015-01-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="fileObjectManager")
public class FileObjectManager extends JXBaseManager<FileObject, FileObject>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Resource
	private PrinterModuleManager printerModuleManager;
	
	/**
	 * <li>说明：根据“报表打印模板主键”和“当前标识”查询报表文件对象实体集合
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param printerModuleIDX 报表打印模板主键
	 * @param currentFlag 当前标识
	 * @return List<FileObject> 报表文件对象实体
	 */
	@SuppressWarnings("unchecked")
	public List<FileObject> getModel(String printerModuleIDX, String currentFlag) {
		String hql = "From FileObject Where recordStatus = 0 And printerModuleIDX = ? And currentFlag = ?";
		return this.daoUtils.find(hql, new Object[]{printerModuleIDX, currentFlag});
	}
	
	/**
	 * <li>说明：根据“报表打印模板主键“查询报表文件对象实体集合
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param printerModuleIDX 报表打印模板主键
	 * @return List<FileObject> 报表文件对象实体
	 */
	@SuppressWarnings("unchecked")
	public List<FileObject> getModelByPrinterModuleIDX(String printerModuleIDX) {
		String hql = "From FileObject Where recordStatus = 0 And printerModuleIDX = ?";
		return this.daoUtils.find(hql, new Object[]{printerModuleIDX});
	}

	/**
	 * <li>说明：根据“报表打印模板主键”获取报表文件的最大版本号
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param printerModuleIDX 报表打印模板主键
	 * @return 最大版本号
	 */
	private String maxVersion(String printerModuleIDX) {
		String hql = "Select Max(version) From FileObject Where recordStatus = 0 And printerModuleIDX = ?";
		Object obj = this.daoUtils.findSingle(hql, new Object[]{printerModuleIDX});
		return null == obj ? "1" : Integer.parseInt((String)obj) + 1 + "";
	}

	/**
	 * <li>说明：保存上传的文件到数据库
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param report 报表文件的File实体
	 * @param fileDesc 报表文件描述
	 * @param fileUploadPath 报表文件上传路径
	 * @param printerModuleIDX 报表打印模板主键
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void saveUpload(File report, String fileDesc, String fileUploadPath, String printerModuleIDX) throws FileNotFoundException, IOException, BusinessException, NoSuchFieldException {
		FileObject entity = new FileObject();
		entity.setCreateTime(Calendar.getInstance().getTime());				// 创建时间
		// 默认设置新增的报表文件为启用
		entity.setCurrentFlag(FileObject.CONST_STR_CURRENT_FLAG_T);
		entity.setFileDesc(fileDesc);										// 文件描述
		entity.setFileLength(report.length());								// 文件大小
		entity.setFileName(fileUploadPath.substring(fileUploadPath.lastIndexOf(FileCatalog.CONST_STR_FILE_SEPARATOR) + 1));			// 文件名称
		entity.setFileObject(Hibernate.createBlob(new FileInputStream(report)));	// 文件的Blob对象
		entity.setFileUploadPath(fileUploadPath);							// 文件上传路径
		entity.setPrinterModuleIDX(printerModuleIDX);						// 报表打印模板主键
		entity.setVersion(maxVersion(printerModuleIDX));			// 版本号
		this.saveOrUpdate(entity);
		
		// 设置其他报表文件的当前标识为“未启用”
		this.updateCurrentFlag(entity);
	}
	
	/**
	 * <li>说明：重写逻辑删除方法，报表文件删除时，要更新报表打印模板的“最近更新时间”字段
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体类主键idx数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */	
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (Serializable idx : ids) {
			this.printerModuleManager.updateLatestUpdateTime(this.getModelById(idx).getPrinterModuleIDX());
		}
		super.logicDelete(ids);
	}
	
	
	/**
	 * <li>说明：启用指定的报表文件对象（设置当前标识为启用），并将之前启用的报表文件的当前标识设置为未启用
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 当前启用的报表文件对象
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void updateCurrentFlag(FileObject entity) throws BusinessException, NoSuchFieldException {
		
		// 获取之前启用的报表文件
		List<FileObject> entityList = this.getModel(entity.getPrinterModuleIDX(), FileObject.CONST_STR_CURRENT_FLAG_T);
		if (null != entityList && 0 < entityList.size()) {
            for (FileObject fo : entityList) {
                // 设置之前报表文件的当前标识为未启用
                fo.setCurrentFlag(FileObject.CONST_STR_CURRENT_FLAG_F);
            }
		}
        
		// 设置正在操作的报表文件的当前标识为启用
        if (null == entity.getCurrentFlag() || !FileObject.CONST_STR_CURRENT_FLAG_T.equals(entity.getCurrentFlag())) {
            entity.setCurrentFlag(FileObject.CONST_STR_CURRENT_FLAG_T);
            entityList.add(entity);
        }
		this.saveOrUpdate(entityList);
        
        // 更新报表打印模板“最近更新时间”
		this.printerModuleManager.updateLatestUpdateTime(entity.getPrinterModuleIDX());
	}
	
    /**
     * <li>说明：导入报表文件到数据库
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param reportFile 报表文件的File实体
     * @param printerModuleIDX 报表打印模板主键
     * @throws FileNotFoundException
     * @throws IOException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void insertDeployedReport(File reportFile, String printerModuleIDX) throws FileNotFoundException, IOException, BusinessException, NoSuchFieldException {
        // 如果正在导入的报表文件已经存在，则不能重复导入
        if (this.isExist(printerModuleIDX)) {
            return;
        }
        FileObject fo = new FileObject();
        fo.setCurrentFlag(FileCatalog.CONST_STR_EDITABLE_T);                            // 当前标识
        fo.setCreateTime(Calendar.getInstance().getTime());                             // 创建时间
        fo.setFileDesc(new StringBuilder("初始化报表文件【").append(DateUtil.yyyy_MM_dd_HH_mm_ss.format(Calendar.getInstance().getTime())).append("】").toString());      // 文件描述
        fo.setFileLength(reportFile.length());                                          // 文件大小
        fo.setFileName(reportFile.getName());                                           // 文件名称
        fo.setFileObject(Hibernate.createBlob(new FileInputStream(reportFile)));        // 报表文件的字节码
        fo.setPrinterModuleIDX(printerModuleIDX);                                       // 报表打印模板主键
        fo.setVersion(this.maxVersion(printerModuleIDX));                               // 版本号
        
        this.saveOrUpdate(fo);
    }
    
    /**
     * <li>说明：验证某一报表打印模板下是否已经有已启用的报表文件
     * <li>创建人：何涛
     * <li>创建日期：2015-02-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param printerModuleIDX 报表打印模板主键
     * @return boolean 如果有已经有已启用的报表文件则返回true，否则返回false
     */
    private boolean isExist(String printerModuleIDX) {
        String hql = "Select Count(*) From FileObject Where recordStatus = 0 And printerModuleIDX = ? And currentFlag = ?";
        int count = this.daoUtils.getCount(hql, new Object[]{printerModuleIDX, FileObject.CONST_STR_CURRENT_FLAG_T});
        return count <= 0 ? false : true;
    }
    
    /**
     * <li>说明：验证某一报表打印模板下是否已经存在指定文件名称的报表文件
     * <li>创建人：何涛
     * <li>创建日期：2015-02-03
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param printerModuleIDX 报表打印模板主键
     * @param fileName 报表文件名称
     * @return int 报表文件数量
     */
    @SuppressWarnings("unused")
    private boolean isExist(String printerModuleIDX, String fileName) {
        String hql = "Select Count(*) From FileObject Where recordStatus = 0 And fileName = ? And printerModuleIDX = ?";
        int count = this.daoUtils.getCount(hql, new Object[]{fileName, printerModuleIDX});
        return count <= 0 ? false : true;
    }
    
}