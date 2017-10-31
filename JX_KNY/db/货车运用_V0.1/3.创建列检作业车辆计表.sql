-- Create table
create table ZB_ZBGL_PLAN_RECORD
(
  IDX                  VARCHAR2(50 CHAR) not null,
  RDP_PLAN_IDX         VARCHAR2(50 CHAR),
  SEQ_NUM              NUMBER(2),
  TRAIN_TYPE_IDX       VARCHAR2(50 CHAR),
  TRAIN_TYPE_CODE      VARCHAR2(50 CHAR),
  TRAIN_TYPE_NAME      VARCHAR2(50 CHAR),
  TRAIN_NO             VARCHAR2(50 CHAR),
  WORK_PERSON_IDX      VARCHAR2(2000 CHAR),
  WORK_PERSON_NAME     VARCHAR2(2000 CHAR),
  RDPIDX               VARCHAR2(50 CHAR),
  RECORD_STATUS        NUMBER(1),
  CREATOR              NUMBER(18) not null,
  CREATE_TIME          DATE not null,
  UPDATOR              NUMBER(18) not null,
  UPDATE_TIME          DATE not null,
  T_VEHICLE_KIND_CODE  VARCHAR2(50 CHAR),
  T_VEHICLE_KIND_NAME  VARCHAR2(200 CHAR),
  RDP_RECORD_STATUS    VARCHAR2(50 CHAR),
  START_PERSON_IDX     VARCHAR2(50 CHAR),
  START_PERSON_NAME    VARCHAR2(50 CHAR),
  COMPLETE_PERSON_IDX  VARCHAR2(50 CHAR),
  COMPLETE_PERSON_NAME VARCHAR2(50 CHAR),
  RDP_START_TIME       DATE,
  RDP_END_TIME         DATE
);


-- Add comments to the table 
comment on table ZB_ZBGL_PLAN_RECORD
  is '列检作业车辆计划';
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN_RECORD.IDX
  is 'idx主键';
comment on column ZB_ZBGL_PLAN_RECORD.RDP_PLAN_IDX
  is '列检计划';
comment on column ZB_ZBGL_PLAN_RECORD.SEQ_NUM
  is '车辆编号';
comment on column ZB_ZBGL_PLAN_RECORD.TRAIN_TYPE_IDX
  is '车辆车型ID';
comment on column ZB_ZBGL_PLAN_RECORD.TRAIN_TYPE_CODE
  is '车辆车型编码';
comment on column ZB_ZBGL_PLAN_RECORD.TRAIN_TYPE_NAME
  is '车辆车型名称';
comment on column ZB_ZBGL_PLAN_RECORD.TRAIN_NO
  is '车辆车号';
comment on column ZB_ZBGL_PLAN_RECORD.WORK_PERSON_IDX
  is '作业人员ID集合';
comment on column ZB_ZBGL_PLAN_RECORD.WORK_PERSON_NAME
  is '作业人员姓名集合';
comment on column ZB_ZBGL_PLAN_RECORD.RDPIDX
  is '作业实例ID';
comment on column ZB_ZBGL_PLAN_RECORD.RECORD_STATUS
  is '表示此条记录的状态：0为表示未删除；1表示删除';
comment on column ZB_ZBGL_PLAN_RECORD.CREATOR
  is '创建人';
comment on column ZB_ZBGL_PLAN_RECORD.CREATE_TIME
  is '创建时间';
comment on column ZB_ZBGL_PLAN_RECORD.UPDATOR
  is '修改人';
comment on column ZB_ZBGL_PLAN_RECORD.UPDATE_TIME
  is '修改时间';
comment on column ZB_ZBGL_PLAN_RECORD.T_VEHICLE_KIND_CODE
  is '车型种类编码';
comment on column ZB_ZBGL_PLAN_RECORD.T_VEHICLE_KIND_NAME
  is '车型种类名称';
comment on column ZB_ZBGL_PLAN_RECORD.RDP_RECORD_STATUS
  is '车辆状态（未处理、处理中、已完成）';
comment on column ZB_ZBGL_PLAN_RECORD.START_PERSON_IDX
  is '车辆任务启动人员ID';
comment on column ZB_ZBGL_PLAN_RECORD.START_PERSON_NAME
  is '车辆任务启动人员姓名';
comment on column ZB_ZBGL_PLAN_RECORD.COMPLETE_PERSON_IDX
  is '完成作业人员ID';
comment on column ZB_ZBGL_PLAN_RECORD.COMPLETE_PERSON_NAME
  is '完成作业人员姓名';
comment on column ZB_ZBGL_PLAN_RECORD.RDP_START_TIME
  is '车辆列检开始时间';
comment on column ZB_ZBGL_PLAN_RECORD.RDP_END_TIME
  is '车辆列检结束时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ZB_ZBGL_PLAN_RECORD
  add constraint PK_ZB_ZBGL_PLAN_RECORD primary key (IDX);