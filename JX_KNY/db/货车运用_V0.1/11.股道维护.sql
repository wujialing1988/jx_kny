create table K_STATION_TRACK
(
  IDX            VARCHAR2(50 CHAR) not null,
  INSPECTION_IDX VARCHAR2(50 CHAR),
  TRACK_NAME     VARCHAR2(100 CHAR),
  TRACK_CODE     VARCHAR2(50 CHAR),
  SEQ_NO         NUMBER(2),
  RECORD_STATUS  NUMBER(1),
  CREATOR        NUMBER(18) not null,
  CREATE_TIME    DATE not null,
  UPDATOR        NUMBER(18) not null,
  UPDATE_TIME    DATE not null
);

-- Add comments to the table 
comment on table K_STATION_TRACK
  is '股道维护';
-- Add comments to the columns 
comment on column K_STATION_TRACK.IDX
  is '主键';
comment on column K_STATION_TRACK.INSPECTION_IDX
  is '列检所';
comment on column K_STATION_TRACK.TRACK_NAME
  is '股道名称';
comment on column K_STATION_TRACK.TRACK_CODE
  is '股道编码';
comment on column K_STATION_TRACK.SEQ_NO
  is '排序';
comment on column K_STATION_TRACK.RECORD_STATUS
  is '记录状态，1：删除；0：未删除；';
comment on column K_STATION_TRACK.CREATOR
  is '创建人';
comment on column K_STATION_TRACK.CREATE_TIME
  is '创建时间';
comment on column K_STATION_TRACK.UPDATOR
  is '修改人';
comment on column K_STATION_TRACK.UPDATE_TIME
  is '修改时间';
  
 -- Create/Recreate primary, unique and foreign key constraints 
alter table K_STATION_TRACK
  add constraint PK_K_STATION_TRACK primary key (IDX);