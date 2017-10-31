package com.yunda.webservice.makecard;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 刷卡、制卡服务类接口
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
public interface IMakeCardService {
    
	/**
	 * 制卡
	 * 
	 * @param cardNo 卡号
	 * @param userId 用户名，如：5502133
	 * @return 操作成功和失败的标识及提示信息：如果卡号为空则发返回“没有读取到卡号，请在此读取”，如果用户名不存在则返回提示：“用户名不存在，请重新输入用户名！”
	 */
	public String makeCard(String cardNo, String userId);

	/**
	 * 刷卡登陆
	 * 
	 * @param cardNo 卡号
     * @param loginLocation 登录位置
	 * @return String
	 */
	public String loginByCard(String cardNo,String loginLocation);

}
