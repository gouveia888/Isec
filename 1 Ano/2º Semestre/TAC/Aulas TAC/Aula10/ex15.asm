;Ficha N2_Assembly
.8086
.model small
.stack 2048

dados segment para 'data'
	TAB_org db 1,2,3,4,5,6,7,8,9,0
	Res dw 0
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds, ax
		xor SI,SI ;Inicia a 0 ou seja começa no primeiro elemento
		xor dx,dx ;coloca somador dos impares a 0
		mov cx,20 ; a tabela no maximo tem 20 elementos
	
	ciclo: xor AH,AH ;garantir que ah esta a 0 para que AX = al
		   MOV AL, TAB_org[SI] ;AX=TAB_org[SI] SI=0
		   mov BL, 2
		   div BL  ;dividir AX por bl (2)
		   cmp AH,0
		   je  HE_PAR ;Se for par vai para HE_PAR
		   add DL,TAB_org[SI] ;Adicionar o valor a Res
		   adc dh,0 ; dh=dh + carry (soma anterior caso exista) + 0
		   ;continua para HE_PAR mesmo sendo par apenas evita a soma
    HE_PAR: inc SI  ;quando e par evita o add e o adc do dl e dh
			cmp AX,0 ;verifica se o elemento da TAB_org[SI] é 0
			loopnz ciclo ;decrementa 1 a cx e vai para ciclo
			
			mov Res,dx; coloca em RES valor de dx
		    mov al, 0
		    mov ah, 4Ch ;Escolha da funçao DOS a executar de seguida 
		    int 21h 	;chamada a uma funçao DOS
		
	main endp
codigo ends
end main