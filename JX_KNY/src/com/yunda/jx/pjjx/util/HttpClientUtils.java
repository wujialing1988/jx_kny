package com.yunda.jx.pjjx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.yunda.common.BusinessException;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：HttpClient工具类
 * <li>创建人：林欢
 * <li>创建日期： 2016-6-12 下午03:12:50
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public enum HttpClientUtils {
	instence;

	public static final String UTF8 = "utf-8";
	public static final String KSHFLAG = "kshFlag";
    private static Properties p;
    

	/**
	 * <li>说明：HttpClient Post 通过传递参数，获取返回值
	 * <li>创建人：林欢
	 * <li>创建日期： 2016-06-12
	 * <li>修改人:
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param paramsList
	 *            查询参数
	 * @return 返回值
	 * @throws IOException
	 * @throws HttpException
	 */
	public String getCloseableHttpClientInstence(String url,
			Map<String, Object> json) throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		try {
//			Map<String, Object> json1 = new HashMap<String, Object>();
//			json1.put("partID", "P1");
//			String[] checkIDs = { "dphsyt_2", "dphsyt_1" };
//			json1.put("checkIDs", checkIDs);
//			json1.put("startTime", "2008-05-12 14:01:38 253");
//			json1.put("endTime", "2008-06-12 17:01:38 253");
//			//封装数据到post中
//			if (json1 != null && !json1.isEmpty()) {
//				StringEntity s = new StringEntity(JSON.toJSONString(json1));
//				s.setContentType("application/json;charset=utf-8");// 发送json数据需要设置contentType
//				post.setEntity(s);
//			}
			//封装数据到post中
			if (json != null && !json.isEmpty()) {
				StringEntity s = new StringEntity(JSON.toJSONString(json));
				s.setContentType("application/json;charset=utf-8");// 发送json数据需要设置contentType
				post.setEntity(s);
			}
			
			//执行postHTTP请求
			HttpResponse res = client.execute(post);
			//当请求响应为200的时候表示成功
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                getProperties().setProperty(KSHFLAG, "true");
				return result;
			}
		} catch (Exception e) {
            //如果可视化系统连接超时，直接设置可视化系统启用为false
            getProperties().setProperty(KSHFLAG, "false");
//            throw new BusinessException("可视化系统连接超时！");
            return "调用失败";
		}
//      如果可视化系统连接超时，直接设置可视化系统启用为false
        getProperties().setProperty(KSHFLAG, "false");
		return "调用失败";

	}
    
    /**
     * <li>说明：HttpClient Post 通过传递参数，获取返回值
     * <li>创建人：林欢
     * <li>创建日期： 2016-06-12
     * <li>修改人:
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param paramsList
     *            查询参数
     * @return 返回值
     * @throws IOException
     * @throws HttpException
	 * @return Properties 配置文件对象
     */
    public Properties getProperties(){
        
        if (p == null ) {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
            p = new Properties();
            try {
                p.load(is);
                is.close();
            } catch (IOException e) {
                throw new BusinessException("获取../src/webservice.properties文件异常，请检查配置文件！");
            }
        }
        return p;
    }
}
