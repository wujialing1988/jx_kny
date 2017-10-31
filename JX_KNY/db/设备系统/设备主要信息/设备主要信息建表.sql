create table e_equipment_primary_info  (
   IDX                  VARCHAR2(32)                    not null,
   ORG_NAME             VARCHAR2(100)                   default NULL,
   ORG_ID               VARCHAR2(30)                    not null,
   CLASS_NAME           VARCHAR2(30)                   default NULL,
   CLASS_CODE           VARCHAR2(30),
   EQUIPMENT_NAME       VARCHAR2(30)                    not null,
   EQUIPMENT_CODE       VARCHAR2(20)                    not null,
   buy_date             DATE                           default NULL,
   FIXED_ASSET_NO       VARCHAR2(30)                   default NULL,
   MODEL                VARCHAR2(50)                   default NULL,
   SPECIFICATION        VARCHAR2(50)                   default NULL,
   MECHANICAL_COEFFICIENT NUMBER(7,2)                    default NULL,
   ELECTRIC_COEFFICIENT NUMBER(7,2)                    default NULL,
   MAKE_FACTORY         VARCHAR2(50)                   default NULL,
   MAKE_DATE            DATE                           default NULL,
   FIXED_ASSET_VALUE    NUMBER(12,2)                   default NULL,
   USE_DATE             DATE                           default NULL,
   USE_PLACE            VARCHAR2(20)                   default NULL,
   MANAGE_LEVEL         VARCHAR2(3)                    default NULL,
   MANAGE_CLASS         VARCHAR2(1)                    default NULL,
   WEIGHT               NUMBER(10,2)                   default NULL,
   MAX_REPAIR_YEAR      INTEGER                        default NULL,
   LEAVE_FACTORY_NO     VARCHAR2(20)                   default NULL,
   ELETRIC_TOTAL_POWER  VARCHAR2(10)                   default NULL,
   TEC_LEVEL            VARCHAR2(2)                    default NULL,
   SHAPE_SIZE           VARCHAR2(30)                   default NULL,
   IS_PRIMARY_DEVICE    SMALLINT                        not null,
   IS_DEDICATED         SMALLINT                        not null,
   IS_SPECIAL_TYPE      SMALLINT                        not null,
   IS_EXACTNESS         SMALLINT                        not null,
   IS_FROCK             SMALLINT                        not null,
   DYNAMIC              INTEGER                         not null,
   RUNING_SHIFTS        INTEGER                         not null,
   xz_state             SMALLINT                        not null,
   cz_state             SMALLINT                        not null,
   fc_state             SMALLINT                        not null,
   USE_WORKSHOP         VARCHAR2(50)                   default NULL,
   USE_WORKSHOP_ID      VARCHAR2(50)                   default NULL,
   USE_PERSON           VARCHAR2(100)                  default NULL,
   USE_PERSON_ID        VARCHAR2(100)                  default NULL,
   ELECTRIC_REPAIR_TEAM VARCHAR2(50)                   default NULL,
   ELECTRIC_REPAIR_TEAM_ID VARCHAR2(50)                   default NULL,
   MECHANICAL_REPAIR_TEAM VARCHAR2(50)                   default NULL,
   MECHANICAL_REPAIR_TEAM_ID VARCHAR2(50)                   default NULL,
   ELECTRIC_REPAIR_PERSON VARCHAR2(100)                  default NULL,
   ELECTRIC_REPAIR_PERSON_ID VARCHAR2(100)                  default NULL,
   MECHANICAL_REPAIR_PERSON VARCHAR2(100)                  default NULL,
   MECHANICAL_REPAIR_PERSON_ID VARCHAR2(100)                  default NULL,
   REMARK               VARCHAR2(100)                  default NULL,
   TEC_STATUS           INTEGER                        default NULL,
   record_status        SMALLINT                        not null,
   creator              NUMBER(10,0)                    not null,
   CREATE_TIME          DATE                            not null,
   updator              NUMBER(10,0)                    not null,
   UPDATE_TIME          DATE                            not null,
   constraint PK_E_EQUIPMENT_PRIMARY_INFO primary key (IDX)
);

comment on table e_equipment_primary_info is
'主要设备信息';

comment on column e_equipment_primary_info.IDX is
'主键';

comment on column e_equipment_primary_info.ORG_NAME is
'单位名称';

comment on column e_equipment_primary_info.ORG_ID is
'单位ID';

comment on column e_equipment_primary_info.CLASS_NAME is
'设备类别名称';

