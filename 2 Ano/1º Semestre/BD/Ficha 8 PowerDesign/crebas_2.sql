/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     04/11/2024 16:58:18                          */
/*==============================================================*/


drop index TEM_FK;

drop index TRANSPORTA_FK;

drop table ENCOMENDA;

drop index REFERE_FK;

drop index RELATIONSHIP_4_FK;

drop table ENC_PAO;

drop table PADARIA;

drop table PAO;

drop table VEICULO;

/*==============================================================*/
/* Table: ENCOMENDA                                             */
/*==============================================================*/
create table ENCOMENDA (
   ID_ENCOMENDA         NUMERIC(4)           not null,
   ID_VEICULO           NUMERIC(4)           null,
   ID_PADARIA           NUMERIC(4)           null,
   MATRICULA            VARCHAR(8)           not null,
   MARCA                VARCHAR(30)          null,
   constraint PK_ENCOMENDA primary key (ID_ENCOMENDA)
);

/*==============================================================*/
/* Index: TRANSPORTA_FK                                         */
/*==============================================================*/
create  index TRANSPORTA_FK on ENCOMENDA (
ID_VEICULO
);

/*==============================================================*/
/* Index: TEM_FK                                                */
/*==============================================================*/
create  index TEM_FK on ENCOMENDA (
ID_PADARIA
);

/*==============================================================*/
/* Table: ENC_PAO                                               */
/*==============================================================*/
create table ENC_PAO (
   ID_ENCOMENDA         NUMERIC(4)           not null,
   ID_PAO               NUMERIC(4)           not null,
   QUANTIDADE           NUMERIC              null,
   constraint PK_ENC_PAO primary key (ID_ENCOMENDA, ID_PAO)
);

/*==============================================================*/
/* Index: RELATIONSHIP_4_FK                                     */
/*==============================================================*/
create  index RELATIONSHIP_4_FK on ENC_PAO (
ID_ENCOMENDA
);

/*==============================================================*/
/* Index: REFERE_FK                                             */
/*==============================================================*/
create  index REFERE_FK on ENC_PAO (
ID_PAO
);

/*==============================================================*/
/* Table: PADARIA                                               */
/*==============================================================*/
create table PADARIA (
   ID_PADARIA           NUMERIC(4)           not null,
   NOME                 VARCHAR(30)          null,
   CIDADE               VARCHAR(20)          null,
   TELEFONE             VARCHAR(12)          null,
   CONTRIBUINTE         VARCHAR(9)           null,
   constraint PK_PADARIA primary key (ID_PADARIA)
);

/*==============================================================*/
/* Table: PAO                                                   */
/*==============================================================*/
create table PAO (
   ID_PAO               NUMERIC(4)           not null,
   MATRICULA            VARCHAR(8)           not null,
   MARCA                VARCHAR(30)          null,
   PESO                 NUMERIC(5,2)         null,
   constraint PK_PAO primary key (ID_PAO)
);

/*==============================================================*/
/* Table: VEICULO                                               */
/*==============================================================*/
create table VEICULO (
   ID_VEICULO           NUMERIC(4)           not null,
   MATRICULA            VARCHAR(8)           not null,
   MARCA                VARCHAR(30)          null,
   MODELO               VARCHAR(30)          null,
   TARA                 NUMERIC(4)           null,
   constraint PK_VEICULO primary key (ID_VEICULO)
);

alter table ENCOMENDA
   add constraint FK_ENCOMEND_TEM_PADARIA foreign key (ID_PADARIA)
      references PADARIA (ID_PADARIA)
      on delete restrict on update restrict;

alter table ENCOMENDA
   add constraint FK_ENCOMEND_TRANSPORT_VEICULO foreign key (ID_VEICULO)
      references VEICULO (ID_VEICULO)
      on delete restrict on update restrict;

alter table ENC_PAO
   add constraint FK_ENC_PAO_REFERE_PAO foreign key (ID_PAO)
      references PAO (ID_PAO)
      on delete restrict on update restrict;

alter table ENC_PAO
   add constraint FK_ENC_PAO_RELATIONS_ENCOMEND foreign key (ID_ENCOMENDA)
      references ENCOMENDA (ID_ENCOMENDA)
      on delete restrict on update restrict;

