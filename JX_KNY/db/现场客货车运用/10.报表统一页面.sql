-- Create table
create table K_REPORT_VIEW
(
  IDX              VARCHAR2(50 CHAR) not null,
  DISPLAY_NAME     VARCHAR2(50 CHAR),
  REPORT_URL       VARCHAR2(100 CHAR),
  REMARK           VARCHAR2(200 CHAR),
  SEQ_NO           NUMBER(2),
  RECORD_STATUS    NUMBER(1),
  CREATOR          NUMBER(18) not null,
  CREATE_TIME      DATE not null,
  UPDATOR          NUMBER(18) not null,
  UPDATE_TIME      DATE not null,
  REPORT_TYPE      VARCHAR2(50 CHAR),
  REPORT_TYPE_NAME VARCHAR2(50 CHAR)
);

-- Add comments to the columns 
comment on column K_REPORT_VIEW.IDX
  is 'idx主键';
comment on column K_REPORT_VIEW.DISPLAY_NAME
  is '报表显示名称';
comment on column K_REPORT_VIEW.REPORT_URL
  is '报表路径';
comment on column K_REPORT_VIEW.REMARK
  is '备注';
comment on column K_REPORT_VIEW.SEQ_NO
  is '排序';
comment on column K_REPORT_VIEW.RECORD_STATUS
  is '表示此条记录的状态：0为表示未删除；1表示删除';
comment on column K_REPORT_VIEW.CREATOR
  is '创建人';
comment on column K_REPORT_VIEW.CREATE_TIME
  is '创建时间';
comment on column K_REPORT_VIEW.UPDATOR
  is '修改人';
comment on column K_REPORT_VIEW.UPDATE_TIME
  is '修改时间';
comment on column K_REPORT_VIEW.REPORT_TYPE
  is '报表分类编码';
comment on column K_REPORT_VIEW.REPORT_TYPE_NAME
  is '报表分类名称';
  
 alter table K_REPORT_VIEW
  add constraint PK_K_REPORT_VIEW primary key (IDX);