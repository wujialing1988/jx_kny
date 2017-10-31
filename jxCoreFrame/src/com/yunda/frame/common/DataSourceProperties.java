package com.yunda.frame.common;

import com.yunda.common.PropertiesUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该类提供检修系统相关配置信息，对应属性文件JXSystem.properties
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class DataSourceProperties{
	
	/** 驱动程序类名 */
	public final static String JDBC_DRIVER_CLASS_NAME = PropertiesUtil.getInstance().getPropValue("dataSource", "jdbc.driverClassName");
	/** 连接地址url */
	public final static String JDBC_URL = PropertiesUtil.getInstance().getPropValue("dataSource", "jdbc.url");	
	/** 用户名 */
	public final static String JDBC_USERNAME = PropertiesUtil.getInstance().getPropValue("dataSource", "jdbc.username");
	/** 密码 */
	public final static String JDBC_PASSWORD = PropertiesUtil.getInstance().getPropValue("dataSource", "jdbc.password");
	/** hibernate方言 */
	public final static String HIBERNATE_DIALECT = PropertiesUtil.getInstance().getPropValue("dataSource", "hibernate.dialect");
	/** 数据库类型 */
	public final static String DRUID_DBTYPE = PropertiesUtil.getInstance().getPropValue("dataSource", "druid.dbType");	
	/** 初始化连接数大小  */
	public final static int DRUID_INITIALSIZE = Integer.parseInt( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.initialSize") );
	/** 最小活动连接数  */
	public final static int DRUID_MINIDLE = Integer.parseInt( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.minIdle") );	
	/** 最大活动连接数  */
	public final static int DRUID_MAXACTIVE = Integer.parseInt( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.maxActive") );
	/** 配置获取连接等待超时的时间，单位是毫秒  */
	public final static int DRUID_MAXWAIT = Integer.parseInt( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.maxWait") );
	/** 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒  */
	public final static int DRUID_TIME_BETWEEN_EVICTION_RUNS_MILLIS = Integer.parseInt( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.timeBetweenEvictionRunsMillis") );
    /** 配置一个连接在池中最小生存的时间，单位是毫秒 */
    public final static int DRUID_MIN_EVICTABLE_IDLE_TIME_MILLIS = Integer.parseInt( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.minEvictableIdleTimeMillis") );
    /** 验证连接可用性的查询语句 */
    public final static String DRUID_VALIDATION_QUERY = PropertiesUtil.getInstance().getPropValue("dataSource", "druid.validationQuery");
    /**  */
    public final static boolean DRUID_TEST_WHILE_IDLE = Boolean.parseBoolean( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.testWhileIdle") );
    /**  */
    public final static boolean DRUID_TEST_ON_BORROW = Boolean.parseBoolean( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.testOnBorrow") );
    /**  */
    public final static boolean DRUID_TEST_ON_RETURN = Boolean.parseBoolean( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.testOnReturn") );
    /** 打开removeAbandoned功能 */
    public final static boolean DRUID_REMOVE_ABANDONED = Boolean.parseBoolean( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.removeAbandoned") );
    /** 超过规定时间未释放则会废弃该连接，单位是秒 */
    public final static int DRUID_REMOVE_ABANDONED_TIMEOUT_MILLIS = Integer.parseInt( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.removeAbandonedTimeoutMillis") );
    /** 关闭abanded连接时输出错误日志 */
    public final static boolean DRUID_LOG_ABANDONED = Boolean.parseBoolean( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.logAbandoned") );
    /** 打开PSCache，mysql 不使用 */
    public final static boolean DRUID_POOL_PREPARED_STATEMENTS = Boolean.parseBoolean( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.poolPreparedStatements") );
    /** 并且指定每个连接上PSCache的大小 */
    public final static int DRUID_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE = Integer.parseInt( PropertiesUtil.getInstance().getPropValue("dataSource", "druid.maxPoolPreparedStatementPerConnectionSize") );
    /** 配置监控统计拦截的filters */
    public final static String DRUID_FILTERS = PropertiesUtil.getInstance().getPropValue("dataSource", "druid.filters");    
    
}