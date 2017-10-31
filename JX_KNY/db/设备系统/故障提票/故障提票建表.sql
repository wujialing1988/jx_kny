create table E_FAULT_ORDER 
(
   IDX                  VARCHAR2(50)         not null,
   EQUIPMENT_IDX        VARCHAR2(32)         not null,
   EQUIPMENT_NAME       VARCHAR2(30),
   EQUIPMENT_CODE       VARCHAR2(20),
   MODEL                VARCHAR2(30),
   SPECIFICATION        VARCHAR2(20),
   SUBMIT_EMP           VARCHAR2(10),
   SUBMIT_EMP_ID        NUMBER(10,0),
   FAULT_ORDER_NO       VARCHAR2(20),
   FAULT_OCCUR_TIME     DATE,
   FAULT_PLACE          VARCHAR2(300),
   FAULT_PHENOMENON     VARCHAR2(500),
   CAUSE_ANALYSIS       VARCHAR2(500),
   REPAIR_TEAM          VARCHAR2(500),
   REPAIR_TEAM_ID       NUMBER(10,0),
   ASSIST_REPAIR_TEAM   VARCHAR2(500),
   ASSIST_REPAIR_TEAM_ID VARCHAR2(50),
   REPAIR_EMP           VARCHAR2(50),
   REPAIR_EMP_ID        VARCHAR2(50),
   ASSIST_REPAIR_EMPS   VARCHAR2(50),
   REPAIR_CONTENT       VARCHAR2(500),
   REPAIR_COST          NUMBER(10,2),
   FAULT_RECOVER_TIME   DATE,
   USE_WORKER           VARCHAR2(10),
   USE_WORKER_ID        NUMBER(10),
   FAULT_LEVEL          VARCHAR2(10),
   STATE                VARCHAR2(10),
   BACK_REASON          VARCHAR2(200),
   DISPATCH_DATE_DD     DATE,
   DISPATCH_DATE_GZ     DATE,
   RECORD_STATUS        NUMBER(1,0)          not null,
   CREATOR              NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   UPDATOR              NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null,
   constraint PK_E_FAULT_ORDER primary key (IDX)
);

comment on table E_FAULT_ORDER is
'故障提票';

comment on column E_FAULT_ORDER.IDX is
'idx主键';

comment on column E_FAULT_ORDER.EQUIPMENT_IDX is
'设备idx主键';

comment on column E_FAULT_ORDER.EQUIPMENT_NAME is
'设备名称';

comment on column E_FAULT_ORDER.EQUIPMENT_CODE is
'设备编码';

comment on column E_FAULT_ORDER.MODEL is
'型号';

comment on column E_FAULT_ORDER.SPECIFICATION is
'规格';

comment on column E_FAULT_ORDER.SUBMIT_EMP is
'提报人';

comment on column E_FAULT_ORDER.SUBMIT_EMP_ID is
'提报人id';

comment on column E_FAULT_ORDER.FAULT_ORDER_NO is
'提票单号';

comment on column E_FAULT_ORDER.FAULT_OCCUR_TIME is
'故障发现时间';

comment on column E_FAULT_ORDER.FAULT_PLACE is
'故障部位及意见';

comment on column E_FAULT_ORDER.FAULT_PHENOMENON is
'故障现象';

comment on column E_FAULT_ORDER.CAUSE_ANALYSIS is
'故障原因分析';

comment on column E_FAULT_ORDER.REPAIR_TEAM is
'施修班组';

comment on column E_FAULT_ORDER.REPAIR_TEAM_ID is
'施修班组id';

comment on column E_FAULT_ORDER.ASSIST_REPAIR_TEAM is
'施修班组，多个班组以英文逗号“,”进行分隔';

comment on column E_FAULT_ORDER.ASSIST_REPAIR_TEAM_ID is
'施修班组id，多个班组以英文逗号“,”进行分隔';

comment on column E_FAULT_ORDER.REPAIR_EMP is
'修理人';

comment on column E_FAULT_ORDER.REPAIR_EMP_ID is
'修理人id';

comment on column E_FAULT_ORDER.ASSIST_REPAIR_EMPS is
'辅修人员';

comment on column E_FAULT_ORDER.REPAIR_CONTENT is
'实际修理内容';

comment on column E_FAULT_ORDER.REPAIR_COST is
'修理费用';

comment on column E_FAULT_ORDER.FAULT_RECOVER_TIME is
'故障恢复时间';

comment on column E_FAULT_ORDER.USE_WORKER is
'使用人';

comment on column E_FAULT_ORDER.USE_WORKER_ID is
'使用人id';

comment on column E_FAULT_ORDER.FAULT_LEVEL is
'故障等级（一般，重大，特大）';

comment on column E_FAULT_ORDER.STATE is
'提票状态（新建，已派工，处理中，已处理，退回）';

comment on column E_FAULT_ORDER.BACK_REASON is
'返回原因';

comment on column E_FAULT_ORDER.DISPATCH_DATE_DD is
'调度派工日期';

comment on column E_FAULT_ORDER.DISPATCH_DATE_GZ is
'工长派工日期';

comment on column E_FAULT_ORDER.RECORD_STATUS is
'记录状态';

comment on column E_FAULT_ORDER.CREATOR is
'创建人';

comment on column E_FAULT_ORDER.CREATE_TIME is
'创建时间';

comment on column E_FAULT_ORDER.UPDATOR is
'更新人';

comment on column E_FAULT_ORDER.UPDATE_TIME is
'更新时间';
