;Ficha N2_Assembly
.8086
.model small
.stack 2048

dados segment para 'data'
	start db 'Mensagem  para contar espacos em branco',0 ;o ultimo elemneto da string é o 0
	space db 0
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds, ax
		lea bx, start   ;load effective address coloca [BX] a apontar para start ---- Estilo ponteiro ----
		
ciclo:	mov al, [BX]	;coloca em al o valor apontado por bx
		inc BX			;coloca BX a apontar para o proximo elemneto
		cmp al,0		;al = 0?
		je  fim			;se já chegou ao fim da sstring termina
		cmp al, ' '		;comparar al com espaço em branco
		jne ciclo		;se nao for igual a espaço vai para ciclo
		inc space		;incrementa um espaço em branco
		jmp ciclo		
		
fim:    mov al, 0
		mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		int 21h 	;chamada a uma funçao DOS
		
	main endp
codigo ends
end main