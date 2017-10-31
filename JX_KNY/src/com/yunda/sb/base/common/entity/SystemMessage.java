package com.yunda.sb.base.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.base.combo.ILogicDelete;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明： T_SYSTEM_MESSAGE，数据表：系统消息
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "T_SYSTEM_MESSAGE")
public class SystemMessage implements Serializable, ILogicDelete {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/**
	 * <li>标题: 设备管理信息系统
	 * <li>说明: 业务消息枚举，用于生成一些仅有消息内容的简单推送消息对象
	 * <li>创建人：何涛
	 * <li>创建日期：2017年4月12日
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * @author 信息系统事业部设备管理系统项目组
	 * @version 3.0.1
	 */
	public static enum BusinessMsg {

		JXJHXF("检修计划下发");

		/** 消息内容 */
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		/**
		 * <li>说明：构造方法
		 * <li>创建人：何涛
		 * <li>创建日期：2017年4月12日
		 * <li>修改人： 
		 * <li>修改内容：
		 * <li>修改日期：	 
		 * @param content 消息内容
		 */
		private BusinessMsg(String content) {
			this.content = content;
		}

		/**
		 * <li>说明：根据枚举常量初始化系统消息实体对象
		 * <li>创建人：何涛
		 * <li>创建日期：2017年4月12日
		 * <li>修改人：
		 * <li>修改内容：
		 * <li>修改日期：
		 * @return 系统消息实体对象
		 */
		public SystemMessage init() {
			return new SystemMessage().content(this.content);
		}
	}

	/** 权限验证模式 - 无需权限验证 */
	public static final int AUTH_VERIFY_MODE_NONE = 0x00000000;

	/** 权限验证模式 - 验证所有权限 */
	public static final int AUTH_VERIFY_MODE_ALL = 0x11111111;

	/** 权限验证模式 - 低1位为1表示需要验证菜单权限 */
	public static final int AUTH_VERIFY_MODE_MENUSEQ = 0x00000001;

	/** 权限验证模式 - 低2位为1表示需要验证班组权限 */
	public static final int AUTH_VERIFY_MODE_ORGID = 0x00000010;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 消息标题（对应用户权限菜单的菜单名称），如：提票调度派工 */
	private String title;

	/** 消息内容 */
	private String content;

	/** 业务idx主键 */
	@Column(name = "BUSINESS_IDX")
	private String businessIdx;

	/** 设备idx主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 设备编码 */
	@Column(name = "EQUIPMENT_CODE")
	private String equipmentCode;

	/** 设备名称 */
	@Column(name = "EQUIPMENT_NAME")
	private String equipmentName;

	/** 功能菜单id  */
	@Column(name = "MENU_ID")
	private String menuId;

	/** 系统菜单序列 */
	@Column(name = "MENU_SEQ")
	private String menuSeq;

	/** 功能菜单URL */
	private String url;

	/** 消息推送次数 */
	@Column(name = "PUSH_TIMES")
	private Integer pushTimes;

	/** 接收班组id，如果有多个班组，则使用英文逗号（,）进行分隔，如果该字段不为空，则只向该字段包含的班组推送消息 */
	@Column(name = "RECEIVE_ORG_IDS")
	private String receiveOrgIds;

	/** 权限验证模式，0x00000011，低1位为1表示需要验证菜单权限，低2位为1表示需要验证班组权限，如果为空则表示不需要权限验证（即：该消息应推送给所有系统在线人员）*/
	@Column(name = "AUTH_VERIFY_MODE")
	private Integer authVerifyMode;

	/** 记录状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/** 消息创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/** 设备故障提票业务相关临时字段 - 开始 */
	/** 故障部位及意见 */
	@Transient
	private String faultPlace;

	/** 故障现象 */
	@Transient
	private String faultPhenomenon;

	/** 设置地点(来源码表) */
	@Transient
	private String usePlace;

	/**
	 * <li>说明：从给定的对象获取相应的属性值
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月25日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 给定的系统消息实体对象
	 */
	public void adaptFrom(SystemMessage t) {
		this.equipmentIdx = t.getEquipmentIdx();
		this.equipmentCode = t.getEquipmentCode();
		this.equipmentName = t.getEquipmentName();
		this.usePlace = t.getUsePlace();
		this.faultPhenomenon = t.getFaultPhenomenon();
		this.faultPlace = t.getFaultPlace();
	}

	public String getFaultPlace() {
		return faultPlace;
	}

	public void setFaultPlace(String faultPlace) {
		this.faultPlace = faultPlace;
	}

