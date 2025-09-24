#include "Simulador.h"
#include <iostream>
#include <fstream>
#include <stdexcept>
#include <sstream>
#include "Caravana/Caravana.h"
#include "Caravana/Barbara.h"

using namespace std;

void Simulador::iniciar() {

    char nome_fich[50];
    string linha;

    do {
        cout << "\nInsira comando fase 1\n";
        getline(cin, linha);
        istringstream iss(linha);
        string cmd;
        iss >> cmd;

        if (cmd == "sair") {
            cout << "Saiu do programa com sucesso!" << endl;
            break;
        }

        if (cmd == "config") {
            if ((iss >> nome_fich)) {
                buffer.getmapa(nome_fich);
                cout << buffer.displaymapa();
                dados.getdados(nome_fich);
                cout << "\nMoedas: " << dados.getMoedas() << endl;
            }

            do {
                cout << "\nInsira comando fase 2\n";
                getline(cin, linha);
                istringstream iss(linha);
                iss >> cmd;

                if (cmd == "terminar") {
                    cout << "Saiu para a fase 1!" << endl;
                    break;
                }

                if (cmd == "exec") {
                    if ((iss >> nome_fich)) {
                        cout << "Executando comandos do arquivo: " << nome_fich << endl;
                        executarComandosDeArquivo(nome_fich);
                    } else {
                        cout << "Erro: Nome do arquivo nao fornecido." << endl;
                    }
                } else {
                    processarComando(cmd, iss);
                }

            } while (true);
        }

    } while (true);
}

