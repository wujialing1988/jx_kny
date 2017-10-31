/*==============================================================*/
/* Table: E_REPAIR_SCOPE                                        */
/*==============================================================*/
create table E_REPAIR_SCOPE  (
   idx                VARCHAR2(32)                    not null,
   seq_no             SMALLINT                       default NULL,
   class_code         VARCHAR2(30),
   class_name         VARCHAR2(30),
   class_name_py      VARCHAR2(30),
   repair_type        SMALLINT                       default NULL,
   repair_scope_name  VARCHAR2(500)                  default NULL,
   repair_class_small SMALLINT,
   repair_class_medium SMALLINT,
   repair_class_subject SMALLINT,
   remark             VARCHAR2(500)                  default NULL,
   record_status      SMALLINT                        not null,
   creator            NUMBER(10,0)                    not null,
   create_time        DATE                            not null,
   updator            NUMBER(10,0)                    not null,
   update_time        DATE                            not null,
   constraint PK_E_REPAIR_SCOPE primary key (idx)
);

comment on table E_REPAIR_SCOPE is
'，对于同类型不同设备可能存在设备检修范围不同的情况，修改classCode可以存储设备编号，以实现该功能（Modified by hetao on 2017-02-16）';

comment on column E_REPAIR_SCOPE.idx is
'主键';

comment on column E_REPAIR_SCOPE.seq_no is
'顺序号';

comment on column E_REPAIR_SCOPE.class_code is
'设备类别编码，对于同类型不同设备可能存在设备检修范围不同的情况，修改classCode可以存储设备编号，以实现该功能（Modified by hetao on 2017-02-16）';

comment on column E_REPAIR_SCOPE.class_name is
'设备类别名称';

comment on column E_REPAIR_SCOPE.class_name_py is
'设备类别名称首拼';

comment on column E_REPAIR_SCOPE.repair_type is
'检修类型，1：机型、2：电气、3：其它';

comment on column E_REPAIR_SCOPE.repair_scope_name is
'检修项目名称';

comment on column E_REPAIR_SCOPE.repair_class_small is
'小修，1：表示包含、0：不包含';

comment on column E_REPAIR_SCOPE.repair_class_medium is
'中修，1：表示包含、0：不包含';

comment on column E_REPAIR_SCOPE.repair_class_subject is
'项修，1：表示包含、0：不包含';

comment on column E_REPAIR_SCOPE.remark is
'备注';

comment on column E_REPAIR_SCOPE.record_status is
'数据状态';

comment on column E_REPAIR_SCOPE.creator is
'创建人';

comment on column E_REPAIR_SCOPE.create_time is
'创建时间';

comment on column E_REPAIR_SCOPE.updator is
'修改人';

comment on column E_REPAIR_SCOPE.update_time is
'修改时间';
