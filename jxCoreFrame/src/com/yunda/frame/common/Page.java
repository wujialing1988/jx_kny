/**
 * <li>说明：用于存放分页记录的对象。
 * <li>创建人： 测控开发部检修系统项目组
 * <li>创建日期：2012-08-07
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunda.frame.util.EntityUtil;

public class Page<T>{
	/**默认每页显示记录数 */
	public final static int PAGE_SIZE = 50;	
	/**	 记录总数	 */
    private Integer total;
    /**  分页后的记录 */
    private List<T> list;
    /** 操作结果 true成功，false失败 */
    private boolean success = true;
    /** 错误信息  */
    private String errMsg;
    /** 主键字段名称 */
    private String id;
    /**
     * <li>说明：默认空构造方法
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-5-7
     * <li>修改人： 
     * <li>修改日期：
     */
    public Page(){}
    /**
     * <li>说明：构造方法
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-8-27
     * <li>修改人： 
     * <li>修改日期：
     * @param list 实体对象集合
     */
    public Page(List<T> list){
    	this.list = list;
    }
    /**
     * <li>说明：构造方法
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-8-27
     * <li>修改人： 
     * <li>修改日期：
     * @param total 记录总数
     * @param list 实体对象集合
     */
    public Page(int total, List<T> list){
    	this.total = total;
    	this.list = list;    	
    }
    /**
     * <li>说明：返回符合extjs（sench touch等AJAX请求）的JSON数据对象
     * <li>创建人：刘晓斌
     * <li>创建日期：2014-10-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public Map<String,Object> extjsResult(){
		Map<String, Object> map = new HashMap<String, Object>();
		if(id != null)	map.put("id", id);
		map.put("success", success);
		if(errMsg != null)	map.put("errMsg", errMsg);
		if(list != null)	map.put("root", this.getList());
		Integer objTotal = getTotal();
		if(objTotal == null)	objTotal = (list == null ? 0 : list.size());
		map.put("totalProperty", objTotal);
		return map;    	
    }
    /**
     * @param page
     * @return 返回符合Spring AJAX要求的extjs store数据类型
     */
    public static Map<String,Object> extjsStore(Page<?> page){
		Map<String, Object> map = new HashMap<String, Object>();
		if(page == null)	return map;
		map.put("id", EntityUtil.IDX);
		map.put("root", page.getList());
		map.put("totalProperty", page.getTotal() == null ? page.getList().size() : page.getTotal());
		return map;
    }
    /**
     * <li>说明：根据List对象集合，生成符合AJAX extjs store的数据类型
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-5-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param  List list 实体对象集合
     * @return Map<String,Object> 返回符合Spring AJAX要求的extjs store数据类型
     * @throws 抛出异常列表
     */
    public static Map<String,Object> extjsStore(List list){
		Map<String, Object> map = new HashMap<String, Object>();
		if(list == null || list.size() < 1)	return map;
		
		map.put("id", EntityUtil.IDX);
		map.put("root", list);
		map.put("totalProperty", list.size());
		return map;    	
    }
    /**
     * <li>说明：根据List对象集合，生成符合AJAX extjs store的数据类型
     * <li>创建人：刘晓斌
     * <li>创建日期：2015-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param  idx 主键字段名称
     * @param  list 实体对象集合
     * @return Map<String,Object> 返回符合Spring AJAX要求的extjs store数据类型
     */
    public static Map<String,Object> extjsStore(String idx, List list){
		Map<String, Object> map = new HashMap<String, Object>();
		if(list == null || list.size() < 1)	return map;
		
		map.put("id", idx);
		map.put("root", list);
		map.put("totalProperty", list.size());
		return map;    	
    }    
    /**
     * @return 返回符合Spring AJAX要求的extjs store数据类型
     */
    public Map<String,Object> extjsStore(){
    	return extjsStore(this);
    }
    /**
     * @return the list
     */
    public List<T> getList(){
    	return list;
    }
	/**
	 * @return the total
	 */
	public Integer getTotal() {
		return total;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}