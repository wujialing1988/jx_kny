-- Create table
create table K_QC_ITEM_DEFINE
(
  IDX           VARCHAR2(50 CHAR) not null,
  QC_ITEM_NO    VARCHAR2(50 CHAR),
  QC_ITEM_NAME  VARCHAR2(50 CHAR),
  SEQ_NO        NUMBER(3),
  SITEID        VARCHAR2(50 CHAR),
  BUSINESS_CODE VARCHAR2(50 CHAR),
  IS_DEFAULT    NUMBER(1)
)
tablespace JX2_COREFRAME
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
comment on table K_QC_ITEM_DEFINE
  is '检查项基础配置';
-- Add comments to the columns 
comment on column K_QC_ITEM_DEFINE.IDX
  is '主键';
comment on column K_QC_ITEM_DEFINE.QC_ITEM_NO
  is '检查项编码';
comment on column K_QC_ITEM_DEFINE.QC_ITEM_NAME
  is '检查项名称';
comment on column K_QC_ITEM_DEFINE.SEQ_NO
  is '顺序号';
comment on column K_QC_ITEM_DEFINE.SITEID
  is '站点标识，为了同步数据而使用';
comment on column K_QC_ITEM_DEFINE.BUSINESS_CODE
  is '业务编码(pda菜单名称)';
comment on column K_QC_ITEM_DEFINE.IS_DEFAULT
  is '是否默认（0=默认 1=非默认）';
  
-- Create table
create table K_QC_ITEM_EMEP_DEFINE
(
  IDX           VARCHAR2(50 CHAR) not null,
  QC_ITEM_IDX   VARCHAR2(50 CHAR),
  CHECK_EMPID   NUMBER(10),
  CHECK_EMPNAME VARCHAR2(25 CHAR),
  SITEID        VARCHAR2(50 CHAR)
)
tablespace JX2_COREFRAME
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
comment on table K_QC_ITEM_EMEP_DEFINE
  is '检查人员基础配置';
-- Add comments to the columns 
comment on column K_QC_ITEM_EMEP_DEFINE.IDX
  is '主键';
comment on column K_QC_ITEM_EMEP_DEFINE.QC_ITEM_IDX
  is '质量检查项主键';
comment on column K_QC_ITEM_EMEP_DEFINE.CHECK_EMPID
  is '检查人员';
comment on column K_QC_ITEM_EMEP_DEFINE.CHECK_EMPNAME
  is '检查人员名称';
comment on column K_QC_ITEM_EMEP_DEFINE.SITEID
  is '站点标识，为了同步数据而使用';

-- Create table
create table K_QUALITY_CONTROL
(
  IDX                  VARCHAR2(50 CHAR) not null,
  BUSINESS_CODE        VARCHAR2(50 CHAR),
  BUSINESS_IDX         VARCHAR2(50 CHAR),
  QC_ITEM_NO           VARCHAR2(50 CHAR),
  QC_ITEM_NAME         VARCHAR2(50 CHAR),
  CHECK_EMPID          NUMBER(10),
  CHECK_EMPNAME        VARCHAR2(25 CHAR),
  OTHER_WOKER          NUMBER(10),
  SITEID               VARCHAR2(50 CHAR),
  CREATOR              NUMBER(18) not null,
  CREATE_TIME          DATE not null,
  REMARK               VARCHAR2(2500 CHAR),
  STATUS               VARCHAR2(20 CHAR),
  TRAIN_TYPE_SHORTNAME VARCHAR2(8 CHAR),
  TRAIN_NO             VARCHAR2(50 CHAR),
  IN_TIME              DATE,
  FUNCTION_NAME        VARCHAR2(50 CHAR),
  BUSINESS_DESC        VARCHAR2(50 CHAR)
)
tablespace JX2_COREFRAME
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
comment on table K_QUALITY_CONTROL
  is '质量检查';
-- Add comments to the columns 
comment on column K_QUALITY_CONTROL.IDX
  is '主键';
comment on column K_QUALITY_CONTROL.BUSINESS_CODE
  is '业务编码';
comment on column K_QUALITY_CONTROL.BUSINESS_IDX
  is 'jt6主键';
comment on column K_QUALITY_CONTROL.QC_ITEM_NO
  is '检查项编码';
comment on column K_QUALITY_CONTROL.QC_ITEM_NAME
  is '检查项名称';
comment on column K_QUALITY_CONTROL.CHECK_EMPID
  is '检查人员';
comment on column K_QUALITY_CONTROL.CHECK_EMPNAME
  is '检查人员名称';
comment on column K_QUALITY_CONTROL.OTHER_WOKER
  is '其他作业人员';
comment on column K_QUALITY_CONTROL.SITEID
  is '站点标识，为了同步数据而使用';
comment on column K_QUALITY_CONTROL.CREATOR
  is '创建人';
comment on column K_QUALITY_CONTROL.CREATE_TIME
  is '创建时间';
comment on column K_QUALITY_CONTROL.REMARK
  is '备注';
comment on column K_QUALITY_CONTROL.STATUS
  is '状态(UNCHECKED == 未检查 qualified == 合格 unqualified == 不合格)';
comment on column K_QUALITY_CONTROL.TRAIN_TYPE_SHORTNAME
  is '车型简称';
comment on column K_QUALITY_CONTROL.TRAIN_NO
  is '车号';
comment on column K_QUALITY_CONTROL.IN_TIME
  is '入段时间';
comment on column K_QUALITY_CONTROL.FUNCTION_NAME
  is '功能名称';
comment on column K_QUALITY_CONTROL.BUSINESS_DESC
  is '业务项描述';

