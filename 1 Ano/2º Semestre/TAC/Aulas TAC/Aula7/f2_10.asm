;Ficha N2_Assembly
.8086
.model small
.stack 2048

dados segment para 'data'
	Vetor1 db 1,2,3,4,5,6,7,8,9,10
	Vetor2 db 11,12,13,14,15,16,17,18,19,20
	Vetor3 dw 10 dup(?) ; cada elemento 2 bytes
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds, ax
		mov cx,10 ;faz ciclo 10 vezes
		xor si,si; limpa o si = 0
		xor di,di
		
ciclo:  xor ah,ah ;limpa o ah = 0
		mov al,Vetor1[si]
		add al,Vetor2[si]
		adc ah,0
		mov Vetor3[di],ax
		inc si
		add di,2 ; di = di+2
		
		loop ciclo ;numero de finito de saltos quanto valor de cx, loop => cx=cx-1 ate cx=0
		
		mov ah, 4ch
		int 21h
	main endp
codigo ends
end main