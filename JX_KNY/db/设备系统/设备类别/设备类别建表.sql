/*==============================================================*/
/* Table: e_equipment_classes                                   */
/*==============================================================*/
create table e_equipment_classes  (
   IDX                  VARCHAR2(32)                    not null,
   CLASS_CODE           VARCHAR2(30)                   default NULL,
   PARENT_IDX          VARCHAR2(32)                   default NULL,
   CLASS_NAME           VARCHAR2(30)                   default NULL,
   leaf                 SMALLINT                        not null,
   SPECIFICATION        VARCHAR2(20)                   default NULL,
   SPECIFICATION_EXPRESS VARCHAR2(50)                   default NULL,
   MODEL_EXPRESS        VARCHAR2(50)                   default NULL,
   EXPECT_USE_YEAR      SMALLINT                       default NULL,
   RESIDUAL_RATE        NUMBER(5,2)                    default NULL,
   DEPRECITION_RATE     NUMBER(5,2)                    default NULL,
   REMARK               VARCHAR2(200)                  default NULL,
   record_status        SMALLINT                        not null,
   creator              NUMBER(10,0)                    not null,
   CREATE_TIME          DATE                            not null,
   updator              NUMBER(10,0)                    not null,
   UPDATE_TIME          DATE                            not null,
   constraint PK_E_EQUIPMENT_CLASSES primary key (IDX)
);

comment on table e_equipment_classes is
'设备类别';

comment on column e_equipment_classes.IDX is
'主键';

comment on column e_equipment_classes.CLASS_CODE is
'类别编码';

comment on column e_equipment_classes.PARENT_IDX is
'父类别编码';

comment on column e_equipment_classes.CLASS_NAME is
'类别名称';

comment on column e_equipment_classes.leaf is
'叶子节点，0：不是叶子节点，1：是叶子节点';

comment on column e_equipment_classes.SPECIFICATION is
'规格型号';

comment on column e_equipment_classes.SPECIFICATION_EXPRESS is
'规格表达式';

comment on column e_equipment_classes.MODEL_EXPRESS is
'型号表达式';

comment on column e_equipment_classes.EXPECT_USE_YEAR is
'预计使用年限';

comment on column e_equipment_classes.RESIDUAL_RATE is
'残值率';

comment on column e_equipment_classes.DEPRECITION_RATE is
'折旧率';

comment on column e_equipment_classes.REMARK is
'备注';

comment on column e_equipment_classes.record_status is
'数据状态';

comment on column e_equipment_classes.creator is
'创建人';

comment on column e_equipment_classes.CREATE_TIME is
'创建时间';

comment on column e_equipment_classes.updator is
'修改人';

comment on column e_equipment_classes.UPDATE_TIME is
'修改时间';
