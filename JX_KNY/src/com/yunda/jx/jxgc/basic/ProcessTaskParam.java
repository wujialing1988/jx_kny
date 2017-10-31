package com.yunda.jx.jxgc.basic;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.frame.common.hibernate.Condition;

/**
 * <li>标题：流程工单查询参数
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-3-20
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
public class ProcessTaskParam {    
    /**
     * 启始页
     */
    public int start = 0;
    /**
     * 页显示长度，0表示不分页
     */
    public int limit = 0;
    /**
     * user id
     */
    public String uid;
    /**
     * user  name
     */
    public String uname;
    /**
     * 工位终端的查询string
     */
    public String queryString = null;
    /**
     * 工位终端的节点过滤，根据actionUrl过滤
     */
    public String in = null;
    /**
     * 排序字段数组
     */
    public Order[] order = null;
    /**
     * 来自Web页面的查询条件
     */
    public List<Condition> condition = new ArrayList<Condition>(0);
    /**
     * 工位终端传递参数，用于标识流程工单token
     */
    public String taskType = null;
}
