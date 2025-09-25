using System.Formats.Asn1;

namespace ex3
{

    interface IFiguraPlana
    {
        double Area();
        double Perimetro();
        string ToString();
    }

    abstract class FiguraPlana : IFiguraPlana
    {
        public abstract double Area();
        public abstract double Perimetro();
        public override string ToString()
        {
            return String.Format("Area: {0:F2} Perimetro: {1:F2}\n", Area(), Perimetro());
        }

    }

    class Retangulo : FiguraPlana { 
        private double Comprimento { get; set; }
        private double Largura { get; set; }

        public Retangulo(double comprimento, double largura) { 
            this.Comprimento = comprimento;
            this.Largura = largura;
        }

        public override double Area() { 
            return Comprimento * Largura;
        }

        public override double Perimetro()
        {
            return (Comprimento + Largura) * 2;
        }

        public override string ToString() { 
            return String.Format("Retangulo: Comprimento = {0:F2} Largura = {1:F2}\n", Comprimento, Largura) + base.ToString();
        }


    }

    class Tringulo : FiguraPlana
    {
        private double LadoA { get; set; }
        private double LadoB { get; set; }
        private double LadoC { get; set; }

        public Tringulo(double ladoA, double ladoB, double ladoC)
        {
            this.LadoA = ladoA;
            this.LadoB = ladoB;
            this.LadoC = ladoC;
        }

        public override double Area()
        {
            double s = (LadoA + LadoB + LadoC) / 2;
            return Math.Sqrt(s * (s - LadoA) * (s - LadoB) * (s - LadoC));
        }
        public override double Perimetro()
        {
            return LadoA + LadoB + LadoC;
        }

        public override string ToString()
        {
            return String.Format("Triangulo: LadoA = {0:F2} LadoB = {1:F2} LadoC = {2:F2}\n", LadoA, LadoB, LadoC) + base.ToString();
        }
    }

    class Circulo : FiguraPlana
    {
        private double Raio { get; set; }

        public Circulo(double raio)
        {
            this.Raio = raio;
        }

        public override double Area()
        {
            return Math.PI * Math.Pow(Raio, 2);
        }
        public override double Perimetro()
        {
            return 2 * Math.PI * Raio;
        }
        public override string ToString()
        {
            return String.Format("Circulo: Raio = {0:F2}\n", Raio) + base.ToString();
        }
    }

    public class Program
    {
        public static void Main(string[] args)
        {
            Retangulo r = new Retangulo(5, 6);
            Tringulo t = new Tringulo(3, 4, 5);
            Circulo c = new Circulo(7);
            Console.WriteLine(r.ToString());
            Console.WriteLine(t.ToString());
            Console.WriteLine(c.ToString());
        }
    }
}