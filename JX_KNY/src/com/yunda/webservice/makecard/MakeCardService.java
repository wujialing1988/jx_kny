package com.yunda.webservice.makecard;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.XFireServletController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.LoginLog;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.EmployeeManager;
import com.yunda.frame.yhgl.manager.LoginLogManager;
import com.yunda.util.PurviewUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.webservice.makecard.manager.MakeCardServiceManager;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 刷卡、制卡服务类实现
 * <li>创建人：王利成
 * <li>创建日期：2014-7-29
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "makeCardWS")
public class MakeCardService implements IMakeCardService {
	/** 卡号没有绑定的异常标识 */
	public static final String NOT_BIND_ERROR = "notbind";

	@Autowired
	private EmployeeManager employeeManager;

	/**
	 * 操作员业务类
	 */
	@Autowired
	private AcOperatorManager acOperatorManager;

	/**
	 * 制卡功能业务类
	 */
	@Autowired
	private MakeCardServiceManager makeCardServiceManager;
    /**
     * 登录日志
     */
    @Autowired
    private LoginLogManager loginLogManager;

	/**
	 * 日志对象
	 */
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>方法名称：loginByCard
	 * <li>方法说明：刷卡登陆实现方法
	 * <li>创建人：王利成
	 * <li>创建时间：2014-7-29 上午11:57:47
	 * <li>修改人：汪东良
	 * <li>修改日期：2014-9-19
	 * <li>修改内容：接口联调，修改代码实现逻辑
     * <li>修改人： 程梅
     * <li>修改日期：2016年5月3日16:56:27
     * <li>修改内容：增加登录日志
     * 
	 * @param cardNo 卡号
     * @param loginLocation   登录位置
	 * @return String
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public String loginByCard(String cardNo,String loginLocation) {
		OperateReturnMessage objReturnMessage = new OperateReturnMessage();
		if (StringUtil.isNullOrBlank(cardNo)) {// 如果卡号为空则直接返回操作失败信息！
			return writeJSONString(objReturnMessage.setFaildFlag("没有正确读取到卡号！"));
		}		
		String omehql = "from OmEmployee as o where o.cardNum = '" + cardNo + "'";
		OmEmployee ome = employeeManager.findSingle(omehql);
		if (ome == null) {
			return writeJSONString(objReturnMessage.setFlag(NOT_BIND_ERROR, "卡号没有绑定！"));
		}
		AcOperator operator = acOperatorManager.findLoginAcOprator(ome.getUserid());
		if (operator == null) {
			return writeJSONString(objReturnMessage.setFaildFlag("没有找到对应的用户信息！"));
		}
		//新增登录日志记录
        LoginLog log = new LoginLog();
        log.setOperatoridx(operator.getOperatorid().toString());
        log.setUserid(operator.getUserid());
        log.setOperatorname(operator.getOperatorname());
        log.setLoginInTime(new Date());
        log.setLoginType(LoginLog.TYPE_SKSB);//刷卡登录
        log.setLoginClient(LoginLog.CLIENT_GWZD);//工位终端登录
        HttpServletRequest request = XFireServletController.getRequest();
        String remoteAddr = request.getRemoteAddr();
        log.setIp(remoteAddr);
        log.setLoginLocation(loginLocation);
//        log.setIp(InetAddress.getLocalHost().getHostAddress()) ;//获取ip地址
        try {
            this.loginLogManager.saveOrUpdate(log);
        } catch (Exception e) {
            objReturnMessage.setFaildFlag("接口服务操作异常！");
            logger.error("新增登录日志时报错！", e);
        }
        
		if (PurviewUtil.isSuperUsers(operator)) {
			return "null";// 超级管理员返回null字符串
		}
		return writeJSONString(operator);
	}

	/**
	 * <li>方法名称：makeCard
	 * <li>方法说明：制卡实现方法,制卡规则，如果此卡已经绑定了用户，则先将之前绑定的用户关系取消再重新制卡（即做覆盖操作）
	 * <li>创建人：王利成
	 * <li>创建时间：2014-7-29 上午11:57:47
	 * <li>修改人：汪东良
	 * <li>修改日期：2014-9-19
	 * <li>修改内容：接口联调，修改代码实现逻辑，保证一个卡只能对应一个用户。
	 * 
	 * @param userId 用户名，如：5502133
	 * @param cardNo 卡号
	 * @return String:操作成功和失败的标识及提示信息：如果卡号为空则发返回“没有读取到卡号，请在此读取”，如果用户名不存在则返回提示：“用户名不存在，请重新输入用户名！”
	 */
	public String makeCard(String cardNo, String userId) {
		OperateReturnMessage objReturnMessage = new OperateReturnMessage();
        
		// 如果卡号为空则直接返回操作失败信息！
		if (StringUtil.isNullOrBlank(cardNo)) {
			return writeJSONString(objReturnMessage.setFaildFlag("没有正确读取到卡号！"));
		}
        
		// 根据用户名查询用户信息；
		OmEmployee ome = employeeManager.findSingle("From OmEmployee Where userid = '" + userId + "'");

		// 如果用户名不存在则返回用户名不存在的操作失败信息！
		if (ome == null) {
			return writeJSONString(objReturnMessage.setFaildFlag("找不到用户名为(" + userId + ")的用户！"));
		}
		try {
			// 执行制卡操作
			makeCardServiceManager.updateMakeCard(cardNo, ome);
		} catch (Exception e) {
			objReturnMessage.setFaildFlag("接口服务操作异常！");
			logger.error("读卡器制卡接口：更新用户信息时报错！", e);
		}
		return writeJSONString(objReturnMessage);
	}

	/**
	 * <li>方法名称：writeJSONString
	 * <li>方法说明：将实体对象转换成JSON字符串，且将异常截获；
	 * <li>创建人：汪东良
	 * <li>创建时间：2014-9-19
	 * <li>修改人：
	 * <li>修改内容：
	 * 
	 * @param entity 实体对象
	 * @return String 返回JSON字符串
	 * 
	 */
	private String writeJSONString(Object entity) {
		try {
			return JSONUtil.write(entity);
		} catch (IOException e) {
			logger.error("将实体值对象转换为JSON字符串时出错！", e);
		}
		return WsConstants.OPERATE_FALSE;
	}
    
    /**
     * <li>说明：接口测试代码
     * <li>创建人：何涛
     * <li>创建日期：2015-12-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数
     * @throws MalformedURLException
     * @throws Exception
     */
    public static void main(String[] args) throws MalformedURLException, Exception {
        Client client = new Client(new URL("http://localhost:8080/CoreFrame/ydservices/MakeCardService?wsdl"));
        Object[] results = client.invoke("makeCard", new Object[]{ "wangliangting", "222222"});
        Logger.getLogger(MakeCardService.class.getName()).debug(results[0]);
    }
    
}
