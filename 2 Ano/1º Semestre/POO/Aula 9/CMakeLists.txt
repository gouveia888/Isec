cmake_minimum_required(VERSION 3.27)
project(Aula9)

set(CMAKE_CXX_STANDARD 23)

add_executable(Aula9 main.cpp
        Pessoa.cpp
        Pessoa.h
        RegistoCivil.cpp
        RegistoCivil.h
        Clube.cpp
        Clube.h)
