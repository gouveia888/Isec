;Programa que usa um procedimento para calcular a media de dois bytes, usando registos para implentar a passagem de parametros
.8086
.model small
.stack 2048

DATA_HERE segment
	
	lo_num db 64h
	hi_num db 11001000b ;c8
	average db ?
DATA_HERE ends

STACK_HERE segment
	dw 40 DUP(0)
STACK_HERE ends


CODE_HERE segment
		assume cs:CODE_HERE, ds:DATA_HERE 
	Main proc
		mov ax, DATA_HERE
		mov ds, ax
		
		MOV AL,lo_num
		Mov AH, hi_num
		
		push ax ;coloca ax na pilha
		call MEDIA		;coloca na pilha o endereço de retorno
						;endereço de registo
						;AX
		pop ax			;retira o ax da pilha
		mov average, AL
		
fim:    mov al, 0
		mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		int 21h 	;chamada a uma funçao DOS
Main endp
		;Calculo da media paramentos de entrada (al,ah) parametro de saida (al)
		
MEDIA proc
		
	pushf
	push ax
	push bx
	push bp
	mov bp,Sp
	mov ax, [bp+10] ;10 bytes NA PILHA BP - BX - AX - FLAGS - END. retorno - AX (CONTEM lo_num E hi_num)
					;	[bp+10] aponta para o lo_num
	
	
	add al,ah ;al = al+ah
	mov ah,00H
	adc ah, 00h;se existir carry soma a ah ou seja ax=al+ah
	mov bl,02H  
	div bl    
	
	mov [bp+10], ax ;10 bytes NA PILHA BP - BX - AX - FLAGS - END. retorno - AX (CONTEM media e resto)
	
	pop bp	  ;guardar as flags no registo das flags e o ponteiro da pilha (SP) fica a apontar para BX
	pop bx
	pop ax	  ;SP fica a pontar para o endereço de retorno e liberta BX para ficar com o conteudo que tinha antes de entrar na funçao
	popf
	ret		  ;SP fica a apontar para conteudo de ax e o endereço de retorno vai para o registo IP

MEDIA endp		

		
end Main