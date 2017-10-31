package com.yunda.frame.baseapp.upload.manager;

import java.util.List;

import com.yunda.frame.baseapp.upload.entity.Attachment;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: IAttachmentManager，附件管理照片查询接口
 * <li>创建人：何涛
 * <li>创建日期：2017年4月24日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public interface IAttachmentManager {

	/**
	 * <li>说明：根据业务记录idx主键，获取该业务已关联（包含直接关联和间接关联）的照片附件信息对象集合
	 * <li>创建人：何涛
	 * <li>创建日期：2017年4月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param idx 业务记录idx主键
	 * @return 该业务已关联（包含直接关联和间接关联）的照片附件信息对象集合
	 */
	public List<Attachment> findImages(String idx);

}
