package com.yunda.jx.scdd.taskprogress;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: Processdata.xml映射实体类
 * <li>创建人：程梅
 * <li>创建日期：2014-3-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProgressList {

	private List<Progress> progress;

	public List<Progress> getProgress() {
		return progress;
	}

	public void setProgress(List<Progress> progress) {
		this.progress = progress;
	}

	
	
}
