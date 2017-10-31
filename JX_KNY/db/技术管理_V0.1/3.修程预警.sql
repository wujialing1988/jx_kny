drop table K_REPAIR_CLASS_WARNING cascade constraints;

/*==============================================================*/
/* Table: K_REPAIR_CLASS_WARNING                                */
/*==============================================================*/
create table K_REPAIR_CLASS_WARNING  (
   IDX                  VARCHAR2(32)                    not null,
   TRAIN_TYPE_IDX       VARCHAR2(50),
   TRAIN_TYPE           VARCHAR2(50),
   TRAIN_NO             VARCHAR2(10),
   REPAIR_CLASS         VARCHAR2(10),
   REPAIR_CLASS_NAME    VARCHAR2(10),
   REPAIR_ORDER         VARCHAR2(10),
   REPAIR_ORDER_NAME    VARCHAR2(10),
   LAST_WARNING_DATE    DATE,
   IS_END               NUMBER(1),
   END_TYPE             VARCHAR2(10),
   T_VEHICLE_TYPE       VARCHAR2(20),
   START_TIME           DATE,
   END_TIME             DATE,
   LIMIT_VALUE          NUMBER(10,2),
   constraint PK_K_REPAIR_CLASS_WARNING primary key (IDX)
);

comment on table K_REPAIR_CLASS_WARNING is
'修程预警';

comment on column K_REPAIR_CLASS_WARNING.IDX is
'主键';

comment on column K_REPAIR_CLASS_WARNING.TRAIN_TYPE_IDX is
'车型ID';

comment on column K_REPAIR_CLASS_WARNING.TRAIN_TYPE is
'车型';

comment on column K_REPAIR_CLASS_WARNING.TRAIN_NO is
'车号';

comment on column K_REPAIR_CLASS_WARNING.REPAIR_CLASS is
'修程';

comment on column K_REPAIR_CLASS_WARNING.REPAIR_CLASS_NAME is
'修程名称';

comment on column K_REPAIR_CLASS_WARNING.REPAIR_ORDER is
'修次';

comment on column K_REPAIR_CLASS_WARNING.REPAIR_ORDER_NAME is
'修次名称';

comment on column K_REPAIR_CLASS_WARNING.LAST_WARNING_DATE is
'最近预警时间';

comment on column K_REPAIR_CLASS_WARNING.IS_END is
'是否终止(0为表示未终止；1表示终止)';

comment on column K_REPAIR_CLASS_WARNING.END_TYPE is
'终止方式 10 人为终止 20 自动终止';

comment on column K_REPAIR_CLASS_WARNING.T_VEHICLE_TYPE is
'客货类型 10 货车 20 客车';

comment on column K_REPAIR_CLASS_WARNING.START_TIME is
'预警生成时间';

comment on column K_REPAIR_CLASS_WARNING.END_TIME is
'预警终止时间';

comment on column K_REPAIR_CLASS_WARNING.LIMIT_VALUE is
'临近或超限值（最大限减去当前累计公里或天数）';
