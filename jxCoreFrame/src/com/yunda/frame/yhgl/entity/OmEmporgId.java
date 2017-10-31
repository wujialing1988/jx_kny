package com.yunda.frame.yhgl.entity;

/**
 * OmEmporgId generated by MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class OmEmporgId implements java.io.Serializable {

	// Fields

	private Long orgid;   //机构ID

	private Long empid;  //人员Id

	// Constructors

	/** default constructor */
	public OmEmporgId() {
	}

	/** full constructor */
	public OmEmporgId(Long orgid, Long empid) {
		this.orgid = orgid;
		this.empid = empid;
	}

	// Property accessors

	public Long getOrgid() {
		return this.orgid;
	}

	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}

	public Long getEmpid() {
		return this.empid;
	}

	public void setEmpid(Long empid) {
		this.empid = empid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OmEmporgId))
			return false;
		OmEmporgId castOther = (OmEmporgId) other;

		return ((this.getOrgid() == castOther.getOrgid()) || (this.getOrgid() != null
				&& castOther.getOrgid() != null && this.getOrgid().equals(
				castOther.getOrgid())))
				&& ((this.getEmpid() == castOther.getEmpid()) || (this
						.getEmpid() != null
						&& castOther.getEmpid() != null && this.getEmpid()
						.equals(castOther.getEmpid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrgid() == null ? 0 : this.getOrgid().hashCode());
		result = 37 * result
				+ (getEmpid() == null ? 0 : this.getEmpid().hashCode());
		return result;
	}

}