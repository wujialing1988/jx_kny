create table e_affiliated_equipment  (
   IDX                  VARCHAR2(32)                    not null,
   EQUIPMENT_IDX        VARCHAR2(32)                   default NULL,
   EQUIPMENT_NO         VARCHAR2(20)                   default NULL,
   EQUIPMENT_NAME       VARCHAR2(15)                   default NULL,
   MODAL                VARCHAR2(10)                   default NULL,
   SPECIFICATION        VARCHAR2(10)                   default NULL,
   MECHANICAL_COEFFICIENT SMALLINT                       default NULL,
   ELECTRIC_COEFFICIENT SMALLINT                       default NULL,
   COUNT                SMALLINT                       default NULL,
   UNIT                 VARCHAR2(4)                    default NULL,
   POWER                INTEGER                        default NULL,
   PRICE                NUMBER(10,2)                   default NULL,
   MAKE_FACTORY         VARCHAR2(20)                   default NULL,
   REMARK               VARCHAR2(100)                  default NULL,
   record_status      SMALLINT                        not null,
   creator            NUMBER(10,0)                    not null,
   CREATE_TIME          DATE                            not null,
   updator            NUMBER(10,0)                    not null,
   UPDATE_TIME          DATE                            not null,
   constraint PK_E_AFFILIATED_EQUIPMENT primary key (IDX)
);

comment on table e_affiliated_equipment is
'设备主要信息-附属设备';

comment on column e_affiliated_equipment.IDX is
'主键';

comment on column e_affiliated_equipment.EQUIPMENT_IDX is
'主设备主键';

comment on column e_affiliated_equipment.EQUIPMENT_NO is
'设备编号';

comment on column e_affiliated_equipment.EQUIPMENT_NAME is
'设备名称';

comment on column e_affiliated_equipment.MODAL is
'设备型号';

comment on column e_affiliated_equipment.SPECIFICATION is
'设备规格';

comment on column e_affiliated_equipment.MECHANICAL_COEFFICIENT is
'机械系数';

comment on column e_affiliated_equipment.ELECTRIC_COEFFICIENT is
'电气系数';

comment on column e_affiliated_equipment.COUNT is
'数量';

comment on column e_affiliated_equipment.UNIT is
'单位';

comment on column e_affiliated_equipment.POWER is
'功率';

comment on column e_affiliated_equipment.PRICE is
'单价';

comment on column e_affiliated_equipment.MAKE_FACTORY is
'生产厂家';

comment on column e_affiliated_equipment.REMARK is
'备注';

comment on column e_affiliated_equipment.record_status is
'数据状态';

comment on column e_affiliated_equipment.creator is
'创建人';

comment on column e_affiliated_equipment.CREATE_TIME is
'创建时间';

comment on column e_affiliated_equipment.updator is
'修改人';

comment on column e_affiliated_equipment.UPDATE_TIME is
'修改时间';
