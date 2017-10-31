-- Add/modify columns 
alter table JXGC_JOB_PROCESS_DEF add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column JXGC_JOB_PROCESS_DEF.T_VEHICLE_TYPE
  is '客货类型 10 货车 20 客车';
