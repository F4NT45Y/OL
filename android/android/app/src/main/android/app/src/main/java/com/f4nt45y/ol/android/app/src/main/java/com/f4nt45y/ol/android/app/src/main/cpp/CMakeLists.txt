cmake_minimum_required(VERSION 3.22)
project(ol_engine LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_library(ol_engine SHARED native-lib.cpp)

find_library(log-lib log)
target_link_libraries(ol_engine GLESv2 EGL ${log-lib})
