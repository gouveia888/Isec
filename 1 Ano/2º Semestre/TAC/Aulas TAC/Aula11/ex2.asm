;Ficha N5_Assembly copia duas colunas x colunas para a frente
.8086
.model small
.stack 2048

dados segment para 'data'
	coluna db 5
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds, ax
		mov ax, 0B800h
		mov es, ax
		
		mov al,coluna
		mov bl,2
		mul bl                   
		mov bx,ax				  ;BX=coluna*2
		
		mov si,bx      ; si=bx
		add si,20*2    ;adiciona 20 colunas a si SI= BX + 20*2
		mov cx, 25	  ; CONTADOR de 25 linhas do ecra
		
				ciclo:
						mov ax, es:[bx]			;copia caracter e cor
						mov ES:[si], AX			;copia caracter e cor
						mov ax, es:[bx+2]
						mov es:[si+2], ax
						add bx, 80*2			;aponta para a linha de baixo
						add si, 80*2			; //          //      //
					loop ciclo
	
		    mov al, 0
		    mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		    int 21h 	;chamada a uma funçao DOS
		
	main endp
codigo ends
end main