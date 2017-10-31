create table E_INSPECT_ROUTE_DETAILS 
(
   IDX                  VARCHAR2(32)         not null,
   ROUTE_IDX            VARCHAR2(32),
   EQUIPMENT_IDX        VARCHAR2(32),
   MAC_INSPECT_EMPID    VARCHAR2(50),
   MAC_INSPECT_EMP      VARCHAR2(50),
   ELC_INSPECT_EMPID    VARCHAR2(50),
   ELC_INSPECT_EMP      VARCHAR2(50),
   SEQ_NO               SMALLINT,
   RECORD_STATUS        NUMBER(1,0)          not null,
   CREATOR              NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   UPDATOR              NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null,
   constraint PK_E_INSPECT_ROUTE_DETAILS primary key (IDX)
);

comment on table E_INSPECT_ROUTE_DETAILS is
'巡检范围明细';

comment on column E_INSPECT_ROUTE_DETAILS.IDX is
'idx主键';

comment on column E_INSPECT_ROUTE_DETAILS.ROUTE_IDX is
'巡检范围idx主键';

comment on column E_INSPECT_ROUTE_DETAILS.EQUIPMENT_IDX is
'设备idx主键';

comment on column E_INSPECT_ROUTE_DETAILS.MAC_INSPECT_EMPID is
'人员id，多个人员以英文逗号“,”进行分隔';

comment on column E_INSPECT_ROUTE_DETAILS.MAC_INSPECT_EMP is
'人员名称，多个人员以英文逗号“,”进行分隔';

comment on column E_INSPECT_ROUTE_DETAILS.ELC_INSPECT_EMPID is
'人员id，多个人员以英文逗号“,”进行分隔';

comment on column E_INSPECT_ROUTE_DETAILS.ELC_INSPECT_EMP is
'人员名称，多个人员以英文逗号“,”进行分隔';

comment on column E_INSPECT_ROUTE_DETAILS.SEQ_NO is
'顺序号（保留）';

comment on column E_INSPECT_ROUTE_DETAILS.RECORD_STATUS is
'记录状态';

comment on column E_INSPECT_ROUTE_DETAILS.CREATOR is
'创建人';

comment on column E_INSPECT_ROUTE_DETAILS.CREATE_TIME is
'创建时间';

comment on column E_INSPECT_ROUTE_DETAILS.UPDATOR is
'更新人';

comment on column E_INSPECT_ROUTE_DETAILS.UPDATE_TIME is
'更新时间';
