package com.yunda.frame.baseapp.upload.manager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.FileUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Attachment业务类,附件管理
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "attachmentManager")
public class AttachmentManager extends JXBaseManager<Attachment, Attachment> {

	/**
	 * <li>说明：新增附件记录
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Attachment att：附件记录对象
	 * @return Attachment 新增保存成功的附件记录
	 * @throws 
	 */
	@Transactional
	public void add(Attachment att) {
		this.daoUtils.getHibernateTemplate().save(att);
	}

	/**
	 * <li>说明：批量删除附件，特别说明该方法实际上是一条一条地进行删除附件。
	 * 因为涉及到磁盘文件操作，所以该方法不享受事务控制，在批量删除过程中可能出现某些删除成功，某些失败的情况。
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public List<String> del(Serializable... ids) {
		//先逻辑删除数据库附件记录
		List<String> errors = new ArrayList<String>();
		for (Serializable id : ids) {
			String err = delete(id);
			if (err != null)
				errors.add(err);
		}
		return errors;
	}

	/**
	 * <li>说明：删除一条附件记录以及相对应的磁盘文件
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Serializable idx：记录主键
	 * @return String 错误信息，如果为null表示操作成功
	 * @throws 抛出异常列表
	 */
	public String delete(Serializable idx) {
		try {
			Attachment att = this.getModelById(idx);
			//先物理删除磁盘文件
			String filepath = uploadFilepath(att);
			boolean deleted = FileUtil.deleteFile(filepath);
			if (!deleted)
				return "系统删除文件" + att.getAttachmentRealName() + "，操作失败。";
			//再逻辑删除数据库附件记录
			att = EntityUtil.setSysinfo(att);
			att = EntityUtil.setDeleted(att);
			this.daoUtils.getHibernateTemplate().saveOrUpdate(att);
		} catch (Exception e) {
			e.printStackTrace();
			return "删除idx=" + idx + "的附件出现异常：" + e;
		}
		return null;
	}

	/**
	 * <li>说明：计算上传附件的保存目录全路径，上传根目录+业务表名+年份+月份
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String tableName：业务表名
	 * @return 保存目录全路径
	 * @throws 
	 */
	public static String uploadDir(String tableName) {
		Calendar calendar = Calendar.getInstance();
		StringBuilder sb = new StringBuilder().append(JXConfig.getInstance().getUploadPath()).append(File.separator).append(tableName).append(File.separator).append(
				calendar.get(Calendar.YEAR)).append(File.separator).append(calendar.get(Calendar.MONTH) + 1).append(File.separator);
		return sb.toString();
	}

	/**
	 * <li>说明：获取制定附件记录的文件保存全路径
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Attachment att：附件记录对象
	 * @return 文件保存全路径
	 * @throws 
	 */
	public static String uploadFilepath(Attachment att) {
		Date uploadTime = att.getUploadTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(uploadTime);
		StringBuilder sb = new StringBuilder().append(JXConfig.getInstance().getUploadPath()).append(File.separator).append(att.getAttachmentKeyName()).append(File.separator)
				.append(calendar.get(Calendar.YEAR)).append(File.separator).append(calendar.get(Calendar.MONTH) + 1).append(File.separator).append(att.getAttachmentSaveName());
		return sb.toString();
	}

	/**
	 * <li>说明：根据附件所属的业务键值获取附件列表
	 * <li>创建人：程锐
	 * <li>创建日期：2015-3-23
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param attachmentKeyIDX 附件所属的关键值
	 * @param attachmentKeyName 附件所属的业务表单，记录表名
	 * @return 附件列表
	 */
	@SuppressWarnings("unchecked")
	public List<Attachment> findListByKey(String attachmentKeyIDX, String attachmentKeyName) {
		Attachment att = new Attachment();
		att.setAttachmentKeyIDX(attachmentKeyIDX);
		att.setAttachmentKeyName(attachmentKeyName);
		att.setRecordStatus(Constants.NO_DELETE);
		return daoUtils.getHibernateTemplate().findByExample(att);
	}

	/**
	 * <li>说明：删除附件
	 * <li>创建人：林欢
	 * <li>创建日期：2016-09-02
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param filePath 相对路径
	 * @return void
	 * @throws Exception
	 */
	public void deleteAttachment(String[] filePaths) {

		for (String filepath : filePaths) {
			//先物理删除磁盘文件
			boolean deleted = FileUtil.deleteFile(filepath);
			if (!deleted) {
				throw new BusinessException("系统删除文件，操作失败。");
			}
		}
	}

