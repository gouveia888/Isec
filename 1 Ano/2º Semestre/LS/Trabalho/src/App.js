import "./assets/styles/App.css";
import { ControlPanel, Header, Footer, GamePanel } from "./components/index.js";
import { useState } from "react";
import { shuffleArray, finalArrayGenerator } from "./helpers/index.js";
import { BOMBS_BASICO, BOMBS_INTERMEDIO, BOMBS_AVANCADO } from "./constants";

function App() {
  const [gameStart, setGameStart] = useState(false);
  const [selectedLevel, setSelectedLevel] = useState("0");
  const [gameState, setGameState] = useState("neutral");
  const [gameover, setGameover] = useState("0");
  const [bombsFound, setBombsFound] = useState(0);
  const [finalTime, setFinalTime] = useState(0);
  const [forceTurn, setForceTurn] = useState(0);

  const [squaresArray, setSquaresArray] = useState([]);
  /*Propriedades dos elementos:
      - name (string)
          "0" - quadrícula que não contém bombas adjacentes
          "1" to "8" - quadrícula que contém "X" bombas adjacentes
          "bomb" - bomba
      - id (number)
      - isFlipped (number):
            0 - não virado
            1 - virado
            2 - virado e é uma bomba
   */

  // Tempo total decorrido
  let time;
  function endTime(t) {
    time = t;
  }

  // Conta o número de flags colocadas
  function handleBombsFound(value) {
    setBombsFound(bombsFound + value);
  }

  // Reset das variáveis de estado, preparação do mapa de jogo e inicialização
  function handleGameStart() {
    setSquaresArray([]);
    setFinalTime(0);
    setBombsFound(0);
    createPanel(selectedLevel);
    setGameState("neutral");
    setGameover("0");

    if (gameStart === true) {
      // Termina jogo
      setGameStart(false);
      setForceTurn(2);
    } else {
      // Inicia jogo
      setGameStart(true);
      setForceTurn(1);
    }
  }

  // Força a criação de um novo mapa de jogo sempre que há alteração do nível selecionado
  function handleLevelChange(event) {
    const value = event.currentTarget.value;
    setGameover("0");
    setSquaresArray([]);
    setSelectedLevel(value);
    setFinalTime(0);
    setBombsFound(0);
    setForceTurn(1);
    createPanel(value);
    setGameState("neutral");
  }

  // Cria um novo mapa de jogo (squaresArry) em função do nível selecionado
  function createPanel(level) {
    let numBombs, arraySize;
    switch (level) {
      case "1":
        numBombs = BOMBS_BASICO;
        arraySize = 9 * 9;
        break;
      case "2":
        numBombs = BOMBS_INTERMEDIO;
        arraySize = 16 * 16;
        break;
      case "3":
        numBombs = BOMBS_AVANCADO;
        arraySize = 30 * 16;
        break;
      default:
        numBombs = 10;
        arraySize = 9 * 9;
        break;
    }

    // Array temporário vazio
    let initialArray = [];

    // Preencher o array inicial
    for (let i = 0; i < arraySize; i++) {
      if (i < numBombs) {
        // Coloca número de bombas de acordo com o nível
        initialArray.push({ id: i, name: "bomb", isFlipped: "0" });
      } else {
        // Coloca restantes elementos como vazios ("0")
        initialArray.push({ id: i, name: "blank", isFlipped: "0" });
      }
    }

    // Array temporário baralhado
    let shuffledArray = shuffleArray(initialArray);

    // Matriz final
    let finalArray = finalArrayGenerator(shuffledArray, level);
    setSquaresArray(finalArray);
  }

  function handleGameOver(state) {
    /*state:
      "1" - vitória
      "2" - derrota */

    // Reset variáveis de estado
    setGameover(state);
    setGameStart(false);
    setFinalTime(time);
    setForceTurn(0);

    if (state === "1") {
      setGameState("win");
    } else {
      setGameState("lost");
    }
  }

  return (
    <div>
      <Header />
      <div id="container">
        <main className="main-content">
          <ControlPanel
            handleGameStart={handleGameStart}
            gameStarted={gameStart}
            selectedLevel={selectedLevel}
            handleLevelChange={handleLevelChange}
            gameState={gameState}
            bombsFound={bombsFound}
            gameover={gameover}
            endTime={endTime}
            finalTime={finalTime}
          />
          <GamePanel
            squares={squaresArray}
            selectedLevel={selectedLevel}
            gameStarted={gameStart}
            handleBombsFound={handleBombsFound}
            handleGameOver={handleGameOver}
            gameover={gameover}
            forceTurn={forceTurn}
          />
        </main>
      </div>
      <Footer />
    </div>
  );
}

export default App;
