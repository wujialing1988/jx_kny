drop table K_TRAIN_STATUS_CHANGE cascade constraints;

/*==============================================================*/
/* Table: K_TRAIN_STATUS_CHANGE                                 */
/*==============================================================*/
create table K_TRAIN_STATUS_CHANGE  (
   IDX                  VARCHAR2(32)                    not null,
   TRAIN_IDX            VARCHAR2(50),
   TRAIN_TYPE_IDX       VARCHAR2(50),
   TRAIN_TYPE_SHORTNAME VARCHAR2(50),
   TRAIN_NO             VARCHAR2(10),
   T_VEHICLE_TYPE       VARCHAR2(20),
   TRAIN_STATE          NUMBER(2),
   RECORD_TIME          DATE,
   BUSINESS_IDX         VARCHAR2(50),
   BUSINESS_NAME        VARCHAR2(50),
   constraint PK_K_TRAIN_STATUS_CHANGE primary key (IDX)
);

comment on table K_TRAIN_STATUS_CHANGE is
'车辆状态流转
';

comment on column K_TRAIN_STATUS_CHANGE.IDX is
'主键';

comment on column K_TRAIN_STATUS_CHANGE.TRAIN_IDX is
'车辆主键';

comment on column K_TRAIN_STATUS_CHANGE.TRAIN_TYPE_IDX is
'车型ID';

comment on column K_TRAIN_STATUS_CHANGE.TRAIN_TYPE_SHORTNAME is
'车型';

comment on column K_TRAIN_STATUS_CHANGE.TRAIN_NO is
'车号';

comment on column K_TRAIN_STATUS_CHANGE.T_VEHICLE_TYPE is
'客货类型 10 货车 20 客车';

comment on column K_TRAIN_STATUS_CHANGE.TRAIN_STATE is
'状态';

comment on column K_TRAIN_STATUS_CHANGE.RECORD_TIME is
'记录时间';

comment on column K_TRAIN_STATUS_CHANGE.BUSINESS_IDX is
'业务ID';

comment on column K_TRAIN_STATUS_CHANGE.BUSINESS_NAME is
'业务环节';
