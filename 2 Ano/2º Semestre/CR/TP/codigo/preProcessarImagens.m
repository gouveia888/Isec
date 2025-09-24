function [inputs, targets ] = preProcessarImagens(CaminhoPasta, classes)
inputs = [];
targets = []; %Inicializa as matrizes de entrada e saida 
imgSize = [25 25]; %Todas as imagens serão redimensionadas para 25x25

    %Junta o nome da classe com o caminho da pasta, e lista todos os ficheiros 
    for i= 1:length(classes)
        path = fullfile(CaminhoPasta, classes{i});
        files = dir(fullfile(path, '*.png'));
         for j = 1:length(files)
            img = imread(fullfile(path, files(j).name)); %lê ficheiro
            if size(img,3) == 3 
            img = rgb2gray(img); %Converte para tons cinzentos
            end
            bin = imbinarize(imresize(img, imgSize));
            %Converte a imagem para binario e redimensiona para 32x32
            inputs(:, end+1) = bin(:); %Achata a imagem 
            %bin transforma a matriz 2D num vetor coluna
            t = zeros(length(classes),1); %vetro com numero de classes
            t(i) = 1;
            targets(:, end+1) = t;
         end
    end 
end
%inputs-> matriz com n colunas cada uma com uma imagem
%targets->uma matriz com m colunas, cada uma com un vetor com a classe
%correta