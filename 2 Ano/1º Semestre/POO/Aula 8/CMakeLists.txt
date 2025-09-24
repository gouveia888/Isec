cmake_minimum_required(VERSION 3.27)
project(Aula8)

set(CMAKE_CXX_STANDARD 20)

add_executable(Aula8 main.cpp
        Automovel.cpp
        Automovel.h
        Roda.cpp
        Roda.h
        Camiao.cpp
        Camiao.h)
