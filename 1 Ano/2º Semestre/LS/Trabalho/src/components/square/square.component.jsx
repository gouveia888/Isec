import React, { useEffect, useState } from "react";
import "./square.css";
import { PLACEHOLDER_SQUARE_PATH } from "../../constants";

function Square(props) {
  const gameStarted = props.gameStarted;
  const name = props.name;
  const isFlipped = props.isFlipped;
  const handleBombsFound = props.handleBombsFound;
  const handleFlip = props.handleFlip;
  const index = props.index;
  const gameover = props.gameover;
  const forceTurn = props.forceTurn;

  const [flipped, setFlipped] = useState(false);
  const [visible, setVisible] = useState("back");

  let flippedClass = ""; // Classes CSS

  if (flipped === true && name === "bomb") {
    // Background vermelho na bomba que foi clicada (fim de jogo - derrota)
    flippedClass = "flipped red";
  } else if (flipped === true) {
    // Mostra a quadrícula que foi clicada
    flippedClass = "flipped";
  } else if (isFlipped === "1" && visible === "back") {
    // Mostra a quadrícula "by force" -> usado na expansão das quadrículas vazias
    flippedClass = "flipped";
  } else if (isFlipped === "2" && visible !== "flag") {
    // Mostra as bombas sem flag (fim de jogo - derrota)
    flippedClass = "flipped";
  } else if (isFlipped === "2" && visible === "flag") {
    // Previne mostrar as bombas com flag (fim de jogo)
    flippedClass = "";
  } else if (gameover === "2" && isFlipped !== "2" && visible === "flag") {
    // Assinala as flags "erradas" <=> não são bomba (fim de jogo - derrota)
    flippedClass = "wrong";
  } else if (gameover === "1" && name === "bomb" && visible !== "flag") {
    // Coloca flags nas bombas não assinaladas (fim de jogo - vitória)
    setVisible("flag");
  }

  useEffect(() => {
    if (forceTurn !== 0) {
      setFlipped(false);
      setVisible("back");
    }
  }, [forceTurn]);

  // Left click
  function handleLeftClick() {
    if (gameStarted === true && flipped === false && visible === "back") {
      handleFlip(index, name);
      setFlipped(true);
    }
  }

  // Right click: back -> flag -> ponto-interrogação
  // Coloca a imagem correspondente
  function handleRightClick(event) {
    event.preventDefault(); // Previne "right click menu"
    if (gameStarted === true && flipped === false) {
      switch (visible) {
        case "back":      
          setVisible("flag");
          handleBombsFound(1);
          break;
        case "flag":
          setVisible("question");
          handleBombsFound(-1);
          break;
        case "question":
          setVisible("back");
          break;
        default:
          setVisible("back");
          break;
      }
    }
  }

  return (
    <div
      className={"square " + flippedClass}
      onClick={handleLeftClick}
      onContextMenu={handleRightClick}
    >
      <img
        src={`${PLACEHOLDER_SQUARE_PATH}${visible}.png`}
        alt="visible"
        className="square-visible"
      />
      <img
        src={`${PLACEHOLDER_SQUARE_PATH}${name}.png`}
        alt="squarefront"
        className="square-front"
      />
    </div>
  );
}

export default Square;
