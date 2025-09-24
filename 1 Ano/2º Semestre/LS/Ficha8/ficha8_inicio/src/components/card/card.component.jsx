import React from "react";
import "./card.css";
import { PLACEHOLDER_CARDBACK_PATH } from "../../constants/index.js";
import { PLACEHOLDER_CARD_PATH } from "../../constants/index.js";
import {useState, useEffect} from "react";

export default function Card({ name, gameStarted, onFlippedCards, matchedCards}) {

  const [virar, setVirar] = useState(false);
  const [matched, setMatched] = useState(false);

  let classVirar;
  virar && gameStarted ? classVirar = " flipped " : classVirar = "";

  function handleVirar(e){
    setVirar(!virar);
    if(!matched && gameStarted){
      onFlippedCards(e.currentTarget.dataset.logo);
      setMatched(e.currentTarget.dataset.logo);
    }

  }    

  return (
    <div className={"card" + classVirar} onClick={handleVirar} data-logo={name}>
      <img src={PLACEHOLDER_CARDBACK_PATH} className="card-back" />
      <img src={PLACEHOLDER_CARD_PATH + name + ".png"} className="card-front" />
    </div>
  );
}
