drop table PJWZ_MAT_TYPE_LIST ;
create table PJWZ_MAT_TYPE_LIST
(
  MAT_CODE    VARCHAR2(50 CHAR) not null,
  MAT_DESC    VARCHAR2(100 CHAR),
  UNIT        VARCHAR2(20 CHAR),
  PRICE       NUMBER(8,2),
  MAT_DESC_EN VARCHAR2(100 CHAR)
);

-- Add comments to the table 
comment on table PJWZ_MAT_TYPE_LIST
  is '物料信息';
-- Add comments to the columns 
comment on column PJWZ_MAT_TYPE_LIST.MAT_CODE
  is '物料编码';
comment on column PJWZ_MAT_TYPE_LIST.MAT_DESC
  is '物料描述';
comment on column PJWZ_MAT_TYPE_LIST.UNIT
  is '单位';
comment on column PJWZ_MAT_TYPE_LIST.PRICE
  is '单价';
comment on column PJWZ_MAT_TYPE_LIST.MAT_DESC_EN
  is '物料描述首拼';