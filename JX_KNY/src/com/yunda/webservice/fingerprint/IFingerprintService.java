package com.yunda.webservice.fingerprint;

import java.io.IOException;

/**
 * <li>标题: 指纹绑定与识别
 * <li>说明: 此接口用于信息系统用户指纹绑定及识别
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-10 下午02:58:37
 * <li>修改人: 姚凯
 * <li>修改日期： 2016-04-20 上午10点
 * <li>修改内容： 将 getFingerprint 接口目前保留，经讨论需要问原始接口创建人同意才删除，建议删除该接口以及接口实现的放方法
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IFingerprintService {

	/**
	 * <li>说明：用户登陆
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-10
	 * <li>修改人： 姚凯
	 * <li>修改日期：2016-4-19 下午4点
	 * <li>修改内容：通过operatorId 去获取登陆人的信息
     * <li>修改人： 程梅
     * <li>修改日期：2016年5月3日16:56:27
     * <li>修改内容：增加登录日志
	 * 
	 * @param operatorId	系统操作员ID（即：操作员用户ID[AC_OPERATOR.OPERATORID]）
     * @param loginLocation   登录位置
	 * @return
     * @throws IOException
	 */
	public String login(Long operatorId,String loginLocation) throws IOException ;
    
    
    /**
     * <li>说明：绑定用户指纹信息
     * <li>创建人：姚凯
     * <li>创建日期：2016-4-19 下午4点
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param jsonObject {
     *  "userId" : 55555
     *  "pwd" : "12346"
     *  "imageBase" : "%%%%jj"
     * }
     * @return
     * @throws IOException 
     */
    public String insertData(String jsonObject) throws IOException;
    
    /**
     * <li>说明：查询登陆用户信息
     * <li>创建人：姚凯
     * <li>创建日期：2016-4-19 下午4点
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return JSON列表（数组）
     * @throws IOException
     * 
     */
    public String pageListFingerPrint() throws IOException;
	

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
	public String getFingerprint(String updateTime);
	
}
