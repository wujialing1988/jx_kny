create table K_TRAIN_INSPECTION
(
  IDX             VARCHAR2(50 CHAR) not null,
  INSPECTION_NAME VARCHAR2(100 CHAR),
  LEVEL_NAME      VARCHAR2(100 CHAR),
  INSPECTION_CODE VARCHAR2(50 CHAR),
  LEVEL_CODE      VARCHAR2(50 CHAR),
  REMARK          VARCHAR2(1000 CHAR),
  RECORD_STATUS   NUMBER(1),
  CREATOR         NUMBER(18) not null,
  CREATE_TIME     DATE not null,
  UPDATOR         NUMBER(18) not null,
  UPDATE_TIME     DATE not null
);

-- Add comments to the columns 
comment on column K_TRAIN_INSPECTION.IDX
  is '主键';
comment on column K_TRAIN_INSPECTION.INSPECTION_NAME
  is '列检所名称';
comment on column K_TRAIN_INSPECTION.LEVEL_NAME
  is '等级名称';
comment on column K_TRAIN_INSPECTION.INSPECTION_CODE
  is '列检所编码';
comment on column K_TRAIN_INSPECTION.LEVEL_CODE
  is '等级编码';
comment on column K_TRAIN_INSPECTION.REMARK
  is '备注';
comment on column K_TRAIN_INSPECTION.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_TRAIN_INSPECTION.CREATOR
  is '创建人';
comment on column K_TRAIN_INSPECTION.CREATE_TIME
  is '创建时间';
comment on column K_TRAIN_INSPECTION.UPDATOR
  is '修改人';
comment on column K_TRAIN_INSPECTION.UPDATE_TIME
  is '修改时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table K_TRAIN_INSPECTION
  add constraint PK_K_TRAIN_INSPECTION primary key (IDX);