package com.yunda.webservice.fingerprint;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.xfire.transport.http.XFireServletController;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.LogonUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.LoginLog;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.LoginLogManager;
import com.yunda.frame.yhgl.manager.OperatorManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.AcOperatorBean;
import com.yunda.util.BeanUtils;
import com.yunda.util.DaoUtils;
import com.yunda.util.PurviewUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.webservice.fingerprint.entity.Fingerprint;
import com.yunda.webservice.fingerprint.manager.FingerprintManager;

/**
 * <li>标题: 指纹识别
 * <li>说明: 此接口用于信息系统用户指纹绑定及识别
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-10 下午02:58:37
 * <li>修改人: 姚凯
 * <li>修改日期：  2016-04-19 下午 15:51
 * <li>修改内容： 因表结构发生变化，bindFingerprint 插入方法无用，所以将其方法修改为login方法了
 * 建议将 checkExist，getFingerprint方法删除 因为表结构发生了变化 （目前保留因为不太清楚对其它的影响）
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */

@Service(value = "fingerprintWS")
public class FingerprintService implements IFingerprintService {

	/**
	 * 日志对象
	 */
	private Logger logger = Logger.getLogger(getClass().getName());


	/** 工作组业务处理类 */
	@Resource
	OperatorManager operatorManager;
    
    /** 数据库操作类 */
    @Resource
    DaoUtils daoUtils;
	
	/** Fingerprint 业务类 */
	@Resource
	FingerprintManager fingerprintManager;
    /**
     * 登录日志
     */
    @Resource
    LoginLogManager loginLogManager;
	
