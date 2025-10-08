package Ex5;

class Ponto <T extends Number, S extends Number>{ //T e S só podem ser do tipo Number ou derivados
    private T x;
    private S y;

    public Ponto(T x, S y) {
        this.x = x;
        this.y = y;
    }

    public void copy(Ponto <? extends T, ? extends S> p){ //qualquer ponto que seja derivado de T e S
        this.x = p.x;
        this.y= p.y;
    }

    public void mostra(){
        System.out.println("("+x+","+y+")");
    }
}

public class Ex5 {

    public static void main(String[] args) {
        Ponto<Integer, Integer> p=new Ponto<Integer,Integer>(3,4);
        Ponto<Number, Number> x=new Ponto<Number,Number>(0,0);
        p.mostra();  // imprime (3,4)
        x.mostra(); // imprime (0,0)
        x.copy(p);
        x.mostra(); // imprime (3,4)
        //Ponto<String, Integer> erro= new Ponto<String,Integer>(“olá”,3); //erro de compilação

    }

}