/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     12/11/2024 21:15:39                          */
/*==============================================================*/


alter table VEICULO
   drop constraint FK_VEICULO_POSSUI_MOTOR;

drop table MOTOR cascade constraints;

drop index MOTOR_FK;

drop table VEICULO cascade constraints;

/*==============================================================*/
/* Table: MOTOR                                                 */
/*==============================================================*/
create table MOTOR 
(
   ID_MOTOR             NUMBER               not null,
   CILINDRADA           NUMBER,
   COMBUSTIVEL          NUMBER,
   CAVALOS              NUMBER,
   constraint PK_MOTOR primary key (ID_MOTOR)
);

/*==============================================================*/
/* Table: VEICULO                                               */
/*==============================================================*/
create table VEICULO 
(
   FK_ID_MOTOR          NUMBER,
   IDVEICULO            NUMBER               not null,
   MARICULA             NUMBER,
   MARCA                NUMBER,
   MODELO               NUMBER,
   constraint PK_VEICULO primary key (IDVEICULO)
);

/*==============================================================*/
/* Index: MOTOR_FK                                              */
/*==============================================================*/
create index MOTOR_FK on VEICULO (
   FK_ID_MOTOR ASC
);

alter table VEICULO
   add constraint FK_VEICULO_POSSUI_MOTOR foreign key (FK_ID_MOTOR)
      references MOTOR (ID_MOTOR);

