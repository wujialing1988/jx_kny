package com.yunda.twt.webservice.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jxpz.utils.TrainTypeMappingUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.twt.twtinfo.entity.Site;
import com.yunda.twt.twtinfo.manager.SiteManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 台位图公用业务接口实现类
 * <li>创建人：程锐
 * <li>创建日期：2015-2-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "twtUtil")
public class TWTUtil extends JXBaseManager<Object, Object> implements ITWTUtil {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 台位图webservice客户端 对象集合 */
    private static Map<String, Client> clientMap = new HashMap<String, Client>();
    
    /** 站场图业务类 */
    @Resource
    private SiteManager siteManager;
    
    /** 机车出入段台账查询业务类 */
    @Autowired
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /**
     * <li>说明：根据台位图服务名、车型车号获取台位图webservice客户端对象
     * <li>创建人：程锐
     * <li>创建日期：2014-5-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param serviceName 台位图服务名
     * @param trainTypeShortName 车型简称
     * @param trainNo 车号
     * @return 台位图webservice客户端对象
     * @throws Exception
     */
    public Client getClient(String serviceName, String trainTypeShortName, String trainNo) throws Exception {
        TrainAccessAccount account = trainAccessAccountQueryManager.findInAccountByTrainName(trainTypeShortName, trainNo);
        if (account == null)
            return null;
        return getDefaultClient(serviceName, account.getSiteID());
    }
    
    /**
     * <li>说明：根据台位图服务名、机车信息获取台位图webservice客户端对象
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param serviceName 台位图服务名
     * @param trainInfo 机车信息JSON字符串
     * @return 台位图webservice客户端对象
     * @throws Exception
     */
    public Client getClient(String serviceName, String trainInfo) throws Exception {
        TrainAccessAccount account = trainAccessAccountQueryManager.getAccountByTrainInfo(trainInfo);
        if (account == null)
            throw new BusinessException("未找到对应入段机车");
        return getDefaultClient(serviceName, account.getSiteID());
    }
    
    /**
     * <li>说明：根据台位图服务名、站点标示获取台位图webservice客户端对象
     * <li>创建人：程锐
     * <li>创建日期：2014-5-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param serviceName 台位图服务名
     * @param siteID 站点标示
     * @return 台位图webservice客户端对象
     */
    public Client getDefaultClient(String serviceName, String siteID) {
        Client client = null;
        try {
            String wsdl = getWsdl(serviceName, siteID);
            if (StringUtil.isNullOrBlank(wsdl))
                return client;
            client = clientMap.get(wsdl);
            if (client == null) {
                client = new Client(new URL(wsdl));
                clientMap.put(wsdl, client);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        }
        return client;
    }
    
    /**
     * <li>说明:根据车型车号六位简称获取机车简称和车号
     * <li>创建人：程锐
     * <li>创建日期：2014-06-03
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 6位字符，前两位是车型、后四位是车号
     * @return TrainType 机车信息
     */
    @SuppressWarnings("unchecked")
    public TrainType getTrainTypeByTrainInfo(String trainInfo) {
        if (StringUtil.isNullOrBlank(trainInfo))
            return null;
        if (trainInfo.length() < 2)
            return null;
        String shortType = trainInfo.substring(0, 2);
        String shortNo = trainInfo.substring(2);
        TrainType trainType = TrainTypeMappingUtil.getTrainTypeOfShorName(shortType);
        if (trainType == null) {
        	trainType = buildTrainType(trainInfo);
        } else {
        	String trainNo = getTrainNO(shortNo);
        	trainType.setTrainNo(trainNo);
        }
        return trainType;
    }
    
    /**
     * <li>说明:根据台位图传递过来的车号构造真实车号（不足4位或5位补全）
     * <li>创建人：程锐
     * <li>创建日期：2016-06-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param shortNo 台位图传递过来的车号
     * @return 车号
     */
    public String getTrainNO(String shortNo){
    	String newTrainNo = shortNo;
    	String endChar = shortNo.substring(shortNo.length() - 1);
    	try {
			Integer.parseInt(endChar);//判断最后一个字符是否是英文字符，如是英文字符则取后5位为车号，主要针对昆明小石坝有0001A这种车号的场景
			if (newTrainNo.length() < 4) {
				int count = 4 - shortNo.length();
				for (int i = 0; i < count; i++) {
					shortNo = "0" + shortNo;
				}
			}
		} catch (Exception e) {
			if (shortNo.length() < 5) {
				int count = 5 - shortNo.length();
				for (int i = 0; i < count; i++) {
					shortNo = "0" + shortNo;
				}
			}
		}
		return shortNo;
    }
    
    /**
     * <li>说明:根据车号识别传递过来的机车信息构造机车信息
     * <li>创建人：程锐
     * <li>创建日期：2016-06-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 车号识别传递的机车信息
     * @return 机车信息
     */
    public TrainType buildTrainType(String trainInfo) throws BusinessException{
        if (StringUtil.isNullOrBlank(trainInfo) || trainInfo.length() < 6)
            throw new BusinessException("机车信息不全");
        String trainTypeShortName = trainInfo.substring(0, trainInfo.length() - 4);
        String trainNo = trainInfo.substring(trainInfo.length() - 4);
        TrainType trainType = TrainTypeMappingUtil.getTrainTypeOfShorName(trainTypeShortName);
        String endChar = trainInfo.substring(trainInfo.length() - 1);
        try {
			Integer.parseInt(endChar);//判断最后一个字符是否是英文字符，如是英文字符则取后5位为车号，主要针对昆明小石坝有0001A这种车号的场景
		} catch (Exception e) {
			trainTypeShortName = trainInfo.substring(0, trainInfo.length() - 5);
	        trainNo = trainInfo.substring(trainInfo.length() - 5);
	        trainType = TrainTypeMappingUtil.getTrainTypeOfShorName(trainTypeShortName);;
		}
		//考虑到有可能只传车号的可能性
		if (trainType == null) {
			trainType = new TrainType();
		}
        trainType.setTrainNo(trainNo);
        return trainType;
    }
    
    /**
     * <li>说明：获取siteID对应的台位图服务wsdl
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param serviceName 台位图服务名
     * @param siteID 站场标示
     * @return siteID 对应的台位图服务wsdl
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private String getWsdl(String serviceName, String siteID) throws Exception {
        Site site = siteManager.getModelById(siteID);
        if (site == null)
            return "";
        return site.getWebAddress() + serviceName + "?wsdl";
    }
    
    public static void main(String[] args) {
    	ITWTUtil twtUtil = new TWTUtil();
    	String trainInfo = "C3001";
    	TrainType trainType = twtUtil.getTrainTypeByTrainInfo(trainInfo);
    	System.out.println(trainType.getShortName().concat(trainType.getTrainNo()));
    }
}
