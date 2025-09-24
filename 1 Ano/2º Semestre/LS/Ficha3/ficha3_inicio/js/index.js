"use strict";
const panelControl = document.querySelector("#panel-control");
const panelGame = document.querySelector("#game");
const btLevel = document.querySelector("#btLevel");
const btPlay = document.querySelector("#btPlay");
const message = document.querySelector("#message");
const infoGame = panelControl.querySelectorAll(".list-item");
// ------------------------
// Funções Genéricas
// ------------------------
function reset() {
  message.classList.remove("hide");
  btPlay.disabled = btLevel.value === "0" ? true : false;
  panelGame.style.display = btLevel.value === "0" ? "none" : "grid";
  for (const el of infoGame) el.classList.remove("gameStarted");
}

function startGame() {
  message.classList.add("hide");
  btLevel.disabled = true;
  btPlay.textContent = "Terminar Jogo";
  for (const el of infoGame) el.classList.add("gameStarted");
}
function stopGame() {
  btPlay.textContent = "Iniciar Jogo";
  btLevel.disabled = false;
  reset();
}

// ------------------------
// Event Listeners
// ------------------------
btLevel.addEventListener("change", reset);
btPlay.addEventListener("click", () =>
  btPlay.textContent === "Terminar Jogo" ? stopGame() : startGame()
);

reset();
