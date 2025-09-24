--Pergunta 1
Select nome, sal, premios
from emp
where premios >= 0;

--Pergunta 2
Select * from emp
where ndep = 20 or ndep = 30;

--Pergunta 3
Select nome from emp
where ndep = 10 or ndep = 20;

--Pergunta 4
Select nome, funcao
from emp
where nome like '%T%' or nome like '%N%';

--Pergunta 5
Select nome, funcao
from emp
where (nome like 'B%' and nome like '%EN%') 
or (nome like 'M%' and nome like '%RI%');

--Pergunta 6
Select nome, funcao, ((sal*12)+NVL(premios,0)) as "Remuneração"
from emp;

--Pergunta 7
Select nome, (sal*0.15*12) "15% do Salario", nvl(premios,0) premios
from emp
where funcao = 'GESTOR'
order by (sal*0.15*12), nome;   

--Pergunta 8
Select nome from emp
where nome like 'MARIA%';

--Pergunta 9
Select nome, data_entrada from emp
where funcao = 'PRESIDENTE';

--Pergunta 10
Select emp.nome, emp.funcao, dep.nome Departamento from emp, dep
where emp.ndep=dep.ndep and dep.nome='CONTABILIDADE';
