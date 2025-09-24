    --S1
create table CLIENTES_BACKUP AS SELECT * FROM CLIENTES;

Select * from clientes_backup;

Insert into clientes_backup values (11,'Jo�o Moreira', 1020304, 'Rua Palheiros das Ondas � 1030 775 Santar�m, Portugal', 243123456);

Select * from clientes_backup;





Select * from clientes_backup;






Update clientes_backup set telefone = 239555555 where codigo_cliente = 8;


Select * from clientes_backup;




Commit;

Select * from clientes_backup;







Select * from clientes_backup;





Drop table funcionario;

Insert into funcionario values (
987654321 , 'Joaquim Almeida', 239534123
);

Update clientes_backup set telefone = 239111111
    where codigo_cliente = 1;







Update clientes_backup set n_contribuinte = 959595959
where codigo_cliente =2;