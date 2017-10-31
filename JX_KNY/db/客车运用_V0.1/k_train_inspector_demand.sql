-- Create table
create table K_TRAIN_INSPECTOR_DEMAND
(
  IDX              VARCHAR2(50 CHAR) not null,
  BACK_STRIPS      VARCHAR2(100 CHAR),
  STRIPS           VARCHAR2(100 CHAR),
  DURATION         VARCHAR2(100 CHAR),
  TRAIN_DEMAND_IDX VARCHAR2(50 CHAR),
  RUNNING_DATE     DATE,
  BACK_DATE        DATE,
  RECORD_STATUS    NUMBER(1),
  CREATOR          NUMBER(18) not null,
  CREATE_TIME      DATE not null,
  UPDATOR          NUMBER(18) not null,
  UPDATE_TIME      DATE not null,
  SITEID           VARCHAR2(50 CHAR),
  EMPID            NUMBER(18),
  EMPNAME          VARCHAR2(100 CHAR)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table K_TRAIN_INSPECTOR_DEMAND
  is '乘务检测人员排班';
-- Add comments to the columns 
comment on column K_TRAIN_INSPECTOR_DEMAND.BACK_STRIPS
  is '返回车次';
comment on column K_TRAIN_INSPECTOR_DEMAND.STRIPS
  is '列车车次';
comment on column K_TRAIN_INSPECTOR_DEMAND.DURATION
  is '单程运行时间';
comment on column K_TRAIN_INSPECTOR_DEMAND.TRAIN_DEMAND_IDX
  is '列车需求IDX';
comment on column K_TRAIN_INSPECTOR_DEMAND.RUNNING_DATE
  is '运行日期';
comment on column K_TRAIN_INSPECTOR_DEMAND.BACK_DATE
  is '从出发地返回的时间';
comment on column K_TRAIN_INSPECTOR_DEMAND.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_TRAIN_INSPECTOR_DEMAND.CREATOR
  is '创建人';
comment on column K_TRAIN_INSPECTOR_DEMAND.CREATE_TIME
  is '创建时间';
comment on column K_TRAIN_INSPECTOR_DEMAND.UPDATOR
  is '修改人';
comment on column K_TRAIN_INSPECTOR_DEMAND.UPDATE_TIME
  is '修改时间';
comment on column K_TRAIN_INSPECTOR_DEMAND.SITEID
  is '站点标识，为了同步数据而使用';
comment on column K_TRAIN_INSPECTOR_DEMAND.EMPID
  is 'ID';
comment on column K_TRAIN_INSPECTOR_DEMAND.EMPNAME
  is '人员名称';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_TRAIN_INSPECTOR_DEMAND
  add constraint PK_K_TRAIN_INSPECTOR_DEMAND primary key (IDX)
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
