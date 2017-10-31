create table e_repair_work_order_stuff 
(
   idx                VARCHAR2(32)         not null,
   repair_work_order_idx VARCHAR2(32)         not null,
   stuff_name         VARCHAR2(32)         default NULL,
   stuff_name_py      VARCHAR2(32)         default NULL,
   stuff_number       NUMBER(10,3),
   stuff_unit         VARCHAR2(10),
   stuff_unit_price   NUMBER(10,2),
   stuff_total_money  NUMBER(13,2),
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_REPAIR_WORK_ORDER_STUFF primary key (idx)
);

comment on table e_repair_work_order_stuff is
'检修用料';

comment on column e_repair_work_order_stuff.idx is
'主键';

comment on column e_repair_work_order_stuff.repair_work_order_idx is
'设备检修工单idx主键';

comment on column e_repair_work_order_stuff.stuff_name is
'材料名称规格';

comment on column e_repair_work_order_stuff.stuff_name_py is
'材料名称规格首拼';

comment on column e_repair_work_order_stuff.stuff_number is
'数量';

comment on column e_repair_work_order_stuff.stuff_unit is
'计量单位，如：克，千克，个，块等';

comment on column e_repair_work_order_stuff.stuff_unit_price is
'单价';

comment on column e_repair_work_order_stuff.stuff_total_money is
'金额';

comment on column e_repair_work_order_stuff.record_status is
'数据状态';

comment on column e_repair_work_order_stuff.creator is
'创建人';

comment on column e_repair_work_order_stuff.create_time is
'创建时间';

comment on column e_repair_work_order_stuff.updator is
'修改人';

comment on column e_repair_work_order_stuff.update_time is
'修改时间';
