;Ficha N2_Assembly
.8086
.model small
.stack 2048

dados segment para 'data'
	string db 'String MAIUSCULAS',0
	num_MAI db 0
	num_min db 0
	num_dig db 0
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds, ax
		lea si, string   ;load effective address coloca [BX] a apontar para start ---- Estilo ponteiro ----
		
ciclo:	mov al, [SI]	;coloca em al o valor apontado por bx
		cmp al,0		;al = 0?
		je  fim			;se já chegou ao fim da sstring termina
		cmp al, 'A'		;comparar al com espaço em branco
		jb	Nao_e_Mai		;se nao for igual a espaço vai para ciclo
		cmp al,'Z'
		ja	Nao_e_Mai
		add al, 'a' - 'A'		;vai incrementar o nosso
		mov [si],al
Nao_e_Mai:	inc si
			jmp ciclo
		
fim:    mov al, 0
		mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		int 21h 	;chamada a uma funçao DOS
		
	main endp
codigo ends
end main