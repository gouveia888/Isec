/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     13/12/2024 15:47:09                          */
/*==============================================================*/


alter table ADICIONADO
   drop constraint FK_ADICIONA_ADICIONAD_CARRINHO;

alter table ADICIONADO
   drop constraint FK_ADICIONA_ADICIONAD_PRODUTOS;

alter table CATEGORIAS
   drop constraint FK_CATEGORI_REFERENCE_PRODUTOS;

alter table CONJUNTO
   drop constraint FK_TABLE_16_REFERENCE_PRODUTO2;

alter table CONJUNTO
   drop constraint FK_CONJUNTO_REFERENCE_PRODUTOS;

alter table CONTACTOS
   drop constraint FK_CONTACTO_DISPOE_FORNECED;

alter table CRIA
   drop constraint FK_CRIA_REFERENCE_CARRINHO;

alter table CRIA
   drop constraint FK_CRIA_REFERENCE_CLIENTES;

alter table ENCOMENDAS
   drop constraint FK_ENCOMEND_REFERENCE_CARRINHO;

alter table ENTREGA
   drop constraint FK_ENTREGA_REFERENCE_ENCOMEND;

alter table ENVIADA
   drop constraint FK_ENVIADA_REFERENCE_TRANSPOR;

alter table ENVIADA
   drop constraint FK_ENVIADA_REFERENCE_ENTREGA;

alter table PAGAMENTOS
   drop constraint FK_PAGAMENT_REFERENCE_ESTADO_P;

alter table PAGAMENTOS
   drop constraint FK_PAGAMENT_TEM_ENCOMEND;

alter table PODE_FORNECER
   drop constraint FK_PODE_FOR_REFERENCE_FORNECED;

alter table PODE_FORNECER
   drop constraint FK_PODE_FOR_REFERENCE_PRODUTOS;

drop index ADICIONADO2_FK;

drop index ADICIONADO_FK;

drop table ADICIONADO cascade constraints;

drop table CARRINHO cascade constraints;

drop table CATEGORIAS cascade constraints;

drop table CLIENTES cascade constraints;

drop table CONJUNTO cascade constraints;

drop index DISPOE_FK;

drop table CONTACTOS cascade constraints;

drop table CRIA cascade constraints;

drop table ENCOMENDAS cascade constraints;

drop table ENTREGA cascade constraints;

drop table ENVIADA cascade constraints;

drop table ESTADO_PAGAMENTO cascade constraints;

drop table FORNECEDORES cascade constraints;

drop index TEM_FK;

drop table PAGAMENTOS cascade constraints;

drop table PODE_FORNECER cascade constraints;

drop table PRODUTOS cascade constraints;

drop table TRANSPORTADORA cascade constraints;

/*==============================================================*/
/* Table: ADICIONADO                                            */
/*==============================================================*/
create table ADICIONADO 
(
   ID_CARRINHO          NUMBER(8)            not null,
   ID_PRODUTO           NUMBER(8)            not null,
   QUANTIDADE           NUMBER,
   constraint PK_ADICIONADO primary key (ID_CARRINHO, ID_PRODUTO)
);

/*==============================================================*/
/* Index: ADICIONADO_FK                                         */
/*==============================================================*/
create index ADICIONADO_FK on ADICIONADO (
   ID_CARRINHO ASC
);

/*==============================================================*/
/* Index: ADICIONADO2_FK                                        */
/*==============================================================*/
create index ADICIONADO2_FK on ADICIONADO (
   ID_PRODUTO ASC
);

/*==============================================================*/
/* Table: CARRINHO                                              */
/*==============================================================*/
create table CARRINHO 
(
   ID_CARRINHO          NUMBER(8)            not null,
   constraint PK_CARRINHO primary key (ID_CARRINHO)
);

/*==============================================================*/
/* Table: CATEGORIAS                                            */
/*==============================================================*/
create table CATEGORIAS 
(
   ID_CATEGORIAS        NUMBER(8)            not null,
   ID_PRODUTO           NUMBER,
   NOME2                VARCHAR2(30),
   constraint PK_CATEGORIAS primary key (ID_CATEGORIAS)
);

