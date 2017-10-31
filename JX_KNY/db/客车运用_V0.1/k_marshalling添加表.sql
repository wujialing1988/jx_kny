-- Create table
create table K_MARSHALLING
(
  IDX              VARCHAR2(50 CHAR) not null,
  MARSHALLING_CODE VARCHAR2(50 CHAR),
  MARSHALLING_NAME VARCHAR2(100 CHAR),
  REMARK           VARCHAR2(1000 CHAR),
  RECORD_STATUS    NUMBER(1),
  CREATOR          NUMBER(18) not null,
  CREATE_TIME      DATE not null,
  UPDATOR          NUMBER(18) not null,
  UPDATE_TIME      DATE not null,
  SITEID           VARCHAR2(50 CHAR),
  TRAIN_COUNT      NUMBER(2)
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
comment on table K_MARSHALLING
  is '编组基本信息';
-- Add comments to the columns 
comment on column K_MARSHALLING.MARSHALLING_CODE
  is '固定编号方式';
comment on column K_MARSHALLING.MARSHALLING_NAME
  is '编组名称';
comment on column K_MARSHALLING.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_MARSHALLING.CREATOR
  is '创建人';
comment on column K_MARSHALLING.CREATE_TIME
  is '创建时间';
comment on column K_MARSHALLING.UPDATOR
  is '修改人';
comment on column K_MARSHALLING.UPDATE_TIME
  is '修改时间';
comment on column K_MARSHALLING.SITEID
  is '站点标识，为了同步数据而使用';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_MARSHALLING
  add constraint PK_K_MARSHALLING primary key (IDX)
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
