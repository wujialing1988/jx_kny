-- Create table
create table K_MARSHALLING_TRAIN
(
  IDX                  VARCHAR2(50 CHAR) not null,
  MARSHALLING_CODE     VARCHAR2(50 CHAR),
  T_VEHICLE_KIND_CODE  VARCHAR2(50 CHAR),
  T_VEHICLE_KIND_NAME  VARCHAR2(100 CHAR),
  TRAIN_NO             VARCHAR2(50 CHAR),
  RECORD_STATUS        NUMBER(1),
  CREATOR              NUMBER(18) not null,
  CREATE_TIME          DATE not null,
  UPDATOR              NUMBER(18) not null,
  UPDATE_TIME          DATE not null,
  SITEID               VARCHAR2(50 CHAR),
  TRAIN_TYPE_IDX       VARCHAR2(32 CHAR),
  TRAIN_TYPE_SHORTNAME VARCHAR2(8 CHAR),
  SEQ_NO               NUMBER(18)
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
comment on table K_MARSHALLING_TRAIN
  is '客车编组车辆记录表';
-- Add comments to the columns 
comment on column K_MARSHALLING_TRAIN.MARSHALLING_CODE
  is '固定编号方式';
comment on column K_MARSHALLING_TRAIN.T_VEHICLE_KIND_CODE
  is '车种类型编码';
comment on column K_MARSHALLING_TRAIN.T_VEHICLE_KIND_NAME
  is '车种类型名称';
comment on column K_MARSHALLING_TRAIN.TRAIN_NO
  is '车辆号';
comment on column K_MARSHALLING_TRAIN.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_MARSHALLING_TRAIN.CREATOR
  is '创建人';
comment on column K_MARSHALLING_TRAIN.CREATE_TIME
  is '创建时间';
comment on column K_MARSHALLING_TRAIN.UPDATOR
  is '修改人';
comment on column K_MARSHALLING_TRAIN.UPDATE_TIME
  is '修改时间';
comment on column K_MARSHALLING_TRAIN.SITEID
  is '站点标识，为了同步数据而使用';
comment on column K_MARSHALLING_TRAIN.TRAIN_TYPE_IDX
  is '车型主键';
comment on column K_MARSHALLING_TRAIN.TRAIN_TYPE_SHORTNAME
  is '车型英文简称';
comment on column K_MARSHALLING_TRAIN.SEQ_NO
  is '顺序号';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_MARSHALLING_TRAIN
  add constraint PK_K_MARSHALLING_TRAIN primary key (IDX)
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
