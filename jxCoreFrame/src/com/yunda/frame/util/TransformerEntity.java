package com.yunda.frame.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

/**
 * 
 * <li>标题：SQL to Object 工具帮助类
 * <li>说明：类实例化对象适用于hibernate Query对象的setResultTransformer方法参数，以处理SQL结果填充到实体对象字段操作。        
 *           
 * <li>创建人：张凡
 * <li>创建时间：2012-10-26
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK
 *
 */
public class TransformerEntity implements ResultTransformer{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 目标POJO类
	 */
	private final Class<?> resultClass;
	/**
	 * setter器
	 */
	private Setter[] setters;
	/**
	 * 属性访问器
	 */
	private PropertyAccessor propertyAccessor;
	//private AbstractEntityPersister meta;
	/**
	 * 构造函数，传入对象类型，将SQL查询结果装进对象
	 * @param resultClass 实体类Class
	 * @param _meta hibernate AbstractEntityPersister对象
	 */
	public TransformerEntity(Class<?> resultClass, AbstractEntityPersister _meta) {
	    //this.meta = _meta;
		if(resultClass == null)
		    throw new IllegalArgumentException("resultClass cannot be null");
		
		this.resultClass = resultClass;
		
		propertyAccessor = new ChainedPropertyAccessor(
		    new PropertyAccessor[] {
		        PropertyAccessorFactory.getPropertyAccessor(resultClass,null),
		        PropertyAccessorFactory.getPropertyAccessor("field")
		    }
		); 		
	}
	
	/**
	 *  SQL to Object 结果转换时，HIBERNATE调用此方法
	 *  @param tuple 查询结果列数组
	 *  @param aliases 字段列数组
	 */
	public Object transformTuple(Object[] tuple, String[] aliases)
	{
		Object result = null;
		try
		{
			if(setters == null)//首先初始化，取得目标POJO类的所有SETTER方法
			{
				initSetter(tuple, aliases, result);
			}
			result = resultClass.newInstance();
			
			setValues(tuple, result);
		}
		catch (InstantiationException e)
		{
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}
		catch (IllegalAccessException e)
		{
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}
		return result;
	}

	/**
	 * <li>方法说明：给目标对象设置值
	 * <li>方法名称：setValues
	 * <li>@param tuple
	 * <li>@param result
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2014-8-12 上午11:32:32
	 * <li>修改人：
	 * <li>修改内容：
	 */
    private void setValues(Object[] tuple, Object result)
    {
        Class<?> paramCls = null;
        for(int i = 0; i < setters.length; i++)
        {
            if(tuple[i] == null){
                continue;
            }
            paramCls = setters[i].getMethod().getParameterTypes()[0];
            
            if(tuple[i] instanceof java.math.BigDecimal)    //值为数值型
            {                
                bigDecimalSetting(tuple, result, paramCls, i);
            }
            else if(tuple[i] instanceof java.util.Date)     //值为日期型
            {
                //给日期赋值，查询出来的date不能精确到时分秒
                setters[i].set(result, new Timestamp(((Date)(tuple[i])).getTime()), null);
            }
            else        //其它类型的值
            {
                if(paramCls.equals(String.class))   //参数为字符串
                {
                    setters[i].set(result, String.valueOf(tuple[i]), null); 
                }
                else if(paramCls.equals(Character.class))   //参数为字符型
                {
                    setters[i].set(result, tuple[i].toString().toCharArray()[0], null);
                }
                else if(paramCls.equals(Date.class))   //参数为日期
                {
                    parseTimestamp(tuple, result, i);
                }
                else //参数为对象类型
                {
                    setters[i].set(result, tuple[i], null); 
                }
            }
        }
    }

