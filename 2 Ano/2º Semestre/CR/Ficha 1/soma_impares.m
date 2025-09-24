function [num] = soma_impares(m)
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here
        indices = find(mod(m,2)~=0); %encontra impares
        impares = m(indices); %indexaçao dos indices dos impares no vetor
        num = sum(impares); %soma dos impares do vetor
end