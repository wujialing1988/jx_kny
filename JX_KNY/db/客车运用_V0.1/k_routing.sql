-- Create table
create table K_ROUTING
(
  IDX                 VARCHAR2(50 CHAR) not null,
  ROUTING_CODE        VARCHAR2(50 CHAR),
  STARTING_STATION    VARCHAR2(50 CHAR),
  LEAVE_OFF_STATION   VARCHAR2(100 CHAR),
  STRIPS              VARCHAR2(100 CHAR),
  DURATION            NUMBER(12,4),
  KILOMETERS          NUMBER(18),
  ARRIVAL_TIME        VARCHAR2(50 CHAR),
  DEPARTURE_TIME      VARCHAR2(50 CHAR),
  RECORD_STATUS       NUMBER(1),
  CREATOR             NUMBER(18) not null,
  CREATE_TIME         DATE not null,
  UPDATOR             NUMBER(18) not null,
  UPDATE_TIME         DATE not null,
  SITEID              VARCHAR2(50 CHAR),
  REMARK              VARCHAR2(1000 CHAR),
  ARRIVAL_BACK_TIME   VARCHAR2(50 CHAR),
  DEPARTURE_BACK_TIME VARCHAR2(50 CHAR)
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
comment on table K_ROUTING
  is '交路基本信息表';
-- Add comments to the columns 
comment on column K_ROUTING.ROUTING_CODE
  is '固定编号方式';
comment on column K_ROUTING.STARTING_STATION
  is '出发地';
comment on column K_ROUTING.LEAVE_OFF_STATION
  is '折返地';
comment on column K_ROUTING.STRIPS
  is '列表车次';
comment on column K_ROUTING.DURATION
  is '所需要的时间(单位：分钟)';
comment on column K_ROUTING.KILOMETERS
  is '行程公里数';
comment on column K_ROUTING.ARRIVAL_TIME
  is '出发的时间';
comment on column K_ROUTING.DEPARTURE_TIME
  is '到达时间';
comment on column K_ROUTING.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_ROUTING.CREATOR
  is '创建人';
comment on column K_ROUTING.CREATE_TIME
  is '创建时间';
comment on column K_ROUTING.UPDATOR
  is '修改人';
comment on column K_ROUTING.UPDATE_TIME
  is '修改时间';
comment on column K_ROUTING.SITEID
  is '站点标识，为了同步数据而使用';
comment on column K_ROUTING.REMARK
  is '备注';
comment on column K_ROUTING.ARRIVAL_BACK_TIME
  is '返程出发的时间';
comment on column K_ROUTING.DEPARTURE_BACK_TIME
  is '返程到达时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_ROUTING
  add constraint PK_K_ROUTING primary key (IDX)
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
