package com.yunda.jwpt.business.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jwpt.common.IAdaptable;
import com.yunda.jwpt.common.IBaseBusinessDataUpdateJob;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机务平台数据同步抽象管理器
 * <li>创建人：何涛
 * <li>创建日期：2016-06-01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @param <T> 业务实体泛型
 * @param <S> 业务查询实体泛型
 * @version 1.0
 */
public abstract class JwptBaseManager<T extends IAdaptable, S> extends JXBaseManager<T, S> {
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机务平台数据同步webservice接口方法名称 */
    private static final String JWPT_WSDL_EXCUTE = "excute";
    
    /** 机务平台数据同步webservice的URL路径 */
    private static String URL;
    
    /** 数据同步模式(开关)，可配置选项有：webservice、dblinks，优先推荐webservice模式，如果不配置该项，则不执行数据同步功能 */
    public static String mode;
    
    /** 实体类全路径名称 - 机车检修日报 */
    protected static String jwptJxrb;
    
    /** 实体类全路径名称 - 机车生产计划 */
    protected static String jwptJcjxjh;
    
    /** 实体类全路径名称 - 机车生产计划明细 */
    protected static String jwptJcjxjhJhmx;
    
    /** 实体类全路径名称 - 机车检修电子合格证 */
    protected static String jwptJxdzhgz;
    
    static {
        try {
            // 初始化webservice的URL路径
            initWebServiceURL();
        } catch (Exception e) {
            Logger.getLogger(JwptBaseManager.class).error("业务层捕获异常：", e);
        }
    }
    
    /**
     * <li>说明：从webservice.properties配置文件中获取机务平台数据同步发布的webservice服务的url路径，仅在类加载时执行一次
     * <li>创建人：何涛
     * <li>创建日期：2016-07-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
    private static void initWebServiceURL() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
        Properties p = new Properties();
        try {
            p.load(is);
            is.close();
        } catch (IOException e) {
            throw new BusinessException("获取../src/webservice.properties文件异常，请检查配置文件！");
        }
        String url = p.getProperty("jwpt.sync.url");
        if (StringUtil.isNullOrBlank(url)) {
            throw new BusinessException("未读取到JWPT.url配置项，请检查webservice.properties文件是否正确！");
        }
        mode = p.getProperty("jwpt.sync.mode").trim();
        jwptJxrb = p.getProperty("jwpt.sync.jwptJxrb").trim();
        jwptJcjxjh = p.getProperty("jwpt.sync.jwptJcjxjh").trim();
        jwptJcjxjhJhmx = p.getProperty("jwpt.sync.jwptJcjxjhJhmx").trim();
        jwptJxdzhgz = p.getProperty("jwpt.sync.jwptJxdzhgz").trim();
        URL = url.trim();
    }
    
    /**
     * <li>说明：重写插入方法，插入记录到数据同步临时表
     * <li>创建人：何涛
     * <li>创建日期：2016-05-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 数据同步临时表实体对象
     * @return 插入成功后的数据同步临时表实体对象
     * @throws BusinessException
     */
    @Override
    public T insert(T t) throws BusinessException {
        try {
            EntityUtil.setNoDelete(t);
        } catch (NoSuchFieldException e) {
            logger.error("实体异常，未获取到实体的recordStatus属性！", e);
        }
        return super.insert(t);
    }
    
    /**
     * <li>说明：重写保存更新方法，数据同步操作无需进行EntityUtil.setSysinfo(t);
     * <li>创建人：何涛
     * <li>创建日期：2016-05-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 数据同步临时表实体对象
     */
    @Override
    public void saveOrUpdate(T t) throws BusinessException, NoSuchFieldException {
        //设置逻辑删除字段状态为未删除
        t = EntityUtil.setNoDelete(t);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
    }
    
