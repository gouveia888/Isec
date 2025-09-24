function [t,y] = NRK4(f,a,b,n,y0)
%NRK4 Método de Runge-Kutta de ordem 4 para resolução numérica de EDO/PVI
%   y=NRK4(f,a,b,n,y0)
%   y'=f(t,y), com t=[a,b] e y(a)=y0 (condição inicial)
% k1 = f(t(i), y(i))
%     k2 = f(t(i)+(h/2), y(i)+(h*k1)/2)
%     k3 = f(t(i)+(h/2), y(i)+h*(k2/2))
%     k4 = f(t(i)+h, y(i)+(h*k3))
% 
%     y(i+1)=y(i)+(h/6)*(k1+2*k2+2*k3+k4)
%     t(i+1)=t(i)+h
%INPUT:
%   f - função do segundo membro da Equação Diferencial
%   [a,b] - intervalo de valores da variável independente t
%   n - número de subintervalos ou iterações do método
%   y0 - condição inicial y(a)=y0
%OUTPUT:
%   y - vetor das soluções aproximadas do PVI em cada um de t(i)
%
% Alunos: Manuel Furtado a2023154006
%         Tiago Filipe a2019112767

h = (b-a)/n;
t=a:h:b;
y=zeros(1,n+1);
y(1)=y0;


for i=1:n
    k1 = f(t(i), y(i));
    k2 = f(t(i)+(h/2), y(i)+(h*k1)/2);
    k3 = f(t(i)+(h/2), y(i)+h*(k2/2));
    k4 = f(t(i)+h, y(i)+(h*k3));
    
    y(i+1)=y(i)+(h/6)*(k1+2*k2+2*k3+k4);
    t(i+1)=t(i)+h;
    
end