-- Add/modify columns 
alter table K_GZTP add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column K_GZTP.T_VEHICLE_TYPE
  is '客货类型';
  
  -- Add/modify columns 
alter table K_QC_ITEM_DEFINE add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column K_QC_ITEM_DEFINE.T_VEHICLE_TYPE
  is '客货类型';
  
  
  -- Add/modify columns 
alter table K_DETAIN_TRAIN add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column K_DETAIN_TRAIN.T_VEHICLE_TYPE
  is '客货类型';
  
  