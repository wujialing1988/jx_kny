-- Add/modify columns 
alter table JXGC_TRAIN_WORK_PLAN add FROM_PERSONID NUMBER(18);
alter table JXGC_TRAIN_WORK_PLAN add FROM_PERSONNAME VARCHAR2(50 CHAR);
alter table JXGC_TRAIN_WORK_PLAN add TO_PERSONID NUMBER(18);
alter table JXGC_TRAIN_WORK_PLAN add TO_PERSONNAME VARCHAR2(50 CHAR);
alter table JXGC_TRAIN_WORK_PLAN add FROM_TIME date;
alter table JXGC_TRAIN_WORK_PLAN add FROM_REMARK VARCHAR2(1000 CHAR);
alter table JXGC_TRAIN_WORK_PLAN add FROM_STATUS NUMBER(1) default 0;
-- Add comments to the columns 
comment on column JXGC_TRAIN_WORK_PLAN.FROM_PERSONID
  is '交验交车人';
comment on column JXGC_TRAIN_WORK_PLAN.FROM_PERSONNAME
  is '交验交车人名称';
comment on column JXGC_TRAIN_WORK_PLAN.TO_PERSONID
  is '交验接车人';
comment on column JXGC_TRAIN_WORK_PLAN.TO_PERSONNAME
  is '交验接车人名称';
comment on column JXGC_TRAIN_WORK_PLAN.FROM_TIME
  is '交验时间';
comment on column JXGC_TRAIN_WORK_PLAN.FROM_REMARK
  is '校验情况描述';
comment on column JXGC_TRAIN_WORK_PLAN.FROM_STATUS
  is '校验状态：0为未校验；1表示已校验';
