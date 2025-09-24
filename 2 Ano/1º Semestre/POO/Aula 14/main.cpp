#include <iostream>

#include "Imobiliaria.h"
#include "Imovel/Imovel.h"
#include "Imovel/Apartamento/Apartamento.h"
#include "Imovel/Loja/Loja.h"

int main()
{
    // Class imovel e abstracta nao podemos criar um objeto desse tipo
    // Imovel* i = new Imovel(1000, 15, 2, "id_imovel");
    Imovel* apartamento1 = new Apartamento(120, 2, 2);
    Imovel* apartamento2 = new Apartamento(100, 2, 1);
    Imovel* loja = new Loja(300);

    Imobiliaria imobiliaria;
    imobiliaria.adicionarImovel(apartamento1);
    imobiliaria.adicionarImovel(apartamento2);
    imobiliaria.adicionarImovel(loja);

    cout << imobiliaria.listaImoveis(2);
    return 0;
}
