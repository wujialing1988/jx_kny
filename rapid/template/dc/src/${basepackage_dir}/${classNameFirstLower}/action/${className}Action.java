<#assign className = table.className>
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${classNameLower}.action;

import org.apache.log4j.Logger;
import com.yunda.frame.common.JXBaseAction;
import ${basepackage}.${classNameLower}.entity.${className};
import ${basepackage}.${classNameLower}.manager.${className}Manager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: ${table.tableAlias}Action
 * <li>创建人：${authorName}
 * <li>创建日期：${now?string('yyyy-MM-dd HH:mm:ss')}
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class ${className}Action extends JXBaseAction<${className}, ${className}, ${className}Manager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    

   
    
    
}
