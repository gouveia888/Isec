#include <memory>
#include <iostream>
#include "Ponto.h"
using namespace std;

int main() {
    // zona inicial
    weak_ptr<Ponto> spw;
    {
        unique_ptr<Ponto> spu = make_unique<Ponto>(2,2);
        //unique_ptr<Ponto> spu2 = spu;  Isto nao e possivel
        unique_ptr<Ponto> novospu = move(spu); //novospu passa a apontar para spu e psu é libertado
        novospu->mostra();
        novospu.release();
        novospu.reset(new Ponto(20,20)); //libertar o anterior e mostrar o novo
        novospu->mostra();
    }
    { // bloco B2
        shared_ptr<Ponto> sps1 = make_shared<Ponto>(3,3);
        shared_ptr<Ponto> sps2 = sps1;
        spw = sps1; //usar spw verificar se sps1 ainda existe
        sps2->mostra();
        sps1->mostra();
        cout << "Numero ponteiros " << sps1.use_count() <<endl;
        shared_ptr<Ponto> sp3 = sps2;
        cout << "Numero ponteiros " << sps1.use_count() <<endl;

        cout << "spw.expired: " << spw.expired() <<endl;

    }
    { // bloco B3
        cout << "spw.expired: " << spw.expired() <<endl;

        shared_ptr<Ponto> sp = spw.lock(); //spw.lock devolve um shared_ptr
        if(sp){
            sp->mostra();
        }else
            cout << "shared pointer ja foi libertado " << endl;
    }

    unique_ptr<Ponto> *uniques = new unique_ptr<Ponto>[10];
    shared_ptr<Ponto> shared[10];
    weak_ptr<Ponto> weaks[10];

    for(int i = 0; i<10; i++){
        uniques[i] = make_unique<Ponto>(i,i);
        shared[i] = make_shared<Ponto>(i,i);
        weaks[i] = shared[i];
    }

    delete[] uniques;
    return 0;
}
