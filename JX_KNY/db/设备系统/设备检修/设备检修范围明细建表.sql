create table E_REPAIR_SCOPE_DETAILS  (
   idx                VARCHAR2(32)                    not null,
   scope_idx          VARCHAR2(32)                    not null,
   seq_no             SMALLINT                       default NULL,
   work_content       VARCHAR2(500)                  default NULL,
   process_standard   VARCHAR2(500),
   remark             VARCHAR2(100)                  default NULL,
   record_status      SMALLINT                        not null,
   creator            NUMBER(10,0)                    not null,
   create_time        DATE                            not null,
   updator            NUMBER(10,0)                    not null,
   update_time        DATE                            not null,
   constraint PK_E_REPAIR_SCOPE_DETAILS primary key (idx)
);

comment on table E_REPAIR_SCOPE_DETAILS is
'设备检修范围明细-作业内容（工单）';

comment on column E_REPAIR_SCOPE_DETAILS.idx is
'主键';

comment on column E_REPAIR_SCOPE_DETAILS.scope_idx is
'范围主键';

comment on column E_REPAIR_SCOPE_DETAILS.seq_no is
'序号';

comment on column E_REPAIR_SCOPE_DETAILS.work_content is
'计划检修内容';

comment on column E_REPAIR_SCOPE_DETAILS.process_standard is
'工艺标准';

comment on column E_REPAIR_SCOPE_DETAILS.remark is
'备注';

comment on column E_REPAIR_SCOPE_DETAILS.record_status is
'数据状态';

comment on column E_REPAIR_SCOPE_DETAILS.creator is
'创建人';

comment on column E_REPAIR_SCOPE_DETAILS.create_time is
'创建时间';

comment on column E_REPAIR_SCOPE_DETAILS.updator is
'修改人';

comment on column E_REPAIR_SCOPE_DETAILS.update_time is
'修改时间';
