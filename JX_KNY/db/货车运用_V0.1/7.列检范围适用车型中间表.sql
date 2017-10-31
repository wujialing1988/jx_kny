create table ZB_FW_VEHICLE_RELATION
(
  IDX              	VARCHAR2(50 CHAR) not null,
  ZBFW_IDX   		VARCHAR2(50 CHAR),
  Train_Vehicle_Code  VARCHAR2(50 CHAR),
  Train_Vehicle_Name VARCHAR2(50 CHAR)
);

comment on table ZB_FW_VEHICLE_RELATION
  is '列检范围适用车型';
-- Add comments to the columns 
comment on column ZB_FW_VEHICLE_RELATION.IDX
  is 'idx主键';
comment on column ZB_FW_VEHICLE_RELATION.ZBFW_IDX
  is '列检范围idx';
comment on column ZB_FW_VEHICLE_RELATION.Train_Vehicle_Code
  is '适用车型编码';
comment on column ZB_FW_VEHICLE_RELATION.Train_Vehicle_Name
  is '适用车型名称';
  
 alter table ZB_FW_VEHICLE_RELATION
  add constraint PK_ZB_FW_VEHICLE_RELATION primary key (IDX);