    /**
     * <li>方法说明：将字符串结果转换成Timestamp类型 
     * <li>方法名称：parseTimestamp
     * <li>@param tuple
     * <li>@param result
     * <li>@param i
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-8-13 下午05:33:19
     * <li>修改人：
     * <li>修改内容：
     */
    private void parseTimestamp(Object[] tuple, Object result, int i)
    {
        try
        {
            Date date = new SimpleDateFormat(getDateFormat(tuple[i].toString())).parse(tuple[i].toString());
            setters[i].set(result, new Timestamp(date.getTime()), null);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <li>方法说明：给列值为数值型的字段设置值 
     * <li>方法名称：bigDecimalSetting
     * <li>@param tuple
     * <li>@param result
     * <li>@param paramCls
     * <li>@param i
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-8-12 上午11:35:54
     * <li>修改人：
     * <li>修改内容：
     */
    private void bigDecimalSetting(Object[] tuple, Object result, Class<?> paramCls,int i)
    {
        if(paramCls.equals(Integer.class))
        {                            
            setters[i].set(result, ((BigDecimal)tuple[i]).intValue(), null);
        }
        else if(paramCls.equals(Long.class))
        {
            setters[i].set(result, ((BigDecimal)tuple[i]).longValue(), null);
        }
        else if(paramCls.equals(Double.class))
        {                           
            setters[i].set(result, ((BigDecimal)tuple[i]).doubleValue(), null);
        }
        else if(paramCls.equals(Float.class))
        {
            setters[i].set(result, ((BigDecimal)tuple[i]).floatValue(), null);
        }
        else    //当作字符串处理
        {
            setters[i].set(result, ((BigDecimal)tuple[i]).toString(), null);
        }
    }

    /**
     * <li>方法说明：初始化setter 
     * <li>方法名称：initSetter
     * <li>@param tuple
     * <li>@param aliases
     * <li>@param result
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-8-12 上午11:33:43
     * <li>修改人：
     * <li>修改内容：
     */
    private void initSetter(Object[] tuple, String[] aliases, Object result)
    {
        setters = new Setter[aliases.length - 1];//之所以length要-1，是因为最后一个是rownum字段。
        //取得POJO所有属性名
        Field[] fields = resultClass.getDeclaredFields();
        for (int i = 0; i < setters.length; i++)
        {
        	String alias = aliases[i];
        	if(alias != null)
        	{
        		//我的逻辑主要是在getSetterByColumnName方法里面，其它都是HIBERNATE的另一个类中COPY的
        		//这里填充所需要的SETTER方法
        		setters[i] = getSetterByColumnName(alias, tuple[i], result, fields);
        	}
        }
    }

	/**
	 *  SQL to Object转换时根据数据库字段名在POJO查找JAVA属性名，参数就是数据库字段名，如：USER_ID
	 *  @param alias 字段名称
	 *  @param value 字段值
	 *  @param result 要set值的目标对象
	 *  @param fields目标对象字段
	 */
	private Setter getSetterByColumnName(String alias, Object value, Object result, Field[] fields)
	{
		if(fields == null || fields.length == 0)
		{
			throw new RuntimeException("实体" + resultClass.getName() + "不含任何属性");
		}
		//把字段名中所有的下杠去除
		String proName = alias.replaceAll("_", "");
		Setter setter = null;
		for (Field field : fields)
		{
			if(field.getName().equalsIgnoreCase(proName))//去除下杠的字段名如果和属性名对得上，就取这个SETTER方法
			{
			    setter  = propertyAccessor.getSetter(resultClass, field.getName());
			    if(setter.getMethod() == null)   //真心不知道hibernate这里怎么计算获取setter方法的，有BUG啊
			    {         
			        setter = propertyAccessor.getSetter(resultClass, (field.getName().charAt(0) + "").toUpperCase() + field.getName().substring(1));
			    }
				return setter;
			}
		}		
		throw new RuntimeException("找不到数据库字段 ：" + alias + " 对应的POJO属性或其getter方法，比如数据库字段为USER_ID或USERID，那么JAVA属性应为userId");
	}

	/**
	 * 继承类需要重写的方法，可能hibernate会使用
	 */
	@SuppressWarnings("unchecked")
	public List transformList(List collection) {
		return collection;
	}
	/* ***********************自定义辅助方法****************************** */
	
	/**	 * 
	 * <li>方法名称：processSQLSplice
	 * <li>方法说明：处理SQL查询条件拼接 
	 * @param sql
	 * @param where
	 * @param orders 有关排序的相关数据，必须为长度为3的数组数据 {1,实体字段，2,表字段，3,升序或降序}
	 * @return 最终的SQL
	 * return: String 
	 * <li>创建人：张凡
	 * <li>创建时间：2012-10-27 下午12:31:59
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static String processSQLSplice(String sql,String where, String... orders){
		//需要拼查询或排序
		if(!where.equals("") || orders != null){
		    String orderField=null;
		    if(orders.length > 0 && orders[1] != null){
    		   //排序字段查找
		        orderField = getOrderField(sql.toLowerCase(),orders[0].toLowerCase());
    		    if(orderField == null){
    		        orderField = getOrderField(sql.toLowerCase(),orders[1].toLowerCase());
    		    }
		    }
		    //有默认排序
			if(sql.toLowerCase().contains("order by")){
				int position = sql.toLowerCase().indexOf("order by");
				String fore = sql.substring(0,position);//前一部份SQL
				String rear = sql.substring(position);//后一部份SQL
				//有排序
				if(orderField != null){
					String lastFore = rear.substring(0,rear.toLowerCase().indexOf("order by")+8);
					String lastRear = rear.substring(rear.toLowerCase().indexOf("order by")+8);
					rear = lastFore + " " + orderField + " " + orders[2] + "," + lastRear;//拼接order by
 				}
				
				//有where
				if(!where.equals("")){
					if(fore.toLowerCase().contains(" where 1=1 ") && !rear.toLowerCase().contains(" where 1=1 ")){    
                        String left = fore.substring(0,fore.toLowerCase().indexOf(" where 1=1 ") + 11); //11是&where 1=1&的长度加1
                        String right = fore.substring(fore.toLowerCase().indexOf(" where 1=1 ") + 11);
						sql = left  + "  and " + where + " " + right + rear;
					}else if(rear.toLowerCase().contains(" where 1=1 ")){
                        String left = fore.substring(0,fore.toLowerCase().indexOf(" where 1=1 ") + 11);
                        String right = fore.substring(fore.toLowerCase().indexOf(" where 1=1 ") + 11);
                        sql = fore +" " + left  + " and " + where + " " + right + rear;
                    }else{
						sql = fore + " where " + where + " " + rear;					
					}
				}else{//没有where
					sql = fore + " " + rear;
				}
			}else{
			    int position ;
                String fore = sql;//前一部份SQL
                String rear = "";//后一部份SQL
                
                if(fore.toLowerCase().contains("group by") && fore.indexOf(" where 1=1 ") < fore.indexOf("group by")){
                    position = fore.toLowerCase().indexOf("group by");
                    fore = fore.substring(0,position-1);//前一部份SQL
                    rear = sql.substring(position) ;//后一部份SQL
                    
                }
                                
				if(!where.equals("")){
					if(sql.toLowerCase().contains(" where 1=1 ")){
                        String left = fore.substring(0,fore.toLowerCase().indexOf(" where 1=1 ")+11);
                        String right = fore.substring(fore.toLowerCase().indexOf(" where 1=1 ")+11);
                        sql = left  + " and " + where + " " + right + rear;
					}else{
					    sql = fore + " where " + where + " " + rear; 
					}
				}
				if(orderField != null){
					sql += " order by " + orderField +" " + orders[2];
 				}
			}
		}
		
		return sql;
	}
	
	/**
	 * 
	 * <li>方法名称：getOrderField
	 * <li>方法说明：截取排序字段 
	 * @param sql
	 * @param field
	 * @return 
	 * return: String
	 * <li>创建人：张凡
	 * <li>创建时间：2012-10-30 上午10:18:32
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static String getOrderField(String sql,String field){
	    if(sql.contains(field)){
	        //取出从开始到要排序的字段之间的字符串 (select idx,abc,xxx from table 取 abc 排序, 则 select idx,abc)
    	    String tmp = sql.substring(0,sql.indexOf(field)+field.length()+1).trim();
    	    //是否包含“,”(select idx,abc,xxx from table)
            if(tmp.contains(",")){
                //判断最后一位是不是“,”,有时候会多取出一个","，如果多取出就去掉
                if(tmp.charAt(tmp.length()-1)==','){
                    tmp = tmp.substring(0,tmp.length()-1).trim();
                }
                //判断是否有to_char，to_date之类的
                if(tmp.lastIndexOf(",") == tmp.lastIndexOf(",'")){
                    tmp = tmp.substring(tmp.lastIndexOf("(")+1,tmp.lastIndexOf(",")).trim();                    
                }else{
                    tmp = tmp.substring(tmp.lastIndexOf(",")+1).trim();
                }
            }else{
                //进入此处当时想的是此字段是第一个，去掉Select
                tmp = tmp.substring(7).trim();
            }
            //有As
            if(tmp.contains(" as \"" + field + "\"")){
                tmp = tmp.substring(0,tmp.indexOf(" as \"" + field + "\"")).trim();
            }
            //没有As 却是以空格起别名
            if(tmp.trim().contains(" ")){
                tmp = tmp.substring(0,tmp.indexOf(" ")).trim();                
            }
            //遇上字段结果组合时使用
            if(tmp.indexOf("|")!=-1){
                tmp = tmp.replace("|", "").trim();
            }
            return tmp;
	    }else{
	        return null;
	    }
    }
	
	/**
	 * <li>方法名称：checkValueKeyWord
	 * <li>方法说明：检查值是否符合SQL关键字查询
	 * <li>@param value
	 * <li>@return
	 * <li>return: String
	 * <li>创建人：张凡
	 * <li>创建时间：2013-1-11 下午01:32:33
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static String checkValueKeyWord(String value){
	    if(value.charAt(0) == '$'  && value.charAt(value.length()-1) == '$'){
	        String keyword = value.substring(1,value.length()-1);
	        if("IS NULL".equalsIgnoreCase(keyword)){
	            return " " + keyword + " ";
	        }
	        if("IS NOT NULL".equalsIgnoreCase(keyword)){
	            return " " + keyword + " ";
	        }
	    }
	    return "";
	}
	
	/**
	 * <li>方法名称：appendSql
	 * <li>方法说明：追加SQL条件 
	 * <li>@param sbWhere 
	 * <li>@param field 字段
	 * <li>@param param 参数对象
	 * <li>@param value 值
	 * <li>@param operator 操作符
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-27 下午01:20:16
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static void appendSql(StringBuffer sbWhere, String field, List<String> param, String value, String operator){
	   
	    String checkRet=checkValueKeyWord(value);//检查是否符合关键字查询	   
        int index =value.indexOf("$[IN]$"); 
        if(index !=-1){
            /*
             * 用来支持in操作,值=$[IN]$abc]|[123 则解析为field in ('abc','123')
             */
            sbWhere.append(field + " in ( ");
            String[] vals = value.substring(index + 6).split("\\]\\|\\[");
            for(int k = 0; k < vals.length; k++){
                sbWhere.append(" ?,");
                param.add(vals[k]);
            }
            sbWhere.deleteCharAt(sbWhere.length()-1);//删除多余的逗号
            sbWhere.append(") "); 
        }else if(value.indexOf("$[OR]$") != -1){
            /*
             * 支持or操作，值=$[OR]$0]|[$IS NULL$则解析为 (field = '0' or field is null)
             */
            String[] vals = value.substring(value.indexOf("$[OR]$")+6).split("\\]\\|\\[");
            String alias = "";//用于保存别名
            if(sbWhere.charAt(sbWhere.length()-1)=='.'){//有别名
                alias = sbWhere.substring(sbWhere.lastIndexOf(" "));//获取别名
                sbWhere.delete(sbWhere.lastIndexOf(" "), sbWhere.length()); //删除别名
            }
            sbWhere.append("(");
            for(int k = 0; k < vals.length; k++){
                checkRet = TransformerEntity.checkValueKeyWord(vals[k]); //检查值是否符合关键字查询
                
                //不为第一次加or
                if(k != 0){
                    sbWhere.append(" or ");
                }
                //符合关键字查询
                if(!"".equals(checkRet)){
                    sbWhere.append(alias + field + checkRet);
                }else{
                    sbWhere.append(alias + field + " " + operator + " ?" );
                    if(operator.equals("=")){
                        param.add(vals[k]);
                    }else{
                        param.add("%" + vals[k] + "%");
                    }
                }
            }
            sbWhere.append(")");
        }else{
            sbWhere.append(field + " " + operator + " ? ");
            if(operator.equals("=")){
                param.add(value);
            }else{
                param.add("%" + value + "%");
            }
        }        
	}
	/**
	 * <li>方法说明：获取日期比较操作符 
	 * <li>方法名称：getToken
	 * <li>@param token
	 * <li>@return
	 * <li>return: String
	 * <li>创建人：张凡
	 * <li>创建时间：2013-5-30 下午11:51:54
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static String getToken(String token){
	    if("XY".equals(token))
	        return "<";
	    if("DY".equals(token))
	        return ">";
	    if("DD".equals(token))
	        return ">=";
	    if("XD".equals(token))
	        return "<=";
	    return null;
	}
	
	/**
	 * <li>方法说明：获取日期格式 
	 * <li>方法名称：getDateFormat
	 * <li>@param value
	 * <li>@return
	 * <li>return: String
	 * <li>创建人：张凡
	 * <li>创建时间：2013-5-30 下午11:57:12
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static String getDateFormat(String value){
	    if(value.indexOf("&") != -1){
	        value = value.substring(0, value.indexOf("&"));
	    }
	    if(value.contains(" ")){
            if(value.length() == 19){
                value = "yyyy-MM-dd HH:mm:ss";
            }else{
                value = "yyyy-MM-dd HH:mm";
            }
        }else{
            value = "yyyy-MM-dd";
        }
	    return value;
	}
	
	/**
	 * <li>方法说明：拼接日期 
	 * <li>方法名称：appendDate
	 * <li>@param sbWhere
	 * <li>@param field
	 * <li>@param param
	 * <li>@param value
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-5-31 上午12:17:05
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static void appendDate(StringBuffer sbWhere, String field, List<String> param, String value, String alias){
	    String format = null;
        alias = alias == null ? "" : alias + ".";
        if(value.indexOf("#BETWEEN#") == 0){
            String[] subValue = null;
            String opt = null;
            //between方式匹配日期
            subValue =  value.substring(9).split("\\|");//取得两个日期值
            if("undefined".equals(subValue[0])){//第一个值为空
                format = getDateFormat(subValue[1]);
                if(subValue[1].indexOf("&") != -1){
                    int index = subValue[1].indexOf("&");
                    opt = subValue[1].substring(index + 1, index + 3);
                    subValue[1] = subValue[1].substring(0, index);
                }else{
                    opt = "XD";//第二个值默认操作符，小于等于
                }
                param.add(subValue[1]);
                sbWhere.append(field + " " + getToken(opt) + " to_date(?,'" + format +"')");
            }else{
                format = getDateFormat(subValue[0]);//第一个值不为空，取第一个值
                if(subValue[0].indexOf("&") != -1){
                    int index = subValue[0].indexOf("&");
                    opt = subValue[0].substring(index + 1, index + 3);
                    subValue[0] = subValue[0].substring(0, index);
                }else{
                    opt = "DD";//第一个值默认操作符，大于等于
                }
                param.add(subValue[0]);
                sbWhere.append(field + " " + getToken(opt) + " to_date(?,'" + format +"')");
                
                if(!"undefined".equals(subValue[1])){
                    format = getDateFormat(subValue[1]);
                    if(subValue[1].indexOf("&") != -1){
                        int index = subValue[1].indexOf("&");
                        opt = subValue[1].substring(index + 1, index + 3);
                        subValue[1] = subValue[1].substring(0, index);
                    }else{
                        opt = "XD";//第二个值默认操作符，小于等于
                    }
                    param.add(subValue[1]);
                    sbWhere.append(" and " + alias + field + " " + getToken(opt) + " to_date(?,'" + format +"')");
                }
            }
            
        }else{
            //单个匹配
            format = TransformerEntity.getDateFormat(value);
            if(value.startsWith("'")){
            	param.add(value.substring(1,value.length()-1));
            }else{
            	param.add(value);
            }
            sbWhere.append(alias + field + " = to_date(?,'" + format + "') ");
        }
	}
	
	/**
	 * <li>方法说明：取值 
	 * <li>方法名称：getValue
	 * <li>@param val
	 * <li>@return
	 * <li>return: String
	 * <li>创建人：张凡
	 * <li>创建时间：2013-10-12 下午03:46:19
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static String getValue(String val){
	    if("".equals(val)){
	        return val;
	    }
	    Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(val.trim());
        if(matcher.matches()){//判断是否为数字
            return val;
        }else{
            return val.substring(1, val.length()-1);//取得值
        }
	}
}
