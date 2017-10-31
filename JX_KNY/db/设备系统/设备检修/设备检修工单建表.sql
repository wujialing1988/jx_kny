create table e_repair_work_order 
(
   idx                VARCHAR2(32)         not null,
   scope_case_idx     VARCHAR2(32)         not null,
   define_idx         VARCHAR2(32),
   sort_no            SMALLINT             default NULL,
   work_content       VARCHAR2(500)        default NULL,
   process_standard   VARCHAR2(500),
   worker_id          NUMBER(10,0),
   worker_name        VARCHAR2(15),
   other_worker_name  VARCHAR2(100),
   process_time       DATE,
   repair_record      VARCHAR2(100),
   remark             VARCHAR2(100)        default NULL,
   order_status       SMALLINT             default 1,
   record_status      SMALLINT             not null,
   creator            NUMBER(10,0)         not null,
   create_time        DATE                 not null,
   updator            NUMBER(10,0)         not null,
   update_time        DATE                 not null,
   constraint PK_E_REPAIR_WORK_ORDER primary key (idx)
);

comment on table e_repair_work_order is
'设备检修工单';

comment on column e_repair_work_order.idx is
'主键';

comment on column e_repair_work_order.scope_case_idx is
'检修范围实例主键';

comment on column e_repair_work_order.define_idx is
'检修范围明细定义主键（e_repair_scope_details.idx）';

comment on column e_repair_work_order.sort_no is
'序号';

comment on column e_repair_work_order.work_content is
'计划检修内容';

comment on column e_repair_work_order.process_standard is
'工艺标准';

comment on column e_repair_work_order.worker_id is
'作业人员ID';

comment on column e_repair_work_order.worker_name is
'作业人员';

comment on column e_repair_work_order.other_worker_name is
'其他作业人员';

comment on column e_repair_work_order.process_time is
'处理时间';

comment on column e_repair_work_order.repair_record is
'实修记录';

comment on column e_repair_work_order.remark is
'备注';

comment on column e_repair_work_order.order_status is
'工单状态，1：未处理，3：已处理';

comment on column e_repair_work_order.record_status is
'数据状态';

comment on column e_repair_work_order.creator is
'创建人';

comment on column e_repair_work_order.create_time is
'创建时间';

comment on column e_repair_work_order.updator is
'修改人';

comment on column e_repair_work_order.update_time is
'修改时间';
