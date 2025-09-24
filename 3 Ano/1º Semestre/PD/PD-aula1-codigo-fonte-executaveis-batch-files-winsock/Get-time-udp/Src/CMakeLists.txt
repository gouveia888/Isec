cmake_minimum_required(VERSION 3.27)
project(Src)

set(CMAKE_CXX_STANDARD 14)

add_executable(Src
        UDPClient.cpp
        UDPServer.cpp)
