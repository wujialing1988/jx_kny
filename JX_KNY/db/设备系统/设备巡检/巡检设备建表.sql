create table E_INSPECT_PLAN_EQUIPMENT 
(
   IDX                  VARCHAR2(32)         not null,
   PLAN_IDX             VARCHAR2(32),
   EQUIPMENT_IDX        VARCHAR2(32),
   SEQ_NO               SMALLINT,
   real_begin_time    DATE,
   real_end_time      DATE,
   USE_WORKER           VARCHAR2(10),
   USE_WORKER_ID        NUMBER(10),
   CHECK_RESULT         VARCHAR2(10),
   CHECK_RESULT_DESC    VARCHAR2(200),
   RECORD_STATUS        NUMBER(1,0)          not null,
   CREATOR              NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   UPDATOR              NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null,
   constraint PK_E_INSPECT_PLAN_EQUIPMENT primary key (IDX)
);

comment on table E_INSPECT_PLAN_EQUIPMENT is
'巡检周期计划设备';

comment on column E_INSPECT_PLAN_EQUIPMENT.IDX is
'idx主键';

comment on column E_INSPECT_PLAN_EQUIPMENT.PLAN_IDX is
'巡检计划idx主键';

comment on column E_INSPECT_PLAN_EQUIPMENT.EQUIPMENT_IDX is
'设备idx主键';

comment on column E_INSPECT_PLAN_EQUIPMENT.SEQ_NO is
'顺序号（保留）';

comment on column E_INSPECT_PLAN_EQUIPMENT.real_begin_time is
'实际开工时间';

comment on column E_INSPECT_PLAN_EQUIPMENT.real_end_time is
'实际完工时间';

comment on column E_INSPECT_PLAN_EQUIPMENT.USE_WORKER is
'使用人';

comment on column E_INSPECT_PLAN_EQUIPMENT.USE_WORKER_ID is
'使用人id';

comment on column E_INSPECT_PLAN_EQUIPMENT.CHECK_RESULT is
'巡检结果（已巡检、未巡检）';

comment on column E_INSPECT_PLAN_EQUIPMENT.CHECK_RESULT_DESC is
'巡检情况描述';

comment on column E_INSPECT_PLAN_EQUIPMENT.RECORD_STATUS is
'记录状态';
