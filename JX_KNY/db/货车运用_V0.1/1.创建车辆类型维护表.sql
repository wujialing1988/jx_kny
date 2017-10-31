-- Create table
create table J_JCGY_VEHICLE_TYPE
(
  IDX            VARCHAR2(50 CHAR) not null,
  T_TYPE_CODE    VARCHAR2(50),
  T_TYPE_NAME    VARCHAR2(50),
  T_SHORT_NAME   VARCHAR2(20),
  T_DESCRIPTION  VARCHAR2(200),
  T_VEHICLE_TYPE VARCHAR2(20),
  RECORD_STATUS  NUMBER(1),
  CREATOR        NUMBER(18) not null,
  CREATE_TIME    DATE not null,
  UPDATOR        NUMBER(18) not null,
  UPDATE_TIME    DATE not null
);

-- Add comments to the table 
comment on table J_JCGY_VEHICLE_TYPE
  is '货车客车车型维护';
-- Add comments to the columns 
comment on column J_JCGY_VEHICLE_TYPE.IDX
  is '主键';
comment on column J_JCGY_VEHICLE_TYPE.T_TYPE_CODE
  is '车型代码';
comment on column J_JCGY_VEHICLE_TYPE.T_TYPE_NAME
  is '车型名称';
comment on column J_JCGY_VEHICLE_TYPE.T_SHORT_NAME
  is '简称';
comment on column J_JCGY_VEHICLE_TYPE.T_DESCRIPTION
  is '描述';
comment on column J_JCGY_VEHICLE_TYPE.T_VEHICLE_TYPE
  is '客货类型';
comment on column J_JCGY_VEHICLE_TYPE.RECORD_STATUS
  is '表示此条记录的状态：0为表示未删除；1表示删除';
comment on column J_JCGY_VEHICLE_TYPE.CREATOR
  is '创建人';
comment on column J_JCGY_VEHICLE_TYPE.CREATE_TIME
  is '创建时间';
comment on column J_JCGY_VEHICLE_TYPE.UPDATOR
  is '修改人';
comment on column J_JCGY_VEHICLE_TYPE.UPDATE_TIME
  is '修改时间';