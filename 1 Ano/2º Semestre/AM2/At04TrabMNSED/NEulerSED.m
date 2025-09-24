function [t,u,v] = NEulerSED(f,g,a,b,n,u0,v0)
%NEulerSED 
% Resumo da função
%INPUT:
%   f - função de ordem 1 de u
%   g - função de ordem 1 de v
%   [a,b] - intervalo de valores da variável independente t
%   n - número de subintervalos, iterações do método
%   u0 - condição inicial f(a)=u0
%   v0 - condição inicial g(a)=v0
%OUTPUT:
%   [t, u, v] - vetor das soluções aproximada
%   t - pontos igualmente espaçados no intervalo [a, b]
%   u - vetor das soluções aproximadas de u(t)
%   v - vetor das soluções aproximadas de v(t)
% 
% Alunos: Manuel Furtado a2023154006
%         Tiago Filipe a2019112767
%   10/04/2024  Arménio Correia     armenioc@isec.pt
h = (b-a)/n;
t = a:h:b;
u = zeros(1,n+1); v = zeros(1,n+1);
u(1) = u0; v(1) = v0;
for i = 1:n
    u(i+1) = u(i)+h*f(t(i),u(i),v(i));
    v(i+1) = v(i)+h*g(t(i),u(i),v(i));
end
end