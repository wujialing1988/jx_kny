package com.yunda.jx.pjjx.base.recorddefine.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.recorddefine.entity.PartsCheckItemDefult;
import com.yunda.jx.pjjx.base.recorddefine.manager.PartsCheckItemDefultManager;
import com.yunda.jx.pjjx.util.HttpClientUtils;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 检测项编码控制器
 * <li>创建人：林欢
 * <li>创建日期：2016-6-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
public class PartsCheckItemDefultAction extends JXBaseAction<PartsCheckItemDefult, PartsCheckItemDefult, PartsCheckItemDefultManager>{
    
    private static final long serialVersionUID = 1L;
    
	//	通过资源文件获取url
	private static String url;
	
	//webservice.properties文件中检测项编码配置key
	private static final String PARTSCHECKITEMDEFULT = "partsCheckItemDefult";
	
	static {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
        Properties p = new Properties();
        try {
            p.load(is);
            is.close();
        } catch (IOException e) {
            throw new BusinessException("获取../src/webservice.properties文件异常，请检查配置文件！");
        }
        url = p.getProperty(PARTSCHECKITEMDEFULT).trim();
        if (StringUtil.isNullOrBlank(url)) {
            throw new BusinessException("未读取到检测项编码配置项，请检查webservice.properties文件是否正确！");
        }
		
	}	

	/** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：同步更新可视化系统检测项编码数据
     * <li>创建人：林欢
     * <li>创建日期：2016-6-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void synPartsCheckItemDefult() throws JsonMappingException, IOException {
        
        //封装返回结果数据
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            //通过http协议获取最新的检测项结果
            String jsonObject = HttpClientUtils.instence.getCloseableHttpClientInstence(url, null);
            JSONObject ob = JSONObject.parseObject(jsonObject);
            //操作结果标示
            String success = ob.getString("success");
            if (StringUtils.isNotBlank(success) && "true".equals(success)) {
//              返回的检测项数据结果
                String root = ob.getString("root");
                PartsCheckItemDefult[] pcid = JSONUtil.read(root, PartsCheckItemDefult[].class);
                
                //更新数据
                for (PartsCheckItemDefult data : pcid) {
                    this.manager.saveOrUpdate(data);
                }
            map.put(Constants.SUCCESS, true);
            }else {
                String errMsg = ob.getString("errMsg");
                map.put(Constants.SUCCESS, false);
                map.put("errMsg", errMsg);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
