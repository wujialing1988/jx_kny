/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

package com.yunda.jwpt.datasyncentertable.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jwpt.business.manager.JwptBaseManager;
import com.yunda.jwpt.business.manager.JwptJcjxjhJhmxManager;
import com.yunda.jwpt.business.manager.JwptJcjxjhManager;
import com.yunda.jwpt.business.manager.JwptJxdzhgzManager;
import com.yunda.jwpt.business.manager.JwptJxrbManager;
import com.yunda.jwpt.common.IBaseBusinessDataUpdateJob;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.jwpt.utils.BaseBusinessDataUpdateJobUtils;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 系统临时中间表业务manager
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Service(value = "jwptDataSynchronizationManager")
public class JwptDataSynchronizationManager extends JXBaseManager<JwptDataSynchronizationCenterTable, JwptDataSynchronizationCenterTable>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** JwptJxdzhgz业务类,数据同步临时表-机车检修电子合格证（主表） */
    @Resource
    private JwptJxdzhgzManager jwptJxdzhgzManager;
    
    /** JxglJxrb业务类,机车检修日报 */
    @Resource
    private JwptJxrbManager jwptJxrbManager;
    
    /** JxglJcjxjh业务类,机车生产计划 */
    @Resource
    private JwptJcjxjhManager jwptJcjxjhManager;
    
    /** JxglJcjxjhJhmx业务类,机车生产计划明细 */
    @Resource
    private JwptJcjxjhJhmxManager jwptJcjxjhJhmxManager;
    
    /**
     * <li>说明：分发数据到对应的业务Job处理
     * <li>创建人：林欢
     * <li>创建日期：2016-5-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */ 
    public void startGiveOutDataToBusinessUpadeJob() {
        // 获取所有的中间表信息
        List<JwptDataSynchronizationCenterTable> dataSynchronizationCenterTableList = getAll();
        // 判断系统中间表是否有数据
        if (null == dataSynchronizationCenterTableList || dataSynchronizationCenterTableList.isEmpty()) {
            return;
        }
        
        // 业务表名称
        String businessTableName = null;
        List<JwptDataSynchronizationCenterTable> jdsctList = null;
        // 按业务模块将数据同步中间表数据进行分组，使用Map<String, List<?>>进行存储
        // key：业务表名 value：业务数据操作过程记录
        Map<String, List<JwptDataSynchronizationCenterTable>> dataBusinessMap = new HashMap<String, List<JwptDataSynchronizationCenterTable>>();
        for (JwptDataSynchronizationCenterTable jdsct : dataSynchronizationCenterTableList) {
            // 业务表名称
            businessTableName = jdsct.getBusinessTableName();
            
            // 系统中间表数据List(当前业务表名下的)
            jdsctList = dataBusinessMap.get(businessTableName);
            // 判断dataBusinessMap中，是否有List存放数据
            if (null == jdsctList) {
                jdsctList = new ArrayList<JwptDataSynchronizationCenterTable>();
                dataBusinessMap.put(businessTableName, jdsctList);
            }
            jdsctList.add(jdsct);
        }
        
        // 循环遍历dataBusinessMap数据，执行业务数据到临时表的存储
        for (Entry<String, List<JwptDataSynchronizationCenterTable>> entry : dataBusinessMap.entrySet()) {
            // 将业务表名称全部转化为小写，应为manager定义的就是小写的业务表名称
            Object bean = BaseBusinessDataUpdateJobUtils.instence.getBean(entry.getKey().toLowerCase());
            if (null == bean || !(bean instanceof IBaseBusinessDataUpdateJob)) {
                continue;
            }
            try {
                // 准备List存放各个业务方法处理后，返回的系统中间表 JwptDataSynchronizationCenterTable 主键IDX
                List<JwptDataSynchronizationCenterTable> successList = IBaseBusinessDataUpdateJob.class.cast(bean).updateDataByBusiness(entry.getValue());
                if (null == successList || successList.isEmpty()) {
                    continue;
                }
                // 删除系统临时表
                this.getDaoUtils().getHibernateTemplate().deleteAll(successList);
            } catch (Exception e) {
                logger.error(String.format("存储业务数据JWPT_DATA_SYN_CENTER_TABLE[BUSINESS_TABLE_NAME='%s']到本地数据同步临时表异常！", businessTableName), e);
                continue;
            }
        }
    }
    
    /**
     * <li>说明：执行数据同步，同步本地临时表中的增量数据到总公司机务平台数据库
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
    public void excuteDataSynchronization() {
        // 验证是否开启了数据同步功能
        if (StringUtil.isNullOrBlank(JwptBaseManager.mode)) {
            return;
        }
        // 机车检修日报数据同步
        Thread thread = new Thread(new DataSynchronizationThread(jwptJxrbManager, "机车检修日报"));
        thread.start();
        
        // 机车检修电子合格证数据同步
        thread = new Thread(new DataSynchronizationThread(jwptJxdzhgzManager, "机车检修电子合格证"));
        thread.start();
        
        // 机车生产计划数据同步
        thread = new Thread(new DataSynchronizationThread(jwptJcjxjhManager, "机车生产计划"));
        thread.start();
        
        // 机车生产计划明细数据同步
        thread = new Thread(new DataSynchronizationThread(jwptJcjxjhJhmxManager, "机车生产计划明细"));
        thread.start();
    }
    
    public static void main(String[] args) {
        System.out.println(String.format("存储业务数据JWPT_DATA_SYN_CENTER_TABLE[BUSINESS_TABLE_NAME='%s']到本地数据同步临时表异常！", "jwpt_jxrp"));
    }
    
}
