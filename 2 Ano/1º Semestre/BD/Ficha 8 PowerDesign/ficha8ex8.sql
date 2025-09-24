/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     13/11/2024 22:52:37                          */
/*==============================================================*/


alter table APLICA
   drop constraint FK_APLICA_APLICA_MECANICO;

alter table APLICA
   drop constraint FK_APLICA_APLICA2_VEICULO;

alter table APLICA
   drop constraint FK_APLICA_APLICA3_PECA;

drop index APLICA3_FK;

drop index APLICA2_FK;

drop index APLICA_FK;

drop table APLICA cascade constraints;

drop table MECANICO cascade constraints;

drop table PECA cascade constraints;

drop table VEICULO cascade constraints;

/*==============================================================*/
/* Table: APLICA                                                */
/*==============================================================*/
create table APLICA 
(
   ID_MECANICO          NUMBER               not null,
   ID_VEICULO           NUMBER               not null,
   ID_PECA              NUMBER               not null,
   constraint PK_APLICA primary key (ID_MECANICO, ID_VEICULO, ID_PECA)
);

/*==============================================================*/
/* Index: APLICA_FK                                             */
/*==============================================================*/
create index APLICA_FK on APLICA (
   ID_MECANICO ASC
);

/*==============================================================*/
/* Index: APLICA2_FK                                            */
/*==============================================================*/
create index APLICA2_FK on APLICA (
   ID_VEICULO ASC
);

/*==============================================================*/
/* Index: APLICA3_FK                                            */
/*==============================================================*/
create index APLICA3_FK on APLICA (
   ID_PECA ASC
);

/*==============================================================*/
/* Table: MECANICO                                              */
/*==============================================================*/
create table MECANICO 
(
   ID_MECANICO          NUMBER               not null,
   DESCRICAO            NUMBER,
   IDADE                NUMBER,
   constraint PK_MECANICO primary key (ID_MECANICO)
);

/*==============================================================*/
/* Table: PECA                                                  */
/*==============================================================*/
create table PECA 
(
   ID_PECA              NUMBER               not null,
   DESCRICAO            NUMBER,
   FABRICANTE           NUMBER,
   constraint PK_PECA primary key (ID_PECA)
);

/*==============================================================*/
/* Table: VEICULO                                               */
/*==============================================================*/
create table VEICULO 
(
   ID_VEICULO           NUMBER               not null,
   MARCA                NUMBER,
   MODELO               NUMBER,
   constraint PK_VEICULO primary key (ID_VEICULO)
);

alter table APLICA
   add constraint FK_APLICA_APLICA_MECANICO foreign key (ID_MECANICO)
      references MECANICO (ID_MECANICO);

alter table APLICA
   add constraint FK_APLICA_APLICA2_VEICULO foreign key (ID_VEICULO)
      references VEICULO (ID_VEICULO);

alter table APLICA
   add constraint FK_APLICA_APLICA3_PECA foreign key (ID_PECA)
      references PECA (ID_PECA);

