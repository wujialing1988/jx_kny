-- Create table
create table K_TRAIN_INSPECTOR
(
  IDX           VARCHAR2(50 CHAR) not null,
  EMPID         NUMBER(18),
  EMPNAME       VARCHAR2(100 CHAR),
  EMPCODE       VARCHAR2(30),
  ORGID         NUMBER(18),
  CREATOR       NUMBER(18) not null,
  CREATE_TIME   DATE not null,
  UPDATOR       NUMBER(18) not null,
  UPDATE_TIME   DATE not null,
  SITEID        VARCHAR2(50 CHAR),
  PLANT         VARCHAR2(50),
  ORGNAME       VARCHAR2(100),
  RECORD_STATUS NUMBER(1),
  GENDER        VARCHAR2(255),
  EMPSTATUS     VARCHAR2(255)
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
comment on table K_TRAIN_INSPECTOR
  is '乘务检测人员维护';
-- Add comments to the columns 
comment on column K_TRAIN_INSPECTOR.EMPID
  is 'ID';
comment on column K_TRAIN_INSPECTOR.EMPNAME
  is '人员名称';
comment on column K_TRAIN_INSPECTOR.EMPCODE
  is '人员代码';
comment on column K_TRAIN_INSPECTOR.ORGID
  is '班组id';
comment on column K_TRAIN_INSPECTOR.CREATOR
  is '创建人';
comment on column K_TRAIN_INSPECTOR.CREATE_TIME
  is '创建时间';
comment on column K_TRAIN_INSPECTOR.UPDATOR
  is '修改人';
comment on column K_TRAIN_INSPECTOR.UPDATE_TIME
  is '修改时间';
comment on column K_TRAIN_INSPECTOR.SITEID
  is '站点标识，为了同步数据而使用';
comment on column K_TRAIN_INSPECTOR.PLANT
  is '所属车间';
comment on column K_TRAIN_INSPECTOR.ORGNAME
  is '所属班组';
comment on column K_TRAIN_INSPECTOR.GENDER
  is '性别';
comment on column K_TRAIN_INSPECTOR.EMPSTATUS
  is '人员状态';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_TRAIN_INSPECTOR
  add constraint PK_K_TRAIN_INSPECTOR primary key (IDX)
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
