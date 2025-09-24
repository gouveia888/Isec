#ifndef TRABALHO_DADOS_H
#define TRABALHO_DADOS_H
#include <sstream>
#include <string>
#include <vector>
#include "Caravana/Caravana.h"
#include "Cidade.h"



class Cidade; // Declaração antecipada da classe Cidade
class Dados {
private:
    static int colunas;
    static int linhas;
    static int moedas;
    int instantes_entre_novos_itens;
    static int duracao_item;
    static int max_itens;
    int preco_venda_mercadoria;
    int preco_compra_mercadoria;
    int preco_caravana;
    int instantes_entre_novos_barbaros;
    int duracao_barbaros;
    std::vector<Cidade*> cidades; // Vetor para armazenar as cidades
    static char** mapa;
public:
    Dados(int colunas = 0, int linhas = 0);
    ~Dados();

    static int getColunas();
    static int getLinhas();
    int getduracao_barbaros() const;
    static std::string displaymapa();
    void setColunas(int colunas);
    void setLinhas(int linhas);
    int getdados(char* nome_fich);
    static void setMoedas(int mais_moedas);
    void criarCaravanas(); // novo metodo para criar caravanas
    void criarCidades();
    static int getMoedas() { return moedas; } // metodo para obter moedas
    static int getduracao_tempo() { return duracao_item; }
    static int getmax_itens() {return max_itens; }
    static char** getpmapa() { return mapa; }
    void mostrarPrecos() const;
    int getPrecoCompraMercadoria() const;
    int getPrecoVendaMercadoria() const;
    void cidadeAleatoria();
};


#endif //TRABALHO_DADOS_H
