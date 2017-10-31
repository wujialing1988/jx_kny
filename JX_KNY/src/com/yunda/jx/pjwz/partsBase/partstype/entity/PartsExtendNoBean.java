package com.yunda.jx.pjwz.partsBase.partstype.entity;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件扩展编号json实体
 * <li>创建人：程锐
 * <li>创建日期：2014-8-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PartsExtendNoBean implements java.io.Serializable {
    
    /** 对应扩展编号字段 */
    private String extendNoField;
    
    /** 扩展编号名称 */
    private String extendNoName;
    
    /** 扩展编号值 */
    private String value;
    
    /**
     * @return 获取对应扩展编号字段
     */
    public String getExtendNoField() {
        return extendNoField;
    }
    
    /**
     * @param extendNoField 设置对应扩展编号字段
     */
    public void setExtendNoField(String extendNoField) {
        this.extendNoField = extendNoField;
    }
    
    /**
     * @return 获取扩展编号名称
     */
    public String getExtendNoName() {
        return extendNoName;
    }
    
    /**
     * @param extendNoName 设置扩展编号名称
     */
    public void setExtendNoName(String extendNoName) {
        this.extendNoName = extendNoName;
    }
    
    /**
     * @return 获取扩展编号值
     */
    public String getValue() {
        return value;
    }
    
    /**
     * @param value 设置扩展编号值
     */
    public void setValue(String value) {
        this.value = value;
    }
    
}
