#include <stdio.h>

// Recebe: Dimensões e endereço de uma matriz de inteiros
// A ordem dos parâmetros é crucial: o número de colunas tem que surgir antes do parâmetro que apresenta a matriz à função.
// A função imprime o conteúdo da matriz na consola
void printMat(int nLin, int nCol, int m[][nCol]){
    int i, j;

    /*for(i=0; i<nLin; i++){
        for(j=0; j<nCol; j++)
            printf("%d\t", m[i][j]);
        putchar('\n');
    }*/

    for(i=0; i<nLin;i++){
        for(j=0;j<nCol;j++){
            int p=i*nCol +j;
            printf(" %d", *(*m+p));
        }
    printf("\n");
    }

    return;
}

// Recebe: Dimensões e endereço de uma matriz de inteiros
// Recebe: Endereço de 2 variáveis inteiras onde deve colocar os indices das colunas com menor e maior média
// Escreve na consola as médias dos valores de cada coluna
void calcMediaCol(int nLin, int nCol, int m[][nCol], int *iMin, int *iMax){

    float media[nCol];
    int i,j,p;

    *iMax=0;
    *iMin=0;

    for(i=0;i<nCol;i++){
            media[i]=0;
        for(j=0;j<nLin;j++){
                p=j*nCol+i;//faz a iteraçao para cada coluna ou seja p[numero da coluna*coluna]+numero de colunas
                 //media[i]+=m[j][i];
                 media[i]+=(*(*m+p)); //é como precorrer apenas um vetor seguido usando a notaçao ponteiro
            }
        //printf("Soma da coluna %d:%d\t", i+1,media[i]);
        media[i]/=nLin;
        //printf("Media da coluna %d: %.2f\n",i+1,media[i]);
    }

    for(i=0;i<nCol;i++){
        if(media[i]>media[*iMax])
            *iMax=i;
        if(media[i]<media[*iMin])
            *iMin=i;
    }

    (*iMax)++;
    (*iMin)++;
}

// Recebe: Dimensões e endereço de uma matriz de inteiros quadrada
// Efetua a transposicao dos valores na matriz
void tMat(int n, int mat[][n]){

    int i, j, aux;

    /*for (i = 0; i < n; i++) {
        for (j = i + 1; j < n; j++) {
            aux = mat[i][j];
            mat[i][j] = mat[j][i];
            mat[j][i] = aux;
        }
    }*/

    for(i=0;i<n;i++){
        for(j=i+1;j<n;j++){
            int l=i*n+j;
            int c=j*n+i;
            aux= *(*mat+l);
            *(*mat+l)=*(*mat+c);
            *(*mat+c)=aux;
        }
    }
}

int unicaMat(int nLin, int nCol, int mat[][nCol]){

    int *p=&mat, *q, i;

    /*
    for (i=0; i<nCol- 1; i++){
        for (q = mat + nCol - 1; q != p; q--) {
            if (*p == *q)
            return 0;
        }
        p++;
    }
return 1;*/

    for(i=0;i<nCol;i++){
        for(q=(*p)+nCol+1; q!=p || q<nCol;q++){
            if(*q==*p)
                return 0;
        }
    }
    return 1;
}


int main() {

    int mat1[3][3] = {{1,2,3},{7,8,9},{12,13,14}};
    int mat2[2][5] = {{10,2,13,4,8},{5, 9, 12, 1, 0}};

    int a=-1,b=-1;

    printf("Mat 1:\n");
    printMat(3, 3, mat1);
    printf("\nMat 2:\n");
    printMat(2, 5, mat2);

    calcMediaCol(2, 5, mat2, &a, &b);
    printf("\n\nCol. com menor media: %d\nCol. com maior media: %d\n", a, b);

    //printMat(3, 3, mat1);
    tMat(3, mat1);
    printf("\nMat 1 Transposta:\n");
    printMat(3, 3, mat1);

    printf("\nA matriz retorna o valor %d",unicaMat(3, 3, mat1));

    return 0;
}
