function test_heart()
    S = readmatrix('heart_test.csv', 'Delimiter', ',', 'DecimalSeparator', '.'); 
    load("heart1.mat", "net");
    in = S';
    t = [1 1 1 0 0 0];
    
    out = sim(net, in);
    out = out>=0.5;
    erro = perform(net,t,out);
    fprintf("Erro: %f\n",erro);
    fprintf("Accuracy: %f\n",(1-erro)*100);
end