	public String getFaultPhenomenon() {
		return faultPhenomenon;
	}

	public void setFaultPhenomenon(String faultPhenomenon) {
		this.faultPhenomenon = faultPhenomenon;
	}

	public String getUsePlace() {
		return usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
	}

	/** 设备故障提票业务相关临时字段 - 结束 */

	/**
	 * <li>说明：设置消息标题（对应用户权限菜单的菜单名称），如：提票调度派工
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param title 消息标题（对应用户权限菜单的菜单名称），如：提票调度派工
	 * @return 系统消息实体对象
	 */
	public SystemMessage title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * <li>说明：设置消息内容
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param content 消息内容
	 * @return 系统消息实体对象
	 */
	public SystemMessage content(String content) {
		this.content = content;
		return this;
	}

	/**
	 * <li>说明：设置业务idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param businessIdx 业务idx主键
	 * @return 系统消息实体对象
	 */
	public SystemMessage businessIdx(String businessIdx) {
		this.businessIdx = businessIdx;
		return this;
	}

	/**
	 * <li>说明：设置设备idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备idx主键
	 * @return 系统消息实体对象
	 */
	public SystemMessage equipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
		return this;
	}

	/**
	 * <li>说明：设置设备编码
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentCode 设备编码
	 * @return 系统消息实体对象
	 */
	public SystemMessage equipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
		return this;
	}

	/**
	 * <li>说明：设置设备名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentName 设备名称
	 * @return 系统消息实体对象
	 */
	public SystemMessage equipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
		return this;
	}

	/**
	 * <li>说明：设置功能菜单id
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param menuId 功能菜单id
	 * @return 系统消息实体对象
	 */
	public SystemMessage menuId(String menuId) {
		this.menuId = menuId;
		return this;
	}

	/**
	 * <li>说明：设置系统菜单序列
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param menuSeq 系统菜单序列
	 * @return 系统消息实体对象
	 */
	public SystemMessage menuSeq(String menuSeq) {
		this.menuSeq = menuSeq;
		return this;
	}

	/**
	 * <li>说明：设置功能菜单URL
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param url 功能菜单URL
	 * @return 系统消息实体对象
	 */
	public SystemMessage url(String url) {
		this.url = url;
		return this;
	}

	/**
	 * <li>说明：设置消息推送次数
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param pushTimes 消息推送次数
	 * @return 系统消息实体对象
	 */
	public SystemMessage pushTimes(Integer pushTimes) {
		this.pushTimes = pushTimes;
		return this;
	}

	/**
	 * <li>说明：设置接收班组id，如果有多个班组，则使用英文逗号（,）进行分隔，如果该字段不为空，则只向该字段包含的班组推送消息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param receiveOrgIds 接收班组id，如果有多个班组，则使用英文逗号（,）进行分隔，如果该字段不为空，则只向该字段包含的班组推送消息
	 * @return 系统消息实体对象
	 */
	public SystemMessage receiveOrgIds(String receiveOrgIds) {
		this.receiveOrgIds = receiveOrgIds;
		return this;
	}

	/**
	 * <li>说明：设置权限验证模式，0x00000011，低1位为1表示需要验证菜单权限，低2位为1表示需要验证班组权限，如果为空则表示不需要权限验证（即：该消息应推送给所有系统在线人员）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param authVerifyMode 权限验证模式，0x00000011，低1位为1表示需要验证菜单权限，低2位为1表示需要验证班组权限，如果为空则表示不需要权限验证（即：该消息应推送给所有系统在线人员）
	 * @return 系统消息实体对象
	 */
	public SystemMessage authVerifyMode(int authVerifyMode) {
		this.authVerifyMode = authVerifyMode;
		return this;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuSeq() {
		return menuSeq;
	}

	public void setMenuSeq(String menuSeq) {
		this.menuSeq = menuSeq;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPushTimes() {
		return pushTimes;
	}

	public void setPushTimes(Integer pushTimes) {
		this.pushTimes = pushTimes;
	}

	public String getBusinessIdx() {
		return businessIdx;
	}

	public void setBusinessIdx(String businessIdx) {
		this.businessIdx = businessIdx;
	}

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getReceiveOrgIds() {
		return receiveOrgIds;
	}

	public void setReceiveOrgIds(String receiveOrgIds) {
		this.receiveOrgIds = receiveOrgIds;
	}

	public Integer getAuthVerifyMode() {
		return authVerifyMode;
	}

	public void setAuthVerifyMode(Integer authVerifyMode) {
		this.authVerifyMode = authVerifyMode;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
