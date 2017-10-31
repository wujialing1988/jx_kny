package com.yunda.jwpt.business.manager;

import org.springframework.stereotype.Service;

import com.yunda.jwpt.business.entity.JwptJcjxjhJhmx;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JxglJcjxjhJhmx业务类,机车生产计划明细
 * <li>创建人：何涛
 * <li>创建日期：2016-06-01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jwptJcjxjhJhmxManager")
public class JwptJcjxjhJhmxManager extends JwptBaseManager<JwptJcjxjhJhmx, JwptJcjxjhJhmx> {

    /**
     * <li>说明：获取总公司机务平台同步数据表对应的java实体类的全路径名称
     * <li>创建人：何涛
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 总公司机务平台同步数据表对应的java实体类的全路径名称
     */
    @Override
    protected String getEntityClass() {
        return JwptBaseManager.jwptJcjxjhJhmx;
    }
    
}