/*==============================================================*/
/* Table: CLIENTES                                              */
/*==============================================================*/
create table CLIENTES 
(
   ID_CLIENTES          NUMBER(8)            not null,
   NIF                  NUMBER(9)            not null,
   NOME2                VARCHAR2(50),
   EMAIL                VARCHAR2(20),
   TELEFONE             NUMBER(9),
   CODIGO_POSTAL        NUMBER(8),
   RUA                  VARCHAR2(50),
   CIDADE               VARCHAR2(20),
   PAIS                 VARCHAR2(20),
   constraint PK_CLIENTES primary key (ID_CLIENTES)
);

/*==============================================================*/
/* Table: CONJUNTO                                              */
/*==============================================================*/
create table CONJUNTO 
(
   ID_PRODUTO           NUMBER(8)            not null,
   ID_PRODUTO1          NUMBER               not null,
   QUANT                NUMBER(4),
   constraint PK_CONJUNTO primary key (ID_PRODUTO, ID_PRODUTO1)
);

/*==============================================================*/
/* Table: CONTACTOS                                             */
/*==============================================================*/
create table CONTACTOS 
(
   TELEMOVEL            NUMBER(9)            not null,
   ID_FORNECEDORES      NUMBER               not null,
   constraint PK_CONTACTOS primary key (TELEMOVEL, ID_FORNECEDORES)
);

/*==============================================================*/
/* Index: DISPOE_FK                                             */
/*==============================================================*/
create index DISPOE_FK on CONTACTOS (
   ID_FORNECEDORES ASC
);

/*==============================================================*/
/* Table: CRIA                                                  */
/*==============================================================*/
create table CRIA 
(
   ID_CARRINHO          NUMBER(8)            not null,
   ID_CLIENTES          NUMBER(8)            not null,
   constraint PK_CRIA primary key (ID_CARRINHO, ID_CLIENTES)
);

/*==============================================================*/
/* Table: ENCOMENDAS                                            */
/*==============================================================*/
create table ENCOMENDAS 
(
   ID_ENCOMENDA         NUMBER(8)            not null,
   ID_CARRINHO          NUMBER,
   ESTADO_ENCOMENDA     VARCHAR2(30),
   DATA_ENCOMENDA       DATE,
   PRECO_TOTAL          NUMBER(8,2),
   PESO_TOTAL           NUMBER(5,3),
   QUANT_JA_PAGA        NUMBER(8,2),
   CODIGO_POSTAL        NUMBER(8),
   RUA                  VARCHAR2(50),
   CIDADE               VARCHAR2(20),
   PAIS                 VARCHAR2(20),
   constraint PK_ENCOMENDAS primary key (ID_ENCOMENDA)
);

/*==============================================================*/
/* Table: ENTREGA                                               */
/*==============================================================*/
create table ENTREGA 
(
   CODIGO_RASTREIO      NUMBER(10)           not null,
   ID_ENCOMENDA         NUMBER(8),
   ESTADO_ENTREGA       VARCHAR2(30),
   DATA_ENTREGA         DATE,
   LOCAL_ENTREGA        VARCHAR2(50),
   constraint PK_ENTREGA primary key (CODIGO_RASTREIO)
);

/*==============================================================*/
/* Table: ENVIADA                                               */
/*==============================================================*/
create table ENVIADA 
(
   ID_TRANSPORTADORA    NUMBER(8)            not null,
   CODIGO_RASTREIO      NUMBER(10)           not null,
   constraint PK_ENVIADA primary key (ID_TRANSPORTADORA, CODIGO_RASTREIO)
);

/*==============================================================*/
/* Table: ESTADO_PAGAMENTO                                      */
/*==============================================================*/
create table ESTADO_PAGAMENTO 
(
   ID_ESTADO            NUMBER(8)            not null,
   constraint PK_ESTADO_PAGAMENTO primary key (ID_ESTADO)
);

