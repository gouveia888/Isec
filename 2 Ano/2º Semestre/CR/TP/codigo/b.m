clear all;
close all;

% Definir classes e caminho
classes = {'circle', 'kite','parallelogram', 'square', 'trapezoid', 'triangle'};
CaminhoPasta = 'C:\Users\Gouveia\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\ImageFolders\train';

% Pré-processar imagens
[inputs, targets] = preProcessarImagens(CaminhoPasta, classes);

% Configurações dos testes
configs = {
    {'Conf1', 5,     {'tansig', 'purelin'},              'trainlm',  [0.7 0.15 0.15]},
    {'Conf2', 10,    {'tansig', 'purelin'},              'trainlm',  [0.7 0.15 0.15]},
    {'Conf3', [5 15], {'tansig', 'tansig', 'purelin'},   'trainlm',  [0.7 0.15 0.15]},
    {'Conf4', [10 15], {'tansig', 'tansig', 'purelin'},  'trainlm',  [0.7 0.15 0.15]},
    {'Conf5', [10],  {'tansig', 'purelin'},              'traingd',  [0.7 0.15 0.15]},
    {'Conf6', [10],  {'tansig', 'purelin'},              'trainbfg', [0.7 0.15 0.15]},
    {'Conf7', [10],  {'tansig', 'purelin'},              'trainlm',  [0.7 0.15 0.15]},
    {'Conf8', [10],  {'tansig', 'purelin'},              'trainrp',  [0.7 0.15 0.15]},
    {'Conf9', [10],  {'logsig', 'purelin'},              'trainlm',  [0.7 0.15 0.15]},
    {'Conf10', [10], {'tansig', 'logsig'},               'trainlm',  [0.7 0.15 0.15]},
    {'Conf11', [10], {'purelin', 'logsig'},              'trainlm',  [0.7 0.15 0.15]},
    {'Conf12', [10], {'logsig', 'tansig'},               'trainlm',  [0.7 0.15 0.15]},
    {'Conf13', [10], {'radbasn', 'tansig'},              'trainlm',  [0.7 0.15 0.15]},
    {'Conf14', [10], {'tansig', 'purelin'},              'trainlm',  [0.6 0.2 0.2]},
    {'Conf15', [10], {'tansig', 'purelin'},              'trainlm',  [0.8 0.1 0.1]},
    {'Conf16', [10], {'tansig', 'purelin'},              'trainlm',  [0.5 0.2 0.3]}
};

% Armazenar resultados e redes
resultados = {};
todasRedes = {};

% Testar cada configuração
for i = 1:length(configs)
    conf = configs{i};
    nomeConf = conf{1};
    topologia = conf{2};
    funcoesAtivacao = conf{3};
    funcaoTreinamento = conf{4};
    divisao = conf{5};

    % Exibir configurações
    fprintf('Configuração: %s\n', nomeConf);
    fprintf('Topologia: %s\n', mat2str(topologia));
    fprintf('Funções de Ativação: %s\n', strjoin(funcoesAtivacao, ' '));
    fprintf('Função de Treinamento: %s\n', funcaoTreinamento);
    fprintf('Divisão: %.2f / %.2f / %.2f\n', divisao(1), divisao(2), divisao(3));

    % Inicializar variáveis para armazenar os resultados das 10 repetições
    erroTotal = 0;
    accGlobalTotal = 0;
    accTesteTotal = 0;

    % Realizar 10 repetições para cada configuração
    for rep = 1:10
        % Criar e configurar rede
        rede = feedforwardnet(topologia);
        rede.trainFcn = funcaoTreinamento;
        rede.trainParam.epochs = 100;
        rede.divideParam.trainRatio = divisao(1);
        rede.divideParam.valRatio = divisao(2);
        rede.divideParam.testRatio = divisao(3);

        % Definir funções de ativação
        for j = 1:length(rede.layers)
            rede.layers{j}.transferFcn = funcoesAtivacao{min(j, length(funcoesAtivacao))};
        end

        % Treinar rede
        [rede, tr] = train(rede, inputs, targets);

        % Simulação
        outputs = sim(rede, inputs);

        % Calcular métricas
        erro = perform(rede, targets, outputs);
        [~, pred] = max(outputs);
        [~, real] = max(targets);
        accGlobal = sum(pred == real) / length(real) * 100;

        % Precisão do teste
        outTeste = outputs(:, tr.testInd);
        tTeste = targets(:, tr.testInd);
        [~, predTeste] = max(outTeste);
        [~, realTeste] = max(tTeste);
        accTeste = sum(predTeste == realTeste) / length(realTeste) * 100;

        % Acumular os resultados das repetições
        erroTotal = erroTotal + erro;
        accGlobalTotal = accGlobalTotal + accGlobal;
        accTesteTotal = accTesteTotal + accTeste;
    end

    % Calcular as médias dos resultados das 10 repetições
    erroMedio = erroTotal / 10;
    accGlobalMedio = accGlobalTotal / 10;
    accTesteMedio = accTesteTotal / 10;

    % Armazenar os resultados médios e redes
    resultados = [resultados; {nomeConf, erroMedio, accGlobalMedio, accTesteMedio}];
    todasRedes{i}.nome = nomeConf;
    todasRedes{i}.net = rede;
    todasRedes{i}.accTeste = accTesteMedio;

    % Exibir os resultados médios
    fprintf('Média do Erro: %.8f\n', erroMedio);
    fprintf('Média da Precisão Global: %.2f%%\n', accGlobalMedio);
    fprintf('Média da Precisão de Teste: %.2f%%\n\n', accTesteMedio);
end
 
Ordenar e salvar as melhores redes atraves de accTesteMedio
[~, idxOrdenado] = sort(cell2mat(resultados(:,4)), 'descend');
for k = 1:min(3, length(idxOrdenado))
    rede = todasRedes{idxOrdenado(k)}.net;
    nomeFicheiro = sprintf('bestB%d.mat', k);
    save(nomeFicheiro, 'rede');
end
