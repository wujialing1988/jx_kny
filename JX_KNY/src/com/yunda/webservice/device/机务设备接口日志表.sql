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
  is '�豸�ӿ�-������־��¼';
-- Add comments to the columns 
comment on column SBJK_CALL_LOG.IDX
  is 'idx';
comment on column SBJK_CALL_LOG.METHOD_NAME
  is '���÷�������';
comment on column SBJK_CALL_LOG.REQUEST_CONTENT
  is '�����������';
comment on column SBJK_CALL_LOG.RESPONSE_RESULT
  is '���ؽ����Ϣ ';
comment on column SBJK_CALL_LOG.CALL_TIME
  is '���ýӿ�ʱ��';
comment on column SBJK_CALL_LOG.CALLER_INFO
  is '���÷���Ϣ';
comment on column SBJK_CALL_LOG.REMARK
  is ' ��ע';
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
