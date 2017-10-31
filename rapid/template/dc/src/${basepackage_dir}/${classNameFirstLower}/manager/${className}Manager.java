<#assign className = table.className>
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${classNameLower}.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import ${basepackage}.${classNameLower}.entity.${className};


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: ${table.tableAlias}业务类
 * <li>创建人：${authorName}
 * <li>创建日期：${now?string('yyyy-MM-dd HH:mm:ss')}
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("${classNameLower}Manager")
public class ${className}Manager extends JXBaseManager<${className}, ${className}> {
    
   
}
