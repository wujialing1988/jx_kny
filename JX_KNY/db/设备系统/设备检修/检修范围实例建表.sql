create table e_repair_scope_case 
(
   idx                VARCHAR2(32)         not null,
   task_list_idx      VARCHAR2(32)         not null,
   scope_define_idx   VARCHAR2(32),
   sort_no            SMALLINT             default NULL,
   repair_item_name   VARCHAR2(500)        default NULL,
   repair_type        SMALLINT             default NULL,
   remark             VARCHAR2(100)        default NULL,
   STATE                VARCHAR2(10),
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_REPAIR_SCOPE_CASE primary key (idx)
);

comment on table e_repair_scope_case is
'检修范围实例（调度派工）';

comment on column e_repair_scope_case.idx is
'主键';

comment on column e_repair_scope_case.task_list_idx is
'任务单主键';

comment on column e_repair_scope_case.scope_define_idx is
'检修范围定义主键（e_repair_scope.idx）';

comment on column e_repair_scope_case.sort_no is
'序号';

comment on column e_repair_scope_case.repair_item_name is
'检修项目名称';

comment on column e_repair_scope_case.repair_type is
'检修类型，1：机型、2：电气、3：其它';

comment on column e_repair_scope_case.remark is
'备注';

comment on column e_repair_scope_case.STATE is
'处理状态：未处理、已处理';

comment on column e_repair_scope_case.record_status is
'数据状态';

comment on column e_repair_scope_case.creator is
'创建人';

comment on column e_repair_scope_case.create_time is
'创建时间';

comment on column e_repair_scope_case.updator is
'修改人';

comment on column e_repair_scope_case.update_time is
'修改时间';