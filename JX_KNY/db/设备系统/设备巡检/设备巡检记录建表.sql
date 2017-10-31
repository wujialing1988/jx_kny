create table E_INSPECT_RECORD 
(
   IDX                  VARCHAR2(32)         not null,
   PLAN_EQUIPMENT_IDX   VARCHAR2(32),
   CLASS_CODE           VARCHAR2(30),
   CLASS_NAME           VARCHAR2(30),
   CHECK_ITEM           VARCHAR2(200)        not null,
   CHECK_ITEM_PY        VARCHAR2(200),
   CHECK_STANDARD       VARCHAR2(200),
   SEQ_NO               SMALLINT,
   REMARKS              VARCHAR2(200),
   CHECK_RESULT         VARCHAR2(50),
   INSPECT_WORKER       VARCHAR2(10),
   INSPECT_WORKER_ID    NUMBER(10),
   REPAIR_TYPE          SMALLINT             default NULL,
   CHECK_TIME           DATE,
   RECORD_STATUS        NUMBER(1,0)          not null,
   CREATOR              NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   UPDATOR              NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null,
   constraint PK_E_INSPECT_RECORD primary key (IDX)
);

comment on table E_INSPECT_RECORD is
'设备巡检记录';

comment on column E_INSPECT_RECORD.IDX is
'idx主键';

comment on column E_INSPECT_RECORD.PLAN_EQUIPMENT_IDX is
'巡检设备idx主键';

comment on column E_INSPECT_RECORD.CLASS_CODE is
'设备类别编码';

comment on column E_INSPECT_RECORD.CLASS_NAME is
'设备类别名称';

comment on column E_INSPECT_RECORD.CHECK_ITEM is
'检查项目';

comment on column E_INSPECT_RECORD.CHECK_ITEM_PY is
'检查项目首拼（用于根据首字母进行快速检索）';

comment on column E_INSPECT_RECORD.CHECK_STANDARD is
'检查标准';

comment on column E_INSPECT_RECORD.SEQ_NO is
'顺序号';

comment on column E_INSPECT_RECORD.REMARKS is
'备注';

comment on column E_INSPECT_RECORD.CHECK_RESULT is
'检查结果（合格、不合格）';

comment on column E_INSPECT_RECORD.INSPECT_WORKER is
'巡检人';

comment on column E_INSPECT_RECORD.INSPECT_WORKER_ID is
'巡检人id';

comment on column E_INSPECT_RECORD.REPAIR_TYPE is
'检修类型，1：机械、2：电气、3：其它';

comment on column E_INSPECT_RECORD.RECORD_STATUS is
'记录状态';
