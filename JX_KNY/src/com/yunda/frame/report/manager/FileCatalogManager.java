package com.yunda.frame.report.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.report.entity.FileCatalog;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FileCatalog业务类,报表模板管理类型标识目录
 * <li>创建人：何涛
 * <li>创建日期：2015-01-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="fileCatalogManager")
public class FileCatalogManager extends JXBaseManager<FileCatalog, FileCatalog> implements IbaseComboTree {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** PrinterModule业务类,报表打印模板 */
	@Resource
	private PrinterModuleManager printerModuleManager;
	
	/**
	 * 
	 * <li>说明：公共查询Base_comboTree和Base_multyComboTree方法
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param req HttpServletRequest对象
	 * @return List<HashMap>前台树所需数据列表
	 * @throws Exception
	 */
	@Override
	public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception {
		String parentIDX = req.getParameter("parentIDX");
		return tree(parentIDX, true);
	}
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(FileCatalog entity) throws BusinessException {
        // 唯一性验证, 同一级目录下不能有名称相同的文件目录
        FileCatalog t = this.getModel(entity.getParentIDX(), entity.getFolderNameEN());
        if (null != t && !t.getIdx().equals(entity.getIdx())) {
            return new String[] { "不能添加重复的报表模板目录！" };
        }
        return null;
    }
	
	/**
	 * <li>说明：查询指定目录下的指定文件夹英文名称的报表模板管理类型标识目录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容
	 * @param parentIDX 上级主键
	 * @param folderNameEN 文件夹英文名称
	 * @return 报表模板管理类型标识目录实体
     *
	 */
	private FileCatalog getModel(String parentIDX, String folderNameEN) {
		String hql = "From FileCatalog Where recordStatus = 0 And parentIDX = ? And folderNameEN = ?";
		return (FileCatalog)this.daoUtils.findSingle(hql, new Object[]{parentIDX, folderNameEN});
	}
	
	/**
	 * <li>说明：查询指定目录的下属子目录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param parentIDX 上级主键
	 * @return List<FileCatalog> 指定目录的下属子目录
	 */
	@SuppressWarnings("unchecked")
	private List<FileCatalog> findChildren (String parentIDX) {
		String hql = "From FileCatalog Where recordStatus = 0 And parentIDX = ? Order By folderNameEN ASC";
		return this.daoUtils.find(hql, new Object[]{parentIDX});
	}
	
	/**
	 * <li>说明：报表模板管理类型标识目录树
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param parentIdx 上级报表模板管理类型标识目录主键
	 * @param isComboStore 是否是BaseCombo组件的数据容器加载
	 * @return List 子节点对象列表
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> tree(String parentIdx, boolean isComboStore) {
		// 查询指定目录的下属子目录
		List<FileCatalog> entityList =  this.findChildren(parentIdx);
		List<HashMap> children = new ArrayList<HashMap>();
		HashMap<String, Object> map = null;
		for (FileCatalog entity : entityList) {
			map = new HashMap<String, Object>();
			map.put("id", entity.getIdx());
            map.put("text", entity.formatDisplayText());
			map.put("leaf", isLeaf(entity));
			map.put("iconCls", "folderIcon");
			
			map.put("parentIDX", entity.getParentIDX());						// 上级主键
			map.put("editable", entity.getEditable());							// 是否可编辑
			map.put("folderDesc", entity.getFolderDesc());						// 文件夹描述
			map.put("folderNameEN", entity.getFolderNameEN());					// 英文名称
			map.put("folderNameCH", entity.getFolderNameCH());					// 中文名称
            if (isComboStore) {
                map.put("folderFullPath", this.formatFolderFullPath(entity));		// 中文名称
            }
			children.add(map);
		}
		return children;
	} 
	
	/**  
	 * <li>说明：递归获取报表部署目录对象的全路径信息
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 报表模板管理类型标识目录实体
	 * @param list 结果集合
	 */
	private void getFolderFullPath(FileCatalog entity, List<String> list) {
		list.add(entity.getFolderNameEN());
		if (!entity.getParentIDX().equals(FileCatalog.CONST_STR_PATH_ROOT)) {
			FileCatalog parentEntity = this.getModelById(entity.getParentIDX());
			getFolderFullPath(parentEntity, list);
		}
	}
	
