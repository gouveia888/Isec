;Ficha N2_Assembly
.8086
.model small
.stack 2048

dados segment para 'data'
	Numero dw 10230
	Quant db ?
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds,ax
		mov bx,10   ; Vou fazer sucessivas divisoes por 0
		mov ax,Numero   ;em DX:AX = Numero
		
	ciclo:xor dx,dx ;vamos colocar 0 a esquerda do Numero
		  div bx	;divisao de DX:AX / BX
		  cmp AX,0  ;chegou a fim dos digitos de Numero
		  je fim    ; se AX for 0 vai para fim
		  cmp dx,0  ;se resto igual a 0 é porque digito de Numero é 0
		  jne ciclo ;se nao for 0 vai para ciclo
		  inc Quant ;senao incrementa o Quant
		  jmp ciclo ;vai para ciclo
		  
	fim:mov al, 0
		mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		int 21h 	;chamada a uma funçao DOS
		
	main endp
codigo ends
end main