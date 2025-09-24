import React, { useState, useEffect} from "react";
import "./game-panel.css";
import { Card } from "../index";

export default function GamePanel({cards, selectedLevel,  gameStarted, onFlippedCards,  onGameStart}) {
   let gameClasse = selectedLevel === "2" ? "intermedio" : selectedLevel === "3" ? "avancado" : " ";
   let flippedCards=[];

    const[matchedCards, setMatchedCards] = useState([]);

    const processMatchingCards = () => {
        const [firstCard, secondCard] = flippedCards;
    
        if (firstCard.value === secondCard.value) {
          setMatchedCards((prevMatchedCards) => [...prevMatchedCards, firstCard, secondCard]);
        } else {
          // Apenas para forçar nova renderização
          setMatchedCards((prevMatchedCards) => [...prevMatchedCards]);
        }
    
        setTimeout(() => {
          flippedCards = [];
        }, 1000); // Tempo de espera antes de virar as cartas de volta ou deixá-las viradas se forem iguais
      };

   function handleFlippedCards(name){
        flippedCards.push(name);
        if(flippedCards.length==2)
            processMatchingCards();
   };

   useEffect(() => { 
        if (matchedCards.length === cards.length && gameStarted) {   
            onGameStart(); 
            matchedCards=[];
        } 
    }, [matchedCards, cards, gameStarted, onGameStart]); 

    return (
        <section id="panel-game">
            <div id="game" className={gameClasse}>
                {cards.map((name) => 
                    <Card 
                        key={name.id} 
                        name={name.name} 
                        gameStarted={gameStarted} 
                        onFlippedCards={handleFlippedCards}
                        //matchedCards={matchedCards}
                      />
                )}
            </div>
        </section>
    );
  }
    