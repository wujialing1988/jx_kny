package com.yunda.jx.pjjx.repairline.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.repairline.entity.PartsWorkStation;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsWorkStation业务类,配件检修工位
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsWorkStationManager")
public class PartsWorkStationManager extends JXBaseManager<PartsWorkStation, PartsWorkStation>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 
     * <li>说明：方法实现功能说明
     * <li>创建人：何涛
     * <li>创建日期：2016-1-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 配件检修流水线主键数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void deleteByRepairLineIds(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<PartsWorkStation> list = new ArrayList<PartsWorkStation>();
        List<PartsWorkStation> tempList = null;
        for (Serializable repairLineIdx : ids) {
            tempList = this.getModelsByRepairLineIdx((String) repairLineIdx);
            if (null != tempList && 0 < tempList.size()) {
                list.addAll(tempList);
            }
        }
        this.logicDelete(list);
    }
    
    /**
     * <li>说明：根据配件流水线主键查询配件检修工位集合
     * <li>创建人：何涛
     * <li>创建日期：2016-1-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param repairLineIdx 配件流水线主键
     * @return 配件检修工位集合
     */
    @SuppressWarnings("unchecked")
    private List<PartsWorkStation> getModelsByRepairLineIdx(String repairLineIdx) {
        String hql = "From PartsWorkStation Where recordStatus = 0 And repairLineIdx = ?";
        return this.daoUtils.find(hql, new Object[]{ repairLineIdx });
    }
    
}