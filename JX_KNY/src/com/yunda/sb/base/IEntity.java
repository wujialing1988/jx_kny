package com.yunda.sb.base;

import java.util.Date;

import com.yunda.sb.base.combo.ILogicDelete;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: IEntity，数据库实体业务相关信息对象接口
 * <li>创建人：何涛
 * <li>创建日期：2016年10月9日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public interface IEntity extends ILogicDelete {
	
	/**
	 * <li>说明：获取idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return idx主键
	 */
	public String getIdx();

	/**
	 * <li>说明：设置idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param idx idx主键
	 */
	public void setIdx(String idx);
	
	/**
	 * <li>说明：获取创建人
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 创建人
	 */
	public Long getCreator();

	/**
	 * <li>说明：设置创建人
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param creator 创建人
	 */
	public void setCreator(Long creator);

	/**
	 * @return 获取创建时间
	 */
	/**
	 * <li>说明：获取创建日期
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 创建日期
	 */
	public Date getCreateTime();

	/**
	 * <li>说明：设置创建时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime);

	/**
	 * <li>说明：获取更新人
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 更新人
	 */
	public Long getUpdator();

	/**
	 * <li>说明：设置更新人
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param updator 更新人
	 */
	public void setUpdator(Long updator);

	/**
	 * <li>说明：获取更新时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 更新时间
	 */
	public Date getUpdateTime();

	/**
	 * <li>说明：设置更新时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param updateTime 更新时间
	 */
	public void setUpdateTime(Date updateTime);

}
