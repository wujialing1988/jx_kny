-- Create table
create table K_DESK_PRIVILIGE
(
  IDX       VARCHAR2(50 CHAR) not null,
  EMP_ID    VARCHAR2(50 CHAR),
  EMP_NAME  VARCHAR2(100 CHAR),
  IS_SHOW   NUMBER(1),
  DICT_CODE VARCHAR2(50 CHAR),
  DICT_NAME VARCHAR2(50 CHAR)
);

-- Add comments to the table 
comment on table K_DESK_PRIVILIGE
  is '桌面权限';
-- Add comments to the columns 
comment on column K_DESK_PRIVILIGE.IDX
  is '主键';
comment on column K_DESK_PRIVILIGE.EMP_ID
  is '人员id';
comment on column K_DESK_PRIVILIGE.EMP_NAME
  is '人员名称';
comment on column K_DESK_PRIVILIGE.IS_SHOW
  is '是否显示';
comment on column K_DESK_PRIVILIGE.DICT_CODE
  is '权限模块编码';
comment on column K_DESK_PRIVILIGE.DICT_NAME
  is '权限模块名称';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_DESK_PRIVILIGE
  add constraint PK_K_DESK_PRIVILIGE primary key (IDX);
  
  