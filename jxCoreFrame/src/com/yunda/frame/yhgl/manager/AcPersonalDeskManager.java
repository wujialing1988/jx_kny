/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：林欢
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

package com.yunda.frame.yhgl.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcPersonalDesk;


/**
 * <li>标题: 自定义桌面Manger
 * <li>说明: 业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "acPersonalDeskManager")
public class AcPersonalDeskManager extends JXBaseManager<AcPersonalDesk, AcPersonalDesk>{

    /**
     * <li>说明：通过当前登录人员ID查询自定义桌面信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 登陆人ID
     * @return List<AcPersonalDesk> 自定义桌面信息
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public List<AcPersonalDesk> findAcPersonalDeskListByoperatorID(Long operatorid) {
        String hql = " select a from AcPersonalDesk a,AcFunction b where a.funccode = b.funccode and a.operatorid = ? order by a.sequenceNumber ";
        List<AcPersonalDesk> list = daoUtils.find(hql, new Object[]{operatorid}); 
        return list;
    }
    
}
