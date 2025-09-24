cmake_minimum_required(VERSION 3.27)
project(CodigoDesafio2 C)

set(CMAKE_C_STANDARD 99)

include_directories(.)

add_executable(CodigoDesafio2
        funcao.c
        funcao.h
        main.c)
