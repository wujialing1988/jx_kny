package com.yunda.jx.pjjx.base.recorddefine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件检测检修项结果项维护
 * <li>创建人：林欢
 * <li>创建日期：2016-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@Table(name = "PJJX_Parts_Step_Result")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsStepResult {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /** 是否默认的处理结果 是 */
    public static final Integer ISDEFAULT_YES = 1;
    /** 是否默认的处理结果 否 */
    public static final Integer ISDEFAULT_NO = 0;
    /** idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    /** 检修检测项主键 */
    @Column(name="Record_RI_IDX")
    private String recordRIIDX;
    /** 处理结果名称 */
    @Column(name="Result_Name")
    private String resultName;
    /** 是否默认的处理结果，1：是；0：否 */
    @Column(name="Is_Default")
    private Integer isDefault;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Integer getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
    
    public String getResultName() {
        return resultName;
    }
    
    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    
    public String getRecordRIIDX() {
        return recordRIIDX;
    }

    
    public void setRecordRIIDX(String recordRIIDX) {
        this.recordRIIDX = recordRIIDX;
    }
    
    //构造方法
    public PartsStepResult(String resultName){
        this.resultName = resultName;
    }
    
//  构造方法
    public PartsStepResult(){
    }
    
}
