-- Create table
create table SBJK_CALL_LOG
(
  IDX             VARCHAR2(50) not null,
  METHOD_NAME     VARCHAR2(50),
  REQUEST_CONTENT VARCHAR2(2000),
  RESPONSE_RESULT VARCHAR2(500),
  CALL_TIME       DATE,
  CALLER_INFO     VARCHAR2(500),
  REMARK          VARCHAR2(1000)
)
tablespace JX2_COREFRAME
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table SBJK_CALL_LOG
  is '设备接口-调用日志记录';
-- Add comments to the columns 
comment on column SBJK_CALL_LOG.IDX
  is 'idx';
comment on column SBJK_CALL_LOG.METHOD_NAME
  is '调用方法名称';
comment on column SBJK_CALL_LOG.REQUEST_CONTENT
  is '请求参数内容';
comment on column SBJK_CALL_LOG.RESPONSE_RESULT
  is '返回结果信息 ';
comment on column SBJK_CALL_LOG.CALL_TIME
  is '调用接口时间';
comment on column SBJK_CALL_LOG.CALLER_INFO
  is '调用方信息';
comment on column SBJK_CALL_LOG.REMARK
  is ' 备注';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SBJK_CALL_LOG
  add constraint PK_CALL_IDX primary key (IDX)
  using index 
  tablespace JX2_COREFRAME
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
