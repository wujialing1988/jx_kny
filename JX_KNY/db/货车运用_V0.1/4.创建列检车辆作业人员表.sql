-- Create table
create table ZB_ZBGL_PLAN_WORKER
(
  IDX              VARCHAR2(50 CHAR) not null,
  RDP_RECORD_IDX   VARCHAR2(50 CHAR),
  WORK_PERSON_IDX  VARCHAR2(50 CHAR),
  WORK_PERSON_NAME VARCHAR2(50 CHAR)
);

-- Add comments to the table 
comment on table ZB_ZBGL_PLAN_WORKER
  is '列检车辆作业人员';
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN_WORKER.IDX
  is 'idx主键';
comment on column ZB_ZBGL_PLAN_WORKER.RDP_RECORD_IDX
  is '列检车辆计划';
comment on column ZB_ZBGL_PLAN_WORKER.WORK_PERSON_IDX
  is '作业人员ID';
comment on column ZB_ZBGL_PLAN_WORKER.WORK_PERSON_NAME
  is '作业人员姓名';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ZB_ZBGL_PLAN_WORKER
  add constraint PK_ZB_ZBGL_PLAN_WORKER primary key (IDX);