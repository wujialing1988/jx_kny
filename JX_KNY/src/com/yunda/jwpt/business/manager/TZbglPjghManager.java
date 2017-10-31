/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：林欢
 * <li>创建日期：2016-5-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

package com.yunda.jwpt.business.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jwpt.business.entity.TZbglPjgh;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备电子合格证- JT6提票（配件更换）(机务平台数据同步官方模型) 业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Service(value = "tZbglPjghManager")
public class TZbglPjghManager extends JXBaseManager<TZbglPjgh, TZbglPjgh>{
    
    /**
     * <li>方法说明：更具jt6ID查询TZbglPjghList
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @param tZbglPjgh 查询条件封装
     * @return TZbglPjgh 返回对象
     */
    @SuppressWarnings("unchecked")
    public TZbglPjgh findTZbglPjghByJt6ID(TZbglPjgh tZbglPjgh){
        StringBuffer sb = new StringBuffer();
        sb.append(" from TZbglPjgh where pjghJt6ID = '").append(tZbglPjgh.getPjghJt6ID()).append("'");
        
        return (TZbglPjgh) this.findSingle(sb.toString());
    }
}
