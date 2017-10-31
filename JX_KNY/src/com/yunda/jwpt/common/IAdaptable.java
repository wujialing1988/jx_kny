package com.yunda.jwpt.common;

import com.yunda.frame.common.Constants;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 数据同步业务对象转换接口
 * <li>创建人：何涛
 * <li>创建日期：2016-06-01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IAdaptable {
    
    /** 英文状态下的单引号【'】 */
    public static final String SINGLE_QUOTE_MARK = Constants.SINGLE_QUOTE_MARK;
    
    /** 英文状态下的逗号【,】 */
    public static final String JOINSTR = Constants.JOINSTR;
    
    /** 英文状态下的单引号加逗号【',】 */
    public static final String SINGLE_QUOTE_MARK_JOINSTR = SINGLE_QUOTE_MARK + JOINSTR;
    
    /**
     * <li>说明：获取idx主键
     * <li>创建人：何涛
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return idx主键
     */
    public String getIdx();
    
    /**
     * <li>说明：设置idx主键
     * <li>创建人：何涛
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx idx主键
     */
    public void setIdx(String idx);
    
    /**
     * <li>说明：获取数据同步操作类型
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据同步操作类型
     */
    public Integer getOperateType();
    
    /**
     * <li>说明：设置数据同步操作类型
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operateType 数据同步操作类型
     */
    public void setOperateType(Integer operateType);
    
    /**
     * <li>说明：检修系统业务对象实体到数据同步业务对象实体的转换
     * <li>创建人：何涛
     * <li>创建日期：2016-06-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数数组，一般情况下：索引0处为检修系统业务对象实体，索引1处为操作类型
     * @return 数据同步业务对象实体
     */
    public Object adaptFrom(Object... args); 
    
    /**
     * <li>说明：获取业务临时数据表对应到总公司机务平台的数据表名称
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 总公司机务平台的数据表名称
     */
    public String getSyncTableName();
    
    /**
     * <li>说明：构建从业务临时数据表到总公司机务平台的数据表的插入sql
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据库插入sql
     */
    public String buildInsertSql();
    
    /**
     * <li>说明：构建从业务临时数据表到总公司机务平台的数据表的更新sql
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据库更新sql
     */
    public String buildUpdateSql();
    
}
