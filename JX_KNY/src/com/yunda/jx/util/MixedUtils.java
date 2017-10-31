package com.yunda.jx.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.web.context.ContextLoader;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.systemsite.ISystemSiteManager;
import com.yunda.util.DaoUtils;

/**
 * <li>标题：杂合工具类
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-2-19
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 */
public class MixedUtils {
    /**
     * <li>方法说明：执行in的SQL 
     * <li>方法名称：execInSql
     * <li>@param beforeSql 示例：[ update table_name set field = value idx in ( ]
     * <li>@param afterSql  示例：[ ) ]
     * <li>@param idx   需要执行in的值数组
     * <li>@param daoUtils
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-8-21 下午03:13:58
     * <li>修改人：
     * <li>修改内容：
     */
    public static void execInSql(String beforeSql, String afterSql, String[] idx, DaoUtils daoUtils){
        
        execInSql(beforeSql, afterSql, null, idx, daoUtils);
    }
    
    /**
     * <li>方法说明：执行in的SQL 
     * <li>方法名称：execInSql
    * <li>@param beforeSql 示例：[ update table_name set field = value idx in ( ]
     * <li>@param afterSql  示例：[ ) ]
     * <li>@param list 需要执行in的值集合
     * <li>@param daoUtils
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-8-21 下午03:28:00
     * <li>修改人：
     * <li>修改内容：
     */
    public static void execInSql(String beforeSql, String afterSql, List<String> list, DaoUtils daoUtils){
        
        execInSql(beforeSql, afterSql, list, null, daoUtils);
    }
    
    /**
     * <li>方法说明：执行in的SQL  
     * <li>方法名称：execInSql
     * <li>@param beforeSql
     * <li>@param afterSql
     * <li>@param list
     * <li>@param idx
     * <li>@param daoUtils
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-8-21 下午03:27:32
     * <li>修改人：
     * <li>修改内容：
     */
    private static void execInSql(String beforeSql, String afterSql, List<String> list, String[] idx, DaoUtils daoUtils){
        
        int arrLen = 0; //数组的长度
        if(idx != null){
            if(idx.length == 0){
                return;
            }
            arrLen = idx.length;
            
        }else if(list != null){
            if(list.size() == 0){
                return;
            }
            arrLen = list.size();
        }
        int excLen = 100; //每次执行新增最大长度
        int limit = 0;  //每次执行拼接idx的长度
        int start = 0;  //每次执行拼接idx的起始下标
        //计算是否还需增加一次循环，如arrlen = 100只循环一次，addLoopCount=0，但101却要循环2次， addLoopCount = 1
        int addLoopCount = (arrLen % excLen == 0) ? 0 : 1;
        // 计算循环次数
        int loopCount = arrLen / excLen + addLoopCount;
        
        for(int i = 1 ; i <= loopCount; i++){
            //计算当次循环需要执行拼接idx的长度
            limit =  (arrLen < i * excLen) ? (arrLen % excLen) : excLen;
            //计算idx的起始下标；
            start = (i - 1) * excLen;
            
            if(idx != null){
                
                daoUtils.executeSql(beforeSql + concat(idx, start , limit) + afterSql);
                
            }else if(list != null){
                
                daoUtils.executeSql(beforeSql + concat(list, start , limit) + afterSql);
            }
        }
    }
    
