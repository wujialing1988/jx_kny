-- Add/modify columns 
alter table JCJX_RUNNING_KM modify TRAIN_TYPE_IDX VARCHAR2(50);
alter table JCJX_RUNNING_KM modify TRAIN_TYPE VARCHAR2(50);
alter table JCJX_RUNNING_KM modify TRAIN_NO VARCHAR2(10);


-- Add/modify columns 
alter table JCJX_RUNNING_KM_HISTORY modify TRAIN_TYPE_IDX VARCHAR2(50);
alter table JCJX_RUNNING_KM_HISTORY modify TRAIN_TYPE VARCHAR2(50);
alter table JCJX_RUNNING_KM_HISTORY modify TRAIN_NO VARCHAR2(10);