void Simulador::processarComando(const string &cmd, istringstream &iss) {
    if (cmd == "prox") {
        int teste;
        iss >> teste;
        if (teste > 0)
            cout << "Numero de instantes a avançar: " << teste << endl;
        else
            cout << "Erro: Numero deve ser maior que zero." << endl;

    } else if (cmd == "comprac") {
        cout << "Comando fase 2!: " << cmd << endl;

    } else if (cmd == "precos") {
        dados.mostrarPrecos(); // Chama o metodo para mostrar os preços

    } else if (cmd == "cidade") {
        cout << "Comando fase 2!: " << cmd << endl;

    } else if (cmd == "caravana") {
        int idCaravana;
        iss >> idCaravana;

        // Busca a caravana pelo ID
        Caravana *caravana = Caravana::encontrarCaravanaPorId(idCaravana);
        if (caravana != nullptr) {
            caravana->exibirDetalhes(); // Chama o metodo para exibir detalhes da caravana
        } else {
            std::cout << "Caravana nao encontrada!" << std::endl;
        }

    } else if (cmd == "compra") {
        int idCaravana, quantidade;
        iss >> idCaravana >> quantidade;

        // Busca a caravana pelo ID
        Caravana *caravana = Caravana::encontrarCaravanaPorId(idCaravana);
        if (caravana != nullptr) {
            int custoTotal = quantidade * dados.getPrecoCompraMercadoria(); // Custo total da compra

            // Verifica se o jogador tem moedas suficientes
            if (dados.getMoedas() < custoTotal) {
                cout << "Moedas insuficientes para comprar " << quantidade << " toneladas de mercadorias." << endl;
                return;
            }

            // Atualiza a quantidade de mercadorias na caravana
            caravana->setMercadoria(caravana->getMercadoria() + quantidade);
            dados.setMoedas(-custoTotal); // Deduz o custo das moedas

            cout << "Comprados " << quantidade << " toneladas de mercadorias para a caravana " << idCaravana << "." << endl;
            cout << "Moedas restantes: " << dados.getMoedas() << endl;
        } else {
            cout << "Caravana nao encontrada!" << endl;
        }
    } else if (cmd == "vende") {
        int idCaravana, quantidade;
        iss >> idCaravana >> quantidade;

        // Busca a caravana pelo ID
        Caravana *caravana = Caravana::encontrarCaravanaPorId(idCaravana);
        if (caravana != nullptr) {
            // Verifica se a caravana tem mercadorias suficientes
            if (caravana->getMercadoria() < quantidade) {
                cout << "A caravana " << idCaravana << " nao tem mercadorias suficientes para vender." << endl;
                return;
            }

            int receitaTotal = quantidade * dados.getPrecoVendaMercadoria(); // Receita total da venda

            // Atualiza a quantidade de mercadorias na caravana
            caravana->setMercadoria(caravana->getMercadoria() - quantidade);
            dados.setMoedas(receitaTotal); // Adiciona a receita às moedas

            cout << "Vendidos " << quantidade << " toneladas de mercadorias da caravana " << idCaravana << "." << endl;
            cout << "Moedas atuais: " << dados.getMoedas() << endl;
        } else {
            cout << "Caravana nao encontrada!" << endl;
        }
    } else if (cmd == "move") {
        int idCaravana;
        string direcao;
        iss >> idCaravana >> direcao;

        // Busca a caravana pelo ID
        Caravana *caravana = Caravana::encontrarCaravanaPorId(idCaravana);
        if (caravana != nullptr) {
            caravana->mover(direcao, buffer, dados); // Chama o método de mover
            // Exibe o mapa atualizado
            cout << buffer.displaymapa();
        } else {
            cout << "Caravana nao encontrada!" << endl;
        }

    } else if (cmd == "auto") {
        int idCaravana;
        iss >> idCaravana;

        // Busca a caravana pelo ID
        Caravana *caravana = Caravana::encontrarCaravanaPorId(idCaravana);
        if (caravana != nullptr) {
            caravana->ativarAutoGestao(); // Ativa o modo automático
            cout << "Caravana " << idCaravana << " agora esta em modo automatico." << endl;
        } else {
            cout << "Caravana nao encontrada!" << endl;
        }

    } else if (cmd == "stop") {
        int idCaravana;
        iss >> idCaravana;

        // Busca a caravana pelo ID
        Caravana *caravana = Caravana::encontrarCaravanaPorId(idCaravana);
        if (caravana != nullptr) {
            caravana->desativarAutoGestao(); // Desativa o modo automático
            cout << "Caravana " << idCaravana << " agora esta em modo manual." << endl;
        } else {
            cout << "Caravana nao encontrada!" << endl;
        }

    } else if (cmd == "barbaro") {
        int linha, coluna;
        if (iss >> linha >> coluna) {
            // Verifica se a posição é válida
            if (buffer.posicaoValida(coluna, linha)) {
                // Cria uma nova caravana bárbara
                Barbara *novaBarbara = new Barbara(dados);
                novaBarbara->setPosicao(coluna, linha); // Define a posição da caravana

                // Atualiza o buffer com a nova posição da caravana
                buffer.atualizarPosicaoCaravana(novaBarbara->getId(), coluna, linha, true); // Passa true para isBarbara
                cout << "Caravana barbara criada na posicao (" << linha << ", " << coluna << ")." << endl;
                cout << buffer.displaymapa();
            } else {
                cout << "Erro: Posicao invalida para criar a caravana barbara." << endl;
            }
        } else {
            cout << "Erro: Linha e coluna nao fornecidas." << endl;
        }

    } else if (cmd == "areia") {
        cout << "Comando fase 2!: " << cmd << endl;

    } else if (cmd == "moedas") {
        int teste;
        if (iss >> teste) {
            dados.setMoedas(teste);
            cout << "Moedas atualizadas: " << dados.getMoedas() << endl;
        } else {
            cout << "Erro: Valor de moedas nao fornecido." << endl;
        }

    }  else if (cmd == "tripul") {
        int idCaravana, quantidade;
        iss >> idCaravana >> quantidade;

        // Busca a caravana pelo ID
        Caravana *caravana = Caravana::encontrarCaravanaPorId(idCaravana);
        if (caravana != nullptr) {
            // Verifica se o jogador tem moedas suficientes
            int custoTotal = quantidade; // 1 moeda por tripulante
            if (dados.getMoedas() < custoTotal) {
                cout << "Moedas insuficientes para comprar " << quantidade << " tripulantes." << endl;
                return;
            }

            // Atualiza a quantidade de tripulantes na caravana
            caravana->setTripulacao(caravana->getTripulacao() + quantidade);
            dados.setMoedas(-custoTotal); // Deduz o custo das moedas

            cout << "Comprados " << quantidade << " tripulantes para a caravana " << idCaravana << "." << endl;
            cout << "Moedas restantes: " << dados.getMoedas() << endl;
        } else {
            cout << "Caravana nao encontrada!" << endl;
        }
    } else if (cmd == "saves") {
        cout << "Comando fase 2!: " << cmd << endl;

    } else if (cmd == "loads") {
        cout << "Comando fase 2!: " << cmd << endl;

    } else if (cmd == "lists") {
        cout << "Comando fase 2!: " << cmd << endl;

    } else if (cmd == "dels") {
        cout << "Comando fase 2!: " << cmd << endl;

    } else {
        cout << "\nComando nao reconhecido: " << cmd << endl;
    }
}

void Simulador::executarComandosDeArquivo(const string &nome_fich) {
    ifstream arquivo(nome_fich);
    if (!arquivo.is_open()) {
        cout << "Erro ao abrir o arquivo: " << nome_fich << endl;
        return;
    }

    string linha;
    while (getline(arquivo, linha)) {
        istringstream iss(linha);
        string cmd;
        iss >> cmd;

        processarComando(cmd, iss);
    }

    arquivo.close();
}