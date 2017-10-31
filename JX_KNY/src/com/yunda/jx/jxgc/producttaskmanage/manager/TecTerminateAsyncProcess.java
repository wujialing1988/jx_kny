/**
 * <li>文件名：TecTerminateAsyncProcess.java
 * <li>标题：
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建日期：2014-8-8
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 */
package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.util.List;

import com.yunda.jx.jxgc.producttaskmanage.entity.ProcessTask;
import com.yunda.util.DaoUtils;


/**
 * <li>标题：异步流程终止
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-8-8
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 * 
 */
public class TecTerminateAsyncProcess {
        
    /**
     * <li>方法说明：获取需要删除的流程数据 
     * <li>方法名称：findNeedDelProcess
     * <li>@param daoUtils
     * <li>@param rdpIdx
     * <li>@return
     * <li>return: List<ProcessTask>
     * <li>创建人：张凡
     * <li>创建时间：2014-8-12 上午10:13:59
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private static List<ProcessTask> findNeedDelProcess(DaoUtils daoUtils, String rdpIdx) {
        String hql = "from ProcessTask where token in ('" + ProcessTask.TYPE_ASY_QUA + "','" + ProcessTask.TYPE_TP_ASY_QUA 
                + "') and rdpIdx = '" + rdpIdx + "' and currentState='" + ProcessTask.STATE_RUNNING + "'";
        List<ProcessTask> list = daoUtils.find(hql);
        return list;
    }
}
