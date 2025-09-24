cmake_minimum_required(VERSION 3.27.8)
project(ficha9aula)

set(CMAKE_CXX_STANDARD 20)

add_executable(ficha9aula main.cpp
        Imovel/Imovel.cpp
        Imovel/Imovel.h
        Imovel/Apartamento/Apartamento.cpp
        Imovel/Apartamento/Apartamento.h
        Imovel/Loja/Loja.cpp
        Imovel/Loja/Loja.h
        Imobiliaria.cpp
        Imobiliaria.h)
