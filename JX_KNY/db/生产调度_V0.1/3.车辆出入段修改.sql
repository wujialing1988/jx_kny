-- Add/modify columns 
alter table TWT_TRAIN_ACCESS_ACCOUNT modify TRAIN_TYPE_IDX VARCHAR2(50 CHAR);
alter table TWT_TRAIN_ACCESS_ACCOUNT modify TRAIN_TYPE_SHORTNAME VARCHAR2(50 CHAR);
alter table TWT_TRAIN_ACCESS_ACCOUNT modify TRAIN_NO VARCHAR2(50 CHAR);
alter table TWT_TRAIN_ACCESS_ACCOUNT add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column TWT_TRAIN_ACCESS_ACCOUNT.T_VEHICLE_TYPE
  is '客货类型 10 货车 20 客车';
