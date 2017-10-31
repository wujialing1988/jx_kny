create table e_sbjx_repair_plan_month 
(
   idx                VARCHAR2(32)         not null,
   equipment_idx      VARCHAR2(32)         not null,
   plan_year          SMALLINT             not null,
   plan_month         SMALLINT             not null,
   repair_class       SMALLINT             not null,
   begin_time         DATE,
   end_time           DATE,
   plan_status        SMALLINT             not null,
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_SBJX_REPAIR_PLAN_MONTH primary key (idx)
);

comment on table e_sbjx_repair_plan_month is
'设备检修月计划，根据【设备检修年计划】生成【设备检修月计划】后，月计划不直接记录与年计划的关联关系。';

comment on column e_sbjx_repair_plan_month.idx is
'主键';

comment on column e_sbjx_repair_plan_month.equipment_idx is
'设备idx主键';

comment on column e_sbjx_repair_plan_month.plan_year is
'计划年度';

comment on column e_sbjx_repair_plan_month.plan_month is
'月份';

comment on column e_sbjx_repair_plan_month.repair_class is
'修程，1:小修、2：中修、3：项修';

comment on column e_sbjx_repair_plan_month.begin_time is
'计划开始时间';

comment on column e_sbjx_repair_plan_month.end_time is
'计划结束时间';

comment on column e_sbjx_repair_plan_month.plan_status is
'计划状态，0：未下发、1：已下发';

comment on column e_sbjx_repair_plan_month.record_status is
'数据状态';

comment on column e_sbjx_repair_plan_month.creator is
'创建人';

comment on column e_sbjx_repair_plan_month.create_time is
'创建时间';

comment on column e_sbjx_repair_plan_month.updator is
'修改人';

comment on column e_sbjx_repair_plan_month.update_time is
'修改时间';
