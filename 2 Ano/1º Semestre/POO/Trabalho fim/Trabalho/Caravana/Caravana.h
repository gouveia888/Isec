#ifndef TRABALHO_CARAVANA_H
#define TRABALHO_CARAVANA_H

#include "../Posição.h" // Inclua o cabeçalho da classe Posição
#include <vector>
#include <algorithm> // Para std::remove
#include "../Buffer.h"
#include "../Dados.h"

class Caravana {
private:
    int id;
    int x; // Posição X
    int y; // Posição Y
    int mercadoria;
    int agua;
    int tripulacao;
    bool autoGestao; // Novo atributo para controlar o modo automático

public:
    static int proximoId; // ID da próxima caravana
    static std::vector<Caravana*> todasAsCaravanas; // Vetor estático para armazenar todas as caravanas

    Caravana(); // Construtor padrão
    Caravana(int mercadoria, int agua, int tripulacao, int x, int y); // Novo construtor
    virtual ~Caravana() = default; // Destrutor virtual


    int getMercadoria() const;
    void setMercadoria(int mercadoria);
    int getId() const;
    void setId(int id);
    int getAgua() const; // Declaração do metodo
    void setAgua(int agua);
    int getTripulacao() const;
    void setTripulacao(int tripulacao);
    void setPosicao(int newX, int newY); // metodo para definir a posição
    int getX() const; // Metodo para obter a posição X
    int getY() const; // Metodo para obter a posição Y

    void ativarAutoGestao(); // Ativa o modo automático
    void desativarAutoGestao(); // Desativa o modo automático
    bool isAutoGestao() const; // Verifica se está em modo automático

    void mover(const std::string& direcao, Buffer& buffer, const Dados& dados);
    static Caravana* encontrarCaravanaPorId(int id); // metodo para encontrar caravana por ID
    void exibirDetalhes() const;
    virtual void verificarConsumoAgua() = 0; // metodo virtual

    void cidadeAleatoria();

    void mina();

};


#endif // TRABALHO_CARAVANA_H