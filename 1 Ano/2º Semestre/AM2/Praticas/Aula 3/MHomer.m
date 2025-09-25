function [p] = MHomer(a,x)
%MHomer Summary of this function goes here
%parametros de entrada a(vetor dos coeficientes) e x e parametro de saida p
%   Detailed explanation goes here
n=length(a);
p=a(end);

    for i=n-1 :-1:1
        p = p.*x+a(i); %multiplicaçao de .* para * vetores
    end
end