/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     12/11/2024 20:03:31                          */
/*==============================================================*/


drop table VEICULO cascade constraints;

/*==============================================================*/
/* Table: VEICULO                                               */
/*==============================================================*/
create table VEICULO 
(
   IDVEICULO            NUMBER               not null,
   MARICULA             NUMBER,
   MARCA                NUMBER,
   MODELO               NUMBER,
   ID_MOTOR             NUMBER,
   CILINDRADA           NUMBER,
   COMBUSTIVEL          NUMBER,
   CAVALOS              NUMBER,
   constraint PK_VEICULO primary key (IDVEICULO)
);

