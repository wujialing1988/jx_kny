create table E_SBJX_EQUIPMENT_SHUTDOWN 
(
   idx                VARCHAR2(32)         not null,
   equipment_idx      VARCHAR2(32)         not null,
   equipment_code     VARCHAR2(20)         not null,
   equipment_name     VARCHAR2(30)         not null,
   class_code         VARCHAR2(30)         not null,
   class_name         VARCHAR2(30)         not null,
   type               VARCHAR2(10),
   start_time         DATE,
   end_time           DATE,
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_SBJX_EQUIPMENT_SHUTDOWN primary key (idx)
);

comment on table E_SBJX_EQUIPMENT_SHUTDOWN is
'设备停机记录';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.idx is
'主键';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.equipment_idx is
'设备idx主键';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.equipment_code is
'设备编码';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.equipment_name is
'设备名称';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.class_code is
'设备类别编码';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.class_name is
'设备类别名称';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.type is
'停机类型：临修、小修、中修、项修、缺勤、停工带料';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.start_time is
'停机开始时间';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.end_time is
'停机结束时间';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.record_status is
'数据状态';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.creator is
'创建人';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.create_time is
'创建时间';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.updator is
'修改人';

comment on column E_SBJX_EQUIPMENT_SHUTDOWN.update_time is
'修改时间';
