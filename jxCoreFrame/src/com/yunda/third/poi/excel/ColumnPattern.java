package com.yunda.third.poi.excel;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>说明：Excel列属性
 * <li>创建人： 张凡
 * <li>创建日期：2015年3月16日
 * <li>成都运达科技股份有限公司
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ColumnPattern {
    /*
     * 列头名称
     */
    private String header;
    /*
     * 数据索引 
     */
    private String dataIndex;
    /**
     * 
     */
    private String datePattern;
    private String[] translate;
    private String xconvert;
    
    public String getHeader() {
        return header;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }
    
    public String getDatePattern() {
        return datePattern;
    }
    
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }
    
    public String[] getTranslate() {
        return translate;
    }
    
    public void setTranslate(String[] translate) {
        this.translate = translate;
    }

    
    public String getDataIndex() {
        return dataIndex;
    }

    
    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    
    public String getXconvert() {
        return xconvert;
    }

    
    public void setXconvert(String convert) {
        this.xconvert = convert;
    }
    
    public ColumnPattern() {
        super();
    }

    public ColumnPattern(String header,String dataIndex) {
        super();
        this.header = header;
        this.dataIndex = dataIndex;
    }
    
    
}
