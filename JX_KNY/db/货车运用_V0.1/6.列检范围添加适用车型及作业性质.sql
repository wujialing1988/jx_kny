-- Add/modify columns 
alter table ZB_ZBFW add Train_Vehicle_Name VARCHAR2(2000 CHAR);
alter table ZB_ZBFW add Train_Vehicle_Code VARCHAR2(2000 CHAR);
alter table ZB_ZBFW add WORK_NATURE_CODE VARCHAR2(2000 CHAR);
alter table ZB_ZBFW add WORK_NATURE VARCHAR2(2000 CHAR);
-- Add comments to the columns 
comment on column ZB_ZBFW.Train_Vehicle_Name
  is '适用车型名称';
comment on column ZB_ZBFW.Train_Vehicle_Code
  is '适用车型编码';
comment on column ZB_ZBFW.WORK_NATURE_CODE
  is '适用作业性质编码';
comment on column ZB_ZBFW.WORK_NATURE
  is '适用作业性质';
  
  
-- Add/modify columns 
alter table ZB_ZBFW add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column ZB_ZBFW.T_VEHICLE_TYPE
  is '客货类型';

