create table E_SBJX_POINT_CHECK_CONTENT 
(
   idx                VARCHAR2(32)         not null,
   point_check_idx    VARCHAR2(32)         not null,
   check_content      VARCHAR2(100),
   technology_state_flag VARCHAR2(10),
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_SBJX_POINT_CHECK_CONTENT primary key (idx)
);

comment on table E_SBJX_POINT_CHECK_CONTENT is
'设备点检内容';

comment on column E_SBJX_POINT_CHECK_CONTENT.idx is
'主键';

comment on column E_SBJX_POINT_CHECK_CONTENT.point_check_idx is
'设备点检任务单idx主键';

comment on column E_SBJX_POINT_CHECK_CONTENT.check_content is
'点检内容';

comment on column E_SBJX_POINT_CHECK_CONTENT.technology_state_flag is
'技术状态标志：良好、不良、待修、修理';

comment on column E_SBJX_POINT_CHECK_CONTENT.record_status is
'数据状态';

comment on column E_SBJX_POINT_CHECK_CONTENT.creator is
'创建人';

comment on column E_SBJX_POINT_CHECK_CONTENT.create_time is
'创建时间';

comment on column E_SBJX_POINT_CHECK_CONTENT.updator is
'修改人';

comment on column E_SBJX_POINT_CHECK_CONTENT.update_time is
'修改时间';
