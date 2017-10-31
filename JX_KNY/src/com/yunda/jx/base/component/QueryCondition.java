package com.yunda.jx.base.component;

/**
 * <li>标题：查询条件组件类
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-4-2
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
public class QueryCondition {
    /**
     * 条件为IN
     */
    public static final int IN = 0;
    /**
     * 条件为等于
     */
    public static final int EQ = 1;
    /**
     * 条件为and ( 1 or 2)
     */
    public static final int ANDOR = 2;
    /*
     * in的值
     */
    private String[] values;
    /*
     * 等于的值
     */
    private String value;
    /*
     * （多个条件）
     */
    private QueryCondition[] or;
    /*
     * 字段名称
     */
    private String field;
    /*
     * 条件比较符号（如 = , in）
     */
    private int token;
    //get set
    public String[] getValues() {
        return values;
    }
    
    public void setValues(String[] values) {
        this.values = values;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public QueryCondition[] getOr() {
        return or;
    }
    
    public void setOr(QueryCondition[] or) {
        this.or = or;
    }
    
    public String getField() {
        return field;
    }
    
    public void setField(String field) {
        this.field = field;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
    //construct
    /**
     * 默认
     */
    public QueryCondition(){}
    /**
     * 匹配 and map_key = value
     * @param value
     */
    public QueryCondition(String value){
        this.token = EQ;
        this.value = value;
    }
    /**
     * 匹配 and field = value
     * @param field
     * @param value
     */
    public QueryCondition(String field, String value){
        this.token = EQ;
        this.field = field;
        this.value = value;
    }
    
    /**
     * 匹配 and (condition or condition)
     * 或 or (condition and condition)
     * @param or
     * @param andOr
     */
    public QueryCondition(QueryCondition[] or, int... andOr){
        this.or = or;
        if(andOr.length >0){
            if(andOr[0] == ANDOR){
                this.token = ANDOR;
            }
        }
    }
    /**
     * 匹配 and map_key in(val1, val2)
     * 或 and map_key = val
     * @param token
     * @param values
     */
    public QueryCondition(int token, String... values){
        if(values.length == 0){
            throw new IllegalArgumentException();
        }
        if(token == IN){
            this.values = values;
        }else{
            this.value = values[0];
        }
        this.token = token;
    }
    //util
    /**
     * 获取条件比较符号
     */
    public String getSymbol(){
        if(this.token == IN){
            return " in ";
        }else if(this.token == EQ || this.token == ANDOR){
            return " = ";
        }
        return "";
    }
}
