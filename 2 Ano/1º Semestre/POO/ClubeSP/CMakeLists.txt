cmake_minimum_required(VERSION 3.27.8)
project(ClubeSP)

set(CMAKE_CXX_STANDARD 20)

add_executable(ClubeSP main.cpp
        Pessoa.cpp
        Pessoa.h
        RegistoCivil.cpp
        RegistoCivil.h
        Clube.cpp
        Clube.h)
