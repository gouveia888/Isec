import "./assets/styles/App.css";
import { ControlPanel, Header, Footer, GamePanel } from "./components/index.js";
import {useState} from "react";
import shuffleArray from "./helpers/shuffle.js";
import { CARDS_LOGOS } from "./constants/index.js";

function App() {
  const [gameStart, setGameStart] = useState(false);
  const [selectedLevel, setSelectedLevel] = useState("0");
  const [cards, setCards] = useState([]);
  ///modal numero totall de pontos e mensagem de alerta

  function handleGameStart() {
    setGameStart(!gameStart); //nega o valor da variavel
    createPanel(selectedLevel);
  }

  function handleLevelChange(level) {
    setSelectedLevel(level);
    createPanel(level);
  }

  function createPanel(level){
    let numOfCardPairs = level === "0" ? 0 : level === "1" ? 3 : level ==="2" ? 6 :10
    
    let initialCards = shuffleArray(CARDS_LOGOS);
    initialCards = initialCards.slice(0, numOfCardPairs);
    
    const doubledCardsObjects = []; 
    initialCards.forEach((card) => {
      doubledCardsObjects.push({ id:card, name: card });
      doubledCardsObjects.push({ id: `${card}-clone`, name: card});
    });
    let doubledShuffledCardsObjects=shuffleArray(doubledCardsObjects);
    setCards(doubledShuffledCardsObjects);
  }

  return (
    <>
      <div id="container">
        <Header />
        <main className="main-content">
          <ControlPanel
            onGameStart={handleGameStart}
            gameStarted={gameStart}
            selectedLevel={selectedLevel}
            onLevelChange={handleLevelChange}        
          />
          <GamePanel  cards={cards} selectedLevel={selectedLevel}  gameStarted={gameStart}/>
        </main>
        <Footer />
      </div>
    </>
  );
}

export default App;
// Esta linha também poderia ser eliminada
// e adefinição da funsão ser substituida
// export default function App() {
