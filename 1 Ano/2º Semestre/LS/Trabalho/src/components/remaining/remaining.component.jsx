import React from "react";
import {
  BOMBS_AVANCADO,
  BOMBS_BASICO,
  BOMBS_INTERMEDIO,
} from "../../constants";

function Remaining(props) {
  const selectedLevel = props.selectedLevel;
  const bombsFound = props.bombsFound;
  const gameover = props.gameover;

  let numBombs;
  switch (selectedLevel) {
    case "1":
      numBombs = BOMBS_BASICO;
      break;
    case "2":
      numBombs = BOMBS_INTERMEDIO;
      break;
    case "3":
      numBombs = BOMBS_AVANCADO;
      break;
    default:
      numBombs = BOMBS_BASICO;
      break;
  }

  let counter = numBombs - bombsFound;

  if (gameover === "1") {
    counter = 0;
  }

  return <div>{counter}</div>;
}

export default Remaining;
