create table SCHEDULE_PROPOSE_AND_APPROVE 
(
   IDX                  VARCHAR(32)          not null,
   TOPIC                VARCHAR(100),
   "ORDER"                VARCHAR(20),
   CONTENT              VARCHAR(200),
   PROPOSER_ID          VARCHAR(10),
   PROPOSER_NAME        VARCHAR(10),
   APPROVER_ID          VARCHAR(10),
   APPROVER_NAME        VARCHAR(10),
   PROPOSE_TIME         DATE,
   APPROVE_TIME         DATE,
   APPROVAL_OPINION     VARCHAR(100),
   STATUS               VARCHAR(3),
   constraint PK_SCHEDULE_PROPOSE_AND_APPROV primary key (IDX)
);

comment on table SCHEDULE_PROPOSE_AND_APPROVE is
'调度命令单 申请-审批 简单流程';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.IDX is
'主键';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.TOPIC is
'主题';

comment on column SCHEDULE_PROPOSE_AND_APPROVE."ORDER" is
'命令号';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.CONTENT is
'内容';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.PROPOSER_ID is
'申请人ID';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.PROPOSER_NAME is
'申请人姓名';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.APPROVER_ID is
'审批人ID';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.APPROVER_NAME is
'审批人姓名';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.PROPOSE_TIME is
'申请时间';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.APPROVE_TIME is
'审批时间';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.APPROVAL_OPINION is
'审批意见';

comment on column SCHEDULE_PROPOSE_AND_APPROVE.STATUS is
'命令单状态';
