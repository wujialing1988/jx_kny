package com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 立体仓库_作业任务表
 * <li>创建人：
 * <li>创建日期：2016-5-10
 * <li>修改人: 张迪
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * 
 */
@Entity
@Table(name = "TASKINS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Taskins implements Serializable {
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 业务编码规则 - 作业任务号 */
    public static final String CODE_RULE_TASK_NO = "LTCK_TASKINS_TASK_NO";
    
    /** 指令类型 - 入库- 出库都为拣选出库  */
  
    public static final String CONST_STR_INS_TYPE = "拣选出库";
    
    /** 执行标志 - 未执行 */
    public static final String CONST_STR_EXEC_FLAG_WZX = "未执行";
    
    /** 执行标志 - 正在执行 */
    public static final String CONST_STR_EXEC_FLAG_ZX = "正在执行";
    
    /** 执行标志 - 已执行 */
    public static final String CONST_STR_EXEC_FLAG_YZX = "已执行";
    
    /** 执行标志 - 暂停执行 */
    public static final String CONST_STR_EXEC_FLAG_ZTZX = "暂停执行";
    
    /** 任务号 */
    @Id
    @Column(name = "TASKNO")
    private Integer taskNo;
    
    /** 货位编号 */
    @Column(name = "CARGONO")
    private String cargoNo;
    
    /** 指令类型 */
    @Column(name = "INSTYPE")
    private String insType;
    
    /** 执行标志 */
    @Column(name = "EXECFLAG")
    private String execFlag;
    /** 输送机号 */
    @Column(name = "DECKID")
    private String deckId;
    
    @Column(name = "MATERIALCODE")
    private String materialCode;
 
    @Column(name = "AMOUNT")
    private Long amount;
    /** 下发时间 */
    @Column(name = "INSCREATETIME")
    private String inscreateTime;
 
    @Column(name = "INSFININSHTIME")
    private String insfininshTime;
   
    private String priority;
    
    @Column(name = "PRODUCTNAME")
    private String productName;
    /** 巷道 */
    @Column(name = "LANEWAY")
    private String laneWay;
    
    @Column(name = "STOREADD")
    private String storeAdd;
    
    @Column(name = "TVID")
    private String tvId;

    private String zyms;
    
    @Column(name = "OPERRATIE_NAME")
    private String operratieName;
    
    @Column(name = "OPERRATIE_ID")
    private String operratieId;
 
    @Transient
    private String gat;
    @Transient
    private String row;
    @Transient
    private String column;
    @Transient
    private String level;
   

    /**
     * <li>说明：默认指定状态为未执行
     * <li>创建人：张迪
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     */
    public Taskins() {
        super();
    }
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
    public String getCargoNo() {
        return cargoNo;
    }
    
    public void setCargoNo(String cargoNo) {
        this.cargoNo = cargoNo;
    }
    
    public String getExecFlag() {
        return execFlag;
    }
    
    public void setExecFlag(String execFlag) {
        this.execFlag = execFlag;
    }
    
    public String getInsType() {
        return insType;
    }
    
    public void setInsType(String insType) {
        this.insType = insType;
    }
    
    public Integer getTaskNo() {
        return taskNo;
    }
    
    public void setTaskNo(Integer taskNo) {
        this.taskNo = taskNo;
    }
    
    public Long getAmount() {
        return amount;
    }
    
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    
    public String getDeckId() {
        return deckId;
    }
    
    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }
    
    public String getInscreateTime() {
        return inscreateTime;
    }
    
    public void setInscreateTime(String inscreateTime) {
        this.inscreateTime = inscreateTime;
    }
    
    public String getInsfininshTime() {
        return insfininshTime;
    }
    
    public void setInsfininshTime(String insfininshTime) {
        this.insfininshTime = insfininshTime;
    }
    
    public String getLaneWay() {
        return laneWay;
    }
    
    public void setLaneWay(String laneWay) {
        this.laneWay = laneWay;
    }
    
    public String getMaterialCode() {
        return materialCode;
    }
    
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    
    public String getOperratieId() {
        return operratieId;
    }
    
    public void setOperratieId(String operratieId) {
        this.operratieId = operratieId;
    }
    
    public String getOperratieName() {
        return operratieName;
    }
    
    public void setOperratieName(String operratieName) {
        this.operratieName = operratieName;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getStoreAdd() {
        return storeAdd;
    }
    
    public void setStoreAdd(String storeAdd) {
        this.storeAdd = storeAdd;
    }
    
    public String getTvId() {
        return tvId;
    }
    
    public void setTvId(String tvId) {
        this.tvId = tvId;
    }
    
    public String getZyms() {
        return zyms;
    }
    
    public void setZyms(String zyms) {
        this.zyms = zyms;
    }
    
    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
    }

    
    public String getColumn() {
        return column;
    }

    
    public void setColumn(String column) {
        this.column = column;
    }

    
    public String getGat() {
        return gat;
    }

    
    public void setGat(String gat) {
        this.gat = gat;
    }

    
    public String getLevel() {
        return level;
    }

    
    public void setLevel(String level) {
        this.level = level;
    }

    
    public String getRow() {
        return row;
    }

    
    public void setRow(String row) {
        this.row = row;
    }
    
}
