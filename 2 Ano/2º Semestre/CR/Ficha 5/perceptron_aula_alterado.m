function [w, out_init, out_sim] = perceptron_aula_alterado(log_op)
%  Implementacao de um perceptrao para aprender as funcoes logicas AND, OR, NAND e XOR
%  Vector out_init devolve o resultado da simulacao inicial do perceptrao (aleatorio) 
%  Vector out_sim devolve o resultado da simulacao final do perceptrao (depois do treino)

% Definição das entradas das operações lógicas
in = [0 0 1 1; 
      0 1 0 1];

n_ent = length(in(:,1));
n_ex = length(in(1,:));

% Inicialização dos pesos com valores aleatórios
w = rand(1, n_ent + 1);
coeff = 1.0;
bias = 1.0;

% Definição dos targets e símbolos para plotagem
switch log_op
    case 'AND'
        target = [0 0 0 1];
        s = {'or', 'or', 'or', '*b'};
    case 'OR'
        target = [0 1 1 1];
        s = {'or', '*b', '*b', '*b'};
    case 'NAND'
        target = [1 1 1 0];
        s = {'*b', '*b', '*b', 'or'};
    case 'XOR'
        target = [0 1 1 0];
        s = {'or', '*b', '*b', 'or'};
end

% PARTE 1: Simulação inicial
fprintf('************\nA SIMULAR O PERCEPTRAO ALEATORIO\n************\n\n')

out_init = zeros(1, n_ex);
for j = 1:n_ex
    S = w(1) * bias;
    for k = 1:n_ent
        S = S + w(k+1) * in(k, j);
    end
    out_init(j) = S >= 0;
end

% PARTE 2: TREINAMENTO DO PERCEPTRON
fprintf('************\nA TREINAR O PERCEPTRAO\n************\n')

for it = 1:10
    fprintf('Iteracao %d\n', it);
    
    out = zeros(1, n_ex);
    delta = zeros(1, n_ex);

    for j = 1:n_ex
        S = w(1) * bias;
        for k = 1:n_ent
            S = S + w(k+1) * in(k, j);
        end

        out(j) = S >= 0;
        delta(j) = target(j) - out(j);

        % Atualizar os pesos
        for k = 1:n_ent
            w(k+1) = w(k+1) + coeff * in(k, j) * delta(j);
        end
        w(1) = w(1) + coeff * bias * delta(j);
    end

    % Plotagem da linha de decisão
    x1 = -1:0.1:2;
    if w(3) ~= 0
        x2 = -(w(2) * x1 + w(1)) / w(3);
        figure(1)
        plot(in(1,1), in(2,1), s{1}, in(1,2), in(2,2), s{2}, ...
             in(1,3), in(2,3), s{3}, in(1,4), in(2,4), s{4});
        axis([-0.5 1.5 -0.5 1.5]);
        xlabel('x1')
        ylabel('x2')
        hold on
        plot(x1, x2, 'LineWidth', 2)
        hold off
        pause(1)
    end

    % Para se o erro for zero
    if ~any(delta)
        break
    end
end

fprintf('********************\nFIM DO TREINO\n********************\n\n')

% PARTE 3: TESTE FINAL
fprintf('************\nA TESTAR O PERCEPTRAO DEPOIS DA APRENDIZAGEM\n************\n\n')

out_sim = zeros(1, n_ex);
for j = 1:n_ex
    S = w(1) * bias;
    for k = 1:n_ent
        S = S + w(k+1) * in(k, j);
    end
    out_sim(j) = S >= 0;
end
end
