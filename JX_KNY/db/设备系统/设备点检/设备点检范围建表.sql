create table E_SBJX_POINT_CHECK_SCOPE 
(
   idx                VARCHAR2(32)         not null,
   class_code         VARCHAR2(20)         not null,
   class_name         VARCHAR2(20),
   class_name_py      VARCHAR2(30),
   check_content      VARCHAR2(50),
   check_content_py   VARCHAR2(50),
   seq_no             SMALLINT,
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_SBJX_POINT_CHECK_SCOPE primary key (idx)
);

comment on table E_SBJX_POINT_CHECK_SCOPE is
'点检范围，根据设备类型维护设备点检内容';

comment on column E_SBJX_POINT_CHECK_SCOPE.idx is
'主键';

comment on column E_SBJX_POINT_CHECK_SCOPE.class_code is
'设备类型编码';

comment on column E_SBJX_POINT_CHECK_SCOPE.class_name is
'设备类型名称';

comment on column E_SBJX_POINT_CHECK_SCOPE.class_name_py is
'设备类别名称首拼';

comment on column E_SBJX_POINT_CHECK_SCOPE.check_content is
'点检内容';

comment on column E_SBJX_POINT_CHECK_SCOPE.check_content_py is
'点检内容首拼';

comment on column E_SBJX_POINT_CHECK_SCOPE.seq_no is
'顺序号';

comment on column E_SBJX_POINT_CHECK_SCOPE.record_status is
'数据状态';

comment on column E_SBJX_POINT_CHECK_SCOPE.creator is
'创建人';

comment on column E_SBJX_POINT_CHECK_SCOPE.create_time is
'创建时间';

comment on column E_SBJX_POINT_CHECK_SCOPE.updator is
'修改人';

comment on column E_SBJX_POINT_CHECK_SCOPE.update_time is
'修改时间';

