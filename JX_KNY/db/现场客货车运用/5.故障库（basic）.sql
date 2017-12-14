-- basic 中执行
-- Add/modify columns 
alter table JC_DZ_EQUIP_FAULT add FAULT_TYPE_NAME VARCHAR2(100);
-- Add comments to the columns 
comment on column JC_DZ_EQUIP_FAULT.FAULT_TYPE_NAME
  is '故障类型名称';
  
  -- Add/modify columns 
alter table JC_DZ_EQUIP_FAULT modify FAULT_TYPE_ID VARCHAR2(50);

  
  -- CoreFrame中执行
  CREATE OR REPLACE VIEW J_JCGY_EQUIP_FAULT
(fault_id, fault_name, fault_type_id,FAULT_TYPE_NAME)
AS
SELECT id, fault_name, fault_type_id,FAULT_TYPE_NAME
     FROM basic.jc_dz_equip_fault
    WHERE state = 1;
    
-- Add/modify columns 
alter table T_JCBM_JCXTFL_FAULT add FAULT_TYPE_ID VARCHAR2(50 CHAR);
alter table T_JCBM_JCXTFL_FAULT add FAULT_TYPE_NAME VARCHAR2(500 CHAR);
-- Add comments to the columns 
comment on column T_JCBM_JCXTFL_FAULT.FAULT_TYPE_ID
  is '故障类别编码';
comment on column T_JCBM_JCXTFL_FAULT.FAULT_TYPE_NAME
  is '故障类别名称';


-- Add/modify columns 
alter table K_GZTP add FAULT_ID VARCHAR2(50 CHAR);
-- Add comments to the columns 
comment on column K_GZTP.FAULT_ID
  is '故障编号';

  