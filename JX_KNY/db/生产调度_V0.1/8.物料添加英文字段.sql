-- Add/modify columns 
alter table PJWZ_MAT_TYPE_LIST add MAT_DESC_EN VARCHAR2(100 CHAR);
-- Add comments to the columns 
comment on column PJWZ_MAT_TYPE_LIST.MAT_DESC_EN
  is '物料描述首拼';
