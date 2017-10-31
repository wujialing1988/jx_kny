create or replace view jxgc_train_record_view as
select SYS_GUID () as idx, t.train_type_idx,t.train_type_shortname,t.train_no ,t.t_vehicle_type
from       jxgc_train_work_plan    t 
where t.work_plan_status in ('COMPLETE') and t.record_status = 0 
group by t.train_type_idx,t.train_type_shortname,t.train_no,t.t_vehicle_type;