	/**
	 * <li>说明：格式化报表模板管理类型标识目录的全路径信息
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 报表模板管理类型标识目录实体
	 * @return 报表模板管理类型标识目录的全路径信息
	 */
	private String formatFolderFullPath(FileCatalog entity) {
		List<String> folderNameList = new ArrayList<String>();
		this.getFolderFullPath(entity, folderNameList);
		StringBuilder sb = new StringBuilder();
		for (int i = folderNameList.size() - 1; i >= 0; i--) {
			sb.append(FileCatalog.CONST_STR_PATH_SEPARATOR).append(folderNameList.get(i));
		}
		return sb.substring(1);
	}
	
	/**
	 * <li>说明：检验指定的报表模板管理类型标识目录是否是叶子节点
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 报表模板管理类型标识目录实体
	 * @return boolean 如果是叶子节点则返回true，否则返回false
	 */
	private boolean isLeaf(FileCatalog entity) {
		String hql = "Select Count(*) From FileCatalog Where recordStatus = 0 And parentIDX = ?";
		int count = this.daoUtils.getCount(hql, new Object[]{entity.getIdx()});
		return 0 >= count ? true : false;
	}
	
	/**
	 * <li>说明：物理删除记录，根据指定的实体身份标示数组进行批量删除实体
	 * <li>创建人：何涛
	 * <li>创建日期：2015-02-06
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体类主键idx数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */	
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (Serializable id : ids) {
			this.logicDelete(getModelById(id));
		}
	}
	
	/**
	 * <li>说明：逻辑删除记录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-27
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 报表模板管理类型标识目录实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void logicDelete(FileCatalog entity) throws BusinessException, NoSuchFieldException {
		// 验证，如果是不可编辑的报表部署目录，则不允许删除
		if (FileCatalog.CONST_STR_EDITABLE_F.equals(entity.getEditable())) {
			throw new BusinessException("报表部署目录" + entity.formatDisplayText() + "不可以删除！");
		}
		
		// 如果是父节点，还需删除子节点
		if (!this.isLeaf(entity)) {
			List<FileCatalog> enityList = this.findChildren(entity.getIdx());
			for (FileCatalog fc : enityList) {
				logicDelete(fc);
			}
		}
		this.logicDelete(entity.getIdx());
	}

	/**
	 * <li>说明：初始化目录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人：何涛
	 * <li>修改日期：2016-04-21
	 * <li>修改内容：在不能获取报表打印模板目录信息时，给与操作提示
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */	
	public void initialize() throws BusinessException, NoSuchFieldException {
        // 删除从页面删除的目录（recordStatus = 1）
        String sql = "DELETE FROM R_FILE_CATALOG WHERE RECORD_STATUS = 1";
        this.daoUtils.executeSql(sql);
        // 获取所有需要初始化的报表打印模板的部署目录
		List list = this.printerModuleManager.listDeployCatalog();
		if (null == list || 0 >= list.size()) {
			throw new BusinessException("不能获取报表打印模板目录信息，请先维护报表打印模板！");
		}
		for (int i = 0; i < list.size(); i++) {
			String folderFullPath = (String) list.get(i);
			this.createFolder(folderFullPath);
		}
	}
	
	/**
	 * <li>说明：根据“文件夹英文全路径”利用递归创建文件夹目录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param folderFullPath 文件夹英文全路径
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	private void createFolder(String folderFullPath) throws BusinessException, NoSuchFieldException {
		String[] folderNames = folderFullPath.split("\\" + FileCatalog.CONST_STR_PATH_SEPARATOR);
		String parentIDX = FileCatalog.CONST_STR_PATH_ROOT;
		for (String folderNameEN : folderNames) {
			FileCatalog fc = this.getModel(parentIDX, folderNameEN);
			// 如果该文件夹已经存在，则不进行插入
			if (null != fc) {
				parentIDX = fc.getIdx();
				continue;
			}
			FileCatalog entity = new FileCatalog();
			entity.setEditable(FileCatalog.CONST_STR_EDITABLE_T);			// 默认设置为可编辑
			entity.setFolderNameEN(folderNameEN);							// 文件英文名称
			entity.setParentIDX(parentIDX);									// 上级主键
			this.saveOrUpdate(entity);
			parentIDX = entity.getIdx();
		}
	}
	
}