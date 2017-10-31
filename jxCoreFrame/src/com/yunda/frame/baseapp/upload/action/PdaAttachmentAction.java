package com.yunda.frame.baseapp.upload.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PdaAttachmentAction，数据表：手持pda附件（照片）控制器
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PdaAttachmentAction extends JXBaseAction<Attachment, Attachment, AttachmentManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>说明： 手持pda附件（照片）上传
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-23
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void uploadImage() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		map.put(Constants.SUCCESS, false);
		OutputStream out = null;
		try {
			String tableName = req.getParameter("tableName");
			String photoBase64Str = req.getParameter("photoBase64Str");
			String extName = req.getParameter("extName");
			String saveDir = AttachmentManager.uploadDir(tableName);
			File folder = new File(saveDir);
			if (!folder.exists() && !folder.mkdirs()) {
				map.put(Constants.ERRMSG, String.format("文件上传目录创建失败！[%s]", saveDir));
				return;
			}
			// 保存在服务器上的文件对象实体,文件名称命名规则为：时间戳+后缀名，例如：“20160809152822244.jpg”
			File file = new File(folder, DateUtil.yyyyMMddHHmmssSSS.format(new Date()) + extName);
			out = new FileOutputStream(file);

			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			byte[] bytes = decoder.decodeBuffer(photoBase64Str);

			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
			out.write(bytes);

			map.put(Constants.SUCCESS, true);
			map.put("filePath", file.getAbsolutePath());
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(resp, map);
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * <li>说明： 手持pda附件（照片）下载
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-23
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void downloadImages() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String attachmentKeyIDX = req.getParameter("attachmentKeyIDX");
			List<Attachment> findImages = this.manager.findImages(attachmentKeyIDX);
			// key：图片在服务的实际存储路径（Attachment.realPath）
			// value：以Base64编码的图片字符串
			Map<String, String> images = new HashMap<String, String>();
			BASE64Encoder encoder = new BASE64Encoder();
			File file = null;
			FileInputStream fis = null;
			byte[] data = null;

			for (Attachment entity : findImages) {
				file = new File(entity.getRealPath());
				if (!file.exists()) {
					continue;
				}
				fis = new FileInputStream(file);
				data = new byte[fis.available()];
				try {
					fis.read(data);
				} catch (Exception e) {
					continue;
				} finally {
					if (null != fis) {
						fis.close();
					}
				}
				images.put(entity.getRealPath(), encoder.encode(data));
			}
			map.put(Constants.SUCCESS, true);
			map.put("iamges", images);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 手持pda附件（照片）删除
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-23
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void deleteImage() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String filePath = req.getParameter("filePath");
			Attachment entity = this.manager.getModelByRealPath(filePath);
			if (null != entity) {
				// 逻辑删除附件管理记录
				this.manager.logicDelete(entity.getIdx());
			}
			// 删除附件
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
