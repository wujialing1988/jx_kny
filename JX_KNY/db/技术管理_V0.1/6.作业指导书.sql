drop table K_WORK_INSTRUCTOR cascade constraints;

/*==============================================================*/
/* Table: K_WORK_INSTRUCTOR                                     */
/*==============================================================*/
create table K_WORK_INSTRUCTOR  (
   IDX                  VARCHAR2(32)                    not null,
   TITLE                VARCHAR2(100),
   CONTENT              VARCHAR2(500),
   START_PAGE           NUMBER(10),
   END_PAGE             NUMBER(10),
   SEQ_NO               NUMBER(2),
   constraint PK_K_WORK_INSTRUCTOR primary key (IDX)
);

comment on table K_WORK_INSTRUCTOR is
'作业指导书';

comment on column K_WORK_INSTRUCTOR.IDX is
'主键';

comment on column K_WORK_INSTRUCTOR.TITLE is
'标题名';

comment on column K_WORK_INSTRUCTOR.CONTENT is
'内容';

comment on column K_WORK_INSTRUCTOR.START_PAGE is
'开始页码';

comment on column K_WORK_INSTRUCTOR.END_PAGE is
'结束页面';

comment on column K_WORK_INSTRUCTOR.SEQ_NO is
'排序';
