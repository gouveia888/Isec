function [t, y] = Adams_Bashworth(f, a, b, y0, n)
%Adams_Bashworth Método de dois passos Adams Bashforth
% Método de resoluçao de PVI y' = f(t,y) com condiçao incial y(0) = y0
% usa n passos de t = a até t = b. O primeiro passo de t = a ate t = a + h
% este metodo basea-se no metodo de Euler Modificado.
%INPUT:
%   f - função da EDO y'=f(t,y)
%   [a,b] - intervalo de valores da variável independente t
%   n - número de subintervalos ou iterações do método
%   y0 - aproximação inicial y(a)=y0 e z(a) = y0
%OUTPUT:
%   t - vetor do intervalo [a,b] discretizado 
%   y - vetor das soluções aproximadas do PVI em cada um dos t(i)
% Alunos: Manuel Furtado a2023154006 
%         Tiago Filipe a2019112767

h=(b-a)/n;
t=a:h:b;
y=zeros(1,length(t));
y(1)=y0;

k1=h*f(t(1),y(1));
k2=h*f(t(1)+h,y(1)+k1);
y(2)=y(1)+1/2*(k1+k2);
for i=2:length(t)-1   
y(i+1)=y(i)+(3/2)*h*f(t(i),y(i))-.5*h*f(t(i-1),y(i-1));
end
end