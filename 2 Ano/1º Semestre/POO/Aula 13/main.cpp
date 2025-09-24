#include <iostream>
#include "ListaCompras.h"
#include "ListaComprasVetor.h"
#include "ListaComprasSet.h"

int main() {
/*
    ListaCompras lca;

    lca.adiciona("Arroz", 2);
    lca.adiciona("Arroz", 2);
    lca.adiciona("massa", 3);
    lca.adiciona("carne", 4);
    cout << lca.obtemLista() << endl;
    lca.elimina("massa");
    lca.removeQty("carne", 1);
    cout << lca.obtemLista() << endl;
*/
/*
ListaComprasVetor lca;
    lca.adiciona("Arroz", 2);
    lca.adiciona("Arroz", 2);
    lca.adiciona("massa", 3);
    lca.adiciona("carne", 4);
    cout << lca.obtemLista() << endl;
    lca.elimina("massa");
    lca.removeQty("carne", 1);
    cout << lca.obtemLista() << endl;
    lca.adiciona("peixe",3);
    lca.eliminaTodosCom(3);
    cout << lca.obtemLista() << endl;
*/
    ListaComprasSet lca;
    lca.adiciona("Arroz", 2);
    lca.adiciona("Arroz", 2);
    lca.adiciona("massa", 3);
    lca.adiciona("carne", 4);
    cout << lca.obtemLista() << endl;
    lca.elimina("massa");
    lca.removeQty("carne", 1);
    cout << lca.obtemLista() << endl;
    lca.adiciona("peixe",3);
    lca.eliminaTodosCom(3);
    cout << lca.obtemLista() << endl;

    return 0;
}
