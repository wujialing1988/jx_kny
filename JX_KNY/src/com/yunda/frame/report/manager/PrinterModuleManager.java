package com.yunda.frame.report.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.report.entity.FileCatalog;
import com.yunda.frame.report.entity.FileObject;
import com.yunda.frame.report.entity.PrinterModule;
import com.yunda.frame.report.entity.ReportToBusiness;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.FileUtil;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PrinterModule业务类,报表打印模板
 * <li>创建人：何涛
 * <li>创建日期：2015-01-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="printerModuleManager")
public class PrinterModuleManager extends JXBaseManager<PrinterModule, PrinterModule>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
    /** FileObject业务类,报表文件对象 */
	@Resource
	private FileObjectManager fileObjectManager;
    
	/** ReportToBusines业务类,报表业务关联 */
	@Resource
	private ReportToBusinessManager reportToBusinessManager;
    
	/** CodeRuleConfig业务类,业务编码规则配置 */
	@Resource
    CodeRuleConfigManager codeRuleConfigManager;
	
	private final String FILE_OPERATE_ERROR_MSG = "报表文件写入错误！";
    
	/**
	 * <li>说明：报表标识码的唯一性验证
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人：何涛
	 * <li>修改日期：2016-03-08
	 * <li>修改内容：重构，优化代码
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
    public String[] validateUpdate(PrinterModule entity) throws BusinessException {
        String hql = "From PrinterModule Where recordStatus = 0 And identifier = ?";
        PrinterModule pm = (PrinterModule) this.daoUtils.findSingle(hql, new Object[] { entity.getIdentifier() });
        if (null != pm && !pm.getIdx().equals(entity.getIdx())) {
            return new String[] { "报表标识码【" + entity.getIdentifier() + "】已经存在，不能重复添加！" };
        }
        hql = "From PrinterModule Where recordStatus = 0 And deployName = ? And deployCatalog = ?";
        pm = (PrinterModule) this.daoUtils.findSingle(hql, new Object[] { entity.getDeployName(), entity.getDeployCatalog() });
        if (null != pm && !pm.getIdx().equals(entity.getIdx())) {
            return new String[]{"报表部署名称【" +entity.getDeployName() + "】已经存在，不能重复添加！"};
        }
        return null;
    }
	
	/**
	 * <li>说明：获取所有有效地额报表打印模板实体集合
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 所有有效地额报表打印模板实体集合
	 */
	private List<PrinterModule> finAll() {
		PrinterModule searchEntity = new PrinterModule();
		searchEntity.setRecordStatus(Constants.NO_DELETE);
		return this.findList(searchEntity);
	}
	
	/**
	 * <li>说明：更新报表打印模板“最近更新时间”
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-28
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 报表打印模板主键
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void updateLatestUpdateTime(String idx) throws BusinessException, NoSuchFieldException {
		PrinterModule entity = this.getModelById(idx);
		if (null == entity) {
			return;
		}
        // 设置最近更新时间为当前时间
        entity.setLatestUpdateTime(Calendar.getInstance().getTime());
		this.saveOrUpdate(entity);
	}
	
	/**
	 * <li>说明：重写删除方法，删除报表打印模板时，要同时删除下属的【报表文件】
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体类主键idx数组
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */	
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		PrinterModule entity = null;
		for (Serializable idx : ids) {
			entity = this.getModelById(idx);
			// 删除报表打印模板下属的【报表文件】
			List<FileObject> list = this.fileObjectManager.getModelByPrinterModuleIDX(entity.getIdx());
            if (null != list && list.size() > 0) {
                this.fileObjectManager.logicDelete(list);
            }
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明：获取所有需要初始化的报表打印模板的部署目录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 所有需要初始化的报表打印模板的部署目录
	 */
	public List listDeployCatalog() {
		String sql = "SELECT DISTINCT T.DEPLOY_CATALOG FROM R_PRINTER_MODULE T WHERE T.RECORD_STATUS = 0";
		return this.daoUtils.executeSqlQuery(sql);
	}
	
	/**
	 * <li>说明：增量部署
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 需要部署的报表打印模板主键数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void deployByIncrement(String[] ids) throws BusinessException, NoSuchFieldException, SQLException, IOException {
		List<PrinterModule> entityList = new ArrayList<PrinterModule>();
		for (String idx : ids) {
			// 获取报表打印模板对象
			entityList.add(this.getModelById(idx));
		}
        this.deploy(entityList);
	}
	
	
	/**
	 * <li>说明：全量部署，部署方式：首先删除报表服务器下的所有已部署的报表文件，再部署数据库中启用的报表文件到报表服务器
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param deleteDir 是否在全量部署前删除服务器下的所有已部署的报表文件
	 * @throws SQLException
	 * @throws IOException
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void deployAll(boolean deleteDir) throws SQLException, IOException, BusinessException, NoSuchFieldException {
        if (deleteDir) {
            // 删除报表服务器下的所有已部署的报表文件
            String reportServerAbsolutePath = this.getReportServerAbsolutePath();
            FileUtil.deleteDir(reportServerAbsolutePath);
        }
        // 批量部署
        this.deploy(this.finAll());
    }		
    
    /**
     * <li>说明：批量部署多个报表打印模板，部署成功后更新报表打印模板的最近部署时间
     * <li>创建人：何涛
     * <li>创建日期：2015-2-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityList 报表打印模板实体集合
     * @throws BusinessException 
     * @throws SQLException
     * @throws IOException
     * @throws NoSuchFieldException
     */
    private void deploy(List<PrinterModule> entityList) throws BusinessException, SQLException, IOException, NoSuchFieldException {
        if (null == entityList || entityList.size() <= 0) {
            return;
        }
        for (PrinterModule entity : entityList) {
            // 部署单个报表打印模板实体
            if (!this.writeToFileSystem(entity)) {
                continue;
            }
            // 更新“最近部署时间”
            entity.setLatestDeployTime(Calendar.getInstance().getTime());
        }
        this.saveOrUpdate(entityList);
    }
	
	/**
	 * <li>说明：将报表文件写入到报表服务器的文件系统中
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 报表模板打印实体
     * @return 如果报表打印模板下有报表文件，并且部署成功则返回true，否则返回false
	 * @throws SQLException
	 * @throws IOException
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	private boolean writeToFileSystem(PrinterModule entity) throws SQLException, IOException, BusinessException, NoSuchFieldException {
        // 部署钻取报表
        String[] ids = this.findChildrenIds(entity.getIdx());
        if (null != ids) {
            // 增量部署
            this.deployByIncrement(ids);
        }
        // 获取报表打印模板对象下启用的【报表文件】
        List<FileObject> foList = this.fileObjectManager.getModel(entity.getIdx(), FileObject.CONST_STR_CURRENT_FLAG_T);
        if (null == foList || 0 >= foList.size()) {
            return false;
        }
        
        // 获取【报表文件】部署在服务器的绝对路径
		String absolutePath = this.getReportAbsolutePath(entity);
		File file = new File(absolutePath);
		if (!file.exists()) {
            file.getParentFile().mkdirs();
		}
		// 一般情况下，一个报表打印模板下有且仅有一个【报表文件】对象为已启用
		FileObject fo = foList.get(0);
		file.createNewFile();
		// 从数据库中获取文件的输入流
		InputStream inputStream = fo.getFileObject().getBinaryStream();
		writeToFile(inputStream, file);
        return true;
	}
    
    /**
     * <li>说明：根据主模板主键获取钻取报表主键数组
     * <li>创建人：何涛
     * <li>创建日期：2015-2-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级报表标主键(主模板主键)
     * @return String[] 钻取报表主键数组
     */
    private String[] findChildrenIds(String parentIDX) {
        List<PrinterModule> children = this.findChildren(parentIDX);
        if (null == children || children.size() <= 0) {
            return null;
        }
        List<String> list = new ArrayList<String>(children.size());
        for (PrinterModule pm : children) {
            list.add(pm.getIdx());
        }
        return list.toArray(new String[children.size()]);
    }
    
    /**
     * <li>说明：根据主模板主键获取钻取报表实体集合
     * <li>创建人：何涛
     * <li>创建日期：2015-2-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级报表标主键(主模板主键)
     * @return List<PrinterModule> 钻取报表实体集合
     */
    @SuppressWarnings("unchecked")
    private List<PrinterModule> findChildren(String parentIDX) {
        String hql = "From PrinterModule Where recordStatus = 0 And parentIDX = ?";
        return this.daoUtils.find(hql, new Object[]{parentIDX});
    }

	/**
	 * <li>说明：将输入流写入到目标文件，常规文件读写方式
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param descFile 目标文件
	 * @param inputStream 输入流
	 */
	@SuppressWarnings("unused")
    private void writeToFile(File descFile, InputStream inputStream) {
		FileOutputStream fos = null;
		try {
			Channels.newChannel(inputStream);
			// 写入报表文件到报表服务器目录
			fos = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			int length = inputStream.read(buf);
			while (length > 0) {
				fos.write(buf);
				length = inputStream.read(buf);
			}
		} catch (Exception e) {
			throw new BusinessException(FILE_OPERATE_ERROR_MSG);
		} finally {
			try {
				fos.flush();
				fos.close();
				inputStream.close();
			} catch (IOException e) {
				throw new BusinessException(FILE_OPERATE_ERROR_MSG);
			}
		}
	}
	
	/**
	 * <li>说明：将输入流写入到目标文件，java nio读写方式
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param inputStream 输入流
	 * @param descFile 目标文件
	 * @throws IOException
	 */
	private void writeToFile(InputStream inputStream, File descFile) {
		ReadableByteChannel fcIn = null;
		FileOutputStream fos = null;
		FileChannel fcOut = null;
		try {
			// 获取输入通道
			fcIn = Channels.newChannel(inputStream);
			// 获取输出通道
			fos = new FileOutputStream(descFile);
			fcOut = fos.getChannel();
			
			// 创建缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (true) {
				// 重设缓冲区，使它可以接受读新的数据读入
				buffer.clear();
				
				int length = fcIn.read(buffer);
				if (0 >= length) {
					break;
				}
				// 使缓冲区可以将新读入的数据写入到输出通道
				buffer.flip();
				// 从输出通道中将新数据写入缓冲区
				fcOut.write(buffer);
			}
		} catch (Exception e) {
			throw new BusinessException(FILE_OPERATE_ERROR_MSG);
		} finally {
			try {
				// 关闭流和通道
				fcOut.close();
				fos.flush();
				fos.close();
				fcIn.close();
				inputStream.close();
			} catch (Exception e) {
				throw new BusinessException(FILE_OPERATE_ERROR_MSG);
			}
		}
	}
	
	/**
	 * <li>说明：获取【报表文件】部署在服务器的绝对路径
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 报表打印模板实体
	 * @return 【报表文件】全路径
	 */
	private String getReportAbsolutePath(PrinterModule entity) {
		// 【报表服务器】在TOMCAT/WEBLOGIC等应用服务器部署的绝对路径
		String reportletsAbsolutePath = this.getReportServerAbsolutePath();
		
		// 构造【报表文件】部署的绝对路径
		StringBuilder sb = new StringBuilder(reportletsAbsolutePath);
		sb.append(FileCatalog.CONST_STR_FILE_SEPARATOR);					// 文件路径分隔符
		sb.append(entity.getDeployCatalog().replace(FileCatalog.CONST_STR_PATH_SEPARATOR, FileCatalog.CONST_STR_FILE_SEPARATOR));
		sb.append(FileCatalog.CONST_STR_FILE_SEPARATOR);					// 文件路径分隔符
		sb.append(entity.getDeployName());
		
		return sb.toString();
	}
	
	/**
	 * <li>说明：获取【报表服务器】在TOMCAT/WEBLOGIC等应用服务器部署的绝对路径
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：何涛
	 * <li>修改日期：2016-03-08
	 * <li>修改内容：修改使用配置的报表服务器部署绝对路径时，文件路径有误的问题
	 * @return 【报表服务器】在TOMCAT/WEBLOGIC等应用服务器部署的绝对路径
	 */
	private String getReportServerAbsolutePath() {
	    StringBuilder sb = new StringBuilder();
        String webappsPath = JXConfig.getInstance().getWebappsPath();
        if (null != webappsPath && webappsPath.trim().length() > 0) {
            // Modified by hetao on 2016-04-21 兼容部分配置项仅配置奥webapps目录的不规格配置项
            return webappsPath.endsWith(PrinterModule.CONST_STR_REPORT_DEPLOY_FOLDER) ? 
                webappsPath : webappsPath + PrinterModule.CONST_STR_REPORT_DEPLOY_PATH;
        }
        // 获取WEB应用（CoreFrame文件夹）的服务器运行目录
        String coreFramePath = ServletActionContext.getServletContext().getRealPath("");
        // 获取TOMCAT/WEBLOGIC等应用服务器WEB应用（webapps文件夹）的容器目录
        File webappsFolder = new File(coreFramePath).getParentFile(); 
        // 获取【报表文件】在TOMCAT/WEBLOGIC等应用服务器的部署目录
        sb.append(webappsFolder.getAbsolutePath());						
        sb.append(PrinterModule.CONST_STR_REPORT_DEPLOY_PATH);				//  "\\ydReport\\WEB-INF\\reportlets";
		return sb.toString();
	}

    /**
     * <li>说明：初始化
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void insertDeployedReport() throws BusinessException, NoSuchFieldException, FileNotFoundException, IOException {
        // 获取【报表服务器】在TOMCAT/WEBLOGIC等应用服务器部署的绝对路径 
        String reportServerAbsolutePath = this.getReportServerAbsolutePath();
        // 获取报表服务器上已部署的所有报表文件
        List<File> fileList = this.getAllReportFile(reportServerAbsolutePath);
        if (fileList.size() < 0) {
            throw new NullPointerException("没有可以导入的报表文件！");
        }
        for (File file : fileList) {
            // 导入文件到报表服务器
            insertDeployedReport(file);
        }
    }
    
    /**
     * <li>说明：导入报表文件到数据库
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param reportFile 报表文件的File实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void insertDeployedReport(File reportFile) throws BusinessException, NoSuchFieldException, FileNotFoundException, IOException {
        String absolutePath = reportFile.getAbsolutePath();
        String path = absolutePath.substring(absolutePath.indexOf("reportlets" + File.separator));
        String deployCatalog = null;
        try {
            deployCatalog = path.substring(path.indexOf(FileCatalog.CONST_STR_FILE_SEPARATOR) + 1, path.lastIndexOf(FileCatalog.CONST_STR_FILE_SEPARATOR));
            deployCatalog = deployCatalog.replace(FileCatalog.CONST_STR_FILE_SEPARATOR, FileCatalog.CONST_STR_PATH_SEPARATOR);
        } catch (StringIndexOutOfBoundsException e) {
            deployCatalog = null;
        }
        
        PrinterModule pm = this.findSingle(deployCatalog, reportFile.getName());
        if (null == pm) {
            String fileName = reportFile.getName();
            pm = new PrinterModule();
            pm.setDeployCatalog(deployCatalog);                             // 报表部署目录 
            pm.setDeployName(fileName);                                     // 报表部署名称
            pm.setEditable(PrinterModule.CONST_STR_EDITABLE_T);             // 默认为可编辑（T）
            pm.setDisplayName(fileName);                                    // 报表显示名称
            pm.setIdentifier(codeRuleConfigManager.makeConfigRule("REPORT_IDENTIFIER_NO"));                      // 报表标识码
            pm.setModuleDesc(new StringBuilder("初始化报表模板【").append(DateUtil.yyyy_MM_dd_HH_mm_ss.format(Calendar.getInstance().getTime())).append("】").toString());      // 报表描述
            pm.setLatestDeployTime(Calendar.getInstance().getTime());       // 设置最近部署时间为当前时间
            pm.setLatestUpdateTime(new Date(reportFile.lastModified()));    // 设置最近更新时间为报表文件的最新修改时间
            this.saveOrUpdate(pm);
        }
        this.fileObjectManager.insertDeployedReport(reportFile, pm.getIdx());
    }
    
    /**
     * <li>说明：根据报表部署目录和报表部署名称获取单个报表打印模板对象
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param deployCatalog 报表部署目录
     * @param deployName 报表部署名称
     * @return PrinterModule 报表打印模板实体
     */
    private PrinterModule findSingle(String deployCatalog, String deployName) {
        String hql = null;
        if (null == deployCatalog) {
            hql = "From PrinterModule Where recordStatus = 0 And deployCatalog Is Null And deployName = ?";
            return (PrinterModule)this.daoUtils.findSingle(hql, new Object[]{deployName});
        } else {
            hql = "From PrinterModule Where recordStatus = 0 And deployCatalog = ? And deployName = ?";
            return (PrinterModule)this.daoUtils.findSingle(hql, new Object[]{deployCatalog, deployName});
        }
    }
    
    /**
     * <li>说明：获取某个文件下的所有报表文件
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param folder 文件夹的File实体
     * @param list 获取到的报表文件的File实体集合
     */
    private void getAllReportFile(File folder, List<File> list) {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().endsWith(".cpt")) {
                    list.add(file);
                }
            } else {
                getAllReportFile(file, list);
            }
        }
    }
    
    /**
     * <li>说明：获取某个路径下的所有报表文件
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param absolutePath 文件夹的绝对路径
     * @return List<File> 报表文件的File实体集合
     */
    private List<File> getAllReportFile(String absolutePath) {
        File folder = new File(absolutePath);
        List<File> list = new ArrayList<File>();
        this.getAllReportFile(folder, list);
        return list;
    }

    /**
     * <li>说明：保存打印模板实体，如果报表业务关联实体主键不为空，还要保存关联信息对象
     * <li>创建人：何涛
     * <li>创建日期：2015-2-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param printerModule 报表打印模板尸体
     * @param businessIDX 业务主键
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveOrUpdate(PrinterModule printerModule, String businessIDX) throws BusinessException, NoSuchFieldException {
        // 保存打印模板实体
        this.saveOrUpdate(printerModule);
        if (null == businessIDX || businessIDX.trim().length() <= 0) {
            return;
        }
        // 报表打印模板主键
        String printerModuleIDX = printerModule.getIdx();
        ReportToBusiness rtb = this.reportToBusinessManager.getModel(printerModuleIDX, businessIDX);
        if (null == rtb) {
            rtb = new ReportToBusiness();
            rtb.setBusinessIDX(businessIDX);                    // 业务主键
            rtb.setPrinterModuleIDX(printerModuleIDX);          // 报表打印模板主键
        }
        this.reportToBusinessManager.saveOrUpdate(rtb);
    }
    
    /**
     * <li>说明：根据“业务主键”获取报表打印模板实体
     * <li>创建人：何涛
     * <li>创建日期：2015-2-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param businessIDX 业务主键
     * @return PrinterModule 报表打印模板实体
     */
    public PrinterModule getModelByBusinessIDX(String businessIDX) {
        String hql = "Select a From PrinterModule a, ReportToBusiness b Where a.recordStatus = 0 And b.recordStatus = 0 And a.idx = b.printerModuleIDX And b.businessIDX = ?";
        return (PrinterModule) this.daoUtils.findSingle(hql, new Object[]{businessIDX});
    }

    /**
     * <li>说明：获取一个可以进行打印预览的报表打印模板实体
     * <li>创建人：何涛
     * <li>创建日期：2015-2-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @param isBusinessIDX 标识主键是“报表打印模板实体主键”还是“业务主键”
     * @return 可以进行打印预览的报表打印模板实体
     */
    public PrinterModule getModelForPreview(String idx, boolean isBusinessIDX) {
        PrinterModule entity;
        if (isBusinessIDX) {
            entity = this.getModelByBusinessIDX(idx);
        } else {
            entity = this.getModelById(idx);
        }
        if (null == entity) {
            throw new NullPointerException("没有打印模板，不能进行打印预览！");
        }
        List<FileObject> foList = this.fileObjectManager.getModel(entity.getIdx(), FileObject.CONST_STR_CURRENT_FLAG_T);
        if (null == foList || foList.size() <= 0) {
            throw new NullPointerException("改打印模板下没有设置报表文件，不进行打印预览！");
        }
        if (!isDeployed(entity)) {
            throw new NullPointerException("打印模板未部署，不能进行打印预览！");
        }
        return entity;
    }
    
    /**
     * <li>说明：验证报表打印模板是否已经部署
     * <li>创建人：何涛
     * <li>创建日期：2015-2-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 报表打印模板实体
     * @return 如果已经部署则返回true，否则返回false
     */
    private boolean isDeployed(PrinterModule entity) {
        // 首先验证报表最近部署日期是否为空，如果为空则判定为还未部署
        if (null == entity.getLatestDeployTime()) {
            return false;
        }
        // 验证报表服务器上是否存在改报表文件，如果不存在则判定为还未部署
        return new File(this.getReportAbsolutePath(entity)).exists();
    }
    
    /**
     * <li>说明：单个打印模板部署，返回部署后的打印模板实体
     * <li>创建人：何涛
     * <li>创建日期：2015-2-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 打印模板主键
     * @return PrinterModule 部署后的打印模板实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws SQLException
     * @throws IOException
     */
    public PrinterModule deploySingle(String id) throws BusinessException, NoSuchFieldException, SQLException, IOException {
        this.deployByIncrement(new String[]{id});
        return this.getModelById(id);
    }
	
}