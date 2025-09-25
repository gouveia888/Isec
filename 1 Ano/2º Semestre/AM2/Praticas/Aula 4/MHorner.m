function [p] = MHorner(a,x)

% MHorner: função que aplica o Método de Horner  
%   p_n(x)=((((a_{n}*x+a_{n-1})*x+a_{n-2})*x+...a_{1})*x+a_{0}
%Input:
%   a - vector com os coeficientes do polinómio [a0,a1,...,an]
%   x - valor ou valor(es) reais para os quais se 
%   pretende calcular o valor do polinómio
%Output: 
%   p - valor ou vetor dos valores do polinómio em x
%   20/03/24 - Arménio Correia | armenioc@isec.pt

n = length(a);
p = a(n);
for i = n-1:-1:1
    p = p.*x+a(i);
end

end

