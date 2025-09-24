;Ficha N5_Assembly copia a segunda e terceira linha 15 linhas para a frente
.8086
.model small
.stack 2048

dados segment para 'data'
	linha db 2
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds, ax
		mov ax, 0B800h
		mov es, ax
		
		mov bl,160
		mov al,linha
		mul bl                   ;AX=linha*160
		mov bx,ax				  ;BX=linha*160
		
		mov dl,160
		mov al,linha
		add al,15
		mul dl                    ;AX=(LInha + 15) * 160	
		mov SI,ax				  ;SI=15*160
;		ADD SI,BX				  ;SI = (LInha + 15) * 160

		mov cx,2*80				;para copiar 2 linhas
		
				ciclo:
						mov ax, es:[bx]			;copia caracter e cor
						mov ES:[si], AX			;copia caracter e cor
						inc bx
						inc bx					;anda 2 bytes para a frente
						inc si				;incrementa 2 a SI
						inc si
					loop ciclo
	
		    mov al, 0
		    mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		    int 21h 	;chamada a uma funçao DOS
		
	main endp
codigo ends
end main