/*==============================================================*/
/* Table: FORNECEDORES                                          */
/*==============================================================*/
create table FORNECEDORES 
(
   ID_FORNECEDORES      NUMBER(8)            not null,
   NOME2                VARCHAR2(30),
   CODIGO_POSTAL        NUMBER(8),
   RUA                  VARCHAR2(50),
   CIDADE               VARCHAR2(20),
   PAIS                 VARCHAR2(20),
   NUM_ARMAZEM          NUMBER(4),
   constraint PK_FORNECEDORES primary key (ID_FORNECEDORES)
);

/*==============================================================*/
/* Table: PAGAMENTOS                                            */
/*==============================================================*/
create table PAGAMENTOS 
(
   ID_PAGAMENTO         NUMBER(8)            not null,
   ID_ENCOMENDA         NUMBER(8),
   ID_ESTADO            NUMBER(8),
   QUANT_A_PAGAR        NUMBER(8,2),
   DATA_PAGAMENTO       DATE,
   constraint PK_PAGAMENTOS primary key (ID_PAGAMENTO)
);

/*==============================================================*/
/* Index: TEM_FK                                                */
/*==============================================================*/
create index TEM_FK on PAGAMENTOS (
   ID_ENCOMENDA ASC
);

/*==============================================================*/
/* Table: PODE_FORNECER                                         */
/*==============================================================*/
create table PODE_FORNECER 
(
   ID_FORNECEDORES      NUMBER(8),
   ID_PRODUTO           NUMBER(8)
);

/*==============================================================*/
/* Table: PRODUTOS                                              */
/*==============================================================*/
create table PRODUTOS 
(
   ID_PRODUTO           NUMBER(8)            not null,
   NOME2                VARCHAR2(30),
   QUATIDADE_STOCK      NUMBER,
   PRECO                NUMBER(8,2),
   PESO                 NUMBER(5,3),
   ALTURA               NUMBER(5,2),
   LARGURA              NUMBER(5,2),
   PROFUNDIDADE         NUMBER(5,2),
   constraint PK_PRODUTOS primary key (ID_PRODUTO)
);

/*==============================================================*/
/* Table: TRANSPORTADORA                                        */
/*==============================================================*/
create table TRANSPORTADORA 
(
   ID_TRANSPORTADORA    NUMBER(8)            not null,
   NOME                 VARCHAR2(30),
   constraint PK_TRANSPORTADORA primary key (ID_TRANSPORTADORA)
);

alter table ADICIONADO
   add constraint FK_ADICIONA_ADICIONAD_CARRINHO foreign key (ID_CARRINHO)
      references CARRINHO (ID_CARRINHO);

alter table ADICIONADO
   add constraint FK_ADICIONA_ADICIONAD_PRODUTOS foreign key (ID_PRODUTO)
      references PRODUTOS (ID_PRODUTO);

alter table CATEGORIAS
   add constraint FK_CATEGORI_REFERENCE_PRODUTOS foreign key (ID_PRODUTO)
      references PRODUTOS (ID_PRODUTO);

alter table CONJUNTO
   add constraint FK_TABLE_16_REFERENCE_PRODUTO2 foreign key (ID_PRODUTO)
      references PRODUTOS (ID_PRODUTO);

alter table CONJUNTO
   add constraint FK_CONJUNTO_REFERENCE_PRODUTOS foreign key (ID_PRODUTO1)
      references PRODUTOS (ID_PRODUTO);

alter table CONTACTOS
   add constraint FK_CONTACTO_DISPOE_FORNECED foreign key (ID_FORNECEDORES)
      references FORNECEDORES (ID_FORNECEDORES);

alter table CRIA
   add constraint FK_CRIA_REFERENCE_CARRINHO foreign key (ID_CARRINHO)
      references CARRINHO (ID_CARRINHO);

alter table CRIA
   add constraint FK_CRIA_REFERENCE_CLIENTES foreign key (ID_CLIENTES)
      references CLIENTES (ID_CLIENTES);

alter table ENCOMENDAS
   add constraint FK_ENCOMEND_REFERENCE_CARRINHO foreign key (ID_CARRINHO)
      references CARRINHO (ID_CARRINHO);

alter table ENTREGA
   add constraint FK_ENTREGA_REFERENCE_ENCOMEND foreign key (ID_ENCOMENDA)
      references ENCOMENDAS (ID_ENCOMENDA);

