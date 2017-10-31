package com.yunda.flow.snaker.entity;

import java.util.List;

/**
 * 流程执行共通返回数据对象
 * 
 * @author hed
 */
public class WsCommonResponse<T> {

    /** 是否调用成功 */
    boolean success = true;
    /** 消息内容 */
    String msg = "操作成功！";

    /** 流程ID */
    String processId;
    /** 流程事例ID */
    String orderId;
    /** 流程任务ID */
    String taskId;
    /** 操作人员 */
    String operator;
    
    /** 流程是否结束 */
    boolean isComplete = false;
    
    /** 分页查询对象 */
    Page<T> page;
    
    /** 对象类型返回值 */
    T obj;
    
    /** 集合类型返回值 */
    List<T> list;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }
}
