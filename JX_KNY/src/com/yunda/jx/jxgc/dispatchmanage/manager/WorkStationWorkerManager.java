package com.yunda.jx.jxgc.dispatchmanage.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStationWorker;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStationWorker业务类,工位作业人员
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workStationWorkerManager")
public class WorkStationWorkerManager extends JXBaseManager<WorkStationWorker, WorkStationWorker>{
	
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}	
    /**
     * <li>说明：批量添加工位作业人员
     * <li>创建人：程锐
     * <li>创建日期：2012-12-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workStationWorkers 作业人员实体对象数组
     * @throws BusinessException, NoSuchFieldException
     */
    public void saveOrUpdateBatch(WorkStationWorker[] workStationWorkers) throws BusinessException, NoSuchFieldException {
        for (WorkStationWorker workStationWorker : workStationWorkers) {
            this.saveOrUpdate(workStationWorker);
        }
    }
       
    /**
     * <li>说明：根据“工位主键”获取工位作业人员集合
     * <li>创建人：何涛
     * <li>创建日期：2015-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workStationIDX 工位主键
     * @return 工位作业人员集合
     */
    @SuppressWarnings("unchecked")
    private List<WorkStationWorker> getModelsByWorkStationIDX(String workStationIDX) {
        String hql = "From WorkStationWorker Where recordStatus = 0 And workStationIDX = ?";
        return this.daoUtils.find(hql,  new Object[]{ workStationIDX });
    }
    
    /**
     * <li>说明：根据“工位主键”删除工位作业人员
     * <li>创建人：何涛
     * <li>创建日期：2015-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workStationIDX 工位主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void deleteByWorkStationIDX(String workStationIDX) throws BusinessException, NoSuchFieldException {
         List<WorkStationWorker> list = this.getModelsByWorkStationIDX(workStationIDX);
         if (null == list || 0 >= list.size()) {
             return;
         }
         this.logicDelete(list);
    }
    
}