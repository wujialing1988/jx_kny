-- Create table
create table K_DETAIN_GZTP
(
  IDX        VARCHAR2(50 CHAR) not null,
  DETAIN_IDX VARCHAR2(50 CHAR),
  GZTP_NAME  VARCHAR2(100 CHAR),
  GZTP_DESC  VARCHAR2(200 CHAR)
);

-- Add comments to the table 
comment on table K_DETAIN_GZTP
  is '扣车故障登记簿';
-- Add comments to the columns 
comment on column K_DETAIN_GZTP.IDX
  is '主键';
comment on column K_DETAIN_GZTP.DETAIN_IDX
  is '扣车id';
comment on column K_DETAIN_GZTP.GZTP_NAME
  is '故障名称';
comment on column K_DETAIN_GZTP.GZTP_DESC
  is '故障描述';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_DETAIN_GZTP
  add constraint PK_K_DETAIN_GZTP primary key (IDX);
  
  