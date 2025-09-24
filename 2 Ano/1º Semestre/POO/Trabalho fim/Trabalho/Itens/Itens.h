#ifndef TRABALHO_ITENS_H
#define TRABALHO_ITENS_H
#include "../Dados.h"
#include <vector>

using namespace std;

enum TipoItem {
    ArcaTesouro,
    CaixaPandora,
    Jaula,
    Mina,
    Surpresa
};

class Itens {
private:
    char deserto = '.';
    int duracao_item, max_itens, itens_atuais=0, x, y;
    TipoItem tipo;
public:
    Itens(TipoItem tipo, int x, int y) : tipo(tipo), x(x), y(y) {
        max_itens = Dados::getmax_itens();
        duracao_item = Dados::getduracao_tempo();
    }
    virtual ~Itens() {}
    void geraitem();
    void atualizarTempo() { duracao_item--; }
    virtual void aplicarEfeito(Caravana& caravana);
};


#endif //TRABALHO_ITENS_H
