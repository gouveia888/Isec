cmake_minimum_required(VERSION 3.27)
project(Ficha7_Codigo_Inicio C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(Ficha7_Codigo_Inicio
        algoritmo.c
        algoritmo.h
        funcao.c
        funcao.h
        main.c
        utils.c
        utils.h)
