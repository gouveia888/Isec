public class Main {
	private static long stopTime;
	private static long startTime;

	private static void showTime() {
		long interval=stopTime-startTime;
		long secs=interval/1000000000L;
		//long decs=interval-secs*1000000000L;
		//decs/=100000000L;
		long millis = (interval % 1000000000L) / 1000000L;
		System.out.println("Tempo decorrido = " + secs + "." + millis + "s");
	}
	private static void stopTimer() {
		stopTime=System.nanoTime();
	}
	private static void startTimer() {
		startTime=System.nanoTime();
	}

	static void ex1b(long n){
		long soma=0;
		startTimer();
		for(long i=0;i<n;i++)
					soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}
	static void ex1a(long n){
		long soma=0;
		startTimer();
		for(long i=0;i<n;i++)
			for(long j=0;j<n;j++)
					soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}

	static void ex1c(long n){
		long soma=0;
		startTimer();
		for (long i=0;i<n;i+=2)
			soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}
	static void ex1d(long n){
		long soma=0;
		startTimer();
		for(long i=0;i<1000;i++)
			for(long j=0;j<n;j++)
				soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}
	static void ex1e(long n){
		long soma=0;
		startTimer();
		for(long i=0;i<n;i++)
			soma++;
		for(long j=0;j<n;j++)
			soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}
	static void ex1f(long n){
		long soma=0;
		startTimer();
		if(n>20000) n=20000;
		for(long i=0;i<n;i++)
			for(long j=0;j<n;j++)
				soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}
	static void ex1g(long n){
		long soma=0;
		startTimer();
		for(long i=0;i<n;i++)
			for(long j=0;j<n*n;j++)
				soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}
	static void ex1h(long n){
		long soma=0;
		startTimer();
		for(long i=0;i<n;i++)
			for(long j=0;j<n*n;j++)
				soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}
	static void ex1i(long n){
		long soma=0;
		startTimer();
		for(long i=0;i<n*n;i++)
			for(long j=0;j<i;j++)
				soma ++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}
	static void ex1j(long n){
		long soma=0;
		startTimer();
		for(long i=1;i<n;i*=2)
			soma++;
		System.out.println("Soma="+soma);
		stopTimer();
		showTime();
	}

	static int[][] ex3(long n){
		int soma=0;
		int[][] matriz = new int[(int)n][(int)n];
		for(int i=0;i<n;i++)
			for (int j = 0; j < n; j++)
				matriz[i][j] = soma++;
		return matriz;
	}

	static boolean ex3_a(int matriz[][], int x){
		startTimer();
		for(int i=1;i< matriz.length;i*=2)
			for(int j=0;j<matriz[i].length;j++)
				if(matriz[i][j]==x) {
					stopTimer();
					showTime();
					return true;

				}
		stopTimer();
		showTime();
		return false;
	}

	static boolean ex3_b(int matriz[][], int x) {
		int n = matriz.length;
		int linha = 0;
		int coluna = n - 1;

		while (linha < n && coluna >= 0) {
			if (matriz[linha][coluna] == x) {
				stopTimer();
				showTime();
				return true;
			} else if (matriz[linha][coluna] > x) {
				//como tabela por ordem crescente
				coluna--;
			} else {
				// O valor e menor move para baixo
				linha++;
			}
		}
		stopTimer();
		showTime();
		return false;
	}

	public static void main(String[] args) {
        long n = 90000000;

		/*ex1a( n );
	    ex1a( 4*n );
		ex1b( n );
		ex1b( 4*n );
		ex1c( n );
		ex1c( 4*n );
		ex1d( n );
		ex1d( 4*n );
		ex1e( n );
		ex1e( 4*n );
		ex1f( n );
		ex1f( 4*n );
		ex1g( n );
		ex1g( 4*n );
		ex1h( n );
		ex1h( 4*n );
		ex1i( n );
		ex1i( 4*n );
		ex1j( n );
		ex1j( 4*n );*/
		ex3_a(ex3(5000),2536);
		ex3_b(ex3(5000),2536);
	}
}
