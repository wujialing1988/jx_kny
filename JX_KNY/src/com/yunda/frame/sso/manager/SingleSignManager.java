package com.yunda.frame.sso.manager;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.sso.entity.SingleSign;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 单点登录业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-8-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Service(value = "singleSignManager")
public class SingleSignManager extends JXBaseManager<SingleSign, SingleSign>{
    
    /**
     * <li>说明：获取url字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-8-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sign 单点登录实体类对象
     * @return url字符串
     * @throws Exception
     */
    public String getUrl(SingleSign sign) throws Exception {
        String queryUrl = sign.getQueryUrl();
        String url = JXConfig.getInstance().getAppURL();
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        if (StringUtil.isNullOrBlank(queryUrl)) {
            return url;
        }
        url = url.concat(queryUrl);
        
        // 获取查询参数并将参数设置到url中返回。
        if (StringUtil.isNullOrBlank(sign.getQueryStr())) {
            return url;
        }
        // 拼接url中的查询参数
        Map querymap =  JSONUtil.read(sign.getQueryStr(), HashMap.class);
        StringBuilder params = new StringBuilder();
        for(Object queryKey : querymap.keySet()){
            //对传入的参数进行加密
            params.append("&").append(queryKey).append("=").append(URLEncoder.encode((String)querymap.get(queryKey), "utf-8"));
        }
        if (params.length() <= 0) {
            return url;
        }
        String joinStr = url.contains("?") ? "&" : "?";
        return url.concat(joinStr).concat(params.substring(1));
    }    
}
