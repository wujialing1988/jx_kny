-- Add/modify columns 
alter table K_CLASS_MAINTAIN add T_VEHICLE_TYPE VARCHAR2(20 CHAR);
-- Add comments to the columns 
comment on column K_CLASS_MAINTAIN.T_VEHICLE_TYPE
  is '客货类型';
