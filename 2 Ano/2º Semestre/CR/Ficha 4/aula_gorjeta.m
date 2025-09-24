function [fis] = gorjeta()

%PASSO 1 criar sistema fis
fis = mamfis;

%PASSO 2 VARIÁVEIS
fis = addInput(fis,[0,10],'Name',"servico");
fis = addInput(fis,[0,10],'Name',"comida");
fis = addInput(fis,[0,40],'Name',"tempo_espera");
fis = addOutput(fis,[0,30],'Name',"gorjeta"); 
       %COMPLETAR

%PASSO 3 FUNÇÕES DE PERTENÇA
fis = addMF(fis,"servico", "gaussmf",[1.5 0],'Name', "fraco");
fis = addMF(fis,"servico", "gaussmf",[1.5 5],'Name', "bom");
fis = addMF(fis,"servico", "gaussmf",[1.5 10],'Name', "excelente");
fis = addMF(fis,"comida", "trapmf",[0 0 1 3],'Name', "ma");
fis = addMF(fis,"comida", "trapmf",[7 9 11 19],'Name', "deliciosa");
fis = addMF(fis,"comida", "trapmf",[2 4 6 8],'Name', "aceitavel");
fis = addMF(fis,"tempo_espera", "trapmf",[0 0 5 15],'Name', "rapido");
fis = addMF(fis,"tempo_espera", "trapmf",[13 18 23 30],'Name', "razoavel");
fis = addMF(fis,"tempo_espera", "trapmf",[30 35 40 40],'Name', "lento");
fis = addMF(fis,"gorjeta", "trimf",[0 5 10],'Name', "fraca");
fis = addMF(fis,"gorjeta", "trimf",[10 15 20],'Name', "media");
fis = addMF(fis,"gorjeta", "trimf",[20 25 30],'Name', "generosa");

	%COMPLETAR para as três variáveis

	%PASSO 4 REGRAS
regra1 = "servico==fraco | comida==ma => gorjeta=fraca";
regra2 = "servico==bom => gorjeta=media";
regra3 = "servico==excelente | comida==deliciosa => gorjeta=generosa";
regra4 = "servico==bom & comida==deliciosa & tempo_espera==razoavel => gorjeta=media";
regra5 = "servico==excelente | comida==deliciosa | tempo_espera==rapido => gorjeta=media";
regra6 = "servico==bom | comida==aceitavel | tempo_espera==razoavel => gorjeta=media";
regras=[regra1 regra2 regra3 regra4 regra5 regra6];
fis = addRule(fis,regras);

%PASSO 5: avaliar para vários valores de servico e comida com evalfis
for servico=0:10
  	  for comida=0:10
          for tempo_espera=0:40
           entrada=[servico comida tempo_espera];
           out = evalfis(fis, entrada);
            fprintf('serviço = %d\nComida = %d\nTempo de espera = %d\nGorjeta = %f\n\n',servico, comida, tempo_espera, out);
          end 
      end
 end
end

