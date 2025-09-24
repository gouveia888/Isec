#include <windows.h>
#include <tchar.h>

// ============================================================================
// Programa base (esqueleto) para aplica��es Windows    
// ============================================================================
// Cria uma janela de nome "Janela Principal" e pinta fundo de branco
// Modelo para programas Windows (composto por 2 fun��es): 
//    _tWinMain()    - Ponto de entrada dos programas windows
//                     1) Define, regista, cria e mostra a janela
//                     2) Loop de recep��o de mensagens provenientes do Windows
//    trataEventos() - Processamentos da janela (pode ter outro nome)
//                     1) � chamada pelo Windows (callback) 
//                     2) Executa c�digo em fun��o da mensagem recebida

LRESULT CALLBACK trataEventos(HWND, UINT, WPARAM, LPARAM);

// Nome da classe da janela (para programas de uma s� janela, normalmente este nome � 
// igual ao do pr�prio programa) "szprogName" � usado mais abaixo na defini��o das 
// propriedades do objecto janela
TCHAR szProgName[] = TEXT("Base");

// ============================================================================
// FUN��O DE IN�CIO DO PROGRAMA - _tWinMain()
// ============================================================================
// Em Windows, o programa come�a sempre a sua execu��o na fun��o _tWinMain()que desempenha
// o papel da fun��o _tmain() do C em modo consola. WINAPI indica o "tipo da fun��o" 
// (WINAPI para todas as declaradas nos headers do Windows e CALLBACK para as fun��es de
// processamento da janela)
// Par�metros:
//    hInst     - Gerado pelo Windows, � o handle (n�mero) da inst�ncia deste programa 
//    hPrevInst - Gerado pelo Windows, � sempre NULL para o NT (era usado no Windows 3.1)
//    lpCmdLine - Gerado pelo Windows, � um ponteiro para uma string terminada por 0
//                destinada a conter par�metros para o programa 
//    nCmdShow  - Par�metro que especifica o modo de exibi��o da janela (usado em  
//        	      ShowWindow()

