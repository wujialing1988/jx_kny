-- Add/modify columns 
alter table K_GZTP add HANDLE_WAY_VALUE VARCHAR2(50 CHAR);
-- Add comments to the columns 
comment on column K_GZTP.HANDLE_WAY_VALUE
  is '处理方式值';
