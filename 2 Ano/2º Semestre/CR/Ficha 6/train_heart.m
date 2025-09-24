function  train_heart()
    S = readmatrix('heart_train.csv', 'Delimiter', ',', 'DecimalSeparator', '.'); 
    in = S(:,1:13)';
    t  = S(:,14)';

    net = feedforwardnet([20 20 25]);
    %net.divideFcn=""; %sem isto fica com melhores resultaddos no teste
    %alinea a do tp com isto
    net = train(net,in,t);
    out = sim(net, in);
    out = out>=0.5;
    erro = perform(net,t,out);
    fprintf("Erro: %f\n",erro);
    fprintf("Accuracy: %f\n",(1-erro)*100);
    save('heart1.mat', 'net');
end