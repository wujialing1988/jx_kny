package com.yunda.jx.jxgc.processdef.entity;

import java.io.Serializable;

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
 * <li>说明: 作业节点所挂记录卡
 * <li>创建人：林欢
 * <li>创建日期：2016-6-3
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Entity
@Table(name = "JXGC_JobNode_Union_WorkSeq")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JobNodeUnionWorkSeq implements Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 节点主键 */
    @Column(name = "Node_IDX")
    private String nodeIDX;
    
    /* 检修记录卡主键 */
    @Column(name = "Record_Card_IDX")
    private String recordCardIDX;

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getNodeIDX() {
        return nodeIDX;
    }

    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }

    
    public String getRecordCardIDX() {
        return recordCardIDX;
    }

    
    public void setRecordCardIDX(String recordCardIDX) {
        this.recordCardIDX = recordCardIDX;
    }
    
    
}
