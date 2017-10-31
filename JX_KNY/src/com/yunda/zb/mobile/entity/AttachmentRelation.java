package com.yunda.zb.mobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;





/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：Attachment_Realtion 附件与所属业务和业务节点的关系 实体类, 数据表：附件关系
 * <li>创建人：刘国栋
 * <li>创建日期：2015-8-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXPZ_Attachment_Relation")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)

public class AttachmentRelation {

	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	
	/* 上传的附件主键 */
	@Column(name="ATTACHMENT_IDX")
	private String attachmentIDX;
	
	/* 附件所属业务的节点 */
	@Column(name="ATTACHMENT_NODE")
	private String attachmentNode;
	
	/* 附件所属业务 */
	@Column(name="ATTACHMENT_KEY_IDX")
	private String attachmentKeyIDX;
	

	public String getAttachmentIDX() {
		return attachmentIDX;
	}

	public void setAttachmentIDX(String attachmentIDX) {
		this.attachmentIDX = attachmentIDX;
	}

	public String getAttachmentKeyIDX() {
		return attachmentKeyIDX;
	}

	public void setAttachmentKeyIDX(String attachmentKeyIDX) {
		this.attachmentKeyIDX = attachmentKeyIDX;
	}

	public String getAttachmentNode() {
		return attachmentNode;
	}

	public void setAttachmentNode(String attachmentNode) {
		this.attachmentNode = attachmentNode;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}
	
	
}
