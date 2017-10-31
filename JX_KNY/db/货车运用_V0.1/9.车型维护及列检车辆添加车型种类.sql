-- Add/modify columns 
alter table ZB_ZBGL_PLAN_RECORD add T_VEHICLE_KIND_CODE VARCHAR2(50 CHAR);
alter table ZB_ZBGL_PLAN_RECORD add T_VEHICLE_KIND_NAME VARCHAR2(200 CHAR);
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN_RECORD.T_VEHICLE_KIND_CODE
  is '车型种类编码';
comment on column ZB_ZBGL_PLAN_RECORD.T_VEHICLE_KIND_NAME
  is '车型种类名称';

  
  -- Add/modify columns 
alter table J_JCGY_VEHICLE_TYPE add T_VEHICLE_KIND_CODE VARCHAR2(50 CHAR);
alter table J_JCGY_VEHICLE_TYPE add T_VEHICLE_KIND_NAME VARCHAR2(200 CHAR);
-- Add comments to the columns 
comment on column J_JCGY_VEHICLE_TYPE.T_VEHICLE_KIND_CODE
  is '车型种类编码';
comment on column J_JCGY_VEHICLE_TYPE.T_VEHICLE_KIND_NAME
  is '车型种类名称';