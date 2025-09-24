"use strict";

const panelControl = document.querySelector("#panel-control");
const panelGame = document.querySelector("#game");
const btLevel = document.querySelector("#btLevel");
const btPlay = document.querySelector("#btPlay");
const message = document.querySelector("#message");
const listItems = document.querySelectorAll(".list-item");
let cards = panelGame.querySelectorAll(".card");
let cardsLogos=["angular","bootstrap","html","javascript","vue","svelte","react","backbone","ember","css"];
let flippedCards;
let totalFlippedCards;
let cartas=0;
cartas=dificuldade(btLevel.value);
const TIMEOUTGAMEBASICO=20;
const TIMEOUTGAMEINTERMEDIO = 60;
const TIMEOUTGAMEAVANCADO = 180;

let labelGameTime = document.querySelector("#gameTime") 
let timer;
let timerId;
btLevel.addEventListener("change",reset);

let labelPoints = document.querySelector("#points");
let totalPoints;
let cartasfinal;

panelGame.innerHTML = '';

let topGamers = [
    { nickname: 'Ze', points: 831 },
    { nickname: 'Maria', points: 321 }
     ]

btPlay.addEventListener("click", function(){

    if(btPlay.textContent=="Terminar Jogo")
        stopGame(); 
    else
        startGame();
    
});

panelGame.addEventListener("click",function(){

    if(message.textContent=="")
        message.textContent="Clique em Iniciar o Jogo!";
    else 
        message.textContent=="";

})
       

function reset(){
    panelGame.style.display="none";
    message.textContent='';
    message.classList.remove('hide');
    panelGame.innerHTML = '';
    
    //btPlay.disabled=true;
    btLevel.value === "0" ? btPlay.disabled=true : btPlay.disabled=false; panelGame.style.display='grid';  
    /*
    if(btLevel.value=="0")
        btPlay.disabled=true;
    else{
        btPlay.disabled=false;
        panelGame.style.display='grid';     
    }*/

    for(let item of listItems){
        item.classList.remove('gameStarted');
        item.classList.remove('flipped')
    }   

    labelGameTime.removeAttribute("style")
    totalPoints=0;
    
    createPanelGame();
}

reset();

function startGame(){    

    flippedCards=[]
    totalFlippedCards=0;
    btLevel.disabled=true;
    btPlay.textContent = "Terminar Jogo";
    message.classList.add("hide");

    for(let item of listItems){
        item.classList.add('gameStarted');
    }
    
    let index = 0;  
     

    //showCards(); alinea 3a
    //console.table(cardsLogos)
    shuffleArray(cardsLogos);
    //console.table(cardsLogos)

   /* for (const card of cards) { //vai iterar cada elemento do array cards
        card.dataset.logo=`${cardsLogos[index]}`
        let imgFront=card.querySelector(".card-front");
        imgFront.src=`images/${cardsLogos[index++]}.png`;
    }*/
    
    let newCardsLogos = cardsLogos.slice(0, cartas/2);
    //let newCardsLogos = cards.length/2;
    newCardsLogos.push(...newCardsLogos);
    shuffleArray(newCardsLogos);

    cartasfinal=newCardsLogos;
    console.log(cartasfinal);

    for (const card of cards) { //vai iterar cada elemento do array cards
        card.dataset.logo=`${newCardsLogos[index]}`
        let imgFront=card.querySelector(".card-front");
        imgFront.src=`images/${newCardsLogos[index++]}.png`;
        card.addEventListener("click",flipcard, { once: true });
        card.addEventListener("mouseover", function(){this.classList.add("cardHover")})
        card.addEventListener("mouseout", (event) => {event.currentTarget.classList.remove("cardHover")})
    }

    timer = getTimer(btLevel.value)
    labelGameTime.textContent=`${timer}s`
    timerId = setInterval(updateGameTime,1000)

}

function stopGame(){

    btPlay.textContent="Iniciar Jogo"
    btLevel.disabled=false
    hideCards();
    modalGameOver.showModal();
    clearInterval(timerId);
    for (const item of cards) {
        item.classList.remove("grayscale")
        item.classList.remove('inative')
    }
    let mensagem = document.querySelector("#messageGameOver");
    let nickname = document.querySelector("#nickname");
    mensagem.textContent=totalPoints;
    nickname.style.display="none"
    panelGame.innerHTML = '';
}

