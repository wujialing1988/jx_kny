package com.yunda.jx.scdd.repairplan.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.scdd.repairplan.entity.BjbfRunningKM;
import com.yunda.jx.scdd.repairplan.entity.RunningKM;
import com.yunda.jxpz.trainshort.entity.TrainShort;
import com.yunda.jxpz.trainshort.manager.TrainShortManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：何涛
 * <li>创建日期：2016-4-30
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "bjbfRunningKMManager")
public class BjbfRunningKMManager extends JXBaseManager<RunningKM, RunningKM> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(BjbfRunningKMManager.class);
    
    /** 北京博飞webservice配件文件配置项 */
    private static final String BJBF_WSDL_CONFIG = "bjbf.url";
    
    /** 北京博飞webservice接口方法名称 */
    private static final String BJBF_WSDL_GET_RUNNING_KM = "getRunningKM";
    
    /** 北京博飞webservice的URL路径 */
    private static String URL;

    /** TrainType业务类,机车车型编码 */
    @Resource
    private TrainTypeManager trainTypeManager;
    
    /** 简称短称对应表业务类 */
    @Resource
    private TrainShortManager trainShortManage;
    
    @Resource
    private RunningKMManager runningKMManager ;
    
    static {
        try {
            // 初始化webservice的URL路径
            initWebServiceURL();
        } catch (Exception e) {
            Logger.getLogger(BjbfRunningKMManager.class).error("业务层捕获异常：", e);
        }
    }
    
    /**
     * <li>说明：从webservice.properties配置文件中获取北京博飞发布的走行公里webservice服务的url路径，仅在类加载时执行一次
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
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
        String url = p.getProperty(BJBF_WSDL_CONFIG);
        if (StringUtil.isNullOrBlank(url)) {
            throw new BusinessException("未读取到bjbf.url配置项，请检查webservice.properties文件是否正确！");
        }
        URL = url.trim();
    }
    
    /**
     * <li>说明：调用北京博飞运安系统webservice接口，获取走行公里数据
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 走行公里数据集合
     */
    private List<BjbfRunningKM> getRunningKM() {
        if (null == URL || 0 > URL.length()) {
            return null;
        }
        Object[] results = null;
        try {
            // 调用北京博飞运安系统webservice接口，获取走行公里数据
            Client client = new Client(new java.net.URL(URL));
            
            results = client.invoke(BJBF_WSDL_GET_RUNNING_KM, new Object[] {});
            if (null == results || 0 >= results.length) {
                return null;
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return null;
        }
        /*
         * 数据结构为：{ "flag": "true", "data": "[{}, {}]" // 获取数据出错时，该字段为错误消息 }
         */
        logger.info("运安系统走行公里接口数据：" + results[0].toString());
        JSONObject jObject = JSONObject.parseObject(results[0].toString());
        boolean flag = jObject.getBooleanValue("flag");
        if (!flag) {
            logger.error("北京博飞运安系统webservice接口，获取走行公里数据失败：" + jObject.getString("data"));
            return null;
        }
        
        List<BjbfRunningKM> list = new ArrayList<BjbfRunningKM>();
        // 解析获取到的JSON数据
        BjbfRunningKM[] runningKMs = null;
        try {
            runningKMs = JSONUtil.read(jObject.getString("data"), BjbfRunningKM[].class);
        } catch (Exception e) {
            logger.error(results[0]);
            ExceptionUtil.process(e, logger);
            return null;
        }
        for (BjbfRunningKM km : runningKMs) {
            // 验证车号是否为4位，如果不足4位，则在左侧补零（0）
            km.checkCh();
            list.add(km);
        }
        return list;
    }
    
    /**
     * <li>说明：保存从运安系统提供的查询接口获取的所有机车最新的走行公里数据，同一车型、车号的仅有存储一条记录，重复获取时进行更新操作
     * <li>创建人：何涛
     * <li>创建日期：2016-4-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchFieldException
     * @throws BusinessException
     */
    public void saveRunningKM() throws BusinessException, NoSuchFieldException {
        // 调用北京博飞运安系统webservice接口，获取走行公里数据
        List<BjbfRunningKM> list = this.getRunningKM();
        if (null == list || list.isEmpty()) {
            return;
        }
        RunningKM entity = null;
        List<RunningKM> entityList = new ArrayList<RunningKM>(list.size());
        for (BjbfRunningKM km : list) {
            
            TrainType trainType = trainTypeManager.getTrainType(km.getCx());
            if(null == trainType){
                // 如果查询不到，则查询车型简称中间表进行匹配，再查询
                TrainShort tShort = trainShortManage.getTrainShortByShort(km.getCx());
                if(null != tShort){
                    trainType = trainTypeManager.getTrainType(tShort.getTrainShort());
                }
            }
            
            // 如果数据库已经存储了同一车型、车号的走行公里数据，则进行更新
            if(trainType != null){
                entity = this.getModelByCxCh(trainType.getShortName(), km.getCh());
            }else{
                entity = this.getModelByCxCh(km.getCx(), km.getCh());
            }
            
            if (null == entity) {
                // 否则进行插入操作
                entity = new RunningKM();
                entity.trainType(km.getCx()).trainNo(km.getCh());       // 车型车号
                entity.siteid(EntityUtil.findSysSiteId(null));          // 设置站点标识
                if (null != trainType) {
                    entity.trainTypeIdx(trainType.getTypeID());         // 设置车型主键
                    entity.setTrainType(trainType.getShortName());      // 车型简称
                    entity.setRepairType(trainType.getRepairType());    // 设置修程类型
                }
            }
            // 更新各个走行公里数据
            entity.c1(km.getFxgl()).c2(km.getXxgl()).c3(km.getZxgl()).c4(km.getDxgl()).newRunningKm(km.getLjgl());
            // 修改20160830 by wujl c1 c2辅修 c3小修 c4 c5中修 c6大修
            //entity.c1(km.getFxgl()).c2(km.getFxgl()).c3(km.getXxgl()).c4(km.getZxgl()).c5(km.getZxgl()).c6(km.getDxgl()).newRunningKm(km.getLjgl());
            entity.regDate(Calendar.getInstance().getTime());       // 设置登记日期
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
        this.daoUtils.flush();
        // 自动计算下次走形公里
        try {
			runningKMManager.updateComputeNextRepair(RunningKM.TYPE_C1C6);
			runningKMManager.updateComputeNextRepair(RunningKM.TYPE_FXDX);
		} catch (Exception e) {
			logger.error("按走形公里计算下次修程，调用时间："+new Date());
		}
    }
    
    /**
     * <li>说明：根据车型、车号获取走行公里数据
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainType 车型
     * @param trainNo 车号
     * @return 走行公里数据对象
     */
    private RunningKM getModelByCxCh(String trainType, String trainNo) {
        String hql = "From RunningKM Where trainType = ? And trainNo = ?";
        return (RunningKM) this.daoUtils.findSingle(hql, new Object[] { trainType, trainNo });
    }
    
    /**
     * <li>说明：单元测试方法
     * <li>创建人：何涛
     * <li>创建日期：2016-4-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数
     */
    public static void main(String[] args) {
        BjbfRunningKMManager m = new BjbfRunningKMManager();
        m.getRunningKM();
    }
    
}
