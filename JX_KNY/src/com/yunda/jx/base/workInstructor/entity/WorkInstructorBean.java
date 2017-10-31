package com.yunda.jx.base.workInstructor.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 作业指导书内容
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class WorkInstructorBean implements java.io.Serializable {

    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    
    /**
     * 指导书地址
     */
    private String instructorUrl ;
    
    /**
     * 作业指导书目录
     */
    private List<WorkInstructor> workInstructors = new ArrayList<WorkInstructor>();

    
    public List<WorkInstructor> getWorkInstructors() {
        return workInstructors;
    }
    
    public void setWorkInstructors(List<WorkInstructor> workInstructors) {
        this.workInstructors = workInstructors;
    }

    
    public String getInstructorUrl() {
        return instructorUrl;
    }

    
    public void setInstructorUrl(String instructorUrl) {
        this.instructorUrl = instructorUrl;
    }
    
    
    
}
