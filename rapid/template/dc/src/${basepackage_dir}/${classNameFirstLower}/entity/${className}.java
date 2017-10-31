<#assign className = table.className>
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${classNameLower}.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: ${table.tableAlias}实体
 * <li>创建人：${authorName}
 * <li>创建日期：${now?string('yyyy-MM-dd HH:mm:ss')}
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "${table.sqlName}")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ${className} implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    <@generateFields/>
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    <@generateNotPkProperties/>
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    
}

<#macro generateFields>
    <#list table.columns as column>
        <#if !column.pk 
        && column.sqlName != 'RECORD_STATUS' 
        && column.sqlName != 'CREATOR' 
        && column.sqlName != 'CREATE_TIME'
        && column.sqlName != 'UPDATOR'
        && column.sqlName != 'UPDATE_TIME'
                >
            <#if '${column.javaType}' == 'java.util.Date'>
    /* ${column.columnAlias} */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "${column.sqlName}")
    private ${column.javaType} ${column.columnNameLower};
            <#else>
            
    /* ${column.columnAlias} */ 
    @Column(name = "${column.sqlName}")
    private ${column.javaType} ${column.columnNameLower};            
            </#if>
        </#if>
    </#list>
</#macro>


<#macro generateNotPkProperties>
    <#list table.columns as column>
    <#if !column.pk 
    && column.sqlName != 'RECORD_STATUS' 
    && column.sqlName != 'CREATOR' 
    && column.sqlName != 'CREATE_TIME'
    && column.sqlName != 'UPDATOR'
    && column.sqlName != 'UPDATE_TIME'
            >
    public ${column.javaType} get${column.columnName}() {
        return this.${column.columnNameLower};
    }
    
    public void set${column.columnName}(${column.javaType} value) {
        this.${column.columnNameLower} = value;
    }
    </#if>
    </#list>
</#macro>
