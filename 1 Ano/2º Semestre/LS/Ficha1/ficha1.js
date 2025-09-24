'use strict';
/*
let a=3;
let b=6;
let c;

console.log("1 =", a+b);
console.log("2 =", a+"c");
console.log("3 =", a+"5");
console.log("4 =", a+"b"); 
console.log("5 =", a+c); 
console.log("6 =", c); 
console.log(`Variavel a*b = ${a*b} ( a=${a} e b=${b})`);

console.log("Exercicio 2");

let a=3;
let b=6;
let c;
c=a+b; 
console.log("1 =",c); 
c=a+"---"+b;
console.log("2 =",c); 
c="6";
console.log("3 =",a+c); 
console.log("4 =",b===c); 
console.log("5 =",b==c); 
console.log("6 =",b!==c); 
console.log("7 =",b!=c); 
console.log("8 =",a++); 
console.log('9 = ',a); 
a=4;
console.log('10 = ',++a);
console.log('11 = ',+a);


console.log("Exercicio 3");

const disciplina;
disciplina="Linguagens Script";
console.log(disciplina); //a variavel const tem de ser declarada


console.log("Exercicio 4");
let uc='Linguagens ';
uc+='Script';
console.log('Disciplina:'+uc +' - 2 semestre');


console.log("Exercicio 5");
const nome1='Nuno'
const nome2='Ricardo'
const resultado = `Os nomes são ${nome1} e ${nome2}`
console.log(resultado);
console.log(resultado+' e Filipe');

console.log("Exercicio 2A e B");
const num1=11;
const num2=100;
const num3=211;

if(num1>num2 && num1>num3){
    console.log(`O maior numero entre ${num1} , ${num2} e ${num3} é ${num1}`);
}else if(num2>num1 && num2>num3){
    console.log(`O maior numero entre ${num1} , ${num2} e ${num3} é ${num2}`);
}else if(num3>num1 && num3>num2){
    console.log(`O maior numero entre ${num1} , ${num2} e ${num3} é ${num3}`);
}

console.log("Exercicio 2C");

const min = 2;
const max = 4;
var soma = 0;

for (let index = min; index <= max; index++) {
    soma = soma + index;
}

console.log(`A soma entre ${min} e ${max} é ${soma}`);


console.log("Exercicio 3A e 3B");

const numeros = [5,10,-12,2,10,-5,-2,-3];
var max = numeros[0];

console.log(numeros.length);

numeros.forEach(num => {
    if(num > max){
        max = num;
    }
});

console.log(`O maior numero do array é o ${max}`);


console.log("Exercicio 3C");

const numeros = [5,10,-12,2,10,-5,-2,-3];
var num = 0;
var soma = 0;
for (let index = 0; index < numeros.length; index++) {
    if(numeros[index] > 0){
        num = num + 1;
        soma = soma + numeros[index];
    }
}

console.log(`Existem ${num} numeros positivos e soma dos numeros é ${soma}`);

console.log("Exercicio 4A");

let n = 50
if (true) {
 let n = 2
 console.log(n)
}
console.log(n);

console.log("Exercicio 4B");

let n = 50
if (true) {
console.log(n);
n = 2
console.log(n)
}
console.log(n); //Nao entendi

console.log("Exercicio 4C");

let n = 50
if (true) {
//console.log(n);
let n = 2
console.log(n)
}
console.log(n);

console.log("Exercicio 4D");

let str = 'Linguagens Script';
function fazQualquerCoisa() {
console.log(str);
}
fazQualquerCoisa();

console.log("Exercicio 4E");

let str = 'Linguagens';
function fazQualquerCoisa() {
str = 'Script';
}
console.log(str);
fazQualquerCoisa();
console.log(str);

console.log("Exercicio 4F");

function fazQualquerCoisa() {
    str = 'Script';
    }
    fazQualquerCoisa();
    console.log(str);

console.log("Exercicio 4G");

function fazQualquerCoisa() {
    let str = 'Script';
    }
    fazQualquerCoisa();
    console.log(str);

console.log("Exercicio 4H");

let str = 'Linguagens';
function fazQualquerCoisa() {
let str2 = ' Script';
console.log(str+str2);
}
fazQualquerCoisa();
console.log(str+str2);

console.log("Exercicio 4I");

var str = 'Linguagens';
function fazQualquerCoisa() {
var str2 = ' Script ';
if (str==='Linguagens') {
var dim='ok';
console.log("->"+dim);
}
console.log(str+str2+"- "+dim);
}
fazQualquerCoisa();
console.log(str+str2);

console.log("Exercicio 4J")

let str = 'Linguagens';
function fazQualquerCoisa() {
let str2 = ' Script';
if (str.length > str2.length) {
let dim="Primeira é maior!"
console.log(dim);
}
else if (str.length === str2.length) {
let dim="São iguais!"
console.log(dim);
}
else {
let dim="Segunda é maior!"
console.log(dim);
}
console.log(str+str2+"-"+dim);
}
fazQualquerCoisa();

console.log("Exercicio 4K")

function mensagem() {
    let nome='José';
    console.log(`Olá ${nome}`);
    }
    mensagem(); 
    mensagem('Maria'); 
    mensagem('Maria','Jose','Vieira');

console.log("Exercicio 4L")

mensagem(); 
function mensagem() {
    let nome='José';
    console.log(`Olá ${nome}`);
}

console.log("Exercicio 4M")

function mensagem(nome='!') {
    console.log(`Olá ${nome}`);
    }
    mensagem(); 
    mensagem('Maria'); 
    mensagem('Jose'); 
    mensagem('Cristiana','Areias'); */


