#include "Dados.h"
#include <sstream>
#include <fstream>
#include <iostream>
#include <cctype> // Para isdigit
#include "Caravana/Comercio.h"
#include "Caravana/Militar.h"
#include "Caravana/Secreta.h"
#include "Caravana/Caravana.h"


using namespace std;

int Dados::colunas = 0;
int Dados::linhas = 0;
int Dados::duracao_item = 0;
int Dados::max_itens = 0;
char** Dados::mapa = nullptr;
int Dados::moedas = 0;

Dados::Dados(int colunas, int linhas) : instantes_entre_novos_itens(0), preco_venda_mercadoria(0), preco_compra_mercadoria(0),
                                        preco_caravana(0), instantes_entre_novos_barbaros(0), duracao_barbaros(0) {
    Dados::colunas = colunas;
    Dados::linhas = linhas;

    mapa = new char*[linhas];
    for (int i = 0; i < linhas; i++) {
        mapa[i] = new char[colunas];
    }
}

Dados::~Dados() {
    for (int i = 0; i < linhas; i++) {
        delete[] mapa[i];
    }
    delete[] mapa;
}

int Dados::getColunas() {
    return colunas;
}

int Dados::getLinhas() {
    return linhas;
}

int Dados::getduracao_barbaros() const {
    return this->duracao_barbaros;
}

string Dados::displaymapa() {
    ostringstream oss;

    for (int l = 0; l < linhas; l++) {
        for (int c = 0; c < colunas; c++) {
            if (c == 0)
                oss << endl;
            oss << mapa[l][c];
        }
    }
    return oss.str();
}

void Dados::setColunas(int colunas) {
    this->colunas = colunas;
}

void Dados::setLinhas(int linhas) {
    this->linhas = linhas;
}

int Dados::getdados(char* nome_fich) {
    ifstream f(nome_fich);
    string parametro;
    int linhas, colunas;

    if (!f.is_open()) {
        cout << "Erro ao abrir o ficheiro: " << nome_fich << endl;
        return -1;
    }

    if (!(f >> parametro && f >> linhas)) {
        cout << "Erro ao ler linhas do ficheiro." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> colunas)) {
        cout << "Erro ao ler colunas do ficheiro." << endl;
        return -1;
    }



    // Libera a memória existente, se houver
    if (this->mapa) {
        for (int i = 0; i < this->linhas; i++) {
            delete[] this->mapa[i];
        }
        delete[] this->mapa;
        this->mapa = nullptr; // Previne acesso a memória liberada
    }

    // Atualiza dimensões
    setLinhas(linhas);
    setColunas(colunas);

    // Realoca o buffer
    mapa = new char*[linhas];
    if (!mapa) {
        cout << "Erro ao alocar memória para mapa." << endl;
        return -1;
    }

    for (int i = 0; i < linhas; i++) {
        mapa[i] = new char[colunas];
        if (!mapa[i]) {
            cout << "Erro ao alocar memória para linha " << i << " do mapa." << endl;
            // Libera a memória já alocada
            for (int j = 0; j < i; j++) {
                delete[] mapa[j];
            }
            delete[] mapa;
            mapa = nullptr; // Previne acesso a memória liberada
            return -1; // Retorna erro
        }
    }

    // Lê o mapa
    for (int l = 0; l < getLinhas(); l++) {
        for (int c = 0; c < getColunas(); c++) {
            if (!(f >> mapa[l][c])) {
                cout << "Erro ao ler o mapa na posição (" << l << ", " << c << ")." << endl;
                // Libera a memória já alocada
                for (int i = 0; i <= l; i++) {
                    delete[] mapa[i];
                }
                delete[] mapa; // Libera a memória do mapa
                mapa = nullptr; // Previne acesso a memória liberada
                return -1; // Retorna erro
            }
        }
    }

    // Lê os dados restantes
    if (!(f >> parametro && f >> moedas)) {
        cout << "Erro ao ler moedas." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> instantes_entre_novos_itens)) {
        cout << "Erro ao ler instantes entre novos itens." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> duracao_item)) {
        cout << "Erro ao ler duração do item." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> max_itens)) {
        cout << "Erro ao ler max itens." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> preco_venda_mercadoria)) {
        cout << "Erro ao ler preço de venda da mercadoria." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> preco_compra_mercadoria)) {
        cout << "Erro ao ler preço de compra da mercadoria." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> preco_caravana)) {
        cout << "Erro ao ler preço da caravana." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> instantes_entre_novos_barbaros)) {
        cout << "Erro ao ler instantes entre novos bárbaros." << endl;
        return -1;
    }
    if (!(f >> parametro && f >> duracao_barbaros)) {
        cout << "Erro ao ler duração dos bárbaros." << endl;
        return -1;
    }

    f.close();

    criarCaravanas(); // Chama o metodo para criar as caravanas a partir do mapa

    return 0; // Retorna 0 em caso de sucesso
}

void Dados::setMoedas(int mais_moedas) {
    moedas += mais_moedas;
}

void Dados::criarCaravanas() {
    for (int l = 0; l < linhas; l++) {
        for (int c = 0; c < colunas; c++) {
            char cell = mapa[l][c];
            if (isdigit(cell)) { // Verifica se a celula contém um digito
                int id = cell - '0'; // Converte o caractere para um inteiro
                Caravana* novaCaravana = nullptr;

                // Cria a caravana com base no dígito
                switch (id) {
                    case 1:
                       novaCaravana = new Militar(); // Cria uma caravana militar
                        break;
                    case 2:
                       novaCaravana = new Comercio(); // Cria uma caravana comercial
                        break;
                    case 3:
                      novaCaravana = new Secreta(); // Cria uma caravana secreta
                        break;

                    default:
                       novaCaravana = new Comercio(); // Cria uma caravana comercial
                }

                // Define a posição da caravana
                if (novaCaravana) {
                    novaCaravana->setPosicao(c, l); // Define a posição da caravana
                    // A caravana já é adicionada ao vetor estático no construtor
                }
            }
        }
    }
}


// Implementação do metodo criarCidades
void Dados::criarCidades() {
    for (int l = 0; l < linhas; l++) {
        for (int c = 0; c < colunas; c++) {
            char cell = mapa[l][c];
            if (islower(cell)) { // Verifica se a célula contém uma letra minúscula
                std::string nome(1, cell); // Cria uma string com o nome da cidade
                Cidade* novaCidade = new Cidade(nome, l, c); // Cria a nova cidade
                cidades.push_back(novaCidade); // Adiciona a cidade ao vetor
            }
        }
    }
}

void Dados::mostrarPrecos() const {
    std::ostringstream oss; // Cria um objeto ostringstream

    oss << "Precos das mercadorias:" << std::endl;
    oss << "Preco de compra: " << preco_compra_mercadoria << " moedas por tonelada" << std::endl;
    oss << "Preco de venda: " << preco_venda_mercadoria << " moedas por tonelada" << std::endl;
    oss << "Preco da caravana: " << preco_caravana << " moedas" << std::endl;

    // Agora imprime o conteúdo do ostringstream
    std::cout << oss.str(); // Converte o conteúdo para string e imprime
}

int Dados::getPrecoCompraMercadoria() const {
    return preco_compra_mercadoria;
}

int Dados::getPrecoVendaMercadoria() const {
    return preco_venda_mercadoria;
}

