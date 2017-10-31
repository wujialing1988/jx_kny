
/*==============================================================*/
/* Table: E_NEW_BUY_EQUIPMENT                                   */
/*==============================================================*/
create table E_NEW_BUY_EQUIPMENT 
(
   IDX                  VARCHAR2(32)         not null,
   EQUIPMENT_IDX        VARCHAR2(32)         not null,
   REMARK               VARCHAR2(100)        default NULL,
   RESPONSIBLE_PERSON   VARCHAR2(50)         default NULL,
   RESPONSIBLE_PERSON_ID VARCHAR2(50)         default NULL,
   RATIFY_ORGNAME       VARCHAR2(30)         default NULL,
   RATIFY_ORGID         VARCHAR2(30)         default NULL,
   AFFILIATED_ORGNAME   VARCHAR2(10)         default NULL,
   AFFILIATED_ORGID     VARCHAR2(30)         default NULL,
   BUY_BATCH_NUM        VARCHAR2(20)         default NULL,
   BUY_DATE             DATE                 not null,
   record_status      SMALLINT             not null,
   creator           NUMBER(10,0)         not null,
   CREATE_TIME          DATE                 not null,
   updator           NUMBER(10,0)         not null,
   UPDATE_TIME          DATE                 not null
);

comment on table E_NEW_BUY_EQUIPMENT is
'新购设备';

comment on column E_NEW_BUY_EQUIPMENT.IDX is
'主键';

comment on column E_NEW_BUY_EQUIPMENT.EQUIPMENT_IDX is
'主设备主键';

comment on column E_NEW_BUY_EQUIPMENT.REMARK is
'备注';

comment on column E_NEW_BUY_EQUIPMENT.RESPONSIBLE_PERSON is
'经办人';

comment on column E_NEW_BUY_EQUIPMENT.RESPONSIBLE_PERSON_ID is
'经办人编号';

comment on column E_NEW_BUY_EQUIPMENT.RATIFY_ORGNAME is
'批准单位';

comment on column E_NEW_BUY_EQUIPMENT.RATIFY_ORGID is
'批准单位ID';

comment on column E_NEW_BUY_EQUIPMENT.AFFILIATED_ORGNAME is
'所属单位';

comment on column E_NEW_BUY_EQUIPMENT.AFFILIATED_ORGID is
'所属单位ID';

comment on column E_NEW_BUY_EQUIPMENT.BUY_BATCH_NUM is
'新购批号';

comment on column E_NEW_BUY_EQUIPMENT.BUY_DATE is
'新购日期';

comment on column E_NEW_BUY_EQUIPMENT.record_status is
'数据状态';

comment on column E_NEW_BUY_EQUIPMENT.creator is
'创建人';

comment on column E_NEW_BUY_EQUIPMENT.CREATE_TIME is
'创建时间';

comment on column E_NEW_BUY_EQUIPMENT.updator is
'修改人';

comment on column E_NEW_BUY_EQUIPMENT.UPDATE_TIME is
'修改时间';
