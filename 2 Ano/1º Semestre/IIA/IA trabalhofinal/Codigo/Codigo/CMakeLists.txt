cmake_minimum_required(VERSION 3.27.8)
project(Codigo C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(Codigo
        trepa-colinas.c
        trepa-colinas.h
    funcao.c
    funcao.h
        main.c
    utils.c
    utils.h
        evolutivo.c)
