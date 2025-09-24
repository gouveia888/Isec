function paridade_par()
%Funcao rn3b: cria, treina e testa uma RN feedforward
%usando as funcoes da NNTool

% limpar
clear all;
close all;

% inicializar entrada                   
% NO TP SAO AS IMAGENS TOOLBOX READIMAGE E CONVERTER PARA MATRIZ colunas
% vao ser as imagens
%no target cada linha vai corresponder a cada tipo l1 circulos, l2
%quadrados
p = [0 0 0 1 1 1 0 0; 
     0 1 0 0 1 1 1 0;
     0 0 1 0 0 1 1 1;
     0 0 0 0 0 1 1 1];

t = [1 0 0 0 1 1 0 1]

% COMPLETAR: criar RN chamada net
net=feedforwardnet;
n.divideFcn="";
% COMPLETAR: ajustar os parametros seguintes
net = train(net, p, t)
% FUNCAO DE ATIVACAO DA CAMADA DE SAIDA
out_sim = sim(net,p)
out_sim = out_sim>=0.5;
disp(out_sim)


% COMPLETAR simular a rede e guardar o resultado na variavel y
y = sim(net,p)
% Mostrar resultado
y = (y >= 0.5);
fprintf('Saida da RN:');
disp(y);
fprintf('Saida desejada:');
disp(t);
erro = perform(net,t,y);
fprintf("Erro: %f\n",erro);
fprintf("Accuracy: %f\n",(1-erro)*100);

end
