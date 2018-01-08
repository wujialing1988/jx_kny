-- Add/modify columns 
alter table JXGC_JOB_PROCESS_NODE_DEF add IS_LXNODE NUMBER(1);
-- Add comments to the columns 
comment on column JXGC_JOB_PROCESS_NODE_DEF.IS_LXNODE
  is '是否临修节点 ,0:否；1：是';
  
  -- Add/modify columns 
alter table JXGC_JOB_PROCESS_NODE add IS_LXNODE NUMBER(1);
-- Add comments to the columns 
comment on column JXGC_JOB_PROCESS_NODE.IS_LXNODE
  is '是否属于临修节点 ,0:是；1：否';
  
  
  -- Add/modify columns 
alter table JXGC_TRAIN_WORK_PLAN add DETAIN_IDX VARCHAR2(50 CHAR);
-- Add comments to the columns 
comment on column JXGC_TRAIN_WORK_PLAN.DETAIN_IDX
  is '扣车实体id';
  
  
