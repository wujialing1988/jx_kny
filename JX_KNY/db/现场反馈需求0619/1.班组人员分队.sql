create table K_CLASS_ORGANIZATION_USER
(
  IDX              VARCHAR2(50) not null,
  WORK_PERSON_IDX  VARCHAR2(50),
  WORK_PERSON_NAME VARCHAR2(50),
  CLASS_ORG_IDX    VARCHAR2(50),
  ORG_IDX          VARCHAR2(50),
  QUEUE_CODE       VARCHAR2(50),
  QUEUE_NAME       VARCHAR2(50)
);

-- Add comments to the table 
comment on table K_CLASS_ORGANIZATION_USER
  is '分队实体';
-- Add comments to the columns 
comment on column K_CLASS_ORGANIZATION_USER.IDX
  is '主键';
comment on column K_CLASS_ORGANIZATION_USER.WORK_PERSON_IDX
  is '作业人员ID';
comment on column K_CLASS_ORGANIZATION_USER.WORK_PERSON_NAME
  is '作业人员名称';
comment on column K_CLASS_ORGANIZATION_USER.CLASS_ORG_IDX
  is '班组班次对应ID';
comment on column K_CLASS_ORGANIZATION_USER.ORG_IDX
  is '班组D';
comment on column K_CLASS_ORGANIZATION_USER.QUEUE_CODE
  is '分队编码';
comment on column K_CLASS_ORGANIZATION_USER.QUEUE_NAME
  is '分队名称';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_CLASS_ORGANIZATION_USER
  add constraint PK_CLASS_ORGANIZATION_USER primary key (IDX);