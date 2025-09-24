function TreinarETestar_C_ii(nomeRede)
    % Definição das classes e dos caminhos das pastas de dados
    classes = {'circle', 'kite','parallelogram', 'square', 'trapezoid', 'triangle'};
    caminhoPastaTest = 'C:\Users\Gouveia\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\test';
    caminhoPastaStart = 'C:\Users\Gouveia\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\start';
    caminhoPastaTrain = 'C:\Users\Gouveia\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\train';
    
    % Processando as imagens da pasta de teste
    [entradas, saidas] = preProcessarImagens(caminhoPastaTest, classes);
    
    % Carregando a rede previamente treinada de b)
    load(nomeRede);  
    
    % Verificando se a variável 'rede' foi carregada corretamente
    if ~exist('rede', 'var')
        error('A rede "rede" não foi carregada corretamente do arquivo "%s".', nomeRede);
    end
    
    % Garantindo que a rede é do tipo esperado "network"
    if ~isa(rede, 'network')
        error('A variável "rede" não é uma rede válida do tipo "network".');
    end
    
    % Realizando o treinamento da rede com os dados de entrada
    [rede, tr] = train(rede, entradas, saidas);
    
    % Realizando a simulação da rede com os dados de entrada
    saidasPrevistas = sim(rede, entradas);
    
    % Calculando a precisão geral do modelo
    [~, previsao] = max(saidasPrevistas);
    [~, real] = max(saidas);
    precisaoGlobal = sum(previsao == real) / length(real) * 100;
    
    % Calculando a precisão no conjunto de teste específico
    saidasTeste = saidasPrevistas(:, tr.testInd);
    saidasReaisTeste = saidas(:, tr.testInd);
    [~, previsaoTeste] = max(saidasTeste);
    [~, reaisTeste] = max(saidasReaisTeste);
    precisaoTeste = sum(previsaoTeste == reaisTeste) / length(reaisTeste) * 100;
    
    % Exibindo as precisões calculadas
    fprintf("Precisão de Teste: %.8f%%\n", precisaoTeste); 
    fprintf("Precisão Global: %.8f%%\n", precisaoGlobal);
    
    % Avalia o desempenho da rede nas pastas Start, Train e Test
    avaliarPasta(rede, caminhoPastaStart, classes, 'Start');
    avaliarPasta(rede, caminhoPastaTrain, classes, 'Train');
    avaliarPasta(rede, caminhoPastaTest, classes, 'Test');
    
    % Gerando a matriz de confusão para os resultados do conjunto de teste
    plotconfusion(saidasReaisTeste, saidasTeste);
    
    % Visualizando a estrutura interna da rede neural
    view(rede);
end
