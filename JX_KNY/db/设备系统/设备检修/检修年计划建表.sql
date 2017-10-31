create table e_sbjx_repair_plan_year 
(
   idx                VARCHAR2(32)         not null,
   equipment_idx      VARCHAR2(32)         not null,
   plan_year          SMALLINT             not null,
   month_1            SMALLINT             default -1,
   month_2            SMALLINT             default -1,
   month_3            SMALLINT             default -1,
   month_4            SMALLINT             default -1,
   month_5            SMALLINT             default -1,
   month_6            SMALLINT             default -1,
   month_7            SMALLINT             default -1,
   month_8            SMALLINT             default -1,
   month_9            SMALLINT             default -1,
   month_10           SMALLINT             default -1,
   month_11           SMALLINT             default -1,
   month_12           SMALLINT             default -1,
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_e_sbjx_repair_plan_year primary key (idx)
);

comment on table e_sbjx_repair_plan_year is
'设备检修设备年计划';

comment on column e_sbjx_repair_plan_year.idx is
'主键';

comment on column e_sbjx_repair_plan_year.equipment_idx is
'设备主键';

comment on column e_sbjx_repair_plan_year.plan_year is
'计划年度';

comment on column e_sbjx_repair_plan_year.month_1 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_2 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_3 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_4 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_5 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_6 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_7 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_8 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_9 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_10 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_11 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.month_12 is
'修程，-1:未设置、1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30';

comment on column e_sbjx_repair_plan_year.record_status is
'数据状态';

comment on column e_sbjx_repair_plan_year.creator is
'创建人';

comment on column e_sbjx_repair_plan_year.create_time is
'创建时间';

comment on column e_sbjx_repair_plan_year.updator is
'修改人';

comment on column e_sbjx_repair_plan_year.update_time is
'修改时间';
