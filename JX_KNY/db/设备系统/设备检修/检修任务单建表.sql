create table e_repair_task_list 
(
   idx                VARCHAR2(32)         not null,
   plan_month_idx     VARCHAR2(32)         not null,
   equipment_idx      VARCHAR2(32)         not null,
   repair_class_name  VARCHAR2(5),
   gz_sign_mac        VARCHAR2(10),
   gz_sign_elc        VARCHAR2(10),
   real_begin_time    DATE,
   real_end_time      DATE,
   begin_time         DATE,
   end_time           DATE,
   is_need_acceptance SMALLINT,
   acceptor_id        NUMBER(10,0),
   acceptor_name      VARCHAR2(15),
   acceptance_reviews VARCHAR2(100),
   user_id            NUMBER(10,0),
   user_name          VARCHAR2(15),
   acceptance_time    DATE,
   STATE                VARCHAR2(10),
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_REPAIR_TASK_LIST primary key (idx)
);

comment on table e_repair_task_list is
'检修任务单';

comment on column e_repair_task_list.idx is
'主键';

comment on column e_repair_task_list.plan_month_idx is
'月计划主键';

comment on column e_repair_task_list.equipment_idx is
'设备idx主键';

comment on column e_repair_task_list.repair_class_name is
'修别（小、中、项）';

comment on column e_repair_task_list.gz_sign_mac is
'机械工长签名';

comment on column e_repair_task_list.gz_sign_elc is
'电气工长签名';

comment on column e_repair_task_list.real_begin_time is
'实际开工时间';

comment on column e_repair_task_list.real_end_time is
'实际完工时间';

comment on column e_repair_task_list.begin_time is
'开工时间';

comment on column e_repair_task_list.end_time is
'完工时间';

comment on column e_repair_task_list.is_need_acceptance is
'是否需要验收，0：无需验收人验收、1：需要验收人验收';

comment on column e_repair_task_list.acceptor_id is
'验收人ID';

comment on column e_repair_task_list.acceptor_name is
'验收人';

comment on column e_repair_task_list.acceptance_reviews is
'验收评语';

comment on column e_repair_task_list.user_id is
'使用人ID，使用人（包机人）确认';

comment on column e_repair_task_list.user_name is
'使用人，使用人（包机人）确认';

comment on column e_repair_task_list.acceptance_time is
'是包机人签认时间';

comment on column e_repair_task_list.STATE is
'处理状态：未处理、已处理、工长已确认、已验收';

comment on column e_repair_task_list.record_status is
'数据状态';

comment on column e_repair_task_list.creator is
'创建人';

comment on column e_repair_task_list.create_time is
'创建时间';

comment on column e_repair_task_list.updator is
'修改人';

comment on column e_repair_task_list.update_time is
'修改时间';
