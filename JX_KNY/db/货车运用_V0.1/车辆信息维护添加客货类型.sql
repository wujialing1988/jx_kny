-- Add/modify columns 
alter table JCZL_TRAIN add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column JCZL_TRAIN.T_VEHICLE_TYPE
  is '客货类型';
