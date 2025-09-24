% Alínea a) - Trabalho Prático

clear all; close all;

% Classes e diretório das imagens
classes = {'circle', 'kite', 'parallelogram', 'square', 'trapezoid', 'triangle'};
caminho = 'C:\Users\tiago\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\start';

% Pré-processamento das imagens
[entradas, saidas] = preProcessarImagens(caminho, classes);

% Criação da rede
rede = feedforwardnet([10]); %quantidade de nos
%rede.layers{1}.transferFcn = 'logsig'; %camada de entrada
%rede.layers{2}.transferFcn = 'tansig'; %camada escondida
rede.trainFcn = 'trainlm';
rede.trainParam.epochs = 100;
rede.divideFcn = ''; % Usa todos os dados para treino

% Treinamento
[rede, infoTreino] = train(rede, entradas, saidas);

% Simulação
previsoes = sim(rede, entradas);
disp(class(previsoes));

% Avaliação
erro = perform(rede, saidas, previsoes);
fprintf("Erro Global = %.2f\n", erro);

[~, pred] = max(previsoes);
[~, real] = max(saidas);
precisao = sum(pred == real) / numel(real) * 100;
fprintf("Precisão: %.2f%%\n", precisao);

% Visualização
plotconfusion(saidas, previsoes);
%plotperform(infoTreino);
view(rede);

% Guarda a rede
save('rede_inicial.mat', 'rede');
