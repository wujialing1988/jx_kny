create table E_SBJX_POINT_CHECK 
(
   idx                VARCHAR2(32)         not null,
   equipment_idx      VARCHAR2(32)         not null,
   check_emp_id       NUMBER(10,0),
   check_emp          VARCHAR2(10),
   check_date         DATE,
   check_time         DATE,
   cal_running_time   FLOAT,
   running_time       FLOAT,
   equipment_state    VARCHAR2(10),
   state              VARCHAR2(10),
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_SBJX_POINT_CHECK primary key (idx)
);

comment on table E_SBJX_POINT_CHECK is
'设备点检任务单';

comment on column E_SBJX_POINT_CHECK.idx is
'主键';

comment on column E_SBJX_POINT_CHECK.equipment_idx is
'设备主键';

comment on column E_SBJX_POINT_CHECK.check_emp_id is
'点检人';

comment on column E_SBJX_POINT_CHECK.check_emp is
'点检人名称';

comment on column E_SBJX_POINT_CHECK.check_date is
'点检日期';

comment on column E_SBJX_POINT_CHECK.check_time is
'点检时间，该字段用于辅助计算“设备运转时间”的临时字段';

comment on column E_SBJX_POINT_CHECK.cal_running_time is
'设备运转时间，单位：小时，运转时间 = 点检结束时间 - 点检开始时间';

comment on column E_SBJX_POINT_CHECK.running_time is
'提交点检时，点检人手动输入的运转时间';

comment on column E_SBJX_POINT_CHECK.equipment_state is
'设备状态：运行、停止';

comment on column E_SBJX_POINT_CHECK.state is
'处理状态：未处理、已处理';

comment on column E_SBJX_POINT_CHECK.record_status is
'数据状态';

comment on column E_SBJX_POINT_CHECK.creator is
'创建人';

comment on column E_SBJX_POINT_CHECK.create_time is
'创建时间';

comment on column E_SBJX_POINT_CHECK.updator is
'修改人';

comment on column E_SBJX_POINT_CHECK.update_time is
'修改时间';