int WINAPI _tWinMain(HINSTANCE hInst, HINSTANCE hPrevInst, LPTSTR lpCmdLine, int nCmdShow) {
    HWND hWnd;          // hWnd � o handler da janela, gerado mais abaixo por CreateWindow()
    MSG lpMsg;          // MSG � uma estrutura definida no Windows para as mensagens
    WNDCLASSEX wcApp;   // WNDCLASSEX � uma estrutura cujos membros servem para 
    // definir as caracter�sticas da classe da janela

// ============================================================================
// 1. Defini��o das caracter�sticas da janela "wcApp" 
//    (Valores dos elementos da estrutura "wcApp" do tipo WNDCLASSEX)
// ============================================================================
    wcApp.cbSize = sizeof(WNDCLASSEX);  // Tamanho da estrutura WNDCLASSEX
    wcApp.hInstance = hInst;  // Inst�ncia da janela actualmente exibida 
    // ("hInst" � par�metro de _tWinMain e vem 
    // inicializada da�)
    wcApp.lpszClassName = szProgName;  // Nome da janela (neste caso = nome do programa)
    wcApp.lpfnWndProc = trataEventos;  // Endere�o da fun��o de processamento da janela
    // ("TrataEventos" foi declarada no in�cio e
    // encontra-se mais abaixo)
    wcApp.style = CS_HREDRAW | CS_VREDRAW;  // Estilo da janela: Fazer o redraw se for
    // modificada horizontal ou verticalmente
    wcApp.hIcon = LoadIcon(NULL, IDI_ASTERISK);  // "hIcon" - handler do �cone normal
    // "NULL" - �cone definido no Windows
    // "IDI_AP..." -  �cone "aplica��o"
    wcApp.hIconSm = LoadIcon(NULL, IDI_SHIELD);  // "hIconSm" - handler do �con pequeno
    // "NULL" - �cone definido no Windows
    // "IDI_INF..." - �cone de informa��o
    wcApp.hCursor = LoadCursor(NULL, IDC_ARROW);  // "hCursor" - handler do cursor (rato) 
    // "NULL" - Forma definida no Windows
    // "IDC_ARROW" - Aspecto "seta" 
    wcApp.lpszMenuName = NULL;  // Classe do menu que a janela pode ter
    // (NULL - n�o tem menu)
    wcApp.cbClsExtra = 0;  // Livre, para uso particular
    wcApp.cbWndExtra = 0;  // Livre, para uso particular
    wcApp.hbrBackground = (HBRUSH)GetStockObject(GRAY_BRUSH);  // "hbrBackground" - handler
    //  para "brush" de pintura do fundo da janela. Devolvido por
    // "GetStockObject". Neste caso o fundo ser� branco.

// ============================================================================
// 2. Registar a classe "wcApp" no Windows
// ============================================================================
    if (!RegisterClassEx(&wcApp))
        return(0);

    // ============================================================================
    // 3. Criar a janela
    // ============================================================================
    hWnd = CreateWindow(
        szProgName,  // Nome da janela (programa) definido acima
        TEXT("Exercicio 1"),  // Texto que figura na barra do t�tulo
        WS_OVERLAPPEDWINDOW,	// Estilo da janela (WS_OVERLAPPED - normal)
        200,  // Posi��o x pixels (default - � direita da �ltima)
        200,  // Posi��o y pixels (default - abaixo da �ltima)
        1000,  // Largura da janela (em pixels)
        500,  // Altura da janela (em pixels)
        (HWND)HWND_DESKTOP,	// handle da janela pai (se se criar uma a partir de outra)
        //  ou HWND_DESKTOP se a janela for a primeira, criada a partir do "desktop"
        (HMENU)NULL,  // handle do menu da janela (se tiver menu)
        (HINSTANCE)hInst,  // handle da inst�ncia do programa actual ("hInst" � 
        // passado num dos par�metros de _tWinMain()
        0);  // N�o h� par�metros adicionais para a janela

    // ============================================================================
    // 4. Mostrar a janela
    // ============================================================================
    ShowWindow(hWnd, nCmdShow);  // "hWnd" - handler da janela, devolvido por 
    // "CreateWindow"; "nCmdShow" - modo de exibi��o (p.e. 
    // normal/modal); � passado como par�metro de _tWinMain()
    UpdateWindow(hWnd);  // Refrescar a janela (Windows envia � janela uma 
    // mensagem para pintar, mostrar dados, (refrescar)� 

// ============================================================================
// 5. Loop de Mensagens
// ============================================================================
// O Windows envia mensagens �s janelas (programas). Estas mensagens ficam numa fila de
// espera at� que GetMessage() possa ler "a mensagem seguinte"	
// Par�metros de "getMessage":
// 1) &lpMsg  - Endere�o de uma estrutura do tipo MSG ("MSG lpMsg" foi declarada no  
//              in�cio de _tWinMain()):
//                 HWND hwnd     - handler da janela a que se destina a mensagem
//                 UINT message  - Identificador da mensagem
//                 WPARAM wParam - Par�metro (p.e. c�digo da tecla premida)
//                 LPARAM lParam - Par�metro (p.e. se ALT tamb�m estava premida)
//                 DWORD time    - Hora a que a mensagem foi enviada pelo Windows
//                 POINT pt      - Localiza��o do mouse (x, y) 
// 2) handle - Janela para a qual se pretendem receber mensagens (=NULL se se pretendem
//             receber as mensagens para todas as janelas pertencentes � thread actual)
// 3) C�digo limite inferior das mensagens que se pretendem receber
// 4) C�digo limite superior das mensagens que se pretendem receber
//
// NOTA: GetMessage() devolve 0 quando for recebida a mensagem de fecho da janela,
//       terminando ent�o o loop de recep��o de mensagens, e o programa 
    while (GetMessage(&lpMsg, NULL, 0, 0) > 0) {
        TranslateMessage(&lpMsg);  // Pr�-processamento da mensagem (p.e. obter c�digo 
        // ASCII da tecla premida)
        DispatchMessage(&lpMsg);  // Enviar a mensagem traduzida de volta ao Windows, que
        // aguarda at� que a possa reenviar � fun��o de 
        // tratamento da janela, CALLBACK trataEventos (abaixo)
    }

    // ============================================================================
    // 6. Fim do programa
    // ============================================================================
    return (int)lpMsg.wParam;  // Retorna sempre o par�metro wParam da estrutura lpMsg
}

// ==============================================================================
// FUN��O DE PROCESSAMENTO DA JANELA
// Esta fun��o pode ter um nome qualquer: Apenas � neces�rio que na inicializa��o da
// estrutura "wcApp", feita no in�cio de _tWinMain(), se identifique essa fun��o. 
// Neste caso "wcApp.lpfnWndProc = WndProc"
//
// WndProc recebe as mensagens enviadas pelo Windows (depois de lidas e pr�-processadas
// no loop "while" da fun��o _tWinMain()
// Par�metros:
//    hWns   - O handler da janela, obtido no CreateWindow()
//    messg  - Identificador da mensagem (ver estrutura em 5. Loop)
//    wParam - O par�metro wParam da estrutura (a mensagem)
//    lParam - O par�metro lParam desta mesma estrutura
//
// A fun��o trataEventos utilizaa um "switch" com "cases" que descriminam a mensagem
// recebida e a tratar. Estas mensagens s�o identificadas por constantes (p.e. 
// WM_DESTROY, WM_CHAR, WM_KEYDOWN, WM_PAINT...) definidas em windows.h
// ==============================================================================

LRESULT CALLBACK trataEventos(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam) {
    switch (messg) {
    case WM_DESTROY:	     // Destruir a janela e terminar o programa 
        PostQuitMessage(0);  // "PostQuitMessage(Exit Status)"		
        break;
    default:
        // Neste exemplo, para qualquer outra mensagem (p.e. "minimizar","maximizar","restaurar")
        // n�o � efectuado nenhum processamento, apenas se segue o "default" do Windows
        return DefWindowProc(hWnd, messg, wParam, lParam);
    }
    return 0;
}