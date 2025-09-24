//produtor
//Produtor

#include <windows.h>
#include <tchar.h>
#include <fcntl.h>
#include <stdio.h>
#include <io.h>
#include <time.h>


#define SHM_NAME TEXT("SHM_PC")           // nome da memoria partilhada
#define MUTEX_NAME TEXT("MUTEX")          // nome do mutex   -> em casa devem pensar numa solução para ter mutex´s distintos de forma a não existir perda de performance 
#define SEM_WRITE_NAME TEXT("SEM_WRITE")  // nome do semaforo de escrita
#define SEM_READ_NAME TEXT("SEM_READ")    // nome do semaforo de leitura
#define BUFFER_SIZE 10

typedef struct _BufferCell {
	unsigned int id; //id do produtor
	unsigned  val; // valor que o produtor gerou
} BufferCell;

typedef struct _SharedMem {
	unsigned int p;   // contador partilhado com o numero de produtores  
	unsigned int c;   // contador partilhado com o numero de consumidores   
	unsigned int wP;  // posicao do buffer circular para a escrita     
	unsigned int rP;  // posicao do buffer circular para a escrita  
	BufferCell buffer[BUFFER_SIZE]; // buffer circular
} SharedMem;

typedef struct _ControlData {
	unsigned int shutdown;  // flag "continua". 0 = continua, 1 = deve terminar
	unsigned int id;        // id do processo  
	unsigned int count;     // contador do numero de vezes  
	HANDLE hMapFile;        // ficheiro de memoria 
	SharedMem* sharedMem;   // memoria partilhada
	HANDLE hMutex;          // mutex - trabalho de casa -> acrescentar os outros 3 mutexes
	HANDLE hWriteSem;       // sem?foro "aguarda por items escritos"
	HANDLE hReadSem;        // sem?foro "aguarda por posições vazias"
} ControlData;




//função que inicializa a memoria, ...

BOOL initMemAndSync(ControlData* cdata) {

	BOOL firstProcess = FALSE;

	cdata->hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, FALSE, SHM_NAME);

	if (cdata->hMapFile == NULL) {
		cdata->hMapFile = CreateFileMapping(
			INVALID_HANDLE_VALUE,
			NULL,
			PAGE_READWRITE,
			0,
			sizeof(SharedMem),
			SHM_NAME);

		firstProcess = TRUE;

		if (cdata->hMapFile == NULL) {
			_tprintf(TEXT("Error: CreateFileMapping (%d)\n"), GetLastError());
			return FALSE;
		}
	}

	cdata->sharedMem = (SharedMem*)MapViewOfFile(cdata->hMapFile,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		sizeof(SharedMem));

	if (cdata->sharedMem == NULL) {
		_tprintf(TEXT("Error: MapViewOfFile (%d)\n"), GetLastError());
		CloseHandle(cdata->hMapFile);
		return FALSE;
	}

	if (firstProcess) {

		cdata->sharedMem->p = 0;
		cdata->sharedMem->c = 0;
		cdata->sharedMem->rP = 0;
		cdata->sharedMem->wP = 0;

	}
		cdata->hMutex = CreateMutex(NULL,
			FALSE,
			MUTEX_NAME);

		if (cdata->hMutex == NULL) {
			_tprintf(TEXT("Error: Mutex (%d)\n"), GetLastError());
			UnmapViewOfFile(cdata->sharedMem);
			CloseHandle(cdata->hMapFile);
			return FALSE;
		}

		cdata->hWriteSem = CreateSemaphore(NULL, BUFFER_SIZE, BUFFER_SIZE, SEM_WRITE_NAME);

		if (cdata->hWriteSem == NULL) {
			_tprintf(TEXT("Error: WriteSem (%d)\n"), GetLastError());
			UnmapViewOfFile(cdata->sharedMem);
			CloseHandle(cdata->hMapFile);
			CloseHandle(cdata->hMutex);
			return FALSE;
		}

		cdata->hReadSem = CreateSemaphore(NULL, 0, BUFFER_SIZE, SEM_READ_NAME);

		if (cdata->hReadSem == NULL) {
			_tprintf(TEXT("Error: ReadSem (%d)\n"), GetLastError());
			UnmapViewOfFile(cdata->sharedMem);
			CloseHandle(cdata->hMapFile);
			CloseHandle(cdata->hWriteSem);
			CloseHandle(cdata->hMutex);
			return FALSE;
		}
	

	return TRUE;
}



//função thread que vai estar a produzir
DWORD WINAPI produce(LPVOID p)
{
	ControlData* cdata = (ControlData*)p;
	BufferCell cell;
	int ranTime;

	cell.id = cdata->id; //id do produtor, incrementado no main
	while (1) {
		if (cdata->shutdown)
			return 0;
		cell.val = rand() % 80 + 10; //numero produzido
		WaitForSingleObject(cdata->hWriteSem, INFINITE);
		WaitForSingleObject(cdata->hMutex, INFINITE);
		
		//escreve na memoria partilhada
		CopyMemory(&(cdata->sharedMem->buffer[(cdata->sharedMem->wP)++]), &cell, sizeof(BufferCell));

		if (cdata->sharedMem->wP == BUFFER_SIZE)
			cdata->sharedMem->wP = 0; 
		ReleaseMutex(cdata->hMutex);
		ReleaseSemaphore(cdata->hReadSem, 1, NULL); // assinala ao consumidor
		_tprintf(_T("P%d produced %d.\n"), cell.id, cell.val);
		ranTime = rand() % 2 + 2;
		Sleep(ranTime * 1000);  //espera tempo aleatorio
		cdata->count++;
	}

		return 0;
}





int _tmain(int argc, TCHAR* argv[])
{
	ControlData cdata;
	HANDLE hThread;
	TCHAR command[100];
#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif
	srand((unsigned int)time(NULL));
	cdata.shutdown = 0; //flag
	cdata.count = 0; //numero de itens
	if (!initMemAndSync(&cdata))
	{
		_tprintf(TEXT("Error creating/opening shared memory and synchronization mechanisms.\n"));
		exit(1);
	}

	WaitForSingleObject(cdata.hMutex, INFINITE);
	cdata.id = ++(cdata.sharedMem->p); // incrementa o contador partilhado com o numero de produtor
	ReleaseMutex(cdata.hMutex);

	hThread = CreateThread(NULL, 0, produce, &cdata, 0, NULL);
	_tprintf(TEXT("Type in 'exit' to leave.\n"));
	do {
		_getts_s(command, 100);
	} while (_tcscmp(command, TEXT("exit")) != 0);
	cdata.shutdown = 1; //altera a flag para que a thread termine

	WaitForSingleObject(hThread, INFINITE);
	_tprintf(TEXT("P%d produced %d items.\n"), cdata.id, cdata.count);
	CloseHandle(hThread);
	UnmapViewOfFile(cdata.sharedMem);
	CloseHandle(cdata.hMapFile);
	CloseHandle(cdata.hMutex);
	CloseHandle(cdata.hWriteSem);
	CloseHandle(cdata.hReadSem);
	return 0;

}