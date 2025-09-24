cmake_minimum_required(VERSION 3.27)
project(Aula_11)

set(CMAKE_CXX_STANDARD 99)

add_executable(Aula_11
        Aula11/Aula11_CodigoFicheiros/cp.c
        cmake-build-debug/cp_ex3.dat
        Aula11/Aula11_CodigoFicheiros/valoresEx2.bin
        cp_ex3.dat)
