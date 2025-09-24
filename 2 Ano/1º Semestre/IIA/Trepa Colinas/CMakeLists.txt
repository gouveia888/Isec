cmake_minimum_required(VERSION 3.27)
project(Trepa_Colinas C)

set(CMAKE_C_STANDARD 11)

add_executable(Trepa_Colinas main.c
        algoritmo.c
        utils.c
        funcao.c)
