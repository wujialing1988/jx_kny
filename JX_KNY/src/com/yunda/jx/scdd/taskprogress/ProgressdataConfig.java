package com.yunda.jx.scdd.taskprogress;

import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
@XmlRootElement(name="Progressdata")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProgressdataConfig {
		/** 静态实例化对象 */
		@XmlTransient
		private static ProgressdataConfig instance = null;
		
		private List<Progress> progress;
		
		private ProgressdataConfig(){}
		
		public static ProgressdataConfig getInstance(){
			if(instance == null)
				instance = JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("/Progressdata.xml"), ProgressdataConfig.class);
			return instance;
		}

    public List <Progress> getContent(){
		List <Progress> list = null;
		list = getInstance().getProgress();
		return list;
	}

	public List<Progress> getProgress() {
		return progress;
	}

	public void setProgress(List<Progress> progress) {
		this.progress = progress;
	}
}
