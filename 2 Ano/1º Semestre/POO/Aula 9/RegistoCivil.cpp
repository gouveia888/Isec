//
// Created by Gouveia on 07/11/2024.
//
//agregaçao a class sabe que existem pessoas compossiçao pessoas pertecem a class

#include "RegistoCivil.h"
#include <fstream>
#include <sstream>

void RegistoCivil::redimensionarPessoas() {

    Pessoa* novoarray = new Pessoa[maxPessoas+TAM_INICIAL];

    maxPessoas+=TAM_INICIAL;

    for (int i = 0; i < nPessoas; i++){
        novoarray[i]=pessoas[i];
    }
    delete [] pessoas;
    pessoas = novoarray;
}

Pessoa* RegistoCivil::procuraPessoa(int BI) const{
    for( int i=0; i<nPessoas ; i++){
        if(pessoas[i].getNumeroBI() == BI)
            return pessoas+i;
    }
    return nullptr;
}

Pessoa* RegistoCivil::adicionaPessooa(string nome, int BI, int NIF){

    // verificar se a pessoa ja existe e se existir retorna ponteiro para essa pessoa
    Pessoa *p = procuraPessoa(BI);
    if(nPessoas == maxPessoas){
        redimensionarPessoas();
        pessoas[nPessoas] = Pessoa(nome, BI, NIF);
    }
    return pessoas+nPessoas++; //&pessoas[nPessoas++] incrementa o nPessoaas depois do return
};

void RegistoCivil::adicionaPessooaFicheiro(string nomeFicheiro) {
    ifstream f(nomeFicheiro);

    if(!f.is_open())  //ou !f operador bool definido na class ifstream
        throw runtime_error("Nao foi possivel abrir o ficheiro");

    string nome;
    int BI, NIF;

    while(f >> nome >> BI >> NIF){
        adicionaPessooa(nome, BI, NIF);
    }

    if(!f.eof())
        throw runtime_error("Ficheiro com formato errado");

    f.close();
}

bool RegistoCivil::removePessoa(int BI){
    for(int i = 0; i < nPessoas; i++){
        if(pessoas[i].getNumeroBI()==BI){
            for(int j = i; j < nPessoas-1; j++) //nPessoas-1 para nao acederemos fora do array
                pessoas[j]=pessoas[j+1];
            --nPessoas; //a nivel de eficiencia melhor que nPessoas--
            return true;
        }
    }
    return false;
}

string RegistoCivil::getNomePessoa(int BI) const {
    Pessoa *p = procuraPessoa(BI);
    return p != nullptr ? p->getNome() : "";
}

string RegistoCivil::obtemListagem() const {
    ostringstream oss;
    oss << "Registo Civil do" << this->pais << endl;
    for(int i = 0; i < nPessoas; i++){
        oss << this->pessoas[i].descricao() << endl;
    }
    return oss.str();
}

bool RegistoCivil::atualizaNome(int BI, string novoNome){
    Pessoa *p = procuraPessoa(BI);
    if(p == nullptr)
        return false;
    p->atualizarNome(novoNome);
    return true;
}

