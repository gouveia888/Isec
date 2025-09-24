#ifndef TRABALHO_POSICAO_H
#define TRABALHO_POSICAO_H

class Dados; // Forward declaration

class Posição {
    Dados* dados;
protected:
    int x, y; // Coordenadas
public:
    Posição(int x = 0, int y = 0, Dados* dados = nullptr);
    virtual ~Posição() = default;

    int getX() const;
    int getY() const;

    virtual void setPosicao(int newX, int newY);
};

#endif // TRABALHO_POSICAO_H