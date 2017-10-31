package com.yunda.webservice.fingerprint.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题:指纹识别
 * <li>说明：Fingerprint 实体类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-10 下午02:39:22
 * <li>修改人: 姚凯
 * <li>修改日期： 2016-4-19 下午3：58
 * <li>修改内容：调整了表结构  将原表的：userId  改为：operatorId  新添加字段：imageBase 图片码  去掉字段：updateTime
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "AC_OPERATOR_FINGERPRINT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Fingerprint implements Serializable {
    
    /** default serial id */
    private static final long serialVersionUID = 1L;
    
    /** 系统操作员ID */
    @Id
    @Column(name = "OPERATORID")
    private Long operatorId;
    
    /** 图片的base64码 */
    @Column(name = "IMAGEBASE")
    private String imageBase;
    
    /**
     * @return imageBase 图片码
     */
    public String getImageBase() {
        return imageBase;
    }
    
    /**
     * @return imageBase 图片码
     */
    public void setImageBase(String imageBase) {
        this.imageBase = imageBase;
    }
    
    /**
     * @return operatorId 操作员ID
     */
    public Long getOperatorId() {
        return operatorId;
    }
    
    /**
     * @return operatorId 操作员ID
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
    
}
