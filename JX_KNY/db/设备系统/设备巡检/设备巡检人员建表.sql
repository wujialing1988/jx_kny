create table E_INSPECT_PLAN_EQUIPMENT_EMP 
(
   IDX                  VARCHAR2(32)         not null,
   PLAN_EQUIPMENT_IDX   VARCHAR2(32),
   INSPECT_EMPID        VARCHAR2(50),
   INSPECT_EMP          VARCHAR2(50),
   INSPECT_ORGID        VARCHAR2(50),
   INSPECT_ORGNAME      VARCHAR2(50),
   ENTRUST_INSPECT_EMPID VARCHAR2(50),
   ENTRUST_INSPECT_EMP  VARCHAR2(50),
   ENTRUST_INSPECT_ORGID VARCHAR2(50),
   ENTRUST_INSPECT_ORGNAME VARCHAR2(50),
   REPAIR_TYPE          SMALLINT             default NULL,
   CHECK_RESULT         VARCHAR2(10),
   CHECK_RESULT_DESC    VARCHAR2(200),
   RECORD_STATUS        NUMBER(1,0)          not null,
   CREATOR              NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   UPDATOR              NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null,
   constraint PK_E_INSPECT_PLAN_EQUIPMENT_EM primary key (IDX)
);

comment on table E_INSPECT_PLAN_EQUIPMENT_EMP is
'设备人巡检员';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.IDX is
'idx主键';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.PLAN_EQUIPMENT_IDX is
'巡检设备idx主键';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.INSPECT_EMPID is
'人员id，多个人员以英文逗号“,”进行分隔';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.INSPECT_EMP is
'人员名称，多个人员以英文逗号“,”进行分隔';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.INSPECT_ORGID is
'班组id，多个班组以英文逗号“,”进行分隔';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.INSPECT_ORGNAME is
'班组名称，多个班组以英文逗号“,”进行分隔';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.ENTRUST_INSPECT_EMPID is
'人员id，多个人员以英文逗号“,”进行分隔';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.ENTRUST_INSPECT_EMP is
'人员名称，多个人员以英文逗号“,”进行分隔';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.ENTRUST_INSPECT_ORGID is
'班组id，多个班组以英文逗号“,”进行分隔';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.ENTRUST_INSPECT_ORGNAME is
'班组名称，多个班组以英文逗号“,”进行分隔';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.REPAIR_TYPE is
'检修类型，1：机械、2：电气、3：其它';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.CHECK_RESULT is
'巡检结果（已巡检、未巡检）';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.CHECK_RESULT_DESC is
'巡检情况描述';

comment on column E_INSPECT_PLAN_EQUIPMENT_EMP.RECORD_STATUS is
'记录状态';
