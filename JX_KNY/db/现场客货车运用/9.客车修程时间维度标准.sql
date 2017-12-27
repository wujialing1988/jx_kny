-- Create table
create table JCJX_REPAIR_STANDARD_TIME
(
  IDX                       VARCHAR2(32) not null,
  TRAIN_TYPE_IDX            VARCHAR2(50),
  REPAIR_CLASS              VARCHAR2(10),
  REPAIR_CLASS_NAME         VARCHAR2(10),
  REPAIR_CLASS_COMPARE      VARCHAR2(10),
  REPAIR_CLASS_COMPARE_NAME VARCHAR2(10),
  COMPARE_DAY               NUMBER(10,2),
  SITEID                    VARCHAR2(5)
);

-- Add comments to the columns 
comment on column JCJX_REPAIR_STANDARD_TIME.IDX
  is '主键';
comment on column JCJX_REPAIR_STANDARD_TIME.TRAIN_TYPE_IDX
  is '车型主键';
comment on column JCJX_REPAIR_STANDARD_TIME.REPAIR_CLASS
  is '修程';
comment on column JCJX_REPAIR_STANDARD_TIME.REPAIR_CLASS_NAME
  is '修程名称';
comment on column JCJX_REPAIR_STANDARD_TIME.REPAIR_CLASS_COMPARE
  is '对比修程';
comment on column JCJX_REPAIR_STANDARD_TIME.REPAIR_ORDER_COMPARE_NAME
  is '对比修程名称';
comment on column JCJX_REPAIR_STANDARD_TIME.COMPARE_DAY
  is '对比天数';
-- Create/Recreate primary, unique and foreign key constraints 
alter table JCJX_REPAIR_STANDARD_TIME
  add constraint PK_REPAIR_STANDARD_TIME primary key (IDX);
  
  
-- 客车修程添加下次修程时间（时间维度）
-- Add/modify columns 
alter table K_REPAIR_WARNING_KC add NEXT_A1_DATE DATE;
alter table K_REPAIR_WARNING_KC add NEXT_A2_DATE DATE;
alter table K_REPAIR_WARNING_KC add NEXT_A3_DATE DATE;
alter table K_REPAIR_WARNING_KC add NEXT_A4_DATE DATE;
alter table K_REPAIR_WARNING_KC add NEXT_A5_DATE DATE;
-- Add comments to the columns 
comment on column K_REPAIR_WARNING_KC.NEXT_A1_DATE
  is '下次A1时间';
comment on column K_REPAIR_WARNING_KC.NEXT_A2_DATE
  is '下次A2时间';
comment on column K_REPAIR_WARNING_KC.NEXT_A3_DATE
  is '下次A3时间';
comment on column K_REPAIR_WARNING_KC.NEXT_A4_DATE
  is '下次A4时间';
comment on column K_REPAIR_WARNING_KC.NEXT_A5_DATE
  is '下次A5时间';
  
 -- Add/modify columns 
alter table K_REPAIR_WARNING_KC add REPAIR_WARNING_TYPE VARCHAR2(50 CHAR);
-- Add comments to the columns 
comment on column K_REPAIR_WARNING_KC.REPAIR_WARNING_TYPE
  is '修程预警类型 10 走行判断 20 时间判断';
 
