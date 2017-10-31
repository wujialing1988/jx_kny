-- Add/modify columns 
alter table SCDD_TRAIN_ENFORCE_PLAN add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column SCDD_TRAIN_ENFORCE_PLAN.T_VEHICLE_TYPE
  is '客货类型 10 货车 20 客车';

  
-- Add/modify columns 
alter table SCDD_TRAIN_ENFORCE_PLAN_DETAIL add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column SCDD_TRAIN_ENFORCE_PLAN_DETAIL.T_VEHICLE_TYPE
  is '客货类型 10 货车 20 客车';
  
 -- Add/modify columns 
alter table SCDD_TRAIN_ENFORCE_PLAN_DETAIL modify TRAIN_TYPE_IDX VARCHAR2(50 CHAR);
alter table SCDD_TRAIN_ENFORCE_PLAN_DETAIL modify TRAIN_TYPE_SHORTNAME VARCHAR2(50 CHAR);
 
  