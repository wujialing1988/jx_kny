package com.yunda.webservice.employee.entity;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 应用功能实体bean
 * <li>创建人：程锐
 * <li>创建日期：2014-5-30
 * <li>修改人: 何涛
 * <li>修改日期：2015-11-25
 * <li>修改内容：实现比较器接口，用于该类型的集合利用“功能调用入口”进行排序
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.0.1
 */
@SuppressWarnings("serial")
public class AcFunctionBean implements java.io.Serializable, Comparable<AcFunctionBean> {
    
    /** 功能编号 */
    private String funccode;
    
    /** 功能名称 */
    private String funcname;
    
    /** 功能调用入口 */
    private String funcaction;
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-8-20
     * <li>修改人： 
     * <li>修改日期：
     */
    public AcFunctionBean() {
        
    }
    
    /**
     * <li>说明：带参构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-8-20
     * <li>修改人： 
     * <li>修改日期：
     * @param funccode 应用功能编码
     * @param funcname 应用功能名称
     * @param funcaction 应用功能入口
     */
    public AcFunctionBean(String funccode, String funcname, String funcaction) {
        this.funccode = funccode;
        this.funcname = funcname;
        this.funcaction = funcaction;
    }
    
    public String getFuncaction() {
        return funcaction;
    }
    
    public void setFuncaction(String funcaction) {
        this.funcaction = funcaction;
    }
    
    public String getFunccode() {
        return funccode;
    }
    
    public void setFunccode(String funccode) {
        this.funccode = funccode;
    }
    
    public String getFuncname() {
        return funcname;
    }
    
    public void setFuncname(String funcname) {
        this.funcname = funcname;
    }

    /**
     * <li>说明：实现排序接口，利用“功能调用入口”进行排序
     * <li>创建人：程锐
     * <li>创建日期：2015-11-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 根据“功能调用入口”所表示的字符串大于、等于还是小于比较对象相应属性（不考虑大小写），分别返回一个负整数、0 或一个正整数。
     * @param o 比较对象
     * @throws Exception
     */
    public int compareTo(AcFunctionBean o) {
        if (null == this.funcaction || this.funcaction.trim().length() <= 0) return 1;
        if (null == o.funcaction || this.funcaction.trim().length() <= 0) return -1;
        try {
            int a = Integer.parseInt(this.funcaction);
            int b = Integer.parseInt(o.funcaction);
            if (a < b) return -1;
            if (a > b) return 1;
            return 0;
        } catch (Throwable e) {
            return this.funcaction.compareToIgnoreCase(o.getFuncaction());
        }
    }
    
}
