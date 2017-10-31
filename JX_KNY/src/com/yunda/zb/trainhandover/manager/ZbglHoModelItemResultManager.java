package com.yunda.zb.trainhandover.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.zb.trainhandover.entity.ZbglHoModelItemResult;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglHoModelItemResult业务类,机车交接模板—交接项情况
 * <li>创建人：程梅
 * <li>创建日期：2015-02-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "zbglHoModelItemResultManager")
public class ZbglHoModelItemResultManager extends JXBaseManager<ZbglHoModelItemResult, ZbglHoModelItemResult> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：逻辑删除与机车交接项关联的机车交接情况模板
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param handOverItemIDX 交接项id
     */
    public void logicDeleteAllResultForModel(String handOverItemIDX) {
        String sql = SqlMapUtil.getSql("zb-trainHandOver:logicDeleteAllResultForModel");
        sql += "(" + SqlMapUtil.getSql("zb-trainHandOver:findAllChildIDXForModel").replace("#modelIDX#", handOverItemIDX) + ")";
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：获取与机车交接项关联的机车交接情况模板
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param handOverItemIDX 交接项id
     * @return 机车交接情况模板列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglHoModelItemResult> getListByModel(String handOverItemIDX) {
        ZbglHoModelItemResult result = new ZbglHoModelItemResult();
        result.setRecordStatus(0);
        result.setHandOverItemIDX(handOverItemIDX);
        return daoUtils.getHibernateTemplate().findByExample(result);
    }
}
