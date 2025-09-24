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
		call MEDIA		
		
EndRet:	mov average, al	;coloca em al o valor apontado por bx

		
fim:    mov al, 0
		mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		int 21h 	;chamada a uma funçao DOS
Main endp
		;Calculo da media paramentos de entrada (al,ah) parametro de saida (al)
		
MEDIA proc
		
	push bx ;guarda bx na pilha
	pushf   ;guarda flags na pilha
	add al,ah ;al = al+ah
	mov ah,0
	adc ah, 0 ;se existir carry soma a ah ou seja ax=al+ah
	mov bl,2  
	div bl    ;al = ax/2 "INSTRUCAO DIV TRABALHA COM ESTES REGISTOS"
				;SP (Apontador para o incio da pilha) que tem a seguinte infromaçao na pilha 
				;Flag
				;bx
				;endereço de retorno
				;counteudo
	popf	  ;guardar as flags no registo das flags e o ponteiro da pilha (SP) fica a apontar para BX
	pop bx	  ;SP fica a pontar para o endereço de retorno e liberta BX para ficar com o conteudo que tinha antes de entrar na funçao
	ret		  ;SP fica a apontar para conteudo e o endereço de retorno vai para o registo IP

MEDIA endp		

		
end Main