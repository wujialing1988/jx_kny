package com.yunda.jx.base.jcgy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 视图v_jczl_undertakeRc的实体类,承修修程
 * <li>创建人：程锐
 * <li>创建日期：2013-9-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="v_jczl_undertakeRc")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class UndertakeRc {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /* idx主键 */
    /* 修程编码 */
    @Id
    @Column(name="XC_ID")
    private String xcID;
    /* 修程名称 */
    @Column(name="XC_NAME")
    private String xcName;
    
    public String getXcID() {
        return xcID;
    }
    
    public void setXcID(String xcID) {
        this.xcID = xcID;
    }
    
    public String getXcName() {
        return xcName;
    }
    
    public void setXcName(String xcName) {
        this.xcName = xcName;
    }
    
}