comment on column e_equipment_primary_info.CLASS_CODE is
'设备类别编码';

comment on column e_equipment_primary_info.EQUIPMENT_NAME is
'设备名称';

comment on column e_equipment_primary_info.EQUIPMENT_CODE is
'设备编码';

comment on column e_equipment_primary_info.buy_date is
'购入日期';

comment on column e_equipment_primary_info.FIXED_ASSET_NO is
'固资编号';

comment on column e_equipment_primary_info.MODEL is
'型号';

comment on column e_equipment_primary_info.SPECIFICATION is
'规格';

comment on column e_equipment_primary_info.MECHANICAL_COEFFICIENT is
'机械系数';

comment on column e_equipment_primary_info.ELECTRIC_COEFFICIENT is
'电气系数';

comment on column e_equipment_primary_info.MAKE_FACTORY is
'制造工厂';

comment on column e_equipment_primary_info.MAKE_DATE is
'制造年月';

comment on column e_equipment_primary_info.FIXED_ASSET_VALUE is
'固资原值';

comment on column e_equipment_primary_info.USE_DATE is
'使用年月';

comment on column e_equipment_primary_info.USE_PLACE is
'设置地点';

comment on column e_equipment_primary_info.MANAGE_LEVEL is
'管理级别';

comment on column e_equipment_primary_info.MANAGE_CLASS is
'管理类别（A、B、C）';

comment on column e_equipment_primary_info.WEIGHT is
'重量';

comment on column e_equipment_primary_info.MAX_REPAIR_YEAR is
'最大修年度';

comment on column e_equipment_primary_info.LEAVE_FACTORY_NO is
'出厂编号';

comment on column e_equipment_primary_info.ELETRIC_TOTAL_POWER is
'电气总功率';

comment on column e_equipment_primary_info.TEC_LEVEL is
'技术等级';

comment on column e_equipment_primary_info.SHAPE_SIZE is
'外形尺寸';

comment on column e_equipment_primary_info.IS_PRIMARY_DEVICE is
'是否主设备';

comment on column e_equipment_primary_info.IS_DEDICATED is
'是否专用设备';

comment on column e_equipment_primary_info.IS_SPECIAL_TYPE is
'是否特种设备';

comment on column e_equipment_primary_info.IS_EXACTNESS is
'是否大精设备';

comment on column e_equipment_primary_info.IS_FROCK is
'是否工装设备';

comment on column e_equipment_primary_info.DYNAMIC is
'设备动态';

comment on column e_equipment_primary_info.RUNING_SHIFTS is
'运行班制';

comment on column e_equipment_primary_info.xz_state is
'闲置状态';

comment on column e_equipment_primary_info.cz_state is
'出租状态';

comment on column e_equipment_primary_info.fc_state is
'封存状态';

comment on column e_equipment_primary_info.USE_WORKSHOP is
'使用车间';

comment on column e_equipment_primary_info.USE_WORKSHOP_ID is
'使用车间ID';

comment on column e_equipment_primary_info.USE_PERSON is
'使用人';

comment on column e_equipment_primary_info.USE_PERSON_ID is
'使用人ID';

comment on column e_equipment_primary_info.ELECTRIC_REPAIR_TEAM is
'电气维修班组';

comment on column e_equipment_primary_info.ELECTRIC_REPAIR_TEAM_ID is
'电气维修班组ID';

comment on column e_equipment_primary_info.MECHANICAL_REPAIR_TEAM is
'机械维修班组';

comment on column e_equipment_primary_info.MECHANICAL_REPAIR_TEAM_ID is
'机械维修班组ID';

comment on column e_equipment_primary_info.ELECTRIC_REPAIR_PERSON is
'电气维修人';

comment on column e_equipment_primary_info.ELECTRIC_REPAIR_PERSON_ID is
'电气维修人ID';

comment on column e_equipment_primary_info.MECHANICAL_REPAIR_PERSON is
'机械维修人';

comment on column e_equipment_primary_info.MECHANICAL_REPAIR_PERSON_ID is
'机械维修人ID';

comment on column e_equipment_primary_info.REMARK is
'备注';

comment on column e_equipment_primary_info.TEC_STATUS is
'技术状态';

comment on column e_equipment_primary_info.record_status is
'数据状态';

comment on column e_equipment_primary_info.creator is
'创建人';

comment on column e_equipment_primary_info.CREATE_TIME is
'创建时间';

comment on column e_equipment_primary_info.updator is
'修改人';

comment on column e_equipment_primary_info.UPDATE_TIME is
'更新时间';
