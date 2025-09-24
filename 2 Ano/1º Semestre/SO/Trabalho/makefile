COMPILADOR=gcc

tudo : manager feed limpar

manager : manager.o
	$(CC) manager.o -o manager

manager.o : manager.c utils.h
	$(COMPILADOR) manager.c -c

feed : feed.o
	$(COMPILADOR) feed.o -o feed

feed.o : feed.c utils.h 
	$(COMPILADOR) feed.c -c

limpar : 
	$(RM) *.o;