package com.yunda.zb.zbfw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbglJobProcessNodeRelDef实体 数据表：整备作业流程节点前后置关系
 * <li>创建人：何涛
 * <li>创建日期：2016年4月7日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_Job_ProcessNode_RelDef")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglJobProcessNodeRelDef implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 节点主键 */
	@Column(name="Node_IDX")
	private String nodeIDX;
	/* 前置节点主键 */
	@Column(name="Pre_Node_IDX")
	private String preNodeIDX;
    /* 前置节点名称 */
    @Transient
    private String preNodeName;
    /* 前置节点顺序 */
    @Transient
    private Integer preSeqNo;
     
    public String getPreNodeName() {
        return preNodeName;
    }

    
    public void setPreNodeName(String preNodeName) {
        this.preNodeName = preNodeName;
    }

    
    public Integer getPreSeqNo() {
        return preSeqNo;
    }

    
    public void setPreSeqNo(Integer preSeqNo) {
        this.preSeqNo = preSeqNo;
    }

    /**
     * <li>说明：构造方法
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 主键
     * @param nodeIDX 作业节点主键
     * @param preNodeIDX 上级作业节点主键
     * @param preNodeName 上级作业节点名称
     * @param preSeqNo 前置节点序号
     */
    public ZbglJobProcessNodeRelDef(String idx, String nodeIDX, String preNodeIDX, String preNodeName, Integer preSeqNo) {
        super();
        this.idx = idx;
        this.nodeIDX = nodeIDX;
        this.preNodeIDX = preNodeIDX;
        this.preNodeName = preNodeName;
        this.preSeqNo = preSeqNo;
    }
    
    /**
     * <li>default constructor
     */
    public ZbglJobProcessNodeRelDef() {
        super();
    }
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
    public String getPreNodeIDX() {
        return preNodeIDX;
    }
    
    public void setPreNodeIDX(String preNodeIDX) {
        this.preNodeIDX = preNodeIDX;
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
    
}