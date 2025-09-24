#include "Itens.h"
#include <iostream>
using namespace std;

void Itens::geraitem() {
    char** mapa = Dados::getpmapa();
    int c,l;

    if(itens_atuais < max_itens ){
        do {
            //coordenadas aleatorias
            c = rand() % Dados::getColunas();
            l = rand() % Dados::getLinhas();
        } while (mapa[l][c] != deserto);

        mapa[l][c] = '*';
        //cout << "Posicao " << l << "," << c << "Conteudo " << mapa[l] [c] << endl;

        TipoItem tipo = static_cast<TipoItem>(rand() % 5);

        itens_atuais++;
    }

}

void Itens::aplicarEfeito(Caravana& caravana) {
    switch (tipo) {
        case ArcaTesouro:

            cout << "Caravana encontrou uma Arca do Tesouro!" << endl;
            break;
        case CaixaPandora:
            // Lógica para efeito da Caixa Pandora
            cout << "Caravana encontrou uma Caixa Pandora!" << endl;
            break;
        case Jaula:
            // Lógica para efeito da Jaula
            std::cout << "Caravana encontrou uma Jaula!" << endl;
            break;
        case Mina:
            // Lógica para efeito da Mina
            cout << "Caravana encontrou uma Mina!" << endl;
            break;
        case Surpresa:
            // Lógica para efeito da Surpresa
            cout << "Caravana encontrou uma Surpresa!" << endl;
            break;
        default:
            break;
    }
}