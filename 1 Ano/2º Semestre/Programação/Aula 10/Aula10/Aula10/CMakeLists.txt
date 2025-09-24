cmake_minimum_required(VERSION 3.27)
project(Aula10 C)

set(CMAKE_C_STANDARD 99)

include_directories(.)

add_executable(Aula10
        funcoes.c
        funcoes.h
        main.c)
