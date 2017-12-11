-- Add/modify columns 
alter table ZB_ZBGL_PLAN_WORKER add QUEUE_NO VARCHAR2(50 CHAR);
alter table ZB_ZBGL_PLAN_WORKER add QUEUE_NAME VARCHAR2(50 CHAR);
alter table ZB_ZBGL_PLAN_WORKER add POSITION_NO VARCHAR2(50 CHAR);
alter table ZB_ZBGL_PLAN_WORKER add POSITION_NAME VARCHAR2(50 CHAR);
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN_WORKER.QUEUE_NO
  is '队列编码';
comment on column ZB_ZBGL_PLAN_WORKER.QUEUE_NAME
  is '队列名称';
comment on column ZB_ZBGL_PLAN_WORKER.POSITION_NO
  is '左右侧编码';
comment on column ZB_ZBGL_PLAN_WORKER.POSITION_NAME
  is '左右侧名称';
  
  
  -- Add/modify columns 
alter table K_CLASS_ORGANIZATION_USER add POSITION_NO VARCHAR2(50);
alter table K_CLASS_ORGANIZATION_USER add POSITION_NAME VARCHAR2(50);
-- Add comments to the columns 
comment on column K_CLASS_ORGANIZATION_USER.POSITION_NO
  is '左右侧编码';
comment on column K_CLASS_ORGANIZATION_USER.POSITION_NAME
  is '左右侧名称';
  
