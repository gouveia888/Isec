function [w, out_init, out_sim] = perceptron_maioria()
%  Implementação de um perceptron para aprender a função de maioria
%  out_init devolve o resultado da simulação inicial (sem treino)
%  out_sim devolve o resultado após o treinamento

% Definição das entradas e do target
in =     [0 0 1 1 0 1; 
          0 1 0 0 0 1;
          0 0 1 0 0 1;
          0 1 0 1 0 1;
          1 0 0 1 0 1];

target = [0 0 0 1 0 1]; % Saída esperada
n_ex = length(in(1,:));
n_ent = length(in(:,1));

% Inicialização dos pesos com valores aleatórios
w = rand(1, n_ent+1);
b = 1;
coeff = 1;

% PARTE 1: Simulação inicial
fprintf('**********************************\n')
fprintf('A SIMULAR O PERCEPTRAO ALEATORIO\n')
fprintf('**********************************\n\n')

out_init = zeros(1, n_ex);
for j = 1:n_ex
    S = w(1) * b;
    for k = 1:n_ent
        S = S + w(k+1) * in(k,j);
    end
    out_init(j) = S >= 0;  
end

% PARTE 2: TREINAMENTO
fprintf('**********************************\n')
fprintf('A TREINAR O PERCEPTRAO MAIORIA\n')
fprintf('**********************************\n')

for it = 1:100
    fprintf('Iteração %d\n', it);
    
    out = zeros(1, n_ex);
    delta = zeros(1, n_ex);

    for j = 1:n_ex
        S = w(1) * b;
        for k = 1:n_ent
            S = S + w(k+1) * in(k,j);
        end

        out(j) = S >= 0;
        delta(j) = target(j) - out(j);

        % Atualizar os pesos
        w(1) = w(1) + coeff * b * delta(j);
        for k = 1:n_ent
            w(k+1) = w(k+1) + coeff * in(k,j) * delta(j);
        end
    end

    % Para se o erro for zero
    if ~any(delta)
        break
    end
end

fprintf('********************************************************\n')
fprintf('FIM DO TREINO\n')
fprintf('********************************************************\n\n')

% PARTE 3: TESTE FINAL
fprintf('**********************************\n')
fprintf('A TESTAR O PERCEPTRAO DEPOIS DA APRENDIZAGEM\n')
fprintf('**********************************\n\n')

out_sim = zeros(1, n_ex);
for j = 1:n_ex
    S = w(1) * b;
    for k = 1:n_ent
        S = S + w(k+1) * in(k,j);
    end
    out_sim(j) = S >= 0;
end   

% Verificação do aprendizado
if isequal(target, out_sim)
    fprintf('Aprendizagem feita com sucesso\n');
else
    fprintf('Aprendizagem não foi feita com sucesso\n');
end

end
