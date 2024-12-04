cmake_minimum_required(VERSION 3.10.2)

# Укажите имя проекта
project("ndroidApp")

# Настройте NDK
set(CMAKE_CXX_STANDARD 17)

# Укажите исходники, которые будут скомпилированы
add_library(native-lib
        SHARED
        src/main/cpp/native-lib.cpp)

# Подключите стандартные библиотеки Android
find_library(log-lib log)

# Привяжите библиотеки
target_link_libraries(native-lib
        ${log-lib})