function showCards(){

    for(let item of cards)
        item.classList.add('flipped')

}

function hideCards(){

    for(let item of cards)
       item.classList.remove('flipped')
}
  

// Algoritmo Fisher-Yates - Algoritmo que baralha um array.
const shuffleArray = array => {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        const temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

function flipcard(){
    this.classList.add("flipped")
    flippedCards.push(this);
    //console.log(flippedCards)
    flippedCards.length == 2 ? checkPair() : 0
    
}

function checkPair(){
    if(flippedCards[0].dataset.logo===flippedCards[1].dataset.logo){
        for (const card of flippedCards) {
            card.classList.add("inative")
            totalFlippedCards+=1;
            let carta = card.children
            setTimeout(function(){
                carta[1].classList.add("grayscale")
                if(gameOver()==true)
                    stopGame();
            },500)
            //console.log(totalFlippedCards);                
        }
        updatePoints('+')
    }
    else{
        for (const card of flippedCards) {
            setTimeout(function() {
                card.classList.remove("flipped")
                card.addEventListener('click', flipcard, { once: true });
              }, 500);
        }
        updatePoints('-')
    }
        
    flippedCards=[]
    
}

function gameOver(){
    let valor;
    totalFlippedCards === cartas ? valor = true : valor = false
    return valor;
}

function updateGameTime(){
    timer-=1
    labelGameTime.textContent=`${timer}s`
    if(timer==2)
        stopGame();
    if(timer<11)
        labelGameTime.style.backgroundColor="red";
}

function getTimer(nivel){
    switch(nivel){
        case '1':nivel=TIMEOUTGAMEBASICO; 
                break;
        case '2':nivel=TIMEOUTGAMEINTERMEDIO;
                break;
        case '3':nivel=TIMEOUTGAMEAVANCADO; 
                break;
    }
    return nivel;
}

function updatePoints(operador){
    if(operador=='-')
        totalPoints-=5;
    else if(operador=='+'){
        totalPoints=totalPoints+(timer*cartas);
    }
    if(totalPoints<0)
        totalPoints=0;
    labelPoints.textContent=`${totalPoints}`
}

function createPanelGame(){
    panelGame.className = '';
    panelGame.innerHTML = '';
    let newDiv = document.createElement('div');
    newDiv.setAttribute('class', 'card');
    let imgBack = document.createElement('img');
    imgBack.setAttribute('class', 'card-back');
    imgBack.setAttribute('src', 'images/ls.png');
    let imgFront = document.createElement('img');
    imgFront.setAttribute('class', 'card-front');
    newDiv.appendChild(imgBack);
    newDiv.appendChild(imgFront);
    panelGame.appendChild(newDiv);
    let num= btLevel.value;
    if(num==1)
        {
            panelGame.setAttribute('class','')
            cartas=6;
        }
        else 
        {
            if(num == 2)
            {
                panelGame.setAttribute('class', 'intermedio')
                cartas=12;
            }
            else 
            {
                if(num == 3)
                {
                    panelGame.setAttribute('class','avancado');
                    cartas=20;
                }
            }else{
                cartas=0;
            }
            
        }
    

    for (let i = 1; i < cartas; i++) {
        let element = newDiv.cloneNode(true);    
        panelGame.appendChild(element);
        
        
        //cria o numero de cartas, cada carta newDiv, de acordo com o nivel 
        //element.setAttribute('data-logo', `${cartasfinal[i]}`);
        //element.imgFront.setAttribute('src', `images/${cartasfinal[i]}.png`);
        //console.log(`${cartasfinal[i]}`);
        //panelGame.appendChild(element);            //adiciona a carta ao panelGame
    }

    cards = panelGame.childNodes;
    
}

 function dificuldade(nivel){
     switch(nivel){
         case '1':nivel=6; 
                 break;
         case '2':nivel=12;
                 break;
         case '3':nivel=20; 
                 break;
     }
    return nivel;
 }

function getTop10(){
    let infoTop = document.getElementById('infoTop');
    let jogador = "";
    infoTop.textContent = "";

    for (const player of topGamers) {
       infoTop.innerHTML= player.nickname + " " + player.points;
    }

    jogador=infoTop.innerHTML;
        console.log(jogador);
}