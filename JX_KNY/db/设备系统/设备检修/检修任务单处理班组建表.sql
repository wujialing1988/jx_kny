create table e_repair_task_list_team 
(
   idx                VARCHAR2(32)         not null,
   task_list_idx      VARCHAR2(32)         not null,
   repair_type        SMALLINT             default NULL,
   org_id             VARCHAR2(50),
   org_name           VARCHAR2(50),
   is_confirmed       SMALLINT,
   real_begin_time    DATE,
   real_end_time      DATE,
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_REPAIR_TASK_LIST_TEAM primary key (idx)
);

comment on table e_repair_task_list_team is
'检修任务单处理班组';

comment on column e_repair_task_list_team.idx is
'主键';

comment on column e_repair_task_list_team.task_list_idx is
'任务单主键';

comment on column e_repair_task_list_team.repair_type is
'检修类型，1：机械、2：电气、3：其它';

comment on column e_repair_task_list_team.org_id is
'班组id，多个班组以英文逗号“,”进行分隔';

comment on column e_repair_task_list_team.org_name is
'班组名称，多个班组以英文逗号“,”进行分隔';

comment on column e_repair_task_list_team.is_confirmed is
'工长确认，0：未确认、1：已确认、2：待确认';

comment on column e_repair_task_list_team.real_begin_time is
'开工日期，是施修人初次扫码施修时间';

comment on column e_repair_task_list_team.real_end_time is
'竣工日期，是包修工长签认时间';
