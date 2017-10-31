package com.yunda.jx.jcll.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车履历实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "jxgc_train_record_view")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainRecord implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @Column(name = "IDX")
    private String idx;
    
    /* 车型主键 */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIDX;
    
    /* 车型 */
    @Column(name = "TRAIN_TYPE_SHORTNAME")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;
    
   
 
    public String getTrainNo() {
        return trainNo;
    }

    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }

    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }

    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }

    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }


    
    public String getIdx() {
        return idx;
    }


    
    public void setIdx(String idx) {
        this.idx = idx;
    }


    
    public String getVehicleType() {
        return vehicleType;
    }


    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    

   
}
