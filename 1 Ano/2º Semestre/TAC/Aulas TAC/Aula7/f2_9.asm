;Ficha N2_Assembly
.8086
.model small
.stack 2048

dados segment para 'data'
	Vetor1 db 'Cadeira - T.A.C.',0
	Vetor2 db 20 dup(?)
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds, ax
		xor si,si ;iniciar si a 0
		
ciclo:	mov ah,Vetor1[si]
		mov Vetor2[si],ah ; Vetor2[si]= Vetor1[si]
		inc si
		cmp ah,0  ;compara ah com 0 
		jne ciclo ;salta para ciclo se ah for diferente de 0 (jump not equal ou  jump not zero) label traduido para endereço
		
		mov al,0
		mov ah, 4ch
		int 21h
	main endp
codigo ends
end main