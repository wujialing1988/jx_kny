package com.yunda.frame.yhgl.entity;


/**
 * EosDictEntry generated by MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class EosDictEntry implements java.io.Serializable {

	// Fields

	private EosDictEntryId id;

	private String dictname;

	private Long status;

	private Long sortno;

	private Long rank;

	private String parentid;

	private String seqno;

	private String filter1;

	private String filter2;
	
	private String tempdicttypeid; //附加字段 类型ID
	
	private String tempdictid;     //附加字段 业务字典项ID


	public EosDictEntry() {
	}
	
	/**
	 * <li>说明：业务字典项列表页面
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public EosDictEntry(String tempdicttypeid, String tempdictid, String dictname, Long status, Long sortno, Long rank, String parentid, String seqno, String filter1, String filter2){
		this.tempdicttypeid = tempdicttypeid;
		this.tempdictid = tempdictid;
		this.dictname = dictname;
		this.status = status;
		this.sortno = status;
		this.rank = rank;
		this.parentid = parentid;
		this.seqno = seqno;
		this.filter1 = filter1;
		this.filter2 = filter2;
	}
	
	/**
	 * 
	 * <li>说明：<承修信息设置>模块中'承修车型'里的新增已设置车型时需要用到的构造方法。
	 * <li>创建人：何源
	 * <li>创建日期：2011-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public EosDictEntry(String dictname,String parentid,EosDictEntryId id) {
		this.dictname = dictname;
		this.parentid = parentid;
		this.id = id;
	}
	
	public EosDictEntry(EosDictEntryId id) {
		this.id = id;
	}
  
	
	public EosDictEntry(String seqno,String dictname, Long sortno, Long status) {
		this.dictname = dictname;
		this.status = status;
		this.sortno = sortno;
		this.seqno = seqno;
	}

	/** full constructor */
	public EosDictEntry(EosDictEntryId id, String dictname, Long status,
			Long sortno, Long rank, String parentid, String seqno,
			String filter1, String filter2) {
		this.id = id;
		this.dictname = dictname;
		this.status = status;
		this.sortno = sortno;
		this.rank = rank;
		this.parentid = parentid;
		this.seqno = seqno;
		this.filter1 = filter1;
		this.filter2 = filter2;
	}

	// Property accessors

	public EosDictEntryId getId() {
		return this.id;
	}

	public void setId(EosDictEntryId id) {
		this.id = id;
	}

	public String getDictname() {
		return this.dictname;
	}

	public void setDictname(String dictname) {
		this.dictname = dictname;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getSortno() {
		return this.sortno;
	}

	public void setSortno(Long sortno) {
		this.sortno = sortno;
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

	public String getFilter1() {
		return this.filter1;
	}

	public void setFilter1(String filter1) {
		this.filter1 = filter1;
	}

	public String getFilter2() {
		return this.filter2;
	}

	public void setFilter2(String filter2) {
		this.filter2 = filter2;
	}

	public String getTempdictid() {
		return tempdictid;
	}

	public void setTempdictid(String tempdictid) {
		this.tempdictid = tempdictid;
	}

	public String getTempdicttypeid() {
		return tempdicttypeid;
	}

	public void setTempdicttypeid(String tempdicttypeid) {
		this.tempdicttypeid = tempdicttypeid;
	}

}