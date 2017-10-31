-- Add/modify columns 
alter table JXGC_FAULT_TICKET add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column JXGC_FAULT_TICKET.T_VEHICLE_TYPE
  is '客货类型 10 货车 20 客车';
  
  
 -- Add/modify columns 
alter table JXGC_FAULT_TICKET modify TRAIN_TYPE_IDX VARCHAR2(50 CHAR);
alter table JXGC_FAULT_TICKET modify TRAIN_TYPE_SHORTNAME VARCHAR2(50 CHAR);
 
