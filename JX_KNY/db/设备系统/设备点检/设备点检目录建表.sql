create table E_SBJX_POINT_CHECK_CATALOG 
(
   idx                VARCHAR2(32)         not null,
   equipment_idx      VARCHAR2(32)         not null,
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_SBJX_POINT_CHECK_CATALOG primary key (idx)
);

comment on table E_SBJX_POINT_CHECK_CATALOG is
'设备点检目录';

comment on column E_SBJX_POINT_CHECK_CATALOG.idx is
'主键';

comment on column E_SBJX_POINT_CHECK_CATALOG.equipment_idx is
'设备点检任务单idx主键';

comment on column E_SBJX_POINT_CHECK_CATALOG.record_status is
'数据状态';

comment on column E_SBJX_POINT_CHECK_CATALOG.creator is
'创建人';

comment on column E_SBJX_POINT_CHECK_CATALOG.create_time is
'创建时间';

comment on column E_SBJX_POINT_CHECK_CATALOG.updator is
'修改人';

comment on column E_SBJX_POINT_CHECK_CATALOG.update_time is
'修改时间';
