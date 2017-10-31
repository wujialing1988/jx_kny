package com.yunda.jx.jxgc.tpmanage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResult;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResultVO;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.entity.RepairEmpBean;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.third.poi.excel.ColumnPattern;
import com.yunda.third.poi.excel.ExcelExport;
import com.yunda.third.poi.excel.ExportExcelDTO;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：FaultTicket控制器, 故障提票
 * <li>创建人：程锐
 * <li>创建日期：2015-06-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@SuppressWarnings(value = "serial")
public class FaultTicketAction extends JXBaseAction<FaultTicket, FaultTicket, FaultTicketManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：检查有无同车同位置同故障现象的未处理的提票
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void checkData() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            FaultTicket entity = (FaultTicket) JSONUtil.read(getRequest(), FaultTicket.class);
            if (manager.checkData(entity)) {
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存并实例化提票
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveTpAndInst() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            FaultTicket[] entities = JSONUtil.read(searchJson, FaultTicket[].class);
            
            FaultTicket entity = entities.length > 0 ? entities[0] : null; // 取第一条提票信息
            if (entity != null) {
                this.manager.saveTpAndInst(entities, entity);
                map.put(Constants.SUCCESS, true);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：提票调度派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateForDdpg() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            FaultTicket data = (FaultTicket) JSONUtil.read(getRequest(), entity.getClass());
            String[] ids = (String[]) JSONUtil.read(getRequest().getParameter("ids"), String[].class);
            this.manager.updateForDdpg(data, ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：提票工长派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateForGzpg() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String[] empids = (String[]) JSONUtil.read(getRequest().getParameter("empids"), String[].class); 
            this.manager.updateForGzpg(empids, ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取其他作业人员和质检项JSON列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getWorkerAndQcByTP() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String empid = getRequest().getParameter("empid");
            List<RepairEmpBean> workerList = manager.getOtherWorkerByTP(id, empid);
            if (workerList != null && workerList.size() > 0) {
                map.put("workerList", JSONUtil.write(workerList));
                // 获取其他作业人员（iPad应用）
                map.put("workerListForPad", workerList);
            }
            List<FaultQCResult> qcList = manager.getIsAssignCheckItems(id);
            if (qcList != null && qcList.size() > 0) {
                map.put("qcList", JSONUtil.write(qcList));
                // 获取质检项（iPad应用）
                map.put("qcListForPad", qcList);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：销票
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void handle() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entityJson = StringUtil.nvlTrim(getRequest().getParameter("entityJson"), "{}");
            FaultTicket entity = JSONUtil.read(entityJson, FaultTicket.class);
            FaultQCResultVO[] qcResult = JSONUtil.read(getRequest(), FaultQCResultVO[].class);            
            manager.handle(entity, qcResult);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：查询导出
     * <li>创建人：张迪
     * <li>创建日期：2016-10-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public void exportFaultTicketListByParm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
//      参数map
        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<ExportExcelDTO> expList = new ArrayList<ExportExcelDTO>();
        try {
            String trainTypeIDX = getRequest().getParameter("trainTypeIDX");
            String trainNo = getRequest().getParameter("trainNo");
            String faultDesc = getRequest().getParameter("faultDesc");
            String type = getRequest().getParameter("type");
            String vehicleType = getRequest().getParameter("vehicleType"); // 客货类型
            String status = getRequest().getParameter("status");
            String fields = StringUtil.nvlTrim(getRequest().getParameter("fields"), "");
            ColumnPattern[] col = JSONUtil.read(getRequest().getParameter("patterns"), ColumnPattern[].class);
            
            QueryCriteria<FaultTicket> query = new QueryCriteria<FaultTicket>();
            query.setEntityClass(FaultTicket.class);
            query.setLimit(10000);
            query.setStart(0);
            List<Condition> whereList = new ArrayList<Condition>();
            String[] statusStr= status.split(",");
            Integer[] statusArray = new Integer[statusStr.length];
            for(int i=0; i< statusStr.length; i++){
                statusArray[i]= Integer.valueOf(statusStr[i]);
            }
            if(null != statusArray){
                whereList.add(new Condition("status", Condition.IN, statusArray)); 
            }
            if(null != trainTypeIDX){
                whereList.add(new Condition("trainTypeIDX", Condition.EQ, trainTypeIDX)); 
            }
            if(null != trainNo){
                whereList.add(new Condition("trainNo", Condition.LIKE, trainNo));   
            }
            if(null != type){
                whereList.add(new Condition("type", Condition.LIKE, type));   
            }
            if(null != faultDesc){
                whereList.add(new Condition("faultDesc", Condition.LIKE, faultDesc));   
            }
            
            // 客货类型
            if(!StringUtil.isNullOrBlank(vehicleType)){
                whereList.add(new Condition("vehicleType", Condition.EQ, vehicleType)); 
            }
              
            query.setWhereList(whereList);           
            ExportExcelDTO dto1 = new ExportExcelDTO();
            Page<FaultTicket> page = this.manager.findPageList(query);
            dto1.setPages(page);
            dto1.setSheetName("主列表信息");
            dto1.setPattern(col);
            expList.add(dto1);
            
            ExcelExport.exportExcel(expList,  "提票查询", getResponse());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            getResponse().getWriter().print("导出文件发生错误！");
        }
    }
}
