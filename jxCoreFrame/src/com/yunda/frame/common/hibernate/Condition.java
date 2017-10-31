package com.yunda.frame.common.hibernate;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.type.Type;

import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该类描述where语句查询条件，用于单表动态查询的条件包装
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class Condition {
	/** 对应SQL的"field=value"表达式,如：Expression.eq("name","zx"); */
	public static final int EQ = 1;
	public static final String EQ_STR = "=";
	/** 方法的参数为一个Map类型对象，包含多个名/值对对应关系，相当于多个Expression.eq的叠加 */
	public static final int ALL_EQ = 2;
	public static final String ALL_EQ_STR = "==";
	/** 对应SQL的“field>value”表达式 */
	public static final int GT = 3;
	public static final String GT_STR = ">";
	/** 对应SQL的“field>=value”表达式 */
	public static final int GE = 4;
	public static final String GE_STR = ">=";
	/** 对应SQL的“field<value”表达式 */
	public static final int LT = 5;
	public static final String LT_STR = "<";
	/** 对应SQL的“field<=value”表达式 */
	public static final int LE = 6;
	public static final String LE_STR = "<=";
	/** 对应SQL语句的between表达式 */
	public static final int BETWEEN = 7;
	public static final String BETWEEN_STR = "between";
	/** 对应SQL语句的”field like value”表达式 */
	public static final int LIKE = 8;
	public static final String LIKE_STR = "like";	
	public static final int LLIKE = 50;//王斌添加2014-5-28
	public static final String LLIKE_STR = "llike";//王斌添加2014-5-28
	/** 对应SQL语句的“field in(……)”表达式 */
	public static final int IN = 9;
	public static final String IN_STR = "in";
	/** 用于比较两个属性值，对应”field=field”SQL表达式 */
	public static final int EQ_PROPERTY = 10;
	public static final String EQ_PROPERTY_STR = "prop=";
	/** 用于比较两个属性值，对应”field>field”SQL表达式 */
	public static final int GT_PROPERTY = 11;
	public static final String GT_PROPERTY_STR = "prop>";
	/** 用于比较两个属性值，对应”field>=field”SQL表达式 */
	public static final int GE_PROPERTY = 12;
	public static final String GE_PROPERTY_STR = "prop>=";
	/** 用于比较两个属性值，对应”field<field”SQL表达式 */
	public static final int LT_PROPERTY = 13;
	public static final String LT_PROPERTY_STR = "prop<";
	/** 用于比较两个属性值，对应”field<=field”SQL表达式 */
	public static final int LE_PROPERTY = 14;
	public static final String LE_PROPERTY_STR = "prop<=";
	/** 对应SQL的“not”表达式 逻辑非操作，相当java中的!逻辑操作 */
	public static final int NOT = 15;
	public static final String NOT_STR = "not";
	/** 对应SQL的“pk=value”表达式 主键等于某个值*/
	public static final int PK_EQ = 16;
	public static final String PK_EQ_STR = "pk=";
	/** 具体不详，备用，待探讨 */
	public static final int ILIKE = 17;
	/** 对应SQL的“field为空白”表达式，待探讨 */
	public static final int IS_EMPTY = 18;
	/** 对应SQL的“field非空白”表达式，待探讨 */
	public static final int IS_NOT_EMPTY = 19;
	/** 对应SQL的“field is not null”表达式 字段不为空*/
	public static final int IS_NOT_NULL = 20;
	public static final String IS_NOT_NULL_STR = "is not null";
	/** 对应SQL的“field is null”表达式 字段为空*/
	public static final int IS_NULL = 21;
	public static final String IS_NULL_STR = "is null";
	/** 对应SQL的“filed!=value”表达式 字段不等于某值 */
	public static final int NE = 22;
	public static final String NE_STR = "!=";
	/** 对应SQL的“field1!=field2”表达式 字段1不等于字段2*/
	public static final int NE_PROPERTY = 23;
	public static final String NE_PROPERTY_STR = "prop!=";
	/** 具体不详，备用，待探讨*/
	public static final int SIZE_EQ = 24;
	/** 具体不详，备用，待探讨*/
	public static final int SIZE_GE = 25;
	/** 具体不详，备用，待探讨*/
	public static final int SIZE_GT = 26;
	/** 具体不详，备用，待探讨*/
	public static final int SIZE_LE = 27;
	/** 具体不详，备用，待探讨*/
	public static final int SIZE_LT = 28;
	/** 具体不详，备用，待探讨*/
	public static final int SIZE_NE = 29;	
	/**
	 *作为补充这个方法提供了原生SQL语句查询的支持，在执行时直接通过原生SQL语句进行限定，
	 *如：Expression.sql(“lower({alias}.name) like (?)”,“zhao%”,Hibernate.STRING) ;
	 *在运行时{ alias }将会由当前查询所关联的实体类名替换，()中的?将会由”zhao%”替换，并且类型由Hibernate.STRING指定。
	 */
	public static final int SQL = 30;
	public static final String SQL_STR = "sql";
	/** 
	 * 在执行时直接通过原生SQL语句进行限定，对应Expression.sqlRestriction(String arg0, Object arg1, Type arg2)操作 
	 * 当Condition.compare == SQL_PARAM时，将根据sql，propValues和valueTypes构造原生sql语句查询条件
	 */
	public static final int SQL_PARAM = 31;
	public static final String SQL_PARAM_STR = "sqlParam";
	/** 
	 * 在执行时直接通过原生SQL语句进行限定，对应Expression.sqlRestriction(String arg0, Object[] arg1, Type[] arg2)操作 
	 * 当Condition.compare == SQL_PARAM时，将根据sql，propValues和valueTypes构造原生sql语句查询条件
	 */	
	public static final int SQL_PARAMS = 32;
	public static final String SQL_PARAMS_STR = "sqlParams";
	/**
	 * 对应SQL语句的and关系组合，expression1 and expression2如：Expression.and(Expression.eq(“name”,”zx”),Expression.eq(“sex”,”1”));
	 * 当Condition.compare == AND时，将从conditionList取得第一个和第二个condition的expression进行组装
	 */
	public static final int AND = 41;
	public static final String AND_STR = "and";
	/**
	 * 对应SQL语句的or关系组合，expression1 or expression2如：Expression.or(Expression.eq(“name”,”zx”),Expression.eq(“name”,”zhaoxin”));
	 * 当Condition.compare == OR时，将从conditionList取得第一个和第二个condition的expression进行组装
	 */
	public static final int OR = 42;
	public static final String OR_STR = "or";
	/** 对应SQL语句的and关系组合，如：(expression1 and expression2 and expression3 and ...) 
	 * 当Condition.compare == CONJUNCTION时，将遍历conditionList取得每个expression进行组装
	 */
	public static final int CONJUNCTION = 43;
	public static final String CONJUNCTION_STR = "AND";
	/** 对应SQL语句的or关系组合，如：(expression1 or expression2 or expression3 or ...) 
	 * 当Condition.compare == DISJUNCTION时，将遍历conditionList取得每个expression进行组装
	 */
	public static final int DISJUNCTION = 44;
	public static final String DISJUNCTION_STR = "OR";
	/* 比较操作符的map集合，key为字符串(>=)，value为整数值(GE) */
	private static Map<String,Integer> compareMap;
	/* 比较操作符的map集合，key为整数值(GE)，value为字符串(>=) */
	private static Map<Integer,String> compareStrMap;
	
	
	/** 查询条件名称（必须与对应实体类的属性名称一致） */
	private String propName;
	/** 条件比较类型 */
	private Integer compare = EQ;
	/** 一个Map类型对象，包含多个名/值对对应关系，相当于多个Expression.eq的叠加 */
	private Map<String,Object> eqMap;
	/** like匹配模式，如全文模糊匹配 */
	@JsonIgnore
	private MatchMode likeMode = MatchMode.ANYWHERE;
	/** like左匹配模式，王斌添加2014-5-28 */
	@JsonIgnore
	private MatchMode llikeMode = MatchMode.START;
	/** 字符串等于（=）操作是否用like模糊匹配替代 */
	private boolean stringLike = false;	
	/** 查询参数值 */
	private Object propValue;
	/** 当compare=SQL(SQL_PARAM,SQL_PARAMS)时，该属性表示原生sql语句字符串 */
	private String sql;
	/** 查询参数值列表 */
	private Object[] propValues;
	/** 查询参数值对象类型，必须和propValues一一对应  */
	@JsonIgnore
	private Type[] valueTypes;
	/** 单个查询条件集合，当compare=EXPRESSION_NOT时，有效 */
	private Condition condition;
	/** 查询条件集合列表，当compare=(EXPRESSION_AND, EXPRESSION_OR)时，有效 */
	private List<Condition> conditionList;

	static{
		compareMap = new HashMap<String, Integer>();
		compareStrMap = new HashMap<Integer, String>();
		compareMap.put(EQ_STR, EQ);
		compareStrMap.put(EQ, EQ_STR);
		
		compareMap.put(ALL_EQ_STR, ALL_EQ);
		compareStrMap.put(ALL_EQ, ALL_EQ_STR);
		
		compareMap.put(GT_STR, GT);
		compareStrMap.put(GT, GT_STR);
		
		compareMap.put(GE_STR, GE);
		compareStrMap.put(GE, GE_STR);
		
		compareMap.put(LT_STR, LT);
		compareStrMap.put(LT, LT_STR);
		
		compareMap.put(LE_STR, LE);
		compareStrMap.put(LE, LE_STR);
		
		compareMap.put(BETWEEN_STR, BETWEEN);
		compareStrMap.put(BETWEEN, BETWEEN_STR);
		
		compareMap.put(LIKE_STR, LIKE);
		compareStrMap.put(LIKE, LIKE_STR);
		
		compareMap.put(IN_STR, IN);
		compareStrMap.put(IN, IN_STR);
		
		compareMap.put(EQ_PROPERTY_STR, EQ_PROPERTY);
		compareStrMap.put(EQ_PROPERTY, EQ_PROPERTY_STR);
		
		compareMap.put(GT_PROPERTY_STR, GT_PROPERTY);
		compareStrMap.put(GT_PROPERTY, GT_PROPERTY_STR);
		
		compareMap.put(GE_PROPERTY_STR, GE_PROPERTY);
		compareStrMap.put(GE_PROPERTY, GE_PROPERTY_STR);
		
		compareMap.put(LT_PROPERTY_STR, LT_PROPERTY);
		compareStrMap.put(LT_PROPERTY, LT_PROPERTY_STR);
		
		compareMap.put(LE_PROPERTY_STR, LE_PROPERTY);
		compareStrMap.put(LE_PROPERTY, LE_PROPERTY_STR);
		
		compareMap.put(NOT_STR, NOT);
		compareStrMap.put(NOT, NOT_STR);
		
		compareMap.put(PK_EQ_STR, PK_EQ);
		compareStrMap.put(PK_EQ, PK_EQ_STR);
		
		compareMap.put(IS_NOT_NULL_STR, IS_NOT_NULL);
		compareStrMap.put(IS_NOT_NULL, IS_NOT_NULL_STR);
		
		compareMap.put(IS_NULL_STR, IS_NULL);
		compareStrMap.put(IS_NULL, IS_NULL_STR);
		
		compareMap.put(NE_STR, NE);
		compareStrMap.put(NE, NE_STR);
		
		compareMap.put(NE_PROPERTY_STR, NE_PROPERTY);
		compareStrMap.put(NE_PROPERTY, NE_PROPERTY_STR);
		
		compareMap.put(SQL_STR, SQL);
		compareStrMap.put(SQL, SQL_STR);
		
		compareMap.put(SQL_PARAM_STR, SQL_PARAM);
		compareStrMap.put(SQL_PARAM, SQL_PARAM_STR);
		
		compareMap.put(SQL_PARAMS_STR, SQL_PARAMS);
		compareStrMap.put(SQL_PARAMS, SQL_PARAMS_STR);
		
		compareMap.put(AND_STR, AND);
		compareStrMap.put(AND, AND_STR);
		
		compareMap.put(OR_STR, OR);
		compareStrMap.put(OR, OR_STR);
		
		compareMap.put(CONJUNCTION_STR, CONJUNCTION);
		compareStrMap.put(CONJUNCTION, CONJUNCTION_STR);
		
		compareMap.put(DISJUNCTION_STR, DISJUNCTION);
		compareStrMap.put(DISJUNCTION, DISJUNCTION_STR);
		//王斌添加2014-5-28
		compareMap.put(LLIKE_STR, ILIKE);
		compareStrMap.put(ILIKE, LIKE_STR);
	}
	
	/**
	 * <li>说明：获取 查询比较操作符 字符串
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param int compare 
	 * @return String 
	 * @throws 
	 */
	public static String getCompare(int compare){
		return compareStrMap.get(compare);
	}
	/**
	 * <li>说明：获取 查询比较操作符 整形值
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String compare
	 * @return int 
	 * @throws 
	 */
	public static int getCompare(String compare){
		return compareMap.get(compare);
	}
	/**
	 * <li>说明：根据whereList返回拼接的查询sql语句条件，暂不推荐使用该方法
	 * 该方法目前只适应普通的条件查询，不支持复合条件查询
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static String getSQLWhere(Class clazz, List<Condition> whereList){
		StringBuilder sb = new StringBuilder();
		sb.append(" 1=1");
		for (Condition con : whereList) {
			sb.append(" and ")
				.append(EntityUtil.getColumnName(clazz, con.getPropName())).append(" ")
				.append(getCompare(con.getCompare())).append(" ");
			if(con.getPropValue() != null){
				Object value = con.getPropValue();
				if(value instanceof Date){
					Date date = (Date)value;
					String dateStr = DateUtil.yyyy_MM_dd_HH_mm_ss.format(date);
					sb.append("to_date('").append(dateStr).append("','yyyy-mm-dd hh24:mi:ss')");
				} else if(value instanceof String){
					if(con.getCompare() == LIKE)	sb.append("'%").append(value).append("%'");
					else	sb.append("'").append(value).append("'");
				} else {
					sb.append(value);
				}
				
			} else if(con.getPropValues() != null){
				sb.append("(");
				Object[] values = con.getPropValues();
				for (Object value : values) {
					if(value instanceof Date){
						Date date = (Date)value;
						String dateStr = DateUtil.yyyy_MM_dd_HH_mm_ss.format(date);
						sb.append("to_date('").append(dateStr).append("','yyyy-mm-dd hh24:mi:ss')");
					} else {
						sb.append(value);
					}
					sb.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(")");
			}
		}
//		如果该实体类有逻辑删除属性则追加过滤逻辑删除的条件
		if(EntityUtil.contains(clazz, "recordStatus"))	sb.append(" and Record_Status=0");
		return sb.toString();
	}
	/**
	 * <li>说明：根据比较类型compare生成查询条件并返回
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Class entityClass：查询实体类描述
	 * @return Criterion 条件查询表达式，只适用于QBC查询模式
	 * @throws ParseException 
	 * @throws NoSuchFieldException 
	 * @throws 抛出异常列表
	 */
	public Criterion getExpression(Class entityClass) throws NoSuchFieldException, ParseException{
		processValue(entityClass);
		switch (this.compare) {
		case EQ:
			return Expression.eq(this.propName, this.propValue);
		case ALL_EQ:
			return Expression.allEq(this.eqMap);
		case GT:
			return Expression.gt(this.propName, this.propValue);
		case GE:
			return Expression.ge(this.propName, this.propValue);
		case LT:
			return Expression.lt(this.propName, this.propValue);
		case LE:
			return Expression.le(this.propName, this.propValue);
		case BETWEEN:
			return Expression.between(this.propName, this.propValues[0], this.propValues[1]);
		case LIKE:
			return Expression.like(this.propName, this.propValue.toString(), this.likeMode);
		case LLIKE:
			return Expression.like(this.propName, this.propValue.toString(), this.llikeMode);//王斌添加2014-5-28
		case IN:
			return Expression.in(this.propName, this.propValues);
		case EQ_PROPERTY:
			return Expression.eqProperty(this.propName, this.propValue.toString());
		case GT_PROPERTY:
			return Expression.gtProperty(this.propName, this.propValue.toString());
		case GE_PROPERTY:
			return Expression.geProperty(this.propName, this.propValue.toString());
		case LT_PROPERTY:
			return Expression.ltProperty(this.propName, this.propValue.toString());
		case LE_PROPERTY:
			return Expression.leProperty(this.propName, this.propValue.toString());
		case NOT:
			return Expression.not(this.condition.getExpression(entityClass));
		case PK_EQ:
			return Expression.idEq(this.propValue);
		case ILIKE:
			return Expression.ilike(this.propName, this.propValue.toString());
		case IS_EMPTY:
			return Expression.isEmpty(this.propName);
		case IS_NOT_EMPTY:
			return Expression.isNotEmpty(this.propName);
		case IS_NOT_NULL:
			return Expression.isNotNull(this.propName);
		case IS_NULL:
			return Expression.isNull(this.propName);
		case NE:
			return Expression.ne(this.propName, this.propValue);
		case NE_PROPERTY:
			return Expression.neProperty(this.propName, this.propValue.toString());
		case SIZE_EQ:
			return Expression.sizeEq(this.propName, Integer.parseInt(this.propValue.toString()));
		case SIZE_GE:
			return Expression.sizeGe(this.propName, Integer.parseInt(this.propValue.toString()));
		case SIZE_GT:
			return Expression.sizeGt(this.propName, Integer.parseInt(this.propValue.toString()));
		case SIZE_LE:
			return Expression.sizeLe(this.propName, Integer.parseInt(this.propValue.toString()));
		case SIZE_LT:
			return Expression.sizeLt(this.propName, Integer.parseInt(this.propValue.toString()));
		case SIZE_NE:
			return Expression.sizeNe(this.propName, Integer.parseInt(this.propValue.toString()));
		case SQL:
			return Expression.sqlRestriction(this.sql);
		case SQL_PARAM:
			return Expression.sqlRestriction(this.sql,this.propValues[0], this.valueTypes[0]);
		case SQL_PARAMS:
			return Expression.sqlRestriction(this.sql.toString(), this.propValues, this.valueTypes);
		case AND:
			return Expression.and(conditionList.get(0).getExpression(entityClass), conditionList.get(1).getExpression(entityClass));
		case OR:
			return Expression.or(conditionList.get(0).getExpression(entityClass), conditionList.get(1).getExpression(entityClass));
		case CONJUNCTION:
			Conjunction conjunction = Expression.conjunction();
            //修改 2015-02-02 汪东良 将condition 局部变量修改成obj开头，保证与属性变量命名不同
			for(Condition objCondition : conditionList){
				conjunction.add(objCondition.getExpression(entityClass));
			}
			return conjunction;
		case DISJUNCTION:
			Disjunction disjunction = Expression.disjunction();
			for(Condition objCondition : conditionList){ //修改 2015-02-02 汪东良 将condition 局部变量修改成obj开头，保证与属性变量命名不同
				disjunction.add(objCondition.getExpression(entityClass));
			}			
			return disjunction;
			
		default:
		}
		throw new RuntimeException("Condition.compare:无效值");
	}
	
	public Condition(){}
	/**
	 * <li>说明：构造方法，sql方式
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param String sql:原生sql构造条件
	 */
	public Condition(String sql){
		this.setCompare(SQL);
		this.setSql(sql);
	}
	/**
	 * <li>说明：构造方法，NOT（逻辑非）单条件
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param Condition condition:单个查询条件对象
	 */
	public Condition(Condition condition){
		this.setCompare(NOT);
		this.setCondition(condition);
	}
	/**
	 * <li>说明：构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param Integer compare:比较类型(OR,AND,CONJUNCTION,DISJUNCTION)之一
	 * @param List<Condition> conditionList：条件组合列表
	 */
	public Condition(Integer compare, List<Condition> conditionList){
		this.setCompare(compare);
		this.setConditionList(conditionList);
	}	
	/**
	 * <li>说明：构造方法，自动设置compare=ALL_EQ可使用该构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param String propName:属性名称
	 * @param Map eqMap：一个Map类型对象，包含多个名/值对对应关系，相当于多个Expression.eq的叠加
	 */
	public Condition(String propName, Map<String,Object> eqMap){
		this.setPropName(propName);
		this.setCompare(ALL_EQ);
		this.setEqMap(eqMap);
	}
	/**
	 * <li>说明：构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param String propName:属性名称
	 * @param Integer compare:比较类型
	 * @param Object propValue:要比较的值
	 */	
	public Condition(String propName, Integer compare, Object propValue){
		this.setPropName(propName);
		this.setCompare(compare);
		this.setPropValue(propValue);
	}
	/**
	 * <li>说明：构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param String propName:属性名称
	 * @param Integer compare:比较类型
	 * @param Object[] propValue:要比较的值列表
	 */	
	public Condition(String propName, Integer compare, Object[] propValues){
		this.setPropName(propName);
		this.setCompare(compare);
		this.setPropValues(propValues);		
	}
	/**
	 * <li>说明：构造方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param String sql:原生sql构造条件
	 * @param Object[] propValues: sql参数值数组，一一对应
	 * @param Type[] valueTypes: sql参数值对象类型数组，一一对应
	 */	
	public Condition(String sql, Object[] propValues, Type[] valueTypes){
		this.setCompare(SQL_PARAMS);
		this.setSql(sql);
		this.setPropValues(propValues);
		this.setValueTypes(valueTypes);
	}
	
	/**
	 * <li>说明：判断propValue、propValue的值是否与实体类中对应属性的值类型是否匹配，若不匹配尝试进行类型转换
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws NoSuchFieldException 
	 * @throws ParseException 
	 */
	private void processValue(Class entityClass) throws NoSuchFieldException, ParseException{
		if(entityClass == null || this.propName == null)	return;
		Class propClass = BeanUtils.getPropertyType(entityClass, this.propName);
		switch (this.compare) {
		case ALL_EQ:
			if(this.eqMap == null || this.eqMap.size() < 1)	break;
			for (Iterator iter = this.eqMap.keySet().iterator(); iter.hasNext();) {
				Object key = iter.next();
				Object value = this.eqMap.get(key);
				Class keyClass = BeanUtils.getPropertyType(entityClass, key.toString());
				if(keyClass.isInstance(value))	continue;
				value = EntityUtil.convert(value, keyClass);
				this.eqMap.put(key.toString(), value);
			}
			
		case BETWEEN:
		case IN:
			if (this.propValues == null || this.propValues.length < 1) break;
			for (int i = 0, length = this.propValues.length; i < length; i++) {
				if (this.propValues[i] == null || propClass.isInstance(this.propValues[i])) continue;
				this.propValues[i] = EntityUtil.convert(this.propValues[i], propClass);
			}
			break;
		case EQ:
		case GT:
		case GE:
		case LT:
		case LE:
		case NE:
			if (this.compare == EQ && stringLike && "java.lang.String".equals(propClass.getName())){
				this.compare = LIKE;
				break;
			}
			if (this.propValue == null || propClass.isInstance(this.propValue)) break;
			this.propValue = EntityUtil.convert(this.propValue, propClass);
			break;
		default:
		}
	}
	
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public Object[] getPropValues() {
		return propValues;
	}
	public void setPropValues(Object[] propValues) {
		this.propValues = propValues;
	}
	public Integer getCompare() {
		return compare;
	}
	public void setCompare(Integer compare) {
		this.compare = compare;
	}

	public Map<String,Object> getEqMap() {
		return eqMap;
	}

	public void setEqMap(Map<String,Object> eqMap) {
		this.eqMap = eqMap;
	}

	public MatchMode getLikeMode() {
		return likeMode;
	}

	public void setLikeMode(MatchMode likeMode) {
		this.likeMode = likeMode;
	}

	public Object getPropValue() {
		return propValue;
	}

	public void setPropValue(Object propValue) {
		this.propValue = propValue;
	}

	public List<Condition> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<Condition> conditionList) {
		this.conditionList = conditionList;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Type[] getValueTypes() {
		return valueTypes;
	}

	public void setValueTypes(Type[] valueTypes) {
		this.valueTypes = valueTypes;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	public boolean isStringLike() {
		return stringLike;
	}
	public void setStringLike(boolean stringLike) {
		this.stringLike = stringLike;
	}
}
