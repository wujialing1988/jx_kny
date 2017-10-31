package com.yunda.frame.baseapp.cache.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.yunda.frame.baseapp.cache.entity.CacheInfo;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: hibernate缓存管理配置文件（CacheInfos.xml）操作类
 * <li>创建人：何涛
 * <li>创建日期：2016-3-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class CacheInfoHandler extends DefaultHandler {
    
    /** 描述hibernate缓存信息的实体类集合 */
    private List<CacheInfo> cacheInfos;
    
    /** 描述hibernate缓存信息的实体类 */
    private CacheInfo cacheInfo;
    
    /** 将正在解析的节点名称赋给preTag */
    private String preTag;
    
    /** 配置文件中引用的其它配置文件路径集合 */
    private List<String> files;
    
    /**
     * <li>说明：获取hibernate缓存信息的实体类集合
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return hibernate缓存信息的实体类集合
     */
    public List<CacheInfo> getCacheInfos() {
        return cacheInfos;
    }
    
    /**
     * <li>说明：解析hibernate缓存管理配置文件，获取hibernate缓存信息的实体类集合
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param xmlStream hibernate缓存管理配置文件流
     * @return hibernate缓存信息的实体类集合
     * @throws Exception
     */
    public List<CacheInfo> parse(InputStream xmlStream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        CacheInfoHandler handler = new CacheInfoHandler();
        parser.parse(xmlStream, handler);
        return handler.getCacheInfos();
    }
    
    /**
     * <li>说明：解析hibernate缓存管理配置文件，获取hibernate缓存信息的实体类集合，递归
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param path hibernate缓存管理配置文件路径
     * @param list hibernate缓存信息的实体类集合
     * @throws Exception
     */
    private void parse(String path, List<CacheInfo> list) throws Exception {
        InputStream xmlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        List<CacheInfo> cacheInfos = this.parse(xmlStream);
        list.addAll(cacheInfos);
    }
    
    /**
     * <li>说明：配置文件解析开始
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        cacheInfos = new ArrayList<CacheInfo>();
        files = new ArrayList<String>();
    }
    
    /**
     * <li>说明：配置文件解析结束，结束时读取配置的文件引用，支持在多个文件中配置
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        if (files.isEmpty()) {
            return;
        }
        for (String path : files) {
            try {
                // 递归
                this.parse(path, cacheInfos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * <li>说明：配置文件DOM节点解析开始
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param uri uri
     * @param localName localName
     * @param qName 节点名称
     * @param attributes 节点属性
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("CacheInfo".equals(qName)) {
            cacheInfo = new CacheInfo();
        } else if ("File".equals(qName)) {
            String path = attributes.getValue(0);
            if (null != path && path.trim().length() > 0) {
                files.add(path.trim());
            }
        }
        preTag = qName;// 将正在解析的节点名称赋给preTag
    }
    
    /**
     * <li>说明：配置文件DOM节点解析
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ch 节点字符集
     * @param start 开始索引
     * @param length 字符长度
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (null == preTag) {
            return;
        }
        String content = new String(ch, start, length);
        if (null == content || content.trim().length() <= 0) {
            return;
        }
        if ("tableName".equals(preTag)) {
            /** 数据库表 */
            cacheInfo.setTableName(content);
        } else if ("tableNameCN".equals(preTag)) {
            /** 数据库表 中文名称 */
            cacheInfo.setTableNameCN(content);
        } else if ("entityClass".equals(preTag)) {
            /** 实体类型 */
            try {
                cacheInfo.setEntityClass(Class.forName(content));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if ("cacheJD".equals(preTag)) {
            /** 基地版缓存 */
            cacheInfo.setCacheJD(content);
        } else if ("cacheJWD".equals(preTag)) {
            /** 机务段版缓存 */
            cacheInfo.setCacheJWD(content);
        } else if ("modifyVersion".equals(preTag)) {
            /** 实现版本 */
            cacheInfo.setModifyVersion(content);
        }
    }
    
    /**
     * <li>说明：配置文件DOM节点解析结束
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param uri uri
     * @param localName localName
     * @param qName 节点名称
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("CacheInfo".equals(qName)) {
            cacheInfos.add(cacheInfo);
            cacheInfo = null;
        }
        preTag = null;
    }
    
    /**
     * <li>说明：本地测试方法
     * <li>创建人：何涛
     * <li>创建日期：2016-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param args 方法参数
     */
    public static void main(String[] args) {
        // InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/yunda/frame/baseapp/cache/manager/CacheInfos.xml");
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("CacheConfig.xml");
        CacheInfoHandler handler = new CacheInfoHandler();
        Logger logger = Logger.getLogger(CacheInfoHandler.class);
        try {
            List<CacheInfo> list = handler.parse(is);
            logger.info(list.size());
            for (CacheInfo info : list) {
                logger.info(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
