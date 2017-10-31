package com.yunda.jwpt.datasyncentertable.manager;

import org.apache.log4j.Logger;

import com.yunda.jwpt.business.manager.JwptBaseManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机务平台数据同步线程类
 * <li>创建人：何涛
 * <li>创建日期：2016-6-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class DataSynchronizationThread implements Runnable {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 数据同步临时表业务管理器 */
    private JwptBaseManager manager;
    
    /** 业务模块名称 */
    private String moduleName;
    
    /**
     * <li>说明：构造方法
     * <li>创建人：何涛
     * <li>创建日期：2016-6-6
     * <li>修改人： 
     * <li>修改日期：
     * @param manager 数据同步临时表业务管理器
     * @param moduleName 业务模块名称
     */
    public DataSynchronizationThread(JwptBaseManager manager, String moduleName) {
        this.manager = manager;
        this.moduleName = moduleName;
    }
    
    /**
     * <li>说明：构造方法，将默认构造方法私有化，防止手动new空对象
     * <li>创建人：何涛
     * <li>创建日期：2016-6-6
     * <li>修改人： 
     * <li>修改日期：
     */
    private DataSynchronizationThread(){
    }
    
    /**
     * <li>说明：执行数据同步
     * <li>创建人：何涛
     * <li>创建日期：2016-06-06
     * <li>修改人：何涛
     * <li>修改日期：2016-07-12
     * <li>修改内容：修改数据同步方式，使用webservice接口执行数据同步
     */
    @Override
    public void run() {
        logger.info(String.format("机务平台数据同步：%s同步开始...", this.moduleName));
        long beginTime = System.currentTimeMillis();
        int stateCode = 0;
        // Modified by hetao on 2016-07-12 修改数据同步方式，使用webservice接口执行数据同步
        if ("webservice".equalsIgnoreCase(JwptBaseManager.mode)) {
            stateCode = manager.excute();
        } else {
            stateCode = manager.excuteDataSynchronization();
        }
        logger.info(
            String.format("机务平台数据同步：%s同步结束，状态码：%d（-1：同步失败，0：同步成功，其它数字：同步失败的记录数），耗时：%d秒！",    // 打印日志样式
                this.moduleName,            // 模块名称
                stateCode,                  // 状态码：同步执行失败的记录数，0：表示数据全部同步成功
                (System.currentTimeMillis() - beginTime) / 1000)    // 耗时时长
        ); 
    }
    
}
