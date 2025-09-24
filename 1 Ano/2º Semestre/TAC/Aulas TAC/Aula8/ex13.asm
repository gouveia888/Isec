;Ficha N2_Assembly
.8086
.model small
.stack 2048

dados segment para 'data'
	string db 'String MAIUSCULAS 1%24#4>5$' ;o ultimo elemneto da string é o $ (24H)
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
		cmp al,'%'		;al = 0?
		je  fim			;se já chegou ao fim da sstring termina
		cmp al, 'A'		;comparar al com espaço em branco
		jb	Nao_e_Mai		;se nao for igual a espaço vai para ciclo
		cmp al,'Z'
		ja fim	
		inc num_MAI
		jmp proximo		;vai incrementar o nosso
		
Nao_e_Mai: 	cmp al, 'a'		;comparar al com espaço em branco
			jb	Nao_e_min		;se nao for igual a espaço vai para ciclo
			cmp al,'z'
			ja Nao_e_min	
			inc num_min
			jmp proximo
			
Nao_e_min: 	cmp al, '0'	
			jb	proximo
			cmp al,'9'
			ja proximo	
			inc num_dig
			;jmp proximo é redundante

proximo: inc si
		 jmp ciclo
		
fim:    mov al, 0
		mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		int 21h 	;chamada a uma funçao DOS
		
	main endp
codigo ends
end main