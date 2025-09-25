function [t,y]=F_ODE45(f,a,b,n,y0)
%F_ODE45  ODE45 Matlab Para fazer calculos de PVI/EDO.
%   [t,y] = F_ODE45(f,a,b,n,y0) Método numérico para a resolução numérica
% de um PVI/EDO
%   y'= f(t,y) com t=[a, b] intervalo de valores de variaveis independentes
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
tspan=a:h:b;

[t,y] = ode45(f,tspan,y0);

if n==1
    y=[y0;y(end)];
end

y=y.';