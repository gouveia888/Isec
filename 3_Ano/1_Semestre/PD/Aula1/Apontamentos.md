Protocolo UDP

Servidor:

        Construtor: 
             try{
            	socket = new DatagramSocket( SERVICE_PORT );
        	}catch 
        Metedo serviceClients:
            if(socket == null) return; //verifica se o socket é nulo
            byte[] buffer = new byte[BUFSIZE]; //definir um buffer grande o suficiente para qualquer cliente
            ciclo infinito con try with resources
            criar datagramPacket para leitura
                espera pelo packets (socket.receive(receivePacket))
                tratar dados
                converrter os dados para bytes
                definir data e length do DatagramPacket 
                criar DatagramPacket de resposta
                enviar o socket 

        Obter um ficheiro do servidor:
            Obter o fileName e o localDirectory
            verifica se existe se é diretorio se pode escrever 
            try{
                localFilePath = localDirectory.getCanonicalPath()+File.separator+fileName;
            }catch 

            try(    FileOutputStream localFileOutputStream = new FileOutputStream(localFilePath);
                    DatagramSocket socket = new DatagramSocket()){

                    System.out.println("Ficheiro " + localFilePath + " criado.");

                    serverAddr = InetAddress.getByName(args[0]);
                    serverPort = Integer.parseInt(args[1]);

                    socket.setSoTimeout(TIMEOUT*1000);

                    packet = new DatagramPacket(fileName.getBytes(), fileName.length(), serverAddr, serverPort);
                    socket.send(packet);

                    System.out.println("Socket receive buffer size: " + socket.getReceiveBufferSize() + " bytes");

                    do{

                        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                        socket.receive(packet);

                        /* TODO */
                        //Se o datagrama recebido provém do servidor,
                        //guardar no ficheiro os bytes recebidos e
                        //atualizar receivedBytes e nChunks

                            //comparar o valor do porto com o servidor e se o objeto e igual ao do servidor
                        if(packet.getPort() == serverPort && packet.getAddress().equals(serverAddr)){
                            ++nChunks;

                            localFileOutputStream.write(packet.getData(), 0, packet.getLength());
                        }

                    }while(packet.getLength() > 0);

                    System.out.println("Transferencia concluida.");

            }catch

Cliente:

        InetAddress serverAddress = null; // a class InetAdres encapsula um IP
        DatagramPacket packet = null;     //socket
        int serverPort;
        String response;

        try(DatagramSocket socket = new DatagramSocket()){ //criar socket
            serverPort = Integer.parseInt(args[1]);         
            serverAddress = InetAddress.getByName(args[0]);
            socket.setSoTimeout(TIMEOUT*10000);
            packet = new DatagramPacket(comando.getBytes(), comando.length(), serverAddress, serverPort); //construir o packet

            socket.send(packet); //envio da mensagem

            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE); //cria um novo packet 
            socket.receive(packet); //recebe o packet

            response = new String(packet.getData(), 0, packet.getLength()); //trascreve a mensagem para uma string
            System.out.println(response);

        }

    Obter ficheiros:
           Obter o fileName e o localDirectory
           verifica se existe se é diretorio se pode escrever 

            try(
              FileOutputStream localFileOutputStream = new FileOutputStream(localFilePath);
              Socket socket = new Socket(args[0],Integer.parseInt(args[1]));
              PrintStream pout = new PrintStream(socket.getOutputStream(), true);
            ){

                System.out.println("Ficheiro " + localFilePath + " criado.");

                socket.setSoTimeout(TIMEOUT*1000); //definir um timeout

                byte[] buff = new byte[MAX_SIZE];
                int nBytes;

                do{
                    nBytes = socket.getInputStream().read(buff); //ler bytes ate echer(4001 tamanho do buffer) ou msg total
                    if(nBytes > 0){ //se for fechado vem imediatamente 0 senao a ligaçao ainda esta ativa

                        localFileOutputStream.write(buff, 0, nBytes);
                        ++nChunks;
                        receivedBytes++;
                    }

                }while(nBytes > 0);

                System.out.println("Transferencia concluida.");

            }catch