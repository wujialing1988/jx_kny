drop table K_CLASS_MAINTAIN cascade constraints;

/*==============================================================*/
/* Table: K_CLASS_MAINTAIN                                      */
/*==============================================================*/
create table K_CLASS_MAINTAIN  (
   IDX                  VARCHAR2(50 CHAR)               not null,
   WORKPLACE_CODE       VARCHAR2(50 CHAR),
   WORKPLACE_NAME       VARCHAR2(100 CHAR),
   CLASS_NO             VARCHAR2(50 CHAR),
   CLASS_NAME           VARCHAR2(100 CHAR),
   SEQ_NO               NUMBER(10),
   REMARK               VARCHAR2(1000 CHAR),
   RECORD_STATUS        NUMBER(1),
   CREATOR              NUMBER(18)                      not null,
   CREATE_TIME          DATE                            not null,
   UPDATOR              NUMBER(18)                      not null,
   UPDATE_TIME          DATE                            not null,
   constraint PK_K_CLASS_MAINTAIN primary key (IDX)
);

comment on table K_CLASS_MAINTAIN is
'班次维护';

comment on column K_CLASS_MAINTAIN.IDX is
'主键';

comment on column K_CLASS_MAINTAIN.WORKPLACE_CODE is
'工作地点标识码';

comment on column K_CLASS_MAINTAIN.WORKPLACE_NAME is
'工作地点名称';

comment on column K_CLASS_MAINTAIN.CLASS_NO is
'班次编码';

comment on column K_CLASS_MAINTAIN.CLASS_NAME is
'班次名称';

comment on column K_CLASS_MAINTAIN.SEQ_NO is
'排序号';

comment on column K_CLASS_MAINTAIN.REMARK is
'备注';

comment on column K_CLASS_MAINTAIN.RECORD_STATUS is
'记录状态，1：删除；0：未删除；';

comment on column K_CLASS_MAINTAIN.CREATOR is
'创建人';

comment on column K_CLASS_MAINTAIN.CREATE_TIME is
'创建时间';

comment on column K_CLASS_MAINTAIN.UPDATOR is
'修改人';

comment on column K_CLASS_MAINTAIN.UPDATE_TIME is
'修改时间';
