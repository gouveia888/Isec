#ifndef TRABALHO_BUFFER_H
#define TRABALHO_BUFFER_H
#include <sstream>
#include <string>


class Buffer {
    int colunas, linhas;
    char **mapa = nullptr;

public:
    Buffer(int colunas=0, int linhas=0);
    ~Buffer();
    int getColunas() const;
    int getLinhas() const;
    void setColunas(int colunas);
    void setLinhas(int linhas);
    int getmapa(char *nome_fich);
    void limpamapa();
    std::string displaymapa();
    bool posicaoValida(int x, int y) const;
    void atualizarPosicaoCaravana(int id, int x, int y, bool isBarbara);
};


#endif //TRABALHO_BUFFER_H
