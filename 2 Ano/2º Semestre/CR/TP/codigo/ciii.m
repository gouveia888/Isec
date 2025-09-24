function [accTeste, accGlobal, rede, saidasTotal, saidasPrevistas] = TreinarETestar_c_iii(nomeRede)
    % Função que recebe uma rede de b), treina e testa usando as pastas Start, Train e Test.
    % Retorna as precisões de teste e global da rede, além da própria rede para armazenamento.
    disp(nomeRede);
    classes = {'circle', 'kite','parallelogram', 'square', 'trapezoid', 'triangle'};
    caminhoPastaTest = 'C:\Users\Gouveia\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\test';
    caminhoPastaStart = 'C:\Users\Gouveia\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\start';
    caminhoPastaTrain = 'C:\Users\Gouveia\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\train';
    
    % Pré-processando as imagens de todas as pastas (Start, Train e Test)
    [entradasStart, saidasStart] = preProcessarImagens(caminhoPastaStart, classes);
    [entradasTrain, saidasTrain] = preProcessarImagens(caminhoPastaTrain, classes);
    [entradasTest, saidasTest] = preProcessarImagens(caminhoPastaTest, classes);
    
    % Unindo os dados de entrada e saída para todas as pastas
    entradasTotal = [entradasStart, entradasTrain, entradasTest];
    saidasTotal = [saidasStart, saidasTrain, saidasTest];
    
    % Carregando a rede já treinada de b), utilizando o nome 'rede'
    load(nomeRede, 'rede'); % Carrega a rede com o nome 'rede'
    
    % Verificando se a rede foi carregada corretamente
    if ~exist('rede', 'var')
        error('A variável "rede" não foi encontrada no arquivo "%s".', nomeRede);
    end
    
    % Realizando o treinamento da rede com todos os dados de entrada
    [rede, tr] = train(rede, entradasTotal, saidasTotal);
    
    % Realizando a simulação da rede usando os dados de entrada
    saidasPrevistas = sim(rede, entradasTotal);
    
    % Calculando a precisão global considerando todos os dados
    [~, pred] = max(saidasPrevistas);      
    [~, real] = max(saidasTotal);    
    accGlobal = sum(pred == real) / length(real) * 100;
    
    % Calculando a precisão específica para o conjunto de teste
    saidasTeste = saidasPrevistas(:, tr.testInd);        
    saidasReaisTeste = saidasTotal(:, tr.testInd);          
    [~, predTeste] = max(saidasTeste);          
    [~, realTeste] = max(saidasReaisTeste);              
    accTeste = sum(predTeste == realTeste) / length(realTeste) * 100;
    
    % Exibindo as precisões de teste e global
    fprintf("Precisão de Teste: %.8f%%\n", accTeste); 
    fprintf("Precisão Global: %.8f%%\n", accGlobal);
    
    % Avaliando a rede nas pastas Start, Train e Test
    avaliarPasta(rede, caminhoPastaStart, classes, 'Start');
    avaliarPasta(rede, caminhoPastaTrain, classes, 'Train');
    avaliarPasta(rede, caminhoPastaTest, classes, 'Test');
    
    % (Opcional) Gerar a matriz de confusão para os resultados
    plotconfusion(saidasTotal, saidasPrevistas);
    
    % (Opcional) Exibir o gráfico da estrutura da rede neural
    % view(rede);
end
