cmake_minimum_required(VERSION 3.27)
project(Aula13 C)

set(CMAKE_C_STANDARD 99)

include_directories(.)

add_executable(Aula13
        banco.c
        banco.h
        ex2.c)
