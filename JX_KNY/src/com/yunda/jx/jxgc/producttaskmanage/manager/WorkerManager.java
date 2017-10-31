package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.Worker;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Worker业务类,作业人员
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workerManager")
public class WorkerManager extends JXBaseManager<Worker, Worker> {
    
    /**
     * <li>说明：获取其他处理人员列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单IDX
     * @param empid 人员ID
     * @return 其他处理人员列表
     */
    @SuppressWarnings("unchecked")
    public List<Worker> getOtherWorkerByWorkCard(String workCardIDX, String empid) {
        StringBuffer hql = new StringBuffer("from Worker where workCardIDX = '")
                                    .append(workCardIDX).append("' and workerID != ").append(empid);
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：根据作业工单主键获取作业人员
     * <li>创建人：何涛
     * <li>创建日期：2016-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单主键
     * @return 作业人员集合
     */
    @SuppressWarnings("unchecked")
    public List<Worker> getWorkersByWorkCard(String workCardIDX) {
        String hql = "From Worker Where workCardIDX = ?";
        return this.daoUtils.find(hql, new Object[]{ workCardIDX });
    }
    
    /**
     * <li>说明：删除非处理人员
     * <li>创建人：何涛
     * <li>创建日期：2016-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单主键
     */
    public void deleteWorkersByWorkCard(String workCardIDX) {
        List<Worker> entityList = this.getWorkersByWorkCard(workCardIDX);
        if (null == entityList || entityList.isEmpty()) {
            return;
        }
        this.daoUtils.getHibernateTemplate().deleteAll(entityList);
    }
    
}