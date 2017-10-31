package com.yunda.frame.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.persister.entity.AbstractEntityPersister;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 通用工具类
 * <li>创建人：程锐
 * <li>创建日期：2015-4-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public class CommonUtil {
    
    /**
     * <li>说明：组装以,号分隔的字符串为sql字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param strs 以,号分隔的字符串
     * @return sql字符串
     * @throws Exception
     */
    public static String buildInSqlStr(String strs) throws Exception {
        if (StringUtil.isNullOrBlank(strs)) {            
//            throw new BusinessException("以,号分隔的字符串为空");
            return "";
        }
        strs = strs.replace(",", "','");
        StringBuilder inSqlStr = new StringBuilder("('").append(strs).append("')");
        return inSqlStr.toString();
    }
    
    /**
     * <li>说明：根据map构造查询hql字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @param mode 查询模式，取Condition中常量
     * @return 查询hql字符串
     */
    @SuppressWarnings("unchecked")
    public static String buildParamsHql(Map paramMap, String... mode) {
        StringBuilder hql = new StringBuilder();
        if (paramMap.isEmpty())
            return hql.toString();
        Set<Map.Entry<String, String>> set = paramMap.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (!StringUtil.isNullOrBlank(value)) {
                if (value.startsWith("<>")) 
                    hql.append(Constants.AND).append(key).append(" <> '").append(value.substring(2)).append(Constants.SINGLE_QUOTE_MARK);
                else if (Condition.LIKE_STR.equals(mode))
                    hql.append(Constants.AND).append(key).append(" like '%").append(value).append("%'");
                else if (value.startsWith(Condition.IN_STR)) {
                    //value格式为以,号分隔的字符串
                    hql.append(Constants.AND).append(key).append(" in (").append(value.substring(2)).append(")");
                }
                else
                    hql.append(Constants.AND).append(key).append(" = '").append(value).append("'");
            }
        }
        return hql.toString();
    }
    
    /** 
     * <li>说明：获取list列表的长度
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list list列表
     * @return 列表长度
     */
    public static int getListSize(List list) {
        if (list == null || list.size() < 1)
            return 0;
        return list.size();
    }
    
    /**
     * <li>说明：构造排序sql
     * <li>创建人：程锐
     * <li>创建日期：2015-3-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param aliasName 表别名
     * @param meta AbstractEntityPersister对象
     * @param orderStrings 排序字段数组
     * @return 排序sql
     */
    public static String buildOrderSql(String aliasName, AbstractEntityPersister meta, String[] orderStrings) {
        return new StringBuilder(" ORDER BY ").append(aliasName)
                                              .append(meta.getPropertyColumnNames(orderStrings[0])[0])
                                              .append(" ")
                                              .append(orderStrings[1]).toString();
                                                                  
    }
    
    /**
     * <li>说明：根据Map的键值获取值并转换为字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param map 集合
     * @param key 键值
     * @return 对应值的字符串
     */
    public static String getMapValue(Map<String, String> map, String key) {
        if (!map.isEmpty() && map.get(key) != null && !StringUtil.isNullOrBlank(map.get(key).toString())) 
            return map.get(key).toString();
        return "";
    }
    
    /**
     * <li>说明：构建错误信息JSON字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param errMsg 错误信息
     * @param logger 日志对象
     * @return 错误信息JSON字符串
     */
    public static String buildFalseJSONMsg(String errMsg, Logger logger) {
        OperateReturnMessage msgObj = new OperateReturnMessage();
        String msg = "";
        msgObj.setFaildFlag(errMsg);
        try {
            msg = JSONUtil.write(msgObj);
        } catch (IOException ex) {
            logger.error("将实体值对象转换为JSON字符串时出错！", ex);
        }
        return msg;
    }
    
    /**
     * <li>说明：比较两个字符串是否相等
     * <li>创建人：程锐
     * <li>创建日期：2014-7-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true 相等 false 不相等
     */
    public static boolean isEqualsByTwoStr(String str1, String str2) {
        if (!StringUtil.isNullOrBlank(str1) && !StringUtil.isNullOrBlank(str2) && str1.equals(str2))
            return true;
        return false;
    }
    
    /**
     * <li>说明：根据查询条件Map构造List<Condition>列表对象
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param queryMap 查询条件Map
     * @param whereList List<Condition>列表对象
     * @return List<Condition>列表对象
     */
    @SuppressWarnings("unchecked")
    public static List<Condition> getWhereList(Map<String, String> queryMap, List<Condition> whereList) {
        if (queryMap.isEmpty())
            return whereList;
        Set<Map.Entry<String, String>> set = queryMap.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (!StringUtil.isNullOrBlank(value)) {
                whereList.add(new Condition(key, Condition.LIKE, value));
            }
        }
        return whereList;
    }
    
    /**
     * <li>说明：根据查询条件json字符串构造List<Condition>列表对象
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件json字符串
     * @param whereList List<Condition>列表对象
     * @return List<Condition>列表对象
     */
    @SuppressWarnings("unchecked")
    public static List<Condition> getWhereList(String searchJson, List<Condition> whereList) throws Exception{
        Map<String, String> queryMap = new HashMap<String, String>();        
        if (!StringUtil.isNullOrBlank(searchJson)) {
            queryMap = JSONUtil.read(searchJson, Map.class); 
        }
        return getWhereList(queryMap, whereList);
    }
}
