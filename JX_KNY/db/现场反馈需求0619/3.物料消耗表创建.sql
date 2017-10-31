create table PJWZ_MAT_TYPE_USE
(
  IDX         VARCHAR2(50 CHAR) not null,
  MAT_CODE    VARCHAR2(50 CHAR),
  MAT_DESC    VARCHAR2(100 CHAR),
  UNIT        VARCHAR2(20 CHAR),
  PRICE       NUMBER(8,2),
  MAT_DESC_EN VARCHAR2(100 CHAR),
  GZTP_IDX    VARCHAR2(50 CHAR),
  MAT_COUNT   NUMBER(5)
);

-- Add comments to the table 
comment on table PJWZ_MAT_TYPE_USE
  is '物料信息';
-- Add comments to the columns 
comment on column PJWZ_MAT_TYPE_USE.IDX
  is '主键';
comment on column PJWZ_MAT_TYPE_USE.MAT_CODE
  is '物料编码';
comment on column PJWZ_MAT_TYPE_USE.MAT_DESC
  is '物料描述';
comment on column PJWZ_MAT_TYPE_USE.UNIT
  is '单位';
comment on column PJWZ_MAT_TYPE_USE.PRICE
  is '单价';
comment on column PJWZ_MAT_TYPE_USE.MAT_DESC_EN
  is '中文名称拼音';
comment on column PJWZ_MAT_TYPE_USE.GZTP_IDX
  is '故障登记主键';
comment on column PJWZ_MAT_TYPE_USE.MAT_COUNT
  is '消耗数量';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PJWZ_MAT_TYPE_USE
  add constraint PK_PJWZ_MAT_TYPE_USE primary key (IDX);