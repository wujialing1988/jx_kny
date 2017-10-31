package com.yunda.jxpz.utils;

import com.yunda.Application;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;


public class CodeRuleUtil {
    
    /**
     * <li>方法说明：获取编码规则  
     * <li>方法名称：getRuleCode
     * <li>@param ruleFunction
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-2-17 上午11:06:51
     * <li>修改人：
     * <li>修改内容：
     */
    public static String getRuleCode(String ruleFunction){
        CodeRuleConfigManager m = (CodeRuleConfigManager)Application.getSpringApplicationContext().getBean("codeRuleConfigManager");
        return m.makeConfigRule(ruleFunction);
    }
}
