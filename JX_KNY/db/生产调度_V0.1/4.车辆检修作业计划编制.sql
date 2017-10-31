-- Add/modify columns 
alter table JXGC_TRAIN_WORK_PLAN modify TRAIN_TYPE_IDX VARCHAR2(50 CHAR);
alter table JXGC_TRAIN_WORK_PLAN modify TRAIN_TYPE_SHORTNAME VARCHAR2(50 CHAR);
alter table JXGC_TRAIN_WORK_PLAN modify REPAIR_CLASS_IDX VARCHAR2(50 CHAR);
alter table JXGC_TRAIN_WORK_PLAN modify REPAIR_TIME_IDX VARCHAR2(50 CHAR);
alter table JXGC_TRAIN_WORK_PLAN add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column JXGC_TRAIN_WORK_PLAN.T_VEHICLE_TYPE
  is '客货类型 10 货车 20 客车';
