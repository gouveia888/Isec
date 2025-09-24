#include <windows.h>
#include <tchar.h>
#include <io.h>
#include <stdio.h>
#include <fcntl.h> 
#include <math.h>

#define SHM_NAME TEXT("fmMsgSpace")
#define MUTEX_NAME TEXT("RWMUTEX")
#define EVENT_NAME TEXT ("NEWMSG")
#define SEM_BOUNCER_NAME _T("SEMPARTILHADO")
#define MAXUSERS 10
#define MSG_SIZE 100

typedef struct _MSG {
	TCHAR szMessage[MSG_SIZE];
} SharedMSG;

#define MSGBUFSIZE sizeof(SharedMSG)

typedef struct {
	HANDLE hMapFile;
	SharedMSG* sharedMSG;
	int threadMustContinue;
	HANDLE newMSG;
	HANDLE hRWMutex;
	HANDLE hSemBouncer;
} ControlData;

BOOL initMemAndSync(ControlData* cdata)
{
	cdata->hSemBouncer = CreateSemaphore(NULL, 2, MAXUSERS, SEM_BOUNCER_NAME); //SEGURANÇA, VALOR INICIAL, VALOR MAXIMO, NOME SEMAFRO

	//Creates or opens a named or unnamed file mapping object for a specified file.
	cdata->hMapFile = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		MSGBUFSIZE, //sizeof(SharedMsg)
		SHM_NAME);
	if (cdata->hMapFile == NULL)
	{
		_tprintf(TEXT("Error: CreateFileMapping (%d)\n"), GetLastError());
		return FALSE;
	}

	//Maps a view of a file mapping into the address space of a calling process

	cdata->sharedMSG = (SharedMSG*)MapViewOfFile(cdata->hMapFile,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		MSGBUFSIZE);



	//criar o mutex, uma vez que varios processos podem estar aceder ao mesmo espaço de memoria 
	cdata->hRWMutex = CreateMutex(NULL,
		FALSE,
		MUTEX_NAME);


	//criar o evento com vai servir para avisar se existe uma nova msg
	cdata->newMSG = CreateEvent(NULL,
		TRUE,
		FALSE,
		EVENT_NAME);// nome do evento para ser utilizado entre processos diferentes

	return TRUE;
}

DWORD WINAPI receiveMSG(LPVOID p) {
	ControlData* pcd = (ControlData*)p;
	SharedMSG msg;
	while (1) {
		WaitForSingleObject(pcd->newMSG, INFINITE);
		if (!pcd->threadMustContinue)
			return 0;
		WaitForSingleObject(pcd->hRWMutex, INFINITE);
		CopyMemory(&msg, pcd->sharedMSG, sizeof(SharedMSG));
		ReleaseMutex(pcd->hRWMutex);
		_tprintf(_T("Message Received: %s\n"), msg.szMessage);
		Sleep(1000);
	}
	return 0;
}

void sendMSG(ControlData* pcd) {
	SharedMSG msg;
	while (1) {
		WaitForSingleObject(pcd->hSemBouncer, INFINITE);
		_getts_s(msg.szMessage, MSG_SIZE);
		WaitForSingleObject(pcd->hRWMutex, INFINITE);
		CopyMemory(pcd->sharedMSG, &msg, sizeof(SharedMSG));
		ReleaseMutex(pcd->hRWMutex);
		if (_tcscmp(msg.szMessage, _T("exit")) == 0)
			pcd->threadMustContinue = 0;
		SetEvent(pcd->newMSG);
		Sleep(500);
		ResetEvent(pcd->newMSG);
		if (!pcd->threadMustContinue)
			break;
	}
}

int _tmain(int argc, TCHAR* argv[]) {


#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
#endif 


	HANDLE hThread;
	ControlData cdata;

	if (!initMemAndSync(&cdata))
	{
		_tprintf(TEXT("Error creating/opening shared memory and synchronization mechanisms.\n"));
		exit(1);
	}

	cdata.threadMustContinue = 1;
	hThread = CreateThread(NULL, 0, receiveMSG, &cdata, 0, NULL);

	_tprintf(TEXT("Send messages to other users. Type in 'exit' to leave.\n"));
	sendMSG(&cdata);//inicia a função sendMsg que escreve na memoria
	_tprintf(TEXT("Client is exiting...\n"));
	
	ReleaseSemaphore(cdata.hSemBouncer, 1, NULL); //QUANTIDADE A LIBERTAR

	WaitForSingleObject(hThread, INFINITE);

	UnmapViewOfFile(cdata.sharedMSG);
	CloseHandle(cdata.hMapFile);
	CloseHandle(cdata.hRWMutex);
	CloseHandle(cdata.newMSG);
	CloseHandle(hThread);
	return 0;
}



