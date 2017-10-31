create table E_INSPECT_SCOPE 
(
   IDX                  VARCHAR2(32)         not null,
   CLASS_CODE           VARCHAR2(30),
   CLASS_NAME           VARCHAR2(30),
   CLASS_NAME_PY        VARCHAR2(30),
   REPAIR_TYPE          SMALLINT             default NULL,
   CHECK_ITEM           VARCHAR2(200)        not null,
   CHECK_ITEM_PY        VARCHAR2(200),
   CHECK_STANDARD       VARCHAR2(200),
   SEQ_NO               SMALLINT,
   REMARKS              VARCHAR2(200),
   RECORD_STATUS        NUMBER(1,0)          not null,
   CREATOR              NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   UPDATOR              NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null,
   constraint PK_E_INSPECT_SCOPE primary key (IDX)
);

comment on table E_INSPECT_SCOPE is
'巡检项目（原巡检范围）';

comment on column E_INSPECT_SCOPE.IDX is
'idx主键';

comment on column E_INSPECT_SCOPE.CLASS_CODE is
'设备类别编码';

comment on column E_INSPECT_SCOPE.CLASS_NAME is
'设备类别名称';

comment on column E_INSPECT_SCOPE.CLASS_NAME_PY is
'设备类别名称首拼';

comment on column E_INSPECT_SCOPE.REPAIR_TYPE is
'检修类型，1：机械、2：电气、3：其它';

comment on column E_INSPECT_SCOPE.CHECK_ITEM is
'检查项目';

comment on column E_INSPECT_SCOPE.CHECK_ITEM_PY is
'检查项目首拼（用于根据首字母进行快速检索）';

comment on column E_INSPECT_SCOPE.CHECK_STANDARD is
'检查标准';

comment on column E_INSPECT_SCOPE.SEQ_NO is
'顺序号';

comment on column E_INSPECT_SCOPE.REMARKS is
'备注';

comment on column E_INSPECT_SCOPE.RECORD_STATUS is
'记录状态';

comment on column E_INSPECT_SCOPE.CREATOR is
'创建人';

comment on column E_INSPECT_SCOPE.CREATE_TIME is
'创建时间';

comment on column E_INSPECT_SCOPE.UPDATOR is
'更新人';

comment on column E_INSPECT_SCOPE.UPDATE_TIME is
'更新时间';
