package com.yunda.frame.baseapp.cache.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.yunda.frame.baseapp.cache.entity.CacheInfo;
import com.yunda.frame.common.JXBaseManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: hibernate缓存管理操作类
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-11-18
 * <li>修改人: 何涛
 * <li>修改日期：2016-03-10
 * <li>修改内容：重构，将CacheInfo的内容作为配置文件CacheConfig.xml，且配置文件支持在多个文件中配置，每个项目可根据各自需求进行配置
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0 TODO 20140328汪东良：检验重构，将CacheInfo的内容作为配置文件，且配置文件支持在多个文件中配置，每个项目跟进各自需求进行配置。且此文件不需要在JX_YoGa中存放，可直接放在jxCoreFrame中即可。
 */
@Service(value = "cacheManager")
public class CacheManager extends JXBaseManager<Object, Object> {
    
    /** 缓存描述信息列表 */
    private static List<CacheInfo> infoList = new ArrayList<CacheInfo>();
    
    private static Map<String, CacheInfo> infoMap = new HashMap<String, CacheInfo>();
    
    // Modified by hetao on 2016-03-10 22:20 将CacheInfo的内容作为配置文件CacheConfig.xml，且配置文件支持在多个文件中配置，每个项目可根据各自需求进行配置
    static {
        // 读取hibernate缓存管理配置文件
        InputStream xmlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("CacheConfig.xml");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        CacheInfoHandler handler = new CacheInfoHandler();
        List<CacheInfo> cacheInfos = null;
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(xmlStream, handler);
            // 获取已配置的缓存信息集合
            cacheInfos = handler.getCacheInfos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (CacheInfo info : cacheInfos) {
            infoList.add(info);
            infoMap.put(info.getTableName(), info);
        }
    }
    
    /**
     * <li>说明：获取系统中CacheInfo.json中使用hibernate二级缓存和查询缓存的数据库表（实体类）等描述信息
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-11-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 缓存描述信息列表
     */
    public List<CacheInfo> getCacheInfo() {
        return infoList;
    }
    
    /**
     * <li>说明：清空所有注册的实体对象的hibernate二级缓存和查询缓存
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-11-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
    public void evictAll() {
        daoUtils.getSessionFactory().evictQueries();
        evict();
    }
    
    /**
     * <li>说明：清空所有在CacheInfo.json文件中注册的实体对象的hibernate二级缓存
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-11-18
     * <li>修改人：
     * <li>修改日期：
     */
    public void evict() {
        List<CacheInfo> infos = getCacheInfo();
        if (infos == null || infos.size() < 1)
            return;
        SessionFactory sf = daoUtils.getSessionFactory();
        for (CacheInfo info : infos) {
            sf.evict(info.getEntityClass());
        }
    }
    
    /**
     * <li>说明：清空所有查询缓存, 并根据全路径类名称指定的实体对象的hibernate二级缓存
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-11-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tableName 表名
     */
    public void evictEntityAndQueries(String tableName) {
        CacheInfo info = infoMap.get(tableName);
        daoUtils.getSessionFactory().evict(info.getEntityClass());
        daoUtils.getSessionFactory().evictQueries();
    }
    
}
