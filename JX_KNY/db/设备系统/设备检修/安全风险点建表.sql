create table E_REPAIR_SCOPE_RISK_WARNING  (
   idx                VARCHAR2(32)                    not null,
   scope_idx          VARCHAR2(32)                    not null,
   risk_item          VARCHAR2(300)                  default NULL,
   record_status      SMALLINT                        not null,
   creator            NUMBER(10,0)                    not null,
   create_time        DATE                            not null,
   updator            NUMBER(10,0)                    not null,
   update_time        DATE                            not null,
   constraint PK_E_REPAIR_SCOPE_RISK_WARNING primary key (idx)
);

comment on table E_REPAIR_SCOPE_RISK_WARNING is
'安全风险点';

comment on column E_REPAIR_SCOPE_RISK_WARNING.idx is
'主键';

comment on column E_REPAIR_SCOPE_RISK_WARNING.scope_idx is
'范围主键';

comment on column E_REPAIR_SCOPE_RISK_WARNING.risk_item is
'风险点';

comment on column E_REPAIR_SCOPE_RISK_WARNING.record_status is
'数据状态';

comment on column E_REPAIR_SCOPE_RISK_WARNING.creator is
'创建人';

comment on column E_REPAIR_SCOPE_RISK_WARNING.create_time is
'创建时间';

comment on column E_REPAIR_SCOPE_RISK_WARNING.updator is
'修改人';

comment on column E_REPAIR_SCOPE_RISK_WARNING.update_time is
'修改时间';