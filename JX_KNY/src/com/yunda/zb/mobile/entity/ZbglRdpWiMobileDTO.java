package com.yunda.zb.mobile.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：pda整备任务单返回对象
 * <li>创建人：林欢
 * <li>创建日期：2016-07-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpWiMobileDTO {
    
    /* 整备任务单idx */
    @Id
    @Column(name = "ZBGL_RDP_WI_IDX")
    private String zbglRdpWiIDX;
    
    /* 整备任务单名称 */
    @Column(name = "WI_NAME")
    private String wiName;
    
    /* 作业情况 */
    @Column(name = "WI_DESC")
    private String wiDesc;//
    
    /* 联合作业人员 */
    @Column(name = "WORKER")
    private String worker;
    
    /* 节点名称 */
    @Column(name = "NODE_NAME")
    private String nodeName ;
    
    /* 任务状态 */
    @Column(name = "WI_STATUS")
    private String wiStatus ;
    
    /* 是否照相核实，1：需要核实；0：不需要核实； */
    @Column(name="isCheckPicture")
    private Integer isCheckPicture;
    
    /* 数据项List */
    @Transient
    private List<ZbglRdpWiDiMobileDTO> zbglRdpWiDiList;
    
    public String getWiDesc() {
        return wiDesc;
    }
    
    public void setWiDesc(String wiDesc) {
        this.wiDesc = wiDesc;
    }
    
    public String getWiName() {
        return wiName;
    }
    
    public void setWiName(String wiName) {
        this.wiName = wiName;
    }
    
    public String getWorker() {
        return worker;
    }
    
    public void setWorker(String worker) {
        this.worker = worker;
    }
    
    public List<ZbglRdpWiDiMobileDTO> getZbglRdpWiDiList() {
        if(zbglRdpWiDiList == null){
            zbglRdpWiDiList = new ArrayList<ZbglRdpWiDiMobileDTO>();
        }
        return zbglRdpWiDiList;
    }
    
    public void setZbglRdpWiDiList(List<ZbglRdpWiDiMobileDTO> zbglRdpWiDiList) {
        this.zbglRdpWiDiList = zbglRdpWiDiList;
    }
    
    public String getZbglRdpWiIDX() {
        return zbglRdpWiIDX;
    }
    
    public void setZbglRdpWiIDX(String zbglRdpWiIDX) {
        this.zbglRdpWiIDX = zbglRdpWiIDX;
    }
    
    public String getNodeName() {
        return nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

	public String getWiStatus() {
		return wiStatus;
	}

	public void setWiStatus(String wiStatus) {
		this.wiStatus = wiStatus;
	}

    
    public Integer getIsCheckPicture() {
        return isCheckPicture;
    }
    
    public void setIsCheckPicture(Integer isCheckPicture) {
        this.isCheckPicture = isCheckPicture;
    }
    
}
