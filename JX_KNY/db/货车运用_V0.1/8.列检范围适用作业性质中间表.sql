drop table ZB_FW_NATURE_RELATION  ;
create table ZB_FW_NATURE_RELATION
(
  IDX              	VARCHAR2(50 CHAR) not null,
  ZBFW_IDX   		VARCHAR2(50 CHAR),
  WORK_NATURE_CODE  VARCHAR2(50 CHAR),
  WORK_NATURE VARCHAR2(50 CHAR)
);

comment on table ZB_FW_NATURE_RELATION
  is '列检范围适用车型';
-- Add comments to the columns 
comment on column ZB_FW_NATURE_RELATION.IDX
  is 'idx主键';
comment on column ZB_FW_NATURE_RELATION.ZBFW_IDX
  is '列检范围idx';
comment on column ZB_FW_NATURE_RELATION.WORK_NATURE_CODE
  is '适用作业性质编码';
comment on column ZB_FW_NATURE_RELATION.WORK_NATURE
  is '适用作业性质名称';
  
 alter table ZB_FW_NATURE_RELATION
  add constraint PK_ZB_FW_NATURE_RELATION primary key (IDX);