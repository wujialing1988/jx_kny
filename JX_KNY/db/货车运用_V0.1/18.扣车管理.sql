create table K_DETAIN_TRAIN
(
  IDX              VARCHAR2(50 CHAR) not null,
  TRAIN_TYPE_IDX   VARCHAR2(50 CHAR),
  TRAIN_TYPE_CODE  VARCHAR2(50 CHAR),
  TRAIN_TYPE_NAME  VARCHAR2(50 CHAR),
  TRAIN_NO         VARCHAR2(50 CHAR),
  DETAIN_STATUS    VARCHAR2(50 CHAR),
  PROPOSER_IDX     NUMBER(10),
  PROPOSER_NAME    VARCHAR2(50),
  PROPOSER_DATE    DATE,
  APPROVE_IDX      NUMBER(10),
  APPROVE_NAME     VARCHAR2(50),
  APPROVE_DATE     DATE,
  DETAIN_TYPE_CODE VARCHAR2(50),
  DETAIN_TYPE_NAME VARCHAR2(50),
  DETAIN_REASON    VARCHAR2(500),
  APPROVE_OPINION  VARCHAR2(500),
  ORDER_USER       VARCHAR2(50),
  ORDER_DATE       DATE,
  ORDER_NO         VARCHAR2(100),
  SITE_ID          VARCHAR2(50),
  SITE_NAME        VARCHAR2(50),
  RECORD_STATUS    NUMBER(1),
  CREATOR          NUMBER(18) not null,
  CREATE_TIME      DATE not null,
  UPDATOR          NUMBER(18) not null,
  UPDATE_TIME      DATE not null
);

-- Add comments to the table 
comment on table K_DETAIN_TRAIN
  is '扣车管理';
-- Add comments to the columns 
comment on column K_DETAIN_TRAIN.IDX
  is '主键';
comment on column K_DETAIN_TRAIN.TRAIN_TYPE_IDX
  is '车辆车型ID';
comment on column K_DETAIN_TRAIN.TRAIN_TYPE_CODE
  is '车辆车型编码';
comment on column K_DETAIN_TRAIN.TRAIN_TYPE_NAME
  is '车辆车型名称';
comment on column K_DETAIN_TRAIN.TRAIN_NO
  is '车辆车号';
comment on column K_DETAIN_TRAIN.DETAIN_STATUS
  is '扣车状态';
comment on column K_DETAIN_TRAIN.PROPOSER_IDX
  is '申请人ID';
comment on column K_DETAIN_TRAIN.PROPOSER_NAME
  is '申请人';
comment on column K_DETAIN_TRAIN.PROPOSER_DATE
  is '申请时间';
comment on column K_DETAIN_TRAIN.APPROVE_IDX
  is '审批人ID';
comment on column K_DETAIN_TRAIN.APPROVE_NAME
  is '审批人';
comment on column K_DETAIN_TRAIN.APPROVE_DATE
  is '审批时间';
comment on column K_DETAIN_TRAIN.DETAIN_TYPE_CODE
  is '扣车类型编码';
comment on column K_DETAIN_TRAIN.DETAIN_TYPE_NAME
  is '扣车类型名称';
comment on column K_DETAIN_TRAIN.DETAIN_REASON
  is '申请原因';
comment on column K_DETAIN_TRAIN.APPROVE_OPINION
  is '审批意见';
comment on column K_DETAIN_TRAIN.ORDER_USER
  is '命令发布者';
comment on column K_DETAIN_TRAIN.ORDER_DATE
  is '命令发布时间';
comment on column K_DETAIN_TRAIN.ORDER_NO
  is '命令号';
comment on column K_DETAIN_TRAIN.SITE_ID
  is '站点ID';
comment on column K_DETAIN_TRAIN.SITE_NAME
  is '站点名称';
comment on column K_DETAIN_TRAIN.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_DETAIN_TRAIN.CREATOR
  is '创建人';
comment on column K_DETAIN_TRAIN.CREATE_TIME
  is '创建时间';
comment on column K_DETAIN_TRAIN.UPDATOR
  is '修改人';
comment on column K_DETAIN_TRAIN.UPDATE_TIME
  is '修改时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_DETAIN_TRAIN
  add constraint PK_K_DETAIN_TRAIN primary key (IDX);