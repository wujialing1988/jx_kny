package com.yunda.twt.asynchronouslogin.manager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.manager.ISysFunctionManager;
import com.yunda.twt.asynchronouslogin.entity.TWTAsynchronmousLongin;
import com.yunda.twt.common.ConstantsTWT;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 异步登陆业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-4-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "asynchronousLoginManager")
public class AsynchronousLoginManager extends JXBaseManager<Object, Object> {
    
    /** 机车出入段台账查询业务类 */
    @Resource
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /**
     * 系统应用功能查询功能接口
     */
    @Resource
    private ISysFunctionManager sysFunctionManager;
    
    private static final String QUESTION_MARK = "?";
    
    private static final String LINK_MARK = "&";
    
    /**
     * <li>说明：根据机车别名和应用功能名称获取对应应用功能url
     * <li>创建人：程锐
     * <li>创建日期：2015-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param queryStr 带机车别名的JSON查询参数
     * @param funcname 应用功能名称
     * @return 对应应用功能url
     * @throws Exception 
     */
    @Deprecated
    public String getFunctionUrl(String queryStr, String funcname) throws Exception {
        TrainAccessAccount entity = new TrainAccessAccount();
        AcFunction function = sysFunctionManager.findByFuncnameAndAppcode(funcname, ConstantsTWT.APP_CODE_TWT_CLIENT);
        if (function == null)
            throw new BusinessException("无对应的应用功能");
        String url = JXConfig.getInstance().getAppURL().concat(function.getFuncaction());
        //如果查询字符串为null或为空则直接返回访问地址（例如机车入段功能）；
        if(queryStr==null || "".equals(queryStr)){
            return  url;
        }
        //获取车的查询参数并将参数设置到url中返回。
         Map querymap =  JSONUtil.read(queryStr, HashMap.class);
         String trainInfo  = (String)querymap.get("trainInfo");
        entity.setTrainAliasName(trainInfo);
        TrainAccessAccount account = trainAccessAccountQueryManager.getAccountByTrainInfo(JSONUtil.write(entity));
        if (account == null) 
            throw new BusinessException("无对应的入段机车");
       
        StringBuilder params = new StringBuilder();
        String joinStr = url.contains(QUESTION_MARK) ? LINK_MARK : QUESTION_MARK;
        params.append(joinStr)
              .append("trainTypeIDX=")
              .append(account.getTrainTypeIDX())
              .append("&trainTypeShortName=")
              .append(account.getTrainTypeShortName())
              .append("&trainNo=")
              .append(account.getTrainNo())
              .append("&idx=")
              .append(account.getIdx())
              .append("&inTime=")
              .append(account.getInTime())
              .append("&planOutTime=")
              .append(account.getPlanOutTime());
        return url.concat(params.toString());
    }
    
    /**
     * <li>说明：根据查询参数JSON字符串和应用功能名称获取"TWTStation"下对应应用功能url
     * <li>创建人：汪东良
     * <li>创建日期：2015-5-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param queryStr 查询参数字符串
     * @param funcname 应用功能名称
     * @return 对应应用功能url
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Deprecated
    public String getTWTStationUrl(String queryStr, String funcname) throws JsonParseException, JsonMappingException, IOException {
        AcFunction function = sysFunctionManager.findByFuncnameAndAppcode(funcname, ConstantsTWT.APP_CODE_TWT_STATION_CONFIG);
        if (function == null)
            throw new BusinessException("无对应的应用功能");
        String url = JXConfig.getInstance().getAppURL().concat(function.getFuncaction());
        StringBuilder params = new StringBuilder();
         Map querymap =  JSONUtil.read(queryStr, HashMap.class);
        for(Object queryKey:querymap.keySet()){
            //对传入的参数进行加密
            params.append(LINK_MARK).append(queryKey).append("=").append(URLEncoder.encode((String)querymap.get(queryKey), "UTF-8"));
        }
        String strParams = params.toString();
        if(!"".equals(strParams)){
            strParams = strParams.substring(1);
        }
        String joinStr = url.contains(QUESTION_MARK) ? LINK_MARK : QUESTION_MARK;
        return url.concat(joinStr).concat(strParams);
    }

	/**
	 * <li>说明：根据查询参数JSON字符串和应用功能类型获取对应的应用功能url
     * <li>创建人：何涛
     * <li>创建日期：2015-06-03
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
	 * @param tal 台位图点击查看web页面传入参数实体
	 * @return 应用功能url
	 * @throws Exception 
	 */
	public String getUrl(TWTAsynchronmousLongin tal) throws Exception {
		String queryType = tal.getQueryType();
		if (null == queryType || queryType.trim().length() <= 0) {
			throw new BusinessException("应用功能类型不能为空！");
		}
		AcFunction function = sysFunctionManager.findByFuncnameAndAppcode(tal.getFuncname(), tal.getQueryType());
		if (function == null) {
			throw new BusinessException("无对应的应用功能！");
		}
		String url = JXConfig.getInstance().getAppURL();
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		url = url.concat(function.getFuncaction());
        //如果查询字符串为null或为空则直接返回访问地址（例如机车入段功能）；
		if (null == url || url.trim().length() <= 0) {
			return url;
		}
        // 获取查询参数并将参数设置到url中返回。
		if (null == tal.getQueryStr() || tal.getQueryStr().trim().length() <= 0) {
			return url;
		}
		// 拼接url中的查询参数
        Map querymap =  JSONUtil.read(tal.getQueryStr(), HashMap.class);
        StringBuilder params = new StringBuilder();
        if (ConstantsTWT.APP_CODE_TWT_CLIENT.equals(queryType.trim())) {
        	getParams(queryType, url, querymap, params);
        } else {
        	for(Object queryKey:querymap.keySet()){
                //对传入的参数进行加密
                params.append(LINK_MARK).append(queryKey).append("=").append(URLEncoder.encode((String)querymap.get(queryKey), "utf-8"));
            }
        }
        if (params.length() <= 0) {
        	return url;
        }
        String joinStr = url.contains(QUESTION_MARK) ? LINK_MARK : QUESTION_MARK;
        return url.concat(joinStr).concat(params.substring(1));
	}

	/**
	 * <li>说明：拼接机车显示信息功能所查询的机车信息参数
     * <li>创建人：何涛
     * <li>创建日期：2015-06-03
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
	 * @param queryType 应用功能类型
	 * @param url web应用URL路径
	 * @param querymap 查询参数Map对象
	 * @param params 拼接好的参数参数字符串
	 * @throws Exception 
	 */
	private void getParams(String queryType, String url, Map querymap, StringBuilder params) throws Exception {
        TrainAccessAccount entity = new TrainAccessAccount();
    	String trainInfo  = (String)querymap.get("trainInfo");
        entity.setTrainAliasName(trainInfo);
        TrainAccessAccount account = trainAccessAccountQueryManager.getAccountByTrainInfo(JSONUtil.write(entity));
        if (account == null) {
        	throw new BusinessException("无对应的入段机车！");
        }
        params.append("&trainTypeIDX=")
              .append(account.getTrainTypeIDX())
              .append("&trainTypeShortName=")
              .append(account.getTrainTypeShortName())
              .append("&trainNo=")
              .append(account.getTrainNo())
              .append("&idx=")
              .append(account.getIdx())
              .append("&inTime=")
              .append(account.getInTime())
              .append("&planOutTime=")
              .append(account.getPlanOutTime());
    }
	
}
