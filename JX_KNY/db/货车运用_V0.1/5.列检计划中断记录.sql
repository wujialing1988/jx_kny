create table ZB_ZBGL_PLAN_INTERRUPT
(
  IDX              VARCHAR2(50 CHAR) not null,
  RDP_PLAN_IDX     VARCHAR2(50 CHAR),
  INTERRUPT_START_TIME    DATE,
  INTERRUPT_END_TIME      DATE,
  RECORD_STATUS    NUMBER(1),
  CREATOR          NUMBER(18) not null,
  CREATE_TIME      DATE not null,
  UPDATOR          NUMBER(18) not null,
  UPDATE_TIME      DATE not null
);

-- Add comments to the table 
comment on table ZB_ZBGL_PLAN_INTERRUPT
  is '列检作业车辆计划';
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN_INTERRUPT.IDX
  is 'idx主键';
comment on column ZB_ZBGL_PLAN_INTERRUPT.RDP_PLAN_IDX
  is '列检计划';
 comment on column ZB_ZBGL_PLAN_INTERRUPT.INTERRUPT_START_TIME
  is '中断开始时间';
 comment on column ZB_ZBGL_PLAN_INTERRUPT.INTERRUPT_END_TIME
  is '中断结束时间';
comment on column ZB_ZBGL_PLAN_INTERRUPT.RECORD_STATUS
  is '表示此条记录的状态：0为表示未删除；1表示删除';
comment on column ZB_ZBGL_PLAN_INTERRUPT.CREATOR
  is '创建人';
comment on column ZB_ZBGL_PLAN_INTERRUPT.CREATE_TIME
  is '创建时间';
comment on column ZB_ZBGL_PLAN_INTERRUPT.UPDATOR
  is '修改人';
comment on column ZB_ZBGL_PLAN_INTERRUPT.UPDATE_TIME
  is '修改时间';
  
 alter table ZB_ZBGL_PLAN_INTERRUPT
  add constraint PK_ZB_ZBGL_PLAN_INTERRUPT primary key (IDX);