	/**
	 * <li>说明：根据条件获取附件列表
	 * <li>创建人：林欢
	 * <li>创建日期：2016-10-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param params 参数封装
	 * @return List<Attachment> 附件列表
	 */
	@SuppressWarnings("unchecked")
	public List<Attachment> findAttachmentListByParams(Map<String, Object> params) {

		StringBuffer sb = new StringBuffer();

		sb.append(" from Attachment a where 1=1 ");

		//附件所属的关键值
		if (params.get("attachmentKeyIDX") != null) {
			sb.append(" and a.attachmentKeyIDX = '").append(params.get("attachmentKeyIDX").toString()).append("'");
		}

		//    	附件所属的关键值
		if (params.get("attachmentRealName") != null) {
			sb.append(" and a.attachmentRealName like '%").append(params.get("attachmentRealName").toString()).append("%'");
		}

		return (List<Attachment>) this.find(sb.toString());
	}

	/**
	 * <li>说明：根据“业务记录idx主键”查询图片列表
	 * <li>创建人： 张凡
	 * <li>创建日期：2015年8月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param attachmentKeyIDX 附件所属的关键值（业务记录idx主键）
	 * @return 附件（图片）对象集合
	 */
	@SuppressWarnings("unchecked")
	public List<Attachment> findImages(String attachmentKeyIDX) {
		String hql = "From Attachment Where recordStatus = 0 And attachmentKeyIDX = ? And fileType In (?, ?, ?)";
		return this.daoUtils.find(hql, attachmentKeyIDX, "jpg", "bmp", "png");
	}

	/**
	 * <li>说明：根据“业务记录idx主键集合”查询图片列表
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param attachmentKeyIds 附件所属的关键值（业务记录idx主键）集合
	 * @return 附件（图片）对象集合
	 */
	@SuppressWarnings("unchecked")
	public List<Attachment> findImages(List<String> attachmentKeyIds) {
		if (null == attachmentKeyIds || attachmentKeyIds.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String idx : attachmentKeyIds) {
			sb.append(",'").append(idx).append("'");
		}
		String ids = sb.substring(1);
		String hql = "From Attachment Where recordStatus = 0 And attachmentKeyIDX In (" + ids + ") And fileType In (?, ?, ?) Order By updateTime Asc";
		return this.daoUtils.find(hql, "jpg", "bmp", "png");
	}

	/**
	 * <li>说明：手持终端上传附件
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param attachmentKeyIDX 附件所属的关键值（业务记录idx主键）
	 * @param attachmentKeyName 附件所属的业务表单，记录表名
	 * @param filePathArray 文件路径数组
	 * @throws NoSuchFieldException
	 */
	public void insert(String attachmentKeyIDX, String attachmentKeyName, String[] filePathArray) throws NoSuchFieldException {
		File file = null;
		Attachment entity = null;
		List<Attachment> entityList = new ArrayList<Attachment>(filePathArray.length);
		for (String filePath : filePathArray) {
			file = new File(filePath);
			if (!file.exists() || null != getModelByRealPath(file.getAbsolutePath())) {
				continue;
			}
			entity = new Attachment();
			String fileName = file.getName();
			entity.setAttachmentKeyIDX(attachmentKeyIDX);
			entity.setAttachmentKeyName(attachmentKeyName);
			entity.setAttachmentSaveName(fileName);
			entity.setAttachmentRealName(fileName);
			entity.setRealPath(file.getAbsolutePath());
			entity.setAttachmentSize(file.length());
			entity.setUploadPerson(SystemContext.getOmEmployee().getEmpid());
			entity.setUploadPersonName(SystemContext.getOmEmployee().getEmpname());
			entity.setUploadTime(new Date(file.lastModified()));
			entity.setFileType(fileName.substring(fileName.indexOf(".") + 1));
			entityList.add(entity);
		}
		if (entityList.isEmpty()) {
			return;
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：验证附件是否已经存在
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param realPath 附件存放真实路径
	 * @return true：存在，false：不存在
	 */
	public Attachment getModelByRealPath(String realPath) {
		String hql = "From Attachment Where recordStatus = 0 And realPath = ?";
		return (Attachment) this.daoUtils.findSingle(hql, realPath);
	}
}
