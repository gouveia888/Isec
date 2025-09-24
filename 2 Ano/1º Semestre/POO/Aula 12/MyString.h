//
// Created by Gouveia on 21/11/2024.
//

#ifndef AULA_12_MYSTRING_H
#define AULA_12_MYSTRING_H
#include <memory>

using namespace std;

class MyString {

public:
    MyString(): my_str(make_unique<char[]>(0)), my_str_lenght(0){}
    MyString(const char* s);
    MyString(const MyString &s);

    int lenght() const{return my_str_lenght;}
    const char* str() const{ return my_str.get();}
    char& at(int index);  // Pode ser utilizada desta forma mystr.at(0) = 'a'
    const char* concat(const MyString &other);
    void clear();
    MyString &operator=(const MyString &other);

private:
    unique_ptr<char[]> my_str;
    int my_str_lenght;
};


#endif //AULA_12_MYSTRING_H
