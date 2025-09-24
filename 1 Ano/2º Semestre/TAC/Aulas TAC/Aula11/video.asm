.8086
.model small
.stack 2048

cseg	segment para public 'code'
	assume  cs:cseg

Main  proc
	mov   ax,0b800h %memoria de video
	mov   es,ax     % mover ax para es (extra segment pode apontar para outros segmentos)

	mov	al,0h    
	mov	ah,'*' 
	mov	bx,0
	mov	cx,25*80      % registo que controla o ciclo
					  %inicio da matrix es com deslocamento 0 coloca o caracter
ciclo:    
	mov	es:[bx],ah    %insere na matriz video es com deslocamento bx com caracter
	mov	es:[bx+1],al  %cor da celula de memoria atual es com deslocamento BX+1 coloca o caracter
	inc	bx		
	inc	bx			 %INCREMENTAMOS 2 vezes cada celula de memoria ocupa 2 bytes
	inc	al		     %incremeneta a cor de 0 a 255	
	loop	ciclo    % intruçao   Se (cx=cx-1) > 0 vai para ciclo senao segue

	mov     ah,4CH
	int     21H
main	endp

cseg    ends
end     main
