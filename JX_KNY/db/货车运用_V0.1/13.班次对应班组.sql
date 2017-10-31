create table K_CLASS_ORGANIZATION  (
   IDX                  VARCHAR2(50 CHAR)               not null,
   CLASS_IDX            VARCHAR2(50 CHAR),
   ORGID                NUMBER(10),
   ORGNAME              VARCHAR2(64),
   ORGSEQ               VARCHAR2(512),
   constraint PK_K_CLASS_ORGANIZATION primary key (IDX)
);

comment on table K_CLASS_ORGANIZATION is
'班次班组维护';

comment on column K_CLASS_ORGANIZATION.IDX is
'主键';

comment on column K_CLASS_ORGANIZATION.CLASS_IDX is
'班次主键';

comment on column K_CLASS_ORGANIZATION.ORGID is
'班组ID';

comment on column K_CLASS_ORGANIZATION.ORGNAME is
'班组名称';

comment on column K_CLASS_ORGANIZATION.ORGSEQ is
'班组序列';