    /**
     * <li>方法说明：连接数组主键 
     * <li>方法名称：concat
     * <li>@param idx 需要连接的值数组
     * <li>@param start 起始下标
     * <li>@param limit 需要连接的长度
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-7-17 下午04:03:25
     * <li>修改人：
     * <li>修改内容：
     */
    public static String concat(String[] idx, int start, int limit){
        
        StringBuilder sb = new StringBuilder();
        int count = start + limit;
        for(int i = start; i < count; i++){
            sb.append("'");
            sb.append(idx[i]);
            sb.append("',");
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }
    
    /**
     * <li>方法说明：连接集合主键 
     * <li>方法名称：concat
     * <li>@param list 需要连接的值集合
     * <li>@param start 起始下标
     * <li>@param limit 需要连接的长度
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-8-21 下午03:21:43
     * <li>修改人：
     * <li>修改内容：
     */
    public static String concat(List<String> list, int start, int limit){
        
        StringBuilder sb = new StringBuilder();
        int count = start + limit;
        for(int i = start; i < count; i++){
            sb.append("'");
            sb.append(list.get(i));
            sb.append("',");
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }
    
    /**
     * <li>方法说明：连接两个字符串 
     * <li>方法名称：spliceStr
     * <li>@param str1
     * <li>@param str2
     * <li>@param spliceChar null默认为“ | ”
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-2-19 下午02:07:02
     * <li>修改人：
     * <li>修改内容：
     */
    public static String spliceStr(Object str1, Object str2, String spliceChar){
        StringBuilder sb  = new StringBuilder();
        if(StringUtil.nvl(str1).equals("") == false){
            sb.append(str1);
        }
        if(StringUtil.nvl(str2).equals("") == false){
            if(sb.length() > 0){
                if(spliceChar == null){
                    spliceChar = " | ";
                }
                sb.append(spliceChar);
            }
            sb.append(str2);
        }
        return sb.toString();
    }
    
    /**
     * <li>方法说明：String to Long 
     * <li>方法名称：string2Long
     * <li>@param val
     * <li>@return
     * <li>return: Long
     * <li>创建人：张凡
     * <li>创建时间：2013-10-11 下午01:17:17
     * <li>修改人：
     * <li>修改内容：
     */
    public static Long string2Long(Object val){
    
        String value = StringUtil.nvl(val);
        if(!value.equals("")){
            return Long.valueOf(value);
        }
        return null;
    }
    
    /**
     * <li>方法说明：判断list是否为空 
     * <li>方法名称：listIsEmpty
     * <li>@param list
     * <li>@return
     * <li>return: boolean
     * <li>创建人：张凡
     * <li>创建时间：2014-3-3 下午01:50:08
     * <li>修改人：
     * <li>修改内容：
     */
    public static boolean listIsEmpty(List<?> list){
        if(list == null){
            return true;
        }        
        return list.size() <= 0;
    }
    
    /**
     * <li>方法说明：时间转字符串格式化
     * <br>（适用于少量日期对象转换，多个日期对象转换不建议使用）
     * <li>方法名称：dateToStr
     * <li>@param date Null时返回当前时间
     * <li>@param mode 使用时不可传递format参数
     *      <div style='margin-left:20px;'>
     *          mode = 1 format = yyyy-MM-dd
     *      </div>
     *      <div style='margin-left:20px;'>
     *          mode = 2 format = yyyy-MM-dd HH:mm:ss
     *      </div>
     *      <div style='margin-left:20px;'>
     *          mode = 3 format = yyyy-MM-dd HH:mm
     *      </div>
     * <li>@param format 自定义格式
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-3-5 上午10:29:32
     * <li>修改人：
     * <li>修改内容：
     */
    public static String dateToStr(Date date, int mode, String...format){
        if(date == null){
            date = new Date();
        }
        String f = null;
        if(format.length <= 0){            
            switch(mode){
                case 1:
                    f = "yyyy-MM-dd";
                    break;
                case 2:
                    f = "yyyy-MM-dd HH:mm:ss";
                    break;
                case 3:
                    f = "yyyy-MM-dd HH:mm";
                    break;
                default:
                    break;
            }
        }else{
            f = format[0];
        }
        return new SimpleDateFormat(f).format(date);
    }
    
    /**
     * <li>说明：验证传入的日期字符串是否有效
     * <li>创建人：王治龙
     * <li>创建日期：2014-4-24
     * <li>修改人： 
     * <li>修改日期：（经验证无法使用）
     * <li>修改内容：
     * @param dateStr：需要验证的字符串
     * @return boolean
     
    @Deprecated
    public static boolean isDate(String dateStr){
    	boolean isOk = true ;
    	SimpleDateFormat sdf = new SimpleDateFormat();
    	sdf.setLenient(false);
    	try {
			sdf.parse(dateStr);
		} catch (ParseException e) {
			isOk = false ;
		}
		return isOk ;
    }*/
    
    /**
     * <li>方法说明：保存或更新，适用于在Manager中保存非泛型<T, S>的实体对象 
     * <li>方法名称：saveOrUpdate
     * <li>@param <T>
     * <li>@param t
     * <li>@param daoUtils
     * <li>@throws NoSuchFieldException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-30 上午09:54:10
     * <li>修改人：
     * <li>修改内容：
     */
    public static <T> void saveOrUpdate(T t, DaoUtils daoUtils) throws NoSuchFieldException{
        t = EntityUtil.setSysinfo(t);
        t = EntityUtil.setNoDelete(t);
        daoUtils.getHibernateTemplate().saveOrUpdate(t);
    }
    
    /**
     * <li>方法说明：非空值验证 
     * <li>方法名称：validate
     * <li>@param strings
     * <li>@return
     * <li>return: boolean [true|有空值]
     * <li>创建人：张凡
     * <li>创建时间：2014-8-11 下午03:10:57
     * <li>修改人：
     * <li>修改内容：
     */
    public static boolean validate(String...strings){
        for(int i = 0; i < strings.length; i++){
            if(StringUtil.isNullOrBlank(strings[i])){
                return true;
            }
        }
        return false;
    }
    
    /**
     * <li>方法说明：获取查询整型值，适用于HQL查询整数字段
     * <li>方法名称：getNumValue
     * <li>@param result
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2014-8-11 下午03:29:45
     * <li>修改人：
     * <li>修改内容：
     */
    public static int getNumValue(Object result){
        if(result instanceof BigDecimal){
            return ((BigDecimal) result).intValue();
        }else if(result instanceof Long){
            return ((Long) result).intValue();
        }
        return -1;
    }
    
    /**
     * <li>方法说明：获取站点ID（同步EntityUtil，PS：如果EntityUtil提取成方法，此方法不会存在）
     * <li>方法名称：getSiteID
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-8-21 上午10:09:46
     * <li>修改人：
     * <li>修改内容：
     */
    public static String getSiteID(){
        ISystemSiteManager siteIdManager =
            (ISystemSiteManager)ContextLoader.getCurrentWebApplicationContext()
            .getBean("systemSiteManager");
        String siteID = null;
        
        if(siteIdManager == null)
            siteID = JXConfig.getInstance().getSynSiteID();
        else 
            siteID = siteIdManager.findSysSiteIdByLoginUser();
        
        return StringUtil.nvlTrim(siteID, JXSystemProperties.SYN_SITEID);
    }
    
    /**
     * <li>方法说明：执行可传参SQL
     * <li>方法名：executeSQL
     * @param daoUtils dao
     * @param sql sql
     * @param params 参数
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-23
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public static int executeSQL(DaoUtils daoUtils, final String sql, final Object... params){
    	return (Integer)daoUtils.getHibernateTemplate().execute(new HibernateCallback(){
            public Integer doInHibernate(Session s) {
                Query query = s.createSQLQuery(sql);
                for(int i = 0; i < params.length; i++){
                    query.setParameter(i, params[i]);
                }
                return query.executeUpdate();
            }
        });
    }
    
    /**
     * <li>方法说明：将数组转换成字符串
     * <li>方法名：parseArray
     * @param arr 数组
     * @return
     * <li>创建人： 黄杨
     * <li>创建日期：2017年5月3日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public static String parseArray(int[] arr){
        StringBuilder s = new StringBuilder();
        s.append(arr[0]);
        for(int i = 1; i < arr.length; i++){
            s.append(",").append(arr[i]);
        }
        return s.toString();
    }
    
    /**
     * <li>方法说明：最大编号
     * <li>方法名：maxCode
     * @param maxCode 最大编号
     * @param classCode 类别编号
     * @return
     * <li>创建人： 黄杨
     * <li>创建日期：2017年5月4日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public static String maxCode(String maxCode, String classCode){
        if(classCode == null) return null;
        Long org = SystemContext.getOmEmployee().getOrgid();
        String fixed = classCode + org;
        if(maxCode == null){
            return fixed + "0001";
        }
        try{
            if(maxCode.replace(classCode, "").length() == 0){
                return fixed + "0001";
            }else{
                maxCode = maxCode.replace(fixed, "");
                return fixed + getIncrementCode(maxCode);
            }
        }catch(NumberFormatException e){
            return "";
        }
    }
    
    /**
     * <li>方法说明：获取自增编号
     * <li>方法名：getIncrementCode
     * @return
     * <li>创建人： 黄杨
     * <li>创建日期：2017年5月4日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    private static String getIncrementCode(String num){
        long code = Long.parseLong(num) + 1;
        int base = 1;
        for(int i = 3; i > 0; i--){
            if((base *= 10) < code){
                continue;
            }
            StringBuilder val = new StringBuilder();
            for(int j = 0; j < i; j++){
                val.append("0");
            }
            return val.append(code).toString();
        }
        return num;
    }
}