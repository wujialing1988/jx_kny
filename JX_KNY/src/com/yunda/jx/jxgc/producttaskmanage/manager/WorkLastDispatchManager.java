package com.yunda.jx.jxgc.producttaskmanage.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkLastDispatch;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 存储最后一次派工记录 业务类
 * <li>创建人：张凡
 * <li>创建日期：2013-12-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="workLastDispatchManager")
public class WorkLastDispatchManager extends JXBaseManager<WorkLastDispatch, WorkLastDispatch> {
	/** 人员选择业务类 */
	@Resource
	private OmEmployeeSelectManager omEmployeeSelectManager;
    
    /**
     * <li>方法说明：存储最后一次派工记录 
     * <li>方法名称：saveLastDispatcher
     * <li>@param idxs
     * <li>@param empids
     * <li>@param operatorid
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2013-12-1 下午02:34:30
     * <li>修改人：
     * <li>修改内容：
     */
    public int saveLastDispatcher(String idxs, String empids, long operatorid){
        
        String empname = omEmployeeSelectManager.findEmpName(empids);
        
        delete(idxs);
        int result = 0;
        
        String[] idx = splitIdxs(idxs, 100);//每次执行100条数据的新增
        
        for(String ids : idx){
            result += insert(ids, empids, operatorid, empname);
        }
        return result;
    }

    /**
     * <li>方法说明：删除最后一次派工记录 
     * <li>方法名称：delete
     * <li>@param idxs
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-12-18 下午03:58:16
     * <li>修改人：
     * <li>修改内容：
     */
    private void delete(String idxs) {
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:deleteLastDispatch").replace("作业工单主键", idxs);
        daoUtils.executeSql(sql);//删除最后一次派工记录
    }

    /**
     * <li>方法说明：新增最后一次派工记录 
     * <li>方法名称：insert
     * <li>@param idxs
     * <li>@param empids
     * <li>@param operatorid
     * <li>@param empname
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2013-12-18 下午03:57:04
     * <li>修改人：
     * <li>修改内容：
     */
    private int insert(String idxs, String empids, long operatorid, String empname) {
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:insertLastDispatch")
                               .replace("操作员", operatorid+"")
                               .replace("作业人员名称", empname)
                               .replace("作业人员ID", empids)
                               .replace("站点", JXSystemProperties.SYN_SITEID)
                               .replace("作业工单主键", idxs)
                               .replace("初始化", WorkCard.STATUS_NEW)
                               .replace("开放中", WorkCard.STATUS_OPEN)
                               .replace("处理中", WorkCard.STATUS_HANDLING)
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
        return daoUtils.executeSql(sql);    //新增最后一次派工记录
    }
    
    /**
     * <li>方法说明：在一组IDX中分隔成指定长度的数组 
     * <li>方法名称：splitIdxs
     * <li>@param ids
     * <li>@param size
     * <li>@return
     * <li>return: String[]
     * <li>创建人：张凡
     * <li>创建时间：2013-12-18 下午03:51:05
     * <li>修改人：
     * <li>修改内容：
     */
    private static String[] splitIdxs(String ids, int size){
        
        int fromIndex = 0;
        int endIndex = 0;
        java.util.List<String> splitIdx = new java.util.ArrayList<String>();
        do {
            for (int i = 0; i < size; i++) {
                
                endIndex = ids.indexOf(",", endIndex + 1);
                if (endIndex == -1) {
                    break;
                }
            }
            if(splitIdx.size() > 0){
                fromIndex += 2;
            }
            if(endIndex == -1){
                splitIdx.add(ids.substring(fromIndex));
            }else{               
                splitIdx.add(ids.substring(fromIndex, endIndex));
            }
            fromIndex = endIndex - 1;
        } while (endIndex != -1);
        String[] idx = new String[splitIdx.size()];
        return splitIdx.toArray(idx);
    }
}
