#include <Windows.h>
#include <TCHAR.H>
#include <fcntl.h>

//ALINEA G +1 THREAD

// função de thread para calcular o sumatório dos numero pares
// pausa, no final de 200 numeros, por 1 segundo
DWORD WINAPI SumEvenNumbers(LPVOID dados) {
	int* sum = (int*)dados;  // ptr para var da main onde fica/é atualiz o result.
	int i, evenCount = 0;
	for (i = 1; i <= 1000; i++)
		if (i % 2 == 0) {
			*sum += i;
			evenCount++;

			if (evenCount % 200 == 0)
				Sleep(10);
		}
	return *sum;  // <- para quê ?? ou *ptr ou return
}


// função aux para verificar se num é primo
DWORD IsPrime(DWORD num) {
	if (num <= 1)  // o numero 1 NÃO É um numero primo   <- não é ???????
		return 0;

	for (DWORD i = 2; i < num; i++) {
		if (num % i == 0) {
			return 0;
		}
	}
	return 1; // se devolver 1 é numero primo
}


// função de thread para calcular sum dos números primos
DWORD WINAPI SumPrimeNumbers(LPVOID lpParam) {
	int i;
	int primeCount = 0;
	int* sum = (int*)lpParam;

	for (i = 1; i <= 3; i++) { //alterar valor dos primos
		if (IsPrime(i)) {
			*sum += i;
			primeCount++;

			if (primeCount % 15 == 0)
				Sleep(1000);
		}
	}
	return *sum;
}

//main

int _tmain(int argc, LPTSTR arg[]) {
	int sumEven = 0, sumPrime = 0;
	int sumEvenRet, sumPrimeRet;

	HANDLE hthread[2]; // array de handles para as threads
	// podiam ser duas variáveis independentes
	// mas se forem "várias" mais vale um array (como em SO)
#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	hthread[0] = CreateThread(
		NULL,           // ptr p/ security attributes. NULL = use default
		0,              // init stack size. 0 = use default 
		SumEvenNumbers, // ptr p/ função da thread
		&sumEven,       // argumento p/ função da thread
		0,              // creation flags. 0 = use default
		NULL);          // ptr p/ DWORD para thread ID. NULL = "i don't care and don't want it"
	
	hthread[1] = CreateThread(
		NULL,
		0,
		SumPrimeNumbers,
		&sumPrime,
		0,              //create_suspended
		NULL);

		//resumethread(hthread[1]);

		//htread[2] = CreateThread....

//b	WaitForSingleObject(hthread[0], 1000);   // <- ****
	//WaitForSingleObject(hthread[0], INFINITE);
	//WaitForSingleObject(hthread[1], INFINITE);
//b (alternativa
	DWORD resultado;
	int count = 0;
	do {
		resultado = WaitForMultipleObjects(
			2-count, //numero de threads
			hthread, //ponteiro para o array com as id´s das threads -REORGANIZAR ARRAY-
			FALSE, //true espera por todas, false só a primeira
			INFINITE); //limite de tempo, pode ser INFINITE
		int nt = resultado - WAIT_OBJECT_0; 

		if (nt==0) {
			GetExitCodeThread(
				hthread[0],//endereço da thread
				&sumEvenRet);  // apanha o resultado 
			_tprintf(_T("Soma Pares\nValor da thread por argumentos %d, via retorno %d\n"), sumEven, sumEvenRet);
			CloseHandle(hthread[0]);

			WaitForSingleObject(hthread[1], INFINITE);
			GetExitCodeThread(
				hthread[1],//endereço da thread
				&sumPrimeRet);  // apanha o resultado 

			_tprintf(_T("Soma Primos\nValor da thread por argumento %d, via retorno %d\n"), sumPrime, sumPrimeRet);
			CloseHandle(hthread[1]);
			
		}

		if (nt == 1) {
			GetExitCodeThread(
				hthread[1],//endereço da thread
				&sumPrimeRet);  // apanha o resultado 

			_tprintf(_T("Soma Primos\nValor da thread por argumento %d, via retorno %d\n"), sumPrime, sumPrimeRet);
			CloseHandle(hthread[1]);

			WaitForSingleObject(hthread[0], INFINITE);
			GetExitCodeThread(
				hthread[0],//endereço da thread
				&sumEvenRet);  // apanha o resultado 
			_tprintf(_T("Soma Pares\nValor da thread por argumentos %d, via retorno %d\n"), sumEven, sumEvenRet);
			CloseHandle(hthread[0]);
		}
		count++;
	} while (count < 2 );


	return 0;

}


/*

INTEL

HANDLE CreateThread(
  LPSECURITY_ATTRIBUTES   lpThreadAttributes, -- child process can hinerit the handle? NULL = não
  SIZE_T                  dwStackSize,        -- initial stack size in bytes (arredondado para multiplo. de page size = 4k). 0=default
  LPTHREAD_START_ROUTINE  lpStartAddress,     -- ptr para a thread function
  LPVOID                  lpParameter,        -- argumento para a função = whatever caiba num sizeof de void * -> como em SO
  DWORD                   dwCreationFlags,    -- flags (o = deafault). de interesse: CREATE_SUSPENDED
  LPDWORD                 lpThreadId          -- ptr para a dword para cicar com a thread ID. NULL = don't care & don't want it
);


*/