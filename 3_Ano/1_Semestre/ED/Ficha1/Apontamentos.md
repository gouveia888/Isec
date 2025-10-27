Classes de complexidade 

A complexidade de um algoritmo indica-nos como o tempo de execução varia consoante o tamanho de entrada de dados

O(1) - Constante
O(N) - linear
O(N^2) - Quadratica 
O(N^3) - Cubica (etc)
O(log n) - Logaritmica

!(image.png)

``` java
for(long i=0;i<n;i++)            //N
	for(long j=0;j<n;j++)       //N
		soma++;                     //N*N = N^2
//Se N aumentar 4 vezes  O(4N)^2 = 16*O(N)
```

``` java
for(long i=0;i<1000;i++) //1000
 for(long j=0;j<n;j++)   //N
 soma++;                //1
//T = 1000*N*1 = 1000N  O(N)
//Se N aumentar 4 vezes  O(N) = O(4N)
```

``` java
for(long i=1;i<n;i*=2) //N/2
 soma++; //N/2
 // T = log N
 // Se N aumentar 4 vezes  O(log 4N) = O log(N) + 3
```