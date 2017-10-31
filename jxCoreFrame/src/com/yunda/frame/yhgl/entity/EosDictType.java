package com.yunda.frame.yhgl.entity;

/**
 * 业务字典类型
 */
@SuppressWarnings("serial")
public class EosDictType implements java.io.Serializable {

	private String dicttypeid;

	private String dicttypename;

	private Long rank;

	private String parentid;

	private String seqno;

	public EosDictType() {
	}

	public EosDictType(String dicttypename, Long rank, String parentid,
			String seqno) {
		this.dicttypename = dicttypename;
		this.rank = rank;
		this.parentid = parentid;
		this.seqno = seqno;
	}

	public String getDicttypeid() {
		return this.dicttypeid;
	}

	public void setDicttypeid(String dicttypeid) {
		this.dicttypeid = dicttypeid;
	}

	public String getDicttypename() {
		return this.dicttypename;
	}

	public void setDicttypename(String dicttypename) {
		this.dicttypename = dicttypename;
	}

	public Long getRank() {
		return this.rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public String getParentid() {
		return this.parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getSeqno() {
		return this.seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
}