package com.yunda.frame.baseapp.cache.entity;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 描述hibernate缓存信息的实体类
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-11-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class CacheInfo {
	/** 数据库表 */
	private String tableName;
	/** 数据库表 中文名称 */
	private String tableNameCN;
	/** 实体类型 */
	private Class entityClass;
//	private String entityName;
	/** 基地版缓存 */
	private String cacheJD;
	/** 机务段版缓存 */
	private String cacheJWD;
	/** 实现版本 */
	private String modifyVersion;
	
	public CacheInfo(){}
	
	public CacheInfo(String tableName, String tableNameCN, Class entityClass, String cacheJD, String cacheJWD, String modifyVersion){
		this.tableName = tableName;
		this.tableNameCN = tableNameCN;
		this.entityClass = entityClass;
		this.cacheJD = cacheJD;
		this.cacheJWD = cacheJWD;
		this.modifyVersion = modifyVersion;
	}
	
	public String getCacheJD() {
		return cacheJD;
	}
	public void setCacheJD(String cacheJD) {
		this.cacheJD = cacheJD;
	}
	public String getCacheJWD() {
		return cacheJWD;
	}
	public void setCacheJWD(String cacheJWD) {
		this.cacheJWD = cacheJWD;
	}
	public String getModifyVersion() {
		return modifyVersion;
	}
	public void setModifyVersion(String modifyVersion) {
		this.modifyVersion = modifyVersion;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableNameCN() {
		return tableNameCN;
	}
	public void setTableNameCN(String tableNameCN) {
		this.tableNameCN = tableNameCN;
	}
	public Class getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	
}
