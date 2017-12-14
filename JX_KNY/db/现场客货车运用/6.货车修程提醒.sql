create table K_REPAIR_WARNING_HC
(
  IDX            VARCHAR2(50 CHAR) not null,
  TRAIN_TYPE_IDX VARCHAR2(50 CHAR),
  TRAIN_TYPE     VARCHAR2(100 CHAR),
  TRAIN_NO       VARCHAR2(50 CHAR),
  BEFORE_FX_DATE DATE,
  BEFORE_DX_DATE DATE,
  BEFORE_CX_DATE DATE
);

-- Add comments to the table 
comment on table K_REPAIR_WARNING_HC
  is '货车修程提醒';
-- Add comments to the columns 
comment on column K_REPAIR_WARNING_HC.IDX
  is '主键';
comment on column K_REPAIR_WARNING_HC.TRAIN_TYPE_IDX
  is '车辆车型主键';
comment on column K_REPAIR_WARNING_HC.TRAIN_TYPE
  is '车辆车型简称';
comment on column K_REPAIR_WARNING_HC.TRAIN_NO
  is '车号';
comment on column K_REPAIR_WARNING_HC.BEFORE_FX_DATE
  is '上次辅修时间';
comment on column K_REPAIR_WARNING_HC.BEFORE_DX_DATE
  is '上次段修时间';
comment on column K_REPAIR_WARNING_HC.BEFORE_CX_DATE
  is '上次厂修时间';
  
  alter table K_REPAIR_WARNING_HC
  add constraint PK_K_REPAIR_WARNING_HC primary key (IDX);
  
  -- Add/modify columns 
alter table K_REPAIR_WARNING_HC add Update_Time date;
-- Add comments to the columns 
comment on column K_REPAIR_WARNING_HC.Update_Time
  is '更新时间';
  