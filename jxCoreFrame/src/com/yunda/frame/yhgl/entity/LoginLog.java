package com.yunda.frame.yhgl.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：LoginLog实体类, 数据表：登录日志
 * <li>创建人：程梅
 * <li>创建日期：2016-4-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="login_log")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class LoginLog implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /*登录方式(指纹识别、刷卡识别、人脸识别、手工登录)*/
    public static final String TYPE_ZWSB = "指纹识别";
    public static final String TYPE_SKSB = "刷卡识别";
    public static final String TYPE_RLSB = "人脸识别";
    public static final String TYPE_SGDL = "手工登录";
    /*登录客户端(工位终端、PDA、PAD、web)*/
    public static final String CLIENT_GWZD = "工位终端";
    public static final String CLIENT_PDA = "PDA";
    public static final String CLIENT_PAD = "PAD";
    public static final String CLIENT_WEB = "WEB";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* ip地址 */
	private String ip;
	/* 操作员id */
	private String operatoridx;
	/* 登录方式 */
	@Column(name="login_type")
	private String loginType;
    /* 登录客户端 */
    @Column(name="login_client")
    private String loginClient;
    /* 登录位置 */
    @Column(name="login_location")
    private String loginLocation;
    /* 操作员姓名 */
    private String operatorname;
    /* 用户名 */
    private String userid;
	/* 登入时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="login_inTime",updatable=false)
	private java.util.Date loginInTime;
	/* 登出时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="login_outTime")
	private java.util.Date loginOutTime;

	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public java.util.Date getLoginInTime() {
        return loginInTime;
    }
    
    public void setLoginInTime(java.util.Date loginInTime) {
        this.loginInTime = loginInTime;
    }
    
    public java.util.Date getLoginOutTime() {
        return loginOutTime;
    }
    
    public void setLoginOutTime(java.util.Date loginOutTime) {
        this.loginOutTime = loginOutTime;
    }
    
    public String getLoginClient() {
        return loginClient;
    }
    
    public void setLoginClient(String loginClient) {
        this.loginClient = loginClient;
    }
    
    public String getLoginLocation() {
        return loginLocation;
    }
    
    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }
    
    public String getLoginType() {
        return loginType;
    }
    
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    
    public String getOperatoridx() {
        return operatoridx;
    }
    
    public void setOperatoridx(String operatoridx) {
        this.operatoridx = operatoridx;
    }
    
    public String getOperatorname() {
        return operatorname;
    }
    
    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }
    
    public String getUserid() {
        return userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
}