package com.yunda.twt.asynchronouslogin.entity;
/**
 * <li>标题: 站场图综合管理平台
 * <li>说明：TWTAsynchronmousLongin实体类, 数据表：台位图点击查看web页面传入参数实体；
 * <li>创建人：汪东良
 * <li>创建日期：2015-05-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class TWTAsynchronmousLongin implements java.io.Serializable {
    
    /**
     * 登录用户名
     */
    private String userId;
    
    /**
     * 查询参数类型，用于区分是“TWTClient:机车”、“TWTStation:台位”或“视频监控点”
     */
    private String queryType;
    
    /**
     * 通过URLEncoder加密算法加密后查询参数字符串 <br>
     * “0”：表示机车信息参数； <br>
     * “1”：表示台位编号和台位名称JSON字符串格式为[{code:"00323" name: "组装台位"}] <br>
     * “2”:待明确TODO
     */
    private String queryStr;
    
    /**
     * 通过URLEncoder加密算法加密后的功能点名称
     */
    private String funcname;

    
    public String getFuncname() {
        return funcname;
    }

    
    public void setFuncname(String funcname) {
        this.funcname = funcname;
    }

    
    public String getQueryStr() {
        return queryStr;
    }

    
    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    
    public String getQueryType() {
        return queryType;
    }

    
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    
    public String getUserId() {
        return userId;
    }

    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
