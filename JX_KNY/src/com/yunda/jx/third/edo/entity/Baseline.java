package com.yunda.jx.third.edo.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 描述项目实际进度的数据，任务实体类，适用于易度项目管理（甘特图）控件从服务端返回给客户端的json数据结构
 * <li>创建人：程锐
 * <li>创建日期：2015-8-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Baseline {
    
    /** 任务开始日期 */
    private String Start;
    
    /** 任务完成日期 */
    private String Finish;
    
    public String getFinish() {
        return Finish;
    }
    
    public void setFinish(String finish) {
        Finish = finish;
    }
    
    public String getStart() {
        return Start;
    }
    
    public void setStart(String start) {
        Start = start;
    }
    
}
