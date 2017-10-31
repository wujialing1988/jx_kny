create table E_INSPECT_ROUTE 
(
   IDX                  VARCHAR2(32)         not null,
   ROUTE_NAME           VARCHAR2(50)         not null,
   PARTROL_WORKER_ID    NUMBER(10),
   PARTROL_WORKER       VARCHAR2(10),
   PERIOD_TYPE          SMALLINT,
   PLAN_PUBLISH_DATE    DATE,
   EXPIRY_DATE          DATE,
   STATE                SMALLINT,
   RECORD_STATUS        NUMBER(1,0)          not null,
   CREATOR              NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   UPDATOR              NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null,
   constraint PK_E_INSPECT_ROUTE primary key (IDX)
);

comment on table E_INSPECT_ROUTE is
'巡检范围（原巡检线路）';

comment on column E_INSPECT_ROUTE.IDX is
'idx主键';

comment on column E_INSPECT_ROUTE.ROUTE_NAME is
'线路名称';

comment on column E_INSPECT_ROUTE.PARTROL_WORKER_ID is
'计划编制人id';

comment on column E_INSPECT_ROUTE.PARTROL_WORKER is
'计划编制人名称';

comment on column E_INSPECT_ROUTE.PERIOD_TYPE is
'巡检周期（1：周检；2：半月检；3：月检；4：季检）';

comment on column E_INSPECT_ROUTE.PLAN_PUBLISH_DATE is
'有效期起';

comment on column E_INSPECT_ROUTE.EXPIRY_DATE is
'有效期至';

comment on column E_INSPECT_ROUTE.STATE is
'状态（1启用、0未启用）';

comment on column E_INSPECT_ROUTE.RECORD_STATUS is
'记录状态';

comment on column E_INSPECT_ROUTE.CREATOR is
'创建人';

comment on column E_INSPECT_ROUTE.CREATE_TIME is
'创建时间';

comment on column E_INSPECT_ROUTE.UPDATOR is
'更新人';

comment on column E_INSPECT_ROUTE.UPDATE_TIME is
'更新时间';
