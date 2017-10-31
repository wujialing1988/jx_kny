create table e_sbjx_repair_period  (
   idx                VARCHAR2(32)                    not null,
   class_name         VARCHAR2(30),
   class_code         VARCHAR2(30),
   dx                 SMALLINT,
   zx                 SMALLINT,
   xx                 SMALLINT,
   adx                SMALLINT,
   azx                SMALLINT,
   axx                SMALLINT,
   record_status      SMALLINT                        not null,
   creator            NUMBER(10,0)                    not null,
   create_time        DATE                            not null,
   updator            NUMBER(10,0)                    not null,
   update_time        DATE                            not null,
   constraint PK_e_sbjx_repair_period primary key (idx)
);

comment on table e_sbjx_repair_period is
'设备检修周期';

comment on column e_sbjx_repair_period.idx is
'主键';

comment on column e_sbjx_repair_period.class_name is
'设备类别名称';

comment on column e_sbjx_repair_period.class_code is
'设备类别编码';

comment on column e_sbjx_repair_period.dx is
'大修(年)';

comment on column e_sbjx_repair_period.zx is
'中修间隔(小修次)';

comment on column e_sbjx_repair_period.xx is
'小修周期(月)';

comment on column e_sbjx_repair_period.adx is
'A类大修(年)';

comment on column e_sbjx_repair_period.azx is
'A类中修间隔(小修次)';

comment on column e_sbjx_repair_period.axx is
'A类小修周期(月)';

comment on column e_sbjx_repair_period.record_status is
'数据状态';

comment on column e_sbjx_repair_period.creator is
'创建人';

comment on column e_sbjx_repair_period.create_time is
'创建时间';

comment on column e_sbjx_repair_period.updator is
'修改人';

comment on column e_sbjx_repair_period.update_time is
'修改时间';
