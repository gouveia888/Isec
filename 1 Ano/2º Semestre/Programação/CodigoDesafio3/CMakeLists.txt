cmake_minimum_required(VERSION 3.27)
project(CodigoDesafio3 C)

set(CMAKE_C_STANDARD 99)

include_directories(.)

add_executable(CodigoDesafio3
        lista.c
        lista.h
        main.c
)
