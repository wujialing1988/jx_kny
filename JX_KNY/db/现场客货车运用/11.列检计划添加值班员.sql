-- Add/modify columns 
alter table ZB_ZBGL_PLAN add DUTY_EMPID VARCHAR2(50 CHAR);
alter table ZB_ZBGL_PLAN add DUTY_EMPNAME VARCHAR2(50 CHAR);
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN.DUTY_EMPID
  is '值班员id';
comment on column ZB_ZBGL_PLAN.DUTY_EMPNAME
  is '值班员名称';
