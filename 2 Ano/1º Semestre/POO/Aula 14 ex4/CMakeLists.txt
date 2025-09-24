cmake_minimum_required(VERSION 3.27.8)
project(aquario_aula)

set(CMAKE_CXX_STANDARD 20)

add_executable(aquario_aula main.cpp
        Aquario.cpp
        Aquario.h
        Peixes/Peixe.cpp
        Peixes/Peixe.h
        Peixes/Carpa/Carpa.cpp
        Peixes/Carpa/Carpa.h
        Peixes/Tubarao/Tubarao.cpp
        Peixes/Tubarao/Tubarao.h
        Aquario.cpp)
