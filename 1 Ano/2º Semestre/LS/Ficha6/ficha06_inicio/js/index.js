"use strict";
const panelControl = document.querySelector("#panel-control");
const panelGame = document.querySelector("#game");
const btLevel = document.querySelector("#btLevel");
const btPlay = document.querySelector("#btPlay");
const message = document.querySelector("#message");
const infoGame = panelControl.querySelectorAll(".list-item");
const labelGameTime = document.querySelector("#gameTime");
const labelPoints = document.querySelector("#points");
let cards = document.querySelectorAll(".card");
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
const TIMEOUTGAME = 20;
let timer,
  timerId,
  totalPoints = 0,
  totalFlippedCards = 0,
  flippedCards = [];

const gameOver = () => totalFlippedCards === cards.length;

const shuffleArray = (array) => {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    const temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }
};

function updatePoints(op = "+") {
  op === "+"
    ? (totalPoints += timer * (cards.length / 2))
    : totalPoints < 5
    ? (totalPoints = 0)
    : (totalPoints -= 5);
  labelPoints.textContent = totalPoints;
}
function updateGameTime() {
  timer--;
  if (timer < 10) labelGameTime.style.backgroundColor = "red";
  labelGameTime.textContent = `${timer}s`;
  if (timer === 0) stopGame();
}
function flipCard() {
  if (this.classList.contains("flipped")) return;
  this.classList.add("flipped");
  flippedCards.push(this);
  if (flippedCards.length === 2) checkPair();
}
function checkPair() {
  let [card1, card2] = flippedCards;
  const isMatch = card1.dataset.logo === card2.dataset.logo;
  if (isMatch) {
    setTimeout(() => {
      card1.classList.add("inative");
      card2.classList.add("inative");
      card1.querySelector(".card-front").classList.add("grayscale");
      card2.querySelector(".card-front").classList.add("grayscale");
      totalFlippedCards += 2;
      updatePoints("+");
      if (gameOver()) stopGame();
    }, 500);
  } else {
    setTimeout(() => {
      card1.classList.remove("flipped");
      card2.classList.remove("flipped");
      updatePoints("-");
    }, 500);
  }
  flippedCards = [];
}
function reset() {
  [btLevel.disabled, btLevel.value, btPlay.disabled, message.textContent] = [
    true,
    1,
    false,
    "",
  ];
  for (const el of infoGame) el.classList.remove("gameStarted");
  cards.forEach((card) => {
    card.classList.remove("flipped", "inative");
    card.querySelector(".card-front").classList.remove("grayscale");
    card.removeEventListener("click", flipCard);
  });

  [labelGameTime.textContent, labelPoints.textContent] = [`${TIMEOUTGAME}s`, 0];
}

const createAndShuffleCards = (array) => {
  shuffleArray(array);
  array.splice(cards.length / 2, Number.MAX_VALUE);
  array.push(...array);
  shuffleArray(array);
};

function startGame() {
  message.classList.add("hide");
  btLevel.disabled = true;
  btPlay.textContent = "Terminar Jogo";
  for (const el of infoGame) el.classList.add("gameStarted");
  let [indice, newCardLogos] = [0, [...cardsLogos]];
  createAndShuffleCards(newCardLogos);
  for (let card of cards) {
    let cardFront = card.querySelector(".card-front");
    cardFront.src = `images/${newCardLogos[indice]}.png`;
    card.dataset.logo = newCardLogos[indice++];
    card.addEventListener("click", flipCard);
  }
  [flippedCards, totalFlippedCards, totalPoints] = [[], 0, 0];
  [timer, timerId] = [TIMEOUTGAME, setInterval(updateGameTime, 1000)];
  [labelGameTime.textContent, labelPoints.textContent] = [`${timer}s`, 0];
  labelGameTime.removeAttribute("style");
}
function stopGame() {
  [btPlay.textContent, document.querySelector("#messageGameOver").textContent] =
    ["Iniciar Jogo", `Pontuação:${totalPoints}`];
  clearInterval(timerId);
  modalGameOver.showModal();
  modalGameOver.querySelector("#nickname").style = "display: none;";
  document.querySelector("#messageGameOver").textContent =
    "Pontuação: " + totalPoints;
}
btLevel.addEventListener("change", reset);
btPlay.addEventListener("click", () =>
  btPlay.textContent === "Terminar Jogo" ? stopGame() : startGame()
);
panelGame.addEventListener("click", () =>
  message.textContent === ""
    ? (message.textContent = "Clique em Iniciar o Jogo!")
    : (message.textContent = "")
);
reset();
