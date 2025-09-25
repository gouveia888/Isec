function [t,y] = Euler_Melhorado(f,a,b,n,y0)
%Euler_Melhorado Método de Euler Melhorado/Modificado para resolução numérica de 
% EDO/PVI
%   y'=f(t,y), t=[a,b], y(a)=y0
%   y(i+1,1) = y(i)+(h/2)*(f(t(i),y(i))+f(t(i+1),z(i+1)));, i=0,1,2,...,n
%   z(i+1) = y(i)+h*f(t(i),y(i)); i=0,1,2,...,n
%INPUT:
%   f - função da EDO y'=f(t,y)
%   [a,b] - intervalo de valores da variável independente t
%   n - número de subintervalos ou iterações do método
%   y0 - aproximação inicial y(a)=y0 e z(a) = y0
%OUTPUT:
%   t - vetor do intervalo [a,b] discretizado 
%   y - vetor das soluções aproximadas do PVI em cada um dos t(i)
%
% Alunos: Manuel Furtado a2023154006 
%         Tiago Filipe a2019112767

h = (b-a)/n;
t = a:h:b;
y=zeros(n+1,1);
y(1) = y0;
z(1) = y0;

for i = 1:n
    z(i+1) = y(i)+h*f(t(i),y(i));
    y(i+1,1) = y(i)+(h/2)*(f(t(i),y(i))+f(t(i+1),z(i+1)));
end
y=y.';