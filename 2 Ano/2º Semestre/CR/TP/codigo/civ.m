%Recebe uma rede previamante guardada, corre a função TrainETeste_c_iii x vezes e guarda a melhor rede das x treinadas
%Esta função utiliza as redes guardadas em b. Se se utiliza Bx guarda no ficheiro Cx. 

close all;
clear all;

bestAccTest = 0;
bestAccGlobal = 0;

%Qual das redes de b) se quer fazer load
%nomeRede = 'bestB1.mat';
%nomeRede = 'bestB2.mat';
nomeRede = 'bestB3.mat';

for i = 1 : 10
    fprintf("\nIteração %d:\n", i);
    [accTesteAtual,accGlobalAtual,rede, saidasTemp, PrevistasTemp] = ciii(nomeRede);
    if accTesteAtual >= bestAccTest && accGlobalAtual >= bestAccGlobal
        bestAccGlobal = accGlobalAtual;
        bestAccTest = accTesteAtual;
        bestRede = rede;
        saidasTotal = saidasTemp;
        saidasPrevistas = PrevistasTemp;
    end
end
fprintf("\n\nMelhor das redes:");
fprintf("Precisão de Teste: %.8f%%\n", bestAccTest); 
fprintf("Precisão Global: %.8f%%\n", bestAccGlobal); 
rede = bestRede;

% (Opcional) Gerar a matriz de confusão para os resultados
plotconfusion(saidasTotal, saidasPrevistas);

%Guardar a melhor das redes treinadas em b
%save('bestC1.mat', "rede");
%save('bestC2.mat', "rede");
save('bestC3.mat', "rede");
