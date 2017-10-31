create table E_INSPECT_PLAN 
(
   IDX                  VARCHAR2(32)         not null,
   ROUTE_IDX            VARCHAR2(32)         not null,
   ROUTE_NAME           VARCHAR2(50)         not null,
   PARTROL_WORKER_ID    NUMBER(10),
   PARTROL_WORKER       VARCHAR2(10),
   PERIOD_TYPE          VARCHAR2(10),
   PLAN_START_DATE      DATE,
   PLAN_END_DATE        DATE,
   REAL_START_DATE      DATE,
   REAL_END_DATE        DATE,
   STATE                VARCHAR2(10),
   RECORD_STATUS        NUMBER(1,0)          not null,
   CREATOR              NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   UPDATOR              NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null,
   constraint PK_E_INSPECT_PLAN primary key (IDX)
);

comment on table E_INSPECT_PLAN is
'巡检周期计划';

comment on column E_INSPECT_PLAN.IDX is
'idx主键';

comment on column E_INSPECT_PLAN.ROUTE_IDX is
'巡检范围idx主键';

comment on column E_INSPECT_PLAN.ROUTE_NAME is
'巡检范围名称';

comment on column E_INSPECT_PLAN.PARTROL_WORKER_ID is
'计划编制人id';

comment on column E_INSPECT_PLAN.PARTROL_WORKER is
'计划编制人名称';

comment on column E_INSPECT_PLAN.PERIOD_TYPE is
'巡检周期（周检，半月检，月检，季检）';

comment on column E_INSPECT_PLAN.PLAN_START_DATE is
'计划开始日期';

comment on column E_INSPECT_PLAN.PLAN_END_DATE is
'计划结束日期';

comment on column E_INSPECT_PLAN.REAL_START_DATE is
'实际开始日期';

comment on column E_INSPECT_PLAN.REAL_END_DATE is
'实际结束日期';

comment on column E_INSPECT_PLAN.STATE is
'处理状态：未处理、已处理';

comment on column E_INSPECT_PLAN.RECORD_STATUS is
'记录状态';
