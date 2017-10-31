-- Add/modify columns 
alter table J_JCGY_XC add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column J_JCGY_XC.T_VEHICLE_TYPE
  is '客货类型 10 货车 20 客车';
  
  -- Add/modify columns 
alter table JCJX_REPAIR_STANDARD modify TRAIN_TYPE_IDX VARCHAR2(50);
  
