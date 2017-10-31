package com.yunda.jx.jxgc.workhis.repairactivity.manager;

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.jx.jxgc.workhis.repairactivity.entity.WorkPlanActivityHis;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivity;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivityDTO;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkPlanActivityHis业务类,机车检修作业计划-检修活动历史
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */ 
@Service(value = "workPlanActivityHisManager")
public class WorkPlanActivityHisManager extends JXBaseManager<WorkPlanActivityHis, WorkPlanActivityHis> {
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * <li>说明：根据检修作业计划主键idx查询机车检修作业计划-检修活动
     * <li>创建人：张迪
     * <li>创建日期：2016-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
       * @param whereListJson 查询条件，json格式
     * @param start 开始页码
     * @param limit 结束页码
     * @param orders 排序字段
     * @return Page<WorkPlanRepairActivityDTO> 检修记录单分页page
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws Exception
     */
    public Page<WorkPlanRepairActivityDTO> findWorkPlanRepairActivityByWorkPlanIDX(String whereListJson, Integer start, Integer limit, Order[] orders)throws SecurityException, NoSuchFieldException  {
        JSONObject ob = JSONObject.parseObject(whereListJson);
        
        //获取数据
        String activityCode = ob.getString("activityCode");//检修活动编码
        String activityName = ob.getString("activityName");//检修活动名称
        String rdpIDX = ob.getString("rdpIDX");//检修作业计划主键idx
       
        StringBuilder sb = new StringBuilder("");
        
        sb.append(" select t.idx,t.activityCode,t.activityName,t.repairProjectIDX, ");
        sb.append(" (case totleCount when 0 then 100 || '%' else oneCount/totleCount * 100 || '%' end) as completPercent ");
        sb.append(" from (select a.idx,a.activity_code activityCode, a.repair_project_idx repairProjectIDX , a.activity_name activityName, ");
        sb.append(" nvl((select count(*) from JXGC_WORK_CARD_HIS t1,jxgc_work_seq t2 where t1.work_seq_card_idx = t2.idx and t2.record_idx = b.idx and (t1.status = 'COMPLETE' or t1.status = 'FINISHED') and t1.record_status = 0 and t2.record_status = 0 and t1.rdp_idx = a.work_plan_idx),0) as oneCount, ");
        sb.append(" nvl((select count(*) from jxgc_work_card_his t1,jxgc_work_seq t2 where t1.work_seq_card_idx = t2.idx and t2.record_idx = b.idx and t1.record_status = 0 and t2.record_status = 0 and t1.rdp_idx = a.work_plan_idx),0) as totleCount ");
        sb.append(" from JXGC_WORK_PLAN_ACTIVITY_HIS a, jxgc_repair_project b ");
        sb.append(" where a.repair_project_idx = b.idx ");
        sb.append(" and b.record_status = 0 ");
        sb.append(" and a.work_plan_idx = '").append(rdpIDX).append("' ");
        //检修活动编码
        if (StringUtils.isNotBlank(activityCode)) {
            sb.append(" and a.activity_code like '%").append(activityCode).append("%' ");
        }
        //检修活动名称
        if (StringUtils.isNotBlank(activityName)) {
            sb.append(" and a.activity_name like '%").append(activityName).append("%' ");
        }
        
        // 排序处理
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = WorkPlanRepairActivity.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY a.").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" ORDER BY a.CREATE_TIME");
        }
        
        sb.append(" ) t ");
       
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), start, limit, false,WorkPlanRepairActivityDTO.class);
    }
 
}