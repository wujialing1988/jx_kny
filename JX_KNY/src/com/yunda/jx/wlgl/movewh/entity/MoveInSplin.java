package com.yunda.jx.wlgl.movewh.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 封装拆分入库库位信息
 * <li>创建人：张迪
 * <li>创建日期：2016-5-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MoveInSplin implements java.io.Serializable{
	/* 使用默认序列版本ID */
	/**  类型：long  */
	private static final long serialVersionUID = 1L;	
    /* idx主键 */
    private String idx;  
    /* 库位主键 */
    private String locationIdx;
    /* 库位名称 */
    private String locationName;   
    /* 移库数量 */
    private Integer qty;
    /* 出库库位状态 */
    private String status ;   
    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getLocationIdx() {
        return locationIdx;
    }

    
    public void setLocationIdx(String locationIdx) {
        this.locationIdx = locationIdx;
    }

    
    public String getLocationName() {
        return locationName;
    }

    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    } 
   
   
}