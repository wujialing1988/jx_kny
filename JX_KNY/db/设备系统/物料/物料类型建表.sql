create table e_wlgl_stuff_class 
(
   idx                VARCHAR2(32)         not null,
   stuff_name         VARCHAR2(32)         default NULL,
   stuff_name_py      VARCHAR2(32)         default NULL,
   stuff_unit         VARCHAR2(10),
   stuff_unit_price   NUMBER(10,2),
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_e_wlgl_stuff_class primary key (idx)
);

comment on table e_wlgl_stuff_class is
'物料类型';

comment on column e_wlgl_stuff_class.idx is
'主键';

comment on column e_wlgl_stuff_class.stuff_name is
'材料名称规格';

comment on column e_wlgl_stuff_class.stuff_name_py is
'材料名称规格首拼';

comment on column e_wlgl_stuff_class.stuff_unit is
'计量单位，如：克，千克，个，块等';

comment on column e_wlgl_stuff_class.stuff_unit_price is
'单价';

comment on column e_wlgl_stuff_class.record_status is
'数据状态';

comment on column e_wlgl_stuff_class.creator is
'创建人';

comment on column e_wlgl_stuff_class.create_time is
'创建时间';

comment on column e_wlgl_stuff_class.updator is
'修改人';

comment on column e_wlgl_stuff_class.update_time is
'修改时间';
