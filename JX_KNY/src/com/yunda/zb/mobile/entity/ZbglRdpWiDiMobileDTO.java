package com.yunda.zb.mobile.entity;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：pda整备数据项返回对象
 * <li>创建人：林欢
 * <li>创建日期：2016-07-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
public class ZbglRdpWiDiMobileDTO {
    private String diCode;//数据项编号
    private String diName;//数据项名称
    private String diStandard;//数据项标准
    private String isBlank;//是否必填，是；否
    private String seqNo;//顺序号
    private String diResult;//数据项结果
    private String zbglRdpWiDiIDX;//数据项主键
    
    public String getDiCode() {
        return diCode;
    }
    
    public void setDiCode(String diCode) {
        this.diCode = diCode;
    }
    
    public String getDiName() {
        return diName;
    }
    
    public void setDiName(String diName) {
        this.diName = diName;
    }
    
    public String getDiResult() {
        return diResult;
    }
    
    public void setDiResult(String diResult) {
        this.diResult = diResult;
    }
    
    public String getDiStandard() {
        return diStandard;
    }
    
    public void setDiStandard(String diStandard) {
        this.diStandard = diStandard;
    }
    
    public String getIsBlank() {
        return isBlank;
    }
    
    public void setIsBlank(String isBlank) {
        this.isBlank = isBlank;
    }
    
    public String getSeqNo() {
        return seqNo;
    }
    
    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }
    
    public String getZbglRdpWiDiIDX() {
        return zbglRdpWiDiIDX;
    }
    
    public void setZbglRdpWiDiIDX(String zbglRdpWiDiIDX) {
        this.zbglRdpWiDiIDX = zbglRdpWiDiIDX;
    }
    
    
}
