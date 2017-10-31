package com.yunda.zb.mobile.manager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.zb.mobile.entity.AttachmentRelation;





/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：Attachment_Realtion 附件与所属业务和业务节点的关系 业务类
 * <li>创建人：刘国栋
 * <li>创建日期：2015-8-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */

@Service(value="attachmentRelationManager")
public class AttachmentRelationManager extends JXBaseManager<AttachmentRelation, AttachmentRelation>{

	/**
	 * <li>说明：新增附件关系记录
	 * <li>创建人：刘国栋
	 * <li>创建日期：2016-8-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param AttachmentRelation attRela：附件关系记录对象
	 * @return AttachmentRelation 新增保存成功的附件关系记录
	 * @throws 
	 */
	@Transactional
	public void add(AttachmentRelation attRela) {
		this.daoUtils.getHibernateTemplate().save(attRela);
	}
}
