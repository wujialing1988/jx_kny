-- Create table
create table K_OPERATION_SAFETY_RECORD
(
  IDX              VARCHAR2(50 CHAR) not null,
  TRAIN_DEMAND_IDX VARCHAR2(50 CHAR),
  RUNNING_DATE     DATE,
  STRIPS           VARCHAR2(100 CHAR),
  REPORT_EMP_ID    VARCHAR2(1000 CHAR),
  REPORT_EMP_NAME  VARCHAR2(1000 CHAR),
  EMP_NAME         VARCHAR2(100 CHAR),
  EMP_ID           NUMBER(18),
  CONTENT          VARCHAR2(1000 CHAR),
  RECORD_STATUS    NUMBER(1),
  CREATOR          NUMBER(18) not null,
  CREATE_TIME      DATE not null,
  UPDATOR          NUMBER(18) not null,
  UPDATE_TIME      DATE not null,
  SITEID           VARCHAR2(50 CHAR)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table K_OPERATION_SAFETY_RECORD
  is '运行安全记录';
-- Add comments to the columns 
comment on column K_OPERATION_SAFETY_RECORD.TRAIN_DEMAND_IDX
  is '列表需求计划idx';
comment on column K_OPERATION_SAFETY_RECORD.RUNNING_DATE
  is '运行开始日期';
comment on column K_OPERATION_SAFETY_RECORD.STRIPS
  is '列表车次';
comment on column K_OPERATION_SAFETY_RECORD.REPORT_EMP_ID
  is '反馈人ID';
comment on column K_OPERATION_SAFETY_RECORD.REPORT_EMP_NAME
  is '反馈人名称';
comment on column K_OPERATION_SAFETY_RECORD.EMP_NAME
  is '填报人名称';
comment on column K_OPERATION_SAFETY_RECORD.EMP_ID
  is '填报人ID';
comment on column K_OPERATION_SAFETY_RECORD.CONTENT
  is '填报内容';
comment on column K_OPERATION_SAFETY_RECORD.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_OPERATION_SAFETY_RECORD.CREATOR
  is '创建人';
comment on column K_OPERATION_SAFETY_RECORD.CREATE_TIME
  is '创建时间';
comment on column K_OPERATION_SAFETY_RECORD.UPDATOR
  is '修改人';
comment on column K_OPERATION_SAFETY_RECORD.UPDATE_TIME
  is '修改时间';
comment on column K_OPERATION_SAFETY_RECORD.SITEID
  is '站点标识，为了同步数据而使用';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_OPERATION_SAFETY_RECORD
  add constraint PK_K_OPERATION_SAFETY_RECORD primary key (IDX)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
