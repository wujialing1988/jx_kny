package com.yunda.frame.sso.entity;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 单点登录实体类
 * <li>创建人：程锐
 * <li>创建日期：2015-8-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
public class SingleSign implements java.io.Serializable {
    /**
     * 登录用户名
     */
    private String userId;
    
    /**
     * 通过URLEncoder加密算法加密后查询参数字符串 <br>
     * [{code:"00323" name: "组装台位"}] <br>
     */
    private String queryStr;
    
    /**
     * 访问的页面Url
     */
    private String queryUrl;

    
    public String getQueryStr() {
        return queryStr;
    }

    
    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    
    public String getQueryUrl() {
        return queryUrl;
    }

    
    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    
    public String getUserId() {
        return userId;
    }

    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    
}
