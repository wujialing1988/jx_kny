package com.yunda.jx.third.edo.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 描述项目进度的数据，前置任务实体类，适用于易度项目管理（甘特图）控件从服务端返回给客户端的json数据结构
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-1-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PredecessorLink{
	/** 前置任务UID */
	private String PredecessorUID;
	/** 任务相关性类型 */
	private Integer Type;
	/** 延隔时间 */
	private Integer LinkLag;
	/** 时间类型  */
	private Integer LagFormat;
	
	public Integer getLagFormat() {
		return LagFormat;
	}
	public void setLagFormat(Integer lagFormat) {
		LagFormat = lagFormat;
	}
	public Integer getLinkLag() {
		return LinkLag;
	}
	public void setLinkLag(Integer linkLag) {
		LinkLag = linkLag;
	}
	public Integer getType() {
		return Type;
	}
	public void setType(Integer type) {
		Type = type;
	}
	public String getPredecessorUID() {
		return PredecessorUID;
	}
	public void setPredecessorUID(String predecessorUID) {
		PredecessorUID = predecessorUID;
	}
    public PredecessorLink(){
        
    }
    public PredecessorLink(String predecessorUID, Integer type) {
        super();
        PredecessorUID = predecessorUID;
        Type = type;
    }
	
}	