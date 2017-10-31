
/*==============================================================*/
/* Table: "e_equipment_class_to_repair_tm"                      */
/*==============================================================*/
create table "e_equipment_class_to_repair_tm" 
(
   "idx"                VARCHAR2(32)         not null,
   "repair_type"        SMALLINT             default NULL,
   "class_code"         VARCHAR2(32)         default NULL,
   "specification"      VARCHAR2(32)         default NULL,
   "tmpl_idx"           VARCHAR2(32)         not null,
   "remark"             VARCHAR2(100)        default NULL,
   "record_status"      SMALLINT             not null,
   "creator"            NUMBER(10,0)         not null,
   "create_time"        DATE                 not null,
   "updator"            NUMBER(10,0)         not null,
   "update_time"        DATE                 not null,
   constraint PK_E_EQUIPMENT_CLASS_TO_REPAIR primary key ("idx")
);

comment on table "e_equipment_class_to_repair_tm" is
'设备类别对应检修模板';

comment on column "e_equipment_class_to_repair_tm"."idx" is
'主键';

comment on column "e_equipment_class_to_repair_tm"."repair_type" is
'检修类型（小修、中修、大修）';

comment on column "e_equipment_class_to_repair_tm"."class_code" is
'类别编号';

comment on column "e_equipment_class_to_repair_tm"."specification" is
'规格型号';

comment on column "e_equipment_class_to_repair_tm"."tmpl_idx" is
'模板主键';

comment on column "e_equipment_class_to_repair_tm"."remark" is
'备注';

comment on column "e_equipment_class_to_repair_tm"."record_status" is
'数据状态';

comment on column "e_equipment_class_to_repair_tm"."creator" is
'创建人';

comment on column "e_equipment_class_to_repair_tm"."create_time" is
'创建时间';

comment on column "e_equipment_class_to_repair_tm"."updator" is
'修改人';

comment on column "e_equipment_class_to_repair_tm"."update_time" is
'修改时间';
