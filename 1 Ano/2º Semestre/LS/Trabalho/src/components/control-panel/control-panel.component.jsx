import React from "react";
import "./control-panel.css";
import { Time, Remaining } from "../index";
import { PLACEHOLDER_SQUARE_PATH } from "../../constants";

function ControlPanel(props) {
  const handleGameStart = props.handleGameStart;
  const gameStarted = props.gameStarted;
  const selectedLevel = props.selectedLevel;
  const handleLevelChange = props.handleLevelChange;
  const gameState = props.gameState;
  const bombsFound = props.bombsFound;
  const gameover = props.gameover;
  const endTime = props.endTime;
  const finalTime = props.finalTime;

  // Classe CSS que esconde/mostra: Timer, Emoji, Nº bombas
  let classDL = gameStarted
    ? " gameStarted "
    : selectedLevel !== "0"
    ? "gameStarted"
    : "";

  return (
    <section id="control-panel">
      <form className="form">
        <fieldset className="form-group">
          <select
            className="form-select"
            id="btLevel"
            defaultValue={selectedLevel}
            disabled={gameStarted}
            onChange={handleLevelChange}
          >
            <option value="0" disabled>
              Selecione o nível
            </option>
            <option value="1">Básico</option>
            <option value="2">Intermédio</option>
            <option value="3">Avançado</option>
          </select>
        </fieldset>
        <button
          type="button"
          id="btPlay"
          onClick={handleGameStart}
          disabled={selectedLevel === "0" ? true : false}
        >
          {gameStarted ? "Terminar Jogo" : "Iniciar Jogo"}
        </button>
      </form>
      <div className="form-metadata">
        <dl className={"list-item " + classDL}>
          <dt>
            <img src="/assets/images/bomb.png" alt="bomb"></img>
          </dt>
          <dd id="bombs">
            {
              <Remaining
                selectedLevel={selectedLevel}
                bombsFound={bombsFound}
                gameover={gameover}
              />
            }
          </dd>
        </dl>
        <dl className={"list-item " + classDL}>
          <dt className="emoji">
            <img
              src={`${PLACEHOLDER_SQUARE_PATH}${gameState}.png`}
              id="emoji"
              alt="gamestatus"
            />
          </dt>{" "}
        </dl>
        <dl className={"list-item " + classDL}>
          <dt>
            <img src="/assets/images/timer.png" alt="timer"></img>
          </dt>
          <dd id="gameTime">
            {gameStarted ? (<Time gameover={gameover} endTime={endTime} />) : (`${finalTime} s`)}
          </dd>
        </dl>
      </div>
    </section>
  );
}

export default ControlPanel;
