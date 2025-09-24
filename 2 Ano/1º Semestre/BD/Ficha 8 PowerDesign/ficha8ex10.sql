/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     13/11/2024 22:54:59                          */
/*==============================================================*/


alter table HDD
   drop constraint FK_HDD_HERANCA2_PRODUTO;

alter table MONITOR
   drop constraint FK_MONITOR_HERANCA_PRODUTO;

alter table PORTATIL
   drop constraint FK_PORTATIL_HERANCA3_PRODUTO;

drop table HDD cascade constraints;

drop table MONITOR cascade constraints;

drop table PORTATIL cascade constraints;

drop table PRODUTO cascade constraints;

/*==============================================================*/
/* Table: HDD                                                   */
/*==============================================================*/
create table HDD 
(
   ID_PRODUTO           NUMBER               not null,
   CAPACIDADE           CHAR(10),
   RPM                  CHAR(10),
   CACHE                CHAR(10),
   constraint PK_HDD primary key (ID_PRODUTO)
);

/*==============================================================*/
/* Table: MONITOR                                               */
/*==============================================================*/
create table MONITOR 
(
   ID_PRODUTO           NUMBER               not null,
   RESOLUCAO            NUMBER,
   INTERFACE            NUMBER,
   TAMANHO              NUMBER,
   constraint PK_MONITOR primary key (ID_PRODUTO)
);

/*==============================================================*/
/* Table: PORTATIL                                              */
/*==============================================================*/
create table PORTATIL 
(
   ID_PRODUTO           NUMBER               not null,
   GAMA                 NUMBER,
   PROCESSADDOR         NUMBER,
   MEMORIA              NUMBER,
   constraint PK_PORTATIL primary key (ID_PRODUTO)
);

/*==============================================================*/
/* Table: PRODUTO                                               */
/*==============================================================*/
create table PRODUTO 
(
   ID_PRODUTO           NUMBER               not null,
   NOME                 NUMBER               not null,
   TIPO                 NUMBER,
   PRECO                NUMBER,
   constraint PK_PRODUTO primary key (ID_PRODUTO)
);

alter table HDD
   add constraint FK_HDD_HERANCA2_PRODUTO foreign key (ID_PRODUTO)
      references PRODUTO (ID_PRODUTO);

alter table MONITOR
   add constraint FK_MONITOR_HERANCA_PRODUTO foreign key (ID_PRODUTO)
      references PRODUTO (ID_PRODUTO);

alter table PORTATIL
   add constraint FK_PORTATIL_HERANCA3_PRODUTO foreign key (ID_PRODUTO)
      references PRODUTO (ID_PRODUTO);

