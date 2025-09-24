import "./assets/styles/App.css";
import Header from "./components/header/header.component";
import Footer from "./components/footer/footer.component";
import GamePanel from "./components/game-panel/game-panel.component";
import ControlPanel from "./components/control-panel/control-panel.component";

function App() {
  return (
    <div id="container">
      <Header />
      <main>
        <ControlPanel />
        <GamePanel />
      </main>
      <Footer />
    </div>
  );
}
export default App;
