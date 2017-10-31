
package com.yunda.jx.jxgc.workhis.workcard.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardBean;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.workhis.workcard.entity.WorkCardHis;
import com.yunda.jx.webservice.stationTerminal.base.entity.DataItemBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCardHis业务类,作业工单历史
 * <li>创建人：程锐
 * <li>创建日期：2012-12-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "workCardHisManager")
public class WorkCardHisManager extends JXBaseManager<WorkCardHis, WorkCardHis> {
	/** 日志工具 */
	@SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    /**WorkTask业务类,作业任务*/
    @Resource
    private WorkTaskHisManager workTaskHisManager;
    
    @Resource
    private WorkCardManager workCardManager;
    
    /**DetectResult业务类,检测结果*/
    @Resource
    private DetectResultHisManager detectResultHisManager;
    @Resource
    private QCResultHisManager  qCResultHisManager;
    
    @Resource
    private OmEmployeeManager  omEmployeeManager;
    
    
    
    /**
     * <li>说明：根据历史记录单查询记录卡详情
     * <li>创建人：张迪
     * <li>创建日期：2016-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanRepairActivityIDX 记录单id
     * @return 记录单集合
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<WorkCardBean> findWorkCardHisByWorkPlanRepairActivityIDX(String workPlanRepairActivityIDX) throws Exception {
        //获取机车检修记录单list
        List<WorkCardBean> workCardBeanList = findWorkCardInfoByRepairActivityIDX(workPlanRepairActivityIDX);
        //循环遍历准备封装任务单
        for (WorkCardBean workCardBean : workCardBeanList) {
            
            //通过机车检修记录卡id查询作业任务
            List<WorkTaskBean> workTaskBeanList = workTaskHisManager.getListByWorkCard(workCardBean.getIdx());
//            List<WorkTaskBean> workTaskBeanList = BeanUtils.copyListToList(WorkTaskBean.class, workTaskHisList);
            
            //遍历循环，封装检查项
            for (WorkTaskBean workTaskBean : workTaskBeanList) {
                //查询检查结果
                List<DataItemBean> detectResultHisList = detectResultHisManager.getListByWorkTask(workTaskBean.getIdx());
//                List<DataItemBean> dataItemList = BeanUtils.copyListToList(DataItemBean.class, detectResultHisList);
                workTaskBean.setDataItemList(detectResultHisList);
                
            }
            //任务单赋值
            workCardBean.setWorkTaskBeanList(workTaskBeanList);
   
            // 通过机车检修记录单idx查询机车检修质量检验结果
            List<QCResult> qCResultHisList = qCResultHisManager.findListByWorkCard(workCardBean.getIdx());
//            List<QCResult> qCResultList = BeanUtils.copyListToList(QCResult.class, qCResultHisList);
            //机车检修质量检验结果赋值
            workCardBean.setQCResultList(qCResultHisList);
            
            //自检人员赋值 修改人
            OmEmployee omEmployee = omEmployeeManager.findByOperator(workCardBean.getUpdator());       
            workCardBean.setUpdatorName(omEmployee.getEmpname());
        }
        
        return workCardBeanList;
    }

 
    /**
     * <li>说明：机车检修作业计划-检修活动IDX查询机车检修作业计划-检修活动下的所有检修记录卡详情
     * <li>创建人：张迪
     * <li>创建日期：2016-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanRepairActivityIDX 检修记录单id
     * @return 检修活动下的所有检修记录卡详情
     * @throws BusinessException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<WorkCardBean> findWorkCardInfoByRepairActivityIDX(String workPlanRepairActivityIDX) throws BusinessException, Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" select new WorkCardBean(c.idx, c.workCardName, c.worker, c.rdpIDX ,c.updator) from WorkCardHis c where repairActivityIDX = ? and recordStatus = 0 ");
        //获取到数据list
        List<WorkCardBean> workCardHisList = getDaoUtils().find(sb.toString(),new Object[]{workPlanRepairActivityIDX});
        return  workCardHisList;
    }


    /**
     * <li>说明：根据状态判断是查询历史还是当前记录卡
     * <li>创建人：张迪
     * <li>创建日期：2016-9-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanRepairActivityIDX 检修活动idx
     * @param workPlanStatus 转历史状态
     * @return  记录单集合
     * @throws Exception 
     */
    public  List<WorkCardBean> findListByWorkPlanRepairActivityIDX(String workPlanRepairActivityIDX, String workPlanStatus) throws Exception {
        if(null != workPlanStatus && workPlanStatus.equals("Complete") ){
            List<WorkCardBean> workCardBeanHisList = this.findWorkCardHisByWorkPlanRepairActivityIDX(workPlanRepairActivityIDX);
            return workCardBeanHisList;
        }
        else{             
            List<WorkCardBean> workCardBeanList = workCardManager.findWorkCardInfoByWorkPlanRepairActivityIDX(workPlanRepairActivityIDX);
            return workCardBeanList;
        }
        
    }
    
}
