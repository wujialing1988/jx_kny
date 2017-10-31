package com.yunda.util;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <li>类型名称：
 * <li>说明： Ibatis分页对象
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-5-5
 * <li>修改人： 
 * <li>修改日期：
 */
@SuppressWarnings("serial")
public class Page implements Serializable {

	public static int DEFAULT_PAGE_SIZE = 10;

	private int pageSize = Page.DEFAULT_PAGE_SIZE; // 每页的记录数

	private int start; // 当前页第一条数据在List中的位置,从0开始

	private Collection data; // 当前页中存放的记录,类型一般为List

	private int totalCount; // 总记录数

    /**
     * <li>说明：构造空页
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-5-5
     * <li>修改人： 
     * <li>修改日期：
     */
	public Page() {
		this(0, 0, Page.DEFAULT_PAGE_SIZE, new ArrayList());
	}

	/**
	 * <li>方法名：Page
	 * <li>@param start 本页数据在数据库中的起始位置
	 * <li>@param totalSize 本页数据在数据库中的起始位置
	 * <li>@param pageSize 本页数据在数据库中的起始位置
	 * <li>@param data 本页包含的数据
	 * <li>说明：构造方法
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Page(int start, int totalSize, int pageSize, Collection data) {
		this.pageSize = pageSize;
		this.start = start;
		totalCount = totalSize;
		this.data = data;
	}

	/**
	 * <li>方法名：getTotalCount
	 * <li>@return 总记录数
	 * <li>返回类型：int
	 * <li>说明：取总记录数
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * <li>方法名：getTotalPageCount
	 * <li>@return 总页数
	 * <li>返回类型：int
	 * <li>说明：取总页数
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public int getTotalPageCount() {
		if (totalCount % pageSize == 0)
			return totalCount / pageSize;
		else
			return totalCount / pageSize + 1;
	}

	/**
	 * <li>方法名：getPageSize
	 * <li>@return 每页条数
	 * <li>返回类型：int
	 * <li>说明：取每页条数
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * <li>方法名：getResult
	 * <li>@return当前页中的记录
	 * <li>返回类型：Collection
	 * <li>说明：取当前页中的记录
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Collection getResult() {
		return data;
	}

	/**
	 * <li>方法名：getCurrentPageNo
	 * <li>@return 当前页码
	 * <li>返回类型：int
	 * <li>说明：取该页当前页码,页码从1开始
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public int getCurrentPageNo() {
		return start / pageSize + 1;
	}

	/**
	 * <li>方法名：hasNextPage
	 * <li>@return
	 * <li>返回类型：boolean
	 * <li>说明：判断该页是否有下一页
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public boolean hasNextPage() {
		return getCurrentPageNo() < getTotalPageCount() - 1;
	}

	/**
	 * <li>方法名：hasPreviousPage
	 * <li>@return
	 * <li>返回类型：boolean
	 * <li>说明：判断该页是否有上一页
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public boolean hasPreviousPage() {
		return getCurrentPageNo() > 1;
	}

	/**
	 * <li>方法名：getStartOfPage
	 * <li>@param pageNo 页数
	 * <li>@return 
	 * <li>返回类型：int
	 * <li>说明：获取任一页第一条数据在数据集的位置，每页条数使用默认值
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	protected static int getStartOfPage(int pageNo) {
		return Page.getStartOfPage(pageNo, Page.DEFAULT_PAGE_SIZE);
	}

	/**
	 * 
	 * <li>方法名：getStartOfPage
	 * <li>@param pageNo 页号
	 * <li>@param pageSize 每页记录条数
	 * <li>@return  任一页第一条数据在数据集的位置.
	 * <li>返回类型：int
	 * <li>说明：获取任一页第一条数据在数据集的位置.
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
	
	public int getCurrentPage() {
		return this.getCurrentPageNo();
	}
	
	public int getTotalPage() {
		return this.getTotalPageCount();
	}
    public int getStart() {	
		return this.start;
	}
}