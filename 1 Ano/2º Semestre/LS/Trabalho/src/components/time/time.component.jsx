import React, { useState, useEffect } from "react";

function Time(props) {
  const gameover = props.gameover;
  const endTime = props.endTime;

  const [seconds, setSeconds] = useState(0);
  const [idInterval, setIdInterval] = useState(null);

  useEffect(() => {
    const interval = setInterval(() => {
      setSeconds((prevSeconds) => prevSeconds + 1);
    }, 1000);

    endTime(seconds);
    setIdInterval(interval);

    return () => clearInterval(interval);
  }, [seconds, endTime]);

  useEffect(() => {
    if (gameover !== "0") {
      clearInterval(idInterval);
    }
  }, [gameover, idInterval]);

  return <div>{seconds} s</div>;
}

export default Time;