//console.log("Exercicio 5");
/*
function compara(num1, num2){

    num1==num2 ? console.log("true") : console.log("false");

    return num1==num2 ? true : false;
    
    /*if(num1 === num2){
        return true;
    }else{
        return false;
    }
}

function ParOuImpar(num){
    if(num % 2 == 0){
        console.log("Numero Par");
    }else{
        console.log("Numero Impar");
    }
}

let num1=3;
let num2=3;
let valor = compara(num1, num2);
console.log(valor);
ParOuImpar(num1);

function obtemQuadrado(num){
    return num*num;
}

console.log(obtemQuadrado(2)) 
console.log(obtemQuadrado(9))
console.log(obtemQuadrado(10)) 

function areaRetangulo(num1, num2=num1){
    return num1*num2;
}

console.log(areaRetangulo(5,10)) // 50
console.log(areaRetangulo(10,20)) // 200
console.log(areaRetangulo(5)) 

console.log("Exercio 5E");

function contaVogais(str){
    let vogais=["a","e","i","o","u"];
    let cont = 0;
    str=str.toLowerCase()

    for (let index = 0; index < str.length; index++) {
        for (let x = 0; x < vogais.length; x++) {
            if(str.charAt(index)==vogais[x])//indexOf
                cont++;  
        }     
    }
    return cont;
}

console.log(contaVogais("Ola")) //2
console.log(contaVogais("Linguagens Script")) //5 

console.log("Exercicio 5F");

const palavras=['angular','bootstrap','javascript','vue','svelte','react'];
insertBegin(palavras, "ember"); 
imprimeArray(palavras);

function imprimeArray(palavras){

    for (let x = 0; x < palavras.length; x++) {
            console.log(palavras[x]);
        
    }
}

function insertBegin(array, palavra){
    array.unshift(palavra); //pull
}*/

let jogadorAtual ="0";
let tabuleiro = [
[" ", " ", " "],
[" ", " ", " "],
[" ", " ", " "],
];
let jogador = "X";



function imprimeTabuleiro() {
    let str="_______\n";
    for (let i = 0; i < 3; i++) {
        str+=""
        for (let j = 0; j < 3; j++) {
           str+= tabuleiro[i][j] == " " ? " |" : tabuleiro[i][j] + " |";
        }
        str+= "\n";
    }
    str+="_______\n"
    console.log(str);
}

function jogada(linha, coluna){

    if(tabuleiro[linha][coluna]==" "){
        tabuleiro[linha][coluna]=jogador;
        jogador=(jogador=="0") ? "X" : "0";
        console.log("O proximo jogador é " + jogador);
    }else{
        console.log("Posição Ocupada. Jogue novamente!!");
    }
}

function verificaVencedor(){
    for( let i = 0; i<3 ; i++){
        if(tabuleiro[i][0]!= " " && tabuleiro [i][0] == tabuleiro[i][1] && tabuleiro [i][1] == tabuleiro[i][2])
        return true;
        if(tabuleiro[0][i]!= " " && tabuleiro [0][i] == tabuleiro[1][i] && tabuleiro [1][i] == tabuleiro[2][i])
        return true;
    }

    if(tabuleiro[0][0] != " " && tabuleiro[0][0]==tabuleiro[1][1] && tabuleiro[0][0]==tabuleiro[2][2]){
        return true;
    }
    

    if(tabuleiro[0][2] != " " && tabuleiro[0][2]==tabuleiro[1][1] && tabuleiro[0][2]==tabuleiro[2][0]){
        return true
    }
    return false;
}

let jogadas= 0;
let fim = false;

function VerificaFimJogo(){
    if(fim) return;
    if(tabuleiro[linha][coluna]==" "){
        tabuleiro[linha][coluna] = jogador;
        jogadas++;
        if(verificaVencedor()){
            console.log("O jogador " + jogador + " ganhou!");
            fim=true;
        }else if (jogadas==9){
            console.log("Deu empate técnico!");
            fim=true;
        }else{
            jogador= (jogador == "X") ? "0" : "X";
        }
    }
}

jogada(1, 1);
jogada(0, 0);
jogada(0, 0);
jogada(0, 1);
jogada(2, 0);
jogada(2, 1);
jogada(0, 2);

imprimeTabuleiro();
