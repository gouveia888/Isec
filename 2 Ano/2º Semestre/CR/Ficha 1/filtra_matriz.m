function [p] = filtra_matriz(m)
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
    p=[];
    % for i = 1 : numel(m)
    %     if mod(m(i),2) == 0
    %         p = [p m(i)];
    %     end
    % end

    indices = find(mod(m,2)==0);
    p = m(indices);

end