alter table ENVIADA
   add constraint FK_ENVIADA_REFERENCE_TRANSPOR foreign key (ID_TRANSPORTADORA)
      references TRANSPORTADORA (ID_TRANSPORTADORA);

alter table ENVIADA
   add constraint FK_ENVIADA_REFERENCE_ENTREGA foreign key (CODIGO_RASTREIO)
      references ENTREGA (CODIGO_RASTREIO);

alter table PAGAMENTOS
   add constraint FK_PAGAMENT_REFERENCE_ESTADO_P foreign key (ID_ESTADO)
      references ESTADO_PAGAMENTO (ID_ESTADO);

alter table PAGAMENTOS
   add constraint FK_PAGAMENT_TEM_ENCOMEND foreign key (ID_ENCOMENDA)
      references ENCOMENDAS (ID_ENCOMENDA);

alter table PODE_FORNECER
   add constraint FK_PODE_FOR_REFERENCE_FORNECED foreign key (ID_FORNECEDORES)
      references FORNECEDORES (ID_FORNECEDORES);

alter table PODE_FORNECER
   add constraint FK_PODE_FOR_REFERENCE_PRODUTOS foreign key (ID_PRODUTO)
      references PRODUTOS (ID_PRODUTO);
      
--------------------------------------VISTAS--------------------------------------
CREATE VIEW VISTA_CLIENTE AS
SELECT 
    C.NOME2 AS NOME_CLIENTE,
    E.ESTADO_ENCOMENDA,
    E.DATA_ENCOMENDA,
    E.PRECO_TOTAL
FROM 
    CLIENTES C
JOIN 
    CRIA CR ON C.ID_CLIENTES = CR.ID_CLIENTES
JOIN 
    ENCOMENDAS E ON CR.ID_CARRINHO = E.ID_CARRINHO;

CREATE VIEW VISTA_FORNECEDORES AS
SELECT 
    P.NOME2 AS NOME_PRODUTO,
    F.NOME2 AS NOME_FORNECEDOR
FROM 
    PODE_FORNECER PF
JOIN 
    PRODUTOS P ON PF.ID_PRODUTO = P.ID_PRODUTO
JOIN 
    FORNECEDORES F ON PF.ID_FORNECEDORES = F.ID_FORNECEDORES;

CREATE VIEW VISTA_CATEGORIA AS
SELECT 
    P.NOME2 AS NOME_PRODUTO,
    C.NOME2 AS NOME_CATEGORIA
FROM 
    PRODUTOS P
JOIN 
    CATEGORIAS C ON P.ID_PRODUTO = C.ID_PRODUTO;
    
CREATE VIEW VISTA_TRANSPORTADORA AS
SELECT 
    T.NOME AS NOME_TRANSPORTADORA,
    EN.ESTADO_ENTREGA,
    EN.DATA_ENTREGA,
    EN.LOCAL_ENTREGA
FROM 
    TRANSPORTADORA T
JOIN 
    ENVIADA EV ON T.ID_TRANSPORTADORA = EV.ID_TRANSPORTADORA
JOIN 
    ENTREGA EN ON EV.CODIGO_RASTREIO = EN.CODIGO_RASTREIO
JOIN 
    ENCOMENDAS E ON EN.ID_ENCOMENDA = E.ID_ENCOMENDA;
    
CREATE VIEW VISTA_PAGAMENTOS AS
SELECT 
    CL.NOME2 AS NOME_CLIENTE,
    P.ID_PAGAMENTO,
    P.ID_ENCOMENDA,
    P.QUANT_A_PAGAR,
    P.DATA_PAGAMENTO
FROM 
    CLIENTES CL
JOIN 
    CRIA CR ON CL.ID_CLIENTES = CR.ID_CLIENTES
JOIN 
    ENCOMENDAS E ON CR.ID_CARRINHO = E.ID_CARRINHO
JOIN 
    PAGAMENTOS P ON E.ID_ENCOMENDA = P.ID_ENCOMENDA
LEFT JOIN 
    ESTADO_PAGAMENTO EP ON P.ID_ESTADO = EP.ID_ESTADO;

    
