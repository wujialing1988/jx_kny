-- Create table
create table ZB_ZBGL_PLAN
(
  IDX                 VARCHAR2(50 CHAR) not null,
  RAILWAY_TIME        VARCHAR2(50 CHAR),
  PLAN_START_TIME     DATE,
  PLAN_END_TIME       DATE,
  REAL_START_TIME     DATE,
  REAL_END_TIME       DATE,
  SITE_ID             VARCHAR2(50 CHAR),
  SITE_NAME           VARCHAR2(50 CHAR),
  TRACK_NO            VARCHAR2(20 CHAR),
  RDP_PLNA_STATUS     VARCHAR2(64),
  WORK_TEAM_ID        VARCHAR2(50 CHAR),
  WORK_TEAM_NAME      VARCHAR2(50 CHAR),
  RDP_NUM             NUMBER(2),
  WORK_TYPE           VARCHAR2(50 CHAR),
  WORK_NATURE         VARCHAR2(50 CHAR),
  RECORD_STATUS       NUMBER(1),
  CREATOR             NUMBER(18) not null,
  CREATE_TIME         DATE not null,
  UPDATOR             NUMBER(18) not null,
  UPDATE_TIME         DATE not null,
  WORK_TYPE_CODE      VARCHAR2(50 CHAR),
  WORK_NATURE_CODE    VARCHAR2(50 CHAR),
  WORK_TEAM_SEQ       VARCHAR2(50 CHAR),
  TRACK_NAME          VARCHAR2(50 CHAR),
  COME_DIRECTION_NO   VARCHAR2(50 CHAR),
  COME_DIRECTION_NAME VARCHAR2(50 CHAR),
  TO_DIRECTION_NO     VARCHAR2(50 CHAR),
  TO_DIRECTION_NAME   VARCHAR2(50 CHAR),
  CHECK_TIME          NUMBER(3),
  DAYNIGHT_TYPENO     VARCHAR2(50 CHAR),
  DAYNIGHT_TYPENAME   VARCHAR2(50 CHAR),
  CLASS_NO            VARCHAR2(50 CHAR),
  CLASS_NAME          VARCHAR2(50 CHAR)
);

-- Add comments to the table 
comment on table ZB_ZBGL_PLAN
  is '货车列检计划';
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN.IDX
  is 'idx主键';
comment on column ZB_ZBGL_PLAN.RAILWAY_TIME
  is '列车车次';
comment on column ZB_ZBGL_PLAN.PLAN_START_TIME
  is '计划开始时间';
comment on column ZB_ZBGL_PLAN.PLAN_END_TIME
  is '计划结束时间';
comment on column ZB_ZBGL_PLAN.REAL_START_TIME
  is '实际开始时间';
comment on column ZB_ZBGL_PLAN.REAL_END_TIME
  is '实际结束时间';
comment on column ZB_ZBGL_PLAN.SITE_ID
  is '站点ID';
comment on column ZB_ZBGL_PLAN.SITE_NAME
  is '站点名称';
comment on column ZB_ZBGL_PLAN.TRACK_NO
  is '停靠股道编码';
comment on column ZB_ZBGL_PLAN.RDP_PLNA_STATUS
  is '列检状态';
comment on column ZB_ZBGL_PLAN.WORK_TEAM_ID
  is '作业班组ID';
comment on column ZB_ZBGL_PLAN.WORK_TEAM_NAME
  is '作业班组名称';
comment on column ZB_ZBGL_PLAN.RDP_NUM
  is '计划列检车辆数量';
comment on column ZB_ZBGL_PLAN.WORK_TYPE
  is '作业方式 人工检查、动态检查、人机分工检查';
comment on column ZB_ZBGL_PLAN.WORK_NATURE
  is '作业性质 到达作业、始发作业、中转作业、通过作业';
comment on column ZB_ZBGL_PLAN.RECORD_STATUS
  is '表示此条记录的状态：0为表示未删除；1表示删除';
comment on column ZB_ZBGL_PLAN.CREATOR
  is '创建人';
comment on column ZB_ZBGL_PLAN.CREATE_TIME
  is '创建时间';
comment on column ZB_ZBGL_PLAN.UPDATOR
  is '修改人';
comment on column ZB_ZBGL_PLAN.UPDATE_TIME
  is '修改时间';
comment on column ZB_ZBGL_PLAN.WORK_TYPE_CODE
  is '作业方式 编码';
comment on column ZB_ZBGL_PLAN.WORK_NATURE_CODE
  is '作业性质 编码';
comment on column ZB_ZBGL_PLAN.WORK_TEAM_SEQ
  is '作业班组序列';
comment on column ZB_ZBGL_PLAN.TRACK_NAME
  is '停靠股道名称';
comment on column ZB_ZBGL_PLAN.COME_DIRECTION_NO
  is '接入方向编码 选列检所';
comment on column ZB_ZBGL_PLAN.COME_DIRECTION_NAME
  is '接入方向名称';
comment on column ZB_ZBGL_PLAN.TO_DIRECTION_NO
  is '发出方向编码 选列检所';
comment on column ZB_ZBGL_PLAN.TO_DIRECTION_NAME
  is '发出方向名称';
comment on column ZB_ZBGL_PLAN.CHECK_TIME
  is '技检时间（分钟）';
comment on column ZB_ZBGL_PLAN.DAYNIGHT_TYPENO
  is '白夜班编码';
comment on column ZB_ZBGL_PLAN.DAYNIGHT_TYPENAME
  is '白夜班名称';
comment on column ZB_ZBGL_PLAN.CLASS_NO
  is '班次编码';
comment on column ZB_ZBGL_PLAN.CLASS_NAME
  is '班次名称';
  
  -- Create/Recreate primary, unique and foreign key constraints 
alter table ZB_ZBGL_PLAN
  add constraint FK_ZB_ZBGL_PLAN primary key (IDX);
  
 -- Add/modify columns 
alter table ZB_ZBGL_PLAN add REMARK VARCHAR2(1000 CHAR);
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN.REMARK
  is '备注';
  
-- Add/modify columns 
alter table ZB_ZBGL_PLAN add T_VEHICLE_TYPE VARCHAR2(20);
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN.T_VEHICLE_TYPE
  is '客货类型';
  
 -- Add/modify columns 
alter table ZB_ZBGL_PLAN add TRAIN_DEMAND_IDX VARCHAR2(50 CHAR);
-- Add comments to the columns 
comment on column ZB_ZBGL_PLAN.TRAIN_DEMAND_IDX
  is '编组任务ID';
 

 