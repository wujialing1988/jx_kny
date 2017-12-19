create table K_REPAIR_WARNING_KC
(
  IDX            VARCHAR2(50 CHAR) not null,
  TRAIN_TYPE_IDX VARCHAR2(50 CHAR),
  TRAIN_TYPE     VARCHAR2(100 CHAR),
  TRAIN_NO       VARCHAR2(50 CHAR),
  BEFORE_A1_DATE DATE,
  BEFORE_A2_DATE DATE,
  BEFORE_A3_DATE DATE,
  UPDATE_TIME    DATE,
  A1_KM          NUMBER(10,2),
  A2_KM          NUMBER(10,2),
  A3_KM          NUMBER(10,2),
  A4_KM          NUMBER(10,2),
  A5_KM          NUMBER(10,2),
  TOTAL_KM       NUMBER(10,2),
  BEFORE_A4_DATE DATE,
  BEFORE_A5_DATE DATE
);

-- Add comments to the columns 
comment on column K_REPAIR_WARNING_KC.IDX
  is '主键';
comment on column K_REPAIR_WARNING_KC.TRAIN_TYPE_IDX
  is '车型主键';
comment on column K_REPAIR_WARNING_KC.TRAIN_TYPE
  is '车间简称';
comment on column K_REPAIR_WARNING_KC.TRAIN_NO
  is '车号';
comment on column K_REPAIR_WARNING_KC.BEFORE_A1_DATE
  is '上次A1时间';
comment on column K_REPAIR_WARNING_KC.BEFORE_A2_DATE
  is '上次A2时间';
comment on column K_REPAIR_WARNING_KC.BEFORE_A3_DATE
  is '上次A3时间';
comment on column K_REPAIR_WARNING_KC.UPDATE_TIME
  is '更新时间';
comment on column K_REPAIR_WARNING_KC.A1_KM
  is 'A1修程累计走行';
comment on column K_REPAIR_WARNING_KC.A2_KM
  is 'A2修程累计走行';
comment on column K_REPAIR_WARNING_KC.A3_KM
  is 'A3修程累计走行';
comment on column K_REPAIR_WARNING_KC.A4_KM
  is 'A4修程累计走行';
comment on column K_REPAIR_WARNING_KC.A5_KM
  is 'A5修程累计走行';
comment on column K_REPAIR_WARNING_KC.TOTAL_KM
  is '总走行公里';
comment on column K_REPAIR_WARNING_KC.BEFORE_A4_DATE
  is '上次A4时间';
comment on column K_REPAIR_WARNING_KC.BEFORE_A5_DATE
  is '上次A5时间';
  
  
  -- Add/modify columns 
alter table K_REPAIR_WARNING_KC add REPAIR_CLASS VARCHAR2(50 CHAR);
alter table K_REPAIR_WARNING_KC add REPAIR_CLASS_NAME VARCHAR2(50 CHAR);
alter table K_REPAIR_WARNING_KC add REMARK VARCHAR2(200 CHAR);
-- Add comments to the columns 
comment on column K_REPAIR_WARNING_KC.REPAIR_CLASS
  is '下一修程';
comment on column K_REPAIR_WARNING_KC.REPAIR_CLASS_NAME
  is '下一修程名称';
comment on column K_REPAIR_WARNING_KC.REMARK
  is '备注';