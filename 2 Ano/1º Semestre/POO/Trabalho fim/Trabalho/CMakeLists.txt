cmake_minimum_required(VERSION 3.27)
project(Trabalho)

set(CMAKE_CXX_STANDARD 20)

add_executable(Trabalho main.cpp
        Buffer.cpp
        Buffer.h
        Caravana/Caravana.cpp
        Caravana/Caravana.h
        Cidade.h
        Deserto.h
        Dados.cpp
        Cidade.cpp
        Simulador.cpp
        Simulador.h
        Posição.cpp
        Caravana/Comercio.cpp
        Caravana/Comercio.h
        Caravana/Militar.cpp
        Caravana/Militar.h
        Caravana/Secreta.cpp
        Caravana/Secreta.h
        Caravana/Barbara.h
        Caravana/Barbara.cpp
        Itens/ArcaTesouro.cpp
        Itens/CaixaPandora.cpp
        Itens/Itens.cpp
        Itens/Jaula.cpp
        Itens/Mina.cpp
        Itens/Surpresa.cpp)
