cmake_minimum_required(VERSION 3.27)
project(Aula_13)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(Aula_13
        Compra.cpp
        Compra.h
        ListaCompras.cpp
        ListaCompras.h
        ListaComprasSet.cpp
        ListaComprasSet.h
        ListaComprasVetor.cpp
        ListaComprasVetor.h
        main.cpp)
