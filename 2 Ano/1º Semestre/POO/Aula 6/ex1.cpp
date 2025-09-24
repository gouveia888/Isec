#include <iostream>
#include <sstream>
#include <exception>
#include <cmath>

using namespace std;

class vetor{

    double x, y;

public:
    vetor(double x, double y): x(x), y(y){}
    //explicit impede que um double seja convertido para vetor
    /*explicit*/vetor(double n): x(n), y(n){}
    vetor():x(0), y(0){};

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
    cout << "Valor de x" << endl;
    iss >> x ;
    cout << "Valor de y" << endl;
    iss >> y ;
    v.setX(x);
    v.setY(y);
    return iss;
}

int main(){

        vetor a(1,1);
        double modulo = double(a);  // significado = modulo do vetor
        double k = a;
        vetor b = 2.5; // verifique primeiro se já está a ser possível isto
        b = a + 4.0;   // verifique se fizer um operador para este caso dará erro
        vetor c(1.0, 1.0);
        cout << "\n Operadores unários \n";
        cout <<"\nc=" << c;
        cout << "\n++   -> c=" << ++c;
        cout <<"\nc="<< c;
        vetor d(1.0, 1.0);
        cout <<"\nd="<< d ;
        cout << "\nd++:" << d++;
        cout << "\nd=" << d << endl;
        cin >> a >> b;
        cout << a << "\n" << b;
        if ((bool) a)
            cout << "\no vetor a tem as coordenadas 0,0";


    /* Alinea a)

vetor v1(2.0, 1.0), v2(1.0, 3.0), v3(2.2), z;
z = v1 + v2 + v3;
cout << v1 << "+" << v2 << "+" << v3 << "=" << z << endl; // obs: "(x,y)"
z = v1 + 10.0;
cout << v1 << " + " << " 10 = " << z << endl;
z = 20.0 + v1; //a funcçao e global porque quem invoca a funcao e  double
cout << "20 + " << v1 << " = " << z << endl;
z = v1 - v2;
cout << v1 << " - " << v2 << " = " << z << endl;
vetor a(1.0, 1.0), b(2.0, 4.0);
cout << " a= " << a << " b= " << b << endl;
a += b += v1;
a += b;
a += 10.0;
cout << " a= " << a << endl;
cout << "(a == b)? " << (a == b) << endl;
cout << "(a != b)? " << (a != b) << endl;
*/

    return 0;
}