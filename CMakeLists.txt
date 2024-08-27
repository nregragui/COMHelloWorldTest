cmake_minimum_required(VERSION 3.10)
project(HelloWorldCOM)

add_library(HelloWorldCOM SHARED src/HelloWorld.cpp)

target_link_libraries(HelloWorldCOM comsuppw)
