-- Add/modify columns 
alter table K_DETAIN_TRAIN add jx_Time date;
alter table K_DETAIN_TRAIN add Repair_Class_IDX VARCHAR2(50);
alter table K_DETAIN_TRAIN add Repair_Class_Name VARCHAR2(50);
alter table K_DETAIN_TRAIN add Repair_time_IDX VARCHAR2(50);
alter table K_DETAIN_TRAIN add Repair_time_Name VARCHAR2(50);
-- Add comments to the columns 
comment on column K_DETAIN_TRAIN.jx_Time
  is '检修时间';
comment on column K_DETAIN_TRAIN.Repair_Class_IDX
  is '修程编码';
comment on column K_DETAIN_TRAIN.Repair_Class_Name
  is '修程名称';
comment on column K_DETAIN_TRAIN.Repair_time_IDX
  is '修次';
comment on column K_DETAIN_TRAIN.Repair_time_Name
  is '修次名称';
