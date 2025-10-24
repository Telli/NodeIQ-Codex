cmake_minimum_required(VERSION 3.22.1)
project(nodeiq_llama)

add_library(nodeiq_llama SHARED src/llama_bridge.cpp)

target_include_directories(nodeiq_llama PRIVATE include)

target_compile_definitions(nodeiq_llama PRIVATE "LLAMA_STUB")

find_library(log-lib log)

target_link_libraries(nodeiq_llama PRIVATE \
    ${log-lib})
