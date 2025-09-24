exec naluno(2019112767);

--ex3
Select titulo, max(preco_unitario) "Preco_mais_Alto"
from livros, vendas, clientes
Where livros.codigo_livro = vendas.codigo_livro
and vendas.codigo_cliente = clientes.codigo_cliente
group by titulo
having count(vendas.codigo_venda) > 1
and count(distinct vendas.codigo_cliente) = 1
order by titulo desc ;

EXEC SQLCHECK('FFHVTJGCGIWCIUC');

--ex4

Select autores.nome, max(livros.preco_tabela) "Preco_mais_Alto"
from autores, livros, vendas
Where livros.codigo_autor=autores.codigo_autor
group by autores.nome 
order by autores.nome;

EXEC SQLCHECK('FFHXKOQDECHYJML');

--ex5
select titulo,preco_tabela  "Preco_mais_Alto"
from livros ,(select max(preco_tabela) maximo from livros where genero = 'Fantástico')
where preco_tabela = maximo  
group by titulo, preco_tabela;

EXEC SQLCHECK('FFNJCWNEXBHAKTA');

--ex6
Select titulo, paginas as "Num_Paginas", preco_tabela as "Preco_mais_Alto"
from livros
where preco_tabela >= all (Select preco_tabela from livros where genero = 'Romance')
group by titulo,paginas, preco_tabela;
EXEC SQLCHECK('FFVYLDWFIAPLLGL');

--ex7
select l.preco_tabela "Preco_mais_Baixo", l.titulo
from livros l
where not exists (select * from livros where genero = 'Informática' and preco_tabela < l.preco_tabela)
and l.genero = 'Informática';

EXEC SQLCHECK('FFEFWFPGQCTIMBC');

--ex8
select titulo "Livro Mais Barato"
from livros, (Select min(preco_tabela) maior from livros where genero like 'Fantástico')
where genero like 'Fantástico'
and preco_tabela = maior;
EXEC SQLCHECK('FFOIGNAHCIWWNCL');

--ex9

select nome
from livros,autores
where livros.codigo_autor=autores.codigo_autor
and paginas < (select avg(paginas) from livros)
group by nome
order by nome;

EXEC SQLCHECK('FFSSGUDIOUTLOUO');

--ex10
select nome, idade
from livros,autores
where livros.codigo_autor=autores.codigo_autor
group by nome, idade
having count(codigo_livro) < (select avg(total) from 
    (Select count(codigo_livro) total from livros group by codigo_autor)) --devolve a media de 16
order by nome;
EXEC SQLCHECK('FFUSGBLJSIHRPRZ');

--ex11
Select l.codigo_autor, l.titulo, l.preco_tabela PRECO, min(l2.preco_tabela) PRECO_MINIMO , l.preco_tabela - min(l2.preco_tabela) DIFERENÇA
from livros l
join livros l2 on l.codigo_autor = l2.codigo_autor
where l.genero='Fantástico' 
group by l.codigo_autor, l.titulo, l.preco_tabela
order by l.codigo_autor, l.titulo;

EXEC SQLCHECK('FFRDYGEKWWBTQCK');

--ex12
Select genero, titulo, unidades_vendidas
from livros l
where unidades_vendidas = (select max(unidades_vendidas) from livros where genero = l.genero)
order by genero;
EXEC SQLCHECK('FFNASDLLQAIDRCF');
--  ex13
Select distinct titulo, to_char(round((livros.unidades_vendidas * 100) / soma ,1),'990.99') PERCENTAGEM
from livros, editoras, vendas, (Select sum(unidades_vendidas) soma
                      from livros, editoras e
                      where livros.codigo_editora=e.codigo_editora 
                      and upper(e.nome) like '%FCA%')
where livros.codigo_editora=editoras.codigo_editora 
and upper(editoras.nome) like '%FCA%'                    
order by 2 desc, titulo;    
EXEC SQLCHECK('FFVLVYUMBIZESBQ');

--ex14
Select titulo
from livros, clientes, vendas
where livros.codigo_livro = vendas.codigo_livro
and vendas.codigo_cliente = clientes.codigo_cliente
and clientes.morada like '%Coimbra%'
and vendas.quantidade = (
                            select max(quantidade) 
                            from vendas, clientes
                            where vendas.codigo_cliente = clientes.codigo_cliente
                            and clientes.morada like '%Coimbra%'
                            );
EXEC SQLCHECK('FFAUGMCNVBEKTJM');
                                                       