#include <iostream>
#include <sstream>
#include <exception>
#include <cmath>
#include <initializer_list>

#define TAM 20

using namespace std;

class vetor{

    double x, y;
    int matriz[20];

public:
    vetor(double x, double y): x(x), y(y){}
    //explicit impede que um double seja convertido para vetor
    /*explicit*/vetor(double n): x(n), y(n){}
    vetor():x(0), y(0){};

    vetor(initializer_list<int> lista){

        int i = 0;
        for (int valor : lista)
            matriz[i++] = valor;

    }

    double getX() const{
        return x;
    }

    double getY() const{
        return y;
    }

    void setX( double x){
        this->x=x;
    }

    void setY( double y){
        this->y=y;
    }

    string toString() const{
        ostringstream oss;
        oss << "(" << this->x << "," << this->y << ")";
        return oss.str();
    }

    vetor operator+(const vetor &v) const{
        // z = v1 + v2 --> v1 vai chamar a funçao e recebe com argumento o v2 devolvendo o z
        vetor r;
        //r.x = getX(this->x) + v.x;
        r.setX(this->x + v.x);
        r.setY(this->getY() + v.getY());

        return r;
    }

    vetor& operator=(const vetor &v){
        //z invoca a funçao sendo o this e os argumentos sao os valores depois do igual
        if(this != &v){
            this->x = v.getX();
            this->y = v.getY();
        }
        return *this;
    }

    vetor& operator=(const double n){

            this->x = n;
            this->y = n;

        return *this;
    }

    vetor operator-(const vetor &v) const{
        // z = v1 + v2 --> v1 vai chamar a funçao e recebe com argumento o v2 devolvendo o z
        vetor r;
        //r.x = getX(this->x) + v.x;
        r.setX(this->x - v.x);
        r.setY(this->getY() - v.getY());

        return r;
    }

   vetor& operator+=(const vetor &v){
        this->x= this->x + v.getX();
        this->y += this->y + v.getY();
        return *this;
   }

   bool operator==(const vetor &v){

        return this->x == v.getX() && this->y == v.getY();
   }

   bool operator!=(const vetor &v){

        return this->x != v.getX() || this->y != v.getY();
   }

   operator double() const{

        return sqrt(pow(this->x,2) + pow(this->y,2));
   }

   vetor& operator++(){ //++v
        this->x++;
        this->y++;
        return *this;
    }

   vetor operator++(int){ //v++
        vetor v = *this;
        this->x++;
        this->y++;
        return v;
    }

   int& operator[](int i){

       int tmp;
       if( i < 0 || i >= TAM)
           return tmp;

        return this->matriz[i];
    }

   int operator()(int num){

       int cont = 0;
       for (int v : this->matriz)
          if(v == num)
              cont++;
       return cont;
   }
};

ostream& operator<< (ostream &os, const vetor &v){

    os << v.toString();
    return os;
}


vetor operator+(const double d,const vetor &v){
    return vetor(v.getX()+d, v.getY()+d);
}

vetor operator+(const vetor &v, const double d){
    return vetor(v.getX()+d, v.getY()+d);
}

istream& operator>>(istream& iss ,vetor &v){

    double x, y;
    iss >> x >> y;
    v.setX(x);
    v.setY(y);
    return iss;
}

int main(){

    //ex4
    vetor a{10,15,20,25,30,25};
    cout << "elemento nas posições 2 e 3: " << a[2] << " " << a[3];
    // aparece os valores 20 e 25
    a[2] = 5;
    cout << "\na posição 2 tem agora: " << a[2]; // aparece 5
    cout << "\nNum. De elementos com valor 25 = " << a(25);
    // aparece 2
    a[500] = 33;
    cout << "esta mensagem não aparece";
    return 0;
}