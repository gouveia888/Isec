import { useState } from "react";
import { Header, Footer, ControlPanel, GamePanel } from "./components/";
import "./assets/styles/App.css";

function App() {
  const [gameStarted, setGameStarted] = useState(false);
  const [selectedLevel, setSelectedLevel] = useState("0");
  const handleGameStart = () => setGameStarted(!gameStarted);
  const handleLevelChange = (event) => {
    const { value } = event.currentTarget;
    setSelectedLevel(value);
  };

  return (
    <div id="container">
      <Header />
      <main>
        <ControlPanel
          gameStarted={gameStarted}
          onGameStart={handleGameStart}
          selectedLevel={selectedLevel}
          onLevelChange={handleLevelChange}
        />
        <GamePanel />
      </main>
      <Footer />
    </div>
  );
}
export default App;
