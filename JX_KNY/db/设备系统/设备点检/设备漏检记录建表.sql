create table E_SBJX_POINT_CHECK_OMIT 
(
   idx                VARCHAR2(32)         not null,
   equipment_idx      VARCHAR2(32)         not null,
   uncheck_date       DATE                 not null,
   update_time        DATE                 not null,
   constraint PK_E_SBJX_POINT_CHECK_OMIT primary key (idx)
);

comment on table E_SBJX_POINT_CHECK_OMIT is
'设备漏检记录';

comment on column E_SBJX_POINT_CHECK_OMIT.idx is
'主键';

comment on column E_SBJX_POINT_CHECK_OMIT.equipment_idx is
'设备idx主键';

comment on column E_SBJX_POINT_CHECK_OMIT.uncheck_date is
'停机开始时间';

comment on column E_SBJX_POINT_CHECK_OMIT.update_time is
'修改时间';