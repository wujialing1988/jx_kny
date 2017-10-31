package com.yunda.jx.jxgc.buildupmanage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFaultMethod;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PlaceFault业务类,故障现象
 * <li>创建人：程锐
 * <li>创建日期：2013-01-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="placeFaultManager")
public class PlaceFaultManager extends JXBaseManager<PlaceFault, PlaceFault>{
	/** 故障现象处理方法业务类 */
	@Resource
    private PlaceFaultMethodManager placeFaultMethodManager;

    /**
     * <li>说明：批量保存故障现象
     * <li>创建人：程锐
     * <li>创建日期：2013-1-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 故障现象实体对象数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void saveOrUpdateList(PlaceFault[] list) throws BusinessException, NoSuchFieldException {
    	if(list == null || list.length < 1) return;
    	for (PlaceFault placeFault : list) {
			if (!StringUtil.isNullOrBlank(placeFault.getBuildUpPlaceIdx())) {
				placeFault.setBuildUpTypeIdx("");// 安装位置主键和组成主键只能设置一个
				List<PlaceFault> faultList = findFaultList(placeFault.getBuildUpPlaceIdx());
				for (PlaceFault fault : faultList) {
					if (fault.getFaultName().equals(placeFault.getFaultName()))
						throw new BusinessException("该位置已关联有相同的故障现象，请重新选择或输入故障现象");
				}
			}
			saveOrUpdate(placeFault);
		}
    } 
    /**
	 * <li>说明：通过组成位置主键查询故障现象
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-2-6
	 * <li>修改人： 程锐
	 * <li>修改日期：2014-07-02 同时查询出组成位置关联的可安装组成型号对应的故障现象并去重
	 * <li>修改内容：（此方法同时用于工位终端查询）
	 * @param buildUpPlaceIdx：组成位置主键
	 * @return List<PlaceFault> 故障现象列表
	 * @throws BusinessException
	 */
    @SuppressWarnings("unchecked")
    public List<PlaceFault> findFaultList(String buildUpPlaceIdx) throws BusinessException{        
    	String sql = "SELECT\n" +
    				" DISTINCT (A.FAULT_NAME) faultName,\n" + 
			        " A.FAULT_ID faultId\n" + 			        
			        " FROM\n" + 
			        " JXGC_PLACE_FAULT A\n" + 
			        " WHERE\n" + 
			        " EXISTS (SELECT 'X' FROM JXGC_FIX_BUILDUP_TYPE B \n" +
			        " WHERE B.BUILDUP_TYPE_IDX = A.BUILDUP_TYPE_IDX AND B.FIX_PLACE_IDX = '" + buildUpPlaceIdx + "')\n" + 
			        " UNION ALL\n" + 
			        " SELECT\n" + 
			        " DISTINCT (A.FAULT_NAME) faultName,\n" + 
			        " A.FAULT_ID faultId\n" + 			        
			        " FROM\n" + 
			        " JXGC_PLACE_FAULT A\n" + 
			        " WHERE A.BUILDUP_PLACE_IDX = '" + buildUpPlaceIdx + "'" +
                    " ORDER BY faultName";
        List list = daoUtils.executeSqlQuery(sql);
        if (list == null || list.size() < 1) return new ArrayList<PlaceFault>();
        List<PlaceFault> faultList = new ArrayList<PlaceFault>();
        for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[]) list.get(i);
			PlaceFault fault = new PlaceFault();
			fault.setFaultId(objs[1] != null ? objs[1].toString() : "");
			fault.setFaultName(objs[0] != null ? objs[0].toString() : "");
			faultList.add(fault);
		}
        return faultList;
    }
    /**
     * 
     * <li>说明：删除位置和组成上的故障现象级联删除其处理方法
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void deletePlaceFault(Serializable... ids) throws BusinessException, NoSuchFieldException {
        for(Serializable id : ids){
            List<PlaceFaultMethod> methodList = placeFaultMethodManager.getListByPlaceFaultId(id.toString());
            placeFaultMethodManager.logicDelete(methodList);
        }
        logicDelete(ids);
    }
}