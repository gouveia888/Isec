//
// Created by Henrique Marques on 07/11/2024.
//

#include "RegistoCivil.h"
#include<fstream>
#include<sstream>

void RegistoCivil::redimensionarPessoas()
{
    shared_ptr<Pessoa>* novoArray = new shared_ptr<Pessoa>[maxPessoas+TAM_INICIAL];
    maxPessoas = maxPessoas + TAM_INICIAL;
    for (int i = 0; i < nPessoas; i++)
    {
        //copiar os ponteiro para pessoas para o novo array
        novoArray[i] = pessoas[i];
    }
    delete[] pessoas; // libertar a memoria do array copiado
    pessoas = novoArray;
}

shared_ptr<Pessoa> RegistoCivil::procuraPessoa(int BI) const
{
    for (int i = 0; i < nPessoas; i++)
    {
        if(pessoas[i]->getNumeroBI() == BI)
            return pessoas[i];
    }
    return nullptr;
}

weak_ptr <const Pessoa> RegistoCivil::adicionaPessoa(string nome, int BI, int NIF)
{
    // Verificar se pessoa com aquele BI ja existe
    // e se existir retornamos um ponteiro para essa pessoa
    shared_ptr<Pessoa> p = procuraPessoa(BI);
    if (p){
        weak_ptr<const Pessoa> wp = p
        return wp;
    }

    if(nPessoas == maxPessoas) //Se o array ja esta cheio vamos redimensionar
        redimensionarPessoas();
    //Adicionar a nova pessoa
    pessoas[nPessoas] = make_shared<Pessoa>(nome, BI, NIF);
    return pessoas[nPessoas++];
}

void RegistoCivil::adicionaPessoasFicheiro(string nomeFicheiro)
{
    ifstream f(nomeFicheiro);
    if (!f.is_open())
        throw runtime_error("Nao foi possivel abrir o ficheiro");

    string nome;
    int BI, NIF;
    while (f >> nome >> BI >> NIF)
        adicionaPessoa(nome, BI, NIF);

    if(!f.eof())
        throw runtime_error("Ficheiro com formato invalido");
    f.close();
}

bool RegistoCivil::removePessoa(int BI)
{
    for (int i = 0; i < nPessoas; i++)
    {
        if(pessoas[i]->getNumeroBI() == BI)
        {
            delete pessoas[i];
            for(int j = i; j < nPessoas-1; j++) // nPessoas -1 para nao acedermos fora do array
                pessoas[j] = pessoas[j+1];
            --nPessoas;
            return true;
        }
    }
    return false;
}

string RegistoCivil::getNomePessoa(int BI) const
{
    shared_ptr<Pessoa> p = procuraPessoa(BI);
    return p ? p->getNome() : ""; // if ? then : else
}

string RegistoCivil::obtemListagem() const
{
    ostringstream oss;
    oss << "Registo Civil de: " << pais << endl;
    for (int i = 0; i < nPessoas; i++)
        oss << pessoas[i]->descricao() << endl;
    return oss.str();
}

bool RegistoCivil::atualizaNome(int BI, string novoNome)
{
    shared_ptr<Pessoa> p = procuraPessoa(BI);
    if (p == nullptr)
        return false;
    p->atualizarNome(novoNome);
    return true;
}
