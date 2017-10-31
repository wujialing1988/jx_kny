package com.yunda.frame.baseapp.message.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: BPS流程参与者
 * <li>创建人：何涛
 * <li>创建日期：2015-3-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
public class ParticipantEntity {
    
    /** 参与者类型-组织机构 */
    public final static String PARTICIPANT_TYPE_ORGANIZATION = "organization";
    
    /** 参与者类型-岗位 */
    public final static String PARTICIPANT_TYPE_POSITION = "position";
    
    /** 参与者类型-工作组 */
    public final static String PARTICIPANT_TYPE_WORKGROUP = "workgroup";
    
    /** 参与者类型-职务 */
    public final static String PARTICIPANT_TYPE_DUTY = "duty";
    
    /** 参与者类型-个人 */
    public final static String PARTICIPANT_TYPE_PERSON = "person";
    
    /** 参与者类型-角色 */
    public final static String PARTICIPANT_TYPE_ROLE = "role";
    
    /** 参与者ID */
    @Id
    private String participantid;
    
    /** 参与者类型 */
    private String participanttype;
    
    /** 参与者名称 */
    private String participantname;
    
    /**
     * @return 获取参与者ID
     */
    public String getParticipantid() {
        return participantid;
    }
    
    /** 
     * @param participantid 设置参与者ID
     */
    public void setParticipantid(String participantid) {
        this.participantid = participantid;
    }
    
    /**
     * @return 获取参与者名称
     */
    public String getParticipantname() {
        return participantname;
    }
    
    /**
     * @param participantname 设置参与者名称
     */
    public void setParticipantname(String participantname) {
        this.participantname = participantname;
    }
    
    /**
     * @return 获取参与者类型
     */
    public String getParticipanttype() {
        return participanttype;
    }
    
    /**
     * @param participanttype 设置参与者类型
     */
    public void setParticipanttype(String participanttype) {
        this.participanttype = participanttype;
    }
    
}
