create table e_equipment_union_rfid 
(
   rfid_code          VARCHAR2(20)         not null,
   constraint PK_E_EQUIPMENT_UNION_RFID primary key (rfid_code)
);

comment on table e_equipment_union_rfid is
'设备RFID关联';

comment on column e_equipment_union_rfid.rfid_code is
'设备编号';