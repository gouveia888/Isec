;Ficha N2_Assembly
.8086
.model small
.stack 2048

dados segment para 'data'
	Adress1 db 201 ;11001001(2)
	Adress2 db 11110110b
	Adress3 dw ?
dados ends

codigo segment para 'code'
	main proc
		assume cs:codigo, ds:dados
		mov ax, dados
		mov ds, ax
		
		mov al,Adress1
		add al,Adress2 ; al = Adress1 + Adress2
		xor ah,ah 	   ; limpa o ah e coloca a 0
		adc ah,0		   ; ah= ah + carry + 0
		mov Adress3,ax
		
		mov al, 0
		mov ah, 4ch
		int 21h
	main endp
codigo ends
end main