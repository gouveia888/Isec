--S2


Select * from clientes_backup;



Select * from clientes_backup;

Update clientes_backup
    set n_contribuinte = 999999999
    where codigo_cliente = 1;

Select * from clientes_backup;

ROLLBACK;

Select * from clientes_backup;


Update clientes_backup set nome = 'Manuel Moreira'
where codigo_cliente = 11;

Select * from clientes_backup;

Update clientes_backup set morada = 'Rua Ferreira Borges � 3000 179 Coimbra, Portugal'
where codigo_cliente = 8;



Select * from clientes_backup;

Create table funcionario (
    nif number PRIMARY key,
    nome varchar(255),
    telefone number
);

Select * from clientes_backup;

Insert into funcionario values (
987654321 , 'Joaquim Almeida', 239534123
);

Commit;

Drop table funcionario;





Update clientes_backup set telefone = 239222222
    where codigo_cliente = 2;

Update clientes_backup set n_contribuinte = 717171717
where codigo_cliente = 1;
