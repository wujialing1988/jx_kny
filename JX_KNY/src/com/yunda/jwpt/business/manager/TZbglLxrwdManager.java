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
import com.yunda.jwpt.business.entity.TZbglLxrwd;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备电子合格证-JT6提票(机务平台数据同步官方模型)（临修 处理后上传结果）业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Service(value = "tZbglLxrwdManager")
public class TZbglLxrwdManager extends JXBaseManager<TZbglLxrwd, TZbglLxrwd>{

    /**
     * <li>方法说明：更具jt6ID查询TZbglLxrwd
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @param tZbglLxrwd 查询条件封装
     * @return TZbglLxrwd 返回对象
     */
    @SuppressWarnings("unchecked")
    public TZbglLxrwd findTZbglLxrwdByJt6ID(TZbglLxrwd tZbglLxrwd){
        StringBuffer sb = new StringBuffer();
        sb.append(" from TZbglLxrwd where lxJt6ID = '").append(tZbglLxrwd.getLxJt6ID()).append("'");
        
        return (TZbglLxrwd)this.findSingle(sb.toString());
    }
    
}
