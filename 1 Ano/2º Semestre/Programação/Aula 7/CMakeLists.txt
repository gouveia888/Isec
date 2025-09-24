cmake_minimum_required(VERSION 3.27)
project(Aula_7 C)

set(CMAKE_C_STANDARD 99)

include_directories(.)

add_executable(Aula_7
        agenda.c
        agenda.h
        main.c)
