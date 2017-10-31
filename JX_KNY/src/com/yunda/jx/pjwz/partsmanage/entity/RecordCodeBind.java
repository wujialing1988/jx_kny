package com.yunda.jx.pjwz.partsmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RecordCodeBind实体类, 数据表：配件检修记录单识别码绑定
 * <li>创建人：程梅
 * <li>创建日期：2016-01-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJJX_RecordCode_Bind")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class RecordCodeBind implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    /* 配件信息主键 */
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIdx;
    /* 配件记录单识别码 */
    @Column(name = "Record_CODE")
    private String recordCode;

    /**
     * @return String 获取配件信息主键
     */
    public String getPartsAccountIdx() {
        return partsAccountIdx;
    }
    
    /**
     * @param partsAccountIdx 设置配件信息主键
     */
    public void setPartsAccountIdx(String partsAccountIdx) {
        this.partsAccountIdx = partsAccountIdx;
    }
    
    
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

    
    public String getRecordCode() {
        return recordCode;
    }

    
    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }
}
