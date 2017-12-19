-- Create table
create table SCDD_TRAIN_ENFORCE_PLAN_HC
(
  IDX              VARCHAR2(50 CHAR) not null,
  RECORD_STATUS    NUMBER(1),
  SITEID           VARCHAR2(50 CHAR),
  CREATOR          NUMBER(18) not null,
  CREATE_TIME      DATE not null,
  UPDATOR          NUMBER(18) not null,
  UPDATE_TIME      DATE not null,
  PLAN_YEAR        VARCHAR2(10 CHAR),
  PLAN_MORTH       VARCHAR2(10 CHAR),
  PLAN_PERSON      NUMBER(18),
  PLAN_PERSON_NAME VARCHAR2(50 CHAR),
  PLAN_TIME        DATE
);

-- Add comments to the table 
comment on table SCDD_TRAIN_ENFORCE_PLAN_HC
  is '货车检修月计划实体';
-- Add comments to the columns 
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.IDX
  is '主键';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.RECORD_STATUS
  is '表示此条记录的状态：0为表示未删除；1表示删除';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.SITEID
  is '站点标识';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.CREATOR
  is '创建人';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.CREATE_TIME
  is '创建时间';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.UPDATOR
  is '修改人';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.UPDATE_TIME
  is '修改时间 ';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.PLAN_YEAR
  is '计划年份';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.PLAN_MORTH
  is '计划月份';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.PLAN_PERSON
  is '编制人';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.PLAN_PERSON_NAME
  is '编制人名称';
comment on column SCDD_TRAIN_ENFORCE_PLAN_HC.PLAN_TIME
  is '编制日期';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SCDD_TRAIN_ENFORCE_PLAN_HC
  add constraint PK_SCDD_TRAIN_ENFORCE_PLAN_HC primary key (IDX);
  
  
 -- Create table
create table SCDD_TRAIN_ENFORCE_DETAIL_HC
(
  IDX                  VARCHAR2(50 CHAR) not null,
  RECORD_STATUS        NUMBER(1),
  SITEID               VARCHAR2(50 CHAR),
  CREATOR              NUMBER(18) not null,
  CREATE_TIME          DATE not null,
  UPDATOR              NUMBER(18) not null,
  UPDATE_TIME          DATE not null,
  PLAN_IDX             VARCHAR2(50 CHAR),
  TRAIN_TYPE_IDX       VARCHAR2(50 CHAR),
  TRAIN_TYPE_SHORTNAME VARCHAR2(50 CHAR),
  PLAN_COUNT           NUMBER(18)
);

-- Add comments to the table 
comment on table SCDD_TRAIN_ENFORCE_DETAIL_HC
  is '货车检修月计划详情';
-- Add comments to the columns 
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.IDX
  is '主键';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.RECORD_STATUS
  is '表示此条记录的状态：0为表示未删除；1表示删除';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.SITEID
  is '站点标识';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.CREATOR
  is '创建人';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.CREATE_TIME
  is '创建时间';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.UPDATOR
  is '修改人';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.UPDATE_TIME
  is '修改时间';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.PLAN_IDX
  is '计划id';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.TRAIN_TYPE_IDX
  is '车型id';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.TRAIN_TYPE_SHORTNAME
  is '车型简称';
comment on column SCDD_TRAIN_ENFORCE_DETAIL_HC.PLAN_COUNT
  is '计划修车数量';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SCDD_TRAIN_ENFORCE_DETAIL_HC
  add constraint PK_TRAIN_ENFORCE_DETAIL_HC primary key (IDX);