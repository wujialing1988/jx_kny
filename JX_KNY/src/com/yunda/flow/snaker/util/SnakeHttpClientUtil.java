package com.yunda.flow.snaker.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * <li>标题: Http调用工具类
 * <li>说明: 类的功能描述
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-9-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class SnakeHttpClientUtil {
    
    /** 日志工具 */
    private static Logger logger = Logger.getLogger(SnakeHttpClientUtil.class.getName());
    
    /**
     * <li>说明：http Get请求
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param url
     * @param queryString
     * @return
     */
    public static String doGet(String url, Map<String, String> params) { 
        String response = null; 
        String queryString = null ;
        HttpClient client = new HttpClient(); 
        HttpMethod method = new GetMethod(url); 
        try { 
            for (Map.Entry<String, String> entry : params.entrySet()) { 
                queryString += "&"+entry.getKey()+ "=" +entry.getValue();
            } 
            if (StringUtils.isNotBlank(queryString)){
                method.setQueryString(URIUtil.encodeQuery(queryString)); 
            }
            client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
            client.getHttpConnectionManager().getParams().setSoTimeout(3000);
            client.executeMethod(method); 
            if (method.getStatusCode() == HttpStatus.SC_OK) { 
                    response = method.getResponseBodyAsString(); 
            } 
        } catch (URIException e) { 
            logger.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e); 
        } catch (IOException e) { 
            logger.error("执行HTTP Get请求" + url + "时，发生异常！", e); 
        } finally { 
                method.releaseConnection(); 
        } 
        return response; 
    } 
    
    /**
     * <li>说明：http Post请求
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, String> params) { 
        String response = null; 
        HttpClient client = new HttpClient(); 
        HttpMethod method = new PostMethod(url); 
        String queryString = null ;
        //设置Http Post数据 
        try { 
            if (params != null) { 
                HttpMethodParams p = new HttpMethodParams(); 
                for (Map.Entry<String, String> entry : params.entrySet()) { 
                        p.setParameter(entry.getKey(), entry.getValue()); 
                        queryString += "&"+entry.getKey()+ "=" +entry.getValue();
                } 
                method.setParams(p);
                method.setQueryString(URIUtil.encodeQuery(queryString)); 
            } 
            client.executeMethod(method); 
            if (method.getStatusCode() == HttpStatus.SC_OK) { 
                    response = method.getResponseBodyAsString(); 
            } 
        } catch (IOException e) { 
            logger.error("执行HTTP Post请求" + url + "时，发生异常！", e); 
            response = "执行HTTP Post请求" + url + "时，发生异常！" ;
        } finally { 
                method.releaseConnection(); 
        } 
        return response; 
    } 
}
