-- Add/modify columns 
alter table WLGL_WH_MAT_QUOTA add WORKPLACE_CODE VARCHAR2(50 CHAR);
alter table WLGL_WH_MAT_QUOTA add WORKPLACE_NAME VARCHAR2(100 CHAR);
alter table WLGL_WH_MAT_QUOTA add CURRENT_QTY NUMBER;
-- Add comments to the columns 
comment on column WLGL_WH_MAT_QUOTA.WORKPLACE_CODE
  is '工作地点标识码';
comment on column WLGL_WH_MAT_QUOTA.WORKPLACE_NAME
  is '工作地点名称';
comment on column WLGL_WH_MAT_QUOTA.CURRENT_QTY
  is '最大量';
