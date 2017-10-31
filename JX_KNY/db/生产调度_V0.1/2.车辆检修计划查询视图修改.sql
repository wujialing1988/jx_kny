CREATE OR REPLACE VIEW V_SCDD_PLAN_DETAIL_SEARCH AS
SELECT td.startplandate,
          td.idx,
          td.train_enforce_plan_idx,
          td.train_type_idx,
          td.train_type_shortname,
          td.train_no,
          td.b_id,
          td.b_name,
          td.b_shortname,
          td.d_id,
          td.d_name,
          td.d_shortname,
          td.used_d_id,
          td.used_d_name,
          td.used_d_shortname,
          td.repair_class_idx,
          td.repair_class_name,
          td.repair_time_idx,
          td.repair_time_name,
          td.work_number,
          td.undertake_orgid,
          td.undertake_orgseq,
          td.undertake_orgname,
          td.plan_start_date,
          td.plan_end_date,
          td.real_start_date,
          td.real_end_date,
          td.plan_status,
          td.remarks,
          td.record_status,
          td.siteid,
          td.creator,
          td.create_time,
          td.updator,
          td.update_time,
          td.t_vehicle_type ,
          p.out_deport_date,                                 --实际出厂日期（出厂/段修日期）
          p.enter_deport_date,                         --实际进车日期（作业进度表的入厂/段修日期）
          p.complete_date,                                --实际交车日期（作业进度表的落成日期）
          TRUNC (p.complete_date) - TRUNC (p.enter_deport_date) stoptime, --停时（落成日期Complete_Date-入厂/段修日期Enter_Deport_Date），单位天
          TRUNC (p.out_deport_date) - TRUNC (p.enter_deport_date) infactory --在厂（出厂/段修日期Out_Deport_Date-入厂/段修日期Enter_Deport_Date)，单位天
     FROM    (SELECT t.plan_start_date startplandate,               --生产计划开始日期
                                                     d.*              --生产计划明细
                FROM scdd_train_enforce_plan t,
                     scdd_train_enforce_plan_detail d
               WHERE     t.idx = d.train_enforce_plan_idx
                     AND t.record_status = 0
                     AND d.record_status = 0) td
          LEFT JOIN
             scdd_work_progress p
          ON td.idx = p.train_enforce_plan_detail_idx AND p.record_status = 0;
