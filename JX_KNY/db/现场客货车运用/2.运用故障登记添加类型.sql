-- Add/modify columns 
alter table K_GZTP add TYPE VARCHAR2(10 CHAR);
-- Add comments to the columns 
comment on column K_GZTP.TYPE
  is '提票类型（数据字典项：如JT6，JT28等）';
