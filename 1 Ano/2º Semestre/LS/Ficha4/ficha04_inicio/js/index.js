"use strict";
const panelControl = document.querySelector("#panel-control");
const panelGame = document.querySelector("#game");
const btLevel = document.querySelector("#btLevel");
const btPlay = document.querySelector("#btPlay");
const message = document.querySelector("#message");
const infoGame = panelControl.querySelectorAll(".list-item");
const elementos = panelControl.querySelectorAll(".list-item");
let cardsLogos = [
  "angular",
  "bootstrap",
  "html",
  "javascript",
  "vue",
  "svelte",
  "react",
  "css",
  "backbone",
  "ember",
];
let cards = document.querySelectorAll(".card");

// ------------------------
// Funções Genéricas
// ------------------------
// Algoritmo Fisher-Yates -  Algoritmo que baralha um array.
const shuffleArray = (array) => {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    const temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }
};

//----------------------------------
function reset() {
  btLevel.disabled = true;
  btLevel.value = 1;
  btPlay.disabled = false;

  panelGame.style.display = "none";
  message.textContent = "";
  message.classList.remove("hide");
  panelGame.style.display = "grid";

  elementos.forEach((elemento) => elemento.classList.remove("gameStarted"));
}

function flipCard() {
  this.classList.add("flipped");
}

function startGame() {
  btLevel.disabled = true;
  btPlay.textContent = "Terminar Jogo";
  message.classList.add("hide");
  elementos.forEach((elemento) => elemento.classList.add("gameStarted"));

  shuffleArray(cardsLogos);
  let [indice, newCardLogos] = [0, cardsLogos.slice(0, cards.length / 2)];
  newCardLogos = [...newCardLogos, ...newCardLogos];
  shuffleArray(newCardLogos);

  for (let card of cards) {
    card.querySelector(
      ".card-front"
    ).src = `images/${newCardLogos[indice]}.png`;
    card.dataset.logo = newCardLogos[indice++];
    card.addEventListener("click", flipCard);
  }
}
function stopGame() {
  btPlay.textContent = "Iniciar Jogo";
  reset();
}

// ------------------------
// Event Listeners
// ------------------------
btLevel.addEventListener("change", reset);
btPlay.addEventListener("click", (e) => {
  e.target.textContent === "Terminar Jogo" ? stopGame() : startGame();
});

panelGame.addEventListener(
  "click",
  () =>
    (message.textContent =
      message.textContent === "" ? "Clique em Iniciar o Jogo!" : "")
);

reset();
