package com.yunda.jx.base.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <li>标题：查询条件组装结果
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-4-2
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
public class QueryConditionResult {
    /*
     * 返回的where条件
     */
    private String whereHql;
    /*
     * 参数集合 
     */
    private Object[] params;
    
    public String getWhereHql() {
        return whereHql;
    }
    
    public void setWhereHql(String whereHql) {
        this.whereHql = whereHql;
    }
    
    public Object[] getParams() {
        return params;
    }
    
    public void setParams(Object[] params) {
        this.params = params;
    }
    
    /**
     * <li>方法说明：获取HQL WHERE组装条件 
     * <li>方法名称：getQueryCondition
     * <li>@param conditions
     * <li>@return
     * <li>return: QueryConditionResult
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 上午09:49:10
     * <li>修改人：
     * <li>修改内容：
     */
    public static QueryConditionResult getQueryCondition(Map<String, QueryCondition> conditions){
        StringBuilder sb = new StringBuilder();
        List<String> params = new ArrayList<String>();
        
        if(conditions != null){
            java.util.Set<String> set = conditions.keySet();
            for(String key : set){
                sb.append(getSplice(key, conditions.get(key), params));
            }
        }        
        QueryConditionResult result = new QueryConditionResult();
        result.setParams(params.toArray());
        result.setWhereHql(sb.toString());
        return result;
    }
    /**
     * <li>方法说明：获取拼接HQL 
     * <li>方法名称：getSplice
     * <li>@param field
     * <li>@param cdn
     * <li>@param params
     * <li>@return
     * <li>return: StringBuilder
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 上午09:49:45
     * <li>修改人：
     * <li>修改内容：
     */
    private static StringBuilder getSplice(String field, QueryCondition cdn,  List<String> params){
        StringBuilder sb = new StringBuilder();
        if(cdn.getOr() != null){
            if(cdn.getToken() == QueryCondition.ANDOR){
                cdn.setToken(QueryCondition.ANDOR);
                AndOrAppend(cdn, params, sb);
            }else{
                OrAndAppend(cdn, params, sb);
            }
            
        }else if(cdn.getValues() != null){
            InAppend(field, cdn, params, sb);
        }else{
            EqualAppend(field, cdn, params, sb);
        }
        return sb;
    }

    /**
     * <li>方法说明：AND (condition OR condition)形式的拼接 
     * <li>方法名称：AndOrAppend
     * <li>@param cdn
     * <li>@param params
     * <li>@param sb
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 上午09:50:01
     * <li>修改人：
     * <li>修改内容：
     */
    private static void AndOrAppend(QueryCondition cdn, List<String> params, StringBuilder sb) {
        sb.append(" and ( ");
        StringBuilder con = new StringBuilder();
        for(QueryCondition q : cdn.getOr()){
            q.setToken(QueryCondition.ANDOR);
            con.append(getSplice(null, q, params));
        }
        con.delete(0, 4);
        sb.append(con).append(" ) ");
    }

    /**
     * <li>方法说明： OR (condition AND condition)形式的拼接 
     * <li>方法名称：OrAndAppend
     * <li>@param cdn
     * <li>@param params
     * <li>@param sb
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 上午09:50:28
     * <li>修改人：
     * <li>修改内容：
     */
    private static void OrAndAppend(QueryCondition cdn, List<String> params, StringBuilder sb) {
        sb.append(" or ( ");
        StringBuilder con = new StringBuilder();
        for(QueryCondition q : cdn.getOr()){
           con.append(getSplice(null, q, params));
        }
        con.delete(0, 5);
        sb.append(con).append(" ) ");
    }
    
    /**
     * <li>方法说明：IN (val1, val2)形式的拼接  
     * <li>方法名称：InAppend
     * <li>@param field
     * <li>@param cdn
     * <li>@param params
     * <li>@param sb
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 上午09:50:56
     * <li>修改人：
     * <li>修改内容：
     */
    private static void InAppend(String field, QueryCondition cdn, List<String> params, StringBuilder sb) {
        sb.append(" and ").append(field).append(cdn.getSymbol()).append(" (");
        for(String val : cdn.getValues()){
            sb.append("?,");
            params.add(val);
        }
        sb.deleteCharAt(sb.length() -1).append(")");
    }
    /**
     * <li>方法说明：field = value形式的拼接  
     * <li>方法名称：EqualAppend
     * <li>@param field
     * <li>@param cdn
     * <li>@param params
     * <li>@param sb
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 上午09:51:12
     * <li>修改人：
     * <li>修改内容：
     */
    private static void EqualAppend(String field, QueryCondition cdn, List<String> params, StringBuilder sb) {
        if(field == null){
            field = cdn.getField();
        }
        if(cdn.getToken() == QueryCondition.ANDOR){
            sb.append(" or ");
        }else{
            sb.append(" and ");
        }
        sb.append(field).append(cdn.getSymbol()).append("?");
        params.add(cdn.getValue());
    }
    
    /*public static void main(String[] args) {        
        
        java.util.Map<String, QueryCondition> map = new java.util.HashMap<String, QueryCondition>();
        map.put("xfire", new QueryCondition("java"));
        QueryCondition[] cdns = new QueryCondition[]{new QueryCondition("csharp", "ADO.NET"), new QueryCondition("csharp", "WPF")};
        map.put("x", new QueryCondition(cdns));
        map.put("baby", new QueryCondition(0, "清子","杨颖"));
        
        QueryCondition[] cdns1 = new QueryCondition[]{new QueryCondition("paython", "ux"), new QueryCondition("basic", "BX")};
        map.put("x1", new QueryCondition(cdns1, 2));
        
        QueryConditionResult r = QueryConditionResult.getQueryCondition(map);
        System.out.println(r.whereHql);
        for(Object o : r.params){
            System.out.println(o);
        }
    }*/
}
