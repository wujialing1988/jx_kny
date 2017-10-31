package com.yunda.frame.yhgl.entity;

/**
 * AcRolefuncId generated by MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class AcRolefuncId implements java.io.Serializable {

	// Fields

	private String roleid;

	private String funccode;

	// Constructors

	/** default constructor */
	public AcRolefuncId() {
	}

	/** full constructor */
	public AcRolefuncId(String roleid, String funccode) {
		this.roleid = roleid;
		this.funccode = funccode;
	}

	// Property accessors

	public String getRoleid() {
		return this.roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getFunccode() {
		return this.funccode;
	}

	public void setFunccode(String funccode) {
		this.funccode = funccode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AcRolefuncId))
			return false;
		AcRolefuncId castOther = (AcRolefuncId) other;

		return ((this.getRoleid() == castOther.getRoleid()) || (this
				.getRoleid() != null
				&& castOther.getRoleid() != null && this.getRoleid().equals(
				castOther.getRoleid())))
				&& ((this.getFunccode() == castOther.getFunccode()) || (this
						.getFunccode() != null
						&& castOther.getFunccode() != null && this
						.getFunccode().equals(castOther.getFunccode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRoleid() == null ? 0 : this.getRoleid().hashCode());
		result = 37 * result
				+ (getFunccode() == null ? 0 : this.getFunccode().hashCode());
		return result;
	}

}