    /**
     * <li>说明：执行数据同步，同步本地临时表中的增量数据到总公司机务平台数据库【oracle数据库Database links数据库直连方式】
     * <li>创建人：何涛
     * <li>创建日期：2016-06-02
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 同步执行失败的记录数，0：表示数据全部同步成功
     */
    @Deprecated
    public int excuteDataSynchronization() {
        // 获取本地临时表中的所有增量数据
        List<T> all = this.getAll();
        if (null == all || 0 >= all.size()) {
            return 0;
        }
        // 执行失败的记录idx主键集合，用于日志记录
        List<String> failedIdxList = new ArrayList<String>();
        // 执行成功的记录idx主键集合，用于在操作成功后删除增量的临时表数据
        List<String> successIdxList = new ArrayList<String>();
        Integer operateType = null;
        for (T t : all) {
            operateType  = t.getOperateType();
            if (null == operateType) {
                failedIdxList.add(t.getIdx());
                continue;
            }
            try {
                // 根据数据同步操作类型执行相应的数据库更新，当前使用的是依次单条执行的方式
                switch (operateType) {
                    case IBaseBusinessDataUpdateJob.INSERT:     // 插入
                        this.insertSyhc(t);
                        break;
                    case IBaseBusinessDataUpdateJob.UPDATE:     // 更新
                        this.updateSyhc(t);
                        break;
                    case IBaseBusinessDataUpdateJob.DELETE:     // 删除
                        this.deleteSyhc(t);
                        break;
                    default:
                        break;
                }
                successIdxList.add(t.getIdx());
            } catch (Exception e) {
                failedIdxList.add(t.getIdx());
                if (e instanceof InvalidDataAccessResourceUsageException) {
                    logger.error("请检查与总公司的机务平台数据库Database links连接是否正常，数据表[" + t.getSyncTableName() + "]" , e);
                }
                continue;
            }
        }
        // 如果有执行失败的记录，则打印日志信息
        if (0 < failedIdxList.size()) {
            logger.error("机务平台数据同步异常，数据表：" + this.getTempTableName() + "\r\n" + failedIdxList);
        }
        // 删除数据同步执行成功的数据记录
        if (0 < successIdxList.size()) {
            this.deleteByIds(successIdxList.toArray((Serializable[])new String[successIdxList.size()]));
        }
        return failedIdxList.size();
    }
    /**
     * <li>说明：执行数据同步，同步本地临时表中的增量数据到总公司机务平台数据库【webservice接口调用方式】
     * <li>创建人：何涛
     * <li>创建日期：2016-07-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据同步执行状态码，-1：同步失败，0：同步成功，其它数字：同步失败的记录数
     */
    public int excute() {
        // 获取本地临时表中的所有增量数据
        List<T> all = this.getAll();
        if (null == all || 0 >= all.size() || null == URL || 0 > URL.length()) {
            return 0;
        }
        Object[] results = null;
        try {
            // 调用总公司机务平台webservice接口，执行数据同步
            Client client = new Client(new java.net.URL(URL));
            results = client.invoke(JWPT_WSDL_EXCUTE, new Object[] {JSONUtil.write(all), this.getEntityClass()});
        } catch (Exception e) {
            logger.error("机务平台数据同步异常！请检查连接是否正常：" + URL, e);
            return -1;
        }
        JSONObject json = JSONObject.parseObject(results[0].toString());
        JSONArray errMsg = json.getJSONArray("errMsg");
        if (!json.getBoolean("success")) {
            logger.error(this.getEntityClass() + " 机务平台数据同步异常：" + errMsg.get(0).toString());
            return -1;
        }
        if (errMsg.size() > 0) {
            logger.error(this.getEntityClass() + " 机务平台数据同步异常：");
            for (int i = 0; i < errMsg.size(); i++) {
                logger.error(errMsg.get(i).toString());
            }
        }
        JSONArray successIds = json.getJSONArray("successIds");
        Serializable[] ids = successIds.toArray(new Serializable[successIds.size()]);
        if (0 < ids.length) {
            // 从同步临时表中删除已经同步成功的业务数据
            this.deleteByIds(ids);
        }
        // 返回同步执行失败的记录数
        return all.size() - ids.length;
    }
    
