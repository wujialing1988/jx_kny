package com.yunda.frame.yhgl.entity;

/**
 * AcOperatorroleId generated by MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class AcOperatorroleId implements java.io.Serializable {

	// Fields

	private Long operatorid;

	private String roleid;

	// Constructors

	/** default constructor */
	public AcOperatorroleId() {
	}

	/** full constructor */
	public AcOperatorroleId(Long operatorid, String roleid) {
		this.operatorid = operatorid;
		this.roleid = roleid;
	}

	// Property accessors

	public Long getOperatorid() {
		return this.operatorid;
	}

	public void setOperatorid(Long operatorid) {
		this.operatorid = operatorid;
	}

	public String getRoleid() {
		return this.roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AcOperatorroleId))
			return false;
		AcOperatorroleId castOther = (AcOperatorroleId) other;

		return ((this.getOperatorid() == castOther.getOperatorid()) || (this
				.getOperatorid() != null
				&& castOther.getOperatorid() != null && this.getOperatorid()
				.equals(castOther.getOperatorid())))
				&& ((this.getRoleid() == castOther.getRoleid()) || (this
						.getRoleid() != null
						&& castOther.getRoleid() != null && this.getRoleid()
						.equals(castOther.getRoleid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getOperatorid() == null ? 0 : this.getOperatorid()
						.hashCode());
		result = 37 * result
				+ (getRoleid() == null ? 0 : this.getRoleid().hashCode());
		return result;
	}

}