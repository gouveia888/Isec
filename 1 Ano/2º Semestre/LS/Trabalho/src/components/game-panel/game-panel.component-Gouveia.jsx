import React, { useState } from "react";
import "./game-panel.css";
import { Square } from "../index";
import {
  BOMBS_AVANCADO,
  BOMBS_BASICO,
  BOMBS_INTERMEDIO,
} from "../../constants";
import { checkNeighbours } from "../../helpers";

function GamePanel(props) {
  const squaresArray = props.squares;
  const selectedLevel = props.selectedLevel;
  const gameStarted = props.gameStarted;
  const handleBombsFound = props.handleBombsFound;
  const handleGameOver = props.handleGameOver;
  const gameover = props.gameover;
  const forceTurn = props.forceTurn;
  const desafio = props.desafio;

  // Classe CSS do mapa de jogo
  let gamePanelGenerator =
    selectedLevel === "3"
      ? "avancado"
      : selectedLevel === "2"
      ? "intermedio"
      : "";

  let width, height, numBombs;

  switch (selectedLevel) {
    case "1":
      numBombs = BOMBS_BASICO;
      width = height = 9;
      break;
    case "2":
      numBombs = BOMBS_INTERMEDIO;
      width = height = 16;
      break;
    case "3":
      numBombs = BOMBS_AVANCADO;
      width = 30;
      height = 16;
      break;
    default:
      numBombs = BOMBS_BASICO;
      width = height = 9;
      break;
  }

  const [click, setClick] = useState(0);

  // Verifica que tipo de quadrícula foi clicada
  function handleFlip(index, name) {
    if (name === "bomb") {
      // Percorre o array e coloca isFlipped = "2" em todas as bombas
      for (let i = 0; i < height; i++) {
        for (let j = 0; j < width; j++) {
          const aux = squaresArray[i][j];
          if (aux["name"] === "bomb") {
            squaresArray[i][j]["isFlipped"] = "2";
          }
        }
      }
      handleGameOver("2"); // Fim de jogo (derrota)
    } else {
      let row, column;
      for (let i = 0; i < height; i++) {
        for (let j = 0; j < width; j++) {
          const aux = squaresArray[i][j];
          if (aux["id"] === index) {
            row = i;
            column = j;
          }
        }
      }
      squaresArray[row][column]["isFlipped"] = "1";

      if (name === "0") {
        // Chama a função que verifica as quadrículas vizinhas e as mostra
        checkNeighbours(squaresArray, width, height, row, column);
        setClick(click + 1);
      }

      countFlipped();
    }
  }

  // Percorre o array e conta quantas quadrículas estão viradas
  function countFlipped() {
    let flippedCount = 0;
    for (let i = 0; i < height; i++) {
      for (let j = 0; j < width; j++) {
        if (squaresArray[i][j]["isFlipped"] === "1") {
          flippedCount++;
        }
      }
    }
      console.log(squaresArray)
    // Verifica se o jogo termina (vitória)
    if (flippedCount === width * height - numBombs) {
      handleGameOver("1"); // Fim de jogo (vitória)
    }
  }

  return (
    <div id="game-panel" className={gamePanelGenerator}>
      {squaresArray.map((row) =>
        row.map((square) => (
          <Square
            key={square.id}
            index={square.id}
            name={square.name}
            isFlipped={square.isFlipped}
            gameStarted={gameStarted}
            handleBombsFound={handleBombsFound}
            handleFlip={handleFlip}
            gameover={gameover}
            forceTurn={forceTurn}
          ></Square>
        ))
      )}
    </div>
  );
}

export default GamePanel;
