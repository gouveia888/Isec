% Caminho para as imagens desenhadas à mão
thisFilePath = mfilename('fullpath');
pastaCode = fileparts(thisFilePath);
pastaProject = fileparts(pastaCode);
pastaImages = fullfile(pastaProject, 'IMG'); 
pastaHandDrawn = fullfile(pastaImages, 'IMGDESENHADAS');

% Definir as classes
classes = {'circle', 'kite', 'parallelogram', 'square', 'trapezoid', 'triangle'};

% 1. Pré-processar as imagens desenhadas à mão
[inputs, targets] = preProcessarImagens(pastaHandDrawn, classes);

% Verifique o tamanho das entradas e saídas
disp(['Tamanho de inputs: ', num2str(size(inputs))]);
disp(['Tamanho de targets: ', num2str(size(targets))]);

% 2. Carregar a rede neuronal treinada
load('C:\Users\Gouveia\OneDrive - ISEC\Informática\2 ano\2º Semestre\CR\TP\codigo\bestB1.mat', 'rede'); 

% Verificar o tamanho esperado pela rede
inputSize = rede.inputs{1}.size;
disp(['Tamanho esperado das entradas: ', num2str(inputSize)]);

% Verificar o tamanho real dos inputs
disp(['Tamanho real das entradas: ', num2str(size(inputs))]);

% Ajustar inputs se necessário (por ex., redimensionar para 625)
if size(inputs, 1) ~= inputSize
    inputs = reshape(inputs, inputSize, []);
end

% Verificação final do tamanho
disp(['Tamanho ajustado das entradas: ', num2str(size(inputs))]);

% 3. Simular a rede nos dados de entrada
outputs = sim(rede, inputs);

% Normalizar outputs para que representem probabilidades
outputs = outputs ./ sum(outputs, 1);

% 4. Cálculo da precisão global
[~, pred] = max(outputs);  % Índice da classe predita
[~, real] = max(targets);  % Índice da classe real
accGlobal = sum(pred == real) / length(real) * 100;
fprintf("Precisão Global: %.2f%%\n", accGlobal); 

% 5. Mostrar a matriz de confusão
figure;
plotconfusion(targets, outputs);

% 6. Mostrar classificação individual com confiança
disp('--- Classificação Individual das Imagens ---');
numImagens = size(inputs, 2);

for i = 1:numImagens
    classeReal = classes{real(i)};
    classePredita = classes{pred(i)};
    correta = strcmp(classeReal, classePredita);
    simbolo = char(10003 * correta + 10007 * ~correta);  % ✓ ou ✗

    % Probabilidade (confiança) na classe prevista
    valorSaida = outputs(:, i);
    [prob, idx] = max(valorSaida);
    confianca = prob * 100;

    fprintf('Imagem %2d → Real: %-15s | Prevista: %-15s %s | Confiança: %.2f%%\n', ...
        i, classeReal, classePredita, simbolo, confianca);
end
