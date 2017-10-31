package com.yunda.frame.baseapp.todojob.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.manager.TodoJobFunction;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 待办事宜业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-8-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Service(value = "todoJobManager")
public class TodoJobManager extends JXBaseManager<TodoJob, TodoJob> {
    
    private static final String HTML_DIV = "</div>";
    
    /**
     * <li>说明：获取当前登录人待办事宜列表，构造html字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-8-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return html字符串
     */
    @SuppressWarnings("all")
    public StringBuilder getTodoJobInfo() {
        List<TodoJob> list = TodoJobFunction.getInstance().getToDoListContext();
        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"tdlContextTab\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
        sb.append("<tr>");
        sb.append("<td width=\"20\"></td>");
        sb.append("<td width=\"*\" style=\"background-color: #ffffff;\">");
        for (int i = 0; i < list.size(); i++) {
            TodoJob tj = list.get(i);
            sb.append("<div class=\"tdlItemDiv\">");
            sb.append("<div class=\"tdlItemTypeDiv\">");
            sb.append("<div class=\"tdlItemTypeLeftDiv\"></div>");
            sb.append("<div class=\"tdlItemTypeCenterDiv\">");
            sb.append(tj.getJobType());
            sb.append(HTML_DIV);
            sb.append("<div class=\"tdlItemTypeRightDiv\"></div>");
            sb.append(HTML_DIV);
            sb.append("<div class=\"tdlItemJobDiv\" title=\"");
            sb.append(tj.getJobText());
            sb.append("\">");
//            sb.append("<b style=\"cursor: hand;\" onclick=\"window.parent.MainFrame.openNewTab('job");
            
            //判断url是否为空
            if (StringUtils.isNotBlank(tj.getJobUrl())) {
                sb.append("<b style=\"cursor: hand;\" onclick=\"top.MainFrame.openNewTab('job");
                sb.append(i);
                sb.append("','");
                sb.append(tj.getJobUrl());
                sb.append("','");
                sb.append(tj.getJobType());
                sb.append("',true)\">");
                sb.append(tj.getJobText().trim());
                sb.append("</b>");
            }else {
                sb.append("<b>");
                sb.append(tj.getJobText().trim() + " -PDA");
                sb.append("</b>");
            }
            
            sb.append(HTML_DIV);
            sb.append("<div class=\"tdlItemNumDiv\">");
            sb.append("<div class=\"tdlItemNumLeftDiv\"></div>");
            sb.append("<div class=\"tdlItemNumTextDiv\">");
            sb.append(tj.getJobNum());
            sb.append(HTML_DIV);
            sb.append("<div class=\"tdlItemNumRightDiv\"></div>");
            sb.append(HTML_DIV);
            sb.append(HTML_DIV);
            if (i % 2 == 0) 
                sb.append("<div style=\"width: 37px; height:26px; float: left; \">&nbsp;</div>");            
        }
        sb.append("</td>");
        sb.append("<td width=\"10\"></td>");
        sb.append("</tr>");
        sb.append("</table>");
        return sb;
    }
}
