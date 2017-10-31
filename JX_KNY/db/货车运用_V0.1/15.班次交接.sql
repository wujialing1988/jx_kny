-- Add/modify columns 
alter table ZB_ZBGL_HO_MODEL_ITEM add SEQ_NO NUMBER(2);
-- Add comments to the columns 
comment on column ZB_ZBGL_HO_MODEL_ITEM.SEQ_NO
  is '交接项目排序';

  
create table K_CLASS_TRANSFER  (
   "IDX"                VARCHAR2(50 CHAR)               not null,
   "EMPID"              VARCHAR2(50 CHAR),
   "EMPNAME"            VARCHAR2(100 CHAR),
   "TRANSFER_EMPID"     VARCHAR2(50 CHAR),
   "TRANSFER_NAME"      VARCHAR2(100 CHAR),
   "CLASS_NO"           VARCHAR2(100 CHAR),
   "CLASS_NAME"         VARCHAR2(100 CHAR),
   "TRANSFER_CLASS_NO"  VARCHAR2(100 CHAR),
   "TRANSFER_CLASS_NAME" VARCHAR2(100 CHAR),
   "TRANSFER_DATE"      date,
   "RECORD_STATUS"      NUMBER(1),
   "CREATOR"            NUMBER(18)                      not null,
   "CREATE_TIME"        DATE                            not null,
   "UPDATOR"            NUMBER(18)                      not null,
   "UPDATE_TIME"        DATE                            not null,
   constraint PK_K_CLASS_TRANSFER primary key ("IDX")
);

comment on table K_CLASS_TRANSFER is
'班次交接';

comment on column K_CLASS_TRANSFER."IDX" is
'主键';

comment on column K_CLASS_TRANSFER."EMPID" is
'当前值班员id';

comment on column K_CLASS_TRANSFER."EMPNAME" is
'当前值班员名称';

comment on column K_CLASS_TRANSFER."TRANSFER_EMPID" is
'交接值班员id';

comment on column K_CLASS_TRANSFER."TRANSFER_NAME" is
'交接值班员名称';

comment on column K_CLASS_TRANSFER."CLASS_NO" is
'当前班组编码';

comment on column K_CLASS_TRANSFER."CLASS_NAME" is
'当前班组名称';

comment on column K_CLASS_TRANSFER."TRANSFER_CLASS_NO" is
'交接班组编码';

comment on column K_CLASS_TRANSFER."TRANSFER_CLASS_NAME" is
'交接班组名称';

comment on column K_CLASS_TRANSFER."TRANSFER_DATE" is
'交接时间';

comment on column K_CLASS_TRANSFER."RECORD_STATUS" is
'记录状态，1：删除；0：未删除；';

comment on column K_CLASS_TRANSFER."CREATOR" is
'创建人';

comment on column K_CLASS_TRANSFER."CREATE_TIME" is
'创建时间';

comment on column K_CLASS_TRANSFER."UPDATOR" is
'修改人';

comment on column K_CLASS_TRANSFER."UPDATE_TIME" is
'修改时间';

create table K_CLASS_TRANSFER_DETAILS  (
   "IDX"                VARCHAR2(50 CHAR),
   "TRANSFER_IDX"       VARCHAR2(50 CHAR),
   "TRANSFER_ITEM"      VARCHAR2(100 CHAR),
   "TRANSFER_TYPE"      VARCHAR2(50 CHAR),
   "TRANSFER_CONTENT"   VARCHAR2(500 CHAR)
);

comment on table K_CLASS_TRANSFER_DETAILS is
'交接明细';

comment on column K_CLASS_TRANSFER_DETAILS."IDX" is
'主键';

comment on column K_CLASS_TRANSFER_DETAILS."TRANSFER_IDX" is
'交接主表id';

comment on column K_CLASS_TRANSFER_DETAILS."TRANSFER_ITEM" is
'交接项目';

comment on column K_CLASS_TRANSFER_DETAILS."TRANSFER_TYPE" is
'交接项目类型';

comment on column K_CLASS_TRANSFER_DETAILS."TRANSFER_CONTENT" is
'交接项目内容';

alter table K_CLASS_TRANSFER_DETAILS
  add constraint PK_K_CLASS_TRANSFER_DETAILS primary key (IDX);
  
 -- Add/modify columns 
alter table K_CLASS_TRANSFER add SITE_ID VARCHAR2(50);
alter table K_CLASS_TRANSFER add SITE_NAME VARCHAR2(50);
-- Add comments to the columns 
comment on column K_CLASS_TRANSFER.SITE_ID
  is '站点ID';
comment on column K_CLASS_TRANSFER.SITE_NAME
  is '站点名称';
 