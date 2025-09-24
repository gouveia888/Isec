clear all;
close all;

% Definir classes e caminho
classes = {'circle', 'kite','parallelogram', 'square', 'trapezoid', 'triangle'};
pastaImagens = 'C:\Users\tiago\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\test';

% Pré-processamento das imagens
[entradas, saidas] = preProcessarImagens(pastaImagens, classes);
% Carregar as melhores redes de b
%load('bestB1.mat');
load('bestB2.mat');
%load('bestB3.mat');
% Simulação da rede e cálculo de erro
previsoes = sim(rede, entradas);
erroTotal = perform(rede, saidas, previsoes);   
fprintf("Erro Total = %.8f\n", erroTotal);    

% Cálculo da precisão teste
[~, previsaoClasses] = max(previsoes);      
[~, classesReais] = max(saidas);             
precisaoGlobal = sum(previsaoClasses == classesReais) / length(classesReais) * 100;
fprintf("Precisão Global: %.8f%%\n", precisaoGlobal);  

% Matriz de confusão para o teste
plotconfusion(saidas, previsoes); 

% Visualizar a estrutura da rede
view(rede);                                   
