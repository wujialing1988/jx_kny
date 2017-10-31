-- Create table
create table K_TRAIN_DEMAND
(
  IDX                         VARCHAR2(50 CHAR) not null,
  STARTING_STATION            VARCHAR2(50 CHAR),
  LEAVE_OFF_STATION           VARCHAR2(100 CHAR),
  STRIPS                      VARCHAR2(100 CHAR),
  DURATION                    NUMBER(12,4),
  KILOMETERS                  NUMBER(18),
  ARRIVAL_TIME                VARCHAR2(50 CHAR),
  DEPARTURE_TIME              VARCHAR2(50 CHAR),
  RECORD_STATUS               NUMBER(1),
  CREATOR                     NUMBER(18) not null,
  CREATE_TIME                 DATE not null,
  UPDATOR                     NUMBER(18) not null,
  UPDATE_TIME                 DATE not null,
  SITEID                      VARCHAR2(50 CHAR),
  REMARK                      VARCHAR2(1000 CHAR),
  ROUTING_CODE                VARCHAR2(50 CHAR),
  MARSHALLING_CODE            VARCHAR2(50 CHAR),
  MARSHALLING_NAME            VARCHAR2(100 CHAR),
  TRAIN_COUNT                 NUMBER(2),
  RUNNING_DATE                DATE,
  EMPID                       NUMBER(18),
  EMPNAME                     VARCHAR2(100 CHAR),
  MARSHALLING_IDX             VARCHAR2(50 CHAR),
  ROUTING_IDX                 VARCHAR2(50 CHAR),
  TRAIN_INSPECTOR_ID          VARCHAR2(1000 CHAR),
  TRAIN_INSPECTOR_NAME        VARCHAR2(1000 CHAR),
  BACK_STRIPS                 VARCHAR2(100 CHAR),
  MARSHALLING_TRAIN_COUNT_STR VARCHAR2(1000 CHAR),
  ARRIVAL_BACK_TIME           VARCHAR2(50 CHAR),
  DEPARTURE_BACK_TIME         VARCHAR2(50 CHAR)
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
comment on table K_TRAIN_DEMAND
  is '列车需求维护';
-- Add comments to the columns 
comment on column K_TRAIN_DEMAND.STARTING_STATION
  is '出发地';
comment on column K_TRAIN_DEMAND.LEAVE_OFF_STATION
  is '折返地';
comment on column K_TRAIN_DEMAND.STRIPS
  is '列表车次';
comment on column K_TRAIN_DEMAND.DURATION
  is '所需要的时间(单位：分钟)';
comment on column K_TRAIN_DEMAND.KILOMETERS
  is '一个交路所需行程公里数';
comment on column K_TRAIN_DEMAND.ARRIVAL_TIME
  is '从出发地出发的时间';
comment on column K_TRAIN_DEMAND.DEPARTURE_TIME
  is '交路完成后的到达时间';
comment on column K_TRAIN_DEMAND.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_TRAIN_DEMAND.CREATOR
  is '创建人';
comment on column K_TRAIN_DEMAND.CREATE_TIME
  is '创建时间';
comment on column K_TRAIN_DEMAND.UPDATOR
  is '修改人';
comment on column K_TRAIN_DEMAND.UPDATE_TIME
  is '修改时间';
comment on column K_TRAIN_DEMAND.SITEID
  is '站点标识，为了同步数据而使用';
comment on column K_TRAIN_DEMAND.ROUTING_CODE
  is '固定编号方式';
comment on column K_TRAIN_DEMAND.MARSHALLING_CODE
  is '固定编号方式';
comment on column K_TRAIN_DEMAND.MARSHALLING_NAME
  is '编组名称';
comment on column K_TRAIN_DEMAND.RUNNING_DATE
  is '运行日期';
comment on column K_TRAIN_DEMAND.EMPID
  is '需求登记人ID';
comment on column K_TRAIN_DEMAND.EMPNAME
  is '需求登记人名称';
comment on column K_TRAIN_DEMAND.MARSHALLING_IDX
  is '编组idX';
comment on column K_TRAIN_DEMAND.ROUTING_IDX
  is '交路idx';
comment on column K_TRAIN_DEMAND.TRAIN_INSPECTOR_ID
  is '乘务检测 员ID（多个人员ID以;号分隔）';
comment on column K_TRAIN_DEMAND.TRAIN_INSPECTOR_NAME
  is '乘务检测 员姓名（多个人员ID以;号分隔）';
comment on column K_TRAIN_DEMAND.BACK_STRIPS
  is '返程列车车次';
comment on column K_TRAIN_DEMAND.MARSHALLING_TRAIN_COUNT_STR
  is '车辆数量显示';
comment on column K_TRAIN_DEMAND.ARRIVAL_BACK_TIME
  is '返程出发的时间';
comment on column K_TRAIN_DEMAND.DEPARTURE_BACK_TIME
  is '返程到达时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_TRAIN_DEMAND
  add constraint PK_K_TRAIN_DEMAND primary key (IDX)
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
