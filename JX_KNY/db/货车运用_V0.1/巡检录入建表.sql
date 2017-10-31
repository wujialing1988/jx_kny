create table TRAIN_INSPECTINFO_RECORD 
(
   IDX                VARCHAR(32)          not null,
   TRAIN_IDX          VARCHAR(32),
   TRAIN_NO           VARCHAR(8),
   PERSON_RESPONSIBLE VARCHAR(10),
   PERSON_RESPONSIBLE_ID VARCHAR(32),
   INSPECT_DETAIL     VARCHAR(200),
   INSPECT_TYPE       VARCHAR(10),
   RECORDTIME         DATE,
   RECORD_PERSON      VARCHAR(10),
   RECORD_PERSON_ID   VARCHAR(32),
   T_VEHICLE_TYPE       VARCHAR(2),
   RECORD_STATUS   NUMBER(1),
   CREATOR         NUMBER(18) not null,
   CREATE_TIME     DATE not null,
   UPDATOR         NUMBER(18) not null,
   UPDATE_TIME     DATE not null,
   constraint PK_TRAIN_INSPECTINFO_RECORD primary key (IDX)
);

comment on column TRAIN_INSPECTINFO_RECORD.IDX is
'主键';

comment on column TRAIN_INSPECTINFO_RECORD.TRAIN_IDX is
'车辆主键';

comment on column TRAIN_INSPECTINFO_RECORD.TRAIN_NO is
'车辆车号';

comment on column TRAIN_INSPECTINFO_RECORD.PERSON_RESPONSIBLE is
'责任人';

comment on column TRAIN_INSPECTINFO_RECORD.PERSON_RESPONSIBLE_ID is
'责任人ID';

comment on column TRAIN_INSPECTINFO_RECORD.INSPECT_DETAIL is
'巡检问题描述';

comment on column TRAIN_INSPECTINFO_RECORD.INSPECT_TYPE is
'巡检类别';

comment on column TRAIN_INSPECTINFO_RECORD.RECORDTIME is
'录入时间';

comment on column TRAIN_INSPECTINFO_RECORD.RECORD_PERSON is
'录入人';

comment on column TRAIN_INSPECTINFO_RECORD.RECORD_PERSON_ID is
'录入人ID';

comment on column TRAIN_INSPECTINFO_RECORD.T_VEHICLE_TYPE is
'客车货车标志';

comment on column TRAIN_INSPECTINFO_RECORD.RECORD_STATUS  is
'记录状态，1：删除；0：未删除；';

comment on column TRAIN_INSPECTINFO_RECORD.CREATOR  is 
'创建人';

comment on column TRAIN_INSPECTINFO_RECORD.CREATE_TIME  is
 '创建时间';
 
comment on column TRAIN_INSPECTINFO_RECORD.UPDATOR  is 
'修改人';

comment on column TRAIN_INSPECTINFO_RECORD.UPDATE_TIME  is 
'修改时间';
