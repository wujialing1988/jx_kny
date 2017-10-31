/**
 * 
 */
package com.yunda.jx.jxgc.producttaskmanage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 质量检验查询对象的封装实体联合主键
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-26 下午02:06:31
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Embeddable
public class WorkCardQCCheckId implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/** 作业工单主键 */
	@Column(name="SOURCE_IDX")
	private String sourceIdx;
	
	/** 质量检查项主键 */
	@Column(name="CHECK_ITEM_CODE")
	private String checkItemCode;

	public WorkCardQCCheckId() {
		super();
	}

	public String getCheckItemCode() {
		return checkItemCode;
	}

	public void setCheckItemCode(String checkItemCode) {
		this.checkItemCode = checkItemCode;
	}

	public String getSourceIdx() {
		return sourceIdx;
	}

	public void setSourceIdx(String sourceIdx) {
		this.sourceIdx = sourceIdx;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((checkItemCode == null) ? 0 : checkItemCode.hashCode());
		result = PRIME * result + ((sourceIdx == null) ? 0 : sourceIdx.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WorkCardQCCheckId other = (WorkCardQCCheckId) obj;
		if (checkItemCode == null) {
			if (other.checkItemCode != null)
				return false;
		} else if (!checkItemCode.equals(other.checkItemCode))
			return false;
		if (sourceIdx == null) {
			if (other.sourceIdx != null)
				return false;
		} else if (!sourceIdx.equals(other.sourceIdx))
			return false;
		return true;
	}
	
}