	/**
	 * <li>说明：用户指纹登陆
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-10
	 * <li>修改人： 姚凯
	 * <li>修改日期：2016-04-19 下午 15:51
	 * <li>修改内容：将用系统操作员ID 去获取用户的信息
     * <li>修改人： 程梅
     * <li>修改日期：2016年5月3日16:56:27
     * <li>修改内容：增加登录日志
     * 
     * @param operatorId    系统操作员ID（即：操作员用户ID[AC_OPERATOR.OPERATORID]）
     * @param loginLocation   登录位置
	 * @return 
	 * @throws IOException 
	 * @throws IOException 
	 */
	public String login(Long operatorId,String loginLocation) throws IOException {
        AcOperator operator = null;
        OperateReturnMessage operateReturnMessage = new OperateReturnMessage();
        try {
            operator = getAcOperatorManager().findLoginAcOprator(operatorId);
            if (operator == null) {
                return JSONUtil.write(operateReturnMessage.setFaildFlag("操作ID为空"));// 操作员为空返回空字符串
            }
            //新增登录日志记录
            LoginLog log = new LoginLog();
            log.setOperatoridx(operator.getOperatorid().toString());
            log.setUserid(operator.getUserid());
            log.setOperatorname(operator.getOperatorname());
            log.setLoginInTime(new Date());
            log.setLoginType(LoginLog.TYPE_ZWSB);//指纹登录
            log.setLoginClient(LoginLog.CLIENT_GWZD);//工位终端登录
            HttpServletRequest request = XFireServletController.getRequest();
            String remoteAddr = request.getRemoteAddr();
            log.setIp(remoteAddr);
            log.setLoginLocation(loginLocation);
            
//            log.setIp(InetAddress.getLocalHost().getHostAddress()) ;//获取ip地址
            this.loginLogManager.saveOrUpdate(log);//新增登录日志
            
            if (PurviewUtil.isSuperUsers(operator)) {
                return "null";// 超级管理员返回null字符串
            }
            AcOperatorBean acOperatorBean = new AcOperatorBean();
            BeanUtils.copyProperties(acOperatorBean, operator);
            return JSONUtil.write(acOperatorBean);
        } catch (Exception e) {
            // 发生异常返回错误
            ExceptionUtil.process(e, logger);
            operateReturnMessage.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(operateReturnMessage);
        
    }
    
    /**
     * <li>说明：插入用户的指纹图片码
     * <li>创建人：姚凯
     * <li>创建日期：2016-04-19 下午 15:51
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
     *  "userId" : 800109
     *  "pwd" : "12346"
     *  "imageBase" : "%%%%jj"
     * }
     * @return 
     * <li>"{'flag':'true','message':'操作成功！'}";
     * <li>"{'flag':'false','message':'操作失败！'}"
     * @throws IOException 
     */
    public String insertData(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 当前作业处理人员ID
        String userId = jo.getString("userId");
        // 获取操作员密码
        String pwd = jo.getString("pwd");
        // 获取图片的base64格式码
        String imageBase = jo.getString("imageBase");
        OperateReturnMessage operateReturnMessage = new OperateReturnMessage();
        AcOperator operator = null;
        try {
            // 根据用户名密码判断用户是否存在
            operator = getAcOperatorManager().findLoginAcOprator(userId, LogonUtil.getPassword(pwd));
            //验证成功后获取对应得 operatorId
            if (PurviewUtil.isSuperUsers(operator)) {
                return JSONUtil.write(operateReturnMessage.setFaildFlag("超级管理员不能注册指纹"));// 超级管理员返回信息
            }
            if (null == operator) {
                return JSONUtil.write(operateReturnMessage.setFaildFlag("用户名或者密码错误"));
            }else{
                // 将当前用户的operatorId 以及 imageBase 图片base64格式码存如数据库
                Long operatorId = operator.getOperatorid();
                Fingerprint fingerprint = new Fingerprint();
                fingerprint.setOperatorId(operatorId);
                fingerprint.setImageBase(imageBase);
                daoUtils.saveOrUpdate(fingerprint);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            operateReturnMessage.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(operateReturnMessage);
    }
    
    /**
     * <li>说明：查询用户指纹库数据
     * <li>创建人：姚凯
     * <li>创建日期：2016-04-19 下午 5点
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return JSON列表（数组）
     */
    public String pageListFingerPrint() throws IOException {
        Fingerprint fg = new Fingerprint();
        List<Fingerprint> fgList = fingerprintManager.findList(fg);
        OperateReturnMessage operateReturnMessage = new OperateReturnMessage();
        if(fgList != null && fgList.size()<1){
        return JSONUtil.write(operateReturnMessage.setFaildFlag("无用户指纹信息"));
        }
        return JSONUtil.write(fgList);
    }
    
    
	
	/**
	 * <li>说明：检验传入的“用户ID”是否存在
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param userId		系统登录用户ID（即：登录用户名[AC_OPERATOR.USERID]）
	 * @return 如果存在则返回null
	 */
	@SuppressWarnings("unused")
    private String checkExist(String userId) {
		AcOperator operator = operatorManager.findSingle("From AcOperator Where userid = '" + userId + "'");
		if (null == operator) {
			OperateReturnMessage msg = new OperateReturnMessage();
			msg.setFaildFlag();
			msg.setFaildFlag("用户ID[" + userId + "]不存在，请核查！");
			return writeJSONString(msg);
		}
		return null;
	}
	

	/**
	 * <li>说明：获取用户指纹信息，如果传入更新日期（updateTime）参数，则只取该日期之后的数据，查询指纹信息时应检验已绑定用户ID是否已经失效，对已失效的用户ID不做查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param updateTime(非必填)		更新时间，格式为“yyyy-MM-dd HH24:mi:ss”
	 * @return[{
	 * 		userid:"gaowenhua",
	 * 		fingerprint:"ADSASFAYBVBD0456SDFG0A5EAF2SA58"
	 * },{
	 * 		userid:"gaopan",
	 * 		fingerprint:"DTZGID46ASDG02AD56F1ASD0354ASS2"
	 * }]
	 */
	@SuppressWarnings("unchecked")
	public String getFingerprint(String updateTime) {
		StringBuilder sb = new StringBuilder("From Fingerprint Where userId In (Select userid From AcOperator)");
		if (null != updateTime) {
			if (isValid(updateTime)) {
				sb.append(" And updateTime >= to_date('").append(updateTime).append("',").append("'yyyy-MM-dd HH24:mi:ss')");
			} else {
				OperateReturnMessage msg = new OperateReturnMessage();
				msg.setFaildFlag();
				msg.setFaildFlag("更新日期[" + updateTime + "]不是接口指定的日期样式，请核查！");
				return writeJSONString(msg);
			}
		}
		// 获取数据库中所有的用户指纹信息
		Collection<Fingerprint> list = this.fingerprintManager.find(sb.toString());
		return writeJSONString(list);
	}
	

	/**
	 * <li>说明：检验参数是否是接口制定的日期样式
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param updateTime	更新时间，格式为“yyyy-MM-dd HH24:mi:ss”
	 * @return 
	 */
	private boolean isValid(String updateTime) {
		String fmt = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(fmt);
		try {
			df.parse(updateTime);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * <li>方法名称：writeJSONString
	 * <li>方法说明：将实体对象转换成JSON字符串，且将异常截获；
	 * <li>创建人：王东良
	 * <li>创建时间：2014-9-19
	 * <li>修改人：
	 * <li>修改内容：
	 * 
	 * @param Object:实体对象
	 * @return String 返回JSON字符串
	 * 
	 */
	@SuppressWarnings("deprecation")
    private String writeJSONString(Object entity) {
		try {
			return JSONUtil.write(entity);
		} catch (IOException e) {
			logger.error("将实体值对象转换为JSON字符串时出错！", e);
		}
		return WsConstants.OPERATE_FALSE;
	}
    
    
    /**
     * <li>说明：注入获取人员类
     * <li>创建人：姚凯
     * <li>创建日期：2016-4-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    protected AcOperatorManager getAcOperatorManager(){
        return (AcOperatorManager)Application.getSpringApplicationContext().getBean("acOperatorManager");
    }

}
