package com.yunda.webservice.common.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: webservice调用操作成功与失败的返回结果值对象，默认结果为操作成功！<br>所有与工位终端、台位图、PDA有关的操作成功与失败返回结果值都应该使用此值对象封装后返回
 * <li>创建人：汪东良
 * <li>创建日期：2014-9-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class OperateReturnMessage {
	/** 操作标记 默认为：“true” */
	private String flag = "true";

	/** 操作返回信息 默认为：“操作成功！” */
	private String message = "操作成功！";

	public String getMessage() {
		return message;
	}
    
    /**
     * <li>说明：构造一个操作异常的结果对象
     * <li>创建人：何涛
     * <li>创建日期：2016-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param message 错误消息
     * @return 返回一个操作异常的结果对象
     */
    public static OperateReturnMessage newFailsInstance(String message) {
        OperateReturnMessage obj = new OperateReturnMessage();
        return obj.setFaildFlag(message);
    }

	/**
	 * 获取操作标志
	 * @return 操作标志
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * 设置操作标志及提示信息
	 * @return 当前对象
	 */
	public OperateReturnMessage setFlag(String flag,String message) {
		this.flag=flag;
		this.message=message;
		return this;
	}
	/**
	 * 操作成功标志
	 */
	public OperateReturnMessage setSucessFlag(String message) {
		this.flag = "true";
		this.message=message;
		return this;
	}
	/**
	 * 操作成功标志
	 */
	public OperateReturnMessage setSucessFlag() {
		return setSucessFlag("操作成功！");
	}
	/**
	 * 操作失败标志
	 *
	 */
	public OperateReturnMessage setFaildFlag(String message) {
		this.flag = "false";
		this.message=message;
		return this;
	}
	/**
	 * 操作失败标志
	 *
	 */
	public OperateReturnMessage setFaildFlag(String[] message) {
		this.flag = "false";
		StringBuilder sb = new StringBuilder();
		for (String msg : message) {
			sb.append("；").append(msg);
		}
		this.message=sb.substring(1);
		return this;
	}
	/**
	 * 操作失败标志
	 *
	 */
	public OperateReturnMessage setFaildFlag() {
		setFaildFlag("操作失败！");
		return this;
	}
}
