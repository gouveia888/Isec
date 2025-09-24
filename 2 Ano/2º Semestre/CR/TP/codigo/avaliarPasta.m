function avaliarPasta(net, pasta, classes, nomePasta)
    %recebe uma rede treinada(em b) ou c)) e usa-a para classificar as imgs
    %de uma pasta
    [inputs, targets] = preProcessarImagens(pasta, classes); % PastaStart/PastaTrain/PastaTest
    [~, ~, testInd] = dividerand(size(inputs,2), 0.7, 0.15, 0.15); %proporções
    outputs = sim(net, inputs);  %simular na pasta pretendida 
    %Avaliar precisão
    outTeste = outputs(:, testInd);
    tTeste = targets(:, testInd);
    [~, pred] = max(outTeste);
    [~, real] = max(tTeste);
    acc = sum(pred == real) / length(real) * 100;
    fprintf("Precisão de Teste na pasta %s: %.8f%%\n", nomePasta, acc);
end

% Matriz de confusão e visualização
%plotconfusion(targets, outputs);