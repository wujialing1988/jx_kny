package com.yunda.twt.trainaccessaccount.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.EosDictEntryManager;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.twt.asynchronouslogin.entity.TWTAsynchronmousLongin;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessInAndOutManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessInAndOutManager.InAndOutMes;
import com.yunda.twt.trainaccessaccount.webservice.TrainAccessAccountBean;
import com.yunda.twt.twtinfo.entity.TrainStatusColors;
import com.yunda.twt.twtinfo.manager.TrainStatusColorsManager;
import com.yunda.twt.webservice.client.ITWTTrainStatusService;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.webservice.common.util.DefaultUserUtilManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainAccessAccount控制器, 机车出入段台账
 * <li>创建人：程锐
 * <li>创建日期：2015-01-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class TrainAccessAccountAction extends JXBaseAction<TrainAccessAccount, TrainAccessAccount, TrainAccessAccountManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 默认颜色值 */
    private final String DEFAULT_COLOR = "#888";
    /** 车号业务类 */
	@Autowired
	private TrainAccessInAndOutManager trainAccessInAndOutManager;
	
	/** 机车出入段台账查询业务类 */
	@Autowired
	private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
	
	/** 机车出入段台账业务类 */
	@Autowired
	private TrainAccessAccountManager trainAccessAccountManager;
	
    /** 机车状态管理客户端业务类 */
    @Autowired
    private ITWTTrainStatusService tWTTrainStatusService;
    
    /**
     * <li>说明：机车入段
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveOrUpdateIn() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainAccessAccount trainAccessAccount = (TrainAccessAccount) JSONUtil.read(getRequest(), entity.getClass());
            InAndOutMes<OperateReturnMessage,TrainAccessAccount> msg = trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(trainAccessAccount);
            //this.manager.saveOrUpdateIn(trainAccessAccount);
            if("false".equals(msg.getOperateReturnMessage().getFlag())){
                map.put(Constants.SUCCESS, false);
            	map.put(Constants.ERRMSG, msg.getOperateReturnMessage().getMessage());
            }else{
            	map.put(Constants.SUCCESS, true);
            	map.put(Constants.ENTITY, trainAccessAccount);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：确认入段去向
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void confirmTogo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainAccessAccount trainAccessAccount = (TrainAccessAccount) JSONUtil.read(getRequest(), entity.getClass());
            this.manager.confirmTogo(trainAccessAccount);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：机车出段
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveOrUpdateOut() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainAccessAccount trainAccessAccount = (TrainAccessAccount) JSONUtil.read(getRequest(), entity.getClass());
            this.manager.saveOrUpdateOut(trainAccessAccount);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：编辑机车入段
     * <li>创建人：程锐
     * <li>创建日期：2015-3-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateIn() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainAccessAccount trainAccessAccount = (TrainAccessAccount) JSONUtil.read(getRequest(), entity.getClass());
            this.manager.updateIn(trainAccessAccount);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：机车入库信息统计
     * <li>创建人：何涛
     * <li>创建日期：2015-4-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void statistics() throws JsonMappingException, IOException {
        Map<String, Object> map = new TreeMap<String, Object>(new Comparator<String>() {
            // 排序比较器
            public int compare(String o1, String o2) {
                int n1 = Integer.parseInt(o1.substring(o1.lastIndexOf(Constants.JOINSTR) + 1));
                int n2 = Integer.parseInt(o2.substring(o2.lastIndexOf(Constants.JOINSTR) + 1));
                return n1 - n2;
            }
        });
        HttpServletRequest req = getRequest();
        try {
            // 查询条件 - 车型（车号）
            String trainNo = StringUtil.nvl(req.getParameter("trainNo"), "").trim();
            StringBuilder sb = new StringBuilder("From TrainAccessAccount Where recordStatus = 0");
            if (!StringUtil.isNullOrBlank(trainNo)) {
                sb.append(" And trainTypeShortName || trainNo Like '%").append(trainNo.toUpperCase()).append("%'");
            }
            // 只查询当前站点的入段机车信息
            String synSiteID = EntityUtil.findSysSiteId(null);
            if (null != synSiteID && synSiteID.trim().length() > 0) {
                sb.append(" And siteID = '").append(synSiteID).append("'");
            }
            // 只查询在段的机车
            sb.append(" And outTime Is Null");
            Collection<TrainAccessAccount> all = this.manager.find(sb.toString());
            String status = null;
            // 页面显示信息
            StringBuilder dispalyInfo = null;
            for (TrainAccessAccount account : all) {
                dispalyInfo = new StringBuilder();
                status = account.getTrainStatus();
                // 获取机车状态及状态颜色配置信息
                if (null == account.getTrainStatus() || status.trim().length() <= 0) {
                    status = "其它,#888,-1";
                } else {
                    status = this.getTrainStatusColor(status, map);
                }
                List<String> list = (List<String>) map.get(status);
                if (null == list) {
                    list = new ArrayList<String>();
                    map.put(status, list);
                }
                if (!StringUtil.isNullOrBlank(account.getTrainTypeShortName())) {
                    dispalyInfo.append(account.getTrainTypeShortName());
                }
                if (!StringUtil.isNullOrBlank(account.getTrainNo())) {
                    dispalyInfo.append(account.getTrainNo());
                }
                // 没有车型车号则显示机车别名，如果连机车别名也没有，则显示为“未知机车”
                if (dispalyInfo.length() <= 0) {
                    dispalyInfo.append(StringUtil.isNullOrBlank(account.getTrainAliasName()) ? "未知机车" : account.getTrainAliasName());
                }
                list.add(account.getIdx() + Constants.JOINSTR + dispalyInfo.toString());
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取机车状态及状态颜色配置信息，如果没有配置该状态的颜色，则默认返回颜色值“#888”
     * <li>创建人：何涛
     * <li>创建日期：2015-8-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param status 机车状态
     * @param map 机车状态及该状态下的机车信息集合
     * @return 机车状态及状态颜色配置信息，返回字符串样式为：“检修-”
     */
    private String getTrainStatusColor(String status, Map<String, Object> map) {
        // 先从map中去查找是否已有该机车信息集合，以避免不会每次都会去查询机车状态颜色（TWT_TRAIN_STATUS_COLORS）数据表
        Set<String> set = map.keySet();
        if (set.size() > 0) {
            for (Iterator<String> i = set.iterator(); i.hasNext();) {
                String trainStatus = i.next();
                if (status.equals(trainStatus.substring(0, trainStatus.indexOf(Constants.JOINSTR)))) {
                    return trainStatus;
                }
            }
        }
        // 获取状态颜色配置信息
        TrainStatusColorsManager trainStatusColorsManager =
            (TrainStatusColorsManager) Application.getSpringApplicationContext().getBean("trainStatusColorsManager");
        TrainStatusColors tsc = trainStatusColorsManager.getModelByStatus(status);
        // 如果没有维护【机车状态颜色】信息，则采用默认颜色值“#888”，顺序号随机
        if (null == tsc) {
            return new StringBuilder(status).append(Constants.JOINSTR).append(DEFAULT_COLOR).append(Constants.JOINSTR).append(
                getRandomNum(10000)).toString();
        }
        // 颜色值
        String color = tsc.getColor();
        color = StringUtil.isNullOrBlank(color) ? DEFAULT_COLOR : color;
        // 机车状态颜色顺序号，如果没有设置则顺序号随机
        int seqNo = null != tsc.getSeqNo() ? tsc.getSeqNo().intValue() : getRandomNum(10000);
        // 如果【机车状态颜色】颜色值为空，则采用默认颜色值“#888”
        if (!color.startsWith("#")) {
            if (color.length() == 3 || color.length() == 6) {
                color = "#" + color;
            } else {
                color = DEFAULT_COLOR;
            }
        }
        return new StringBuilder(status).append(Constants.JOINSTR).append(color).append(Constants.JOINSTR).append(seqNo).toString();
    }

    /**
     * <li>说明：获取一个10000以内的随机正整数
     * <li>创建人：何涛
     * <li>创建日期：2015-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param range 随机数范围值
     * @return 随机正整数
     */
    private int getRandomNum(int range) {
        return Math.abs(new Random(System.currentTimeMillis()).nextInt(10000));
    }
    
    /**
     * <li>说明：单表分页查询，返回单表分页查询记录的json
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-12-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void pageQuery() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            QueryCriteria<TrainAccessAccount> query =
                new QueryCriteria<TrainAccessAccount>(getQueryClass(), getWhereList(), getOrderList(), getStart(), getLimit());
            map = this.manager.findPageList(query).extjsStore();
            List<TrainAccessAccount> list = (List<TrainAccessAccount>) map.get("root");
            for (TrainAccessAccount account : list) {
                if (!StringUtil.isNullOrBlank(account.getTrainStatus()))
                    account.setColor(TrainAccessAccountManager.getColorByStatus(account.getTrainStatus()));
            }
            map.put("root", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据idx主键获取机车出入段台账实体对象
     * <li>创建人：何涛
     * <li>创建日期：2015-8-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModelByIdx() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            @SuppressWarnings("hiding")
            TrainAccessAccount entity = this.manager.getModelById(id);
            if (!StringUtil.isNullOrBlank(entity.getToGo())) {
                EosDictEntryManager entryManager = (EosDictEntryManager) Application.getSpringApplicationContext().getBean("eosDictEntryManager");
                // 入段去向数据字典
                EosDictEntry entry = entryManager.getEntity(entity.getToGo(), "TWT_TRAIN_ACCESS_ACCOUNT_TOGO");
                if (null != entry) {
                    entity.setToGoName(entry.getDictname());
                }
            }
            map.put(Constants.ENTITY, entity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：机车批量入段
     * <li>创建人：林欢
     * <li>创建日期：2016-3-10
	 * <li>修改人：汪东良
	 * <li>修改日期：2016-07-21
	 * <li>修改内容：修改机车批量入段的方式，将所有机车自动入段都修改为统一通过trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(accessAccount)进行调用。
     * @throws Exception
     */
    public void saveOrUpdateListIn() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            TrainAccessAccount[] arr = JSONUtil.read(getRequest(), TrainAccessAccount[].class);
            for(TrainAccessAccount trainAccessAccount:arr){
                trainAccessInAndOutManager.saveOrUpdateTrainAccessIn(trainAccessAccount);
                //this.manager.saveOrUpdateIn(trainAccessAccount);
            }
            map.put(Constants.SUCCESS, true);
            map.put(Constants.ENTITY, arr);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：台位图通过台位图服务子系统获取当前站点的所有在段机车列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
    public void getInAccountTrainList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<TrainAccessAccountBean> beanList = new ArrayList<TrainAccessAccountBean>();
        try {
            
            String siteID = StringUtil.nvl(getRequest().getParameter("siteID"), "").trim();
            // 获取数据
            beanList = this.manager.getInAccountTrainList(siteID);
            logger.info("---------------------根据操作者获取在段机车列表:" + JSONUtil.write(beanList));
            JSONUtil.write(this.getResponse(), beanList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取台位图-确定入段去向接口-机车出入段台账信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void getTrainInfoForToGo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String trainInfo = StringUtil.nvl(getRequest().getParameter("trainInfo"), "").trim();
            JSONObject jsonObj  = this.manager.getTrainInfoForToGo(trainInfo);
            JSONUtil.write(this.getResponse(), jsonObj);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取承修车型列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void getUndertakeTrainType() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
//          获取数据
            List<TrainType> list  = this.manager.getUndertakeTrainType();
            JSONUtil.write(this.getResponse(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取机车入段去向数据字典列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 机车入段去向数据字典列表
     */
    public void getTrainToGo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
//          获取数据
            List<EosDictEntryBean> beanlist = this.manager.getTrainToGo();
            JSONUtil.write(this.getResponse(), beanlist);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据车型获取车号列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型简称
     * @return 车号列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public void getTrainNoByTrainType() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String trainTypeShortName = StringUtil.nvl(getRequest().getParameter("trainTypeShortName"), "").trim();
            Map queryParamsMap = this.manager.getTrainNoByTrainType(trainTypeShortName);
            JSONUtil.write(this.getResponse(), queryParamsMap);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：判断该入段去向是否需要生成整备范围活
     * <li>创建人：林欢
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void existNeedToDoZbRdp() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String toGo = getRequest().getParameter("toGo");
            map = this.manager.existNeedToDoZbRdp(toGo,map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：通过右键台位图，判断当前车是否属于检修 整备 通用
     * <li>创建人：林欢
     * <li>创建日期：2016-9-22
     * <li>修改人： lidt
     * <li>修改日期：2016-10-24
     * <li>修改内容：修正接收的参数（与 朱宝石 联调台位图）
     * @param trainTypeShortName
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws Exception
     */
    public void getTrainStateByMessage() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
        	
        	String trainTypeShortName = getRequest().getParameter("trainTypeShortName");
        	
        	if (StringUtil.isNullOrBlank(trainTypeShortName)) {
        		map.put("flag", false);
        		map.put("message", "右键台位图，传递数据为空!");
            }
        	
//          获取数据
//            String trainTypeShortName = ob.getString("trainTypeShortName");
            map = this.manager.getTrainStateByMessage(trainTypeShortName);
        	
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：台位图服务调用该接口获取web页面显示URL
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：汪东良
     * <li>修改日期：2015-05-25
     * <li>修改内容：增加查询参数类型，并对代码进行重构以适应点击台位弹出页面的功能
     * @param queryJSONStr 查询参数
     * @param queryType 查询分类 
     *      <br>TWTClient:表示机车信息参数格式为：{"trainInfo":"N50001"}
     *      <br>TWTStation：表示台位queryJSONStr参数格式为：{"code":"1201","name":"组装台位","mapcode":"HEBJD"}
     *      <br>TWTVideo:表示台位queryJSONStr参数格式为：{"siteID":"A", "videoCode":"1", "videoName":"1号摄像头"}
     * @param funcname 功能名称
     * @param userId 登录名
     * @return web端机车相关信息显示URL字符串,包含车型车号参数.url字符串格式样式：http://www.baidu.com,如果查询不到机车则返回失败的字符串
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void getDisplayURL() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
        	
        	String queryJSONStr = getRequest().getParameter("queryJSONStr");
        	String queryType = getRequest().getParameter("queryType");
        	String funcname = getRequest().getParameter("funcname");
        	String userId = getRequest().getParameter("userId");
        	
//        	将参数值设置到TWTAsynchronmousLongin对象中并输出成JSON格式；
            TWTAsynchronmousLongin twtAsynchronmousLongin=new TWTAsynchronmousLongin();
            twtAsynchronmousLongin.setQueryType(queryType);
            twtAsynchronmousLongin.setFuncname(funcname);
            twtAsynchronmousLongin.setQueryStr(queryJSONStr);
            twtAsynchronmousLongin.setUserId(userId);            
            String paramsStr = JSONUtil.write(twtAsynchronmousLongin);
            
            String url = JXConfig.getInstance().getAppURL().concat("jsp/twt/asynchronouslogin/AsynchronousLogin.jsp");
            StringBuilder params = new StringBuilder();
            //对参数进行URL加密
            params.append(url.contains("?") ? "&" : "?").append("queryParams=").append(URLEncoder.encode(paramsStr, "UTF-8"));
            String strURL = url.concat(params.toString());
        	
            JSONUtil.write(this.getResponse(), strURL);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    
    /**
     * <li>说明：台位图-设置车头方向
     * <li>创建人：lidt
     * <li>创建日期：2016-11-09
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串（关键是需要 机车简称trainAliasName）
     * @param ctfx 车头方向
     * @return 操作成功与否
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void setCtfx() throws JsonMappingException, IOException{
    	try {
    		String trainInfo = getRequest().getParameter("trainInfo");
    		String ctfx = getRequest().getParameter("ctfx");
    		TrainAccessAccount account = JSONUtil.read(trainInfo, TrainAccessAccount.class);
			//TrainAccessAccount account = new TrainAccessAccount();
			//account.setTrainAliasName(trainAliasName);
			TrainAccessAccount trainAccessAccount = trainAccessAccountQueryManager.getAccountByTrainInfo(JSONUtil.write(account));
			if (trainAccessAccount == null)
				JSONUtil.write(this.getResponse(), WsConstants.OPERATE_FALSE);
			trainAccessAccount.setCtfx(ctfx);
			DefaultUserUtilManager.setDefaultOperator();
			trainAccessAccountManager.saveOrUpdate(trainAccessAccount);
			JSONUtil.write(this.getResponse(), WsConstants.OPERATE_SUCCESS);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			JSONUtil.write(this.getResponse(), WsConstants.OPERATE_FALSE);
		}
    }
    
	   /**
     * <li>说明：台位图服务端手工修改机车状态后，需要调用此服务通知web服务子系统，web服务子系统调用设置接车状态通知台位图服务端，由台位图服务端通知各个台位图客户端
     * <li>创建人：lidt
     * <li>创建日期：2016-11-09
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @param status 机车状态
     * @return 操作是否成功的字符串
	 * @throws IOException 
	 * @throws JsonMappingException 
     */
    public void updateTrainStatus() throws JsonMappingException, IOException {
        try {
        	String trainInfo = getRequest().getParameter("trainInfo");
    		String status = getRequest().getParameter("status");
            tWTTrainStatusService.setTrainStatus(trainInfo, status);
            JSONUtil.write(this.getResponse(), WsConstants.OPERATE_SUCCESS);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("flag", false);
            map.put("message", e.getMessage());
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}