    /**
     * <li>说明：获取总公司机务平台同步数据表对应的java实体类的全路径名称
     * <li>创建人：何涛
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 总公司机务平台同步数据表对应的java实体类的全路径名称，例如：com.yunda.jwpt.entity.JwptJxrb
     */
    protected abstract String getEntityClass();

    /**
     * <li>说明：机务平台数据同步-插入（以Database links方式建立到机务平台的数据库连接）
     * <li>创建人：何涛
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 数据同步业务实体对象
     * @return 数据库执行成功记录数
     */
    protected int insertSyhc(T t) {
        String sql = t.buildInsertSql();
        if (null == sql) {
            return -1;
        }
        return this.daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：机务平台数据同步-更新（以Database links方式建立到机务平台的数据库连接）
     * <li>创建人：何涛
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 数据同步业务实体对象
     * @return 数据库执行成功记录数
     */
    protected int updateSyhc(T t) {
        String sql = t.buildUpdateSql();
        if (null == sql) {
            return -1;
        }
        return this.daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：机务平台数据同步-删除（以Database links方式建立到机务平台的数据库连接）
     * <li>创建人：何涛
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 数据同步业务实体对象
     * @return 数据库执行成功记录数
     */
    protected int deleteSyhc(T t) {
        StringBuilder sb = new StringBuilder(30);
        sb.append("DELETE FROM ").append(t.getSyncTableName());
        sb.append(" WHERE ");
        sb.append(this.getPrimaryKeyName()).append(" = '").append(t.getIdx()).append("'");
        return this.daoUtils.executeSql(sb.toString());
    }

    /**
     * <li>说明：获取检修管理系统本地业务临时数据表名称
     * <li>创建人：何涛
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 业务临时数据表名称
     */
    protected String getTempTableName() {
        // 获取@Table注解
        Table tableAnno = entityClass.getAnnotation(Table.class);
        return tableAnno.name();
    }
    
    /**
     * <li>说明：获取检修管理系统本地业务临时数据表主键字段名称
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 业务临时数据表主键字段名称
     */
    protected String getPrimaryKeyName() {
        Field idxField = null;
        try {
            idxField = entityClass.getDeclaredField("idx");
        } catch (SecurityException e) {
            logger.error(e);
        } catch (NoSuchFieldException e) {
            logger.error("未获取到idx属性[" + entityClass.getName() + "]", e);
        }
        Column idxAanno = idxField.getAnnotation(Column.class);
        return idxAanno.name();
    }
    
    /**
     * <li>说明：转换日期对象为sql语句
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param date 日期对象
     * @return 日期对象为sql语句，形如：to_date('2016-06-04 11:56:00', 'yyyy-mm-dd hh24:mi:ss')";
     */
    public static String toDate(Date date) {
        return toDate(date, null);
    }
    
    /**
     * <li>说明：转换日期对象为sql语句
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param date 日期对象
     * @param fmt 日期格式，可以为空，如果为空则默认以"yyyy-MM-dd HH:mm:ss"格式化日期字符串
     * @return 日期对象为sql语句，形如：to_date('2016-06-04 11:56:00', 'yyyy-mm-dd hh24:mi:ss')";
     */
    public static String toDate(Date date, String fmt) {
        if (null == date) {
            return null;
        }
        if (null == fmt) {
            fmt = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(fmt);
        String dateStr = df.format(date);
        return "to_date('"+ dateStr +"', 'yyyy-mm-dd hh24:mi:ss')";
    }
    
    /**
     * <li>说明：单元测试代码
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数数组
     */
    public static void main(String[] args) {
        Logger.getLogger(JwptBaseManager.class).info(toDate(new Date(), null));
    }
    
}
