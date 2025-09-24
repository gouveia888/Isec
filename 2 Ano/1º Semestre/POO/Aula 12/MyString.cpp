//
// Created by Gouveia on 21/11/2024.
//

#include "MyString.h"
#include <string.h>
MyString::MyString(const char *s) {

    int len = strlen(s);
    my_str = make_unique<char[]>(len+1); //+1 para o '\0'
    strcpy(my_str.get(),s); //get danos um ponteiro para os dados / strcpy copia o '\0'
    my_str_lenght= len;
}

MyString

char &MyString::at(int index) {
    char s[this->my_str_lenght];
    if(index > 0 || index < this->my_str_lenght){
        return this->my_str[index];
    }
}

void MyString::clear() {
    my_str.reset(new char[0]); //delete do mystring e vai ficar a apontar para um char[0] para evitar erros
}

MyString &MyString::operator=(const MyString &other) {

    if(other == this)
        return *this;

    my_str = make_unique<char[]>( other.my_str_lenght + 1);
    strcpy(my_str.get(), other.my_str.get());
    my_str_lenght = other.my_str_lenght;
}